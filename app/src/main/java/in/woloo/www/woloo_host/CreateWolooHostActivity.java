package in.woloo.www.woloo_host;

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
import in.woloo.www.utils.Logger;

public class CreateWolooHostActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    public static String TAG= CreateWolooHostActivity.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_woloo_host);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate");
    }
    /*calling on initViews*/
    private void initViews() {
        try{
            Logger.i(TAG, "initViews");
            loadFragment(CreateWolooHostFragment.newInstance("",""));
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
            fragmentTransaction.replace(R.id.flFragments,fragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}