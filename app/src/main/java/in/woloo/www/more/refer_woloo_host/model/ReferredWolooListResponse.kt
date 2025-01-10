package `in`.woloo.www.more.refer_woloo_host.model

import com.google.gson.annotations.SerializedName

class ReferredWolooListResponse {
    @SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class DataItem {
        @SerializedName("code")
        val code: String? = null

        @SerializedName("city")
        val city: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("title")
        val title: String? = null

        @SerializedName("is_safe_space")
        val isSafeSpace = 0

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("is_feeding_room")
        val isFeedingRoom = 0

        @SerializedName("recommended_by")
        val recommendedBy = 0

        @SerializedName("id")
        val id = 0

        @SerializedName("is_sanitizer_available")
        val isSanitizerAvailable = 0

        @SerializedName("lat")
        val lat: String? = null

        @SerializedName("image")
        val image: List<String>? = null

        @SerializedName("pincode")
        val pincode = 0

        @SerializedName("address")
        val address: String? = null

        @SerializedName("lng")
        val lng: String? = null

        @SerializedName("is_makeup_room_available")
        val isMakeupRoomAvailable = 0

        @SerializedName("restaurant")
        val restaurant: String? = null

        @SerializedName("is_clean_and_hygiene")
        val isCleanAndHygiene = 0

        @SerializedName("is_washroom")
        val isWashroom = 0

        @SerializedName("deleted_at")
        val deletedAt: String? = null

        @SerializedName("is_coffee_available")
        val isCoffeeAvailable = 0

        @SerializedName("is_wheelchair_accessible")
        val isWheelchairAccessible = 0

        @SerializedName("is_sanitary_pads_available")
        val isSanitaryPadsAvailable = 0

        @SerializedName("is_franchise")
        val isFranchise = 0

        @SerializedName("is_premium")
        val isPremium = 0

        @SerializedName("user_id")
        val userId: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("opening_hours")
        val openingHours: String? = null

        @SerializedName("recommended_mobile")
        val recommendedMobile: String? = null

        @SerializedName("segregated")
        val segregated: String? = null

        @SerializedName("status")
        val status = 0

        @SerializedName("is_covid_free")
        val isCovidFree = 0
    }
}