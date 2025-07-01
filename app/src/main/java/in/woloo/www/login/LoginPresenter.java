package in.woloo.www.login;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.login.models.OTPResponse;
import in.woloo.www.login.models.UpdateTokenResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.NetworkStatus;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.splash.PendingReviewStatusResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class LoginPresenter implements NetworkAPIResponseCallback {

    private static String TAG = LoginPresenter.class.getSimpleName();

    private Context context;
    private LoginView loginView;

    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    private CommonUtils mCommonUtils;

    public LoginPresenter(Context context, LoginView loginView) {
        this.context = context;
        this.loginView = loginView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    /*calling loginAPI*/
    public void loginAPI(String enteredText,String password) {
        Logger.i(TAG, "loginAPI");
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                if(!TextUtils.isEmpty(enteredText) && isValidEmail(enteredText.trim())){
                    mJsObjParam.put(JSONTagConstant.EMAIL,enteredText);
                }else if(!TextUtils.isEmpty(enteredText) && isValidMobile(enteredText)) {
                    mJsObjParam.put(JSONTagConstant.MOBILE_NUMBER,enteredText);
                }
                mJsObjParam.put(JSONTagConstant.PASSWORD,password);

            } catch (Exception e) {
                Logger.e(TAG, "getUserProfileAPI: "+e.getMessage());
            }
            Type parserType = new TypeToken<LoginResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.LOGIN_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);

        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }
/*calling getAuthConfig*/
    public void getAuthConfig() {
        Logger.i(TAG, "getAuthConfig");
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.PACKAGE_NAME,context.getApplicationContext().getPackageName());
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<AuthConfigResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.AUTH_CONFIG, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*callling updateDeviceTokenAPI*/
    public void updateDeviceTokenAPI(String deviceSerial,String token) {
        Logger.i(TAG, "updateDeviceTokenAPI");
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.DEVICE_SERIAL,deviceSerial);
                mJsObjParam.put(JSONTagConstant.DEVICE_TOKEN,token);
            } catch (Exception e) {
                Logger.e(TAG, "getUserProfileAPI: "+e.getMessage());
            }
            Type parserType = new TypeToken<UpdateTokenResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.UPDATE_DEVICE_TOKEN, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }


    /*callling sendOtpAPI*/
    public void sendOtpAPI(Context mContext,String enteredText,String refferalCode,String hashKey) {
        Logger.i(TAG, "sendOtpAPI");
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                if(!TextUtils.isEmpty(enteredText) && isValidEmail(enteredText)){
                    mJsObjParam.put(JSONTagConstant.EMAIL,enteredText);
                }else if(!TextUtils.isEmpty(enteredText) && isValidMobile(enteredText)) {
                    mJsObjParam.put(JSONTagConstant.MOBILE_NUMBER,enteredText);
                }
                if(!TextUtils.isEmpty(refferalCode)){
                    mJsObjParam.put(JSONTagConstant.REFFERAL_CODE,refferalCode);
                }
                mJsObjParam.put(JSONTagConstant.HASH_KEY,hashKey);
            } catch (Exception e) {
                Logger.e(TAG, "getSendOtpAPI: "+e.getMessage());
            }
            Type parserType = new TypeToken<OTPResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SEND_OTP_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setCustomObject(enteredText);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }
    /*callling getReviewPendingStatus*/
    public void getReviewPendingStatus() {
        Logger.i(TAG, "getReviewPendingStatus");
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<PendingReviewStatusResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.PENDING_REVIEW_STATUS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*callling isValidMobile*/
    private boolean isValidMobile(String phone) {
        Logger.i(TAG, "isValidMobile");
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    /*callling isValidEmail*/
    public static boolean isValidEmail(CharSequence target) {
        Logger.i(TAG, "isValidEmail");
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*callling onSuccessResponse*/
    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SEND_OTP_API:
                Logger.i(TAG, "SEND_OTP_API");
                try {
                    OTPResponse otpResponse = (OTPResponse) networkAPICallModel.getResponseObject();
                    if(otpResponse.getCode() ==  NetworkStatus.SUCCESS){
                        String enteredText = (String) networkAPICallModel.getCustomObject();
                        otpResponseFlow(otpResponse,enteredText);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.LOGIN_API:
                Logger.i(TAG, "LOGIN_API");
                try {
                    LoginResponse loginResponse = (LoginResponse) networkAPICallModel.getResponseObject();
                    if(loginResponse != null){
                        loginResponseFlow(loginResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.UPDATE_DEVICE_TOKEN:
                Logger.i(TAG, "UPDATE_DEVICE_TOKEN");
                try{
                    UpdateTokenResponse updateTokenResponse = (UpdateTokenResponse) networkAPICallModel.getResponseObject();
                    loginView.tokenUpdateSuccess(updateTokenResponse);
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            case APIConstants.AUTH_CONFIG:
                Logger.i(TAG, "AUTH_CONFIG");
                try {
                    AuthConfigResponse authConfigResponse = (AuthConfigResponse) networkAPICallModel.getResponseObject();
                    if(authConfigResponse != null){
                        loginView.authConfigSuccess(authConfigResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.PENDING_REVIEW_STATUS:
                Logger.i(TAG, "PENDING_REVIEW_STATUS");
                try{
                    PendingReviewStatusResponse pendingReviewStatusResponse = (PendingReviewStatusResponse) networkAPICallModel.getResponseObject();
                    if(pendingReviewStatusResponse != null){
                        loginView.pendingReviewStatusResponse(pendingReviewStatusResponse);
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            default:
            break;
        }
    }

    /*callling loginResponseFlow*/
    private void loginResponseFlow(LoginResponse loginResponse) {
        Logger.i(TAG, "loginResponseFlow");
        try{
            loginView.loginSuccessFlow(loginResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*callling otpResponseFlow*/
    private void otpResponseFlow(OTPResponse otpResponse, String enteredText) {
        Logger.i(TAG, "otpResponseFlow");
        try{
            loginView.sendOtpSuccessFlow(otpResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {
        try{
            if(networkAPICallModel.getApiURL().equals(APIConstants.LOGIN_API)){
                //Logger.i(TAG, new Gson().toJson(networkAPICallModel.getResponseObject()));
                loginView.onInvalidOTP();
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
