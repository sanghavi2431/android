package in.woloo.www.home_details.mvp;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.home_details.models.LikeResponse;
import in.woloo.www.home_details.models.LikeStatusResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.review.models.ReviewListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class HomeDetailsPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private HomeDetailsView homeDetailsView;

    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    TextView tv_like;

    public HomeDetailsPresenter(Context mContext, HomeDetailsView homeDetailsView) {
        this.mContext = mContext;
        this.homeDetailsView = homeDetailsView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void like_unlike(int wolooId, String endpoint, TextView tv_like) {
        this.tv_like = tv_like;
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.WOLOO_ID, wolooId);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<LikeResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(endpoint, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void getLikeStatus(int wolooId) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.WOLOO_ID, wolooId);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<LikeStatusResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.WOLOO_LIKE_STATUS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    public void getReviewList(int wolooId, int pageNumber) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.WOLOO_ID, wolooId);
                mJsObjParam.put(JSONTagConstant.PAGE_NUMBER, pageNumber);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<ReviewListResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GET_REVIEW_LIST, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(false);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void redeemOffer(int offerId) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.OFFER_ID, offerId);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            Type parserType = new TypeToken<JSONObject>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.WOLOO_REDEEM_OFFER, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(true);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.GET_REVIEW_LIST:
                try {
                    ReviewListResponse reviewListResponse = (ReviewListResponse) networkAPICallModel.getResponseObject();
                    if (reviewListResponse != null) {
                        reviewListResponseFlow(reviewListResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;

            case APIConstants.WOLOOLIKE:
            case APIConstants.WOLOOUNLIKE:
                try {
                    LikeResponse likeResponse = (LikeResponse) networkAPICallModel.getResponseObject();
                    if (likeResponse != null) {
                        like_unlikeresponsefunction(likeResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.WOLOO_LIKE_STATUS:
                try {
                    LikeStatusResponse likeStatusResponse = (LikeStatusResponse) networkAPICallModel.getResponseObject();
                    if (likeStatusResponse != null) {
                        likeStatusResponseFlow(likeStatusResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.WOLOO_REDEEM_OFFER:
                try {
                    JSONObject responseObject = (JSONObject) networkAPICallModel.getResponseObject();
                    Logger.i("TAG", responseObject.toString());
                    homeDetailsView.onRedeemSuccess();
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void likeStatusResponseFlow(LikeStatusResponse likeStatusResponse) {
        try {
            if (likeStatusResponse != null && likeStatusResponse.getStatus() != null && likeStatusResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                homeDetailsView.likeStatusSuccess(likeStatusResponse);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void like_unlikeresponsefunction(LikeResponse likeResponse) {
        homeDetailsView.getLike_Unlike(likeResponse, tv_like);
    }

    private void reviewListResponseFlow(ReviewListResponse reviewListResponse) {
        homeDetailsView.getReviewList(reviewListResponse);
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
