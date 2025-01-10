package `in`.woloo.www.more.subscribe.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.EditProfileActivity
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.fragments.MoreFragment
import `in`.woloo.www.more.subscribe.fragments.SubscribeFragment
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.more.subscribe.models.PlanResponse
import `in`.woloo.www.more.subscribe.subscription.model.InitSubscriptionRequest
import `in`.woloo.www.more.subscribe.subscription.viewmodel.SubscriptionViewModel
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SubscribeAdapter(
    private val context: Context,
    private val subscriptionArrayList: ArrayList<PlanResponse.Data>,
    viewModel: SubscriptionViewModel,
    viewProfileResponse: UserProfile?,
    currentSubscriptionPlan: String,
    futureSubscriptionPlan: String,
    msg: String,
    isEmail: Boolean,
    mobile: String,
    purchasedBy: String
) : RecyclerView.Adapter<SubscribeAdapter.ViewHolder>() {
    private val selectedSubscriptionArrayList: ArrayList<PlanResponse.Data>? = null

    //    private SubscribePresenter subscribePresenter;
    private val viewModel: SubscriptionViewModel
    private var viewProfileResponse: UserProfile?
    private val currentSubscriptionPlan: String
    private val futureSubscriptionPlan: String
    private val msg: String
    private val isEmail: Boolean
    private val mobile: String
    private val selectedPosition = 0
    private val subscribeFragment: SubscribeFragment? = null
    private var planId: String? = null
    private var id: Int? = null
    private val expanded = false
    private val dateFormat: SimpleDateFormat? = null
    var isFutureSubscription = false
    private var purchasedBy = ""
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //View listItem = layoutInflater.inflate(R.layout.subscribe_item, parent, false);
        val listItem: View
        /*  if (viewType == 0) {
            listItem = layoutInflater.inflate(R.layout.item_gold_membership, parent, false);
        } else {*/listItem = layoutInflater.inflate(R.layout.item_silver_membership, parent, false)
        // }
        return ViewHolder(listItem)
    }

    override fun getItemViewType(position: Int): Int {
        val data = subscriptionArrayList[position]
        //        if (data.getDays() >= 365) {
        return if (data.frequency != null && data.frequency.equals("annual", ignoreCase = true)) {
            0
        } else {
            1
        }
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*if (subscriptionArrayList.get(position).getFrequency() != null)
            holder.frequency.setText(subscriptionArrayList.get(position).getFrequency());*/
        /*else
            holder.frequency.setText(subscriptionArrayList.get(position).getFrequency() + " Days");*/
        var currencySymbol: String? = ""
        if (!TextUtils.isEmpty(subscriptionArrayList[position].currency)) {
            currencySymbol =
                if (subscriptionArrayList[position].currency.equals("INR", ignoreCase = true)) {
                    "\u20B9"
                } else {
                    subscriptionArrayList[position].currency
                }
        }
        holder.tv_price!!.text = currencySymbol + subscriptionArrayList[position].price
        if (subscriptionArrayList[position].frequency != null) {
            holder.tvPlan!!.text =
                subscriptionArrayList[position].frequency!!.uppercase(Locale.getDefault()) + holder.tvPlan!!.text
        }
        //holder.tv_class.setText(subscriptionArrayList.get(position).getName());
        /*holder.description_Wb.setBackgroundColor(Color.TRANSPARENT);
        holder.description_Wb.getSettings().setDomStorageEnabled(true);
        holder.description_Wb.getSettings().setAppCacheEnabled(true);
        holder.description_Wb.getSettings().setLoadsImagesAutomatically(true);
        holder.description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        holder.description_Wb.getSettings().setJavaScriptEnabled(true);
        holder.description_Wb.loadDataWithBaseURL(null, subscriptionArrayList.get(position).getDescription(), "text/html", "UTF-8", null);*/try {
            if (!TextUtils.isEmpty(currentSubscriptionPlan) && currentSubscriptionPlan == subscriptionArrayList[position].planId) {
                holder.ivSubscriptionMark!!.visibility = View.VISIBLE
                holder.tvSubscriptionTitle!!.text = "Active"
                holder.renewNow!!.visibility = View.GONE
                //holder.tv_upgrade.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(futureSubscriptionPlan) && futureSubscriptionPlan == subscriptionArrayList[position].planId) {
                holder.ivSubscriptionMark!!.visibility = View.VISIBLE
                holder.tvSubscriptionTitle!!.text = "Future"
                holder.renewNow!!.visibility = View.GONE
                //holder.tv_upgrade.setVisibility(View.GONE);
            } else {
                holder.ivSubscriptionMark!!.visibility = View.GONE
                holder.renewNow!!.visibility = View.VISIBLE
                //holder.tv_upgrade.setVisibility(View.VISIBLE);
            }
            if (viewProfileResponse != null) {
                if (!TextUtils.isEmpty(viewProfileResponse!!.profile!!.name)) {
                    holder.tvUserName!!.text = viewProfileResponse!!.profile!!.name
                } else {
                    holder.tvUserName!!.text = "Guest"
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
        //holder.tvSubscriptionDetails.setText("Buy Now " + subscriptionArrayList.get(position).getFrequency() + " Membership");
        //holder.ll_subscription.setBackgroundColor(subscriptionArrayList.get(position).getCo);
        /*if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Monthly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Quarterly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Half-yearly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Yearly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        }*/
        /*try {
            if (subscriptionArrayList.get(position).getShield_color() != null && subscriptionArrayList.get(position).getShield_color().contains("#")) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_path_silver);
                drawable.setTint(Color.parseColor(subscriptionArrayList.get(position).getShield_color()));
                holder.ll_subscription.setBackground(drawable);
                //holder.ll_subscription.getBackground().setTint(Color.parseColor(subscriptionArrayList.get(position).getShield_color()));
            }
            if (subscriptionArrayList.get(position).getBackgroud_color() != null && subscriptionArrayList.get(position).getShield_color().contains("#")) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_shape);
                drawable.setTint(Color.parseColor(subscriptionArrayList.get(position).getBackgroud_color()));
                holder.background_layout_ll.setBackground(drawable);
                //holder.background_layout_ll.getBackground().setTint(Color.parseColor(subscriptionArrayList.get(position).getBackgroud_color()));
            }
        } catch (Exception e) {

        }*/
        val startDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val c = Calendar.getInstance()
        c.time = c.time
        c.add(Calendar.DATE, subscriptionArrayList[position].days!!)
        val expDate = c.time
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val endDate = formatter.format(expDate)
        holder.tv_startDate!!.text = "START DATE : \n$startDate"
        holder.tv_endDate!!.text = "END DATE : \n$endDate"
        holder.frequency!!.text = subscriptionArrayList[position].frequency
        //        holder.tvSubscriptionDetails.setText(subscriptionArrayList.get(position).getDescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvSubscriptionDetails!!.text = Html.fromHtml(
                subscriptionArrayList[position].description,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            holder.tvSubscriptionDetails!!.text =
                Html.fromHtml(subscriptionArrayList[position].description)
        }
        try {
            val priceStr = subscriptionArrayList[position].price
            val beforeDiscountPrice = subscriptionArrayList[position].beforeDiscountPrice
            if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                val price = priceStr!!.toInt()
                if (price < beforeDiscountPrice) {
                    holder.tvBeforeDiscount!!.text = currencySymbol + beforeDiscountPrice.toString()
                    holder.tvBeforeDiscount!!.visibility = View.GONE
                    holder.tvBeforeDiscount!!.paintFlags =
                        holder.tvBeforeDiscount!!.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    holder.tvBeforeDiscount!!.visibility = View.INVISIBLE
                }
            } else {
                holder.tvBeforeDiscount!!.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            holder.tvBeforeDiscount!!.visibility = View.INVISIBLE
        }

        /*if (selectedPosition == position) {
            holder.ll_expanded.setVisibility(View.VISIBLE);
        } else {
            holder.ll_expanded.setVisibility(View.GONE);
        }*/


        /*holder.llParentLayout.setOnClickListener(v -> {
            String planId = "";
            if (viewProfileResponse.getData().getPlanData() != null)
                planId = viewProfileResponse.getData().getPlanData().getPlanId();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.CURRENT_MEMBERSHIP_ID, planId);
            Utility.logFirebaseEvent(context, bundle, AppConstants.UPGRADE_PACKAGE_CLICK);
            holder.ll_expanded.setVisibility(View.VISIBLE);
            selectedPosition = position;
            notifyDataSetChanged();
        });*/holder.llParentLayout!!.setOnClickListener { v: View? -> onClickBuy(position) }
        holder.tv_upgrade!!.setOnClickListener { v: View? -> onClickBuy(position) }
        holder.renewNow!!.setOnClickListener { v: View? -> onClickBuy(position) }
    }

    private fun onClickBuy(position: Int) {
        try {
            val bundle = Bundle()
            bundle.putString(AppConstants.MEMBERSHIP_ID, planId)
            bundle.putString(AppConstants.CURRENT_MEMBERSHIP_ID, planId)
            logFirebaseEvent(context, bundle, AppConstants.MEMBERSHIP_UPGRADE)
            val payload = HashMap<String, Any>()
            payload[AppConstants.MEMBERSHIP_ID] = planId!!
            payload[AppConstants.CURRENT_MEMBERSHIP_ID] = planId!!
            logNetcoreEvent(context, payload, AppConstants.MEMBERSHIP_UPGRADE)

//            Date stringtodate = null;
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                stringtodate = format.parse(viewProfileResponse.getData().getProfile().getExpiryDate());
//            } catch (ParseException e) {
//                  CommonUtils.printStackTrace(e)
//            }
            if (!TextUtils.isEmpty(currentSubscriptionPlan) && currentSubscriptionPlan == subscriptionArrayList[position].name) {
                Toast.makeText(
                    context,
                    context.getString(R.string.subscription_not_expired_validation),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (viewProfileResponse!!.isFutureSubcriptionExist == "true") {
                showdialog("You already have an Active Future Membership, You can buy new Membership only after the future Membership is active")
            } else if (!CommonUtils.isSubscriptionExpired(viewProfileResponse!!.profile!!.expiryDate)) {
                if (purchasedBy == AppConstants.PURCHASE_BY_APPLE) {
                    showdialog(AppConstants.PURCHASE_BY_APPLE_MSG)
                } else showAlreadySubscriptionDialog(position)
            } else {
                initSubscriptionSelected(position, false)
                //                subscribePresenter.initSubscription(subscriptionArrayList.get(position).getId(), subscriptionArrayList.get(position).getPlanId(), initSubscriptionCallback, isFutureSubscription);
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun initSubscriptionSelected(position: Int, isFutureSubscription: Boolean) {
        planId = subscriptionArrayList[position].planId
        id = subscriptionArrayList[position].id
        val request = InitSubscriptionRequest()
        request.planId = planId!!
        request.id = id!!
        request.future = isFutureSubscription
        viewModel.initSubscriptionRequest = request
        viewModel.setSelectedSubscription(subscriptionArrayList[position])
    }

    private fun showAlreadySubscriptionDialog(position: Int) {
        Logger.i(TAG, "AlreadySubscriptionDialog")
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.error_already_subscription)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val futureSubscriptionBtn = dialog.findViewById<RadioButton>(R.id.futureSubscriptionBtn)
            if (viewProfileResponse!!.lifetime_free == "1") {
                futureSubscriptionBtn.visibility = View.GONE
            }
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llOkayBtn) as LinearLayout
            llStartFreeTrial.setOnClickListener {
                initSubscriptionSelected(position, futureSubscriptionBtn.isChecked)
                //                    subscribePresenter.initSubscription(subscriptionArrayList.get(position).getId(), subscriptionArrayList.get(position).getPlanId(), initSubscriptionCallback, isFutureSubscription);
                dialog.dismiss()
            }
            val llCancelBtn = dialog.findViewById<View>(R.id.llCancelBtn) as LinearLayout
            llCancelBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showdialog(msg: String?) {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
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
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun showErrorDialog() {
        try {
            val dialog = Dialog(context, R.style.CustomAlertDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_subscription_validation)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val tvCancel = dialog.findViewById<View>(R.id.tvCancel) as TextView
            tvCancel.setOnClickListener { dialog.dismiss() }
            val tvOkay = dialog.findViewById<View>(R.id.tvOkay) as TextView
            tvOkay.setOnClickListener {
                try {
                    WolooApplication.instance!!.profileResponse = viewProfileResponse
                    try {
                        MoreFragment.isNeedToUpdateProfile = true
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                    val intent = Intent(context, EditProfileActivity::class.java)
                    intent.putExtra(AppConstants.ID, id)
                    intent.putExtra(AppConstants.FROM_SUBSCRIPTION, true)
                    intent.putExtra(AppConstants.PLAN_ID, planId)
                    intent.putExtra(AppConstants.MSG, msg)
                    intent.putExtra(AppConstants.IS_EMAIL, isEmail)
                    intent.putExtra(AppConstants.MOBILE, mobile)
                    context.startActivity(intent)
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
                dialog.dismiss()
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    override fun getItemCount(): Int {
        return subscriptionArrayList.size
    }

    fun setViewProfile(viewProfileResponse: UserProfile?) {
        this.viewProfileResponse = viewProfileResponse
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.frequency)
        var frequency: TextView? = null

        @JvmField
        @BindView(R.id.tv_price)
        var tv_price: TextView? = null

        /*@BindView(R.id.tv_class)
        TextView tv_class;*/
        /*@BindView(R.id.ll_subscription)
        LinearLayout ll_subscription;*/
        /*@BindView(R.id.background_layout_ll)
        LinearLayout background_layout_ll;*/
        @JvmField
        @BindView(R.id.clCard)
        var llParentLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.tvSubscriptionDetails)
        var tvSubscriptionDetails: TextView? = null

        @JvmField
        @BindView(R.id.tvSubscriptionTitle)
        var tvSubscriptionTitle: TextView? = null

        @JvmField
        @BindView(R.id.ivSubscriptionMark)
        var ivSubscriptionMark: LinearLayout? = null

        /*@BindView(R.id.tv_price_info)
        TextView tv_price_info;*/
        /* @BindView(R.id.ll_expanded)
        LinearLayout ll_expanded;*/
        @JvmField
        @BindView(R.id.tv_upgrade)
        var tv_upgrade: TextView? = null

        @JvmField
        @BindView(R.id.renew_now)
        var renewNow: TextView? = null

        @JvmField
        @BindView(R.id.tvStartDate)
        var tv_startDate: TextView? = null

        /*@BindView(R.id.description_Wb)
        WebView description_Wb;*/
        @JvmField
        @BindView(R.id.tvEndDate)
        var tv_endDate: TextView? = null

        @JvmField
        @BindView(R.id.tvName)
        var tvUserName: TextView? = null

        @JvmField
        @BindView(R.id.tvBeforeDiscount)
        var tvBeforeDiscount: TextView? = null

        @JvmField
        @BindView(R.id.tvPlan)
        var tvPlan: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    var initSubscriptionCallback: InitSubscriptionCallback = object : InitSubscriptionCallback {
        override fun initSubscriptionSuccess(initSubscriptionResponse: InitSubscriptionResponse?) {
            try {
                if (initSubscriptionResponse != null && initSubscriptionResponse.data != null) {
                    val subscriptionId = initSubscriptionResponse.data!!.subscriptionId
                    if (!TextUtils.isEmpty(subscriptionId)) {
                        CommonUtils.navigateToRazorPayFlow(
                            context,
                            planId,
                            subscriptionId,
                            msg,
                            isEmail,
                            mobile,
                            true,
                            null,
                            isFutureSubscription,
                            false
                        )
                    } else {
                        //showErrorDialog();
                        showdialog(initSubscriptionResponse.message)
                    }
                } else {
                    showdialog(initSubscriptionResponse?.message)
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

    init {
        this.viewModel = viewModel
        this.viewProfileResponse = viewProfileResponse
        this.currentSubscriptionPlan = currentSubscriptionPlan
        this.futureSubscriptionPlan = futureSubscriptionPlan
        this.msg = msg
        this.isEmail = isEmail
        this.purchasedBy = purchasedBy
        this.mobile = mobile
    }

    interface InitSubscriptionCallback {
        fun initSubscriptionSuccess(initSubscriptionResponse: InitSubscriptionResponse?)
    }

    companion object {
        private val TAG = SubscribeAdapter::class.java.simpleName
        const val REQUEST_CODE = 100
    }
}
