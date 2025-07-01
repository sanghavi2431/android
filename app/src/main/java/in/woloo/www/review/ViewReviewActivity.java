package in.woloo.www.review;

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
import in.woloo.www.review.fragments.ViewReviewFragment;
import in.woloo.www.utils.AppConstants;

public class ViewReviewActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        try{
            String review = getIntent().getStringExtra(AppConstants.REVIEW);
            loadFragment(ViewReviewFragment.newInstance(review,""));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public void loadFragment(Fragment fragment){
        try{
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