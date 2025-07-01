package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class NearbyWolooRequest(

    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lng") var lng: Double? = null,
    @SerializedName("mode") var mode: Int? = null,
    @SerializedName("range") var range: Int? = null,
    @SerializedName("package_name") var packageName: String? = null,
    @SerializedName("page") var page: String? = null,
    @SerializedName("isSearch") var isSearch: Int? = null,
    @SerializedName("is_offer") var isOffer: Int? = null,
    @SerializedName("showAll") var showAll: Int? = null

)
