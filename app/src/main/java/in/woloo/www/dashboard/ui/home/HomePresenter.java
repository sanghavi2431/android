package in.woloo.www.dashboard.ui.home;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.NetworkStatus;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;

public class HomePresenter implements NetworkAPIResponseCallback {
    private Context mContext;
    private HomeViewPresenterInterface homeViewPresenterInterface;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;

    public HomePresenter(Context mContext,HomeViewPresenterInterface homeViewPresenterInterface){
        this.mContext=mContext;
        this.homeViewPresenterInterface=homeViewPresenterInterface;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
    }

    public void getNearByStore(double lat, double lng, String page, String travelMode, String range, boolean showLoader){
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put(JSONTagConstant.LATITUDE,lat);
            mJsObjParam.put(JSONTagConstant.LONGITUDE,lng);
            mJsObjParam.put(JSONTagConstant.PAGE_NUMBER_NEAR_WOLOO,page);
            mJsObjParam.put(JSONTagConstant.KM_RANGE,"6");
            mJsObjParam.put(JSONTagConstant.TRANSPORT_MODE,travelMode);
            mJsObjParam.put(JSONTagConstant.IS_SEARCH,"0");
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        Type parserType = new TypeToken<NearByStoreResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.NEAR_BY_STORE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, JetEncryptor.getInstance());
        networkAPICallModel.setShowProgress(showLoader);
        networkAPICallModel.setParserType(parserType);
        int timeout = 120 * 1000;
        networkAPICallModel.setTimeOut(timeout);
        mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
    }

    public void getAuthConfig() {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.PACKAGE_NAME,mContext.getApplicationContext().getPackageName());
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<AuthConfigResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.AUTH_CONFIG, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }
    private void authConfigResponseFlow(AuthConfigResponse authConfigResponse) {
        try{
            if(authConfigResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                homeViewPresenterInterface.authConfigSuccess(authConfigResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()){
            case APIConstants.NEAR_BY_STORE:
                NearByStoreResponse nearByStoreResponse=(NearByStoreResponse)networkAPICallModel.getResponseObject();
                if (nearByStoreResponse.getStatus().equalsIgnoreCase(NetworkStatus.SUCCESS_STR)){
                    homeViewPresenterInterface.onGetNearByStore(nearByStoreResponse,networkAPICallModel);
                }
                break;
            case APIConstants.AUTH_CONFIG:
                try {
                    AuthConfigResponse authConfigResponse = (AuthConfigResponse) networkAPICallModel.getResponseObject();
                    if(authConfigResponse != null){
                        authConfigResponseFlow(authConfigResponse);
                    }
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
