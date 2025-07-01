package in.woloo.www.woloo_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;
import in.woloo.www.webview.WebViewActivity;
import in.woloo.www.woloo_info.fragments.GoBindasFragment;
import in.woloo.www.woloo_info.fragments.StartFreeTrialFragment;
import in.woloo.www.woloo_info.fragments.WolooInfoFragment;

public class WolooInfoActivity extends AppCompatActivity {

    @BindView(R.id.frmView)
    FrameLayout frameLayout;
    public static String TAG= WolooInfoActivity.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woloo_info);
        ButterKnife.bind(this);
        Logger.i(TAG, "onCreate");
        initView();
    }
    /*calling on initView*/
    private void initView() {
        try{
            Logger.i(TAG, "initView");
              loadFragment(StartFreeTrialFragment.newInstance("",""));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on loadFragment*/
    public void loadFragment(Fragment fragment){
        try{
            Logger.i(TAG, "loadFragment");
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frmView,fragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}