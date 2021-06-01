package ir.mag.interview.note.ui.main

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
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository,
    private val notesDB: NotesDatabaseRepository
) : ViewModel() {


    var currentFolderId: MutableLiveData<Long> = noteRepository.currentFolderId
    var currentFiles: MediatorLiveData<EnumMap<File.Types, List<File>>> = MediatorLiveData()

    init {
        currentFiles.value = EnumMap(File.Types::class.java)
        currentFiles.value?.put(File.Types.FOLDER, ArrayList())
        currentFiles.value?.put(File.Types.NOTE, ArrayList())
        currentFiles.value?.let {

            currentFiles.addSource(getFolderNotes()) { folder ->
                folder?.notes?.let { notes ->
//                    Log.d(TAG, "init view model notes: $notes")
                    it[File.Types.NOTE] = notes
                    currentFiles.postValue(it)
                }
            }

            currentFiles.addSource(getFolderSubFolders()) { folder ->
                folder?.subFolders?.let { folders ->
//                    Log.d(TAG, "init view model folders: $folders")
                    it.put(File.Types.FOLDER, folders)
                    currentFiles.postValue(it)
                }
            }

        }
    }

    fun goToEditPage(noteId: Long) {
        noteRepository.changeCurrentNote(noteId)
        noteRepository.changeMode(NoteRepository.Modes.EDITOR)
    }

    private fun getFolderNotes(): LiveData<FolderWithNotes> {
        return notesDB.getFolderNotes(currentFolderId.value!!)
    }

    private fun getFolderSubFolders(): LiveData<FolderWithSubFolders> {
        return notesDB.getFolderSubFolders(currentFolderId.value!!)
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
        currentFolderId.value?.let {
            Log.d(
                TAG, "addUntitledNote: ${
                Note(
                    0,
                    it,
                    "عنوان نامشخص",
                    "",
                    Date()
                )}"
            )
            addNote(
                Note(
                    0,
                    it,
                    "عنوان نامشخص",
                    "",
                    Date()
                )
            )
        }
    }

    fun addUntitledFolder() {
        currentFolderId.value?.let {
            Log.d(TAG, "addUntitledFolder: ${Folder(0, it, "پوشه بدون اسم").parentFolderId}")
            addFolder(Folder(0, it, "پوشه بدون اسم"))
        }
    }

    companion object {
        private const val TAG = "ViewModel.Notes"
        private const val ROOT_FOLDER_ID = 1L
    }
}