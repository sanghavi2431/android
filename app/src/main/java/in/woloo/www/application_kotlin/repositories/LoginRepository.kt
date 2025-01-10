package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.SendOtpResponse
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
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
                    ApiServiceCallback(webserviceCallback)
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
                    ApiServiceCallback(webserviceCallback)
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

    fun updateDeviceToken(
        request: HashMap<String,String>,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<String>>>
    ){
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<String>> =
                    apiService.updateDeviceToken(request)
                val callback: ApiServiceCallback<BaseResponse<String>> =
                    ApiServiceCallback(webserviceCallback)
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