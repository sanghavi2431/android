package in.woloo.www.home_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.home.fragments.HomeCategoryFragment;
import in.woloo.www.home_details.fragments.HomeDetailsFragment;
import in.woloo.www.search.fragments.WolooSearchFragment;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class HomeDetailsActivity extends AppCompatActivity {

    private boolean fromSearch;
    public static String TAG= HomeDetailsActivity.class.getSimpleName();
    /*calling onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate");
    }
    /*calling initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
            fromSearch = getIntent().getBooleanExtra(AppConstants.FROM_SEARCH,false);
            loadFragment(new HomeDetailsFragment().newInstance(fromSearch));
        }catch(Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling loadFragment*/
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