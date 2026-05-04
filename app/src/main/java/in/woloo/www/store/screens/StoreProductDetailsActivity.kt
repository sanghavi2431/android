package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentStoreProductDetailsBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.ColorItemListAdapter
import `in`.woloo.www.store.adapter.OptionsAdapter
import `in`.woloo.www.store.adapter.ProductImageRecylcerAdapter
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.adapter.SizeItemListAdapter
import `in`.woloo.www.store.adapter.StoreReviewAdapter
import `in`.woloo.www.store.adapter.TopBrandsCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.collections_response.CollectionsListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.reviews.ReviewListData
import `in`.woloo.www.utils.Logger
import java.text.DecimalFormat


class StoreProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: FragmentStoreProductDetailsBinding
    private lateinit var product: ProductListData
    private var storeViewModel: StoreViewModel? = null
    private  var cartList : CartParams? = null
    private lateinit var productList : ArrayList<ProductListData>
    private  var wishlistItemId : String = ""
    private var isWishListed : Boolean = false
    var variantPosition : Int = 0
    private var callingActivity : String = ""
    private var isBuyNow : Boolean = false

    lateinit var oprice : String
    lateinit var dprice : String
    lateinit var categoryId :String
    lateinit var  productImageRecylcerAdapter :ProductImageRecylcerAdapter

    private val selectedOptionsMap = mutableMapOf<String, String>()


    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val refresh = result.data?.getBooleanExtra("refresh", false) ?: false

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStoreProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        storeViewModel!!.getAddressesList()

        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
        storeViewModel!!.observeAddressesList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}"
                    )
                    /*for (i in it.addresses!!.indices)
                    Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                    if (!it.data!!.addresses!!.isNullOrEmpty()) {
                        SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.addresses!![0].customer_id.toString())
                        var findAddress =
                            it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }
                        if (findAddress != null) {
                            binding.addressName.text = findAddress.address_name
                            binding.fullAddress.text = findAddress.address_1
                        } else {
                            binding.addressName.text = "Select address"
                            binding.fullAddress.text = "Address not selected"

                        }
                    } else {
                        binding.addressName.text = "Add address"
                        binding.fullAddress.text = "Address not added"
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }

        })

        binding.viewAllProductsReviews.setOnClickListener {

            val bottomSheetFragment = StoreReviewsPopUpFragment.newInstance(product.id.toString() , product.title.toString())
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        }

        var address = SharedPrefSettings.getPreferences.fetchSelectedAddressId()
        if(address != null)
        {
            Logger.d("Address object" , address )
        }

        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)

        val productJson = intent.getStringExtra("PRODUCT_DETAILS")
        product = Gson().fromJson(productJson, ProductListData::class.java)
        isWishListed = product.variants?.any { it.isWishlisted == true } == true
        wishlistItemId = product.variants
            ?.firstOrNull { it.isWishlisted == true }
            ?.wishlist_item_id.toString()



        try {
            callingActivity = intent.getStringExtra("CALLINGACTIVITY")!!
        }
        catch (e : Exception) {}


        if(product.variants!![0].inventoryQuantity == 0) {
            binding.addCartView.isEnabled = false
            binding.addCartView.background = getDrawable(R.drawable.new_button_background_smallest)
            binding.addCartViewText.text = "Out of Stock"
        }
        Log.i("Aaarati Product Data" , productJson.toString())
        Log.i("Aaarati Product Data 1" , product.toString())
        Log.i("Aaarati Product Data 2" , product.title.toString())

        try {

                binding.productDetailsText.text = product.title
                binding.productDetailsVenderName.text = product.collection!!.title
                binding.productDetailsDescription.text = product.description
                storeViewModel!!.getReviewsListForProduct(product.id!!)

        }
        catch (e : Exception)
        {

        }

        try {
            categoryId = product.categories!![0].id.toString()
        }catch (e:Exception)
        { Log.i("Aaarati Product Data 2" , "After Adapter")
            categoryId = "pcat_01JPH852CKBG58XW4F482EZAS0"
        }

        binding.ivBack.setOnClickListener {

                val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
                if (isShowBackButton) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
           finish()

        }



        storeViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
try {
    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
    cartList = it.cart!!
    val matchFound =
        it.cart!!.items!!.find { item -> item.variant_id == product.variants!![variantPosition].id }
    if (matchFound != null) {
        binding.cartCountText.text = matchFound.quantity.toString()

        binding.addCartView.visibility = View.VISIBLE
     //   binding.quantityLayout.visibility = View.VISIBLE
    } else {


        binding.addCartView.visibility = View.VISIBLE
       // binding.quantityLayout.visibility = View.GONE

    }
}catch(e: Exception)
{
    CommonUtils.printStackTrace(e)
}
            }
        })





        // Handle back button visibility (if needed)


        storeViewModel!!.observeCitySalesChannelList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store city",
                        "setLiveData ${it.stock_locations!! + it.stock_locations!!.size}"
                    )
                    for (i in it.stock_locations!!.indices)
                        Logger.i(
                            "Aarati Store city",
                            "setLiveData ${it.stock_locations!![i].address!!.city}"
                        )
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })



        storeViewModel?.getCategoryWiseProductList(categoryId)

        try {
            val sizeValue = product.options
                ?.firstOrNull { it.title.equals("size", ignoreCase = true) || it.title.equals("sizes", ignoreCase = true) }
                ?.values
                ?.firstOrNull()
                ?.value

            if (sizeValue != null) {
                Logger.d("Aarati SizeValue", "First size: $sizeValue + ${product.variants!!.get(0).toString()}")
               binding.sizeText.visibility = View.VISIBLE
               binding.sizeText.text = sizeValue
            } else {
                Logger.d("Aarati SizeValue", "Size option not found or no values")
                binding.sizeText.visibility = View.GONE
            }
                     //   binding.productPrice.text = "₹" + product.variants!!.get(variantPosition).calculated_price!!.originalAmount.toString()

            binding.productPrice.text =
                "₹" + product.variants!!.get(0).calculated_price!!.calculatedAmount.toString() + "/-"
            binding.productPriceOriginal.apply{
                text =
                    "MRP ₹" + product.variants!!.get(0).calculated_price!!.originalAmount.toString() + "/-"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG }

           /* if(product.thumbnail != null) {
                Glide.with(this)
                    .load(product.thumbnail) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .into(binding.productDetailsImage)
            }
            else if(product.images!!.get(0).url != null)
            {
                Glide.with(this)
                    .load(product.images!!.get(0).url) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .error(R.drawable.feather_img)
                    .into(binding.productDetailsImage)
            }*/

        }catch (e : Exception)
        {
                CommonUtils.printStackTrace(e)
        }



        storeViewModel!!.observeCategoryWiseProductList().observe(this, Observer { response ->
            response?.let {

                try {
                    Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )
                        binding.productRecycler.layoutManager =    LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL , false)
                        binding.productRecycler.adapter = ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })



        storeViewModel!!.observeGetReviewListForProduct().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store REVIEWs",
                        "setLiveData ${it.data!!.reviews!! + it.data!!.reviews!!.size}"
                    )
                    binding.reviewCountText.setText("(${it.data!!.reviews!!.size})")
                  /*  if(it.data!!.reviews!!.size >= 2) {
                        val limitedList: ArrayList<ReviewListData> =
                            ArrayList(it.data!!.reviews!!.subList(0, minOf(2, it.data!!.reviews!!.size)))

                        binding.reviewRecycler.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        binding.reviewRecycler.adapter =
                            StoreReviewAdapter(this,limitedList, storeViewModel!!)

                    }
                    else{*/
                        binding.reviewRecycler.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        binding.reviewRecycler.adapter =
                            StoreReviewAdapter(this, it.data!!.reviews!!, storeViewModel!!)
                  //  }


                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })


        productImageRecylcerAdapter = ProductImageRecylcerAdapter(
            this,
            product.images!!,
            product.thumbnail!!,
            product.options!![0].values!!,
            0,
            product.variants!![variantPosition].id.toString(),
            isWishListed,
            wishlistItemId,
            storeViewModel!!
        )
        binding.productImageRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.productImageRecycler.adapter = productImageRecylcerAdapter

        productImageRecylcerAdapter.notifyDataSetChanged()

        if (isShowBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        storeViewModel!!.observeDeleteFromCart().observe(this, Observer { response ->
            response?.let {
                //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                // binding.displayCartCount.setText(it.cart!!.items.size.toString())
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.deleted}")
                    if (it.deleted == true) {
                        binding.cartCountText.text = "0"
                    }
                    val intent = intent
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        storeViewModel!!.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    /*val matchFound =
                        it.cart!!.items!!.find { item -> item.variant_id == product.variants!![variantPosition].id }
                    cartList = it.cart!!
                    if (matchFound != null) {
                        binding.cartCountText.text = matchFound.quantity.toString()
                    }*/
                   /* if(isBuyNow == true)
                    {*/
                        isBuyNow = false
                        val intent = Intent(this , ShoppingCartActivity::class.java)
                        intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                        startActivity(intent)

                   /* }
                    else {
                        val intent = intent
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }*/
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }


            }
        })

        storeViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    val matchFound =
                        it.cart!!.items!!.find { item -> item.variant_id == product.variants!![variantPosition].id }
                    cartList = it.cart!!
                    if (matchFound != null) {
                        binding.cartCountText.text = matchFound.quantity.toString()
                    }
                    val intent = intent
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        val optionsAdapter = OptionsAdapter(this ,product.options ?: arrayListOf()  , product.variants!! , product , productImageRecylcerAdapter , selectedOptionsMap)
        { updatedMap ->

            val displayText = updatedMap.entries.joinToString(", ") { (key, value) ->
                "${key.toString()}: ${value.toString()}"
            }
            val sizeValue = updatedMap.entries
                .find { it.key.equals("size", ignoreCase = true) }
                ?.value

            if (sizeValue != null) {
                binding.sizeText.text = "${sizeValue.capitalize()}"
            }
            updatePriceUI(updatedMap)

        }

        binding.optionsParentList.layoutManager = LinearLayoutManager(this)
        binding.optionsParentList.adapter = optionsAdapter





        binding.ivBack.setOnClickListener {
            SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
            if(callingActivity.isNotEmpty() && callingActivity.equals("STORELISTING" , ignoreCase = true)) {

                val intent = Intent(this, WolooDashboard::class.java)
                intent.putExtra("StoreListingFragment", "StoreListingFragment")
                startActivity(intent)
                finish()
            }
            else{
                onSupportNavigateUp()


        }
        }


        storeViewModel!!.observeAddWishListItem().observe(this , Observer{response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.wishlist!!}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })


        storeViewModel!!.observeDeleteWishListItem().observe(this , Observer{response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.wishlist!!}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })



        binding.changeAddress.setOnClickListener{
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.buyNowDirectPayment.setOnClickListener{
            /*val bottomSheetFragment = CartBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*/
            val request = CartAddRequest()
            request.variant_id = product.variants!!.get(variantPosition).id
            request.quantity = 1
            storeViewModel!!.getAddToCart(SharedPrefSettings.getPreferences.fetchCartId().toString() , request)
            isBuyNow = true

            val variantId = getMatchingVariantId()

            if (variantId != null) {
                Log.d("BUY_NOW", "Selected Variant ID: $variantId")
                // Use this variantId to add to cart, move to next screen, etc.
            } else {
                Toast.makeText(this, "Please select all options.", Toast.LENGTH_SHORT).show()
            }
/*
            val intent = Intent(this, ShoppingCartActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            activityResultLauncher.launch(intent)*/
        }

       /* binding.addToCartDialog.setOnClickListener{
          *//*  val bottomSheetFragment = CartBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*//*
            val request2 = CartAddRequest().apply {
                variant_id = product.variants!![variantPosition].id
                quantity = 1
            }
            storeViewModel?.getAddToCart(
                SharedPrefSettings.getPreferences.fetchCartId().toString(),
                request2
            )
        }*/


      /*  binding.removeCartItemImage.setOnClickListener{
            if(binding.cartCountText.text.toString().toInt() > 0) {
                binding.cartCountText.text =
                    (binding.cartCountText.text.toString().toInt() - 1).toString()
            }

        }*/


     /*   binding.buyNowButton.setOnClickListener{
            val request = CartAddRequest()
            request.variant_id = product.variants!!.get(variantPosition).id
            request.quantity = 1
            storeViewModel!!.getAddToCart(
                SharedPrefSettings.getPreferences.fetchCartId().toString(), request
            )
            isBuyNow = true

            val variantId = getMatchingVariantId()

            if (variantId != null) {
                Log.d("BUY_NOW", "Selected Variant ID: $variantId")
                // Use this variantId to add to cart, move to next screen, etc.
            } else {
                Toast.makeText(this, "Please select all options.", Toast.LENGTH_SHORT).show()
            }

          *//*  val intent = Intent(this , ShoppingCartActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            startActivity(intent)*//*
        }*/

binding.removeCartItemImage.setOnClickListener {
try {
    val variantId = getMatchingVariantId()

    if (variantId != null){
        var cartItemFound = cartList!!.items!!.find { it.variant_id == variantId}
        if (cartItemFound != null) {


            val quantityToCalculate: Int = (cartItemFound.quantity.toString()).toInt()
            if (quantityToCalculate > 1) {
                val request2 = CartUpdateRequest()
                request2.quantity = quantityToCalculate - 1
                storeViewModel!!.getUpdateToCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(),
                    cartItemFound.line_id.toString(),
                    request2
                )
                binding.cartCountText.text = cartItemFound.quantity.toString()
            } else if (quantityToCalculate == 1) {
                storeViewModel!!.getDeleteFromCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), cartItemFound.line_id.toString()
                )
                binding.cartCountText.text = cartItemFound.quantity.toString()
            } else {
                Logger.d("aarati Store", "no product in cart")
            }
        }
        val intent = intent
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)


   }
    else
   {
       var cartItemFound = cartList!!.items!!.find { it.variant_id == product.variants!![0].id }
       if (cartItemFound != null) {


           val quantityToCalculate: Int = (cartItemFound.quantity.toString()).toInt()
           if (quantityToCalculate > 1) {
               val request2 = CartUpdateRequest()
               request2.quantity = quantityToCalculate - 1
               storeViewModel!!.getUpdateToCart(
                   SharedPrefSettings.getPreferences.fetchCartId().toString(),
                   cartItemFound.line_id.toString(),
                   request2
               )
               binding.cartCountText.text = cartItemFound.quantity.toString()
           } else if (quantityToCalculate == 1) {
               storeViewModel!!.getDeleteFromCart(
                   SharedPrefSettings.getPreferences.fetchCartId().toString(), cartItemFound.line_id.toString()
               )
               binding.cartCountText.text = cartItemFound.quantity.toString()
           } else {
               Logger.d("aarati Store", "no product in cart")
           }
       }
       val intent = intent
       finish()
       overridePendingTransition(0, 0)
       startActivity(intent)
       overridePendingTransition(0, 0)
   }


}catch (e :Exception)
{

}
}
        binding.addCartView.setOnClickListener {

            // quantityToCalculate = (cartItemFound.quantity.toString()).toInt()

            val variantId = getMatchingVariantId()

            if (variantId != null) {
                Log.d("BUY_NOW", "Selected Variant ID: $variantId")
                val request1 = CartAddRequest()
                request1.variant_id = variantId
                request1.quantity = 1
                storeViewModel!!.getAddToCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), request1
                )
            } else {
                val request1 = CartAddRequest()
                request1.variant_id = product.variants?.get(0)?.id.toString()
                request1.quantity = 1
                storeViewModel!!.getAddToCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), request1
                )
            }


        }


        binding.addCartItemImage.setOnClickListener {
            try {

                var quantityToCalculate: Int = 1
                val variantId = getMatchingVariantId()

                if (variantId != null) {

                    val matchFound =
                        cartList!!.items!!.find { item -> item.variant_id == variantId }
                    if (matchFound == null) {
                        val request2 = CartAddRequest().apply {
                            variant_id =variantId
                            quantity = 1
                        }
                        storeViewModel?.getAddToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(),
                            request2
                        )
                        binding.cartCountText.text = (quantityToCalculate + 1).toString()
                    } else {
                        val request1 = CartUpdateRequest().apply {
                            quantity = matchFound.quantity.toString().toInt() + 1
                        }
                        storeViewModel?.getUpdateToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(),
                            matchFound.line_id.toString(),
                            request1
                        )
                    }
                }
                else{

                    val matchFound =
                        cartList!!.items!!.find { item -> item.variant_id == product.variants!![variantPosition].id }
                    if (matchFound == null) {
                        val request2 = CartAddRequest().apply {
                            variant_id = product.variants!![variantPosition].id
                            quantity = 1
                        }
                        storeViewModel?.getAddToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(),
                            request2
                        )
                        binding.cartCountText.text = (quantityToCalculate + 1).toString()
                    } else {
                        val request1 = CartUpdateRequest().apply {
                            quantity = matchFound.quantity.toString().toInt() + 1
                        }
                        storeViewModel?.getUpdateToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(),
                            matchFound.line_id.toString(),
                            request1
                        )
                    }

                }

        }catch(e : Exception)
            {
                CommonUtils.printStackTrace(e)

            }

    }
}

    private fun getMatchingVariantId(): String? {
        for (variant in product.variants.orEmpty()) {
            val variantMap = variant.options?.associateBy(
                { opt -> product.options?.find { it.id == opt.option_id }?.title?.lowercase().orEmpty() },
                { it.value.orEmpty().lowercase() }
            )

            if (variantMap == selectedOptionsMap) {
                return variant.id
            }
        }
        return null
    }

    private fun updatePriceUI(selectedMap: Map<String, String>) {
        val selectedVariant = product.variants?.find { variant ->
            val variantOptions = variant.options?.associateBy(
                { option -> product.options?.find { it.id == option.option_id }?.title?.lowercase() ?: "" },
                { it.value?.lowercase() ?: "" }
            )

            variantOptions == selectedMap.mapValues { it.value.lowercase() }
        }

        if (selectedVariant != null) {
            val price = selectedVariant.calculated_price?.originalAmount ?: 0
            val decimalFormat = DecimalFormat("0.00")
            binding.productPrice.text = "₹" + decimalFormat.format(price).toString() + "/-"  // Convert paise to rupees if needed
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}