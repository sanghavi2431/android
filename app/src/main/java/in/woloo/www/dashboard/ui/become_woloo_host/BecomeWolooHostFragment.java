package in.woloo.www.dashboard.ui.become_woloo_host;

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
import in.woloo.www.dashboard.ui.about_us.AboutuUsFragment;
import in.woloo.www.dashboard.ui.buy.BuyViewModel;
import in.woloo.www.utils.Logger;


public class BecomeWolooHostFragment extends Fragment {

    private BecomeWolooHostViewModel buyViewModel;
    public static String TAG= BecomeWolooHostViewModel.class.getSimpleName();
    /*calling  onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        buyViewModel =
                new ViewModelProvider(this).get(BecomeWolooHostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_becom_woloo_host, container, false);
        buyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
}