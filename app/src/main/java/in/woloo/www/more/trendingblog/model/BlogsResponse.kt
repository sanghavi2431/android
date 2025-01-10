package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogsResponse {
    var status: String? = null
    var data: Data? = null
    var message: String? = null
    var code = 0

    inner class Blog {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("author_id")
        @Expose
        var authorId: Int? = null

        @SerializedName("main_image")
        @Expose
        var mainImage: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("detailed_blog_link")
        @Expose
        var detailedBlogLink: String? = null

        @SerializedName("categories")
        @Expose
        var categories: String? = null

        @SerializedName("sub_categories")
        @Expose
        var subCategories: String? = null

        @SerializedName("like_counts")
        @Expose
        var likeCounts: Int? = null

        @SerializedName("favourite_counts")
        @Expose
        var favouriteCounts: Int? = null

        @SerializedName("is_liked")
        @Expose
        var isLiked: Int? = null

        @SerializedName("is_favourite")
        @Expose
        var isFavourite: Int? = null
    }

    inner class Category {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("category_icon_url")
        @Expose
        var categoryIconUrl: String? = null
    }

    inner class SubCategory {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("sub_category")
        @Expose
        var subCategory: String? = null

        @SerializedName("sub_category_icon_url")
        @Expose
        var subCategoryIconUrl: String? = null
    }

    inner class Data {
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
}
