package in.woloo.www.login;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.netcore.android.Smartech;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.base.BaseActivity;
import in.woloo.www.boarding.BoardingActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.fcm.DeeplinkReceiver;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.splash.PendingReviewStatusResponse;
import in.woloo.www.splash.SplashPresenter;
import in.woloo.www.splash.SplashToRedirectionInterface;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.DeeplinkDispatcherActivity;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.login.activity.LoginActivity;
import in.woloo.www.v2.search.SearchActivity;
import in.woloo.www.v2.splash.model.LocaleRequest;
import in.woloo.www.v2.util.NetcoreUserDetails;
import in.woloo.www.vtion.activities.VitionUserDataActivity;
import io.hansel.hanselsdk.Hansel;
import io.hansel.hanselsdk.HanselDeepLinkListener;
import io.hansel.ujmtracker.HanselInternalEventsListener;
import io.hansel.ujmtracker.HanselTracker;

import static in.woloo.www.dashboard.WolooDashboard.ARG_IS_SHOW_PERIOD_TRACKER;
import static in.woloo.www.dashboard.WolooDashboard.ARG_IS_SHOW_SHOP;
import static in.woloo.www.dashboard.WolooDashboard.ARG_IS_SHOW_THIRST_REMINDER;
import static in.woloo.www.dashboard.WolooDashboard.ARG_IS_SHOW_TRENDING_BLOGS;
import static in.woloo.www.search.SearchWolooActivity.ARG_SHOW_OFFERS;
import static in.woloo.www.utils.AppConstants.BLOG_CONTENT_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.NEARBY_WOLOO_OFFERS;
import static in.woloo.www.utils.AppConstants.NEW_WOLOO_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PERIOD_TRACKER_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PRODUCT_OFFER_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.THIRST_REMINDER_NOTIFICATION;

public class SplashActivity extends BaseActivity//, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 90;
    private static final String TAG = SplashActivity.class.getSimpleName();

    private HomeViewModel homeViewModel;

    @BindView(R.id.ivAppLogo)
    ImageView ivAppLogo;
    @BindView(R.id.tvPoweredBy)
    TextView tvPoweredBy;
    @BindView(R.id.llNextSkip)
    LinearLayout llNextSkip;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvSplashText)
    TextView tvSplashText;


    @BindView(R.id.tv_skip)
    TextView tv_skip;

    @BindView(R.id.loading_animation)
    LottieAnimationView loading_animation;
    private CommonUtils mCommonUtils;
    private GoogleApiClient mGoogleApiClient;

    public String refer_url;
    private String refer_code;
    private String referralcode = "";
    private boolean notNow;
    private boolean isProcessing;

    int DIALOGID = 2;

    @RequiresApi(api = Build.VERSION_CODES.P)
    /*calling  onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        new DeeplinkReceiver().onReceive(this, getIntent());
        Hansel.pairTestDevice(getIntent().getDataString());
        initView();
        setUpHanselListeners();
        setLiveData();
    }

    private void setUpHanselListeners() {
        HanselInternalEventsListener hanselInternalEventsListener = new HanselInternalEventsListener() {
            @Override
            public void onEvent(String eventName, HashMap dataFromHansel) {
                HashMap<String, Object> payload = new HashMap<>();
                Smartech.getInstance(new WeakReference(this)).trackEvent(eventName, payload);
            }
        };
        //Register the listener with Hansel SDK by using this code.
        HanselTracker.registerListener(hanselInternalEventsListener);
        Hansel.registerHanselDeeplinkListener(new HanselDeepLinkListener() {
            @Override
            public void onLaunchUrl(String url) {
                // Perform task based on url
                Logger.w(TAG,"Hansel-> registerHanselDeeplinkListener");
                Logger.w(TAG,url);

                if (!url.isEmpty()) {

                    Logger.i("DeeplinkReceiver", "Deeplink: " + url);
                    Intent dispatcherIntent = new Intent(SplashActivity.this, DeeplinkDispatcherActivity.class);
                    dispatcherIntent.putExtra("deeplink", url);
                    dispatcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dispatcherIntent);

                } else {
                    Logger.v("Activity", "does not have deeplink path.");
                }

            }
        });
    }

    private void setLiveData() {
        homeViewModel.observeAppConfig().observe(this, new Observer<AuthConfigResponse.Data>() {
            @Override
            public void onChanged(AuthConfigResponse.Data data) {
                if(data != null) {
                    Logger.i(TAG, "observeAppConfig");
                    storeAppConfig(data);
                }
            }
        });

        homeViewModel.observePendingReviewStatus().observe(this, new Observer<BaseResponse<PendingReviewStatusResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PendingReviewStatusResponse.Data> pendingReviewStatusResponse) {
                try {
                    Logger.i(TAG, "pendingReviewStatusResponse");
                    if (pendingReviewStatusResponse != null && pendingReviewStatusResponse.getData() != null) {
                        Intent intent = new Intent(SplashActivity.this, AddReviewActivity.class);
                        intent.putExtra(AppConstants.WOLOO_ID, pendingReviewStatusResponse.getData().getWolooId());
                        startActivity(intent);
                        finish();
                    } else {
                       /* SharedPrefSettings sps = new SharedPrefSettings();
                        if(sps.fetchIsVTION()){
                            callVitionActivity();
                        }
                        else{*/
                            startDashboardActivity();
                       // }

                    }
                } catch (Exception ex) {
                    CommonUtils.printStackTrace(ex);
                    SharedPrefSettings sps = new SharedPrefSettings();
                   /* if(sps.fetchIsVTION()){
                        callVitionActivity();
                    }
                    else {*/
                        startDashboardActivity();
                    //}
                }
            }
        });
    }

    private void storeAppConfig(AuthConfigResponse.Data data) {
        SharedPrefSettings.Companion.getGetPreferences().storeAuthConfig(data);

        if (data.getmAINTENANCESETTINGS().getMaintenanceFlag().equals("1")) {
            CommonUtils.showMaintenanceDialog(this, false);
        } else if (mCommonUtils.isLoggedIn()) {
            //startActivity(new Intent(SplashActivity.this,WolooDashboard.class));
            //finish();
            Log.d("Aarati" , "going to this flow");
            Bundle bundle = new Bundle();
            Utility.logFirebaseEvent(this, bundle, AppConstants.APP_OPEN_AGAIN);
           /* SharedPrefSettings sps = new SharedPrefSettings();
            if(sps.fetchIsVTION()){
                callVitionActivity();
            }
            else {*/
                showForceUpdateDialog(data);
           // }
        } else {
            startSplashTextAnimation();
        }
    }

    /*calling  onResume*/
    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }

    /*calling  initView*/
    private void initView() {
        try {
            Logger.i(TAG, "initView");
            mCommonUtils = new CommonUtils();
            mSharedPreference.setStoredBooleanPreference(this, SharedPreferencesEnum.APP_LAUNCHED_ONE_TIME.getPreferenceKey(), true);
            loading_animation.setVisibility(VISIBLE);
            tv_skip.setVisibility(GONE);
            //checkAndRequestPermissions();
            startAnimation();
            onRedirection();
//            splashPresenter.setDefaultInit();
//            splashPresenter.initJetEncryptor();
            tvNext.setOnClickListener(v -> {
                /*startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();*/
                if(SharedPrefSettings.Companion.getGetPreferences().issShownOnBoarding()){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, BoardingActivity.class));
                }
                finish();
            });

            tv_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SplashActivity.this, WolooDashboard.class));
                    finish();
                }
            });

            try {
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                // Get deep link from result (may be null if no link is found)
                                try {
                                    Uri deepLink = null;
                                    if (pendingDynamicLinkData != null) {
                                        deepLink = pendingDynamicLinkData.getLink();
                                        Logger.e("Dynamic Link  ", deepLink.toString());
                                        //Toast.makeText(SplashActivity.this, deepLink.toString(), Toast.LENGTH_SHORT).show();
                                        if (deepLink != null) {
                                            String voucher;
                                            if (deepLink.toString().contains("voucher")) {
                                                try {
                                                    Uri uri = Uri.parse(deepLink.toString());
                                                    voucher = uri.getQueryParameter("voucher");
                                                } catch (Exception e){
                                                    displayToast("Error while applying coupon!");
                                                    return;
                                                }
                                                if(TextUtils.isEmpty(voucher)) return;
                                                //String voucher = deepLink.toString().split("/")[deepLink.toString().split("/").length - 2];
                                                mSharedPreference.setStoredPreference(SplashActivity.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), voucher);
                                            } else if (deepLink.toString().contains("/wahcertificate/")) {
                                                String wahCertificate = deepLink.toString().split("/")[deepLink.toString().split("/").length - 3];
                                                mSharedPreference.setStoredPreference(SplashActivity.this, SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(), wahCertificate);
                                            } else if (deepLink.toString().contains("/giftcard")) {
                                                //Toast.makeText(SplashActivity.this, deepLink.toString(), Toast.LENGTH_SHORT).show();
                                                mSharedPreference.setStoredPreference(SplashActivity.this, SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.getPreferenceKey(), "GiftCard");
                                            } else if (deepLink.toString().contains("giftId=")) {
                                                try {
                                                    Uri uri = Uri.parse(deepLink.toString());
                                                    String giftId = uri.getQueryParameter("giftId");
                                                    if(TextUtils.isEmpty(giftId)) return;
                                                    mSharedPreference.setStoredPreference(SplashActivity.this, SharedPreferencesEnum.GIFT_CARD_ID.getPreferenceKey(), giftId);
                                                } catch (Exception e){
                                                    displayToast("Error while applying coupon!");
                                                    return;
                                                }
                                            } else if (deepLink.toString().contains("/mobile_blog_detail")) {
                                                getIntent().setData(pendingDynamicLinkData.getLink());
                                            }
                                            else {
                                                referralcode = deepLink.toString().substring(43);
                                                Logger.e("referralcode  ", referralcode);
                                                referralcode = commonUtils.getBase64Decoded(referralcode);
                                                Logger.e("referralcode", "getBase64Decoded: " + referralcode);
                                                mSharedPreference.setStoredPreference(SplashActivity.this, SharedPreferencesEnum.REFERRAL_CODE.getPreferenceKey(), referralcode);
                                                SharedPrefSettings.Companion.getGetPreferences().storeReferralCode(referralcode);
                                            }
                                        }
                                    }
                                } catch (Exception exception) {
                                    CommonUtils.printStackTrace(exception);
                                }
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Logger.w(TAG, "getDynamicLink:onFailure", e);
                            }
                        });


            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling  startAnimation*/
    public void startAnimation() {
        try {
            Logger.i(TAG, "startAnimation");
            Animation aniScaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
            ivAppLogo.startAnimation(aniScaleUp);
            aniScaleUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling  startSplashTextAnimation*/
    private void startSplashTextAnimation() {
        try {
            Logger.i(TAG, "startSplashTextAnimation");
            Animation aniBottomUpLogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottomtoup);
            ivAppLogo.startAnimation(aniBottomUpLogo);

            aniBottomUpLogo.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            Animation aniBottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottomtouptwo);
            tvSplashText.startAnimation(aniBottomUp);

            aniBottomUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    tvSplashText.setVisibility(VISIBLE);
                    llNextSkip.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
            llNextSkip.setVisibility(VISIBLE);
        }
    }

    /*calling  onRedirection*/
    public void onRedirection() {
        loading_animation.setVisibility(GONE);

        Logger.i(TAG, "onRedirection");
        //splashPresenter.getAuthConfig();

        LocaleRequest.Locale request = new LocaleRequest.Locale();
        request.setPackageName("in.woloo.www");
        request.setPlatform("android");

        LocaleRequest localeRequest = new LocaleRequest();
        localeRequest.setLocale(request);

        homeViewModel.getAppConfig(localeRequest);
    }

    private void startDashboardActivity() {
        new NetcoreUserDetails(this).checkIfUserIdentityIsSet();
        Intent intent = new Intent(SplashActivity.this, WolooDashboard.class);
        if (getIntent().getAction() != null) {
            switch (getIntent().getAction()) {
                case BLOG_CONTENT_NOTIFICATION:
                    intent.putExtra(ARG_IS_SHOW_TRENDING_BLOGS, true);
                    break;
                case PERIOD_TRACKER_NOTIFICATION:
                    intent.putExtra(ARG_IS_SHOW_PERIOD_TRACKER, true);
                    break;
                case NEW_WOLOO_NOTIFICATION:
                    break;
                case NEARBY_WOLOO_OFFERS:
                    intent = new Intent(SplashActivity.this, SearchActivity.class);
                    intent.putExtra(ARG_SHOW_OFFERS, true);
                    break;
                case PRODUCT_OFFER_NOTIFICATION:
                    intent.putExtra(ARG_IS_SHOW_SHOP, true);
                    break;
                case THIRST_REMINDER_NOTIFICATION:
                    intent.putExtra(ARG_IS_SHOW_THIRST_REMINDER, true);
                    break;
            }
            if (getIntent() != null && getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());

                Uri uri = getIntent().getData();
                if (uri != null && uri.getPath().contains("mobile_blog_detail")) {
                    intent.putExtra(ARG_IS_SHOW_TRENDING_BLOGS, true);
                    intent.putExtra("blog_link", getIntent().getData().toString());
                }
            }
        }
        startActivity(intent);
        finish();
    }


    /*calling  checkAndRequestPermissions*/
    private boolean checkAndRequestPermissions() {
        Logger.i(TAG, "checkAndRequestPermissions");
        int permissionAccessFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean isLuckyPatcherCheck(List<String> blockPackageList) {
        Logger.i(TAG, "isLuckyPatcherCheck");
        boolean isValidate = false;
        try {
            List<String> allPackageOfMobile = commonUtils.getListOfInstalledApps(SplashActivity.this);
            for (int i = 0; i < allPackageOfMobile.size(); i++) {
                for (int j = 0; j < blockPackageList.size(); j++) {
                    if (blockPackageList.get(j).equalsIgnoreCase(allPackageOfMobile.get(i))) {
                        isValidate = true;
                        break;
                    }
                }
                if (isValidate) {
                    break;
                }
            }
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
        return isValidate;
    }

    /*calling  showForceUpdateDialog*/
    private void showForceUpdateDialog(final AuthConfigResponse.Data outputAppConfiqModel) {
        try {
            Logger.i(TAG, "showForceUpdateDialog");
            if (notNow) {
                /**
                 * Already Dialog is shown and user chose not now
                 */
                return;
            }
            String versionName = "";
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionName = String.valueOf(pInfo.versionCode);
            } catch (Exception e) {
                CommonUtils.printStackTrace(e);
            }
            if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(outputAppConfiqModel.getaPPVERSION().getVersionCode())
            ) {
                double appVersion = Double.parseDouble(versionName);
                double liveAppVersion = Double.parseDouble(outputAppConfiqModel.getaPPVERSION().getVersionCode().trim());
                if (appVersion < liveAppVersion) {
                    String checkUpdateType = outputAppConfiqModel.getaPPVERSION().getForceUpdate();
                    //checkUpdateType = "1";
                    boolean cancelable = false;
                    int type = 0;
                    if ("0".equals(checkUpdateType)) {
                        type = 2;
                    } else if ("1".equals(checkUpdateType)) {
                        type = 1;
                    }


                    final Dialog customUpdateDialog = CommonUtils.getAppUpdateDialog(SplashActivity.this, cancelable, type);

                    Button mCancelButton = customUpdateDialog.findViewById(R.id.app_update_cancel_button);
                    TextView mMessageTextView = customUpdateDialog.findViewById(R.id.app_update_text);
                    try {
                        //here we setting message update text from server
                        String updateText = outputAppConfiqModel.getaPPVERSION().getUpdateText();
                        if (updateText == null) {
                            mMessageTextView.setText(getResources().getString(R.string.update_text));
                        } else {
                            mMessageTextView.setText(updateText);
                        }
                    } catch (Exception e) {
                        mMessageTextView.setText(getResources().getString(R.string.update_text));
                        CommonUtils.printStackTrace(e);
                    }
                    if ("1".equals(checkUpdateType)) {
                        mCancelButton.setVisibility(View.GONE);
                    } else {
                        mCancelButton.setVisibility(View.VISIBLE);
                    }
                    mCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notNow = true;
                            customUpdateDialog.dismiss();
                            redirectedToApplication();
                        }
                    });
                    Button mUpdateButton = customUpdateDialog.findViewById(R.id.app_update_update_button);
                    mUpdateButton.setVisibility(View.VISIBLE);
                    mUpdateButton.setOnClickListener(view -> {
                        try {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id="
                                                + BuildConfig.APPLICATION_ID)));
                            }
                            finishAffinity();
                        } catch (Exception e) {
                            CommonUtils.printStackTrace(e);
                        }
                    });

                    customUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            isProcessing = false;
                        }
                    });
                    if (customUpdateDialog != null) {
                        isProcessing = true;
                        customUpdateDialog.show();
                    }
                } else {
                    redirectedToApplication();
                }
            } else {
                redirectedToApplication();
            }
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
            redirectedToApplication();
        }

    }

    /*calling  redirectedToApplication*/
    private void redirectedToApplication() {
        Logger.i(TAG, "redirectedToApplication");
        homeViewModel.getPendingReviewStatus();
    }

    private void callVitionActivity() {
        Intent intent = new Intent(this, VitionUserDataActivity.class);
        startActivity(intent);
        finish();
    }

}