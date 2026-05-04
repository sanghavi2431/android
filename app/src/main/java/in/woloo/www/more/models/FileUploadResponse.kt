package `in`.woloo.www.more.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileUploadResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("converted_name")
    @Expose
    var convertedName: String? = null

    @SerializedName("path")
    @Expose
    var path: String? = null
}
