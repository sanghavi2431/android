package in.woloo.www.dashboard.ui.period_reminder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PeriodReminderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PeriodReminderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}