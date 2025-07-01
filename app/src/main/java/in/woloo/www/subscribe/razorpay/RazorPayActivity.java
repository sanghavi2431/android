package in.woloo.www.subscribe.razorpay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.giftcard.mvp.GiftCardView;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.models.GetSubscriptionDetailsResponse;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.subscribe.razorpay.mvp.RazorPayPresenter;
import in.woloo.www.subscribe.razorpay.mvp.RazorPayView;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.subscription.model.SubmitSubscriptionPurchaseRequest;
import in.woloo.www.v2.subscription.viewmodel.SubscriptionViewModel;

public class RazorPayActivity extends AppCompatActivity implements PaymentResultWithDataListener, RazorPayView {

    private static final String TAG = RazorPayActivity.class.getSimpleName();
    private String planId;
    private String subscriptionId;
//    private RazorPayPresenter razorPayPresenter;
SubscriptionViewModel subscriptionViewModel;
    private String msg;
    private boolean isEmail;
    private String mobile;
    private String order_id, amount;
    GiftCardView giftCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay);
        ButterKnife.bind(this);
        Checkout.preload(getApplicationContext());
        planId = getIntent().getStringExtra(AppConstants.PLAN_ID);
        if (getIntent().hasExtra(AppConstants.SUBSCRIPTION_ID))
            subscriptionId = getIntent().getStringExtra(AppConstants.SUBSCRIPTION_ID);
        else {
            order_id = getIntent().getStringExtra(AppConstants.ORDER_ID);
            amount = getIntent().getStringExtra(AppConstants.ORDER_AMOUNT);
           // giftCardView = (GiftCardView) getIntent().getSerializableExtra(AppConstants.GIFT_CARD_VIEW_PRESENTER);
        }
        msg = getIntent().getStringExtra(AppConstants.MSG);
        isEmail = getIntent().getBooleanExtra(AppConstants.IS_EMAIL, false);
        mobile = getIntent().getStringExtra(AppConstants.MOBILE);
//        razorPayPresenter = new RazorPayPresenter(this, RazorPayActivity.this);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        startPayment();
        setLiveData();
    }

    void setLiveData(){
        subscriptionViewModel.observeSubmitSubscriptionPurchaseResponse().observe(this, new Observer<BaseResponse<InitSubscriptionResponse>>() {
            @Override
            public void onChanged(BaseResponse<InitSubscriptionResponse> it) {
                try{
                    if (it != null && it.getSuccess()) {
                        Logger.i(TAG, "onPaymentSuccess: line 161 ");
                        showPaymentSuccessDialog();
                    }else{
                        WolooApplication.setErrorMessage("");
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            CommonUtils commonUtils = new CommonUtils();
            AuthConfigResponse.Data authConfigResponse = commonUtils.authconfig_response(this);
            if (authConfigResponse != null && authConfigResponse != null && authConfigResponse.getrZCRED() != null) {
                if (!TextUtils.isEmpty(authConfigResponse.getrZCRED().getKey())) {
                    co.setKeyID(authConfigResponse.getrZCRED().getKey());
                }
            }
            JSONObject options = new JSONObject();
            options.put("name", "Woloo");

            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            if (getIntent().hasExtra(AppConstants.SUBSCRIPTION_ID)) {
                options.put("order_id", subscriptionId);
                options.put("description", "Woloo Pee’rs Club Membership");
            }
            else {
                options.put("description", "Send Points");
                options.put("order_id", order_id);
                options.put("amount", amount);
            }
            JSONObject preFill = new JSONObject();
                preFill.put("email", msg);
                preFill.put("contact", mobile);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
              CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        try {
            Logger.i(TAG, "onPaymentSuccess: razorpayPaymentID : " + razorpayPaymentID);
            Logger.i(TAG, "onPaymentSuccess: OrderId : " + paymentData.getOrderId());
            Logger.i(TAG, "onPaymentSuccess: Signature : " + paymentData.getSignature());

            if (getIntent().hasExtra(AppConstants.isGiftSub)) {
                Logger.i(TAG, "onPaymentSuccess: line 117 ");
                try {
                    Bundle bundle = new Bundle();
                    if (getIntent().hasExtra(AppConstants.gift_numbers))
                        bundle.putString(AppConstants.SENT_GIFT_NUMBERS, getIntent().getStringExtra(AppConstants.gift_numbers));
                    Logger.i(TAG, "onPaymentSuccess: line 122 "+ bundle.toString());
                    Utility.logFirebaseEvent(RazorPayActivity.this, bundle, AppConstants.GIFT_SUBSCRIPTION_COMPLETED);
                } catch (Exception ex) {

                }
            }

            if (getIntent().hasExtra(AppConstants.SUBSCRIPTION_ID))
            {
                Logger.i(TAG, "onPaymentSuccess: line 131 ");
                boolean isFutureSubscription = false;
                if(getIntent().hasExtra(AppConstants.FUTURE_SUBSCRIPTION))
                    isFutureSubscription = true;
                SubmitSubscriptionPurchaseRequest request = new SubmitSubscriptionPurchaseRequest();
                request.setPaymentId(razorpayPaymentID);
                request.setSubscriptionId(subscriptionId);
                request.setPlanId(planId);
                request.setFuture(isFutureSubscription);
                subscriptionViewModel.submitSubscriptionPurchase(request);
//                razorPayPresenter.submitSubscriptionPurchase(planId, razorpayPaymentID, paymentData.getSignature(), subscriptionId, isFutureSubscription);
            }
            else {
                showPaymentSuccessDialog();
                Logger.i(TAG, "onPaymentSuccess: line 139 ");
            }

        } catch (Exception e) {
            Logger.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        try {
//            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Payment failed!", Toast.LENGTH_SHORT).show();
            RazorPayActivity.this.finish();
        } catch (Exception e) {
            Logger.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onPaymentSuccess() {
        try{
            Logger.i(TAG, "onPaymentSuccess: line 161 ");
            showPaymentSuccessDialog();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public void showPaymentSuccessDialog(){
        try {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_payment_success);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_paymentsuccess = (TextView) dialog.findViewById(R.id.tv_paymentsuccess);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(RazorPayActivity.this);
            if (authConfigResponse != null) {
                String paymentSuccessDialogText = authConfigResponse.getcUSTOMMESSAGE().getPaymentSuccessDialogText();
                tv_paymentsuccess.setText(paymentSuccessDialogText.replaceAll("\\\\n","\n"));
            }

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) dialog.dismiss();
                    //onBackPressed();
                    if (getIntent().hasExtra(AppConstants.SUBSCRIPTION_ID)) {
                        /*Intent i = new Intent(RazorPayActivity.this, WolooDashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);*/
                        RazorPayActivity.this.finish();
                    } else if (getIntent().hasExtra(AppConstants.isGiftSub)) {
                        Intent i = new Intent(RazorPayActivity.this, WolooDashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        RazorPayActivity.this.finish();
                    } else {
                        RazorPayActivity.this.finish();
                        //   giftCardView.pointsAddedResponseSuccess();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

}