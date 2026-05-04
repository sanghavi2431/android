package `in`.woloo.www.more.editprofile.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.more.editprofile.profile.model.EditProfileResponse
import `in`.woloo.www.more.editprofile.profile.repository.ProfileRepository
import `in`.woloo.www.v2.profile.model.UserProfile
import okhttp3.RequestBody

class ProfileViewModel : BaseViewModel() {

    private val mProfileRepository: ProfileRepository = ProfileRepository()
    private val mEditProfile: EventLiveData<BaseResponse<EditProfileResponse>> = EventLiveData()
    private val mProfile: EventLiveData<BaseResponse<UserProfile>> = EventLiveData()
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    private val _toastMessageErrorCode = MutableLiveData<Int>()
    val toastMessageErrorCode: LiveData<Int> get() = _toastMessageErrorCode

    fun getUserProfile() {
        try {
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
        }catch (e: Exception) {
          WolooApplication.errorMessage  = "Something went wrong! Please try again."
        }
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
                    Log.e("TAG", "onWebResponse: " + data.responseCode + data.message)
                    WolooApplication.errorMessage = data.message
                  //  mEditProfile.value = data.data
                    notifyNetworkError(data)
                        _toastMessage.postValue(data.message ?: "Bad Request")
                        _toastMessageErrorCode.postValue(data.responseCode ?: 0)
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