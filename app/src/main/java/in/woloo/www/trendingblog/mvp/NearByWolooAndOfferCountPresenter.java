package in.woloo.www.trendingblog.mvp;

import static in.woloo.www.networksUtils.APIConstants.BLOGS;
import static in.woloo.www.networksUtils.APIConstants.BLOG_CATEGORIES;
import static in.woloo.www.networksUtils.APIConstants.BLOG_READ_POINT;
import static in.woloo.www.networksUtils.APIConstants.FAVOURITE_A_BLOG;
import static in.woloo.www.networksUtils.APIConstants.LIKE_A_BLOG;
import static in.woloo.www.networksUtils.APIConstants.NEAR_BY_WOLOO_AND_OFFER_COUNT;
import static in.woloo.www.networksUtils.APIConstants.READ_A_BLOG;
import static in.woloo.www.networksUtils.APIConstants.USER_PROFILE_MERGED;
import static in.woloo.www.woloo_host.BecomeWolooHostFragment.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.trendingblog.model.blog.BlogsResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class NearByWolooAndOfferCountPresenter implements NetworkAPIResponseCallback {

    private Context context;
    private NearByWolooAndOfferCountView nearByWolooAndOfferCountView;
    private JetEncryptor mJetEncryptor;
    private CommonUtils mCommonUtils;

    public NearByWolooAndOfferCountPresenter(Context context, NearByWolooAndOfferCountView nearByWolooAndOfferCountView) {
        this.context = context;
        this.nearByWolooAndOfferCountView = nearByWolooAndOfferCountView;
        mJetEncryptor = JetEncryptor.getInstance();
        mCommonUtils = new CommonUtils();
    }

    public void getNearByWolooAndOffer(String lat, String lng) {
        try {
            NetworkAPICall mNetworkAPICall = new NetworkAPICall();
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("lat", lat);
            mJsObjParam.put("lng", lng);
            mJsObjParam.put(JSONTagConstant.KM_RANGE,"6");
            Type parserType = new TypeToken<NearByWolooAndOfferCountResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(NEAR_BY_WOLOO_AND_OFFER_COUNT, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getBlogCategories() {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<CategoriesResponse>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.BLOG_CATEGORIES, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
        networkAPICallModel.setParserType(parserType);
        networkAPICallModel.setShowProgress(true);
        mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
    }

    public void getBlogs(String category, int page) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<in.woloo.www.trendingblog.model.blog.BlogsResponse>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("category", category);//"non_saved_category":true,
            mJsObjParam.put("non_saved_category", true);
            mJsObjParam.put("page", page);

            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(BLOGS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public void favouriteABlog(Blog blog) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<JSONObject>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("blog_id", blog.getId());
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.FAVOURITE_A_BLOG, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public void likeABlog(Blog blog) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<JSONObject>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("blog_id", blog.getId());
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.LIKE_A_BLOG, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public void readABlog(Blog blog) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<JSONObject>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("blog_id", blog.getId());
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.READ_A_BLOG, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public void addBlogReadPoints(Blog blog) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        Type parserType = new TypeToken<JSONObject>() {
        }.getType();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("blog_id", blog.getId());
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.BLOG_READ_POINT, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public void getUserProfile() {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserProfileMergedResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_PROFILE_MERGED, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        try {
            switch (networkAPICallModel.getApiURL()) {
                case NEAR_BY_WOLOO_AND_OFFER_COUNT:
                    NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse = (NearByWolooAndOfferCountResponse) networkAPICallModel.getResponseObject();
                    if (nearByWolooAndOfferCountResponse != null) {
                        nearByWolooAndOfferCountFlow(nearByWolooAndOfferCountResponse);
                    }
                    break;
                case BLOG_CATEGORIES:
                    CategoriesResponse categoriesResponse = (CategoriesResponse) networkAPICallModel.getResponseObject();
                    if (categoriesResponse != null && categoriesResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                        nearByWolooAndOfferCountView.getCategories(categoriesResponse);
                    }
                    break;
                case BLOGS:
                    BlogsResponse blogsResponse = (BlogsResponse) networkAPICallModel.getResponseObject();
                    if (blogsResponse != null && blogsResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                        nearByWolooAndOfferCountView.getBlogs(blogsResponse);
                    }
                    break;
                case FAVOURITE_A_BLOG:
                    JSONObject responseObject = (JSONObject) networkAPICallModel.getResponseObject();
                    if (responseObject != null) {
                        nearByWolooAndOfferCountView.onFavouriteABlog();
                    }
                    break;
                case LIKE_A_BLOG:
                    JSONObject responseObject1 = (JSONObject) networkAPICallModel.getResponseObject();
                    if (responseObject1 != null) {
                        nearByWolooAndOfferCountView.onLikeABlog();
                    }
                    break;
                case READ_A_BLOG:
                    JSONObject responseObject2 = (JSONObject) networkAPICallModel.getResponseObject();
                    if (responseObject2 != null) {
                        nearByWolooAndOfferCountView.onReadABlog();
                    }
                    break;
                case BLOG_READ_POINT:
                    JSONObject responseObject3 = (JSONObject) networkAPICallModel.getResponseObject();
                    if (responseObject3 != null) {
                        nearByWolooAndOfferCountView.onBlogReadPointsAdded();
                    }
                    break;
                case USER_PROFILE_MERGED:
                    UserProfileMergedResponse userProfile = (UserProfileMergedResponse) networkAPICallModel.getResponseObject();
                    if (userProfile != null) {
                        nearByWolooAndOfferCountView.setUserProfileMergedResponse(userProfile);
                    }
                    break;
            }
        } catch (Exception e) {
            mCommonUtils.printStackTrace(e);
        }

    }

    private void nearByWolooAndOfferCountFlow(NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse) {
        try {
            if (nearByWolooAndOfferCountResponse != null && nearByWolooAndOfferCountResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                nearByWolooAndOfferCountView.nearByWolooAndOfferCountResponse(nearByWolooAndOfferCountResponse);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
