package `in`.woloo.www.application_kotlin.view_models

import android.util.Log
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.model.server_response.ReviewOptionsResponse
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_request.SubmitReviewRequest
import `in`.woloo.www.application_kotlin.repositories.ReviewRepository
import org.json.JSONObject

class ReviewViewModel  : BaseViewModel(){
    private val mReviewRepository: ReviewRepository = ReviewRepository()
    private val mReviewOptionsResponse: EventLiveData<BaseResponse<ReviewOptionsResponse.Data>> = EventLiveData()
    private val mSubmitReviewResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()

    fun getReviewOptions() {
        updateProgress(true)
        mReviewRepository.getReviewOptions(object :
            WebserviceCallback<ApiResponseData<BaseResponse<ReviewOptionsResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ReviewOptionsResponse.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mReviewOptionsResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mReviewOptionsResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeReviewOptions(): EventLiveData<BaseResponse<ReviewOptionsResponse.Data>> {
        return mReviewOptionsResponse
    }

    fun submitReview(wolooId:Int,  userRating:Int, reviewOptionList:ArrayList<Int>,  reviewDescription:String) {
        updateProgress(true)
        val request  = SubmitReviewRequest()

        request.rating = userRating
        request.wolooId = wolooId
        request.ratingOption = reviewOptionList
        request.reviewDescription = reviewDescription
        Log.e("TAG", "submitReview: {$wolooId}${userRating}${reviewDescription}" )
        mReviewRepository.submitReview(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mSubmitReviewResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mSubmitReviewResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeSubmitReview(): EventLiveData<BaseResponse<JSONObject>> {
        return mSubmitReviewResponse
    }
}