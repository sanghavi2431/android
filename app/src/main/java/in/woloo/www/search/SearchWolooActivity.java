package in.woloo.www.search;

import static in.woloo.www.utils.AppConstants.NEARBY_WOLOO_OFFERS;

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
import in.woloo.www.scan_qr_code.QRcodeScannerActivity;
import in.woloo.www.search.fragments.WolooSearchFragment;
import in.woloo.www.utils.Logger;

public class SearchWolooActivity extends AppCompatActivity {

    public static final String ARG_SHOW_OFFERS = "ARG_SHOW_OFFERS";
    @BindView(R.id.frmFragment)
    FrameLayout frmFragment;
    public static String TAG = SearchWolooActivity.class.getSimpleName();

    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_woloo);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate");
    }

    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            if (getIntent() != null && getIntent().getBooleanExtra(ARG_SHOW_OFFERS, false)) {
                loadFragment(WolooSearchFragment.newInstance(true));
            } else {
                loadFragment(WolooSearchFragment.newInstance(false));
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on loadFragment*/
    public void loadFragment(Fragment fragment) {
        try {
            Logger.i(TAG, "loadFragment");
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frmFragment, fragment);
            fragmentTransaction.commit();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }
}