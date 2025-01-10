package `in`.woloo.www.more.subscribe.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InitSubscriptionResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @JvmField
    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmField
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @JvmField
        @SerializedName("subscription_id")
        @Expose
        var subscriptionId: String? = null
    }
}
