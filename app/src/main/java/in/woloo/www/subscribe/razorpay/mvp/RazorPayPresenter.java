package in.woloo.www.subscribe.razorpay.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.subscribe.razorpay.model.RazorPayResponse;
import in.woloo.www.utils.AppConstants;

public class RazorPayPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private RazorPayView razorPayView;
    private  NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public RazorPayPresenter(Context mContext, RazorPayView razorPayView) {
        this.mContext = mContext;
        this.razorPayView = razorPayView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void submitSubscriptionPurchase(String planId, String paymentId, String paymentSignature, String subscriptionId, boolean isFutureSubscription) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.PLAN_ID, planId);
                mJsObjParam.put(JSONTagConstant.PAYMENT_ID, paymentId);
                mJsObjParam.put(JSONTagConstant.PAYMENT_SIGNATURE, paymentSignature);
                mJsObjParam.put(JSONTagConstant.SUBSCRIPTIONID, subscriptionId);
                if (isFutureSubscription)
                    mJsObjParam.put(JSONTagConstant.FUTURE_PLAN_ID, "1");
                else
                    mJsObjParam.put(JSONTagConstant.FUTURE_PLAN_ID, "0");

            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<RazorPayResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SUBMIT_SUBCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }



    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SUBMIT_SUBCRIPTION:
                try {
                    razorPayView.onPaymentSuccess();
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
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
