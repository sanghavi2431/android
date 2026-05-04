package `in`.woloo.www.more.period_tracker.periodtracker.model

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.more.period_tracker.model.Log

class PeriodTrackerRequest {

    @SerializedName("period_date")
    var periodDate: String = ""

    @SerializedName("cycle_length")
    var cycleLenght: String = ""

    @SerializedName("period_length")
    var periodLength: String = ""

    @SerializedName("luteal_length")
    var lutealLength: String = ""

    @SerializedName("log")
    var log: Log = Log()
}