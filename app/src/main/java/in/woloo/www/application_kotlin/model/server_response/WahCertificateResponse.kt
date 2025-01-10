package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.SerializedName

class WahCertificateResponse {
    @SerializedName("code")
     var code: String? = null

    @SerializedName("city")
     var city: String? = null

    @SerializedName("description")
     var description: String? = null

    @SerializedName("created_at")
     var createdAt: String? = null

    @SerializedName("title")
     var title: Any? = null

    @SerializedName("is_safe_space")
     var isSafeSpace = 0

    @SerializedName("updated_at")
     var updatedAt: String? = null

    @SerializedName("is_feeding_room")
     var isFeedingRoom = 0

    @SerializedName("recommended_by")
     var recommendedBy: Any? = null

    @SerializedName("id")
     var id = 0

    @SerializedName("is_sanitizer_available")
     var isSanitizerAvailable = 0

    @SerializedName("lat")
     var lat: String? = null

    @SerializedName("image")
     var image: Any? = null

    @SerializedName("pincode")
     var pincode: Any? = null

    @SerializedName("address")
     var address: String? = null

    @SerializedName("lng")
     var lng: String? = null

    @SerializedName("is_makeup_room_available")
     var isMakeupRoomAvailable = 0

    @SerializedName("restaurant")
     var restaurant: Any? = null

    @SerializedName("is_clean_and_hygiene")
     var isCleanAndHygiene = 0

    @SerializedName("is_washroom")
     var isWashroom = 0

    @SerializedName("deleted_at")
     var deletedAt: Any? = null

    @SerializedName("is_coffee_available")
     var isCoffeeAvailable = 0

    @SerializedName("is_wheelchair_accessible")
     var isWheelchairAccessible = 0

    @SerializedName("is_sanitary_pads_available")
     var isSanitaryPadsAvailable = 0

    @SerializedName("is_franchise")
     var isFranchise = 0

    @SerializedName("is_premium")
     var isPremium = 0

    @SerializedName("user_id")
     var userId: Any? = null

    @SerializedName("name")
     var name: String? = null

    @SerializedName("opening_hours")
     var openingHours: Any? = null

    @SerializedName("recommended_mobile")
     var recommendedMobile: Any? = null

    @SerializedName("segregated")
     var segregated: String? = null

    @SerializedName("status")
     var status = 0

    @SerializedName("is_covid_free")
     var isCovidFree = 0
}