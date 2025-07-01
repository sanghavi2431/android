package in.woloo.www.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.home.adapter.HomeCategoryAdapter;
import in.woloo.www.home.adapter.WolooStoreAdapter;
import in.woloo.www.home.adapter.WolooStoreImagesAdapter;
import in.woloo.www.home_details.fragments.HomeDetailsFragment;
import in.woloo.www.home_details.models.LikeResponse;
import in.woloo.www.home_details.models.LikeStatusResponse;
import in.woloo.www.home_details.mvp.HomeDetailsPresenter;
import in.woloo.www.home_details.mvp.HomeDetailsView;
import in.woloo.www.review.models.ReviewListResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.enroute.EnrouteDirectionActivity;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WolooStoreInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WolooStoreInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "WolooStoreInfoFragment";

    @BindView(R.id.rvWolooStoreImages)
    RecyclerView rvWolooStoreImages;

    @BindView(R.id.tv_woloo)
    TextView tv_woloo;

    private int index;
    private LinearLayoutManager layoutManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<NearByStoreResponse.Data> dataList;

    HomeViewModel homeViewModel ;
    WolooStoreAdapter adapter ;

    public WolooStoreInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WolooStoreInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WolooStoreInfoFragment newInstance(String param1, String param2) {
        WolooStoreInfoFragment fragment = new WolooStoreInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDataList(List<NearByStoreResponse.Data> dataList) {
        this.dataList = dataList;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Logger.i(TAG, "onCreate");
    }
    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_woloo_store_info, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        setLiveData();
        return rootView;
    }
    /*calling initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
             adapter = new WolooStoreAdapter(getContext(),dataList,homeViewModel);

            rvWolooStoreImages.setHasFixedSize(true);
            layoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
            rvWolooStoreImages.setLayoutManager(layoutManager);
            rvWolooStoreImages.setAdapter(adapter);

            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(rvWolooStoreImages);

            //rvWolooStoreImages.smoothScrollToPosition(index);
            rvWolooStoreImages.scrollToPosition(index);

            tv_woloo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() instanceof  WolooDashboard) {
                        ((WolooDashboard) getActivity()).removeWolooStoreInfo();
                    }else if(getActivity() instanceof EnrouteDirectionActivity){
                        ((EnrouteDirectionActivity) getActivity()).removeWolooStoreInfo();
                    }
                }
            });

            rvWolooStoreImages.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                        try {
                            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                            Logger.d(TAG,"firstVisiblePosition "+firstVisiblePosition);
                            if(getActivity() instanceof WolooDashboard) {
                                ((WolooDashboard) getActivity()).moveMarkerToIndex(firstVisiblePosition);
                            }else{
                                ((EnrouteDirectionActivity) getActivity()).animateCameraToMarkerPosition(firstVisiblePosition);
                            }
                        } catch (Exception e) {
                             CommonUtils.printStackTrace(e);
                        }
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }

    }


    void setLiveData(){
        homeViewModel.observeWolooEngagement().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response!= null && response.getSuccess()){
                    if(adapter != null){
                        dataList.get(adapter.getWolooSelectedIndex()).setIsLiked(adapter.getWolooEngagementRequest().getLike());
                        adapter.notifyItemChanged(adapter.getWolooSelectedIndex());
                    }
                }
            }
        });
    }
}