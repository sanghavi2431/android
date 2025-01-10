package `in`.woloo.www.shopping.mvp

import `in`.woloo.www.shopping.model.GeoCodeResponse

interface GeoCodeView {
    fun setGeoCodeResponse(geoCodeResponse: GeoCodeResponse?)
}
