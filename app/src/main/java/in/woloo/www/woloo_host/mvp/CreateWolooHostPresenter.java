package in.woloo.www.woloo_host.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import in.woloo.www.utils.Logger;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.woloo_host.model.AddWolooResponse;
import in.woloo.www.woloo_host.model.GeoCodeResponse;

public class CreateWolooHostPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = CreateWolooHostPresenter.class.getSimpleName();

    private Context mContext;
    private CreateWolooHostView createWolooHostView;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public CreateWolooHostPresenter(Context mContext, CreateWolooHostView createWolooHostView) {
        this.mContext = mContext;
        this.createWolooHostView = createWolooHostView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void getProfile(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<ViewProfileResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.VIEW_PROFILE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getLocation(String lat,String lng){
        try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("lat",lat);
            mJsObjParam.put("lng",lng);
            Type parserType = new TypeToken<GeoCodeResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GEO_CODE_LOCATION_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void becomeWolooHost(String name, String city, String address, String zipCode, JSONArray uploadedImages, double lat, double log){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.CITY,city);
                mJsObjParam.put(JSONTagConstant.ADDRESS,address + " " + city + " " + zipCode + " India");
                mJsObjParam.put(JSONTagConstant.IMAGE,uploadedImages);
                mJsObjParam.put(JSONTagConstant.ZIPCODE,zipCode);
                mJsObjParam.put(JSONTagConstant.LATITUDE,lat);
                mJsObjParam.put(JSONTagConstant.LONGITUDE,log);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<AddWolooResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.ADD_WOLOO_HOST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.VIEW_PROFILE:
                try {
                    ViewProfileResponse viewProfileResponse = (ViewProfileResponse) networkAPICallModel.getResponseObject();
                    if(viewProfileResponse != null){
                        viewProfileResponseFlow(viewProfileResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.ADD_WOLOO_HOST:
                try{
                    AddWolooResponse addWolooResponse = (AddWolooResponse) networkAPICallModel.getResponseObject();
                    if(addWolooResponse != null){
                        addWolooHostResponseFlow(addWolooResponse);
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            case APIConstants.GEO_CODE_LOCATION_API:
                try{
                    GeoCodeResponse geoCodeResponse = (GeoCodeResponse) networkAPICallModel.getResponseObject();
                    if(geoCodeResponse != null){
                        geoCodeResponseFlow(geoCodeResponse);
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            default:
                break;
        }
    }

    private void geoCodeResponseFlow(GeoCodeResponse geoCodeResponse) {
        if(geoCodeResponse != null && geoCodeResponse.getStatus().equals(AppConstants.API_SUCCESS)){
            createWolooHostView.geoCodeResponseSuccess(geoCodeResponse);
        }
        else
            CommonUtils.showCustomDialogBackClick(mContext,"Unable to find Location Service. Please start your location Service Or Reboot your device.");

    }

    private void addWolooHostResponseFlow(AddWolooResponse addWolooResponse) {
        try{
            if(addWolooResponse != null && addWolooResponse.getData().getStatus().equals(AppConstants.API_SUCCESS)){
                createWolooHostView.addWolooHostSuccess(addWolooResponse.getData().getMessage());
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void viewProfileResponseFlow(ViewProfileResponse viewProfileResponse) {
        try{
            if(viewProfileResponse != null && viewProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                createWolooHostView.setProfileResponse(viewProfileResponse);
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
