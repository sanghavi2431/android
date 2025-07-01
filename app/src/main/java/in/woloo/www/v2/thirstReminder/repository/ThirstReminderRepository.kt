package `in`.woloo.www.v2.thirstReminder.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.v2.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.v2.util.NetworkUtils
import retrofit2.Call

class ThirstReminderRepository : BaseRepository(){

    fun thirstReminder(
        request: ThirstReminderRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ThirstReminderResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<ThirstReminderResponse>> =
                    apiService.thirstRemainder(request)
                val callback: ApiServiceCallback<BaseResponse<ThirstReminderResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ThirstReminderResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}