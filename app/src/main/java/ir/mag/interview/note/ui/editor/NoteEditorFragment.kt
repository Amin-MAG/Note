package ir.mag.interview.note.ui.editor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mag.interview.note.R


/**
 * A simple [Fragment] subclass.
 * Use the [NoteEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteEditorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_editor, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment [NoteEditorFragment].
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoteEditorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}