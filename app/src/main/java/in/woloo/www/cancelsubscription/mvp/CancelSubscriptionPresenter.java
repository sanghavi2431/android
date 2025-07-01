package in.woloo.www.cancelsubscription.mvp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.cancelsubscription.model.CancelSubscriptionResponse;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.more.models.SubscriptionStatusResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.models.VoucherDetailsResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;

import static in.woloo.www.dashboard.WolooDashboard.TAG;


public class CancelSubscriptionPresenter implements NetworkAPIResponseCallback {

    private Context context;
    private CancelSubscriptionView cancelSubscriptionView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;
    private static final String DEFAULT_SUPPORT_EMAIL  = "woloo.in@gmail.com";

    public CancelSubscriptionPresenter(Context context, CancelSubscriptionView cancelSubscriptionView) {
        this.context = context;
        this.cancelSubscriptionView = cancelSubscriptionView;
        this.mCommonUtils = new CommonUtils();
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
    }

    public void cancelSubscription(String reason, String remark, AuthConfigResponse.SUPPORT_EMAIL supportEmail) {
        /*Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:woloo.in@gmail.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"woloo.in@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, reason);
        intent.putExtra(Intent.EXTRA_TEXT, remark);
        try{
            //Intent.createChooser(intent, "Send email");
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "No email app", Toast.LENGTH_SHORT).show();
        }*/

        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        String emailAddress;
        try {
            if (supportEmail != null && !TextUtils.isEmpty(supportEmail.getId())) {
                emailAddress = supportEmail.getId();
            } else {
                emailAddress = DEFAULT_SUPPORT_EMAIL;
            }
        } catch (NullPointerException e) {
            emailAddress = DEFAULT_SUPPORT_EMAIL;
        }

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, reason);
        emailIntent.putExtra(Intent.EXTRA_TEXT, remark);
        emailIntent.setSelector(selectorIntent);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
        }

        /*try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("cancel_reason",reason);
            mJsObjParam.put("remark",remark);
            Type parserType = new TypeToken<CancelSubscriptionResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.CANCEL_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }*/
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.CANCEL_SUBSCRIPTION:
                try {
                    CancelSubscriptionResponse cancelSubscriptionResponse = (CancelSubscriptionResponse) networkAPICallModel.getResponseObject();
                    if (cancelSubscriptionResponse != null) {
                        cancelSubscriptionResponseFlow(cancelSubscriptionResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void cancelSubscriptionResponseFlow(CancelSubscriptionResponse cancelSubscriptionResponse) {
        try {
            if (cancelSubscriptionResponse != null && cancelSubscriptionResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                cancelSubscriptionView.cancelSubscriptionResponse(cancelSubscriptionResponse);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
