package in.woloo.www.application_kotlin.presentation;

import static in.woloo.www.utils.Utility.logFirebaseEvent;
import static in.woloo.www.utils.Utility.logNetcoreEvent;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.MapStyleOptions;
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
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.application_kotlin.adapters.NearByRateSearchAdapter;
import in.woloo.www.application_kotlin.adapters.NearByWolooSearchAdapter;
import in.woloo.www.application_kotlin.adapters.PagerAdapterHorizontal;
import in.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter;
import in.woloo.www.application_kotlin.api_classes.BaseResponse;
import in.woloo.www.application_kotlin.api_classes.NetworkAPICallModel;
import in.woloo.www.application_kotlin.database.SharedPrefSettings;
import in.woloo.www.application_kotlin.database.SharedPreference;
import in.woloo.www.application_kotlin.interfaces.HomeViewPresenterInterface;
import in.woloo.www.application_kotlin.model.CreditCoinsRequest;
import in.woloo.www.application_kotlin.model.lists_models.LocaleRequest;
import in.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete;
import in.woloo.www.application_kotlin.model.server_request.LocationForRate;
import in.woloo.www.application_kotlin.model.server_request.NearByStoreResultsWrapper;
import in.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest;
import in.woloo.www.application_kotlin.model.server_request.Pagination;
import in.woloo.www.application_kotlin.model.server_request.SearchWolooRequest;
import in.woloo.www.application_kotlin.model.server_response.AuthConfigResponse;
import in.woloo.www.application_kotlin.model.server_response.NearByStoreResponse;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.BookmarkListActivity;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.QRcodeScannerActivity;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.SerachLayoutForOnRouteFragment;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.application_kotlin.presentation.fragments.loo_discovery.HomeCategoryFragment;
import in.woloo.www.application_kotlin.presentation.fragments.loo_discovery.SharedSearchViewModel;
import in.woloo.www.application_kotlin.utilities.DateToLocalDateConverter;
import in.woloo.www.application_kotlin.view_models.BaseViewModel;
import in.woloo.www.application_kotlin.view_models.HomeViewModel;
import in.woloo.www.application_kotlin.view_models.ScrollEnrouteListViewModel;
import in.woloo.www.application_kotlin.view_models.ScrollListViewModel;
import in.woloo.www.application_kotlin.view_models.WolooViewModel;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.application_kotlin.base_old.BaseFragment;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HomeViewPresenterInterface{

    //private HomeViewModel homeViewModel;

    private View rootView;
    private boolean isSectionShow;
  //  public static int height;

    @BindView(R.id.show_extra_parameters)
    ImageView showExtraParams;

    @BindView(R.id.extra_parameters_layout)
    LinearLayout extraParamsLayout;

    @BindView(R.id.imgScanQR)
    public ImageView imgScanQR;

    @BindView(R.id.imgEnrouteClick)
    public ImageView imgEnrouteClick;


   /* @BindView(R.id.imgSearch)
    public  ImageView imgSearch;
*/
    @BindView(R.id.frm_home_data)
    FrameLayout frm_home_data;

    @BindView(R.id.current_location_button)
    ImageView currentLocationButton;

    @BindView(R.id.search_auto_complete_destination_layout)
    LinearLayout destinationSearchLayout;

    @BindView(R.id.frm_dialog_sos)
    FrameLayout frm_dialog_sos;

    @BindView(R.id.frm_home_map)
    FrameLayout frm_home_map;

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
    TextView searchAutoComplete;


    @BindView(R.id.clear_image)
    ImageView clearSearchText;
    @BindView(R.id.cbWolooWithOffers)
    CheckBox cbWolooWithOffers;

    @BindView(R.id.cbOpenNow)
    CheckBox cbOpenNow;
    @BindView(R.id.cbBookmarkedWoloo)
    CheckBox cbBookmarkedWoloo;

    @BindView(R.id.contact_us)
    ImageView imgContactUs;


    @BindView(R.id.current_transport_mode)
    ImageView current_transport_mode;

    @BindView(R.id.byCarShow)
    ImageView carMode;

    @BindView(R.id.byBikeShow)
    ImageView bikeMode;

    @BindView(R.id.byWalkShow)
    ImageView walkMode;

    @BindView(R.id.transport_mode_layout)
    LinearLayout transport_mode_layout;

    @BindView(R.id.show_bookmark_list)
    ImageView bookmarkListView;

    @BindView(R.id.search_image)
    ImageView searchImageIcon;

  /*  @BindView(R.id.overlay_layout)
    FrameLayout overlay_layout;*/

    private Fragment fragment_map;
    Boolean isOverLay = false;



    public GoogleMap map;
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

    private HomeViewModel homeViewModel;

    private String selectedTravelMode = "";

    public List<Marker> markerList;
    private Marker selectedMarker;
    private Marker previousMarker = null;
    public List<NearByStoreResponse.Data> nearByStoreResponseList = new ArrayList<>();
    private ArrayList<NearByStoreResponse.Data> nearByStoreResponseListFromApi = new ArrayList<>();
    public ArrayList<NearByStoreResponse.Data> bookmarkedWolooList = new ArrayList<>();

    View mMapView;
    protected static SharedPreference mSharedPreference;
    public boolean isFromClickFlag = true;
    private boolean isExpire = false;
    private Handler handler;
    /*Calling on onCreateView*/

    final int DIALOGID = 2;

    ProgressDialog progressdialog;
    private  ArrayList<String> nearByStoreNameList = new ArrayList<>();
    private ArrayList<NearByStoreResponse.Data>  nearByStoreResponseListSearch = new ArrayList<>();

    private boolean isGpsDialogShown = false;
    private boolean isLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 200;

    private static final int REQUEST_CALL_PERMISSION = 2001;

    private int range = 2;
    private PlacesClient placesClient;
    public HashMap<Marker, NearByStoreResponse.Data> markerDataMap = new HashMap<>();
    ArrayList<PlaceAutocomplete> placeSuggestionList = new ArrayList<>();
    ArrayAdapter<PlaceAutocomplete> placeAdapter = null;
    private boolean openNow = false;
    private boolean isSearched = false;
    public boolean showList = true;
    private boolean wolooWithOffers = true;

  //  private boolean isFirstLaunch = true; // Flag to check first launch


    public static int heightOfMapForMarker = 200;

    String hospitalAddress , hospitalName , hospitalContact ,hospitalImage ;
    String policeAddress , policeStationName , policeStationContact , policeStationImage;

    String fireAddress , fireName , fireContact , fireImage;

    String callOnNumber;

    int numberOfPhone = 0;

    private FusedLocationProviderClient fusedLocationClient;
    public ScrollListViewModel scrollViewModel;
    private WolooViewModel wolooViewModel = null;
    UserProfile userProfileResponse = null;
    private ProfileViewModel profileViewModel = null;
  //  ScrollEnrouteListViewModel scrollEnrouteListViewModel;

    private Boolean isSearchedWoloosAvailable = true;
    public Boolean isFromSearchedWoloos = false;

    public  Boolean isApiCalled = false;

    public int is1stSearch = 0;


    private SharedSearchViewModel sharedViewModelSearch;

    public Double newLatitudeSearch = 0.0;
    public  Double newLongitudeSearch = 0.0;

  /*  FragmentHomeBinding binding;



     SearchOptionsBinding searchOptionsBinding;*/

    private ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            if (data != null) {

                                String keyAssigned = data.getStringExtra("EXTRA_KEY");
                                String textSearched = data.getStringExtra("SEARCHED_TEXT");

                                PlaceAutocomplete place =
                                        (PlaceAutocomplete) data.getSerializableExtra("PLACE_NEW");

                                double newLatitude = data.getDoubleExtra("NEW_LATITUDE", 0.0);
                                double newLongitude = data.getDoubleExtra("NEW_LONGITUDE", 0.0);
                                searchAutoComplete.setText(textSearched.toString());
                                Logger.d("MainActivity1" , "Lat"  +newLatitude + "Lan" + newLongitude);
                                isFromSearchedWoloos = true;
                                lastKnownLattitude = newLatitude;
                                lastKnownLongitude = newLongitude;
                                nearByStoreResponseListFromApi.clear();
                                try {

                                    scrollViewModel.setScrollIndex(0);
                                }catch (Exception e)
                                {

                                }
                                WolooDashboard activity = (WolooDashboard) requireActivity();
                               //activity.isEmptyDialogShown = false;
                                ((WolooDashboard) requireActivity()).resetEmptyDialogFlag();

                                getNearByWoloos(newLatitude, newLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), 2, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());

                            }
                        }
                    }
            );




    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragment_map = getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mMapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
        ButterKnife.bind(this, rootView);
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());
        String key = CommonUtils.googlemapapikey(getContext());
        Places.initialize(getContext(), key);
        mSharedPreference = new SharedPreference(getContext());
        scrollViewModel = ((WolooDashboard) getActivity()).getScrollViewModel();
        sharedViewModelSearch = new ViewModelProvider(requireActivity())
                .get(SharedSearchViewModel.class);
        wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        String modeIs = SharedPrefSettings.Companion.getGetPreferences().fetchTransportMode();
        String mode = "0";
        mode = mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey());
        if (modeIs == null || modeIs.equals("") || modeIs.isEmpty()) {
            selectedTravelMode = "car";
            SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("car");
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");


        } else {
            selectedTravelMode = modeIs;
        }

      /*  if(modeIs== null)
        {
            selectedTravelMode = "car";

            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");

        }


*/

        clearSearchText.setOnClickListener(view -> {

            searchAutoComplete.setText("");

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {

                            LatLng currentLatLng =
                                    new LatLng(location.getLatitude(), location.getLongitude());

                            lastKnownLattitude = location.getLatitude();
                            lastKnownLongitude = location.getLongitude();

                            getNearByWoloos(
                                    lastKnownLattitude,
                                    lastKnownLongitude,
                                    Integer.parseInt(
                                            new SharedPreference(requireActivity())
                                                    .getStoredPreference(
                                                            getContext(),
                                                            SharedPreferencesEnum.TRANSPORT_MODE
                                                                    .getPreferenceKey(),
                                                            "0"
                                                    )
                                    ),
                                    2,
                                    1,
                                    cbWolooWithOffers.isChecked(),
                                    cbOpenNow.isChecked()
                            );
                        }
                    });
        });



        Log.d("Aarati Mode is ", modeIs);

        if ("car".equalsIgnoreCase(modeIs)) {
            current_transport_mode.setImageResource(R.drawable.car_navigation);
            mSharedPreference.setStoredPreference(
                    getContext(),
                    SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                    "0"
            );
        } else if ("bike".equalsIgnoreCase(modeIs)) {
            current_transport_mode.setImageResource(R.drawable.bike_navigation);
            mSharedPreference.setStoredPreference(
                    getContext(),
                    SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                    "3"
            );
        } else if ("walk".equalsIgnoreCase(modeIs)) {
            current_transport_mode.setImageResource(R.drawable.walk_navigation);
            mSharedPreference.setStoredPreference(
                    getContext(),
                    SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                    "1"
            );
        }

  /*      if(mode == "0")
        {
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.car_navigation));
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
        }
        else if(mode == "3")
        {
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.bike_navigation));
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "3");
        }
        else if(mode == "1")
        {
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.walk_navigation));
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "1");
        }
*/
        // scrollEnrouteListViewModel = ((EnrouteDirectionActivity) getActivity()).getScrollEnrouteListViewModel();
        placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
       /* ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) frm_home_data.getLayoutParams();
        Log.d("margin bottom is " , String.valueOf(params.bottomMargin));
        density();*/
        try {
            checkGpsAndRequestLocation();
            initView();
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }

        try {
            scrollViewModel.getScrollToIndexForMarker().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer index) {
                    Log.d("Aarati List marker change", String.valueOf(index));
                 /*   if (isFirstLaunch) {
                        isFirstLaunch = false; // Ignore the first event
                        return;
                    }
*/

                    if (index != null && index >= 0 && index < nearByStoreResponseList.size()) {
                        NearByStoreResponse.Data data = nearByStoreResponseList.get(index);

                        // Find the marker associated with this data
                        for (Map.Entry<Marker, NearByStoreResponse.Data> entry : markerDataMap.entrySet()) {
                            if (entry.getValue().equals(data)) {
                                Marker markerToClick = entry.getKey();

                                if (markerToClick != null) {
                                    scrollViewModel.setProgrammaticScroll(true);
                                    onMarkerClick(markerToClick); // Programmatically trigger marker click
                                }
                                break;
                            }
                        }
                    }


                }
            });




        } catch (Exception e) {

        }





        imgScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), QRcodeScannerActivity.class);
                if (((WolooDashboard) getActivity()).getUserProfileResponse() != null) {
                    String viewProfileInString = new Gson().toJson(((WolooDashboard) getActivity()).getUserProfileResponse());
                    intent.putExtra(AppConstants.VIEW_PROFILE_STRING, viewProfileInString);
                }
                startActivity(intent);
            }
        });

        bookmarkListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BookmarkListActivity.class);
                intent.putExtra("LAT" , lastKnownLattitude);
                intent.putExtra("LNG" , lastKnownLongitude);
                intent.putExtra(AppConstants.searchLocationToHost , searchAutoComplete.getText().toString());
                        startActivity(intent);
            }
        });
        searchAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = 2;
                is1stSearch = is1stSearch +1;
                Intent intent = new Intent(getActivity().getApplicationContext(), SerachLayoutForOnRouteFragment.class);
                intent.putExtra("EXTRA_KEY", "search_host");
                resultLauncher.launch(intent);
            }
        });

        currentLocationButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    scrollViewModel.setScrollIndexForMarker(0);
                    scrollViewModel.setProgrammaticScroll(true);
                    scrollViewModel.setScrollIndex(0);
                }catch (Exception e)
                {

                }

                searchAutoComplete.setText("");
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {

                                LatLng currentLatLng =
                                        new LatLng(location.getLatitude(), location.getLongitude());

                                lastKnownLattitude = location.getLatitude();
                                lastKnownLongitude = location.getLongitude();

                                getNearByWoloos(
                                        lastKnownLattitude,
                                        lastKnownLongitude,
                                        Integer.parseInt(
                                                new SharedPreference(requireActivity())
                                                        .getStoredPreference(
                                                                getContext(),
                                                                SharedPreferencesEnum.TRANSPORT_MODE
                                                                        .getPreferenceKey(),
                                                                "0"
                                                        )
                                        ),
                                        2,
                                        1,
                                        cbWolooWithOffers.isChecked(),
                                        cbOpenNow.isChecked()
                                );
                            }
                        });

            }
        });

        destinationSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        imgEnrouteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((WolooDashboard) getActivity()).getUserProfileResponse() != null) {
                    DateToLocalDateConverter dc = new DateToLocalDateConverter();
                    String localExpiryDateIs = dc.dateConvert(((WolooDashboard) getActivity()).getUserProfileResponse().getProfile().getExpiryDate().toString());

                    if (!CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), EnrouteDirectionActivity.class);
                        intent.putExtra("SEARCHLOCATION" , searchAutoComplete.getText().toString());
                        startActivity(intent);

                    } else {
                        displayToast(getResources().getString(R.string.expired_text));
                    }
                }
            }
                                           });
   /* showExtraParams.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(extraParamsLayout.getVisibility() == View.VISIBLE)
            {
                extraParamsLayout.setVisibility(View.GONE);
                imgContactUs.setVisibility(View.GONE);
                imgScanQR.setVisibility(View.GONE);
                bookmarkListView.setVisibility(View.GONE);
                showExtraParams.setRotation(270f);

            }
            else if(extraParamsLayout.getVisibility() == View.GONE)
            {
                extraParamsLayout.setVisibility(View.VISIBLE);
                showExtraParams.setRotation(90f);
                startImageAnimation();
            }
        }
    });*/

       /* if (Objects.requireNonNull(WolooDashboard.Companion.getMSharedPreference()).getStoredBooleanPreference(
                getActivity(),
                SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED_LOO.getPreferenceKey(),
                true
        )
        ) {
            overlay_layout.setVisibility(View.VISIBLE);
            isOverLay = true;
        } else {

            isOverLay = false;
        }
        overlay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(WolooDashboard.Companion.getMSharedPreference()).setStoredBooleanPreference(
                        getActivity(),
                        SharedPreferencesEnum.FIRST_TIME_APP_LAUNCHED_LOO.getPreferenceKey(),
                        false
                );
                overlay_layout.setVisibility(View.GONE);
            }
        });*/






        return rootView;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModelSearch = new ViewModelProvider(requireActivity())
                .get(SharedSearchViewModel.class);

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // sharedViewModelSearch.getSearchText().setValue();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sharedViewModelSearch.getSearchText().setValue(s.toString());
                Log.d("Search Location Home", s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedViewModelSearch.getSearchText().setValue(s.toString());
                Log.d("Search Location Home After", s.toString());

            }
        });
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
            if (WolooApplication.Companion.getInstance().nearByWoloo != null && WolooApplication.Companion.getInstance().getUpdatedLikeStatus() != -1) {
                NearByStoreResponse.Data selectedWoloo = WolooApplication.Companion.getInstance().nearByWoloo;
                if (nearByStoreResponseListFromApi.contains(selectedWoloo)) {
                    nearByStoreResponseListFromApi.get(nearByStoreResponseListFromApi.indexOf(selectedWoloo)).isLiked = WolooApplication.Companion.getInstance().getUpdatedLikeStatus();
                }
                bookmarkedWolooList = new ArrayList<>();
                for (int i = 0; i < nearByStoreResponseListFromApi.size(); i++) {
                    if (nearByStoreResponseListFromApi.get(i).isLiked == 1) {
                        bookmarkedWolooList.add(nearByStoreResponseListFromApi.get(i));
                    }
                }
                if (cbBookmarkedWoloo.isChecked()) {
                    renderNearByWoloos(bookmarkedWolooList);
                } else {
                    renderNearByWoloos(nearByStoreResponseListFromApi);
                }
                WolooApplication.Companion.getInstance().nearByWoloo = null;
                WolooApplication.Companion.getInstance().setUpdatedLikeStatus(-1);
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
    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void initView() {
        Logger.i(TAG, "initView");
        // Construct a FusedLocationProviderClient.
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(getContext());
        }


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


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(progressdialog != null && progressdialog.isShowing()){
                    progressdialog.dismiss();
                }

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




        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      //  height = displayMetrics.heightPixels;
        //  tv_woloo.setVisibility(View.INVISIBLE);
     //   setWidthAndHeight(frm_home_map, height,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);



        voucherExpireLL.setOnTouchListener((v, event) -> true);


        imgContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AARATI" , "event");

                showSosDialog();
            }
        });

        current_transport_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showSelectTransportPopup(v);
                transport_mode_layout.setVisibility(View.VISIBLE);
                current_transport_mode.setVisibility(View.GONE);
            }
        });

        carMode.setOnClickListener(v -> {
            selectedTravelMode = "car";
            try {
                scrollViewModel.requestScrollToFirst();
                scrollViewModel.setScrollIndexForMarker(0);
                scrollViewModel.setProgrammaticScroll(true);
                scrollViewModel.setScrollIndex(0);
            }catch (Exception e)
            {

            }
            nearByStoreResponseList.clear();
            nearByStoreResponseListFromApi.clear();
            SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("car");
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
            current_transport_mode.setVisibility(View.VISIBLE);
            transport_mode_layout.setVisibility(View.GONE);
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.car_navigation));
            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, 0, range, 1,wolooWithOffers,cbOpenNow.isChecked());
        });

        bikeMode.setOnClickListener(v -> {
            selectedTravelMode = "bike";
            try {
                scrollViewModel.requestScrollToFirst();
                scrollViewModel.setScrollIndexForMarker(0);
                scrollViewModel.setProgrammaticScroll(true);
                scrollViewModel.setScrollIndex(0);
            }catch (Exception e)
            {

            }
            nearByStoreResponseList.clear();
            nearByStoreResponseListFromApi.clear();
            SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("bike");
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "3");
            current_transport_mode.setVisibility(View.VISIBLE);
            transport_mode_layout.setVisibility(View.GONE);
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.bike_navigation));

            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, 3, range, 1,wolooWithOffers,cbOpenNow.isChecked());
        });

        walkMode.setOnClickListener(v -> {
            selectedTravelMode = "walk";
            try {
                scrollViewModel.requestScrollToFirst();
                scrollViewModel.setScrollIndexForMarker(0);
                scrollViewModel.setProgrammaticScroll(true);
                scrollViewModel.setScrollIndex(0);
            }catch (Exception e)
            {

            }
            nearByStoreResponseList.clear();
            nearByStoreResponseListFromApi.clear();
            SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("walk");
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "1");
            current_transport_mode.setVisibility(View.VISIBLE);
            transport_mode_layout.setVisibility(View.GONE);
            current_transport_mode.setImageDrawable(getResources().getDrawable(R.drawable.walk_navigation));
            getNearByWoloos(lastKnownLattitude, lastKnownLongitude, 1, range, 1,wolooWithOffers,cbOpenNow.isChecked());
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
           /* searchAutoComplete.setAdapter(placeAdapter);
            searchAutoComplete.setThreshold(1);*/
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
      /*  searchOptionsLayout.setVisibility(View.GONE);
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
        });*/

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

        profileViewModel.getUserProfile();

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


        profileViewModel.observeUserProfile().observe(
                getViewLifecycleOwner(), // use this instead of "this" inside a Fragment
                new Observer<BaseResponse<UserProfile>>() {
                    @Override
                    public void onChanged(BaseResponse<UserProfile> viewProfileResponse) {
                        try {

                            if (viewProfileResponse != null && viewProfileResponse.getData() != null) {
                                userProfileResponse = viewProfileResponse.getData();


                                if (userProfileResponse.getProfile().getExpiryDate() != null) {
                                    DateToLocalDateConverter dc = new DateToLocalDateConverter();
                                    String localExpiryDateIs = dc.dateConvert(
                                            String.valueOf(userProfileResponse.getProfile().getExpiryDate())
                                    );
                                    Logger.d("Local Date to Test is : ",
                                            userProfileResponse.getProfile().getExpiryDate() + " " + localExpiryDateIs);
                                    if (CommonUtils.isSubscriptionExpired(localExpiryDateIs)) {

                                        isExpire = true;
                                        setSubscriptionDetails(localExpiryDateIs, userProfileResponse);
                                        voucherExpireLL.setVisibility(View.VISIBLE);
                                    }
                                }
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



        if (wolooViewModel != null) {
            wolooViewModel.observeAddCoinstoWolooUser()
                    .observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
                        @Override
                        public void onChanged(BaseResponse<JSONObject> response) {
                            new CommonUtils().hideProgress();
                            mSharedPreference.setStoredBooleanPreference(getContext(), SharedPreferencesEnum.SEARCH_COINS_MODE.getPreferenceKey(), true);
                          //  showSuccessDialog();
                        }
                    });
        }

        homeViewModel.observeNearByWoloo().observe(getViewLifecycleOwner(), arrayListBaseResponse -> {
            try {
                try {
                    scrollViewModel.requestScrollToFirst();
                    scrollViewModel.setScrollIndexForMarker(0);
                    scrollViewModel.setProgrammaticScroll(true);
                    scrollViewModel.setScrollIndex(0);
                }catch (Exception e)
                {

                }
                Utility.hideKeyboard(requireActivity());
                Logger.i("Aarati test " , arrayListBaseResponse.getData().toString());
                if (arrayListBaseResponse != null) {

                    Logger.i("Aarati test 1" , arrayListBaseResponse.getData().toString());
                    if (arrayListBaseResponse.getData() == null || arrayListBaseResponse.getData().isEmpty()) {
                        Logger.i("Aarati test 2", arrayListBaseResponse.getData().toString());
                        nearByStoreResponseListFromApi = new ArrayList<>();

                        if (isFromSearchedWoloos == true) {
                            if(range == 2)
                            {
                                range = 4;
                                getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
                            }
                            else if( range == 4)
                            {
                                range = 5 ;
                                getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
                            }
                            else if( range == 5)
                            {
                                range = 6;
                                getNearByWoloos(lastKnownLattitude, lastKnownLongitude, Integer.parseInt(new SharedPreference(requireActivity()).getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 1, cbWolooWithOffers.isChecked(), cbOpenNow.isChecked());
                            }
                            else {
                                WolooDashboard activity = (WolooDashboard) requireActivity();

                                if (activity.canShowEmptyDialog()) {
                                    activity.markEmptyDialogShown();
                                    showSearchLocationDialog();
                                }
                            }
                        }
                        renderNearByWoloos(nearByStoreResponseListFromApi);
                        HomeCategoryFragment childFragment =
                                (HomeCategoryFragment) getChildFragmentManager().findFragmentByTag("HomeCategoryFragment");

                        if (childFragment != null) {
                            childFragment.setChildViewVisibility(true);
                            Logger.i("Aarati Visibility", "show morelayout 1");
                        }




                        return;
                    } else {
                        nearByStoreResponseListFromApi = new ArrayList<>();
                        HomeCategoryFragment childFragment =
                                (HomeCategoryFragment) getChildFragmentManager().findFragmentByTag("HomeCategoryFragment");

                        if (childFragment != null) {
                            childFragment.setChildViewVisibility(false);
                        }
                        nearByStoreResponseListFromApi = arrayListBaseResponse.getData();
                    }

                } else {

                    nearByStoreResponseListFromApi = new ArrayList<>();
                    HomeCategoryFragment childFragment =
                            (HomeCategoryFragment) getChildFragmentManager().findFragmentByTag("HomeCategoryFragment");

                    if (childFragment != null) {
                        childFragment.setChildViewVisibility(false);
                    }
                }
                bookmarkedWolooList = new ArrayList<>();
                for (int i = 0; i < nearByStoreResponseListFromApi.size(); i++) {
                    if (nearByStoreResponseListFromApi.get(i).isLiked == 1) {
                        bookmarkedWolooList.add(nearByStoreResponseListFromApi.get(i));
                    }
                }
                if (cbBookmarkedWoloo.isChecked()) {
                    renderNearByWoloos(bookmarkedWolooList);
                } else if (nearByStoreResponseListFromApi.isEmpty()) {

                /*if(openNow)
                    displayToast("No Woloos available at the moment.!!");*/

                    Bundle bundle = new Bundle();
                    HashMap<String, Object> payload = new HashMap<>();
                    try {
                        if (lastKnownLocation != null) {
                            bundle.putString(AppConstants.LOCATION, "(" + lastKnownLattitude + "," + lastKnownLongitude + ")");
                            payload.put(AppConstants.LOCATION, "(" + lastKnownLattitude + "," + lastKnownLongitude + ")");
                        }
                        if (isSearched) {
                            bundle.putString(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
                            payload.put(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
                        }
                        isSearched = false;
                    } catch (Exception ex) {

                    }

                    logFirebaseEvent(getActivity(), bundle, AppConstants.NO_LOCATION_FOUND);
                    logNetcoreEvent(getActivity(), payload, AppConstants.NO_LOCATION_FOUND);
                    renderNearByWoloos(nearByStoreResponseListFromApi);
                } else {
                    renderNearByWoloos(nearByStoreResponseListFromApi);
                }
            }catch (Exception e)
            {

            }
        });
    }

    public void getNearByWoloos(double lat, double lng, int mode, int range, int isSearch) {

        if(isApiCalled == false) {
            isApiCalled = true;
            NearbyWolooRequest request = new NearbyWolooRequest();
            request.setLat(lat);
            request.setLng(lng);
            request.setMode(mode);
            request.setRange(range);
            request.setPackageName("in.woloo.app");
            request.setSearch(isSearch);
            request.setShowAll(2);
            this.openNow = true;
            homeViewModel.getNearbyWoloos(request);
            this.isSearched = false;
            fetchNearestSosStation(lat, lng, AppConstants.HOSPITAL);
            fetchNearestSosStation(lat, lng, AppConstants.POLICESTATION);
            fetchNearestSosStation(lat, lng, AppConstants.FIRESTATION);
        }

    }


    private void showSuccessDialog(String coins) {
        try {
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(getResources().getColor(R.color.transparent_background))
                );
            }

            dialog.setContentView(R.layout.dialog_coins_success);

            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;

            int dialogWidth = screenWidth;

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }

            TextView btnCloseDialog = dialog.findViewById(R.id.tv_go_back_to_home);
            TextView btnShopDialog = dialog.findViewById(R.id.tv_shop_now);
            ImageView gifImageView = dialog.findViewById(R.id.gifImageView);

            TextView btnSuccessTextDialog = (TextView) dialog.findViewById(R.id.tv_logout);
            btnSuccessTextDialog.setText("Woohoo! You Earned " + coins + " Woloo Points!");


            btnShopDialog.setVisibility(View.GONE);

            Glide.with(this)
                    .load(R.drawable.coins_animate)
                    .into(gifImageView);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }


    public void getNearByWoloos(double lat, double lng, int mode, int range, int isSearch, boolean isOffer, boolean open){
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
            request.setShowAll(2);
        } else {
            this.openNow = false;
            request.setShowAll(2);
        }
        request.setPackageName("in.woloo.app");
        request.setSearch(0);
        homeViewModel.getNearbyWoloos(request);

        this.isSearched = true;
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
        bundle.putString(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        logFirebaseEvent(getContext(), bundle, AppConstants.SEARCH_WOLOO_EVENT);

        HashMap<String,Object> payload = new HashMap<>();
        payload.put(AppConstants.SEARCH_KEYWORD, searchAutoComplete.getText().toString());
        payload.put(AppConstants.LOCATION, "(" + lat + "," + lng + ")");
        logNetcoreEvent(getContext(),payload,AppConstants.SEARCH_WOLOO_EVENT);
    }


    public boolean isWolooListVisible(){
        return  frm_home_data.getVisibility() == View.VISIBLE;
    }

    /*Calling on hideAndShow*/
    /*public void hideAndShow(boolean status) {
        Logger.i(TAG, "hideAndShow");
        if (status) {
            setWidthAndHeight(frm_home_map, height,false);
            this.isSectionShow = false;
            frm_home_data.setVisibility(View.GONE);
//            nsv.setSmoothScrollingEnabled(false);
          //  tv_woloo.setVisibility(View.INVISIBLE);
            frm_home_data.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_bottom));
//            tv_woloo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
//            nsv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
        } else {
            Bundle bundle = new Bundle();
            HashMap<String,Object> payload = new HashMap<>();
            try {
                bundle.putString(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                payload.put(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                SharedPrefSettings.getGetPreferences()
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
           // tv_woloo.setVisibility(View.VISIBLE);
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
    }*/

    /*Calling on onMapReady*/
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Logger.i(TAG, "onMapReady");
        map = googleMap;
       // map.getUiSettings().setAllGesturesEnabled(false);

        // Disable Compass button
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        moveCameraToDefaultLocation(false);
        checkGpsAndRequestLocation();
        map.setOnMarkerClickListener(HomeFragment.this);

      //  map.setInfoWindowAdapter(new CustomMarker(getActivity().getApplicationContext()));



      /*  boolean success = map.setMapStyle(
                new MapStyleOptions(
                        "[\n" +
                                "    {\n" +
                                "        \"featureType\": \"all\",\n" +
                                "        \"elementType\": \"geometry\",\n" +
                                "        \"stylers\": [\n" +
                                "            {\n" +
                                "                \"color\": \"#EEEEEE\"\n" +
                                "            }\n" +
                                "        ]\n" +
                                "    }\n" +
                                "]"
                )
        );
*/
      /*  List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(-34.0, 151.0));
        points.add(new LatLng(-34.1, 151.1));
        points.add(new LatLng(-34.2, 151.0));

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(10f) // Make sure this is visible
                .color(Color.RED); // Ensure this color stands out

        map.addPolyline(polylineOptions);*/


    }

    private void moveCameraToDefaultLocation(boolean shouldShowNearbyLoos) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, AppConstants.DEFAULT_ZOOM);
        map.moveCamera(cameraUpdate);
        if (shouldShowNearbyLoos) {
            //hideAndShow(true);
            getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
        }
    }

    /*Calling on getDeviceLocation*/
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            if (progressdialog != null && !progressdialog.isShowing()) {
                progressdialog.show();
            }
        } catch (Exception e) {
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
                    //     Toast.makeText(requireActivity(), "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    new CommonUtils().printStackTrace(e);
                }
            }
        };

        try{
        if (getActivity() == null || !isAdded()) {
            return; // Fragment not attached → skip
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (progressdialog != null && progressdialog.isShowing()) {
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
                        getNearByWoloos(defaultLocation.latitude, defaultLocation.longitude, Integer.parseInt(mSharedPreference.getStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")), range, 0);
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
    }catch(Exception e) {

    }
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
                  //  Toast.makeText(requireActivity(), "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
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
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
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
   /* private void setWidthAndHeight(FrameLayout view, int height, boolean isHeightForFragment) {
        Logger.i(TAG, "setWidthAndHeight");
        if(!isHeightForFragment)
        {
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
*/
    @Override
    public void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null && callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }


    public Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID, int index) {
        map.getUiSettings().setZoomGesturesEnabled(true);
        int height = 110;
        int width = 90;
        BitmapDrawable bitmapdraw;
      //  if(nearByStoreResponseList.get(i).isOpenNow.matches("1")) {
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(iconResID);
        //}


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
            nearByStoreResponseList = dataObject.data;

            try {

                ((HomeCategoryFragment) fragment_map).setNearestWalk(nearByStoreResponseList,false,false,false );
            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }
            for (int i = 0; i < nearByStoreResponseList.size(); i++) {
                NearByStoreResponse.Data data = nearByStoreResponseList.get(i);
                //markerList.add(createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.ic_store_mark_dest, i));
             //   if(nearByStoreResponseList.get(i).isOpenNow.matches("1")) {
                    markerList.add(createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.ic_store_mark_dest, i));
              /*  }
                else {
                    markerList.add(createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.try_icon, i));
                }*/
            }
            animateCameraToMarkerPosition(0);

            if (isFromClickFlag) {
                //hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
            //    ((WolooDashboard) requireActivity()).hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
            } else
                isFromClickFlag = true;
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    public void renderNearByWoloos(ArrayList<NearByStoreResponse.Data> dataObject) {
        try {
            try {
                scrollViewModel.requestScrollToFirst();
                scrollViewModel.setScrollIndexForMarker(0);
                scrollViewModel.setProgrammaticScroll(true);
                scrollViewModel.setScrollIndex(0);
            }catch (Exception e)
            {

            }
            isApiCalled = false;
            if (dataObject == null) {
                dataObject = new ArrayList<>();
            }

            markerList = new ArrayList<>();
            map.clear();
            nearByStoreResponseList = dataObject;
            try {
                ((HomeCategoryFragment) fragment_map).setNearestWalk( nearByStoreResponseList, cbOpenNow.isChecked(), cbBookmarkedWoloo.isChecked(), false );
            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }
            for (int i = 0; i < nearByStoreResponseList.size(); i++) {
                NearByStoreResponse.Data data = nearByStoreResponseList.get(i);
              //  if(nearByStoreResponseList.get(i).isOpenNow.matches("1")) {
                Marker marker = createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.ic_store_mark_dest, i);
                    markerList.add(marker);
               /* }
                else {
                    markerList.add(createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.try_icon, i));
                }*/

                markerDataMap.put(marker , data);

            }
            animateCameraToMarkerPosition(0);
            if (isFromClickFlag) {
                //hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
              //  ((WolooDashboard) requireActivity()).hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
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

  /*  public void isShowingMarkerFragment(boolean isShow){
        if (isShow){
            setWidthAndHeight(frm_home_map, heightOfMapForMarker,true);
        }else{
            setWidthAndHeight(frm_home_map, height,false);
        }
    }*/

    /*Calling on onMarkerClick*/
    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        Logger.d(TAG, "onMarkerClick");
        CommonUtils.hideKeyboard(getActivity());

        try {

           /* NearByStoreResponse.Data data = nearByStoreResponseList.get((int) marker.getZIndex());
            WolooApplication.instance().setNearByWoloo(nearByStoreResponseList.get((int) marker.getZIndex()));
            Intent intent = new Intent(getActivity().getApplicationContext(), HomeDetailsActivity.class);
            startActivity(intent);
            HashMap<String, Object> payload = new HashMap<>();
            payload.put(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
            payload.put(AppConstants.TRAVEL_MODE, selectedTravelMode);
            payload.put(AppConstants.HOST_CLICKED_ID, data.id);
            payload.put(AppConstants.HOST_CLICKED_LOCATION, "(" + data.lat + "," + data.lng + ")");
            Utility.logNetcoreEvent(getContext(), payload, AppConstants.WOLOO_MARKER_CLICK);*/

           // marker.showInfoWindow();
            Bundle bundle = new Bundle();
            HashMap<String, Object> payload = new HashMap<>();
            bundle.putString(AppConstants.WOLOO_MARKER_CLICK, "On launch");
            bundle.putString(AppConstants.WOLOO_MARKER_CLICK, "on launch");
            Utility.logFirebaseEvent(getContext() , bundle ,AppConstants.WOLOO_MARKER_CLICK );
            payload.put(AppConstants.WOLOO_MARKER_CLICK, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
            payload.put(AppConstants.WOLOO_MARKER_CLICK, selectedTravelMode);
            Utility.logNetcoreEvent(getContext(), payload, AppConstants.WOLOO_MARKER_CLICK);


        if(previousMarker != null && previousMarker != clickedMarker) {
            int height = 110;
            int width = 90;
            BitmapDrawable bitmapdraw;
            bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_store_mark_dest);

            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            // Change the icon of the clicked marker
            previousMarker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        }

            NearByStoreResponse.Data clickData = markerDataMap.get(clickedMarker);
        String cibilData = clickData.getCibilScore();

            clickedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(cibilData)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(clickedMarker.getPosition() , 16f));
            previousMarker = clickedMarker;

            int clickedIndex = -1;
            Log.d("Aarati List test" , nearByStoreResponseList.size() + "");
            for (int i = 0; i < nearByStoreResponseList.size(); i++) {
                if (nearByStoreResponseList.get(i).equals(clickData)) {
                    clickedIndex = i;
                    break;
                }
            }


            Log.d("Aarati List" , clickedIndex + "");
            // Pass the index to ViewModel
            if (clickedIndex != -1) {

                scrollViewModel.setScrollIndex(clickedIndex); // Pass the index here
            }
            else
            {

            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
       // ((WolooDashboard) getActivity()).loadMarkerFragmentWithIndex((int) marker.getZIndex(), nearByStoreResponseList);
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

            if(Objects.equals(placeType, AppConstants.HOSPITAL))
            {
                hospitalName = name;
                hospitalAddress = address;
                hospitalContact = phoneNumber.trim();
                hospitalContact = replaceFirstCharacter(hospitalContact);
                hospitalImage = photoUrl;
                Log.d(placeType + " Info", hospitalContact  + " " + hospitalImage);
            } else if (Objects.equals(placeType, AppConstants.POLICESTATION)) {

                policeStationName = name;
                policeAddress = address;
                policeStationContact = phoneNumber.trim();
                policeStationContact = replaceFirstCharacter(policeStationContact);
                policeStationImage = photoUrl;
                Log.d(placeType + " Info", policeStationContact + " " + policeStationImage);

            } else if (Objects.equals(placeType, AppConstants.FIRESTATION)) {
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

            Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_sos_category);

            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;  // Set width to match parent
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  // Set height to match parent
            dialog.getWindow().setAttributes(layoutParams);

            ImageView imageViewClose = dialog.findViewById(R.id.close_dialog_sos);
            LinearLayout wolooImg = dialog.findViewById(R.id.woloo_image);
            LinearLayout hospitalImg = dialog.findViewById(R.id.hospital_image);
            LinearLayout policeImg = dialog.findViewById(R.id.police_image);
            LinearLayout fireImg = dialog.findViewById(R.id.fire_image);

            wolooImg.setOnClickListener(v -> {
                showContactConnectDialog("WOLOO" , "Woloo Support" , "1706 Lodha Supremus,Tunga Village opp.MTNL, Powai Andheri (E),Mumbai" ,(AppConstants.CALL_MOBILE +AppConstants.MOBILENUMBER).substring(4) );
                dialog.dismiss();
            });

            hospitalImg.setOnClickListener(v -> {
                showContactConnectDialog("HOSPITAL" , hospitalName , hospitalAddress , hospitalContact);
                dialog.dismiss();
            });

            policeImg.setOnClickListener(v -> {
                showContactConnectDialog("POLICE" , policeStationName , policeAddress , policeStationContact);
                dialog.dismiss();
            });

            fireImg.setOnClickListener(v -> {
                showContactConnectDialog("FIRE" , fireName , fireAddress , fireContact);
                dialog.dismiss();
            });


            imageViewClose.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }


/*    private void showSelectTransportDialog() {
        Logger.i(TAG, "showSosDialog");
        try {

            Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_transport_mode);

            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;  // Set width to match parent
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  // Set height to match parent
            dialog.getWindow().setAttributes(layoutParams);

            ImageView imageViewClose = dialog.findViewById(R.id.close_dialog_sos);
            ImageView carMode = dialog.findViewById(R.id.byCarShow);
            ImageView bikeMode = dialog.findViewById(R.id.byBikeShow);
            ImageView walkMode = dialog.findViewById(R.id.byWalkShow);


            carMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTravelMode = "car";
                    SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("car");
                    mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
                    dialog.dismiss();
                }
            });

            bikeMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTravelMode = "bike";
                    SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("bike");
                    mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "3");
                    dialog.dismiss();
                }
            });

            walkMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedTravelMode = "walk";
                    SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("walk");
                    mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "2");
                    dialog.dismiss();
                }
            });






            imageViewClose.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }*/

    private void showSelectTransportPopup(View anchorView) {
        try {
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            View popupView = inflater.inflate(R.layout.dialog_transport_mode, null);

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true // Focusable
            );

            // Optional background and outside dismiss
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);

            // Init Views
           // ImageView imageViewClose = popupView.findViewById(R.id.close_dialog_sos);
            ImageView carMode = popupView.findViewById(R.id.byCarShow);
            ImageView bikeMode = popupView.findViewById(R.id.byBikeShow);
            ImageView walkMode = popupView.findViewById(R.id.byWalkShow);

            carMode.setOnClickListener(v -> {
                selectedTravelMode = "car";
                SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("car");
                mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
                popupWindow.dismiss();
            });

            bikeMode.setOnClickListener(v -> {
                selectedTravelMode = "bike";
                SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("bike");
                mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "3");
                popupWindow.dismiss();
            });

            walkMode.setOnClickListener(v -> {
                selectedTravelMode = "walk";
                SharedPrefSettings.Companion.getGetPreferences().storeTransportMode("walk");
                mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "1");
                popupWindow.dismiss();
            });

           // imageViewClose.setOnClickListener(v -> popupWindow.dismiss());

            // Show the popup below the clicked view (anchorView)
            popupWindow.showAsDropDown(anchorView, 0, 0); // adjust offset if needed

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showContactConnectDialog(String type , String name , String address , String number)
    {
        Logger.i(TAG, "showSosDialog");
        try {
            if (getActivity() == null || getActivity().isFinishing()) {
                return;  // If the activity is not valid, do not show the dialog
            }
            Dialog dialogNew = new Dialog(requireActivity());
            dialogNew.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogNew.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialogNew.setCancelable(true);
            dialogNew.setCanceledOnTouchOutside(true);
            dialogNew.setContentView(R.layout.dialog_sos_details);



            TextView nameSOS = dialogNew.findViewById(R.id.emergency_name);
            TextView phoneSos = dialogNew.findViewById(R.id.emergency_phone);
            ImageView callSOS = dialogNew.findViewById(R.id.call_sos_num);

            if(name != null) {
                nameSOS.setText(name);
            }
            else
            {
                nameSOS.setText("No nearby hospital found within 5 kms.");
            }

            if(number != null) {
                phoneSos.setText(number);
            }
            else
            {
                if(Objects.equals(type, "HOSPITAL"))
                {
                    phoneSos.setText("Call for fire station on 102");
                }
                else if(Objects.equals(type, "POLICE"))
                {
                    phoneSos.setText("Call for fire station on 100");
                }
                else if(Objects.equals(type, "FIRE"))
                {
                    phoneSos.setText("Call for fire station on 101");
                }
                else {
                    phoneSos.setText("Number does not exists");
                }
            }

            callSOS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Objects.equals(type, "WOLOO"))
                    {
                        makePhoneCall(AppConstants.CALL_MOBILE + AppConstants.MOBILENUMBER);
                    }
                    else {
                    makePhoneCall(number);
                    }
                    dialogNew.dismiss();

                }
            });

            dialogNew.show();

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


    public void startImageAnimation() {
        // Set images to be visible first
        imgScanQR.setVisibility(View.VISIBLE);
        bookmarkListView.setVisibility(View.VISIBLE);
        imgContactUs.setVisibility(View.VISIBLE);

        // Create individual ObjectAnimators for each ImageView
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imgScanQR, "alpha", 0f, 1f);
        anim1.setDuration(500); // 500ms for fade-in effect
        anim1.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(bookmarkListView,"alpha" , 0f ,1f);
        anim2.setDuration(500);




        ObjectAnimator anim3 = ObjectAnimator.ofFloat(imgContactUs, "alpha", 0f, 1f);
        anim3.setDuration(500);

        // Create an AnimatorSet to play animations sequentially
        AnimatorSet animatorSet = new AnimatorSet();

        // Chain the animations one after another
        animatorSet.playSequentially(anim1, anim2, anim3);

        // Start the animation sequence
        animatorSet.start();
    }

    private Bitmap createCustomMarker(String text) {
        View markerView = getLayoutInflater().inflate(R.layout.custome_onclick_marker_design, null);

        // Set the text on the marker
        TextView markerText = markerView.findViewById(R.id.show_cibil_score);
        markerText.setText(text);

        // Measure and layout the view
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerView.layout(0, 0, markerView.getMeasuredWidth(), markerView.getMeasuredHeight());

        // Convert the view to a bitmap
        Bitmap bitmap = Bitmap.createBitmap(markerView.getMeasuredWidth(), markerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerView.draw(canvas);

        return bitmap;
    }



    public  void  density()
    {


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = metrics.densityDpi;

        String densityCategory;

        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                densityCategory = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityCategory = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityCategory = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityCategory = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                densityCategory = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                densityCategory = "xxxhdpi";
                break;
            default:
                densityCategory = "Unknown density";
                break;
        }

// Now, you can use the densityCategory variable to determine the screen density.
        Log.d("Screen Density", "Density: " + densityCategory);

    }

    private void showSearchLocationDialog() {
        try {
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.transparent_background))
            );

            dialog.setContentView(R.layout.dialog_search_another_location);

            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int dialogWidth = screenWidth;

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(
                        dialogWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                dialog.getWindow().setGravity(Gravity.CENTER);
            }

            TextView btnCloseDialog = dialog.findViewById(R.id.tv_go_back_to_home);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        Boolean searchValue = mSharedPreference.getStoredBooleanPreference(getContext(), SharedPreferencesEnum.SEARCH_COINS_MODE.getPreferenceKey(), true);
                        if (!searchValue) {
                            CreditCoinsRequest request = new CreditCoinsRequest(
                                    Integer.parseInt(CommonUtils.authconfig_response(requireContext()).getNoWolooFound()),
                                    AppConstants.NO_WOLOO_FOUND_CLICK,
                                    AppConstants.NO_WOLOO_FOUND_CLICK,
                                    0,
                                    0, 0, 0
                            );
                            wolooViewModel.addCoinstoWolooUser(request);
                            Bundle bundle = new Bundle();
                            HashMap<String, Object> payload = new HashMap<>();

                            bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found");
                            bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found");

                            payload.put(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found");
                            payload.put(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found");

                            logFirebaseEvent(requireActivity(), bundle, AppConstants.NO_WOLOO_FOUND_CLICK);
                            logNetcoreEvent(requireActivity(), payload, AppConstants.NO_WOLOO_FOUND_CLICK);
                            SharedPrefSettings.Companion.getGetPreferences().storeIs25KM("NO");

                        }
                    }
                }
            });
            dialog.show();


        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }


}