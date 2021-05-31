package ir.mag.interview.note.database.entity.file

abstract class File {
    enum class Types {
        FOLDER, NOTE
    }

    abstract val type: Types
}