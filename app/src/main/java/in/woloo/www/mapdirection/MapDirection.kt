package `in`.woloo.www.mapdirection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseActivity
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchWolooActivity
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.WolooStoreInfoFragment
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.more.fragments.MoreFragment
import `in`.woloo.www.more.models.SubscriptionStatusResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.models.VoucherDetailsResponse
import `in`.woloo.www.more.mvp.MorePresenter
import `in`.woloo.www.more.mvp.MoreView


class MapDirection : BaseActivity(), MoreView {
    //    @BindView(R.id.tv_woloo_store)
    //    TextView tv_woloo_store;
    @JvmField
    @BindView(R.id.ll_bottom_nav)
    var ll_bottom_nav: FrameLayout? = null

    //    @BindView(R.id.ll_nav_view)
    //    LinearLayout ll_nav_view;
    //    @BindView(R.id.nav_view)
    //    CurvedBottomNavigationView nav_view;
    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.imgSearch)
    var imgSearch: ImageView? = null


    //    @BindView(R.id.ivProfile)
    //    CircleImageView ivProfile;
    //    @BindView(R.id.txtName)
    //    TextView txtName;
    private var isSectionShow = false
    private var height = 0
    private var mapsfragment: MapsFragment? = null
    private var wolooStoreInfoFragment: WolooStoreInfoFragment? = null
    var isLocationPermissionGranted: Boolean = false
        private set
    private var morePresenter: MorePresenter? = null

    var locationManager: LocationManager? = null
    var mContext: Context? = null
    private var destlat: String? = ""
    private var destlong: String? = ""
    private var wolooId = 0
    private var tag: String? = null
    private val SETTINGS_REQUEST = 1020

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_directionlayout)
        ButterKnife.bind(this)

        val i = intent
        destlat = i.getStringExtra("destlat")
        destlong = i.getStringExtra("destlong")
        tag = i.getStringExtra("tag")
        wolooId = i.getIntExtra("wolooId", 0)

        val displayMetrics = resources.displayMetrics
        height = displayMetrics.heightPixels
        mContext = this
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager


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
        mapsfragment = MapsFragment()


        val args = Bundle()
        args.putString("destlat", destlat)
        args.putString("destlong", destlong)
        args.putInt("wolooId", wolooId)
        args.putString("tag", tag)
        mapsfragment!!.arguments = args



        loadFragment(mapsfragment!!, "mapsfragment")
        //        nav_view.getMenu().getItem(2).setChecked(true);
//        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        locationPermission

        //loadMarkerFragment(new WolooStoreInfoFragment(),"WolooStoreInfoFragment");
        imgSearch!!.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MapDirection,
                    SearchWolooActivity::class.java
                )
            )
        }
        morePresenter = MorePresenter(this@MapDirection, this)
        morePresenter!!.profile
        //        GiftCardPresenter  giftCardPresenter=new GiftCardPresenter(MapDirection.this,);
//        giftCardPresenter.sendGiftCard(500,8880881559l);
    }


    override fun onDestroy() {
        super.onDestroy()
        //kill this runnable when ever you want to stop displaying toast like this
//        showLocationHandler.removeCallbacks(showLocationRunnable);
    }

    override fun onResume() {
        super.onResume()
        isLocationEnabled
    }

    private val isLocationEnabled: Unit
        get() {
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val alertDialog =
                    AlertDialog.Builder(this@MapDirection)
                alertDialog.setTitle("Enable Location")
                alertDialog.setCancelable(false)
                alertDialog.setMessage("Your location setting is not enabled. Please enable it in settings menu.")
                alertDialog.setPositiveButton(
                    "Location Settings"
                ) { dialog, which ->
                    val intent =
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                alertDialog.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    dialog.cancel()
                    finish()
                }
                val alert = alertDialog.create()
                alert.show()
            } else {
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

    fun hideToolbar() {
        try {
            toolbar!!.visibility = View.GONE
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    fun showToolbar() {
        try {
            toolbar!!.visibility = View.VISIBLE
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            bottomViewTransparent()
            when (item.itemId) {
                R.id.navigation_inviteFriend -> {}
                R.id.navigation_location -> {}
                R.id.navigation_more -> loadFragment(MoreFragment(), "mapsfragment")
            }
            false
        }

    fun loadFragment(fragment: Fragment, homeTah: String?) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frm_contant, fragment, homeTah)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    fun loadMenuFragment(fragment: Fragment, homeTah: String?) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frm_contant, fragment, homeTah)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    fun loadMarkerFragmentWithIndex(index: Int, dataList: List<NearByStoreResponse.Data?>?) {
        wolooStoreInfoFragment = WolooStoreInfoFragment()
        wolooStoreInfoFragment!!.setIndex(index)
        wolooStoreInfoFragment!!.setDataList(dataList)
        loadMarkerFragment(wolooStoreInfoFragment!!, "WolooStoreInfoFragment")
    }

    @SuppressLint("CommitTransaction")
    fun removeWolooStoreInfo() {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction().remove(wolooStoreInfoFragment!!).commit()
            //  fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frm_marker_detail)).commit();
        } catch (e: Exception) {
            printStackTrace(e)
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
    fun loadMarkerFragment(fragment: Fragment, homeTah: String?) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_up,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,  // popEnter
                R.anim.slide_out_up // popExit
            )
            fragmentTransaction.replace(R.id.frm_marker_detail, fragment, homeTah)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    fun hideAndShow(status: Boolean) {
        if (status) {
            isSectionShow = false
            bottomViewTransparent90()
        } else {
            isSectionShow = true
            bottomViewTransparent()
        }
    }

    private fun bottomViewTransparent90() {
//        fadeInAnimation(tv_woloo_store);
//        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent_black_90));
        ll_bottom_nav!!.setBackgroundColor(
            ContextCompat.getColor(
                baseContext,
                R.color.transparent_black_90
            )
        )
    }

    private fun bottomViewTransparent() {
//        fadeOutAnimation(tv_woloo_store);
//        ll_nav_view.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.transparent));
        ll_bottom_nav!!.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.transparent))
    }

    private fun fadeInAnimation(view: TextView) {
        val animFadeIn = AnimationUtils.loadAnimation(
            applicationContext, R.anim.fade_in
        )
        view.startAnimation(animFadeIn)
        animFadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }

    private fun fadeOutAnimation(view: TextView) {
        val animFadeOut = AnimationUtils.loadAnimation(
            applicationContext, R.anim.fade_out
        )
        view.startAnimation(animFadeOut)
        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.INVISIBLE
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    isLocationPermissionGranted = true
                } else {
                    showLocationPermissionDeniedDialog()
                    //                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
        /*if (mapsfragment!=null) {
            mapsfragment.updateLocationUI();
        }*/
    }

    private fun showLocationPermissionDeniedDialog() {
        val alertDialog = AlertDialog.Builder(this@MapDirection)
        alertDialog.setTitle("Location Permission")
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Your location permission is denied. Please enabled it in settings menu.")
        alertDialog.setPositiveButton(
            "Location Settings"
        ) { dialog, which ->
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivity(intent)
            finish()
        }
        alertDialog.setNegativeButton(
            "Go Back"
        ) { dialog, which ->
            dialog.cancel()
            finish()
        }
        val alert = alertDialog.create()
        alert.show()
    }

    val locationPermission: Unit
        /**
         * Prompts the user for permission to use the device location.
         */
        get() {
            /*
             * Request location permission, so that we can get the location of the
             * device. The result of the permission request is handled by a callback,
             * onRequestPermissionsResult.
             */
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                isLocationPermissionGranted = true
                mapsfragment!!.deviceLocation
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }

    override fun setProfileResponse(viewProfileResponse: ViewProfileResponse?) {
        if (viewProfileResponse != null) {
            if (!TextUtils.isEmpty(viewProfileResponse.userData!!.name)) {
//                txtName.setText(commonUtils.getFirstLaterCaps(viewProfileResponse.getUserData().getName()));
            } else {
//                txtName.setText("--");
            }
            //            txtName.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(viewProfileResponse.userData!!.avatar)) {
//                ivProfile.setImageDrawable(ContextCompat.getDrawable(MapDirection.this, R.drawable.ic_account_circle));
            } else {
//                ImageUtil.loadImageProfile(MapDirection.this, ivProfile, AppConstants.DEFAULT_BASE_URL_FOR_PROFILE_IMAGE + viewProfileResponse.getUserData().getAvatar());
            }
        }
    }

    override fun editProfileSuccess() {
    }

    override fun userCoinsResponseSuccess(userCoinsResponse: UserCoinsResponse?) {
    }

    override fun setSubscriptionResponse(subscriptionStatusResponse: SubscriptionStatusResponse?) {
    }

    override fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?) {
    }

    override fun setVoucherResponse(voucherDetailsResponse: VoucherDetailsResponse?) {
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (!MapsFragment.hasReachedAtDestination) {
            super.onBackPressed()
        }
    } //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == SETTINGS_REQUEST){
    //            getLocationPermission();
    //            Logger.e("Permission","Ask Again");
    //        }
    //    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
