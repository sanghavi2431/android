package `in`.woloo.www.more.period_tracker.circular_calender

import android.graphics.Color

enum class PERIOD_TYPES(
    private val stringValue: String,
    private val intValue: Int,
    colorString: String,
    innerColorString: String
) {
    PERIOD("Normal", 0, "#f7eb30", "#F7C330"),
    MENSTRUATION("Menstruation", 1, "#f8646b", "#fee8e9"),
    OVULATION("Ovulation", 2, "#2abdc4", "#dff5f6"),
    PREGNANCY("High Fertility", 3, "#ed8524", "#fcedde");


    private val intColor = Color.parseColor(colorString)
    private val intInnerColor = Color.parseColor(innerColorString)

    override fun toString(): String {
        return stringValue
    }

    fun color(): Int {
        return intColor
    }

    fun innerColor(): Int {
        return intInnerColor
    }
}
