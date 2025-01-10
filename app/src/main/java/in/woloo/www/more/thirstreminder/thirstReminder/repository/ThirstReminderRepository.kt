package `in`.woloo.www.more.thirstreminder.thirstReminder.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.repositories.BaseRepository
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import retrofit2.Call

class ThirstReminderRepository : BaseRepository(){

    fun thirstReminder(
        request: ThirstReminderRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ThirstReminderResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
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