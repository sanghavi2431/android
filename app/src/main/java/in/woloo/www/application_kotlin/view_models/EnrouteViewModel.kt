package `in`.woloo.www.application_kotlin.view_models

import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_request.EnrouteRequest
import `in`.woloo.www.application_kotlin.repositories.EnrouteRepository
import org.json.JSONObject

class EnrouteViewModel : BaseViewModel() {
    private val enrouteRepository: EnrouteRepository = EnrouteRepository()
    private val mGetNearbyWoloos: EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> = EventLiveData()
    private val mWolooNavigationReward: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()


    fun getEnrouteWoloo(request: EnrouteRequest) {
        updateProgress(true)
        enrouteRepository.getEnrouteWoloo(request, object :
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
        })
    }


    fun getWolooNavigationReward(wolooId : Int) {
        updateProgress(true)
        enrouteRepository.getWolooNavigationReward(wolooId, object :
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
        })
    }

    fun observeNearByWoloo(): EventLiveData<BaseResponse<ArrayList<NearByStoreResponse.Data>>> {
        return mGetNearbyWoloos
    }

    fun observeWolooNavigationReward(): EventLiveData<BaseResponse<JSONObject>> {
        return mWolooNavigationReward
    }
}