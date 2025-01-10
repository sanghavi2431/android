package `in`.woloo.www.more.trendingblog.model.blog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @JvmField
    @SerializedName("blogs")
    @Expose
    var blogs: List<Blog>? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    @SerializedName("sub_categories")
    @Expose
    var subCategories: List<SubCategory>? = null
}
