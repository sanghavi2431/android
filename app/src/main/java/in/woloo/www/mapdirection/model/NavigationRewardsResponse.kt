package `in`.woloo.www.mapdirection.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NavigationRewardsResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}
