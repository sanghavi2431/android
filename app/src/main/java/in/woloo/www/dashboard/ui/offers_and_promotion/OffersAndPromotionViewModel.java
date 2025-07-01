package in.woloo.www.dashboard.ui.offers_and_promotion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OffersAndPromotionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OffersAndPromotionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}