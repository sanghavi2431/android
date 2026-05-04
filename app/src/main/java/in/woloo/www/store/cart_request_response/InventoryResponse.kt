package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName


data class InventoryResponse(
    val success: Boolean,
    val message: String,
    val data: InventoryData
)

data class InventoryData(
    val items: ArrayList<InventoryItem>
)

data class InventoryItem(
    @SerializedName("inventory_item_id") val inventoryItemId: String,
    @SerializedName("required_quantity") val requiredQuantity: Int,
    @SerializedName("allow_backorder") val allowBackorder: Boolean,
    val quantity: Int,
    @SerializedName("location_ids") val locationIds: List<String>
)


data class InventoryErrorResponse(
    val success: Boolean,
    val message: String,
    val errors: ArrayList<InventoryErrorItem>,
    val summary: InventoryErrorSummary
)

data class InventoryErrorItem(
    @SerializedName("variant_id") val variantId: String,
    val title: String,
    @SerializedName("requested_quantity") val requestedQuantity: Int,
    @SerializedName("available_quantity") val availableQuantity: Int
)

data class InventoryErrorSummary(
    @SerializedName("total_items_checked") val totalItemsChecked: Int,
    @SerializedName("items_out_of_stock") val itemsOutOfStock: Int,
    @SerializedName("items_in_stock") val itemsInStock: Int
)


