package in.woloo.www.dashboard.ui.my_account;

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
import in.woloo.www.dashboard.ui.invite_woloo_host.InviteWolooHostFragment;
import in.woloo.www.utils.Logger;


public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;
    private static final String TAG = MyAccountFragment.class.getSimpleName();
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        myAccountViewModel =
                new ViewModelProvider(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        myAccountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
}