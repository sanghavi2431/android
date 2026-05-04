package `in`.woloo.www.application_kotlin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.SendOtpResponse
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.application_kotlin.repositories.LoginRepository
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import org.json.JSONObject

class WolooViewModel: BaseViewModel() {

    private var sendOTPServicesLiveData: MutableLiveData<SendOtpResponse>? = null
    private var verifyOTPServicesLiveData: MutableLiveData<VerifyOtpResponse>? = null
    private val mLoginRepository: LoginRepository = LoginRepository()
    private val mSendOtp: EventLiveData<BaseResponse<SendOtpResponse>> = EventLiveData()
    private val mVerifyOtp: EventLiveData<BaseResponse<VerifyOtpResponse>> = EventLiveData()
    private val mDeleteUser: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val mUserCoins: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val _otpMessage = MutableLiveData<String?>()
    val otpMessage: LiveData<String?> get() = _otpMessage

    fun sendOtp(
        request: SendOtpRequest
    ) {
        updateProgress(true)
        mLoginRepository.sendOtp(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<SendOtpResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<SendOtpResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mSendOtp.value = data.data
                } else {
                    _otpMessage.value = data.message
                    WolooApplication.errorMessage = data.message
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
                    WolooApplication.errorMessage = data.message
                    mVerifyOtp.value = data.data
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
                    WolooApplication.errorMessage =data.message
                    mDeleteUser.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun addCoinstoWolooUser(
        request: CreditCoinsRequest
    ) {
        updateProgress(true)
        mLoginRepository.addCoinstoWolooUser(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mUserCoins.value = data.data
                } else {
                    WolooApplication.errorMessage =data.message
                    mUserCoins.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }


    fun observeAddCoinstoWolooUser(): EventLiveData<BaseResponse<JSONObject>> {
        return mUserCoins
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
