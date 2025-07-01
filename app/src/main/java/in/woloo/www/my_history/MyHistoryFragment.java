package in.woloo.www.my_history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.my_account.MyAccountFragment;
import in.woloo.www.my_account.mvp.MyAccountPresenter;
import in.woloo.www.my_history.adapter.MyHistoryAdapter;
import in.woloo.www.my_history.adapter.MyOfferAdapter;
import in.woloo.www.my_history.model.MyHistoryResponse;
import in.woloo.www.my_history.mvp.MyHistoryPresenter;
import in.woloo.www.my_history.mvp.MyHistoryView;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.trendingblog.fragments.TrendBlogFragment;
import in.woloo.www.utils.EmptyRecyclerView;
import in.woloo.www.utils.EndlessRecyclerOnScrollListener;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.giftcard.viewmodel.HistoryViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyHistoryFragment extends Fragment {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvMyHistory)
    EmptyRecyclerView rvMyHistory;

    @BindView(R.id.ll_nodatafound)
    LinearLayout ll_nodatafound;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean isFromOffer;

    private HistoryViewModel historyViewModel;
    private int mPageNumber = 1;
    private int mNextPage;
    private List<MyHistoryResponse.History> historyList = new ArrayList<MyHistoryResponse.History>();
    private ArrayList<NearByStoreResponse.Data> myOffersList = new ArrayList();
    private MyHistoryAdapter myHistoryAdapter;
    private MyOfferAdapter myOfferAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;


    public MyHistoryFragment() {
        // Required empty public constructor
    }

    public static String TAG = MyHistoryFragment.class.getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyHistoryFragment newInstance(String param1, String param2, Boolean isFromOffer) {
        MyHistoryFragment fragment = new MyHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean("isFromOffer", isFromOffer);
        fragment.setArguments(args);
        return fragment;
    }

    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            isFromOffer = getArguments().getBoolean("isFromOffer");
        }
        Logger.i(TAG, "onCreate");
    }

    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_history, container, false);
        ButterKnife.bind(this, rootView);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        initView();
        setLiveData();
        Logger.i(TAG, "onCreateView");
        return rootView;
    }

    /*calling on initView*/
    private void initView() {
        try {
            Logger.i(TAG, "initView");
            ll_nodatafound.setVisibility(View.GONE);
            rvMyHistory.setVisibility(View.VISIBLE);
            setAdapter(isFromOffer);
            if (isFromOffer) {
                tvTitle.setText("Offer Cart");
                historyViewModel.getMyOffers();
            } else {
                tvTitle.setText(getResources().getString(R.string.my_history));
                historyViewModel.getRewardHistory(mPageNumber);
            }
            ivBack.setOnClickListener(v -> {
                try {
                    //getActivity().onBackPressed();
                    FragmentManager fm = requireActivity().getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        ((WolooDashboard) requireActivity()).loadFragment(new TrendBlogFragment(), TrendBlogFragment.TAG);
                        ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_dash_home));
                    }
                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        historyViewModel.observeRewardHistory().observe(getViewLifecycleOwner(), new Observer<BaseResponse<MyHistoryResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<MyHistoryResponse.Data> myHistoryResponse) {
                //Logger.d("Aarati" , "history is " + myHistoryResponse.toString());
                ll_nodatafound.setVisibility(View.GONE);
                rvMyHistory.setVisibility(View.VISIBLE);
                try {
                    Logger.d("Aarati" , "history is " + myHistoryResponse.getData().getHistory().size());
                    if (mPageNumber == 1) {
                        historyList.clear();
                    }
                    if (myHistoryResponse != null && myHistoryResponse.getData() != null && myHistoryResponse.getData().getHistory() != null && myHistoryResponse.getData().getHistory().size() > 0) {
                        historyList.addAll(myHistoryResponse.getData().getHistory());
                        myHistoryAdapter.notifyDataSetChanged();
                        Logger.d("Aarati" , "history is " + historyList.size());

                        if (myHistoryResponse.getData().getNext() != null && myHistoryResponse.getData().getNext() != null) {
                            mNextPage = myHistoryResponse.getData().getNext();
                        } else {
                            mNextPage = 0;
                        }
                    } else {
                        WolooApplication.setErrorMessage("");
                        if (mPageNumber == 1) {
                            showNoHistory();
                        }
                    }

                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }


            }
        });

        historyViewModel.observeMyOffers().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ArrayList<NearByStoreResponse.Data>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<NearByStoreResponse.Data>> response) {
                ll_nodatafound.setVisibility(View.GONE);
                rvMyHistory.setVisibility(View.VISIBLE);
                try {
                    myOffersList.clear();
                    if (response != null && response.getData() != null && response.getData().size() > 0) {
                        myOffersList.addAll(response.getData());
                        myOfferAdapter.notifyDataSetChanged();

                    } else {
                        WolooApplication.setErrorMessage("");
                        showNoHistory();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setAdapter(boolean isFromOffer) {
        try {
            if(isFromOffer){
                myOfferAdapter = new MyOfferAdapter(getContext(), myOffersList);
                rvMyHistory.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rvMyHistory.setLayoutManager(linearLayoutManager);
                rvMyHistory.setAdapter(myOfferAdapter);
            }
            else {
                myHistoryAdapter = new MyHistoryAdapter(getContext(), historyList);
                rvMyHistory.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rvMyHistory.setLayoutManager(linearLayoutManager);
                endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {
                        mPageNumber = mNextPage;
                        if (mNextPage != 0) {
                            historyViewModel.getRewardHistory(mPageNumber);
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

    /*calling on showNoHistory*/
    public void showNoHistory() {

        Logger.i(TAG, "showNoHistory");
        Toast.makeText(getActivity().getApplicationContext(), "No History Available", Toast.LENGTH_SHORT).show();
        ll_nodatafound.setVisibility(View.VISIBLE);
        rvMyHistory.setVisibility(View.GONE);
    }
}