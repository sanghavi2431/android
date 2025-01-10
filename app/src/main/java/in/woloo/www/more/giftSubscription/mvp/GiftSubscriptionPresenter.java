package in.woloo.www.more.giftSubscription.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.application_kotlin.api_classes.APIConstants;
import in.woloo.www.application_kotlin.api_classes.JSONTagConstant;
import in.woloo.www.application_kotlin.api_classes.NetworkAPICallModel;
import in.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.giftSubscription.model.GetGiftPlansResponse;
import in.woloo.www.more.giftSubscription.model.SendGiftCardResponse;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class GiftSubscriptionPresenter implements NetworkAPIResponseCallback {
    private static final String TAG = GiftSubscriptionPresenter.class.getSimpleName();

    private Context context;
    private GiftSubscriptionView giftSubscriptionView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;

    public GiftSubscriptionPresenter(Context context, GiftSubscriptionView giftSubscriptionView){
        this.context = context;
        this.giftSubscriptionView = giftSubscriptionView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public void getGiftPlans(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<GetGiftPlansResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GET_GIFT_PLANS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void sendGiftCard(String sender,String message,String mobile){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.sender,sender);
                mJsObjParam.put(JSONTagConstant.message,message);
                mJsObjParam.put(JSONTagConstant.mobile,mobile);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<SendGiftCardResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SEND_GIFT_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.GET_GIFT_PLANS:
                try {
                    GetGiftPlansResponse getGiftPlansResponse = (GetGiftPlansResponse) networkAPICallModel.getResponseObject();
                    if(getGiftPlansResponse != null){
                        getGiftPlansResponseFlow(getGiftPlansResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.SEND_GIFT_SUBSCRIPTION:
                try {
                    SendGiftCardResponse sendGiftCardResponse = (SendGiftCardResponse) networkAPICallModel.getResponseObject();
                    if(sendGiftCardResponse != null){
                        sendGiftCardResponseFlow(sendGiftCardResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void sendGiftCardResponseFlow(SendGiftCardResponse sendGiftCardResponse) {
        giftSubscriptionView.sendGiftCardResponse(sendGiftCardResponse);
    }

    private void getGiftPlansResponseFlow(GetGiftPlansResponse getGiftPlansResponse) {
        giftSubscriptionView.getGiftPlansResponse(getGiftPlansResponse);
    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
