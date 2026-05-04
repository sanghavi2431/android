package `in`.woloo.www.more.subscribe.subscription.model

import com.google.gson.annotations.SerializedName

class InitSubscriptionRequest {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("plan_id")
    var planId: String = ""

    @SerializedName("future")
    var future: Boolean = false

    @SerializedName("locale")
    var locale: Locale = Locale()

    class Locale {
        @SerializedName("country")
        var country: String = "India"

        @SerializedName("actualCountry")
        var actualCountry: String = "India"

        @SerializedName("platform")
        var platform: String = "android"
    }
}