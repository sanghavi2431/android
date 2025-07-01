package `in`.woloo.www.v2.blog.model

import com.google.gson.annotations.SerializedName

class SaveUserCategoryRequest {

    @SerializedName("categories")
    var categories: ArrayList<Int> = ArrayList<Int>()
}
