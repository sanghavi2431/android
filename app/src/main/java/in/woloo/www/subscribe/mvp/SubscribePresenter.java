package in.woloo.www.subscribe.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import in.woloo.www.utils.Logger;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.subscribe.adapter.SubscribeAdapter;
import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.subscribe.models.GetSubscriptionDetailsResponse;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.subscribe.models.PlanResponse;
import in.woloo.www.subscribe.models.PurchaseSubscriptionResponse;
import in.woloo.www.subscribe.models.SubscriptionListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class SubscribePresenter implements NetworkAPIResponseCallback {

    private static final String TAG = SubscribePresenter.class.getSimpleName();

    private Context mContext;
    private SubscribeView subscribeView;
    private SubscribeFragment subscribeFragment;
    private String currentSubscriptionPlan;
    private String msg;
    private boolean isEmail;
    private String mobile;

    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    ArrayList<PlanResponse.Data> subscriptionArrayList;
    ArrayList<PlanResponse.Data> selectedSubscriptionArrayList = new ArrayList<>();
    RecyclerView recyclerView_subscribe;
    private SubscribeAdapter adapter;
    private UserProfileMergedResponse viewProfileResponse;
    private String futureSubscriptionPlan="";
    private String purchasedBy="";

    public SubscribePresenter(Context mContext, SubscribeView subscribeView, ArrayList<PlanResponse.Data> subscriptionArrayList, RecyclerView recyclerView_subscribe, SubscribeFragment subscribeFragment, String currentSubscriptionPlan, String futureSubscriptionPlan, String msg, boolean isEmail, String mobile,String purchasedBy) {
        this.mContext = mContext;
        this.subscribeView = subscribeView;
        this.subscribeFragment = subscribeFragment;
        this.currentSubscriptionPlan = currentSubscriptionPlan;
        this.futureSubscriptionPlan = futureSubscriptionPlan;
        this.msg = msg;
        this.isEmail = isEmail;
        this.mobile = mobile;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
        this.subscriptionArrayList=subscriptionArrayList;
        this.recyclerView_subscribe=recyclerView_subscribe;
        this.purchasedBy=purchasedBy;
        getProfile();
    }

    public SubscribePresenter(Context mContext, SubscribeView subscribeView) {
        this.mContext = mContext;
        this.subscribeView = subscribeView;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
        getProfile();
    }

    public void getProfile(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<UserProfileMergedResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_PROFILE_MERGED, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getMySubscriptionList(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<GetSubscriptionDetailsResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.MY_SUBSCRIPTION_LIST_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getSubscriptionList(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<SubscriptionListResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SUBSCRIPTION_LIST_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void getSubscriptionPlans(){
        try {
            JSONObject mJsObjParam = new JSONObject();
            Type parserType = new TypeToken<PlanResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GET_PLAN, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void initSubscription(int id, String planId, SubscribeAdapter.InitSubscriptionCallback initSubscriptionCallback, boolean isFutureSubscription) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.ID, id);
                mJsObjParam.put(JSONTagConstant.PLAN_ID, planId);
                if (isFutureSubscription)
                    mJsObjParam.put(JSONTagConstant.FUTURE_PLAN_ID, "1");
                else
                    mJsObjParam.put(JSONTagConstant.FUTURE_PLAN_ID, "0");
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<InitSubscriptionResponse>() {
            }.getType();
            //NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.INIT_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.INIT_SUBSCRIPTION_ORDER, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setCustomObject(initSubscriptionCallback);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    public void purchaseSubscription(String subscriptionId){
        try {
            JSONObject mJsObjParam = new JSONObject();
            try{
                mJsObjParam.put(JSONTagConstant.SUBSCRIPTION_ID,subscriptionId);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<PurchaseSubscriptionResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.PURCHASE_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
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
            case APIConstants.SUBSCRIPTION_LIST_API:
                try {
                    SubscriptionListResponse subscriptionListResponse = (SubscriptionListResponse) networkAPICallModel.getResponseObject();
                    if(subscriptionListResponse != null){
                        Logger.e("subscriptionList",subscriptionListResponse.toString());
                        //subscriptionArrayList.addAll(subscriptionListResponse.getSubscription());
                        //setSearchResults();
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.MY_SUBSCRIPTION_LIST_API:
                try {
                    GetSubscriptionDetailsResponse getSubscriptionDetailsResponse = (GetSubscriptionDetailsResponse) networkAPICallModel.getResponseObject();
                    if(getSubscriptionDetailsResponse != null){
                        GetSubscriptionDetailsResponseFlow(getSubscriptionDetailsResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.GET_PLAN:
                try{
                    PlanResponse planResponse = (PlanResponse) networkAPICallModel.getResponseObject();
                    if(planResponse != null) {
                        selectedSubscriptionArrayList = new ArrayList<>();
                        subscriptionArrayList = new ArrayList<>();
                        selectedSubscriptionArrayList.addAll(planResponse.getData());
                        if (currentSubscriptionPlan != null) {
                            for (int i = 0; i < selectedSubscriptionArrayList.size(); i++) {
                                if (currentSubscriptionPlan.equals(selectedSubscriptionArrayList.get(i).getPlanId())) {
                                    subscriptionArrayList.add(0, selectedSubscriptionArrayList.get(i));
                                    selectedSubscriptionArrayList.remove(i);
                                }
                            }
                        }
                        if (futureSubscriptionPlan != null) {
                            for (int i = 0; i < selectedSubscriptionArrayList.size(); i++) {
                                if (futureSubscriptionPlan.equals(selectedSubscriptionArrayList.get(i).getPlanId())) {
                                    if (subscriptionArrayList.size() < 1)
                                        subscriptionArrayList.add(0, selectedSubscriptionArrayList.get(i));
                                    else
                                        subscriptionArrayList.add(1, selectedSubscriptionArrayList.get(i));
                                    selectedSubscriptionArrayList.remove(i);
                                }
                            }
                        }
                        subscriptionArrayList.addAll(selectedSubscriptionArrayList);
                        setPlanResults();
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            case APIConstants.INIT_SUBSCRIPTION_ORDER:
                try{
                    InitSubscriptionResponse initSubscriptionResponse = (InitSubscriptionResponse) networkAPICallModel.getResponseObject();
                    SubscribeAdapter.InitSubscriptionCallback initSubscriptionCallback = (SubscribeAdapter.InitSubscriptionCallback)networkAPICallModel.getCustomObject();
                    if(initSubscriptionResponse != null && initSubscriptionCallback != null){
                        initSubscriptionCallback.initSubscriptionSuccess(initSubscriptionResponse);
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            case APIConstants.USER_PROFILE_MERGED:
                try{
                    this.viewProfileResponse = (UserProfileMergedResponse) networkAPICallModel.getResponseObject();
                    if(adapter != null){
//                        adapter.setViewProfile(viewProfileResponse);
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
                break;
            default:
                break;
        }
    }

    private void GetSubscriptionDetailsResponseFlow(GetSubscriptionDetailsResponse getSubscriptionDetailsResponse) {
        try{
            if(getSubscriptionDetailsResponse != null && getSubscriptionDetailsResponse.getStatus().equals(AppConstants.API_SUCCESS)){
                subscribeView.setMySubscriptionResponse(getSubscriptionDetailsResponse);
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

    private void setPlanResults() {
//        try{
//            adapter = new SubscribeAdapter(mContext,subscriptionArrayList,this,viewProfileResponse,currentSubscriptionPlan,futureSubscriptionPlan,msg,isEmail,mobile,purchasedBy);
//            recyclerView_subscribe.setHasFixedSize(true);
//            recyclerView_subscribe.setLayoutManager(new LinearLayoutManager(mContext));
//            recyclerView_subscribe.setAdapter(adapter);
//        }catch (Exception ex){
//             CommonUtils.printStackTrace(ex);
//        }
    }
}
