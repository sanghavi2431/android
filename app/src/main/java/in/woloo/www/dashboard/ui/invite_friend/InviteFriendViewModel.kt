package `in`.woloo.www.dashboard.ui.invite_friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InviteFriendViewModel : ViewModel() {
    private val mText =
        MutableLiveData<String>()

    init {
        mText.value = "This is gallery fragment"
    }

    val text: LiveData<String>
        get() = mText
}