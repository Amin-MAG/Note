package ir.mag.interview.note.database.entity.note

import androidx.annotation.NonNull
import androidx.room.*
import ir.mag.interview.note.database.converter.DateConverter
import ir.mag.interview.note.database.entity.file.File
import java.util.*

@Entity(tableName = "notes")
@TypeConverters(DateConverter::class)
class Note(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    val folderId: Long,
    val title: String,
    val content: String,
    val date: Date
) : File() {
    override val type: Types
        get() = Types.NOTE
}
