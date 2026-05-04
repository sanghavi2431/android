package `in`.woloo.www.application_kotlin.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    // LiveData to trigger the refresh event
    private val _refreshEvent = MutableLiveData<Boolean>()
    val refreshEvent: LiveData<Boolean> get() = _refreshEvent

    private val _distanceRange = MutableLiveData<Int>()
    val distanceRange: LiveData<Int> get() = _distanceRange

    private val _showMoreEvent = MutableLiveData<Boolean>()
    val showMoreEvent: LiveData<Boolean> get() = _showMoreEvent

    // Trigger the refresh event
    fun triggerRefresh() {
        _refreshEvent.value = true
    }

    // Optionally reset the event after handling it (to avoid multiple updates)
    fun resetRefreshEvent() {
        _refreshEvent.value = false
    }

    fun setDistanceRange(distance: Int) {
        _distanceRange.value = distance
    }

    fun onShowMoreClicked() {
        _showMoreEvent.value = true
    }

    fun clearShowMoreEvent() {
        _showMoreEvent.value = false
    }

}