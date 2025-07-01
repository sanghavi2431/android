package in.woloo.www.shopping.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.base.BaseFragment;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.invite_friend.fragments.InviteFriendFragment;
import in.woloo.www.mapdirection.GpsTracker;
import in.woloo.www.shopping.adapter.BannerAdapter;
import in.woloo.www.shopping.adapter.DashboardCategoryAdapterTop;
import in.woloo.www.shopping.adapter.HomeCategoryAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.CategoryModel;
import in.woloo.www.shopping.model.GeoCodeResponse;
import in.woloo.www.shopping.model.HomeProductModel;
import in.woloo.www.shopping.mvp.GeoCodeView;
import in.woloo.www.shopping.mvp.UserGiftCardPresenter;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.geocode.ReverseGeocodeItem;
import in.woloo.www.v2.profile.model.Profile;
import in.woloo.www.v2.profile.model.TotalCoins;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.splash.UserDetails;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends BaseFragment implements GeoCodeView {



    List<String> menuList = new ArrayList<String>();

    private String redirect_to = "";
    private String redirect_coupon_code = "";

    private  View root;

    ProgressDialog progressDialog;
    private UserGiftCardPresenter userGiftCardPresenter;

    @BindView(R.id.txtPoint)
    public  TextView txtPoint;
    @BindView(R.id.txtName)
    public  TextView txtName;
    @BindView(R.id.imgSearch)
    public  ImageView imgSearch;
    @BindView(R.id.imgCart)
    public ImageView imgCart;

    @BindView(R.id.cart_count_textview)
    public TextView cart_count_textview;

    @BindView(R.id.ivProfile)
    CircleImageView civProfileImage;

    @BindView(R.id.pincodeLayout)
    LinearLayout pincodeLayout;

    @BindView(R.id.pincodeTextview)
    TextView pincodeTextview;

    @BindView(R.id.pager)
    public  ViewPager mPager;

    @BindView(R.id.indicator)
    public CircleIndicator indicator;

    @BindView(R.id.recycler_view_cat)
    public RecyclerView  recycler_view_cat;

    @BindView(R.id.recycler_view_home_cat)
    public RecyclerView   recycler_view_home_cat;

    OtpEditText otpEditText;
    ViewGroup address_radio_group;
    ImageView cancel_popup;


    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    ProfileViewModel profileViewModel;
    private ArrayList<String> ImagesArray = new ArrayList<String>();

    public static List<List<String>> all_product_images = new ArrayList<List<String>>();
    public static List<CategoryModel> categoryList = new ArrayList<>();
    public List<CategoryModel> homeCategoryList = new ArrayList<>();
    public static List<HomeProductModel> homeProductList = new ArrayList<>();
    public static List<CategoryModel> homeSubCategoryList = new ArrayList<>();


    public DashboardCategoryAdapterTop adapterViewAndroidtop;
    public HomeCategoryAdapter homeCategoryAdapter;



    private FusedLocationProviderClient fusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    private boolean locationPermissionGranted;
    private GpsTracker gps;
    List<Address> addresses;



    // These value required  from homepage
    public static int userTotalPoints = 0, userTotalPointsBackup = 0, userGiftCardValue = 0 , totalCouponDiscount = 0;
    public static String user_id = "1" , user_name = "" , user_address ="" , user_phone="",user_email="" , user_type="customer";
    public static String selected_address_id="0";
    public static String pincode="" , current_pincode ="" , coupon_code="", coupon_value="" , coupon_value_unit="" ;
    public static  List<String> couponProductList = new ArrayList<>();
    Geocoder geocoder;
    // Usert Type: franchisee, customer , host

    public static  ArrayList<ArrayList<String>> all_cart_list = new ArrayList<ArrayList<String>>();




    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String bannerUrl = Config.hostname+"get_home_banner_api.php";
    private String categoryUrl = Config.hostname+"get_category_api.php";
    private String homeCategoryProductUrl = Config.hostname+"get_home_category_product_api.php";
    private String addressUrl = Config.hostname+"get_address_list_api.php";
    private String saveAddressUrl = Config.hostname+"save_address_api.php";







    public ShoppingFragment() {
        // Required empty public constructor
    }

    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString("redirect_to", param1);
        args.putString("redirect_coupon_code", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userGiftCardPresenter = new UserGiftCardPresenter(getContext(),ShoppingFragment.this);

        if (getArguments() != null) {
            redirect_to = getArguments().getString("redirect_to");
            redirect_coupon_code = getArguments().getString("redirect_coupon_code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (  root==null ) {
            root = inflater.inflate(R.layout.fragment_shop, container, false);
            ButterKnife.bind(this, root);
            /*LoginResponse userInfo = new CommonUtils().getUserInfo(getContext());
            user_id = String.valueOf(userInfo.getData().getUser().getId());
            user_name = userInfo.getData().getUser().getName();
            user_address ="" , user_phone="",user_email="" , user_type="host";*/
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            initViews();
            setLiveData();
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).showFooter();

            // set updated coins

            txtPoint.setText("Point: "+String.valueOf(userTotalPoints));

            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }




        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void initViews() {
        try{

            try {


                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                gps = new GpsTracker(getContext());
                LatLng current = new LatLng(gps.getLatitude(), gps.getLongitude());

                geocoder = new Geocoder(getContext());

                addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 10);
                Address address = addresses.get(0);
                current_pincode = address.getPostalCode();

                if (pincode.equalsIgnoreCase("")) {

                    pincode = current_pincode;

                }

                pincodeTextview.setText(pincode);


            }
            catch(Exception e) {

                //pincodeTextview.setText("Enter Pincode");
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                gps = new GpsTracker(getContext());
//                String lat = String.valueOf(gps.getLatitude());
//                String lng = String.valueOf(gps.getLongitude());
//                userGiftCardPresenter.getReverseGeocoding(lat,lng);
                profileViewModel.reverseGeocoding(gps.getLatitude(),gps.getLongitude());

            }






            // Toast.makeText(getActivity().getApplicationContext(),"LAt:"+gps.getLatitude()+" pinoce:"+address.getPostalCode(),Toast.LENGTH_SHORT).show();
            getUserInformationWS();
            getUserInformation();
            txtName.setText(user_name);

            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }






            // Get Home banner
            getHomeBanner();

            // Get Top Category
            getTopCategory();

            // Get Home Category with Product



            // Show Popup pincode
            if(pincode.equalsIgnoreCase("")) {
                // showPincodePopup();
            }
            else {

                getCategoryProduct();
            }


            pincodeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPincodePopup();

                }
            });

            pincodeTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPincodePopup();

                }
            });



            imgSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    try {

                        SearchFragment myFragment = new SearchFragment();
                        Bundle b = new Bundle();
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    catch(Exception e) {


                    }

                }
            });


            civProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    try {




                        MyOrdersFragment myFragment = new MyOrdersFragment();
                        Bundle b = new Bundle();
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    catch(Exception e) {


                    }

                }
            });




            // Go to cart

            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(ShoppingFragment.all_cart_list.size() != 0 ) {



                        Bundle bundle = new Bundle();
                        bundle.putString("pincode", ShoppingFragment.pincode);
                        Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "shopping_cart_icon_click");



                        CartFragment myFragment = new CartFragment();
                        Bundle b = new Bundle();
                        b.putString("selected_address_id", ShoppingFragment.selected_address_id);
                        b.putString("selected_address", ShoppingFragment.user_address);
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Your Cart is empty",Toast.LENGTH_LONG).show();
                    }
                }
            });







            // Redirect to coupon code if passes

            try {

                if(redirect_to.equalsIgnoreCase("coupon")) {

                    redirect_to = "";
                    coupon_code = redirect_coupon_code;

                    ProductListFragment myFragment = new ProductListFragment();
                    Bundle b = new Bundle();
                    b.putString("catId",coupon_code);
                    b.putString("catName", coupon_code+" Products");
                    b.putString("cat_type", "coupon");
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

            }
            catch(Exception e) {


            }

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        profileViewModel.observeReverseGeocoding().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ArrayList<ReverseGeocodeItem>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<ReverseGeocodeItem>> geoCodeResponse) {
                if (geoCodeResponse != null && geoCodeResponse.getData() != null) {
                    try {

                        List<ReverseGeocodeItem> data = geoCodeResponse.getData();

                        List<ReverseGeocodeItem.AddressComponentsItem> address_component = data.get(0).getAddressComponents();

                        current_pincode = address_component.get(7).getLongName();

                    } catch(Exception e) { }

                    // current_pincode = geoCodeResponse;

                    if (pincode.equalsIgnoreCase("")) {

                        pincode = current_pincode;

                    }

                    pincodeTextview.setText(pincode);

                    // Show Popup pincode
                    if(pincode.equalsIgnoreCase("")) {
                        showPincodePopup();
                    }
                    else {

                        getCategoryProduct();
                    }
                }else{
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }


    public void showPincodePopup()
    {
        final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
        dialog.setContentView(R.layout.custom_pincode_popup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        otpEditText = dialog.findViewById(R.id.oev_view);
        cancel_popup = dialog.findViewById(R.id.cancel_popup);
        TextView  add_address_button = dialog.findViewById(R.id.add_address_button);
        TextView  usePincode = dialog.findViewById(R.id.usePincode);
        LinearLayout add_address_layout = dialog.findViewById(R.id.add_address_layout);
        EditText name_edittext = dialog.findViewById(R.id.name);
        EditText pincode_edittext = dialog.findViewById(R.id.pincode);
        EditText city_edittext = dialog.findViewById(R.id.city);
        EditText state_edittext = dialog.findViewById(R.id.state);
        EditText area_edittext = dialog.findViewById(R.id.area);
        EditText flat_building_edittext = dialog.findViewById(R.id.flat_building);
        EditText landmark_edittext = dialog.findViewById(R.id.landmark);
        Button save_address_button = dialog.findViewById(R.id.save_address);

        usePincode.setVisibility(View.GONE);



        add_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_address_layout.setVisibility(View.VISIBLE);
            }
        });

        save_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name_value = name_edittext.getText().toString();
                String pincode_value = pincode_edittext.getText().toString();
                String city_value = city_edittext.getText().toString();
                String state_value = state_edittext.getText().toString();
                String area_value = area_edittext.getText().toString();
                String flat_building_value = flat_building_edittext.getText().toString();
                String landmark_value = landmark_edittext.getText().toString();

                if(name_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
                }
                else if(pincode_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter Pincode",Toast.LENGTH_SHORT).show();
                }
                else if(city_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter City",Toast.LENGTH_SHORT).show();
                }
                else if(state_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter State",Toast.LENGTH_SHORT).show();
                }
                else if(area_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter Area",Toast.LENGTH_SHORT).show();
                }
                else if(flat_building_value.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter Flat No and Bulding Name",Toast.LENGTH_SHORT).show();
                }

                else {

                    pincode = pincode_value;
                    String address_val = flat_building_value+" , "+landmark_value+" , "+ area_value+" , "+city_value+"-"+pincode_value+" "+state_value;
                    user_address = address_val;

                    pincodeTextview.setText(pincode);

                    getCategoryProduct();

                    dialog.dismiss();


                    Bundle bundle = new Bundle();
                    bundle.putString("name",name_value);
                    bundle.putString("city",city_value);
                    bundle.putString("state",state_value);
                    bundle.putString("area",area_value);
                    bundle.putString("flat_building",flat_building_value);
                    bundle.putString("landmark",landmark_value);
                    bundle.putString("pincode", pincode_value);
                    Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "shopping_add_address");




                    saveAddressApi(ShoppingFragment.user_id, name_value,ShoppingFragment.user_phone, pincode_value, city_value, state_value, area_value, flat_building_value, landmark_value);

                }

            }
        });















        //otpEditText.set



        otpEditText.setOnCompleteListener(new OnCompleteListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(String value) {

                pincode = value;
                pincodeTextview.setText(pincode);

                usePincode.setVisibility(View.VISIBLE);

                // getCategoryProduct();
                //  dialog.dismiss();
            }
        });

        usePincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCategoryProduct();
                dialog.dismiss();
            }
        });


        cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });




        // add radio button address address_radio_group
        address_radio_group = dialog.findViewById(R.id.address_radio_group);




        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(getContext());
        mRequestQueue.getCache().remove(addressUrl+"?user_id="+user_id);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, addressUrl+"?user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {



                    JSONArray obj = new JSONArray(response);

                    if(obj.length() == 0)
                    {
                        add_address_layout.setVisibility(View.VISIBLE);
                        add_address_button.setVisibility(View.GONE);
                    }


                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject catObj = (JSONObject) obj.get(i);

                        RadioButton button = new RadioButton(getContext());
                        button.setId(i);
                        button.setPadding(5,5,5,5);
                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{


                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[] {

                                        Color.BLACK //disabled
                                        ,Color.BLUE //enabled

                                }
                        );


                        button.setButtonTintList(colorStateList);//set the color tint list

                        String address_val = catObj.getString("flat_building")+" , "+catObj.getString("landmark")+" , "+ catObj.getString("area")+" , "+catObj.getString("city")+"-"+catObj.getString("pincode")+" "+catObj.getString("state");
                        String pincode_val = catObj.getString("pincode");
                        button.setText(address_val);

                        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                user_address = address_val;
                                pincode = pincode_val;
                                pincodeTextview.setText(pincode);

                                getCategoryProduct();

                                dialog.dismiss();
                            }
                        });


                        address_radio_group.addView(button);



                    }


                }
                catch (Exception e) {



                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.getCache().clear();
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);


















        dialog.show();
    }








    private void navigateToInviteFriendScreen() {
        try{
            ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).loadMenuFragment(InviteFriendFragment.newInstance(true), "InviteFriendFragment");
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    private void getUserInformationWS() {
        try {
            profileViewModel.getUserProfile();
            profileViewModel.observeUserProfile().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserProfile>>() {
                @Override
                public void onChanged(BaseResponse<UserProfile> userProfileBaseResponse) {
                    if(userProfileBaseResponse!=null && userProfileBaseResponse.getData()!=null){
                        setProfileResponse(userProfileBaseResponse.getData());
                    }else{
                        Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        WolooApplication.setErrorMessage("");
                    }
                }
            });
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }

    private void getUserInformation() {
        try {
            UserDetails getUserInfo= commonUtils.getUserInfo();
            //LoginResponse.User user=getUserInfo.getData().getUser();
            String userType="customer";
            if(getUserInfo.getRoleId()!=null) {
                switch (getUserInfo.getRoleId()) {
                    case "9":
                        userType = "host";
                        break;
                    case "10":
                        userType = "franchisee";
                        break;
                }
            }
            else
                userType = "customer";
            setUserInformationToShopping(""+getUserInfo.getId(),""+getUserInfo.getName(),""+getUserInfo.getAddress(),""+getUserInfo.getEmail(),""+getUserInfo.getMobile(), userType);
        } catch (Exception e) {
            commonUtils.printStackTrace(e);
        }
    }

    private void setUserInformationToShopping(String userId, String userName, String userAddress, String userEmail, String userMobile, String userType){
        user_id = userId;
        user_name =userName;
        //  user_address =userAddress;
        user_phone=userMobile;
        user_email=userEmail;
        user_type = userType;


        try {

            if (user_name.equalsIgnoreCase("")) {
                txtName.setText(getString(R.string.guest));
            }

        } catch(Exception e) {  txtName.setText(getString(R.string.guest)); }

    }

    public void setProfileResponse(UserProfile userProfile) {
        if (userProfile !=null) {
            try {
                Profile userData= userProfile.getProfile();
                String userType="customer";
                if(userData.getRoleId()!=null) {
                    switch (userData.getRoleId()) {
                        case "9":
                            userType = "host";
                            break;
                        case "10":
                            userType = "franchisee";
                            break;
                    }
                }
                else
                    userType = "customer";
                setUserInformationToShopping(""+userData.getId(),userData.getName(),userData.getAddress(),userData.getEmail(),userData.getMobile(),userType);
            } catch (Exception e) {
                commonUtils.printStackTrace(e);
            }
            if (!TextUtils.isEmpty(userProfile.getProfile().getName())) {
                txtName.setText(userProfile.getProfile().getName());

                try {

                    if (userProfile.getProfile().getName().equalsIgnoreCase("") || userProfile.getProfile().getName().equalsIgnoreCase(" ")) {
                        txtName.setText(getString(R.string.guest));
                    }

                } catch(Exception e) {  txtName.setText(getString(R.string.guest)); }


            } else {
                if (commonUtils.isLoggedIn()) {
                    txtName.setText(R.string.guest);
                } else {
                    txtName.setText(getString(R.string.guest));
                }
            }

            if (TextUtils.isEmpty(userProfile.getProfile().getAvatar())) {
                civProfileImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle));
            } else {
                if (userProfile.getProfile().getAvatar().trim().equals("users/default.png") || userProfile.getProfile().getAvatar().trim().equals("default.png")) {
                    ImageUtil.loadImageProfile(getContext(), civProfileImage, BuildConfig.BASE_URL + "public/userProfile/default.png");
                } else {
                    ImageUtil.loadImageProfile(getContext(), civProfileImage, BuildConfig.BASE_URL + "public/userProfile/" + userProfile.getProfile().getAvatar());
                }
            }

            setUserCoins(userProfile.getTotalCoins());
        }

    }

    public void setUserCoins(TotalCoins totalCoins) {
        try {
            if (totalCoins != null) {

                if(userTotalPointsBackup == 0) {
                    txtPoint.setText(String.format(getString(R.string.point_format), "" + totalCoins.getTotalCoins()));

                    userTotalPoints = totalCoins.getTotalCoins();

                    userTotalPointsBackup = userTotalPoints;

                }

            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    private void getHomeBanner() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, bannerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                ImagesArray.clear();

                try {
                    JSONArray obj = new JSONArray(response);


                    for (int i = 0; i < obj.length(); i++) {

                        JSONObject catObj = (JSONObject) obj.get(i);
                        ImagesArray.add(catObj.getString("image"));
                    }


                    mPager.setAdapter(new BannerAdapter(getActivity(), ImagesArray, "dashboard"));

                    indicator.setViewPager(mPager);

                    // Auto start of viewpager


                    final Handler handler = new Handler();


                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == ImagesArray.size()) {
                                currentPage = 0;
                            }


                            mPager.setCurrentItem(currentPage++, true);

                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 4000, 4000);

                }
                catch (Exception e) { }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private void getTopCategory() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, categoryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();
                categoryList.clear();



                try {
                    JSONArray obj = new JSONArray(response);

                    adapterViewAndroidtop = new DashboardCategoryAdapterTop(categoryList);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recycler_view_cat.setLayoutManager(layoutManager);
                    recycler_view_cat.setItemAnimator(new DefaultItemAnimator());
                    recycler_view_cat.setAdapter(adapterViewAndroidtop);
                    recycler_view_cat.setNestedScrollingEnabled(false);

                    CategoryModel yourwork;

                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject catObj = (JSONObject) obj.get(i);

                        yourwork = new CategoryModel(catObj.getString("image"), catObj.getString("name"), catObj.getString("id"), "dashboard",catObj.getString("banner1_image"),catObj.getString("banner2_image"),catObj.getString("banner3_image"));


                        categoryList.add(yourwork);


                    }

                    adapterViewAndroidtop.notifyDataSetChanged();












                }
                catch (Exception e) {

                    //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private void getCategoryProduct() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, homeCategoryProductUrl+"?user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();


                try {

                    recycler_view_home_cat.setNestedScrollingEnabled(false);
                    recycler_view_home_cat.setHasFixedSize(false);

                    homeCategoryList.clear();
                    // recyclerView.setNestedScrollingEnabled(false);
                    homeCategoryAdapter = new HomeCategoryAdapter(homeCategoryList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recycler_view_home_cat.setLayoutManager(mLayoutManager);
                    recycler_view_home_cat.setItemAnimator(new DefaultItemAnimator());

                    recycler_view_home_cat.setAdapter(homeCategoryAdapter);





                    JSONArray obj = new JSONArray(response);

                    JSONArray category_info = obj.getJSONArray(0);
                    JSONArray product_info = obj.getJSONArray(1);
                    JSONArray product_images = obj.getJSONArray(2);
                    JSONArray sub_category_info = obj.getJSONArray(3);





                    int c=0;
                    CategoryModel yourwork;
                    for (int i = 0; i < category_info.length(); i++) {
                        c++;
                        JSONObject catObj = (JSONObject) category_info.get(i);


                        yourwork = new CategoryModel("", catObj.getString("name"), catObj.getString("id"), "dashboard", catObj.getString("banner1_image"),catObj.getString("banner2_image"),catObj.getString("banner3_image"));


                        homeCategoryList.add(yourwork);


                    }



                    homeSubCategoryList.clear();



                    CategoryModel yourwork2;
                    for (int i = 0; i < sub_category_info.length(); i++) {

                        JSONObject catObj = (JSONObject) sub_category_info.get(i);


                        yourwork2 = new CategoryModel(catObj.getString("image"), catObj.getString("name"), catObj.getString("id"), catObj.getString("category_id"), "","","");


                        homeSubCategoryList.add(yourwork2);


                    }







                    /***  ADD Product  ***/

                    homeProductList.clear();
                    HomeProductModel yourwork1;

                    for (int i = 0; i < product_info.length(); i++) {

                        JSONObject catObj = (JSONObject) product_info.get(i);

                        yourwork1 = new HomeProductModel(catObj.getString("image"), catObj.getString("name"), catObj.getString("id"), "dashboard",catObj.getString("cat_id"),catObj.getString("desc"),catObj.getString("price"));

                        homeProductList.add(yourwork1);
                    }



                    all_product_images.clear();
                    for (int i = 0; i < product_images.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product_images.get(i);




                        ArrayList<String>single_product=new ArrayList<String>();

                        single_product.add(yourworkObj.getString("product_id"));
                        single_product.add(yourworkObj.getString("img"));


                        all_product_images.add(single_product);


                    }







                    homeCategoryAdapter.notifyDataSetChanged();





                }
                catch (Exception e) {

                    //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }



    private void saveAddressApi(String user_id,String name, String phone,String pincode_value, String city_value, String state_value, String area_value, String flat_builing_value, String landmark_value) {


        String postUrl = saveAddressUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", user_id);
            postData.put("name", name);
            postData.put("phone", phone);
            postData.put("pincode", pincode_value);
            postData.put("city", city_value);
            postData.put("state", state_value);
            postData.put("area", area_value);
            postData.put("flat_builing", flat_builing_value);
            postData.put("landmark", landmark_value);

        } catch (JSONException e) {
             CommonUtils.printStackTrace(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);

                Toast.makeText(getActivity().getApplicationContext(),"Address Added Successfully",Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.printStackTrace(error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void setGeoCodeResponse(GeoCodeResponse geoCodeResponse) {




        try {

            List<GeoCodeResponse.DataItem> data = geoCodeResponse.getData();

            List<GeoCodeResponse.AddressComponentsItem> address_component = data.get(0).getAddressComponents();

            current_pincode = address_component.get(7).getLongName();

        } catch(Exception e) { }





        // current_pincode = geoCodeResponse;

        if (pincode.equalsIgnoreCase("")) {

            pincode = current_pincode;

        }

        pincodeTextview.setText(pincode);

        // Show Popup pincode
        if(pincode.equalsIgnoreCase("")) {
            showPincodePopup();
        }
        else {

            getCategoryProduct();
        }



    }
}