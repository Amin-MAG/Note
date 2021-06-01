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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.databinding.FragmentNotesBinding
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.dialog.CommonDialog
import ir.mag.interview.note.ui.main.recycler.adapter.FilesRecyclerAdapter
import javax.inject.Inject

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

        setupUI()

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun setupUI() {
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
//            viewModel.addUntitledNote()
        }
        binding.fabNewFolder.setOnClickListener {
            CommonDialog.Builder(this)
                .setTitle("پوشه جدید")
                .setDescription("برای پوشه خود عنوان بنویسید.")
                .setConfirmText("ایجاد پوشه")
                .setListener(object : CommonDialog.OnHandle {
                    override fun onCancel(dialog: AlertDialog) {
                        dialog.dismiss()
                    }

                    override fun onConfirm(dialog: AlertDialog) {
                        dialog.dismiss()
                    }
                })
                .setHasPrompt(true)
                .setPromptHint("عنوان پوشه")
                .build()
                .show()
//            viewModel.addUntitledFolder()
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
}