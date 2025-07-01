package in.woloo.www.utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import in.woloo.www.more.models.FileUploadResponse;

public interface IOnCallWSCallBack {
    public void onSuccessResponse(FileUploadResponse fileUploadResponse);
    public void onFailure(VolleyError volleyError);
}
