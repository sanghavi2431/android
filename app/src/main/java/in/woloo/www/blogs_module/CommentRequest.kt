package `in`.woloo.www.blogs_module

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("blog_id")
    val blogId: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("comment_text")
    val commentText: String
)

