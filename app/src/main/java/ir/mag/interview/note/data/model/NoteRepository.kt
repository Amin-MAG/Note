package ir.mag.interview.note.data.model

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository
@Inject
constructor(
    private val noteDao: NoteDao
) {

    val readAllData: LiveData<List<Note>> = noteDao.readAllData()

    suspend fun addNote(note: Note) {
        noteDao.insertNote(note)
    }

}