package `in`.woloo.www.more.period_tracker.periodtracker.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.more.period_tracker.periodtracker.repository.PeriodTrackerRepository


class PeriodTrackerViewModel : BaseViewModel(){
    private val mPeriodTrackerRepository: PeriodTrackerRepository = PeriodTrackerRepository()
    private val mPeriodTracker: EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> = EventLiveData()
    private val mGetPeriodTracker: EventLiveData<BaseResponse<PeriodTrackerResponse.Data>> = EventLiveData()

    fun sendPeriodTrackerData(
        request: PeriodTrackerRequest
    ) {
        updateProgress(true)
        mPeriodTrackerRepository.periodTracker(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mPeriodTracker.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mPeriodTracker.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun getPeriodTracker() {
        updateProgress(true)
        mPeriodTrackerRepository.getPeriodTracker( object :
            WebserviceCallback<ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<PeriodTrackerResponse.Data>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetPeriodTracker.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
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