package `in`.woloo.www.v2.periodtracker.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.giftcard.model.AddCoinsRequest
import `in`.woloo.www.v2.giftcard.model.AddCoinsResponse
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.v2.util.NetworkUtils
import retrofit2.Call

class PeriodTrackerRepository {


    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun periodTracker(
        request: PeriodTrackerRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<PeriodTrackerResponse.Data>> =
                    service.periodTracker(request)
                val callback: ApiServiceCallback<BaseResponse<PeriodTrackerResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getPeriodTracker(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<PeriodTrackerResponse.Data>> =
                    service.getPeriodTracker()
                val callback: ApiServiceCallback<BaseResponse<PeriodTrackerResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

}
