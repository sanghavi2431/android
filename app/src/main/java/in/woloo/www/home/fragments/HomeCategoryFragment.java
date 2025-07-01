package in.woloo.www.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.home.adapter.NearestWalkAdapter;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.search.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeCategoryFragment extends Fragment {

    @BindView(R.id.rvHomeCategory)
    RecyclerView rvHomeCategory;

    @BindView(R.id.tv_search_more)
    TextView tv_search_more;

    @BindView(R.id.tvNoWolooFound)
    LinearLayout tvNoWolooFound;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.pullToRefreshLayout)
    SwipeRefreshLayout pullToRefreshLayout;


    @BindView(R.id.bottomMargin)
    TextView bottomMargin;

    @BindView(R.id.tv_no_woloo_title)
    TextView noWolooTitle;

    @BindView(R.id.tv_no_woloo_search_text)
    TextView noWolooSearchText;


    public int pageNumber = 1;
    boolean stopLoading = false;


    private List<NearByStoreResponse.Data> nearByStoreResponseList = new ArrayList<NearByStoreResponse.Data>();
    private NearestWalkAdapter adapter;


    public HomeCategoryFragment() {
        // Required empty public constructor
    }

    public static String TAG = HomeCategoryFragment.class.getSimpleName();

    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
    }

    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_category, container, false);
        ButterKnife.bind(this, root);
        initViews();
        Logger.i(TAG, "onCreateView");
        return root;
    }

    /*calling initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            if(getParentFragment() instanceof HomeFragment){
                bottomMargin.setVisibility(View.VISIBLE);
            }else {
                bottomMargin.setVisibility(View.GONE);
                tv_search.setVisibility(View.GONE);
                tv_search_more.setVisibility(View.GONE);
            }
            setHomeCategories();
            tv_search.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("lat",((HomeFragment) getParentFragment()).lastKnownLattitude);
                intent.putExtra("lng",((HomeFragment) getParentFragment()).lastKnownLongitude);
                requireActivity().startActivity(intent);
            });

            tv_search_more.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("lat",((HomeFragment) getParentFragment()).lastKnownLattitude);
                intent.putExtra("lng",((HomeFragment) getParentFragment()).lastKnownLongitude);
                requireActivity().startActivity(intent);
            });

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling setHomeCategories*/
    private void setHomeCategories() {
        Logger.i(TAG, "setHomeCategories");
        try {
            adapter = new NearestWalkAdapter(getContext(), nearByStoreResponseList);
            rvHomeCategory.setHasFixedSize(true);
            rvHomeCategory.setLayoutManager(new LinearLayoutManager(getContext()));
            rvHomeCategory.setAdapter(adapter);
//            rvHomeCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
//                        return;
//                    }
//                    if (!recyclerView.canScrollVertically(1)) {
//                        if (!stopLoading) {
//                            pageNumber++;
//                            ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber),false);
//                        }
//                    }
//                }
//            });
            pullToRefreshLayout.setOnRefreshListener(() -> {
                pageNumber = 1;
                //tvNoWolooFound.setVisibility(View.GONE);
                //rvHomeCategory.setVisibility(View.GONE);
//                if(getParentFragment() instanceof HomeFragment) {
//                    ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber), true);
//                    ((HomeFragment) getParentFragment()).isFromClickFlag = false;
//                }
                pullToRefreshLayout.setRefreshing(false);
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling setNearestWalk*/
    public void setNearestWalk(List<NearByStoreResponse.Data> nearByStoreResponseList, boolean openNow, boolean bookmark, boolean isEnroute) {
        Logger.i(TAG, "setNearestWalk");
        Logger.e("initViews"," "+isEnroute);
        //if (pageNumber == 1)
            this.nearByStoreResponseList.clear();
        if (nearByStoreResponseList.size() > 0)
            this.nearByStoreResponseList.addAll(nearByStoreResponseList);
        adapter.notifyDataSetChanged();
        if (this.nearByStoreResponseList.size() == 0) {
            if(openNow){
                noWolooTitle.setText(getResources().getString(R.string.no_woloo_found_at_moment));
            }else if(bookmark){
                noWolooTitle.setText(getResources().getString(R.string.no_woloo_found_bookmark));
            }else{
                noWolooTitle.setText(getResources().getString(R.string.no_woloo_found));
            }
            tvNoWolooFound.setVisibility(View.VISIBLE);
            rvHomeCategory.setVisibility(View.GONE);
            pullToRefreshLayout.setVisibility(View.GONE);
            if(!(getParentFragment() instanceof HomeFragment)) {
                noWolooTitle.setText("Sorry, couldn’t find any Woloo Host On-Route");
                noWolooSearchText.setVisibility(View.GONE);
            }
            if(isEnroute){
                Logger.e("initViews",TAG+ getActivity().getLocalClassName());

                tv_search.setVisibility(View.GONE);
                tv_search_more.setVisibility(View.GONE);
            }
        } else {
            pullToRefreshLayout.setVisibility(View.VISIBLE);
            rvHomeCategory.setVisibility(View.VISIBLE);
            if(getParentFragment() instanceof HomeFragment) {
                tv_search_more.setVisibility(View.VISIBLE);
            }
            tvNoWolooFound.setVisibility(View.GONE);
            if(isEnroute){
                Logger.e("initViews",TAG+ getActivity().getLocalClassName());

                tv_search.setVisibility(View.GONE);
                tv_search_more.setVisibility(View.GONE);
            }
        }
        if (nearByStoreResponseList.size() == 0 && pageNumber != 1) {
            stopLoading = true;
        }
    }
}