package in.woloo.www.scan_qr_code.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.scan_qr_code.model.ScanQRCodeResponse;
import in.woloo.www.utils.AppConstants;

public class ScanQRCodePresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private ScanQRCodeView scanQRCodeView;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public ScanQRCodePresenter(Context mContext, ScanQRCodeView scanQRCodeView) {
        this.mContext = mContext;
        this.scanQRCodeView = scanQRCodeView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void scanQRCode(Context context,String name){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.NAME,name);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<ScanQRCodeResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SCAN_WOLOO, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
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
            case APIConstants.SCAN_WOLOO:
                try {
                    ScanQRCodeResponse scanQRCodeResponse = (ScanQRCodeResponse) networkAPICallModel.getResponseObject();
                    if (scanQRCodeResponse != null) {
                        scanQRCodeResponseFlow(scanQRCodeResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }

    }

    private void scanQRCodeResponseFlow(ScanQRCodeResponse scanQRCodeResponse) {
        try {
             scanQRCodeView.scanQRResponse(scanQRCodeResponse);
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
