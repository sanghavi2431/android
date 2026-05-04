package `in`.woloo.www.application_kotlin.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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

 /*   @SuppressLint("ResourceAsColor", "DrawAllocation")
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
            (height / 5).coerceAtMost(50).toFloat() // Prevent extremely large thumb radius

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

        val thumbX = (value - valueFrom) / (valueTo - valueFrom) * width // Calculate thumb position based on value
        val thumbY = ((height / 3) + 10).toFloat()

// Draw stroke (black outline)
        paint.style = Paint.Style.STROKE // Set paint style to stroke
        paint.color = Color.BLACK // Set stroke color
        paint.strokeWidth = 10f // Adjust stroke width
        paint.shader = null
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint) // Draw stroke first

// Draw filled circle (thumb color)
        paint.style = Paint.Style.FILL // Set paint style back to fill
        paint.color = ContextCompat.getColor(context, R.color.start_theme_color) // Thumb color
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint) // Draw fill on top


    }*/


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.coerceIn(100, 2000)
        val height = height.coerceIn(100, 500)

        // Get colors from resources
        val colors = listOf(
            ContextCompat.getColor(context, R.color.red_line),
            ContextCompat.getColor(context, R.color.orange_line),
            ContextCompat.getColor(context, R.color.yello_line),
            ContextCompat.getColor(context, R.color.green_line),
            ContextCompat.getColor(context, R.color.dark_green_line)
        )

        val segmentWidth = width / colors.size

        // Draw white background
        paint.shader = null
        paint.color = ContextCompat.getColor(context, R.color.white)
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Draw color segments
        for ((index, color) in colors.withIndex()) {
            paint.color = color
            val left = index * segmentWidth.toFloat()
            val right = (index + 1) * segmentWidth.toFloat()
            val top = (height / 2 - 10).toFloat()
            val bottom = (height / 2 + 10).toFloat()
            canvas.drawRect(left, top, right, bottom, paint)
        }

        // Draw thumb
        val thumbX = (value - valueFrom) / (valueTo - valueFrom) * width
        val thumbY = ((height / 2) + 10).toFloat()
        thumbRadius = (height / 5).coerceAtMost(50).toFloat()

        // Thumb stroke (outline)
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint)

        // Thumb fill
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.start_theme_color)
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint)
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
