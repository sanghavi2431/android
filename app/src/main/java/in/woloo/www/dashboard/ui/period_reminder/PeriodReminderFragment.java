package in.woloo.www.dashboard.ui.period_reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import in.woloo.www.R;
import in.woloo.www.dashboard.ui.offers_and_promotion.OffersAndPromotionFragment;
import in.woloo.www.utils.Logger;


public class PeriodReminderFragment extends Fragment {
    private static final String TAG = PeriodReminderFragment.class.getSimpleName();
    private PeriodReminderViewModel periodReminderViewModel;
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        periodReminderViewModel = new ViewModelProvider(this).get(PeriodReminderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_period_reminder, container, false);
        periodReminderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
}