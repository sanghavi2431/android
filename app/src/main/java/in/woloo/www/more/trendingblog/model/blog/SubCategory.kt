package `in`.woloo.www.more.trendingblog.model.blog

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubCategory {
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
