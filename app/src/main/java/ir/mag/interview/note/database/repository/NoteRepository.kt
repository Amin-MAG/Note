package ir.mag.interview.note.database.repository

import androidx.lifecycle.LiveData
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.entity.note.NoteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository
@Inject
constructor(
    private val noteDao: NoteDao
) {

    val notes: LiveData<List<Note>> = noteDao.readAll()

    suspend fun addNote(note: Note) {
        noteDao.insert(note)
    }
    
}