package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

class NearByWolooAndOfferCountRequest {
    @SerializedName("lat")
    var lat : Double? = null

    @SerializedName("lng")
    var lng : Double? = null
}