package ir.mag.interview.note.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.repository.NoteRepository
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.repository.FolderRepository
import ir.mag.interview.note.database.entity.relation.FolderWithNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class NotesViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ViewModel.Notes"
    }

    val notes: LiveData<List<Note>> = noteRepository.notes
    val folders: LiveData<List<Folder>> = folderRepository.folders
    val foldersWithNotes: LiveData<List<FolderWithNotes>> = folderRepository.foldersWithNotes

    private fun addFolder(folder: Folder) {
        viewModelScope.launch(Dispatchers.IO) {
            folderRepository.addFolder(folder)
        }
    }

    private fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addNote(note)
        }
    }

    fun addUntitledNote() {
        addNote(
            Note(
                0,
                1,
                "عنوان نامشخص",
                "",
                Date()
            )
        )
    }

    fun addUntitledFolder() {
        addFolder(Folder(0, 0, "پوشه بدون اسم"))
    }


//    fun getAllFolderNotes(folderId: Int) : LiveData<List<FolderWithNotes>> {
//        return folderRepository.readWithNotesById(folderId)
//    }

}