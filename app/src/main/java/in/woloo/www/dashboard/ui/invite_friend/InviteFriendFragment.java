package in.woloo.www.dashboard.ui.invite_friend;

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
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.utils.Logger;


public class InviteFriendFragment extends Fragment {
    private static final String TAG = InviteFriendFragment.class.getSimpleName();
    private InviteFriendViewModel inviteFriendViewModel;
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        inviteFriendViewModel =
                new ViewModelProvider(this).get(InviteFriendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        inviteFriendViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
}