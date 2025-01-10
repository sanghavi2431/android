package `in`.woloo.www.more.period_tracker.model

import com.google.gson.annotations.SerializedName

class GetPeriodDataResponse {
    @SerializedName("code")
    val code: String? = null

    @SerializedName("data")
    val data: Data? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class Data {
        @SerializedName("period_date")
        val periodDate: String? = null

        @SerializedName("cycle_lenght")
        val cycleLenght: Int = 0

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("user_id")
        val userId: Int = 0

        @SerializedName("log")
        val log: Log? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("period_length")
        val periodLength: Int = 0

        @SerializedName("luteal_length")
        val lutealLength: String? = null
    }
}