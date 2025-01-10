package `in`.woloo.www.more.giftcard.giftcard.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsResponse
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.more.giftcard.giftcard.repository.CoinsRepository


class CoinsViewModel : BaseViewModel(){
    private val mCoinsRepository: CoinsRepository = CoinsRepository()
    private val mAddCoins: EventLiveData<BaseResponse<AddCoinsResponse>> = EventLiveData()
    private val mUserCoins: EventLiveData<BaseResponse<UserCoins>> = EventLiveData()
    private val mCoinHistory: EventLiveData<BaseResponse<UserCoinHistoryModel.Data>> = EventLiveData()

    fun addCoins(request: AddCoinsRequest) {
        updateProgress(true)
        mCoinsRepository.addCoins(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<AddCoinsResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<AddCoinsResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddCoins.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddCoins.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun getUserCoins() {
        updateProgress(true)
        mCoinsRepository.userCoins(object :
            WebserviceCallback<ApiResponseData<BaseResponse<UserCoins>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<UserCoins>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mUserCoins.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mUserCoins.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeAddCoins(): EventLiveData<BaseResponse<AddCoinsResponse>> {
        return mAddCoins
    }

    fun observeUserCoins(): EventLiveData<BaseResponse<UserCoins>> {
        return mUserCoins
    }

    fun getCoinHistory(pageNumber: Int) {
        updateProgress(true)
        mCoinsRepository.coinHistory(pageNumber,object :
            WebserviceCallback<ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCoinHistory.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCoinHistory.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeCoinHistory(): EventLiveData<BaseResponse<UserCoinHistoryModel.Data>> {
        return mCoinHistory
    }
}