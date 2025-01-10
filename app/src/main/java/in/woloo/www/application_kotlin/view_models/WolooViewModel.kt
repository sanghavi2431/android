package `in`.woloo.www.application_kotlin.view_models

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

class WolooViewModel: BaseViewModel() {

    private var sendOTPServicesLiveData: MutableLiveData<SendOtpResponse>? = null
    private var verifyOTPServicesLiveData: MutableLiveData<VerifyOtpResponse>? = null
    private val mLoginRepository: LoginRepository = LoginRepository()
    private val mSendOtp: EventLiveData<BaseResponse<SendOtpResponse>> = EventLiveData()
    private val mVerifyOtp: EventLiveData<BaseResponse<VerifyOtpResponse>> = EventLiveData()

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

    fun observeSendOtp(): EventLiveData<BaseResponse<SendOtpResponse>> {
        return mSendOtp
    }

    fun observeVerifyOtp(): EventLiveData<BaseResponse<VerifyOtpResponse>> {
        return mVerifyOtp
    }

}
