package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.SerializedName

class CategoriesResponse {
    var status: String? = null
    var data: Data? = null
    var message: String? = null
    var code = 0

    inner class Category {
        @SerializedName("blog_count")
        var blogCount = 0
        var id = 0

        @SerializedName("category_icon_url")
        var categoryIconUrl: String? = null

        @SerializedName("category_name")
        var categoryName: String? = null

        @Transient
        var isSelected = false
    }

    inner class Data {
        var categories: List<Category>? = null
    }
}
