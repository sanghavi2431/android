package in.woloo.www.giftcard.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import in.woloo.www.utils.Logger;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.giftcard.model.GiftCardModelResponse;
import in.woloo.www.giftcard.model.RequestPointsResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;

public class GiftCardPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private GiftCardView giftCardView;
    private CommonUtils mCommonUtil;

    public GiftCardPresenter(Context context,GiftCardView giftCardView) {
        this.mContext=context;
        this.giftCardView = giftCardView;
        mCommonUtil = new CommonUtils();
    }

    public void sendGiftCard(String amount, String mobile_number, String message){
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put(JSONTagConstant.AMOUNT, Integer.parseInt(amount));
            mJsObjParam.put(JSONTagConstant.MOBILE_NUMBER_, Long.parseLong(mobile_number));
            mJsObjParam.put(JSONTagConstant.GIFT_MESSAGE, message);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        Type parserType = new TypeToken<GiftCardModelResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.WOLOO_GIFT, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, JetEncryptor.getInstance());
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
    }

    public void RequestPoints(String amount, String mobile_number, String message) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put(JSONTagConstant.COINS, Integer.parseInt(amount));
            mJsObjParam.put(JSONTagConstant.MOBILE, Long.parseLong(mobile_number));
            mJsObjParam.put(JSONTagConstant.GIFT_MESSAGE, message);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        Type parserType = new TypeToken<RequestPointsResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.REQUEST_POINTS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, JetEncryptor.getInstance());
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
    }

    public void getUserCoins() {
        try {
            NetworkAPICall mNetworkAPICall = new NetworkAPICall();
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserCoinsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_COINS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,  JetEncryptor.getInstance());
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e("TAG", e.getMessage());
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.WOLOO_GIFT:
                try {
                    GiftCardModelResponse giftCardModelResponse = (GiftCardModelResponse) networkAPICallModel.getResponseObject();
                    if(giftCardModelResponse != null){
                        giftCardResponseFlow(giftCardModelResponse);
                    }
                } catch (Exception e) {
                    mCommonUtil.printStackTrace(e);
                }
                break;
            case APIConstants.USER_COINS:
                try {
                    UserCoinsResponse userCoinsResponse = (UserCoinsResponse) networkAPICallModel.getResponseObject();
                    if (userCoinsResponse != null) {
                        giftCardView.userCoinsResponseSuccess(userCoinsResponse);
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.REQUEST_POINTS:
                try {
                    RequestPointsResponse RequestPointsResponse = (RequestPointsResponse) networkAPICallModel.getResponseObject();
                    if (RequestPointsResponse != null) {
                        giftCardView.RequestPointsResponseSuccess(RequestPointsResponse);
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void giftCardResponseFlow(GiftCardModelResponse giftCardModelResponse) {
        try{
            giftCardView.showResult(giftCardModelResponse.getData().getMessage());
        }catch (Exception ex){
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
