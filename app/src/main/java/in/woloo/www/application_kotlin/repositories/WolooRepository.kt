package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateResponse
import okhttp3.RequestBody
import retrofit2.Call

class WolooRepository : BaseRepository() {

    fun recommendWoloo(
        body: RequestBody,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.recommendWoloo(body)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getRecommendWolooList(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>> =
                    apiService.getRecommendWolooList()
                val callback: ApiServiceCallback<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                 CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun addWoloo(
        body: RequestBody,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.addWoloo(body)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun scanWoloo(
        name: String,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.scanWoloo(name)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun wahCertificate(
        wolooId: String,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<WahCertificateResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<WahCertificateResponse>> =
                    apiService.wahCertificate(wolooId)
                val callback: ApiServiceCallback<BaseResponse<WahCertificateResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<WahCertificateResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}