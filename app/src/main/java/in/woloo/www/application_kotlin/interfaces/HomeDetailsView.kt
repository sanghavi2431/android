package `in`.woloo.www.application_kotlin.interfaces

import android.widget.TextView
import `in`.woloo.www.application_kotlin.model.server_response.LikeResponse
import `in`.woloo.www.application_kotlin.model.server_response.LikeStatusResponse
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse

interface HomeDetailsView {
    fun getReviewList(reviewListResponse: ReviewListResponse?)
    fun getLike_Unlike(likeResponse: LikeResponse?, tv_like: TextView?)
    fun likeStatusSuccess(likeStatusResponse: LikeStatusResponse?)
    fun onRedeemSuccess()
}
