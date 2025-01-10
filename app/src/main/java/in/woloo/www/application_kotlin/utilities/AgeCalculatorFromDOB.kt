package `in`.woloo.www.application_kotlin.utilities

import java.util.Calendar
import java.util.Date

object AgeCalculatorFromDOB {
    @JvmStatic
    fun calculateAge(birthDate: Date?): Int {
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = birthDate
        val currentCalendar = Calendar.getInstance()
        var years = currentCalendar[Calendar.YEAR] - birthCalendar[Calendar.YEAR]

        // Adjust age when the current date is before the birth date (e.g., just after New Year for a birthday in December)
        if (currentCalendar[Calendar.DAY_OF_YEAR] < birthCalendar[Calendar.DAY_OF_YEAR]) {
            years--
        }
        return years
    }
}
