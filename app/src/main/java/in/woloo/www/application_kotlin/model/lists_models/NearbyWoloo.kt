package `in`.woloo.www.application_kotlin.model.lists_models
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse.Data.Offer


data class NearbyWoloo(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("opening_hours") var openingHours: String? = null,
    @SerializedName("restaurant") var restaurant: String? = null,
    @SerializedName("segregated") var segregated: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("is_covid_free") var isCovidFree: Int? = null,
    @SerializedName("is_safe_space") var isSafeSpace: Int? = null,
    @SerializedName("is_clean_and_hygiene") var isCleanAndHygiene: Int? = null,
    @SerializedName("is_sanitary_pads_available") var isSanitaryPadsAvailable: Int? = null,
    @SerializedName("is_makeup_room_available") var isMakeupRoomAvailable: Int? = null,
    @SerializedName("is_coffee_available") var isCoffeeAvailable: Int? = null,
    @SerializedName("is_sanitizer_available") var isSanitizerAvailable: Int? = null,
    @SerializedName("is_feeding_room") var isFeedingRoom: Int? = null,
    @SerializedName("is_wheelchair_accessible") var isWheelchairAccessible: Int? = null,
    @SerializedName("is_washroom") var isWashroom: Int? = null,
    @SerializedName("is_premium") var isPremium: Int? = null,
    @SerializedName("is_franchise") var isFranchise: Int? = null,
    @SerializedName("pincode") var pincode: Int? = null,
    @SerializedName("recommended_by") var recommendedBy: String? = null,
    @SerializedName("recommended_mobile") var recommendedMobile: String? = null,
    @SerializedName("distance") var distance: String?,
    @SerializedName("duration") var duration: String?,
    @SerializedName("user_rating") var userRating: String?,
    @SerializedName("offer") var offer: Offer
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("offer")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(openingHours)
        parcel.writeString(restaurant)
        parcel.writeString(segregated)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(lat)
        parcel.writeString(lng)
        parcel.writeValue(userId)
        parcel.writeValue(status)
        parcel.writeString(description)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(deletedAt)
        parcel.writeValue(isCovidFree)
        parcel.writeValue(isSafeSpace)
        parcel.writeValue(isCleanAndHygiene)
        parcel.writeValue(isSanitaryPadsAvailable)
        parcel.writeValue(isMakeupRoomAvailable)
        parcel.writeValue(isCoffeeAvailable)
        parcel.writeValue(isSanitizerAvailable)
        parcel.writeValue(isFeedingRoom)
        parcel.writeValue(isWheelchairAccessible)
        parcel.writeValue(isWashroom)
        parcel.writeValue(isPremium)
        parcel.writeValue(isFranchise)
        parcel.writeValue(pincode)
        parcel.writeString(recommendedBy)
        parcel.writeString(recommendedMobile)
        parcel.writeString(distance)
        parcel.writeString(duration)
        parcel.writeString(userRating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NearbyWoloo> {
        override fun createFromParcel(parcel: Parcel): NearbyWoloo {
            return NearbyWoloo(parcel)
        }

        override fun newArray(size: Int): Array<NearbyWoloo?> {
            return arrayOfNulls(size)
        }
    }

}