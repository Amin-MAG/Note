package ir.mag.interview.note

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ir.mag.interview.note.di.ApplicationComponent
import ir.mag.interview.note.di.DaggerApplicationComponent


class NoteApplication : Application() {
    companion object {
        private lateinit var context: Context
        fun getApplicationContext(): Context = context
    }


    override fun onCreate() {
        super.onCreate()

    }
}