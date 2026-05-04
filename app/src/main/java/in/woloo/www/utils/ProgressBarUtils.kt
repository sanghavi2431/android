package `in`.woloo.www.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.Window
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils

object ProgressBarUtils {
    private const val TIME_OUT = 800

    @JvmStatic
    fun initProgressDialog(context: Context): Dialog? {
        var dialoProgress: Dialog? = null
        try {
            dialoProgress = Dialog(context, R.style.NewDialog)
            //dialoProgress = new Dialog(context);
            dialoProgress.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialoProgress.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
            dialoProgress.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialoProgress.setContentView(R.layout.dialog_progress_overlay)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return dialoProgress
    }

    fun showProgressDialog(dialoProgress: Dialog?, isCancelable: Boolean) {
        try {
            if (dialoProgress != null) {
                dialoProgress.setCancelable(isCancelable)
                dialoProgress.show()
                Handler().postDelayed({
                    dismissProgressDialog(
                        dialoProgress
                    )
                }, TIME_OUT.toLong())
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showProgressDialogWithOutTime(dialoProgress: Dialog?, isCancelable: Boolean) {
        try {
            if (dialoProgress != null) {
                dialoProgress.setCancelable(isCancelable)
                dialoProgress.show()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    @JvmStatic
    fun dismissProgressDialog(dialoProgress: Dialog?) {
        try {
            if (dialoProgress != null) {
                if (dialoProgress.isShowing) {
                    dialoProgress.dismiss()
                }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }
}
