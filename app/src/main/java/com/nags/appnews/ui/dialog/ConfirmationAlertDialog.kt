package com.nags.appnews.ui.dialog

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nags.appnews.R

/**
 * Creates a custom dialog with specified parameters.
 *
 * @param context The context of the dialog.
 * @param title The title of the dialog.
 * @param message The message of the dialog.
 * @param positiveText The text for the positive button.
 * @param negativeText The text for the negative button.
 * @param positiveListener The click listener for the positive button.
 * @param negativeListener The click listener for the negative button.
 */
fun createDialog(
    context: Context,
    title: String?,
    message: String?,
    positiveText: String?,
    negativeText: String?,
    positiveListener: DialogInterface.OnClickListener? = null,
    negativeListener: DialogInterface.OnClickListener? = null
) {
    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme)
        .setTitle(title)
        .setMessage(message)

    if (!negativeText.isNullOrEmpty()) {
        materialAlertDialogBuilder.setNegativeButton(negativeText) { dialogInterface, i ->
            negativeListener?.onClick(dialogInterface, i)
        }
    }

    if (!positiveText.isNullOrEmpty()) {
        materialAlertDialogBuilder.setPositiveButton(positiveText) { dialogInterface, i ->
            positiveListener?.onClick(dialogInterface, i)
        }
    }

    materialAlertDialogBuilder.show()
}
