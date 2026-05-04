package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class DeleteFromCartResponse {

        @SerializedName("id")
        val id: String? = null
        @SerializedName("object")
        val objType: String? = null
        @SerializedName("deleted")
        val deleted: Boolean? = null
        @SerializedName("parent")
        val parent: CartParams? = null
}

    data class CartParent(
        @SerializedName("id") val id: String,
        @SerializedName("currency_code") val currencyCode: String,
        @SerializedName("email") val email: String,
        @SerializedName("region_id") val regionId: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("completed_at") val completedAt: String?,
        @SerializedName("total") val total: Int,
        @SerializedName("subtotal") val subtotal: Int,
        @SerializedName("tax_total") val taxTotal: Int,
        @SerializedName("discount_total") val discountTotal: Int,
        @SerializedName("discount_subtotal") val discountSubtotal: Int,
        @SerializedName("discount_tax_total") val discountTaxTotal: Int,
        @SerializedName("original_total") val originalTotal: Int,
        @SerializedName("original_tax_total") val originalTaxTotal: Int,
        @SerializedName("item_total") val itemTotal: Int,
        @SerializedName("item_subtotal") val itemSubtotal: Int,
        @SerializedName("item_tax_total") val itemTaxTotal: Int,
        @SerializedName("original_item_total") val originalItemTotal: Int,
        @SerializedName("original_item_subtotal") val originalItemSubtotal: Int,
        @SerializedName("original_item_tax_total") val originalItemTaxTotal: Int,
        @SerializedName("shipping_total") val shippingTotal: Int,
        @SerializedName("shipping_subtotal") val shippingSubtotal: Int,
        @SerializedName("shipping_tax_total") val shippingTaxTotal: Int,
        @SerializedName("original_shipping_tax_total") val originalShippingTaxTotal: Int,
        @SerializedName("original_shipping_subtotal") val originalShippingSubtotal: Int,
        @SerializedName("original_shipping_total") val originalShippingTotal: Int,
        @SerializedName("metadata") val metadata: Any?,
        @SerializedName("sales_channel_id") val salesChannelId: String,
        @SerializedName("shipping_address_id") val shippingAddressId: String,
        @SerializedName("customer_id") val customerId: String,
        @SerializedName("items") val items: ArrayList<CartLineItems>,
        @SerializedName("shipping_methods") val shippingMethods: List<Any>,
        @SerializedName("shipping_address") val shippingAddressClass: ShippingAddress?,
        @SerializedName("billing_address") val billingAddress: Any?,
        @SerializedName("customer") val customer: CustomerClass,
        @SerializedName("region") val region: RegionClass,
        @SerializedName("promotions") val promotions: List<Promotion>
    )



data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("collection_id") val collectionId: String,
    @SerializedName("type_id") val typeId: String?,
    @SerializedName("categories") val categories: List<Category>,
    @SerializedName("tags") val tags: List<Any>
)

data class Category(
    @SerializedName("id") val id: String
)



data class Country(
    @SerializedName("iso_2") val iso2: String,
    @SerializedName("iso_3") val iso3: String,
    @SerializedName("num_code") val numCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("region_id") val regionId: String,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String?
)

data class Promotion(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("is_automatic") val isAutomatic: Boolean,
    @SerializedName("application_method") val applicationMethod: ApplicationMethod
)

data class ApplicationMethod(
    @SerializedName("value") val value: Int,
    @SerializedName("type") val type: String,
    @SerializedName("currency_code") val currencyCode: String
)



data class CustomerClass(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("groups") val groups: List<Any>
)

data class RegionClass(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("currency_code") val currencyCode: String,
    @SerializedName("automatic_taxes") val automaticTaxes: Boolean,
    @SerializedName("countries") val countries: List<Country>
)


