package `in`.woloo.www.v2.login.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityLoginV2Binding
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.v2.WebActivity
import `in`.woloo.www.v2.base.BaseActivity
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.constant.IntentConstants
import `in`.woloo.www.v2.data.local.SharedPrefSettings
import `in`.woloo.www.v2.data.local.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.v2.home.viewmodel.HomeViewModel
import `in`.woloo.www.v2.login.model.SendOtpRequest
import `in`.woloo.www.v2.login.model.SendOtpResponse
import `in`.woloo.www.v2.login.viewmodel.LoginViewModel
import `in`.woloo.www.v2.splash.model.LocaleRequest
import studios.codelight.smartloginlibrary.SmartLogin
import studios.codelight.smartloginlibrary.SmartLoginCallbacks
import studios.codelight.smartloginlibrary.SmartLoginConfig
import studios.codelight.smartloginlibrary.users.SmartUser
import studios.codelight.smartloginlibrary.util.SmartLoginException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : BaseActivity(), SmartLoginCallbacks {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityLoginV2Binding

    private var currentUser: SmartUser? = null
    private var config: SmartLoginConfig? = null
    private var smartLogin: SmartLogin? = null

    private val callbackManager: CallbackManager? = null
    private val CREDENTIAL_PICKER_REQUEST = 1001

    private val phoneNumberHintLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val phoneNumber = Identity.getSignInClient(this)
                    .getPhoneNumberFromIntent(result.data)
                binding.etEmailMobile.setText(phoneNumber?.removePrefix("+91"))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginV2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // splashPresenter.getAuthConfig();
        val request = LocaleRequest.Locale()
        request.packageName = "in.woloo.www"
        request.platform = "android"

        val localeRequest = LocaleRequest()
        localeRequest.locale = request

        homeViewModel.getAppConfig(localeRequest)

        setProgressBar()
        setNetworkDetector()
        setLiveData()
        addTextWatchers()
        setClickables()
        setupSmartLoginConfig()
        generateKeyHash()
        requestHint()
    }

    private fun addTextWatchers() {
        binding.etEmailMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cs?.length!! >= 1) {
                    binding.etEmailMobile.setBackgroundResource(R.drawable.rounded_corner_button)
                    enableSubmitButton()
                } else {
                    binding.etEmailMobile.setBackgroundResource(R.drawable.rounded_white)
                    disableSubmitButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun generateKeyHash() {
        Handler(Looper.getMainLooper()).postDelayed({ keyhash() }, 5000)
    }

   /* private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val credentialsClient = Credentials.getClient(this)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            CREDENTIAL_PICKER_REQUEST,
            null, 0, 0, 0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CREDENTIAL_PICKER_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val credential = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    if (!TextUtils.isEmpty(credential?.id)) {
                        binding.etEmailMobile.setText(credential?.id?.removePrefix("+91"))
                    }
                }
        }
    }
*/

    private fun requestHint() {
        val request = GetPhoneNumberHintIntentRequest.builder().build()
        Identity.getSignInClient(this)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result ->
                phoneNumberHintLauncher.launch(IntentSenderRequest.Builder(result.intentSender).build())
            }
            .addOnFailureListener {
                // Handle failure if no phone number found or API unavailable
                Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show()
            }
    }

    private fun enableSubmitButton() {
        try {
            binding.txtSendOtp.background = resources.getDrawable(R.drawable.yellow_rectangle_shape)
            binding.txtSendOtp.setTextColor(resources.getColor(R.color.black))
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun disableSubmitButton() {
        try {
            binding.txtSendOtp.background = resources.getDrawable(R.drawable.rounded_corner_button)
            binding.txtSendOtp.setTextColor(resources.getColor(R.color.text_color_five))
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setupSmartLoginConfig() {
        config = SmartLoginConfig(this, this)
        config!!.facebookAppId = getString(R.string.facebook_app_id)
        config!!.facebookPermissions = null
        config!!.googleApiClient = null
    }

   /* private fun keyhash() {
        val info: PackageInfo
        try {
            info = this.packageManager
                .getPackageInfo("in.woloo.www", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Logger.e("hash key", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Logger.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Logger.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Logger.e("exception", e.toString())
        }
    }*/

    private fun keyhash() {
        try {
            val info = packageManager.getPackageInfo("in.woloo.www", PackageManager.GET_SIGNATURES)
            val signatures = info.signatures
            if (signatures != null) {
                for (signature in signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), Base64.DEFAULT))
                    Logger.e("hash key", hashKey)
                }
            } else {
                Logger.e("hash key", "Signatures are null")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.e("name not found", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Logger.e("no such algorithm", e.toString())
        } catch (e: Exception) {
            Logger.e("exception", e.toString())
        }
    }

    private fun setClickables() {
        binding.txtSendOtp.setOnClickListener {
            binding.etEmailMobile.text.removePrefix("+91")
            sendOtp()
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("privacy_policy", "https://woloo.in/privacy-policy/") // Changed By Aarati
            // intent.putExtra("privacy_policy","https://api.woloo.in/WolooTermsofUse.html")
            startActivity(intent)
        }
    }

    private fun sendOtp() {
        if (!TextUtils.isEmpty(binding.etEmailMobile.text.toString())) {
            if (TextUtils.isDigitsOnly(binding.etEmailMobile.text.toString())) {
                if (CommonUtils.isValidMobileNumber(binding.etEmailMobile.text.toString())) {
                    val bundle = Bundle()
                    Utility.logFirebaseMobileEvent(
                        this,
                        bundle,
                        AppConstants.MOBILE_OTP,
                        binding.txtSendOtp.text.toString()
                    )
                    val request = SendOtpRequest()
                    if (SharedPrefSettings.getPreferences.fetchReferralCode() != null) {
                        request.referralCode = SharedPrefSettings.getPreferences.fetchReferralCode().toString()
                    }
                    request.mobile = binding.etEmailMobile.text.toString().trim { it <= ' ' }
                    loginViewModel.sendOtp(request)
                } else {
                    showLoginFailureDialog("Please enter mobile number !")
                }
            } else {
                showLoginFailureDialog("Please enter mobile number !")
            }
        }
    }

    private fun setLiveData() {
        homeViewModel.observeAppConfig().observe(this) {
            if (it != null) {
                getPreferences.storeAuthConfig(it)
            }
        }
        loginViewModel.observeSendOtp().observe(
            this,
            Observer {
                Logger.i("Aarati Login", "in otp responseti")
                if (it != null) {
                    goToOtpVerification(it.data)
                }
            }
        )

        loginViewModel.otpMessage.observe(
            this,
            Observer { message ->
                message?.let {
                    Logger.i("Aarati Login", "in otp message $message")
                    if (it.contains("inactive", ignoreCase = true)) {
                        Logger.i("Aarati Login", "in otp message {$it")
                        showLoginFailureDialog(it)
                        loginViewModel.clearOtpMessage()
                    }
                }
            }
        )
    }

    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun goToOtpVerification(data: SendOtpResponse?) {
        if (data == null) return
        if (data.requestId.isNotEmpty()) {
            val intent = Intent(this, OtpVerificationActivity::class.java)
            intent.putExtra(IntentConstants.REQUEST_ID, data.requestId)
            intent.putExtra(
                IntentConstants.MOBILE_NO,
                binding.etEmailMobile.text.toString().trim { it <= ' ' }
            )
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        finish()
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

    override fun onLoginSuccess(user: SmartUser?) {
    }

    override fun onLoginFailure(e: SmartLoginException?) {
    }

    override fun doCustomLogin(): SmartUser {
        val user = SmartUser()
        user.email = ""
        return user
    }

    override fun doCustomSignup(): SmartUser {
        val user = SmartUser()
        user.email = ""
        return user
    }
}
