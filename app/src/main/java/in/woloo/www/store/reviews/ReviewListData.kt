package `in`.woloo.www.store.reviews

import com.google.gson.annotations.SerializedName

class ReviewListData {


    @SerializedName("id")
    var id: String? = null
    @SerializedName("rating")
    var rating: Int? = null
    @SerializedName("comment")
    var comment: String? = null
    @SerializedName("approval")
    var approval: Boolean? = null
    @SerializedName("created_at")
    var created_at: String? = null
    @SerializedName("updated_at")
    var updated_at: String? = null
    @SerializedName("deleted_at")
    var deleted_at: String? = null
    @SerializedName("customer")
    var customer: CustomerReviews? = null

}


data class CustomerReviews(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("company_name")
    var companyName : String? = null,


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
    val metadata: Any? = null, // Use a specific type if you know the structure

    @SerializedName("created_by")
    val createdBy: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null, // Or use LocalDateTime with a type converter

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null
)
