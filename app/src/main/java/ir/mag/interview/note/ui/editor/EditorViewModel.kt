package ir.mag.interview.note.ui.editor

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.repository.NotesDatabaseRepository
import java.util.*
import javax.inject.Inject


class EditorViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository,
    private val notesDB: NotesDatabaseRepository
) : ViewModel() {

    var mode: LiveData<NoteRepository.Modes> = noteRepository.mode
    var currentNote: LiveData<Note> = noteRepository.currentNote
    var editedNote: Note = Note(0, 0, "", "", Date())

    fun goBackToBrowser() {
        noteRepository.changeMode(NoteRepository.Modes.BROWSER)
    }

    fun postGoBackToBrowser() {
        noteRepository.postChangeMode(NoteRepository.Modes.BROWSER)
    }

    suspend fun updateNewNote() {
        editedNote.date = Date()
        notesDB.updateNote(editedNote)
    }

    suspend fun deleteNote(note: Note) {
        notesDB.deleteNote(note)
    }

}