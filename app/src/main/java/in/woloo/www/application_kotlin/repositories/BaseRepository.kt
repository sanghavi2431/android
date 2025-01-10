package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.lists_models.ReverseGeocodeItem
import `in`.woloo.www.application_kotlin.model.server_request.ReverseGeocodeRequest
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

open class BaseRepository {

    val apiService: ApiService = ApiServiceClientAdapter.instance.apiService

    fun appConfig(
        request: LocaleRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<AuthConfigResponse.Data>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<AuthConfigResponse.Data>> =
                    apiService.appConfig(request)
                val callback: ApiServiceCallback<BaseResponse<AuthConfigResponse.Data>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
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
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
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