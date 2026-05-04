package `in`.woloo.www.application_kotlin.api_classes

import android.content.Context
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ApiServiceCallback<T>(private val webserviceCallback: WebserviceCallback<ApiResponseData<T>>,
                            private val showDialog: Boolean = true
) :
    Callback<T> {

    private val genericError: String = "Something went wrong, try again"

    override fun onResponse(call: Call<T>, response: Response<T>) {

        if (response != null && response.isSuccessful) {
            val apiResponseData = ApiResponseData<T>()
            apiResponseData.status = ApiResponseData.API_SUCCESS
            apiResponseData.data = response.body()
            apiResponseData.responseCode = response.code()
            webserviceCallback.onWebResponse(apiResponseData)
        } else {
            val apiResponseData = ApiResponseData<T>()
            apiResponseData.status = ApiResponseData.API_FAILURE
            apiResponseData.data = null
            apiResponseData.responseCode = response.code()
            var jsonObject: JSONObject? = null
           /* try {
                jsonObject = JSONObject(response.errorBody()!!.string())
                val error: JSONObject = jsonObject.getJSONObject("error")
                val message: String = error.getString("message")
                apiResponseData.message = message
            } catch (e: JSONException) {
                  CommonUtils.printStackTrace(e)
                apiResponseData.message = genericError
            }*/
            try {
                val json = JSONObject(response.errorBody()?.string().orEmpty())
                // Try to get root-level message
                val message = json.optString("message", genericError)
                apiResponseData.message = message
            } catch (e: JSONException) {
                CommonUtils.printStackTrace(e)
                apiResponseData.message = genericError
            }
            if (response.code() != 200 && showDialog) {
                WolooApplication.currentActivity?.let { activity ->
                    CommonUtils.showCustomDialogBackClick(activity, apiResponseData.message)
                }
            }
            webserviceCallback.onWebResponse(apiResponseData)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        val apiResponseData = ApiResponseData<T>()
        onHandleError()
        CommonUtils.printStackTrace(t as Exception)
        apiResponseData.responseCode = -1
        WolooApplication.currentActivity?.let { activity ->
           // CommonUtils.showCustomDialogBackClick(activity, apiResponseData.message)
        }
        webserviceCallback.onWebResponse(apiResponseData)
    }

    private fun onHandleError(responseCode: Int = -1) {
        val apiResponseData = ApiResponseData<T>()
        apiResponseData.status = ApiResponseData.API_FAILURE
        apiResponseData.message = genericError
        apiResponseData.data = null
        apiResponseData.responseCode = responseCode
        webserviceCallback.onWebResponse(apiResponseData)
    }
}