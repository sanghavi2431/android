package `in`.woloo.www.application_kotlin.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class DialpogObserver: ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun requestDialog() {
        _showDialog.value = true
    }

    fun resetDialogRequest() {
        _showDialog.value = false
    }
}