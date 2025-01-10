package `in`.woloo.www.more.refer_woloo_host.mvp

import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse
import `in`.woloo.www.more.woloo_host.model.GeoCodeResponse

interface ReferredWolooView {
    fun referredWolooListResponse(referredWolooListResponse: ReferredWolooListResponse?)
    fun setProfileResponse(viewProfileResponse: ViewProfileResponse?)
    fun referWolooHostSuccess(message: String?)
    fun geoCodeResponseSuccess(geoCodeResponse: GeoCodeResponse?)
}
