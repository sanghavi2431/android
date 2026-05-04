package `in`.woloo.www.store.auth_request_response

import com.google.gson.annotations.SerializedName

data class Customer (
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("company_name") val companyName: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("metadata") val metadata: Any?,
    @SerializedName("has_account") val hasAccount: Boolean,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
  //  @SerializedName("addresses") val addresses: List<Address>
)


