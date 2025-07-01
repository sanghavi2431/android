package in.woloo.www.splash;

import android.app.Activity;
import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jet.jetconnectiontester.ConnectionChangeListner;
import com.jet.jetconnectiontester.ConnectionQuality;
import com.jet.jetconnectiontester.ConnectionType;
import com.jet.jetconnectiontester.JetConnectionClassManager;
import com.jet.jetconnectiontester.JetConnectionListner;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.jetsynthesys.encryptor.JobListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.listners.DialogCallListener;
import in.woloo.www.models.SafetyNetResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.CustomVolleyRequest;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.NetworkUtils;
import in.woloo.www.networksUtils.VolleySingleton;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ToastUtils;


public class SplashPresenter implements NetworkAPIResponseCallback {
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private JobListener jobListener;
    private JetConnectionClassManager jetConnectionClassManager;
    private Context context;
    private CommonUtils commonUtils=new CommonUtils();
    private RequestQueue queue;
    private NetworkUtils networkUtils;
    private SharedPreference mSharedPreference;
    public NetworkAPICall mNetworkAPICall;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    protected Gson gson;
    private SplashToRedirectionInterface splashToRedirectionInterface;

    public SplashPresenter(Context context, RequestQueue queue, NetworkUtils networkUtil, SharedPreference mSharedPreference, SplashToRedirectionInterface splashToRedirectionInterface){
        this.context=context;
        this.queue=queue;
        this.networkUtils=networkUtil;
        this.mSharedPreference=mSharedPreference;
        gson=new Gson();
        this.splashToRedirectionInterface=splashToRedirectionInterface;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }



    public void setDefaultInit() {
        try {
            jobListener = new JobListener() {
                @Override
                public void workStarted(int i) {

                }

                @Override
                public void workFinished(int i) {
                    VolleySingleton.getInstance(context.getApplicationContext()).setAfterJetEncryptorInitilization(context.getApplicationContext());
                    queue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
                    splashToRedirectionInterface.onRedirection();
//                    checkSafetyNet();
                }

                @Override
                public void workResult(String s) {

                }

                @Override
                public void onworkError(String s)
                {
                    Logger.e(TAG+ " JetEncryptor onworkError",s);
                }
            };
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }



    //SafetyNet Api
    public void safetyNetCheck(String JWS, Context mContext) {
        if (networkUtils.isConnected(context)) {
            try {
                String safetyNetUrl = "";
                final JSONObject postParamsObject = new JSONObject();

                try {
                    safetyNetUrl = BuildConfig.SAFETY_URL;
                } catch (Exception e) {
                    new CommonUtils().printStackTrace(e);
                }
                RequestQueue queue = VolleySingleton.getInstance(mContext).getRequestQueue();

                CustomVolleyRequest requestSafetyNet = new CustomVolleyRequest(
                        Request.Method.POST, safetyNetUrl, postParamsObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Logger.d(TAG, "safetyNetCheck "+response);
                                    if (response != null) {

                                        Gson gson = new Gson();
                                        SafetyNetResponse safetyNetResponse = gson.fromJson(response.toString(), SafetyNetResponse.class);
                                        if (safetyNetResponse.isCtsProfileMatch()) {
                                            mSharedPreference.setStoredBooleanPreference(context, SharedPreferencesEnum.GOOGLE_VERIFICATION_DEVICE_STATUS.getPreferenceKey(),true);
                                            splashToRedirectionInterface.onRedirection();
                                        } else {
                                            mSharedPreference.setStoredBooleanPreference(context, SharedPreferencesEnum.GOOGLE_VERIFICATION_DEVICE_STATUS.getPreferenceKey(),false);
                                            showNotAuthorizedDialog();
                                        }

                                    } else {
                                        showNotAuthorizedDialog();
                                    }
                                } catch (Exception e) {
//                                  CommonUtils.printStackTrace(e);
                                    showNotAuthorizedDialog();
                                    new CommonUtils().printStackTrace(e);
                                }
                            }
                        },
                        error -> {
                            Logger.d(TAG, "safetyNetCheck "+error.getLocalizedMessage());
                            CommonUtils.printStackTrace(error);
                            showNotAuthorizedDialog();
                        }, mContext, commonUtils.APP_TYPE_MOBILE) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();

                        try {
                            headers.put("Content-Type", "application/json");
                            headers.put("Accept", "application/json");
                            headers.put("JWS", JWS);
                            headers.put("product", BuildConfig.FLAVOR);
                        } catch (Exception e) {
                            new CommonUtils().printStackTrace(e);
                        }

                        return headers;
                    }
                };

                requestSafetyNet.setShouldCache(false);
                requestSafetyNet.setRetryPolicy(new DefaultRetryPolicy(
                        AppConstants.TIME_OUT_EXCEPTION_TIME*2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestSafetyNet.setShouldCache(false);
                queue.add(requestSafetyNet);

            } catch (Exception e) {
                new CommonUtils().printStackTrace(e);
//              CommonUtils.printStackTrace(e)
            }
        }
        else {
            networkUtils.noInternetConnOrServerErrorDialog((Activity)context, context.getString(R.string.no_internet_connection_),
                    context.getString(R.string.tap_to_retry), R.drawable.ic_no_internet, new DialogCallListener() {
                        @Override
                        public void positiveButtonClick() {
                            safetyNetCheck(JWS, mContext);
                        }

                        @Override
                        public void negativeButtonClick() {

                        }
                    });
        }

    }

    private void showNotAuthorizedDialog() {
        commonUtils.showNotAuthorizedDialog((Activity)context);
    }

    public void initJetEncryptor() {
        try {
            if (!JetEncryptor.getInstance().isInilized()){
                if (networkUtils.isConnected(context)) {
                    String device_id = commonUtils.getDeviceId(context);
                    if (BuildConfig.LIVE_URL.equalsIgnoreCase("3")) {
                        JetEncryptor.getInstance().initInBackground(context, jobListener, true, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID, device_id);
                    } else if (BuildConfig.LIVE_URL.equalsIgnoreCase("4")) {
                        JetEncryptor.getInstance().initInBackground(context, jobListener, true, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID + "_pp", device_id);
                    } else {
                        JetEncryptor.getInstance().initInBackground(context, jobListener, false, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID, device_id);
                    }
                } else {
                    networkUtils.noInternetConnOrServerErrorDialog((Activity)context, context.getString(R.string.no_internet_connection_),
                            context.getString(R.string.tap_to_retry), R.drawable.ic_no_internet, new DialogCallListener() {
                                @Override
                                public void positiveButtonClick() {
                                    initJetEncryptor();
                                }

                                @Override
                                public void negativeButtonClick() {

                                }
                            });
                }
            }else {
                splashToRedirectionInterface.onRedirection();
            }


        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }

    public void setConnectionListener(){
        try {
            jetConnectionClassManager=new JetConnectionClassManager(context, new JetConnectionListner() {
                @Override
                public void getErrorMsg(String s, ConnectionQuality connectionQuality) {

                }

                @Override
                public void getCurrentBandWidth(ConnectionQuality connectionQuality) {

                }

                @Override
                public void getCurrentBandWidth(ConnectionQuality connectionQuality, double v) {

                }

                @Override
                public void getCurrentBandWidth(ConnectionQuality connectionQuality, double bandWidth, long responseTime) {

                    String bandWidthType = "UNKNOWN";

                    switch (connectionQuality) {
                        case POOR:
                            Logger.e(TAG, "Connection Quality POOR: ");
                            ToastUtils.showSlowInternetToast(context,context.getString(R.string.slow_internet_connection_header),true);
                            bandWidthType = "POOR";
                            break;

                        case MODERATE:
                            Logger.e(TAG, "MODERATE: ");
                            ToastUtils.showSlowInternetToast(context,context.getString(R.string.moderate_bandwidth_msg),true);
                            bandWidthType = "MODERATE";
                            break;

                        case GOOD:
                            Logger.e(TAG, "GOOD: ");
                            bandWidthType = "GOOD";
                            break;

                        case EXCELLENT:
                            Logger.e(TAG, "EXCELLENT: ");
                            bandWidthType = "EXCELLENT";
                            break;

                        case UNKNOWN:
                            Logger.e(TAG, "UNKNOWN: " + System.currentTimeMillis());
                            bandWidthType = "UNKNOWN";
                            break;

                        case INTERNET_NOT_AVAILABLE:
                            Logger.e(TAG, "INTERNET_NOT_AVAILABLE");
                            bandWidthType = "INTERNET_NOT_AVAILABLE";
                            break;
                    }
                }
            }, new ConnectionChangeListner() {
                @Override
                public void connectionChangeType(boolean isConnected, ConnectionType connectionType) {
                    switch (connectionType){
                        case TYPE_MOBILE:
                            Logger.e(TAG, "Network Type: TYPE_MOBILE Connected: "+isConnected);
                            break;

                        case TYPE_WIFI:
                            Logger.e(TAG, "Network Type: TYPE_WIFI: "+isConnected);
                            break;

                        case TYPE_NO_NETWORK:
                            Logger.e(TAG, "Network Type: TYPE_NO_NETWORK: "+isConnected);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }
    public void registerConnectionListner(){
        try {
            jetConnectionClassManager.registerListner();
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }
    public void removeConnectionListner(){
        try {
            if (jetConnectionClassManager!=null)
                jetConnectionClassManager.removeListner();
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.AUTH_CONFIG:
                try {
                    AuthConfigResponse authConfigResponse = (AuthConfigResponse) networkAPICallModel.getResponseObject();
                    if(authConfigResponse != null){
                        //authConfigResponseFlow(authConfigResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.PENDING_REVIEW_STATUS:
                try {
                    PendingReviewStatusResponse pendingReviewStatusResponse = (PendingReviewStatusResponse) networkAPICallModel.getResponseObject();
                    if(pendingReviewStatusResponse != null){
                        pendingReviewStautsResponseFlow(pendingReviewStatusResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void pendingReviewStautsResponseFlow(PendingReviewStatusResponse pendingReviewStatusResponse) {
        try{
            splashToRedirectionInterface.pendingReviewStatusResponse(pendingReviewStatusResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

//    private void authConfigResponseFlow(AuthConfigResponse authConfigResponse) {
//        try{
//            if(authConfigResponse.getStatus().equals(AppConstants.API_SUCCESS)){
//                splashToRedirectionInterface.authConfigSuccess(authConfigResponse);
//
//            }
//        }catch (Exception ex){
//             CommonUtils.printStackTrace(ex);
//        }
//    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    public void getAuthConfig() {
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

    public void getAuthConfigNode(){

    }

    public void getReviewPendingStatus() {
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

    public void getAuthConfig(NetworkAPIResponseCallback networkAPIResponseCallback) {
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
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, networkAPIResponseCallback);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

}