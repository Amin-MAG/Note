package ir.mag.interview.note.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var instance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            val returnedInstance = instance
            if (returnedInstance != null) {
                return returnedInstance
            }

            synchronized(this) {
                val tempInstance = instance
                if (tempInstance != null) {
                    return tempInstance
                }

                // Create new object
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notes_database"
                ).build()
                instance = newInstance

                return newInstance
            }
        }
    }

}