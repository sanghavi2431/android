package `in`.woloo.www.application_kotlin.utilities

import android.app.Activity
import android.os.Handler
import android.widget.TextView
import `in`.woloo.www.R


/**
 * Created by jadhavpankaj16 on 2019-11-15
 */
class AppProgressBaseDialog : AppBaseDialogBox {

    private var txtMessage: TextView = mDialog.findViewById(R.id.txtMessage)

    constructor(context: Activity) : super(context, R.layout.dialog_progress) {
        mDialog.setCancelable(false)
    }

    fun show(message: String) {
        txtMessage.text = message
        if (!mDialog.isShowing) {
            mDialog.show()
        }
    }

    override fun hide() {
        if (mDialog != null && mDialog.isShowing && !context.isFinishing) {
            Handler().postDelayed(Runnable {
                mDialog.dismiss()
            }, 500)
        }
    }

    fun hideWithoutDelay() {
        if (mDialog != null && mDialog.isShowing) {
            mDialog.dismiss()
        }
    }
}