package in.woloo.www.invite_friend.fragments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.util.ArrayList;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.invite_friend.fragments.adapter.InviteFriendsAdapter;
import in.woloo.www.invite_friend.fragments.model.Contacts;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.subscribe.models.SubscriptionListResponse;
import in.woloo.www.subscribe.mvp.SubscribeView;
import in.woloo.www.utils.Logger;

public class FetchFriendsPresenter implements NetworkAPIResponseCallback {

    private static final String TAG = FetchFriendsPresenter.class.getSimpleName();

    private Context mContext;
//    private SubscribeView subscribeView;



    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    ArrayList<Contacts> subscriptionArrayList;
    RecyclerView recyclerView_invitecontacts;

//    public FetchFriendsPresenter(Context mContext, SubscribeView subscribeView, ArrayList<Contacts> subscriptionArrayList, RecyclerView recyclerView_invitecontacts) {
//        this.mContext = mContext;
//        this.subscribeView = subscribeView;
//        mCommonUtils = new CommonUtils();
//        mJetEncryptor = JetEncryptor.getInstance();
//        mNetworkAPICall = new NetworkAPICall();
//        this.subscriptionArrayList=subscriptionArrayList;
//        this.recyclerView_invitecontacts=recyclerView_invitecontacts;
//    }

//    public void getSubscriptionList(){
//        try {
//            JSONObject mJsObjParam = new JSONObject();
//            Type parserType = new TypeToken<SubscriptionListResponse>() {
//            }.getType();
//            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.SUBSCRIPTION_LIST_API, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
//            networkAPICallModel.setParserType(parserType);
//            networkAPICallModel.setShowProgress(true);
//            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
//        } catch (Exception e) {
//            Logger.e(TAG, e.getMessage());
//        }
//    }

//    public void purchaseSubscription(String subscriptionId){
//        try {
//            JSONObject mJsObjParam = new JSONObject();
//            try{
//                mJsObjParam.put(JSONTagConstant.SUBSCRIPTION_ID,subscriptionId);
//            }catch (Exception ex){
//                 CommonUtils.printStackTrace(ex);
//            }
//            Type parserType = new TypeToken<PurchaseSubscriptionResponse>() {
//            }.getType();
//            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.PURCHASE_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
//            networkAPICallModel.setParserType(parserType);
//            networkAPICallModel.setShowProgress(true);
//            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
//        } catch (Exception e) {
//            Logger.e(TAG, e.getMessage());
//        }
//    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.SUBSCRIPTION_LIST_API:
                try {
                    SubscriptionListResponse subscriptionListResponse = (SubscriptionListResponse) networkAPICallModel.getResponseObject();
                    if(subscriptionListResponse != null){
                        Logger.e("subscriptionList",subscriptionListResponse.toString());
//                        subscriptionArrayList.addAll(subscriptionListResponse.getSubscription());
                        setSearchResults();
                    }
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

    private void setSearchResults() {
        try{
            InviteFriendsAdapter adapter = new InviteFriendsAdapter(mContext,subscriptionArrayList);
            recyclerView_invitecontacts.setHasFixedSize(true);
            recyclerView_invitecontacts.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView_invitecontacts.setAdapter(adapter);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}
