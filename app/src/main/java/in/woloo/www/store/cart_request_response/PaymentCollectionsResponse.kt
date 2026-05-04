package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class PaymentCollectionsResponse {

@SerializedName("payment_collection")
var paymentCollection: PaymentCollectionObject? = null
}

 class PaymentCollectionObject{

     @SerializedName("id")
     var id: String? = null
     @SerializedName("currency_code")
     var currencyCode: String? = null
     @SerializedName("amount")
     var amount: Double? = null
     @SerializedName("payment_sessions")
     var paymentSessions: ArrayList<PaymentSession>? = null
}

class PaymentSession{

    @SerializedName("id")
    var id: String? = null
    @SerializedName("currency_code")
    var currencyCode: String? = null
    @SerializedName("provider_id")
    var providerId: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("authorized_at")
    var authorizedAt: String? = null
    @SerializedName("amount")
    var amount: Double? = null
    @SerializedName("payment_collection_id")
    var paymentCollectionId: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null
    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("deleted_at")
    var deletedAt: String? = null
    @SerializedName("data")
    var data: DataSession? = null
    @SerializedName("context")
    var context: Any? = null
    @SerializedName("metadata")
    var metadata: Any? = null
    @SerializedName("raw_amount")
    var rawAmount: RawAmount? = null




}

    data class RawAmount(
        @SerializedName("value")
        var value: String? = null,
        @SerializedName("precision")
        var precision: Int? = null

    )

class DataSession{
    @SerializedName("id")
    var id: String? = null
    @SerializedName("currency_code")
    var currencyCode: String? = null
    @SerializedName("amount")
    var amount: Double? = null

}