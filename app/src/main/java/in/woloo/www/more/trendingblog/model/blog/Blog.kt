package `in`.woloo.www.more.trendingblog.model.blog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Blog {

    @kotlin.jvm.JvmField
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

    @SerializedName("short_link")
    @Expose
    var shortLink: String? = null

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

    @SerializedName("is_blog_read")
    @Expose
    var isBlogRead: Int? = null

    @SerializedName("category_types")
    @Expose
    var blogType: String? = null

    @SerializedName("shop_map_id")
    @Expose
    var isMapToShop: String? = null

    @SerializedName("")
    @Expose
    var categoryIdOfProduct: String? = null
}
