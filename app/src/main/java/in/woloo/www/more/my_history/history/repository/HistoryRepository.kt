package `in`.woloo.www.v2.giftcard.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.more.my_history.model.MyHistoryResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

class HistoryRepository {


    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun wolooRewardHistory(
        pageNumber: Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MyHistoryResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MyHistoryResponse.Data>> =
                    service.getWolooRewardHistory(pageNumber)
                val callback: ApiServiceCallback<BaseResponse<MyHistoryResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                 CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<MyHistoryResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun myOffers(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    service.myOffers()
                val callback: ApiServiceCallback<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}