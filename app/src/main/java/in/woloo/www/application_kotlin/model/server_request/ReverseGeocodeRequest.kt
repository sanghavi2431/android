package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

class ReverseGeocodeRequest {
    @SerializedName("lat")
    var lat: Double = 0.0

    @SerializedName("lng")
    var lng: Double = 0.0

}