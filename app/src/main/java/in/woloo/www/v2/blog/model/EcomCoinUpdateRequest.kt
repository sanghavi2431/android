package `in`.woloo.www.v2.blog.model

import com.google.gson.annotations.SerializedName

class EcomCoinUpdateRequest {

    @SerializedName("type")
    var type: String = ""

    @SerializedName("coins")
    var coins: Int = 0

    @SerializedName("orderid")
    var orderId: String = ""
}
