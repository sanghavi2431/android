package `in`.woloo.www.v2.giftcard.model

import com.google.gson.annotations.SerializedName

class CoinHistoryRequest {
    @SerializedName("pageIndex")
    var pageNumber: Int = 1
}
