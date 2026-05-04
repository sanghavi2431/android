package `in`.woloo.www.more.fragments.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InviteFriendsResponse {
    @kotlin.jvm.JvmField
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}

