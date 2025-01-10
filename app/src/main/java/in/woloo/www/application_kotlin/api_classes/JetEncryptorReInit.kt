package `in`.woloo.www.application_kotlin.api_classes

import android.app.Activity
import com.jetsynthesys.encryptor.JetEncryptor
import com.jetsynthesys.encryptor.JobListener
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.networksUtils.VolleySingleton
import `in`.woloo.www.utils.Logger

class JetEncryptorReInit(private var activity: Activity) {
    private val mUtility = CommonUtils()
    private var mJetEncryptor: JetEncryptor? = null
    private var networkAPICallModel: NetworkAPICallModel? = null
    private var networkAPIResponseCallback: NetworkAPIResponseCallback? = null
    private var networkAPICall: NetworkAPICall? = null
    fun callApplicationWS(
        activity: Activity,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?,
        networkAPICall: NetworkAPICall?
    ) {
        this.activity = activity
        this.networkAPICallModel = networkAPICallModel
        this.networkAPIResponseCallback = networkAPIResponseCallback
        this.networkAPICall = networkAPICall
        if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance()
        }
        initJetEncryptor()
    }

    private fun initJetEncryptor() {
        try {
            val device_id = mUtility.getDeviceId(activity)
            if (BuildConfig.LIVE_URL.equals("3", ignoreCase = true)) {
                mJetEncryptor!!.initInBackground(
                    activity.applicationContext,
                    jobListener,
                    true,
                    BuildConfig.BASE_URL_ENC,
                    BuildConfig.PORT_ENC,
                    BuildConfig.END_POINT_ENC,
                    BuildConfig.APPLICATION_ID,
                    device_id
                )
            } else if (BuildConfig.LIVE_URL.equals("4", ignoreCase = true)) {
                mJetEncryptor!!.initInBackground(
                    activity.applicationContext,
                    jobListener,
                    true,
                    BuildConfig.BASE_URL_ENC,
                    BuildConfig.PORT_ENC,
                    BuildConfig.END_POINT_ENC,
                    BuildConfig.APPLICATION_ID + "_pp",
                    device_id
                )
            } else {
                mJetEncryptor!!.initInBackground(
                    activity.applicationContext,
                    jobListener,
                    false,
                    BuildConfig.BASE_URL_ENC,
                    BuildConfig.PORT_ENC,
                    BuildConfig.END_POINT_ENC,
                    BuildConfig.APPLICATION_ID,
                    device_id
                )
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            if (networkAPICall != null) networkAPICall!!.callApplicationWS(
                activity,
                networkAPICallModel!!,
                networkAPIResponseCallback
            )
        }
    }

    private val jobListener: JobListener = object : JobListener {
        override fun workStarted(i: Int) {
            Logger.i(TAG, "workStarted")
        }

        override fun workFinished(i: Int) {
            Logger.i(TAG, "workFinished")
            try {
                VolleySingleton.getInstance(activity.applicationContext)
                    .setAfterJetEncryptorInitilization(
                        activity.applicationContext
                    )
                if (networkAPICall != null) networkAPICall!!.callApplicationWS(
                    activity,
                    networkAPICallModel!!,
                    networkAPIResponseCallback
                )
                mJetEncryptor!!.cancelInitJob()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }

        override fun workResult(s: String) {
            Logger.i(TAG, "workResult")
        }

        override fun onworkError(s: String) {
            Logger.i(TAG, "onworkError")
            if (networkAPICall != null) networkAPICall!!.callApplicationWS(
                activity,
                networkAPICallModel!!,
                networkAPIResponseCallback
            )
            mJetEncryptor!!.cancelInitJob()
        }
    }

    companion object {
        private const val TAG = "JetEncryptor"
    }
}
