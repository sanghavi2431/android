package `in`.woloo.www.application_kotlin.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.lists_models.Voucher
import `in`.woloo.www.application_kotlin.model.server_request.NearByWolooAndOfferCountRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_request.ReviewListRequest
import `in`.woloo.www.application_kotlin.model.server_request.VoucherRequest
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse
import `in`.woloo.www.application_kotlin.model.server_response.PendingReviewStatusResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.giftcard.giftcard.model.ValidateGiftCardResponse
import `in`.woloo.www.application_kotlin.repositories.HomeRepository
import `in`.woloo.www.more.editprofile.profile.model.ShowProfileResponse
import org.json.JSONObject

class HomeViewModel : BaseViewModel() {
    val TAG =javaClass.name

    private val mHomeRepository: HomeRepository = HomeRepository()
    private val mGetNearbyWoloos: EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> = EventLiveData()
    private val mVoucher: EventLiveData<BaseResponse<Voucher>> = EventLiveData()
    private val mShowProfile: EventLiveData<BaseResponse<ShowProfileResponse>> = EventLiveData()
    private val mValidateGiftCard: EventLiveData<BaseResponse<ValidateGiftCardResponse>> = EventLiveData()
    private val mNearByWolooAndOfferCount: EventLiveData<BaseResponse<NearByWolooAndOfferCountResponse.Data>> = EventLiveData()
    private val mWolooEngagement: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val mReviewList: EventLiveData<BaseResponse<ReviewListResponse.Data>> = EventLiveData()
    private val mPendingReviewStatusResponse = EventLiveData<BaseResponse<PendingReviewStatusResponse.Data>>()
    private val redeemOfferResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()


    fun getNearbyWoloos(request: NearbyWolooRequest) {
        Log.d(TAG, "getNearbyWoloos:$request")
        //updateProgress(true)
        mHomeRepository.getNearbyWoloos(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>) {
                //updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetNearbyWoloos.value = data.data
                } else {
                    mGetNearbyWoloos.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun showProfile(userId: String) {
        mHomeRepository.showProfile(userId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<ShowProfileResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ShowProfileResponse>>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mShowProfile.value = data.data
                } else {
                    mShowProfile.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun applyVoucher(request: VoucherRequest) {
        updateProgress(true)
        mHomeRepository.applyVoucher(
            request,
            object : WebserviceCallback<ApiResponseData<BaseResponse<Voucher>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<Voucher>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mVoucher.value = data.data
                    } else {
                        WolooApplication.errorMessage = data.message
                        mVoucher.value = data.data
                        notifyNetworkError(data)
                    }
                }
            })
    }

    fun validateGiftCard(giftCardId : String){
        updateProgress(true)
        mHomeRepository.validateGiftCard(giftCardId,object :
            WebserviceCallback<ApiResponseData<BaseResponse<ValidateGiftCardResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ValidateGiftCardResponse>>) {
                updateProgress(false)
                if(data.status == ApiResponseData.API_SUCCESS){
                    mValidateGiftCard.value = data.data
                }else{
                    WolooApplication.errorMessage = data.message
                    mValidateGiftCard.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun getNearByWolooAndOfferCount(request: NearByWolooAndOfferCountRequest){
        updateProgress(true)
        mHomeRepository.getNearByWolooAndOfferCount(request,object :
            WebserviceCallback<ApiResponseData<BaseResponse<NearByWolooAndOfferCountResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<NearByWolooAndOfferCountResponse.Data>>) {
                updateProgress(false)
                if(data.status == ApiResponseData.API_SUCCESS){
                    mNearByWolooAndOfferCount.value = data.data
                }else{
                    WolooApplication.errorMessage = data.message
                    mNearByWolooAndOfferCount.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun wolooEngagement(request: WolooEngagementRequest){
        updateProgress(true)
        mHomeRepository.wolooEngagement(request,object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if(data.status == ApiResponseData.API_SUCCESS){
                    mWolooEngagement.value = data.data
                }else{
                    WolooApplication.errorMessage = data.message
                    mWolooEngagement.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun getReviewList(request: ReviewListRequest){
        updateProgress(true)
        mHomeRepository.getReviewList(request,object :
            WebserviceCallback<ApiResponseData<BaseResponse<ReviewListResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ReviewListResponse.Data>>) {
                updateProgress(false)
                if(data.status == ApiResponseData.API_SUCCESS){
                    mReviewList.value = data.data
                }else{
                    WolooApplication.errorMessage = data.message
                    mReviewList.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observePendingReviewStatus(): MutableLiveData<BaseResponse<PendingReviewStatusResponse.Data>> {
        return mPendingReviewStatusResponse
    }

    fun getPendingReviewStatus(){
        mHomeRepository.getPendingReviewStatus(object :
            WebserviceCallback<ApiResponseData<BaseResponse<PendingReviewStatusResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<PendingReviewStatusResponse.Data>>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mPendingReviewStatusResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mPendingReviewStatusResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }


    fun observeReviewList(): EventLiveData<BaseResponse<ReviewListResponse.Data>> {
        return mReviewList
    }

    fun observeWolooEngagement(): EventLiveData<BaseResponse<JSONObject>> {
        return mWolooEngagement
    }

    fun observeNearByWolooAndOfferCount(): EventLiveData<BaseResponse<NearByWolooAndOfferCountResponse.Data>> {
        return mNearByWolooAndOfferCount
    }

    fun observeVoucher(): EventLiveData<BaseResponse<Voucher>> {
        return mVoucher
    }

    fun observeNearByWoloo(): EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> {
        return mGetNearbyWoloos
    }

    fun observeShowProfile(): EventLiveData<BaseResponse<ShowProfileResponse>> {
        return mShowProfile
    }

    fun observeValidateGiftCard(): EventLiveData<BaseResponse<ValidateGiftCardResponse>> {
        return mValidateGiftCard
    }

    fun redeemOffer(offerId: Int) {
        updateProgress(true)
        mHomeRepository.redeemOffer(offerId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MessageResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    redeemOfferResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    redeemOfferResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeRedeemOffer(): EventLiveData<BaseResponse<MessageResponse>> {
        return redeemOfferResponse
    }
}