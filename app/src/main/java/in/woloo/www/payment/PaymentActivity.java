package in.woloo.www.payment;

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
import in.woloo.www.giftcard.GiftCardFragment;
import in.woloo.www.home.adapter.HomeCategoryAdapter;
import in.woloo.www.home.fragments.HomeCategoryFragment;
import in.woloo.www.home.fragments.WolooStoreInfoFragment;
import in.woloo.www.notification.NotificationFragment;
import in.woloo.www.payment.fragments.PaymentFragment;
import in.woloo.www.utils.Logger;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    public static String TAG= PaymentActivity.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate");
    }
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
           loadFragment(new PaymentFragment().newInstance("",""));//PaymentFragment()  HomeCategoryFragment
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