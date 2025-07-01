package `in`.woloo.www.v2.giftcard.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.dashboard.ui.home.model.NearByStoreResponse
import `in`.woloo.www.my_history.model.MyHistoryResponse
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
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