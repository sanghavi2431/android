package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SayItAddMessageResponse (

    @SerializedName("status")
    @Expose
    var status: Int? = null,

    @SerializedName("QrId")
    @Expose
    var qrId: String? = null,

    @SerializedName("msg")
@Expose
var message: String? = null,

@SerializedName("MessageId")
@Expose
var messageId: String? = null,

    @SerializedName("Type")
    @Expose
    var typeCountry: String? = null

)

