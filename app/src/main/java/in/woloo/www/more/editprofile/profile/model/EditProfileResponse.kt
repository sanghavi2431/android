package `in`.woloo.www.more.editprofile.profile.model

import com.google.gson.annotations.SerializedName

class EditProfileResponse {

    @SerializedName("wolooGuest")
    var wolooGuest: WolooGuest = WolooGuest()

}

class WolooGuest {
    @SerializedName("Response")
    var response: String = ""
}