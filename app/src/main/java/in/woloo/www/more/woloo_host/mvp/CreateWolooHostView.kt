package `in`.woloo.www.more.woloo_host.mvp

import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.woloo_host.model.GeoCodeResponse

interface CreateWolooHostView {
    fun setProfileResponse(viewProfileResponse: ViewProfileResponse?)
    fun addWolooHostSuccess(message: String?)
    fun geoCodeResponseSuccess(geoCodeResponse: GeoCodeResponse?)
}
