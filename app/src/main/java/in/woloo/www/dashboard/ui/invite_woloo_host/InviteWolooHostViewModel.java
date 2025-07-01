package in.woloo.www.dashboard.ui.invite_woloo_host;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InviteWolooHostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InviteWolooHostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}