package in.woloo.www.dashboard.ui.wah_certificate.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.wah_certificate.WahCertificateActivity;
import in.woloo.www.dashboard.ui.wah_certificate.model.WahCertificateDetailsResponse;
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
import in.woloo.www.woloo_host.mvp.CreateWolooHostPresenter;
import in.woloo.www.woloo_host.mvp.CreateWolooHostView;

public class WahCertificatePresenter implements NetworkAPIResponseCallback {

    private static final String TAG = WahCertificatePresenter.class.getSimpleName();

    private Context mContext;
    WahCertificateView wahCertificateView;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public WahCertificatePresenter(Context mContext, WahCertificateView wahCertificateView) {
        this.mContext = mContext;
        this.wahCertificateView = wahCertificateView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void getWahCertificateDetails(String wahCertificateCode){
        try{
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put(JSONTagConstant.WAH_CERTIFICATE_CODE,wahCertificateCode);
            Type parserType = new TypeToken<WahCertificateDetailsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.WAH_CERTIFICATE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.WAH_CERTIFICATE:
                try {
                    WahCertificateDetailsResponse wahCertificateDetailsResponse = (WahCertificateDetailsResponse) networkAPICallModel.getResponseObject();
                    if(wahCertificateDetailsResponse != null){
                        wahCertificateDetailsResponseFlow(wahCertificateDetailsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void wahCertificateDetailsResponseFlow(WahCertificateDetailsResponse wahCertificateDetailsResponse) {
        try{
            if(wahCertificateDetailsResponse != null && wahCertificateDetailsResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                wahCertificateView.setWahCertificateResponse(wahCertificateDetailsResponse);
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
