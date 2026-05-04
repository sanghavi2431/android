package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.orders_response.OrdersListData

class CompleteCartResponse {

    @SerializedName("type") val type: String? = null
    //@SerializedName("order") val order: Order? = null
    @SerializedName("order_set")
    val orderSet: OrderSet? = null
}

/*

data class Order(
    @SerializedName("parent_order") val parent_order: ParentOrder
)
*/




data class OrderSet(
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("display_id")
    val displayId: String?,
    @SerializedName("customer_id")
    val customerId: String,
    @SerializedName("cart_id")
    val cartId: String,
    @SerializedName("payment_collection_id")
    val paymentCollectionId: String,
    @SerializedName("customer")
    val customer: CustomerCart,
    @SerializedName("cart")
    val cart: Cart,
    @SerializedName("payment_collection")
    val paymentCollection: PaymentCollection,
    @SerializedName("orders")
    val orders: ArrayList<OrdersListData>,
    @SerializedName("status")
    val status: String,
    @SerializedName("payment_status")
    val paymentStatus: String,
    @SerializedName("fulfillment_status")
    val fulfillmentStatus: String,
    @SerializedName("tax_total")
    val taxTotal: Int,
    @SerializedName("shipping_tax_total")
    val shippingTaxTotal: Int,
    @SerializedName("shipping_total")
    val shippingTotal: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("subtotal")
    val subtotal: Int
)

data class CustomerCart(
    @SerializedName("id")
    val id: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("has_account")
    val hasAccount: Boolean
)

data class Cart(
    @SerializedName("id")
    val id: String,
    @SerializedName("region_id")
    val regionId: String,
    @SerializedName("customer_id")
    val customerId: String,
    @SerializedName("sales_channel_id")
    val salesChannelId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("shipping_address_id")
    val shippingAddressId: String,
    @SerializedName("billing_address_id")
    val billingAddressId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class PaymentCollection(
    @SerializedName("id")
    val id: String,
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("authorized_amount")
    val authorizedAmount: Int,
    @SerializedName("captured_amount")
    val capturedAmount: Int,
    @SerializedName("refunded_amount")
    val refundedAmount: Int
)

data class ParentOrder(
    @SerializedName("id")
    val id: String,
    @SerializedName("customer_id")
    val customerId: String,
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("subtotal")
    val subtotal: Int,
    @SerializedName("tax_total")
    val taxTotal: Int,
    @SerializedName("shipping_total")
    val shippingTotal: Int,
    @SerializedName("items")
    val items: List<OrderItem>,
    @SerializedName("payment_collections")
    val paymentCollections: List<PaymentCollection>,
  /*  @SerializedName("fulfillments")
    val fulfillments: List<Fulfillment>,*/
    @SerializedName("payment_status")
    val paymentStatus: String,
    @SerializedName("fulfillment_status")
    val fulfillmentStatus: String
)

data class OrderItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("variant_id")
    val variantId: String,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("variant_title")
    val variantTitle: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit_price")
    val unitPrice: Int,
    @SerializedName("subtotal")
    val subtotal: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("detail")
    val detail: OrderItemDetail
)

data class OrderItemDetail(
    @SerializedName("id")
    val id: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("item_id")
    val itemId: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("fulfilled_quantity")
    val fulfilledQuantity: Int,
    @SerializedName("shipped_quantity")
    val shippedQuantity: Int,
    @SerializedName("delivered_quantity")
    val deliveredQuantity: Int
)

