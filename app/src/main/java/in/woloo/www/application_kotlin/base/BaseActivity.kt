package `in`.woloo.www.application_kotlin.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import com.android.volley.RequestQueue
import com.google.gson.Gson
import dev.b3nedikt.restring.Restring.wrapContext
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.application_kotlin.api_classes.NetworkUtils
import `in`.woloo.www.networksUtils.VolleySingleton
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ToastUtils


open class BaseActivity : AppCompatActivity() {
    @JvmField
    var commonUtils: CommonUtils? = CommonUtils()
    var mNetworkAPICall: NetworkAPICall = NetworkAPICall()
    var mSharedPreference: SharedPreference? = null
    protected var mApplicationConstant: AppConstants? = null

    //protected JetEncryptor mJetEncryptor;
    protected var networkUtils: NetworkUtils? = null
    protected var gson: Gson? = null
    protected var queue: RequestQueue? = null
    var TAG = BaseActivity::class.java.simpleName
    private var appCompatDelegate: AppCompatDelegate? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.layout_sign_up);
        Logger.i(TAG, "onCreate")
        initializationBase()
    }

    @NonNull
    override fun getDelegate(): AppCompatDelegate {
        if (appCompatDelegate == null) {
            appCompatDelegate = ViewPumpAppCompatDelegate(
                super.getDelegate(),
                this
            ) { base: Context -> wrapContext(base) }
        }
        return appCompatDelegate as AppCompatDelegate
    }

    /*calling on initializationBase*/
    private fun initializationBase() {
        Logger.i(TAG, "initializationBase")
        if (commonUtils == null) {
            commonUtils = CommonUtils()
        }
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(this@BaseActivity)
        }
        if (mApplicationConstant == null) {
            mApplicationConstant = AppConstants
        }

        /*if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance();
        }*/if (gson == null) {
            gson = CommonUtils.gson
        }
        if (queue == null) {
            queue = VolleySingleton.getInstance(applicationContext).requestQueue
        }
        if (networkUtils == null) {
            networkUtils = NetworkUtils()
        }
    }

    /*calling on displayToast*/
    protected fun displayToast(mTitle: String?) {
        Logger.i(TAG, "displayToast")
        ToastUtils.show(applicationContext, mTitle, false)
    }
}