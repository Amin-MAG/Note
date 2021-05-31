package ir.mag.interview.note.database.repository

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
class NotesRepository
@Inject
constructor(
    private val noteDao: NoteDao,
    private val folderDao: FolderDao
) {

    val notes: LiveData<List<Note>> = noteDao.readAll()
    val folders: LiveData<List<FolderWithNotes>> = folderDao.readAllWithNotes()

    suspend fun addNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder)
    }

    fun getFolderNotes(folderId: Long): LiveData<FolderWithNotes> {
        return folderDao.readByIdWithNotes(folderId)
    }

    fun getFolderSubFolders(folderId: Long): LiveData<FolderWithSubFolders> {
        return folderDao.readByIdWithSubFolders(folderId)
    }

}