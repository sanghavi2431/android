package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Base64
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
/*import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest*/
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.SendOtpResponse
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityLoginV2Binding
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.api_classes.IntentConstants
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseActivity() {

    private lateinit var wolooViewModel: WolooViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityLoginV2Binding

   // private var config: SmartLoginConfig? = null


    private val callbackManager: CallbackManager? = null
    private val CREDENTIAL_PICKER_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginV2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        wolooViewModel = ViewModelProvider(this)[WolooViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

      /*  val drawable: Drawable = binding.ivLogo.drawable
        if (drawable != null) {
            val imageWidth = drawable.intrinsicWidth - 70
            val imageHeight = drawable.intrinsicHeight

            // Set the text size to match the image height
            binding.logoText.setTextSize(TypedValue.COMPLEX_UNIT_PX, imageHeight.toFloat())
        }*/

        //splashPresenter.getAuthConfig();
        val request = LocaleRequest.Locale()
        request.packageName = "in.woloo.www"
        request.platform = "android"

        val localeRequest = LocaleRequest()
        localeRequest.locale = request

        homeViewModel.getAppConfig(localeRequest)

       /* val textPT = "Please read our " +
                "<a href='https://api.woloo.in/WolooTermsofUse.html'><u>Terms &amp; Conditions</u></a> and our " +
                "<a href='https://woloo.in/privacy-policy/'><u>Privacy Policy</u></a>"

        binding.privacyTermsText.text = Html.fromHtml(textPT, Html.FROM_HTML_MODE_LEGACY)
        binding.privacyTermsText.movementMethod = LinkMovementMethod.getInstance()
*/

        val textPT = "Please read our Terms & Conditions and our Privacy Policy"
        val spannable = SpannableString(textPT)

// Adding clickable links using URLSpan
        val start1 = textPT.indexOf("Terms & Conditions")
        val end1 = start1 + "Terms & Conditions".length
        spannable.setSpan(URLSpan("https://woloo.in/terms-condition/"), start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val start2 = textPT.indexOf("Privacy Policy")
        val end2 = start2 + "Privacy Policy".length
        spannable.setSpan(URLSpan("https://woloo.in/privacy-policy/"), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

// Changing link color with ForegroundColorSpan
        spannable.setSpan(ForegroundColorSpan(getColor(R.color.chip_color)), start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(getColor(R.color.chip_color)), start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.privacyTermsText.text = spannable
        binding.privacyTermsText.movementMethod = LinkMovementMethod.getInstance()


        setProgressBar()
        setNetworkDetector()
        setLiveData()
        addTextWatchers()
        setClickables()
        //setupSmartLoginConfig()
        generateKeyHash()
        //requestHint()
    }

    private fun addTextWatchers() {
        binding.etEmailMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (cs?.length!! >= 1) {
                   // binding.etEmailMobile.setBackgroundResource(R.drawable.rounded_corner_button)
                    enableSubmitButton()
                } else {
                   // binding.etEmailMobile.setBackgroundResource(R.drawable.rounded_white)
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

    private fun enableSubmitButton() {
        try {
            //binding.txtSendOtp.background = resources.getDrawable(R.drawable.yellow_rectangle_shape)
          //  binding.txtSendOtp.setTextColor(resources.getColor(R.color.black))
        } catch (ex: java.lang.Exception) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private fun disableSubmitButton() {
        try {
         //   binding.txtSendOtp.background = resources.getDrawable(R.drawable.rounded_corner_button)
           // binding.txtSendOtp.setTextColor(resources.getColor(R.color.text_color_five))
        } catch (ex: java.lang.Exception) {
             CommonUtils.printStackTrace(ex);
        }
    }



    private fun keyhash() {
        val info: PackageInfo
        try {
            info = this.packageManager
                .getPackageInfo("in.woloo.www", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures!!) {
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
    }

    private fun setClickables() {
        binding.txtSendOtp.setOnClickListener {
           binding.txtSendOtp.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
            sendOtp()
        }

       /* binding.tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("privacy_policy","https://woloo.in/privacy-policy/") // Changed By Aarati
            //intent.putExtra("privacy_policy","https://api.woloo.in/WolooTermsofUse.html")
            startActivity(intent)
        }*/
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
                    val request = SendOtpRequest(binding.etEmailMobile.text.toString().trim() , SharedPrefSettings.getPreferences.fetchReferralCode().toString())
                   /* if(SharedPrefSettings.getPreferences.fetchReferralCode()!= null) {
                        request.referralCode = SharedPrefSettings.getPreferences.fetchReferralCode().toString()
                    }
                    request.mobile = binding.etEmailMobile.text.toString().trim { it <= ' ' }*/
                    wolooViewModel.sendOtp(request)
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
        wolooViewModel.observeSendOtp().observe(this, Observer {
            if(it != null){
                goToOtpVerification(it.data)
            }
        })
    }

    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this)[WolooViewModel::class.java]
    }

    private fun goToOtpVerification(data: SendOtpResponse?) {
        if (data == null) return
        if (data.requestId.isNotEmpty()) {
            val intent = Intent(this, OtpVerificationActivity::class.java)
            intent.putExtra(IntentConstants.REQUEST_ID, data.requestId)
            intent.putExtra(
                IntentConstants.MOBILE_NO,
                binding.etEmailMobile.text.toString().trim { it <= ' ' })
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


}