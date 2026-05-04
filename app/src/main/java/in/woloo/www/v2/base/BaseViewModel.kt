package `in`.woloo.www.v2.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import `in`.woloo.www.application_kotlin.model.lists_models.ReverseGeocodeItem
import `in`.woloo.www.application_kotlin.model.server_request.ReverseGeocodeRequest
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.utilities.ProgressBarAttr


open class BaseViewModel: ViewModel() {

    private val baseAPIRepository: BaseRepository = BaseRepository()
    private val mProgressDialogLd = MutableLiveData<ProgressBarAttr>()
    private val mNetworkErrorLd = MutableLiveData<ApiResponseData<*>>()
    private val mAppConfig = MutableLiveData<AuthConfigResponse.Data?>()
    private val reverseGeocodeResponse: EventLiveData<BaseResponse<ArrayList<ReverseGeocodeItem>>> = EventLiveData()


    fun observeProgressDialogLiveData(): MutableLiveData<ProgressBarAttr> {
        return mProgressDialogLd
    }

    fun observeNetworkDetectorLiveData(): MutableLiveData<ApiResponseData<*>> {
        return mNetworkErrorLd
    }

    fun observeAppConfig(): MutableLiveData<AuthConfigResponse.Data?>{
        return mAppConfig
    }

    fun getAppConfig(request: LocaleRequest){
        baseAPIRepository.appConfig(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<AuthConfigResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<AuthConfigResponse.Data>>) {
                if(data.status == ApiResponseData.API_SUCCESS && data.data != null) {
                    mAppConfig.value = data.data!!.data!!
                } else {
                    mAppConfig.value = null
                }
            }
        })
    }

    fun reverseGeocoding(lat : Double, lng : Double) {
        updateProgress(true)
        val request = ReverseGeocodeRequest()
        request.lat = lat
        request.lng = lng
        baseAPIRepository.reverseGeocoding(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<ReverseGeocodeItem>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<ReverseGeocodeItem>>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    reverseGeocodeResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    reverseGeocodeResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeReverseGeocoding(): EventLiveData<BaseResponse<ArrayList<ReverseGeocodeItem>>> {
        return reverseGeocodeResponse
    }

    fun updateProgress(show: Boolean) {
        val progress = ProgressBarAttr()
        progress.isShow = show
        mProgressDialogLd.value = progress
    }

    protected fun notifyNetworkError(error: ApiResponseData<*>) {
        mNetworkErrorLd.value = error
    }
}