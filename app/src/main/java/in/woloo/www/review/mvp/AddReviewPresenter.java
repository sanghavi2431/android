package in.woloo.www.review.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import in.woloo.www.utils.Logger;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.review.models.ReviewOptionsResponse;
import in.woloo.www.review.models.SubmitReviewResponse;
import in.woloo.www.subscribe.models.SubscriptionListResponse;
import in.woloo.www.utils.AppConstants;

public class AddReviewPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = AddReviewPresenter.class.getSimpleName();

    private Context mContext;
    private AddReviewView addReviewView;

    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;

    public AddReviewPresenter(Context mContext, AddReviewView addReviewView) {
        this.mContext = mContext;
        this.addReviewView = addReviewView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
    }

    public void addReview(String rating,String title){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                 mJsObjParam.put(JSONTagConstant.RATING,rating);
                mJsObjParam.put(JSONTagConstant.TITLE,title);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<SubscriptionListResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.ADD_REVIEW, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getReviewOptions(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<ReviewOptionsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GET_REVIEW_OPTIONS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void submitReview(int wolooId, int userRating, List<Integer> reviewOptionList, String reviewDescription) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                String reviewOption = android.text.TextUtils.join(",", reviewOptionList);
                mJsObjParam.put(JSONTagConstant.WOLOO_ID,wolooId);
                mJsObjParam.put(JSONTagConstant.USER_RATING,userRating);
                mJsObjParam.put(JSONTagConstant.REVIEW_OPTION,reviewOption);
                mJsObjParam.put(JSONTagConstant.REVIEW_DESCRIPTION,reviewDescription);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<SubmitReviewResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SUBMIT_REVIEW, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }



    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.GET_REVIEW_OPTIONS:
                try {
                    ReviewOptionsResponse reviewOptionsResponse = (ReviewOptionsResponse) networkAPICallModel.getResponseObject();
                    if(reviewOptionsResponse != null){
                        reviewOptionsResponseFlow(reviewOptionsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.SUBMIT_REVIEW:
                try {
                    SubmitReviewResponse submitReviewResponse = (SubmitReviewResponse) networkAPICallModel.getResponseObject();
                    if(submitReviewResponse != null){
                        submitReviewResponseFlow(submitReviewResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void submitReviewResponseFlow(SubmitReviewResponse submitReviewResponse) {
        try{
            addReviewView.showSubmitReviewResponse(submitReviewResponse);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void reviewOptionsResponseFlow(ReviewOptionsResponse reviewOptionsResponse) {
        try{
            if(reviewOptionsResponse != null && reviewOptionsResponse.getStatus() != null && reviewOptionsResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                addReviewView.reviewOptionsList(reviewOptionsResponse);
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
