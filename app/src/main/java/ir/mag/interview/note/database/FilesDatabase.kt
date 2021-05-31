package ir.mag.interview.note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.folder.FolderDao
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.database.entity.note.NoteDao

@Database(
    entities = [Folder::class, Note::class],
    version = 1,
    exportSchema = false
)
abstract class FilesDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var instance: FilesDatabase? = null

        fun getDatabase(context: Context): FilesDatabase {
            val returnedInstance =
                instance
            if (returnedInstance != null) {
                return returnedInstance
            }

            synchronized(this) {
                val tempInstance =
                    instance
                if (tempInstance != null) {
                    return tempInstance
                }

                // Create new object
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    FilesDatabase::class.java,
                    "files_database"
                ).build()
                instance = newInstance

                return newInstance
            }
        }
    }

}