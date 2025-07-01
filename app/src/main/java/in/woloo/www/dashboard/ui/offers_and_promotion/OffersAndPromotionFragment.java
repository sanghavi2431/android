package in.woloo.www.dashboard.ui.offers_and_promotion;

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
import in.woloo.www.dashboard.ui.notifications.NotificationsFragment;
import in.woloo.www.utils.Logger;


public class OffersAndPromotionFragment extends Fragment {

    private OffersAndPromotionViewModel offersAndPromotionViewModel;
    private static final String TAG = OffersAndPromotionFragment.class.getSimpleName();
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        offersAndPromotionViewModel =
                new ViewModelProvider(this).get(OffersAndPromotionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_offers_and_promotion, container, false);
        offersAndPromotionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
}