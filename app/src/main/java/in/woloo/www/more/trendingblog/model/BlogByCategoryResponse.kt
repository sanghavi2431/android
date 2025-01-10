package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.more.trendingblog.model.BlogsResponse
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.more.trendingblog.model.blog.SubCategory

class BlogByCategoryResponse {
    @SerializedName("blogs")
    var blogs: List<Blog>? = null

    @SerializedName("categories")
    var categories: List<Category>? = null

    @SerializedName("sub_categories")
    var subCategories: List<SubCategory>? = null
}