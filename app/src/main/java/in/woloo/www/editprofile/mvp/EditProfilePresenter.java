package in.woloo.www.editprofile.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.netcore.android.Smartech;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.subscribe.adapter.SubscribeAdapter;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.util.NetcoreUserDetails;

public class EditProfilePresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private EditProfileView editProfileView;

    private NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public EditProfilePresenter(Context mContext, EditProfileView editProfileView) {
        this.mContext = mContext;
        this.editProfileView = editProfileView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void editProfile(Context context,JSONObject mJsObjParam){
        try{
            Type parserType = new TypeToken<EditProfileResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.EDIT_PROFILE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void initSubscription(int id, String planId, SubscribeAdapter.InitSubscriptionCallback initSubscriptionCallback){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.ID,id);
                mJsObjParam.put(JSONTagConstant.PLAN_ID,planId);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<InitSubscriptionResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.INIT_SUBSCRIPTION_ORDER, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setCustomObject(initSubscriptionCallback);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.EDIT_PROFILE:
                try {
                    EditProfileResponse editProfileResponse = (EditProfileResponse) networkAPICallModel.getResponseObject();
                    if(editProfileResponse != null){
                        //Saving User Profile  Data to Netcore
                        new NetcoreUserDetails(mContext).updateNetcoreUserProfile();

                        editProfileResponseFlow(editProfileResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.INIT_SUBSCRIPTION_ORDER:
                try {
                    SubscribeAdapter.InitSubscriptionCallback initSubscriptionCallback = (SubscribeAdapter.InitSubscriptionCallback) networkAPICallModel.getCustomObject();
                    InitSubscriptionResponse initSubscriptionResponse = (InitSubscriptionResponse) networkAPICallModel.getResponseObject();
                    if(initSubscriptionResponse != null){
                        initSubscriptionCallback.initSubscriptionSuccess(initSubscriptionResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void editProfileResponseFlow(EditProfileResponse editProfileResponse) {
        try{
            if(editProfileResponse != null && editProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                editProfileView.editProfileSuccess();
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
