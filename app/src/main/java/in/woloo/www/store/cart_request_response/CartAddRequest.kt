package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class CartAddRequest {

    @SerializedName("variant_id")
    var variant_id: String? = null

    @SerializedName("quantity")
    var quantity: Int? = null


}


class CartAddRequestHygiene {
    @SerializedName("variant_id")
    var variant_id: String? = null

    @SerializedName("quantity")
    var quantity: Int? = null

    @SerializedName("metadata")
    var metadata : MetaDataLineItemsRequest? = null
}

data class MetaDataLineItemsRequest(

    @SerializedName("Services")
    var services_data : ArrayList<ServicesData>? = null
)

data class ServicesData(
    @SerializedName("service_date") var serviceDate: String?,
    @SerializedName("service_time") var serviceTime: String?,
    @SerializedName("service_area") var serviceArea: String?
)

