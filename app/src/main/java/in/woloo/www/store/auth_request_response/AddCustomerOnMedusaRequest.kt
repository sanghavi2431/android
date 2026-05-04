package `in`.woloo.www.store.auth_request_response

import com.google.gson.annotations.SerializedName

class AddCustomerOnMedusaRequest {
    @SerializedName("email")
    var email : String = ""

}

class UpdateCustomerRequest{
    @SerializedName("first_name")
    var firstName : String = ""
    @SerializedName("last_name")
    var lastName : String = ""

}