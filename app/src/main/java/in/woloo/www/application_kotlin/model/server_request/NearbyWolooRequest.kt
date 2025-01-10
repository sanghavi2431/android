package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
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