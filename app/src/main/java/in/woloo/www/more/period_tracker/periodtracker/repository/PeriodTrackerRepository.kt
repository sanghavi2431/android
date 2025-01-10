package `in`.woloo.www.more.period_tracker.periodtracker.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

class PeriodTrackerRepository {


    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun periodTracker(
        request: PeriodTrackerRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
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
