package ir.mag.interview.note.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import ir.mag.interview.note.data.model.NoteRepository
import javax.inject.Inject


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ViewModel.Notes"
    }

    fun test() {
        Log.d(TAG, "Done")
    }

}