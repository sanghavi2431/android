package `in`.woloo.www.v2.giftcard.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.more.my_history.model.MyHistoryResponse
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.v2.giftcard.repository.HistoryRepository


class HistoryViewModel : BaseViewModel(){
    private val historyRepository: HistoryRepository = HistoryRepository()
    private val historyResponse: EventLiveData<BaseResponse<MyHistoryResponse.Data>> = EventLiveData()
    private val myOffersResponse: EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> = EventLiveData()

    fun getRewardHistory(pageNumber: Int) {
        updateProgress(true)
        historyRepository.wolooRewardHistory(pageNumber, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MyHistoryResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MyHistoryResponse.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    historyResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    historyResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeRewardHistory(): EventLiveData<BaseResponse<MyHistoryResponse.Data>> {
        return historyResponse
    }

    fun getMyOffers() {
        updateProgress(true)
        historyRepository.myOffers(object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    myOffersResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    myOffersResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeMyOffers(): EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> {
        return myOffersResponse
    }
}