package `in`.woloo.www.application_kotlin.utilities


import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout

object ViewUtils {

    fun setWidthAndHeight(
        view: FrameLayout,
        height: Int,
        hasMarkerList: Boolean
    ) {
        var finalHeight = height

        if (hasMarkerList) {
            finalHeight -= 250
        }

        view.layoutParams.height = finalHeight
        view.requestLayout()
    }

}
