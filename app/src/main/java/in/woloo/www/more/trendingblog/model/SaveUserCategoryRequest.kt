package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.SerializedName

class SaveUserCategoryRequest {

    @SerializedName("categories")
    var categories: ArrayList<Int> = ArrayList<Int>()

}