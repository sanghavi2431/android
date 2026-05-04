package `in`.woloo.www.more.period_tracker


data class CalendarDay(
    val day: Int?,         // The day of the month (nullable for blank spaces)
    val dots: List<Int>    // List of color resource IDs for the dots under each day
)
