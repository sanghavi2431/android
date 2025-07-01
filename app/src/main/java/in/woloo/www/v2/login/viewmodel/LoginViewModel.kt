package `in`.woloo.www.v2.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.login.model.VerifyOtpRequest
import `in`.woloo.www.v2.login.model.VerifyOtpResponse
import `in`.woloo.www.v2.login.repository.LoginRepository
import org.json.JSONObject


class LoginViewModel : BaseViewModel() {

    private val mLoginRepository: LoginRepository = LoginRepository()
    private val mSendOtp: EventLiveData<BaseResponse<SendOtpResponse>> = EventLiveData()
    private val mVerifyOtp: EventLiveData<BaseResponse<VerifyOtpResponse>> = EventLiveData()
    private val mDeleteUser: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val _otpMessage = MutableLiveData<String?>()
    val otpMessage: LiveData<String?> get() = _otpMessage


    fun sendOtp(request: SendOtpRequest) {
        updateProgress(true)
        mLoginRepository.sendOtp(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<SendOtpResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<SendOtpResponse>>) {
                updateProgress(false)

                // ✅ Always set message (even on success or failure)

Logger.e("Aarati data.status",data.message.toString())

                if (data.status == ApiResponseData.API_SUCCESS) {
                    mSendOtp.value = data.data
                } else {
                    _otpMessage.value = data.message
                    WolooApplication.setErrorMessage(data.message)
                    mSendOtp.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun verifyOtp(
        request: VerifyOtpRequest
    ) {
        updateProgress(true)
        mLoginRepository.verifyOtp(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<VerifyOtpResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<VerifyOtpResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mVerifyOtp.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    mVerifyOtp.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun deleteWolooUser(
       id: Int
    ) {
        updateProgress(true)
        mLoginRepository.deleteWolooUser(id, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mDeleteUser.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    mDeleteUser.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun updateDeviceToken(
        request: HashMap<String,String>
    ) {
        updateProgress(true)
        mLoginRepository.updateDeviceToken(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<String>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<String>>) {
                updateProgress(false)
            }
        })
    }

    fun observeSendOtp(): EventLiveData<BaseResponse<SendOtpResponse>> {
        return mSendOtp
    }

    fun observeVerifyOtp(): EventLiveData<BaseResponse<VerifyOtpResponse>> {
        return mVerifyOtp
    }

    fun observeDeleteWolooUser(): EventLiveData<BaseResponse<JSONObject>> {
        return mDeleteUser
    }

    fun clearOtpMessage() {
        _otpMessage.value = null
    }


}