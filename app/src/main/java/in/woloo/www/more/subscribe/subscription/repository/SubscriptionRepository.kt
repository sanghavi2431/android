package `in`.woloo.www.more.subscribe.subscription.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.subscribe.models.GetSubscriptionDetailsResponse
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.more.subscribe.models.PlanResponse
import `in`.woloo.www.application_kotlin.repositories.BaseRepository
import `in`.woloo.www.more.subscribe.subscription.model.InitSubscriptionRequest
import `in`.woloo.www.more.subscribe.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

class SubscriptionRepository : BaseRepository() {


    fun getMySubscription(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<GetSubscriptionDetailsResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<GetSubscriptionDetailsResponse.Data>> =
                    apiService.getMySubscription()
                val callback: ApiServiceCallback<BaseResponse<GetSubscriptionDetailsResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<GetSubscriptionDetailsResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getSubscriptionPlans(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<PlanResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<PlanResponse.Data>>> =
                    apiService.getSubscriptionPlan()
                val callback: ApiServiceCallback<BaseResponse<ArrayList<PlanResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<PlanResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun initSubscriptionByOrder(
        request: InitSubscriptionRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<InitSubscriptionResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<InitSubscriptionResponse.Data>> =
                    apiService.initSubscriptionByOrder(request)
                val callback: ApiServiceCallback<BaseResponse<InitSubscriptionResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<InitSubscriptionResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun submitSubscriptionPurchase(
        request: SubmitSubscriptionPurchaseRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<InitSubscriptionResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<InitSubscriptionResponse>> =
                    apiService.submitSubscriptionPurchase(request)
                val callback: ApiServiceCallback<BaseResponse<InitSubscriptionResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<InitSubscriptionResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}