package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.*
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class ImageProcessor {
    companion object {
        fun applyBlur(context: Context, view: View, radius: Float = 20f) {
            // Wait until the view is measured to get width and height
            view.post {
                val drawable = view.background ?: return@post

                // Convert background drawable to bitmap
                val bitmap = drawableToBitmap(drawable, view.width, view.height)
                if (bitmap == null) return@post // Avoid crash if width/height is zero

                val blurredBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // API 31+: Use RenderEffect for blurring
                    view.setRenderEffect(RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP))
                    bitmap
                } else {
                    // Use RenderScript for older devices
                    blurBitmap(context, bitmap, radius)
                }

                // Convert the blurred bitmap back to drawable
                val blurredDrawable: Drawable = BitmapDrawable(context.resources, blurredBitmap)

                // Set the blurred background without affecting child views (icons/tabs)
                if (view is BottomNavigationView) {
                    // Wrap background in a FrameLayout (prevents affecting tabs)
                    val container = view.getChildAt(0) as? FrameLayout
                    container?.background = blurredDrawable
                } else {
                    view.setBackground(blurredDrawable)
                }
            }
        }

        private fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
            val outputBitmap = Bitmap.createBitmap(bitmap)
            val renderScript = RenderScript.create(context)

            val input = Allocation.createFromBitmap(renderScript, bitmap)
            val output = Allocation.createFromBitmap(renderScript, outputBitmap)

            val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            scriptIntrinsicBlur.setRadius(radius)
            scriptIntrinsicBlur.setInput(input)
            scriptIntrinsicBlur.forEach(output)

            output.copyTo(outputBitmap)
            renderScript.destroy()

            return outputBitmap
        }

        private fun drawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap? {
            if (width <= 0 || height <= 0) return null // Prevent crash due to invalid width/height

            return if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
        }
    }
}
