package ir.mag.interview.note.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.databinding.FragmentDialogCommonBinding
import ir.mag.interview.note.databinding.FragmentNotesBinding
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.dialog.CommonDialog
import ir.mag.interview.note.ui.main.recycler.adapter.FilesRecyclerAdapter
import javax.inject.Inject
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel: NotesViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentNotesBinding

    @Inject
    lateinit var filesRecyclerAdapter: FilesRecyclerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as NotesMainActivity).notesComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_notes,
            container,
            false
        )
        binding.lifecycleOwner = this

        observe()
        setupUI()

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun observe() {
        if (viewModel.currentFolder.value == null) {
            viewModel.getRootFolder().observeOnce(this, Observer {
                it?.let { folder ->
                    Log.d(
                        TAG,
                        "onCreateView current folder changed root founded: ${folder.folderId}"
                    )
                    viewModel.changeFolder(folder)
                    viewModel.setCurrentFilesSources()
                }
            })
        }
    }


    private fun setupUI() {
        viewModel.currentFolder.observe(this, Observer {
            it?.let { folder ->
                Log.d(TAG, "onCreateView current folder changed: ${folder.folderId}")

                if (folder.folderId == NoteRepository.ROOT_FOLDER_ID) {
                    viewModel.changeModeToNormalBrowsing()
                } else {
                    viewModel.changeModeToInFolderBrowsing()
                }

                viewModel.setCurrentFilesSources()
            }
        })


        // set adapter for files recycler and observe database
        binding.notesFilesList.adapter = filesRecyclerAdapter
        viewModel.currentFiles.observe(this, Observer {
            val newFiles = ArrayList<File>()
            it?.get(File.Types.FOLDER)?.let { list ->
                newFiles.addAll(list)
            }
            it?.get(File.Types.NOTE)?.let { list ->
                newFiles.addAll(list)
            }

            logFiles(newFiles)

            filesRecyclerAdapter.files = newFiles
            filesRecyclerAdapter.notifyDataSetChanged()
        })


        // floating action button
        binding.fabNewNote.setOnClickListener {
            viewModel.addNote(resources.getString(R.string.untitled))
            viewModel.currentNote.observeOnce(this, Observer {
                it?.let {
                    viewModel.goToEditPage(it)
                }
            })
        }

        binding.fabNewFolder.setOnClickListener {
            CommonDialog.Builder(this)
                .setTitle("پوشه جدید")
                .setDescription("برای پوشه خود عنوان بنویسید.")
                .setConfirmText("ایجاد پوشه")
                .setListener(object : CommonDialog.OnHandle {
                    override fun onCancel(
                        dialog: AlertDialog,
                        dialogBinding: FragmentDialogCommonBinding
                    ) {
                        dialog.dismiss()
                    }

                    override fun onConfirm(
                        dialog: AlertDialog,
                        dialogBinding: FragmentDialogCommonBinding
                    ) {
                        Log.d(TAG, "onConfirm: ${dialogBinding.commonDialogTextField.text}")
                        viewModel.addFolder(dialogBinding.commonDialogTextField.text.toString())
                        dialog.dismiss()
                    }
                })
                .setHasPrompt(true)
                .setPromptHint("عنوان پوشه")
                .build()
                .show()
        }
    }

    private fun logFiles(newFiles: ArrayList<File>) {
        for (file in newFiles) {
            when (file.type) {
                File.Types.NOTE -> {
                    val note = file as Note
                    Log.d(
                        TAG,
                        "setupUI: ${file.type} ${note.noteId} ${note.folderId} ${note.title}"
                    )
                }

                File.Types.FOLDER -> {
                    val folder = file as Folder
                    Log.d(
                        TAG,
                        "setupUI: ${file.type} ${folder.folderId} ${folder.parentFolderId} ${folder.name}"
                    )
                }

                else -> {
                }
            }
        }
    }

    companion object {
        private const val TAG = "Ui.NotesFragment";
    }


    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}