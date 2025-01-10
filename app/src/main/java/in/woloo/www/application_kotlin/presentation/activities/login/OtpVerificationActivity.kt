package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.databinding.ActivityOtpVerificationBinding
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchWolooActivity
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.api_classes.IntentConstants
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.model.lists_models.UserDetails
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import `in`.woloo.www.application_kotlin.presentation.fragments.login.DialogVitionEducation
import `in`.woloo.www.application_kotlin.presentation.fragments.login.NotificationPermissionDialog
import `in`.woloo.www.application_kotlin.utilities.AccessibilitySettingsObserver
import `in`.woloo.www.application_kotlin.utilities.AccessibilityUtil
import `in`.woloo.www.application_kotlin.utilities.NotificationSettingsObserver
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : BaseActivity(), TextWatcher , AccessibilitySettingsObserver.AccessibilityStateChangeListener{

    private lateinit var wolooViewModel: WolooViewModel
    private lateinit var binding: ActivityOtpVerificationBinding
    var resendCount = 0
    private var mobileNumber: String? = null
    private var requestId: String? = null

    private val SMS_CONSENT_REQUEST = 1002
    private val ACCESSIBILITY_REQUEST_CODE = 101  //// Added by Aarati July 2024  , for access permissions request
    private var isReceiverRegistered = false
    protected var mSharedPreference: SharedPreference? = null
    var isVtionUser : String? = null
    var voucherId : String? = null
    var isFirstSession : Int? = null
    private var observer: AccessibilitySettingsObserver? = null
    private var accessibilityStateReceiver: BroadcastReceiver? = null
    private var notificationStateReceiver: BroadcastReceiver? = null
    private var observerN: NotificationSettingsObserver? = null
    lateinit var alertDialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        mobileNumber = intent.getStringExtra(IntentConstants.MOBILE_NO)
        requestId = intent.getStringExtra(IntentConstants.REQUEST_ID)
        setContentView(binding.root)
        mSharedPreference = SharedPreference(this)
        wolooViewModel = ViewModelProvider(this)[WolooViewModel::class.java]
        setProgressBar()
        setNetworkDetector()
        setLiveData()
        initViews()
        setClickables()


        binding.editTextOne.setOnLongClickListener {
            pasteOtp()
            true
        }

        binding.editTextTwo.setOnLongClickListener {
            pasteOtp()
            true
        }

        binding.editTextThree.setOnLongClickListener {
            pasteOtp()
            true
        }

        binding.editTextFour.setOnLongClickListener {
            pasteOtp()
            true
        }

        // Added By Aarati
        if (!isReceiverRegistered) {
            SmsRetriever.getClient(this).startSmsUserConsent(null)
            val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            applicationContext.registerReceiver(smsVerificationReceiver, intentFilter , RECEIVER_EXPORTED)
            isReceiverRegistered = true;
        }

        observer = AccessibilitySettingsObserver(this);
        observer?.register();
        observer!!.setAccessibilityStateChangeListener(this)

        observerN = NotificationSettingsObserver(this);
        observerN?.register();


        accessibilityStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (AccessibilitySettingsObserver.ACTION_ACCESSIBILITY_STATE_CHANGED
                    == intent.action
                ) {
                    val isEnabled = intent.getBooleanExtra(
                        AccessibilitySettingsObserver.EXTRA_IS_ENABLED, false
                    )
                    Log.d("Accessibility Changed" , "service is $isEnabled")
                    onAccessibilityStateChanged(isEnabled)
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            accessibilityStateReceiver!!,
            IntentFilter(AccessibilitySettingsObserver.ACTION_ACCESSIBILITY_STATE_CHANGED)
        )


        notificationStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (NotificationSettingsObserver.ACTION_NOTIFICATION_LISTENER_STATE_CHANGED
                    == intent.action
                ) {
                    val isEnabled = intent.getBooleanExtra(
                        NotificationSettingsObserver.EXTRA_IS_ENABLED, false
                    )
                    onNotificationStateChanged(isEnabled)
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            notificationStateReceiver!!,
            IntentFilter(NotificationSettingsObserver.ACTION_NOTIFICATION_LISTENER_STATE_CHANGED)
        )


        /*  SmsRetriever.getClient(this).startSmsUserConsent(null)
          val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
          registerReceiver(smsVerificationReceiver, intentFilter)*/
    }

    private fun setClickables() {

    }

    private var countDownTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.timer.text = millisecondsToTime(millisUntilFinished) + "Sec"
            binding.tvResendcode.visibility = View.GONE
        }

        override fun onFinish() {
            binding.timer.text = ""
            binding.tvResendcode.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this)[WolooViewModel::class.java]
    }

    private fun millisecondsToTime(milliseconds: Long): String {
        return "" + String.format(
            "%d ",
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
    }

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            val name = consentIntent!!.resolveActivity(packageManager)
                            if (name.packageName.equals("com.google.android.gms", true) &&
                                name.className.equals(
                                    "com.google.android.gms.auth.api.phone.ui.UserConsentPromptActivity",
                                    true
                                )
                            ) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    consentIntent.removeFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    consentIntent.removeFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    consentIntent.removeFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                    consentIntent.removeFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                                }
                                startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                            }
                        } catch (e: ActivityNotFoundException) {
                            finish()
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    val oneTimeCode = parseOneTimeCode(message, 4)
                    if (!TextUtils.isEmpty(oneTimeCode)) {
                        binding.editTextOne.setText(oneTimeCode!![0].toString())
                        binding.editTextTwo.setText(oneTimeCode[1].toString())
                        binding.editTextThree.setText(oneTimeCode[2].toString())
                        binding.editTextFour.setText(oneTimeCode[3].toString())
                        binding.editTextFour.setSelection(binding.editTextFour.text!!.length);
                        binding.txtProceed.requestFocus();
                    }
                } else {
                    // Consent denied. User can type OTP manually.
                }

            ACCESSIBILITY_REQUEST_CODE ->
                if (isAccessibilityServiceEnabled) {
                    if (!isNotificationListenerEnabled) {
                          showNotificationPermissionDialog(this@OtpVerificationActivity)
                    } else {
                        callVitionActivity()
                    }
                } else {
                    callGenderActivity()
                }

            else -> super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun parseOneTimeCode(message: String?, smsCodeLength: Int): String? =
        message?.filter { it.isDigit() }
            ?.substring(0 until smsCodeLength)

    override fun onDestroy() {
        super.onDestroy()
       // unregisterReceiver(smsVerificationReceiver)
        //Added By Aarati
        if (isReceiverRegistered) {
            applicationContext.unregisterReceiver(smsVerificationReceiver)
            isReceiverRegistered = false;
        }
        observer!!.unregister()
        observerN!!.unregister()


    }

    private fun initViews() {
        try {
            countDownTimer.start()
            binding.tvResendcode.setOnClickListener(View.OnClickListener {
                resendCount++
                if (resendCount > 2) {
                    showResendOTPDialog()
                }
                if (!TextUtils.isEmpty(mobileNumber)) {
                    binding.editTextOne.setText("")
                    binding.editTextTwo.setText("")
                    binding.editTextThree.setText("")
                    binding.editTextFour.setText("")

                    val referralCode: String = SharedPrefSettings.getPreferences.fetchReferralCode().toString()
                    val request = SendOtpRequest(mobileNumber.toString() , referralCode)
                    request.mobile = mobileNumber.toString()
                    wolooViewModel.sendOtp(request)
                }
            })
            binding.tvMessage.text = resources.getString(R.string.otp_message) + " " + mobileNumber
            binding.editTextOne.addTextChangedListener(this)
            binding.editTextTwo.addTextChangedListener(this)
            binding.editTextThree.addTextChangedListener(this)
            binding.editTextFour.addTextChangedListener(this)
            binding.txtProceed.setOnClickListener(View.OnClickListener { v: View? ->
                try {
                    binding.txtProceed.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
                    verifyOtp()
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex);
                }
            })
            binding.editTextOne.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyOtp()
                }
                false
            })
            binding.editTextTwo.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyOtp()
                }
                false
            })
            binding.editTextThree.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyOtp()
                }
                false
            })
            binding.editTextFour.setOnEditorActionListener(TextView.OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyOtp()
                }
                false
            })
            binding.editTextFour.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == 67 && TextUtils.isEmpty(binding.editTextFour.text)) {
                    binding.editTextThree.requestFocus()
                }
                false
            })
            binding.editTextThree.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == 67 && TextUtils.isEmpty(binding.editTextThree.text)) {
                    binding.editTextTwo.requestFocus()
                }
                false
            })
            binding.editTextTwo.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == 67 && TextUtils.isEmpty(binding.editTextTwo.text)) {
                    binding.editTextOne.requestFocus()
                }
                false
            })
            binding.tvNotyou.setOnClickListener(View.OnClickListener { v: View? ->
                try {
                    goToLoginPage()
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex);
                }
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private fun showResendOTPDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_otp_not_received)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.btnCloseDialog)
            llStartFreeTrial.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun verifyOtp() {
        try {
            val password: String = binding.editTextOne.text.toString() + binding.editTextTwo.text
                .toString() + binding.editTextThree.text.toString() + binding.editTextFour.text.toString()
            if (isEnteredOTP()) {
                val request = VerifyOtpRequest( requestId.toString() , password.trim { it <= ' ' } , SharedPrefSettings.getPreferences.fetchReferralCode().toString() )
                request.requestId = requestId.toString()
                request.otp = password.trim { it <= ' ' }
                if(SharedPrefSettings.getPreferences.fetchReferralCode()!= null){
                    request.referralCode = SharedPrefSettings.getPreferences.fetchReferralCode().toString()
                }
                wolooViewModel.verifyOtp(request)
            } else {
                displayToast("Please enter valid otp")
            }
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private fun isEnteredOTP(): Boolean {
        var isOTPEntered = false
        try {
            if (!TextUtils.isEmpty(binding.editTextOne.text.toString()) && !TextUtils.isEmpty(
                    binding.editTextTwo.text.toString()
                ) && !TextUtils.isEmpty(binding.editTextThree.text.toString()) && !TextUtils.isEmpty(
                    binding.editTextFour.text.toString()
                )
            ) {
                isOTPEntered = true
            }
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex);
        }
        return isOTPEntered
    }

    private fun goToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun setLiveData() {
        wolooViewModel.observeVerifyOtp().observe(this, Observer {
            if (it != null) {
                userLoggedInSuccessfully(it.data)
                val updateDeviceRequest  = HashMap<String,String>()
                updateDeviceRequest.put("deviceSerial",CommonUtils().getDeviceId(this))
                updateDeviceRequest.put("deviceToken",CommonUtils().getDeviceToken(this)!!)
                wolooViewModel.updateDeviceToken(updateDeviceRequest)
                //moveToDashboard()
            } else {
                displayToast(WolooApplication.errorMessage)
                WolooApplication.errorMessage = ""
            }
        })
        wolooViewModel.observeSendOtp().observe(this, Observer {
            if (it != null) {
                requestId = it.data?.requestId
                resetViews()
            } else {
                displayToast(WolooApplication.errorMessage)
                WolooApplication.errorMessage = ""
            }
        })
    }

    private fun resetViews() {
        countDownTimer.start()
//        if (!TextUtils.isEmpty(mobileNumber) && OTPFragment.isValidEmail(mobileNumber)) {
//            Toast.makeText(
//                this,
//                "OTP is successfully sent to registered email address.",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else
        if (!TextUtils.isEmpty(mobileNumber) && isValidMobile(mobileNumber.toString())) {
            Toast.makeText(
                this,
                "OTP has been sent on your registered mobile number!",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.tvResendcode.visibility = View.GONE
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun userLoggedInSuccessfully(data: VerifyOtpResponse?) {
        if (data != null) {
            pref.storeToken(data.token)
            pref.storeSupplierId(data.supplierId)
            data.user?.let { pref.storeUserDetails(it) }

            SharedPrefSettings.getPreferences.storeReferralCode("")
            SharedPrefSettings.getPreferences.storeIsLoggedIn(true)
            //SharedPrefSettings.getPreferences.storeIsVTION(true)




            if (data.user != null && data.user!!
                    .isFirstSession != null && data.user!!.isFreeTrial == 1) {
                // showFreeTrialDialog(data.user!!)
                isVtionUser = data.user!!.isVtionUser
                voucherId = data.user!!.voucherId
                isFirstSession = data.user!!.isFirstSession
                Logger.d("aarati" , "vtion voucher $isVtionUser $voucherId")
                val serviceClassName = "sdk.vtion.in.sdkcore.UsersAccessibility"
                val isEnabled: Boolean = AccessibilityUtil.isAccessibilityServiceEnabled(
                    this,
                    serviceClassName)
                Log.i("Access Log" , isEnabled.toString());
                if (isAccessibilityServiceEnabled) {
                    if (SharedPrefSettings.getPreferences.fetchIsVTION()) {
                        callVitionActivity()
                    } else {
                        startDashboardActivity()
                    }

                } else {
                    val vCode: String  = mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "").toString()
                    Log.d("Voucher_code" , vCode);
                    if(vCode == null || vCode.isEmpty()) {
                        if(isFirstSession == 1) {
                           /* startActivity(
                                Intent(
                                    applicationContext,
                                    TermsAndConditionsActivity::class.java
                                )
                            )*/
                            showConsentDialog()
                        }
                        else{
                            startDashboardActivity()
                            finish()
                        }
                    }
                    else
                    {
                        startDashboardActivity()
                        finish()
                    }


                }
            }else {
                sendDeviceToken(this)
                moveToDashboard()
            }
        }
    }

    private fun showFreeTrialDialog(user: UserDetails) {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val startFreeTrial = dialog.findViewById<View>(R.id.tv_startfreetrial) as TextView
            val daysTrials = dialog.findViewById<View>(R.id.tv_daysTrials) as TextView
            val typeOfVoucher = dialog.findViewById<View>(R.id.tv_typeOfVoucher) as TextView
            val ivFreeTrial = dialog.findViewById<View>(R.id.ivFreeTrial) as ImageView
            try {
                val authConfigResponse: AuthConfigResponse.Data =
                    CommonUtils.authconfig_response(this)!!
                daysTrials.text = authConfigResponse.freeTrialPeriodDays+"\nDAYS"
                typeOfVoucher.text = authConfigResponse.freeTrialText
//                ImageUtil.loadImage(
//                    this,
//                    ivFreeTrial,
//                    authConfigResponse.getuRLS().free_trial_image_url
//                )
//                tv_daysTrials.setText(authConfigResponse.freeTrialPeriodDays+"\nDays")
//                ivFreeTrial.setImageResource(R.drawable.free_trial_image)
            } catch (ex: java.lang.Exception) {
                CommonUtils.printStackTrace(ex);
            }
            val authConfigResponse: AuthConfigResponse.Data? =
                CommonUtils.authconfig_response(this)
            if (authConfigResponse != null) {
                var freeTrialDialogText: String =
                    authConfigResponse.getcUSTOMMESSAGE()?.freeTrialDialogText.toString()
                freeTrialDialogText = freeTrialDialogText.replace("\\\\n".toRegex(), "\n")
                if(freeTrialDialogText.contains("7")) {
                    freeTrialDialogText = freeTrialDialogText.replace(
                        "7",
                        authConfigResponse.freeTrialPeriodDays
                    ); // Added By Aarati for vtion users
                }

                startFreeTrial.text = decode(freeTrialDialogText)
            }
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llStartFreeTrial) as LinearLayout
            llStartFreeTrial.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

            dialog.setOnDismissListener {
                sendDeviceToken(this)
                moveToDashboard()
            }
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun sendDeviceToken(context: Context) {

    }

    private fun decode(text: String): String? {
        return text.replace("&amp;", "&")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private fun enableSubmitButton() {
        try {
            binding.txtProceed.isEnabled = true
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private fun disableSubmitButton() {
        try {
            binding.txtProceed.isEnabled = false
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex);
        }
    }

//    private fun sendDeviceToken(mContext: Context) {
//        Logger.i(OTPFragment.TAG, "sendDeviceToken")
//        try {
//            mLoginPresenter.updateDeviceTokenAPI(
//                CommonUtils().getDeviceId(mContext),
//                CommonUtils().getDeviceToken(mContext)
//            )
//        } catch (ex: java.lang.Exception) {
//             CommonUtils.printStackTrace(ex);
//        }
//    }

    private fun moveToAddReview(wolooId: Int) {
        val intent: Intent = Intent(this, AddReviewActivity::class.java)
        intent.putExtra(AppConstants.WOLOO_ID, wolooId)
        startActivity(intent)
        finish()
    }

    private fun moveToDashboard() {
        if(SharedPrefSettings.getPreferences.fetchIsVTION()){
            callVitionActivity()
        }
        else
        {
            val userInfo: UserDetails = CommonUtils().userInfo!!

            NetcoreUserDetails(this).setNetcoreUserIdentityAndLogin(userInfo.mobile.orEmpty())
            NetcoreUserDetails(this).updateNetcoreUserProfile()

            Logger.e("data", "save to netcore")
            if (TextUtils.isEmpty(userInfo.gender)) {

                val serviceClassName = "sdk.vtion.in.sdkcore.UsersAccessibility"
                val isEnabled: Boolean = AccessibilityUtil.isAccessibilityServiceEnabled(
                    this,
                    serviceClassName)
                Log.i("Access Log" , isEnabled.toString());
                if (isAccessibilityServiceEnabled) {

                    if (SharedPrefSettings.getPreferences.fetchIsVTION()) {
                        callVitionActivity()
                    } else {
                        startDashboardActivity()
                    }

                } else {
                    val vCode: String  = mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "").toString()
                    Log.d("Voucher_code" , vCode);
                    if(vCode == null || vCode.isEmpty()) {
                        if(isFirstSession == 1) {
                           /* startActivity(
                                Intent(
                                    applicationContext,
                                    TermsAndConditionsActivity::class.java
                                )
                            )*/
                            showConsentDialog()
                        }
                        else
                        {
                            startDashboardActivity()
                        }
                    }
                    else
                    {
                        startDashboardActivity()
                    }
                    finish()
                }
                // Changes done by Aarati // Changes by Aarati July 2024  , simple function which consist of activity call for vtion
                //if (userInfo.isFreeTrial == 1) {

                //   startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), ACCESSIBILITY_REQUEST_CODE)

                /* } else {

                 }
                startActivity(
                    Intent(
                        applicationContext,
                        SelectGenderActivity::class.java
                    )
                )
                finish()
    */
            } else {
                try {
                    startActivity(
                        Intent(
                            this,
                            WolooDashboard::class.java
                        ).putExtra("valuepassed", "showdialog")
                    )
                    finish()
                } catch (ex: java.lang.Exception) {
                    CommonUtils.printStackTrace(ex);
                }
            }
        }
    }

    override fun afterTextChanged(editable: Editable?) {
        try {
            if (editable?.length == 1) {
                if (binding.editTextOne.length() == 1) {
                    binding.editTextTwo.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextTwo.length() == 1) {
                    binding.editTextThree.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextThree.length() == 1) {
                    binding.editTextFour.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextFour.length() == 1) {
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
            } else if (editable?.length == 0) {
                if (binding.editTextFour.length() == 0) {
                    binding.editTextThree.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextThree.length() == 0) {
                    binding.editTextTwo.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextTwo.length() == 0) {
                    binding.editTextOne.requestFocus()
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
                if (binding.editTextOne.length() == 0) {
                    if (isEnteredOTP()) {
                        enableSubmitButton()
                    } else {
                        disableSubmitButton()
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex);
        }
    }




    // Added by Aarati July 2024 @woloo  , simple function which consist of activity call for vtion
    private fun callVitionActivity() {
        startActivity(
            Intent(
                applicationContext,
                VitionUserDataActivity::class.java
            )
        )
        finish()
    }


    // Added by Aarati July 2024  , simple function which consist of activity call for gender
    private fun callGenderActivity(){
        startActivity(
            Intent(
                applicationContext,
                SelectGenderActivity::class.java
            )
        )
        finish()
    }


    private fun pasteOtp() {
        // Get the ClipboardManager system service
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData? = clipboard.primaryClip

        // Check if the clipboard contains data and that it is valid text
        if (clipData != null && clipData.itemCount > 0) {
            val textToPaste = clipData.getItemAt(0).text?.toString()

            Toast.makeText(this , textToPaste , Toast.LENGTH_LONG)

            // Check if the text content is valid and has a length of 4 (for OTP)
            if (!textToPaste.isNullOrEmpty() && textToPaste.length == 4) {
                // Split the text into individual OTP characters and set them to respective fields
                binding.editTextOne.setText(textToPaste[0].toString())
                binding.editTextTwo.setText(textToPaste[1].toString())
                binding.editTextThree.setText(textToPaste[2].toString())
                binding.editTextFour.setText(textToPaste[3].toString())
            }
        }
    }



    private fun startDashboardActivity() {
        NetcoreUserDetails(this).checkIfUserIdentityIsSet()
        var intent = Intent(
            this@OtpVerificationActivity,
            WolooDashboard::class.java
        )
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
                    intent = Intent(this@OtpVerificationActivity, SearchActivity::class.java)
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
                finish()
            }
        }
        startActivity(intent)
        finish()
    }


    private fun showConsentDialog() {
        try {
            val alertDialogBuilder = AlertDialog.Builder(this@OtpVerificationActivity)
            val child: View = layoutInflater.inflate(R.layout.dialog_consent_layout, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(false)
            alertDialog = alertDialogBuilder.create()
            alertDialog?.window?.setBackgroundDrawableResource(R.color.transparent_background)
            val tvDeny = child.findViewById<TextView>(R.id.tv_disagree_dialog)
            val tvAgree = child.findViewById<TextView>(R.id.tv_agree_dialog)


            // Set fullscreen layout parameters
            alertDialog.setOnShowListener {
                val window = alertDialog.window
                if (window != null) {
                    val params = window.attributes
                    params.gravity = Gravity.BOTTOM
                    window.attributes = params
                }
            }
            tvDeny.setOnClickListener {
                alertDialog.dismiss()
                callGenderActivity()
            }
            tvAgree.setOnClickListener {
                callAccessibility()
                alertDialog.dismiss()
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    private fun callAccessibility() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE)
    }

    private val isAccessibilityServiceEnabled: Boolean
        get() {
            val accessibilityEnabled = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED,
                0
            )
            return accessibilityEnabled == 1
        }


    private fun checkNotificationPermission() {
        if (!isNotificationListenerEnabled) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        } else {
            callVitionActivity()
        }
    }

    private val isNotificationListenerEnabled: Boolean
        get() {
            val pkgName = packageName
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            return flat != null && flat.contains(pkgName)
        }



    override fun onAccessibilityStateChanged(isEnabled: Boolean) {
        if (isEnabled) {
            // Accessibility service was enabled
            Log.e("Terms and Conditions", "Accessibility Service Enabled!")
          //  callVitionActivity()

            startActivity(
                Intent(
                    applicationContext,
                    NotificationPermissionDialog::class.java
                )
            )
            finish()
              //  newFragment.show(fragmentManager, )

          /* lifecycleScope.launch {
                val currentTheme = resources.newTheme()
                theme.applyStyle(R.style.AppTheme, true)
                Log.d("ThemeDebug", "Current Theme: $currentTheme")
                // Call the suspend function to show the dialog
                showNotificationDialog(applicationContext)
            }*/

        } else {
            // Accessibility service was disabled
            Log.e("Terms and Conditions", "Accessibility Service Disabled!")
        }
    }

    private fun onNotificationStateChanged(isEnabled: Boolean) {
        if (isEnabled) {
            // Accessibility service was enabled
            Log.e("Terms and Conditions", "Notification Service Enabled!")
            callVitionActivity()
        } else {
            // Accessibility service was disabled
            Log.e("Terms and Conditions", "Notification Service Disabled!")
            callGenderActivity()
        }
    }



    override fun onResume() {
        super.onResume()
        val isEnabled =intent.getBooleanExtra(
            AccessibilitySettingsObserver.EXTRA_IS_ENABLED, false
        )
        onAccessibilityStateChanged(isEnabled)
    }

    fun showNotificationPermissionDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Notification Permission Required")
        builder.setMessage("Please enable notifications for this app to receive important updates.")

        builder.setPositiveButton("Go to Settings") { dialog, _ ->
            dialog.dismiss()
            checkNotificationPermission()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()

        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showNotificationDialog(context: Context) {
        try {
            val alertDialogBuilder = AlertDialog.Builder(context)
            val child: View = layoutInflater.inflate(R.layout.logout_dialog, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(false)
            alertDialog = alertDialogBuilder.create()
            alertDialog?.window?.setBackgroundDrawableResource(R.color.transparent_background)
            val tvDeny = child.findViewById<TextView>(R.id.tvCancel)
            val tvAgree = child.findViewById<TextView>(R.id.tvLogout)
            val textHeader = child.findViewById<TextView>(R.id.tv_logout)

            textHeader.text = "Notification Permission Required\nPlease enable notifications for this app to receive important updates."


            // Set fullscreen layout parameters
            alertDialog.setOnShowListener {
                val window = alertDialog.window
                if (window != null) {
                    val params = window.attributes
                    params.gravity = Gravity.CENTER
                    window.attributes = params
                }
            }
            tvDeny.setOnClickListener {
                alertDialog.dismiss()
                callVitionActivity()
            }
            tvAgree.setOnClickListener {
                alertDialog.dismiss()
                checkNotificationPermission()

            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    companion object {
        private const val ACCESSIBILITY_REQUEST_CODE = 101
    }

}