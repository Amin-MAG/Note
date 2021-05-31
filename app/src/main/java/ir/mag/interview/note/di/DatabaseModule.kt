package ir.mag.interview.note.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ir.mag.interview.note.NoteApplication
import ir.mag.interview.note.data.model.NoteDao
import ir.mag.interview.note.data.model.NoteDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    fun provideNoteDao(
        context: Application
    ): NoteDao {
        return NoteDatabase.getDatabase(context).noteDao()
    }

}