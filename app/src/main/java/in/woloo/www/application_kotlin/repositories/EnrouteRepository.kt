package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_request.EnrouteRequest
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class EnrouteRepository : BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun getEnrouteWoloo(
        request: EnrouteRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    apiService.getEnrouteWoloo(request)
                val callback: ApiServiceCallback<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getWolooNavigationReward(
        wolooId : Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.getWolooNavigationReward(wolooId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}