package `in`.woloo.www.application_kotlin.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.google.gson.Gson
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.application_kotlin.api_classes.NetworkUtils
import `in`.woloo.www.networksUtils.VolleySingleton
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

open class BaseFragment : Fragment() {
    @JvmField
    var commonUtils: CommonUtils? = CommonUtils()
    private var mApplicationConstant: AppConstants? = null
    protected var mJetEncryptor: JetEncryptor? = null
    private var networkUtils: NetworkUtils? = null
    protected var gson: Gson? = null
    private var queue: RequestQueue? = null
    var mNetworkAPICall: NetworkAPICall = NetworkAPICall()
    var TAG = BaseFragment::class.java.simpleName

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializationBase()
        Logger.i(TAG, "onCreate")
    }

    /*calling on initializationBase*/
    private fun initializationBase() {
        Logger.i(TAG, "initializationBase")
        if (commonUtils == null) {
            commonUtils = CommonUtils()
        }
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(activity)
        }
        if (mApplicationConstant == null) {
            mApplicationConstant = AppConstants
        }
        if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance()
        }
        if (gson == null) {
            gson = CommonUtils.gson
        }
        if (queue == null) {
            queue = VolleySingleton.getInstance(requireActivity()).requestQueue
        }
        if (networkUtils == null) {
            networkUtils = NetworkUtils()
        }
    }

    companion object {
        protected var mSharedPreference: SharedPreference? = null
    }
}
