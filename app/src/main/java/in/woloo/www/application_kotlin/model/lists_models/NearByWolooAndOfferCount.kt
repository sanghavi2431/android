package `in`.woloo.www.application_kotlin.model.lists_models

import com.google.gson.annotations.SerializedName

class NearByWolooAndOfferCount {
    @SerializedName("wolooCount")
    var wolooCount : Int = 0

    @SerializedName("offerCount")
    var offerCount : Int = 0

    @SerializedName("shopOffer")
    var shopOffer : ArrayList<Any> = ArrayList()
}