package in.woloo.www.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.woloo.www.BuildConfig;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.FileUploadResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.ImageMultipartRequest;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.VolleySingleton;


public class ProfileAPIUtil implements NetworkAPIResponseCallback {

    private CommonUtils commonUtils=new CommonUtils();
    private NetworkAPICall mNetworkAPICall = new NetworkAPICall();
    private NetworkAPIResponseCallback networkAPIResponseCallback;
    private Context context;

    public ProfileAPIUtil(Context context, NetworkAPIResponseCallback networkAPIResponseCallback){
        this.context=context;
        this.networkAPIResponseCallback=networkAPIResponseCallback;
    }


    public void updateUserProfile(Activity activity,Bitmap bitmap, JetEncryptor mJetEncryptor,IOnCallWSCallBack iOnCallWSCallBack, String type) {
        try {
            commonUtils.showProgress(context);
            RequestQueue queue = VolleySingleton.getInstance(activity.getApplicationContext()).getRequestQueue();
            String updateUserUrl = BuildConfig.BASE_URL + APIConstants.FILE_UPLOAD;
            ImageMultipartRequest multipartRequest = new ImageMultipartRequest(Request.Method.POST,
                    updateUserUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String jsonString = null;
                    try {
                        jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        try {
                            jsonString = new String(jsonString.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                              CommonUtils.printStackTrace(e);
                        }
                        FileUploadResponse fileUploadResponse = new Gson().fromJson(jsonString, FileUploadResponse.class);
                        commonUtils.hideProgress();
                        iOnCallWSCallBack.onSuccessResponse(fileUploadResponse);

                        /*if(bitmap != null && !bitmap.isRecycled()){
                           bitmap.recycle();
                        }*/
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                        commonUtils.hideProgress();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                if (activity != null) {
                                    iOnCallWSCallBack.onFailure(error);
                                }
                            }
                        }
                        commonUtils.hideProgress();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                        commonUtils.hideProgress();
                    }
                    CommonUtils.printStackTrace(error);
                }
            },context) {
                @Override
                protected Map<String, String> getParams() {
                    JSONObject jsonObject = new JSONObject();
                    Map<String, String> encryptedRequest = null;
                    try {
                        jsonObject.put(JSONTagConstant.TYPE,type);
                        jsonObject.put(JSONTagConstant.LOCALE,commonUtils.getCustomLocale(context, commonUtils.APP_TYPE_MOBILE));
                        encryptedRequest = getEncryptedMultipartRequest(activity, jsonObject, mJetEncryptor);
                    } catch (Exception e) {
                        commonUtils.printStackTrace(e);
                        commonUtils.hideProgress();
                    }
                    return encryptedRequest;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    Drawable drawable = null;
                    if (bitmap != null) {
                        drawable = new BitmapDrawable(activity.getResources(), bitmap);
                        SimpleDateFormat dateformat = new SimpleDateFormat("MMddyyyyhhmmss");
                        Date date = new Date();
                        String fileName = dateformat.format(date) + ".jpg";
                        if (drawable != null) {
                            params.put("filenames", new DataPart(fileName, getFileDataFromDrawable(drawable),
                                    "image/jpeg"));
                        }
                    }
                    return params;
                }
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstants.TIME_OUT_EXCEPTION_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            multipartRequest.setShouldCache(false);
            //Adding request to the queue
            queue.add(multipartRequest);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
            commonUtils.hideProgress();
        }
    }

    public Map<String, String> getEncryptedMultipartRequest(Activity context, JSONObject postParamsObject, JetEncryptor mJetEncryptor) {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject postParamsObjectEnc = null;
        try {
            JSONObject localeObject;
            localeObject = commonUtils.getCustomLocale(context.getApplicationContext(), AppConstants.APP_TYPE_MOBILE);

            String postParamsObjectStr = mJetEncryptor.processData(context, postParamsObject.toString());
            postParamsObjectEnc = new JSONObject(postParamsObjectStr);

            params.put("param1", postParamsObjectEnc.getString("param1"));
            params.put("param2", postParamsObjectEnc.getString("param2"));
            params.put("param3", postParamsObjectEnc.getString("param3"));
            params.put("locale[country]", localeObject.getString("country"));
            params.put("locale[language]", localeObject.getString("language"));
            params.put("locale[platform]", localeObject.getString("platform"));
            params.put("locale[version]", localeObject.getString("version"));
            params.put("locale[segment]", localeObject.getString("segment"));
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

        return params;
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()){
            case APIConstants.FILE_UPLOAD:
//                networkAPIResponseCallback.onSuccessResponse(response,networkAPICallModel);
                try {
                } catch (Exception e) {
                    commonUtils.printStackTrace(e);
                }
                break;
        }
    }
}
