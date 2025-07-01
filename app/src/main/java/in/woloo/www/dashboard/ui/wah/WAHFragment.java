package in.woloo.www.dashboard.ui.wah;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.slideshow.SlideshowFragment;
import in.woloo.www.utils.Logger;


public class WAHFragment extends Fragment {

    @BindView(R.id.tvWAHTitle)
    TextView tvWAHTitle;
    @BindView(R.id.tvWAHMessage)
    TextView tvWAHMessage;
    public static String TAG= WAHFragment.class.getSimpleName();
    private WAHViewModel wahViewModel;
    /*calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        wahViewModel =
                new ViewModelProvider(this).get(WAHViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wah, container, false);
        ButterKnife.bind(this,root);
        initView();
        wahViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
    /*calling on initView*/
    private void initView() {
        try{
            Logger.i(TAG, "initView");
            SpannableStringBuilder wah_title = new SpannableStringBuilder(getResources().getString(R.string.wah_title));
            wah_title.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wah_title.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wah_title.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 11,12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wah_title.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 21,22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvWAHTitle.setText(wah_title);

            SpannableStringBuilder wah_msg = new SpannableStringBuilder(getResources().getString(R.string.wah_msg));
            wah_msg.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 15, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wah_msg.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 70, 80, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvWAHMessage.setText(wah_msg);

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}