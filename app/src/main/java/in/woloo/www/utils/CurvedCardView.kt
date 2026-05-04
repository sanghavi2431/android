package `in`.woloo.www.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import `in`.woloo.www.R




class CurvedCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rect = RectF()
    private var cornerRadius = 51f
    private var mPath: Path = Path()
    private var mPaint: Paint = Paint()
    private var mBorderPaint: Paint = Paint()
    private var CURVE_CIRCLE_RADIUS = 0
    private val CIRCLE_RADIUS = 256

    private val mFirstCurveStartPoint = Point()
    private val mFirstCurveEndPoint = Point()
    private val mFirstCurveControlPoint1 = Point()
    private val mFirstCurveControlPoint2 = Point()
    private var mSecondCurveStartPoint = Point()
    private val mSecondCurveEndPoint = Point()
    private val mSecondCurveControlPoint1 = Point()
    private val mSecondCurveControlPoint2 = Point()

    private var mNavigationBarWidth = 0
    private var mNavigationBarHeight = 0

    init {
        CURVE_CIRCLE_RADIUS = CIRCLE_RADIUS / context.resources.getInteger(R.integer.curve_radius)

        // Set up paint for the background
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#D9D9D9") // Background color

        // Set up paint for the border
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.color = Color.WHITE
        mBorderPaint.strokeWidth = 4f

        // Allow the background to be drawn
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
        // Get the width and height of the view
        mNavigationBarWidth = width
        mNavigationBarHeight = height


        // Update Y-coordinates to draw the curve at the bottom instead of the top
        val bottomY = mNavigationBarHeight

        // The coordinates (x,y) of the start point before the curve (bottom left)
        mFirstCurveStartPoint[mNavigationBarWidth / 2 - CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS / 3] =
            bottomY

        // The coordinates (x,y) of the end point after the curve (center bottom)
        mFirstCurveEndPoint[mNavigationBarWidth / 2] = bottomY - (CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 4)

        // Same thing for the second curve (bottom right)
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint[mNavigationBarWidth / 2 + CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS / 3] =
            bottomY

        // The coordinates (x,y) of the 1st control point on a cubic curve
        mFirstCurveControlPoint1[mFirstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 2] =
            mFirstCurveStartPoint.y
        // The coordinates (x,y) of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2[mFirstCurveEndPoint.x - CURVE_CIRCLE_RADIUS * 2 + CURVE_CIRCLE_RADIUS] =
            mFirstCurveEndPoint.y

        mSecondCurveControlPoint1[mSecondCurveStartPoint.x + CURVE_CIRCLE_RADIUS * 2 - CURVE_CIRCLE_RADIUS] =
            mSecondCurveStartPoint.y
        mSecondCurveControlPoint2[mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + CURVE_CIRCLE_RADIUS / 2)] =
            mSecondCurveEndPoint.y

        // Reset path and draw the bottom curve
        mPath!!.reset()
        mPath!!.moveTo(0f, 0f) // Start from the top-left corner
        mPath!!.lineTo(0f, bottomY.toFloat()) // Move to the bottom-left
        mPath!!.lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat()) // Move to start curve

        // Draw the cubic bezier curves
        mPath!!.cubicTo(
            mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat(),
            mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat(),
            mFirstCurveEndPoint.x.toFloat(), mFirstCurveEndPoint.y.toFloat()
        )

        mPath!!.cubicTo(
            mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat(),
            mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat(),
            mSecondCurveEndPoint.x.toFloat(), mSecondCurveEndPoint.y.toFloat()
        )

        // Finish the path at the bottom-right and close
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), bottomY.toFloat())
        mPath!!.lineTo(mNavigationBarWidth.toFloat(), 0f) // Move back to top-right
        mPath!!.lineTo(0f, 0f) // Move back to top-left
        mPath!!.close()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val clipPath = Path()
        clipPath.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(clipPath)

        canvas.drawPath(mPath, mPaint)
        canvas.drawPath(mPath, mBorderPaint)
    }
}
