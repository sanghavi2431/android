package `in`.woloo.www.application_kotlin.model.lists_models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class UserDetails(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("role_id") var roleId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("remember_token") var rememberToken: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("fb_id") var fbId: String? = null,
    @SerializedName("gp_id") var gpId: String? = null,
    @SerializedName("ref_code") var refCode: String? = null,
    @SerializedName("sponsor_id") var sponsorId: String? = null,
    @SerializedName("woloo_id") var wolooId: String? = null,
    @SerializedName("subscription_id") var subscriptionId: String? = null,
    @SerializedName("expiry_date") var expiryDate: String? = null,
    @SerializedName("voucher_id") var voucherId: String? = null,
    @SerializedName("gift_subscription_id") var giftSubscriptionId: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null,
    @SerializedName("otp") var otp: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("settings") var settings: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("is_first_session") var isFirstSession: Int? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("is_thirst_reminder") var isThirstReminder: Int? = null,
    @SerializedName("thirst_reminder_hours") var thirstReminderHours: String? = null,
    @SerializedName("is_blog_content_notification") var isBlogContentNotification: Int? = null,
    @SerializedName("isFreeTrial") var isFreeTrial: Int? = 0,
    @SerializedName("IsVtionUser") var isVtionUser: String? = null

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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(roleId)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(rememberToken)
        parcel.writeString(mobile)
        parcel.writeString(city)
        parcel.writeString(pincode)
        parcel.writeString(address)
        parcel.writeString(avatar)
        parcel.writeString(fbId)
        parcel.writeString(gpId)
        parcel.writeString(refCode)
        parcel.writeString(sponsorId)
        parcel.writeString(wolooId)
        parcel.writeString(subscriptionId)
        parcel.writeString(expiryDate)
        parcel.writeString(voucherId)
        parcel.writeString(giftSubscriptionId)
        parcel.writeString(lat)
        parcel.writeString(lng)
        parcel.writeValue(otp)
        parcel.writeString(status)
        parcel.writeString(settings)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(deletedAt)
        parcel.writeString(gender)
        parcel.writeValue(isFirstSession)
        parcel.writeString(dob)
        parcel.writeValue(isThirstReminder)
        parcel.writeString(thirstReminderHours)
        parcel.writeValue(isBlogContentNotification)
        parcel.writeString(isVtionUser)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetails> {
        override fun createFromParcel(parcel: Parcel): UserDetails {
            return UserDetails(parcel)
        }

        override fun newArray(size: Int): Array<UserDetails?> {
            return arrayOfNulls(size)
        }
    }
}