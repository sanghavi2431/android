package in.woloo.www.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.woloo.www.base.BaseFragment;
import in.woloo.www.utils.Logger;


public class JetEngageApplication extends Application {

    private static JetEngageApplication mInstance;
    private Context mContext;

    public static synchronized JetEngageApplication getInstance() {
        return mInstance;
    }
    public  String TAG= JetEngageApplication.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        Logger.i(TAG, "onCreate");

    }


}
