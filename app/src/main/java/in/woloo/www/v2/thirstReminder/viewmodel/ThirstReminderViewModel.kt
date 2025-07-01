package `in`.woloo.www.v2.thirstReminder.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.login.model.VerifyOtpResponse
import `in`.woloo.www.v2.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.v2.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.v2.thirstReminder.repository.ThirstReminderRepository

class ThirstReminderViewModel : BaseViewModel(){
    private val thirstReminderRepository: ThirstReminderRepository = ThirstReminderRepository()
    private val thirstReminderResponse: EventLiveData<BaseResponse<ThirstReminderResponse>> = EventLiveData()

    fun thirstReminder(
        request: ThirstReminderRequest
    ) {
        updateProgress(true)
        thirstReminderRepository.thirstReminder(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<ThirstReminderResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ThirstReminderResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    thirstReminderResponse.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    thirstReminderResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeThirstReminder(): EventLiveData<BaseResponse<ThirstReminderResponse>> {
        return thirstReminderResponse
    }
}