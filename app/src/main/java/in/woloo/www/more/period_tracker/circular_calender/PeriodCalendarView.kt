package `in`.woloo.www.more.period_tracker.circular_calender

import android.R
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PeriodCalendarView : View {
    private var height = 0
    private var width = 0
    private var padding = 0
    private var fontSize = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val days: MutableList<Int> = ArrayList()
    private val rect = Rect()
    private var buttonRect = RectF()
    private val boxPadding = 40
    private var periodType = PERIOD_TYPES.PERIOD
    private val TWO = 2
    private val todayColors = intArrayOf(Color.parseColor("#F7EB30"), Color.parseColor("#F7C330"))

    // Following line added By Aarati to Change color of Today dot @woloo on 22 July 2024
    private val todayColorsNew =
        intArrayOf(Color.parseColor("#414042"), Color.parseColor("#414042"))

    private var menstruationDays: List<Int> = mutableListOf()
    private var ovulationDays: List<Int> = mutableListOf()
    private var pregnancyDays: List<Int> = mutableListOf()
    private var currentMonth = 0
    private var currentYear = 0
    private var currentDay = 0
    private var periodDays = 3

    private var periodCalendarViewListener: PeriodCalendarViewListener? = null

    var colors: MutableList<Int> = ArrayList()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    fun initPeriodCalendar() {
        height = getHeight()
        width = getWidth()
        padding = (width * 0.05).toInt() //50;
        val fontSizeOffset = (width * 0.0125).toInt() //13;
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            fontSizeOffset.toFloat(),
            resources.displayMetrics
        ).toInt()
        val min = min(height.toDouble(), width.toDouble()).toInt()
        radius = min / TWO - padding
        paint = Paint()

        val c = Calendar.getInstance()
        currentYear = c[Calendar.YEAR]
        currentMonth = c[Calendar.MONTH]
        currentDay = c[Calendar.DATE]

        resetCalendar()

        isInit = true
    }

    private fun resetCalendar() {
        colors.clear()
        days.clear()

        val c: Calendar = GregorianCalendar(currentYear, currentMonth, 1)
        val daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)


        for (i in 1..daysInMonth) {
            days.add(i)
        }

        for (number in days) {
            var color = 0

            color = if (menstruationDays.indexOf(number) != -1) {
                PERIOD_TYPES.MENSTRUATION.color()
            } else if (pregnancyDays.indexOf(number) != -1) {
                PERIOD_TYPES.PREGNANCY.color()
            } else if (ovulationDays.indexOf(number) != -1) {
                PERIOD_TYPES.OVULATION.color()
            } else {
                PERIOD_TYPES.PERIOD.color()
            }

            colors.add(color)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val cx = event.x
        val cy = event.y

        if (buttonRect.contains(cx, cy)) {
            if (periodCalendarViewListener != null) {
                periodCalendarViewListener!!.onEdit()
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initPeriodCalendar()
        }

        val innerCircleOffset = (width * 0.0833).toInt() //90

        canvas.drawColor(Color.WHITE)
        drawArcs(canvas)
        drawCircle(canvas, 6, boxPadding - 1, resources.getColor(R.color.white))
        drawCircle(canvas, 7, innerCircleOffset + boxPadding, resources.getColor(R.color.white))
        drawCenter(canvas)
        drawDays(canvas)
        drawPeriodCircle(canvas, periodType)
        drawTitle(canvas)
        drawDaysTitle(canvas)
        //        drawPeriodTitles(canvas); //TODO: need to implement it properly
        drawButton(canvas)

        //        postInvalidateDelayed(500); //TODO: Do not uncomment it
        invalidate()
    }

    @Deprecated("")
    private fun drawPeriodTitles(canvas: Canvas) {
        val fontSizeOffset = (width * 0.0125).toInt() //12;
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            fontSizeOffset.toFloat(),
            resources.displayMetrics
        ).toInt()
        paint!!.reset()
        paint!!.textSize = fontSize.toFloat()
        //        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        try {
            val typeface = ResourcesCompat.getFont(
                context, `in`.woloo.www.R.font.inter_semibold
            )
            paint!!.setTypeface(typeface)
        } catch (e: Exception) {
        }
        paint!!.color = resources.getColor(R.color.black)
        paint!!.style = Paint.Style.STROKE
        var offset = 150f
        val sweepAngle = 64.28f
        var enclosingRect = RectF(offset, offset, width - offset, height - offset)

        //TODO: only for debugging purpose
//        float cx = width / TWO; //Center of the circle
//        float cy = height / TWO; //Center of the circle
//
//        canvas.drawCircle(cx, cy, (radius + padding - offset) , paint);
        paint!!.style = Paint.Style.FILL

        val path = Path()
        path.addArc(enclosingRect, 106.28f, -sweepAngle)
        canvas.drawTextOnPath(PERIOD_TYPES.OVULATION.toString(), path, 0f, 0f, paint!!)

        offset = 175f
        enclosingRect = RectF(offset, offset, width - offset, height - offset)

        //TODO: only for debugging purpose
//        canvas.drawCircle(cx, cy, (radius + padding - offset) , paint);
        path.reset()
        path.addArc(enclosingRect, 174.28f, sweepAngle)
        canvas.drawTextOnPath(PERIOD_TYPES.PREGNANCY.toString(), path, 0f, 0f, paint!!)

        path.reset()
        path.addArc(enclosingRect, -83.28f, sweepAngle)
        canvas.drawTextOnPath(PERIOD_TYPES.MENSTRUATION.toString(), path, 0f, 0f, paint!!)
    }

    private fun drawPeriodCircle(canvas: Canvas, type: PERIOD_TYPES) {
        val circleOffset = (width * 0.085).toInt()
        val innerCircleOffset = (width * 0.070).toInt()
        val paddingOffset = circleOffset + boxPadding //94
        paint!!.reset()

        paint!!.color = type.color()
        paint!!.style = Paint.Style.FILL
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / TWO).toFloat(),
            (height / TWO).toFloat(),
            (radius + padding - paddingOffset).toFloat(),
            paint!!
        )

        paint!!.color = type.innerColor()
        canvas.drawCircle(
            (width / TWO).toFloat(),
            (height / TWO).toFloat(),
            (radius + padding - (paddingOffset + innerCircleOffset)).toFloat(),
            paint!!
        )


        //Draw background
        val bg_options = BitmapFactory.Options()
        bg_options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, `in`.woloo.www.R.drawable.pt_background, bg_options)

        val bitmapWidth = bg_options.outWidth
        val bitmapHeight = bg_options.outHeight

        val topOffset = (width * 0.01).toInt()
        val widthOffset = (width * 0.07).toInt()
        val heightOffset = (height * 0.05).toInt()

        var left = (width - bitmapWidth) / TWO
        var top = (height - bitmapHeight) / TWO

        left -= widthOffset
        top += topOffset //50

        var right = left + bitmapWidth + (widthOffset * 2)
        var bottom = top + bitmapHeight + (heightOffset * 2)

        val bg_bitmap = BitmapFactory.decodeResource(
            resources, `in`.woloo.www.R.drawable.pt_background
        )
        canvas.drawBitmap(bg_bitmap, null, Rect(left, top, right, bottom), paint)

        //Draw background

        //Draw icon
        var icon = 0
        val icon_options = BitmapFactory.Options()
        icon_options.inJustDecodeBounds = true
        when (type) {
            PERIOD_TYPES.MENSTRUATION -> icon = `in`.woloo.www.R.drawable.menstrual_cup
            PERIOD_TYPES.OVULATION -> icon = `in`.woloo.www.R.drawable.fertilization
            PERIOD_TYPES.PREGNANCY -> icon = `in`.woloo.www.R.drawable.fetus
            PERIOD_TYPES.PERIOD -> TODO()
        }

        if (icon > 0) {
            BitmapFactory.decodeResource(resources, icon, icon_options)

            val iconWidthOffset = (width * 0.04).toInt() //10
            var offset = 0.05
            if (type == PERIOD_TYPES.PREGNANCY || type == PERIOD_TYPES.PERIOD) {
                offset = 0.04
            }
            val iconHeightOffset = (height * offset).toInt() //10
            val iconTopOffset = (height * 0.17).toInt() //100
            val iconWidth = icon_options.outWidth + iconWidthOffset
            val iconHeight = icon_options.outHeight + iconHeightOffset

            left = (width - iconWidth) / TWO
            top = radius + padding - (paddingOffset + iconTopOffset)

            right = left + iconWidth
            bottom = top + iconHeight

            val icon_bitmap = BitmapFactory.decodeResource(
                resources, icon
            )
            canvas.drawBitmap(icon_bitmap, null, Rect(left, top, right, bottom), paint)
        }

        //Draw icon
    }

    private fun drawArcs(canvas: Canvas) {
        val box = RectF(
            boxPadding.toFloat(),
            boxPadding.toFloat(),
            (width - boxPadding).toFloat(),
            (height - boxPadding).toFloat()
        )

        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeWidth = 5f

        var start = -90f
        val angle = (360.0f / days.size.toFloat()) * 1f
        var index = 0

        var menstruationStartAngle = 0f
        var ovulationStartAngle = 0f
        var pregnancyStartAngle = 0f

        if (days.size > 0) {
            for (number in days) {
                paint!!.color = colors[index]
                paint!!.style = Paint.Style.FILL_AND_STROKE
                canvas.drawArc(box, start, angle, true, paint!!)
                paint!!.color = resources.getColor(R.color.white)
                paint!!.style = Paint.Style.STROKE
                canvas.drawArc(box, start, angle, true, paint!!)

                if (menstruationDays.indexOf(number) != -1 && menstruationStartAngle == 0f) {
                    menstruationStartAngle = start
                } else if (pregnancyDays.indexOf(number) != -1 && pregnancyStartAngle == 0f) {
                    pregnancyStartAngle = start
                } else if (ovulationDays.indexOf(number) != -1 && ovulationStartAngle == 0f) {
                    ovulationStartAngle = start
                }

                start += angle
                index++
            }
        }

        //        Log.v("Start Menstruation",String.valueOf(menstruationStartAngle));
//        Log.v("Start Ovulation",String.valueOf(ovulationStartAngle));
//        Log.v("Start Pregnancy",String.valueOf(pregnancyStartAngle));
    }

    private fun drawDays(canvas: Canvas) {
        paint!!.reset()
        paint!!.textSize = fontSize.toFloat()
        try {
            val typeface = ResourcesCompat.getFont(
                context, `in`.woloo.www.R.font.inter_semibold
            )
            paint!!.setTypeface(typeface)
        } catch (e: Exception) {
        }
        //        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint!!.color = resources.getColor(R.color.black)

        //        28 = + 0.10
//        29 = + 0.05
//        30 = - 0.01
//        31 = - 0.05
        var offset = 0.0f
        when (days.size) {
            28 -> offset = 0.10f
            29 -> offset = 0.05f
            30 -> offset = -0.01f
            31 -> offset = -0.05f
        }

        val min = min(height.toDouble(), width.toDouble()).toInt()
        val lineRadius = min / TWO - 10

        var cx = 0f
        var cy = 0f

        for (number in days) {
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = (Math.PI / (days.size.toFloat() / TWO) * (number - 8)) + offset
            val x =
                (width / TWO + cos(angle) * (radius - boxPadding + 5) - rect.width() / TWO).toInt()
            val y =
                (height / TWO + sin(angle) * (radius - boxPadding + 5) + rect.height() / TWO).toInt()
            paint!!.color = resources.getColor(R.color.black)
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)

            if (number == currentDay) {
                cx = (width / TWO + cos(angle) * lineRadius).toFloat()
                cy = (height / TWO + sin(angle) * lineRadius).toFloat()
            }
        }

        //Draw Today
        if (cx > 0 && cy > 0) {
            drawToday(canvas, 10, todayColorsNew[0], todayColorsNew[1], cx, cy)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint!!.style = Paint.Style.FILL
        canvas.drawCircle((width / TWO).toFloat(), (height / TWO).toFloat(), 12f, paint!!)
    }

    private fun drawCircle(canvas: Canvas, strokeWidth: Int, paddingOffset: Int, color: Int) {
        paint!!.reset()
        paint!!.color = color
        paint!!.strokeWidth = strokeWidth.toFloat()
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / TWO).toFloat(),
            (height / TWO).toFloat(),
            (radius + padding - paddingOffset).toFloat(),
            paint!!
        )
    }

    private fun drawToday(
        canvas: Canvas,
        radius: Int,
        startColor: Int,
        endColor: Int,
        cx: Float,
        cy: Float
    ) {
        val shader: Shader = LinearGradient(
            0f,
            cy,
            0f,
            radius.toFloat(),
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
        paint!!.setShader(shader)
        canvas.drawCircle(cx, cy, radius.toFloat(), paint!!)
    }

    private fun drawTitle(canvas: Canvas) {
        val fontSizeOffset = (width * 0.0125).toInt() //12;
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            fontSizeOffset.toFloat(),
            resources.displayMetrics
        ).toInt()
        paint!!.reset()
        paint!!.textSize = fontSize.toFloat()
        try {
            val typeface = ResourcesCompat.getFont(
                context, `in`.woloo.www.R.font.inter_regular
            )
            paint!!.setTypeface(typeface)
        } catch (e: Exception) {
        }
        paint!!.color = resources.getColor(R.color.black)

        val title = String.format("%s Cycle", periodType.toString())

        paint!!.getTextBounds(title, 0, title.length, rect)

        val cx = (width - rect.width()) / TWO
        val cy = (height - rect.height()) / TWO
        val topOffset = (height * 0.06).toInt() //65
        canvas.drawText(title, cx.toFloat(), (cy - topOffset).toFloat(), paint!!)
    }

    private fun drawDaysTitle(canvas: Canvas) {
        val fontSizeOffset = (width * 0.028).toInt() //30;
        val fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            fontSizeOffset.toFloat(),
            resources.displayMetrics
        ).toInt()
        paint!!.reset()
        paint!!.textSize = fontSize.toFloat()
        try {
            val typeface = ResourcesCompat.getFont(
                context, `in`.woloo.www.R.font.inter_bold
            )
            paint!!.setTypeface(typeface)
        } catch (e: Exception) {
//             CommonUtils.printStackTrace(e)
        }
        //        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        paint!!.color = periodType.color()

        val title = "Day $periodDays"

        paint!!.getTextBounds(title, 0, title.length, rect)

        val cx = (width - rect.width()) / TWO
        val cy = (height - rect.height()) / TWO
        val topOffset = (height * 0.0555).toInt() //60
        canvas.drawText(title, cx.toFloat(), (cy + topOffset).toFloat(), paint!!)
    }

    private fun drawButton(canvas: Canvas) {
        val buttonSize = (width * 0.09).toInt()
        val topOffset = (height * 0.09).toInt()
        val buttonRadius = 25
        val cx = (width - buttonSize) / TWO
        var cy = (height - buttonSize) / TWO

        cy += topOffset

        val right = cx + buttonSize
        val bottom = cy + buttonSize

        buttonRect = RectF(cx.toFloat(), cy.toFloat(), right.toFloat(), bottom.toFloat())

        val shader: Shader = LinearGradient(
            0f, buttonRect.bottom, 0f, buttonRect.top,
            todayColors[0],
            todayColors[1], Shader.TileMode.CLAMP
        )
        val paint = Paint()
        paint.setShader(shader)
        canvas.drawRoundRect(buttonRect, buttonRadius.toFloat(), buttonRadius.toFloat(), paint)

        val bg_options = BitmapFactory.Options()
        bg_options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, `in`.woloo.www.R.drawable.pencil, bg_options)

        val bitmapWidth = bg_options.outWidth
        val bitmapHeight = bg_options.outHeight
        var offset = bitmapWidth
        if (bitmapHeight > bitmapWidth) {
            offset = bitmapHeight
        }

        val iconOffset = (offset * 0.7).toInt()

        val buttonIconRect = RectF(
            (cx + iconOffset).toFloat(),
            (cy + iconOffset).toFloat(),
            (right - iconOffset).toFloat(),
            (bottom - iconOffset).toFloat()
        )

        val bg_bitmap = BitmapFactory.decodeResource(
            resources, `in`.woloo.www.R.drawable.pencil
        )
        canvas.drawBitmap(bg_bitmap, null, buttonIconRect, paint)
    }

    fun setCalendar(day: Int, month: Int, year: Int) {
        currentMonth = month
        currentYear = year
        currentDay = day

        resetCalendar()

        invalidate()
        requestLayout()
    }

    fun setPeriodType(type: PERIOD_TYPES) {
        periodType = type

        invalidate()
        requestLayout()
    }

    fun getPeriodType(): PERIOD_TYPES {
        return periodType
    }

    fun setPeriodCycle(menstruation: List<Int>, ovulation: List<Int>, pregnancy: List<Int>) {
        menstruationDays = menstruation
        ovulationDays = ovulation
        pregnancyDays = pregnancy

        invalidate()
        requestLayout()
    }

    fun setPeriodCalendarViewListener(listener: PeriodCalendarViewListener?) {
        periodCalendarViewListener = listener
    }

    fun setPeriodDays(days: Int) {
        periodDays = days
        invalidate()
        requestLayout()
    }
}
