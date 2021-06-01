package ir.mag.interview.note.database.repository

import android.util.Log
import androidx.lifecycle.LiveData
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.folder.FolderDao
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.entity.note.NoteDao
import ir.mag.interview.note.database.relation.FolderWithNotes
import ir.mag.interview.note.database.relation.FolderWithSubFolders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesDatabaseRepository
@Inject
constructor(
    private val noteDao: NoteDao,
    private val folderDao: FolderDao
) {

    val notes: LiveData<List<Note>> = noteDao.readAll()
    val folders: LiveData<List<FolderWithNotes>> = folderDao.readAllWithNotes()

    suspend fun addNote(note: Note): Long {
        return noteDao.insert(note)
    }

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder)
    }

    fun getFolderByIdWithNotes(folderId: Long): LiveData<FolderWithNotes> {
        return folderDao.readByIdWithNotes(folderId)
    }

    fun getFolderById(folderId: Long): LiveData<Folder> {
        return folderDao.selectById(folderId)
    }

    fun getFolderSubFolders(folderId: Long): LiveData<FolderWithSubFolders> {
        return folderDao.readByIdWithSubFolders(folderId)
    }

    fun getNoteById(noteId: Long): LiveData<Note> {
        return noteDao.selectById(noteId)
    }

    fun getNoteByIdNow(noteId: Long): Note {
        return noteDao.selectByIdNow(noteId)
    }

    companion object {
        const val TAG: String = "Repository.NotesDatabase"
    }
}