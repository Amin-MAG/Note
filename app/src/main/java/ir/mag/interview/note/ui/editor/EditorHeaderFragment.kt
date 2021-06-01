package ir.mag.interview.note.ui.editor

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.databinding.FragmentNotesBinding
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.NotesViewModel
import ir.mag.interview.note.ui.main.recycler.adapter.FilesRecyclerAdapter
import javax.inject.Inject

class EditorHeaderFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel: NotesViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as NotesMainActivity).notesComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.editor_action_bar, container, false)
    }

    companion object {
        private const val TAG = "Ui.NotesHeader";
    }
}