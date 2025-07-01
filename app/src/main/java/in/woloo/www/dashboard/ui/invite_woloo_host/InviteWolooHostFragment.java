package in.woloo.www.dashboard.ui.invite_woloo_host;

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
import in.woloo.www.dashboard.ui.invite_friend.InviteFriendFragment;
import in.woloo.www.utils.Logger;


public class InviteWolooHostFragment extends Fragment {

    private InviteWolooHostViewModel inviteWolooHostViewModel;
    private static final String TAG = InviteWolooHostFragment.class.getSimpleName();
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        inviteWolooHostViewModel =
                new ViewModelProvider(this).get(InviteWolooHostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_invite_woloo_host, container, false);
        inviteWolooHostViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
}