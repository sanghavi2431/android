package `in`.woloo.www.v2.woloo.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateResponse
import `in`.woloo.www.application_kotlin.repositories.WolooRepository
import okhttp3.RequestBody

class WolooViewModel : BaseViewModel() {
    private val wolooRepository: WolooRepository = WolooRepository()
    private val recommendWolooResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()
    private val referredWolooListResponse: EventLiveData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>> = EventLiveData()
    private val addWolooResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()
    private val scanWolooResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()
    private val wahCertificateResponse: EventLiveData<BaseResponse<WahCertificateResponse>> = EventLiveData()

    fun recommendWoloo(body: RequestBody) {
        updateProgress(true)
        wolooRepository.recommendWoloo(body, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MessageResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    recommendWolooResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    recommendWolooResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeRecommendWoloo(): EventLiveData<BaseResponse<MessageResponse>> {
        return recommendWolooResponse
    }

    fun getRecommendWolooList() {
        updateProgress(true)
        wolooRepository.getRecommendWolooList( object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    referredWolooListResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    referredWolooListResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeRecommendWolooList(): EventLiveData<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>> {
        return referredWolooListResponse
    }

    fun addWoloo(body: RequestBody) {
        updateProgress(true)
        wolooRepository.addWoloo(body, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MessageResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    addWolooResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    addWolooResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeAddWoloo(): EventLiveData<BaseResponse<MessageResponse>> {
        return addWolooResponse
    }

    fun scanQRCode(name: String) {
        updateProgress(true)
        wolooRepository.scanWoloo(name, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MessageResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    scanWolooResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    scanWolooResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeScanQRCode(): EventLiveData<BaseResponse<MessageResponse>> {
        return scanWolooResponse
    }

    fun wahCertificate(name: String) {
        updateProgress(true)
        wolooRepository.wahCertificate(name, object :
            WebserviceCallback<ApiResponseData<BaseResponse<WahCertificateResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<WahCertificateResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    wahCertificateResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    wahCertificateResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observewWahCertificate(): EventLiveData<BaseResponse<WahCertificateResponse>> {
        return wahCertificateResponse
    }
}