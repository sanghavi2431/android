package in.woloo.www.mapdirection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.HttpResponse;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;
import com.netcore.android.Smartech;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsPresenter;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsView;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.mapdirection.model.NavigationRewardsResponse;
import in.woloo.www.mapdirection.mvp.MapDirectionPresenter;
import in.woloo.www.mapdirection.mvp.MapDirectionView;
import in.woloo.www.my_account.MyAccountFragment;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.SubscribeActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.ApiService;
import in.woloo.www.v2.data.remote.ApiServiceClientAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, InviteFriendsView, MapDirectionView, RoutingListener {

    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private GoogleMap mMap;
    ArrayList markerPoints = new ArrayList();
    public GpsTracker gps;
    private Bitmap currentBitmap;
    private Marker currentLocationMarker;
    private double curlat;
    private double curlon;

    @BindView(R.id.tv_areaname)
    TextView tv_areaname;


    @BindView(R.id.tv_distance)
    TextView tv_distance;


    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_nextturnname)
    TextView tv_nextturnname;

    @BindView(R.id.ivDirection)
    ImageView ivDirection;

    @BindView(R.id.ivarrow)
    ImageView ivarrow;

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.iv_currentlocation)
    ImageView iv_currentlocation;


    @BindView(R.id.iv_shortdist)
    TextView iv_shortdist;

    @BindView(R.id.tv_start)
    TextView tv_start;


    private String distanceroad;
    private String duration;
    private String maneuver;
    private GetDistance getdistance;

    public static Handler showLocationHandler;
    public static Runnable showLocationRunnable;
    private static final int DELAY_TIME = 1000 * 5; //delay time - 1 minute
    private Marker marker;
    private LatLng currentpos;
    private boolean isMarkerRotating = false;
    private LatLng origin, dest;
    private String firstdistance = "";
    private LatLng ll;


    LocationManager locationManager;
    String destlat;
    String destlong;
    private GetDistance.Routes.Legs.Steps first_step;
    private GetDistance.Routes.Legs.Steps second_step;
    private int destination_steps = 0;
    private int finaldistance = 0;
    private boolean needToCallDistanceAPI = true;
    public InviteFriendsPresenter inviteFriendsPresenter;
    private String expiryDate = "";
    private Context mContext;
    private Activity context;

    private MapDirectionPresenter mapDirectionPresenter;
    private int wolooId;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = true;
    String tag;
    SupportMapFragment mapFragment;
    View mapView;
    private ProgressDialog progressDialog;
    private LatLng start, end;
    private List<Polyline> polylines;

    ArrayList points = null;
    private static final String LOG_TAG = "MyActivity";
    private double newlat;
    private double newlng;
    LatLng newlatlng;
    private Handler handler;
    public static boolean hasReachedAtDestination = false;
    public static String TAG = MapsFragment.class.getSimpleName();
    protected SharedPreference mSharedPreference;
    private final LatLng defaultLocation = new LatLng(19.055229, 72.830829);


    public MapsFragment() {
        // Required empty public constructor
    }

    /*calling on onCreate*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        if (getArguments() != null) {
            destlat = getArguments().getString("destlat");
            destlong = getArguments().getString("destlong");
            wolooId = getArguments().getInt("wolooId");
            tag = getArguments().getString("tag");
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapView = mapFragment.getView();

        context = getActivity();
        getProfile();
    }

    /*calling on onResume*/
    @Override
    public void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
        if (hasReachedAtDestination) {
            hasReachedAtDestination = false;
            Intent intent = new Intent(context, AddReviewActivity.class);
            intent.putExtra(AppConstants.WOLOO_ID, wolooId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    /*calling on getProfile*/
    private void getProfile() {
        Logger.i(TAG, "getProfile");
        try {
            mapDirectionPresenter = new MapDirectionPresenter(getContext(), this);
            inviteFriendsPresenter = new InviteFriendsPresenter(getContext(), this);
            inviteFriendsPresenter.getProfile();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on getDeviceLocation*/
    void getDeviceLocation() {
        Logger.i(TAG, "getDeviceLocation");
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = task.getResult();
//                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                            LatLng currentLatLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                    AppConstants.DEFAULT_ZOOM);
                            mMap.moveCamera(update);
                        }
                    }
                });
            }else{
                LatLng currentLatLng = new LatLng(defaultLocation.latitude,
                        defaultLocation.longitude);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                        AppConstants.DEFAULT_ZOOM);
                mMap.moveCamera(update);
            }
        } catch (Exception e) {
            Logger.e("Exception: %s", e.getMessage());
        }
    }

    /*calling on onCreateView*/
    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        ButterKnife.bind(this, rootView);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GpsTracker(getContext());

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        tv_start.setVisibility(View.GONE);

        try {
            if (tag.equalsIgnoreCase("start")) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        2000,
                        10, locationListenerGPS);
                tv_start.setVisibility(View.GONE);
            }else{
                tv_start.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> payload = new HashMap<>();
                payload.put(AppConstants.WOLOO_NAME, String.valueOf(wolooId));
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.START_WOLOO_EVENT);

                Intent i = new Intent(context, MapDirection.class);
                i.putExtra("destlat", destlat);
                i.putExtra("destlong",destlong);
                i.putExtra("wolooId",wolooId);
                i.putExtra("tag","start");
                context.startActivity(i);
                context.finish();
            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                route();
                context.finish();

//                try {
//                    new getdistanceVal(curlat, curlon, dest.latitude, dest.longitude).execute();
//                } catch (Exception e) {
//                      CommonUtils.printStackTrace(e);
//                }
//
//                mMap.clear();
//
//                String url = getDirectionsUrl(origin, dest);
//                Logger.e("url", "" + url);
//
//                DownloadTask downloadTask = new DownloadTask();
//
//                downloadTask.execute(url);

            }
        });

        iv_currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(getActivity());

                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
//                mMap.clear();

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                    drawMarker(curlat, curlon);
                }

//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curlat, curlon), 16F));

                LatLng latLng1 = new LatLng(Double.parseDouble(destlat), Double.parseDouble(destlong));
                markerPoints.add(latLng1);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng1);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(options);

                updateLocationUI();

                try {
//                    if (newlat!=0.0 && newlng!=0.0) {
                    new getdistanceVal(newlat, newlng, dest.latitude, dest.longitude).execute();
//                        String url = getDirectionsUrl(newlatlng, dest);
//                        Logger.e("url", "" + url);
                    String str_origin = "";
                    try {
                        if (newlng != 0.0 && newlng != 0.0) {
                            str_origin = "origin=" + newlat + "," + newlng;
                        } else {
                            str_origin = "origin=" + gps.getLatitude() + "," + gps.getLongitude();
                        }
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                        str_origin = "origin=" + gps.getLatitude() + "," + gps.getLongitude();
                    }


                    // Destination of route
                    String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

                    // Sensor enabled
                    String sensor = "sensor=false";
                    String mode = "mode=driving";
                    if (mSharedPreference == null) {
                        mSharedPreference = new SharedPreference(context);
                    }
                    String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
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
//                    String key = "key=" + getResources().getString(R.string.google_maps_key);
                    String key = "key=" +CommonUtils.googlemapapikey(context);

                    String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

                    // Output format
                    String output = "json";

                    // Building the url to the web service
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


                    DownloadTask downloadTask = new DownloadTask(getContext());

                    downloadTask.execute(url);
//                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }


            }
        });


        return rootView;
    }
    /*calling on route*/
    private void route() {
        Logger.i(TAG, "route");
        start = new LatLng(curlat, curlon);
        end = new LatLng(Double.parseDouble(destlat), Double.parseDouble(destlong));


        progressDialog = ProgressDialog.show(getContext(), "Please wait.",
                "Fetching route information.", true);
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .build();
        routing.execute();
    }

    /*calling on updateLocationUI*/
    void updateLocationUI() {
        Logger.i(TAG, "updateLocationUI");
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Logger.e("Exception: %s", e.getMessage());
        }
    }
    /*calling on animateMarker*/
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

    /*calling on Locationlistener for location changed and update the marker position*/
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            try {
                Logger.i(TAG, "onLocationChanged");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                newlat = latitude;
                newlng = longitude;
                newlatlng = new LatLng(newlat, newlng);


                String msg = "Lat: " + latitude + ",Long: " + longitude;
                //Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Logger.e("lat", String.valueOf(latitude));
                Logger.e("long", String.valueOf(longitude));

                if (location == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Cant Find User Location", Toast.LENGTH_SHORT);
                } else {
//                    Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                    curlat = latitude;
                    curlon = longitude;
                    currentpos = new LatLng(curlat, curlon);
                    if (marker != null) {
                        marker.setPosition(currentpos);
                    } else {
                        drawMarker(curlat, curlon);
                    }
                    try {
                        getAddress(curlat, curlon);
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }

                    /*calling on getdistanceVal*/
                    try {
                        new getdistanceVal(curlat, curlon, dest.latitude, dest.longitude).execute();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }
            }
            catch (Exception e){

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param curlat
     * @param curlon
     */
    /*calling on drawMarker*/
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

    /*calling on bearingBetweenLocations*/
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
    /*calling on getAddress*/
    public void getAddress(double lat, double lng) {
        Logger.i(TAG, "getAddress");
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Logger.e("Address", "Address" + add);
            tv_areaname.setText(obj.getLocality() + "");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if(e.toString().contains("Service not Available"))
            {
                CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
              CommonUtils.printStackTrace(e);
//            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /*calling on convertInputStreamToString*/
    private static String convertInputStreamToString(InputStream is) throws IOException {
        Logger.i(TAG, "convertInputStreamToString");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        // Java 1.1
        return result.toString(StandardCharsets.UTF_8.name());

        // Java 10
        // return result.toString(StandardCharsets.UTF_8);

    }
    /*calling on getDistanceOnRoad*/
    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {
        Logger.i(TAG, "getDistanceOnRoad");
        final String[] result_in_kms = {""};
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        String mode = "mode=driving";
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(context);
        }
        String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
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
        String key = "key=" +CommonUtils.googlemapapikey(context);
        String url = "https://maps.google.com/maps/api/directions/json?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&"+mode+"&units=metric" + "&" + key;
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            ApiServiceClientAdapter adapter = ApiServiceClientAdapter.Companion.getInstance();
            Call<GetDistance> call = adapter.getApiService().getDirections(url);
            call.enqueue(new Callback<GetDistance>() {
                @Override
                public void onResponse(Call<GetDistance> call, Response<GetDistance> response) {
                    getdistance = response.body();
                    assert getdistance != null;
                    duration = getdistance.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                    result_in_kms[0] = getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getText();

                    try {
                        first_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(0);
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }

                    try {
                        second_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(1);
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }

                    destination_steps = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().size();

                    updateDirections();

                    try {
                        Logger.e("distroad", distanceroad);
                        tv_distance.setText(result_in_kms[0]);
                        tv_time.setText(duration);
                        iv_shortdist.setText(first_step.getDistance().getText());
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }

                @Override
                public void onFailure(Call<GetDistance> call, Throwable t) {

                }
            });
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpPost httpPost = new HttpPost(url);
//            response = httpClient.execute(httpPost, localContext);
//            InputStream is = response.getEntity().getContent();
//            String result = convertInputStreamToString(is);
//            Logger.e("resultIS", result);
//
//            Gson gson = new Gson();
//            getdistance = gson.fromJson(result.toString(), GetDistance.class);
//
//            duration = getdistance.getRoutes().get(0).getLegs().get(0).getDuration().getText();
//            result_in_kms = getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getText();
//
//            try {
//                first_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(0);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            try {
//                second_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(1);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            destination_steps = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().size();
//

        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return result_in_kms[0];
    }

    /*calling on getMarkerIconFromDrawable*/
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Logger.i(TAG, "getMarkerIconFromDrawable");
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    /*calling on onMapReady*/
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            Logger.i(TAG, "onMapReady");
            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.getUiSettings().setCompassEnabled(true);
//            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
//            mMap.setBuildingsEnabled(true);
//            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            gps = new GpsTracker(getContext());
            curlat = gps.getLatitude();
            curlon = gps.getLongitude();
            currentpos = new LatLng(curlat, curlon);


            try {
                getAddress(curlat, curlon);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentpos)      // Sets the center of the map to Mountain View
                    .zoom(AppConstants.DEFAULT_ZOOM)                   // Sets the zoom
                    //                .bearing(30)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpos, AppConstants.DEFAULT_ZOOM));


            if (markerPoints.size() > 1) {
                markerPoints.clear();
                mMap.clear();
                drawMarker(curlat, curlon);
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


//            Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_loaction);
//            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
//
//            marker = mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(curlat, curlon))
////                    .title("My Marker")
//                            .icon(markerIcon).flat(true)
//            );
//
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(curlat, curlon)));
//            animateMarker(marker, new LatLng(curlat, curlon), false);


            LatLng latLng1 = new LatLng(Double.parseDouble(destlat), Double.parseDouble(destlong));

            markerPoints.add(latLng1);

            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
            options.position(latLng1);

            //                if (markerPoints.size() == 1) {
            //                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //                } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //                }

            // Add new marker to the Google Map Android API V2
            mMap.addMarker(options);

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
                try {
                    new getdistanceVal(curlat, curlon, dest.latitude, dest.longitude).execute();
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }


            String url = getDirectionsUrl(origin, dest);
            Logger.e("url", "" + url);

            DownloadTask downloadTask = new DownloadTask(getContext());

            downloadTask.execute(url);

//                }
//            });
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

    }

    /*calling on goToLocation*/
    private void goToLocation(double latitude, double longitude, int i) {
        Logger.i(TAG, "goToLocation");
        LatLng ll = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, AppConstants.DEFAULT_ZOOM);
        mMap.animateCamera(update);

        if (marker != null) {
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .title("Test")
                .draggable(true)
                .position(new LatLng(latitude, longitude));
        marker = mMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Cant Find User Location", Toast.LENGTH_SHORT);
        } else {
//            Toast.makeText(getActivity().getApplicationContext(), location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT);

            ll = new LatLng(location.getLatitude(), location.getLongitude());

            marker.setPosition(ll);


        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {

    }
    /*calling on inviteFriendSuccess*/
    @Override
    public void inviteFriendSuccess(String msg) {
        Logger.i(TAG, "inviteFriendSuccess");
        try {
            Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on showRefferalCode*/
    @Override
    public void showRefferalCode(String refCode, String expiryDate) {
        Logger.i(TAG, "showRefferalCode");
        this.expiryDate = expiryDate;
        Logger.e("expiryDatdee", expiryDate);
        Logger.e("expiryDatdee", CommonUtils.isSubscriptionExpired(expiryDate) + "");
        if (CommonUtils.isSubscriptionExpired(expiryDate) == true) {
//Subscribe Dialog
        } else {
//QR Code Dialog
        }

    }
    /*calling on navigationRewardSuccess*/
    @Override
    public void navigationRewardSuccess(NavigationRewardsResponse navigationRewardsResponse) {
        Logger.i(TAG, "navigationRewardSuccess");
        try {
            if (navigationRewardsResponse != null && navigationRewardsResponse.getCode() == 200) {
                showdialog(false,true);
            } else {
                showdialog(false,false);
               // Toast.makeText(getActivity().getApplicationContext(), "You have arrived at your destination.", Toast.LENGTH_SHORT).show();
            }
            Bundle bundle = new Bundle();
            //bundle.put
            Utility.logFirebaseEvent(requireActivity(), bundle, AppConstants.DESTIONATION_REACHED);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on onRoutingFailure*/
    @Override
    public void onRoutingFailure(RouteException e) {
        Logger.i(TAG, "onRoutingFailure");
        progressDialog.dismiss();
        if (e != null) {
            Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }
    /*calling on onRoutingSuccess*/
    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        Logger.i(TAG, "onRoutingSuccess");
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(AppConstants.DEFAULT_ZOOM);

        mMap.moveCamera(center);

//        if(polylines.size()>0) {
//            for (Polyline poly : polylines) {
//                poly.remove();
//            }
//        }
        if (points.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i2 = 0; i2 < arrayList.size(); i2++) {

            //In case of more than 5 alternative routes
//            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(getContext(), R.color.blue_theme));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(arrayList.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            //Toast.makeText(getActivity(), "Route " + (i + 1) + ": distance - " + arrayList.get(i).getDistanceValue() + ": duration - " + arrayList.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
////        options.position(start);
////        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
////        mMap.addMarker(options);
        drawMarker(curlat, curlon);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {
        Logger.i(LOG_TAG, "Routing was cancelled.");
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private Context mContext;

        public DownloadTask(Context mContext) {
            this.mContext = mContext;
        }

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

            ParserTask parserTask = new ParserTask(mContext);


            parserTask.execute(result);

        }
    }

    private class getdistanceVal extends AsyncTask<Void, Void, Void> {
        double sourcelat, sourcelong, destlat, destlong;

        private getdistanceVal(double curlat, double curlon, double latitude, double longitude) {
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
            try {
                if (needToCallDistanceAPI) {
                    if (sourcelat!=0.0&&sourcelong!=0.0){
                        distanceroad = getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong);
                    }else{
                        distanceroad = getDistanceOnRoad(gps.getLatitude(), gps.getLongitude(), destlat, destlong);
                    }

                }
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (first_step != null && first_step.getManeuver() != null && first_step.getManeuver().equalsIgnoreCase("turn-left")) {
                    ivDirection.setImageResource(R.drawable.ic_turn_left);
                } else if (first_step != null && first_step.getManeuver() != null && first_step.getManeuver().equalsIgnoreCase("turn-right")) {
                    ivDirection.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    ivDirection.setImageResource(R.drawable.ic_keep_moving);
                }
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }

            try {
                if (second_step != null && second_step.getManeuver() != null && second_step.getManeuver().equalsIgnoreCase("turn-left")) {
                    ivarrow.setImageResource(R.drawable.ic_turn_left);
                } else if (second_step != null && second_step.getManeuver() != null && second_step.getManeuver().equalsIgnoreCase("turn-right")) {
                    ivarrow.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    ivarrow.setImageResource(R.drawable.ic_keep_moving);
                }
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }

            Logger.e("destinationstep", String.valueOf(destination_steps));
            try {
                if (first_step != null && destination_steps == 1) {
                    finaldistance = first_step.getDistance().getValue();
                    if (finaldistance <= 50) {
                        needToCallDistanceAPI = false;
//                        showdialog(true);
                        /*if (CommonUtils.isSubscriptionExpired(expiryDate) == true) {
                            showdialog(true);
                        } else {
                            mapDirectionPresenter.getWolooNavigationReward(wolooId);
                        }*/
                        mapDirectionPresenter.getWolooNavigationReward(wolooId);
                    }
                }
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }


            try {
                Logger.e("distroad", distanceroad);
                tv_distance.setText(distanceroad);
                tv_time.setText(duration);
                iv_shortdist.setText(first_step.getDistance().getText());
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
        }


    }

    public void updateDirections(){
        try {
            if (first_step != null && first_step.getManeuver() != null && first_step.getManeuver().equalsIgnoreCase("turn-left")) {
                ivDirection.setImageResource(R.drawable.ic_turn_left);
            } else if (first_step != null && first_step.getManeuver() != null && first_step.getManeuver().equalsIgnoreCase("turn-right")) {
                ivDirection.setImageResource(R.drawable.ic_arrow_right);
            } else {
                ivDirection.setImageResource(R.drawable.ic_keep_moving);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

        try {
            if (second_step != null && second_step.getManeuver() != null && second_step.getManeuver().equalsIgnoreCase("turn-left")) {
                ivarrow.setImageResource(R.drawable.ic_turn_left);
            } else if (second_step != null && second_step.getManeuver() != null && second_step.getManeuver().equalsIgnoreCase("turn-right")) {
                ivarrow.setImageResource(R.drawable.ic_arrow_right);
            } else {
                ivarrow.setImageResource(R.drawable.ic_keep_moving);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

        Logger.e("destinationstep", String.valueOf(destination_steps));
        try {
            if (first_step != null && destination_steps == 1) {
                finaldistance = first_step.getDistance().getValue();
                if (finaldistance <= 50) {
                    needToCallDistanceAPI = false;
//                        showdialog(true);
                        /*if (CommonUtils.isSubscriptionExpired(expiryDate) == true) {
                            showdialog(true);
                        } else {
                            mapDirectionPresenter.getWolooNavigationReward(wolooId);
                        }*/
                    mapDirectionPresenter.getWolooNavigationReward(wolooId);
                }
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }


        try {
            Logger.e("distroad", distanceroad);
            tv_distance.setText(distanceroad);
            tv_time.setText(duration);
            iv_shortdist.setText(first_step.getDistance().getText());
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    public void showdialog(boolean is_expired, boolean isPoints) {
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_destinationarrived);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
            TextView tv_subscribe = (TextView) dialog.findViewById(R.id.tv_subscribe);
            TextView tv_text2 = (TextView) dialog.findViewById(R.id.tv_text2);
            tv_text2.setVisibility(View.GONE);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (is_expired == false) {
//                tv_text.setText(getResources().getString(R.string.qrcode_scantext));


                if (authConfigResponse != null) {
                    String arrivedDestinationDialogText = authConfigResponse.getcUSTOMMESSAGE().getArrivedDestinationText();
                    String arrivedDestinationPoints = authConfigResponse.getcUSTOMMESSAGE().getArrivedDestinationPoints();
//                    String arrivedDestinationDialogText = authConfigResponse.getData().getcUSTOMMESSAGE().getArrivedDestinationDialogText();
//                    tv_text.setText(arrivedDestinationDialogText.replaceAll("\\\\n","\n"));
//                    String str = arrivedDestinationDialogText.replaceAll("\\\\n", "\n");
//                    Spannable spannable = new SpannableString(str);
//                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#B5AA3A")), str.indexOf("10 Woloo points."), str.indexOf("10 Woloo points.") + "10 Woloo points.".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv_text.setText(spannable);
                    tv_text.setText(arrivedDestinationDialogText.replaceAll("\\\\n","\n"));
                    if(isPoints) {
                        tv_text2.setText(arrivedDestinationPoints);
                        tv_text2.setVisibility(View.VISIBLE);
                        tv_subscribe.setVisibility(View.GONE);
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hasReachedAtDestination = true;
                                dialog.dismiss();
                                Intent intent = new Intent(context, AddReviewActivity.class);
                                intent.putExtra(AppConstants.WOLOO_ID, wolooId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        }, 3000);
                    }
                    else{
                        tv_text2.setVisibility(View.GONE);
                        tv_subscribe.setVisibility(View.VISIBLE);
                        tv_subscribe.setText("HOME");
                    }
                }
//                tv_subscribe.setText("SCAN QR CODE");
            } else {
//                tv_text.setText(getResources().getString(R.string.mapsdestination_msg));
                if (authConfigResponse != null) {
                    String subscribeNowDialogText = authConfigResponse.getcUSTOMMESSAGE().getSubscribeNowDialogText();
                    tv_text.setText(subscribeNowDialogText.replaceAll("\\\\n", "\n"));
                }
                tv_subscribe.setText("Subscribe Now");
            }


            tv_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!is_expired) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Intent i = new Intent(getContext(), WolooDashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {

                        if (dialog.isShowing())
                            dialog.dismiss();
                        Intent i = new Intent(getContext(), SubscribeActivity.class);
                        startActivity(i);

                    }

                }
            });

            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private Context mContext;

        public ParserTask(Context mContext) {
            this.mContext = mContext;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

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

            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

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
                lineOptions.color(ContextCompat.getColor(mContext, R.color.blue_theme));
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            try {
                mMap.addPolyline(lineOptions);
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
//                Toast.makeText(getActivity().getApplicationContext(), "Boundary Crossed!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(context);
        }
        String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
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
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        String key = "key=" +CommonUtils.googlemapapikey(context);

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
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
}
