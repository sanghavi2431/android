package `in`.woloo.www.v2.invite.model

import com.google.gson.annotations.SerializedName

class InviteRequest {
    @SerializedName("mobile_numbers")
    var mobileNumbers = ArrayList<String>()
}