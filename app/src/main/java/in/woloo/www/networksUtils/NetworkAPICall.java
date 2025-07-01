package in.woloo.www.networksUtils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONArray;
import org.json.JSONObject;

import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.listners.DialogCallListener;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ToastUtils;


public class NetworkAPICall {
    private static final String TAG = NetworkAPICall.class.getSimpleName() + " http";
    private final int MAX_RETRY_LIMIT = 2;
    //private CommonUtils commonUtils = new CommonUtils();


    public void callApplicationWS(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {
        try {
            CommonUtils commonUtils = networkAPICallModel.getCommonUtils();
            NetworkUtils networkUtils = new NetworkUtils();
            //Netcore Disabled Log
            Logger.v(TAG, "callApplicationWS API Name:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :");
            if (networkUtils.isConnected(context)) {
                try {
                    if (networkAPICallModel.isShowProgress() && !networkAPICallModel.isProgressVisible()) {
                        commonUtils.showProgress(context);
                        networkAPICallModel.setProgressVisible(true);
                    }

                    networkAPICallModel.setApp_type(commonUtils.getMobileTvType(context));
                    RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
                    String userStatusUrl = BuildConfig.BASE_URL + networkAPICallModel.getApiURL();
                    JSONObject jsonObject = null;
                    if (networkAPICallModel.getApp_type() != null && networkAPICallModel.getApp_type().equalsIgnoreCase(commonUtils.APP_TYPE_TV)) {
                        jsonObject = networkAPICallModel.getJsonObjectRequest();
                        jsonObject.put(JSONTagConstant.LOCALE, commonUtils.getCustomLocale(context, commonUtils.APP_TYPE_TV));
                    } else {
                        jsonObject = networkAPICallModel.getJsonObjectRequest();
                        jsonObject.put(JSONTagConstant.LOCALE, commonUtils.getCustomLocale(context, commonUtils.APP_TYPE_MOBILE));
                    }
                    if (networkAPICallModel.getJetEncryptor() == null) {
                        networkAPICallModel.setJetEncryptor(JetEncryptor.getInstance());
                    }
                    JSONObject encryptedRequest = networkAPICallModel.getEncryptedRequest();
                    if (encryptedRequest == null && JetEncryptor.getInstance().isInilized()) {
                        encryptedRequest = commonUtils.getEncryptedJsonRequest(context, networkAPICallModel.getJsonObjectRequest(), networkAPICallModel.getJetEncryptor(), networkAPICallModel.getApp_type());
                        //                      Logger.i(TAG,"callApplicationWS Volley encryptedRequest ++++++++ : "+encryptedRequest.toString());
                        networkAPICallModel.setEncryptedRequest(encryptedRequest);
                    }
                    /*if (networkAPICallModel.getEncryptedRequest()==null && networkAPICallModel.getJetEncryptor()==null && !JetEncryptor.getInstance().isInilized() && networkAPICallModel.getMax_try()<MAX_RETRY_LIMIT){
                        JetEncryptorReInit jetEncryptorReInit=new JetEncryptorReInit(context);
                        networkAPICallModel.setMax_try(networkAPICallModel.getMax_try()+1);
                        jetEncryptorReInit.callApplicationWS(context,networkAPICallModel,networkAPIResponseCallback,NetworkAPICall.this);
                        Logger.e(TAG," JetEncryptorReInit:  ++++++++ : "+networkAPICallModel.getApiURL()+"   ++++++++ :"+networkAPICallModel.getMax_try());
                        return;
                    }*/
                    try {
                        if (encryptedRequest == null || encryptedRequest.length() == 0 || !JetEncryptor.getInstance().isInilized()) {
//                            Netcore Disabled Log
                            Logger.e(TAG, " encryptedRequest API Name:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :");
                            Logger.e(TAG, "\n  encryptedRequest jsonObject ++++++++ : " + jsonObject.toString());
                            Logger.e(TAG, " encryptedRequest==null || encryptedRequest.length()==0 " + encryptedRequest + " Encrypter status" + JetEncryptor.getInstance().isInilized());
                            commonUtils.hideProgress();
                            if (networkAPICallModel.getMax_try() < MAX_RETRY_LIMIT) {
                                JetEncryptorReInit jetEncryptorReInit = new JetEncryptorReInit(context);
                                networkAPICallModel.setMax_try(networkAPICallModel.getMax_try() + 1);
                                jetEncryptorReInit.callApplicationWS(context, networkAPICallModel, networkAPIResponseCallback, NetworkAPICall.this);
                                //Netcore Disabled Log
                                Logger.e(TAG, " JetEncryptorReInit:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :" + networkAPICallModel.getMax_try());
                            }
                            return;
                        }
            //Netcore Disabled Log
                        Logger.v(TAG, " API Name:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :");
                        Logger.v(TAG, "\n Volley jsonObject ++++++++ : " + jsonObject.toString());
                       Logger.v(TAG,"\nVolley encryptedRequest ++++++++ : "+encryptedRequest.toString());
                    } catch (Exception e) {
                        commonUtils.printStackTrace(e);
                    }
                    //Netcore Disabled Log
                    Logger.i("HttpURL", userStatusUrl);
                    Logger.i("Encyrpted data", encryptedRequest.toString());
                    CustomVolleyRequest stringRequest = new CustomVolleyRequest(networkAPICallModel.getRequest_type(), userStatusUrl, encryptedRequest,
                            (response) -> {
                                try {
                                    try {
                                        if (response.has("code") && response.getInt("code") == NetworkStatus.JET_ENCRYPTOR_ERROR) {
                                            if (networkAPICallModel.getMax_try() < MAX_RETRY_LIMIT) {
                                                JetEncryptorReInit jetEncryptorReInit = new JetEncryptorReInit(context);
                                                networkAPICallModel.setMax_try(networkAPICallModel.getMax_try() + 1);
                                                jetEncryptorReInit.callApplicationWS(context, networkAPICallModel, networkAPIResponseCallback, NetworkAPICall.this);
                                                //Netcore Disabled Log
                                                Logger.e(TAG, networkAPICallModel.getApiURL() + "\n NetworkStatus.JET_ENCRYPTOR_ERROR Volley UserCoinHistoryModel ++++++++ : \n" + response.toString());
                                                return;
                                            }
                                        }
                                        //Netcore Disabled Log
                                        Logger.v(TAG, networkAPICallModel.getApiURL() + "\n networkAPICallModel.getApiURL()Volley UserCoinHistoryModel ++++++++ : \n" + response.toString());
                                    } catch (Exception e) {
                                        commonUtils.printStackTrace(e);
                                    }
                                    commonUtils.hideProgress();
                                    if (networkAPICallModel.getParserType() != null) {
                                        Object object = new Gson().fromJson(response.toString(), networkAPICallModel.getParserType());//Todo
                                        networkAPICallModel.setResponseObject(object);
                                    }
                                    if (networkAPIResponseCallback != null)
                                        networkAPIResponseCallback.onSuccessResponse(response, networkAPICallModel);
                                } catch (Exception e) {
                                    if (networkAPIResponseCallback != null)
                                        networkAPIResponseCallback.onFailure(new VolleyError(e), networkAPICallModel);
                                    commonUtils.printStackTrace(e);
                                    commonUtils.hideProgress();
                                }
                            }, error -> {
                        try {
                            commonUtils.hideProgress();
                            if (error != null && error.getClass().equals(TimeoutError.class)) {
                                if (context != null) {
                                    if (networkAPICallModel.getApp_type() != null && networkAPICallModel.getApp_type().equalsIgnoreCase(commonUtils.APP_TYPE_MOBILE)) {
                                        networkUtils.noInternetConnOrServerErrorDialog(context, context.getString(R.string.slow_internet_connection_), "", R.drawable.ic_slow_internet, null);
                                    } else {
                                        ToastUtils.onShowToast(context, R.drawable.ic_slow_internet, context.getString(R.string.error), context.getString(R.string.slow_internet_connection_), false);
                                    }
                                }

                            } else {
                                try {
                                    //Netcore Disabled Log
//                                    Logger.v(TAG, " API ERROR Name:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :");
//                                    if (error != null && error.getLocalizedMessage() != null && !TextUtils.isEmpty(error.getLocalizedMessage())) {
//                                        Logger.v(TAG, " ERROR Name 1:  ++++++++ : " + error.getMessage() + "   ++++++++ :");
//                                        Logger.v(TAG, " ERROR Name 2:  ++++++++ : " + error.getLocalizedMessage() + "   ++++++++ :");
//                                    }
//                                    if (error != null && error.networkResponse != null && error.networkResponse.allHeaders != null) {
//                                        Logger.v(TAG, " ERROR Name 3:  ++++++++ : " + error.networkResponse.allHeaders + "   ++++++++ :");
//                                    }
//                                    if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
//                                        Logger.v(TAG, " ERROR Name 4:  ++++++++ : " + error.networkResponse.data.length + "   ++++++++ :");
//                                        Logger.v(TAG, " ERROR Name 5:  ++++++++ : " + error.getStackTrace().toString() + "   ++++++++ :");
//                                    }

                                } catch (Exception e) {
                                    commonUtils.printStackTrace(e);
                                }
                                if (networkAPICallModel.getMax_try_error() < MAX_RETRY_LIMIT) {
                                   /* networkAPICallModel.setMax_try_error(networkAPICallModel.getMax_try_error()+1);
                                    Logger.v(TAG,"API ERROR RETRY:  ++++++++ : "+networkAPICallModel.getApiURL()+"   ++++++++ :");
                                  callApplicationWS(context,networkAPICallModel,networkAPIResponseCallback);*/
                                } else if (networkAPIResponseCallback != null) {
                                    networkAPIResponseCallback.onFailure(error, networkAPICallModel);
                                }

                                try {
                                    if (context != null) {
                                        Activity activity = (Activity) context;
                                        if (activity != null) {
                                            StringBuffer stringBuffer = new StringBuffer("User Code: " + commonUtils.getUserCode(context) + "\nMessage: " + error.getMessage());
                                            StringBuffer stringBuffer1 = new StringBuffer("\nEncrypt Payload: " + networkAPICallModel.getEncryptedRequest());
                                            stringBuffer1.append("\nPayload:" + networkAPICallModel.getJsonObjectRequest());
                                            if (error != null) {
                                                stringBuffer.append("\nLocal Message: " + error.getLocalizedMessage());
                                            }
                                            if (error.networkResponse != null) {
                                                stringBuffer.append("\nAll Headers: " + error.networkResponse.allHeaders);
                                            }
                                            if (error.networkResponse != null) {
                                                stringBuffer.append("\nHeaders: " + error.networkResponse.headers);
                                            }
                                            stringBuffer.append("\nDevice Information: " + commonUtils.getDeviceAllInformation(context));
                                            commonUtils.sendAPIErrorLogToServer(context, activity.getLocalClassName(),
                                                    userStatusUrl, stringBuffer1.toString(), "" + stringBuffer.toString());
                                        }
                                    }
                                } catch (Exception e) {
                                    commonUtils.printStackTrace(e);
                                    commonUtils.hideProgress();
                                }
                            }
                        } catch (Exception e) {
                            commonUtils.printStackTrace(e);
                            commonUtils.hideProgress();
                        }
                    }, context, networkAPICallModel.getApp_type());
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            networkAPICallModel.getTimeOut(),
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    stringRequest.setShouldCache(false);
                    queue.add(stringRequest);
                } catch (Exception e) {
                    commonUtils.printStackTrace(e);
                    commonUtils.hideProgress();
                }

            } else {
                if (networkAPICallModel.getApp_type() != null && networkAPICallModel.getApp_type().equalsIgnoreCase(commonUtils.APP_TYPE_MOBILE)) {
                    networkUtils.noInternetConnOrServerErrorDialog(context, context.getString(R.string.no_internet_connection_),
                            context.getString(R.string.tap_to_retry), R.drawable.ic_no_internet, new DialogCallListener() {
                                @Override
                                public void positiveButtonClick() {
                                    callApplicationWS(context, networkAPICallModel, networkAPIResponseCallback);
                                }

                                @Override
                                public void negativeButtonClick() {

                                }
                            });
                } else {
                    ToastUtils.onShowToast(context, R.drawable.ic_no_internet, context.getString(R.string.error), context.getString(R.string.no_internet_connection_), false);
                }
            }

        } catch (Exception e) {
            networkAPICallModel.getCommonUtils().printStackTrace(e);
            networkAPICallModel.getCommonUtils().hideProgress();
        }
    }
}
