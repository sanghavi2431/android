package `in`.woloo.www.store

import androidx.lifecycle.MutableLiveData

data class ApiErrorEvent(val code: Int, val message: String, val url: String)


object ApiEventBus {
    val apiErrorLiveData = MutableLiveData<ApiErrorEvent>()

    fun postApiError(code: Int, message: String, url: String) {
        apiErrorLiveData.postValue(ApiErrorEvent(code, message, url))
    }
}

