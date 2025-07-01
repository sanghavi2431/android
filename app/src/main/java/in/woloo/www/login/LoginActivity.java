package in.woloo.www.login;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.login.receiver.OTP_Receiver;
import in.woloo.www.utils.Logger;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    private static final String TAG = "LoginActivity";

    /*calling onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);
        requestsmspermission();
        initView();

    }

    /*sms permission*/
    private void requestsmspermission() {
        Logger.i(TAG, "requestsmspermission");
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, smspermission);
        //check if read SMS permission is granted or not
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = smspermission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    /*enabling sms receiver*/
    public void enableSMSReceiver(Context context) {
        Logger.i(TAG, "enableSMSReceiver");
        ComponentName component = new ComponentName(context, OTP_Receiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                component,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /*disabling sms receiver*/
    public static void disableSMSReceiver(Context context) {
        Logger.i(TAG, "disableSMSReceiver");
        ComponentName component = new ComponentName(context, OTP_Receiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                component,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    /*init view*/
    private void initView() {
        Logger.i(TAG, "initView");
        try {
            loadFragment(new LoginFragment().newInstance("", ""));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*loadFragment*/
    public void loadFragment(Fragment fragment) {
        Logger.i(TAG, "loadFragment");
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

    /*on  resume*/
    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }

    /*on  onPause*/
    @Override
    protected void onPause() {
        super.onPause();
        Logger.i(TAG, "onPause");
    }

}