package in.woloo.www.more.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.netcore.android.Smartech;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.more.models.SubscriptionStatusResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.models.VoucherDetailsResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.util.NetcoreUserDetails;

public class MorePresenter implements NetworkAPIResponseCallback {

    private static final String TAG = MorePresenter.class.getSimpleName();

    private final Context mContext;
    private final MoreView moreView;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public MorePresenter(Context context, MoreView moreView) {
        this.mContext = context;
        this.moreView = moreView;
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

    public void getSubscriptionDetails(){
        try{
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<SubscriptionStatusResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SUBSCRIPTION_STATUS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
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

    public void getUserProfileAll(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserProfileMergedResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_PROFILE_MERGED, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getVoucherDetails(String voucher_code){
        try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put(JSONTagConstant.VOUCHER_CODE,voucher_code);
            Type parserType = new TypeToken<VoucherDetailsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.VOUCHER_CODE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
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

    public void getUserOffers(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserCoinsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_OFFER_LIST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void UploadFile(String fileName){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.TYPE,AppConstants.USER_PROFILE);
                mJsObjParam.put(JSONTagConstant.FILE_NAMES,fileName);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<UserCoinsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.FILE_UPLOAD, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
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
            case APIConstants.SUBSCRIPTION_STATUS:
                try {
                    SubscriptionStatusResponse subscriptionStatusResponse = (SubscriptionStatusResponse) networkAPICallModel.getResponseObject();
                    if(subscriptionStatusResponse != null){
                        subscriptionStatusResponseFlow(subscriptionStatusResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.USER_PROFILE_MERGED:
                try {
                    UserProfileMergedResponse userProfileMergedResponse = (UserProfileMergedResponse) networkAPICallModel.getResponseObject();
                    if(userProfileMergedResponse != null){
                        userProfileMergedResponseResponseFlow(userProfileMergedResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.VOUCHER_CODE:
                try {
                    VoucherDetailsResponse voucherDetailsResponse = (VoucherDetailsResponse) networkAPICallModel.getResponseObject();
                    if(voucherDetailsResponse != null){
                        voucherDetailsResponseResponseFlow(voucherDetailsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void voucherDetailsResponseResponseFlow(VoucherDetailsResponse voucherDetailsResponse) {
        try{
            if(voucherDetailsResponse != null && voucherDetailsResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                moreView.setVoucherResponse(voucherDetailsResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void subscriptionStatusResponseFlow(SubscriptionStatusResponse subscriptionStatusResponse) {
        try{
            if(subscriptionStatusResponse != null && subscriptionStatusResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                moreView.setSubscriptionResponse(subscriptionStatusResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void userProfileMergedResponseResponseFlow(UserProfileMergedResponse userProfileMergedResponse) {
        try{
            if(userProfileMergedResponse != null && userProfileMergedResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                moreView.setUserProfileMergedResponse(userProfileMergedResponse);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void editProfileResponseFlow(EditProfileResponse editProfileResponse) {
        try{
             moreView.editProfileSuccess();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void userCoinsResponseFlow(UserCoinsResponse userCoinsResponse) {
        try{
            moreView.userCoinsResponseSuccess(userCoinsResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void viewProfileResponseFlow(ViewProfileResponse viewProfileResponse) {
        try{
            if(viewProfileResponse != null && viewProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                moreView.setProfileResponse(viewProfileResponse);
                // TODO Save updated user info?
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
