package `in`.woloo.www.v2.base

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.splash.AuthConfigResponse
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.geocode.ReverseGeocodeItem
import `in`.woloo.www.v2.geocode.ReverseGeocodeRequest
import `in`.woloo.www.v2.splash.model.LocaleRequest
import `in`.woloo.www.v2.util.NetworkUtils
import retrofit2.Call

open class BaseRepository {

    val apiService: ApiService = ApiServiceClientAdapter.instance.apiService

    fun appConfig(
        request: LocaleRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<AuthConfigResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<AuthConfigResponse.Data>> =
                    apiService.appConfig(request)
                val callback: ApiServiceCallback<BaseResponse<AuthConfigResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<AuthConfigResponse.Data>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun reverseGeocoding(
        request: ReverseGeocodeRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<ReverseGeocodeItem>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<ArrayList<ReverseGeocodeItem>>> =
                    apiService.reverseGeocoding(request)
                val callback: ApiServiceCallback<BaseResponse<ArrayList<ReverseGeocodeItem>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<ReverseGeocodeItem>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}
