package in.woloo.www.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.review.fragments.AddReviewsFragment;
import in.woloo.www.utils.AppConstants;


public class AddReviewActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    public boolean hasReachedAtDestination = false;
    private int wolooId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);
        handleDeepLink(getIntent());
        initViews();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleDeepLink(intent);
        Log.d("DeepLinkTest", "onNewIntent triggered");

    }

    private void initViews() {
        try {
            hasReachedAtDestination = false;
            boolean isDeepLinkProcessed = handleDeepLink(getIntent());
            if (!isDeepLinkProcessed) {
                wolooId = getIntent().getIntExtra(AppConstants.WOLOO_ID, 0);
            }
            loadFragment(new AddReviewsFragment().newInstance(wolooId, ""));
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }


    public void loadFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private boolean handleDeepLink(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri deepLink = intent.getData();
            if (("https".equals(deepLink.getScheme()) || "http".equals(deepLink.getScheme())) && "app.woloo.in".equals(deepLink.getHost()) || "/woloo_feedback".equals(deepLink.getPath())) {
                String wolooIdString = deepLink.getQueryParameter("wolooId");
                if (wolooIdString != null) {
                    try {
                        wolooId = Integer.parseInt(wolooIdString);
                        loadFragment(new AddReviewsFragment().newInstance(wolooId, ""));
                        // Successfully processed the deep link
                        return true;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}