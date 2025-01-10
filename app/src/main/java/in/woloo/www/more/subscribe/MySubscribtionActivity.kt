package `in`.woloo.www.more.subscribe

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewStub
import android.view.Window
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.cancelsubscription.model.CancelSubscriptionResponse
import `in`.woloo.www.more.cancelsubscription.mvp.CancelSubscriptionPresenter
import `in`.woloo.www.more.cancelsubscription.mvp.CancelSubscriptionView
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.subscribe.models.GetSubscriptionDetailsResponse
import `in`.woloo.www.more.subscribe.subscription.viewmodel.SubscriptionViewModel
import `in`.woloo.www.utils.AppConstants
import java.util.Locale

class MySubscribtionActivity : AppCompatActivity(), CancelSubscriptionView {
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
    @BindView(R.id.future_description_Wb)
    var future_description_Wb: WebView? = null

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
    @BindView(R.id.future_frequency)
    var future_frequency: TextView? = null

    @JvmField
    @BindView(R.id.purchasedByTv)
    var purchasedByTv: TextView? = null

    @JvmField
    @BindView(R.id.future_tv_price)
    var future_tv_price: TextView? = null

    @JvmField
    @BindView(R.id.future_tv_class)
    var future_tv_class: TextView? = null

    @JvmField
    @BindView(R.id.future_ll_subscription)
    var future_ll_subscription: LinearLayout? = null

    @JvmField
    @BindView(R.id.future_llParentLayout)
    var future_llParentLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.future_tvSubscriptionDetails)
    var future_tvSubscriptionDetails: TextView? = null

    @JvmField
    @BindView(R.id.futureSubscriptionMark)
    var future_ivSubscriptionMark: LinearLayout? = null

    @JvmField
    @BindView(R.id.ll_nodatafound)
    var ll_nodatafound: LinearLayout? = null

    @JvmField
    @BindView(R.id.appLogo_tv)
    var appLogo_tv: ImageView? = null

    @JvmField
    @BindView(R.id.voucher_Logo_tv)
    var voucher_Logo_tv: ImageView? = null

    @JvmField
    @BindView(R.id.future_tv_price_info)
    var future_tv_price_info: TextView? = null

    @JvmField
    @BindView(R.id.voucherTV)
    var voucherTV: TextView? = null

    @JvmField
    @BindView(R.id.future_ll_expanded)
    var future_ll_expanded: LinearLayout? = null

    @JvmField
    @BindView(R.id.future_tv_startDate)
    var future_tv_startDate: TextView? = null

    @JvmField
    @BindView(R.id.future_tv_endDate)
    var future_tv_endDate: TextView? = null

    @JvmField
    @BindView(R.id.tv_cancelSubscription)
    var tv_cancelSubscription: TextView? = null
    var cancelSubscriptionPresenter: CancelSubscriptionPresenter? = null
    var subscriptionViewModel: SubscriptionViewModel? = null
    private var userProfileResponse: UserProfile? = null
    var expanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_subscribtion)
        ButterKnife.bind(this)
        cancelSubscriptionPresenter = CancelSubscriptionPresenter(this, this)
        initViews()
        setLiveData()
    }

    private fun initViews() {
        //tvTitle.setText(getResources().getString(R.string.mysubscription));
        tvTitle!!.text = "My Subscription"
        ivBack!!.setOnClickListener { v: View? -> finish() }
        if (intent.hasExtra("plan")) {
            val intent = this.intent
            val bundle = intent.extras
            userProfileResponse = getIntent().getSerializableExtra("plan") as UserProfile?
            if (userProfileResponse != null) {
                if (userProfileResponse!!.profile!!.voucherId != null || userProfileResponse!!.profile!!.subscriptionId != null || userProfileResponse!!.profile!!.giftSubscriptionId == null) tv_cancelSubscription!!.visibility =
                    View.VISIBLE else tv_cancelSubscription!!.visibility = View.GONE
            } else tv_cancelSubscription!!.visibility = View.VISIBLE
            subscriptionViewModel = ViewModelProvider(this).get<SubscriptionViewModel>(
                SubscriptionViewModel::class.java
            )
            if (userProfileResponse!!.profile != null && !TextUtils.isEmpty(
                    userProfileResponse!!.profile!!.expiryDate
                ) && CommonUtils.isSubscriptionExpired(
                    userProfileResponse!!.profile!!.expiryDate
                )
            ) {
                ll_nodatafound!!.visibility = View.VISIBLE
            } else {
                ll_nodatafound!!.visibility = View.GONE
                //                subscriptionViewModel.getMySubscription();
            }
            subscriptionViewModel!!.getMySubscription()
        }
    }

    fun setLiveData() {
        subscriptionViewModel!!.observeMySubscription().observe(this, Observer<BaseResponse<GetSubscriptionDetailsResponse.Data>> { response ->
            response?.let {
                if (it.data?.activeSubscription == null) {
                    ll_nodatafound!!.visibility = View.VISIBLE
                    // showdialog(getSubscriptionDetailsResponse.message, true)
                } else {
                    setData(it.data!!)
                }
            } ?: run {
                // displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }
        })

    }

    @get:SuppressLint("ClickableViewAccessibility")
    private val reasonPopup: Unit
        private get() {
            try {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setContentView(R.layout.dialog_cancel_subscription_reason)
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                val etRemark = dialog.findViewById<EditText>(R.id.etRemark)
                etRemark.setOnTouchListener(OnTouchListener { v, event ->
                    if (etRemark.hasFocus()) {
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
                val spinner = dialog.findViewById<View>(R.id.cancel_reason_spinner) as Spinner
                val mSharedPref = SharedPreference(this)
                val authConfigResponse = getPreferences.fetchAuthConfig()
                val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item,
                    authConfigResponse!!.getcUSTOMMESSAGE()!!.cancelSubscriptionReasons!!.split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                val llCancelBtn = dialog.findViewById<TextView>(R.id.llCancelBtn)
                llCancelBtn.setOnClickListener { v: View? -> dialog.dismiss() }
                val llOkayBtn = dialog.findViewById<TextView>(R.id.llOkayBtn)
                llOkayBtn.setOnClickListener {
                    if (etRemark.text.toString().trim { it <= ' ' } == "") {
                        Toast.makeText(
                            this@MySubscribtionActivity,
                            "Please Add Comment",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        dialog.dismiss()
                        cancelSubscriptionPresenter!!.cancelSubscription(
                            authConfigResponse.getcUSTOMMESSAGE()!!.cancelSubscriptionReasons!!.split(
                                ",".toRegex()
                            ).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[spinner.selectedItemPosition],
                            etRemark.text.toString().trim { it <= ' ' },
                            authConfigResponse.supportEmail
                        )
                    }
                }
                dialog.show()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }

    override fun cancelSubscriptionResponse(cancelSubscriptionResponse: CancelSubscriptionResponse?) {
        showdialog("Membership Cancelled Successfully", true)
    }

    fun showdialog(msg: String?, isCloseActivity: Boolean) {
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
                    if (isCloseActivity) onBackPressed()
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun setData(data: GetSubscriptionDetailsResponse.Data) {
        setViewData(data)
        ll_nodatafound!!.visibility = View.GONE
        llParentLayout!!.visibility = View.VISIBLE
        val activeSubscription: GetSubscriptionDetailsResponse.ActiveSubscription? = data.activeSubscription
        val futureSubscription = data.futureSubscription
        if (activeSubscription != null) {
            val cardView: View
            val viewStub = findViewById<ViewStub>(R.id.vsCardView)
            if (activeSubscription.frequency != null && activeSubscription.frequency
                    .equals("annual", ignoreCase = true)
            ) {
                // inflate gold card
                viewStub.layoutResource = R.layout.item_gold_membership
            } else {
                // inflate silver card
                viewStub.layoutResource = R.layout.item_silver_membership
            }
            cardView = viewStub.inflate()
            cardView.findViewById<View>(R.id.tv_upgrade).visibility = View.GONE
            var currencySymbol = ""
            if (!TextUtils.isEmpty(activeSubscription.currency)) {
                currencySymbol =
                    if (activeSubscription.currency.equals("INR", ignoreCase = true)) {
                        "\u20B9"
                    } else {
                        activeSubscription.currency.toString()
                    }
            }
            (cardView.findViewById<View>(R.id.tv_price) as TextView).text =
                currencySymbol + activeSubscription.price
            if (userProfileResponse != null && userProfileResponse != null) {
                val tvUserName = cardView.findViewById<TextView>(R.id.tvName)
                if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.name)) {
                    tvUserName.text = userProfileResponse!!.profile!!.name
                } else {
                    tvUserName.text = "Guest"
                }
            }
            val tv_startDate = cardView.findViewById<TextView>(R.id.tvStartDate)
            val tv_endDate = cardView.findViewById<TextView>(R.id.tvEndDate)
            val tvBeforeDiscount = cardView.findViewById<TextView>(R.id.tvBeforeDiscount)
            tv_startDate.text = "START DATE : " + activeSubscription.start_at
            tv_endDate.text = "END DATE : " + activeSubscription.end_at
            val tvDays = cardView.findViewById<TextView>(R.id.frequency)
            tvDays.text =
                if (activeSubscription.frequency == null) "" else activeSubscription.frequency
            val tvPlanName = cardView.findViewById<TextView>(R.id.tvPlan)
            tvPlanName.text =
                if (activeSubscription.frequency == null) "" else activeSubscription.frequency
                    .uppercase(
                        Locale.getDefault()
                    ) + tvPlanName.text
            try {
                val priceStr: String = activeSubscription.price.toString()
                val beforeDiscountPrice: Int = activeSubscription.beforeDiscountPrice!!.toInt()
                if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                    val price = priceStr.toInt()
                    if (price < beforeDiscountPrice) {
                        tvBeforeDiscount.text = beforeDiscountPrice.toString()
                        tvBeforeDiscount.visibility = View.VISIBLE
                        tvBeforeDiscount.paintFlags =
                            tvBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        tvBeforeDiscount.visibility = View.INVISIBLE
                    }
                } else {
                    tvBeforeDiscount.visibility = View.INVISIBLE
                }
            } catch (e: Exception) {
                tvBeforeDiscount.visibility = View.INVISIBLE
            }
        }
        if (futureSubscription != null) {
            for (i in futureSubscription.indices) {
                var cardView: View
                //  ViewStub viewStub = findViewById(R.id.vsFutureCardView);
                val parentLayout =
                    findViewById<LinearLayout>(R.id.viewstubs_layout) // Replace with your actual parent layout ID

// 2. Create a new ViewStub
                val viewStub = ViewStub(this)
                if (futureSubscription[i].frequency != null && futureSubscription[i].frequency.equals(
                        "annual",
                        ignoreCase = true
                    )
                ) {
                    // inflate gold card
                    viewStub.layoutResource = R.layout.item_gold_membership
                } else {
                    // inflate silver card
                    viewStub.layoutResource = R.layout.item_silver_membership
                }
                parentLayout.addView(viewStub)
                cardView = viewStub.inflate()
                cardView.findViewById<View>(R.id.tv_upgrade).visibility = View.GONE
                var currencySymbol = ""
                if (!TextUtils.isEmpty(futureSubscription[i].currency)) {
                    currencySymbol =
                        if (futureSubscription[i].currency.equals("INR", ignoreCase = true)) {
                            "\u20B9"
                        } else {
                            futureSubscription[i].currency.toString()
                        }
                }
                (cardView.findViewById<View>(R.id.tv_price) as TextView).text =
                    currencySymbol + futureSubscription[i].price
                cardView.findViewById<View>(R.id.ivSubscriptionMark).visibility = View.VISIBLE
                (cardView.findViewById<View>(R.id.tvSubscriptionTitle) as TextView).text = "Future"
                if (userProfileResponse != null && userProfileResponse != null) {
                    val tvUserName = cardView.findViewById<TextView>(R.id.tvName)
                    if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.name)) {
                        tvUserName.text = userProfileResponse!!.profile!!.name
                    } else {
                        tvUserName.text = "Guest"
                    }
                }
                val tv_startDate = cardView.findViewById<TextView>(R.id.tvStartDate)
                val tv_endDate = cardView.findViewById<TextView>(R.id.tvEndDate)
                val tvBeforeDiscount = cardView.findViewById<TextView>(R.id.tvBeforeDiscount)
                val tvSubscriptionDetails =
                    cardView.findViewById<TextView>(R.id.tvSubscriptionDetails)
                tv_startDate.text = "START DATE : " + futureSubscription[i].start_at
                tv_endDate.text = "END DATE : " + futureSubscription[i].end_at
                val tvDays = cardView.findViewById<TextView>(R.id.frequency)
                tvDays.text =
                    if (futureSubscription[i].frequency == null) "" else futureSubscription[i].frequency
                val tvPlanName = cardView.findViewById<TextView>(R.id.tvPlan)
                tvPlanName.text =
                    if (futureSubscription[i].frequency == null) "" else futureSubscription[i].frequency!!.uppercase(
                        Locale.getDefault()
                    ) + tvPlanName.text
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvSubscriptionDetails.text = Html.fromHtml(
                            futureSubscription[i].description,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    } else {
                        tvSubscriptionDetails.text =
                            Html.fromHtml(futureSubscription[i].description)
                    }
                    val priceStr = futureSubscription[i].price
                    val beforeDiscountPrice = futureSubscription[i].beforeDiscountPrice
                    if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                        val price = priceStr!!.toInt()
                        if (price < beforeDiscountPrice) {
                            tvBeforeDiscount.text = beforeDiscountPrice.toString()
                            tvBeforeDiscount.visibility = View.VISIBLE
                            tvBeforeDiscount.paintFlags =
                                tvBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        } else {
                            tvBeforeDiscount.visibility = View.INVISIBLE
                        }
                    } else {
                        tvBeforeDiscount.visibility = View.INVISIBLE
                    }
                } catch (e: Exception) {
                    tvBeforeDiscount.visibility = View.INVISIBLE
                }
            }
        }
        /*if (data.getActiveSubscription().getName().equals("FREE TRIAL")) {
            ll_subscription.setVisibility(View.GONE);
            ll_Voucher.setVisibility(View.VISIBLE);
            voucherNameTv.setText("Free Trial");
            voucher_Logo_tv.setVisibility(View.VISIBLE);
            voucherTV.setVisibility(View.GONE);
            frequency.setVisibility(View.INVISIBLE);
        }
        tv_class.setText(data.getActiveSubscription().getName());
        if (data.getActiveSubscription().getIsVoucher() == 1) {
            ll_subscription.setVisibility(View.GONE);
            ll_Voucher.setVisibility(View.VISIBLE);
            voucher_Logo_tv.setVisibility(View.GONE);
            voucherTV.setVisibility(View.VISIBLE);
            voucherNameTv.setText(data.getActiveSubscription().getName());
            voucherTV.setText(data.getActiveSubscription().getCorporate_name());
        } else {
            ll_subscription.setVisibility(View.VISIBLE);
            ll_Voucher.setVisibility(View.GONE);
        }
        if (data.getActiveSubscription().getGifted_by() != null) {
            purchasedByTv.setVisibility(View.VISIBLE);
            purchasedByTv.setText("Gift from " + data.getActiveSubscription().getGifted_by());
        } else {
            purchasedByTv.setVisibility(View.GONE);
        }

        if (data.getActiveSubscription().frequency != null)
            frequency.setText(data.getActiveSubscription().getFrequency());
       */
        /* else
            future_frequency.setText(data.getActiveSubscription().frequency + " Days");*/
        /*
        tv_price.setText("\u20B9 " + data.getActiveSubscription().getPrice());
        //tv_price_info.setText(data.getActiveSubscription().getDescription());
        description_Wb.setBackgroundColor(Color.TRANSPARENT);
        description_Wb.getSettings().setDomStorageEnabled(true);
        description_Wb.getSettings().setAppCacheEnabled(true);
        description_Wb.getSettings().setLoadsImagesAutomatically(true);
        description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        description_Wb.getSettings().setJavaScriptEnabled(true);
        description_Wb.loadDataWithBaseURL(null, data.getActiveSubscription().getDescription(), "text/html", "UTF-8", null);
        tvSubscriptionDetails.setText("Buy Now " + data.getActiveSubscription().frequency + " Membership");
        if (data.getActiveSubscription().frequency.equalsIgnoreCase("Monthly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
        } else if (data.getActiveSubscription().frequency.equalsIgnoreCase("Quarterly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        } else if (data.getActiveSubscription().frequency.equalsIgnoreCase("Half-yearly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
        } else if (data.getActiveSubscription().frequency.equalsIgnoreCase("Yearly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        }
        String startDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(c.getTime());
        c.add(Calendar.DATE, 30);
        Date expDate = c.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String endDate = formatter.format(expDate);


        tv_startDate.setText(data.getActiveSubscription().getStart_at());
        tv_endDate.setText(data.getActiveSubscription().getEnd_at());
        llParentLayout.setOnClickListener(v -> {
            ll_expanded.setVisibility(View.VISIBLE);
            future_ll_expanded.setVisibility(View.GONE);
        });
*/
//        if (userProfileMergedResponse.getData().getIsFutureSubcriptionExist().equals("true")) {
//            future_llParentLayout.setVisibility(View.VISIBLE);
//            if (data.getFutureSubscription().frequency != null)
//                future_frequency.setText(data.getFutureSubscription().getFrequency());
//            else
//                future_frequency.setText(data.getFutureSubscription().frequency + " Days");
//            future_tv_price.setText("\u20B9 " + data.getFutureSubscription().getPrice());
//            future_tv_class.setText(data.getFutureSubscription().getName());
//            //  future_tv_price_info.setText(data.getFutureSubscription().getDescription());
//            future_description_Wb.setBackgroundColor(Color.TRANSPARENT);
//            future_description_Wb.getSettings().setDomStorageEnabled(true);
//            future_description_Wb.getSettings().setAppCacheEnabled(true);
//            future_description_Wb.getSettings().setLoadsImagesAutomatically(true);
//            future_description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            future_description_Wb.getSettings().setJavaScriptEnabled(true);
//            future_description_Wb.loadDataWithBaseURL(null, data.getFutureSubscription().getDescription(), "text/html", "UTF-8", null);
//            //future_description_Wb.loadData(data.getFutureSubscription().getDescription(), "text/html", "UTF-8");
//
//            String future_startDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//
//            Calendar future_c = Calendar.getInstance();
//            future_c.setTime(future_c.getTime());
//            future_c.add(Calendar.DATE, 30);
//            Date future_expDate = future_c.getTime();
//            DateFormat future_formatter = new SimpleDateFormat("dd/MM/yyyy");
//            String future_endDate = future_formatter.format(future_expDate);
//
//            future_tvSubscriptionDetails.setText("Buy Now " + data.getFutureSubscription().frequency + " Membership");
//            if (data.getFutureSubscription().frequency.equalsIgnoreCase("Monthly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
//            } else if (data.getFutureSubscription().frequency.equalsIgnoreCase("Quarterly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
//            } else if (data.getFutureSubscription().frequency.equalsIgnoreCase("Half-yearly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
//            } else if (data.getFutureSubscription().frequency.equalsIgnoreCase("Yearly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
//            }
//            future_tv_startDate.setText(data.getFutureSubscription().getStart_at());
//            future_tv_endDate.setText(data.getFutureSubscription().getEnd_at());
//            future_llParentLayout.setOnClickListener(v -> {
//                ll_expanded.setVisibility(View.GONE);
//                future_ll_expanded.setVisibility(View.VISIBLE);
//            });
//            future_ll_expanded.setVisibility(View.GONE);
//        } else
//            future_llParentLayout.setVisibility(View.GONE);
    }

    private fun setViewData(data: GetSubscriptionDetailsResponse.Data) {
        tv_cancelSubscription!!.setOnClickListener { v: View? ->
            var purchasedBy = ""
            if (data.purchase_by != null) purchasedBy = data.purchase_by
            if (userProfileResponse!!.planData!!.isIs_cancel) {
                showdialog("You have already Canceled the Membership", false)
            } else if ((data.activeSubscription!!.name == "FREE TRIAL")) {
                showdialog("You don't have an active membership", false)
            } else if ((purchasedBy == AppConstants.PURCHASE_BY_APPLE)) {
                showdialog(AppConstants.PURCHASE_BY_APPLE_MSG, false)
            } else {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.dialog_discontinue_subscription)
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                val discontinueTextTv = dialog.findViewById<TextView>(R.id.discontinueTextTv)
                discontinueTextTv.text =
                    "Even if you choose to cancel your current membership, you can still use the app benefits until the end of current billing period."
                val ll_keepMyMembership =
                    dialog.findViewById<View>(R.id.ll_keep_my_membership) as LinearLayout
                val ll_continueToCancel =
                    dialog.findViewById<View>(R.id.ll_continue_to_cancel) as LinearLayout
                ll_keepMyMembership.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                ll_continueToCancel.setOnClickListener(object : View.OnClickListener {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    override fun onClick(v: View) {
                        dialog.dismiss()
                        reasonPopup
                    }
                })
                dialog.show()
            }
        }
    }
}