package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentStoreProductListBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.adapter.WishlistCustomeAdapter
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger


class StoreProductListActivity  : AppCompatActivity() {

    lateinit var binding : FragmentStoreProductListBinding
    private lateinit var product: ArrayList<ProductListData>
    private lateinit var productList: ArrayList<ProductListData>
    private var storeViewModel: StoreViewModel? = null
    private var cartList: CartParams? = null
    private var wishListItems : ArrayList<ProductListData>? = null
    private var productsCollectionsCustomeAdapter: ProductsCollectionsCustomeAdapter? = null
    private var wishListAdapter: WishlistCustomeAdapter? = null
    var  fromScreen: String = ""
    private  var latestProductsNamesList : ArrayList<String> = ArrayList()
    var isFilterApplied = false


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStoreProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
        storeViewModel!!.getAddressesList()

        fromScreenFunction()

        binding.swipeRefreshLayout.setOnRefreshListener {
           binding.swipeRefreshLayout.isRefreshing = true
            storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
            storeViewModel!!.getAddressesList()
            fromScreenFunction()

//            productsCollectionsCustomeAdapter!!.notifyDataSetChanged()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.filterProducts.setOnClickListener {
            val bottomSheetFragment = FilterProductsFragment()
            bottomSheetFragment.filterListener = object :
                FilterProductsFragment.OnFilteredListener {
                override fun onFilterApplied(
                    selectedCategory: String,
                    slectedSizeForFilter: String,
                    FROMSCREEN: String
                ) {
                    isFilterApplied = true
                   /* recreate()
                    Log.d("Aarati Filters" , "${selectedCategory} ${slectedSizeForFilter}")
                    Logger.i("Aarati selected" , "${selectedCategory} + ${slectedSizeForFilter}")
                    if(selectedCategory == null)
                    {
                        storeViewModel!!.getFilterProductList(slectedSizeForFilter)
                    }
                    else if (slectedSizeForFilter == null)
                    {
                        storeViewModel!!.getCategoryWiseProductList(selectedCategory)
                    }
                    else
                    {
                        storeViewModel!!.getFilterProductList(selectedCategory, "${slectedSizeForFilter}")
                    }*/
                    when {
                        selectedCategory.isEmpty() -> {
                            storeViewModel!!.getFilterProductList("" ,slectedSizeForFilter)
                        }

                        slectedSizeForFilter.isEmpty() -> {
                            storeViewModel!!.getCategoryWiseProductList(selectedCategory)
                        }

                        else -> {
                            storeViewModel!!.getFilterProductList(
                                selectedCategory,
                                slectedSizeForFilter
                            )
                        }
                    }
                }
            }
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        storeViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
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
                    productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                        this,
                        product,
                        storeViewModel!!
                    )
                    binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                    binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                }
            }
        }

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
            if(cartList!!.items!!.size == 0)
            {
                showCartEmptyDialog()
            }
            else {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                startActivity(intent)
            }
        }


        storeViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
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

        storeViewModel!!.observeDeleteFromCart().observe(this, Observer { response ->
            response?.let {
                try{
                    //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    binding.displayCartCount.setText("0")
                    Logger.i("Aarati Store", "setLiveData ${it.deleted}")
                    if (it.deleted == true) {
                        cartList = it.parent
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
                //
            }
        })


        storeViewModel!!.observeAddToCart().observe(this, Observer { response ->
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

        storeViewModel!!.observeSearchedProductList().observe(this , Observer { response ->
            response?.let {
                try {
                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                        binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }
                }catch (e :Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        storeViewModel!!.observeFilterProducts().observe(this , Observer { response ->
            response?.let {
                try {
                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                        binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
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

        storeViewModel!!.observeCategoryWiseProductList().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                try {
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )
                        productsCollectionsCustomeAdapter =  ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                        binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }

                    }
                    else{
                        showListEmptyDialog()
                    }
                } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeAddWishListItem().observe(this , Observer{response ->
            response?.let {
                try{
                Logger.i("Aarati Store wishlist", "setLiveData ${it.wishlist!!}")

                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        storeViewModel!!.observeCollectionWiseProductList().observe(this, Observer { response ->
            response?.let {

                try {
                    Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )
                        productsCollectionsCustomeAdapter =  ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                        binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeProductWithPriceList()
            .observe(this, Observer { response ->
                response?.let {
                    try {
                        Logger.i(
                            "Aarati Store test wishlist",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
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

                                wishListAdapter = WishlistCustomeAdapter(
                                        this,
                                       wishListItems!!,
                                        storeViewModel!!
                                    )
                                    binding.collectionsRecycler.layoutManager = GridLayoutManager(this , 3)
                                    binding.collectionsRecycler.adapter = wishListAdapter
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

                            productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                                this,
                                it.products!!,
                                storeViewModel!!
                            )
                            binding.collectionsRecycler.layoutManager =
                                GridLayoutManager(this, 3)
                            binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                            cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                        }
                    }catch (e :Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }
                }

            })

        storeViewModel!!.observeDeleteWishListItem().observe(this , Observer { response ->
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


    fun fromScreenFunction()
    {
        if (isFilterApplied) return
        if(!intent.getStringExtra("FROMSCREEN").isNullOrEmpty()) {
            fromScreen = intent.getStringExtra("FROMSCREEN").toString()
            if(fromScreen == AppConstants.FROM_CATEGORIES)
            {
                val categoryId = intent.getStringExtra("CATEGORY_ID")
                if (!categoryId.isNullOrEmpty()) {
                    storeViewModel?.getCategoryWiseProductList(categoryId)
                }
            }
           /* else  if(fromScreen == AppConstants.FROM_FILTER_CATEGORIES)
            {
                try {
                    val jsonString = intent.getStringExtra("productArray")
                    val productFilterList: ArrayList<ProductListData> = Gson().fromJson(
                        jsonString,
                        object : TypeToken<ArrayList<ProductListData>>() {}.type)

                    for (i in productFilterList.indices) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${productFilterList[i].id + productFilterList[i].title}"
                        )

                        latestProductsNamesList.add(productFilterList[i].title.toString())
                    }

                    productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                        this,
                        productFilterList,
                        storeViewModel!!
                    )
                    binding.collectionsRecycler.layoutManager =
                        GridLayoutManager(this, 3)
                    binding.collectionsRecycler.adapter = productsCollectionsCustomeAdapter
                    cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }

            }catch (e :Exception)
            {
                CommonUtils.printStackTrace(e)
            }


            }*/
            else if(fromScreen == AppConstants.FROM_TOP_BRANDS)
            {
                val collectionId = intent.getStringExtra("COLLECTION_ID")
                if (!collectionId.isNullOrEmpty()) {
                    storeViewModel?.getCollectionWiseProductList(collectionId)
                }
            }
            else if(fromScreen == AppConstants.FROM_SEARCH_PRODUCT)
            {
                binding.searchLayout.visibility = View.GONE
                binding.filterProducts.visibility = View.GONE
                val itemSearch = intent.getStringExtra("QUERY")
                if (!itemSearch.isNullOrEmpty()) {
                    storeViewModel?.getProductWithPriceListWithQuery("*variants.calculated_price,+variants.inventory_quantity,*categories", SharedPrefSettings.getPreferences.fetchRegionId().toString(),itemSearch)
                }
                else
                {
                    storeViewModel?.getProductWithPriceList(
                        "*variants.calculated_price,+variants.inventory_quantity,*categories",
                        SharedPrefSettings.getPreferences.fetchRegionId().toString()
                    )
                }
            }
            else if(fromScreen == AppConstants.FROM_COLLECTIONS_SEE_MORE)
            {
                storeViewModel!!.getProductWithPriceList(
                    "*variants.calculated_price,+variants.inventory_quantity,*categories",
                    SharedPrefSettings.getPreferences.fetchRegionId().toString()
                )
            }
            else if(fromScreen == AppConstants.FROM_PERIOD_CATEGORY)
            {
                val categoryId = intent.getStringExtra("CATEGORY_ID")
                if (!categoryId.isNullOrEmpty()) {
                    storeViewModel?.getCategoryWiseProductList(categoryId)
                }
            }
            else if(fromScreen == AppConstants.TO_WISHLIST)
            {
                storeViewModel!!.getProductWithPriceList(
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