package ir.mag.interview.note.ui.main.recycler.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.mag.interview.note.R
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.databinding.FileViewHolderBinding

class FilesRecyclerAdapter : RecyclerView.Adapter<FilesRecyclerAdapter.FileViewHolder>() {

    private lateinit var activity: Activity

    var files: List<File> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        activity = parent.context as Activity

        val binding: FileViewHolderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.file_view_holder,
            parent,
            false
        )

        return FileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(files[position])
    }

    companion object {
        private const val TAG = "Adapter.FilesRecycler"
    }

    inner class FileViewHolder(private val binding: FileViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
            Log.d(TAG, "bind: ")

            when (file.type) {

                File.Types.NOTE -> {
                    val note = file as Note

                    binding.fileCardTitle.text = note.title
                    binding.fileCardDescription.text = note.date.toString()
                    binding.fileCardIconFrame.background =
                        ContextCompat.getDrawable(activity, R.drawable.circle_light_blue);
                    binding.fileCardIcon.setImageResource(R.drawable.ic_note)
                }

                File.Types.FOLDER -> {
                    val folder = file as Folder

                    binding.fileCardTitle.text = folder.name
                    binding.fileCardDescription.text = "تعداد"
                    binding.fileCardIconFrame.background =
                        ContextCompat.getDrawable(activity, R.drawable.circle_yellow);
                    binding.fileCardIcon.setImageResource(R.drawable.ic_folder)
                }

                else -> throw UnsupportedOperationException("can not show this type of file")
            }

        }

    }

}