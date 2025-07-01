package in.woloo.www.my_account.mvp;

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
import in.woloo.www.more.models.UserCoinHistoryModel;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;

public class MyAccountPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = MyAccountPresenter.class.getSimpleName();

    private Context mContext;
    private MyAccountView myAccountView;

    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public MyAccountPresenter(Context mContext, MyAccountView myAccountView) {
        this.mContext = mContext;
        this.myAccountView = myAccountView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void getUserCoins(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserCoinsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_COINS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }


    public void getCoinHistory(int mPageNumber){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.PAGE_NUMBER,mPageNumber);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<UserCoinHistoryModel>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.COINHISTORY, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(false);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.USER_COINS:
                try {
                    UserCoinsResponse userCoinsResponse = (UserCoinsResponse) networkAPICallModel.getResponseObject();
                    if(userCoinsResponse != null){
                        userCoinsResponseFlow(userCoinsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;

            case APIConstants.COINHISTORY:
                try {
                    UserCoinHistoryModel userCoinHistoryModel = (UserCoinHistoryModel) networkAPICallModel.getResponseObject();
                    if(userCoinHistoryModel != null){
                        userCoinsHistoryFlow(userCoinHistoryModel);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;

            default:
                break;
        }
    }

    private void userCoinsResponseFlow(UserCoinsResponse userCoinsResponse) {
        try{
            myAccountView.userCoinsSuccess(userCoinsResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void userCoinsHistoryFlow(UserCoinHistoryModel userCoinHistoryModel) {
        try{
            myAccountView.userCoinsHistorySuccess(userCoinHistoryModel);
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
