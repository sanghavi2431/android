package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.lists_models.Voucher
import `in`.woloo.www.application_kotlin.model.server_request.NearByWolooAndOfferCountRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_request.ReviewListRequest
import `in`.woloo.www.application_kotlin.model.server_request.VoucherRequest
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse
import `in`.woloo.www.application_kotlin.model.server_response.PendingReviewStatusResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.application_kotlin.repositories.BaseRepository
import `in`.woloo.www.more.giftcard.giftcard.model.ValidateGiftCardResponse
import `in`.woloo.www.more.editprofile.profile.model.ShowProfileResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class HomeRepository : BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun getNearbyWoloos(
        request: NearbyWolooRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    apiService.getNearbyWoloos(request)
                val callback: ApiServiceCallback<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getNearByWolooAndOfferCount(
        request: NearByWolooAndOfferCountRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<NearByWolooAndOfferCountResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<NearByWolooAndOfferCountResponse.Data>> =
                    apiService.getNearByWolooAndOfferCount(request)
                val callback: ApiServiceCallback<BaseResponse<NearByWolooAndOfferCountResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<NearByWolooAndOfferCountResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun showProfile(
        userId: String,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ShowProfileResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ShowProfileResponse>> =
                    apiService.showProfile(userId)
                val callback: ApiServiceCallback<BaseResponse<ShowProfileResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ShowProfileResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun applyVoucher(request: VoucherRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<Voucher>>>){
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<Voucher>> =
                    apiService.applyVoucher(request)
                val callback: ApiServiceCallback<BaseResponse<Voucher>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<Voucher>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun validateGiftCard(giftCardId : String, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ValidateGiftCardResponse>>>){
        if(NetworkUtils.isInternetAvailable(WolooApplication.instance!!)){
            try{
                val call : Call<BaseResponse<ValidateGiftCardResponse>> = apiService.verifyGiftCardId(giftCardId)
                val callback = ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e : Exception){
                  CommonUtils.printStackTrace(e)
            }
        }
    }

    fun wolooEngagement(request: WolooEngagementRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>){
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.wolooEngagements(request)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getReviewList(request: ReviewListRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ReviewListResponse.Data>>>){
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ReviewListResponse.Data>> =
                    apiService.getReviewList(request)
                val callback: ApiServiceCallback<BaseResponse<ReviewListResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ReviewListResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getPendingReviewStatus(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<PendingReviewStatusResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<PendingReviewStatusResponse.Data>> =
                    apiService.getPendingReviewStatus()
                val callback: ApiServiceCallback<BaseResponse<PendingReviewStatusResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<PendingReviewStatusResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun redeemOffer(
        offerId: Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.redeemOffer(offerId)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                e.printStackTrace()
            }

        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}