package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.SerializedName

class EcomCoinUpdateResponse {

    @SerializedName("message")
    var message: String = ""

    @SerializedName("remaining_coins")
    var remaining_coins: Int = 0
}