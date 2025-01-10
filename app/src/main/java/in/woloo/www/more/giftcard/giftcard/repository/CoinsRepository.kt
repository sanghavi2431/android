package `in`.woloo.www.more.giftcard.giftcard.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsResponse
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

class CoinsRepository {


    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun addCoins(request: AddCoinsRequest,
                 webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<AddCoinsResponse>>>
    )
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<AddCoinsResponse>> =
                    service.addCoin(request)
                val callback: ApiServiceCallback<BaseResponse<AddCoinsResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                 CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<AddCoinsResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun userCoins(webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<UserCoins>>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<UserCoins>> =
                    service.getUserCoins()
                val callback: ApiServiceCallback<BaseResponse<UserCoins>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                 CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<UserCoins>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun coinHistory(pageNumber: Int,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<UserCoinHistoryModel.Data>> =
                    service.getCoinHistory(pageNumber)
                val callback: ApiServiceCallback<BaseResponse<UserCoinHistoryModel.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}
