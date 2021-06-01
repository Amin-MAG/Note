package ir.mag.interview.note.data.repository

import androidx.lifecycle.MutableLiveData
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
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

    var currentNote: MutableLiveData<Note> = MutableLiveData()
        private set

    var currentFolder: MutableLiveData<Folder> = MutableLiveData()
        private set

    fun changeMode(newMode: Modes) {
        mode.postValue(newMode)
    }

    fun changeCurrentNote(note: Note) {
        currentNote.postValue(note)
    }

    fun changeCurrentFolder(folder: Folder) {
        currentFolder.value = folder
    }

    companion object {
        private const val TAG = "Repository.Notes"
        private const val ROOT_FOLDER_ID = 1L
    }
}