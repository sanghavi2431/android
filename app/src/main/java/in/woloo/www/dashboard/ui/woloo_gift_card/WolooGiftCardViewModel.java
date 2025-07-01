package in.woloo.www.dashboard.ui.woloo_gift_card;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WolooGiftCardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WolooGiftCardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}