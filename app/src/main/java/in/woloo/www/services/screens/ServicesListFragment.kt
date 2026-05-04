package `in`.woloo.www.services.screens

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentServicesListBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.services.ExpressServiceItem
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.TakeSneakPeekServiceItem
import `in`.woloo.www.services.adapter.ExpressServiceAdapter
import `in`.woloo.www.services.adapter.OnCategoryItemClickListener
import `in`.woloo.www.services.adapter.ServicesCategoriesListAdapter
import `in`.woloo.www.services.adapter.TakeASneakPeekAdapter
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.screens.AddressBottomSheetFragment
import `in`.woloo.www.services.screens.ServicesOrderListActivity
import `in`.woloo.www.store.user_details.CreateUpdateAddressRequest
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger


class ServicesListFragment : Fragment() , OnCategoryItemClickListener {

    lateinit var binding: FragmentServicesListBinding
    var isDefaultAddress : Boolean = false
    private var storeViewModel: StoreViewModel? = null
    private var servicesViewModel: ServiceViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    private var selectedCategoryId : String = ""
    private var  selectedVideoUrl: String = ""
    private var cartList: CartParams? = null
    private var productList: ArrayList<ProductListData> = ArrayList()
    private var expressBookingList: ArrayList<ProductListData> = ArrayList()
    private var prouctNameList : ArrayList<String> = ArrayList()
    private var categoriesVideosList: ArrayList<TakeSneakPeekServiceItem> = ArrayList()
    lateinit var sharedPlayer : ExoPlayer
    lateinit var adapterVideo: TakeASneakPeekAdapter


    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val refresh = result.data?.getBooleanExtra("refresh", false) ?: false
            if (refresh) {
                refreshFragment()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        sharedPlayer = ExoPlayer.Builder(requireContext()).build()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesListBinding.inflate(inflater, container, false)


        binding.showOrdersList.setOnClickListener {
            val intent = Intent(context, ServicesOrderListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            startActivity(intent)
        }

        binding.searchLayout.setOnClickListener{
          //  Toast.makeText(requireActivity() , "WORK IN PROGRESS" , Toast.LENGTH_SHORT ).show()
            val intent = Intent(requireActivity(), SearchFromServicesActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("LATESTPRODUCTS" , prouctNameList)// Pass the boolean value if needed
            startActivity(intent)
        }

        binding.searchText.setOnClickListener{
           // Toast.makeText(requireActivity() , "WORK IN PROGRESS" , Toast.LENGTH_SHORT ).show()
            val intent = Intent(requireActivity(), SearchFromServicesActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("LATESTPRODUCTS" , prouctNameList)// Pass the boolean value if needed
            startActivity(intent)
        }

        binding.searchImage.setOnClickListener{
           // Toast.makeText(requireActivity() , "WORK IN PROGRESS" , Toast.LENGTH_SHORT ).show()
            val intent = Intent(requireActivity(), SearchFromServicesActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("LATESTPRODUCTS" , prouctNameList)// Pass the boolean value if needed
            startActivity(intent)
        }




        apiCalls()
        initviews()
        setLiveData()

        binding.goToCartView.setOnClickListener {
            val items = cartList?.items
            if (items.isNullOrEmpty()) {
                showCartEmptyDialog()
            } else {
                val intent = Intent(requireActivity(), ServicingCartActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                activityResultLauncher.launch(intent)
            }
        }

        binding.showFavouritesList.setOnClickListener {

            val productListJson = Gson().toJson(productList)
            val intent = Intent(context, ServicesProductsListingActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("PRODUCT_LISTS" , productListJson.toString())
            intent.putExtra("FROMSCREEN" , AppConstants.TO_WISHLIST)
            startActivity(intent)

        }

        return binding.root
    }

    fun apiCalls()
    {

        if(SharedPrefSettings.getPreferences.fetchServiceCartId().isNullOrEmpty())
        {
            val request = CartRequest()
            request.region_id = SharedPrefSettings.getPreferences.fetchRegionId()
            servicesViewModel!!.getCartCreate(request)
        }
        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
        var address = SharedPrefSettings.getPreferences.fetchSelectedAddressId()

        if(address != null)
        {
            Logger.d("Address object" , address )
        }
        servicesViewModel!!.getCategoriesList()
        storeViewModel!!.getAddressesList()
        servicesViewModel!!.createWishlist()
        servicesViewModel!!.getProductWithPriceList("title,*variants.calculated_price,+variants.inventory_quantity,*categories,metadata",
            SharedPrefSettings.getPreferences.fetchRegionId().toString()
        )
    }

    fun initviews()
    {
        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

      /*  val dummyItems = listOf(
            ExpressServiceItem("Deep Cleaning\nServices for\nyour home", R.drawable.booking_express_img ,R.drawable.express_image),
            ExpressServiceItem("Pest Control\nServices for your \nHome", R.drawable.red_express_booking  ,R.drawable.red_booking_design ),
            ExpressServiceItem("Bathroom Cleaning \nServices for \nyour Home", R.drawable.copper_express_booking ,R.drawable.copper_booking_design),
            ExpressServiceItem("A/C Services \nfor your \nHome", R.drawable.pink_express_booking ,R.drawable.ping_booking_design)
        )
*/



        val dummyItemsVideo =
           categoriesVideosList


        adapterVideo = TakeASneakPeekAdapter(dummyItemsVideo , sharedPlayer)
        binding.servicesSneakPeekRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.servicesSneakPeekRecycler.adapter = adapterVideo

    }

    fun setLiveData()
    {
        servicesViewModel!!.observeCartCreate().observe(viewLifecycleOwner , Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    SharedPrefSettings.getPreferences.storeServiceCartId(it.cart!!.id)
                    Log.i("Aarati Store Cart ServiceList" , SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
                    Log.i("Aarati Store Cart DateTime" , SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })


        servicesViewModel!!.observeProductWithPriceList()
            .observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    try{
                        Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                        for (i in it.products!!.indices) {
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"

                            )
                            prouctNameList.add(it.products!![i].title.toString())

                            if(it.products!![i].metadata != null) {
                                Log.d(
                                    "Aarati services image",
                                    it.products!![i].metadata!!.serviceImage.toString()
                                )
                                expressBookingList.add(it.products!![i])
                            }

                        }

                        productList = it.products!!
                        val adapter = ExpressServiceAdapter(expressBookingList)
                        binding.servicesExpressRecycler.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.servicesExpressRecycler.adapter = adapter


                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }


                }

            })


        profileViewModel!!.observeUserProfile().observe(
            requireActivity()
        ) { userProfileBaseResponse ->
            if (userProfileBaseResponse != null) {
                if (userProfileBaseResponse.data != null) {
                    try {

                        Log.i("aarati Woloo Coins ", "${userProfileBaseResponse.data!!.totalCoins!!.totalCoins}")
                        Log.i("aarati Woloo Coins ", "${userProfileBaseResponse.data!!.profile!!.address.toString()}")
                        /*if(userProfileBaseResponse.data!!.profile!!.address == null)
                        {
                            val bottomSheetFragment = AddEditAddressBottomSheetFragment()
                            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

                        }else*/

                        if(userProfileBaseResponse.data!!.profile!!.name != null
                            &&
                            userProfileBaseResponse.data!!.profile!!.city != null
                            &&
                            userProfileBaseResponse.data!!.profile!!.pincode != null
                            &&
                            userProfileBaseResponse.data!!.profile!!.address != null)
                        {
                            binding.homeText.text = "Home"
                            binding.addressText.text =
                                userProfileBaseResponse.data!!.profile!!.address.toString()
                            val request = CreateUpdateAddressRequest()
                            request.first_name = userProfileBaseResponse.data!!.profile!!.name
                            request.last_name = ""
                            request.address_1 = userProfileBaseResponse.data!!.profile!!.address
                            request.city = userProfileBaseResponse.data!!.profile!!.city
                            request.phone = userProfileBaseResponse.data!!.profile!!.mobile
                            request.address_name = "Default"
                            request.postal_code = userProfileBaseResponse.data!!.profile!!.pincode
                            storeViewModel!!.getAddAddress(request)
                            isDefaultAddress = true
                        }
                        else
                        {
                            binding.homeText.text = "Add"
                            binding.addressText.text ="Add Address"
                            isDefaultAddress = false
                        }




                    }catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } else {
                //                    displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }
        }

        storeViewModel!!.observeAddressesList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
                    var responseCode = it.responseCode
                    if(responseCode == 401)
                    {
                        var mobileNumber =SharedPrefSettings.getPreferences.fetchShopMobileNumber()
                        var decryptedData = SharedPrefSettings.getPreferences.fetchDecryptedPassword()
                        val request = RegisterOnMedusaRequest()
                        request.email = "$mobileNumber@gmail.com"
                        request.password = decryptedData!!
                        storeViewModel!!.getCustomerAuthPassOnMedusa(request)
                    }
                    Logger.i("Aarati Store", "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}")
                    /*for (i in it.addresses!!.indices)
                        Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                    if(!it.data!!.addresses!!.isNullOrEmpty())
                    {
                        SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.addresses!![0].customer_id.toString())
                        // SharedPrefSettings.getPreferences.storeSelectedAddressId(it.addresses!![0].customer_id.toString())
                        Log.d("ADDRESS ID SELECTED 1" , "${it.data!!.addresses!![0].id}")
                        Log.d("ADDRESS ID SELECTED 3" , "${SharedPrefSettings.getPreferences.fetchSelectedAddressId()}")
                        var findAddress = it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }
                        // Log.d("ADDRESS ID SELECTED 2" , "${findAddress!!.id}")
                        if(findAddress != null)
                        {
                            binding.homeText.text = findAddress.address_name
                            binding.addressText.text = findAddress.address_1
                        }
                        else{

                            binding.homeText.text = "Select address"
                            binding.addressText.text = "Address not selected"

                            /* val bottomSheetFragment = AddressBottomSheetFragment()
                             bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)*/

                        }
                    }
                    else{

                        profileViewModel!!.getUserProfile()
                    }

                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }

        })

        servicesViewModel!!.observeCategoriesList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                    try{
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.productCategories!! + it.productCategories!!.size}"
                    )
                    for (i in it.productCategories!!.indices) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.productCategories!![i].id + it.productCategories!![i].name}"
                        )
                    }


                    val newCategoryList = ArrayList<CategoriesListData>()


                    for (category in it.productCategories.orEmpty()) {
                        val parentName = category.parent_category?.name


                        if (!category.parent_category_id.isNullOrEmpty() &&
                            parentName != null &&
                            (parentName.contains("Hygiene", ignoreCase = true) )
                        ) {
                            newCategoryList.add(category)
                            if(!category.metadata?.videoUrl.isNullOrEmpty())
                            {
                                categoriesVideosList.add(TakeSneakPeekServiceItem(imageResId =category.metadata!!.videoUrl.toString() , categori_id = category.id.toString()))
                                Log.i("Aarati Service Video Url", "parent cate ${category.metadata!!.videoUrl!!}")
                            }
                                }

                    }

                    for(i in newCategoryList.indices)
                    {
                        Logger.i("Aarati Store", "setLiveData ${newCategoryList[i].id + newCategoryList[i].name}")
                    }

                    if(newCategoryList.size >= 9) {
                        val limitedList: ArrayList<CategoriesListData> =
                            ArrayList(newCategoryList.subList(0, minOf(9, newCategoryList.size)))
                        binding.servicesCategoriesRecycler.layoutManager =
                            GridLayoutManager(requireContext(), 3)
                        binding.servicesCategoriesRecycler.adapter =
                            ServicesCategoriesListAdapter(requireActivity(), limitedList , servicesViewModel!! , this)
                    }
                    else{
                        binding.servicesCategoriesRecycler.layoutManager =
                            GridLayoutManager(requireContext(), 3)
                        binding.servicesCategoriesRecycler.adapter =
                            ServicesCategoriesListAdapter(requireActivity(), newCategoryList , servicesViewModel!! , this)
                    }


               }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }

        })

        servicesViewModel!!.observeCategoryWiseProductList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                try {
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )

                        val productJson = Gson().toJson(it.products!![0])


                         if(it.products!!.size == 1)
                        {
                            val intent = Intent(context, ServicesProductDetailsActivity::class.java)
                            intent.putExtra(
                                "IS_SHOW_BACK_BUTTON",
                                true
                            ) // Pass the boolean value if needed
                            intent.putExtra("PRODUCT_DETAILS", productJson)
                            startActivity(intent)
                        }
                        else if(it.products!!.size >= 2 && selectedCategoryId.isNotEmpty())
                         {
                             val intent = Intent(context, ServicesProductsListingActivity::class.java)
                             intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                             intent.putExtra("CATEGORY_ID" , selectedCategoryId)
                             intent.putExtra("FROMSCREEN" , AppConstants.FROM_CATEGORIES)
                             intent.putExtra("VIDEO_URL" , selectedVideoUrl)
                             startActivity(intent)
                         }

                        selectedCategoryId = ""


                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        servicesViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store Cart", "${it.cart!!.items!!.size} ${it.cart!!.toString()} ")
                    cartList = it.cart!!


                    binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                    //  compareListFinal()
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

    }



    override fun onCategoryItemClicked(data: String , url: String) {
        selectedCategoryId = data
        selectedVideoUrl = url
    }

    private fun showCartEmptyDialog() {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.tv_msg)
            val llclose = dialog.findViewById<TextView>(R.id.btnCloseDialog)
            llStartFreeTrial.text = "Your cart is empty.Please add items to Cart"
            llclose.setOnClickListener{dialog.dismiss()}
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun refreshFragment() {

        apiCalls()
        initviews()
        setLiveData()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPlayer.release()
        adapterVideo.releaseAllPlayers()

    }

    override fun onStart() {
        super.onStart()
        refreshFragmentData()
        refreshFragment()
    }



    override fun onResume() {
        super.onResume()
        refreshFragmentData()
        refreshFragment()
    }

    fun refreshFragmentData()
    {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }

}