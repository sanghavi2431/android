package `in`.woloo.www.v2.review.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.review.models.ReviewOptionsResponse
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.ApiServiceCallback
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.review.SubmitReviewRequest
import `in`.woloo.www.v2.util.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class ReviewRepository : BaseRepository() {

    fun getReviewOptions(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ReviewOptionsResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<ReviewOptionsResponse.Data>> =
                    apiService.getReviewOptions()
                val callback: ApiServiceCallback<BaseResponse<ReviewOptionsResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<ReviewOptionsResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun submitReview(
        request: SubmitReviewRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.submitReview(request)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}
