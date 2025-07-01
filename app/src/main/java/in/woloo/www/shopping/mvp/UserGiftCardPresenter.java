package in.woloo.www.shopping.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.giftcard.model.GiftCardModelResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.NetworkStatus;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.search.mvp.WolooSearchPresenter;
import in.woloo.www.search.mvp.WolooSearchView;
import in.woloo.www.shopping.model.UserGiftCardResponse;
import in.woloo.www.shopping.model.UserGiftCardUpdateResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.shopping.model.GeoCodeResponse;
import in.woloo.www.utils.Logger;

public class UserGiftCardPresenter implements NetworkAPIResponseCallback {
    private static final String TAG = WolooSearchPresenter.class.getSimpleName();

    private Context context;
    private UserGiftCardView userGiftCardView;
    private UserGiftCardUpdateView userGiftCardViewUpdate;
    private GeoCodeView geoCodeView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;

    public UserGiftCardPresenter(Context context,UserGiftCardView userGiftCardView ) {
        this.context = context;
        this.userGiftCardView = userGiftCardView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public UserGiftCardPresenter(Context context,UserGiftCardUpdateView userGiftCardViewUpdate ) {
        this.context = context;
        this.userGiftCardViewUpdate = userGiftCardViewUpdate;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public UserGiftCardPresenter(Context context,GeoCodeView geoCodeView ) {
        this.context = context;
        this.geoCodeView = geoCodeView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }


    public void getUserGiftCard(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserGiftCardResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_GIFT_CARD_DETAILS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }


    public void updateGiftCard(String type,  int points, String orderid){
        try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("type", type);
            mJsObjParam.put("coins", points);
            mJsObjParam.put("orderid", orderid);
            Type parserType = new TypeToken<UserGiftCardUpdateResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_GIFT_CARD_UPDATE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }


    public void updateGiftCardFail(int transaction_id){
        try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("transaction_id", transaction_id);

            Type parserType = new TypeToken<UserGiftCardUpdateResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_GIFT_CARD_FAIL, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }






    public void getReverseGeocoding(String lat, String lng){
        try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("lat", lat);
            mJsObjParam.put("lng", lng);
            mJsObjParam.put("dev", true);

            Type parserType = new TypeToken<GeoCodeResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GEO_CODE_LOCATION_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.USER_GIFT_CARD_DETAILS:
                try {
                    UserGiftCardResponse userGiftCardResponse = (UserGiftCardResponse) networkAPICallModel.getResponseObject();
                    if(userGiftCardResponse != null){
                        userGiftCardResponseFlow(userGiftCardResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;

            case APIConstants.USER_GIFT_CARD_UPDATE:
                try {
                    UserGiftCardUpdateResponse userGiftCardUpdateResponse = (UserGiftCardUpdateResponse) networkAPICallModel.getResponseObject();
                    if(userGiftCardUpdateResponse != null){
                        userGiftCardResponseFlowUpdate(userGiftCardUpdateResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;

            case APIConstants.USER_GIFT_CARD_FAIL:
                try {
                    UserGiftCardUpdateResponse userGiftCardUpdateResponse = (UserGiftCardUpdateResponse) networkAPICallModel.getResponseObject();
                    if(userGiftCardUpdateResponse != null){
                        userGiftCardResponseFlowUpdate(userGiftCardUpdateResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;



            case APIConstants.GEO_CODE_LOCATION_API:
                try {
                    GeoCodeResponse geoCodeResponse = (GeoCodeResponse) networkAPICallModel.getResponseObject();
                    if(geoCodeResponse != null){
                        geoCodeResponseFlow(geoCodeResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;



            default:
                break;
        }
    }


    private void userGiftCardResponseFlowUpdate(UserGiftCardUpdateResponse userGiftCardUpdateResponse) {
        try{
            if(userGiftCardUpdateResponse != null && userGiftCardUpdateResponse.isSuccess()){
                userGiftCardViewUpdate.setUserCardResponseUpdate(userGiftCardUpdateResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }

    }


    private void userGiftCardResponseFlow(UserGiftCardResponse userGiftCardResponse) {
        try{
            if(userGiftCardResponse != null && userGiftCardResponse.isSuccess()){
                userGiftCardView.setUserCardResponse(userGiftCardResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }

    }



    private void geoCodeResponseFlow(GeoCodeResponse geoCodeResponse) {
        try{
            if(geoCodeResponse != null){
                geoCodeView.setGeoCodeResponse(geoCodeResponse);
            }
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