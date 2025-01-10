package `in`.woloo.www.application_kotlin.utilities

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import kotlin.math.roundToInt


/**
 * Created by jadhavpankaj16 on 2019-11-15
 */
abstract class AppBaseDialogBox(val context: Activity, layoutResource: Int) {

    protected var mDialog: Dialog = Dialog(context)

    init {
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCancelable(true)
        mDialog.setContentView(layoutResource)
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        mDialog.window!!.setLayout((displayMetrics.widthPixels * 0.7f).roundToInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    protected fun disableOnTouchOutside(parent: View?, boundary: View?) {
        if (parent != null && boundary != null) {
            parent.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                    val rect = Rect(boundary.left, boundary.top, boundary.right, boundary.bottom)
                    if (rect != null && !rect.contains(motionEvent.x as Int, motionEvent.y as Int)) {
                        mDialog.dismiss()
                        return true
                    }
                    return false
                }
            })
        }
    }

    fun show() {
        if (!mDialog.isShowing) {
            mDialog.show()
        }
    }

    open fun hide() {
        if (mDialog.isShowing) {
            Handler().postDelayed(Runnable {
                mDialog.dismiss()
            },500)
        }
    }
}