package `in`.woloo.www.more.mvp

import `in`.woloo.www.more.models.SubscriptionStatusResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.models.VoucherDetailsResponse

interface MoreView {
    fun setProfileResponse(viewProfileResponse: ViewProfileResponse?)
    fun editProfileSuccess()
    fun userCoinsResponseSuccess(userCoinsResponse: UserCoinsResponse?)
    fun setSubscriptionResponse(subscriptionStatusResponse: SubscriptionStatusResponse?)
    fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?)
    fun setVoucherResponse(voucherDetailsResponse: VoucherDetailsResponse?)
}
