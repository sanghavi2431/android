package in.woloo.www.search.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import in.woloo.www.utils.Logger;

import com.android.volley.VolleyError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.networksUtils.NetworkStatus;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Utility;

public class WolooSearchPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = WolooSearchPresenter.class.getSimpleName();

    private Context context;
    private WolooSearchView wolooSearchView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;
    String keywords = "";

    public WolooSearchPresenter(Context context, WolooSearchView wolooSearchView) {
        this.context = context;
        this.wolooSearchView = wolooSearchView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public void wolooSearchAPI(String lat, String lng, String name, int pageNumber) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put("lat", lat);
                mJsObjParam.put("lng", lng);
                mJsObjParam.put("name", name);
                mJsObjParam.put("pageNumber", pageNumber);
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }

            Type parserType = new TypeToken<SearchWolooResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SEARCH_WOLOO_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            int timeout = 120 * 1000;
            networkAPICallModel.setTimeOut(timeout);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getNearByStore(double lat, double lng, String name, boolean isOffer, boolean isSearchWithOnlyOffer) {
        Bundle bundle = new Bundle();
        keywords = name;
        bundle.putString(AppConstants.SEARCH_KEYWORD, name);
        bundle.putString(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        Utility.logFirebaseEvent(context, bundle, AppConstants.SEARCH_WOLOO_EVENT);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(AppConstants.SEARCH_KEYWORD, name);
        payload.put(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        Utility.logNetcoreEvent(context,payload,AppConstants.SEARCH_WOLOO_EVENT);

        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put(JSONTagConstant.LATITUDE, lat);
            mJsObjParam.put(JSONTagConstant.LONGITUDE, lng);
            mJsObjParam.put(JSONTagConstant.PAGE_NUMBER_NEAR_WOLOO, "1");
            mJsObjParam.put(JSONTagConstant.KM_RANGE, "2");
            mJsObjParam.put(JSONTagConstant.TRANSPORT_MODE, new SharedPreference(context).getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"));
            mJsObjParam.put(JSONTagConstant.IS_SEARCH, "1");
            if (isOffer || isSearchWithOnlyOffer) {
                mJsObjParam.put(JSONTagConstant.IS_OFFER, "1");
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        Type parserType = new TypeToken<NearByStoreResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.NEAR_BY_STORE, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, JetEncryptor.getInstance());
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        int timeout = 120 * 1000;
        networkAPICallModel.setTimeOut(timeout);
        mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SEARCH_WOLOO_API:
                try {
                    SearchWolooResponse searchWolooResponse = (SearchWolooResponse) networkAPICallModel.getResponseObject();
                    if (searchWolooResponse != null) {
                        searchWolooResponseFlow(searchWolooResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.NEAR_BY_STORE:
                try {
                    NearByStoreResponse nearByStoreResponse = (NearByStoreResponse) networkAPICallModel.getResponseObject();
                    if (nearByStoreResponse.getStatus().equalsIgnoreCase(NetworkStatus.SUCCESS_STR)) {
                        wolooSearchView.onGetNearByStore(nearByStoreResponse, networkAPICallModel, keywords);
                    }
                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }
                break;
            default:
                break;
        }
    }

    private void searchWolooResponseFlow(SearchWolooResponse searchWolooResponse) {
        try {
            Utility.hideKeyboard((Activity) context);
            wolooSearchView.searchWolooSuccess(searchWolooResponse, keywords);
        } catch (Exception ex) {
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
