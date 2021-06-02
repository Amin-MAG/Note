package ir.mag.interview.note.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
        BROWSER,
        IN_FOLDER_BROWSING
    }

    var mode: MutableLiveData<Modes> = MutableLiveData(Modes.BROWSER)
        private set

    var currentNote: MutableLiveData<Note> = MutableLiveData()
        private set

    var currentFolder: MutableLiveData<Folder> = MutableLiveData()
        private set

    var editedNote: MutableLiveData<Note> = MutableLiveData()

    fun changeMode(newMode: Modes) {
        Log.d(TAG, "changeMode: $newMode")
        mode.value = newMode
    }

    fun postChangeMode(newMode: Modes) {
        Log.d(TAG, "postChangeMode: $newMode")
        mode.postValue(newMode)
    }

    fun changeCurrentNote(note: Note) {
        currentNote.value = note
    }

    fun postChangeCurrentFolder(folder: Folder) {
        Log.d(TAG, "postChangeCurrentFolder: ${folder.folderId}")
        currentFolder.postValue(folder)
    }

    fun changeCurrentFolder(folder: Folder) {
        Log.d(TAG, "changeCurrentFolder: ${folder.folderId}")
        currentFolder.value = folder
    }

    companion object {
        private const val TAG = "Repository.Notes"
        const val ROOT_FOLDER_ID = 1L
    }
}