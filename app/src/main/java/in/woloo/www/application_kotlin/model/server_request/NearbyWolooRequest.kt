package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
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


data class SearchWolooRequest(

    @SerializedName("query") var query: String? = null,
    @SerializedName("pagination") var pagination: Pagination? = null,
    @SerializedName("location") var location : LocationForRate? = null


)

data class Pagination(

    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("page") var page: Int? = null

)

data class NearByStoreResultsWrapper(
    @SerializedName("data")
    val data: ArrayList<NearByStoreResponse.DataSearch>? = arrayListOf()
)

data class LocationForRate(

    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null

)
