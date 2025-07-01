package in.woloo.www.home_details.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.utils.Logger;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.home_details.adapters.NearByWolooImageAdapter;
import in.woloo.www.home_details.adapters.PhotosAdapter;
import in.woloo.www.home_details.adapters.ReviewsAdapter;
import in.woloo.www.home_details.adapters.SearchedPhotosAdapter;
import in.woloo.www.home_details.models.LikeResponse;
import in.woloo.www.home_details.models.LikeStatusResponse;
import in.woloo.www.home_details.mvp.HomeDetailsPresenter;
import in.woloo.www.home_details.mvp.HomeDetailsView;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.mapdirection.DirectionsJSONParser;
import in.woloo.www.mapdirection.GpsTracker;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.review.models.ReviewListResponse;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EndlessRecyclerOnScrollListener;
import in.woloo.www.utils.EqualSpacingItemDecoration;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.data.remote.MessageResponse;
import in.woloo.www.v2.enroute.EnrouteDirectionActivity;
import in.woloo.www.v2.home.model.ReviewListRequest;
import in.woloo.www.v2.home.model.WolooEngagementRequest;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.splash.UserDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeDetailsFragment extends Fragment implements HomeDetailsView, OnMapReadyCallback {

    @BindView(R.id.chipsFacilityServices)
    ChipGroup chipsFacilityServices;

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tv_reviewslabel)
    TextView tv_reviewslabel;

    @BindView(R.id.tvDistance)
    TextView tvDistance;

    @BindView(R.id.tvRequiredTime)
    TextView tvRequiredTime;

    @BindView(R.id.tvRating)
    TextView tvRating;

    @BindView(R.id.tvOpeningHours)
    TextView tvOpeningHours;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvPremium)
    TextView tvPremium;

    @BindView(R.id.tv_direction)
    TextView tv_direction;

    @BindView(R.id.tv_like)
    TextView tv_like;

    @BindView(R.id.tv_start)
    TextView tv_start;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.iv_banner_recycle)
    RecyclerView iv_banner_recycle;

    @BindView(R.id.v_review)
    View v_review;

    @BindView(R.id.fl_map)
    FrameLayout fl_map;

    @BindView(R.id.tvPhotos)
    TextView tvPhotos;

    @BindView(R.id.vwPhotoDevider)
    View vwPhotoDevider;

    @BindView(R.id.tvShare)
    TextView tvShare;

    @BindView(R.id.ivTransportMode)
    ImageView ivTransportMode;

    @BindView(R.id.btnRedeemOffer)
    Button btnRedeemOffer;

    @BindView(R.id.cibil_image)
    ImageView cibilImage;

    @BindView(R.id.v_cibil)
    View v_cibil;

    @BindView(R.id.cibil_title)
    TextView cibilTitle;


    @BindView(R.id.btnAddReview)
    TextView buttonAddReview;

    @BindView(R.id.no_reviews)
    TextView no_reviews;

    private NearByStoreResponse.Data nearByWoloo;
    private HomeDetailsPresenter homeDetailsPresenter;
    private ReviewsAdapter reviewAdapter;

    private List<ReviewListResponse.Review> reviewList = new ArrayList<ReviewListResponse.Review>();
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private int mPageNumber;
    private int mNextPage;
    private int buttonClick = 0;
    private GoogleMap mMap;
    private Marker marker;
    public GpsTracker gps;
    LocationManager locationManager;
    String destlat;
    String destlong;
    private double curlat;
    private double curlon;
    private LatLng currentpos;
    ArrayList markerPoints = new ArrayList();
    private LatLng origin, dest;
    protected SharedPreference mSharedPreference;

    private boolean fromSearch = false;
    private SearchWolooResponse.Data.Woloo searchedWoloo;
    public static String TAG = HomeDetailsFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    WolooEngagementRequest wolooEngagementRequest = new WolooEngagementRequest();

    int previousLikeStatus = -1;
    int updatedLikeStatus = -1;


    public HomeDetailsFragment() {
        // Required empty public constructor
    }

    public static HomeDetailsFragment newInstance(boolean fromSearch) {
        HomeDetailsFragment fragment = new HomeDetailsFragment();
        Bundle args = new Bundle();
        args.putBoolean(AppConstants.FROM_SEARCH, fromSearch);
        fragment.setArguments(args);
        return fragment;
    }

    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromSearch = getArguments().getBoolean(AppConstants.FROM_SEARCH, false);
        }
        Logger.i(TAG, "onCreate");
    }

    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView");
        View rootViews = inflater.inflate(R.layout.fragment_home_details, container, false);
        ButterKnife.bind(this, rootViews);
        initViews();
        setLiveData();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GpsTracker(getContext());

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                2000,
//                10, locationListenerGPS);
        return rootViews;
    }

    /*calling drawMarker*/
    public void drawMarker(double curlat, double curlon) {
        Logger.i(TAG, "drawMarker");
        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_loaction);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(curlat, curlon))
//                .title("My Marker")
                        .icon(markerIcon).flat(true)
        );


        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(curlat, curlon)));
        animateMarker(marker, new LatLng(curlat, curlon), false);
        try {
            float bearing = (float) bearingBetweenLocations(origin, dest);
            marker.setRotation(bearing);
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling animateMarker*/
    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        Logger.i(TAG, "animateMarker");
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    /*calling bearingBetweenLocations*/
    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
        Logger.i(TAG, "bearingBetweenLocations");
        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    /*calling getMarkerIconFromDrawable*/
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Logger.i(TAG, "getMarkerIconFromDrawable");
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /*calling initViews*/
    public void initViews() {
        try {
            Logger.i(TAG, "initViews");
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            homeDetailsPresenter = new HomeDetailsPresenter(getContext(), HomeDetailsFragment.this);
            if (fromSearch) {
                searchedWoloo = WolooApplication.getInstance().getSearchedWoloo();
                setData();
                /*if (searchedWoloo != null && searchedWoloo.getOffer() != null) {
                    //setOffers();
                } else {
                    tvPhotos.setVisibility(View.GONE);
                    vwPhotoDevider.setVisibility(View.GONE);
                    rvPhotos.setVisibility(View.GONE);
                }*/
                mPageNumber = 1;
                getReviews();
                setReviews();

                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToMaps();
                    }
                });

                tv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle params = new Bundle();
                        params.putString(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                        Utility.logFirebaseEvent(getActivity(), params, AppConstants.LIKE_WOLOO_EVENT);

                        HashMap<String,Object> payload = new HashMap<>();
                        payload.put(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                        Utility.logNetcoreEvent(getActivity(),payload,AppConstants.LIKE_WOLOO_EVENT);

                        wolooEngagementRequest.setWolooId(searchedWoloo.getId().toString());
                        UserDetails userInfo = new CommonUtils().getUserInfo();
                        wolooEngagementRequest.setUserId(userInfo.getId().toString());

                        if (buttonClick == 0) {
                            buttonClick = 2;
                            try {
                                wolooEngagementRequest.setLike(1);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        } else if (buttonClick == 2) {
                            buttonClick = 0;
                            try {
                                wolooEngagementRequest.setLike(0);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOUNLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        }
                    }
                });
            } else {
                nearByWoloo = WolooApplication.getInstance().getNearByWoloo();
                previousLikeStatus = nearByWoloo.getIsLiked();
                setData();
                /*if (nearByWoloo != null && nearByWoloo.getOffer() != null) {
                    //setOffers();
                } else {
                    tvPhotos.setVisibility(View.GONE);
                    vwPhotoDevider.setVisibility(View.GONE);
                    rvPhotos.setVisibility(View.GONE);
                }*/
                mPageNumber = 1;
                getReviews();
                setReviews();

                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToMaps();
                    }
                });

                if (nearByWoloo.getIsLiked() == 1) {
                    buttonClick = 2;
                    setLike();
                } else {
                    buttonClick = 0;
                    setDislike();
                }
                tv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle params = new Bundle();
                        params.putString(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                        Utility.logFirebaseEvent(getActivity(), params, AppConstants.LIKE_WOLOO_EVENT);

                        HashMap<String,Object> payload = new HashMap<>();
                        payload.put(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                        Utility.logNetcoreEvent(getActivity(),payload,AppConstants.LIKE_WOLOO_EVENT);

                        wolooEngagementRequest.setWolooId(nearByWoloo.getId().toString());
                        UserDetails userInfo = new CommonUtils().getUserInfo();
                        wolooEngagementRequest.setUserId(userInfo.getId().toString());

                        if (buttonClick == 0) {
                            buttonClick = 2;
                            try {
                                updatedLikeStatus = 1;
                                wolooEngagementRequest.setLike(1);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
                                setLike();
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        } else if (buttonClick == 2) {
                            buttonClick = 0;
                            try {
                                updatedLikeStatus = 0;
                                wolooEngagementRequest.setLike(0);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
                                setDislike();
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOUNLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        }
                    }
                });
            }

            tvShare.setOnClickListener(v -> {
                Bundle params = new Bundle();
                params.putString(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                Utility.logFirebaseEvent(getActivity(), params, AppConstants.SHARE_WOLOO_EVENT);

                HashMap<String,Object> payload = new HashMap<>();
                payload.put(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.SHARE_WOLOO_EVENT);

                shareMessage();
            });

            if (mSharedPreference == null) {
                mSharedPreference = new SharedPreference(getContext());
            }
            String transport_mode = mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
            switch (transport_mode) {
                case "0":
                    ivTransportMode.setImageResource(R.drawable.ic_car);
                    break;
                case "1":
                    ivTransportMode.setImageResource(R.drawable.ic_walking_transport_mode);
                    break;
                case "2":
                    ivTransportMode.setImageResource(R.drawable.ic_bicycle_transport_mode);
                    break;
            }

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(TAG," WolooHomeFragment onPause");
        if(previousLikeStatus != updatedLikeStatus){
            WolooApplication.getInstance().setUpdatedLikeStatus(updatedLikeStatus);
        }

    }

    void setLiveData(){
        homeViewModel.observeWolooEngagement().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response!= null && response.getSuccess()){
//                    if(wolooEngagementRequest.getLike() == 0)
//                        setDislike();
//                    else
//                        setLike();
                } else {
                    if (!WolooApplication.getErrorMessage().isEmpty()) {
                        Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        homeViewModel.observeReviewList().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ReviewListResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<ReviewListResponse.Data> reviewListResponse) {
                if (reviewListResponse != null && reviewListResponse.getData() != null) {
                   try{
                       if (reviewListResponse.getData().getReview() != null && reviewListResponse.getData().getReviewCount() == 0) {
                           //Changed by Aarati 29 Aug 24
                           rvReviews.setVisibility(View.GONE);
                           tv_reviewslabel.setVisibility(View.VISIBLE);
                           v_review.setVisibility(View.VISIBLE);
                           no_reviews.setVisibility(View.VISIBLE);
                       } else {
                           v_review.setVisibility(View.VISIBLE);
                           rvReviews.setVisibility(View.VISIBLE);
                           tv_reviewslabel.setVisibility(View.VISIBLE);
                           no_reviews.setVisibility(View.GONE);
                           if (mPageNumber == 1) {
                               reviewList.clear();
                           }
                           reviewList.addAll(reviewListResponse.getData().getReview());
                           reviewAdapter.notifyDataSetChanged();
                           try {
                               if (reviewListResponse.getData() != null && reviewListResponse.getData().getNext() != null && reviewListResponse.getData().getNext() != 0) {
                                   mNextPage = reviewListResponse.getData().getNext();
                               } else {
                                   mNextPage = 0;
                               }
                           } catch (Exception ex) {
                                CommonUtils.printStackTrace(ex);
                           }
                       }
                   }catch(Exception e){
                        CommonUtils.printStackTrace(e);
                   }
                } else {
                    if (!WolooApplication.getErrorMessage().isEmpty()) {
                        Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        homeViewModel.observeRedeemOffer().observe(getViewLifecycleOwner(), new Observer<BaseResponse<MessageResponse>>() {
            @Override
            public void onChanged(BaseResponse<MessageResponse> response) {
                if(response!= null && response.getSuccess()){
                    onRedeemSuccess();
                } else {
                    if (!WolooApplication.getErrorMessage().isEmpty()) {
                        Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    /*calling showMapDirection*/
    public void showMapDirection() {
        try {
            Logger.i(TAG, "showMapDirection");
            Intent i = new Intent(getContext(), EnrouteDirectionActivity.class);
            if (fromSearch) {
                i.putExtra("destlat", searchedWoloo.getLat());
                i.putExtra("destlong", searchedWoloo.getLng());
                i.putExtra("wolooId", searchedWoloo.getId());
                i.putExtra("wolooName", searchedWoloo.getName());
                i.putExtra("wolooAddress", searchedWoloo.getAddress());
                i.putExtra("tag", "start");
            } else {
                i.putExtra("destlat", nearByWoloo.getLat());
                i.putExtra("destlong", nearByWoloo.getLng());
                i.putExtra("wolooId", nearByWoloo.getId());
                i.putExtra("wolooName", nearByWoloo.getName());
                i.putExtra("wolooAddress", nearByWoloo.getAddress());
                i.putExtra("tag", "start");
            }
            getContext().startActivity(i);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    /*calling getReviews*/
    private void getReviews() {
        try {
            Logger.i(TAG, "getReviews");
            ReviewListRequest request = new ReviewListRequest();
            request.setPageNumber(mPageNumber);
            if (fromSearch) {
                request.setWolooId(searchedWoloo.getId());
            } else {
                request.setWolooId(nearByWoloo.getId());
            }
            homeViewModel.getReviewList(request);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling setData*/
    private void setData() {
        try {
            Logger.i(TAG, "setData");
            if (fromSearch) {
                tvTitle.setText(searchedWoloo.getName());
                tvAddress.setText(searchedWoloo.getAddress());
                tvDistance.setText(searchedWoloo.getDistance());
                tvRequiredTime.setText("" + CommonUtils.getTimeForWolooStoreInfo(searchedWoloo.getDuration()));
                tvRating.setText("" + searchedWoloo.getUserRating());
               if(searchedWoloo.getCibilScoreImage() != ""){
                   Glide.with(getContext())
                           .load(searchedWoloo.getCibilScoreImage())
                           .into(cibilImage);
                   cibilImage.setVisibility(View.VISIBLE);
                   cibilTitle.setVisibility(View.VISIBLE);
                   v_cibil.setVisibility(View.VISIBLE);
               }else{
                   cibilImage.setVisibility(View.GONE);
                   cibilTitle.setVisibility(View.GONE);
                   v_cibil.setVisibility(View.GONE);
               }
                if (!TextUtils.isEmpty(searchedWoloo.getOpeningHours()))
                    tvOpeningHours.setText("" + getString(R.string.open_time) + " " + searchedWoloo.getOpeningHours());
                if (searchedWoloo.getIsPremium() == 1) {
                    tvPremium.setVisibility(View.VISIBLE);
                } else {
                    tvPremium.setVisibility(View.GONE);
                }
                setCategoryChips();
                tv_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //goToMaps();
                        if (searchedWoloo.getDistance().equals("-")) {
                            CommonUtils.showCustomDialog(getActivity(), "No route found for the transport mode. Please change mode and try again");
                        } else {
                            Bundle params = new Bundle();
                            params.putString(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                            Utility.logFirebaseEvent(getActivity(), params, AppConstants.DIRECTION_WOLOO_EVENT);

                            HashMap<String,Object> payload = new HashMap<>();
                            payload.put(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                            Utility.logNetcoreEvent(getActivity(),payload,AppConstants.DIRECTION_WOLOO_EVENT);
//                        fl_map.setVisibility(View.VISIBLE);
//                        iv_banner.setVisibility(View.GONE);

                            try {
                                Intent i = new Intent(getContext(), EnrouteDirectionActivity.class);
                                if (fromSearch) {
                                    i.putExtra("destlat", searchedWoloo.getLat());
                                    i.putExtra("destlong", searchedWoloo.getLng());
                                    i.putExtra("wolooId", searchedWoloo.getId());
                                    i.putExtra("wolooName", searchedWoloo.getName());
                                    i.putExtra("wolooAddress", searchedWoloo.getAddress());
                                    i.putExtra("tag", "start");
                                } else {
                                    i.putExtra("destlat", nearByWoloo.getLat());
                                    i.putExtra("destlong", nearByWoloo.getLng());
                                    i.putExtra("wolooId", nearByWoloo.getId());
                                    i.putExtra("wolooName", nearByWoloo.getName());
                                    i.putExtra("wolooAddress", nearByWoloo.getAddress());
                                    i.putExtra("tag", "start");
                                }
                                getContext().startActivity(i);
                            } catch (Exception e) {
                                 CommonUtils.printStackTrace(e);

                            }
                        }

                    }
                });
                /*if (!TextUtils.isEmpty(searchedWoloo.getImage())) {
                    String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + searchedWoloo.getImage();
                    ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
                } else {
                    String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                    ImageUtil.loadImage(getContext(), iv_banner, imgUrl);
                }*/
                if (nearByWoloo != null && nearByWoloo.getOffer() != null) {
                    setOffers();
                } else
                    setImageData(nearByWoloo.getImage());
            } else {
                tvTitle.setText(nearByWoloo.getName());
                tvAddress.setText(nearByWoloo.getAddress());
                tvDistance.setText(nearByWoloo.getDistance());
                tvRequiredTime.setText(nearByWoloo.getDuration());
                tvRating.setText("" + nearByWoloo.getUserRating());
                if(nearByWoloo.getCibilScoreImage() != ""){
                    Glide.with(getContext())
                            .load(nearByWoloo.getCibilScoreImage())
                            .into(cibilImage);
                    cibilImage.setVisibility(View.VISIBLE);
                    cibilTitle.setVisibility(View.VISIBLE);
                    v_cibil.setVisibility(View.VISIBLE);
                }else{
                    cibilImage.setVisibility(View.GONE);
                    cibilTitle.setVisibility(View.GONE);
                    v_cibil.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(nearByWoloo.getOpeningHours()))
                    tvOpeningHours.setText("" + getString(R.string.open_time) + " " + nearByWoloo.getOpeningHours());
                if (nearByWoloo.getIsPremium() == 1) {
                    tvPremium.setVisibility(View.VISIBLE);
                } else {
                    tvPremium.setVisibility(View.GONE);
                }
                // set offer button visibility
                if (nearByWoloo.getOffer() != null) {
                    btnRedeemOffer.setVisibility(View.VISIBLE);
                    btnRedeemOffer.setOnClickListener(view -> {
                        NearByStoreResponse.Data.Offer offer = nearByWoloo.getOffer();
//                        homeDetailsPresenter.redeemOffer(offer.getId());
                        homeViewModel.redeemOffer(offer.getId());
                    });
                } else {
                    btnRedeemOffer.setVisibility(View.GONE);
                }
                setCategoryChips();

                buttonAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity().getApplicationContext() , AddReviewActivity.class);
                        i.putExtra(AppConstants.WOLOO_ID , nearByWoloo.getId());
                        getContext().startActivity(i);
                    }
                });
                tv_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //goToMaps(); //
                        if (nearByWoloo.getDistance().equals("-")) {
                            CommonUtils.showCustomDialog(getActivity(), "No route found for the transport mode. Please change mode and try again");
                        } else {
                            Bundle params = new Bundle();
                            HashMap<String,Object> payload = new HashMap<>();
                            if (fromSearch) {
                                params.putString(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                                payload.put(AppConstants.WOLOO_NAME, String.valueOf(searchedWoloo.getId()));
                            } else {
                                params.putString(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                                payload.put(AppConstants.WOLOO_NAME, String.valueOf(nearByWoloo.getId()));
                            }
                            Utility.logFirebaseEvent(getActivity(), params, AppConstants.DIRECTION_WOLOO_EVENT);


                            Utility.logNetcoreEvent(getActivity(),payload,AppConstants.DIRECTION_WOLOO_EVENT);
//                        fl_map.setVisibility(View.VISIBLE);
//                        iv_banner.setVisibility(View.GONE);
                            Intent i = new Intent(getContext(), EnrouteDirectionActivity.class);
                            if (fromSearch) {
                                i.putExtra("destlat", searchedWoloo.getLat());
                                i.putExtra("destlong", searchedWoloo.getLng());
                                i.putExtra("wolooId", searchedWoloo.getId());
                                i.putExtra("wolooName", searchedWoloo.getName());
                                i.putExtra("wolooAddress", searchedWoloo.getAddress());
                                i.putExtra("tag", "start");
                            } else {
                                i.putExtra("destlat", nearByWoloo.getLat());
                                i.putExtra("destlong", nearByWoloo.getLng());
                                i.putExtra("wolooId", nearByWoloo.getId());
                                i.putExtra("wolooName", nearByWoloo.getName());
                                i.putExtra("wolooAddress", nearByWoloo.getAddress());
                                i.putExtra("tag", "start");
                            }
                            getContext().startActivity(i);
                        }


                    }
                });
                /*if (nearByWoloo.getImage().size()>0) {
                    String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + nearByWoloo.getImage().get(0);
                    ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
                } else {
                    String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                    //ImageUtil.loadImage(getContext(), iv_banner, imgUrl);
                }*/
                if (nearByWoloo != null && nearByWoloo.getOffer() != null) {
                    setOffers();
                } else
                    setImageData(nearByWoloo.getImage());
                /*String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                ImageUtil.loadImage(getContext(), iv_banner, imgUrl);*/
            }

            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void setImageData(List<String> image) {
        iv_banner.setVisibility(View.VISIBLE);
        String imgUrl;

        if(image != null && image.size() > 0){
            if(!image.get(0).contains(nearByWoloo.getBaseUrl())){
                imgUrl = nearByWoloo.getBaseUrl() + image.get(0);
            } else {
                imgUrl = image.get(0);
            }
        } else {
            imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
        }
        ImageUtil.loadImage(getContext(), iv_banner, imgUrl);

        if (image == null || image.size() == 0){
            image = new ArrayList<>();
            image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
        } else {
            for (int i = 0; i < image.size(); i++) {
                if (!image.get(i).contains(nearByWoloo.getBaseUrl())) {
                    image.set(i, nearByWoloo.getBaseUrl() + image.get(i));
                }
            }
        }
//            image = new ArrayList<>();
//        else
//            for (int i = 0; i < image.size(); i++) {
//                if (!image.get(i).contains(nearByWoloo.getBaseUrl())) {
//                    image.set(i, nearByWoloo.getBaseUrl() + image.get(i));
//                }
//            }
//        for (int i = image.size(); i < 5; i++) {
//            image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
//        }

        tvPhotos.setVisibility(View.VISIBLE);
        rvPhotos.setVisibility(View.VISIBLE);
        NearByWolooImageAdapter nearByWolooImageAdapter = new NearByWolooImageAdapter(getActivity(), image);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(mLayoutManager);
        rvPhotos.setAdapter(nearByWolooImageAdapter);
        rvPhotos.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvPhotos, false);
        rvPhotos.setHasFixedSize(true);
        rvPhotos.setItemViewCacheSize(20);
        rvPhotos.setDrawingCacheEnabled(true);
        rvPhotos.addItemDecoration(new EqualSpacingItemDecoration(dpToPx(2), EqualSpacingItemDecoration.HORIZONTAL)); // 16px. In practice, you'll want to use getDimensionPixelSize
        rvPhotos.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    public int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void goToMaps(){
        String lat = "",lng = "", mode = "";

        if (fromSearch) {
            lat = searchedWoloo.getLat();
            lng =  searchedWoloo.getLng();
        } else {
            lat = nearByWoloo.getLat();
            lng =  nearByWoloo.getLng();
        }
        String transport_mode = mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
        switch (transport_mode) {
            case "0":
                mode = "d";
                break;
            case "1":
                mode = "w";
                break;
            case "2":
                mode = "l"; //b for bycycler & l for two wheeler
                break;
        }
        // Create a Uri from an intent string. Use the result to create an Intent.
        String request = "google.navigation:q="+lat+","+lng+"&mode="+mode;
        Logger.e(TAG,request);
        Uri mapIntentUri = Uri.parse(request);
// Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    /*calling setReviews*/
    private void setReviews() {
        try {
            Logger.i(TAG, "setReviews");
            reviewAdapter = new ReviewsAdapter(getContext(), reviewList);
            rvReviews.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
            rvReviews.setLayoutManager(linearLayoutManager);
            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    mPageNumber = mNextPage;
                    if (mNextPage != 0) {
                        loadMore();
                    }
                }
            };
            rvReviews.addOnScrollListener(endlessRecyclerOnScrollListener);
            rvReviews.setAdapter(reviewAdapter);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling loadMore*/
    private void loadMore() {
        getReviews();
        Logger.i(TAG, "loadMore");
    }

    /*calling setOffers*/
    private void setOffers() {
        try {
            Logger.i(TAG, "setOffers");
            if (fromSearch) {
                /*List<SearchWolooResponse.Data.Offer> offerList = new ArrayList<SearchWolooResponse.Data.Offer>();
                offerList.add(searchedWoloo.getOffer());
                SearchedPhotosAdapter adapter = new SearchedPhotosAdapter(getContext(), offerList);
                rvPhotos.setHasFixedSize(true);
                rvPhotos.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                rvPhotos.setAdapter(adapter);*/
                String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + searchedWoloo.getOffer().getImage();
                ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
            } else {
                /*List<NearByStoreResponse.Data.Offer> offerList = new ArrayList<NearByStoreResponse.Data.Offer>();
                offerList.add(nearByWoloo.getOffer());
                PhotosAdapter adapter = new PhotosAdapter(getContext(), offerList);
                rvPhotos.setHasFixedSize(true);
                rvPhotos.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                rvPhotos.setAdapter(adapter);*/
                String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + nearByWoloo.getOffer().getImage();
                ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    /*calling setCategoryChips*/
    public void setCategoryChips() {
        Logger.i(TAG, "setCategoryChips");
        if (fromSearch) {
            if (searchedWoloo.getIsSafeSpace().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.safe_space));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_safe_chip));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            if (searchedWoloo.getIsCovidFree().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.covid_free));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_covid_free));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            if (searchedWoloo.getIsCleanAndHygiene().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.clean_and_hygiene));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_clean_hygienic_icon));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            ///////////////
            if (searchedWoloo.getIsWashroom() != null && searchedWoloo.getIsWashroom() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_washroom));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            } else {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_washroom));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_indian_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsFeedingRoom() != null && searchedWoloo.getIsFeedingRoom() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_feeding_room));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_mom_feeding_baby));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsSanitizerAvailable() != null && searchedWoloo.getIsSanitizerAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_sanitizer));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_hand_sanitizer));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsCoffeeAvailable() != null && searchedWoloo.getIsCoffeeAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_coffee));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_coffee));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsMakeupRoomAvailable() != null && searchedWoloo.getIsMakeupRoomAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_makeup));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_makeup));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsWheelchairAccessible() != null && searchedWoloo.getIsWheelchairAccessible() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_wheelchair));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_physically_disable));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getIsSanitaryPadsAvailable() != null && searchedWoloo.getIsSanitaryPadsAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_sanitary_pads));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_diaper));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (searchedWoloo.getSegregated() != null && searchedWoloo.getSegregated().equalsIgnoreCase("YES")) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_separate));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_separate_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            } else {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_unisex));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_unisex_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
           ////////////////
        } else {
            if (nearByWoloo.getIsSafeSpace().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.safe_space));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_safe_chip));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            if (nearByWoloo.getIsCovidFree().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.covid_free));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_covid_free));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            if (nearByWoloo.getIsCleanAndHygiene().equals(1)) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.clean_and_hygiene));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_clean_hygienic_icon));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            ///////////////
            if (nearByWoloo.getIsWashroom() != null && nearByWoloo.getIsWashroom() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_washroom));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            } else {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_washroom));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_indian_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getIsFeedingRoom() != null && nearByWoloo.getIsFeedingRoom() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_feeding_room));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_mom_feeding_baby));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getIsSanitizerAvailable() != null && nearByWoloo.getIsSanitizerAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_sanitizer));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_hand_sanitizer));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getIsCoffeeAvailable() != null && nearByWoloo.getIsCoffeeAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_coffee));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_coffee));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getIsMakeupRoomAvailable() != null && nearByWoloo.getIsMakeupRoomAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_makeup));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_makeup));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            if (nearByWoloo.getIsWheelchairAccessible() != null && nearByWoloo.getIsWheelchairAccessible() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_wheelchair));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_physically_disable));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getIsSanitaryPadsAvailable() != null && nearByWoloo.getIsSanitaryPadsAvailable() == 1) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_sanitary_pads));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_diaper));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }

            if (nearByWoloo.getSegregated() != null && nearByWoloo.getSegregated().equalsIgnoreCase("YES")) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_separate));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_unisex_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            } else {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.facility_services_chip_item, null, false);
                mChip.setText(getString(R.string.label_unisex));
                mChip.setChipIcon(getResources().getDrawable(R.drawable.ic_separate_toilet));
                mChip.setChipIconSize(50);
                mChip.setChipIconVisible(true);
                mChip.setChipIconTint(ContextCompat.getColorStateList(getContext(), R.color.white));
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                mChip.setPadding(paddingDp, 0, paddingDp, 0);
                chipsFacilityServices.addView(mChip);
            }
            ////////////////
        }
    }

    /*calling getReviewList*/
    @Override
    public void getReviewList(ReviewListResponse reviewListResponse) {
        try {
            Logger.i(TAG, "getReviewList");
            if (reviewListResponse != null) {
                if (reviewListResponse.getData() != null && reviewListResponse.getData().getReview() != null && reviewListResponse.getData().getReview().size() == 0) {
                    rvReviews.setVisibility(View.GONE);
                    tv_reviewslabel.setVisibility(View.GONE);
                    v_review.setVisibility(View.GONE);
                } else {
                    v_review.setVisibility(View.VISIBLE);
                    rvReviews.setVisibility(View.VISIBLE);
                    tv_reviewslabel.setVisibility(View.VISIBLE);
                    if (mPageNumber == 1) {
                        this.reviewList.clear();
                    }
                    this.reviewList.addAll(reviewListResponse.getData().getReview());
                    reviewAdapter.notifyDataSetChanged();
                    try {
                        if (reviewListResponse.getData() != null && reviewListResponse.getData().getNext() != null && reviewListResponse.getData().getNext() != 0) {
                            mNextPage = reviewListResponse.getData().getNext();
                        } else {
                            mNextPage = 0;
                        }
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    /*calling getLike_Unlike*/
    @Override
    public void getLike_Unlike(LikeResponse likeResponse, TextView tv_like) {

    }

    /*calling setLike*/
    public void setLike() {
        try {
            Logger.i(TAG, "setLike");
            tv_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.details_layer_list_liked, 0, 0, 0);
            tv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.yello_rectangle_shape_new));
            tv_like.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_five));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling setDislike*/
    public void setDislike() {
        try {
            Logger.i(TAG, "setDislike");
            tv_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.details_like_layer_list, 0, 0, 0);
            tv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.transperent_rectangle_shape));
            tv_like.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling likeStatusSuccess*/
    @Override
    public void likeStatusSuccess(LikeStatusResponse likeStatusResponse) {
    }

    @Override
    public void onRedeemSuccess() {
        Logger.i(TAG, "onRedeemSuccess");
        //Toast.makeText(requireActivity(), "Offer redeemed!", Toast.LENGTH_SHORT).show();
        showDialog("Offer redeemed! Please visit the Woloo host to redeem the offer.");
    }

    public void showDialog(String msg) {
        try {
            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_login_failure);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);
            btnCloseDialog.setText("Goto Offer cart");
            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(msg);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        Intent intent = new Intent(requireActivity(), WolooDashboard.class);
                        intent.setAction(AppConstants.SHOW_OFFER_CART);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        requireActivity().startActivity(intent);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    /*calling createMarker*/
    private Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID, int index) {
        Logger.i(TAG, "createMarker");
        int height = 110;
        int width = 90;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_store_mark_dest);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                //    .anchor(0.5f, 0.5f)
                //  .title(title)
                .zIndex(index)
                //  .snippet(snippet));
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    }

    /*calling onMapReady*/
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            Logger.e("onMapReady", "OnMapready");
            Logger.i(TAG, "onMapReady");

            gps = new GpsTracker(getContext());
            curlat = gps.getLatitude();
            curlon = gps.getLongitude();
            currentpos = new LatLng(curlat, curlon);
            if (fromSearch) {
                destlat = searchedWoloo.getLat();
                destlong = searchedWoloo.getLng();
            } else {
                destlat = nearByWoloo.getLat();
                destlong = nearByWoloo.getLng();
            }
//            try {
//                getAddress(curlat, curlon);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentpos)      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the zoom
                    //                .bearing(30)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpos, 10));


            mMap.setMyLocationEnabled(true);

            if (markerPoints.size() > 1) {
                markerPoints.clear();
                mMap.clear();
//                drawMarker(curlat, curlon);
            }

//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//
//                    if (markerPoints.size() >= 1) {
//                        markerPoints.clear();
//                        mMap.clear();
//                        drawMarker(curlat, curlon);
//                    }

            // Adding new item to the ArrayList


            Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_loaction);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

            marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(curlat, curlon))
//                    .title("My Marker")
                            .icon(markerIcon).flat(true)
            );


            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(curlat, curlon)));
//            animateMarker(marker, new LatLng(curlat, curlon), false);


            LatLng latLng1 = new LatLng(Double.parseDouble(destlat), Double.parseDouble(destlong));

            markerPoints.add(latLng1);

            // Creating MarkerOptions
//            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
//            options.position(latLng1);

            //                if (markerPoints.size() == 1) {
            //                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //                } else if (markerPoints.size() == 2) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //                }
//            markerPoints.add(createMarker(Double.parseDouble(destlat),Double.parseDouble(destlong),"My Destination","",R.drawable.ic_store_mark_dest,0));


            markerPoints.add(createMarker(Double.parseDouble(destlat), Double.parseDouble(destlong), "My Destination", "", R.drawable.ic_store_mark_dest, 0));


            // Add new marker to the Google Map Android API V2
//            mMap.addMarker(markerPoints);

            // Checks, whether start and end locations are captured
            //                if (markerPoints.size() >= 2) {
            LatLng currentpos = new LatLng(curlat, curlon);
            //                    LatLng origin = (LatLng) markerPoints.get(0);
            origin = currentpos;
            dest = (LatLng) markerPoints.get(0);

            try {
                float bearing = (float) bearingBetweenLocations(origin, dest);
                marker.setRotation(bearing);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }


            if (curlat != dest.latitude && curlon != dest.longitude)
                new getdistanceVal(curlat, curlon, dest.latitude, dest.longitude).execute();


            String url = getDirectionsUrl(origin, dest);
            Logger.e("url", "" + url);

            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);

//                }
//            });
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

    }


    /*calling downloadUrl*/
    private String downloadUrl(String strUrl) throws IOException {
        Logger.i(TAG, "downloadUrl");
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Logger.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Logger.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /*calling ParserTask for parsing direction api*/
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            Logger.i(TAG, "ParserTask");
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.parseColor("#1866D1"));
                    lineOptions.geodesic(true);

                }

                // Drawing polyline in the Google Map for the i-th route
                try {
                    mMap.addPolyline(lineOptions);
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                    // Toast.makeText(getActivity().getApplicationContext(), "Boundary Crossed!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /*calling getdistanceVal*/
    private class getdistanceVal extends AsyncTask<Void, Void, Void> {
        double sourcelat, sourcelong, destlat, destlong;

        public getdistanceVal(double curlat, double curlon, double latitude, double longitude) {
            this.sourcelat = curlat;
            this.sourcelong = curlon;
            this.destlat = latitude;
            this.destlong = longitude;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Logger.i(TAG, "getdistanceVal");
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
//            distanceroad = getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            Logger.e("distroad", distanceroad);
//            //this method will be running on UI thread
//            tv_distance.setText(distanceroad);
//            tv_time.setText(duration);
//            try {
//                if (maneuver.equalsIgnoreCase("turn-left")) {
//                    ivDirection.setImageResource(R.drawable.ic_turn_left);
//                    ivarrow.setImageResource(R.drawable.ic_turn_left);
//                } else if (maneuver.equalsIgnoreCase("turn-right")) {
//                    ivDirection.setImageResource(R.drawable.ic_arrow_right);
//                    ivarrow.setImageResource(R.drawable.ic_arrow_right);
//                } else {
//                    ivDirection.setImageResource(R.drawable.ic_keep_moving);
//                    ivarrow.setImageResource(R  .drawable.ic_keep_moving);
//                }
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            tv_nextturnname.setText(maneuver);
//            iv_shortdist.setText(firstdistance);
//            Toast.makeText(getActivity().getApplicationContext(),"Called Dist",Toast.LENGTH_SHORT).show();
//            pdLoading.dismiss();
        }

    }

    /*calling getDirectionsUrl*/
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        Logger.i(TAG, "getDirectionsUrl");
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(getContext());
        }
        String transport_mode = mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
        switch (transport_mode) {
            case "0":
                mode = "mode=driving";
                break;
            case "1":
                mode = "mode=walking";
                break;
            case "2":
                mode = "mode=bicycling";
                break;
        }
        // Building the parameters to the web service

        String key = "key=" + CommonUtils.googlemapapikey(getContext());
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /*calling shareMessage*/
    public void shareMessage() {
        try {
            Logger.i(TAG, "shareMessage");
            String message = "";
            if (fromSearch) {
                message = searchedWoloo.getName() + "\n" + searchedWoloo.getAddress() + "\n" + CommonUtils.authconfig_response(getContext()).getuRLS().getApp_share_url();
            } else {
                message = nearByWoloo.getName() + "\n" + nearByWoloo.getAddress() + "\n" + CommonUtils.authconfig_response(getContext()).getuRLS().getApp_share_url();
            }
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Woloo Share"));

//            Dialog dialog=new Dialog(getContext());
//            CommonUtils.calldeeplink(getContext(),dialog,"Woloo Share",message, CommonUtils.authconfig_response(getContext()).getData().getuRLS().getApp_share_url(),"https://woloo.page.link/share");

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


}