package `in`.woloo.www.more.subscribe.razorpay

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.mSharedPreference
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.giftcard.mvp.GiftCardView
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.more.subscribe.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.more.subscribe.subscription.viewmodel.SubscriptionViewModel
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject

class RazorPayActivity : AppCompatActivity(), PaymentResultWithDataListener , RazorPayView{
    private var planId: String? = null
    private var subscriptionId: String? = null

    //    private RazorPayPresenter razorPayPresenter;
    var subscriptionViewModel: SubscriptionViewModel? = null
    private var msg: String? = null
    private var isEmail = false
    private var mobile: String? = null
    private var order_id: String? = null
    private var amount: String? = null
    var giftCardView: GiftCardView? = null
    private var wolooViewModel: WolooViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_razor_pay)
        ButterKnife.bind(this)
        Checkout.preload(applicationContext)
        planId = intent.getStringExtra(AppConstants.PLAN_ID)
        if (intent.hasExtra(AppConstants.SUBSCRIPTION_ID)) subscriptionId = intent.getStringExtra(
            AppConstants.SUBSCRIPTION_ID
        )
        else {
            order_id = intent.getStringExtra(AppConstants.ORDER_ID)
            amount = intent.getStringExtra(AppConstants.ORDER_AMOUNT)
            // giftCardView = (GiftCardView) getIntent().getSerializableExtra(AppConstants.GIFT_CARD_VIEW_PRESENTER);
        }
        msg = intent.getStringExtra(AppConstants.MSG)
        isEmail = intent.getBooleanExtra(AppConstants.IS_EMAIL, false)
        mobile = intent.getStringExtra(AppConstants.MOBILE)
        //        razorPayPresenter = new RazorPayPresenter(this, RazorPayActivity.this);
        subscriptionViewModel = ViewModelProvider(this).get<SubscriptionViewModel>(
            SubscriptionViewModel::class.java
        )
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )
        startPayment()
        setLiveData()
    }

    fun setLiveData() {
        wolooViewModel?.observeAddCoinstoWolooUser()
            ?.observe(this, Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
                showSuccessDialog()
            })
        subscriptionViewModel!!.observeSubmitSubscriptionPurchaseResponse().observe(
            this,
            Observer<BaseResponse<InitSubscriptionResponse>> { it ->
                try {
                    if (it != null && it.success) {
                        i(
                            TAG,
                            "onPaymentSuccess: line 161 "
                        )
                        showPaymentSuccessDialog()
                    } else {
                        WolooApplication.errorMessage = ""
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            })
    }

    fun startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val activity: Activity = this

        val co = Checkout()

        try {
            val commonUtils = CommonUtils()
            val authConfigResponse = CommonUtils.authconfig_response(this)
            if (authConfigResponse != null && authConfigResponse != null && authConfigResponse.getrZCRED() != null) {
                if (!TextUtils.isEmpty(authConfigResponse.getrZCRED()!!.key)) {
                    co.setKeyID(authConfigResponse.getrZCRED()!!.key)
                }
            }
            val options = JSONObject()
            options.put("name", "Woloo")

            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR")
            if (intent.hasExtra(AppConstants.SUBSCRIPTION_ID)) {
                options.put("order_id", subscriptionId)
                options.put("description", "Woloo Pee’rs Club Membership")
            } else {
                options.put("description", "Send Points")
                options.put("order_id", order_id)
                options.put("amount", amount)
            }
            val preFill = JSONObject()
            preFill.put("email", msg)
            preFill.put("contact", mobile)
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            CommonUtils.printStackTrace(e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String, paymentData: PaymentData) {
        try {
            i(
                TAG,
                "onPaymentSuccess: razorpayPaymentID : $razorpayPaymentID"
            )
            i(TAG, "onPaymentSuccess: OrderId : " + paymentData.orderId)
            i(TAG, "onPaymentSuccess: Signature : " + paymentData.signature)

            if (intent.hasExtra(AppConstants.isGiftSub)) {
                i(TAG, "onPaymentSuccess: line 117 ")
                try {
                    val bundle = Bundle()
                    if (intent.hasExtra(AppConstants.gift_numbers)) bundle.putString(
                        AppConstants.SENT_GIFT_NUMBERS, intent.getStringExtra(
                            AppConstants.gift_numbers
                        )
                    )
                    i(
                        TAG,
                        "onPaymentSuccess: line 122 $bundle"
                    )
                    logFirebaseEvent(
                        this@RazorPayActivity,
                        bundle,
                        AppConstants.GIFT_SUBSCRIPTION_COMPLETED
                    )
                } catch (ex: Exception) {
                }
            }

            if (intent.hasExtra(AppConstants.SUBSCRIPTION_ID)) {
                i(TAG, "onPaymentSuccess: line 131 ")
                var isFutureSubscription = false
                if (intent.hasExtra(AppConstants.FUTURE_SUBSCRIPTION)) isFutureSubscription = true
                val request: SubmitSubscriptionPurchaseRequest = SubmitSubscriptionPurchaseRequest()
                request.paymentId = razorpayPaymentID
             //   request.subscriptionId = subscriptionId
                request.planId = planId!!
                request.paymentSignature = paymentData.signature
                    request.orderId = paymentData.orderId
                    request.future = isFutureSubscription
                i(
                    TAG,
                    "onPaymentSuccess: line 161 "
                )
                showPaymentSuccessDialog()
                val request1 = CreditCoinsRequest(
                    coins = CommonUtils.authconfig_response(this).getWolooSubscriptionPoints()!!.toInt(),
                    remarks = AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK,
                    type = AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK,
                    isGift = 0,
                    blogId = 0
                )
                wolooViewModel!!.addCoinstoWolooUser(request1)
               // subscriptionViewModel!!.submitSubscriptionPurchase(request)
                //                razorPayPresenter.submitSubscriptionPurchase(planId, razorpayPaymentID, paymentData.getSignature(), subscriptionId, isFutureSubscription);
            } else {
                showPaymentSuccessDialog()
                i(TAG, "onPaymentSuccess: line 139 ")
            }
        } catch (e: Exception) {
            e(TAG, "Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentError(code: Int, response: String, paymentData: PaymentData) {
        try {
//            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Toast.makeText(applicationContext, "Payment failed!", Toast.LENGTH_SHORT).show()
            this@RazorPayActivity.finish()
        } catch (e: Exception) {
            e(TAG, "Exception in onPaymentError", e)
        }
    }

    override fun onPaymentSuccess() {
        try{
            Logger.i(TAG, "onPaymentSuccess: line 161 ");
            showPaymentSuccessDialog();

        }catch(e:Exception){
            CommonUtils.printStackTrace(e);
        }
    }

    fun showPaymentSuccessDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_payment_success)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView

            val tv_paymentsuccess = dialog.findViewById<View>(R.id.tv_paymentsuccess) as TextView

            val authConfigResponse = CommonUtils.authconfig_response(this@RazorPayActivity)
            if (authConfigResponse != null) {
                val paymentSuccessDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()!!.paymentSuccessDialogText
                tv_paymentsuccess.text = paymentSuccessDialogText!!.replace("\\\\n".toRegex(), "\n")
            }


            var bundle = Bundle()
            var payload = HashMap<String, Any>()
            bundle.putString(AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK, planId)
            bundle.putString(AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK, planId)
            payload[AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK] = planId.toString()
            payload[AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK] = planId.toString()
            logFirebaseEvent(this, bundle, AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK)
            logNetcoreEvent( this, payload, AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) dialog.dismiss()
                //onBackPressed();
                if (intent.hasExtra(AppConstants.SUBSCRIPTION_ID)) {
                    /*Intent i = new Intent(RazorPayActivity.this, WolooDashboard.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);*/
                    this@RazorPayActivity.finish()
                }
                else if (intent.hasExtra(AppConstants.isGiftSub)) {
                    val i = Intent(
                        this@RazorPayActivity,
                        WolooDashboard::class.java
                    )
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)
                    this@RazorPayActivity.finish()
                }
                else if(intent.hasExtra(AppConstants.PAYPERUSE))
                {
                    val wolooId = intent.getStringExtra(AppConstants.WOLOO_ID_Pay)
                    mSharedPreference?.setStoredPreference(
                        this,
                        SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                        wolooId
                    )
                    mSharedPreference!!.setStoredPreference(
                        this,
                        SharedPreferencesEnum.POWDER_ROOM_CODE.getPreferenceKey(),
                        ""
                    )
                    startActivity(
                        Intent(
                            this@RazorPayActivity,
                            AddReviewActivity::class.java
                        )
                    )
                }
                    else {
                    this@RazorPayActivity.finish()
                    //   giftCardView.pointsAddedResponseSuccess();
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showSuccessDialog()
    {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            btnShopDialog.visibility = View.GONE
            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned ${CommonUtils.authconfig_response(this).getWolooSubscriptionPoints()!!.toInt()} Woloo Points!")

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

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

    companion object {
        private val TAG: String = RazorPayActivity::class.java.simpleName
    }


}