package `in`.woloo.www.store.auth_request_response

import com.google.gson.annotations.SerializedName

class CustomerListResponse {
    @SerializedName("customer")
    var customer: Customer? = null
}