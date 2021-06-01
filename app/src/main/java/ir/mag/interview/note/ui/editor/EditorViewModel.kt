package ir.mag.interview.note.ui.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.database.repository.NotesDatabaseRepository
import javax.inject.Inject


class EditorViewModel
@Inject
constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    var mode: LiveData<NoteRepository.Modes> = noteRepository.mode


    fun goBackToBrowser() {
        noteRepository.changeMode(NoteRepository.Modes.BROWSER)
    }

}