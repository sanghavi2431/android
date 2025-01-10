package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.SerializedName

class EcomCoinUpdateRequest {

    @SerializedName("type")
    var type: String = ""

    @SerializedName("coins")
    var coins: Int = 0

    @SerializedName("orderid")
    var orderId: String = ""
}