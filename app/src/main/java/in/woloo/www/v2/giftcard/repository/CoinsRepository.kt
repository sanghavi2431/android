package `in`.woloo.www.v2.giftcard.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.giftcard.model.AddCoinsRequest
import `in`.woloo.www.v2.giftcard.model.AddCoinsResponse
import `in`.woloo.www.v2.giftcard.model.UserCoins
import `in`.woloo.www.v2.util.NetworkUtils
import retrofit2.Call

class CoinsRepository {


    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun addCoins(request: AddCoinsRequest,
                 webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<AddCoinsResponse>>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
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
