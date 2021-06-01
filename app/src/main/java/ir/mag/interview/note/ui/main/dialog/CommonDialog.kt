package ir.mag.interview.note.ui.main.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ir.mag.interview.note.R
import ir.mag.interview.note.databinding.FragmentDialogCommonBinding

class CommonDialog private constructor(
    val fragment: Fragment,
    val title: String?,
    val description: String?,
    // Prompt
    val hasPrompt: Boolean = false,
    val promptHint: String?,
    // Buttons and Callbacks
    val listener: OnHandle?,
    val confirmText: String?,
    val cancelText: String?
) {

    lateinit var binding: FragmentDialogCommonBinding
    val context: Context = fragment.requireContext()
    private lateinit var dialog: AlertDialog

    data class Builder(
        private var fragment: Fragment,
        private var title: String? = null,
        private var description: String? = null,
        private var hasPrompt: Boolean = false,
        private var promptHint: String? = null,
        private var listener: OnHandle? = null,
        private var confirmText: String? = null,
        private var cancelText: String? = null
    ) {

        fun setTitle(title: String) = apply { this.title = title }
        fun setDescription(description: String) = apply { this.description = description }
        fun setHasPrompt(hasPrompt: Boolean) = apply { this.hasPrompt = hasPrompt }
        fun setPromptHint(promptHint: String) = apply { this.promptHint = promptHint }
        fun setListener(listener: OnHandle) = apply { this.listener = listener }
        fun setCancelText(cancelText: String) = apply { this.cancelText = cancelText }
        fun setConfirmText(confirmText: String) = apply { this.confirmText = confirmText }
        fun build() = CommonDialog(
            fragment,
            title,
            description,
            hasPrompt,
            promptHint,
            listener,
            confirmText,
            cancelText
        )
    }

    fun show() {
        // inflate
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_dialog_common,
            null,
            false
        )
        binding.lifecycleOwner = fragment


        // create and show the alert dialog
        dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()
        dialog.window?.setBackgroundDrawable(context.resources.getDrawable(R.drawable.dialog_bg))


        // set properties
        binding.confirmDialogTitle.text = title
        binding.confirmDialogQuestion.text = description
        binding.confirmDialogAcceptButton.text = confirmText
        cancelText?.let {
            binding.confirmDialogCancelButton.text = it
        }

        // prompt
        if (hasPrompt) {
            binding.commonDialogTextFieldLayout.visibility = View.VISIBLE
            binding.commonDialogTextField.visibility = View.VISIBLE
            promptHint?.let { hint ->
                binding.commonDialogTextFieldLayout.hint = hint
            }
        }

        // listeners
        listener?.let { handler ->
            binding.confirmDialogAcceptButton.setOnClickListener {
                handler.onConfirm(dialog)
            }
            binding.confirmDialogCancelButton.setOnClickListener {
                handler.onCancel(dialog)
            }
        }

        dialog.show()
    }

    interface OnHandle {
        fun onCancel(dialog: AlertDialog)
        fun onConfirm(dialog: AlertDialog)
    }
}