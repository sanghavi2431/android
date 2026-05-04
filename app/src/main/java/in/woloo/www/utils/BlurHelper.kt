package `in`.woloo.www.utils

/*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.FrameLayout
import androidx.core.graphics.drawable.toBitmap

object BlurHelper {

    */
/**
     * Apply blur effect to a given FrameLayout's background.
     *//*

    fun applyBlur(context: Context, view: FrameLayout, blurRadius: Float = 20f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val bitmap = (view.background as? BitmapDrawable)?.toBitmap()
            bitmap?.let {
                val blurredBitmap = blurBitmap(context, it, blurRadius)
                view.setBackgroundDrawable(BitmapDrawable(context.resources, blurredBitmap))
            }
        }
    }

    */
/**
     * Apply blur to a Bitmap.
     *//*

    private fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap {
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)

        rs.destroy()
        return bitmap
    }
}
*/

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.FrameLayout
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.drawable.toBitmap

object BlurHelper {

    /**
     * Apply blur effect with a background color and corner radius to a given FrameLayout's background.
     */
    fun applyBlurWithBackground(
        context: Context,
        view: FrameLayout,
        blurRadius: Float = 20f,
        backgroundColor: Int = Color.parseColor("#40FFFFFF"),  // Default 40% opacity white
        cornerRadius: Float = 50f
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val bitmap = (view.background as? BitmapDrawable)?.toBitmap()
            bitmap?.let {
                val blurredBitmap = blurBitmap(context, it, blurRadius)

                // Apply the blurred bitmap as the background
                val drawable = BitmapDrawable(context.resources, blurredBitmap)

                // Set the color overlay with corner radius
                val overlayDrawable = createRoundedOverlayDrawable(backgroundColor, cornerRadius)

                // Combine both the blur and color overlay
                view.setBackground(drawable)
                view.background = overlayDrawable
            }
        }
    }

    /**
     * Apply blur to a Bitmap.
     */
    private fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap {
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)

        rs.destroy()
        return bitmap
    }

    /**
     * Create a rounded drawable with the specified background color and corner radius.
     */
    private fun createRoundedOverlayDrawable(color: Int, cornerRadius: Float): Drawable {
        val shape = GradientDrawable()
        shape.cornerRadius = cornerRadius
        shape.setColor(color)
        return shape
    }
}

