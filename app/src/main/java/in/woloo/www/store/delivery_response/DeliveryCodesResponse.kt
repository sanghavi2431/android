package `in`.woloo.www.store.delivery_response

import com.google.gson.annotations.SerializedName

class DeliveryCodesResponse {


    @SerializedName("delivery_codes")
    val deliveryCodes: List<DeliveryCode>? = null
}

    data class DeliveryCode(
        @SerializedName("postal_code")
        val postalCode: PostalCode
    )

    data class PostalCode(
        @SerializedName("max_weight")
        val maxWeight: Double,

        @SerializedName("city")
        val city: String,

        @SerializedName("cod")
        val cod: String,

        @SerializedName("inc")
        val inc: String,

        @SerializedName("district")
        val district: String,

        @SerializedName("pin")
        val pin: Int,

        @SerializedName("max_amount")
        val maxAmount: Double,

        @SerializedName("pre_paid")
        val prePaid: String,

        @SerializedName("cash")
        val cash: String,

        @SerializedName("state_code")
        val stateCode: String,

        @SerializedName("remarks")
        val remarks: String,

        @SerializedName("pickup")
        val pickup: String,

        @SerializedName("repl")
        val repl: String,

        @SerializedName("covid_zone")
        val covidZone: String,

        @SerializedName("country_code")
        val countryCode: String,

        @SerializedName("is_oda")
        val isOda: String,

        @SerializedName("protect_blacklist")
        val protectBlacklist: Boolean,

        @SerializedName("sort_code")
        val sortCode: String,

        @SerializedName("sun_tat")
        val sunTat: Boolean,

        @SerializedName("center")
        val center: ArrayList<Center>
    )

    data class Center(
        @SerializedName("code")
        val code: String,

        @SerializedName("e")
        val endDate: String? = null,

        @SerializedName("cn")
        val centerName: String,

        @SerializedName("s")
        val startDate: String? = null,

        @SerializedName("u")
        val updatedBy: String,

        @SerializedName("ud")
        val updatedDate: String? = null,

        @SerializedName("sort_code")
        val sortCode: String
    )


