package ir.mag.interview.note.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.R
import ir.mag.interview.note.databinding.HeaderEditorActionBarBinding
import ir.mag.interview.note.databinding.HeaderInFolderActionBarBinding
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.NotesMainViewModel
import ir.mag.interview.note.ui.editor.EditorHeaderFragment
import ir.mag.interview.note.ui.editor.EditorViewModel
import javax.inject.Inject

class InFolderHeaderFragment
@Inject
constructor(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel: NotesMainViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: HeaderInFolderActionBarBinding

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
            R.layout.header_in_folder_action_bar,
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
        private const val TAG = "Ui.InFolderHeader";
    }
}