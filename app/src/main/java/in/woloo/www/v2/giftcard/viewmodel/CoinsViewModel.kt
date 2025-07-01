package `in`.woloo.www.v2.giftcard.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.giftcard.model.AddCoinsRequest
import `in`.woloo.www.v2.giftcard.model.AddCoinsResponse
import `in`.woloo.www.v2.giftcard.model.UserCoins
import `in`.woloo.www.v2.giftcard.repository.CoinsRepository

class CoinsViewModel : BaseViewModel() {
    private val mCoinsRepository: CoinsRepository = CoinsRepository()
    private val mAddCoins: EventLiveData<BaseResponse<AddCoinsResponse>> = EventLiveData()
    private val mUserCoins: EventLiveData<BaseResponse<UserCoins>> = EventLiveData()
    private val mCoinHistory: EventLiveData<BaseResponse<UserCoinHistoryModel.Data>> = EventLiveData()

    fun addCoins(request: AddCoinsRequest) {
        updateProgress(true)
        mCoinsRepository.addCoins(
            request,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<AddCoinsResponse>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<AddCoinsResponse>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mAddCoins.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mAddCoins.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
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
                        WolooApplication.setErrorMessage(data.message)
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
        mCoinsRepository.coinHistory(
            pageNumber,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<UserCoinHistoryModel.Data>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mCoinHistory.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mCoinHistory.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun observeCoinHistory(): EventLiveData<BaseResponse<UserCoinHistoryModel.Data>> {
        return mCoinHistory
    }
}
