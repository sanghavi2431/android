package `in`.woloo.www.application_kotlin.interfaces

import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel

interface HomeViewPresenterInterface {
    fun onGetNearByStore(
        nearByStoreResponse: NearByStoreResponse?,
        networkAPICallModel: NetworkAPICallModel?
    )

    fun authConfigSuccess(authConfigResponse: AuthConfigResponse?)
}
