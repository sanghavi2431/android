package in.woloo.www.subscribe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.utils.Logger;

public class SubscribeActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    public static String TAG= SubscribeActivity.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "loadFragment");
    }
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
            loadFragment(new SubscribeFragment().newInstance("","",false,"", true, false, ""));
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
            fragmentTransaction.replace(R.id.flFragments,fragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}