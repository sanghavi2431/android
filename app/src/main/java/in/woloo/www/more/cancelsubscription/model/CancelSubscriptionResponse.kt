package `in`.woloo.www.more.cancelsubscription.model

import com.google.gson.annotations.SerializedName

class CancelSubscriptionResponse {
    @SerializedName("code")
    var code: Int = 0

    @SerializedName("message")
    var message: String? = null

    @kotlin.jvm.JvmField
	@SerializedName("status")
    var status: String? = null
}