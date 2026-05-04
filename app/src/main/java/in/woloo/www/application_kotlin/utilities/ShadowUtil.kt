package `in`.woloo.www.application_kotlin.utilities


import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.View

class ShadowUtil {


        companion object {
            // Extension function to apply custom shadow on any View
            fun applyCustomShadow(
                view: View,
                shadowColor: Int = Color.parseColor("#33000000"), // 20% opacity black (#000000 with 20% opacity)
                shadowRadius: Float = 16f, // Blur radius
                shadowDx: Float = 0f, // Horizontal offset (0)
                shadowDy: Float = 4f, // Vertical offset (4)
                cornerRadius: Float = 25f, // Corner radius for the button (optional)
                backgroundColor: Int = Color.WHITE // Background color (default is white)
            ) {
                // Enable software layer for custom shadows
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

                // Create a paint object for the shadow
                val paint = Paint()
                paint.color = Color.TRANSPARENT // The paint color itself is transparent, we only use shadowLayer
                paint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor) // Set the shadow layer

                // Create a shape drawable with rounded corners and a solid color
                val shapeDrawable = GradientDrawable()
                shapeDrawable.shape = GradientDrawable.RECTANGLE
                shapeDrawable.setColor(backgroundColor) // Set the solid color
                shapeDrawable.cornerRadius = cornerRadius // Set the corner radius

                // Apply the shape drawable as the background of the view
                view.background = shapeDrawable
            }

            fun Context.dpToPx(dp: Float): Float {
                return dp * resources.displayMetrics.density
            }
        }

}