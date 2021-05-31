package ir.mag.interview.note.ui.main

import android.content.Context
import android.os.Bundle
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
import ir.mag.interview.note.databinding.FragmentNotesBinding
import ir.mag.interview.note.ui.NotesMainActivity
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
        viewModel.notes.observe(this, Observer {
            it?.let {
                Log.d(TAG, "setupUI observe files: $it")
                filesRecyclerAdapter.files = it
                filesRecyclerAdapter.notifyDataSetChanged()
            }
        })

    }

    companion object {
        private const val TAG = "Ui.NotesFragment";
    }
}