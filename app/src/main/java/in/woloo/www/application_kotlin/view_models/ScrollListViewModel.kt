package `in`.woloo.www.application_kotlin.view_models


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ScrollListViewModel  : ViewModel() {

    private val _scrollToIndex = MutableLiveData<Int>()
    val scrollToIndex: LiveData<Int> = _scrollToIndex
    private val _scrollToIndexForMarker = MutableLiveData<Int>()
    val scrollToIndexForMarker: LiveData<Int> = _scrollToIndexForMarker
    private val _isProgrammaticScroll = MutableLiveData<Boolean>(false)
    val isProgrammaticScroll: LiveData<Boolean> get() = _isProgrammaticScroll

    fun setScrollIndex(index: Int) {
        _scrollToIndex.value = index
    }

    fun setScrollIndexForMarker(index: Int) {
        _scrollToIndexForMarker.value = index
    }

    fun setProgrammaticScroll(isProgrammatic: Boolean) {
        _isProgrammaticScroll.value = isProgrammatic
    }


    private val _resetToFirst = MutableLiveData<Boolean>()
    val resetToFirst: LiveData<Boolean> = _resetToFirst

    fun requestScrollToFirst() {
        _resetToFirst.value = true
    }

    fun consumeScrollRequest() {
        _resetToFirst.value = false
    }
}
