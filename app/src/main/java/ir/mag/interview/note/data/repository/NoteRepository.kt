package ir.mag.interview.note.data.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.ui.main.NotesViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository
@Inject
constructor() {

    enum class Modes {
        EDITOR,
        BROWSER
    }

    var mode: MutableLiveData<Modes> = MutableLiveData(Modes.BROWSER)
        private set

    var currentNoteId: MutableLiveData<Long> = MutableLiveData()
        private set

    var currentFolderId: MutableLiveData<Long> = MutableLiveData(ROOT_FOLDER_ID)
        private set

    fun changeMode(newMode: Modes) {
        mode.postValue(newMode)
    }

    fun changeCurrentNote(noteId: Long) {
        currentNoteId.postValue(noteId)
    }

    companion object {
        private const val TAG = "Repository.Notes"
        private const val ROOT_FOLDER_ID = 1L
    }
}