package `in`.woloo.www.more.thirstreminder.thirstReminder.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.more.thirstreminder.thirstReminder.repository.ThirstReminderRepository

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
                    WolooApplication.errorMessage = data.message
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