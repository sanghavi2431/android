package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.ReviewOptionsResponse
import `in`.woloo.www.application_kotlin.repositories.BaseRepository
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_request.SubmitReviewRequest
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class ReviewRepository : BaseRepository() {

    fun getReviewOptions(
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ReviewOptionsResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ReviewOptionsResponse.Data>> =
                    apiService.getReviewOptions()
                val callback: ApiServiceCallback<BaseResponse<ReviewOptionsResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.submitReview(request)
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
}