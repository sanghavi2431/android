package `in`.woloo.www.v2.enroute.viewmodel

import `in`.woloo.www.dashboard.ui.home.model.NearByStoreResponse
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.data.remote.ApiResponseData
import `in`.woloo.www.v2.data.remote.BaseResponse
import `in`.woloo.www.v2.data.remote.EventLiveData
import `in`.woloo.www.v2.data.remote.WebserviceCallback
import `in`.woloo.www.v2.enroute.model.EnrouteRequest
import `in`.woloo.www.v2.enroute.repository.EnrouteRepository
import org.json.JSONObject

class EnrouteViewModel : BaseViewModel() {
    private val enrouteRepository: EnrouteRepository = EnrouteRepository()
    private val mGetNearbyWoloos: EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> = EventLiveData()
    private val mWolooNavigationReward: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()

    fun getEnrouteWoloo(request: EnrouteRequest) {
        updateProgress(true)
        enrouteRepository.getEnrouteWoloo(
            request,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<NearByStoreResponse.Data>>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mGetNearbyWoloos.value = data.data
                    } else {
                        mGetNearbyWoloos.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun getWolooNavigationReward(wolooId: Int) {
        updateProgress(true)
        enrouteRepository.getWolooNavigationReward(
            wolooId,
            object :
                WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
                override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                    updateProgress(false)
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mWolooNavigationReward.value = data.data
                    } else {
                        mWolooNavigationReward.value = data.data
                        notifyNetworkError(data)
                    }
                }
            }
        )
    }

    fun observeNearByWoloo(): EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> {
        return mGetNearbyWoloos
    }

    fun observeWolooNavigationReward(): EventLiveData<BaseResponse<JSONObject>> {
        return mWolooNavigationReward
    }
}
