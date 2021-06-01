package ir.mag.interview.note.ui.main

import android.icu.text.CaseMap
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
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository,
    private val notesDB: NotesDatabaseRepository
) : ViewModel() {


    var currentFolder: LiveData<Folder> = noteRepository.currentFolder
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
                    currentFiles.postValue(it)
                }
            }

            currentFiles.addSource(getCurrentSubFolders()) { folder ->
                folder?.subFolders?.let { folders ->
                    it[File.Types.FOLDER] = folders
                    currentFiles.postValue(it)
                }
            }
        }
    }

    fun goToEditPage(noteId: Long) {
        val note = notesDB.getNoteById(noteId)
        if (note.value == null) {
            throw IllegalStateException("can not find the note !")
        }

        noteRepository.changeCurrentNote(note.value!!)
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

    fun addFolder(folder: Folder) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDB.addFolder(folder)
        }
    }

    private fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDB.addNote(note)
        }
    }

    fun addUntitledNote() {
        currentFolder.value?.let {
            Log.d(
                TAG, "addUntitledNote: ${
                Note(
                    0,
                    it.folderId,
                    "عنوان نامشخص",
                    "",
                    Date()
                )}"
            )
            addNote(
                Note(
                    0,
                    it.folderId,
                    "عنوان نامشخص",
                    "",
                    Date()
                )
            )
        }
    }

    fun addUntitledFolder() {
        currentFolder.value?.let {
            Log.d(
                TAG,
                "addUntitledFolder: ${Folder(0, it.folderId, "پوشه بدون اسم").parentFolderId}"
            )
            addFolder(Folder(0, it.folderId, "پوشه بدون اسم"))
        }
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