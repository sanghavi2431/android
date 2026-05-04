package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityServicesDetailsBinding
import `in`.woloo.www.databinding.ActivityServiesListingBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.ServicesProductListAdapter
import `in`.woloo.www.services.adapter.ServicesWishlistCustomAdapter
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.screens.AddressBottomSheetFragment
import `in`.woloo.www.store.screens.FilterProductsFragment
import `in`.woloo.www.store.screens.SearchFromStoreActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

class ServicesProductsListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiesListingBinding
    private var serviceViewModel: ServiceViewModel? = null
    var  fromScreen: String = ""
    private var productsCollectionsCustomeAdapter: ServicesProductListAdapter? = null
    private var  selectedVideoUrl: String = ""
    private lateinit var product: ArrayList<ProductListData>
    private var storeViewModel: StoreViewModel? = null
    private var cartList: CartParams? = null
    private var wishListItems : ArrayList<ProductListData>? = null
    private var wishListAdapter: ServicesWishlistCustomAdapter? = null
    private  var latestProductsNamesList : ArrayList<String> = ArrayList()
    var isFilterApplied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiesListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )

        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        serviceViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
        storeViewModel!!.getAddressesList()



        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }



        if(intent.getStringExtra("FROMSCREEN").isNullOrEmpty()) {
            if (!intent.getStringExtra("PRODUCT_LISTS").isNullOrEmpty()) {
                val productJson = intent.getStringExtra("PRODUCT_LISTS")

                product = Gson().fromJson(
                    productJson,
                    object : TypeToken<ArrayList<ProductListData>>() {}.type
                )
                if (!productJson.isNullOrEmpty()) {
                    productsCollectionsCustomeAdapter = ServicesProductListAdapter(
                        this,
                        product,
                        supportFragmentManager,
                        serviceViewModel!!
                    )
                    binding.servicesRecycler.layoutManager = GridLayoutManager(this, 2)
                    binding.servicesRecycler.adapter = productsCollectionsCustomeAdapter
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                }
            }
        }


        fromScreenFunction()
        setLiveData()

        binding.filterProducts.setOnClickListener {

            Toast.makeText(this , "WORK IN PROGRESS" , Toast.LENGTH_SHORT ).show()
         /*   val bottomSheetFragment = FilterProductsFragment()
            bottomSheetFragment.filterListener = object :
                FilterProductsFragment.OnFilteredListener {
                override fun onFilterApplied(
                    selectedCategory: String,
                    slectedSizeForFilter: String,
                    FROMSCREEN: String
                ) {
                    isFilterApplied = true

                    when {
                        selectedCategory.isEmpty() -> {
                            serviceViewModel!!.getFilterProductList("" ,slectedSizeForFilter)
                        }

                        slectedSizeForFilter.isEmpty() -> {
                            serviceViewModel!!.getCategoryWiseProductList(selectedCategory)
                        }

                        else -> {
                            serviceViewModel!!.getFilterProductList(
                                selectedCategory,
                                slectedSizeForFilter
                            )
                        }
                    }
                }
            }
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*/
        }

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        serviceViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    Logger.i("Aarati Store cart", "${it.cart!!.items!!.size} ${it.cart!!.toString()} ")
                    cartList = it.cart!!
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                    //  compareListFinal()
                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeAddressesList().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}")
                    /*for (i in it.addresses!!.indices)
                        Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                    if(!it.data!!.addresses!!.isNullOrEmpty())
                    {
                        SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.addresses!![0].customer_id.toString())
                        var findAddress = it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }
                        if(findAddress != null)
                        {
                            binding.homeText.text = findAddress.address_name
                            binding.addressText.text = findAddress.address_1
                        }
                        else{
                            binding.homeText.text = "Select address"
                            binding.addressText.text = "Address not selected"

                        }
                    }
                    else{
                        binding.homeText.text = "Add address"
                        binding.addressText.text = "Address not added"
                    }
                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }

        })

        binding.ivBack.setOnClickListener {

            onSupportNavigateUp()
        }

        binding.searchLayout.setOnClickListener {
            val intent = Intent(this , SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }
        binding.searchImage.setOnClickListener {
            val intent = Intent(this , SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }
        binding.searchText.setOnClickListener {
            val intent = Intent(this , SearchFromStoreActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("LATESTPRODUCTS" , latestProductsNamesList)// Pass the boolean value if needed
            startActivity(intent)
        }

        binding.goToCartView.setOnClickListener {
            try {
                if (cartList!!.items!!.size == 0) {
                    showCartEmptyDialog()
                } else {
                    val intent = Intent(this, ServicingCartActivity::class.java)
                    intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                    startActivity(intent)
                }
            }catch (e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        }


    }

   /* fun fromScreenCalled()
    {
        if(!intent.getStringExtra("FROMSCREEN").isNullOrEmpty()) {
            fromScreen = intent.getStringExtra("FROMSCREEN").toString()
            if (fromScreen == AppConstants.FROM_CATEGORIES) {
                val categoryId = intent.getStringExtra("CATEGORY_ID")
                if (!categoryId.isNullOrEmpty()) {
                    serviceViewModel?.getCategoryWiseProductList(categoryId)
                }
            }
        }
    }*/

    fun setLiveData()
    {
        serviceViewModel!!.observeCategoryWiseProductList().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                try {
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )
                        productsCollectionsCustomeAdapter =  ServicesProductListAdapter(
                            this,
                            it.products!!,
                            supportFragmentManager,
                            serviceViewModel!!
                        )
                        binding.servicesRecycler.layoutManager = GridLayoutManager(this,2)
                        binding.servicesRecycler.adapter = productsCollectionsCustomeAdapter
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        serviceViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    Logger.i("Aarati Store cart", "${it.cart!!.items!!.size} ${it.cart!!.toString()} ")
                    binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                    cartList = it.cart
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }


                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        serviceViewModel!!.observeDeleteFromCart().observe(this, Observer { response ->
            response?.let {
                try{
                    //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")

                    Logger.i("Aarati Store", "setLiveData ${it.deleted}")
                    if (it.deleted == true) {
                        cartList = it.parent
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                        Logger.i("Aarati Store cart", "${it.parent!!.items!!.size} ${it.parent!!.toString()} ")
                        binding.displayCartCount.setText(it.parent!!.items!!.size)
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
                //
            }
        })


        serviceViewModel!!.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    Logger.i("Aarati Store cart", "${it.cart!!.items!!.size} ${it.cart!!.toString()} ")
                    binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                    cartList = it.cart
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }



                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }


            }
        })

        serviceViewModel!!.observeSearchedProductList().observe(this , Observer { response ->
            response?.let {
                try {
                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        productsCollectionsCustomeAdapter = ServicesProductListAdapter(
                            this,
                            it.products!!,
                            supportFragmentManager,
                            serviceViewModel!!
                        )
                        binding.servicesRecycler.layoutManager = GridLayoutManager(this, 2)
                        binding.servicesRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }
                }catch (e :Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        serviceViewModel!!.observeFilterProducts().observe(this , Observer { response ->
            response?.let {
                try {
                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        productsCollectionsCustomeAdapter = ServicesProductListAdapter(
                            this,
                            it.products!!,
                            supportFragmentManager,
                            serviceViewModel!!
                        )
                        binding.servicesRecycler.layoutManager = GridLayoutManager(this, 2)
                        binding.servicesRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }
                    else
                    {
                        showListEmptyDialog()
                    }
                }catch (e :Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        serviceViewModel!!.observeAddWishListItem().observe(this , Observer{response ->
            response?.let {
                try{
                    Logger.i("Aarati Store wishlist", "setLiveData ${it.wishlist!!}")

                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        serviceViewModel!!.observeProductWithPriceList()
            .observe(this, Observer { response ->
                response?.let {
                    try {
                        Logger.i(
                            "Aarati Store test wishlist",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        fromScreen = intent.getStringExtra("FROMSCREEN").toString()
                        if(fromScreen.equals(AppConstants.TO_WISHLIST))
                        {
                            try {
                                Logger.i(
                                    "Aarati Store test wishlist",
                                    "in to wishlist}"
                                )
                                wishListItems  = it.products!!
                                    ?.filter { product ->
                                        product.variants?.any { it.isWishlisted == true } == true
                                    }
                                    ?.let { ArrayList(it) } ?: ArrayList()

                                for(i in wishListItems!!.indices)
                                {
                                    Logger.i("Aarati Store WISHLIST", "setLiveData ${wishListItems!![i].id + wishListItems!![i].title}")
                                }

                                wishListAdapter = ServicesWishlistCustomAdapter(
                                    this,
                                    wishListItems!!,
                                    serviceViewModel!!
                                )
                                binding.servicesRecycler.layoutManager = GridLayoutManager(this , 2)
                                binding.servicesRecycler.adapter = wishListAdapter
                                cartList?.let { wishListAdapter?.updateCart(it) }



                            } catch (e: Exception) {
                                CommonUtils.printStackTrace(e)
                            }
                        }
                        else {
                            for (i in it.products!!.indices) {
                                Logger.i(
                                    "Aarati Store",
                                    "setLiveData ${it.products!![i].id + it.products!![i].title}"
                                )

                                latestProductsNamesList.add(it.products!![i].title.toString())
                            }

                            productsCollectionsCustomeAdapter = ServicesProductListAdapter(
                                this,
                                it.products!!,
                                supportFragmentManager,
                                serviceViewModel!!
                            )
                            binding.servicesRecycler.layoutManager =
                                GridLayoutManager(this, 2)
                            binding.servicesRecycler.adapter = productsCollectionsCustomeAdapter
                            cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                        }
                    }catch (e :Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }
                }

            })

        serviceViewModel!!.observeDeleteWishListItem().observe(this , Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store test wishlist",
                        "setLiveData}"
                    )
                    cartList?.let { wishListAdapter?.updateCart(it) }
                    wishListItems?.let { wishListAdapter?.updateWishList(it) }
                    fromScreenFunction()

                }catch (e: Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

    }

    @OptIn(UnstableApi::class)
    fun fromScreenFunction()
    {
        if (isFilterApplied) return
        if(!intent.getStringExtra("FROMSCREEN").isNullOrEmpty()) {
            fromScreen = intent.getStringExtra("FROMSCREEN").toString()
            if(fromScreen == AppConstants.FROM_CATEGORIES)
            {
                val categoryId = intent.getStringExtra("CATEGORY_ID")
                if (!categoryId.isNullOrEmpty()) {
                    serviceViewModel?.getCategoryWiseProductList(categoryId)
                }
                selectedVideoUrl = intent.getStringExtra("VIDEO_URL").toString()
                val player = ExoPlayer.Builder(this).build().apply {
                    val mediaItem = MediaItem.fromUri(selectedVideoUrl)
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = true  // ⬅️ This enables autoplay
                    repeatMode = Player.REPEAT_MODE_ONE
                }
               binding.productVideo.useController = false
               binding.productVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
               binding.productVideo.player = player
            }
            else if(fromScreen == AppConstants.FROM_SEARCH_PRODUCT)
            {
                binding.searchLayout.visibility = View.GONE
                binding.productVideo.visibility = View.GONE
                binding.filterProducts.visibility = View.GONE
                val itemSearch = intent.getStringExtra("QUERY")
                if (!itemSearch.isNullOrEmpty()) {
                    serviceViewModel?.getProductWithPriceListWithQuery("*variants.calculated_price,+variants.inventory_quantity,*categories", SharedPrefSettings.getPreferences.fetchRegionId().toString(),itemSearch)
                }
                else
                {
                    serviceViewModel?.getProductWithPriceList(
                        "*variants.calculated_price,+variants.inventory_quantity,*categories",
                        SharedPrefSettings.getPreferences.fetchRegionId().toString()
                    )
                }
            }
            else if(fromScreen == AppConstants.TO_WISHLIST)
            {
                Logger.i(
                    "Aarati Store test wishlist",
                    "setLiveData"
                )
                serviceViewModel!!.getProductWithPriceList(
                    "*variants.calculated_price,+variants.inventory_quantity,*categories",
                    SharedPrefSettings.getPreferences.fetchRegionId().toString()
                )
            }
        }
    }


    private fun showCartEmptyDialog() {
        try {
            val dialog = Dialog(this)
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


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        // recreate()
    }

    @SuppressLint("SetTextI18n")
    private fun showListEmptyDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.tv_msg)
            val llclose = dialog.findViewById<TextView>(R.id.btnCloseDialog)
            llStartFreeTrial.text = "Products  not available for your selection"
            llclose.setOnClickListener{dialog.dismiss()}
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }
}