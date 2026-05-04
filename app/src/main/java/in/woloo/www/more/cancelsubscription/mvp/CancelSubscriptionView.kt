package `in`.woloo.www.more.cancelsubscription.mvp

import `in`.woloo.www.more.cancelsubscription.model.CancelSubscriptionResponse

interface CancelSubscriptionView {
    fun cancelSubscriptionResponse(cancelSubscriptionResponse: CancelSubscriptionResponse?)
}
