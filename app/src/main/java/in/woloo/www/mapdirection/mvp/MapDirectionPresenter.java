package in.woloo.www.mapdirection.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.mapdirection.model.NavigationRewardsResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.razorpay.model.RazorPayResponse;
import in.woloo.www.utils.AppConstants;

public class MapDirectionPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private MapDirectionView mapDirectionView;
    private NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public MapDirectionPresenter(Context mContext, MapDirectionView mapDirectionView) {
        this.mContext = mContext;
        this.mapDirectionView = mapDirectionView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void getWolooNavigationReward(int wolooId){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.WOLOO_ID,wolooId);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<NavigationRewardsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.NAVIGATION_REWARD, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
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
            case APIConstants.NAVIGATION_REWARD:
                try {
                    NavigationRewardsResponse navigationRewardsResponse = (NavigationRewardsResponse) networkAPICallModel.getResponseObject();
                    mapDirectionView.navigationRewardSuccess(navigationRewardsResponse);
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
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
