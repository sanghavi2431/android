package `in`.woloo.www.v2.invite.viewmodel

import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.invite.repository.InviteRepository
import `in`.woloo.www.v2.invite.model.InviteRequest
import org.json.JSONObject

class InviteViewModel : BaseViewModel() {
    private val inviteRepository: InviteRepository = InviteRepository()
    private val inviteResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()


    fun invite(mobileNumber: ArrayList<String>) {
        updateProgress(true)
        val request = InviteRequest()
        request.mobileNumbers = mobileNumber
        inviteRepository.invite(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    inviteResponse.value = data.data
                } else {
                    inviteResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeInvite(): EventLiveData<BaseResponse<JSONObject>> {
        return inviteResponse
    }
}