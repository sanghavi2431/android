package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.IntentConstants
import `in`.woloo.www.application_kotlin.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import `in`.woloo.www.application_kotlin.presentation.HomeFragment
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchWolooActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.utilities.DialpogObserver
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.databinding.ActivityOtpVerificationBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.auth_request_response.AddCustomerOnMedusaRequest
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.splash.UserDetails
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : BaseActivity(), TextWatcher
 {

    private lateinit var wolooViewModel: WolooViewModel
    private lateinit var binding: ActivityOtpVerificationBinding
    var resendCount = 0
    private var mobileNumber: String? = null
    private var requestId: String? = null

    private val SMS_CONSENT_REQUEST = 1002
    private var isReceiverRegistered = false
    protected var mSharedPreference: SharedPreference? = null
    var isVtionUser : String? = null
    var voucherId : String? = null
    var isFirstSession : Int? = null
    lateinit var alertDialog:AlertDialog
    private var shouldShowDialog: Boolean = false
     private lateinit var dialogViewModel: DialpogObserver
     private var isFirstTime : Boolean = false
     private var storeViewModel: StoreViewModel? = null
     private var regionId: String? = null
     private var cartId: String? = null
     private var newChagnedFlowData : VerifyOtpResponse? = null
     private var loginToken : String? = null
     var encryptedFromApi: String = ""
     val secretKey = AppConstants.SECRETEKEYCRYPTO
     var decryptedData : String = ""
     var isRegisteredForShop: Int = 0
     private var otpVerified = false            // set true only after OTP success
     private var consentFlowActive = false
     private var profileViewModel: ProfileViewModel? = null
     private var powderroomExpiryDate:String? = ""
     private var subscriptionName:String? = ""



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        mobileNumber = intent.getStringExtra(IntentConstants.MOBILE_NO)
        requestId = intent.getStringExtra(IntentConstants.REQUEST_ID)
        setContentView(binding.root)
        mSharedPreference = SharedPreference(this)
        wolooViewModel = ViewModelProvider(this)[WolooViewModel::class.java]
         storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
         otpVerified = false
         consentFlowActive = false
         profileViewModel = ViewModelProvider(this).get<ProfileViewModel>(
             ProfileViewModel::class.java
         )


         setProgressBar()
        setNetworkDetector()
        setLiveData()
        initViews()
        setClickables()

         binding.ivBack.setOnClickListener{
             val intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)
             finish()
         }

           if(binding.tvResendcode.isEnabled == true) {
               binding.tvResendcode.setTextColor(ContextCompat.getColor(this, R.color.black))
           }
         else{
               binding.tvResendcode.setTextColor(ContextCompat.getColor(this, R.color.chip_color))
           }

         /*    dialogViewModel = ViewModelProvider(this).get(DialpogObserver::class.java)

            dialogViewModel.showDialog.observe(this) { shouldShow ->
                if (shouldShow) {
                    showNotificationDialog(this)
                    dialogViewModel.resetDialogRequest() // Reset the state
                }
            }*/

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



    }

    private fun setClickables() {

    }

    private var countDownTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.timer.text = millisecondsToTime(millisUntilFinished) + "Sec"
            //binding.tvResendcode.visibility = View.GONE
            binding.tvResendcode.isEnabled = false
        }

        override fun onFinish() {
            binding.timer.text = ""
            binding.tvResendcode.visibility = View.VISIBLE
            binding.tvResendcode.isEnabled = true

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


            else -> super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun parseOneTimeCode(message: String?, smsCodeLength: Int): String? =
        message?.filter { it.isDigit() }
            ?.substring(0 until smsCodeLength)

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            applicationContext.unregisterReceiver(smsVerificationReceiver)
            isReceiverRegistered = false;
        }

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
            if ((binding.editTextOne.text.toString().isEmpty())
                || (binding.editTextTwo.text.toString().isEmpty())
                || (binding.editTextThree.text.toString().isEmpty())
                || (binding.editTextFour.text.toString().isEmpty())) {
                showLoginFailureDialog("Please enter OTP")
            }
            else {
                val password: String =
                    binding.editTextOne.text.toString() + binding.editTextTwo.text
                        .toString() + binding.editTextThree.text.toString() + binding.editTextFour.text.toString()
                if (isEnteredOTP()) {
                    val request = VerifyOtpRequest(
                        requestId.toString(),
                        password.trim { it <= ' ' },
                        SharedPrefSettings.getPreferences.fetchReferralCode().toString())
                    request.requestId = requestId.toString()
                    request.otp = password.trim { it <= ' ' }
                    if (SharedPrefSettings.getPreferences.fetchReferralCode() != null) {
                        request.referralCode =
                            SharedPrefSettings.getPreferences.fetchReferralCode().toString()
                    }
                    wolooViewModel.verifyOtp(request)
                } else {
                    displayToast("Please enter valid otp")
                }
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

    @SuppressLint("SuspiciousIndentation")
    private fun setLiveData() {



        wolooViewModel.observeVerifyOtp().observe(this, Observer {
            if (it != null) {

              //  profileViewModel!!.getUserProfile()

              /*  profileViewModel?.observeUserProfile()
                    ?.observe(this, Observer<BaseResponse<UserProfile>> { viewProfileResponse ->
                        try{
                            if (viewProfileResponse == null) {
                                Logger.e("TAG", "viewProfileResponse is null")
                            }

                            Logger.i("TAG", "setProfileResponse")

                            if (viewProfileResponse != null && viewProfileResponse.data != null) {
                                powderroomExpiryDate = viewProfileResponse?.data!!.profile!!.expiryDate
                                subscriptionName = viewProfileResponse?.data!!.planData!!.name
                                mSharedPreference?.setStoredPreference(
                                    this,
                                    SharedPreferencesEnum.USER_PROFILE_EXPIRY_DATE.preferenceKey,
                                    powderroomExpiryDate
                                )
                                if(subscriptionName!= null) {
                                    mSharedPreference?.setStoredPreference(
                                        this,
                                        SharedPreferencesEnum.USER_PROFILE_SUBSCRIPTION_NAME.preferenceKey,
                                        subscriptionName
                                    )
                                }
                            }




                        }catch (e : Exception)
                        {

                        }

                    })*/

                Handler(Looper.getMainLooper()).postDelayed({
                userLoggedInSuccessfully(it.data)

                    powderroomExpiryDate = it.data!!.user!!.expiryDate
                    subscriptionName = it?.data!!.user!!.isFreeTrial.toString()
                    mSharedPreference?.setStoredPreference(
                        this,
                        SharedPreferencesEnum.USER_PROFILE_EXPIRY_DATE.preferenceKey,
                        powderroomExpiryDate
                    )
                    if(subscriptionName!= null) {
                        mSharedPreference?.setStoredPreference(
                            this,
                            SharedPreferencesEnum.USER_PROFILE_SUBSCRIPTION_NAME.preferenceKey,
                            subscriptionName
                        )
                    }

                val updateDeviceRequest  = HashMap<String,String>()
                updateDeviceRequest.put("deviceSerial",CommonUtils().getDeviceId(this))
                updateDeviceRequest.put("deviceToken",CommonUtils().getDeviceToken(this))
                wolooViewModel.updateDeviceToken(updateDeviceRequest)

                if(it.data?.user?.isFirstSession == 1) {
                    if (it.data?.user?.isFreeTrial == 1) {
                        isFirstTime = true
                    }
                }
                }, 1000)

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
        storeViewModel!!.observeRegisteredOnMedusa().observe(this , Observer
        {
            try {
                if (it != null) {
                    SharedPrefSettings.getPreferences.storeShopRegisterToken(it.token)
                    Logger.d("aarati token", it.token)
                    val request = AddCustomerOnMedusaRequest()
                    request.email = "$mobileNumber@gmail.com"

                    // storeViewModel!!.getCustomerAuthPassOnMedusa(request)
                    storeViewModel!!.getAddCustomer(request)
                }
            }catch(e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        })
        storeViewModel!!.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let {
               // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                Logger.i("Aarati Store" ,"Registration Error: $it")
                val request = RegisterOnMedusaRequest()
                request.email = "$mobileNumber@gmail.com"
                request.password = decryptedData
                storeViewModel!!.getCustomerAuthPassOnMedusa(request)
            }
        })
        storeViewModel!!.observeAddCustomer().observe(this , Observer
        {
            try{
            if(it != null)
            {
                Logger.d("aarati cust" , it.customer.toString())
                SharedPrefSettings.getPreferences.storeStoreCustomerId(it.customer!!.id)

                storeViewModel!!.getInformRegisterForShop()
                val request = RegisterOnMedusaRequest()
                request.email = "$mobileNumber@gmail.com"
                request.password = decryptedData
                storeViewModel!!.getCustomerAuthPassOnMedusa(request)
                Logger.d("aarati cust" , it.customer.toString())
            }
            }catch(e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        })

        storeViewModel!!.observeCustomerAuthPassOnMedusa().observe(this , Observer
        {
            try{
            it?.let {
                storeViewModel!!.getRegionList()
                Logger.d("aarati token 4", it.token)
                loginToken = it.token
                SharedPrefSettings.getPreferences.storeShopLoginToken(it.token)
                Logger.d("aarati token 1", it.token)
            }
            storeViewModel!!.observeCustomerAuthPassOnMedusa().removeObservers(this)
            }catch(e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        })



        storeViewModel!!.observeRegionList().observe(this , Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.regions!!}")
                regionId = it.regions!![0].id
                val request = CartRequest()
                request.region_id = regionId
                SharedPrefSettings.getPreferences.storeRegionId(regionId!!)
                storeViewModel!!.getCartCreate(request)
                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })


        storeViewModel!!.observeCartCreate().observe(this , Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                SharedPrefSettings.getPreferences.storeCartId(it.cart!!.id)
               /* userLoggedInSuccessfully(newChagnedFlowData)
                val updateDeviceRequest = HashMap<String, String>()
                updateDeviceRequest.put("deviceSerial", CommonUtils().getDeviceId(applicationContext))
                updateDeviceRequest.put("deviceToken", CommonUtils().getDeviceToken(applicationContext)!!)
                wolooViewModel.updateDeviceToken(updateDeviceRequest)*/
                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
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
      //  binding.tvResendcode.visibility = View.GONE
        binding.tvResendcode.isEnabled = false
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

             Toast.makeText(this , data.user!!.isFreeTrial.toString() + " "+  data.user!!
                 .isFirstSession, Toast.LENGTH_LONG)


             if (data.user != null && data.user!!
                     .isFirstSession != null && data.user!!.isFreeTrial == 1) {

                 sendDeviceToken(this)
                 moveToDashboard()
               //  showFreeTrialDialog(data.user!!)
             } else {
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
         val userInfo: UserDetails = CommonUtils().userInfo

         NetcoreUserDetails(this).setNetcoreUserIdentityAndLogin(userInfo.mobile.orEmpty())
         NetcoreUserDetails(this).updateNetcoreUserProfile()

         mSharedPreference!!.setStoredBooleanPreference(
             this,
             SharedPreferencesEnum.SEARCH_COINS_MODE.getPreferenceKey(),
             false
         );
         mSharedPreference!!.setStoredBooleanPreference(
             this,
             SharedPreferencesEnum.COINS_MODE.getPreferenceKey(),
             false
         );
         mSharedPreference!!.setStoredBooleanPreference(
             this,
             SharedPreferencesEnum.ENROUTE_COINS_MODE.getPreferenceKey(),
             false
         );
         Logger.e("data","save to netcore")
         if (TextUtils.isEmpty(userInfo.gender)) {
            showLoginConsentDialog()
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

     private fun moveToDashboard1() {
         val commonUtils = CommonUtils()
         val (_, _, _, _, _, _, mobile) = commonUtils.userInfo!!
         val netcoreUserDetails = NetcoreUserDetails(this)
         netcoreUserDetails.setNetcoreUserIdentityAndLogin(mobile ?: "")
         netcoreUserDetails.updateNetcoreUserProfile()
         Logger.e("data", "save to netcore")
         startActivity(Intent(this@OtpVerificationActivity, WolooDashboard::class.java))
         finish()
     }

     private fun showFreeTrialDialog() {
         try {
             val dialog = Dialog(this)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
             dialog.setCancelable(true)
             dialog.setCanceledOnTouchOutside(true)
             dialog.setContentView(R.layout.dialog_start_free_trial)
             dialog.window!!.attributes = dialog.window!!.attributes
             dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
             val startFreeTrial = dialog.findViewById<TextView>(R.id.tv_startfreetrial)
             /* val daysTrials = dialog.findViewById<TextView>(R.id.tv_daysTrials)
              val typeOfVoucher = dialog.findViewById<TextView>(R.id.tv_typeOfVoucher)
              val ivFreeTrial = dialog.findViewById<ImageView>(R.id.ivFreeTrial)
              try {
                  val authConfigResponse = CommonUtils.authconfig_response(this)
                  daysTrials.text = """
                      ${authConfigResponse.freeTrialPeriodDays}
                      DAYS
                      """.trimIndent()
                  typeOfVoucher.text = authConfigResponse.freeTrialText
                  // ImageUtil.loadImage(
                  //     this,
                  //     ivFreeTrial,
                  //     authConfigResponse.getuRLS().getFreeTrialImageUrl()
                  // );
                  // tv_daysTrials.setText(authConfigResponse.getFreeTrialPeriodDays() + "\nDays");
                  // ivFreeTrial.setImageResource(R.drawable.free_trial_image);
              } catch (ex: Exception) {
                  CommonUtils.printStackTrace(ex)
              }*/

             startFreeTrial.setText("Your" + AppConstants.FREE_TRAIL_NORMAL_DAYS + "Days\nFree Trial has been\nActivated.")
             val authConfigResponse = CommonUtils.authconfig_response(this)
             if (authConfigResponse != null) {
                 var freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE()?.freeTrialDialogText
                 freeTrialDialogText = freeTrialDialogText?.replace("\\\\n", "\n")
                 freeTrialDialogText = freeTrialDialogText?.replace("<", "")
                 freeTrialDialogText = freeTrialDialogText?.replace(">", "")
                 freeTrialDialogText =
                     freeTrialDialogText?.replace("Trial Days", AppConstants.FREE_TRAIL_NORMAL_DAYS)
                 startFreeTrial.text = decode(freeTrialDialogText.toString())
             }
             val llStartFreeTrial = dialog.findViewById<LinearLayout>(R.id.llStartFreeTrial)
             llStartFreeTrial.setOnClickListener { v: View? -> dialog.dismiss() }
             dialog.show()
             dialog.setOnDismissListener { dialog1: DialogInterface? ->
                 moveToDashboard1()
                 dialog.dismiss()
             }
         } catch (e: Exception) {
             CommonUtils.printStackTrace(e)
         }
     }

     private fun showLoginFailureDialog(msg: String?) {
         try {
             val dialog = Dialog(this)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.setCancelable(false)
             dialog.setCanceledOnTouchOutside(false)
             dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
             dialog.setContentView(R.layout.dialog_login_failure)
             dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
             val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
             val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
             tv_msg.text = msg
             btnCloseDialog.setOnClickListener {
                 if (dialog.isShowing) {
                     dialog.dismiss()
                 }
             }
             dialog.show()
         } catch (e: java.lang.Exception) {
             CommonUtils.printStackTrace(e)
         }
     }

     private fun showLoginConsentDialog() {
         try {
             val dialog = Dialog(this)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.setCancelable(false)
             dialog.setCanceledOnTouchOutside(false)
             dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
             dialog.setContentView(R.layout.show_all_in_one_consent)
             dialog.window?.setGravity(Gravity.BOTTOM)
             dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
             dialog.window?.setLayout(
                 WindowManager.LayoutParams.MATCH_PARENT, // Width to match parent (full screen)
                 WindowManager.LayoutParams.WRAP_CONTENT  // Height to match parent (full screen)
             )
             val btnCloseDialog = dialog.findViewById<View>(R.id.tv_agree_dialog) as TextView
             val tv_msg = dialog.findViewById<View>(R.id.tv_disagree_dialog) as TextView
             btnCloseDialog.setOnClickListener {
                 if (dialog.isShowing) {
                     if(isFirstTime)
                    showFreeTrialDialog()
                     else
                         moveToDashboard1()
                     dialog.dismiss()
                 }
             }
             tv_msg.setOnClickListener {
                 if (dialog.isShowing) {
                     if(isFirstTime)
                   showFreeTrialDialog()
                     else
                         moveToDashboard1()
                     dialog.dismiss()
                 }
             }
             dialog.show()
         } catch (e: java.lang.Exception) {
             CommonUtils.printStackTrace(e)
         }
     }

}