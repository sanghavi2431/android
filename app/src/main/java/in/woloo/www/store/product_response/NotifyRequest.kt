package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName

class NotifyRequest {

    @SerializedName("variant_id")
    var variantId: String? = null

    @SerializedName("phone")
    var userMobileNumber: String? = null

  /*  @SerializedName("sales_channel_id")
    var salesChannelId: String? = null*/
}
