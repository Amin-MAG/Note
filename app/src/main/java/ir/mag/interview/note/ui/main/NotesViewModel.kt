package ir.mag.interview.note.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mag.interview.note.data.model.Note
import ir.mag.interview.note.data.model.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ViewModel.Notes"
    }

    val notes: LiveData<List<Note>> = noteRepository.readAllData

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addNote(note)
        }
    }

    fun test() {
        Log.d(TAG, "Done")
    }

}