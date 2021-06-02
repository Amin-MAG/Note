package ir.mag.interview.note.ui.editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.database.entity.folder.Folder
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
    var currentFolder: LiveData<Folder> = noteRepository.currentFolder
    var editedNote: MutableLiveData<Note> = noteRepository.editedNote

    fun goBackToBrowserMode() {
        noteRepository.changeMode(NoteRepository.Modes.BROWSER)
    }

    fun postGoBackToBrowser() {
        noteRepository.postChangeMode(NoteRepository.Modes.BROWSER)
    }

    suspend fun updateNewNote() {
        noteRepository.editedNote.value?.let {
            it.lastUpdateDate = Date()
            Log.d(TAG, "updateNewNote: ${it.content}")
            notesDB.updateNote(it)
        }
    }

    suspend fun deleteNote(note: Note) {
        notesDB.deleteNote(note)
    }

    fun goBackToInFolderBrowserMode() {
        noteRepository.changeMode(NoteRepository.Modes.IN_FOLDER_BROWSING)
    }

    fun postGoBackToInFolderBrowserMode() {
        noteRepository.postChangeMode(NoteRepository.Modes.IN_FOLDER_BROWSING)
    }

    companion object {
        const val TAG = "ViewModel.Editor"
    }
}