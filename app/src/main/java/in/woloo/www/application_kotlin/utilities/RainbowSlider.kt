package `in`.woloo.www.application_kotlin.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import `in`.woloo.www.R

class RainbowSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Slider(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var thumbRadius = 0f // Thumb radius will be calculated
    private var gradientShader: Shader? = null

    init {
        paint.isAntiAlias = true
    }

    @SuppressLint("ResourceAsColor", "DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Ensure width and height are within reasonable bounds
        val width = width.coerceIn(100, 2000) // Limit width to a reasonable size
        val height = height.coerceIn(100, 500) // Limit height to a reasonable size

        // Get the actual colors from resources
        val red = ContextCompat.getColor(context, R.color.red_line)
        val orange = ContextCompat.getColor(context, R.color.orange_line)
        val yellow = ContextCompat.getColor(context, R.color.yello_line)
        val green = ContextCompat.getColor(context, R.color.green_line)
        val blue = ContextCompat.getColor(context, R.color.dark_green_line)

        // Calculate the thumb radius: 3x the track height, ensure reasonable max size
        thumbRadius =
            (height / 3).coerceAtMost(50).toFloat() // Prevent extremely large thumb radius

        // Clear canvas before drawing
        paint.shader = null
        paint.color = ContextCompat.getColor(context, R.color.white)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Create the linear gradient only once and reuse it
        if (gradientShader == null) {
            gradientShader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                intArrayOf(red, orange, yellow, green, blue),
                null,
                Shader.TileMode.CLAMP // Using CLAMP instead of DECAL can help with memory issues
            )
        }
        paint.shader = gradientShader

        // Draw the gradient-filled rectangle for the track
        canvas.drawRect(0f, ((height / 2) - 5).toFloat(),
            width.toFloat(), (height / 3).toFloat(), paint)

        // Draw the slider thumb: Set thumb size as 3 times the track height
        paint.shader = null
        paint.color = ContextCompat.getColor(context, R.color.start_theme_color) // Thumb color
        val thumbX = (value - valueFrom) / (valueTo - valueFrom) * width // Calculate thumb position based on value
        canvas.drawCircle(thumbX, ((height / 2) - 7).toFloat(), thumbRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Calculate the raw value based on the touch position
                val rawValue = (event.x / width) * (valueTo - valueFrom) + valueFrom

                // Ensure the value respects the stepSize
                val roundedValue = valueFrom + (Math.round((rawValue - valueFrom) / stepSize) * stepSize)

                // Set the new value and invalidate to redraw
                value = roundedValue.coerceIn(valueFrom, valueTo)
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
