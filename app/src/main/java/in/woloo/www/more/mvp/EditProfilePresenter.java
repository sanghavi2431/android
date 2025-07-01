package in.woloo.www.more.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.netcore.android.Smartech;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.giftcard.model.GiftCardModelResponse;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.util.NetcoreUserDetails;

public class EditProfilePresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private EditProfileView editProfileView;

    private NetworkAPICall mNetworkAPICall;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public EditProfilePresenter(Context mContext, EditProfileView editProfileView) {
        this.mContext = mContext;
        this.editProfileView = editProfileView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void editProfile(Context context,String name,String email,String fileNames,String path,String city,String pincode,String address){
        try{
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.NAME,name);
                mJsObjParam.put(JSONTagConstant.EMAIL,email);
                mJsObjParam.put(JSONTagConstant.FILE_NAMES,fileNames);
                mJsObjParam.put(JSONTagConstant.PATH,path);
                mJsObjParam.put(JSONTagConstant.CITY,city);
                mJsObjParam.put(JSONTagConstant.PINCODE,pincode);
                mJsObjParam.put(JSONTagConstant.ADDRESS,address);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<EditProfileResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.EDIT_PROFILE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam,mJetEncryptor);
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
            case APIConstants.EDIT_PROFILE:
                try {
                    EditProfileResponse editProfileResponse = (EditProfileResponse) networkAPICallModel.getResponseObject();
                    if(editProfileResponse != null){
                        //Saving User Profile  Data to Netcore
                        new NetcoreUserDetails(mContext).updateNetcoreUserProfile();
                        editProfileResponseFlow(editProfileResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void editProfileResponseFlow(EditProfileResponse editProfileResponse) {
        try{
            if(editProfileResponse != null && editProfileResponse.getStatus().equals(AppConstants.API_SUCCESS)){

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
