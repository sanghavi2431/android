package in.woloo.www.mapdirection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.base.BaseActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.customview.CurvedBottomNavigationView;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.giftcard.mvp.GiftCardPresenter;
import in.woloo.www.home.fragments.WolooStoreInfoFragment;
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.more.models.SubscriptionStatusResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.models.VoucherDetailsResponse;
import in.woloo.www.more.mvp.MorePresenter;
import in.woloo.www.more.mvp.MoreView;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;


public class MapDirection extends BaseActivity implements MoreView {


//    @BindView(R.id.tv_woloo_store)
//    TextView tv_woloo_store;

    @BindView(R.id.ll_bottom_nav)
    FrameLayout ll_bottom_nav;

//    @BindView(R.id.ll_nav_view)
//    LinearLayout ll_nav_view;

//    @BindView(R.id.nav_view)
//    CurvedBottomNavigationView nav_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imgSearch)
    ImageView imgSearch;

//    @BindView(R.id.ivProfile)
//    CircleImageView ivProfile;

//    @BindView(R.id.txtName)
//    TextView txtName;


    private boolean isSectionShow=false;
    private int height;
    private MapsFragment mapsfragment;
    private  WolooStoreInfoFragment wolooStoreInfoFragment;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private MorePresenter morePresenter;

    LocationManager locationManager;
    Context mContext;
    private String destlat="";
    private String destlong="";
    private int wolooId;
    private String tag;
    private int SETTINGS_REQUEST = 1020;

    //    LocationListener locationListenerGPS=new LocationListener() {
//        @Override
//        public void onLocationChanged(android.location.Location location) {
//            double latitude=location.getLatitude();
//            double longitude=location.getLongitude();
//            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
//            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_directionlayout);
        ButterKnife.bind(this);

        Intent i=getIntent();
        destlat=i.getStringExtra("destlat");
        destlong=i.getStringExtra("destlong");
        tag=i.getStringExtra("tag");
        wolooId=i.getIntExtra("wolooId",0);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        height= displayMetrics.heightPixels;
        mContext=this;
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
//                2000,
//                10, locationListenerGPS);

//        tv_woloo_store.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                try {
////                    mapsfragment.hideAndShow(isSectionShow);
////                    hideAndShow(isSectionShow);
////                } catch (Exception e) {
////                     CommonUtils.printStackTrace(e);
////                }
//            }
//        });




        mapsfragment=new MapsFragment();



        Bundle args = new Bundle();
        args.putString("destlat", destlat);
        args.putString("destlong", destlong);
        args.putInt("wolooId",wolooId);
        args.putString("tag",tag);
        mapsfragment.setArguments(args);



        loadFragment(mapsfragment,"mapsfragment");
//        nav_view.getMenu().getItem(2).setChecked(true);
//        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getLocationPermission();
        //loadMarkerFragment(new WolooStoreInfoFragment(),"WolooStoreInfoFragment");

        imgSearch.setOnClickListener(v -> {
            startActivity(new Intent(MapDirection.this, SearchWolooActivity.class));
        });
        morePresenter=new MorePresenter(MapDirection.this,this);
        morePresenter.getProfile();
//        GiftCardPresenter  giftCardPresenter=new GiftCardPresenter(MapDirection.this,);
//        giftCardPresenter.sendGiftCard(500,8880881559l);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //kill this runnable when ever you want to stop displaying toast like this
//        showLocationHandler.removeCallbacks(showLocationRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }
    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapDirection.this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Your location setting is not enabled. Please enable it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
     /*       AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapDirection.this);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();*/
        }
}
    public void hideToolbar(){
        try{
            toolbar.setVisibility(View.GONE);
        }catch (Exception ex){
            CommonUtils.printStackTrace(ex);
        }
    }

    public void showToolbar(){
        try {
            toolbar.setVisibility(View.VISIBLE);
        }catch (Exception ex){
            CommonUtils.printStackTrace(ex);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            bottomViewTransparent();
            switch (item.getItemId()){
               /* case R.id.navigation_shop:
                    break;*/
                case R.id.navigation_inviteFriend:
                    break;
                case R.id.navigation_home:
                    loadFragment(mapsfragment,"mapsfragment");
                    bottomViewTransparent90();
                    break;
                case R.id.navigation_chatty_cat:
                    break;
                case R.id.navigation_more:
                    loadFragment(new MoreFragment(),"mapsfragment");
                    break;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment,String homeTah){
        try{
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frm_contant,fragment,homeTah);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }catch (Exception ex){
            CommonUtils.printStackTrace(ex);
        }
    }

    public void loadMenuFragment(Fragment fragment,String homeTah){
        try{
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frm_contant,fragment,homeTah);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }catch (Exception ex){
            CommonUtils.printStackTrace(ex);
        }
    }

    public void loadMarkerFragmentWithIndex(int index, List<NearByStoreResponse.Data> dataList){
        wolooStoreInfoFragment=new WolooStoreInfoFragment();
        wolooStoreInfoFragment.setIndex(index);
        wolooStoreInfoFragment.setDataList(dataList);
        loadMarkerFragment(wolooStoreInfoFragment,"WolooStoreInfoFragment");
    }

    public void removeWolooStoreInfo(){
        try {
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(wolooStoreInfoFragment).commit();
            //  fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frm_marker_detail)).commit();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

  /*  public void moveMarkerToIndex(int index){
        mapsfragment.animateCameraToMarkerPosition(index);
    }
    public void animateCameraToMarkerPosition(int position) {
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(markerList.get(position).getPosition())      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mma.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);
    }
*/

    public void loadMarkerFragment(Fragment fragment,String homeTah){
        try{
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out_up  // popExit
            );
            fragmentTransaction.replace(R.id.frm_marker_detail,fragment,homeTah);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    public void hideAndShow(boolean status){
        if (status){
            isSectionShow=false;
            bottomViewTransparent90();
        }else {
            isSectionShow=true;
            bottomViewTransparent();
        }

    }

    private void bottomViewTransparent90() {
//        fadeInAnimation(tv_woloo_store);
//        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent_black_90));
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent_black_90));
    }

    private void bottomViewTransparent() {
//        fadeOutAnimation(tv_woloo_store);
//        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent));
        ll_bottom_nav.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent));
    }

    private void fadeInAnimation(TextView view){
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        view.startAnimation(animFadeIn);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutAnimation(TextView view){
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        view.startAnimation(animFadeOut);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }else{
                    showLocationPermissionDeniedDialog();
//                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
        /*if (mapsfragment!=null) {
            mapsfragment.updateLocationUI();
        }*/
    }

    private void showLocationPermissionDeniedDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapDirection.this);
        alertDialog.setTitle("Location Permission");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Your location permission is denied. Please enabled it in settings menu.");
        alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert=alertDialog.create();
        alert.show();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            mapsfragment.getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    @Override
    public void setProfileResponse(ViewProfileResponse viewProfileResponse) {
        if(viewProfileResponse != null) {
            if (!TextUtils.isEmpty(viewProfileResponse.getUserData().getName())) {
//                txtName.setText(commonUtils.getFirstLaterCaps(viewProfileResponse.getUserData().getName()));
            } else {
//                txtName.setText("--");
            }
//            txtName.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(viewProfileResponse.getUserData().getAvatar())) {
//                ivProfile.setImageDrawable(ContextCompat.getDrawable(MapDirection.this, R.drawable.ic_account_circle));
            } else {
//                ImageUtil.loadImageProfile(MapDirection.this, ivProfile, AppConstants.DEFAULT_BASE_URL_FOR_PROFILE_IMAGE + viewProfileResponse.getUserData().getAvatar());
            }
        }
    }

    @Override
    public void editProfileSuccess() {

    }

    @Override
    public void userCoinsResponseSuccess(UserCoinsResponse userCoinsResponse) {

    }

    @Override
    public void setSubscriptionResponse(SubscriptionStatusResponse subscriptionStatusResponse) {

    }

    @Override
    public void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse) {

    }

    @Override
    public void setVoucherResponse(VoucherDetailsResponse voucherDetailsResponse) {

    }

    @Override
    public void onBackPressed() {
        if(!MapsFragment.hasReachedAtDestination){
            super.onBackPressed();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SETTINGS_REQUEST){
//            getLocationPermission();
//            Logger.e("Permission","Ask Again");
//        }
//    }
}
