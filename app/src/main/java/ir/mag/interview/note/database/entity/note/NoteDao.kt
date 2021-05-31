package ir.mag.interview.note.database.entity.note

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun readAll(): LiveData<List<Note>>

}