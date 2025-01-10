package `in`.woloo.www.shopping.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.broooapps.otpedittext2.OnCompleteListener
import com.broooapps.otpedittext2.OtpEditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.base.BaseFragment
import `in`.woloo.www.application_kotlin.model.lists_models.ReverseGeocodeItem
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.invite_friend.fragments.InviteFriendFragment.Companion.newInstance
import `in`.woloo.www.mapdirection.GpsTracker
import `in`.woloo.www.more.editprofile.profile.model.TotalCoins
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.shopping.adapter.BannerAdapter
import `in`.woloo.www.shopping.adapter.DashboardCategoryAdapterTop
import `in`.woloo.www.shopping.adapter.HomeCategoryAdapter
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.model.CategoryModel
import `in`.woloo.www.shopping.model.GeoCodeResponse
import `in`.woloo.www.shopping.model.HomeProductModel
import `in`.woloo.www.shopping.mvp.GeoCodeView
import `in`.woloo.www.shopping.mvp.UserGiftCardPresenter
import `in`.woloo.www.utils.CircleImageView
import `in`.woloo.www.utils.ImageUtil.loadImageProfile
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import me.relex.circleindicator.CircleIndicator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingFragment : BaseFragment(), GeoCodeView {
    var menuList: List<String> = ArrayList()

    private var redirect_to: String? = ""
    private var redirect_coupon_code: String? = ""

    private lateinit var root: View

    var progressDialog: ProgressDialog? = null
    private var userGiftCardPresenter: UserGiftCardPresenter? = null

    @JvmField
    @BindView(R.id.txtPoint)
    var txtPoint: TextView? = null

    @JvmField
    @BindView(R.id.txtName)
    var txtName: TextView? = null

    @JvmField
    @BindView(R.id.imgSearch)
    var imgSearch: ImageView? = null

    @JvmField
    @BindView(R.id.imgCart)
    var imgCart: ImageView? = null

    @JvmField
    @BindView(R.id.cart_count_textview)
    var cart_count_textview: TextView? = null

    @JvmField
    @BindView(R.id.ivProfile)
    var civProfileImage: CircleImageView? = null

    @JvmField
    @BindView(R.id.pincodeLayout)
    var pincodeLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.pincodeTextview)
    var pincodeTextview: TextView? = null

    @JvmField
    @BindView(R.id.pager)
    var mPager: ViewPager? = null

    @JvmField
    @BindView(R.id.indicator)
    var indicator: CircleIndicator? = null

    @JvmField
    @BindView(R.id.recycler_view_cat)
    var recycler_view_cat: RecyclerView? = null

    @JvmField
    @BindView(R.id.recycler_view_home_cat)
    var recycler_view_home_cat: RecyclerView? = null

    var otpEditText: OtpEditText? = null
    var address_radio_group: ViewGroup? = null
    var cancel_popup: ImageView? = null


    var profileViewModel: ProfileViewModel? = null
    private val ImagesArray = ArrayList<String>()

    var homeCategoryList: MutableList<CategoryModel> = ArrayList()
    var adapterViewAndroidtop: DashboardCategoryAdapterTop? = null
    var homeCategoryAdapter: HomeCategoryAdapter? = null


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private val lastKnownLocation: Location? = null

    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    private val locationPermissionGranted = false
    private var gps: GpsTracker? = null
    var addresses: List<Address>? = null


    var geocoder: Geocoder? = null

    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val bannerUrl = Config.hostname + "get_home_banner_api.php"
    private val categoryUrl = Config.hostname + "get_category_api.php"
    private val homeCategoryProductUrl = Config.hostname + "get_home_category_product_api.php"
    private val addressUrl = Config.hostname + "get_address_list_api.php"
    private val saveAddressUrl = Config.hostname + "save_address_api.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userGiftCardPresenter = UserGiftCardPresenter(requireContext(), this@ShoppingFragment)

        if (arguments != null) {
            redirect_to = requireArguments().getString("redirect_to")
            redirect_coupon_code = requireArguments().getString("redirect_coupon_code")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_shop, container, false)
            ButterKnife.bind(this, root)
            /*LoginResponse userInfo = new CommonUtils().getUserInfo(getContext());
            user_id = String.valueOf(userInfo.getData().getUser().getId());
            user_name = userInfo.getData().getUser().getName();
            user_address ="" , user_phone="",user_email="" , user_type="host";*/
            profileViewModel = ViewModelProvider(this).get(
                ProfileViewModel::class.java
            )
            initViews()
            setLiveData()
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        try {
            /*  ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).showFooter();*/

            // set updated coins

            txtPoint!!.text = "Point: " + userTotalPoints.toString()

            cart_count_textview!!.text = all_cart_list.size.toString()
            if (all_cart_list.size == 0) {
                cart_count_textview!!.visibility = View.GONE
            } else {
                cart_count_textview!!.visibility = View.VISIBLE
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    private fun initViews() {
        try {
            try {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                    requireActivity()
                )
                gps = GpsTracker(requireContext())
                val current = LatLng(gps!!.getLatitude(), gps!!.getLongitude())

                geocoder = Geocoder(requireContext())

                addresses = geocoder!!.getFromLocation(gps!!.getLatitude(), gps!!.getLongitude(), 10)
                val address = addresses!![0]
                current_pincode = address.postalCode

                if (pincode.equals("", ignoreCase = true)) {
                    pincode = current_pincode
                }

                pincodeTextview!!.text = pincode
            } catch (e: Exception) {
                //pincodeTextview.setText("Enter Pincode");

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                    requireActivity()
                )
                gps = GpsTracker(requireContext())
                //                String lat = String.valueOf(gps.getLatitude());
//                String lng = String.valueOf(gps.getLongitude());
//                userGiftCardPresenter.getReverseGeocoding(lat,lng);
                profileViewModel!!.reverseGeocoding(gps!!.getLatitude(), gps!!.getLongitude())
            }


            // Toast.makeText(getActivity().getApplicationContext(),"LAt:"+gps.getLatitude()+" pinoce:"+address.getPostalCode(),Toast.LENGTH_SHORT).show();
            userInformationWS
            userInformation
            txtName!!.text = user_name

            cart_count_textview!!.text = all_cart_list.size.toString()
            if (all_cart_list.size == 0) {
                cart_count_textview!!.visibility = View.GONE
            } else {
                cart_count_textview!!.visibility = View.VISIBLE
            }


            // Get Home banner
            homeBanner

            // Get Top Category
            topCategory


            // Get Home Category with Product


            // Show Popup pincode
            if (pincode.equals("", ignoreCase = true)) {
                // showPincodePopup();
            } else {
                categoryProduct
            }


            pincodeLayout!!.setOnClickListener { showPincodePopup() }

            pincodeTextview!!.setOnClickListener { showPincodePopup() }



            imgSearch!!.setOnClickListener {
                try {
                    val myFragment = SearchFragment()
                    val b = Bundle()
                    myFragment.arguments = b


                    val fragmentManager: FragmentManager =
                        (activity as WolooDashboard).getSupportFragmentManager()
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } catch (e: Exception) {
                }
            }


            civProfileImage!!.setOnClickListener {
                try {
                    val myFragment = MyOrdersFragment()
                    val b = Bundle()
                    myFragment.arguments = b


                    val fragmentManager: FragmentManager =
                        (activity as WolooDashboard).getSupportFragmentManager()
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } catch (e: Exception) {
                }
            }


            // Go to cart
            imgCart!!.setOnClickListener {
                if (all_cart_list.size != 0) {
                    val bundle = Bundle()
                    bundle.putString("pincode", pincode)
                    logFirebaseEvent(
                        (activity as WolooDashboard?),
                        bundle,
                        "shopping_cart_icon_click"
                    )


                    val myFragment = CartFragment()
                    val b = Bundle()
                    b.putString(
                        "selected_address_id",
                        selected_address_id
                    )
                    b.putString("selected_address", user_address)
                    myFragment.arguments = b


                    val fragmentManager: FragmentManager =
                        (activity as WolooDashboard).supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Your Cart is empty",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            // Redirect to coupon code if passes
            try {
                if (redirect_to.equals("coupon", ignoreCase = true)) {
                    redirect_to = ""
                    coupon_code = redirect_coupon_code

                    val myFragment = ProductListFragment()
                    val b = Bundle()
                    b.putString("catId", coupon_code)
                    b.putString("catName", coupon_code + " Products")
                    b.putString("cat_type", "coupon")
                    myFragment.arguments = b


                    val fragmentManager: FragmentManager =
                        (activity as WolooDashboard).getSupportFragmentManager()
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            } catch (e: Exception) {
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    fun setLiveData() {
        profileViewModel!!.observeReverseGeocoding().observe(
            viewLifecycleOwner,
            object : Observer<BaseResponse<ArrayList<ReverseGeocodeItem>>> {
                override fun onChanged(value: BaseResponse<ArrayList<ReverseGeocodeItem>>) {
                    if (value.data != null) {
                        try {
                            val data: List<ReverseGeocodeItem> = value.data!!

                            val address_component = data[0].addressComponents

                            current_pincode = address_component!![7].longName
                        } catch (e: Exception) {
                        }

                        // current_pincode = geoCodeResponse;
                        if (pincode.equals("", ignoreCase = true)) {
                            pincode = current_pincode
                        }

                        pincodeTextview!!.text = pincode

                        // Show Popup pincode
                        if (pincode.equals("", ignoreCase = true)) {
                            showPincodePopup()
                        } else {
                            categoryProduct
                        }
                    } else {
                        WolooApplication.errorMessage = ""
                    }
                }
            })
    }


    fun showPincodePopup() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(requireContext().resources.getColor(R.color.transparent_background)))
        dialog.setContentView(R.layout.custom_pincode_popup)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        otpEditText = dialog.findViewById(R.id.oev_view)
        cancel_popup = dialog.findViewById(R.id.cancel_popup)
        val add_address_button = dialog.findViewById<TextView>(R.id.add_address_button)
        val usePincode = dialog.findViewById<TextView>(R.id.usePincode)
        val add_address_layout = dialog.findViewById<LinearLayout>(R.id.add_address_layout)
        val name_edittext = dialog.findViewById<EditText>(R.id.name)
        val pincode_edittext = dialog.findViewById<EditText>(R.id.pincode)
        val city_edittext = dialog.findViewById<EditText>(R.id.city)
        val state_edittext = dialog.findViewById<EditText>(R.id.state)
        val area_edittext = dialog.findViewById<EditText>(R.id.area)
        val flat_building_edittext = dialog.findViewById<EditText>(R.id.flat_building)
        val landmark_edittext = dialog.findViewById<EditText>(R.id.landmark)
        val save_address_button = dialog.findViewById<Button>(R.id.save_address)

        usePincode.visibility = View.GONE



        add_address_button.setOnClickListener {
            add_address_layout.visibility = View.VISIBLE
        }

        save_address_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val name_value = name_edittext.text.toString()
                val pincode_value = pincode_edittext.text.toString()
                val city_value = city_edittext.text.toString()
                val state_value = state_edittext.text.toString()
                val area_value = area_edittext.text.toString()
                val flat_building_value = flat_building_edittext.text.toString()
                val landmark_value = landmark_edittext.text.toString()

                if (name_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter Name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (pincode_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter Pincode",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (city_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter City",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (state_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter State",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (area_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter Area",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (flat_building_value.trim { it <= ' ' }.equals("", ignoreCase = true)) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Please Enter Flat No and Bulding Name",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    pincode = pincode_value
                    val address_val =
                        "$flat_building_value , $landmark_value , $area_value , $city_value-$pincode_value $state_value"
                    user_address = address_val

                    pincodeTextview!!.text = pincode

                    categoryProduct

                    dialog.dismiss()


                    val bundle = Bundle()
                    bundle.putString("name", name_value)
                    bundle.putString("city", city_value)
                    bundle.putString("state", state_value)
                    bundle.putString("area", area_value)
                    bundle.putString("flat_building", flat_building_value)
                    bundle.putString("landmark", landmark_value)
                    bundle.putString("pincode", pincode_value)
                    logFirebaseEvent((activity as WolooDashboard?), bundle, "shopping_add_address")




                    saveAddressApi(
                        user_id,
                        name_value,
                        user_phone,
                        pincode_value,
                        city_value,
                        state_value,
                        area_value,
                        flat_building_value,
                        landmark_value
                    )
                }
            }
        })


        //otpEditText.set
        otpEditText!!.setOnCompleteListener(OnCompleteListener { value ->
            pincode = value
            pincodeTextview!!.text = pincode

            usePincode.visibility = View.VISIBLE

            // getCategoryProduct();
            //  dialog.dismiss();
        })

        usePincode.setOnClickListener {
            categoryProduct
            dialog.dismiss()
        }


        cancel_popup!!.setOnClickListener(View.OnClickListener { dialog.dismiss() })


        // add radio button address address_radio_group
        address_radio_group = dialog.findViewById(R.id.address_radio_group)


        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context)
        mRequestQueue!!.cache.remove(addressUrl + "?user_id=" + user_id)


        //String Request initialized
        mStringRequest = object : StringRequest(
            Method.POST, addressUrl + "?user_id=" + user_id,
            Response.Listener<String?> { response ->
                try {
                    val obj = JSONArray(response)

                    if (obj.length() == 0) {
                        add_address_layout.visibility = View.VISIBLE
                        add_address_button.visibility = View.GONE
                    }


                    for (i in 0 until obj.length()) {
                        val catObj = obj[i] as JSONObject

                        val button = RadioButton(context)
                        button.id = i
                        button.setPadding(5, 5, 5, 5)
                        val colorStateList = ColorStateList(
                            arrayOf(
                                intArrayOf(-android.R.attr.state_enabled),  //disabled
                                intArrayOf(android.R.attr.state_enabled) //enabled
                            ),
                            intArrayOf(
                                Color.BLACK,  //disabled
                                Color.BLUE //enabled

                            )
                        )


                        button.buttonTintList = colorStateList //set the color tint list

                        val address_val =
                            catObj.getString("flat_building") + " , " + catObj.getString("landmark") + " , " + catObj.getString(
                                "area"
                            ) + " , " + catObj.getString("city") + "-" + catObj.getString("pincode") + " " + catObj.getString(
                                "state"
                            )
                        val pincode_val = catObj.getString("pincode")
                        button.text = address_val

                        button.setOnCheckedChangeListener(object :
                            CompoundButton.OnCheckedChangeListener {
                            override fun onCheckedChanged(
                                compoundButton: CompoundButton,
                                b: Boolean
                            ) {
                                user_address = address_val
                                pincode = pincode_val
                                pincodeTextview!!.text = pincode

                                categoryProduct

                                dialog.dismiss()
                            }
                        })


                        address_radio_group!!.addView(button)
                    }
                } catch (e: Exception) {
                }
            },
            Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params: Map<String, String> = HashMap()

                return params
            }
        }

        mRequestQueue!!.cache.clear()
        mStringRequest!!.setShouldCache(false)
        mRequestQueue!!.add(mStringRequest)


        dialog.show()
    }

    private fun navigateToInviteFriendScreen() {
        try {
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(newInstance(true), "InviteFriendFragment")
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    private val userInformationWS: Unit
        get() {
            try {
                Log.d("Request", "DONE getUserInformationWS")
                profileViewModel!!.getUserProfile()
                profileViewModel!!.observeUserProfile().observe(
                    viewLifecycleOwner
                ) { userProfileBaseResponse ->
                    if (userProfileBaseResponse?.data != null) {
                        setProfileResponse(userProfileBaseResponse.data)
                    } else {
                        Toast.makeText(
                            context,
                            WolooApplication.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        WolooApplication.errorMessage = ""
                    }
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }

    private val userInformation: Unit
        get() {
            try {
                Log.d("Request", "DONE getUserInformation")
                val cu = CommonUtils()
                val getUserInfo =
                    cu.userInfo
                //LoginResponse.User user=getUserInfo.getData().getUser();
                var userType = "customer"
                if (getUserInfo!!.roleId != null) {
                    when (getUserInfo.roleId) {
                        "9" -> userType = "host"
                        "10" -> userType = "franchisee"
                    }
                } else userType = "customer"
                setUserInformationToShopping(
                    "" + getUserInfo.id,
                    "" + getUserInfo.name,
                    "" + getUserInfo.address,
                    "" + getUserInfo.email,
                    "" + getUserInfo.mobile,
                    userType
                )
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }

    private fun setUserInformationToShopping(
        userId: String,
        userName: String,
        userAddress: String?,
        userEmail: String,
        userMobile: String,
        userType: String
    ) {
        user_id = userId
        user_name = userName
        //  user_address =userAddress;
        user_phone = userMobile
        user_email = userEmail
        user_type = userType


        try {
            if (user_name.equals("", ignoreCase = true)) {
                txtName!!.text = getString(R.string.guest)
            }
        } catch (e: Exception) {
            txtName!!.text = getString(R.string.guest)
        }
    }

    fun setProfileResponse(userProfile: UserProfile?) {
        if (userProfile != null) {
            try {
                val userData = userProfile.profile
                var userType = "customer"
                if (userData!!.roleId != null) {
                    when (userData.roleId) {
                        "9" -> userType = "host"
                        "10" -> userType = "franchisee"
                    }
                } else userType = "customer"
                setUserInformationToShopping(
                    "" + userData.id,
                    userData.name!!, userData.address,
                    userData.email!!, userData.mobile!!, userType
                )
            } catch (e: Exception) {
                printStackTrace(e)
            }
            if (!TextUtils.isEmpty(userProfile.profile!!.name)) {
                txtName!!.text = userProfile.profile!!.name

                try {
                    if (userProfile.profile!!.name.equals(
                            "",
                            ignoreCase = true
                        ) || userProfile.profile!!.name.equals(" ", ignoreCase = true)
                    ) {
                        txtName!!.text = getString(R.string.guest)
                    }
                } catch (e: Exception) {
                    txtName!!.text = getString(R.string.guest)
                }
            } else {
                val cu = CommonUtils()
                if (cu.isLoggedIn) {
                    txtName!!.setText(R.string.guest)
                } else {
                    txtName!!.text = getString(R.string.guest)
                }
            }

            if (TextUtils.isEmpty(userProfile.profile!!.avatar)) {
                civProfileImage!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.ic_account_circle
                    )
                )
            } else {
                if (userProfile.profile!!.avatar!!.trim { it <= ' ' } == "users/default.png" || userProfile.profile!!.avatar!!.trim { it <= ' ' } == "default.png") {
                    loadImageProfile(
                        requireContext(),
                        civProfileImage!!, BuildConfig.BASE_URL + "public/userProfile/default.png"
                    )
                } else {
                    loadImageProfile(
                        requireContext(),
                        civProfileImage!!,
                        BuildConfig.BASE_URL + "public/userProfile/" + userProfile.profile!!.avatar
                    )
                }
            }

            setUserCoins(userProfile.totalCoins)
        }
    }

    fun setUserCoins(totalCoins: TotalCoins?) {
        try {
            if (totalCoins != null) {
                if (userTotalPointsBackup == 0) {
                    txtPoint!!.text = String.format(
                        getString(R.string.point_format),
                        "" + totalCoins.totalCoins
                    )

                    userTotalPoints = totalCoins.totalCoins

                    userTotalPointsBackup = userTotalPoints
                }
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    private val homeBanner: Unit
        get() {
            //RequestQueue initialized


            mRequestQueue = Volley.newRequestQueue(requireActivity().applicationContext)
            Log.d("Request", "mStringRequest: $mStringRequest")
            Log.d("Aarati test getHomeBanner", mRequestQueue.toString())
            //String Request initialized
            mStringRequest = object : StringRequest(
                Method.GET,
                bannerUrl,
                Response.Listener<String?> { response ->
                    Log.d("Request", "inside fun: $mStringRequest")
                    ImagesArray.clear()
                    try {
                        val obj = JSONArray(response)


                        for (i in 0 until obj.length()) {
                            val catObj = obj[i] as JSONObject
                            ImagesArray.add(catObj.getString("image"))
                        }


                        mPager!!.adapter = BannerAdapter(
                            requireActivity(),
                            ImagesArray,
                            "dashboard"
                        )

                        indicator!!.setViewPager(mPager)


                        // Auto start of viewpager
                        val handler = Handler()


                        val Update = Runnable {
                            if (currentPage == ImagesArray.size) {
                                currentPage = 0
                            }
                            mPager!!.setCurrentItem(
                                currentPage++,
                                true
                            )
                        }
                        val swipeTimer = Timer()
                        swipeTimer.schedule(object : TimerTask() {
                            override fun run() {
                                handler.post(Update)
                            }
                        }, 4000, 4000)
                    } catch (e: Exception) {
                    }
                },
                Response.ErrorListener { Log.d("Request", "error: $mStringRequest") }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    val params: Map<String, String> =
                        HashMap()

                    return params
                }
            }
            Log.d("Request", "queue.add: $mStringRequest")
            mRequestQueue!!.add(mStringRequest)
        }


    private val topCategory: Unit
        get() {
            Log.d("Request", "in top catt: $mStringRequest")
            //RequestQueue initialized
            mRequestQueue = Volley.newRequestQueue(context)

            Log.d("Aarati test getTopCategory", mRequestQueue.toString())

            //String Request initialized
            mStringRequest = object : StringRequest(
                Method.POST,
                categoryUrl,
                Response.Listener<String?> { response ->
                    // Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();
                    categoryList.clear()
                    try {
                        val obj = JSONArray(response)

                        adapterViewAndroidtop =
                            DashboardCategoryAdapterTop(categoryList)
                        val layoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recycler_view_cat!!.layoutManager = layoutManager
                        recycler_view_cat!!.itemAnimator = DefaultItemAnimator()
                        recycler_view_cat!!.adapter = adapterViewAndroidtop
                        recycler_view_cat!!.isNestedScrollingEnabled = false

                        var yourwork: CategoryModel

                        for (i in 0 until obj.length()) {
                            val catObj = obj[i] as JSONObject

                            yourwork = CategoryModel(
                                catObj.getString("image"),
                                catObj.getString("name"),
                                catObj.getString("id"),
                                "dashboard",
                                catObj.getString("banner1_image"),
                                catObj.getString("banner2_image"),
                                catObj.getString("banner3_image")
                            )


                            categoryList.add(yourwork)
                        }

                        adapterViewAndroidtop!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                        //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                },
                Response.ErrorListener {
                    //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    val params: Map<String, String> =
                        HashMap()

                    return params
                }
            }

            mRequestQueue!!.add(mStringRequest)
        }


    private val categoryProduct: Unit
        get() {
            //RequestQueue initialized

            mRequestQueue = Volley.newRequestQueue(context)

            Log.d("Aarati test getProduct category", mRequestQueue.toString())

            //String Request initialized
            mStringRequest = object : StringRequest(
                Method.POST,
                homeCategoryProductUrl + "?user_type=" + user_type + "&pincode=" + pincode,
                Response.Listener<String?> { response ->
                    // Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();
                    try {
                        recycler_view_home_cat!!.isNestedScrollingEnabled = false
                        recycler_view_home_cat!!.setHasFixedSize(false)

                        homeCategoryList.clear()
                        // recyclerView.setNestedScrollingEnabled(false);
                        homeCategoryAdapter =
                            HomeCategoryAdapter(
                                homeCategoryList
                            )
                        val mLayoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(activity)
                        recycler_view_home_cat!!.layoutManager = mLayoutManager
                        recycler_view_home_cat!!.itemAnimator = DefaultItemAnimator()

                        recycler_view_home_cat!!.adapter = homeCategoryAdapter

                        for (i in 0..homeCategoryList.size) {
                            Log.d("Request", homeCategoryList[i].banner1)
                        }


                        val obj = JSONArray(response)

                        val category_info = obj.getJSONArray(0)
                        val product_info = obj.getJSONArray(1)
                        val product_images = obj.getJSONArray(2)
                        val sub_category_info = obj.getJSONArray(3)

                        Log.d(
                            "Aarati test category_info",
                            category_info.toString()
                        )
                        Log.d("Aarati test product_info", product_info.toString())
                        Log.d(
                            "Aarati test product_images",
                            product_images.toString()
                        )
                        Log.d(
                            "Aarati test sub_category_info",
                            sub_category_info.toString()
                        )


                        var c = 0
                        var yourwork: CategoryModel
                        for (i in 0 until category_info.length()) {
                            c++
                            val catObj = category_info[i] as JSONObject


                            yourwork = CategoryModel(
                                "",
                                catObj.getString("name"),
                                catObj.getString("id"),
                                "dashboard",
                                catObj.getString("banner1_image"),
                                catObj.getString("banner2_image"),
                                catObj.getString("banner3_image")
                            )


                            homeCategoryList.add(yourwork)
                        }



                        homeSubCategoryList.clear()


                        var yourwork2: CategoryModel
                        for (i in 0 until sub_category_info.length()) {
                            val catObj = sub_category_info[i] as JSONObject


                            yourwork2 = CategoryModel(
                                catObj.getString("image"),
                                catObj.getString("name"),
                                catObj.getString("id"),
                                catObj.getString("category_id"),
                                "",
                                "",
                                ""
                            )


                            homeSubCategoryList.add(yourwork2)
                        }


                        /***  ADD Product   */


                        /***  ADD Product   */
                        homeProductList.clear()
                        var yourwork1: HomeProductModel

                        for (i in 0 until product_info.length()) {
                            val catObj = product_info[i] as JSONObject

                            yourwork1 = HomeProductModel(
                                catObj.getString("image"),
                                catObj.getString("name"),
                                catObj.getString("id"),
                                "dashboard",
                                catObj.getString("cat_id"),
                                catObj.getString("desc"),
                                catObj.getString("price")
                            )

                            homeProductList.add(yourwork1)
                            Log.d(
                                "Aarati test",
                                homeProductList[i].toString()
                            )
                        }



                        all_product_images.clear()
                        for (i in 0 until product_images.length()) {
                            c++
                            val yourworkObj = product_images[i] as JSONObject


                            val single_product =
                                ArrayList<String>()

                            single_product.add(yourworkObj.getString("product_id"))
                            single_product.add(yourworkObj.getString("img"))


                            all_product_images.add(single_product)
                        }







                        homeCategoryAdapter!!.notifyDataSetChanged()
                    } catch (e: Exception) {
                        //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                },
                Response.ErrorListener {
                    //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    val params: Map<String, String> =
                        HashMap()

                    return params
                }
            }

            mRequestQueue!!.add(mStringRequest)
        }


    private fun saveAddressApi(
        user_id: String,
        name: String,
        phone: String,
        pincode_value: String,
        city_value: String,
        state_value: String,
        area_value: String,
        flat_builing_value: String,
        landmark_value: String
    ) {
        val postUrl = saveAddressUrl
        val requestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("user_id", user_id)
            postData.put("name", name)
            postData.put("phone", phone)
            postData.put("pincode", pincode_value)
            postData.put("city", city_value)
            postData.put("state", state_value)
            postData.put("area", area_value)
            postData.put("flat_builing", flat_builing_value)
            postData.put("landmark", landmark_value)
        } catch (e: JSONException) {
            printStackTrace(e)
        }
        Log.d("Aarati test address api", saveAddressUrl)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, postUrl, postData,
            { //System.out.println(response);
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Address Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            },
            { error ->
                printStackTrace(
                    error
                )
            })

        requestQueue.add(jsonObjectRequest)
    }


    override fun setGeoCodeResponse(geoCodeResponse: GeoCodeResponse?) {
        Log.d("Aarati test setGeoCodeResponse", geoCodeResponse.toString())

        try {
            val data = geoCodeResponse!!.data

            val address_component = data!![0].addressComponents

            current_pincode = address_component!![7].longName
        } catch (e: Exception) {
        }


        // current_pincode = geoCodeResponse;
        if (pincode.equals("", ignoreCase = true)) {
            pincode = current_pincode
        }

        pincodeTextview!!.text = pincode

        // Show Popup pincode
        if (pincode.equals("", ignoreCase = true)) {
            showPincodePopup()
        } else {
            categoryProduct
        }
    }

    companion object {
        private var currentPage = 0
        private const val NUM_PAGES = 0

        @JvmField
        var all_product_images: MutableList<List<String>> = ArrayList()
        var categoryList: MutableList<CategoryModel> = ArrayList()
        var homeProductList: MutableList<HomeProductModel> = ArrayList<HomeProductModel>()
        var homeSubCategoryList: MutableList<CategoryModel> = ArrayList()


        // These value required  from homepage
        @JvmField
        var userTotalPoints: Int = 0
        @JvmField
        var userTotalPointsBackup: Int = 0
        @JvmField
        var userGiftCardValue: Int = 0
        @JvmField
        var totalCouponDiscount: Int = 0
        @JvmField
        var user_id: String = "1"
        @JvmField
        var user_name: String = ""
        @JvmField
        var user_address: String = ""
        @JvmField
        var user_phone: String = ""
        var user_email: String = ""
        @JvmField
        var user_type: String = "customer"
        @JvmField
        var selected_address_id: String = "0"
        @JvmField
        var pincode: String? = ""
        var current_pincode: String? = ""
        @JvmField
        var coupon_code: String? = ""
        @JvmField
        var coupon_value: String? = ""
        @JvmField
        var coupon_value_unit: String? = ""
        @JvmField
        var couponProductList: List<String> = ArrayList()

        // Usert Type: franchisee, customer , host
        @JvmField
        var all_cart_list: ArrayList<ArrayList<String>> = ArrayList()


        fun newInstance(param1: String?, param2: String?): ShoppingFragment {
            val fragment = ShoppingFragment()
            val args = Bundle()
            args.putString("redirect_to", param1)
            args.putString("redirect_coupon_code", param2)
            fragment.arguments = args
            return fragment
        }
    }
}