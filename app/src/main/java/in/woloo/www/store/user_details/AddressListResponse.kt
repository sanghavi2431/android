package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName

class AddressListResponse {
    @SerializedName("addresses")
    var addresses: ArrayList<AddressList>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}