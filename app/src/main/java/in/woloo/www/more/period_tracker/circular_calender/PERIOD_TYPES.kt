package `in`.woloo.www.more.period_tracker.circular_calender

import android.graphics.Color
import `in`.woloo.www.R

enum class PERIOD_TYPES(
    private val stringValue: String,
    private val intValue: Int,
    colorString: String,
    innerColorString: String
) {
    PERIOD("Normal", 0, "#FAED10", "#F7C330"),
    MENSTRUATION("Menstruation", 1, "#F1779B", "#fee8e9"),
    OVULATION("Ovulation", 2, "#B0C8E8", "#dff5f6"),
    PREGNANCY("High Fertility", 3, "#DFA7CC", "#fcedde");


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
