package `in`.woloo.www.store.orders_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.cart_request_response.ServicesData

class OrderListResponse {
   /* @SerializedName("orders")
    var orders: ArrayList<OrdersListData>? = null*/

    @SerializedName("order_sets")
    var orderSets: ArrayList<OrderSetResponse>? = null

    @SerializedName("order")
    var order: OrdersListData? = null


    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}

data class OrderSetResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("display_id") val displayId: String?,
    @SerializedName("customer_id") val customerId: String?,
    @SerializedName("cart_id") val cartId: String?,
    @SerializedName("payment_collection_id") val paymentCollectionId: String?,
    @SerializedName("customer") val customer: CustomerData?,
    @SerializedName("cart") val cart: CartData?,
    @SerializedName("orders") val orders: ArrayList<OrdersListData>?,
    @SerializedName("payment_collection") val paymentCollection: PaymentCollection?,
    @SerializedName("status") val status: String?,
@SerializedName("payment_status") val paymentStatus: String?,
@SerializedName("fulfillment_status") val fulfillmentStatus: String?,
@SerializedName("tax_total") val taxTotal: Double?,
@SerializedName("shipping_tax_total") val shippingTaxTotal: Double?,
@SerializedName("shipping_total") val shippingTotal: Double?,
@SerializedName("total") val total: Double?,
@SerializedName("subtotal") val subtotal: Double?,
@SerializedName("discount_total") val discountTotal: Double?,
@SerializedName("discount_tax_total") val discountTaxTotal: Double?,
@SerializedName("original_total") val originalTotal: Double?,
@SerializedName("original_tax_total") val originalTaxTotal: Double?,
@SerializedName("item_total") val itemTotal: Double?,
@SerializedName("item_subtotal") val itemSubtotal: Double?,
@SerializedName("item_tax_total") val itemTaxTotal: Double?,
@SerializedName("original_item_total") val originalItemTotal: Double?,
@SerializedName("original_item_subtotal") val originalItemSubtotal: Double?,
@SerializedName("original_item_tax_total") val originalItemTaxTotal: Double?,
    @SerializedName("delivery_date") val delivery_date: String?
)


data class OrderSet (

    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("payment_status") val paymentStatus: String,
    @SerializedName("fulfillment_status") val fulfillmentStatus: String,

    @SerializedName("tax_total") val taxTotal: Double,
    @SerializedName("shipping_tax_total") val shippingTaxTotal: Double,
    @SerializedName("shipping_total") val shippingTotal: Double,
    @SerializedName("total") val total: Double,
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("discount_total") val discountTotal: Double,
    @SerializedName("discount_tax_total") val discountTaxTotal: Double,
    @SerializedName("original_total") val originalTotal: Double,
    @SerializedName("original_tax_total") val originalTaxTotal: Double,

    @SerializedName("item_total") val itemTotal: Double,
    @SerializedName("item_subtotal") val itemSubtotal: Double,
    @SerializedName("item_tax_total") val itemTaxTotal: Double,
    @SerializedName("original_item_total") val originalItemTotal: Double,
    @SerializedName("original_item_subtotal") val originalItemSubtotal: Double,
    @SerializedName("original_item_tax_total") val originalItemTaxTotal: Double,

    @SerializedName("display_id") val displayId: String?,  // Nullable
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("cart_id") val cartId: String,
    @SerializedName("payment_collection_id") val paymentCollectionId: String,

    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,

    @SerializedName("orders")
    var orders: List<OrdersListData>,

    @SerializedName("customer")
    var customer: CustomerData,

    @SerializedName("cart")
    var cart: CartData? ,

    @SerializedName("payment_collection")
    var paymentCollection: PaymentCollection
    )

data class OrdersListData (
    @SerializedName("id") val id: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("total") val total: Double?,
    @SerializedName("subtotal") val subtotal: Double?,
    @SerializedName("tax_total") val taxTotal: Double?,
    @SerializedName("discount_total") val discountTotal: Double?,
    @SerializedName("discount_tax_total") val discountTaxTotal: Double?,
    @SerializedName("original_total") val originalTotal: Double?,
    @SerializedName("original_tax_total") val originalTaxTotal: Double?,
    @SerializedName("items") val items: ArrayList<ItemsListDataOrderSet>?,
    @SerializedName("item_total") val itemTotal: Double?,
    @SerializedName("item_subtotal") val itemSubtotal: Double?,
    @SerializedName("item_tax_total") val itemTaxTotal: Double?,
    @SerializedName("sales_channel_id") val salesChannelId: String?,
    @SerializedName("original_item_total") val originalItemTotal: Double?,
    @SerializedName("original_item_subtotal") val originalItemSubtotal: Double?,
    @SerializedName("original_item_tax_total") val originalItemTaxTotal: Double?,
    @SerializedName("shipping_total") val shippingTotal: Double?,
    @SerializedName("shipping_subtotal") val shippingSubtotal: Double?,
    @SerializedName("shipping_tax_total") val shippingTaxTotal: Double?,
    @SerializedName("payment_status") val paymentStatus: String?,
    @SerializedName("fulfillment_status") val fulfillmentStatus: String?,
    @SerializedName("fulfillments") val fulfillments: List<Any>?,
    @SerializedName("payment_collections") val paymentCollections: List<PaymentCollectionNew>?
)

data class PaymentCollectionNew(
    @SerializedName("id") val id: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("completed_at") val completedAt: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("raw_amount") val rawAmount: RawAmount?,
    @SerializedName("raw_authorized_amount") val rawAuthorizedAmount: RawAmount?,
    @SerializedName("raw_captured_amount") val rawCapturedAmount: RawAmount?,
    @SerializedName("raw_refunded_amount") val rawRefundedAmount: RawAmount?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("payments") val payments: List<Payment>?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("authorized_amount") val authorizedAmount: Double?,
    @SerializedName("captured_amount") val capturedAmount: Double?,
    @SerializedName("refunded_amount") val refundedAmount: Double?
)

data class RawAmount(
    @SerializedName("value") val value: String?,
    @SerializedName("precision") val precision: Int?
)

data class Payment(
    @SerializedName("id") val id: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("provider_id") val providerId: String?,
    @SerializedName("data") val data: PaymentData?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("captured_at") val capturedAt: String?,
    @SerializedName("canceled_at") val canceledAt: String?,
    @SerializedName("payment_collection_id") val paymentCollectionId: String?,
    @SerializedName("payment_session") val paymentSession: PaymentSession?,
    @SerializedName("raw_amount") val rawAmount: RawAmount?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("payment_session_id") val paymentSessionId: String?,
    @SerializedName("refunds") val refunds: List<Any>?,
    @SerializedName("amount") val amount: Double?
)

data class PaymentSession(
    @SerializedName("id") val id: String?
)

class ItemsListData {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("thumbnail")
    var thumbnail: String? = null

    @SerializedName("variant_id")
    var variantId: String? = null

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("product_title")
    var productTitle: String? = null

    @SerializedName("product_description")
    var productDescription: String? = null

    @SerializedName("product_subtitle")
    var productSubtitle: String? = null

    @SerializedName("product_type")
    var productType: String? = null

    @SerializedName("product_collection")
    var productCollection: String? = null

    @SerializedName("product_handle")
    var productHandle: String? = null

    @SerializedName("variant_sku")
    var variantSku: String? = null

    @SerializedName("variant_barcode")
    var variantBarcode: String? = null

    @SerializedName("variant_title")
    var variantTitle: String? = null

    @SerializedName("total")
    var total: Double? = null

    @SerializedName("quantity")
    var quantity: Int? = null

    @SerializedName("unit_price")
    var unitPrice: Double? = null


    @SerializedName("compare_at_unit_price")
    var compare_at_unit_price: Double? = null

    @SerializedName("discount_total")
    var discountTotal: Double? = null

    @SerializedName("shipped_total")
    var shipped_total: Double? = null

    @SerializedName("original_total")
    var original_total: Double? = null


    @SerializedName("fulfilled_quantity")
    var fulfilledQuantity: Int? = null

    @SerializedName("delivered_quantity")
    var deliveredQuantity: Int? = null

    @SerializedName("shipped_quantity")
    var shippedQuantity: Int? = null
}

class ItemsListDataOrderSet(
@SerializedName("id") val id: String?,
@SerializedName("title") val title: String?,
@SerializedName("subtitle") val subtitle: String?,
@SerializedName("thumbnail") val thumbnail: String?,
@SerializedName("variant_id") val variantId: String?,
@SerializedName("product_id") val productId: String?,
@SerializedName("product_title") val productTitle: String?,
@SerializedName("product_description") val productDescription: String?,
@SerializedName("product_subtitle") val productSubtitle: String?,
@SerializedName("product_type") val productType: String?,
@SerializedName("product_type_id") val productTypeId: String?,
@SerializedName("product_collection") val productCollection: String?,
@SerializedName("product_handle") val productHandle: String?,
@SerializedName("variant_sku") val variantSku: String?,
@SerializedName("variant_barcode") val variantBarcode: String?,
@SerializedName("variant_title") val variantTitle: String?,
@SerializedName("variant_option_values") val variantOptionValues: Any?,
@SerializedName("requires_shipping") val requiresShipping: Boolean?,
@SerializedName("is_giftcard") val isGiftcard: Boolean?,
@SerializedName("is_discountable") val isDiscountable: Boolean?,
@SerializedName("is_tax_inclusive") val isTaxInclusive: Boolean?,
@SerializedName("is_custom_price") val isCustomPrice: Boolean?,
@SerializedName("metadata") val metadata: MetaDataLineItems?,
@SerializedName("raw_compare_at_unit_price") val rawCompareAtUnitPrice: RawAmount?,
@SerializedName("raw_unit_price") val rawUnitPrice: RawAmount?,
@SerializedName("created_at") val createdAt: String?,
@SerializedName("updated_at") val updatedAt: String?,
@SerializedName("deleted_at") val deletedAt: String?,
@SerializedName("tax_lines") val taxLines: List<Any>?,
@SerializedName("adjustments") val adjustments: List<Adjustment>?,
@SerializedName("compare_at_unit_price") val compareAtUnitPrice: Double?,
@SerializedName("unit_price") val unitPrice: Double?,
@SerializedName("quantity") val quantity: Int?,
@SerializedName("raw_quantity") val rawQuantity: RawAmount?,
@SerializedName("detail") val detail: Detail?,
@SerializedName("subtotal") val subtotal: Double?,
@SerializedName("total") val total: Double?,
@SerializedName("original_total") val originalTotal: Double?,
@SerializedName("discount_total") val discountTotal: Double?,
@SerializedName("discount_subtotal") val discountSubtotal: Double?,
@SerializedName("discount_tax_total") val discountTaxTotal: Double?,
@SerializedName("tax_total") val taxTotal: Double?,
@SerializedName("original_tax_total") val originalTaxTotal: Double?,
@SerializedName("refundable_total_per_unit") val refundableTotalPerUnit: Double?,
@SerializedName("refundable_total") val refundableTotal: Double?,
@SerializedName("fulfilled_total") val fulfilledTotal: Double?,
@SerializedName("shipped_total") val shippedTotal: Double?,
@SerializedName("return_requested_total") val returnRequestedTotal: Double?,
@SerializedName("return_received_total") val returnReceivedTotal: Double?,
@SerializedName("return_dismissed_total") val returnDismissedTotal: Double?,
@SerializedName("write_off_total") val writeOffTotal: Double?,
@SerializedName("raw_subtotal") val rawSubtotal: RawAmount?,
@SerializedName("raw_total") val rawTotal: RawAmount?,
@SerializedName("raw_original_total") val rawOriginalTotal: RawAmount?,
@SerializedName("raw_discount_total") val rawDiscountTotal: RawAmount?,
@SerializedName("raw_discount_subtotal") val rawDiscountSubtotal: RawAmount?,
@SerializedName("raw_discount_tax_total") val rawDiscountTaxTotal: RawAmount?,
@SerializedName("raw_tax_total") val rawTaxTotal: RawAmount?,
@SerializedName("raw_original_tax_total") val rawOriginalTaxTotal: RawAmount?,
@SerializedName("raw_refundable_total_per_unit") val rawRefundableTotalPerUnit: RawAmount?,
@SerializedName("raw_refundable_total") val rawRefundableTotal: RawAmount?,
@SerializedName("raw_fulfilled_total") val rawFulfilledTotal: RawAmount?,
@SerializedName("raw_shipped_total") val rawShippedTotal: RawAmount?,
@SerializedName("raw_return_requested_total") val rawReturnRequestedTotal: RawAmount?,
@SerializedName("raw_return_received_total") val rawReturnReceivedTotal: RawAmount?,
@SerializedName("raw_return_dismissed_total") val rawReturnDismissedTotal: RawAmount?,
@SerializedName("raw_write_off_total") val rawWriteOffTotal: RawAmount?,
@SerializedName("variant") val variant: Variant?
)

data class Adjustment(
    @SerializedName("id") val id: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("promotion_id") val promotionId: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("provider_id") val providerId: String?,
    @SerializedName("item_id") val itemId: String?,
    @SerializedName("raw_amount") val rawAmount: RawAmount?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("subtotal") val subtotal: Double?,
    @SerializedName("total") val total: Double?,
    @SerializedName("raw_subtotal") val rawSubtotal: RawAmount?,
    @SerializedName("raw_total") val rawTotal: RawAmount?
)

data class Detail(
    @SerializedName("id") val id: String?,
    @SerializedName("version") val version: Int?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("order_id") val orderId: String?,
    @SerializedName("raw_unit_price") val rawUnitPrice: RawAmount?,
    @SerializedName("raw_compare_at_unit_price") val rawCompareAtUnitPrice: RawAmount?,
    @SerializedName("raw_quantity") val rawQuantity: RawAmount?,
    @SerializedName("raw_fulfilled_quantity") val rawFulfilledQuantity: RawAmount?,
    @SerializedName("raw_delivered_quantity") val rawDeliveredQuantity: RawAmount?,
    @SerializedName("raw_shipped_quantity") val rawShippedQuantity: RawAmount?,
    @SerializedName("raw_return_requested_quantity") val rawReturnRequestedQuantity: RawAmount?,
    @SerializedName("raw_return_received_quantity") val rawReturnReceivedQuantity: RawAmount?,
    @SerializedName("raw_return_dismissed_quantity") val rawReturnDismissedQuantity: RawAmount?,
    @SerializedName("raw_written_off_quantity") val rawWrittenOffQuantity: RawAmount?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("item_id") val itemId: String?,
    @SerializedName("unit_price") val unitPrice: Double?,
    @SerializedName("compare_at_unit_price") val compareAtUnitPrice: Double?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("fulfilled_quantity") val fulfilledQuantity: Int?,
    @SerializedName("delivered_quantity") val deliveredQuantity: Int?,
    @SerializedName("shipped_quantity") val shippedQuantity: Int?,
    @SerializedName("return_requested_quantity") val returnRequestedQuantity: Int?,
    @SerializedName("return_received_quantity") val returnReceivedQuantity: Int?,
    @SerializedName("return_dismissed_quantity") val returnDismissedQuantity: Int?,
    @SerializedName("written_off_quantity") val writtenOffQuantity: Int?
)

data class Variant(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("sku") val sku: String?,
    @SerializedName("barcode") val barcode: String?,
    @SerializedName("ean") val ean: String?,
    @SerializedName("upc") val upc: String?,
    @SerializedName("allow_backorder") val allowBackorder: Boolean?,
    @SerializedName("manage_inventory") val manageInventory: Boolean?,
    @SerializedName("hs_code") val hsCode: String?,
    @SerializedName("origin_country") val originCountry: String?,
    @SerializedName("mid_code") val midCode: String?,
    @SerializedName("material") val material: String?,
    @SerializedName("weight") val weight: Double?,
    @SerializedName("length") val length: Double?,
    @SerializedName("height") val height: Double?,
    @SerializedName("width") val width: Double?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("variant_rank") val variantRank: Int?,
    @SerializedName("product_id") val productId: String?,
    @SerializedName("product") val product: ProductOrderSet?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("options") val options: List<OptionOrderSet>?
)

data class ProductOrderSet(
    @SerializedName("id") val id: String?
)

data class OptionOrderSet(
    @SerializedName("id") val id: String?,
    @SerializedName("value") val value: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("option_id") val optionId: String?,
    @SerializedName("option") val option: OptionDetailOrderSet?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?
)

data class OptionDetailOrderSet(
    @SerializedName("id") val id: String?
)

/*data class (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("company_name")
    val companyName: String? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("has_account")
    val hasAccount: Boolean? = null,

    @SerializedName("metadata")
    val metadata: Any? = null,

    @SerializedName("created_by")
    val createdBy: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null
)*/
data class CustomerData(
    @SerializedName("id") val id: String,
    @SerializedName("company_name") val companyName: String?,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("has_account") val hasAccount: Boolean,
    @SerializedName("metadata") val metadata: Any?, // Use `Map<String, Any>?` if metadata is a known structure
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String?
)

data class CartData(
    @SerializedName("id") val id: String?,
    @SerializedName("region_id") val regionId: String?,
    @SerializedName("customer_id") val customerId: String?,
    @SerializedName("sales_channel_id") val salesChannelId: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("completed_at") val completedAt: String?,
    @SerializedName("shipping_address") val shippingAddress: AddressReference?,
    @SerializedName("billing_address") val billingAddress: AddressReference?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("shipping_address_id") val shippingAddressId: String?,
    @SerializedName("billing_address_id") val billingAddressId: String?
)

data class AddressReference(
    @SerializedName("id") val id: String?,
    @SerializedName("customer_id") val customerId: String?,
@SerializedName("company") val company: String?,
@SerializedName("first_name") val firstName: String?,
@SerializedName("last_name") val lastName: String?,
@SerializedName("address_1") val address1: String?,
@SerializedName("address_2") val address2: String?,
@SerializedName("city") val city: String?,
@SerializedName("country_code") val countryCode: String?,
@SerializedName("province") val province: String?,
@SerializedName("postal_code") val postalCode: String?,
@SerializedName("phone") val phone: String?,
@SerializedName("metadata") val metadata: Any?,
@SerializedName("created_at") val createdAt: String?,
@SerializedName("updated_at") val updatedAt: String?,
@SerializedName("deleted_at") val deletedAt: String?
)

data class PaymentCollection(
    @SerializedName("id") val id: String?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("completed_at") val completedAt: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("raw_amount") val rawAmount: AmountDetail?,
    @SerializedName("raw_authorized_amount") val rawAuthorizedAmount: AmountDetail?,
    @SerializedName("raw_captured_amount") val rawCapturedAmount: AmountDetail?,
    @SerializedName("raw_refunded_amount") val rawRefundedAmount: AmountDetail?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("authorized_amount") val authorizedAmount: Double?,
    @SerializedName("captured_amount") val capturedAmount: Double?,
    @SerializedName("refunded_amount") val refundedAmount: Double?
)

data class AmountDetail(
    @SerializedName("value") val value: String?,
    @SerializedName("precision") val precision: Int?
)

data class PaymentData(
    @SerializedName("id") val id: String?,
    @SerializedName("notes") val notes: PaymentNotes?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("entity") val entity: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("receipt") val receipt: String?,
    @SerializedName("attempts") val attempts: Int?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("offer_id") val offerId: String?,
    @SerializedName("amount_due") val amountDue: Double?,
    @SerializedName("created_at") val createdAt: Long?,
    @SerializedName("amount_paid") val amountPaid: Double?
)

data class PaymentNotes(
    @SerializedName("customer") val customer: PaymentCustomer?,
    @SerializedName("idempotency_key") val idempotencyKey: String?
)

data class PaymentCustomer(
    @SerializedName("id") val id: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("addresses") val addresses: List<AddressOrderSet>?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("company_name") val companyName: String?,
    @SerializedName("account_holders") val accountHolders: List<Any>?,
    @SerializedName("billing_address") val billingAddress: AddressOrderSet?
)

data class AddressOrderSet(
    @SerializedName("id") val id: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("company") val company: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("province") val province: String?,
    @SerializedName("address_1") val address1: String?,
    @SerializedName("address_2") val address2: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("customer_id") val customerId: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("address_name") val addressName: String?,
    @SerializedName("country_code") val countryCode: String?,
    @SerializedName("is_default_billing") val isDefaultBilling: Boolean?,
    @SerializedName("is_default_shipping") val isDefaultShipping: Boolean?
)


data class MetaDataLineItems(
    @SerializedName("Services") val servieceDataResponse: ArrayList<ServicesData>?,
)


