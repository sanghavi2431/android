package in.woloo.www.dashboard.ui.woloo_gift_card;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.dashboard.ui.wah.WAHFragment;
import in.woloo.www.utils.Logger;


public class WolooGiftCardFragment extends Fragment {

    private WolooGiftCardViewModel wolooGiftCardViewModel;
    public static String TAG= WolooGiftCardFragment.class.getSimpleName();
    @BindView(R.id.etMobile)
    EditText etMobile;

    /*calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        wolooGiftCardViewModel =
                new ViewModelProvider(this).get(WolooGiftCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_woloo_gift_card, container, false);
        wolooGiftCardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });




        return root;
    }
}