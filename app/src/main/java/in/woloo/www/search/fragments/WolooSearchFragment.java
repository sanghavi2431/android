package in.woloo.www.search.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.search.adapter.PlacesAutoCompleteAdapter;
import in.woloo.www.search.adapter.WolooSearchAdapter;
import in.woloo.www.search.mvp.WolooSearchPresenter;
import in.woloo.www.search.mvp.WolooSearchView;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EndlessRecyclerOnScrollListener;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.base.BaseFragment;
import in.woloo.www.v2.base.BaseViewModel;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.home.model.NearbyWolooRequest;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WolooSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WolooSearchFragment extends BaseFragment implements WolooSearchView, PlacesAutoCompleteAdapter.ClickListener {

    @BindView(R.id.rvSearchResults)
    RecyclerView rvSearchResults;

    @BindView(R.id.etSearchText)
    EditText etSearchText;

    @BindView(R.id.ll_nodatafound)
    LinearLayout ll_nodatafound;

    @BindView(R.id.rvGoogleNearbyPlaces)
    RecyclerView rvGoogleNearbyPlaces;

    @BindView(R.id.search_no_found_revard_Tv_layout)
    LinearLayout search_no_found_revard_Tv_layout;

    @BindView(R.id.woloo_points_Tv)
    TextView woloo_points;

    @BindView(R.id.shop_Tv)
    TextView shop_Tv;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.cbWolooWithOffers)
    CheckBox cbWolooWithOffers;

    @BindView(R.id.cbOpenNow)
    CheckBox cbOpenNow;

    @BindView(R.id.list_of_woloos_tv)
    TextView tvListOfWoloos;

    public static final String ARG_SHOW_OFFERS = "ARG_SHOW_OFFERS";
    private boolean isShowOffers;

    private WolooSearchPresenter mWolooSearchPresenter;

    private HomeViewModel homeViewModel;
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;

    private double lastKnownLattitude;
    private double lastKnownLongitude;


    private List<SearchWolooResponse.Data.Woloo> searchWolooList = new ArrayList<SearchWolooResponse.Data.Woloo>();
    private WolooSearchAdapter adapter;

    private int mPageNumber;
    private int mNextPage;
    private EndlessRecyclerOnScrollListener endEndlessRecyclerOnScrollListener;

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private List<NearByStoreResponse.Data> nearByStoreResponseList = new ArrayList<NearByStoreResponse.Data>();
    private InputMethodManager imm;

    public WolooSearchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public BaseViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public static WolooSearchFragment newInstance(boolean isShowOffers) {
        WolooSearchFragment fragment = new WolooSearchFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_OFFERS, isShowOffers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isShowOffers = getArguments().getBoolean(ARG_SHOW_OFFERS, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_woloo_search, container, false);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initViews() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mWolooSearchPresenter = new WolooSearchPresenter(getContext(), WolooSearchFragment.this);
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            getDeviceLocation();
            setProgressBar();
            setNetworkDetector();
            setSearchResults();
            setLiveData();
            shop_Tv.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), WolooDashboard.class).putExtra("goToShop", "").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                getActivity().finish();
            });
            ivBack.setOnClickListener(v -> {
                Utility.hideKeyboard(getActivity());
                ((SearchWolooActivity) getActivity()).onBackPressed();
            });

            etSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (etSearchText.hasFocus()) {

                        //et1.setCursorVisible(true);
                        etSearchText.setActivated(true);
                        etSearchText.setPressed(true);

                    }
                }
            });
            //etSearchText.requestFocus();
            etSearchText.setOnEditorActionListener((v, actionId, event) -> {
               /*
               if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(etSearchText.getText().toString().length() >= 3){
                        mPageNumber = 1;
                        loadMore();
                        return true;
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.search_validation),Toast.LENGTH_SHORT).show();
                        hideKeyboard(getActivity());
                        return false;
                    }
                }*/
                return false;
            });

            cbWolooWithOffers.setOnCheckedChangeListener((compoundButton, b) -> {
                if (lastKnownLocation == null || TextUtils.isEmpty(etSearchText.getText().toString())) {
                    return;
                }
                getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
                //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
            });

            cbOpenNow.setOnCheckedChangeListener((compoundButton, b) -> {
                if (lastKnownLocation == null || TextUtils.isEmpty(etSearchText.getText().toString())) {
                    return;
                }
                getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(),cbOpenNow.isChecked());
                //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
            });

            //Utility.showKeyboard(getActivity());
            if (!isShowOffers) {
                showKeyboard();
            } else {
                hideKeyboard();
            }

            try {
                String key = CommonUtils.googlemapapikey(getContext());
                Places.initialize(getContext(), key);
//                Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
                etSearchText.addTextChangedListener(filterTextWatcher);
                mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getContext());
                rvGoogleNearbyPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
                mAutoCompleteAdapter.setClickListener(this);
                rvGoogleNearbyPlaces.setAdapter(mAutoCompleteAdapter);
                mAutoCompleteAdapter.notifyDataSetChanged();
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void setLiveData() {
        homeViewModel.observeNearByWoloo().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ArrayList<NearByStoreResponse.Data>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<NearByStoreResponse.Data>> arrayListBaseResponse) {
                if(arrayListBaseResponse != null){
                    renderNearByWolooList(arrayListBaseResponse.getData(),"");
                }
            }
        });
    }

    private void getNearByWoloos(double lat, double lng, int mode, int range, int isSearch, boolean isOffer, boolean openNow){
        NearbyWolooRequest request = new NearbyWolooRequest();
        request.setLat(lat);
        request.setLng(lng);
        request.setMode(mode);
        request.setRange(range);
        if(isOffer){
            request.setOffer(1);
        } else {
            request.setOffer(0);
        }
        if(openNow){
            request.setShowAll(0);
        } else {
            request.setShowAll(1);
        }
        request.setPackageName("in.woloo.app");
        request.setSearch(isSearch);
        homeViewModel.getNearbyWoloos(request);
    }

    private void showKeyboard() {
        try {
            etSearchText.requestFocus();
            imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void hideKeyboard() {
        try {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                ll_nodatafound.setVisibility(View.GONE);
                if (rvGoogleNearbyPlaces.getVisibility() == View.GONE) {
                    rvGoogleNearbyPlaces.setVisibility(View.VISIBLE);
                }
            } else {
                if (rvGoogleNearbyPlaces.getVisibility() == View.VISIBLE) {
                    rvGoogleNearbyPlaces.setVisibility(View.GONE);
                    nearByStoreResponseList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };


    private void setSearchResults() {
        try {
            adapter = new WolooSearchAdapter(getContext(), nearByStoreResponseList, lastKnownLocation);
            rvSearchResults.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rvSearchResults.setLayoutManager(linearLayoutManager);
            endEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    mPageNumber = mNextPage;
                    if (mNextPage != 0) {
                        //loadMore();
                    }
                }
            };
            //rvSearchResults.addOnScrollListener(endEndlessRecyclerOnScrollListener);
            rvSearchResults.setAdapter(adapter);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void loadMore() {
        try {
            mWolooSearchPresenter.wolooSearchAPI(String.valueOf(lastKnownLocation.getLatitude()), String.valueOf(lastKnownLocation.getLongitude()), etSearchText.getText().toString(), mPageNumber);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            locationPermissionGranted = isLocationPermissionGranted();
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            isShowOffers = getArguments().getBoolean(ARG_SHOW_OFFERS, false);
                            if (isShowOffers) {
                                getNearByWoloos(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
                                //mWolooSearchPresenter.getNearByStore(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), "OFFER", true, cbWolooWithOffers.isChecked());
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Logger.e("Exception: %s", e.getMessage(), e);
        }
    }

    public boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void searchWolooSuccess(SearchWolooResponse searchWolooResponse, String keywords) {

        try {
            if (searchWolooResponse != null && !TextUtils.isEmpty(searchWolooResponse.getStatus()) && searchWolooResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                if (searchWolooResponse.getData().getWoloos().size() > 0) {
//                    if (mPageNumber == 1) {
//                    }
                    searchWolooList.clear();
                    searchWolooList.addAll(searchWolooResponse.getData().getWoloos());
                    adapter.notifyDataSetChanged();
                    ll_nodatafound.setVisibility(View.GONE);
                    rvSearchResults.setVisibility(View.VISIBLE);

                    if (searchWolooResponse.getData().getNext() != null && searchWolooResponse.getData().getNext() > 1) {
                        mNextPage = searchWolooResponse.getData().getNext();
                    }
                } else {
                    Utility.hideKeyboard(getActivity());
                    searchWolooList.clear();
                    adapter.notifyDataSetChanged();
//                   Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.search_error),Toast.LENGTH_SHORT).show();

                    ll_nodatafound.setVisibility(View.VISIBLE);
                    rvSearchResults.setVisibility(View.GONE);

                }
            } else {
//               Toast.makeText(getActivity().getApplicationContext(),searchWolooResponse.getMessage(),Toast.LENGTH_SHORT).show();
                Utility.hideKeyboard(getActivity());
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onGetNearByStore(NearByStoreResponse data, NetworkAPICallModel networkAPICallModel, String keywords) {
        try {
            Utility.hideKeyboard(getActivity());
            if (data.getData().size() > 0) {
                nearByStoreResponseList.clear();
                rvGoogleNearbyPlaces.setVisibility(View.GONE);
                ll_nodatafound.setVisibility(View.GONE);
                nearByStoreResponseList.addAll(data.getData());
                adapter.lastKnownLocation = lastKnownLocation;
                adapter.keyword = keywords;
                adapter.notifyDataSetChanged();
            } else {
                if (data.getMessage().contains("Woloo Points")) {
                    search_no_found_revard_Tv_layout.setVisibility(View.VISIBLE);
                    woloo_points.setText(data.getMessage().split(" ")[0]);
                } else {
                    search_no_found_revard_Tv_layout.setVisibility(View.GONE);
                }
                rvGoogleNearbyPlaces.setVisibility(View.GONE);
                Utility.hideKeyboard(getActivity());
                ll_nodatafound.setVisibility(View.VISIBLE);
                nearByStoreResponseList.clear();
                adapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void renderNearByWolooList(ArrayList<NearByStoreResponse.Data> data, String keywords){
        try {
            Utility.hideKeyboard(getActivity());
            if (data.size() > 0) {
                nearByStoreResponseList.clear();
                rvGoogleNearbyPlaces.setVisibility(View.GONE);
                ll_nodatafound.setVisibility(View.GONE);
                nearByStoreResponseList.addAll(data);
                adapter.lastKnownLocation = lastKnownLocation;
                adapter.keyword = keywords;
                adapter.notifyDataSetChanged();

                tvListOfWoloos.setVisibility(View.VISIBLE);
            } else {
//                if (data.getMessage().contains("Woloo Points")) {
//                    search_no_found_revard_Tv_layout.setVisibility(View.VISIBLE);
//                    woloo_points.setText(data.getMessage().split(" ")[0]);
//                } else {
//                    search_no_found_revard_Tv_layout.setVisibility(View.GONE);
//                }
                rvGoogleNearbyPlaces.setVisibility(View.GONE);
                Utility.hideKeyboard(getActivity());
                ll_nodatafound.setVisibility(View.VISIBLE);
                nearByStoreResponseList.clear();
                adapter.notifyDataSetChanged();

                tvListOfWoloos.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void click(Place place) {
        try {
            //Toast.makeText(getActivity().getApplicationContext(), place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            try {
                Utility.hideKeyboard(getActivity());
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            lastKnownLattitude = place.getLatLng().latitude;
            lastKnownLongitude = place.getLatLng().longitude;
            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
            //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
            etSearchText.setText(place.getAddress());
            etSearchText.setSelection(etSearchText.getText().length());
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

}