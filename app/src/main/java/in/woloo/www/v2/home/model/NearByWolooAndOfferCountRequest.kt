package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class NearByWolooAndOfferCountRequest {
    @SerializedName("lat")
    var lat : Double? = null

    @SerializedName("lng")
    var lng : Double? = null
}