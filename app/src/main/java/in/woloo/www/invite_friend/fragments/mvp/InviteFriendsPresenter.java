package in.woloo.www.invite_friend.fragments.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.invite_friend.fragments.models.InviteFriendsResponse;
import in.woloo.www.mapdirection.MapsFragment;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class InviteFriendsPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = InviteFriendsPresenter.class.getSimpleName();

    private Context mContext;
    private InviteFriendsView inviteFriendsView;

    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public InviteFriendsPresenter(Context mContext, InviteFriendsView inviteFriendsView) {
        this.mContext = mContext;
        this.inviteFriendsView = inviteFriendsView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }


    public void inviteContacts(String mobileNumber,String message,String shareUrl){
        try{
            try {
                JSONObject mJsObjParam = new JSONObject();
                try{
                    mJsObjParam.put(JSONTagConstant.INVITE_MOBILE_NUMBER,mobileNumber);
                    mJsObjParam.put(JSONTagConstant.MESSAGE,message);
                    mJsObjParam.put(JSONTagConstant.SHARE_URL,shareUrl);
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                Type parserType = new TypeToken<InviteFriendsResponse>() {
                }.getType();
                NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.INVITE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
                networkAPICallModel.setParserType(parserType);
                networkAPICallModel.setShowProgress(true);
                mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
            } catch (Exception e) {
                Logger.e(TAG, e.getMessage());
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
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


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.INVITE:
                try {
                    InviteFriendsResponse inviteFriendsResponse = (InviteFriendsResponse) networkAPICallModel.getResponseObject();
                    if(inviteFriendsResponse != null){
                         inviteFriendsResponseFlow(inviteFriendsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
             case APIConstants.VIEW_PROFILE:
                 try{
                     ViewProfileResponse viewProfileResponse = (ViewProfileResponse) networkAPICallModel.getResponseObject();
                     if(viewProfileResponse != null){
                         viewProfileResponseFlow(viewProfileResponse);
                     }
                 }catch (Exception ex){
                      CommonUtils.printStackTrace(ex);
                 }
                break;
            default:
                break;
        }
    }

    private void viewProfileResponseFlow(ViewProfileResponse viewProfileResponse) {
        try{
            if(viewProfileResponse != null && viewProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                 inviteFriendsView.showRefferalCode(viewProfileResponse.getUserData().getRefCode(),viewProfileResponse.getUserData().getExpiryDate());
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void inviteFriendsResponseFlow(InviteFriendsResponse inviteFriendsResponse) {
        try{
            if(inviteFriendsResponse != null && inviteFriendsResponse.getStatus().equalsIgnoreCase(AppConstants.API_SUCCESS)){
                inviteFriendsView.inviteFriendSuccess(inviteFriendsResponse.getMessage());
            }else{
                CommonUtils.showMaintenanceDialog(mContext,true);
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
