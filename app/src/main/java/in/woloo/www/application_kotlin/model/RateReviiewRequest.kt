package `in`.woloo.www.application_kotlin.model

import com.google.gson.annotations.SerializedName

class RateReviiewRequest {
    @SerializedName("rating")
    var rating: Int = 0

    @SerializedName("review_description")
    var reviewDescription: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("address")
    var address: String = ""

    @SerializedName("city")
    var city: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("lng")
    var lng: String = ""

    @SerializedName("pincode")
    var pincode: String = ""
}



