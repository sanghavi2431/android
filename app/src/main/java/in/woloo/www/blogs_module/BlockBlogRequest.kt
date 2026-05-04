package `in`.woloo.www.blogs_module

import com.google.gson.annotations.SerializedName

class BlockBlogRequest{

    @SerializedName("blog_id")
    var blog_id : String? = null

}


data class FavouriteResult(
    @SerializedName("favourite") val favourite: Boolean
)