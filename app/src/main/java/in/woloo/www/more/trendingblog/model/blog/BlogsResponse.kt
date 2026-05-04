package `in`.woloo.www.more.trendingblog.model.blog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogsResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmField
    @SerializedName("data")
    @Expose
    var data: Data? = null
}
