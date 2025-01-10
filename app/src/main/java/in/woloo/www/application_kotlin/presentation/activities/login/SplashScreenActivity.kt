package `in`.woloo.www.application_kotlin.presentation.activities.login


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.netcore.android.Smartech
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchWolooActivity
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.deeplink.DeeplinkDispatcherActivity
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import `in`.woloo.www.databinding.ActivitySplashBinding
import io.hansel.hanselsdk.Hansel
import io.hansel.ujmtracker.HanselInternalEventsListener
import io.hansel.ujmtracker.HanselTracker
import java.lang.ref.WeakReference

class SplashScreenActivity :
    BaseActivity()
{
    lateinit var homeViewModel: HomeViewModel
    private var mCommonUtils: CommonUtils? = null
    private var referralcode = ""
    private var notNow = false
    private var isProcessing = false
    var binding: ActivitySplashBinding? = null

    @RequiresApi(api = Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        Logger.i(Companion.TAG, "onCreate")
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
      //  DeeplinkReceiver().onReceive(this, intent)
        Hansel.pairTestDevice(intent.dataString)
        initView()
        setUpHanselListeners()
        setLiveData()
    }

    private fun setUpHanselListeners() {
        val hanselInternalEventsListener =
            HanselInternalEventsListener { eventName, _ ->
                val payload = HashMap<String, Any>()
                Smartech.getInstance(WeakReference(this@SplashScreenActivity)).trackEvent(eventName, payload)
            }
        HanselTracker.registerListener(hanselInternalEventsListener)
        Hansel.registerHanselDeeplinkListener { url ->
            Logger.w(Companion.TAG, "Hansel-> registerHanselDeeplinkListener")
            Logger.w(Companion.TAG, url)
            if (url.isNotEmpty()) {
                Logger.i("DeeplinkReceiver", "Deeplink: $url")
                val dispatcherIntent =
                    Intent(this@SplashScreenActivity, DeeplinkDispatcherActivity::class.java)
                dispatcherIntent.putExtra("deeplink", url)
                dispatcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(dispatcherIntent)
            } else {
                Logger.v("Activity", "does not have deeplink path.")
            }
        }
    }

    private fun setLiveData() {
        homeViewModel.observeAppConfig()
            .observe(this
            ) { data ->
                if (data != null) {
                    Logger.i(Companion.TAG, "observeAppConfig")
                    storeAppConfig(data)
                }
            }
        homeViewModel.observePendingReviewStatus().observe(this) { pendingReviewStatusResponse ->
            try {
                Logger.i(Companion.TAG, "pendingReviewStatusResponse")
                if (pendingReviewStatusResponse?.data != null) {
                    val intent = Intent(this@SplashScreenActivity, AddReviewActivity::class.java)
                    intent.putExtra(
                        AppConstants.WOLOO_ID,
                        pendingReviewStatusResponse.data!!.wolooId
                    )
                    startActivity(intent)
                    finish()
                } else {
                    /* SharedPrefSettings sps = new SharedPrefSettings();
                            if(sps.fetchIsVTION()){
                                callVtionActivity();
                            }
                            else{*/
                    startDashboardActivity()
                    // }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
                /* if(sps.fetchIsVTION()){
                            callVtionActivity();
                        }
                        else {*/startDashboardActivity()
                //}
            }
        }
    }

    private fun storeAppConfig(data: AuthConfigResponse.Data) {
        SharedPrefSettings.getPreferences.storeAuthConfig(data)
        if (data.getmAINTENANCESETTINGS()?.maintenanceFlag == "1") {
            CommonUtils.showMaintenanceDialog(this, false)
        } else if (mCommonUtils?.isLoggedIn == true) {
            //startActivity(new Intent(SplashActivity.this,WolooDashboard.class));
            //finish();
            val bundle = Bundle()
            Utility.logFirebaseEvent(this, bundle, AppConstants.APP_OPEN_AGAIN)
            /* SharedPrefSettings sps = new SharedPrefSettings();
            if(sps.fetchIsVTION()){
                callVtionActivity();
            }
            else {*/showForceUpdateDialog(data)
            // }
        } else {
            startSplashTextAnimation()
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.i(Companion.TAG, "onResume")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        try {
            Logger.i(Companion.TAG, "initView")
            mCommonUtils = CommonUtils()
            mSharedPreference?.setStoredBooleanPreference(
                this,
                SharedPreferencesEnum.APP_LAUNCHED_ONE_TIME.preferenceKey,
                true
            )
            binding?.loadingAnimation?.visibility = View.VISIBLE
            binding?.tvSkip?.visibility = View.GONE
            startAnimation()
            onRedirection()

            binding?.tvNext?.setOnClickListener {
                binding?.tvNext?.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
                if (SharedPrefSettings.getPreferences.issShownOnBoarding()) {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            } else {
               // startActivity(Intent(this@SplashScreenActivity, BoardingActivity::class.java))
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }
                finish()
            }
            binding?.tvSkip?.setOnClickListener {
                binding?.tvSkip?.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
                startActivity(Intent(this@SplashScreenActivity, WolooDashboard::class.java))
                finish()
            }
            try {
                FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(intent)
                    .addOnSuccessListener(
                        this,
                        object : OnSuccessListener<PendingDynamicLinkData?> {

                            override fun onSuccess(pendingDynamicLinkData: PendingDynamicLinkData?) {

                                try {
                                    var deepLink: Uri?
                                    if (pendingDynamicLinkData != null) {
                                        deepLink = pendingDynamicLinkData.link
                                        Logger.e("Dynamic Link  ", deepLink.toString())
                                        if (deepLink != null) {
                                            val voucher: String
                                            if (deepLink.toString().contains("voucher")) {
                                                try {
                                                    val uri = Uri.parse(deepLink.toString())
                                                    voucher = uri.getQueryParameter("voucher").toString()
                                                } catch (e: Exception) {
                                                    displayToast("Error while applying coupon!")
                                                    return
                                                }
                                                if (TextUtils.isEmpty(voucher)) return
                                                mSharedPreference!!.setStoredPreference(
                                                    this@SplashScreenActivity,
                                                    SharedPreferencesEnum.VOUCHER_CODE.preferenceKey,
                                                    voucher
                                                )
                                            } else if (deepLink.toString()
                                                    .contains("/wahcertificate/")
                                            ) {
                                                val wahCertificate =
                                                    deepLink.toString().split("/".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray()[deepLink.toString()
                                                        .split("/".toRegex())
                                                        .dropLastWhile { it.isEmpty() }
                                                        .toTypedArray().size - 3]
                                                mSharedPreference!!.setStoredPreference(
                                                    this@SplashScreenActivity,
                                                    SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                                                    wahCertificate
                                                )
                                            } else if (deepLink.toString().contains("/giftcard")) {
                                                mSharedPreference?.setStoredPreference(
                                                    this@SplashScreenActivity,
                                                    SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.preferenceKey,
                                                    "GiftCard"
                                                )
                                            } else if (deepLink.toString().contains("giftId=")) {
                                                try {
                                                    val uri = Uri.parse(deepLink.toString())
                                                    val giftId = uri.getQueryParameter("giftId")
                                                    if (TextUtils.isEmpty(giftId)) return
                                                    mSharedPreference?.setStoredPreference(
                                                        this@SplashScreenActivity,
                                                        SharedPreferencesEnum.GIFT_CARD_ID.preferenceKey,
                                                        giftId
                                                    )
                                                } catch (e: Exception) {
                                                    displayToast("Error while applying coupon!")
                                                    return
                                                }
                                            } else if (deepLink.toString()
                                                    .contains("/mobile_blog_detail")
                                            ) {
                                                intent.setData(pendingDynamicLinkData.link)
                                            } else {
                                                referralcode = deepLink.toString().substring(43)
                                                Logger.e("referralcode  ", referralcode)
                                                referralcode =
                                                    commonUtils!!.getBase64Decoded(referralcode)
                                                Logger.e(
                                                    "referralcode",
                                                    "getBase64Decoded: $referralcode"
                                                )
                                                mSharedPreference!!.setStoredPreference(
                                                    this@SplashScreenActivity,
                                                    SharedPreferencesEnum.REFERRAL_CODE.preferenceKey,
                                                    referralcode
                                                )
                                                SharedPrefSettings.getPreferences.storeReferralCode(
                                                    referralcode
                                                )
                                            }
                                        }
                                    }
                                } catch (exception: Exception) {
                                    CommonUtils.printStackTrace(exception)
                                }
                            }
                        })
                    .addOnFailureListener(this
                    ) { e -> Logger.w(Companion.TAG, "getDynamicLink:onFailure", e) }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun startAnimation() {
        try {
            Logger.i(Companion.TAG, "startAnimation")
            val aniScaleUp = AnimationUtils.loadAnimation(applicationContext, R.anim.scale_up)
            binding?.ivAppLogo?.startAnimation(aniScaleUp)
            aniScaleUp.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    private fun startSplashTextAnimation() {
        try {
            Logger.i(Companion.TAG, "startSplashTextAnimation")
            val aniBottomUpLogo = AnimationUtils.loadAnimation(
                applicationContext, R.anim.bottomtoup
            )
            binding?.ivAppLogo?.startAnimation(aniBottomUpLogo)
            aniBottomUpLogo.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
            val aniBottomUp = AnimationUtils.loadAnimation(
                applicationContext, R.anim.bottomtouptwo
            )
            binding?.tvSplashText?.startAnimation(aniBottomUp)
            aniBottomUp.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    binding?.tvSplashText?.visibility = View.VISIBLE
                    binding?.tvNext?.visibility = View.VISIBLE

                }
                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
            binding?.tvNext?.visibility = View.VISIBLE
        }
    }

    private fun onRedirection() {
        binding?.loadingAnimation?.visibility = View.GONE
        Logger.i(Companion.TAG, "onRedirection")
        val request = LocaleRequest.Locale()
        request.packageName = "in.woloo.www"
        request.platform = "android"
        val localeRequest = LocaleRequest()
        localeRequest.locale = request
        homeViewModel.getAppConfig(localeRequest)
    }

    private fun startDashboardActivity() {
        NetcoreUserDetails(this).checkIfUserIdentityIsSet()
        var intent = Intent(this@SplashScreenActivity, WolooDashboard::class.java)
        if (getIntent().action != null) {
            when (getIntent().action) {
                AppConstants.BLOG_CONTENT_NOTIFICATION -> intent.putExtra(
                    WolooDashboard.ARG_IS_SHOW_TRENDING_BLOGS,
                    true
                )

                AppConstants.PERIOD_TRACKER_NOTIFICATION -> intent.putExtra(
                    WolooDashboard.ARG_IS_SHOW_PERIOD_TRACKER,
                    true
                )

                AppConstants.NEW_WOLOO_NOTIFICATION -> {}
                AppConstants.NEARBY_WOLOO_OFFERS -> {
                    intent = Intent(this@SplashScreenActivity, SearchActivity::class.java)
                    intent.putExtra(SearchWolooActivity.ARG_SHOW_OFFERS, true)
                }

                AppConstants.PRODUCT_OFFER_NOTIFICATION -> intent.putExtra(
                    WolooDashboard.ARG_IS_SHOW_SHOP,
                    true
                )

                AppConstants.THIRST_REMINDER_NOTIFICATION -> intent.putExtra(
                    WolooDashboard.ARG_IS_SHOW_THIRST_REMINDER,
                    true
                )
            }
            if (getIntent() != null && getIntent().extras != null) {
                intent.putExtras(getIntent().extras!!)
                val uri = getIntent().data
                if (uri != null && uri.path!!.contains("mobile_blog_detail")) {
                    intent.putExtra(WolooDashboard.ARG_IS_SHOW_TRENDING_BLOGS, true)
                    intent.putExtra("blog_link", getIntent().data.toString())
                }
            }
        }
        startActivity(intent)
        finish()
    }


    private fun showForceUpdateDialog(outputAppConfigModel: AuthConfigResponse.Data) {
        try {
            Logger.i(Companion.TAG, "showForceUpdateDialog")
            if (notNow) {
                return
            }
            var versionName = ""
            try {
                versionName = BuildConfig.VERSION_CODE.toString()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(outputAppConfigModel.getaPPVERSION()?.versionCode)) {
                val appVersion = versionName.toDouble()
                val liveAppVersion =
                    outputAppConfigModel.getaPPVERSION()?.versionCode?.trim()?.toDouble()
                if (appVersion < liveAppVersion!!) {
                    val checkUpdateType = outputAppConfigModel.getaPPVERSION()?.forceUpdate
                    val cancelable = false
                    var type = 0
                    if (("0" == checkUpdateType)) {
                        type = 2
                    } else if (("1" == checkUpdateType)) {
                        type = 1
                    }
                    val customUpdateDialog =
                        CommonUtils.getAppUpdateDialog(this@SplashScreenActivity, cancelable, type)
                    val mCancelButton =
                        customUpdateDialog!!.findViewById<Button>(R.id.app_update_cancel_button)
                    val mMessageTextView =
                        customUpdateDialog.findViewById<TextView>(R.id.app_update_text)
                    try {
                        //here we setting message update text from server
                        val updateText = outputAppConfigModel.getaPPVERSION()?.updateText
                        if (updateText == null) {
                            mMessageTextView.text = resources.getString(R.string.update_text)
                        } else {
                            mMessageTextView.text = updateText
                        }
                    } catch (e: Exception) {
                        mMessageTextView.text = resources.getString(R.string.update_text)
                        CommonUtils.printStackTrace(e)
                    }
                    if (("1" == checkUpdateType)) {
                        mCancelButton.visibility = View.GONE
                    } else {
                        mCancelButton.visibility = View.VISIBLE
                    }
                    mCancelButton.setOnClickListener {
                        notNow = true
                        customUpdateDialog.dismiss()
                        redirectedToApplication()
                    }
                    val mUpdateButton =
                        customUpdateDialog.findViewById<Button>(R.id.app_update_update_button)
                    mUpdateButton.visibility = View.VISIBLE
                    mUpdateButton.setOnClickListener {
                        try {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            ("https://play.google.com/store/apps/details?id="
                                                    + BuildConfig.APPLICATION_ID)
                                        )
                                    )
                                )
                            }
                            finishAffinity()
                        } catch (e: Exception) {
                            CommonUtils.printStackTrace(e)
                        }
                    }
                    customUpdateDialog.setOnDismissListener { isProcessing = false }
                    isProcessing = true
                    customUpdateDialog.show()
                } else {
                    redirectedToApplication()
                }
            } else {
                redirectedToApplication()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            redirectedToApplication()
        }
    }

    private fun redirectedToApplication() {
        Logger.i(Companion.TAG, "redirectedToApplication")
        homeViewModel.getPendingReviewStatus()
    }

    private fun callVtionActivity() {
        val intent = Intent(this, VitionUserDataActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }
}