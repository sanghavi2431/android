package `in`.woloo.www.v2.thirstReminder.model

import com.google.gson.annotations.SerializedName

class ThirstReminderResponse {

    @SerializedName("is_thirst_reminder")
    var isThirstReminder : Int? = 0

    @SerializedName("thirst_reminder_hours")
    var thirstReminderHours : Int? = null
}