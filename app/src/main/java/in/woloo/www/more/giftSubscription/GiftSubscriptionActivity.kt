package `in`.woloo.www.more.giftSubscription

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.fragments.contacts.InviteContactsActivity
import `in`.woloo.www.more.giftSubscription.model.GetGiftPlansResponse
import `in`.woloo.www.more.giftSubscription.model.SendGiftCardResponse
import `in`.woloo.www.more.giftSubscription.mvp.GiftSubscriptionPresenter
import `in`.woloo.www.more.giftSubscription.mvp.GiftSubscriptionView
import `in`.woloo.www.utils.AppConstants

class GiftSubscriptionActivity : AppCompatActivity(), GiftSubscriptionView {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null

    @JvmField
    @BindView(R.id.frequency)
    var frequency: TextView? = null

    @JvmField
    @BindView(R.id.tv_price)
    var tv_price: TextView? = null

    @JvmField
    @BindView(R.id.tv_class)
    var tv_class: TextView? = null

    @JvmField
    @BindView(R.id.ll_subscription)
    var ll_subscription: LinearLayout? = null

    @JvmField
    @BindView(R.id.ll_Voucher)
    var ll_Voucher: LinearLayout? = null

    @JvmField
    @BindView(R.id.llParentLayout)
    var llParentLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.tvSubscriptionDetails)
    var tvSubscriptionDetails: TextView? = null

    @JvmField
    @BindView(R.id.ivSubscriptionMark)
    var ivSubscriptionMark: LinearLayout? = null

    @JvmField
    @BindView(R.id.tv_price_info)
    var tv_price_info: TextView? = null

    @JvmField
    @BindView(R.id.voucherNameTv)
    var voucherNameTv: TextView? = null

    @JvmField
    @BindView(R.id.description_Wb)
    var description_Wb: WebView? = null

    @JvmField
    @BindView(R.id.ll_expanded)
    var ll_expanded: LinearLayout? = null

    @JvmField
    @BindView(R.id.tv_startDate)
    var tv_startDate: TextView? = null

    @JvmField
    @BindView(R.id.tv_endDate)
    var tv_endDate: TextView? = null

    @JvmField
    @BindView(R.id.etAmount)
    var etAmount: EditText? = null

    @JvmField
    @BindView(R.id.etMobileNumber)
    var etMobileNumber: TextView? = null

    @JvmField
    @BindView(R.id.llsendButton)
    var llsendButton: TextView? = null

    @JvmField
    @BindView(R.id.etMessage)
    var etMessage: EditText? = null

    var giftSubscriptionPresenter: GiftSubscriptionPresenter? = null
    var price: String = "0"
    var commonUtils: CommonUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_subscription)
        ButterKnife.bind(this)
        commonUtils = CommonUtils()
        giftSubscriptionPresenter = GiftSubscriptionPresenter(this, this)
        llParentLayout!!.visibility = View.GONE
        initViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        mSharedPreference = SharedPreference(this)
        mSharedPreference!!.setStoredPreference(
            this,
            SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.getPreferenceKey(),
            ""
        )
        tvTitle!!.text = resources.getString(R.string.giftSubscription)
        ivBack!!.setOnClickListener { v: View? ->
            finish()
        }

        etMobileNumber!!.setOnClickListener { v: View? ->
            try {
                if (checkAndRequestPermissions()) {
                    ContactsLogs()
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }

        etMessage!!.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etMessage!!.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        llsendButton!!.setOnClickListener { v: View? ->
            if (isValidate) {
                giftSubscriptionPresenter!!.sendGiftCard(
                    etAmount!!.text.toString().trim { it <= ' ' },
                    etMessage!!.text.toString().trim { it <= ' ' },
                    etMobileNumber!!.text.toString().trim { it <= ' ' })
            }
        }

        giftSubscriptionPresenter!!.getGiftPlans()
    }

    private val isValidate: Boolean
        get() {
            try {
                //Logger.i(TAG, "isValidate");
                val mobileNumber =
                    etMobileNumber!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(
                        etAmount!!.text.toString().trim { it <= ' ' })
                ) {
                    Toast.makeText(
                        this.applicationContext,
                        "Please enter your name",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(
                        this.applicationContext,
                        "Please enter mobile number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (TextUtils.isEmpty(
                        etMessage!!.text.toString().trim { it <= ' ' })
                ) {
                    Toast.makeText(
                        this.applicationContext,
                        "Please enter message",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                val user =
                    CommonUtils().userInfo
                //LoginResponse userInfo = new CommonUtils().getUserInfo(this);
                if (mobileNumber == user!!.mobile) {
                    Toast.makeText(
                        this.applicationContext,
                        "You can not send Gift to YourSelf",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                /*if(userCoinsResponse != null && userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null && userCoinsResponse.getData().getTotalCoins() < Integer.parseInt(etAmount.getText().toString().trim())){
            //Toast.makeText(this.getApplicationContext(),"You don't have enough points",Toast.LENGTH_SHORT).show();
            return false;
        }
*/
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            return true
        }

    private fun checkAndRequestPermissions(): Boolean {
        // Logger.i(TAG, "checkAndRequestPermissions");
        val permissionReadContact =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        val permissionWriteContact =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionReadContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (permissionWriteContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun getGiftPlansResponse(getGiftPlansResponse: GetGiftPlansResponse?) {
        try {
            if (getGiftPlansResponse != null && getGiftPlansResponse.status == AppConstants.API_SUCCESS) {
                setData(getGiftPlansResponse)
            } else {
                CommonUtils.showCustomDialog(this, getGiftPlansResponse!!.message)
            }
        } catch (exception: Exception) {
        }
    }

    override fun sendGiftCardResponse(sendGiftCardResponse: SendGiftCardResponse?) {
        try {
            if (sendGiftCardResponse != null && sendGiftCardResponse.status == AppConstants.API_SUCCESS) {
                val userInfo = CommonUtils().userInfo
                var isEmail = false
                var email: String? = ""
                if (!TextUtils.isEmpty(userInfo!!.email)) {
                    isEmail = true
                    email = userInfo.email
                }
                CommonUtils.navigateToRazorPayFlow(
                    this,
                    etMobileNumber!!.text.toString().trim { it <= ' ' },
                    sendGiftCardResponse.orderId,
                    email,
                    isEmail,
                    userInfo.mobile,
                    false,
                    null,
                    false,
                    true
                )
            } else {
                CommonUtils.showCustomDialog(this, sendGiftCardResponse!!.message)
            }
        } catch (exception: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        //Logger.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    ContactsLogs()
                } else {
                    checkAndRequestPermissions()
                }
            }
        }
    }

    private fun ContactsLogs() {
        val i = Intent(
            this@GiftSubscriptionActivity,
            InviteContactsActivity::class.java
        )
        //i.putExtra("ARRAYLIST", jsonArray.toString());
        i.putExtra("isGiftSub", "")
        //i.putExtra(AppConstants.REFCODE, refcode);
        startActivityForResult(i, 999)
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun setData(data: GetGiftPlansResponse) {
        llParentLayout!!.visibility = View.VISIBLE
        if (data.data!![0].frequency != null) frequency!!.text = data.data[0].frequency
        /*else
                frequency.setText(data.getData().get(0).getFrequency() + " Days");*/
        tv_price!!.text = "\u20B9 " + data.data.get(0).price
        price = data.data[0].price.toString()
        tv_class!!.text = data.data[0].name
        //  tv_price_info.setText(data.getData().get(0).getDescription());
        description_Wb!!.setBackgroundColor(Color.TRANSPARENT)
        description_Wb!!.settings.domStorageEnabled = true
        //description_Wb.getSettings().setAppCacheEnabled(true); // commented By Aarati
        description_Wb!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // Added by Aarati
        description_Wb!!.settings.loadsImagesAutomatically = true
        description_Wb!!.settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        description_Wb!!.settings.javaScriptEnabled = true
        description_Wb!!.loadDataWithBaseURL(
            null,
            data.data.get(0).description.toString(),
            "text/html",
            "UTF-8",
            null
        )

        //description_Wb.loadData(data.getData().get(0).getDescription(), "text/html", "UTF-8");
        tvSubscriptionDetails!!.text =
            ("Buy Now " + data.data[0].frequency).toString() + " Membership"
        if (data.data.get(0).frequency.equals("Monthly", ignoreCase = true)) {
            ll_subscription!!.setBackgroundResource(R.drawable.ic_monthly_subscription_bg)
        } else if (data.data.get(0).frequency.equals("Quarterly", ignoreCase = true)) {
            ll_subscription!!.setBackgroundResource(R.drawable.ic_path_silver)
        } else if (data.data.get(0).frequency.equals("Half-yearly", ignoreCase = true)) {
            ll_subscription!!.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg)
        } else if (data.data.get(0).frequency.equals("Yearly", ignoreCase = true)) {
            ll_subscription!!.setBackgroundResource(R.drawable.ic_path_silver)
        }
        tv_startDate!!.text = data.data[0].start_at
        tv_endDate!!.text = data.data[0].end_at
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        commonUtils!!.hideProgress()
        try {
            if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
                etMobileNumber!!.text = data!!.getStringExtra("mobilenumber")
                val totalNumber = data.getStringExtra("totalNumbers")!!.toInt()
                val prize = price.toInt()
                llsendButton!!.text =
                    "Pay \u20B9 " + totalNumber * prize + " (" + totalNumber + " x " + prize + ")"
            }
        } catch (exception: Exception) {
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 85
        protected var mSharedPreference: SharedPreference? = null
    }
}