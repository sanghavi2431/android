package `in`.woloo.www.v2.subscription.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.subscribe.models.GetSubscriptionDetailsResponse
import `in`.woloo.www.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.subscribe.models.PlanResponse
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.subscription.model.InitSubscriptionRequest
import `in`.woloo.www.v2.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.v2.subscription.repository.SubscriptionRepository

class SubscriptionViewModel : BaseViewModel() {

    var subscriptionRepository: SubscriptionRepository = SubscriptionRepository()
    var mySubscriptionResponse: EventLiveData<BaseResponse<GetSubscriptionDetailsResponse.Data>> = EventLiveData()
    var subscriptionPlansResponse: EventLiveData<BaseResponse<ArrayList<PlanResponse.Data>>> = EventLiveData()
    var initSubscriptionResponse: EventLiveData<BaseResponse<InitSubscriptionResponse.Data>> = EventLiveData()
    var submitSubscriptionPurchaseResponse: EventLiveData<BaseResponse<InitSubscriptionResponse>> = EventLiveData()
    var initSubscriptionRequest: InitSubscriptionRequest = InitSubscriptionRequest()
    var subscriptionSelected: EventLiveData<PlanResponse.Data> = EventLiveData()

    fun setSelectedSubscription(subscription: PlanResponse.Data) {
        subscriptionSelected.value = subscription
    }

    fun getMySubscription() {
        updateProgress(true)
        subscriptionRepository.getMySubscription(object : WebserviceCallback<ApiResponseData<BaseResponse<GetSubscriptionDetailsResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<GetSubscriptionDetailsResponse.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mySubscriptionResponse.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    mySubscriptionResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun getSubscriptionPlans() {
        updateProgress(true)
        subscriptionRepository.getSubscriptionPlans(object : WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<PlanResponse.Data>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<PlanResponse.Data>>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    subscriptionPlansResponse.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    subscriptionPlansResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun initSubscriptionByOrder(request: InitSubscriptionRequest) {
        initSubscriptionRequest = request
        updateProgress(true)
        subscriptionRepository.initSubscriptionByOrder(
            request,
            object : WebserviceCallback<ApiResponseData<BaseResponse<InitSubscriptionResponse.Data>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<InitSubscriptionResponse.Data>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        initSubscriptionResponse.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        initSubscriptionResponse.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun submitSubscriptionPurchase(request: SubmitSubscriptionPurchaseRequest) {
        updateProgress(true)
        subscriptionRepository.submitSubscriptionPurchase(
            request,
            object : WebserviceCallback<ApiResponseData<BaseResponse<InitSubscriptionResponse>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<InitSubscriptionResponse>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        submitSubscriptionPurchaseResponse.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        submitSubscriptionPurchaseResponse.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun observeMySubscription(): EventLiveData<BaseResponse<GetSubscriptionDetailsResponse.Data>> {
        return mySubscriptionResponse
    }

    fun observeSubscriptionPlans(): EventLiveData<BaseResponse<ArrayList<PlanResponse.Data>>> {
        return subscriptionPlansResponse
    }
    fun observeInitSubscriptionByOrder(): EventLiveData<BaseResponse<InitSubscriptionResponse.Data>> {
        return initSubscriptionResponse
    }

    fun observeSubmitSubscriptionPurchaseResponse(): EventLiveData<BaseResponse<InitSubscriptionResponse>> {
        return submitSubscriptionPurchaseResponse
    }

    fun observeSubscriptionSelected(): EventLiveData<PlanResponse.Data> {
        return subscriptionSelected
    }
}
