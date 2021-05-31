package ir.mag.interview.note.database.entity

abstract class File {
    enum class Types {
        FOLDER, NOTE
    }

    abstract val type: Types
}