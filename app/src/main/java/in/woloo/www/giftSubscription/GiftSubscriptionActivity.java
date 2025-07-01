package in.woloo.www.giftSubscription;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.giftSubscription.model.GetGiftPlansResponse;
import in.woloo.www.giftSubscription.model.SendGiftCardResponse;
import in.woloo.www.giftSubscription.mvp.GiftSubscriptionPresenter;
import in.woloo.www.giftSubscription.mvp.GiftSubscriptionView;
import in.woloo.www.invite_friend.fragments.ImportContactsAsync;
import in.woloo.www.invite_friend.fragments.contacts.InviteContactsActivity;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.splash.UserDetails;
import jagerfield.mobilecontactslibrary.Contact.Contact;

public class GiftSubscriptionActivity extends AppCompatActivity implements GiftSubscriptionView {

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

    @BindView(R.id.ll_expanded)
    LinearLayout ll_expanded;

    @BindView(R.id.tv_startDate)
    TextView tv_startDate;

    @BindView(R.id.tv_endDate)
    TextView tv_endDate;

    @BindView(R.id.etAmount)
    EditText etAmount;

    @BindView(R.id.etMobileNumber)
    TextView etMobileNumber;

    @BindView(R.id.llsendButton)
    TextView llsendButton;

    @BindView(R.id.etMessage)
    EditText etMessage;

    GiftSubscriptionPresenter giftSubscriptionPresenter;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 85;
    protected static SharedPreference mSharedPreference;
    String price = "0";
    CommonUtils commonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_subscription);
        ButterKnife.bind(this);
        commonUtils = new CommonUtils();
        giftSubscriptionPresenter = new GiftSubscriptionPresenter(this, this);
        llParentLayout.setVisibility(View.GONE);
        initViews();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        mSharedPreference = new SharedPreference(this);
        mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.GIFT_CARD_DEEP_LINK.getPreferenceKey(), "");
        tvTitle.setText(getResources().getString(R.string.giftSubscription));
        ivBack.setOnClickListener(v -> {
            finish();
        });

        etMobileNumber.setOnClickListener(v -> {
            try {
                if (checkAndRequestPermissions()) {
                    ContactsLogs();
                }
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
        });

        etMessage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (etMessage.hasFocus()) {
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

        llsendButton.setOnClickListener(v -> {
            if (isValidate()) {
                giftSubscriptionPresenter.sendGiftCard(etAmount.getText().toString().trim(), etMessage.getText().toString().trim(), etMobileNumber.getText().toString().trim());
            }
        });

        giftSubscriptionPresenter.getGiftPlans();

    }

    private boolean isValidate() {
        try {
            //Logger.i(TAG, "isValidate");
            String mobileNumber = etMobileNumber.getText().toString().trim();
            if (TextUtils.isEmpty(etAmount.getText().toString().trim())) {
                Toast.makeText(this.getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(mobileNumber)) {
                Toast.makeText(this.getApplicationContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                Toast.makeText(this.getApplicationContext(), "Please enter message", Toast.LENGTH_SHORT).show();
                return false;
            }
            UserDetails user = new CommonUtils().getUserInfo();
            //LoginResponse userInfo = new CommonUtils().getUserInfo(this);
            if (mobileNumber.equals(user.getMobile())) {
                Toast.makeText(this.getApplicationContext(), "You can not send Gift to YourSelf", Toast.LENGTH_SHORT).show();
                return false;
            }
            /*if(userCoinsResponse != null && userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null && userCoinsResponse.getData().getTotalCoins() < Integer.parseInt(etAmount.getText().toString().trim())){
                //Toast.makeText(this.getApplicationContext(),"You don't have enough points",Toast.LENGTH_SHORT).show();
                return false;
            }
*/
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {
        // Logger.i(TAG, "checkAndRequestPermissions");
        int permissionReadContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionWriteContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionWriteContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void getGiftPlansResponse(GetGiftPlansResponse getGiftPlansResponse) {
        try {
            if (getGiftPlansResponse != null && getGiftPlansResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                setData(getGiftPlansResponse);
            } else {
                CommonUtils.showCustomDialog(this, getGiftPlansResponse.getMessage());
            }
        } catch (Exception exception) {

        }
    }

    @Override
    public void sendGiftCardResponse(SendGiftCardResponse sendGiftCardResponse) {
        try {
            if (sendGiftCardResponse != null && sendGiftCardResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                UserDetails userInfo = new CommonUtils().getUserInfo();
                boolean isEmail = false;
                String email = "";
                if (!TextUtils.isEmpty(userInfo.getEmail())) {
                    isEmail = true;
                    email = userInfo.getEmail();
                }
                CommonUtils.navigateToRazorPayFlow(this, etMobileNumber.getText().toString().trim(), sendGiftCardResponse.getOrderId(), email, isEmail, userInfo.getMobile(), false, null, false, true);
            } else {
                CommonUtils.showCustomDialog(this, sendGiftCardResponse.getMessage());
            }
        } catch (Exception exception) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Logger.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    ContactsLogs();

                } else {
                    checkAndRequestPermissions();
                }
            }
        }
    }

    private void ContactsLogs() {
        Intent i = new Intent(GiftSubscriptionActivity.this, InviteContactsActivity.class);
        //i.putExtra("ARRAYLIST", jsonArray.toString());
        i.putExtra("isGiftSub", "");
        //i.putExtra(AppConstants.REFCODE, refcode);
        startActivityForResult(i, 999);
    }

    private void setData(GetGiftPlansResponse data) {
        llParentLayout.setVisibility(View.VISIBLE);
        if (data.getData().get(0).getFrequency() != null)
            frequency.setText(data.getData().get(0).getFrequency());
            /*else
                frequency.setText(data.getData().get(0).getFrequency() + " Days");*/
        tv_price.setText("\u20B9 " + data.getData().get(0).getPrice());
        price = data.getData().get(0).getPrice();
        tv_class.setText(data.getData().get(0).getName());
        //  tv_price_info.setText(data.getData().get(0).getDescription());
        description_Wb.setBackgroundColor(Color.TRANSPARENT);
        description_Wb.getSettings().setDomStorageEnabled(true);
        //description_Wb.getSettings().setAppCacheEnabled(true); // commented By Aarati
        description_Wb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // Added by Aarati
        description_Wb.getSettings().setLoadsImagesAutomatically(true);
        description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        description_Wb.getSettings().setJavaScriptEnabled(true);
        description_Wb.loadDataWithBaseURL(null, data.getData().get(0).getDescription(), "text/html", "UTF-8", null);
        //description_Wb.loadData(data.getData().get(0).getDescription(), "text/html", "UTF-8");

        tvSubscriptionDetails.setText("Buy Now " + data.getData().get(0).getFrequency() + " Membership");
        if (data.getData().get(0).getFrequency().equalsIgnoreCase("Monthly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
        } else if (data.getData().get(0).getFrequency().equalsIgnoreCase("Quarterly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        } else if (data.getData().get(0).getFrequency().equalsIgnoreCase("Half-yearly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
        } else if (data.getData().get(0).getFrequency().equalsIgnoreCase("Yearly")) {
            ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        }
        tv_startDate.setText(data.getData().get(0).getStart_at());
        tv_endDate.setText(data.getData().get(0).getEnd_at());
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        commonUtils.hideProgress();
        try {
            if (requestCode == 999 && resultCode == RESULT_OK) {
                etMobileNumber.setText(data.getStringExtra("mobilenumber"));
                int totalNumber = Integer.parseInt(data.getStringExtra("totalNumbers"));
                int prize = Integer.parseInt(price);
                llsendButton.setText("Pay \u20B9 " + totalNumber * prize + " (" + totalNumber + " x " + prize + ")");
            }
        } catch (Exception exception) {
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}