package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class NearByWolooAndOfferCount {
    @SerializedName("wolooCount")
    var wolooCount: Int = 0

    @SerializedName("offerCount")
    var offerCount: Int = 0

    @SerializedName("shopOffer")
    var shopOffer: ArrayList<Any> = ArrayList()
}
