package ir.mag.interview.note.database.repository

import androidx.lifecycle.LiveData
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.folder.FolderDao
import ir.mag.interview.note.database.entity.relation.FolderWithNotes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository
@Inject
constructor(
    private val folderDao: FolderDao
) {

    val folders: LiveData<List<Folder>> = folderDao.readAll()
    val foldersWithNotes: LiveData<List<FolderWithNotes>> = folderDao.readAllWithNotes()

    suspend fun addFolder(folder: Folder) {
        folderDao.insert(folder)
    }

    fun getFolderById(folderId: Int): LiveData<FolderWithNotes> {
        return folderDao.readByIdWithNotes(folderId)
    }

}