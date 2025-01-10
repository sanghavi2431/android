package `in`.woloo.www.dashboard.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel() {
    private val mText =
        MutableLiveData<String>()

    init {
        mText.value = "This is gallery fragment"
    }

    val text: LiveData<String>
        get() = mText
}