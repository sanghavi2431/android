package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.SendOtpResponse
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import `in`.woloo.www.common.CommonUtils
import org.json.JSONObject
import retrofit2.Call

class LoginRepository: BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun sendOtp(
        request: SendOtpRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<SendOtpResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<SendOtpResponse>> =
                    apiService.sendOtp(request)
                val callback: ApiServiceCallback<BaseResponse<SendOtpResponse>> =
                    ApiServiceCallback(webserviceCallback ,  showDialog = false)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<SendOtpResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun verifyOtp(
        request: VerifyOtpRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<VerifyOtpResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<VerifyOtpResponse>> =
                    apiService.verifyOtp(request)
                val callback: ApiServiceCallback<BaseResponse<VerifyOtpResponse>> =
                    ApiServiceCallback(webserviceCallback ,  showDialog = false)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<VerifyOtpResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun deleteWolooUser(
        id: Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.deleteWolooUser(id)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun addCoinstoWolooUser(
        request: CreditCoinsRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.addCoinstoWolooUser(request)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback , false)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun updateDeviceToken(
        request: HashMap<String,String>,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<String>>>
    ){
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<String>> =
                    apiService.updateDeviceToken(request)
                val callback: ApiServiceCallback<BaseResponse<String>> =
                    ApiServiceCallback(webserviceCallback , false)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<String>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}