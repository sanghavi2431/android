package `in`.woloo.www.application_kotlin.api_classes

import `in`.woloo.www.common.CommonUtils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ApiServiceCallback<T>(private val webserviceCallback: WebserviceCallback<ApiResponseData<T>>) :
    Callback<T> {

    private val genericError: String = "Something went wrong, try again"

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response != null && response.isSuccessful) {
            val apiResponseData = ApiResponseData<T>()
            apiResponseData.status = ApiResponseData.API_SUCCESS
            apiResponseData.data = response.body()
            webserviceCallback.onWebResponse(apiResponseData)
        } else {
            val apiResponseData = ApiResponseData<T>()
            apiResponseData.status = ApiResponseData.API_FAILURE
            apiResponseData.data = null
            var jsonObject: JSONObject? = null
            try {
                jsonObject = JSONObject(response.errorBody()!!.string())
                val error: JSONObject = jsonObject.getJSONObject("error")
                val message: String = error.getString("message")
                apiResponseData.message = message
            } catch (e: JSONException) {
                  CommonUtils.printStackTrace(e)
                apiResponseData.message = genericError
            }
            webserviceCallback.onWebResponse(apiResponseData)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onHandleError()
        CommonUtils.printStackTrace(t as Exception);
    }

    private fun onHandleError() {
        val apiResponseData = ApiResponseData<T>()
        apiResponseData.status = ApiResponseData.API_FAILURE
        apiResponseData.message = genericError
        apiResponseData.data = null
        webserviceCallback.onWebResponse(apiResponseData)
    }
}