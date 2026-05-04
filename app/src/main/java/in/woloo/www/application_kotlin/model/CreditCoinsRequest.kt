package `in`.woloo.www.application_kotlin.model

import com.google.gson.annotations.SerializedName

data class CreditCoinsRequest(
    @SerializedName("coins") val coins: Int,
    @SerializedName("remarks") val remarks: String,
    @SerializedName("type") val type: String,
    @SerializedName("is_gift") val isGift: Int,
    @SerializedName("blog_id") val blogId: Int? = null,
    @SerializedName("woloo_id") val wolooId: Int? = null,
    @SerializedName("wolooCoins") val wolooCoins: Int? = null
)