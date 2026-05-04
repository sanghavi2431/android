package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.VariantsProductListData

class CartLineItems {
    @SerializedName("id")
    val line_id: String? = null
    @SerializedName("thumbnail")
    val thumbnail: String? = null
   @SerializedName("variant_id")
    val variant_id: String? = null
    @SerializedName("product_id")
    val product_id: String? = null
    @SerializedName("product_type_id")
    val product_type_id: Double? = null
    @SerializedName("product_title")
    val product_title: String? = null
    @SerializedName("product_description")
    val product_description: String? = null
    @SerializedName("product_subtitle")
    val product_subtitle: String? = null
    @SerializedName("product_type")
    val product_type: String? = null
    @SerializedName("product_collection")
    val product_collection: String? = null
    @SerializedName("product_handle")
    val product_handle: String? = null
    @SerializedName("variant_sku")
    val variant_sku: String? = null
    @SerializedName("variant_barcode")
    val variant_barcode: String? = null
    @SerializedName("variant_title")
    val variant_title: String? = null
    @SerializedName("requires_shipping")
    val requires_shipping: Boolean? = null
    @SerializedName("metadata")
    val metadata : MetaDataLineItems? = null
    @SerializedName("quantity")
    val quantity: Int? = null
    @SerializedName("unit_price")
    val unit_price: String? = null
    @SerializedName("compare_at_unit_price")
    val compare_at_unit_price: String? = null
    @SerializedName("is_tax_inclusive")
    val is_tax_inclusive: Boolean? = null
    @SerializedName("tax_lines")
    val tax_lines: Any? = null
    @SerializedName("created_at")
    val created_at: String? = null
    @SerializedName("updated_at")
    val updated_at: String? = null
    @SerializedName("title")
    val title: String? = null
    @SerializedName("adjustments")
    val adjustments: Any? = null
    @SerializedName("product")
    val product: ProductListData? = null
    @SerializedName("variant")
    var variants: VariantsProductListData? = null

}

data class MetaDataLineItems(
  @SerializedName("Services") val servieceDataResponse: ArrayList<ServicesData>?,
)

