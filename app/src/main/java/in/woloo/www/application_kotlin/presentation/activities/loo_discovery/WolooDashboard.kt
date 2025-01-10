package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.base.BaseActivity
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment.Companion.newInstance
import `in`.woloo.www.application_kotlin.utilities.DateToLocalDateConverter
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.dashboard.ui.home.HomeFragment
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.more.thirstreminder.AlarmReceiver
import `in`.woloo.www.more.thirstreminder.SaveThirstReminderDialog
import `in`.woloo.www.more.thirstreminder.ThirstReminderDialog
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.editprofile.EditProfileActivity
import `in`.woloo.www.more.giftSubscription.GiftSubscriptionActivity
import `in`.woloo.www.more.giftcard.GiftCardFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.WolooStoreInfoFragment
import `in`.woloo.www.more.fragments.InviteFriendFragment
import `in`.woloo.www.mapdirection.GetDistance
import `in`.woloo.www.more.fragments.MoreFragment
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.my_history.MyHistoryFragment
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.more.period_tracker.ui.EditCycleActivity
import `in`.woloo.www.more.period_tracker.ui.PeriodTrackerFragment
import `in`.woloo.www.more.subscribe.fragments.SubscribeFragment
import `in`.woloo.www.more.trendingblog.fragments.TrendBlogFragment
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CircleImageView
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.view_models.EnrouteViewModel
import `in`.woloo.www.application_kotlin.model.server_request.VoucherRequest
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.more.period_tracker.periodtracker.viewmodel.PeriodTrackerViewModel
import `in`.woloo.www.more.editprofile.profile.activity.ProfileActivity
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.more.thirstreminder.thirstReminder.viewmodel.ThirstReminderViewModel
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class WolooDashboard : BaseActivity(), Serializable, ThirstReminderDialog.ThirstCallbacks,
    SaveThirstReminderDialog.SaveThirstCallbacks {
    private val EDIT_PROFILE = 11

    @JvmField
    @BindView(R.id.nav_view)
    var nav_view: BottomNavigationView? = null

    @JvmField
    @BindView(R.id.maintoolbar)
    var mainToolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.imgSearch)
    var imgSearch: ImageView? = null

    @JvmField
    @BindView(R.id.ivProfile)
    var ivProfile: CircleImageView? = null

    @JvmField
    @BindView(R.id.txtName)
    var txtName: TextView? = null

    @JvmField
    @BindView(R.id.imgScanQR)
    var imgScanQR: ImageView? = null

    @JvmField
    @BindView(R.id.imgWlLogo)
    var profileImage: ImageView? = null

    @JvmField
    @BindView(R.id.fabHomeDashboard)
    var fabDashboardButton: ImageView? = null

    @JvmField
    @BindView(R.id.fabHomeDashboardDown)
    var fabDashboardButtonDown: ImageView? = null

    @JvmField
    @BindView(R.id.progress_bar)
    var progressBar: ProgressBar? = null
    var isSectionShow = false
    private var height = 0
    private var currentFragment: Fragment? = null
    private var wolooStoreInfoFragment: WolooStoreInfoFragment? = null
    private val locationPermissionGranted = false
    private val mGoogleApiClient: GoogleApiClient? = null
    var savedinstancestate: Bundle? = null
    var doubleBackToExitPressedOnce = false
    private var lastSelectedMenuItem: MenuItem? = null
    @JvmField
    var isOverLay = false
     var userProfileResponse: UserProfile? = null
    var giftSubsFlag = false
    private var periodTrackerViewModel: PeriodTrackerViewModel? = null
    private var enrouteViewModel: EnrouteViewModel? = null
    private val userProfileMergedResponse: UserProfileMergedResponse? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var colBottomSheet: CoordinatorLayout? = null
    private var menu: Menu? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var homeViewModel: HomeViewModel? = null
    private var thirstReminderViewModel: ThirstReminderViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    var wolooTemp: EnrouteDirectionActivity.DirectionWoloo? = null
    private val mShowProfile = true
    private var isSubscription = false

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woloo_dashboard)

        FirebaseMessaging.getInstance().token.addOnSuccessListener { s ->
            Logger.i(
                Companion.TAG,
                "onCreate $s"
            )
        }
        println("We have " + FirebaseInAppMessaging.getInstance())
        ButterKnife.bind(this)
        val displayMetrics = resources.displayMetrics
        height = displayMetrics.heightPixels
        tv_woloo_store = findViewById<View>(R.id.tv_woloo_store) as TextView
        ll_nav_view = findViewById<View>(R.id.ll_nav_view) as LinearLayout
        ll_bottom_nav = findViewById<View>(R.id.ll_bottom_nav) as FrameLayout
        homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        thirstReminderViewModel = ViewModelProvider(this).get<ThirstReminderViewModel>(
            ThirstReminderViewModel::class.java
        )
        profileViewModel = ViewModelProvider(this).get<ProfileViewModel>(
            ProfileViewModel::class.java
        )
        periodTrackerViewModel = ViewModelProvider(this).get<PeriodTrackerViewModel>(
            PeriodTrackerViewModel::class.java
        )
        enrouteViewModel = ViewModelProvider(this).get<EnrouteViewModel>(
            EnrouteViewModel::class.java
        )
        mainToolbar = findViewById<Toolbar>(R.id.maintoolbar)
        imgSearch = findViewById<ImageView>(R.id.imgSearch)
        ivProfile = findViewById<CircleImageView>(R.id.ivProfile)
        txtName = findViewById<TextView>(R.id.txtName)
        imgScanQR = findViewById<ImageView>(R.id.imgScanQR)
        nav_view = findViewById<BottomNavigationView>(R.id.nav_view)
        colBottomSheet = findViewById<CoordinatorLayout>(R.id.colBottomSheet)
        val bottomSheet = findViewById<View>(R.id.bottom_sheet_layout)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val overlay_layout = findViewById<FrameLayout>(R.id.overlay_layout)

        nav_view!!.itemIconTintList = null;

        if (Companion.mSharedPreference == null) {
            Companion.mSharedPreference = SharedPreference(this)
        }
        if (Companion.mSharedPreference!!.getStoredPreference(
                this,
                SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(),
                ""
            ) != ""
        ) {
            startActivity(Intent(this@WolooDashboard, WahCertificateActivity::class.java))
        }
        if(mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED)
        {
            fabDashboardButton!!.visibility = View.VISIBLE
        }
        if (Companion.mSharedPreference!!.getStoredBooleanPreference(
                this,
                SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED.getPreferenceKey(),
                true
            )
        ) {
            overlay_layout.visibility = View.VISIBLE
            isOverLay = true
        } else {
            overlay_layout.visibility = View.GONE
            isOverLay = false
        }
        val i = intent
        val fragmentType = i.getStringExtra("FRAGMENT")
        if (fragmentType != null) {
            if (ARG_IS_SHOW_PERIOD_TRACKER === "PERIOD_TRACKER_FRAGMENT") {
                currentFragment = PeriodTrackerFragment()
                loadFragment(currentFragment, PeriodTrackerFragment.TAG)
            }
        }
        setLiveData()

        mBottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fabDashboardButton!!.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // Handle the BottomSheet expanded state
                        fabDashboardButton!!.visibility = View.GONE
                    }

                }
            }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }
                })

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Logger.i(TAG, "activity launcher")
                if (currentFragment != null) {
                    if (currentFragment !is PeriodTrackerFragment) {
                        currentFragment = PeriodTrackerFragment()
                        loadFragment(currentFragment, PeriodTrackerFragment.TAG)
                        blogType = "2"
                    }
                }
            }
        }

        overlay_layout.setOnClickListener { v: View? ->
            Companion.mSharedPreference!!.setStoredBooleanPreference(
                this,
                SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED.getPreferenceKey(),
                false
            )
            overlay_layout.visibility = View.GONE
          /*  if (currentFragment is HomeFragment) {
                (currentFragment as HomeFragment?)?.hideAndShow(false)
            }
            hideAndShow(false)*/
        }
        tv_woloo_store!!.setOnClickListener {
            try {
              /*  if (currentFragment is HomeFragment) {
                    (currentFragment as HomeFragment?)?.hideAndShow(isSectionShow)
                }
                hideAndShow(isSectionShow)*/
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
        profileImage!!.setOnClickListener {
            if (userProfileResponse != null) {
                WolooApplication.instance!!.profileResponse = userProfileResponse
            }
            startActivity(Intent(applicationContext, EditProfileActivity::class.java))
        }

        if (intent != null && (intent.getBooleanExtra(
                ARG_IS_SHOW_PERIOD_TRACKER,
                false
            ) || intent.action != null && intent.action == AppConstants.PERIOD_TRACKER_NOTIFICATION)
        ) {
            currentFragment = PeriodTrackerFragment()
            loadFragment(currentFragment, PeriodTrackerFragment.TAG)
            blogType = "2"
        } else if (intent != null && (intent.getBooleanExtra(
                ARG_IS_SHOW_TRENDING_BLOGS,
                false
            ) || intent.action != null && intent.action == AppConstants.BLOG_CONTENT_NOTIFICATION)
        ) {
            val blogLink = intent.extras!!.getString("blog_link")
            if (blogLink != null) { // App in the background
                /*Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.ARG_BLOG_URL, blogLink);
                startActivity(intent);
                finish();
                return;*/
                hideToolbar()
                currentFragment = newInstance("Blog", blogLink, Companion.TAG)
                loadFragment(currentFragment, WebViewFragment.TAG)
            } else {
                currentFragment = ContentCommerceFragment()
                loadFragment(currentFragment, ContentCommerceFragment.TAG)
                blogType = "0"
            }
        } else if (intent != null && (intent.getBooleanExtra(
                ARG_IS_SHOW_SHOP,
                false
            ) || intent.action != null && intent.action == AppConstants.PRODUCT_OFFER_NOTIFICATION)
        ) {
            val code = intent.extras!!.getString("coupon_code")
            if (code != null) {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("woloo_coupon", code)
                clipboard.setPrimaryClip(clip)
                currentFragment = ShoppingFragment.newInstance("coupon", code)
                Toast.makeText(this, "Coupon copied!", Toast.LENGTH_SHORT).show()
            } else {
                currentFragment = ShoppingFragment()
            }
            loadFragment(currentFragment, PeriodTrackerFragment.TAG)
            blogType = "2"
        } else if (intent != null && intent.action != null && intent.action == AppConstants.SHOW_OFFER_CART) {
            hideToolbar()
            loadFragment(MyHistoryFragment.newInstance("", "", true), "MyHistoryFragment")
        } else if (checkForDeeplink()) {
        } else {
            currentFragment = HomeFragment()
            loadFragment(currentFragment, "HomeFragment")
        }
        if (intent != null && intent.getBooleanExtra(ARG_IS_SHOW_THIRST_REMINDER, false)) {
            showThirstReminder()
        }
        if (currentFragment !is HomeFragment) {
            bottomViewTransparent()
        }
        nav_view!!.menu.getItem(2).setChecked(true)
        changeIcon(nav_view!!.menu.getItem(2))

        nav_view!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //loadMarkerFragment(new WolooStoreInfoFragment(),"WolooStoreInfoFragment");
        imgSearch!!.setOnClickListener(View.OnClickListener {
            //startActivity(new Intent(WolooDashboard.this, SearchWolooActivity.class));
            if (userProfileResponse != null) {
                val dc = DateToLocalDateConverter()
                val localExpiryDateIs: String =
                    dc.dateConvert(userProfileResponse!!.profile?.expiryDate.toString())
                if (!CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {
                    startActivity(Intent(this@WolooDashboard, EnrouteDirectionActivity::class.java))
                    //                    startActivity(new Intent(WolooDashboard.this, SearchWolooActivity.class));
                } else {
                    displayToast(resources.getString(R.string.expired_text))
                }
            }
        })
        imgScanQR!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@WolooDashboard, QRcodeScannerActivity::class.java)
            if (userProfileResponse != null) {
                val viewProfileInString = Gson().toJson(userProfileResponse)
                intent.putExtra(AppConstants.VIEW_PROFILE_STRING, viewProfileInString)
            }
            startActivity(intent)
        })
        if (intent.hasExtra("goToHome")) {
            nav_view!!.selectedItemId = R.id.navigation_dash_home
        }

        profileViewModel!!.getUserProfile()
        if (SharedPrefSettings.Companion.getPreferences.fetchIsDirectionWoloo()) {
            checkIfUserReachedLocation()
        }
    }

    private fun setLiveData() {
        homeViewModel!!.observeVoucher().observe(this) { voucherBaseResponse ->
            if (voucherBaseResponse != null && voucherBaseResponse.data != null) {
                dismissExpiryPopup()
                if (!voucherBaseResponse.data!!.isAlreadyConsumed) {
                    if (voucherBaseResponse.data!!.isAlreadyApplied) {
                        showAlreadyVoucherDialog()
                    } else {
                        Companion.mSharedPreference!!.setStoredPreference(
                            this@WolooDashboard,
                            SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(),
                            ""
                        )
                        showFreeTrialDialog(
                            voucherBaseResponse.data!!.message,
                            voucherBaseResponse.data!!.days.toString(),
                            voucherBaseResponse.data!!.isLifetime,
                            voucherBaseResponse.data!!.typeOfVoucher,
                            voucherBaseResponse.data!!.expiryNote
                        )
                    }
                }
            } else {
                Companion.mSharedPreference?.setStoredPreference(
                    this@WolooDashboard,
                    SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(),
                    ""
                )
                if (!WolooApplication.errorMessage.isEmpty()) {
                    showdialog(WolooApplication.errorMessage)
                }
                WolooApplication.errorMessage = ""
            }
        }
        homeViewModel!!.observeShowProfile().observe(this) {
            //                if(showProfileResponseBaseResponse != null && showProfileResponseBaseResponse.getData() != null){
//                    if (showProfileResponseBaseResponse.getData().getShouldShowProfileForm()){
//                        launchProfileActivity();
//                    }
//                }
        }
        homeViewModel!!.observeValidateGiftCard()
            .observe(this) { validateGiftCardResponseBaseResponse ->
                Companion.mSharedPreference!!.setStoredPreference(
                    this@WolooDashboard,
                    SharedPreferencesEnum.GIFT_CARD_ID.getPreferenceKey(),
                    ""
                )
                if (validateGiftCardResponseBaseResponse != null && validateGiftCardResponseBaseResponse.data != null) {
//                    dismissExpiryPopup();
                    if (validateGiftCardResponseBaseResponse.data!!.showPopUp == 1) {
                        showGiftCardDialog(validateGiftCardResponseBaseResponse.data!!.message)
                    }
                } else {
//                    mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
//                    if (!WolooApplication.getErrorMessage().isEmpty()) {
//                        showdialog(WolooApplication.getErrorMessage());
//                    }
//                    showGiftCardDialog(WolooApplication.getErrorMessage());
                    WolooApplication.errorMessage = ""
                }
            }

        thirstReminderViewModel?.observeThirstReminder()
            ?.observe(this, Observer<BaseResponse<ThirstReminderResponse>> { response ->
                if (response != null && response.data != null) {
                    response.data!!.thirstReminderHours?.let { hours ->
                        if (hours != 0) {
                            setAlarm(hours)
                            mSharedPreference?.setStoredPreference(this, SharedPreferencesEnum.THIRST_REMINDER.preferenceKey, hours.toString())
                        }
                    }
                } else {
                    WolooApplication.errorMessage = ""
                }
            })


        periodTrackerViewModel?.observeGetPeriodTracker()
            ?.observe(this, Observer<BaseResponse<PeriodTrackerResponse.Data>> { getPeriodDataResponse ->
            try {
                if (getPeriodDataResponse != null && getPeriodDataResponse.data != null) {
                    if (mSharedPreference == null) {
                        mSharedPreference = SharedPreference(baseContext)
                    }

                    mSharedPreference!!.setStoredPreference(baseContext, SharedPreferencesEnum.PERIOD_STARTING_DATE.preferenceKey, getPeriodDataResponse.data!!.periodDate)
                    mSharedPreference!!.setStoredPreference(baseContext, SharedPreferencesEnum.PERIOD_LENGTH.preferenceKey, getPeriodDataResponse.data!!.periodLength.toString())
                    mSharedPreference!!.setStoredPreference(baseContext, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.preferenceKey, getPeriodDataResponse.data!!.cycleLenght.toString())
                    mSharedPreference!!.setStoredPreference(applicationContext, SharedPreferencesEnum.DAILY_LOG.preferenceKey, Gson().toJson(getPeriodDataResponse.data!!.log))

                    val log = getPeriodDataResponse.data!!.log
                    try {
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.BLEEDING, log?.bleeding!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.MOOD, log?.mood!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.HABITS, log?.habits!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE, log?.sexDrive!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.PREMENSTRUATION, log?.premenstruation!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION, log?.diseasesandmedication!!)
                        DayLogUtils.instance?.setDailyLog(SharedPreferencesEnum.MENSTRUATION, log?.menstruation!!)
                    } catch (e: NullPointerException) {
                        activityResultLauncher.launch(Intent(this, EditCycleActivity::class.java))
                        return@Observer
                    }

                    val periodStartDate = Calendar.getInstance()
                    val startDate = getPeriodDataResponse.data!!.periodDate
                    if (startDate!!.isNotEmpty()) {
                        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        try {
                            periodStartDate.time = format.parse(startDate) ?: Date()
                        } catch (e: ParseException) {
                            CommonUtils.printStackTrace(e)
                        }
                    }

                    periodStartDate.add(Calendar.DATE, -36)
                    if (Calendar.getInstance().after(periodStartDate)) {
                        currentFragment = PeriodTrackerFragment()
                        loadFragment(currentFragment, PeriodTrackerFragment.TAG)
                        blogType = "2"
                    } else {
                        activityResultLauncher?.launch(Intent(this, EditCycleActivity::class.java))
                    }
                } else {
                    activityResultLauncher?.launch(Intent(this, EditCycleActivity::class.java))
                }
            } catch (ex: Exception) {
                Logger.e("TAG", ex.javaClass.simpleName)
               // if (ex is JsonSyntaxException || ex is NumberFormatException) {
                    activityResultLauncher?.launch(Intent(this, EditCycleActivity::class.java))
                /*} else {
                    CommonUtils.printStackTrace(ex)
                }*/
            }
        })


        profileViewModel?.observeUserProfile()
            ?.observe(this, Observer<BaseResponse<UserProfile>> { viewProfileResponse ->
                Logger.i(TAG, "setProfileResponse")
                progressBar?.visibility = View.VISIBLE

                if (viewProfileResponse != null && viewProfileResponse.data != null) {
                    userProfileResponse = viewProfileResponse.data
                    val dc = DateToLocalDateConverter()
                    val localExpiryDateIs = dc.dateConvert(userProfileResponse?.profile?.expiryDate.toString())
                    Logger.d("Local Date to Test is : ", "${userProfileResponse?.profile?.expiryDate} $localExpiryDateIs")

                    if (!TextUtils.isEmpty(viewProfileResponse.data!!.profile?.name)) {
                        txtName?.text = commonUtils!!.getFirstLaterCaps(viewProfileResponse.data!!.profile?.name!!.split(" ")[0])
                    } else {
                        txtName?.text = " Guest"
                    }

                    NetcoreUserDetails(this).updateNetcoreUserProfile(
                        viewProfileResponse.data!!.profile?.roleId,
                        viewProfileResponse.data!!.profile?.name,
                        viewProfileResponse.data!!.profile?.email,
                        viewProfileResponse.data!!.profile?.gender,
                        viewProfileResponse.data!!.profile?.dob,
                        viewProfileResponse.data!!.profile?.expiryDate,
                        viewProfileResponse.data!!.profile?.subscriptionId == null && viewProfileResponse.data!!.profile?.voucherId == null
                    )

                    if (mSharedPreference?.getStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.preferenceKey, "")!!.isNotEmpty()) {
                        applyVoucher(false)
                    }

                    if (mSharedPreference?.getStoredPreference(this, SharedPreferencesEnum.GIFT_CARD_ID.preferenceKey, "")!!.isNotEmpty()) {
                        homeViewModel?.validateGiftCard(mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.GIFT_CARD_ID.preferenceKey, "").toString()).toString()
                    }

                    if (mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.preferenceKey, "")!!.isNotEmpty()) {
                        giftSubsFlag = true
                        navigateToGiftSubscription()
                    }

                    val expireDate = viewProfileResponse.data!!.profile?.expiryDate
                    val fragment = (this as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frm_contant)
                    if (fragment is HomeFragment) {
                        fragment.setSubscriptionDetails(expireDate, userProfileResponse!!)
                    }

                    mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                    fabDashboardButton!!.visibility = View.VISIBLE
                } else {
                    WolooApplication.errorMessage = ""
                }

                txtName?.visibility = View.VISIBLE
                if (viewProfileResponse == null || viewProfileResponse.data == null || viewProfileResponse.data!!.profile == null ||
                    viewProfileResponse.data!!.profile?.avatar == null) {
                    ivProfile?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_circle))
                } else {
                    val avatar = viewProfileResponse.data!!.profile?.avatar
                    if (TextUtils.isEmpty(avatar) || avatar?.trim() == "users/default.png" || avatar?.trim() == "default.png") {
                        ImageUtil.loadImageProfile(this, ivProfile!!, "${BuildConfig.BASE_URL}public/userProfile/default.png")
                    } else {
                        ImageUtil.loadImageProfile(this, ivProfile!!, "${viewProfileResponse.data!!.profile?.baseUrl}$avatar")
                    }
                }

                progressBar?.visibility = View.GONE
            })


        enrouteViewModel!!.observeWolooNavigationReward().observe(this) { navigationRewardsResponse ->
            try {
                if (navigationRewardsResponse != null && navigationRewardsResponse.success) {
                    showdialogForNavigationReward(true)
                } else {
                    showdialogForNavigationReward(false)
                    WolooApplication.errorMessage = ""
                    // Toast.makeText(requireContext(), "You have arrived at your destination.", Toast.LENGTH_SHORT).show()
                }
                val bundle = Bundle()
                // bundle.put...
                Utility.logFirebaseEvent(applicationContext, bundle, AppConstants.DESTIONATION_REACHED)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }


    }

    /*calling on resume*/
    override fun onResume() {
        super.onResume()
        Logger.i(Companion.TAG, "onResume")
        if (giftSubsFlag) {
            giftSubsFlag = false
            // String expireDate = userProfileResponse.getProfile().getExpiryDate();
            val dc = DateToLocalDateConverter()
            val expireDate: String = dc.dateConvert(userProfileResponse!!.profile?.expiryDate.toString())
            val fragment =
                (this as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frm_contant)
            if (fragment != null) {
                if (fragment is HomeFragment) {
                    (fragment as HomeFragment).setSubscriptionDetails(
                        expireDate,
                        userProfileResponse!!
                    )
                }
            }
        }
        //        showGiftCardDialog("Congratulation!!! \nYou have received giftcard");
        homeViewModel!!.showProfile(CommonUtils().userInfo!!.id.toString())
    }

    val userProfile: Unit
        get() {
            if (isSubscription) {
                profileViewModel!!.getUserProfile()
            }
        }

    private fun checkIfUserReachedLocation() {
        Logger.i(Companion.TAG, "checkIfUserReachedLocation")
        try {
            //Changes done by Aarati july 2024 @ woloo
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permissions already granted
                // You can proceed with using location
            } else {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(OnCompleteListener<Location?> { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val location = task.result ?: return@OnCompleteListener
                    //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    wolooTemp = SharedPrefSettings.Companion.getPreferences.fetchDirectionWoloo()
                    val destLatLng = LatLng(wolooTemp!!.lat, wolooTemp!!.lng)
                    val url = getDirectionsUrl(currentLatLng, destLatLng)
                    Logger.e("url", "" + url)
                    ApiServiceClientAdapter.Companion.instance.apiService
                        .getDirections(url)
                        .enqueue(object : Callback<GetDistance?> {
                            override fun onResponse(
                                call: Call<GetDistance?>,
                                response: Response<GetDistance?>
                            ) {
                                val getdistance: GetDistance? = response.body()
                                val distance: Int =
                                    getdistance!!.routes[0].legs[0].distance
                                        .value
                                if (distance < 50) {
                                    enrouteViewModel!!.getWolooNavigationReward(wolooTemp!!.wolooid)
                                }
                            }

                            override fun onFailure(call: Call<GetDistance?>, t: Throwable) {}
                        })
                }
            })
        } catch (e: Exception) {
            Logger.e("Exception: %s", e.message!!)
        }
    }

    private fun getDirectionsUrl(
        origin: LatLng,
        dest: LatLng
    ): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        var mode = "mode=driving"
        if (Companion.mSharedPreference == null) {
            Companion.mSharedPreference = SharedPreference(this)
        }
        val transport_mode: String =
            Companion.mSharedPreference!!.getStoredPreference(
                this,
                SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                "0"
            ).toString()
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
            "2" -> mode = "mode=bicycling"
        }
        // Building the parameters to the web service
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        val key = "key=" + CommonUtils.googlemapapikey(this)
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Logger.i(Companion.TAG, "newIntent")
    }

    fun showExpiryPopup() {
        if (userProfileResponse == null) return
        val dc = DateToLocalDateConverter()
        val expireDate: String = dc.dateConvert(userProfileResponse!!.profile?.expiryDate.toString())
        // String expireDate = userProfileResponse.getProfile().getExpiryDate();
        val fragment =
            (this as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frm_contant)
        if (fragment != null) {
            if (fragment is HomeFragment) {
                (fragment as HomeFragment).setSubscriptionDetails(expireDate, userProfileResponse!!)
            }
        }
    }

    fun dismissExpiryPopup() {
        if (userProfileResponse == null) return
        val fragment =
            (this as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frm_contant)
        if (fragment != null) {
            if (fragment is HomeFragment) {
                (fragment as HomeFragment).dismissVoucherExpireDialogs()
            }
        }
    }

    /*calling hide mainToolbar*/
    fun hideToolbar() {
        Logger.i(Companion.TAG, "hideToolbar")
        try {
            mainToolbar!!.visibility = View.GONE
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling showtoolbar*/
    fun showToolbar() {
        Logger.i(Companion.TAG, "showToolbar")
        try {
            mainToolbar!!.visibility = View.VISIBLE
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling showFooter*/
    fun showFooter() {
        Logger.i(Companion.TAG, "showFooter")
        try {
            ll_bottom_nav!!.visibility = View.VISIBLE
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling hideFooter*/
    fun hideFooter() {
        Logger.i(Companion.TAG, "hideFooter")
        try {
            ll_bottom_nav!!.visibility = View.GONE
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    @Optional
    @OnClick(R.id.fabHomeDashboard)
    fun onClickFab() {
        //bottomViewTransparent();
        fabDashboardButton!!.visibility = View.GONE
        handleFabClick()


    }

    @Optional
    @OnClick(R.id.shop_layout)
    fun onClickShop() {
        if (currentFragment !is ShoppingFragment) {
            val bundle = Bundle()
            Utility.logFirebaseEvent(this@WolooDashboard, bundle, AppConstants.SHOP_CLICK)
            Utility.logNetcoreEvent(this@WolooDashboard, HashMap(), AppConstants.SHOP_CLICK)
            unCheckMenuItem()
            currentFragment = ShoppingFragment()
            loadFragment(currentFragment, "ShoppingFragment")
        }
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
        changeIcon(nav_view!!.menu.getItem(1))
        //((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
    }

    @Optional
    @OnClick(R.id.loo_layout)
    fun onClickLocateLoo() {
        // load Home
        if (currentFragment !is HomeFragment) {
            unCheckMenuItem()
            profileViewModel!!.getUserProfile()
            currentFragment = HomeFragment()
            loadFragment(currentFragment, "HomeFragment")
            bottomViewTransparent90()
        }
        changeIcon(nav_view!!.menu.getItem(2))
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
    }

    @Optional
    @OnClick(R.id.period_tracker_layout)
    fun onClickPeriodTracker() {
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
        if (currentFragment !is PeriodTrackerFragment) {
            unCheckMenuItem()
            //            periodTrackerPresenter.getPeriodData();
            periodTrackerViewModel!!.getPeriodTracker()
          /*  currentFragment = PeriodTrackerFragment()
            loadFragment(currentFragment, "PeriodTrackerFragment")*/
            bottomViewTransparent90()
            blogType = "2"
        }
    }




    @Optional
    @OnClick(R.id.thirst_layout)
    fun onClickThirstReminder() {
        showThirstReminder()
    }

    @Optional
    @OnClick(R.id.profile_layout)
    fun onClickProfile() {
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
        currentFragment = MoreFragment()
        loadFragment(currentFragment, "MoreFragment")
        changeIcon(nav_view!!.menu.getItem(3))
    }

    @Optional
    @OnClick(R.id.home_layout)
    fun onClickHome() {
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
        currentFragment = ContentCommerceFragment()
        loadFragment(currentFragment, "DashboardHomeFragment")
        changeIcon(nav_view!!.menu.getItem(0))
        blogType = "0"
    }

    @Optional
    @OnClick(R.id.blogs_layout)
    fun onClickBlogs() {
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
        currentFragment = TrendBlogFragment()
        loadFragment(currentFragment, "DashboardHomeFragment")
       // changeIcon(nav_view!!.menu.getItem(0))
        blogType = "1"
    }



    @Optional
    @OnClick(R.id.fabHomeDashboardDown)
    fun onClickFabHomeDashboardDown(){
        fabDashboardButton!!.visibility = View.VISIBLE
        handleFabClick()
    }

    private fun showThirstReminder() {
        val thirstReminderStr: String = Companion.mSharedPreference!!.getStoredPreference(
            this,
            SharedPreferencesEnum.THIRST_REMINDER.getPreferenceKey(),
            "0"
        ).toString()
        try {
            val reminderHours = thirstReminderStr.toInt()
            if (reminderHours > 0) {
                SaveThirstReminderDialog(
                    reminderHours
                ).show(
                    supportFragmentManager,
                    SaveThirstReminderDialog.TAG
                )
            } else {
                ThirstReminderDialog()
                    .show(supportFragmentManager, ThirstReminderDialog.TAG)
            }
        } catch (e: NumberFormatException) {
        }
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
    }

    @Optional
    @OnClick(R.id.community_layout)
    fun onClickWolooCommunity() {
        if (currentFragment !is WebViewFragment) {
            unCheckMenuItem()
            hideToolbar()
            val webViewFragment = newInstance(
                "Woloo Community",
                "https://iamhere.app/community/women_hygiene_1624314543470",
                Companion.TAG
            )
            loadMenuFragment(webViewFragment, WebViewFragment.TAG)

            /*int colorInt = Color.parseColor("#FFFFFF");
            CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(colorInt)
                    .build();

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setDefaultColorSchemeParams(defaultColors);
            CustomTabsIntent customTabsIntent = builder.build();

            customTabsIntent.launchUrl(this, Uri.parse("https://iamhere.app/community/women_hygiene_1624314543470"));*/
        }
        mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        fabDashboardButton!!.visibility = View.VISIBLE
    }

    private fun unCheckMenuItem() {

        /*nav_view.getMenu().setGroupCheckable(0, false, false);
                int size = nav_view.getMenu().size();
                for (int i = 0; i < size; i++) {
                    nav_view.getMenu().getItem(i).setChecked(false);
                    nav_view.getMenu().getItem(i).setCheckable(false);
                }
                nav_view.getMenu().setGroupCheckable(0, true, true);*/
        //changeIcon(nav_view!!.menu.findItem(R.id.navigation_home))
    }

    /*calling onCreateOptionsMenu*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Logger.i(Companion.TAG, "onCreateOptionsMenu")
        this.menu = menu
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        //            menu.findItem(R.id.navigation_shop).setIcon(drawableResourceId);
        return true
    }

    /*calling changeIcon*/
    fun changeIcon(item: MenuItem) {
        Logger.i(Companion.TAG, "changeIcon")
        nav_view!!.itemIconTintList = null;
        runOnUiThread {
            if (lastSelectedMenuItem != null) {
                if (lastSelectedMenuItem!!.itemId == R.id.navigation_dash_home) {
                    lastSelectedMenuItem!!.setIcon(R.drawable.home_icon_bottom_nav)
                }
                if (lastSelectedMenuItem!!.itemId == R.id.navigation_inviteFriend) {
                    lastSelectedMenuItem!!.setIcon(R.drawable.shop_icon_bottom_nav)
                }
                if (lastSelectedMenuItem!!.itemId == R.id.navigation_location) {
                    lastSelectedMenuItem!!.setIcon(R.drawable.location_icon_bottom_nav)
                }
                if (lastSelectedMenuItem!!.itemId == R.id.navigation_more) {
                    lastSelectedMenuItem!!.setIcon(R.drawable.user_icon_bottom_nav)
                }
            }
            if (item.itemId == R.id.navigation_dash_home) {
//                    ((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
                item.setIcon(R.drawable.home_icon_botton_nav_selected)
            } else if (item.itemId == R.id.navigation_inviteFriend) {
                item.setIcon(R.drawable.shop_icon_botton_nav_selected)
            } /*else if(item.getItemId()==R.id.navigation_home){
                    item.setIcon(R.drawable.ic_selected_shop);
                }*/ else if (item.itemId == R.id.navigation_location) {
                item.setIcon(R.drawable.location_icon_botton_nav_selected)
            } else if (item.itemId == R.id.navigation_more) {
                item.setIcon(R.drawable.profile_icon_botton_nav_selected)
            }
            lastSelectedMenuItem = item
        }
    }

    private val drawableResourceId: Int = R.drawable.ic_selected_shop
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            bottomViewTransparent()
            if (mBottomSheetBehavior != null) {
                if (mBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            when (item.itemId) {
                R.id.navigation_dash_home -> if (currentFragment !is ContentCommerceFragment) {
                    val bundle = Bundle()
                    Utility.logFirebaseEvent(
                        this@WolooDashboard,
                        bundle,
                        AppConstants.DASH_HOME_CLICK
                    )
                    changeIcon(item)
                    blogType = "0"

                    currentFragment = ContentCommerceFragment()
                    loadFragment(currentFragment, "DashboardHomeFragment")
                    //                    ((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
                    true
                }

                R.id.navigation_inviteFriend ->  if (currentFragment !is ShoppingFragment) {
                    val bundle = Bundle()
                    Utility.logFirebaseEvent(this@WolooDashboard, bundle, AppConstants.SHOP_CLICK)
                    Utility.logNetcoreEvent(this@WolooDashboard, HashMap(), AppConstants.SHOP_CLICK)
                    unCheckMenuItem()
                    changeIcon(item)
                    currentFragment = ShoppingFragment()
                    loadFragment(currentFragment, "ShoppingFragment")
                }


                    /* if (currentFragment !is InviteFriendFragment) {
                    val bundle = Bundle()
                    Utility.logFirebaseEvent(this@WolooDashboard, bundle, AppConstants.INVITE_CLICK)
                    val payload = HashMap<String, Any>()
                    Utility.logNetcoreEvent(this@WolooDashboard, payload, AppConstants.INVITE_CLICK)
                    changeIcon(item)
                    currentFragment = InviteFriendFragment.newInstance(false)
                    loadFragment(currentFragment, "InviteFriendFragment")
                    //  bottomViewTransparent90();
                }*/


                R.id.navigation_location -> if (currentFragment !is HomeFragment) {
                unCheckMenuItem()
                profileViewModel!!.getUserProfile()
                currentFragment = HomeFragment()
                loadFragment(currentFragment, "HomeFragment")
                bottomViewTransparent90()
                    changeIcon(item)
            }
                    /*if (currentFragment !is MyAccountFragment) {
                    val bundle = Bundle()
                    //bundle.put
                    Utility.logFirebaseEvent(
                        this@WolooDashboard,
                        bundle,
                        AppConstants.MY_ACCOUNT_CLICK
                    )
                    Utility.logNetcoreEvent(
                        this@WolooDashboard,
                        HashMap(),
                        AppConstants.MY_ACCOUNT_CLICK
                    )
                    changeIcon(item)
                    currentFragment = MyAccountFragment.newInstance(false)
                    loadFragment(currentFragment, "MyAccountFragment")
                }*/



                R.id.navigation_more -> if (currentFragment !is MoreFragment) {
                    changeIcon(item)
                    currentFragment = MoreFragment()
                    loadFragment(currentFragment, "MoreFragment")
                    true
                }

              //  R.id.navigation_home -> handleFabClick()
            }
            false
        }

    private fun handleFabClick() {
        if (mBottomSheetBehavior != null) {
            if (mBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
                Logger.d("state is" , "STATE_COLLAPSED")
            } else {
                mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
                fabDashboardButton!!.visibility = View.VISIBLE
                try {
                    if (currentFragment is HomeFragment) {
                        if (tv_woloo_store!!.visibility == View.GONE && !(currentFragment as HomeFragment?)?.isWolooListVisible!!) {
                            bottomViewTransparent90()
                        }
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        removeWolooStoreInfo()
        val fragment =
            (this as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.frm_contant)
        if (fragment != null) {
            if (fragment is ContentCommerceFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(
                    applicationContext,
                    "Please click BACK again to exit",
                    Toast.LENGTH_SHORT
                ).show()
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            } else if (fragment is MoreFragment ||
                fragment is ShoppingFragment
            ) {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack()
                } else {
                    nav_view!!.selectedItemId = R.id.navigation_location
                }
            } else if (fragment is MyHistoryFragment) {
                try {
                    //getActivity().onBackPressed();
                    val fm = supportFragmentManager
                    if (fm.backStackEntryCount > 0) {
                        fm.popBackStack()
                    } else {
                        loadFragment(ContentCommerceFragment(), ContentCommerceFragment.TAG)
                        changeIcon(nav_view!!.menu.findItem(R.id.navigation_dash_home))
                        blogType = "0"
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
            else if (fragment is TrendBlogFragment) {
                try {
                    //getActivity().onBackPressed();
                    val fm = supportFragmentManager
                    if (fm.backStackEntryCount > 0) {
                        fm.popBackStack()
                    } else {
                        loadFragment(TrendBlogFragment(), TrendBlogFragment.TAG)
                        changeIcon(nav_view!!.menu.findItem(R.id.navigation_dash_home))
                        blogType = "0"
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
            else {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack()
                } else {
                    currentFragment = ContentCommerceFragment()
                    loadFragment(currentFragment, ContentCommerceFragment.TAG)
                    changeIcon(nav_view!!.menu.findItem(R.id.navigation_dash_home))
                    blogType = "0"
                }
            }
        } else super.onBackPressed()
    }

    /*calling load fragment*/
    fun loadFragment(fragment: Fragment?, homeTah: String?) {
        Logger.i(Companion.TAG, "loadFragment")
        currentFragment = fragment
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frm_contant, fragment!!, homeTah)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling loadMenuFragment*/
    fun loadMenuFragment(fragment: Fragment?, homeTah: String?) {
        Logger.i(Companion.TAG, "loadMenuFragment")
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frm_contant, fragment!!, homeTah)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling loadMarkerFragmentWithIndex*/
    fun loadMarkerFragmentWithIndex(index: Int, dataList: List<NearByStoreResponse.Data?>?) {
        Logger.i(Companion.TAG, "loadMarkerFragmentWithIndex")
        wolooStoreInfoFragment = WolooStoreInfoFragment()
        wolooStoreInfoFragment!!.setIndex(index)
        wolooStoreInfoFragment!!.setDataList(dataList)
        loadMarkerFragment(wolooStoreInfoFragment, "WolooStoreInfoFragment")
    }

    /*calling removeWolooStoreInfo*/
    fun removeWolooStoreInfo() {
        if (wolooStoreInfoFragment != null && wolooStoreInfoFragment!!.isAdded()) {
            Logger.i(Companion.TAG, "removeWolooStoreInfo")
            try {
                val fragmentManager = (this as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction().remove(wolooStoreInfoFragment!!).commit()
               /* if (currentFragment is HomeFragment) {
                    (currentFragment as HomeFragment?)?.hideAndShow(true)
                }*/
                //  fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frm_marker_detail)).commit();
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
    }

    /*calling moveMarkerToIndex*/
    fun moveMarkerToIndex(index: Int) {
        Logger.i(Companion.TAG, "moveMarkerToIndex")
        if (currentFragment is HomeFragment) {
            (currentFragment as HomeFragment?)?.animateCameraToMarkerPosition(index)
        }
    }

    /*calling loadMarkerFragment*/
    fun loadMarkerFragment(fragment: Fragment?, homeTah: String?) {
        Logger.i(Companion.TAG, "loadMarkerFragment")
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_up,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,  // popEnter
                R.anim.slide_out_up // popExit
            )
            fragmentTransaction.replace(R.id.frm_marker_detail, fragment!!, homeTah)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling hideAndShow*/
    fun hideAndShow(status: Boolean) {
        Logger.i(Companion.TAG, "hideAndShow")
        if (status) {
            isSectionShow = false
            bottomViewTransparent90()
        } else {
            isSectionShow = true
            bottomViewTransparent()
        }
    }

    fun hideAndShowBlog(status: Boolean) {
        Logger.i(Companion.TAG, "hideAndShow")
        if (status) {
            isSectionShow = false
            bottomViewTransparent90Blog()
        } else {
            isSectionShow = true
            bottomViewTransparentBlog()
        }
    }

    /*calling bottomViewTransparent90*/
    private fun bottomViewTransparent90() {
        Logger.i(Companion.TAG, "bottomViewTransparent90")
        tv_woloo_store!!.visibility = View.VISIBLE
        fadeInAnimation(tv_woloo_store)
        ll_nav_view!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
        ll_bottom_nav!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
    }

    private fun bottomViewTransparent90Blog() {
        Logger.i(Companion.TAG, "bottomViewTransparent90")
        tv_woloo_store!!.visibility = View.GONE
        ll_nav_view!!.visibility = View.GONE
        ll_bottom_nav!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
    }

    /*calling bottomViewTransparent*/
    private fun bottomViewTransparent() {
        Logger.i(Companion.TAG, "bottomViewTransparent")
        tv_woloo_store!!.visibility = View.GONE
        ll_nav_view!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
        ll_bottom_nav!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
    }

    private fun bottomViewTransparentBlog() {
        Logger.i(Companion.TAG, "bottomViewTransparent")
        tv_woloo_store!!.visibility = View.GONE
        ll_nav_view!!.visibility = View.GONE
        ll_bottom_nav!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext, R.color.transparent
            )
        )
    }

    /*calling fadeInAnimation*/
    private fun fadeInAnimation(view: TextView?) {
        Logger.i(Companion.TAG, "fadeInAnimation")
        val animFadeIn = AnimationUtils.loadAnimation(
            applicationContext, R.anim.fade_in
        )
        view!!.startAnimation(animFadeIn)
        animFadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    /*calling fadeOutAnimation*/
    private fun fadeOutAnimation(view: TextView) {
        Logger.i(Companion.TAG, "fadeOutAnimation")
        val animFadeOut = AnimationUtils.loadAnimation(
            applicationContext, R.anim.fade_out
        )
        view.startAnimation(animFadeOut)
        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.INVISIBLE
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun launchProfileActivity() {
        startActivity(Intent(this@WolooDashboard, ProfileActivity::class.java))
    }

    private fun applyVoucher(isForce: Boolean) {
        val request = VoucherRequest()
        request.voucherCode = Companion.mSharedPreference!!.getStoredPreference(
            this,
            SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(),
            ""
        ).toString()
        request.forceApply = isForce
        homeViewModel!!.applyVoucher(request)
    }

    private fun navigateToGiftSubscription() {
        startActivity(Intent(this@WolooDashboard, GiftSubscriptionActivity::class.java))
    }

    private fun navigateToGiftCardScreen() {
        Logger.i(Companion.TAG, "navigateToGiftCardScreen")
        try {
            var isEmail = false
            var email = ""
            var mobile = ""
            if (!TextUtils.isEmpty(userProfileResponse!!.profile?.mobile)) {
                mobile = userProfileResponse!!.profile?.mobile.toString()
            } else if (!TextUtils.isEmpty(userProfileResponse!!.profile?.email)) {
                isEmail = true
                email = userProfileResponse!!.profile?.email.toString()
            }
            hideToolbar()
            loadMenuFragment(
                GiftCardFragment.newInstance(email, isEmail, mobile),
                "GiftCardFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun showdialog(msg: String?) {
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
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun navigateToSubscriptionScreen(isForSubscription: Boolean) {
        Logger.i(Companion.TAG, "navigateToSubscriptionScreen")
        try {
            var isEmail = false
            var email = ""
            var mobile = ""
            var subscriptionPlanName = ""
            if (userProfileResponse != null) {
                val dc = DateToLocalDateConverter()
                val localExpiryDateIs: String =
                    dc.dateConvert(userProfileResponse!!.profile?.expiryDate.toString())
                Logger.d(
                    "Local Date to Test is : ",
                    userProfileResponse!!.profile?.expiryDate + " " + localExpiryDateIs
                )
                if (!TextUtils.isEmpty(userProfileResponse!!.profile?.mobile)) {
                    mobile = userProfileResponse!!.profile?.mobile.toString()
                } else if (!TextUtils.isEmpty(userProfileResponse!!.profile?.email)) {
                    isEmail = true
                    email = userProfileResponse?.profile?.email.toString()
                }
                if (!CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {
                    subscriptionPlanName = ""
                }
            }
            hideToolbar()
            isSubscription = true
            loadMenuFragment(
                SubscribeFragment.newInstance(
                    subscriptionPlanName,
                    email,
                    isEmail,
                    mobile,
                    isForSubscription,
                    false,
                    ""
                ), "InviteFriendFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun showFreeTrialDialog(message: String?, days: String) {
        Logger.i(Companion.TAG, "showFreeTrialDialog")
        dismissExpiryPopup()
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val tv_startfreetrial = dialog.findViewById<View>(R.id.tv_startfreetrial) as TextView
            val ivFreeTrial = dialog.findViewById<View>(R.id.ivFreeTrial) as ImageView
            ivFreeTrial.visibility = View.VISIBLE
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llStartFreeTrial) as LinearLayout
            tv_startfreetrial.text = message
            llStartFreeTrial.setOnClickListener {
                profileViewModel?.getUserProfile()
                dialog.dismiss()
            }
            val tv_daysTrials = dialog.findViewById<View>(R.id.tv_daysTrials) as TextView
            if (days == "-1") {
                ivFreeTrial.visibility = View.GONE
                tv_daysTrials.visibility = View.GONE
            } else {
                if (days == "lifetime") {
                    tv_daysTrials.visibility = View.GONE
                    ivFreeTrial.setImageResource(R.drawable.unlimited_days_image)
                } else {
                    tv_daysTrials.text = "$days Days"
                    ivFreeTrial.setImageResource(R.drawable.free_trial_image)
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showFreeTrialDialog(
        message: String?,
        days: String,
        islifetime: Int,
        typeOfVoucher: String,
        expiryNote: String
    ) {
        Logger.i(Companion.TAG, "showFreeTrialDialog")
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val tv_startfreetrial = dialog.findViewById<View>(R.id.tv_startfreetrial) as TextView
            val tv_typeOfVoucher = dialog.findViewById<View>(R.id.tv_typeOfVoucher) as TextView
            val tv_expiryNote = dialog.findViewById<View>(R.id.tv_expiryNote) as TextView
            val ivFreeTrial = dialog.findViewById<View>(R.id.ivFreeTrial) as ImageView
            ivFreeTrial.visibility = View.GONE
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llStartFreeTrial) as LinearLayout
            tv_startfreetrial.text = message
            if (!expiryNote.isEmpty()) {
                tv_expiryNote.text = "Note: $expiryNote"
                tv_expiryNote.visibility = View.VISIBLE
            }
            llStartFreeTrial.setOnClickListener {
                profileViewModel?.getUserProfile()
                dialog.dismiss()
            }
            val tv_daysTrials = dialog.findViewById<View>(R.id.tv_daysTrials) as TextView
            if (days == "-1") {
                ivFreeTrial.visibility = View.GONE
                tv_daysTrials.visibility = View.GONE
            } else {
                if (islifetime == 1) {
                    tv_daysTrials.visibility = View.GONE
                    ivFreeTrial.setImageResource(R.drawable.unlimited_days_image)
                } else {
                    tv_daysTrials.text = "$days Days"
                    tv_typeOfVoucher.text = typeOfVoucher.uppercase(Locale.getDefault())
                   // tv_daysTrials.visibility == View.GONE
                    //                    if (typeOfVoucher.equalsIgnoreCase("free")) {
//                        ivFreeTrial.setImageResource(R.drawable.free_trial_image);
//                    }else{
//                        ivFreeTrial.setImageResource(R.drawable.paid_image);
//                    }
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showGiftCardDialog(message: String?) {
        Logger.i(Companion.TAG, "showFreeTrialDialog")
        dismissExpiryPopup()
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_gift_card_received)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val messageTV = dialog.findViewById<View>(R.id.tv_message) as TextView
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llStartFreeTrial) as LinearLayout
            messageTV.text = message
            llStartFreeTrial.setOnClickListener {
                profileViewModel?.getUserProfile()
                dialog.dismiss()
                //                    navigateToGiftSubscription();
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showAlreadyVoucherDialog() {
        Logger.i(Companion.TAG, "showFreeTrialDialog")
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.error_already_voucher)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llCancelBtn = dialog.findViewById<View>(R.id.llCancelBtn) as LinearLayout
            llCancelBtn.setOnClickListener {
                Companion.mSharedPreference?.setStoredPreference(
                    this,
                    SharedPreferencesEnum.VOUCHER_CODE.preferenceKey,
                    ""
                )
                dialog.dismiss()
            }
            val llStartFreeTrial = dialog.findViewById<View>(R.id.llOkayBtn) as LinearLayout
            llStartFreeTrial.setOnClickListener {
                dialog.dismiss()
                //morePresenter.getVoucherDetails(mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), ""));
                applyVoucher(true)
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun onClickThirstYes() {
        SaveThirstReminderDialog()
            .show(supportFragmentManager, SaveThirstReminderDialog.TAG)
    }

    override fun onClickThirstNo() {
        val request = ThirstReminderRequest()
        request.isThirstReminder = 0
        request.thirstReminderHours = 0
        thirstReminderViewModel?.thirstReminder(request)
        val payload = HashMap<String, Any>()
        payload[AppConstants.IS_THIRST_REMINDER] = 0
        payload[AppConstants.THIRST_REMINDER_HOURS] = 0
        Utility.logNetcoreEvent(this, payload, AppConstants.THIRST_REMINDER_CLICK)
    }

    override fun onClickSaveThirstReminder(hours: Int) {
        val request = ThirstReminderRequest()
        request.isThirstReminder = if (hours <= 0) 0 else 1
        request.thirstReminderHours = hours
        thirstReminderViewModel?.thirstReminder(request)
        val payload = HashMap<String, Any>()
        payload[AppConstants.IS_THIRST_REMINDER] = if (hours <= 0) 0 else 1
        payload[AppConstants.THIRST_REMINDER_HOURS] = hours
        Utility.logNetcoreEvent(this, payload, AppConstants.THIRST_REMINDER_CLICK)
    }

    private fun setAlarm(hours: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        notificationIntent.putExtra(AlarmReceiver.REMINDER_HOURS, hours)
        //java.lang.IllegalArgumentException: in.woloo.www: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
        //                                     Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        val pendingIntent = PendingIntent.getBroadcast(
            this.applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (hours > 0) {
            val calendar = Calendar.getInstance()
            //calendar.set(Calendar.HOUR, hours);
            Logger.i(Companion.TAG, calendar.toString())
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                hours * AlarmManager.INTERVAL_HOUR,
                pendingIntent
            )
            showdialog("Thirst reminder set for every $hours hour(s)")
        } else { //Cancel the alarm
            alarmManager.cancel(pendingIntent)
            showdialog("Thirst reminder cancelled!")
        }
    }

    //
    //    void openSettingsForGPS(IntentSender intentSender, int DIALOGID, Object o, int i, int i1, int i2, Object o1){
    //
    //    }
    fun checkForDeeplink(): Boolean {
        if (intent.hasExtra("deeplink")) {
            Logger.e(Companion.TAG, intent.getStringExtra("deeplink")!!)
            when (intent.getStringExtra("deeplink")) {
                "subscription" -> {
                    if (currentFragment !is MoreFragment) {
                        changeIcon(nav_view!!.menu.findItem(R.id.navigation_more))
                        currentFragment = MoreFragment()
                        loadFragment(currentFragment, "MoreFragment")
                    }
                    navigateToSubscriptionScreen(true)
                    return true
                }

                "shop" -> {
                    hideToolbar()
                    loadFragment(ShoppingFragment(), "ShoppingFragment")
                //    changeIcon(nav_view!!.menu.findItem(R.id.navigation_home))
                    return true
                }

                "refer" -> {
                    bottomViewTransparent()
                    if (currentFragment !is InviteFriendFragment) {
                        val bundle = Bundle()
                        Utility.logFirebaseEvent(
                            this@WolooDashboard,
                            bundle,
                            AppConstants.INVITE_CLICK
                        )
                        val payload = HashMap<String, Any>()
                        Utility.logNetcoreEvent(
                            this@WolooDashboard,
                            payload,
                            AppConstants.INVITE_CLICK
                        )
                        changeIcon(nav_view!!.menu.findItem(R.id.navigation_inviteFriend))
                        currentFragment = InviteFriendFragment.newInstance(false)
                        loadFragment(currentFragment, "InviteFriendFragment")
                        //  bottomViewTransparent90();
                    }
                    return true
                }
            }
        }
        return false
    }

    fun showdialogForNavigationReward(isPoints: Boolean) {
        try {
            SharedPrefSettings.Companion.getPreferences.storeIsDirectionWoloo(false)
            SharedPrefSettings.Companion.getPreferences.storeDirectionWoloo(null)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_destinationarrived)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val tv_text = dialog.findViewById<View>(R.id.tv_text) as TextView
            val tv_subscribe = dialog.findViewById<View>(R.id.tv_subscribe) as TextView
            val tv_text2 = dialog.findViewById<View>(R.id.tv_text2) as TextView
            tv_text2.visibility = View.GONE
            val authConfigResponse = CommonUtils.authconfig_response(this)
            //                tv_text.setText(getResources().getString(R.string.qrcode_scantext));
            if (authConfigResponse != null) {
                val arrivedDestinationDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()!!.arrivedDestinationText
                val arrivedDestinationPoints =
                    authConfigResponse.getcUSTOMMESSAGE()!!.arrivedDestinationPoints

                tv_text.text = arrivedDestinationDialogText!!.replace("\\\\n".toRegex(), "\n")
                if (isPoints) {
                    tv_text2.text = arrivedDestinationPoints
                    tv_text2.visibility = View.VISIBLE
                    tv_subscribe.visibility = View.GONE
                    val handler = Handler()
                    handler.postDelayed({
                        dialog.dismiss()
                        val intent = Intent(this@WolooDashboard, AddReviewActivity::class.java)
                        intent.putExtra(AppConstants.WOLOO_ID, wolooTemp!!.wolooid)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }, 3000)
                } else {
                    tv_text2.visibility = View.GONE
                    tv_subscribe.visibility = View.VISIBLE
                    tv_subscribe.text = "HOME"
                }
            }
            //                tv_subscribe.setText("SCAN QR CODE");
            tv_subscribe.setOnClickListener { if (dialog.isShowing) dialog.dismiss() }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        const val ARG_IS_SHOW_PERIOD_TRACKER = "ARG_IS_SHOW_PERIOD_TRACKER"
        const val ARG_IS_SHOW_TRENDING_BLOGS = "ARG_IS_SHOW_TRENDING_BLOGS"
        const val ARG_IS_SHOW_SHOP = "ARG_IS_SHOW_SHOP"
        const val ARG_IS_SHOW_THIRST_REMINDER = "ARG_IS_SHOW_THIRST_REMINDER"
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        var tv_woloo_store: TextView? = null
        var ll_nav_view: LinearLayout? = null
        var ll_bottom_nav: FrameLayout? = null
        protected var mSharedPreference: SharedPreference? = null
        var TAG = WolooDashboard::class.java.simpleName
        var blogType = WolooDashboard::class.java.simpleName
    }
}
