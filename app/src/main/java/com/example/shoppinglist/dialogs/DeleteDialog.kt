package com.example.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.shoppinglist.databinding.DeleteDialogBinding
import com.example.shoppinglist.databinding.NewListDialogBinding

object DeleteDialog {
    fun showDeleteDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            dtnDeleteList.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
            }
            dtnCancel.setOnClickListener {
                dialog?.dismiss()
            }
            dialog = builder.create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
            dialog?.show()
        }
    }

    interface Listener {
        fun onClick()
    }
}