package `in`.woloo.www.v2.profile.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.profile.model.EditProfileResponse
import `in`.woloo.www.v2.profile.model.UserProfile
import `in`.woloo.www.v2.profile.repository.ProfileRepository
import okhttp3.RequestBody

class ProfileViewModel : BaseViewModel() {

    private val mProfileRepository: ProfileRepository = ProfileRepository()
    private val mEditProfile: EventLiveData<BaseResponse<EditProfileResponse>> = EventLiveData()
    private val mProfile: EventLiveData<BaseResponse<UserProfile>> = EventLiveData()

    fun getUserProfile() {
        updateProgress(true)
        mProfileRepository.getUserProfile(object :
                WebserviceCallback<ApiResponseData<BaseResponse<UserProfile>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<UserProfile>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mProfile.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mProfile.value = data.data
                        notifyNetworkError(data)
                    }
                }
            })
    }

    fun updateProfile(
        body: RequestBody
    ) {
        updateProgress(true)
        mProfileRepository.updateProfile(
            body,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<EditProfileResponse>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<EditProfileResponse>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mEditProfile.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mEditProfile.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun observeUserProfile(): EventLiveData<BaseResponse<UserProfile>> {
        return mProfile
    }

    fun observeEditProfile(): EventLiveData<BaseResponse<EditProfileResponse>> {
        return mEditProfile
    }
}
