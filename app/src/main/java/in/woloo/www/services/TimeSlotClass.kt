package `in`.woloo.www.services

import java.time.LocalDate

data class TimeSlotClass(
    val slotStartTime: String,
    val slotEndTime: String
)

data class  SelectedDateTimeClass
    (
            val selectedDate: LocalDate,
    val selectedTimeSlots: TimeSlotClass,

            )