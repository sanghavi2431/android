package `in`.woloo.www.more.giftcard.model

import com.google.gson.annotations.SerializedName

class RequestPointsResponse {
    @SerializedName("data")
    val data: Data? = null

    inner class Data {
        @SerializedName("message")
        val message: String? = null

        @SerializedName("order_id")
        val orderId: String? = null

        @SerializedName("status")
        val status: String? = null
    }
}