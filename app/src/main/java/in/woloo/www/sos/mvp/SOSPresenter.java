package in.woloo.www.sos.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.sos.models.SOSCreateResponse;
import in.woloo.www.sos.models.SOSDeleteResponse;
import in.woloo.www.sos.models.SOSEditResponse;
import in.woloo.www.sos.models.SOSListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class SOSPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private SOSViews sosViews;
    private NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public SOSPresenter(Context mContext, SOSViews sosViews) {
        this.mContext = mContext;
        this.sosViews = sosViews;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void createSOS(Context context,String mobile,String name){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.MOBILE,mobile);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<SOSCreateResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SOS_CREATE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void sosDetails(Context context,String mobile,String name){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.MOBILE,mobile);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<SOSCreateResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SOS_DETAIL, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void editSOS(Context context,String sosId,String mobile,String name){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.SOS_ID,sosId);
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.MOBILE,mobile);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<SOSEditResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SOS_EDIT, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void deleteSOS(Context context,String sosId){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.SOS_ID,sosId);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<SOSDeleteResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SOS_DELETE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    public void sosList(Context context){
        try{
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<SOSListResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SOS_LIST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SOS_CREATE:
                try {

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
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SOS_CREATE:
                try {
                    Logger.i("TAG", "onFailureResponse: ");
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
