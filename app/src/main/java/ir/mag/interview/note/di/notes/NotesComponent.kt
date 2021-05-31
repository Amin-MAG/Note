package ir.mag.interview.note.di.notes

import dagger.Subcomponent
import ir.mag.interview.note.ui.NotesMainActivity
import ir.mag.interview.note.ui.main.NotesFragment

@NotesScope
@Subcomponent(
    modules = [
        NotesModule::class,
        NotesFragmentModule::class,
        NotesViewModelModule::class
    ]
)
interface NotesComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(): NotesComponent
    }

    fun inject(activity: NotesMainActivity)
    fun inject(fragment: NotesFragment)
}