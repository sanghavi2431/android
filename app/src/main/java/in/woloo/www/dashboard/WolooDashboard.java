package in.woloo.www.dashboard;

import static in.woloo.www.utils.AppConstants.BLOG_CONTENT_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PERIOD_TRACKER_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PRODUCT_OFFER_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.SHOW_OFFER_CART;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import in.woloo.www.BuildConfig;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.base.BaseActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.common.DayLogUtils;
import in.woloo.www.customview.CurvedBottomNavigationView;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.dashboard.ui.thirstreminder.AlarmReceiver;
import in.woloo.www.dashboard.ui.thirstreminder.SaveThirstReminderDialog;
import in.woloo.www.dashboard.ui.thirstreminder.ThirstReminderDialog;
import in.woloo.www.dashboard.ui.wah_certificate.WahCertificateActivity;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.editprofile.EditProfileActivity;
import in.woloo.www.giftSubscription.GiftSubscriptionActivity;
import in.woloo.www.giftcard.GiftCardFragment;
import in.woloo.www.home.fragments.WolooStoreInfoFragment;
import in.woloo.www.invite_friend.fragments.InviteFriendFragment;
import in.woloo.www.mapdirection.GetDistance;
import in.woloo.www.mapdirection.model.NavigationRewardsResponse;
import in.woloo.www.mapdirection.mvp.MapDirectionPresenter;
import in.woloo.www.mapdirection.mvp.MapDirectionView;
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.mvp.MorePresenter;
import in.woloo.www.my_account.MyAccountFragment;
import in.woloo.www.my_history.MyHistoryFragment;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;
import in.woloo.www.period_tracker.ui.EditCycleActivity;
import in.woloo.www.period_tracker.ui.PeriodTrackerFragment;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.scan_qr_code.QRcodeScannerActivity;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.trendingblog.fragments.TrendBlogFragment;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.ApiServiceClientAdapter;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.enroute.EnrouteDirectionActivity;
import in.woloo.www.v2.enroute.viewmodel.EnrouteViewModel;
import in.woloo.www.v2.giftcard.model.ValidateGiftCardResponse;
import in.woloo.www.v2.home.model.Voucher;
import in.woloo.www.v2.home.model.VoucherRequest;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.periodtracker.viewmodel.PeriodTrackerViewModel;
import in.woloo.www.v2.profile.activity.ProfileActivity;
import in.woloo.www.v2.profile.model.ShowProfileResponse;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.thirstReminder.model.ThirstReminderRequest;
import in.woloo.www.v2.thirstReminder.model.ThirstReminderResponse;
import in.woloo.www.v2.thirstReminder.viewmodel.ThirstReminderViewModel;
import in.woloo.www.v2.util.DateToLocalDateConverter;
import in.woloo.www.v2.util.NetcoreUserDetails;
import in.woloo.www.webview.WebViewFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WolooDashboard extends BaseActivity implements Serializable, ThirstReminderDialog.ThirstCallbacks, SaveThirstReminderDialog.SaveThirstCallbacks {
    public static final String ARG_IS_SHOW_PERIOD_TRACKER = "ARG_IS_SHOW_PERIOD_TRACKER";
    public static final String ARG_IS_SHOW_TRENDING_BLOGS = "ARG_IS_SHOW_TRENDING_BLOGS";
    public static final String ARG_IS_SHOW_SHOP = "ARG_IS_SHOW_SHOP";
    public static final String ARG_IS_SHOW_THIRST_REMINDER = "ARG_IS_SHOW_THIRST_REMINDER";

    private final int EDIT_PROFILE = 11;

    @Nullable
    @BindView(R.id.nav_view)
    public CurvedBottomNavigationView nav_view;

    @Nullable
    @BindView(R.id.maintoolbar)
    Toolbar mainToolbar;
    @Nullable
    @BindView(R.id.imgSearch)
    ImageView imgSearch;

    @Nullable
    @BindView(R.id.ivProfile)
    CircleImageView ivProfile;

    @Nullable
    @BindView(R.id.txtName)
    TextView txtName;

    @Nullable
    @BindView(R.id.imgScanQR)
    ImageView imgScanQR;

    @BindView(R.id.imgWlLogo)
    ImageView profileImage;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    public boolean isSectionShow = false;
    private int height;
    private Fragment currentFragment;
    private WolooStoreInfoFragment wolooStoreInfoFragment;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private GoogleApiClient mGoogleApiClient;
    Bundle savedinstancestate;
    public static TextView tv_woloo_store;
    public static LinearLayout ll_nav_view;
    public static FrameLayout ll_bottom_nav;
    protected static SharedPreference mSharedPreference;

    boolean doubleBackToExitPressedOnce = false;
    private MenuItem lastSelectedMenuItem = null;
    public boolean isOverLay = false;
    private UserProfile userProfileResponse = null;
    boolean giftSubsFlag = false;

    public static String TAG = WolooDashboard.class.getSimpleName();
    private PeriodTrackerViewModel periodTrackerViewModel;
    private EnrouteViewModel enrouteViewModel;

    private UserProfileMergedResponse userProfileMergedResponse;
    private BottomSheetBehavior mBottomSheetBehavior;
    private CoordinatorLayout colBottomSheet;
    private Menu menu;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    private HomeViewModel homeViewModel;
    private ThirstReminderViewModel thirstReminderViewModel;
    private ProfileViewModel profileViewModel;
    EnrouteDirectionActivity.DirectionWoloo wolooTemp;


    private boolean mShowProfile = true;
    private boolean isSubscription = false;

    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Map<String, String> hindiStringsMap = new HashMap<>();
        hindiStringsMap.put("invite_friend", "किसी मित्र को निमंत्रित करें");
        Locale locale = new Locale("HI");
        Restring.putStrings(locale, hindiStringsMap);
        Restring.setLocale(locale);*/
        setContentView(R.layout.activity_woloo_dashboard);

        //Logger.i(TAG, "onCreate "+ FirebaseMessaging.getInstance().getToken().getResult());
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Logger.i(TAG, "onCreate " + s);
            }
        });
        System.out.println("We have " + FirebaseInAppMessaging.getInstance());
        ButterKnife.bind(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        tv_woloo_store = (TextView) findViewById(R.id.tv_woloo_store);
        ll_nav_view = (LinearLayout) findViewById(R.id.ll_nav_view);
        ll_bottom_nav = (FrameLayout) findViewById(R.id.ll_bottom_nav);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        thirstReminderViewModel = new ViewModelProvider(this).get(ThirstReminderViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        periodTrackerViewModel = new ViewModelProvider(this).get(PeriodTrackerViewModel.class);
        enrouteViewModel = new ViewModelProvider(this).get(EnrouteViewModel.class);

        mainToolbar = findViewById(R.id.maintoolbar);
        imgSearch = findViewById(R.id.imgSearch);
        ivProfile = findViewById(R.id.ivProfile);
        txtName = findViewById(R.id.txtName);
        imgScanQR = findViewById(R.id.imgScanQR);
        nav_view = findViewById(R.id.nav_view);
        colBottomSheet = findViewById(R.id.colBottomSheet);
        View bottomSheet = findViewById(R.id.bottom_sheet_layout);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        FrameLayout overlay_layout = findViewById(R.id.overlay_layout);
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(this);
        }

        if (!mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(), "").equals("")) {
            startActivity(new Intent(WolooDashboard.this, WahCertificateActivity.class));
        }

        if (mSharedPreference.getStoredBooleanPreference(this, SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED.getPreferenceKey(), true)) {
            overlay_layout.setVisibility(View.VISIBLE);
            isOverLay = true;
        } else {
            overlay_layout.setVisibility(View.GONE);
            isOverLay = false;
        }

        Intent i= getIntent();
        String fragmentType = i.getStringExtra("FRAGMENT");

        if(fragmentType != null)
        {
            if(ARG_IS_SHOW_PERIOD_TRACKER == "PERIOD_TRACKER_FRAGMENT")
            {
                currentFragment = new PeriodTrackerFragment();
                loadFragment(currentFragment, PeriodTrackerFragment.TAG);
            }
        }


        setLiveData();

        overlay_layout.setOnClickListener(v -> {
            mSharedPreference.setStoredBooleanPreference(this, SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED.getPreferenceKey(), false);
            overlay_layout.setVisibility(View.GONE);
            if (currentFragment instanceof HomeFragment) {
                ((HomeFragment) currentFragment).hideAndShow(false);
            }
            hideAndShow(false);
        });
        tv_woloo_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (currentFragment instanceof HomeFragment) {
                        ((HomeFragment) currentFragment).hideAndShow(isSectionShow);
                    }
                    hideAndShow(isSectionShow);
                } catch (Exception e) {
                    CommonUtils.printStackTrace(e);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfileResponse != null) {
                    WolooApplication.getInstance().setProfileResponse(userProfileResponse);
                }
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
        if (getIntent() != null && (getIntent().getBooleanExtra(ARG_IS_SHOW_PERIOD_TRACKER, false)
                || (getIntent().getAction() != null && getIntent().getAction().equals(PERIOD_TRACKER_NOTIFICATION)))) {
            currentFragment = new PeriodTrackerFragment();
            loadFragment(currentFragment, PeriodTrackerFragment.TAG);
        } else if (getIntent() != null && (getIntent().getBooleanExtra(ARG_IS_SHOW_TRENDING_BLOGS, false)
                || (getIntent().getAction() != null && getIntent().getAction().equals(BLOG_CONTENT_NOTIFICATION)))) {
            String blogLink = getIntent().getExtras().getString("blog_link");
            if (blogLink != null) {// App in the background
                /*Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.ARG_BLOG_URL, blogLink);
                startActivity(intent);
                finish();
                return;*/
                hideToolbar();
                currentFragment = WebViewFragment.newInstance("Blog", blogLink , WolooDashboard.TAG);
                loadFragment(currentFragment, WebViewFragment.TAG);
            } else {
                currentFragment = new TrendBlogFragment();
                loadFragment(currentFragment, TrendBlogFragment.TAG);
            }
        } else if (getIntent() != null && (getIntent().getBooleanExtra(ARG_IS_SHOW_SHOP, false)
                || (getIntent().getAction() != null && getIntent().getAction().equals(PRODUCT_OFFER_NOTIFICATION)))) {

            String code = getIntent().getExtras().getString("coupon_code");
            if (code != null) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("woloo_coupon", code);
                clipboard.setPrimaryClip(clip);
                currentFragment = ShoppingFragment.newInstance("coupon", code);
                Toast.makeText(this, "Coupon copied!", Toast.LENGTH_SHORT).show();
            } else {
                currentFragment = new ShoppingFragment();
            }
            loadFragment(currentFragment, PeriodTrackerFragment.TAG);
        } else if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(SHOW_OFFER_CART)) {
            hideToolbar();
            loadFragment(MyHistoryFragment.newInstance("", "",true), "MyHistoryFragment");
        } else if(checkForDeeplink()){
        } else {
            currentFragment = new HomeFragment();
            loadFragment(currentFragment, "HomeFragment");
        }

        if (getIntent() != null && getIntent().getBooleanExtra(ARG_IS_SHOW_THIRST_REMINDER, false)) {
            showThirstReminder();
        }
        if (!(currentFragment instanceof HomeFragment)) {
            bottomViewTransparent();
        }

        nav_view.getMenu().getItem(2).setChecked(true);
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //loadMarkerFragment(new WolooStoreInfoFragment(),"WolooStoreInfoFragment");
        imgSearch.setOnClickListener(v -> {
            //startActivity(new Intent(WolooDashboard.this, SearchWolooActivity.class));
            if (userProfileResponse != null) {
                DateToLocalDateConverter dc = new DateToLocalDateConverter();
                String localExpiryDateIs = dc.dateConvert(userProfileResponse.getProfile().getExpiryDate());
                if (!CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {
                    startActivity(new Intent(WolooDashboard.this, EnrouteDirectionActivity.class));
//                    startActivity(new Intent(WolooDashboard.this, SearchWolooActivity.class));
                } else {
                    displayToast(getResources().getString(R.string.expired_text));
                }
            }
        });

        imgScanQR.setOnClickListener(v -> {
            Intent intent = new Intent(WolooDashboard.this, QRcodeScannerActivity.class);
            if (userProfileResponse != null) {
                String viewProfileInString = new Gson().toJson(userProfileResponse);
                intent.putExtra(AppConstants.VIEW_PROFILE_STRING, viewProfileInString);
            }
            startActivity(intent);
        });

        if (getIntent().hasExtra("goToHome")) {
            nav_view.setSelectedItemId(R.id.navigation_dash_home);
        }

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Logger.i(TAG, "activity launcher");
                        if (currentFragment != null) {
                            if (!(currentFragment instanceof PeriodTrackerFragment)) {
                                currentFragment = new PeriodTrackerFragment();
                                loadFragment(currentFragment, PeriodTrackerFragment.TAG);
                            }
                        }
                    }
                });
        profileViewModel.getUserProfile();
        if(SharedPrefSettings.Companion.getGetPreferences().fetchIsDirectionWoloo()) {
            checkIfUserReachedLocation();
        }
    }

    private void setLiveData() {
        homeViewModel.observeVoucher().observe(this, new Observer<BaseResponse<Voucher>>() {
            @Override
            public void onChanged(BaseResponse<Voucher> voucherBaseResponse) {
                if (voucherBaseResponse != null && voucherBaseResponse.getData() != null) {
                    dismissExpiryPopup();
                    if(!voucherBaseResponse.getData().isAlreadyConsumed()) {
                        if (voucherBaseResponse.getData().isAlreadyApplied()) {
                            showAlreadyVoucherDialog();
                        } else {
                            mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
                            showFreeTrialDialog(voucherBaseResponse.getData().getMessage(), String.valueOf(voucherBaseResponse.getData().getDays()), voucherBaseResponse.getData().isLifetime(),voucherBaseResponse.getData().getTypeOfVoucher(), voucherBaseResponse.getData().getExpiryNote());
                        }
                    }
                } else {
                    mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
                    if (!WolooApplication.getErrorMessage().isEmpty()) {
                        showdialog(WolooApplication.getErrorMessage());
                    }
                    WolooApplication.setErrorMessage("");
                }
            }
        });
        homeViewModel.observeShowProfile().observe(this, new Observer<BaseResponse<ShowProfileResponse>>() {
            @Override
            public void onChanged(BaseResponse<ShowProfileResponse> showProfileResponseBaseResponse) {
//                if(showProfileResponseBaseResponse != null && showProfileResponseBaseResponse.getData() != null){
//                    if (showProfileResponseBaseResponse.getData().getShouldShowProfileForm()){
//                        launchProfileActivity();
//                    }
//                }
            }
        });
        homeViewModel.observeValidateGiftCard().observe(this, new Observer<BaseResponse<ValidateGiftCardResponse>>(){
            @Override
            public void onChanged(BaseResponse<ValidateGiftCardResponse> validateGiftCardResponseBaseResponse) {
                mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.GIFT_CARD_ID.getPreferenceKey(), "");
                if (validateGiftCardResponseBaseResponse != null && validateGiftCardResponseBaseResponse.getData() != null) {
//                    dismissExpiryPopup();
                    if(validateGiftCardResponseBaseResponse.getData().getShowPopUp() == 1) {
                        showGiftCardDialog(validateGiftCardResponseBaseResponse.getData().getMessage());
                    }
                } else {
//                    mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
//                    if (!WolooApplication.getErrorMessage().isEmpty()) {
//                        showdialog(WolooApplication.getErrorMessage());
//                    }
//                    showGiftCardDialog(WolooApplication.getErrorMessage());
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        thirstReminderViewModel.observeThirstReminder().observe(this, new Observer<BaseResponse<ThirstReminderResponse>>() {
            @Override
            public void onChanged(BaseResponse<ThirstReminderResponse> response) {

                if (response != null && response.getData() != null) {
                    if(response.getData().getThirstReminderHours() != null || response.getData().getThirstReminderHours() != 0) {
                        setAlarm(response.getData().getThirstReminderHours());
                        mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.THIRST_REMINDER.getPreferenceKey(), String.valueOf(response.getData().getThirstReminderHours()));
                    }
                } else {
//                    mSharedPreference.setStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
//                    if (!WolooApplication.getErrorMessage().isEmpty()) {
//                        showdialog(WolooApplication.getErrorMessage());
//                    }
//                    showGiftCardDialog(WolooApplication.getErrorMessage());
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        periodTrackerViewModel.observeGetPeriodTracker().observe(this, new Observer<BaseResponse<PeriodTrackerResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PeriodTrackerResponse.Data> getPeriodDataResponse) {
                try {
                    if (getPeriodDataResponse != null && getPeriodDataResponse.getData() != null) {
                        if (mSharedPreference == null)
                            mSharedPreference = new SharedPreference(getBaseContext());
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), getPeriodDataResponse.getData().getPeriodDate());
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), String.valueOf(getPeriodDataResponse.getData().getPeriodLength()));
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), String.valueOf(getPeriodDataResponse.getData().getCycleLenght()));


                        mSharedPreference.setStoredPreference(getApplicationContext(), SharedPreferencesEnum.DAILY_LOG.getPreferenceKey(), new Gson().toJson(getPeriodDataResponse.getData().getLog()));

                        Log log = getPeriodDataResponse.getData().getLog();
                        try {
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.BLEEDING, log.getBleeding());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.MOOD, log.getMood());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.HABITS, log.getHabits());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE, log.getSexDrive());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.PREMENSTRUATION, log.getPremenstruation());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION, log.getDiseasesandmedication());
                            DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.MENSTRUATION, log.getMenstruation());
                        } catch (NullPointerException e) {
                            activityResultLauncher.launch(new Intent(WolooDashboard.this, EditCycleActivity.class));
                            return;
                        }

                        Calendar periodStartDate = Calendar.getInstance();
                        String startDate = getPeriodDataResponse.getData().getPeriodDate();
                        if (!startDate.equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                periodStartDate.setTime(Objects.requireNonNull(format.parse(startDate)));
                            } catch (ParseException e) {
                                CommonUtils.printStackTrace(e);
                            }
                        }
                        periodStartDate.add(Calendar.DATE, -36);
                        if (Calendar.getInstance().after(periodStartDate)) {
                    /*FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.frm_contant, new PeriodTrackerFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                            currentFragment = new PeriodTrackerFragment();
                            loadFragment(currentFragment, PeriodTrackerFragment.TAG);
                        } else {
                            activityResultLauncher.launch(new Intent(WolooDashboard.this, EditCycleActivity.class));
                        }

                    } else {
                        activityResultLauncher.launch(new Intent(WolooDashboard.this, EditCycleActivity.class));
                    }
                } catch (Exception ex) {
                    Logger.e("TAG", ex.getClass().getSimpleName());
                    if (ex instanceof JsonSyntaxException || ex instanceof NumberFormatException) {
                        activityResultLauncher.launch(new Intent(WolooDashboard.this, EditCycleActivity.class));
                    } else {
                        CommonUtils.printStackTrace(ex);
                    }
                }
            }
        });

        profileViewModel.observeUserProfile().observe(this, new Observer<BaseResponse<UserProfile>>() {
            @Override
            public void onChanged(BaseResponse<UserProfile> viewProfileResponse) {
                Logger.i(TAG, "setProfileResponse");
                progressBar.setVisibility(View.VISIBLE);
                if (viewProfileResponse != null && viewProfileResponse.getData() != null) {
                    userProfileResponse = viewProfileResponse.getData();
                    DateToLocalDateConverter dc = new DateToLocalDateConverter();


                    String expiryRaw = userProfileResponse.getProfile().getExpiryDate();

                    String dateExpiry = expiryRaw;
                    if (expiryRaw == null) {
                        // Get current date + 7 days in ISO 8601 format (e.g., "2025-06-12T00:00:00Z")
                        dateExpiry = Instant.now().plus(7, ChronoUnit.DAYS).toString();
                    } else {
                        dateExpiry = expiryRaw.toString(); // Ensure this is in ISO 8601 format
                    }

                    String localExpiryDateIs = dc.dateConvert(dateExpiry);
                    Logger.d("Local Date to Test is : " , userProfileResponse.getProfile().getExpiryDate() + " " + localExpiryDateIs);
                    if (!TextUtils.isEmpty(viewProfileResponse.getData().getProfile().getName())) {
                        txtName.setText(commonUtils.getFirstLaterCaps(viewProfileResponse.getData().getProfile().getName().split(" ")[0]));
                    } else {
//                if (commonUtils.getFirstLaterCaps(userProfileResponse.getUserData().getName())!=null){
                        txtName.setText(" Guest");
//                }

                    }
                    new NetcoreUserDetails(WolooDashboard.this).updateNetcoreUserProfile(
                            viewProfileResponse.getData().getProfile().getRoleId(),
                            viewProfileResponse.getData().getProfile().getName(),
                            viewProfileResponse.getData().getProfile().getEmail(),
                            viewProfileResponse.getData().getProfile().getGender(),
                            viewProfileResponse.getData().getProfile().getDob(),
                            viewProfileResponse.getData().getProfile().getExpiryDate(),
                            viewProfileResponse.getData().getProfile().getSubscriptionId() == null && viewProfileResponse.getData().getProfile().getVoucherId() == null);
//            checkForDeeplink();
                    if (!mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "").equals("")) {
//                Date stringtodate = null;
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                    stringtodate = format.parse(userProfileResponse.getUserData().getExpiryDate());
//                } catch (ParseException e) {
//                     CommonUtils.printStackTrace(e);
//                }
//                if (userProfileResponse.getIsFutureSubcriptionExist().equals("true")) {
//                    showdialog("You already have an Active Future Membership, You can use the voucher only after the future Membership is active");
//                    mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
//                    String expireDate = userProfileResponse.getUserData().getExpiryDate();
//                    Fragment fragment = ((AppCompatActivity) this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
//                    if (fragment != null) {
//                        if (fragment instanceof HomeFragment) {
//                            ((HomeFragment) fragment).setSubscriptionDetails(expireDate, userProfileResponse);
//                        }
//                    }
//                } else if (new Date().after(stringtodate)) {
//                    morePresenter.getVoucherDetails(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), ""));
//                } else {
//                    showAlreadyVoucherDialog();
//                }
                        applyVoucher(false);

                    }
                    if (!mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.GIFT_CARD_ID.getPreferenceKey(), "").equals("")) {
                        homeViewModel.validateGiftCard(mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.GIFT_CARD_ID.getPreferenceKey(), ""));
                    }

                    if (!mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.getPreferenceKey(), "").equals("")) {
                        //if (true) {
                        //navigateToGiftCardScreen();
                        giftSubsFlag = true;
                        navigateToGiftSubscription();
                    }
                    String expireDate = viewProfileResponse.getData().getProfile().getExpiryDate();
                    Fragment fragment = ((AppCompatActivity) WolooDashboard.this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
                    if (fragment != null) {
                        if (fragment instanceof HomeFragment) {
                            ((HomeFragment) currentFragment).setSubscriptionDetails(expireDate, userProfileResponse);
                        }
                    }

                    // set bottom sheet
                    //View bottomSheet = findViewById(R.id.bottom_sheet_layout);
//                    ViewProfileResponse.UserData user = viewProfileResponse.getUserData();
            /*if (user != null && user.getGender() != null) {
                if (user.getGender().equalsIgnoreCase("Male")) {
                    bottomSheet.findViewById(R.id.blogCatRelThree).setVisibility(View.GONE);
                }
            }*/
                    //mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else{
                    WolooApplication.setErrorMessage("");
                }
                txtName.setVisibility(View.VISIBLE);
                if (viewProfileResponse == null || viewProfileResponse.getData() == null || viewProfileResponse.getData().getProfile() == null
                        || viewProfileResponse.getData().getProfile().getAvatar() == null) {
                    ivProfile.setImageDrawable(ContextCompat.getDrawable(WolooDashboard.this, R.drawable.ic_account_circle));

                } else {
                    if (viewProfileResponse!=null && TextUtils.isEmpty(viewProfileResponse.getData().getProfile().getAvatar()) || viewProfileResponse.getData().getProfile().getAvatar().trim().equals("users/default.png") || viewProfileResponse.getData().getProfile().getAvatar().trim().equals("default.png")) {
                        ImageUtil.loadImageProfile(WolooDashboard.this, ivProfile, BuildConfig.BASE_URL + "public/userProfile/default.png");
                    } else {
                        ImageUtil.loadImageProfile(WolooDashboard.this, ivProfile, viewProfileResponse.getData().getProfile().getBaseUrl() + viewProfileResponse.getData().getProfile().getAvatar());
                    }
                } progressBar.setVisibility(View.GONE);
            }
        });

        enrouteViewModel.observeWolooNavigationReward().observe(this, new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> navigationRewardsResponse) {
                try {
                    if (navigationRewardsResponse != null && navigationRewardsResponse.getSuccess()) {
                        showdialogForNavigationReward(true);
                    } else {
                        showdialogForNavigationReward(false);
                        WolooApplication.setErrorMessage("");
                        // Toast.makeText(getActivity().getApplicationContext(), "You have arrived at your destination.", Toast.LENGTH_SHORT).show();
                    }
                    Bundle bundle = new Bundle();
                    //bundle.put
                    Utility.logFirebaseEvent(getApplicationContext(), bundle, AppConstants.DESTIONATION_REACHED);
                } catch (Exception ex) {
                    CommonUtils.printStackTrace(ex);
                }
            }
        });
    }

    /*calling on resume*/
    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
        if (giftSubsFlag) {
            giftSubsFlag = false;
            // String expireDate = userProfileResponse.getProfile().getExpiryDate();
            DateToLocalDateConverter dc = new DateToLocalDateConverter();
            String expireDate = dc.dateConvert(userProfileResponse.getProfile().getExpiryDate());
            Fragment fragment = ((AppCompatActivity) this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
            if (fragment != null) {
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).setSubscriptionDetails(expireDate, userProfileResponse);
                }
            }
        }
//        showGiftCardDialog("Congratulation!!! \nYou have received giftcard");
        homeViewModel.showProfile(String.valueOf(new CommonUtils().getUserInfo().getId()));
    }

    public void getUserProfile(){
        if(isSubscription){
            profileViewModel.getUserProfile();
        }
    }
    private void checkIfUserReachedLocation() {
        Logger.i(TAG, "checkIfUserReachedLocation");
        try {
            //Changes done by Aarati july 2024 @ woloo
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permissions already granted
                // You can proceed with using location
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> locationResult  = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location location = task.getResult();
                        if(location == null) return;
                        //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                        LatLng currentLatLng =new LatLng(location.getLatitude(), location.getLongitude());
                        wolooTemp = SharedPrefSettings.Companion.getGetPreferences().fetchDirectionWoloo();
                        LatLng destLatLng = new LatLng(wolooTemp.getLat(), wolooTemp.getLng());
                        String url = getDirectionsUrl(currentLatLng, destLatLng);
                        Logger.e("url", "" + url);
                        ApiServiceClientAdapter.Companion.getInstance().getApiService()
                                .getDirections(url)
                                .enqueue(new Callback<GetDistance>() {
                                    @Override
                                    public void onResponse(Call<GetDistance> call, Response<GetDistance> response) {
                                        GetDistance getdistance = response.body();
                                        int distance =  getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getValue();
                                        if(distance<50){
                                            enrouteViewModel.getWolooNavigationReward(wolooTemp.getWolooid());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GetDistance> call, Throwable t) {

                                    }
                                });
                    }
                }
            });
        } catch (Exception e) {
            Logger.e("Exception: %s", e.getMessage());
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(this);
        }
        String transport_mode = mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
        switch (transport_mode) {
            case "0":
                mode = "mode=driving";
                break;
            case "1":
                mode = "mode=walking";
                break;
            case "2":
                mode = "mode=bicycling";
                break;
        }
        // Building the parameters to the web service
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        String key = "key=" +CommonUtils.googlemapapikey(this);

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.i(TAG, "newIntent");
    }

    public void showExpiryPopup(){
        if(userProfileResponse == null) return;

        DateToLocalDateConverter dc = new DateToLocalDateConverter();
        String expireDate = dc.dateConvert(userProfileResponse.getProfile().getExpiryDate());
       // String expireDate = userProfileResponse.getProfile().getExpiryDate();
        Fragment fragment = ((AppCompatActivity) this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
        if (fragment != null) {
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).setSubscriptionDetails(expireDate, userProfileResponse);
            }
        }
    }

    public void dismissExpiryPopup(){
        if(userProfileResponse == null) return;
        Fragment fragment = ((AppCompatActivity) this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
        if (fragment != null) {
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).dismissVoucherExpireDialogs();

            }
        }
    }

    /*calling hide mainToolbar*/
    public void hideToolbar() {
        Logger.i(TAG, "hideToolbar");
        try {
            mainToolbar.setVisibility(View.GONE);
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling showtoolbar*/
    public void showToolbar() {
        Logger.i(TAG, "showToolbar");
        try {
            mainToolbar.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling showFooter*/
    public void showFooter() {
        Logger.i(TAG, "showFooter");
        try {
            ll_bottom_nav.setVisibility(View.VISIBLE);

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling hideFooter*/
    public void hideFooter() {
        Logger.i(TAG, "hideFooter");
        try {
            ll_bottom_nav.setVisibility(View.GONE);

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    @Optional
    @OnClick(R.id.fabHomeDashboard)
    protected void onClickFab() {
        //bottomViewTransparent();
        handleFabClick();
    }

    @Optional
    @OnClick(R.id.imgOneRel)
    protected void onClickShop() {
        if (!(currentFragment instanceof ShoppingFragment)) {
            Bundle bundle = new Bundle();
            Utility.logFirebaseEvent(WolooDashboard.this, bundle, AppConstants.SHOP_CLICK);
            Utility.logNetcoreEvent(WolooDashboard.this, new HashMap<String,Object>(), AppConstants.SHOP_CLICK);
            unCheckMenuItem();
            currentFragment = new ShoppingFragment();
            loadFragment(currentFragment, "ShoppingFragment");
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
    }

    @Optional
    @OnClick(R.id.imgTwoRel)
    protected void onClickLocateLoo() {
        // load Home
        if (!(currentFragment instanceof HomeFragment)) {
            unCheckMenuItem();
            profileViewModel.getUserProfile();
            currentFragment = new HomeFragment();
            loadFragment(currentFragment, "HomeFragment");
            bottomViewTransparent90();
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Optional
    @OnClick(R.id.imgThreeRel)
    protected void onClickPeriodTracker() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (!(currentFragment instanceof PeriodTrackerFragment)) {
            unCheckMenuItem();
//            periodTrackerPresenter.getPeriodData();
            periodTrackerViewModel.getPeriodTracker();
        }
    }

    @Optional
    @OnClick(R.id.imgFourRel)
    protected void onClickThirstReminder() {
        showThirstReminder();
    }

    private void showThirstReminder() {
        String thirstReminderStr = mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.THIRST_REMINDER.getPreferenceKey(), "0");
        try {
            int reminderHours = Integer.parseInt(thirstReminderStr);
            if (reminderHours > 0) {
                new SaveThirstReminderDialog(reminderHours).show(getSupportFragmentManager(), SaveThirstReminderDialog.TAG);
            } else {
                new ThirstReminderDialog().show(getSupportFragmentManager(), ThirstReminderDialog.TAG);
            }
        } catch (NumberFormatException e) {

        }

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Optional
    @OnClick(R.id.imgFiveRel)
    protected void onClickWolooCommunity() {
        if (!(currentFragment instanceof WebViewFragment)) {
            unCheckMenuItem();
            hideToolbar();
            WebViewFragment webViewFragment = WebViewFragment.newInstance("Woloo Community", "https://iamhere.app/community/women_hygiene_1624314543470" , WolooDashboard.TAG);
            loadMenuFragment(webViewFragment, WebViewFragment.TAG);

            /*int colorInt = Color.parseColor("#FFFFFF");
            CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(colorInt)
                    .build();

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setDefaultColorSchemeParams(defaultColors);
            CustomTabsIntent customTabsIntent = builder.build();

            customTabsIntent.launchUrl(this, Uri.parse("https://iamhere.app/community/women_hygiene_1624314543470"));*/
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }



    private void unCheckMenuItem() {

                /*nav_view.getMenu().setGroupCheckable(0, false, false);
                int size = nav_view.getMenu().size();
                for (int i = 0; i < size; i++) {
                    nav_view.getMenu().getItem(i).setChecked(false);
                    nav_view.getMenu().getItem(i).setCheckable(false);
                }
                nav_view.getMenu().setGroupCheckable(0, true, true);*/
        changeIcon(nav_view.getMenu().findItem(R.id.navigation_home));
    }

    /*calling onCreateOptionsMenu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.i(TAG, "onCreateOptionsMenu");
        this.menu = menu;
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//            menu.findItem(R.id.navigation_shop).setIcon(drawableResourceId);
        return true;
    }

    /*calling changeIcon*/
    public void changeIcon(MenuItem item) {
        Logger.i(TAG, "changeIcon");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lastSelectedMenuItem != null) {
                    if (lastSelectedMenuItem.getItemId() == R.id.navigation_dash_home) {
                        lastSelectedMenuItem.setIcon(R.drawable.ic_baseline_home_24);
                    }
                    if (lastSelectedMenuItem.getItemId() == R.id.navigation_inviteFriend) {
                        lastSelectedMenuItem.setIcon(R.drawable.ic_invitefriend);
                        ;
                    }
                    if (lastSelectedMenuItem.getItemId() == R.id.navigation_chatty_cat) {
                        lastSelectedMenuItem.setIcon(R.drawable.ic_accountdeactive);
                    }
                    if (lastSelectedMenuItem.getItemId() == R.id.navigation_more) {
                        lastSelectedMenuItem.setIcon(R.drawable.ic_more_deactive);
                    }
                }
                if (item.getItemId() == R.id.navigation_dash_home) {
//                    ((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
                    item.setIcon(R.drawable.ic_baseline_home_filled);
                } else if (item.getItemId() == R.id.navigation_inviteFriend) {
                    item.setIcon(R.drawable.ic_new_invitefriend);
                }/*else if(item.getItemId()==R.id.navigation_home){
                    item.setIcon(R.drawable.ic_selected_shop);
                }*/ else if (item.getItemId() == R.id.navigation_chatty_cat) {
                    item.setIcon(R.drawable.ic_myaccount_active);
                } else if (item.getItemId() == R.id.navigation_more) {
                    item.setIcon(R.drawable.ic_more_active);
                }
                lastSelectedMenuItem = item;

            }
        });
    }

    private int drawableResourceId = R.drawable.ic_selected_shop;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            bottomViewTransparent();

            if (mBottomSheetBehavior != null) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
            }

            //Fragment fragment = ((AppCompatActivity) WolooDashboard.this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
            switch (item.getItemId()) {
                case R.id.navigation_dash_home:
                    if (!(currentFragment instanceof TrendBlogFragment)) {
                        Bundle bundle = new Bundle();
                        Utility.logFirebaseEvent(WolooDashboard.this, bundle, AppConstants.DASH_HOME_CLICK);
                        changeIcon(item);
                        currentFragment = new TrendBlogFragment();
                        loadFragment(currentFragment, "DashboardHomeFragment");
//                    ((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
                    }
                   /* if(!(fragment instanceof ShoppingFragment)){
                        Bundle bundle = new Bundle();
                        Utility.logFirebaseEvent(WolooDashboard.this,bundle,AppConstants.SHOP_CLICK);
                        changeIcon(item);
                        loadFragment(new ShoppingFragment(), "ShoppingFragment");
//                    ((MenuItemImpl) item).mMenu.mItems.get(0).getTitle();
                    }*/
                    break;
                case R.id.navigation_inviteFriend:
                    if (!(currentFragment instanceof InviteFriendFragment)) {
                        Bundle bundle = new Bundle();
                        Utility.logFirebaseEvent(WolooDashboard.this, bundle, AppConstants.INVITE_CLICK);

                        HashMap<String,Object> payload = new HashMap<>();
                        Utility.logNetcoreEvent(WolooDashboard.this, payload, AppConstants.INVITE_CLICK);

                        changeIcon(item);
                        currentFragment = InviteFriendFragment.newInstance(false);
                        loadFragment(currentFragment, "InviteFriendFragment");
                        //  bottomViewTransparent90();
                    }
                    break;
                //case R.id.navigation_home:

                 /*if(!(fragment instanceof HomeFragment)) {
                        changeIcon(item);
                        loadFragment(homeFragment, "HomeFragment");
                        bottomViewTransparent90();
                    }*/

                //bottomViewTransparent90Blog();
                //extracted();
                //bottomDialog.show();
                //startActivity(new Intent(WolooDashboard.this, EditCycleActivity.class));
                //
                //  break;
                case R.id.navigation_chatty_cat:
                    if (!(currentFragment instanceof MyAccountFragment)) {
                        Bundle bundle = new Bundle();
                        //bundle.put
                        Utility.logFirebaseEvent(WolooDashboard.this, bundle, AppConstants.MY_ACCOUNT_CLICK);
                        Utility.logNetcoreEvent(WolooDashboard.this, new HashMap<>(), AppConstants.MY_ACCOUNT_CLICK);

                        changeIcon(item);
                        currentFragment = MyAccountFragment.newInstance(false);
                        loadFragment(currentFragment, "MyAccountFragment");
                    }
                    // bottomViewTransparent90();
                    break;
                case R.id.navigation_more:
                    if (!(currentFragment instanceof MoreFragment)) {
                        changeIcon(item);
                        currentFragment = new MoreFragment();
                        loadFragment(currentFragment, "MoreFragment");
                    }
                    break;
                case R.id.navigation_home:
                    handleFabClick();
                    break;
            }
            return false;
        }
    };

    private void handleFabClick() {
        if (mBottomSheetBehavior != null) {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                try {
                    if (currentFragment instanceof HomeFragment) {
                        if(tv_woloo_store.getVisibility() == View.GONE && !((HomeFragment) currentFragment).isWolooListVisible()){
                            bottomViewTransparent90();
                        }
                    }
                } catch (Exception e) {
                    CommonUtils.printStackTrace(e);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        removeWolooStoreInfo();
        Fragment fragment = ((AppCompatActivity) this).getSupportFragmentManager().findFragmentById(R.id.frm_contant);
        if (fragment != null) {
            if (fragment instanceof TrendBlogFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            } else if (fragment instanceof MoreFragment || fragment instanceof HomeFragment || fragment instanceof InviteFriendFragment ||
                    fragment instanceof MyAccountFragment || fragment instanceof ShoppingFragment ||
                    fragment instanceof PeriodTrackerFragment ) {
                nav_view.setSelectedItemId(R.id.navigation_dash_home);

            }
            else if ( fragment instanceof MyHistoryFragment){
                try {
                    //getActivity().onBackPressed();
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                       loadFragment(new TrendBlogFragment(), TrendBlogFragment.TAG);
                        changeIcon(nav_view.getMenu().findItem(R.id.navigation_dash_home));
                    }
                } catch (Exception ex) {
                    CommonUtils.printStackTrace(ex);
                }
            }else {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                } else{
                    currentFragment = new TrendBlogFragment();
                    loadFragment(currentFragment, TrendBlogFragment.TAG);
                    changeIcon(nav_view.getMenu().findItem(R.id.navigation_dash_home));
                }
            }
        } else
            super.onBackPressed();

    }

    /*calling load fragment*/
    public void loadFragment(Fragment fragment, String homeTah) {
        Logger.i(TAG, "loadFragment");
        currentFragment = fragment;
        try {
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frm_contant, fragment, homeTah);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling loadMenuFragment*/
    public void loadMenuFragment(Fragment fragment, String homeTah) {
        Logger.i(TAG, "loadMenuFragment");
        try {
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frm_contant, fragment, homeTah);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling loadMarkerFragmentWithIndex*/
    public void loadMarkerFragmentWithIndex(int index, List<NearByStoreResponse.Data> dataList) {
        Logger.i(TAG, "loadMarkerFragmentWithIndex");
        wolooStoreInfoFragment = new WolooStoreInfoFragment();
        wolooStoreInfoFragment.setIndex(index);
        wolooStoreInfoFragment.setDataList(dataList);
        loadMarkerFragment(wolooStoreInfoFragment, "WolooStoreInfoFragment");
    }

    /*calling removeWolooStoreInfo*/
    public void removeWolooStoreInfo() {
        if (wolooStoreInfoFragment != null && wolooStoreInfoFragment.isAdded()) {
            Logger.i(TAG, "removeWolooStoreInfo");
            try {
                FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(wolooStoreInfoFragment).commit();
                if (currentFragment instanceof HomeFragment) {
                    ((HomeFragment) currentFragment).hideAndShow(true);
                }
                //  fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frm_marker_detail)).commit();
            } catch (Exception e) {
                CommonUtils.printStackTrace(e);
            }
        }
    }

    /*calling moveMarkerToIndex*/
    public void moveMarkerToIndex(int index) {
        Logger.i(TAG, "moveMarkerToIndex");
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).animateCameraToMarkerPosition(index);
        }
    }

    /*calling loadMarkerFragment*/
    public void loadMarkerFragment(Fragment fragment, String homeTah) {
        Logger.i(TAG, "loadMarkerFragment");
        try {
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out_up  // popExit
            );
            fragmentTransaction.replace(R.id.frm_marker_detail, fragment, homeTah);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling hideAndShow*/
    public void hideAndShow(boolean status) {
        Logger.i(TAG, "hideAndShow");
        if (status) {
            isSectionShow = false;
            bottomViewTransparent90();

        } else {
            isSectionShow = true;
            bottomViewTransparent();

        }

    }

    public void hideAndShowBlog(boolean status) {
        Logger.i(TAG, "hideAndShow");
        if (status) {
            isSectionShow = false;
            bottomViewTransparent90Blog();

        } else {
            isSectionShow = true;
            bottomViewTransparentBlog();

        }

    }

    /*calling bottomViewTransparent90*/
    private void bottomViewTransparent90() {
        Logger.i(TAG, "bottomViewTransparent90");
        tv_woloo_store.setVisibility(View.VISIBLE);
        fadeInAnimation(tv_woloo_store);
        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent_black_90));
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent_black_90));
    }

    private void bottomViewTransparent90Blog() {
        Logger.i(TAG, "bottomViewTransparent90");
        tv_woloo_store.setVisibility(View.GONE);
        ll_nav_view.setVisibility(View.GONE);
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent_white));
    }

    /*calling bottomViewTransparent*/
    private void bottomViewTransparent() {
        Logger.i(TAG, "bottomViewTransparent");
        tv_woloo_store.setVisibility(View.GONE);
        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent));
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent));
    }

    private void bottomViewTransparentBlog() {
        Logger.i(TAG, "bottomViewTransparent");
        tv_woloo_store.setVisibility(View.GONE);
        ll_nav_view.setVisibility(View.GONE);
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.transparent));
    }

    /*calling fadeInAnimation*/
    private void fadeInAnimation(TextView view) {
        Logger.i(TAG, "fadeInAnimation");
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        view.startAnimation(animFadeIn);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /*calling fadeOutAnimation*/
    private void fadeOutAnimation(TextView view) {
        Logger.i(TAG, "fadeOutAnimation");
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        view.startAnimation(animFadeOut);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void launchProfileActivity(){
        startActivity(new Intent(WolooDashboard.this, ProfileActivity.class));
    }

    private void applyVoucher(Boolean isForce){
        VoucherRequest request = new VoucherRequest();
        request.setVoucherCode(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), ""));
        request.setForceApply(isForce);
        homeViewModel.applyVoucher(request);
    }

    private void navigateToGiftSubscription() {
        startActivity(new Intent(WolooDashboard.this, GiftSubscriptionActivity.class));
    }

    private void navigateToGiftCardScreen() {
        Logger.i(TAG, "navigateToGiftCardScreen");
        try {
            boolean isEmail = false;
            String email = "";
            String mobile = "";
            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                mobile = userProfileResponse.getProfile().getMobile();
            } else if (!TextUtils.isEmpty(userProfileResponse.getProfile().getEmail())) {
                isEmail = true;
                email = userProfileResponse.getProfile().getEmail();
            }
            hideToolbar();
            loadMenuFragment(GiftCardFragment.newInstance(email, isEmail, mobile), "GiftCardFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    public void showdialog(String msg) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_login_failure);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(msg);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }


    public void navigateToSubscriptionScreen(boolean isForSubscription) {
        Logger.i(TAG, "navigateToSubscriptionScreen");
        try {
            boolean isEmail = false;
            String email = "";
            String mobile = "";
            String subscriptionPlanName = "";
            if(userProfileResponse != null){
                DateToLocalDateConverter dc = new DateToLocalDateConverter();
                String localExpiryDateIs = dc.dateConvert(userProfileResponse.getProfile().getExpiryDate());
                Logger.d("Local Date to Test is : " , userProfileResponse.getProfile().getExpiryDate() + " " + localExpiryDateIs);
                if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                    mobile = userProfileResponse.getProfile().getMobile();
                } else if (!TextUtils.isEmpty(userProfileResponse.getProfile().getEmail())) {
                    isEmail = true;
                    email = userProfileResponse.getProfile().getEmail();
                }

                if (!CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {
                    subscriptionPlanName = "";
                }
            }
            hideToolbar();
            isSubscription = true;
            loadMenuFragment(SubscribeFragment.newInstance(subscriptionPlanName, email, isEmail, mobile, isForSubscription, false, ""), "InviteFriendFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    public void showFreeTrialDialog(String message, String days) {
        Logger.i(TAG, "showFreeTrialDialog");
        dismissExpiryPopup();
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_start_free_trial);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tv_startfreetrial = (TextView) dialog.findViewById(R.id.tv_startfreetrial);
            ImageView ivFreeTrial = (ImageView) dialog.findViewById(R.id.ivFreeTrial);
            ivFreeTrial.setVisibility(View.VISIBLE);
            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llStartFreeTrial);
            tv_startfreetrial.setText(message);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    profileViewModel.getUserProfile();
                    dialog.dismiss();
                }
            });
            TextView tv_daysTrials = (TextView) dialog.findViewById(R.id.tv_daysTrials);
            if (days.equals("-1")) {
                ivFreeTrial.setVisibility(View.GONE);
                tv_daysTrials.setVisibility(View.GONE);
            } else {
                if (days.equals("lifetime")) {
                    tv_daysTrials.setVisibility(View.GONE);
                    ivFreeTrial.setImageResource(R.drawable.unlimited_days_image);
                } else {
                    tv_daysTrials.setText(days + " Days");
                    ivFreeTrial.setImageResource(R.drawable.free_trial_image);
                }
            }
            dialog.show();

        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    public void showFreeTrialDialog(String message, String days, int islifetime, String typeOfVoucher, String expiryNote) {
        Logger.i(TAG, "showFreeTrialDialog");
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_start_free_trial);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tv_startfreetrial = (TextView) dialog.findViewById(R.id.tv_startfreetrial);
            TextView tv_typeOfVoucher = (TextView) dialog.findViewById(R.id.tv_typeOfVoucher);
            TextView tv_expiryNote = (TextView) dialog.findViewById(R.id.tv_expiryNote);
            ImageView ivFreeTrial = (ImageView) dialog.findViewById(R.id.ivFreeTrial);
            ivFreeTrial.setVisibility(View.VISIBLE);
            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llStartFreeTrial);
            tv_startfreetrial.setText(message);
            if(!expiryNote.isEmpty()) {
                tv_expiryNote.setText("Note: " + expiryNote);
                tv_expiryNote.setVisibility(View.VISIBLE);
            }
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    profileViewModel.getUserProfile();
                    dialog.dismiss();
                }
            });
            TextView tv_daysTrials = (TextView) dialog.findViewById(R.id.tv_daysTrials);
            if (days.equals("-1")) {
                ivFreeTrial.setVisibility(View.GONE);
                tv_daysTrials.setVisibility(View.GONE);
            } else {
                if (islifetime == 1) {
                    tv_daysTrials.setVisibility(View.GONE);
                    ivFreeTrial.setImageResource(R.drawable.unlimited_days_image);
                } else {
                    tv_daysTrials.setText(days + " Days");
                    tv_typeOfVoucher.setText(typeOfVoucher.toUpperCase());
//                    if (typeOfVoucher.equalsIgnoreCase("free")) {
//                        ivFreeTrial.setImageResource(R.drawable.free_trial_image);
//                    }else{
//                        ivFreeTrial.setImageResource(R.drawable.paid_image);
//                    }
                }
            }
            dialog.show();

        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    public void showGiftCardDialog(String message) {
        Logger.i(TAG, "showFreeTrialDialog");
        dismissExpiryPopup();
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_gift_card_received);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView messageTV = (TextView) dialog.findViewById(R.id.tv_message);
            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llStartFreeTrial);
            messageTV.setText(message);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    profileViewModel.getUserProfile();
                    dialog.dismiss();
//                    navigateToGiftSubscription();
                }
            });
            dialog.show();

        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    public void showAlreadyVoucherDialog() {
        Logger.i(TAG, "showFreeTrialDialog");
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.error_already_voucher);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            LinearLayout llCancelBtn = (LinearLayout) dialog.findViewById(R.id.llCancelBtn);
            llCancelBtn.setOnClickListener(v -> {
                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "");
                dialog.dismiss();
            });
            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llOkayBtn);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //morePresenter.getVoucherDetails(mSharedPreference.getStoredPreference(WolooDashboard.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), ""));
                    applyVoucher(true);
                }
            });
            dialog.show();

        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClickThirstYes() {
        new SaveThirstReminderDialog().show(getSupportFragmentManager(), SaveThirstReminderDialog.TAG);
    }

    @Override
    public void onClickThirstNo() {
        ThirstReminderRequest request = new ThirstReminderRequest();
        request.setThirstReminder(0);
        request.setThirstReminderHours(0);
        thirstReminderViewModel.thirstReminder(request);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(AppConstants.IS_THIRST_REMINDER, 0);
        payload.put(AppConstants.THIRST_REMINDER_HOURS, 0);
        Utility.logNetcoreEvent(this,payload,AppConstants.THIRST_REMINDER_CLICK);
    }

    @Override
    public void onClickSaveThirstReminder(int hours) {

        ThirstReminderRequest request = new ThirstReminderRequest();
        request.setThirstReminder(hours <= 0 ? 0 : 1);
        request.setThirstReminderHours(hours);
        thirstReminderViewModel.thirstReminder(request);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(AppConstants.IS_THIRST_REMINDER, hours <= 0 ? 0 : 1);
        payload.put(AppConstants.THIRST_REMINDER_HOURS, hours);
        Utility.logNetcoreEvent(this,payload,AppConstants.THIRST_REMINDER_CLICK);
    }

    private void setAlarm(int hours) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.REMINDER_HOURS, hours);
        //java.lang.IllegalArgumentException: in.woloo.www: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
        //                                     Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        if (hours > 0) {
            Calendar calendar = Calendar.getInstance();
            //calendar.set(Calendar.HOUR, hours);
            Logger.i(TAG, calendar.toString());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours * AlarmManager.INTERVAL_HOUR, pendingIntent);
            showdialog("Thirst reminder set for every " + hours + " hour(s)");
        } else {//Cancel the alarm
            alarmManager.cancel(pendingIntent);
            showdialog("Thirst reminder cancelled!");
        }
    }
//
//    void openSettingsForGPS(IntentSender intentSender, int DIALOGID, Object o, int i, int i1, int i2, Object o1){
//
//    }

    boolean checkForDeeplink(){
        if(getIntent().hasExtra("deeplink")){
            Logger.e(TAG,getIntent().getStringExtra("deeplink"));
            switch (getIntent().getStringExtra("deeplink")){
                case "subscription" :
                    if (!(currentFragment instanceof MoreFragment)) {
                        changeIcon(this.nav_view.getMenu().findItem(R.id.navigation_more));
                        currentFragment = new MoreFragment();
                        loadFragment(currentFragment, "MoreFragment");
                    }
                    navigateToSubscriptionScreen(true);
                    return true;
                case "shop" :
                    hideToolbar();
                    loadFragment(new ShoppingFragment(), "ShoppingFragment");
                    changeIcon(this.nav_view.getMenu().findItem(R.id.navigation_home));
                    return true;
                case "refer" :
                    bottomViewTransparent();
                    if (!(currentFragment instanceof InviteFriendFragment)) {
                        Bundle bundle = new Bundle();
                        Utility.logFirebaseEvent(WolooDashboard.this, bundle, AppConstants.INVITE_CLICK);

                        HashMap<String,Object> payload = new HashMap<>();
                        Utility.logNetcoreEvent(WolooDashboard.this, payload, AppConstants.INVITE_CLICK);

                        changeIcon(this.nav_view.getMenu().findItem(R.id.navigation_inviteFriend));
                        currentFragment = InviteFriendFragment.newInstance(false);
                        loadFragment(currentFragment, "InviteFriendFragment");
                        //  bottomViewTransparent90();
                    }
                    return true;
            }
        }
        return false;
    }

    public void showdialogForNavigationReward(boolean isPoints) {
        try {
            SharedPrefSettings.Companion.getGetPreferences().storeIsDirectionWoloo(false);
            SharedPrefSettings.Companion.getGetPreferences().storeDirectionWoloo(null);
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_destinationarrived);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
            TextView tv_subscribe = (TextView) dialog.findViewById(R.id.tv_subscribe);
            TextView tv_text2 = (TextView) dialog.findViewById(R.id.tv_text2);
            tv_text2.setVisibility(View.GONE);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(this);
//                tv_text.setText(getResources().getString(R.string.qrcode_scantext));


            if (authConfigResponse != null) {
                String arrivedDestinationDialogText = authConfigResponse.getcUSTOMMESSAGE().getArrivedDestinationText();
                String arrivedDestinationPoints = authConfigResponse.getcUSTOMMESSAGE().getArrivedDestinationPoints();
//                    String arrivedDestinationDialogText = authConfigResponse.getData().getcUSTOMMESSAGE().getArrivedDestinationDialogText();
//                    tv_text.setText(arrivedDestinationDialogText.replaceAll("\\\\n","\n"));
//                    String str = arrivedDestinationDialogText.replaceAll("\\\\n", "\n");
//                    Spannable spannable = new SpannableString(str);
//                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#B5AA3A")), str.indexOf("10 Woloo points."), str.indexOf("10 Woloo points.") + "10 Woloo points.".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv_text.setText(spannable);
                tv_text.setText(arrivedDestinationDialogText.replaceAll("\\\\n","\n"));
                if(isPoints) {
                    tv_text2.setText(arrivedDestinationPoints);
                    tv_text2.setVisibility(View.VISIBLE);
                    tv_subscribe.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(WolooDashboard.this, AddReviewActivity.class);
                            intent.putExtra(AppConstants.WOLOO_ID,wolooTemp.getWolooid());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }, 3000);
                }
                else{
                    tv_text2.setVisibility(View.GONE);
                    tv_subscribe.setVisibility(View.VISIBLE);
                    tv_subscribe.setText("HOME");
                }
            }
//                tv_subscribe.setText("SCAN QR CODE");


            tv_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }



}
