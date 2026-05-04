package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.SerializedName

data class PurchaseNowResponse (
    @SerializedName("order_id")
    val orderId: String,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("currency")
    val currency: String
)