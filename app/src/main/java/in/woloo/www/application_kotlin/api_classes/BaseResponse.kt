package `in`.woloo.www.application_kotlin.api_classes

import com.google.gson.annotations.SerializedName


class BaseResponse<T> {

    @SerializedName(ResponseConstants.SUCCESS)
    var success: Boolean = false

    @SerializedName("message")
    var message: String = ""

    @SerializedName(ResponseConstants.DATA)
    var data: T? = null

    @SerializedName(ResponseConstants.ERROR)
    var error: T? = null

}