package in.woloo.www.dashboard.ui.invite_friend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InviteFriendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InviteFriendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}