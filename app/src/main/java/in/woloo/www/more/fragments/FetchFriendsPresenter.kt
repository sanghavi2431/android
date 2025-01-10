package `in`.woloo.www.more.fragments

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.fragments.adapter.InviteFriendsAdapter
import `in`.woloo.www.more.fragments.model.Contacts
import `in`.woloo.www.more.subscribe.models.SubscriptionListResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.Logger
import org.json.JSONObject

class FetchFriendsPresenter : NetworkAPIResponseCallback {
    private val mContext: Context? = null


    //    private SubscribeView subscribeView;
    private val mCommonUtils: CommonUtils? = null
    private val mJetEncryptor: JetEncryptor? = null
    private val mNetworkAPICall: NetworkAPICall? = null
    var subscriptionArrayList: ArrayList<Contacts>? = null
    var recyclerView_invitecontacts: RecyclerView? = null


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
    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.SUBSCRIPTION_LIST_API -> try {
                val subscriptionListResponse: SubscriptionListResponse =
                    networkAPICallModel.responseObject as SubscriptionListResponse
                if (subscriptionListResponse != null) {
                    Logger.e("subscriptionList", subscriptionListResponse.toString())
                    //                        subscriptionArrayList.addAll(subscriptionListResponse.getSubscription());
                    setSearchResults()
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {
    }

    override fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    override fun onTimeOutConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    private fun setSearchResults() {
        try {
            val adapter = InviteFriendsAdapter(
                mContext!!, subscriptionArrayList!!
            )
            recyclerView_invitecontacts!!.setHasFixedSize(true)
            recyclerView_invitecontacts!!.layoutManager = LinearLayoutManager(mContext)
            recyclerView_invitecontacts!!.adapter = adapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        private val TAG: String = FetchFriendsPresenter::class.java.simpleName
    }
}
