package ir.mag.interview.note.database.entity.folder

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mag.interview.note.database.entity.relation.FolderWithNotes

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder)

    @Query("SELECT * FROM folders ORDER BY folderId DESC")
    fun readAll(): LiveData<List<Folder>>

    @Query("SELECT * FROM folders")
    fun readAllWithNotes(): LiveData<List<FolderWithNotes>>

    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun readByIdWithNotes(folderId: Int): LiveData<FolderWithNotes>

}