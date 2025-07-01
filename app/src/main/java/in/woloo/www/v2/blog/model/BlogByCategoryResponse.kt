package `in`.woloo.www.v2.blog.model

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.trendingblog.model.blog.Blog
import `in`.woloo.www.trendingblog.model.blog.Category
import `in`.woloo.www.trendingblog.model.blog.SubCategory

class BlogByCategoryResponse {
    @SerializedName("blogs")
    var blogs: List<Blog>? = null

    @SerializedName("categories")
    var categories: List<Category>? = null

    @SerializedName("sub_categories")
    var subCategories: List<SubCategory>? = null
}
