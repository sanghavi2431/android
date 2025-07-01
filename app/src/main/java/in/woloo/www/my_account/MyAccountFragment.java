package in.woloo.www.my_account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.more.models.UserCoinHistoryModel;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.my_account.adapter.CreditHistoryAdapter;
import in.woloo.www.my_account.mvp.MyAccountPresenter;
import in.woloo.www.my_account.mvp.MyAccountView;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.utils.EndlessRecyclerOnScrollListener;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.giftcard.model.UserCoins;
import in.woloo.www.v2.giftcard.viewmodel.CoinsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment implements MyAccountView  {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivShop)
    ImageView ivShop;
    @BindView(R.id.rvCreditHistory)
    RecyclerView rvCreditHistory;

    @BindView(R.id.tvWolooMoney)
    TextView tvWolooMoney;

    @BindView(R.id.tvWolooPoints)
    TextView tvWolooPoints;

    @BindView(R.id.toolbar)
    Toolbar toolbar;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean isShowBackButton;
    private boolean mParam2;

    private MyAccountPresenter myAccountPresenter;
    private CoinsViewModel coinsViewModel;
   private ArrayList<UserCoinHistoryModel.Data.HistoryItem> usercoinHistoryArrayList;
    private CreditHistoryAdapter creditHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener endEndlessRecyclerOnScrollListener;
    private int mPageNumber;
    private int mNextPage;
    public static String TAG= MyAccountFragment.class.getSimpleName();

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public static MyAccountFragment newInstance(boolean isShowBackButton) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isShowBackButton);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isShowBackButton = getArguments().getBoolean(ARG_PARAM1);
        }
        Logger.i(TAG, "onCreate");

    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this,root);
        initViews();
        setLiveData();
        if(isShowBackButton){
            ivBack.setVisibility(View.VISIBLE);
        }else {
            ivBack.setVisibility(View.GONE);
        }
        return root;
    }

    /*calling on onResume*/
    @Override
    public void onResume() {
        Logger.i(TAG, "onResume");
        super.onResume();
        ((WolooDashboard)getActivity()).hideToolbar();
    }
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
            usercoinHistoryArrayList=new ArrayList<>();
            coinsViewModel = new ViewModelProvider(this).get(CoinsViewModel.class);
            coinsViewModel.getUserCoins();
            myAccountPresenter = new MyAccountPresenter(getContext(),MyAccountFragment.this);
//            myAccountPresenter.getUserCoins(); // TODO Node
            mPageNumber = 1;
//            myAccountPresenter.getCoinHistory(mPageNumber);
            coinsViewModel.getCoinHistory(mPageNumber);
            tvTitle.setText(getString(R.string.my_account));
            ivShop.bringToFront();
            ivShop.setOnClickListener(v -> {
                if (requireActivity() instanceof WolooDashboard) {
                    ((WolooDashboard) requireActivity()).loadFragment(new ShoppingFragment(), "ShoppingFragment");
                    ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
                }
            });
            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });
            setCreditHistoryAdapter();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        coinsViewModel.observeUserCoins().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserCoins>>() {
            @Override
            public void onChanged(BaseResponse<UserCoins> userCoinsResponse) {
                Logger.i(TAG, "userCoinsSuccess");
                try{
                    if(userCoinsResponse != null){
                        if(userCoinsResponse.getData() != null && userCoinsResponse.getData() != null){
                            tvWolooPoints.setText(""+userCoinsResponse.getData().getTotalCoins());
                            tvWolooMoney.setText(""+userCoinsResponse.getData().getGiftCoins());
                        }
                    }else{
                        WolooApplication.setErrorMessage("");
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            }
        });

        coinsViewModel.observeCoinHistory().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserCoinHistoryModel.Data>>() {
            @Override
            public void onChanged(BaseResponse<UserCoinHistoryModel.Data> response) {
                Logger.i(TAG, "userCoinsHistorySuccess");
                try{
                    if(response != null && response.getData() != null){
                        if(mPageNumber == 1){
                            usercoinHistoryArrayList.clear();
                        }
//                        usercoinHistoryArrayList.addAll(response.getData().getHistory());
                        for(int i = 0; i<response.getData().getHistory().size();i++)
                        {
                            Logger.i(TAG, "index : "+i);
                            usercoinHistoryArrayList.add(response.getData().getHistory().get(i));
                        }
                        creditHistoryAdapter.notifyDataSetChanged();
                        try{
                            if(response.getData().getLastPage() != null
                                    && mPageNumber < response.getData().getLastPage()){
                                mNextPage = mPageNumber+1;
                            }else{
                                mNextPage = 0;
                            }
                        }catch (Exception ex){
                            CommonUtils.printStackTrace(ex);
                        }
                    }
                }catch (Exception ex){
                    CommonUtils.printStackTrace(ex);
                }
            }
        });
    }

    /*calling on setCreditHistoryAdapter*/
    private void setCreditHistoryAdapter() {
        Logger.i(TAG, "setCreditHistoryAdapter");
        try{
             creditHistoryAdapter = new CreditHistoryAdapter(getContext(),usercoinHistoryArrayList);
             linearLayoutManager = new LinearLayoutManager(getContext());
             rvCreditHistory.setLayoutManager(linearLayoutManager);
             endEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (mNextPage != 0) {
                        mPageNumber = mNextPage;
                        loadMore();
                    }
                }
             };

             rvCreditHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
                 @Override
                 public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                     super.onScrolled(recyclerView, dx, dy);


                     int totalItem = linearLayoutManager.getItemCount();
                     int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                     if (lastVisibleItem == totalItem - 1) {
                         if (mNextPage != 0) {
                             mPageNumber = mNextPage;
                             loadMore();
                         }
                     }
                 }
             });
             rvCreditHistory.setAdapter(creditHistoryAdapter);

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on loadMore*/
    private void loadMore() {
        Logger.i(TAG, "loadMore");
        try{
//            myAccountPresenter.getCoinHistory(mPageNumber);
            coinsViewModel.getCoinHistory(mPageNumber);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on userCoinsSuccess*/
    @Override
    public void userCoinsSuccess(UserCoinsResponse userCoinsResponse) {
        Logger.i(TAG, "userCoinsSuccess");
        try{
            if(userCoinsResponse != null){
                if(userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null){
                    tvWolooPoints.setText(""+userCoinsResponse.getData().getTotalCoins());
                }
                if(userCoinsResponse.getData() != null && userCoinsResponse.getData().getGiftCoins() != null){
                    tvWolooMoney.setText(""+userCoinsResponse.getData().getGiftCoins());
                }
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

/*
    calling on userCoinsHistorySuccess
*/
    @Override
    public void userCoinsHistorySuccess(UserCoinHistoryModel userCoinHistoryModel) {
        Logger.i(TAG, "userCoinsHistorySuccess");
        try{
            if(userCoinHistoryModel != null){
                if(mPageNumber == 1){
                    usercoinHistoryArrayList.clear();
                }
                usercoinHistoryArrayList.addAll(userCoinHistoryModel.getData().getHistory());
                creditHistoryAdapter.notifyDataSetChanged();
                try{
                    if(userCoinHistoryModel.getData() != null && userCoinHistoryModel.getData().getNext() != null && userCoinHistoryModel.getData().getNext() != null){
                        mNextPage = userCoinHistoryModel.getData().getNext();
                    }else{
                        mNextPage = 0;
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    


}