package ir.mag.interview.note.ui.main.recycler.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import ir.mag.interview.note.R
import ir.mag.interview.note.data.model.file.File
import ir.mag.interview.note.database.entity.folder.Folder
import ir.mag.interview.note.database.entity.note.Note
import ir.mag.interview.note.databinding.FileViewHolderBinding
import ir.mag.interview.note.databinding.FragmentDialogCommonBinding
import ir.mag.interview.note.ui.main.NotesFragment
import ir.mag.interview.note.ui.main.NotesViewModel
import ir.mag.interview.note.ui.main.dialog.CommonDialog
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class FilesRecyclerAdapter
constructor(
    val notesViewModel: NotesViewModel
) : RecyclerView.Adapter<FilesRecyclerAdapter.FileViewHolder>() {

    private lateinit var activity: AppCompatActivity

    var files: List<File> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        activity = parent.context as AppCompatActivity

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
//        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(files[position])
    }

    companion object {
        private const val TAG = "Adapter.FilesRecycler"
    }

    inner class FileViewHolder(private val binding: FileViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
//            Log.d(TAG, "bind: ")

            when (file.type) {

                File.Types.NOTE -> {
                    val note = file as Note

                    binding.fileCardTitle.text = note.title
                    binding.fileCardDescription.text = note.date.toString()
                    binding.fileCardIconFrame.background =
                        ContextCompat.getDrawable(activity, R.drawable.circle_light_blue);
                    binding.fileCardIcon.setImageResource(R.drawable.ic_note)
                    binding.fileCard.setOnClickListener {
                        notesViewModel.goToEditPage(note)
                    }
                    binding.fileCardOptionButton.setOnClickListener {
                        it?.let {
                            showMenu(it, R.menu.note_file_more_menu, file)
                        }
                    }
                }

                File.Types.FOLDER -> {
                    val folder = file as Folder

                    binding.fileCardTitle.text = folder.name
                    binding.fileCardDescription.text = "تعداد"
                    binding.fileCardIconFrame.background =
                        ContextCompat.getDrawable(activity, R.drawable.circle_yellow);
                    binding.fileCardIcon.setImageResource(R.drawable.ic_folder)
                    binding.fileCard.setOnClickListener {
                        Log.d(TAG, "bind onclick card: ")
                        notesViewModel.changeFolder(folder)
                    }
                    binding.fileCardOptionButton.setOnClickListener {
                        Log.d(TAG, "bind onclick more button: ")
                        it?.let {
                            showMenu(it, R.menu.folder_file_more_menu, file)
                        }
                    }
                }

                else -> throw UnsupportedOperationException("can not show this type of file")
            }

        }

        //In the showMenu function from the previous example:
        private fun showMenu(v: View, @MenuRes menuRes: Int, file: File) {
            val popup = PopupMenu(activity, v)
            popup.menuInflater.inflate(menuRes, popup.menu)

            setupIcons(popup)

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.edit_name -> {
                        true
                    }

                    R.id.delete_folder -> {
                        CommonDialog.Builder(activity, activity)
                            .setTitle(activity.getString(R.string.delete_folder))
                            .setDescription(activity.getString(R.string.delete_folder_description))
                            .setConfirmText(activity.getString(R.string.delete))
                            .setListener(object : CommonDialog.OnHandle {
                                override fun onCancel(
                                    dialog: AlertDialog,
                                    dialogBinding: FragmentDialogCommonBinding
                                ) {
                                    dialog.dismiss()
                                }

                                override fun onConfirm(
                                    dialog: AlertDialog,
                                    dialogBinding: FragmentDialogCommonBinding
                                ) {
                                    GlobalScope.launch {
                                        notesViewModel.deleteFolder(file as Folder)
                                    }
                                    dialog.dismiss()
                                }
                            })
                            .build()
                            .show()
                        true
                    }

                    R.id.delete_note -> {
                        CommonDialog.Builder(activity, activity)
                            .setTitle(activity.getString(R.string.delete_note))
                            .setDescription(activity.getString(R.string.delete_note_description))
                            .setConfirmText(activity.getString(R.string.delete))
                            .setListener(object : CommonDialog.OnHandle {
                                override fun onCancel(
                                    dialog: AlertDialog,
                                    dialogBinding: FragmentDialogCommonBinding
                                ) {
                                    dialog.dismiss()
                                }

                                override fun onConfirm(
                                    dialog: AlertDialog,
                                    dialogBinding: FragmentDialogCommonBinding
                                ) {
                                    GlobalScope.launch {
                                        notesViewModel.deleteNote(file as Note)
                                    }
                                    dialog.dismiss()
                                }
                            })
                            .build()
                            .show()
                        true
                    }

                    else -> throw UnsupportedOperationException("there is not this item")
                }
            }
            popup.setOnDismissListener {
                Log.d(TAG, "showMenu: Respond to popup being dismissed.")
            }

            popup.show()
        }

        @SuppressLint("RestrictedApi")
        private fun setupIcons(popup: PopupMenu) {
            if (popup.menu is MenuBuilder) {
                val menuBuilder = popup.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
                for (item in menuBuilder.visibleItems) {
                    val iconMarginPx =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 20f, activity.resources.displayMetrics
                        )
                            .toInt()
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon =
                                object :
                                    InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                    override fun getIntrinsicWidth(): Int {
                                        return intrinsicHeight + iconMarginPx + iconMarginPx
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

}