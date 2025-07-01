package in.woloo.www.dashboard.ui.buy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
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
import in.woloo.www.dashboard.ui.become_woloo_host.BecomeWolooHostViewModel;
import in.woloo.www.dashboard.ui.gallery.GalleryViewModel;
import in.woloo.www.utils.Logger;


public class BuyFragment extends Fragment {

    private BuyViewModel buyViewModel;

    @BindView(R.id.ivClassic)
    ImageView ivClassic;
    @BindView(R.id.ivSilver)
    ImageView ivSilver;
    @BindView(R.id.ivGold)
    ImageView ivGold;
    @BindView(R.id.ivElite)
    ImageView ivElite;
    public static String TAG= BuyFragment.class.getSimpleName();
    /*calling  onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        buyViewModel =
                new ViewModelProvider(this).get(BuyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_buy, container, false);
        ButterKnife.bind(this,root);
        initView();
        buyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
    /*calling  initView*/
    private void initView() {
        try{
            Logger.i(TAG, "initView");
            ivClassic.setOnClickListener(v -> {
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg));
            });
            ivSilver.setOnClickListener(v -> {
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg));
            });
            ivGold.setOnClickListener(v -> {
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg));
            });
            ivElite.setOnClickListener(v -> {
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg));
            });
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling  showSubscriptionDialog*/
    private void showSubscriptionDialog(String msg){
        try{
            Logger.i(TAG, "showSubscriptionDialog");
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.subcription_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}
