package `in`.woloo.www.more.period_tracker.model

import com.google.gson.annotations.SerializedName

class Log {
    @SerializedName("sexDrive")
    var sexDrive: List<String>? = null

    @SerializedName("habits")
    var habits: List<String>? = null

    @SerializedName("bleeding")
    var bleeding: List<String>? = null

    @SerializedName("mood")
    var mood: List<String>? = null

    @SerializedName("menstruation")
    var menstruation: List<String>? = null

    @SerializedName("diseasesandmedication")
    var diseasesandmedication: List<String>? = null

    @SerializedName("premenstruation")
    var premenstruation: List<String>? = null
}