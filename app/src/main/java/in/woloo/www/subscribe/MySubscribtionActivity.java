package in.woloo.www.subscribe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.cancelsubscription.model.CancelSubscriptionResponse;
import in.woloo.www.cancelsubscription.mvp.CancelSubscriptionPresenter;
import in.woloo.www.cancelsubscription.mvp.CancelSubscriptionView;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.models.GetSubscriptionDetailsResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.subscription.viewmodel.SubscriptionViewModel;

public class MySubscribtionActivity extends AppCompatActivity implements CancelSubscriptionView{

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.frequency)
    TextView frequency;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_class)
    TextView tv_class;

    @BindView(R.id.ll_subscription)
    LinearLayout ll_subscription;

    @BindView(R.id.ll_Voucher)
    LinearLayout ll_Voucher;

    @BindView(R.id.llParentLayout)
    LinearLayout llParentLayout;

    @BindView(R.id.tvSubscriptionDetails)
    TextView tvSubscriptionDetails;

    @BindView(R.id.ivSubscriptionMark)
    LinearLayout ivSubscriptionMark;

    @BindView(R.id.tv_price_info)
    TextView tv_price_info;

    @BindView(R.id.voucherNameTv)
    TextView voucherNameTv;

    @BindView(R.id.description_Wb)
    WebView description_Wb;

    @BindView(R.id.future_description_Wb)
    WebView future_description_Wb;

    @BindView(R.id.ll_expanded)
    LinearLayout ll_expanded;

    @BindView(R.id.tv_startDate)
    TextView tv_startDate;

    @BindView(R.id.tv_endDate)
    TextView tv_endDate;

    @BindView(R.id.future_frequency)
    TextView future_frequency;

    @BindView(R.id.purchasedByTv)
    TextView purchasedByTv;

    @BindView(R.id.future_tv_price)
    TextView future_tv_price;

    @BindView(R.id.future_tv_class)
    TextView future_tv_class;

    @BindView(R.id.future_ll_subscription)
    LinearLayout future_ll_subscription;

    @BindView(R.id.future_llParentLayout)
    LinearLayout future_llParentLayout;

    @BindView(R.id.future_tvSubscriptionDetails)
    TextView future_tvSubscriptionDetails;

    @BindView(R.id.futureSubscriptionMark)
    LinearLayout future_ivSubscriptionMark;

    @BindView(R.id.ll_nodatafound)
    LinearLayout ll_nodatafound;

    @BindView(R.id.appLogo_tv)
    ImageView appLogo_tv;

    @BindView(R.id.voucher_Logo_tv)
    ImageView voucher_Logo_tv;

    @BindView(R.id.future_tv_price_info)
    TextView future_tv_price_info;

    @BindView(R.id.voucherTV)
    TextView voucherTV;

    @BindView(R.id.future_ll_expanded)
    LinearLayout future_ll_expanded;

    @BindView(R.id.future_tv_startDate)
    TextView future_tv_startDate;

    @BindView(R.id.future_tv_endDate)
    TextView future_tv_endDate;

    @BindView(R.id.tv_cancelSubscription)
    TextView tv_cancelSubscription;

    CancelSubscriptionPresenter cancelSubscriptionPresenter;
    SubscriptionViewModel subscriptionViewModel;
    private UserProfile userProfileResponse;
    public boolean expanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribtion);
        ButterKnife.bind(this);
        cancelSubscriptionPresenter = new CancelSubscriptionPresenter(this, this);
        initViews();
        setLiveData();
    }

    private void initViews() {
        //tvTitle.setText(getResources().getString(R.string.mysubscription));
        tvTitle.setText("My Subscription");
        ivBack.setOnClickListener(v -> {
            finish();
        });

        if (getIntent().hasExtra("plan")) {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            this.userProfileResponse = (UserProfile) getIntent().getSerializableExtra("plan");

            if (userProfileResponse != null) {
                if (userProfileResponse.getProfile().getVoucherId() != null || userProfileResponse.getProfile().getSubscriptionId() != null || userProfileResponse.getProfile().getGiftSubscriptionId() == null)
                    tv_cancelSubscription.setVisibility(View.VISIBLE);
                else
                    tv_cancelSubscription.setVisibility(View.GONE);
            } else
                tv_cancelSubscription.setVisibility(View.VISIBLE);

            subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

            if (userProfileResponse.getProfile() != null && !TextUtils.isEmpty(userProfileResponse.getProfile().getExpiryDate()) && CommonUtils.isSubscriptionExpired(userProfileResponse.getProfile().getExpiryDate())) {
                ll_nodatafound.setVisibility(View.VISIBLE);
            } else {
                ll_nodatafound.setVisibility(View.GONE);
//                subscriptionViewModel.getMySubscription();
            }
            subscriptionViewModel.getMySubscription();
        }
    }

    void setLiveData(){
        subscriptionViewModel.observeMySubscription().observe(this, new Observer<BaseResponse<GetSubscriptionDetailsResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<GetSubscriptionDetailsResponse.Data> it) {
                if (it != null) {
                    if (it.getData().getActiveSubscription() == null) {
                        ll_nodatafound.setVisibility(View.VISIBLE);
                        //showdialog(getSubscriptionDetailsResponse.getMessage(), true);
                    } else
                        setData(it.getData());
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getReasonPopup() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_cancel_subscription_reason);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            EditText etRemark = dialog.findViewById(R.id.etRemark);
            etRemark.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (etRemark.hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                    return false;
                }
            });
            Spinner spinner = (Spinner) dialog.findViewById(R.id.cancel_reason_spinner);
            SharedPreference mSharedPref = new SharedPreference(this);
            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, authConfigResponse.getcUSTOMMESSAGE().getCancelSubscriptionReasons().split(","));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            TextView llCancelBtn = dialog.findViewById(R.id.llCancelBtn);
            llCancelBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });
            TextView llOkayBtn = dialog.findViewById(R.id.llOkayBtn);
            llOkayBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if (etRemark.getText().toString().trim().equals("")) {
                        Toast.makeText(MySubscribtionActivity.this, "Please Add Comment", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        cancelSubscriptionPresenter.cancelSubscription(authConfigResponse.getcUSTOMMESSAGE().getCancelSubscriptionReasons().split(",")[spinner.getSelectedItemPosition()],
                                etRemark.getText().toString().trim(),
                                authConfigResponse.getSupportEmail());
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public void cancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
        showdialog("Membership Cancelled Successfully", true);
    }

    public void showdialog(String msg, boolean isCloseActivity) {
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
                        if (isCloseActivity)
                            onBackPressed();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    private void setData(GetSubscriptionDetailsResponse.Data data) {
        setViewData(data);
        ll_nodatafound.setVisibility(View.GONE);
        llParentLayout.setVisibility(View.VISIBLE);
        GetSubscriptionDetailsResponse.ActiveSubscription activeSubscription = data.getActiveSubscription();
        ArrayList<GetSubscriptionDetailsResponse.FutureSubscription> futureSubscription = data.getFutureSubscription();

        if (activeSubscription != null) {
            View cardView;
            ViewStub viewStub = findViewById(R.id.vsCardView);
            if (activeSubscription.getFrequency() != null && activeSubscription.getFrequency().equalsIgnoreCase("annual")) {
                // inflate gold card
                viewStub.setLayoutResource(R.layout.item_gold_membership);
            } else {
                // inflate silver card
                viewStub.setLayoutResource(R.layout.item_silver_membership);
            }
            cardView = viewStub.inflate();
            cardView.findViewById(R.id.tv_upgrade).setVisibility(View.GONE);
            String currencySymbol = "";
            if (!TextUtils.isEmpty(activeSubscription.getCurrency())) {
                if (activeSubscription.getCurrency().equalsIgnoreCase("INR")) {
                    currencySymbol = "\u20B9";
                } else {
                    currencySymbol = activeSubscription.getCurrency();
                }
            }
            ((TextView) cardView.findViewById(R.id.tv_price)).setText(currencySymbol + activeSubscription.getPrice());

            if (userProfileResponse != null && userProfileResponse != null) {
                TextView tvUserName = cardView.findViewById(R.id.tvName);
                if (!TextUtils.isEmpty(userProfileResponse.getProfile().getName())) {
                    tvUserName.setText(userProfileResponse.getProfile().getName());
                } else {
                    tvUserName.setText("Guest");
                }
            }
            TextView tv_startDate = cardView.findViewById(R.id.tvStartDate);
            TextView tv_endDate = cardView.findViewById(R.id.tvEndDate);
            TextView tvBeforeDiscount = cardView.findViewById(R.id.tvBeforeDiscount);

            tv_startDate.setText("START DATE : " + activeSubscription.getStart_at());
            tv_endDate.setText("END DATE : " + activeSubscription.getEnd_at());

            TextView tvDays = cardView.findViewById(R.id.frequency);
            tvDays.setText(activeSubscription.getFrequency() == null ? "" : activeSubscription.getFrequency());

            TextView tvPlanName = cardView.findViewById(R.id.tvPlan);
            tvPlanName.setText(activeSubscription.getFrequency() == null ? "" : activeSubscription.getFrequency().toUpperCase() + tvPlanName.getText());

            try {
                String priceStr = activeSubscription.getPrice();
                Integer beforeDiscountPrice = activeSubscription.getBeforeDiscountPrice();
                if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                    int price = Integer.parseInt(priceStr);
                    if (price < beforeDiscountPrice) {
                        tvBeforeDiscount.setText(String.valueOf(beforeDiscountPrice));
                        tvBeforeDiscount.setVisibility(View.VISIBLE);
                        tvBeforeDiscount.setPaintFlags(tvBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        tvBeforeDiscount.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvBeforeDiscount.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                tvBeforeDiscount.setVisibility(View.INVISIBLE);
            }
        }

        if (futureSubscription != null) {
            for (int i = 0; i < futureSubscription.size(); i++)
            {
                View cardView;
          //  ViewStub viewStub = findViewById(R.id.vsFutureCardView);
                LinearLayout parentLayout = findViewById(R.id.viewstubs_layout); // Replace with your actual parent layout ID

// 2. Create a new ViewStub
                ViewStub viewStub = new ViewStub(this);
                if (futureSubscription.get(i).getFrequency() != null && futureSubscription.get(i).getFrequency().equalsIgnoreCase("annual")) {
                // inflate gold card
                viewStub.setLayoutResource(R.layout.item_gold_membership);
            } else {
                // inflate silver card
                viewStub.setLayoutResource(R.layout.item_silver_membership);
            }
                parentLayout.addView(viewStub);
            cardView = viewStub.inflate();
            cardView.findViewById(R.id.tv_upgrade).setVisibility(View.GONE);
            String currencySymbol = "";
            if (!TextUtils.isEmpty(futureSubscription.get(i).getCurrency())) {
                if (futureSubscription.get(i).getCurrency().equalsIgnoreCase("INR")) {
                    currencySymbol = "\u20B9";
                } else {
                    currencySymbol = futureSubscription.get(i).getCurrency();
                }
            }
            ((TextView) cardView.findViewById(R.id.tv_price)).setText(currencySymbol + futureSubscription.get(i).getPrice());
            cardView.findViewById(R.id.ivSubscriptionMark).setVisibility(View.VISIBLE);
            ((TextView) cardView.findViewById(R.id.tvSubscriptionTitle)).setText("Future");
            if (userProfileResponse != null && userProfileResponse != null) {
                TextView tvUserName = cardView.findViewById(R.id.tvName);
                if (!TextUtils.isEmpty(userProfileResponse.getProfile().getName())) {
                    tvUserName.setText(userProfileResponse.getProfile().getName());
                } else {
                    tvUserName.setText("Guest");
                }
            }
            TextView tv_startDate = cardView.findViewById(R.id.tvStartDate);
            TextView tv_endDate = cardView.findViewById(R.id.tvEndDate);
            TextView tvBeforeDiscount = cardView.findViewById(R.id.tvBeforeDiscount);
            TextView tvSubscriptionDetails = cardView.findViewById(R.id.tvSubscriptionDetails);

            tv_startDate.setText("START DATE : " + futureSubscription.get(i).getStart_at());
            tv_endDate.setText("END DATE : " + futureSubscription.get(i).getEnd_at());

            TextView tvDays = cardView.findViewById(R.id.frequency);
            tvDays.setText(futureSubscription.get(i).getFrequency() == null ? "" : futureSubscription.get(i).getFrequency());

            TextView tvPlanName = cardView.findViewById(R.id.tvPlan);
            tvPlanName.setText(futureSubscription.get(i).getFrequency() == null ? "" : futureSubscription.get(i).getFrequency().toUpperCase() + tvPlanName.getText());

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvSubscriptionDetails.setText(Html.fromHtml(futureSubscription.get(i).getDescription(),
                            Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvSubscriptionDetails.setText(Html.fromHtml(futureSubscription.get(i).getDescription()));
                }
                String priceStr = futureSubscription.get(i).getPrice();
                Integer beforeDiscountPrice = futureSubscription.get(i).getBeforeDiscountPrice();
                if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                    int price = Integer.parseInt(priceStr);
                    if (price < beforeDiscountPrice) {
                        tvBeforeDiscount.setText(String.valueOf(beforeDiscountPrice));
                        tvBeforeDiscount.setVisibility(View.VISIBLE);
                        tvBeforeDiscount.setPaintFlags(tvBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        tvBeforeDiscount.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvBeforeDiscount.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                tvBeforeDiscount.setVisibility(View.INVISIBLE);
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

        if (data.getActiveSubscription().getFrequency() != null)
            frequency.setText(data.getActiveSubscription().getFrequency());
       *//* else
            future_frequency.setText(data.getActiveSubscription().getFrequency() + " Days");*//*
        tv_price.setText("\u20B9 " + data.getActiveSubscription().getPrice());
        //tv_price_info.setText(data.getActiveSubscription().getDescription());
        description_Wb.setBackgroundColor(Color.TRANSPARENT);
        description_Wb.getSettings().setDomStorageEnabled(true);
        description_Wb.getSettings().setAppCacheEnabled(true);
        description_Wb.getSettings().setLoadsImagesAutomatically(true);
        description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        description_Wb.getSettings().setJavaScriptEnabled(true);
        description_Wb.loadDataWithBaseURL(null, data.getActiveSubscription().getDescription(), "text/html", "UTF-8", null);
        tvSubscriptionDetails.setText("Buy Now " + data.getActiveSubscription().getFrequency() + " Membership");
        if (data.getActiveSubscription().getFrequency().equalsIgnoreCase("Monthly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
        } else if (data.getActiveSubscription().getFrequency().equalsIgnoreCase("Quarterly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        } else if (data.getActiveSubscription().getFrequency().equalsIgnoreCase("Half-yearly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
        } else if (data.getActiveSubscription().getFrequency().equalsIgnoreCase("Yearly")) {
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
//            if (data.getFutureSubscription().getFrequency() != null)
//                future_frequency.setText(data.getFutureSubscription().getFrequency());
//            else
//                future_frequency.setText(data.getFutureSubscription().getFrequency() + " Days");
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
//            future_tvSubscriptionDetails.setText("Buy Now " + data.getFutureSubscription().getFrequency() + " Membership");
//            if (data.getFutureSubscription().getFrequency().equalsIgnoreCase("Monthly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
//            } else if (data.getFutureSubscription().getFrequency().equalsIgnoreCase("Quarterly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
//            } else if (data.getFutureSubscription().getFrequency().equalsIgnoreCase("Half-yearly")) {
//                future_ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
//            } else if (data.getFutureSubscription().getFrequency().equalsIgnoreCase("Yearly")) {
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

    private void setViewData(GetSubscriptionDetailsResponse.Data data) {
        tv_cancelSubscription.setOnClickListener(v -> {
            String purchasedBy = "";
            if (data.getPurchase_by() != null)
                purchasedBy = data.getPurchase_by();
            if (userProfileResponse.getPlanData().isIs_cancel()) {
                showdialog("You have already Canceled the Membership", false);
            } else if (data.getActiveSubscription().getName().equals("FREE TRIAL")) {
                showdialog("You don't have an active membership", false);
            } else if (purchasedBy.equals(AppConstants.PURCHASE_BY_APPLE)) {
                showdialog(AppConstants.PURCHASE_BY_APPLE_MSG, false);
            } else {
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.dialog_discontinue_subscription);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                TextView discontinueTextTv = dialog.findViewById(R.id.discontinueTextTv);
                discontinueTextTv.setText("Even if you choose to cancel your current membership, you can still use the app benefits until the end of current billing period.");
                LinearLayout ll_keepMyMembership = (LinearLayout) dialog.findViewById(R.id.ll_keep_my_membership);
                LinearLayout ll_continueToCancel = (LinearLayout) dialog.findViewById(R.id.ll_continue_to_cancel);
                ll_keepMyMembership.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ll_continueToCancel.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getReasonPopup();
                    }
                });
                dialog.show();
            }
        });
    }
}