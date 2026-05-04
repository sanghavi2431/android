package `in`.woloo.www.more.period_tracker.model

import com.google.gson.annotations.SerializedName

class PeriodTrackerResponse {
    @SerializedName("code")
    var code: String? = null

    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    @JvmField
	@SerializedName("status")
    var status: String? = null

    inner class Data {
        @JvmField
		@SerializedName("period_date")
        var periodDate: String? = null

        @JvmField
		@SerializedName("cycle_length")
        var cycleLenght: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("user_id")
        var userId: Int = 0

        @JvmField
		@SerializedName("log")
        var log: Log? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("id")
        var id: Int = 0

        @JvmField
		@SerializedName("period_length")
        var periodLength: String? = null

        @SerializedName("luteal_length")
        var lutealLength: String? = null
    }
}