package `in`.woloo.www.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import `in`.woloo.www.R

class CustomProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val imageView: AppCompatImageView

    init {
        // Set background with transparency (semi-transparent black overlay)
        setBackgroundColor(Color.parseColor("#88000000")) // Semi-transparent black background

        // Set the layout params to make this view cover the entire screen
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams = params

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val screenWidth = display.width  // Get full screen width
        val screenHeight = display.height // Get full screen height

        // Calculate image width (1/4 of screen width) and height (1/8 of screen height)
        val imageWidth = (screenWidth / 4).toInt()
        val imageHeight = (screenHeight / 8).toInt()

        // Create and set up the animated image
        imageView = AppCompatImageView(context)
        val imageParams = LayoutParams(imageWidth, imageHeight, Gravity.CENTER)
        imageView.layoutParams = imageParams

        addView(imageView)

        // Set WebP animation
        imageView.setImageResource(R.drawable.woloo_loader) // Your WebP file in res/drawable
        val drawable = imageView.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
    }

    fun show() {
        visibility = VISIBLE

        // Check if context is an Activity, Fragment, or Dialog
        val activity = when (val currentContext = context) {
            is Activity -> currentContext
            is Fragment -> currentContext.activity
            is Dialog -> currentContext.context as? Activity
            else -> throw IllegalStateException("Context must be an Activity, Fragment, or Dialog.")
        }

        activity?.let {
            // Add this CustomProgressView to the root view of the activity
            val rootView = it.findViewById<View>(android.R.id.content) as ViewGroup
            rootView.addView(this)
        } ?: throw IllegalStateException("Unable to retrieve an Activity from the context.")
    }

    fun hide() {
        visibility = GONE
        (parent as? ViewGroup)?.removeView(this)
    }
}
