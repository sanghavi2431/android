package in.woloo.www.woloo_host.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import in.woloo.www.utils.Logger;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.review.models.SubmitReviewResponse;
import in.woloo.www.utils.AppConstants;

public class BecomeWolooHostPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = BecomeWolooHostPresenter.class.getSimpleName();

    private Context context;
    private BecomeWolooHostView becomeWolooHostView;

    private NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public BecomeWolooHostPresenter(Context context, BecomeWolooHostView becomeWolooHostView) {
        this.context = context;
        this.becomeWolooHostView = becomeWolooHostView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void becomeWolooHost(String name,String city,String address,double lat,double log){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.CITY,city);
                mJsObjParam.put(JSONTagConstant.ADDRESS,address);
                mJsObjParam.put(JSONTagConstant.LATITUDE,lat);
                mJsObjParam.put(JSONTagConstant.LONGITUDE,log);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<SubmitReviewResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.ADD_WOLOO_HOST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }


    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {

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
