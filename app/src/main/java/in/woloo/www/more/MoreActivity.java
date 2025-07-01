package in.woloo.www.more;

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
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.utils.Logger;

public class MoreActivity extends AppCompatActivity {
    public static String TAG= MoreActivity.class.getSimpleName();
    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    /*calling onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate");
    }
    /*calling initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
             loadFragment(new MoreFragment().newInstance("",""));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }


    }
    /*calling loadFragment*/
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