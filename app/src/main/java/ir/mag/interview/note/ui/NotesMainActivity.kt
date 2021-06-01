package ir.mag.interview.note.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ir.mag.interview.note.NoteApplication
import ir.mag.interview.note.data.repository.NoteRepository
import ir.mag.interview.note.databinding.ActivityNotesMainBinding
import ir.mag.interview.note.di.notes.NotesComponent
import ir.mag.interview.note.ui.editor.EditorFragment
import ir.mag.interview.note.ui.editor.EditorHeaderFragment
import ir.mag.interview.note.ui.main.InFolderHeaderFragment
import ir.mag.interview.note.ui.main.NotesFragment
import ir.mag.interview.note.ui.main.NotesHeaderFragment
import ir.mag.interview.note.util.UiUtil
import java.lang.UnsupportedOperationException
import javax.inject.Inject
import kotlin.math.log

class NotesMainActivity : AppCompatActivity() {

    lateinit var notesComponent: NotesComponent

    @Inject
    lateinit var fragmentFactory: NotesFragmentFactory

    @Inject
    lateinit var notesMainViewModel: NotesMainViewModel

    lateinit var binding: ActivityNotesMainBinding

    // Fragments
    private lateinit var notesFragment: Fragment
    private lateinit var editorFragment: Fragment
    private lateinit var notesHeaderFragment: Fragment
    private lateinit var inFolderHeaderFragment: Fragment
    private lateinit var editorHeaderFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        inject()

        provideFragments()

        binding = ActivityNotesMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupUI()
    }

    private fun provideFragments() {
        // main fragments
        notesFragment =
            fragmentFactory.instantiate(classLoader, NotesFragment::class.java.name)
        editorFragment =
            fragmentFactory.instantiate(classLoader, EditorFragment::class.java.name)

        // headers
        notesHeaderFragment =
            fragmentFactory.instantiate(classLoader, NotesHeaderFragment::class.java.name)
        inFolderHeaderFragment =
            fragmentFactory.instantiate(classLoader, InFolderHeaderFragment::class.java.name)
        editorHeaderFragment =
            fragmentFactory.instantiate(classLoader, EditorHeaderFragment::class.java.name)
    }

    private fun setupUI() {
        Log.d(TAG, "setupUI: ")

        notesMainViewModel.mode.observe(this, Observer {
            it?.let {

                when (it) {
                    NoteRepository.Modes.BROWSER -> {
                        Log.d(TAG, "setupUI: change to normal browser mode")
                        updateFragments(
                            notesHeaderFragment,
                            notesFragment
                        )
                    }

                    NoteRepository.Modes.IN_FOLDER_BROWSING -> {
                        Log.d(TAG, "setupUI: change to in folder browser mode")
                        updateFragments(
                            inFolderHeaderFragment,
                            notesFragment
                        )
                    }
                    NoteRepository.Modes.EDITOR -> updateFragments(
                        editorHeaderFragment,
                        editorFragment
                    )

                    else -> UnsupportedOperationException("this kind of mode is not supported")
                }
            }
        })

    }

    fun updateFragments(header: Fragment, content: Fragment) {
        // header fragment
        UiUtil.changeFragment(
            supportFragmentManager,
            header,
            binding.header.id,
            true,
            header::class.java.name
        )
        // content fragment
        UiUtil.changeFragment(
            supportFragmentManager,
            content,
            binding.mainFrameLayout.id,
            true,
            content::class.java.name
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