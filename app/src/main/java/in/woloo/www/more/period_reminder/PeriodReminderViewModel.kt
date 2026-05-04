package `in`.woloo.www.more.period_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PeriodReminderViewModel : ViewModel() {
    private val mText =
        MutableLiveData<String>()

    init {
        mText.value = "This is gallery fragment"
    }

    val text: LiveData<String>
        get() = mText
}