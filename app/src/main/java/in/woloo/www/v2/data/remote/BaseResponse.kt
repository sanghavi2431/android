package `in`.woloo.www.v2.data.remote

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.v2.constant.ResponseConstants

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
