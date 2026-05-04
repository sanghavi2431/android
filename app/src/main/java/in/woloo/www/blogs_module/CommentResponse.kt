package `in`.woloo.www.blogs_module

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.more.subscribe.models.PlanResponse.Data

class CommentResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    inner class Data {

        @SerializedName("comment_id")
        val commentId: String = ""

        @SerializedName("blog_id")
        val blogId: String = ""

        @SerializedName("user_id")
        val userId: Int = 0

        @SerializedName("comment_text")
        val commentText: String = ""

        @SerializedName("created_at")
        val createdAt: String = ""

        @SerializedName("user_name")
        val userName: String = ""

        @SerializedName("user_email")
        val userEmail: String = ""

        @SerializedName("user_profile_picture")
        val userProfilePicture: String = ""
    }
}