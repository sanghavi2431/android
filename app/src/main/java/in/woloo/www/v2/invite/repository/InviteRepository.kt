package `in`.woloo.www.v2.invite.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.invite.model.InviteRequest
import org.json.JSONObject
import retrofit2.Call

class InviteRepository : BaseRepository() {

    fun invite(
        request: InviteRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.invite(request)
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