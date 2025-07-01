package in.woloo.www.dashboard.ui.become_woloo_host;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BecomeWolooHostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BecomeWolooHostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}