package in.woloo.www.dashboard.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;

import in.woloo.www.app.WolooApplication;
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.utils.Logger;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.netcore.android.Smartech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.home.fragments.HomeCategoryFragment;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.base.BaseFragment;
import in.woloo.www.v2.base.BaseViewModel;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.home.SearchPlacesAdapter;
import in.woloo.www.v2.home.model.NearbyWolooRequest;
import in.woloo.www.v2.home.model.PlaceAutocomplete;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.login.activity.LoginActivity;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.splash.model.LocaleRequest;
import in.woloo.www.vtion.model.ResultVtionSdkModel;
import in.woloo.www.vtion.utilities.EmailSenderClass;
import in.woloo.www.vtion.utilities.MessageList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HomeViewPresenterInterface {

    //private HomeViewModel homeViewModel;

    private View rootView;
    private boolean isSectionShow;
    public static int height;
    @BindView(R.id.tv_woloo)
    TextView tv_woloo;

    @BindView(R.id.frm_home_data)
    FrameLayout frm_home_data;

    @BindView(R.id.frm_dialog_sos)
    FrameLayout frm_dialog_sos;

    @BindView(R.id.frm_home_map)
    FrameLayout frm_home_map;

    @BindView(R.id.nsv)
    NestedScrollView nsv;

    @BindView(R.id.unselected_transport_mode_layout)
    LinearLayout unselected_transport_mode_layout;

    @BindView(R.id.selected_transport_mode_layout)
    ImageView selected_transport_mode_layout;

    @BindView(R.id.car_mode)
    ImageView car_mode;

    @BindView(R.id.bicycle_mode)
    ImageView bicycle_mode;

    @BindView(R.id.walking_mode)
    ImageView walking_mode;

    @BindView(R.id.cancel_mode_icon)
    ImageView cancel_mode_icon;

    @BindView(R.id.voucherExpireLL)
    LinearLayout voucherExpireLL;

    @Nullable
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @Nullable
    @BindView(R.id.search_option_layout)
    View searchOptionsLayout;

    @Nullable
    @BindView(R.id.search_auto_complete)
    AutoCompleteTextView searchAutoComplete;

    @BindView(R.id.iv_cancel)
    ImageView ivCancel;

    @BindView(R.id.cbWolooWithOffers)
    CheckBox cbWolooWithOffers;

    @BindView(R.id.cbOpenNow)
    CheckBox cbOpenNow;
    @BindView(R.id.cbBookmarkedWoloo)
    CheckBox cbBookmarkedWoloo;

    @BindView(R.id.contact_us)
    ImageView imgContactUs;
    private Fragment fragment_map;

    private GoogleMap map;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback callback;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    public Location lastKnownLocation;
    public double lastKnownLattitude;
    public double lastKnownLongitude;
    // not granted.
    private final LatLng defaultLocation = new LatLng(19.055229, 72.830829);

    private static final String TAG = HomeFragment.class.getSimpleName();
    private WolooDashboard wolooDashboard;
    private boolean locationPermissionGranted;

    private HomePresenter homePresenter;

    private HomeViewModel homeViewModel;
    private List<Marker> markerList;
    private List<NearByStoreResponse.Data> nearByStoreResponseList = new ArrayList<>();
    private ArrayList<NearByStoreResponse.Data> nearByStoreResponseListFromApi = new ArrayList<>();
    private ArrayList<NearByStoreResponse.Data> bookmarkedWolooList = new ArrayList<>();

    View mMapView;
    protected static SharedPreference mSharedPreference;
    public boolean isFromClickFlag = true;
    private boolean isExpire = false;
    private Handler handler;
    /*Calling on onCreateView*/

    final int DIALOGID = 2;

    ProgressDialog progressdialog;

    private boolean isGpsDialogShown = false;
    private boolean isLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 200;

    private static final int REQUEST_CALL_PERMISSION = 2001;

    private int range = 2;
    private String selectedTravelMode = "car";
    private PlacesClient placesClient;
    ArrayList<PlaceAutocomplete> placeSuggestionList = new ArrayList<>();
    ArrayAdapter<PlaceAutocomplete> placeAdapter = null;
    private boolean openNow = false;
    private boolean isSearched = false;
    public boolean showList = true;
    private boolean wolooWithOffers = false;

    public static int heightOfMapForMarker = 200;

    String hospitalAddress , hospitalName , hospitalContact ,hospitalImage ;
    String policeAddress , policeStationName , policeStationContact , policeStationImage;

    String fireAddress , fireName , fireContact , fireImage;

    String callOnNumber;

    int numberOfPhone = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fragment_map = getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mMapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
        ButterKnife.bind(this, rootView);
        String key = CommonUtils.googlemapapikey(getContext());
        Places.initialize(getContext(), key);
        placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
        try {
            initView();
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }

        return rootView;
    }

    /*Calling on onResume*/
    @Override
    public void onResume() {
        super.onResume();
        ((WolooDashboard) getActivity()).showToolbar();
        ((WolooDashboard) getActivity()).getUserProfile();
//        if (!isGpsDialogShown) {
        //checkGpsAndRequestLocation();
//        }

        try {
            if (WolooApplication.getInstance().getNearByWoloo() != null && WolooApplication.getInstance().getUpdatedLikeStatus() != -1) {
                NearByStoreResponse.Data selectedWoloo = WolooApplication.getInstance().getNearByWoloo();
                if (nearByStoreResponseListFromApi.contains(selectedWoloo)) {
                    nearByStoreResponseListFromApi.get(nearByStoreResponseListFromApi.indexOf(selectedWoloo))
                            .setIsLiked(WolooApplication.getInstance().getUpdatedLikeStatus());
                }
                bookmarkedWolooList = new ArrayList<>();
                for (int i = 0; i < nearByStoreResponseListFromApi.size(); i++) {
                    if (nearByStoreResponseListFromApi.get(i).getIsLiked() == 1) {
                        bookmarkedWolooList.add(nearByStoreResponseListFromApi.get(i));
                    }
                }
                if (cbBookmarkedWoloo.isChecked()) {
                    renderNearByWoloos(bookmarkedWolooList);
                } else {
                    renderNearByWoloos(nearByStoreResponseListFromApi);
                }
                WolooApplication.getInstance().setNearByWoloo(null);
                WolooApplication.getInstance().setUpdatedLikeStatus(-1);
            }
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }

        Utility.hideKeyboard(getActivity());
        try {
            if (mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "") == null) {
                ((WolooDashboard) getActivity()).showExpiryPopup();
                Log.d("aarati", "dialog not shown 1");
            }
            Log.d("aarati", "dialog  shown 1");
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    @Nullable
    @Override
    public BaseViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    /*Calling on initView*/
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        Logger.i(TAG, "initView");
        // Construct a FusedLocationProviderClient.
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(getContext());
        }
        String transport_mode = mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
        try {
            setTransportMode(Integer.parseInt(transport_mode), true);
        } catch (Exception e) {

        }
        homePresenter = new HomePresenter(getActivity(), this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        progressdialog = new ProgressDialog(getContext());
        progressdialog.setMessage("Fetching your current location");
        progressdialog.setCancelable(false);

        setProgressBar();
        setNetworkDetector();
        setLiveData();

        //homePresenter.getAuthConfig();
        LocaleRequest.Locale request = new LocaleRequest.Locale();
        request.setPackageName("in.woloo.www");
        request.setPlatform("android");

        LocaleRequest localeRequest = new LocaleRequest();
        localeRequest.setLocale(request);
        homeViewModel.getAppConfig(localeRequest);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        height = displayMetrics.heightPixels;
        tv_woloo.setVisibility(View.INVISIBLE);
        setWidthAndHeight(frm_home_map, height,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        nsv.setSmoothScrollingEnabled(false);
        tv_woloo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof WolooDashboard) {
                    ((WolooDashboard) getActivity()).hideAndShow(true);
                }
                hideAndShow(isSectionShow);
            }
        });
        selected_transport_mode_layout.setOnClickListener(v -> {
            Utility.hideKeyboard(getActivity());
            selected_transport_mode_layout.setVisibility(View.GONE);
            unselected_transport_mode_layout.setVisibility(View.VISIBLE);
        });

        cancel_mode_icon.setOnClickListener(v -> {
            selected_transport_mode_layout.setVisibility(View.VISIBLE);
            unselected_transport_mode_layout.setVisibility(View.GONE);
        });

        car_mode.setOnClickListener(v -> {
            setTransportMode(0, false);
        });

        bicycle_mode.setOnClickListener(v -> {
            setTransportMode(2, false);
        });

        walking_mode.setOnClickListener(v -> {
            setTransportMode(1, false);
        });

        voucherExpireLL.setOnTouchListener((v, event) -> true);


        imgContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AARATI" , "event");
               showSosDialog();
            }
        });

        //checkGpsAndRequestLocation();

        PlacesClient mPlacesClient;
        try {
            String key = CommonUtils.googlemapapikey(getContext());
            Places.initialize(getContext(), key);
//                Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
//            searchAutoComplete.addTextChangedListener(filterTextWatcher);

            mPlacesClient = Places.createClient(getContext());
            placeAdapter = new SearchPlacesAdapter(getContext(), R.layout.item_search_autocomplete, mPlacesClient);
            searchAutoComplete.setAdapter(placeAdapter);
            searchAutoComplete.setThreshold(1);
            searchOptionsLayout.setVisibility(View.GONE);

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        searchAutoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((WolooDashboard) getActivity()).removeWolooStoreInfo();
                return false;
            }
        });
        searchOptionsLayout.setVisibility(View.VISIBLE);
        searchAutoComplete.addTextChangedListener(filterTextWatcher);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PlaceAutocomplete place = (PlaceAutocomplete) adapterView.getItemAtPosition(pos);
                searchAutoComplete.setText(place.address);
                searchAutoComplete.setSelection(searchAutoComplete.length());
                cbOpenNow.setChecked(false);
                cbBookmarkedWoloo.setChecked(false);
                onPlaceClick(place);
//                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();
            }
        });

        cbWolooWithOffers.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (lastKnownLocation == null || TextUtils.isEmpty(etSearchText.getText().toString())) {
//                return;
//            }
            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
            wolooWithOffers = cbWolooWithOffers.isChecked();
//            //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
        });

        cbOpenNow.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (lastKnownLocation == null || TextUtils.isEmpty(etSearchText.getText().toString())) {
//                return;
//            }
            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1,wolooWithOffers,cbOpenNow.isChecked());
//            //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
        });

        cbBookmarkedWoloo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showList = true;
                ((WolooDashboard) getActivity()).removeWolooStoreInfo();
                if(isChecked){
                    //show bookmarked list
                    renderNearByWoloos(bookmarkedWolooList);
                }else{
                    //show normal list
                    renderNearByWoloos(nearByStoreResponseListFromApi);
                }
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAutoComplete.setText("");
            }
        });
    }

    private void onPlaceClick(PlaceAutocomplete item) {
        try {
            String placeId = String.valueOf(item.placeId);

            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                @Override
                public void onSuccess(FetchPlaceResponse response) {
                    Place place = response.getPlace();
                    try {
                        Utility.hideKeyboard(getActivity());
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                    lastKnownLattitude = place.getLatLng().latitude;
                    lastKnownLongitude = place.getLatLng().longitude;


                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(place.getLatLng())      // Sets the center of the map to Mountain View
                            .zoom(AppConstants.DEFAULT_ZOOM)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                    getNearByWoloos(lastKnownLattitude, lastKnownLongitude,
                            Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 0, wolooWithOffers, cbOpenNow.isChecked());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof ApiException) {
                        Toast.makeText(getContext(), exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception ex){}
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
//            if (!s.toString().equals("")) {
//                searchOptionsLayout.setVisibility(View.VISIBLE);
//            } else {
//                searchOptionsLayout.setVisibility(View.GONE);
//            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    };


    private void setLiveData() {
        homeViewModel.observeAppConfig().observe(getViewLifecycleOwner(), new Observer<AuthConfigResponse.Data>() {
            @Override
            public void onChanged(AuthConfigResponse.Data data) {
                SharedPrefSettings.Companion.getGetPreferences().storeIsLoggedIn(true);
                if (data != null) {
                    SharedPrefSettings.Companion.getGetPreferences().storeAuthConfig(data);
                }
            }
        });
        homeViewModel.observeNearByWoloo().observe(getViewLifecycleOwner(), arrayListBaseResponse -> {
            Utility.hideKeyboard(getActivity());
            if (arrayListBaseResponse != null) {
                nearByStoreResponseListFromApi = arrayListBaseResponse.getData();
            } else {
                nearByStoreResponseListFromApi = new ArrayList<>();
            }
            bookmarkedWolooList = new ArrayList<>();
            for (int i = 0; i < nearByStoreResponseListFromApi.size(); i++) {
                if(nearByStoreResponseListFromApi.get(i).getIsLiked() == 1){
                    bookmarkedWolooList.add(nearByStoreResponseListFromApi.get(i));
                }
            }
            if(cbBookmarkedWoloo.isChecked()){
                renderNearByWoloos(bookmarkedWolooList);
            }else if(nearByStoreResponseListFromApi.isEmpty()) {

                if(openNow) displayToast("No Woloos available at the moment.!!");

                Bundle bundle = new Bundle();
                HashMap<String,Object> payload = new HashMap<>();
                try {
                    if (lastKnownLocation != null) {
                        bundle.putString(AppConstants.LOCATION, "(" + lastKnownLattitude + "," + lastKnownLongitude + ")");
                        payload.put(AppConstants.LOCATION, "(" + lastKnownLattitude+ "," + lastKnownLongitude + ")");
                    }
                    if(isSearched){
                        bundle.putString(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
                        payload.put(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
                    }
                    isSearched = false;
                } catch (Exception ex) {

                }

                Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.NO_LOCATION_FOUND);
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.NO_LOCATION_FOUND);
                renderNearByWoloos(nearByStoreResponseListFromApi);
            }else {
                renderNearByWoloos(nearByStoreResponseListFromApi);
            }
        });
    }

    private void getNearByWoloos(double lat, double lng, int mode, int range, int isSearch) {
        NearbyWolooRequest request = new NearbyWolooRequest();
        request.setLat(lat);
        request.setLng(lng);
        request.setMode(mode);
        request.setRange(range);
        request.setPackageName("in.woloo.app");
        request.setSearch(isSearch);
        request.setShowAll(1);
        this.openNow = true;
        homeViewModel.getNearbyWoloos(request);
        this.isSearched = false;
                        fetchNearestSosStation(lat, lng , AppConstants.HOSPITAL);
                        fetchNearestSosStation(lat, lng , AppConstants.POLICESTATION);
                        fetchNearestSosStation(lat, lng , AppConstants.FIRESTATION);

    }

    private void getNearByWoloos(double lat, double lng, int mode, int range, int isSearch, boolean isOffer, boolean open){
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
        if(open){
            this.openNow = true;
            request.setShowAll(0);
        } else {
            this.openNow = false;
            request.setShowAll(1);
        }
        request.setPackageName("in.woloo.app");
        request.setSearch(0);
        homeViewModel.getNearbyWoloos(request);

        this.isSearched = true;
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
        bundle.putString(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        Utility.logFirebaseEvent(getContext(), bundle, AppConstants.SEARCH_WOLOO_EVENT);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
        payload.put(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        Utility.logNetcoreEvent(getContext(),payload,AppConstants.SEARCH_WOLOO_EVENT);
    }
    void setTransportMode(int mode_type, boolean firstTime) { // 0-> Car 1-> walking 2->bicycle
        selected_transport_mode_layout.setVisibility(View.VISIBLE);
        unselected_transport_mode_layout.setVisibility(View.GONE);
        int transportMode;
        Bundle bundle = new Bundle();
        HashMap<String, Object> payload = new HashMap<>();
        switch (mode_type) {
            case 0:
                transportMode = R.drawable.car_selected_transport_mode;
                car_mode.setImageResource(transportMode);
                bicycle_mode.setImageResource(R.drawable.bicycle_unselected_transport_mode);
                walking_mode.setImageResource(R.drawable.walking_unselected_transport_mode);
                bundle.putString(AppConstants.TRAVEL_MODE, "car");
                payload.put(AppConstants.TRAVEL_MODE, "car");
                selectedTravelMode = "car";
                break;
            case 1:
                transportMode = R.drawable.walking_selected_transport_mode;
                walking_mode.setImageResource(transportMode);
                bicycle_mode.setImageResource(R.drawable.bicycle_unselected_transport_mode);
                car_mode.setImageResource(R.drawable.car_unselected_transport_mode);
                bundle.putString(AppConstants.TRAVEL_MODE, "walking");
                payload.put(AppConstants.TRAVEL_MODE, "walking");
                selectedTravelMode = "walking";
                break;
            case 2:
                transportMode = R.drawable.bicycle_selected_transport_mode;
                bicycle_mode.setImageResource(transportMode);
                car_mode.setImageResource(R.drawable.car_unselected_transport_mode);
                walking_mode.setImageResource(R.drawable.walking_unselected_transport_mode);
                bundle.putString(AppConstants.TRAVEL_MODE, "bicycle");
                payload.put(AppConstants.TRAVEL_MODE, "bicycle");
                selectedTravelMode = "bicycle";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode_type);
        }
        if (!firstTime) {
            Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.TRAVEL_MODE_CLICK);
            Utility.logNetcoreEvent(getActivity(), payload, AppConstants.TRAVEL_MODE_CLICK);
        }
        selected_transport_mode_layout.setImageResource(transportMode);
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(getContext());
        }
        mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), String.valueOf(mode_type));
        if (!firstTime) {
            try {
                if (lastKnownLocation != null) {
                    lastKnownLattitude = lastKnownLocation.getLatitude();
                    lastKnownLongitude = lastKnownLocation.getLongitude();
                    Smartech.getInstance(new WeakReference<>(getContext())).setUserLocation(lastKnownLocation);
//                    getNearByWoloos(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), mode_type, range, 0);
                    getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1,wolooWithOffers,cbOpenNow.isChecked());
                }
                ((HomeCategoryFragment) fragment_map).pageNumber = 1;
            } catch (Exception e) {
                Logger.e("Exception: %s", e.getMessage(), e);
            }
        }
        ((WolooDashboard) getActivity()).removeWolooStoreInfo();

    }

    public boolean isWolooListVisible(){
        return  frm_home_data.getVisibility() == View.VISIBLE;
    }

    /*Calling on hideAndShow*/
    public void hideAndShow(boolean status) {
        Logger.i(TAG, "hideAndShow");
        if (status) {
            setWidthAndHeight(frm_home_map, height,false);
            this.isSectionShow = false;
            frm_home_data.setVisibility(View.GONE);
//            nsv.setSmoothScrollingEnabled(false);
            tv_woloo.setVisibility(View.INVISIBLE);
            frm_home_data.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_bottom));
//            tv_woloo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
//            nsv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
        } else {
            Bundle bundle = new Bundle();
            HashMap<String,Object> payload = new HashMap<>();
            try {
                bundle.putString(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                payload.put(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                SharedPrefSettings.Companion.getGetPreferences()
                        .storeLocationForNetcore("(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
            } catch (Exception ex) {

            }
            Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.HOST_NEAR_ME);
            Utility.logNetcoreEvent(getActivity(), payload, AppConstants.HOST_NEAR_ME);
            isFromClickFlag = false;
            //loadMore("1", true);
            setWidthAndHeight(frm_home_map, height / 2,false);
            frm_home_data.setVisibility(View.VISIBLE);
            this.isSectionShow = true;
//            nsv.setSmoothScrollingEnabled(true);
            tv_woloo.setVisibility(View.VISIBLE);
            frm_home_data.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
            handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    if (!isExpire) {
//                        if (HomeFragment.this.isVisible()) {
//                            if (getActivity() instanceof WolooDashboard) {
//                                ((WolooDashboard) getActivity()).hideAndShow(true);
//                            }
//                            hideAndShow(true);
//                        }
//                    }
//                }
//            }, 60000);
//            tv_woloo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_up));
//            nsv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_up));
        }
    }

    /*Calling on onMapReady*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Logger.i(TAG, "onMapReady");
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(true);
        moveCameraToDefaultLocation(false);
        checkGpsAndRequestLocation();
        map.setOnMarkerClickListener(HomeFragment.this);
    }

    private void moveCameraToDefaultLocation(boolean shouldShowNearbyLoos) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, AppConstants.DEFAULT_ZOOM);
        map.moveCamera(cameraUpdate);
        if (shouldShowNearbyLoos) {
            hideAndShow(true);
            getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
        }
    }

    /*Calling on getDeviceLocation*/
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try{
            if(progressdialog != null && !progressdialog.isShowing()){
                progressdialog.show();
            }
        }catch (Exception e){
            new CommonUtils().printStackTrace(e);
        }

        LocationRequest request = LocationRequest.create();
//        request.setInterval(2000);
//        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);

        Handler locationTimeOutHandler = new Handler(Looper.getMainLooper());
        Runnable expiryCallback = new Runnable() {
            @Override
            public void run() {
                try {
                    if (progressdialog != null && progressdialog.isShowing()) {
                        progressdialog.dismiss();
                    }
                    Toast.makeText(requireActivity(), "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    new CommonUtils().printStackTrace(e);
                }
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(progressdialog != null && progressdialog.isShowing()){
                    progressdialog.dismiss();
                }
                locationTimeOutHandler.removeCallbacks(expiryCallback);
                if (locationResult != null) {
                    if (mSharedPreference == null) {
                        mSharedPreference = new SharedPreference(getContext());
                    }
                    Location location = locationResult.getLastLocation();
                    if (location == null) {
                        lastKnownLocation = null;
                        goToLocation(defaultLocation);
                        getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")),range , 0);
                    } else {
//                        Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                        lastKnownLocation = location;
                        lastKnownLattitude = lastKnownLocation.getLatitude();
                        lastKnownLongitude = lastKnownLocation.getLongitude();
                        Smartech.getInstance(new WeakReference<>(getContext())).setUserLocation(location);
                        goToLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                        getNearByWoloos(location.getLatitude(), location.getLongitude(), Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
                    }
                } else {
                    lastKnownLocation = null;
                    goToLocation(defaultLocation);
                    getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(request, callback, Looper.getMainLooper());
        locationTimeOutHandler.postDelayed(expiryCallback, 25000);
    }

    public void checkGpsAndRequestLocation() {
        if (isLocationPermissionGranted()) {

            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setInterval(2000);
//            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(1);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true); //this is the key ingredient
            builder.addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireActivity())
                    .checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        getDeviceLocation();
                        updateLocationUI();
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    startIntentSenderForResult(resolvable.getResolution().getIntentSender(), DIALOGID, null, 0, 0, 0, null);
                                    isGpsDialogShown = true;
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                } catch (IntentSender.SendIntentException e) {
                                     CommonUtils.printStackTrace(e);
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;

                        }
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            });
        } else {
            getLocationPermission();
        }
    }

    public void getLocationPermission() {
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    /*calling isLocationPermissionGranted*/
    public boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsAndRequestLocation();
                    //showDialogForBackgroundLocationPermission();
                }else{
                    Toast.makeText(requireActivity(), "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
                    getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
                }
            }
            break;
            case BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can proceed with accessing the background location
                } else {
                    Toast.makeText(getContext(), "Unable to get your background location. Please allow from settings", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case REQUEST_CALL_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(numberOfPhone == 1)
                    {
                        callOnNumber = AppConstants.CALL_MOBILE +AppConstants.MOBILENUMBER;
                        startCall(callOnNumber);
                    } else if (numberOfPhone == 2) {
                        callOnNumber = hospitalContact;
                        startCall(callOnNumber);
                    } else if (numberOfPhone == 3) {
                        callOnNumber = policeStationContact;
                        startCall(callOnNumber);
                    } else if (numberOfPhone == 4) {
                        callOnNumber = fireContact;
                        startCall(callOnNumber);
                    }
                    else{
                        Toast.makeText(getActivity(), "Cannot make calls", Toast.LENGTH_SHORT).show();
                    }
                         numberOfPhone = 0;

                } else {
                    Toast.makeText(getActivity(), "Permission denied to make calls", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void goToLocation(LatLng defaultLocation) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, AppConstants.DEFAULT_ZOOM);
        map.moveCamera(cameraUpdate);
    }

    public void loadMore(String page, boolean showLoader) {
        try {
            try {
                ((HomeCategoryFragment) fragment_map).pageNumber = Integer.parseInt(page);
            }catch(Exception e){

            }
            if (mSharedPreference == null) {
                mSharedPreference = new SharedPreference(getContext());
            }
            if (lastKnownLocation != null)
                lastKnownLattitude = lastKnownLocation.getLatitude();
            lastKnownLongitude = lastKnownLocation.getLongitude();
                getNearByWoloos(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
        } catch (Exception e) {
            Logger.e("Exception: %s", e.getMessage(), e);
        }

    }

    /*Calling on updateLocationUI*/
    public void updateLocationUI() {
        Logger.i(TAG, "updateLocationUI");
        if (map == null) {
            return;
        }
        try {
            if (isLocationPermissionGranted()) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setZoomGesturesEnabled(true);
                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Utility.hideKeyboard(getActivity());
                        Location location = map.getMyLocation();
                        if(location != null) {
                            lastKnownLattitude = location.getLatitude();
                            lastKnownLongitude = location.getLongitude();
                            ((WolooDashboard) getActivity()).removeWolooStoreInfo();
                            searchAutoComplete.setText("");
                            cbOpenNow.setChecked(false);
                            cbBookmarkedWoloo.setChecked(false);
                            showList = true;
                            getNearByWoloos(location.getLatitude(), location.getLongitude(), Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
                        }
                        return true;
                    }
                });
//                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationClickListener() {
//                    @Override
//                    public void onMyLocationClick(@NonNull Location location) {
//                        Toast.makeText(wolooDashboard, "my location clicked", Toast.LENGTH_SHORT).show();
//                        getNearByWoloos(location.getLatitude(), location.getLongitude(), Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, "0");
//                    }
//                });

            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
//                if (getActivity() instanceof WolooDashboard) {
//                    ((WolooDashboard) getActivity()).getLocationPermission();
//                }
                map.setOnMyLocationClickListener(null);
            }
        } catch (SecurityException e) {
            Logger.e("Exception: %s", e.getMessage());
        }
    }


    /*Calling on setWidthAndHeight*/
    private void setWidthAndHeight(FrameLayout view, int height, boolean isHeightForFragment) {
        Logger.i(TAG, "setWidthAndHeight");
        if(!isHeightForFragment) {
            if (nearByStoreResponseListFromApi != null) {
                if (nearByStoreResponseListFromApi.size() >= 5) {
                    height = height - 250;
                } else {
                    height = height + 450;
                }
            }
        }
//        if (nearByStoreResponseList != null) {
//            if (nearByStoreResponseList.size() >= 5) {
//                height = height - 250;
//            } else if (nearByStoreResponseList.size() >= 4) {
//                height = height + 450;
//            } else if (nearByStoreResponseList.size() >= 3) {
//                height = height + 950;
//            } else if (nearByStoreResponseList.size() >= 2) {
//                height = height + 1350;
//            } else if (nearByStoreResponseList.size() >= 1) {
//                height = height + 450;
//            } else if (nearByStoreResponseList.size() == 0) {
//                height = height + 450;
//            }
//        }
        view.getLayoutParams().height = height;
        view.requestLayout();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null && callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }

    /*Calling on createMarker*/
    private Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID, int index) {
        Logger.i(TAG, "createMarker");
        map.getUiSettings().setZoomGesturesEnabled(true);
        int height = 110;
        int width = 90;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_store_mark_dest);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                //    .anchor(0.5f, 0.5f)
                //  .title(title)
                .zIndex(index)
                //  .snippet(snippet));
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    }

    /*Calling on onGetNearByStore*/
    @Override
    public void onGetNearByStore(NearByStoreResponse dataObject, NetworkAPICallModel networkAPICallModel) {
        Logger.i(TAG, "onGetNearByStore");
        try {
            markerList = new ArrayList<>();
            map.clear();
            nearByStoreResponseList = dataObject.getData();
            try {
                ((HomeCategoryFragment) fragment_map).setNearestWalk(nearByStoreResponseList,false,false,false);
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            for (int i = 0; i < nearByStoreResponseList.size(); i++) {
                NearByStoreResponse.Data data = nearByStoreResponseList.get(i);
                markerList.add(createMarker(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()), data.getTitle(), "", R.drawable.ic_store_mark_dest, i));
            }
            animateCameraToMarkerPosition(0);
            if (isFromClickFlag) {
                hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
                ((WolooDashboard) requireActivity()).hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
            } else
                isFromClickFlag = true;
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    private void renderNearByWoloos(ArrayList<NearByStoreResponse.Data> dataObject) {
        try {
            markerList = new ArrayList<>();
            map.clear();
            nearByStoreResponseList = dataObject;

            try {
                ((HomeCategoryFragment) fragment_map).setNearestWalk(nearByStoreResponseList, cbOpenNow.isChecked(), cbBookmarkedWoloo.isChecked(), false);
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            for (int i = 0; i < nearByStoreResponseList.size(); i++) {
                NearByStoreResponse.Data data = nearByStoreResponseList.get(i);
                markerList.add(createMarker(Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()), data.getTitle(), "", R.drawable.ic_store_mark_dest, i));
            }
            animateCameraToMarkerPosition(0);
            if (isFromClickFlag) {
                hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
                ((WolooDashboard) requireActivity()).hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
            } else
                isFromClickFlag = true;
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*Calling on authConfigSuccess*/
    @Override
    public void authConfigSuccess(AuthConfigResponse authConfigResponse) {
        Logger.i(TAG, "authConfigSuccess");
        try {
            if (mSharedPreference == null) {
                mSharedPreference = new SharedPreference(getContext());
            }
            mSharedPreference.setStoredBooleanPreference(getContext(), SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey(), true);
            String authConfigInfo = new Gson().toJson(authConfigResponse);
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.AUTH_CONFIG.getPreferenceKey(), authConfigInfo);
//            startActivity(new Intent(getContext(),WolooDashboard.class));
//            finish();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*Calling on animateCameraToMarkerPosition*/
    public void animateCameraToMarkerPosition(int position) {
        if (markerList != null && !markerList.isEmpty()) {
            Logger.i(TAG, "animateCameraToMarkerPosition");
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerList.get(position).getPosition())      // Sets the center of the map to Mountain View
                    .zoom(AppConstants.DEFAULT_ZOOM)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
        }
    }

    public void isShowingMarkerFragment(boolean isShow){
        if (isShow){
            setWidthAndHeight(frm_home_map, heightOfMapForMarker,true);
        }else{
            setWidthAndHeight(frm_home_map, height,false);
        }
    }

    /*Calling on onMarkerClick*/
    @Override
    public boolean onMarkerClick(Marker marker) {
        Logger.d(TAG, "onMarkerClick");
        CommonUtils.hideKeyboard(getActivity());
        if (isSectionShow) {
            if (getActivity() instanceof WolooDashboard) {
                ((WolooDashboard) getActivity()).hideAndShow(isSectionShow);
            }
            hideAndShow(isSectionShow);
        }
        setWidthAndHeight(frm_home_map, height/2,false);
        try {
            NearByStoreResponse.Data data = nearByStoreResponseList.get((int) marker.getZIndex());
            HashMap<String, Object> payload = new HashMap<>();
            payload.put(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
            payload.put(AppConstants.TRAVEL_MODE, selectedTravelMode);
            payload.put(AppConstants.HOST_CLICKED_ID,data.getId());
            payload.put(AppConstants.HOST_CLICKED_LOCATION, "(" + data.getLat() + "," + data.getLng() + ")");
            Utility.logNetcoreEvent(getContext(), payload, AppConstants.WOLOO_MARKER_CLICK);
        }
        catch (Exception e){

        }
        ((WolooDashboard) getActivity()).loadMarkerFragmentWithIndex((int) marker.getZIndex(), nearByStoreResponseList);
        return false;
    }

    public void setSubscriptionDetails(String expireDate, UserProfile userProfileResponse) {
        if (expireDate != null && !expireDate.equals("")) {

            Date stringtodate = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                stringtodate = format.parse(expireDate);
                Calendar c = Calendar.getInstance();
                c.setTime(stringtodate);
                c.add(Calendar.DATE, 1);
                stringtodate = c.getTime();
            } catch (ParseException e) {
                 CommonUtils.printStackTrace(e);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(stringtodate);
            calendar.add(Calendar.DATE, -7);
            Date newDate = calendar.getTime();
            TextView tv_expired = (TextView) rootView.findViewById(R.id.tv_expired);
            TextView tv_expiredate = (TextView) rootView.findViewById(R.id.tv_expiredate);
            LinearLayout llRenew = (LinearLayout) rootView.findViewById(R.id.ll_renew);
            llRenew.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    ((WolooDashboard) getActivity()).navigateToSubscriptionScreen(true);
                }
            });

            boolean isDismissable = false;

            if (new Date().after(stringtodate)) {
                tv_expired.setVisibility(View.VISIBLE);
                tv_expiredate.setVisibility(View.GONE);
                isExpire = true;
                isDismissable = false;
                voucherExpireLL.setVisibility(View.VISIBLE);
            } else if (new Date().after(newDate) && userProfileResponse.isFutureSubcriptionExist().equals("false")) {
                tv_expired.setVisibility(View.GONE);
                tv_expiredate.setVisibility(View.VISIBLE);
                tv_expiredate.setText("Your Voucher/Membership will expire on " + expireDate);
                isDismissable = true;
                if (mSharedPreference.getStoredBooleanPreference(getContext(), SharedPreferencesEnum.APP_LAUNCHED_ONE_TIME.getPreferenceKey(), false))
                    if(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), "") == null) {
                        showDialog(expireDate, isDismissable);
                        Log.d("aarati", "dialog not shown");
                    }
                Log.d("aarati", "dialog shown");
                mSharedPreference.setStoredBooleanPreference(getContext(), SharedPreferencesEnum.APP_LAUNCHED_ONE_TIME.getPreferenceKey(), false);
            }else{
                dismissVoucherExpireDialogs();
            }

        }
    }

    private Dialog dialog;

    private void showDialog(String expireDate, Boolean isDismissable) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
        dialog.setCanceledOnTouchOutside(isDismissable);
        dialog.setCancelable(isDismissable);
        dialog.setContentView(R.layout.dialog_subscription_expire);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        TextView tv_expired = (TextView) dialog.findViewById(R.id.tv_expired);
        TextView tv_expiredate = (TextView) dialog.findViewById(R.id.tv_expiredate);
        LinearLayout llRenew = (LinearLayout) dialog.findViewById(R.id.ll_renew);
        llRenew.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((WolooDashboard) getActivity()).navigateToSubscriptionScreen(true);
            }
        });
        tv_expired.setVisibility(View.GONE);
        tv_expiredate.setVisibility(View.VISIBLE);
        tv_expiredate.setText("Your Voucher/Membership will expire on " + expireDate);
        dialog.show();
    }

    private void showDialogForBackgroundLocationPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check if the user should be shown an explanation for granting the permission
            if (shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                // Show an explanation to the user as to why the permission is needed
                // You can customize the message based on your app's context

                new AlertDialog.Builder(getContext())
                        .setTitle("Background Location Permission")
                        .setMessage("This app requires background location permission for geofencing functionality.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Request the permission
                                requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                        BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // No explanation needed, request the permission directly
                requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DIALOGID:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getDeviceLocation();
                        updateLocationUI();
                        break;
                    case Activity.RESULT_CANCELED:
                        moveCameraToDefaultLocation(true);
                        break;
                }
                break;
        }
    }

    public void dismissVoucherExpireDialogs(){
        if(dialog != null && dialog.isShowing()){
            Logger.e("toast","dismiss dialog");
            dialog.dismiss();
        }
        voucherExpireLL.setVisibility(View.GONE);
        Logger.e("toast","dismiss");
    }

    private void makePhoneCall( String mobilenumber) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            // Permission already granted, proceed with making the call
            startCall(mobilenumber);
        }
    }

    private void startCall(String mobileNumber) {
        String phoneNumber =   mobileNumber; // Replace with your phone number
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Your device doesn't support phone calls.", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchNearestSosStation(double latitude, double longitude , String placeType) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latitude + "," + longitude + "&radius=5000&type=" + placeType + "&key=" + CommonUtils.googlemapapikey(getContext()) ;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    parseSos(jsonData , placeType);
                }
            }
        });
    }

    private void parseSos(String jsonData , String placeType) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject hospital = results.getJSONObject(0);
                String name = hospital.getString("name");
                String address = hospital.getString("vicinity");
                String placeId = hospital.getString("place_id");
                String photoReference = null;

                if (hospital.has("photos")) {
                    JSONArray photos = hospital.getJSONArray("photos");
                    photoReference = photos.getJSONObject(0).getString("photo_reference");
                }

                // Now fetch the details for phone number
                fetchSosDetails(placeId, name, address , placeType , photoReference);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchSosDetails(String placeId, String name, String address , String placeType , String photoReference) {
        String apiKey = CommonUtils.googlemapapikey(getContext()) ;
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                + placeId + "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    parseSosDetails(jsonData, name, address , placeType , photoReference);
                }
            }
        });
    }

    private void parseSosDetails(String jsonData, String name, String address , String placeType , String photoReference) {
        try {
            String apiKey = CommonUtils.googlemapapikey(getContext()) ;
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject result = jsonObject.getJSONObject("result");
            String phoneNumber = result.optString("formatted_phone_number", "N/A");

            String photoUrl = null;
            if (photoReference != null) {
                photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                        + photoReference + "&key=" + apiKey;
            }

            if(placeType == AppConstants.HOSPITAL)
            {
                hospitalName = name;
                hospitalAddress = address;
                hospitalContact = phoneNumber.trim();
                hospitalContact = replaceFirstCharacter(hospitalContact);
                hospitalImage = photoUrl;
                Log.d(placeType + " Info", hospitalContact  + " " + hospitalImage);
            } else if (placeType == AppConstants.POLICESTATION) {

                policeStationName = name;
                policeAddress = address;
                policeStationContact = phoneNumber.trim();
                policeStationContact = replaceFirstCharacter(policeStationContact);
                policeStationImage = photoUrl;
                Log.d(placeType + " Info", policeStationContact + " " + policeStationImage);

            } else if (placeType == AppConstants.FIRESTATION) {
                fireName = name;
                fireAddress = address;
                fireContact = phoneNumber.trim();
                fireContact = replaceFirstCharacter(fireContact);
                fireImage = photoUrl;
                Log.d(placeType + " Info", fireContact + " " + fireImage);
            }

            // Display or use the data as needed
            Log.d(placeType + " Info", "Name: " + name + ", Address: " + address + ", Phone: " + phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showSosDialog() {
        Logger.i(TAG, "showSosDialog");
        try {
            BottomSheetDialog  alertDialogBuilder = new BottomSheetDialog(getActivity());
            View child = getLayoutInflater().inflate(R.layout.dialog_sos_category, null);
            alertDialogBuilder.setContentView(child);
            FrameLayout bottomSheet = alertDialogBuilder.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                // Set the behavior for the bottom sheet
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setPeekHeight((int) (getResources().getDisplayMetrics().heightPixels * 0.6));
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // Ensure it starts collapsed
            }
            TextView tvWolooName = child.findViewById(R.id.woloo_name);
            TextView tvWolooAddres = child.findViewById(R.id.woloo_address);
            TextView tvWolooPhone = child.findViewById(R.id.woloo_phone);
            TextView tvHospitalName = child.findViewById(R.id.hospital_name);
            TextView tvHospitalAddress = child.findViewById(R.id.hospital_address);
            TextView tvHospitalPhone = child.findViewById(R.id.hospital_phone);
            TextView tvPoliceName = child.findViewById(R.id.police_name);
            TextView tvPoliceAddress = child.findViewById(R.id.police_address);
            TextView tvPolicePhone = child.findViewById(R.id.police_phone);
            TextView tvFireName = child.findViewById(R.id.fire_name);
            TextView tvFireAddress = child.findViewById(R.id.fire_address);
            TextView tvFirePhone = child.findViewById(R.id.fire_phone);
            ImageView imageViewClose = child.findViewById(R.id.close_dialog_sos);
            ImageView callWoloo = child.findViewById(R.id.call_woloo);
            ImageView callHospital = child.findViewById(R.id.call_hospital);
            ImageView callPolice = child.findViewById(R.id.call_police);
            ImageView callFire = child.findViewById(R.id.call_fire);
            ImageView wolooImg = child.findViewById(R.id.woloo_image);
            ImageView hospitalImg = child.findViewById(R.id.hospital_image);
            ImageView policeImg = child.findViewById(R.id.police_image);
            ImageView fireImg = child.findViewById(R.id.fire_image);
            LinearLayout wolooLayout = child.findViewById(R.id.woloo_support);
            LinearLayout hospitalLayout = child.findViewById(R.id.hospital_support);
            LinearLayout policeLayout = child.findViewById(R.id.police_support);
            LinearLayout fireLayout = child.findViewById(R.id.fire_support);


            tvWolooName.setText("Woloo Supoort");
            tvWolooAddres.setText("1706 Lodha Supremus,Tunga Village opp.MTNL, Powai Andheri (E),Mumbai");
            tvWolooPhone.setText((AppConstants.CALL_MOBILE +AppConstants.MOBILENUMBER).substring(4));
            if(hospitalName != null) {
                tvHospitalName.setText(hospitalName);
            }
            else
            {
                tvHospitalName.setText("No nearby hospital found within 5 kms.");
            }
            tvHospitalAddress.setText(hospitalAddress);
            if(hospitalContact != null) {
                tvHospitalPhone.setText(hospitalContact.substring(4));
                callHospital.setVisibility(View.VISIBLE);
            }
            else{
                tvHospitalPhone.setText("Call for ambulance on 102");
                callHospital.setVisibility(View.GONE);
            }
            if(policeStationName != null) {
                tvPoliceName.setText(policeStationName);
            }
            else
            {
                tvPoliceName.setText("No nearby police station found within 5 kms.");
            }
            tvPoliceAddress.setText(policeAddress);
            if(policeStationContact != null) {
                tvPolicePhone.setText(policeStationContact.substring(4));
                callPolice.setVisibility(View.VISIBLE);
            }
            else {
                tvPolicePhone.setText("Call for police on 100");
                callPolice.setVisibility(View.GONE);
            }
            if(fireName != null) {
                tvFireName.setText(fireName);
            }
            else
            {
                tvFireName.setText("No nearby fire station available within 5 kms.");
            }
            tvFireAddress.setText(fireAddress);
            if(fireContact != null) {
                tvFirePhone.setText(fireContact.substring(4));
                callFire.setVisibility(View.VISIBLE);
            }
            else {
                tvFirePhone.setText("Call for fire station on 101");
                callFire.setVisibility(View.GONE);
            }

           /* if (hospitalImage != null) {
                Glide.with(getContext())
                        .load(hospitalImage)
                        .placeholder(R.drawable.hospital_bg) // Optional placeholder image
                        .error(R.drawable.hospital_bg) // Optional error image
                        .into(hospitalImg);
            }

            if (policeStationImage != null) {
                Glide.with(getContext())
                        .load(policeStationImage)
                        .placeholder(R.drawable.police) // Optional placeholder image
                        .error(R.drawable.police) // Optional error image
                        .into(policeImg);
            }

            if (fireImage != null) {
                Glide.with(getContext())
                        .load(fireImage)
                        .placeholder(R.drawable.firestation) // Optional placeholder image
                        .error(R.drawable.firestation) // Optional error image
                        .into(fireImg);
            }
*/
            wolooImg.setOnClickListener(v -> {
                wolooLayout.setVisibility(View.VISIBLE);
                hospitalLayout.setVisibility(View.GONE);
                policeLayout.setVisibility(View.GONE);
                fireLayout.setVisibility(View.GONE);
            });

            hospitalImg.setOnClickListener(v -> {
                wolooLayout.setVisibility(View.GONE);
                hospitalLayout.setVisibility(View.VISIBLE);
                policeLayout.setVisibility(View.GONE);
                fireLayout.setVisibility(View.GONE);
            });

            policeImg.setOnClickListener(v -> {
                wolooLayout.setVisibility(View.GONE);
                hospitalLayout.setVisibility(View.GONE);
                policeLayout.setVisibility(View.VISIBLE);
                fireLayout.setVisibility(View.GONE);
            });

            fireImg.setOnClickListener(v -> {
                wolooLayout.setVisibility(View.GONE);
                hospitalLayout.setVisibility(View.GONE);
                policeLayout.setVisibility(View.GONE);
                fireLayout.setVisibility(View.VISIBLE);
            });

callWoloo.setOnClickListener(v -> {
    makePhoneCall(AppConstants.CALL_MOBILE + AppConstants.MOBILENUMBER);
    numberOfPhone = 1;
    alertDialogBuilder.dismiss();
});
            callHospital.setOnClickListener(v -> {
                makePhoneCall(hospitalContact);
                numberOfPhone = 2;
                alertDialogBuilder.dismiss();
            });
            callPolice.setOnClickListener(v -> {
                makePhoneCall(policeStationContact);
                numberOfPhone = 3;
                alertDialogBuilder.dismiss();
            });
            callFire.setOnClickListener(v -> {
                makePhoneCall(fireContact);
                numberOfPhone = 4;
                alertDialogBuilder.dismiss();
            });


            imageViewClose.setOnClickListener(v -> {
                alertDialogBuilder.dismiss();
            });

            alertDialogBuilder.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    public String replaceFirstCharacter(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.startsWith("0")) {
            return AppConstants.CALL_MOBILE + phoneNumber.substring(1);
        }
        else if (phoneNumber != null && phoneNumber.startsWith("91")) {
            return AppConstants.CALL_MOBILE + phoneNumber.substring(2);
        } else if (phoneNumber != null && phoneNumber.startsWith("+0")) {
            return AppConstants.CALL_MOBILE + phoneNumber.substring(2);
        } else if (phoneNumber != null && phoneNumber.startsWith("+91")) {
            return AppConstants.CALL_MOBILE + phoneNumber.substring(3); // Already in the correct format
        }
        else {
            return AppConstants.CALL_MOBILE + phoneNumber;
        }

    }


}