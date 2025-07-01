package in.woloo.www.networksUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.splash.UserDetails;


public class CustomVolleyRequest extends JsonObjectRequest {

    private Response.Listener<JSONObject> mListener;
    private Response.ErrorListener mErrorListener;
    JSONObject jsonObject;
    Gson gson;
    private Context mContext;
    private String mobileTv;
    //private Utility mUtility;
    private JetEncryptor jetEncryptor;
    private String TAG=CustomVolleyRequest.class.getSimpleName();

    public CustomVolleyRequest(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context context, String mobileTv) {
        super(method, url, jsonObject, listener, errorListener);

      //  this.mUtility = new Utility();
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.jsonObject = jsonObject;
        mContext = context;
        this.mobileTv = mobileTv;
        if (gson == null) {
            gson = new Gson();
        }

    }

    public CustomVolleyRequest(String url, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonObject, listener, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.jsonObject = jsonObject;
        if (gson == null) {
            gson = new Gson();
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
       /* try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return UserCoinHistoryModel.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return UserCoinHistoryModel.error(new ParseError(e));
        } catch (JSONException je) {
            return UserCoinHistoryModel.error(new ParseError(je));
        }*/
        return super.parseNetworkResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            CommonUtils commonUtils = new CommonUtils();

            try {
                if (mContext!=null && mContext instanceof Activity) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            } catch (Exception e) {
                CommonUtils.printStackTrace(e);
            }

            NetworkResponse response = volleyError.networkResponse;
            //Netcore Disabled Log
//            Logger.e(TAG,"deliverError:: "+volleyError.getLocalizedMessage());
//            Logger.e(TAG,"deliverError:: "+volleyError.getMessage());
//            Logger.e(TAG,"deliverError:: "+volleyError.getCause());
//            Logger.e(TAG,"deliverError:: "+volleyError.getNetworkTimeMs());
//            Logger.e(TAG,"deliverError:: "+volleyError.getSuppressed());
//            Logger.e(TAG,"deliverError Header:: "+response.allHeaders);
//            Logger.e(TAG,"deliverError Status Code:: "+response.statusCode);
            if (volleyError instanceof ServerError && response != null) {
                try {
                    //Netcore Disabled Log
//                    Logger.e(TAG,"deliverError 1: "+volleyError.getMessage());
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
                    JSONObject obj = new JSONObject(res);
                    //Netcore Disabled Log
//                    Logger.e(TAG,"deliverError 2: "+obj);
                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string
                    CommonUtils.printStackTrace(e1);
                } catch (JSONException e2) {
                    // returned data is not JSONObject?
                    CommonUtils.printStackTrace(e2);
                }catch (Exception e){
                      CommonUtils.printStackTrace(e);
                }
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (error instanceof TimeoutError) {
            if (mContext != null) {
                Toast.makeText(mContext,
                        mContext.getResources().getString(R.string.slow_internet_connection_),
                        Toast.LENGTH_LONG).show();
            }
        }
        // As of f605da3 the following should work
        NetworkResponse response = error.networkResponse;
        if (error instanceof ServerError && response != null) {
            try {
                String res = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                // Now you can use any deserializer to make sense of data
                JSONObject obj = new JSONObject(res);
                Logger.e(TAG,"deliverError: "+obj);
            } catch (UnsupportedEncodingException e1) {
                // Couldn't properly decode data to string
                CommonUtils.printStackTrace(e1);
            } catch (JSONException e2) {
                // returned data is not JSONObject?
                CommonUtils.printStackTrace(e2);
            }catch (Exception e){
                  CommonUtils.printStackTrace(e);
            }
        }
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        try {
            jetEncryptor = JetEncryptor.getInstance();
           // headers.put("Authorization", "Bearer " + jetEncryptor.getJwtkey());
            if(new CommonUtils().isLoggedIn()){
                UserDetails userInfo = new CommonUtils().getUserInfo();
                headers.put("Authorization", "Bearer "+ SharedPrefSettings.Companion.getGetPreferences().fetchToken());
            }else {
                headers.put("Authorization", "Bearer " + jetEncryptor.getJwtkey());
            }
          /*  headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");*/
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
            return headers;
    }

}
