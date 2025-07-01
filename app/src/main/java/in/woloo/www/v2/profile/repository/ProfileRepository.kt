package `in`.woloo.www.v2.profile.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.profile.model.EditProfileResponse
import `in`.woloo.www.v2.profile.model.UserProfile
import `in`.woloo.www.v2.util.NetworkUtils
import okhttp3.RequestBody
import retrofit2.Call

class ProfileRepository: BaseRepository() {

    val service: ApiService = ApiServiceClientAdapter.instance.apiService

    fun getUserProfile(webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<UserProfile>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<UserProfile>> =
                    apiService.getUserProfile(CommonUtils().userInfo.id.toString())
                val callback: ApiServiceCallback<BaseResponse<UserProfile>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<UserProfile>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun updateProfile(
        body: RequestBody,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<EditProfileResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<EditProfileResponse>> =
                    apiService.updateProfile(body)
                val callback: ApiServiceCallback<BaseResponse<EditProfileResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                  CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<EditProfileResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}