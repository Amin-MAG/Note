package ir.mag.interview.note.database.entity.note

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mag.interview.note.database.entity.folder.Folder

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM notes ORDER BY lastUpdateDate DESC")
    fun readAll(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE noteId= :noteId ORDER BY lastUpdateDate DESC LIMIT 1")
    fun selectById(noteId: Long): LiveData<Note>

    @Query("SELECT * FROM notes WHERE noteId= :noteId ORDER BY lastUpdateDate DESC LIMIT 1")
    fun selectByIdNow(noteId: Long): Note

    @Update
    suspend fun update(note: Note);

    @Delete
    suspend fun delete(note: Note)

}