package ir.mag.interview.note.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ir.mag.interview.note.NoteApplication
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.databinding.ActivityNotesMainBinding
import ir.mag.interview.note.di.notes.NotesComponent
import ir.mag.interview.note.ui.editor.EditorFragment
import ir.mag.interview.note.ui.editor.EditorHeaderFragment
import ir.mag.interview.note.ui.main.NotesFragment
import ir.mag.interview.note.ui.main.NotesHeaderFragment
import ir.mag.interview.note.util.UiUtil
import javax.inject.Inject

class NotesMainActivity : AppCompatActivity() {

    lateinit var notesComponent: NotesComponent

    @Inject
    lateinit var fragmentFactory: NotesFragmentFactory

    lateinit var binding: ActivityNotesMainBinding

    // Fragments
    private lateinit var notesFragment: Fragment
    private lateinit var editorFragment: Fragment
    private lateinit var notesHeaderFragment: Fragment
    private lateinit var editorHeaderFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        inject()

        // main fragments
        notesFragment =
            fragmentFactory.instantiate(classLoader, NotesFragment::class.java.name)
        editorFragment =
            fragmentFactory.instantiate(classLoader, EditorFragment::class.java.name)
        // headers
        notesHeaderFragment =
            fragmentFactory.instantiate(classLoader, NotesHeaderFragment::class.java.name)
        editorHeaderFragment =
            fragmentFactory.instantiate(classLoader, EditorHeaderFragment::class.java.name)

        binding = ActivityNotesMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        UiUtil.changeFragment(
            supportFragmentManager,
            notesHeaderFragment,
            binding.header.id,
            true,
            NotesHeaderFragment::class.java.name
        )

        setupUI()
    }

    private fun setupUI() {
        Log.d(TAG, "setupUI: ")
        UiUtil.changeFragment(
            supportFragmentManager,
            notesFragment,
            binding.mainFrameLayout.id,
            true,
            notesFragment::class.java.name
        )
    }


    private fun inject() {
        notesComponent =
            (applicationContext as NoteApplication).applicationComponent
                .notesComponent()
                .create()
        notesComponent.inject(this)
    }

    companion object {
        const val FRAGMENT_TAG: String = "NOTES_MAIN_ACTIVITY_FRAGMENT"
        private const val TAG = "Activity.NotesMainActivity"
    }
}