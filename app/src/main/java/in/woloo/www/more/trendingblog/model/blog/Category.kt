package `in`.woloo.www.more.trendingblog.model.blog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Category {
    @JvmField
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @JvmField
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null

    @JvmField
    @SerializedName("category_icon_url")
    @Expose
    var categoryIconUrl: String? = null
}
