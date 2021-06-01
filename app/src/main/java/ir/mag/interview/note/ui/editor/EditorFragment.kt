package ir.mag.interview.note.ui.editor

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.databinding.FragmentNoteEditorBinding
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.NotesViewModel
import javax.inject.Inject


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

    private val viewModel: NotesViewModel by viewModels {
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


    }

    companion object {
        private const val TAG = "Ui.NoteEditorFragment";
    }
}