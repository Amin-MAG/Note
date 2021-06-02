package ir.mag.interview.note.ui.editor

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.databinding.FragmentNoteEditorBinding
import ir.mag.interview.note.databinding.FragmentNoteEditorBindingImpl
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.NotesViewModel
import javax.inject.Inject
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 * Use the [EditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditorFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel: EditorViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: FragmentNoteEditorBinding


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
            R.layout.fragment_note_editor,
            container,
            false
        )
        binding.lifecycleOwner = this

        setupUI()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupUI() {
        // NOTE: think about it later
        viewModel.currentNote.observe(this, Observer {
            it?.let {
                binding.noteEditorTitle.text = SpannableStringBuilder(it.title)
                binding.noteEditorContent.text = SpannableStringBuilder(it.content)
                viewModel.editedNote = it
            }
        })

        binding.noteEditorTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val editedNote = viewModel.editedNote
                editedNote.title = s.toString()
                viewModel.editedNote = editedNote
            }
        })

        binding.noteEditorContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val editedNote = viewModel.editedNote
                editedNote.content = s.toString()
                viewModel.editedNote = editedNote
            }
        })
    }

    companion object {
        private const val TAG = "Ui.NoteEditorFragment";
    }
}