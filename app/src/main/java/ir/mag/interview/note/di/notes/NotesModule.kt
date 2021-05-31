package ir.mag.interview.note.di.notes

import dagger.Module
import dagger.Provides
import ir.mag.interview.note.ui.main.recycler.adapter.FilesRecyclerAdapter
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Module
object NotesModule {

    @Provides
    fun provideFilesRecyclerAdapter() : FilesRecyclerAdapter {
        return FilesRecyclerAdapter()
    }

}