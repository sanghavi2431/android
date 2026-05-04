package `in`.woloo.www.more.subscribe.subscription.model

import com.google.gson.annotations.SerializedName

class SubmitSubscriptionPurchaseRequest {


    @SerializedName("plan_id")
    var planId: String = ""

    @SerializedName("payment_id")
    var paymentId: String? = null

    @SerializedName("order_id")
    var orderId: String? = null

    @SerializedName("payment_signature")
    var paymentSignature: String? = null


    @SerializedName("future")
    var future: Boolean = false

    @SerializedName("userGiftPoints")
    var userGiftPoints: Boolean = false
}


class SubmitGiftCardSubscriptionPurchaseRequest {


    @SerializedName("plan_id")
    var planId: String = ""

    @SerializedName("future")
    var future: Int = 0

}