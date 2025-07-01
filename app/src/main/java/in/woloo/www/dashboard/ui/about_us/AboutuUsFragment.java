package in.woloo.www.dashboard.ui.about_us;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
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
import in.woloo.www.dashboard.ui.gallery.GalleryViewModel;
import in.woloo.www.invite_friend.fragments.InviteFriendFragment;
import in.woloo.www.utils.Logger;


public class AboutuUsFragment extends Fragment {

    @BindView(R.id.tvWolooInfo)
    TextView tvWolooInfo;

    @BindView(R.id.tvWolooPowderRoomInfo)
    TextView tvWolooPowderRoomInfo;

    @BindView(R.id.tvWolooAppInfo)
    TextView tvWolooAppInfo;


    private AboutUsViewModel aboutUsViewModel;
    public static String TAG= AboutuUsFragment.class.getSimpleName();
    /*calling  onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        aboutUsViewModel =
                new ViewModelProvider(this).get(AboutUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this,root);
        aboutUsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        initView();
        return root;
    }
    /*calling  initView*/
    private void initView() {
        try{
            Logger.i(TAG, "initView");
            SpannableStringBuilder about_woloo_info = new SpannableStringBuilder(getResources().getString(R.string.about_woloo_info));
            about_woloo_info.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvWolooInfo.setText(about_woloo_info);

            SpannableStringBuilder about_woloo_powder_room_info = new SpannableStringBuilder(getResources().getString(R.string.about_woloo_womans_powder_room));
            about_woloo_powder_room_info.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvWolooPowderRoomInfo.setText(about_woloo_powder_room_info);

            SpannableStringBuilder about_woloo_app_info = new SpannableStringBuilder(getResources().getString(R.string.about_woloo_app_info));
            about_woloo_app_info.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvWolooAppInfo.setText(about_woloo_app_info);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}