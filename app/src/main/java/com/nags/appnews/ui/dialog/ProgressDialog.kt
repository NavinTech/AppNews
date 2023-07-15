package com.nags.appnews.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.nags.appnews.R

class ProgressDialog {
    private var mProgressDialog: Dialog? = null

    /**
     * It shows a progress dialog with title and message.
     * @param onDismissListener
     */
    fun showDialog(
        context: Context,
        isCancelable: Boolean,
        onDismissListener: DialogInterface.OnDismissListener?
    ) {
        if (mProgressDialog == null || !mProgressDialog!!.isShowing) {
            dismissDialog()
            mProgressDialog = Dialog(context)
            mProgressDialog?.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                if (null != window) {
                    window?.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                }
                setContentView(R.layout.dialog_progress)
                setCancelable(isCancelable)
                setCanceledOnTouchOutside(false)
                try {
                    show()
                } catch (e: Exception) {
                    Log.e("showDialog", "showDialog: ${e.localizedMessage}" )
                }
            }
        }
        if (null != onDismissListener) {
            mProgressDialog?.setOnDismissListener(onDismissListener)
        }
    }

    /**
     * It dismisses a running progress dialog.
     */
    fun dismissDialog() {
        mProgressDialog?.takeIf { it.isShowing }.let {
            try {
                it?.dismiss()
                mProgressDialog = null
            } catch (_: Exception) {

            }
        }
    }
}