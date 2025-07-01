package in.woloo.www.interestedtopic.mvp;

import static in.woloo.www.networksUtils.APIConstants.BLOG_CATEGORIES;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Utility;

public class InterestedTopicPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = InterestedTopicPresenter.class.getSimpleName();

    private Context context;
    private InterestedTopicView interestedTopicView;
    private final NetworkAPICall mNetworkAPICall;
    private final JetEncryptor mJetEncryptor;
    private final CommonUtils mCommonUtils;
    String keywords = "";

    public InterestedTopicPresenter(Context context, InterestedTopicView interestedTopicView) {
        this.context = context;
        this.interestedTopicView = interestedTopicView;
        mNetworkAPICall = new NetworkAPICall();
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public void getCategories() {
        Type parserType = new TypeToken<CategoriesResponse>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();

        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.BLOG_CATEGORIES, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
        networkAPICallModel.setParserType(parserType);
        networkAPICallModel.setShowProgress(true);
        mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
    }

    public void saveUserCategories(JSONArray categories) {
        Type parserType = new TypeToken<JSONObject>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("categories", categories);
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SAVE_USER_CATEGORIES, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
              CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SAVE_USER_CATEGORIES:
                networkAPICallModel.getResponseObject();
                interestedTopicView.onSaveUserCategories();
                break;
            case BLOG_CATEGORIES:
                CategoriesResponse categoriesResponse = (CategoriesResponse) networkAPICallModel.getResponseObject();
                if (categoriesResponse != null && categoriesResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                    interestedTopicView.getCategories(categoriesResponse);
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
