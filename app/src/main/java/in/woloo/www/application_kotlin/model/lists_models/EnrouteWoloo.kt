package `in`.woloo.www.application_kotlin.model.lists_models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse

class EnrouteWoloo {

    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("code")
    @Expose
     val code: String? = null

    @SerializedName("name")
    @Expose
     val name: String? = null

    @SerializedName("title")
    @Expose
     val title: String? = null
    

    @SerializedName("opening_hours")
    @Expose
     val openingHours: String? = null

    @SerializedName("restaurant")
    @Expose
     val restaurant: String? = null

    @SerializedName("segregated")
    @Expose
     val segregated: String? = null

    @SerializedName("address")
    @Expose
     val address: String? = null

    @SerializedName("city")
    @Expose
     val city: String? = null

    @SerializedName("lat")
    @Expose
     val lat: String? = null

    @SerializedName("lng")
    @Expose
     val lng: String? = null

    @SerializedName("user_id")
    @Expose
     val userId: Int? = null

    @SerializedName("status")
    @Expose
     val status: Int? = null

    @SerializedName("description")
    @Expose
     val description: String? = null

    @SerializedName("created_at")
    @Expose
     val createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
     val updatedAt: String? = null

    @SerializedName("deleted_at")
    @Expose
     val deletedAt: Any? = null

    @SerializedName("is_covid_free")
    @Expose
     val isCovidFree: Int? = null

    @SerializedName("is_safe_space")
    @Expose
     val isSafeSpace: Int? = null

    @SerializedName("is_clean_and_hygiene")
    @Expose
     val isCleanAndHygiene: Int? = null

    @SerializedName("is_sanitary_pads_available")
    @Expose
     val isSanitaryPadsAvailable: Int? = null

    @SerializedName("is_makeup_room_available")
    @Expose
     val isMakeupRoomAvailable: Int? = null

    @SerializedName("is_coffee_available")
    @Expose
     val isCoffeeAvailable: Int? = null

    @SerializedName("is_sanitizer_available")
    @Expose
     val isSanitizerAvailable: Int? = null

    @SerializedName("is_feeding_room")
    @Expose
     val isFeedingRoom: Int? = null

    @SerializedName("is_wheelchair_accessible")
    @Expose
     val isWheelchairAccessible: Int? = null

    @SerializedName("is_washroom")
    @Expose
     val isWashroom: Int? = null

    @SerializedName("is_premium")
    @Expose
     val isPremium: Int? = null

    @SerializedName("distance")
    @Expose
     val distance: String? = null

    @SerializedName("duration")
    @Expose
     val duration: String? = null

    @SerializedName("user_rating")
    @Expose
     val userRating: String? = null

    @SerializedName("user_review_count")
    @Expose
     val userReviewCount: Int? = null

    @SerializedName("offer")
    @Expose
     val offer: NearByStoreResponse.Data.Offer? = null

    @SerializedName("base_url")
    @Expose
     val baseUrl: String? = null

    @SerializedName("is_liked")
    @Expose
     val isLiked = 0

    @SerializedName("cibil_score")
    @Expose
     val cibilScore = 0

    @SerializedName("cibil_score_image")
    @Expose
     val cibilScoreImage = ""

    @SerializedName("cibil_score_colour")
    @Expose
     val cibilScoreColour = ""
}