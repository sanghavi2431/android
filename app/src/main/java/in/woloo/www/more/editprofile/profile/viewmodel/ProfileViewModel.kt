package `in`.woloo.www.more.editprofile.profile.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.more.editprofile.profile.model.EditProfileResponse
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.repository.ProfileRepository
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
                    WolooApplication.errorMessage = data.message
                    mProfile.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun updateProfile(
        body: RequestBody) {
        updateProgress(true)
        mProfileRepository.updateProfile(body, object :
            WebserviceCallback<ApiResponseData<BaseResponse<EditProfileResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<EditProfileResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mEditProfile.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mEditProfile.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeUserProfile(): EventLiveData<BaseResponse<UserProfile>> {
        return mProfile
    }

    fun observeEditProfile(): EventLiveData<BaseResponse<EditProfileResponse>> {
        return mEditProfile
    }

}