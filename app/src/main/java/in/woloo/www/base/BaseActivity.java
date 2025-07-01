package in.woloo.www.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.ViewPumpAppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.jetsynthesys.encryptor.JetEncryptor;

import butterknife.ButterKnife;
import dev.b3nedikt.restring.Restring;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkUtils;
import in.woloo.www.networksUtils.VolleySingleton;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ToastUtils;
import kotlin.jvm.functions.Function1;


public class BaseActivity extends AppCompatActivity {
    public CommonUtils commonUtils = new CommonUtils();
    public NetworkAPICall mNetworkAPICall = new NetworkAPICall();
    public SharedPreference mSharedPreference;
    protected AppConstants mApplicationConstant;
    //protected JetEncryptor mJetEncryptor;
    protected NetworkUtils networkUtils;
    protected Gson gson;
    protected RequestQueue queue;
    public String TAG = BaseActivity.class.getSimpleName();
    private AppCompatDelegate appCompatDelegate = null;

    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_sign_up);
        Logger.i(TAG, "onCreate");
        initializationBase();
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (appCompatDelegate == null) {
            appCompatDelegate = new ViewPumpAppCompatDelegate(
                    super.getDelegate(),
                    this,
                    Restring::wrapContext
            );
        }
        return appCompatDelegate;
    }

    /*calling on initializationBase*/
    private void initializationBase() {
        Logger.i(TAG, "initializationBase");
        if (commonUtils == null) {
            commonUtils = new CommonUtils();
        }

        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(BaseActivity.this);
        }

        if (mApplicationConstant == null) {
            mApplicationConstant = new AppConstants();
        }

        /*if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance();
        }*/
        if (gson == null) {
            gson = commonUtils.getGson();
        }

        if (queue == null) {
            queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        }

        if (networkUtils == null) {
            networkUtils = new NetworkUtils();
        }
    }

    /*calling on displayToast*/
    protected void displayToast(String mTitle) {
        Logger.i(TAG, "displayToast");
        ToastUtils.show(getApplicationContext(), mTitle, false);
    }

    /*@Override
    protected void onDestroy() {
        if (mJetEncryptor != null) {
            mJetEncryptor.cancelInitJob();
        }
        super.onDestroy();
    }*/
}