package `in`.woloo.www.more.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserCoinsResponse {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("total_coins")
        @Expose
        var totalCoins: Int? = null

        @SerializedName("gift_coins")
        @Expose
        var giftCoins: Int? = null
    }
}
