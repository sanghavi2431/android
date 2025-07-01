package `in`.woloo.www.v2.enroute.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.dashboard.ui.home.model.NearByStoreResponse
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.enroute.model.EnrouteRequest
import `in`.woloo.www.v2.util.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class EnrouteRepository : BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun getEnrouteWoloo(
        request: EnrouteRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    apiService.getEnrouteWoloo(request)
                val callback: ApiServiceCallback<BaseResponse<ArrayList<NearByStoreResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getWolooNavigationReward(
        wolooId: Int,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.getWolooNavigationReward(wolooId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}
