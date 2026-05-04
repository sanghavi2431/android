package `in`.woloo.www.store.admin_response

import com.google.gson.annotations.SerializedName

class StockLocationsListData {

        @SerializedName("id")
        var stockLocationsId: String? = null
                @SerializedName("name")
        val stockLocationsName: String? = null
        @SerializedName("metadata")
        val stockLocationsMetadata: Any? = null
                @SerializedName("created_at")
        val stockLocationsCreatedAt: String? = null
        @SerializedName("updated_at")
        val stockLocationsUpdatedAt: String? = null
        @SerializedName("address")
        var address : AddressStockLocation? = null

}

data class AddressStockLocation(
    @SerializedName("id")
    val id: String,
    @SerializedName("address_1")
    val address_1: String,
    @SerializedName("address_2")
    val address_2: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country_code")
    val country_code: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("province")
    val province: String,
    @SerializedName("postal_code")
    val postal_code: String,
    @SerializedName("metadata")
    val metadata: Any
)
