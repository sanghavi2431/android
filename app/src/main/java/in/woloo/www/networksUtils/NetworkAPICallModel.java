package in.woloo.www.networksUtils;

import android.app.Activity;

import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;


public class NetworkAPICallModel {
   private String apiURL;
   private String app_type;
   private int request_type;
   private JSONObject jsonObjectRequest;
   private JetEncryptor jetEncryptor;
   private int timeOut= 30*1000;
   private Type parserType;
   private Object responseObject;
   private Object customObject;
   private IOnResponseBindCallBack onResponseBindCallBack;
   private boolean showProgress=true;
   private Activity activity;
   private int max_try=0;
   private int max_try_error=0;
   private JSONObject encryptedRequest=null;
   private boolean isProgressVisible=false;
   private CommonUtils commonUtils = new CommonUtils();

   public NetworkAPICallModel(String apiURL, int request_type, String app_type, JSONObject jsonObjectRequest, JetEncryptor jetEncryptor){
       this.apiURL=apiURL;
       this.request_type=request_type;
       this.app_type=app_type;
       this.jsonObjectRequest=jsonObjectRequest;
       this.jetEncryptor=jetEncryptor;
   }

    public CommonUtils getCommonUtils() {
        return commonUtils;
    }

    public void setCommonUtils(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    public boolean isProgressVisible() {
        return isProgressVisible;
    }

    public void setProgressVisible(boolean progressVisible) {
        isProgressVisible = progressVisible;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getApp_type() {
        return app_type;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public JSONObject getJsonObjectRequest() {
        return jsonObjectRequest;
    }

    public void setJsonObjectRequest(JSONObject jsonObjectRequest) {
        this.jsonObjectRequest = jsonObjectRequest;
    }

    public JetEncryptor getJetEncryptor() {
        return jetEncryptor;
    }

    public void setJetEncryptor(JetEncryptor jetEncryptor) {
        this.jetEncryptor = jetEncryptor;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Type getParserType() {
        return parserType;
    }

    public void setParserType(Type parserType) {
        this.parserType = parserType;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }

    public int getRequest_type() {
        return request_type;
    }

    public void setRequest_type(int request_type) {
        this.request_type = request_type;
    }

    public Object getCustomObject() {
        return customObject;
    }

    public void setCustomObject(Object customObject) {
        this.customObject = customObject;
    }

    public void setOnResponseBindCallBack(IOnResponseBindCallBack onResponseBindCallBack) {
        this.onResponseBindCallBack = onResponseBindCallBack;
    }

    public IOnResponseBindCallBack getOnResponseBindCallBack() {
        return onResponseBindCallBack;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public int getMax_try() {
        return max_try;
    }

    public void setMax_try(int max_try) {
        this.max_try = max_try;
    }

    public int getMax_try_error() {
        return max_try_error;
    }

    public void setMax_try_error(int max_try_error) {
        this.max_try_error = max_try_error;
    }

    public JSONObject getEncryptedRequest() {
        return encryptedRequest;
    }

    public void setEncryptedRequest(JSONObject encryptedRequest) {
        this.encryptedRequest = encryptedRequest;
    }
}
