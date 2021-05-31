package ir.mag.interview.note.database.entity.folder

import androidx.annotation.NonNull
import androidx.room.*
import ir.mag.interview.note.database.entity.file.File

@Entity(tableName = "folders")
class Folder(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val folderId: Long,
    val parentFolderId: Long?,
    val name: String
) : File() {
    override val type: Types
        get() = Types.FOLDER
}