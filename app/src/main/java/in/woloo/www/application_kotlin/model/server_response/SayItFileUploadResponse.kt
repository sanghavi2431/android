package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SayItFileUploadResponse(
    @SerializedName("Status")
    @Expose
    var status: Int? = null,

    @SerializedName("S3URL")
    @Expose
    var s3URL: String? = null
)

