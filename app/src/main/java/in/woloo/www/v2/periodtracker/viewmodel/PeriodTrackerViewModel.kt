package `in`.woloo.www.v2.periodtracker.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.v2.periodtracker.repository.PeriodTrackerRepository

class PeriodTrackerViewModel : BaseViewModel() {
    private val mPeriodTrackerRepository: PeriodTrackerRepository = PeriodTrackerRepository()
    private val mPeriodTracker: EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> = EventLiveData()
    private val mGetPeriodTracker: EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> = EventLiveData()

    fun sendPeriodTrackerData(
        request: PeriodTrackerRequest
    ) {
        updateProgress(true)
        mPeriodTrackerRepository.periodTracker(
            request,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mPeriodTracker.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mPeriodTracker.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun getPeriodTracker() {
        updateProgress(true)
        mPeriodTrackerRepository.getPeriodTracker(object :
                WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mGetPeriodTracker.value = data.data
                    } else {
                        WolooApplication.setErrorMessage(data.message)
                        mGetPeriodTracker.value = data.data
                        notifyNetworkError(data)
                    }
                }
            })
    }

    fun observePeriodTracker(): EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> {
        return mPeriodTracker
    }

    fun observeGetPeriodTracker(): EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> {
        return mGetPeriodTracker
    }
}
