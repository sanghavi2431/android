package `in`.woloo.www.v2.login.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.login.model.VerifyOtpRequest
import `in`.woloo.www.v2.login.model.VerifyOtpResponse
import `in`.woloo.www.v2.util.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class LoginRepository: BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun sendOtp(
        request: SendOtpRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<SendOtpResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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

    fun deleteWolooUser(
        id: Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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

    fun updateDeviceToken(
        request: HashMap<String,String>,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<String>>>
    ){
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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