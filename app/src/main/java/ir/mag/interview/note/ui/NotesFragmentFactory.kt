package ir.mag.interview.note.ui

import android.content.res.Resources
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import ir.mag.interview.note.di.notes.NotesScope
import ir.mag.interview.note.ui.main.NotesFragment
import javax.inject.Inject


@NotesScope
class NotesFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {


    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            NotesFragment::class.java.name -> {
                NotesFragment(viewModelFactory)
            }


            else -> {
                throw Resources.NotFoundException("No such fragment $className")
            }

        }

}