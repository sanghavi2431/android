package in.woloo.www.my_history.mvp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.my_history.adapter.MyHistoryAdapter;
import in.woloo.www.my_history.adapter.MyOfferAdapter;
import in.woloo.www.my_history.model.MyHistoryResponse;
import in.woloo.www.my_history.model.MyOffersResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EmptyRecyclerView;
import in.woloo.www.utils.EndlessRecyclerOnScrollListener;

public class MyHistoryPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private MyHistoryView myHistoryView;
    private EmptyRecyclerView rvMyHistory;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    private MyHistoryAdapter myHistoryAdapter;
    private MyOfferAdapter myOfferAdapter;

    private List<MyHistoryResponse.History> historyList = new ArrayList<MyHistoryResponse.History>();
    private List<NearByStoreResponse.Data> dataItemList = new ArrayList<>();
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private int mPageNumber;
    private int mNextPage;
    LinearLayout ll_nodatafound;


    public MyHistoryPresenter(Context mContext, MyHistoryView myHistoryView, EmptyRecyclerView rvMyHistory, LinearLayout ll_nodatafound, boolean isFromOffer) {
        this.mContext = mContext;
        this.myHistoryView = myHistoryView;
        this.rvMyHistory = rvMyHistory;
        mCommonUtils = new CommonUtils();
        mJetEncryptor = JetEncryptor.getInstance();
        mNetworkAPICall = new NetworkAPICall();
        this.ll_nodatafound = ll_nodatafound;
        setAdapter(isFromOffer);
        mPageNumber = 1;
        if (isFromOffer) {
            getMyOffers(mPageNumber);
        } else {
            getRewardHistory(mPageNumber);
        }
    }

    private void getMyOffers(int page) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.PAGE_NUMBER, page);
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<MyOffersResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.MY_OFFERS, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(false);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void getRewardHistory(int page) {
        try {
            JSONObject mJsObjParam = new JSONObject();
            try {
                mJsObjParam.put(JSONTagConstant.PAGE_NUMBER, page);
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            Type parserType = new TypeToken<MyHistoryResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.REWARD_HISTORY, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setShowProgress(false);
            networkAPICallModel.setParserType(parserType);
            mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.MY_OFFERS:
                try {
                    MyOffersResponse myOffersResponse = (MyOffersResponse) networkAPICallModel.getResponseObject();
                    if (myOffersResponse != null && myOffersResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                        myOffersResponseFlow(myOffersResponse);
                    } else {
                        myHistoryView.showNoHistory();
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.REWARD_HISTORY:
                try {
                    MyHistoryResponse myHistoryResponse = (MyHistoryResponse) networkAPICallModel.getResponseObject();
                    if (myHistoryResponse != null && myHistoryResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                        myHistoryResponseFlow(myHistoryResponse);
                    } else {
                        myHistoryView.showNoHistory();
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void myOffersResponseFlow(MyOffersResponse myOffersResponse) {
        ll_nodatafound.setVisibility(View.GONE);
        rvMyHistory.setVisibility(View.VISIBLE);
        try {
            if (mPageNumber == 1) {
                dataItemList.clear();
            }
            if (myOffersResponse != null && myOffersResponse.getData() != null && myOffersResponse.getData() != null && myOffersResponse.getData().size() > 0) {
                dataItemList.addAll(myOffersResponse.getData());
                myOfferAdapter.notifyDataSetChanged();

            } else {
                if (mPageNumber == 1) {
                    myHistoryView.showNoHistory();
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void myHistoryResponseFlow(MyHistoryResponse myHistoryResponse) {
        ll_nodatafound.setVisibility(View.GONE);
        rvMyHistory.setVisibility(View.VISIBLE);
        try {
            if (mPageNumber == 1) {
                historyList.clear();
            }
            if (myHistoryResponse != null && myHistoryResponse.getData() != null && myHistoryResponse.getData().getHistory() != null && myHistoryResponse.getData().getHistory().size() > 0) {
                historyList.addAll(myHistoryResponse.getData().getHistory());
                myHistoryAdapter.notifyDataSetChanged();

                if (myHistoryResponse.getData().getNext() != null && myHistoryResponse.getData().getNext() != null) {
                    mNextPage = myHistoryResponse.getData().getNext();
                } else {
                    mNextPage = 0;
                }
            } else {
                if (mPageNumber == 1) {
                    myHistoryView.showNoHistory();
                }
            }
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

    private void setAdapter(boolean isFromOffer) {
        try {
            if(isFromOffer){
                myOfferAdapter = new MyOfferAdapter(mContext, dataItemList);
                rvMyHistory.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                rvMyHistory.setLayoutManager(linearLayoutManager);
                rvMyHistory.setAdapter(myOfferAdapter);
            }
            else {
                myHistoryAdapter = new MyHistoryAdapter(mContext, historyList);
                rvMyHistory.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                rvMyHistory.setLayoutManager(linearLayoutManager);
                endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        mPageNumber = mNextPage;
                        if (mNextPage != 0) {
                            getRewardHistory(mPageNumber);
                        }
                    }
                };
                rvMyHistory.addOnScrollListener(endlessRecyclerOnScrollListener);
                rvMyHistory.setAdapter(myHistoryAdapter);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

}
