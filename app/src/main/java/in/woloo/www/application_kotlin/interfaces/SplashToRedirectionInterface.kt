package `in`.woloo.www.application_kotlin.interfaces

import `in`.woloo.www.application_kotlin.model.server_response.PendingReviewStatusResponse

interface SplashToRedirectionInterface {
    fun onRedirection()
    fun onAppConfigComplete(`object`: Any?)
    fun pendingReviewStatusResponse(pendingReviewStatusResponse: PendingReviewStatusResponse?)
}
