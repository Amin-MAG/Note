package ir.mag.interview.note.ui.main

import android.icu.text.CaseMap
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.*
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.relation.FolderWithNotes
import ir.mag.interview.note.database.relation.FolderWithSubFolders
import ir.mag.interview.note.database.repository.NotesDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.log


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository,
    private val notesDB: NotesDatabaseRepository
) : ViewModel() {


    var currentFolder: LiveData<Folder> = noteRepository.currentFolder
    var currentNote: LiveData<Note> = noteRepository.currentNote
    var currentFiles: MediatorLiveData<EnumMap<File.Types, List<File>>> = MediatorLiveData()

    fun changeFolder(folder: Folder) {
        noteRepository.changeCurrentFolder(folder)
    }

    fun setCurrentFilesSources() {
        currentFiles.value = EnumMap(File.Types::class.java)
        currentFiles.value?.put(File.Types.FOLDER, ArrayList())
        currentFiles.value?.put(File.Types.NOTE, ArrayList())

        currentFiles.value?.let {

            currentFiles.addSource(getCurrentNotes()) { folder ->
                folder?.notes?.let { notes ->
                    it[File.Types.NOTE] = notes
                    currentFiles.value = it
                }
            }

            currentFiles.addSource(getCurrentSubFolders()) { folder ->
                folder?.subFolders?.let { folders ->
                    it[File.Types.FOLDER] = folders
                    currentFiles.value = it
                }
            }
        }
    }

    fun goToEditPage(note: Note) {
        noteRepository.changeCurrentNote(note)
        noteRepository.changeMode(NoteRepository.Modes.EDITOR)
    }

    private fun getCurrentNotes(): LiveData<FolderWithNotes> {
        return getFolderByIdWithNotes(currentFolder.value!!.folderId)
    }

    private fun getFolderByIdWithNotes(folderId: Long): LiveData<FolderWithNotes> {
        return notesDB.getFolderByIdWithNotes(folderId)
    }

    private fun getCurrentSubFolders(): LiveData<FolderWithSubFolders> {
        return getFolderByIdWithSubFolders(currentFolder.value!!.folderId)
    }

    private fun getFolderByIdWithSubFolders(folderId: Long): LiveData<FolderWithSubFolders> {
        return notesDB.getFolderSubFolders(folderId)
    }

    fun getRootFolder(): LiveData<Folder> {
        return notesDB.getFolderById(NoteRepository.ROOT_FOLDER_ID)
    }

    fun getParentFolder(): LiveData<Folder> {
        return notesDB.getFolderById(currentFolder.value!!.parentFolderId!!)
    }

    fun addFolder(folderName: String) {
        currentFolder.value?.let { folder ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                Log.d(TAG, "addFolder: ${currentFolder.value!!.folderId}")
                Log.d(TAG, "addFolder: ${currentFolder.value!!.parentFolderId}")
                Log.d(TAG, "addFolder: ${currentFolder.value!!.name}")
                Log.d(TAG, "addFolder: ----------------------------------")
                notesDB.addFolder(Folder(0, folder.folderId, folderName))
            }
        }
    }

    fun addNote(noteName: String) {
        currentFolder.value?.let { folder ->
            viewModelScope.launch(Dispatchers.IO + NonCancellable) {
                val id = notesDB.addNote(Note(0, folder.folderId, noteName, "", Date()))
                val note = notesDB.getNoteByIdNow(id)
                noteRepository.currentNote.postValue(note)
            }
        }
    }

    suspend fun deleteFolder(folder: Folder) {
        notesDB.deleteFolder(folder)
    }

    suspend fun deleteNote(note: Note) {
        notesDB.deleteNote(note)
    }

    fun changeModeToInFolderBrowsing() {
        noteRepository.changeMode(NoteRepository.Modes.IN_FOLDER_BROWSING)
    }

    fun changeModeToNormalBrowsing() {
        noteRepository.changeMode(NoteRepository.Modes.BROWSER)
    }

    companion object {
        private const val TAG = "ViewModel.Notes"
    }
}