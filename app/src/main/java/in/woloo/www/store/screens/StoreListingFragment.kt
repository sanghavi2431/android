package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.HomeFragment
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.DialogShopOrServiceBinding
import `in`.woloo.www.databinding.FragmentStoreListingBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CategoriesCustomeAdapter
import `in`.woloo.www.store.adapter.NewInStoreCustomAdapter
import `in`.woloo.www.store.adapter.PeriodEssentialsCustomeAdapter
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.adapter.TopBrandsCustomAdapter
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.cart_request_response.CartResponse
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.cart_request_response.ComparedCartViewModel
import `in`.woloo.www.store.cart_request_response.ComparedItemsDataForCart
import `in`.woloo.www.store.cart_request_response.MatchVariantIdsWithLineIds
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.collections_response.CollectionsListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.user_details.CreateUpdateAddressRequest
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import java.time.Instant


class StoreListingFragment : Fragment() {


    lateinit var binding: FragmentStoreListingBinding
    private var storeViewModel: StoreViewModel? = null
    private var cartList: CartParams? = null
    private var productList: ArrayList<ProductListData> = ArrayList()
    private var comparedItemsList : List<ComparedItemsDataForCart>? = null
    private var profileViewModel: ProfileViewModel? = null
    var isDefaultAddress : Boolean = false
    private var productsCollectionsCustomeAdapter: ProductsCollectionsCustomeAdapter? = null
    private  var latestProductsNamesList : ArrayList<String> = ArrayList()
    val payload = HashMap<String, Any>()
    val bundle = Bundle()

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
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        bundle.putString(
            AppConstants.ON_NEW_SHOP_CLICK,
            "Entered to Store"
        )
        logFirebaseEvent(activity, bundle, AppConstants.ON_NEW_SHOP_CLICK)

        payload[AppConstants.ON_NEW_SHOP_CLICK] =
            "Entered to Store"
        logNetcoreEvent(requireActivity(), payload, AppConstants.ON_NEW_SHOP_CLICK)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStoreListingBinding.inflate(inflater, container, false)


 /*       when (requireActivity()) {
            is WolooDashboard -> {


            }
        }*/

     /*   if((requireActivity() is WolooDashboard) &&  ! SharedPrefSettings.getPreferences.fetchIsReturningFromBottomSheet())
        {
            showShopOrServicesDialog()
        }*/
        SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(false)

        initViews()
        setLiveData()

        var address = SharedPrefSettings.getPreferences.fetchSelectedAddressId()
        if(address != null)
        {
            Logger.d("Address object" , address )
        }




        binding.seeMoreBrandsLayout.setOnClickListener {
            val intent = Intent(context, SeeMoreItemsActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            startActivity(intent)

        }

        binding.seeMoreProductLayout.setOnClickListener {
            val intent = Intent(context, StoreProductListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("FROMSCREEN" , AppConstants.FROM_COLLECTIONS_SEE_MORE)
            startActivity(intent)
        }

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.showFavouritesList.setOnClickListener {
            val productListJson = Gson().toJson(productList)
            val intent = Intent(context, StoreProductListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("PRODUCT_LISTS" , productListJson.toString())
            intent.putExtra("FROMSCREEN" , AppConstants.TO_WISHLIST)
            startActivity(intent)
        }

        binding.showOrdersList.setOnClickListener {
            val intent = Intent(context, OrdersListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            startActivity(intent)
        }

        binding.goToCartView.setOnClickListener {
            if(cartList!!.items!!.size == 0)
            {
                showCartEmptyDialog()
            }
            else {
                val intent = Intent(requireActivity(), ShoppingCartActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                activityResultLauncher.launch(intent)
            }
        }

        binding.searchLayout.setOnClickListener {
            val intent = Intent(requireActivity(), SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }

        binding.searchText.setOnClickListener {
            val intent = Intent(requireActivity(), SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }

        binding.searchImage.setOnClickListener {
            val intent = Intent(requireActivity(), SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }






        return binding.root
    }


    fun getLatestProducts(
        products: ArrayList<ProductListData>,
        count: Int = 5
    ): List<ProductListData> {
        return products.sortedByDescending { Instant.parse(it.created_at) }
            .take(count)
    }

    fun initViews() {



        storeViewModel!!.getProductWithPriceList(
            "*variants.calculated_price,+variants.inventory_quantity,*categories",
            SharedPrefSettings.getPreferences.fetchRegionId().toString()
        )
        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
        storeViewModel!!.getCategoriesList()
        storeViewModel!!.getCollectionsList("id,title,metadata")
        storeViewModel!!.getAddressesList()
        storeViewModel!!.createWishlist()





    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun setLiveData() {

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

        storeViewModel!!.observeCreateWishlist().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.wishlist!!}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeProductWithPriceList()
            .observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    try{
                    Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                    for (i in it.products!!.indices) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!![i].id + it.products!![i].title}"

                        )
                        latestProductsNamesList.add(it.products!![i].title.toString())

                    }

                    productList = it.products!!
                    if(productList.size >= 8) {
                        val limitedList: ArrayList<ProductListData> =
                            it.products!!.subList(0, minOf(9, it.products!!.size))
                                .toCollection(ArrayList())
                        try {
                            productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                                requireActivity(),
                                limitedList,
                                storeViewModel!!
                            )
                            binding.collectionsRecycler.layoutManager =
                                GridLayoutManager(requireContext(), 3)
                            binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                            cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                        }catch (e : Exception)
                        {
                            CommonUtils.printStackTrace(e)
                        }
                    }
                    else
                    {
                        productsCollectionsCustomeAdapter =   ProductsCollectionsCustomeAdapter(
                            requireActivity(),
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionsRecycler.layoutManager =
                            GridLayoutManager(requireContext(), 3)
                        binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }

                    val latestProducts = getLatestProducts(it.products!!)
                    Logger.i("Aarati Store", "setLiveData ${latestProducts + latestProducts.size}")
                    for (i in latestProducts.indices) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${latestProducts[i].id + latestProducts[i].title}"

                        )

                    }

/*
                    binding.newInStoreRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.newInStoreRecycler.adapter =
                        NewInStoreCustomAdapter(requireActivity(), latestProducts)*/

                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }


                }

            })

        storeViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    cartList = it.cart!!
                    productsCollectionsCustomeAdapter?.updateCart(it.cart)

                    binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                    //  compareListFinal()
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        storeViewModel!!.observeAddAddress().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.toString()}")
              SharedPrefSettings.getPreferences.storeSelectedAddressId(it.data!!.customer!!.addresses!![0].id.toString())
                SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.customer!!.addresses!![0].customer_id.toString())
                isDefaultAddress = false
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

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

        storeViewModel!!.observeCollectionsList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.collections!! + it.collections!!.size}")
                for (i in it.collections!!.indices)
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.collections!![i].id + it.collections!![i].title}"
                    )

                if(it.collections!!.size >= 6) {
                    val limitedList: ArrayList<CollectionsListData> =
                        ArrayList(it.collections!!.subList(0, minOf(6, it.collections!!.size)))
                    binding.topBrandsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                    binding.topBrandsRecycler.adapter =
                        TopBrandsCustomAdapter(requireActivity(), limitedList)
                }
                else{
                    binding.topBrandsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                    binding.topBrandsRecycler.adapter =
                        TopBrandsCustomAdapter(requireActivity(), it.collections!!)
                }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeCategoriesList().observe(viewLifecycleOwner, Observer { response ->
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
                    /* Logger.i(
                         "Aarati Store",
                         "metadata ${it.productCategories!![i].metadata!!.image + " " + it.productCategories!![i].metadata!!.background_color}"
                     )*/
                }

                val CategoryList = ArrayList<CategoriesListData>()

                for (category in it.productCategories!!) {
                    if (category.parent_category_id.isNullOrEmpty()
                            && !category.name!!.contains("new in store", ignoreCase = true))
                        {
                        CategoryList.add(category)

                    }
                }

                binding.categoriesRecycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.categoriesRecycler.adapter =
                    CategoriesCustomeAdapter(requireActivity(), CategoryList)

                    val newCategoryList = ArrayList<CategoriesListData>()
                    val newInStoreCategoryList = ArrayList<CategoriesListData>()

                    for (category in it.productCategories.orEmpty()) {
                        val parentName = category.parent_category?.name
                        Log.i("Period Data", "parent cate $parentName")

                        if (!category.parent_category_id.isNullOrEmpty() &&
                            parentName != null &&
                            (parentName.contains("Menstrual", ignoreCase = true) ||
                                    parentName.contains("Menstruation", ignoreCase = true) ||
                                    parentName.contains("Period", ignoreCase = true))
                        ) {

                                newCategoryList.add(category)
                        }


                        if (!category.parent_category_id.isNullOrEmpty() &&
                            parentName != null &&
                            (parentName.contains("New In Store", ignoreCase = true) ||
                                    parentName.contains("NewInStore", ignoreCase = true) ||
                                    parentName.contains("Period", ignoreCase = true))
                        ) {
                            newInStoreCategoryList.add(category)
                        }

                    }


                    binding.newInStoreRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.newInStoreRecycler.adapter =
                        NewInStoreCustomAdapter(requireActivity(), newInStoreCategoryList)

                    binding.periodEssentialsRecycler.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.periodEssentialsRecycler.adapter =
                    PeriodEssentialsCustomeAdapter(requireActivity(), newCategoryList)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }

        })

        storeViewModel!!.observeUpdateToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                cartList = it.cart
                cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeDeleteFromCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
              //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    binding.displayCartCount.text = it.parent!!.items!!.size.toString()
                Logger.i("Aarati Store", "setLiveData ${it.deleted}")
                 //   var cartParams: CartParams = CartMapper.INSTANCE.fromParentToParams(it.parent)
                //if (it.deleted == true) {
                    cartList = it.parent
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
              //  }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
               //
            }
        })

        storeViewModel!!.observeAddToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                cartList = it.cart
                cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })




        storeViewModel!!.observeNotifyUserForProduct().observe(viewLifecycleOwner , Observer{response ->
            response?.let {
                try{
                Logger.i("Aarati Store Notify", "setLiveData ${it.toString()}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

                }
        })



    }

    fun compareListFinal()
    {
        Logger.i("Aarati Store line id", productList.size.toString() + " " + cartList.toString())
        if(productList != null && cartList != null) {
            comparedItemsList = compareLists(cartList!!, productList)
            Log.i("Aarati Store line id", comparedItemsList!!.size.toString())
            for (i in comparedItemsList!!.indices) {
                Logger.i(
                    "Aarati Store line id",
                    comparedItemsList!!.get(i).product_id.toString() + comparedItemsList!!.get(i).variant_id.toString()
                            + comparedItemsList!!.get(i).item_id.toString() + comparedItemsList!!.get(
                        i
                    ).item_quantity
                )

            }
        }
    }

    companion object {



        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StoreListingFragment().apply {

            }
    }



    fun compareLists(cartResponse: CartParams, productList: List<ProductListData>): List<ComparedItemsDataForCart> {
        val resultList = mutableListOf<ComparedItemsDataForCart>()

        val cartItems = cartResponse.items ?: return emptyList()

        val productVariantMap = productList.flatMap { product ->
            product.variants?.map { variant -> variant.id to product.id } ?: emptyList()
        }.toMap()

        cartItems.forEach { cartItem ->
            if (cartItem.variant_id in productVariantMap) {
                resultList.add(
                    ComparedItemsDataForCart(
                        product_id = productVariantMap[cartItem.variant_id],
                        variant_id = cartItem.variant_id,
                        item_id = cartItem.line_id,
                        item_quantity = cartItem.quantity // Replace with actual quantity field if available
                    )
                )
            }
        }

        return resultList
    }



    fun refreshFragment() {

        initViews()
        setLiveData()


    }

    override fun onResume() {
        super.onResume()
        refreshFragmentData()
    }

    fun refreshFragmentData()
    {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
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

    private fun showShopOrServicesDialog() {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            val binding = DialogShopOrServiceBinding.inflate(LayoutInflater.from(requireContext()))
            dialog.setContentView(binding.root)

            dialog.window?.attributes = dialog.window?.attributes
            dialog.window?.setWindowAnimations(R.style.DialogAnimation)

            binding.goToStore.setOnClickListener {
                dialog.dismiss()
            }

           /* binding.goToServices.setOnClickListener{
                dialog.dismiss()
                (requireActivity() as WolooDashboard).loadFragment(ServicesListFragment(), "serviceslistfragment")
            }*/


            dialog.show()

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


}