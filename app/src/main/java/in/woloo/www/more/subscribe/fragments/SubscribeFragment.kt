package `in`.woloo.www.more.subscribe.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.lists_models.ResultVtionSdkModel
import `in`.woloo.www.application_kotlin.presentation.activities.login.LoginActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.utilities.EmailSenderClass
import `in`.woloo.www.application_kotlin.utilities.MessageList
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.cancelsubscription.model.CancelSubscriptionResponse
import `in`.woloo.www.more.cancelsubscription.mvp.CancelSubscriptionPresenter
import `in`.woloo.www.more.cancelsubscription.mvp.CancelSubscriptionView
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.subscribe.adapter.SubscribeAdapter
import `in`.woloo.www.more.subscribe.models.GetSubscriptionDetailsResponse
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.more.subscribe.models.PlanResponse
import `in`.woloo.www.more.subscribe.subscription.model.SubmitGiftCardSubscriptionPurchaseRequest
import `in`.woloo.www.more.subscribe.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.more.subscribe.subscription.viewmodel.SubscriptionViewModel
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import `in`.woloo.www.v2.profile.model.UserProfile
import java.util.Objects

class SubscribeFragment : Fragment(), CancelSubscriptionView {
    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.recyclerView_subscribe)
    var recyclerView_subscribe: RecyclerView? = null

    /*    @BindView(R.id.tv_gift_points)
    TextView tvGiftPoints;*/
    @JvmField
    @BindView(R.id.discontinue_membership)
    var discontinueMembership: TextView? = null

    @JvmField
    @BindView(R.id.delete_user)
    var deleteUser: TextView? = null

    // TODO: Rename and change types of parameters
    private var currentSubscriptionPlan: String? = ""
    private var purchasedBy = ""
    private var futureSubscriptionPlan: String? = ""
    private var mDatabase: DatabaseReference? = null
    private var msg: String? = null
    var subscriptionArrayList: ArrayList<PlanResponse.Data>? = null
    var subscriptionViewModel: SubscriptionViewModel? = null
    var profileViewModel: ProfileViewModel? = null
    private var isEmail = false
    private var mobile: String? = null
    private var expireDate: String? = null
    var isForSubscription = false
    var cancelSubscriptionPresenter: CancelSubscriptionPresenter? = null
    var isCancelSubscription = false
     var selectedSubscriptionArrayList = ArrayList<PlanResponse.Data>()
    private var adapter: SubscribeAdapter? = null
    private var viewProfileResponse: UserProfile? = null
    var subscriptionId = ""
    var mobileNumber: String? = ""
    var bundle = Bundle()
    var payload = HashMap<String, Any>()
    lateinit var  progressView: CustomProgressView
    private var loginViewModel: WolooViewModel? = null
    var isFutureSubscriptionExists: Boolean = false
    var isActiveNowSubscription: Boolean = false
    private var wolooViewModel: WolooViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currentSubscriptionPlan = requireArguments().getString(ARG_PARAM1)
            msg = requireArguments().getString(ARG_PARAM2)
            isEmail = requireArguments().getBoolean(ARG_PARAM3)
            isForSubscription = requireArguments().getBoolean(ARG_PARAM5)
            isCancelSubscription = requireArguments().getBoolean(ARG_PARAM6)
            mobile = requireArguments().getString(ARG_PARAM4)
            expireDate = requireArguments().getString(ARG_PARAM7)
            mobileNumber = mobile
        }
    }

    override fun onResume() {
        super.onResume()
        subscriptionViewModel!!.getMySubscription()
        //        subscribePresenter.getMySubscriptionList();
        //initViews();
    }

    private fun setLiveData() {
        subscriptionViewModel!!.observeMySubscription()
            .observe(viewLifecycleOwner, Observer<BaseResponse<GetSubscriptionDetailsResponse.Data>> { it ->
                if (Objects.requireNonNull<GetSubscriptionDetailsResponse.Data>(it.data).activeSubscription != null) {
                    if (!CommonUtils.isSubscriptionExpired(expireDate)) currentSubscriptionPlan =
                        it.data!!.activeSubscription!!.planId
                    if (it.data!!.purchase_by != null) purchasedBy = it.data!!.purchase_by.toString()
                } else {
                    currentSubscriptionPlan = ""
                    purchasedBy = ""
                }
                if(it.data!!.futureSubscription!!.isNotEmpty())
                {
                    isFutureSubscriptionExists = true
                }
                futureSubscriptionPlan =
                    if (it.data!!.futureSubscription != null && !it.data!!.futureSubscription!!.isEmpty()) {
                        it.data!!.futureSubscription!!.get(0).planId
                    } else {
                        ""
                    }
                initViews()
                initViews()
            })
        subscriptionViewModel!!.observeSubscriptionPlans()
            .observe(viewLifecycleOwner, Observer<BaseResponse<ArrayList<PlanResponse.Data>>> { it ->
                if (it != null) {
                    initSubscriptionPlans(it.data)
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.errorMessage = ""
                }
            })
        subscriptionViewModel!!.observeSubscriptionSelected()
            .observe(viewLifecycleOwner
            ) { plan ->
                if (plan != null) {
                    try {
                        if (plan.price!!.toInt() <= viewProfileResponse!!.totalCoins!!.giftCoins) {
                            showBottomSheetDialog()
                        } else {
                            subscriptionViewModel!!.initSubscriptionByOrder(subscriptionViewModel!!.initSubscriptionRequest)
                        }
                        //                        Toast.makeText(getContext(), "Price " + plan.getPrice() + "\nPlan Name "+plan.getName(), Toast.LENGTH_SHORT).show();
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                }
            }
        subscriptionViewModel!!.observeInitSubscriptionByOrder()
            .observe(viewLifecycleOwner, Observer<BaseResponse<InitSubscriptionResponse.Data>> { initSubscriptionResponse ->
                if (initSubscriptionResponse != null) {
                    try {
                        if (initSubscriptionResponse.data != null) {
                            subscriptionId = initSubscriptionResponse.data!!.subscriptionId.toString()
                            if (!TextUtils.isEmpty(subscriptionId)) {
                                CommonUtils.navigateToRazorPayFlow(
                                    requireContext(),
                                    subscriptionViewModel!!.initSubscriptionRequest.planId,
                                    subscriptionId,
                                    msg,
                                    isEmail,
                                    mobile,
                                    true,
                                    null,
                                    subscriptionViewModel!!.initSubscriptionRequest.future,
                                    false
                                )
                            } else {
                                //showErrorDialog();
                                showdialog(initSubscriptionResponse.message)
                                Logger.e(
                                    "REponse if else",
                                    initSubscriptionResponse.data.toString()
                                )
                            }
                        } else {
                            showdialog(if (initSubscriptionResponse != null) initSubscriptionResponse.message else null)
                            Logger.e("REponse else", initSubscriptionResponse.data.toString())
                        }
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.errorMessage = ""
                }
            })
        subscriptionViewModel!!.observeSubmitSubscriptionPurchaseResponse()
            .observe(viewLifecycleOwner, Observer<BaseResponse<InitSubscriptionResponse>> { it ->
                try {
                    Logger.i("SubscibeFragment", "observeSubmitSubscriptionPurchaseResponse")
                    if (it != null && it.success) {
                        profileViewModel!!.getUserProfile()
                        showPaymentSuccessDialog()
                    } else {
                        if (!WolooApplication.errorMessage.isEmpty())
                            Toast.makeText(
                            context,
                            WolooApplication.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        WolooApplication.errorMessage = ""
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            })
        subscriptionViewModel!!.observeSubmitGiftCardSubscriptionPurchaseResponse()
            .observe(viewLifecycleOwner, Observer<BaseResponse<InitSubscriptionResponse>> { it ->
                try {
                    Logger.i("SubscibeFragment", "observeSubmitSubscriptionPurchaseResponse")
                    if (it != null && it.success) {
                        profileViewModel!!.getUserProfile()
                        showPaymentSuccessDialog()
                        val request = CreditCoinsRequest(
                            coins = CommonUtils.authconfig_response(requireActivity()).getWolooSubscriptionPoints()!!.toInt(),
                            remarks = AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK,
                            type = AppConstants.WOLOO_SUBSCRIPTION_POINTS_CLICK,
                            isGift = 0,
                            blogId = 0
                        )
                        wolooViewModel!!.addCoinstoWolooUser(request)
                    } else {
                        if (!WolooApplication.errorMessage.isEmpty()) Toast.makeText(
                            context,
                            WolooApplication.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        WolooApplication.errorMessage = ""
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            })

        profileViewModel!!.observeUserProfile().observe( requireActivity()) { it ->
            try {
                Logger.i("SubscibeFragment", "observeUserProfile")
                if (it != null && it.success) {

                    viewProfileResponse = it.data
                    if (adapter != null) {
                        adapter!!.setViewProfile(viewProfileResponse)
                        setPlanResults()
                        discontinueMembership!!.visibility = View.VISIBLE
                        deleteUser!!.visibility = View.VISIBLE
                    }
                    //  tvGiftPoints.setText("Gift Voucher : "+viewProfileResponse.getTotalCoins().getGiftCoins());
                } else {
                    if (!WolooApplication.errorMessage.isEmpty()) Toast.makeText(
                        context,
                        WolooApplication.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    WolooApplication.errorMessage = ""
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
        progressView.hide()
        discontinueMembership!!.visibility = View.VISIBLE
        deleteUser!!.visibility = View.VISIBLE
    }

    fun showPaymentSuccessDialog() {
        try {
            val dialog = Dialog( requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_payment_success)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
            val tv_paymentsuccess = dialog.findViewById<View>(R.id.tv_paymentsuccess) as TextView
            val authConfigResponse = CommonUtils.authconfig_response(context)
            if (authConfigResponse != null) {
                val paymentSuccessDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()!!.paymentSuccessDialogText
                tv_paymentsuccess.text = paymentSuccessDialogText!!.replace("\\\\n".toRegex(), "\n")
            }
            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) dialog.dismiss()
                //                    FragmentManager fm = requireActivity().getSupportFragmentManager();
//                    fm.popBackStack();
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

  /*  fun initSubscriptionPlans(planList: List<PlanResponse.Data>?) {
        try {
            selectedSubscriptionArrayList = ArrayList()
            subscriptionArrayList = ArrayList()
            selectedSubscriptionArrayList.addAll(planList!!)
            if (currentSubscriptionPlan != null) {
                for (i in selectedSubscriptionArrayList.indices-2) {
                    if (currentSubscriptionPlan == selectedSubscriptionArrayList[i].planId) {
                        subscriptionArrayList!!.add(0, selectedSubscriptionArrayList[i])
                        selectedSubscriptionArrayList.removeAt(i)
                    }
                }
            }
            if (futureSubscriptionPlan != null) {
                for (i in selectedSubscriptionArrayList.indices-2) {
                    if (futureSubscriptionPlan == selectedSubscriptionArrayList[i].planId) {
                        if (subscriptionArrayList!!.size < 1) subscriptionArrayList!!.add(
                            0,
                            selectedSubscriptionArrayList[i]
                        ) else subscriptionArrayList!!.add(1, selectedSubscriptionArrayList[i])
                        selectedSubscriptionArrayList.removeAt(i)
                    }
                }
            }
            subscriptionArrayList!!.addAll(selectedSubscriptionArrayList)
            setPlanResults()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }
*/

    fun initSubscriptionPlans(planList: List<PlanResponse.Data>?) {
        try {
            selectedSubscriptionArrayList = ArrayList()
            subscriptionArrayList = ArrayList()

            // Add all the planList items to selectedSubscriptionArrayList
            selectedSubscriptionArrayList.addAll(planList!!)

            // If there's a currentSubscriptionPlan, move it to the front of the list
            if (currentSubscriptionPlan != null) {
                val currentPlanIndex = selectedSubscriptionArrayList.indexOfFirst { it.planId == currentSubscriptionPlan }
                if (currentPlanIndex != -1) {
                    // Remove the current plan from the original list and add it at the start of subscriptionArrayList
                    subscriptionArrayList!!.add(0, selectedSubscriptionArrayList[currentPlanIndex])
                    selectedSubscriptionArrayList.removeAt(currentPlanIndex)
                }
            }

            // If there's a futureSubscriptionPlan, move it to the front or second position
            if (futureSubscriptionPlan != null) {
                val futurePlanIndex = selectedSubscriptionArrayList.indexOfFirst { it.planId == futureSubscriptionPlan }
                if (futurePlanIndex != -1) {
                    // If the subscriptionArrayList is empty, add the future plan at the start
                    // Otherwise, add it at the second position
                    if (subscriptionArrayList!!.isEmpty()) {
                        subscriptionArrayList!!.add(0, selectedSubscriptionArrayList[futurePlanIndex])
                    } else {
                        subscriptionArrayList!!.add(1, selectedSubscriptionArrayList[futurePlanIndex])
                    }
                    selectedSubscriptionArrayList.removeAt(futurePlanIndex)
                }
            }

            // Add the remaining plans at the end of subscriptionArrayList
            subscriptionArrayList!!.addAll(selectedSubscriptionArrayList)

            // Set the final plan results
            setPlanResults()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_subscribe, container, false)
        ButterKnife.bind(this, root)
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )
        progressView = CustomProgressView(requireActivity())
        progressView.show()
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        subscriptionViewModel = ViewModelProvider(this).get<SubscriptionViewModel>(
            SubscriptionViewModel::class.java
        )
        loginViewModel = ViewModelProvider(this).get(WolooViewModel::class.java)
        subscriptionViewModel!!.getMySubscription()
        profileViewModel!!.getUserProfile()
        mDatabase = FirebaseDatabase.getInstance().reference
        deleteUser!!.setOnClickListener {
            logFirebaseEvent(activity, bundle, AppConstants.DELETE_CLICK)
            logNetcoreEvent( requireActivity(), payload, AppConstants.DELETE_CLICK)
            if (mobileNumber == null) {
                Log.d("Delete User", "Mobile Number does not exists")
                Toast.makeText(
                     requireActivity().applicationContext,
                    MessageList.TRYLATER,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showDeleteUserDialog()
            }
        }
        discontinueMembership!!.setOnClickListener {
            Log.i("Aarati Subscription" , " click on discontinue membership")
            if (viewProfileResponse!!.planData == null) {
                Log.i("Aarati Subscription" , "in the loop viewProfileResponse!!.planData == null ${viewProfileResponse!!.planData.toString()}")
                showdialog("You don't have an active membership")
            } else if (viewProfileResponse!!.profile != null && !TextUtils.isEmpty(
                    viewProfileResponse!!.profile!!.expiryDate
                ) && CommonUtils.isSubscriptionExpired(
                    viewProfileResponse!!.profile!!.expiryDate
                )
            ) {
                Log.i("Aarati Subscription" , "in the loop viewProfileResponse!!.profile != null" +
                        " ${viewProfileResponse!!.profile.toString()} " +
                        "viewProfileResponse!!.profile!!.expiryDateis empty ${viewProfileResponse!!.profile!!.expiryDate}"
                +"CommonUtils.isSubscriptionExpired viewProfileResponse!!.profile!!.expiryDate " +
                        " ${CommonUtils.isSubscriptionExpired(viewProfileResponse!!.profile!!.expiryDate)}")
                showdialog("You don't have an active membership")
            } else if (viewProfileResponse!!.planData!!.isIs_cancel) {
                Log.i("Aarati Subscription" , "in the loopviewProfileResponse!!.planData!!.isIs_cancel ${viewProfileResponse!!.planData!!.isIs_cancel}")
                showdialog("You have already Unsubscribed the Membership")
            } else if (viewProfileResponse!!.planData!!.name == null || viewProfileResponse!!.planData!!.name == "FREE TRIAL") {

                Log.i("Aarati Subscription" , "in the loop viewProfileResponse!!.planData!!.name == null" +
                        "${viewProfileResponse!!.planData!!.name == null}" + "viewProfileResponse!!.planData!!.name == FREE TRIAL" +
                        "${viewProfileResponse!!.planData!!.name == "FREE TRIAL"}")

                showdialog("You don't have an active membership")
            } else if (viewProfileResponse!!.purchase_by != null) {
                Log.i("Aarati Subscription" , "in the loop viewProfileResponse!!.purchase_by != null ${viewProfileResponse!!.purchase_by != null}")
                if (viewProfileResponse!!.purchase_by == AppConstants.PURCHASE_BY_APPLE) {
                    Log.i("Aarati Subscription" , "viewProfileResponse!!.purchase_by == AppConstants.PURCHASE_BY_APPLE ${viewProfileResponse!!.purchase_by == AppConstants.PURCHASE_BY_APPLE}")
                    showdialog(AppConstants.PURCHASE_BY_APPLE_MSG)
                }
            } else
                navigateToSubscriptionScreen(false)
        }
        setLiveData()


        //initViews();
        return root
    }

    private fun initViews() {
        subscriptionArrayList = ArrayList()
        try {
            profileViewModel = ViewModelProvider(this).get(
                ProfileViewModel::class.java
            )
            subscriptionViewModel = ViewModelProvider(this).get<SubscriptionViewModel>(
                SubscriptionViewModel::class.java
            )
            cancelSubscriptionPresenter =
                CancelSubscriptionPresenter(requireContext(), this@SubscribeFragment)
            //            subscribePresenter.getSubscriptionPlans();
            subscriptionViewModel!!.getSubscriptionPlans()
            //            setSearchResults();
            tvTitle!!.text = "Pee'rs Club Membership"
            ivBack!!.setOnClickListener {

                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            }
            if (!isForSubscription) {
                if (isCancelSubscription) {
                    showdialog("You have already Unsubscribed the Membership")
                } else {
                    showCancelSubscriptionDialog()
                }
                isForSubscription = true
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

  /*  private fun setPlanResults() {
        try {
            adapter = SubscribeAdapter(
                requireContext(),
                subscriptionArrayList!!,
                subscriptionViewModel!!,
                viewProfileResponse,
                currentSubscriptionPlan!!,
                futureSubscriptionPlan!!,
                msg!!,
                isEmail!!,
                mobile!!,
                purchasedBy
            )
            recyclerView_subscribe!!.setHasFixedSize(true)
            recyclerView_subscribe!!.layoutManager = LinearLayoutManager(
                context
            )
            recyclerView_subscribe!!.adapter = adapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
        discontinueMembership!!.visibility = View.VISIBLE
        deleteUser!!.visibility = View.VISIBLE
    }*/

    private fun setPlanResults() {
        try {

            adapter = SubscribeAdapter(
                requireContext(),
                subscriptionArrayList,
                subscriptionViewModel,
                viewProfileResponse,
                currentSubscriptionPlan,
                futureSubscriptionPlan,
                msg,
                isEmail,
                mobile,
                purchasedBy,
                isFutureSubscriptionExists
            ){ isActiveNow ->
                // This block runs when adapter triggers the callback
                if (isActiveNow) {
                        isActiveNowSubscription = isActiveNow
                }
            }
            recyclerView_subscribe?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@SubscribeFragment.adapter
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }



    override fun cancelSubscriptionResponse(cancelSubscriptionResponse: CancelSubscriptionResponse?) {
        showdialog("Membership Cancelled Successfully")
        //initViews();
    }

    fun showCancelSubscriptionDialog() {
        try {
            val dialog = Dialog( requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_discontinue_subscription)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val discontinueTextTv = dialog.findViewById<TextView>(R.id.discontinueTextTv)
            discontinueTextTv.text =
                "Even if you choose to cancel your current membership,you can still use the app benefits until the end of current billing period."
            val llCancelBtn = dialog.findViewById<TextView>(R.id.llCancelBtn)
            llCancelBtn.setOnClickListener { v: View? -> dialog.dismiss() }
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.llOkayBtn)
            llStartFreeTrial.setOnClickListener {
                dialog.dismiss()
                reasonPopup
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    @get:SuppressLint("ClickableViewAccessibility")
    private val reasonPopup: Unit
        private get() {
            try {
                val dialog = Dialog( requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setContentView(R.layout.dialog_cancel_subscription_reason)
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
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
                val mSharedPref = SharedPreference(context)
                val authConfigResponse = getPreferences.fetchAuthConfig()
                val adapter = ArrayAdapter(
                     requireContext(),
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
                             requireActivity().applicationContext,
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

    /*calling on showdialog*/
    fun showdialog(msg: String?) {
        try {
            val dialog = Dialog( requireContext())
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
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottomsheet_payment_options)
        val giftcard = bottomSheetDialog.findViewById<RadioButton>(R.id.radio_gift)
        val razorpay = bottomSheetDialog.findViewById<RadioButton>(R.id.radio_razorpay)
        val tvGiftPoints = bottomSheetDialog.findViewById<TextView>(R.id.tv_gift_points)
        val giftLayout = bottomSheetDialog.findViewById<View>(R.id.gift_layout)
        val razorpayLayout = bottomSheetDialog.findViewById<View>(R.id.razorpay_layout)
        val pay = bottomSheetDialog.findViewById<TextView>(R.id.btn_pay)
        giftcard!!.setOnClickListener {
            if (!giftcard.isChecked) {
                giftcard.isChecked = true
            }
            razorpay!!.isChecked = false
        }
        giftLayout!!.setOnClickListener {
            if (!giftcard.isChecked) {
                giftcard.isChecked = true
            }
            razorpay!!.isChecked = false
        }
        razorpay!!.setOnClickListener {
            if (!razorpay.isChecked) {
                razorpay.isChecked = true
            }
            giftcard.isChecked = false
        }
        razorpayLayout!!.setOnClickListener {
            if (!razorpay.isChecked) {
                razorpay.isChecked = true
            }
            giftcard.isChecked = false
        }

        tvGiftPoints!!.visibility = View.VISIBLE

        tvGiftPoints!!.setText("\u20B9 " + viewProfileResponse!!.totalCoins!!.giftCoins.toString() + "/-");
        pay!!.setOnClickListener {
            if (!giftcard.isChecked && !razorpay.isChecked) {
                Toast.makeText(context, "Please select any one payment options", Toast.LENGTH_SHORT)
                    .show()
            } else if (giftcard.isChecked) {
                //giftcard

                val request = SubmitGiftCardSubscriptionPurchaseRequest()
                request.planId = subscriptionViewModel!!.initSubscriptionRequest.planId
                if(isActiveNowSubscription)
                request.future = 1
                else
                request.future = 0

             //  subscriptionViewModel!!.submitSubscriptionPurchase(request)

                subscriptionViewModel!!.submitGiftCardSubscriptionPurchase(request)

               /* profileViewModel!!.getUserProfile()
                showPaymentSuccessDialog()*/

            } else if (razorpay.isChecked) {
                subscriptionViewModel!!.initSubscriptionByOrder(subscriptionViewModel!!.initSubscriptionRequest)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDeleteUserDialog() {
        Logger.i("Delete User", "showDeleteUserDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                requireActivity()
            )
            val child: View = layoutInflater.inflate(R.layout.logout_dialog, null)
            alertDialogBuilder.setView(child)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent_background)
            val tvCancel = child.findViewById<TextView>(R.id.tvCancel)
            val tvLogout = child.findViewById<TextView>(R.id.tvLogout)
            val tv_logout = child.findViewById<TextView>(R.id.tv_logout)
            tv_logout.text =
                "Are you sure you want to delete your account?\nThis will permanently erase your account. It will take around 48 hrs to delete your data"
            tvLogout.text = "Delete"
            val authConfigResponse = CommonUtils.authconfig_response(context)
            if (authConfigResponse != null) {
                val logoutDialog = authConfigResponse.getcUSTOMMESSAGE()!!.logoutDialog
                // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }
            tvCancel.setOnClickListener { v: View? -> alertDialog.dismiss() }
            tvLogout.setOnClickListener { v: View? ->
                Log.d("Mobile", mobileNumber!!)

                SharedPrefSettings.getPreferences.fetchUserDetails()!!.id?.let {
                    loginViewModel!!.deleteWolooUser(
                        it
                    )
                }

                mDatabase!!.child("result").child(mobileNumber!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val result: ResultVtionSdkModel? =
                                    dataSnapshot.getValue<ResultVtionSdkModel>(
                                        ResultVtionSdkModel::class.java
                                    )
                                if (result != null) {
                                    val status: String = result.result.toString()
                                    Log.d(
                                        "FirebaseCheck",
                                        "Mobile number exists: $mobileNumber $status"
                                    )
                                    val subject: String = MessageList.EMAILSUBJECT + mobileNumber
                                    val message: String =
                                        MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO
                                    if (status.equals(
                                            "SUCCESS",
                                            ignoreCase = true
                                        ) || status.matches("SUCCESS".toRegex())
                                    ) {
                                        // The mobile number exists
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //    EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.VTIONEMAIL,
                                                subject,
                                                message
                                            ) // calling for vtionuser
                                            //    EmailSenderClass.Companion.sendEmail("aaratigujar@gmail.com", subject, message);
                                        }
                                    } else if (status.equals(
                                            "FAILED",
                                            ignoreCase = true
                                        ) || status.matches("FAILED".toRegex())
                                    ) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                    }
                                }

                                // Delete user details from firebase.
                                val mobileNumberRef = mDatabase!!.child("result").child(
                                    mobileNumber!!
                                )


// Retrieve the data
                                mobileNumberRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Check if the data exists
                                        if (dataSnapshot.exists()) {
                                            // Get the current data as a ResultVtionSdkModel object
                                            val existingData: ResultVtionSdkModel? =
                                                dataSnapshot.getValue<ResultVtionSdkModel>(
                                                    ResultVtionSdkModel::class.java
                                                )
                                            if (existingData != null) {
                                                // Update the status field
                                                existingData.deleteRequest = true

                                                // Save the updated data back to the database
                                                mobileNumberRef.setValue(existingData)
                                                    .addOnCompleteListener { task: Task<Void?> ->
                                                        if (task.isSuccessful) {
                                                            Log.d(
                                                                "Firebase",
                                                                "Status updated successfully"
                                                            )
                                                        } else {
                                                            Log.e(
                                                                "Firebase",
                                                                "Failed to update status",
                                                                task.exception
                                                            )
                                                        }
                                                    }
                                            }
                                        } else {
                                            Log.d(
                                                "Firebase",
                                                "No data found for mobile number: $mobileNumber"
                                            )
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e(
                                            "Firebase",
                                            "Database error: " + databaseError.message
                                        )
                                    }
                                })
                                this@SubscribeFragment.context?.let {
                                    CommonUtils().clearApplicationData(
                                        it
                                    )
                                }
                                startActivity(
                                    Intent(
                                        this@SubscribeFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@SubscribeFragment.activity as WolooDashboard?)!!.finish()
                                alertDialog.dismiss()
                            } else {
                                // The mobile number does not exist
                                Log.d(
                                    "FirebaseCheck",
                                    "Mobile number does not exist: $mobileNumber"
                                )
                                val subject: String = MessageList.EMAILSUBJECT + mobileNumber
                                val message: String =
                                    MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    EmailSenderClass.Companion.sendEmail(
                                        MessageList.WOLOOEMAIL,
                                        subject,
                                        message
                                    )
                                    // EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                }
                                this@SubscribeFragment.context?.let {
                                    CommonUtils().clearApplicationData(
                                        it
                                    )
                                }
                                startActivity(
                                    Intent(
                                        this@SubscribeFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@SubscribeFragment.activity as WolooDashboard?)!!.finish()
                                alertDialog.dismiss()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle possible errors
                            Log.e("FirebaseCheck", "Database error: " + databaseError.message)
                            Toast.makeText(
                                 requireActivity().applicationContext,
                                MessageList.TRYLATER,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun navigateToSubscriptionScreen(isForSubscription: Boolean) {
        i("MoreFragment.TAG", "navigateToSubscriptionScreen")
        try {
            var isEmail = false
            var email = ""
            var mobile = ""
            if (!TextUtils.isEmpty(viewProfileResponse!!.profile!!.mobile)) {
                mobile = viewProfileResponse!!.profile!!.mobile.toString()
            } else if (!TextUtils.isEmpty(viewProfileResponse!!.profile!!.email)) {
                isEmail = true
                email = viewProfileResponse!!.profile!!.email.toString()
            }
            var subscriptionPlanName = ""
            if (viewProfileResponse!!.planData != null) if (!CommonUtils.isSubscriptionExpired(
                    viewProfileResponse!!.profile!!.expiryDate
                ) && viewProfileResponse!!.planData!!.planId != null
            ) {
                subscriptionPlanName = viewProfileResponse!!.planData!!.planId.toString()
            }
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                newInstance(
                    subscriptionPlanName,
                    email,
                    isEmail,
                    mobile,
                    isForSubscription,
                    false,
                    viewProfileResponse!!.profile!!.expiryDate
                ), "InviteFriendFragment"
            )
        } catch (ex: java.lang.Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        private const val ARG_PARAM4 = "param4"
        private const val ARG_PARAM5 = "param5"
        private const val ARG_PARAM6 = "param6"
        private const val ARG_PARAM7 = "param7"

        @JvmStatic
        fun newInstance(
            param1: String?,
            param2: String?,
            isEmail: Boolean,
            mobile: String?,
            isForSubscription: Boolean,
            isCancelSubscription: Boolean,
            end_at: String?
        ): SubscribeFragment {
            val fragment = SubscribeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM7, end_at)
            args.putBoolean(ARG_PARAM3, isEmail)
            args.putString(ARG_PARAM4, mobile)
            args.putBoolean(ARG_PARAM5, isForSubscription)
            args.putBoolean(ARG_PARAM6, isCancelSubscription)
            fragment.arguments = args
            return fragment
        }
    }
}