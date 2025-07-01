package in.woloo.www.refer_woloo_host.mvp;

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

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.refer_woloo_host.model.ReferWolooResponse;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.woloo_host.model.AddWolooResponse;
import in.woloo.www.woloo_host.model.GeoCodeResponse;

public class ReferredWolooPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = ReferredWolooPresenter.class.getSimpleName();

    private Context context;
    private ReferredWolooView referredWolooView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;

    public ReferredWolooPresenter(Context context, ReferredWolooView referredWolooView){
        this.context = context;
        this.referredWolooView = referredWolooView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public void getProfile(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<ViewProfileResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.VIEW_PROFILE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
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
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void referredWoloo(String name, String city, String address,String number , String zipCode, JSONArray uploadedImages, double lat, double log){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.CITY,city);
                mJsObjParam.put(JSONTagConstant.ADDRESS,address);
                mJsObjParam.put(JSONTagConstant.IMAGE,uploadedImages);
                mJsObjParam.put(JSONTagConstant.RECOMMEND_NUMBER,number);
                mJsObjParam.put(JSONTagConstant.ZIPCODE,zipCode);
                mJsObjParam.put(JSONTagConstant.LATITUDE,lat);
                mJsObjParam.put(JSONTagConstant.LONGITUDE,log);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<ReferWolooResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.REFER_WOLOO, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void referredWolooList(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<ReferredWolooListResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.REFERRED_WOLOO_LIST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
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
            case APIConstants.REFERRED_WOLOO_LIST:
                try {
                    ReferredWolooListResponse referredWolooListResponse = (ReferredWolooListResponse) networkAPICallModel.getResponseObject();
                    if(referredWolooListResponse != null){
                        referredWolooFlow(referredWolooListResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
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
            case APIConstants.REFER_WOLOO:
                try{
                    ReferWolooResponse referWolooResponse = (ReferWolooResponse) networkAPICallModel.getResponseObject();
                    if(referWolooResponse != null){
                        ReferWolooHostResponseFlow(referWolooResponse);
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
            referredWolooView.geoCodeResponseSuccess(geoCodeResponse);
        }
        else
            CommonUtils.showCustomDialogBackClick(context,"Unable to find Location Service. Please start your location Service Or Reboot your device.");

    }

    private void ReferWolooHostResponseFlow(ReferWolooResponse referWolooResponse) {
        try{
            if(referWolooResponse != null && referWolooResponse.getData().getStatus().equals(AppConstants.API_SUCCESS)){
                referredWolooView.referWolooHostSuccess(referWolooResponse.getData().getMessage());
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void viewProfileResponseFlow(ViewProfileResponse viewProfileResponse) {
        try{
            if(viewProfileResponse != null && viewProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                referredWolooView.setProfileResponse(viewProfileResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void referredWolooFlow(ReferredWolooListResponse referredWolooListResponse) {
        try{
            if(referredWolooListResponse != null && referredWolooListResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                referredWolooView.referredWolooListResponse(referredWolooListResponse);
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
