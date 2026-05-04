package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentShoppingCartBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CartItemsCustomAdapter
import `in`.woloo.www.store.cart_request_response.AddPromotionsRequest
import `in`.woloo.www.store.cart_request_response.Address
import `in`.woloo.www.store.cart_request_response.CalculateShippingRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.ShippingBillingAddressRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequestParam
import `in`.woloo.www.store.cart_request_response.ShippingOptionCalculate
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logNetcoreEvent


class ShoppingCartActivity  : AppCompatActivity() , InventoryBottomSheetFragment.InventoryActionListener {

    lateinit var binding : FragmentShoppingCartBinding
    private var storeViewModel: StoreViewModel? = null
    lateinit var providerId : String
    private lateinit var orderId : String
    private lateinit var orderIdAfterPayment : String
    private lateinit var amount : String
    private var profileViewModel: ProfileViewModel? = null
    private var useCoins : Boolean = false
    private var shippingAddress : String = ""
    private  var totalAmountFinal : String = ""
    private  var discountedAmountFinal : String = ""
    private  var productsAmountFinal : String = ""
    private  var promotionApplied : String = ""
    private  var shippingAmount : String = "0"
    private var coinsUsed : Boolean = false
    private  var pendingCoins : String= ""
    var findAddress : AddressList? = null
    var options: ArrayList<ShippingMethodsRequestParam> = ArrayList()
    var findDefaultAddress : AddressList? = null
    var cartItemsCustomAdapter : CartItemsCustomAdapter? = null
    private lateinit var progressView: CustomProgressView
    private var cartList : Int = 0
    private var deletedPromotion : String = ""
    private var deletedWolooPromotion : String = ""
    private lateinit var addressList : ArrayList<AddressList>
    private lateinit var cartListForInventory : ArrayList<CartLineItems>
    private var userPromotion : String = ""


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShoppingCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Checkout.preload(applicationContext)
        progressView = CustomProgressView(this)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )



        storeViewModel!!.getAddressesList()
        profileViewModel!!.getUserProfile()

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

     /*   binding.deletePromotion.setOnClickListener {
            try {
                if(binding.userPromocode.text.toString() != null)
                {
                var request = AddPromotionsRequest()
                request.promo_codes = ArrayList<String>(
                    listOf(
                        binding.userPromocode.text.toString()
                    )
                )
                storeViewModel!!.getDeletePromotionFromCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                )
                    }
            }catch (e : Exception)
            {

            }
        }*/


        storeViewModel!!.observeAddressesList().observe(this, Observer { response ->
            response?.let {

                try {
                    Logger.i("Aarati Store", "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}")
                    /*for (i in it.addresses!!.indices)
                    Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                    if (!it.data!!.addresses!!.isNullOrEmpty()) {
                      //  SharedPrefSettings.getPreferences.storeSelectedAddressId(it.addresses!![0].customer_id.toString())
                        SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.addresses!![0].customer_id.toString())
                        if (!SharedPrefSettings.getPreferences.fetchSelectedAddressId()
                                .isNullOrEmpty()
                        ) {
                            addressList =  it.data!!.addresses!!
                            findAddress =
                                it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }!!
                            if (findAddress != null) {
                                binding.homeText.text = findAddress!!.address_name
                                binding.addressText.text = findAddress!!.address_1
                            } else {
                                binding.homeText.text = "Select address"
                                binding.addressText.text = "Address not selected"

                            }

                       //     findDefaultAddress =   it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchDefaultAddressId() }!!

                            val defaultId = SharedPrefSettings.getPreferences.fetchDefaultAddressId()
                            findDefaultAddress = it.data!!.addresses!!.find { it.id == defaultId }

                        }
                    } else {
                        binding.homeText.text = "Add address"
                        binding.addressText.text = "Address not added"
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }

        })

        var address = SharedPrefSettings.getPreferences.fetchSelectedAddressId()
        if(address != null)
        {
            Logger.d("Address object" , address )
        }

        binding.ivBack.setOnClickListener {
            SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
            val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
            if (isShowBackButton) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

        }


        // Handle back button visibility (if needed)
        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {
            val intent = Intent()
            intent.putExtra("refresh", true)
            setResult(Activity.RESULT_OK, intent)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.ivBack.setOnClickListener{
            onSupportNavigateUp()
        }

        binding.applyPromotion.setOnClickListener {
            userPromotion = "1"
            if(binding.applyPromotion.text.toString().equals("Apply" , ignoreCase = true)) {
                if (!binding.userPromocode.text.isNullOrEmpty()) {


                    var request = AddPromotionsRequest()
                    request.promo_codes = ArrayList<String>(
                        listOf(
                            binding.userPromocode.text.toString()
                        )
                    )

                    storeViewModel!!.getAddPromotionToCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                    )

                }
            }
            else {
                try {
                    if(binding.userPromocode.text.toString() != null)
                    {
                        var request = AddPromotionsRequest()
                        request.promo_codes = ArrayList<String>(
                            listOf(
                                binding.userPromocode.text.toString()
                            )
                        )
                        storeViewModel!!.getDeletePromotionFromCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                        )
                        deletedPromotion = binding.userPromocode.text.toString()
                    }
                }catch (e : Exception)
                {

                }
            }
        }



        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())

        storeViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
try {
    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
    cartList = it.cart!!.items!!.size
    cartListForInventory = it.cart!!.items!!
    cartItemsCustomAdapter =
        CartItemsCustomAdapter(this, it.cart!!.items!!, storeViewModel!!, false)
    binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
    binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
    binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
    binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
    if(shippingAmount == null)
        shippingAmount = "0"
    binding.totalAmount.text = "₹ ${shippingAmount}/-"
    binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
    binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"

    val promotions = it.cart?.promotions.orEmpty()
    val promoCode = promotions
        .firstOrNull { promo -> promo.code != "WOLOO_COINS" }
        ?.code
    binding.userPromocode.setText(promoCode ?: "")
    if(promoCode !=null)
    {
        binding.applyPromotion.setText("Remove")
    }

    val promoCodeWoloo = promotions
        .firstOrNull { promo -> promo.code == "WOLOO_COINS" }
        ?.code
    if(promoCodeWoloo !=null)
    {
        binding.applyWolooPromotion.setText("Remove")
    }

    totalAmountFinal = it.cart!!.total.toString()
    discountedAmountFinal = it.cart!!.discount_total.toString()
    productsAmountFinal = it.cart!!.original_item_total.toString()
    var quantityTotal: Int = 0
    for (i in it.cart!!.items!!.indices) {
        quantityTotal = it.cart!!.items!!.size
    }
    binding.totalCartQuantity.text = "Total Items : ${quantityTotal} Units"
}catch (e : Exception)
{
    CommonUtils.printStackTrace(e)
}
            }
        })

        storeViewModel!!.observeAddPromotionToCart().observe(this, Observer { response ->
            response?.let {
try {
    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
    /* binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                binding.deliveryDetailsRecycler.adapter =
                    CartItemsCustomAdapter(this, it.cart!!.items, storeViewModel!!)*/
    if(it.cart!!.promotions != null) {
        binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
        binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
        if (shippingAmount == null)
            shippingAmount = "0"
        binding.totalAmount.text = "₹ ${shippingAmount}/-"
        binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
        binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
        totalAmountFinal = it.cart!!.total.toString()
        discountedAmountFinal = it.cart!!.discount_total.toString()
        productsAmountFinal = it.cart!!.original_item_total.toString()


        val promoList = it.cart?.promotions ?: emptyList()

        val userCode = binding.userPromocode.text.toString().trim()

// --- Check if WOLOO_COINS applied ---
        val isWolooApplied =
            promoList.any { promo -> promo.code.equals("WOLOO_COINS", ignoreCase = true) }
        binding.applyWolooPromotion.text = if (isWolooApplied) "Remove" else "Apply"
        SharedPrefSettings.getPreferences.storeCoinsUsed(isWolooApplied)
        coinsUsed = isWolooApplied

// --- Check if USER promo code applied ---
        val isUserPromoApplied =
            promoList.any { promo -> promo.code.equals(userCode, ignoreCase = true) }
        binding.applyPromotion.text = if (isUserPromoApplied) "Remove" else "Apply"
        promotionApplied = if (isUserPromoApplied) userCode else ""
        if (binding.applyPromotion.text.toString()
                .equals("Apply", ignoreCase = true) && !isUserPromoApplied
        )
        {
            if (userPromotion.isNotEmpty()) {
                showLoginFailureDialog("Promo code not valid")
            }

    }

}

    userPromotion = ""

}catch (e : Exception)
{
    CommonUtils.printStackTrace(e)
}
            }
        })

        storeViewModel!!.observeDeletePromotionFromCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")

                    binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                    binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
                    binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
                    totalAmountFinal = it.cart!!.total.toString()
                    discountedAmountFinal = it.cart!!.discount_total.toString()
                    productsAmountFinal = it.cart!!.original_item_total.toString()

                    val promoList = it.cart?.promotions ?: emptyList()

                    val userCode = deletedPromotion
                    if(userCode.isNotEmpty()) {
                        val isUserPromoStillApplied = promoList.any { promo ->
                            promo.code.equals(
                                userCode,
                                ignoreCase = true
                            )
                        }
                        binding.applyPromotion.text =
                            if (isUserPromoStillApplied) "Remove" else "Apply"
                        binding.applyWolooPromotion.text = binding.applyWolooPromotion.text
                        deletedPromotion = ""
                        binding.userPromocode.text = null
                    }

                    val userCodeWoloo = deletedWolooPromotion
if(userCodeWoloo.isNotEmpty()){
                    val isWolooStillApplied = promoList.any { promo -> promo.code.equals(userCodeWoloo, ignoreCase = true) }
                    binding.applyWolooPromotion.text = if (isWolooStillApplied) "Remove" else "Apply"
                    deletedWolooPromotion = ""
    binding.applyPromotion.text = binding.applyPromotion.text
                    SharedPrefSettings.getPreferences.storeCoinsUsed(isWolooStillApplied)
                    coinsUsed = isWolooStillApplied
                }
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
                // binding.displayCartCount.setText(it.cart!!.items.size.toString())\\

                    if(it.parent!!.items!!.size == 0)
                    {
                        cartList = 0
                    }
                        cartItemsCustomAdapter =
                            CartItemsCustomAdapter(this, it.parent.items!!, storeViewModel!!, false)
                        binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                        binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
                        binding.productPrice.text =
                            "₹ ${it.parent.original_item_total.toString()}/-"
                        binding.discountPrice.text = "₹ ${it.parent.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                        binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.totalAmount.text = "₹ ${it.parent!!.original_shipping_total.toString()}/-"
                        binding.grandTotal.text = "₹ ${it.parent.total.toString()}/-"
                        totalAmountFinal = it.parent.total.toString()
                        discountedAmountFinal = it.parent.discount_total.toString()
                        productsAmountFinal = it.parent.original_item_total.toString()

                    var quantityTotal: Int = 0
                    for (i in it.parent!!.items!!.indices) {
                        quantityTotal = it.parent!!.items!!.size
                    }
                    binding.totalCartQuantity.text = "Total Items : ${quantityTotal} Units"
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                cartItemsCustomAdapter =   CartItemsCustomAdapter(this, it.cart!!.items!!, storeViewModel!! , false)
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
                binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
                binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
                totalAmountFinal = it.cart!!.total.toString()
                discountedAmountFinal = it.cart!!.discount_total.toString()
                productsAmountFinal = it.cart!!.original_item_total.toString()

                    var quantityTotal: Int = 0
                    for (i in it.cart!!.items!!.indices) {
                        quantityTotal = it.cart!!.items!!.size
                    }
                    binding.totalCartQuantity.text = "Total Items : ${quantityTotal} Units"

                /* cartList = it.cart
                 adapter = ProductsCollectionsCustomeAdapter(requireActivity() , productList , cartList!! , storeViewModel!!)
                 adapter.notifyDataSetChanged()*/
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                binding.deliveryDetailsRecycler.adapter =
                    CartItemsCustomAdapter(this, it.cart!!.items!!, storeViewModel!! , false)
                binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
                binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
                totalAmountFinal = it.cart!!.total.toString()
                discountedAmountFinal = it.cart!!.discount_total.toString()
                productsAmountFinal = it.cart!!.original_item_total.toString()
                    var quantityTotal: Int = 0
                    for (i in it.cart!!.items!!.indices) {
                        quantityTotal = it.cart!!.items!!.size
                    }
                    binding.totalCartQuantity.text = "Total Items : ${quantityTotal} Units"

                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        binding.applyWolooPromotion.setOnClickListener {

            if(binding.applyWolooPromotion.text.toString().equals("Apply" , ignoreCase = true)) {
                try{
                var request = AddPromotionsRequest()
                request.promo_codes = ArrayList<String>(
                    listOf(
                        "WOLOO_COINS",
                    )
                )
                storeViewModel!!.getAddPromotionToCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                )
                profileViewModel!!.getUserProfile()
                    }catch (e : Exception)
                {

                    }
            }
            else{
                try {
                    var request = AddPromotionsRequest()
                    request.promo_codes = ArrayList<String>(
                        listOf(
                            "WOLOO_COINS",
                        )
                    )
                    storeViewModel!!.getDeletePromotionFromCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                    )
                    deletedWolooPromotion =  "WOLOO_COINS"
                }catch (e : Exception)
                {

                }
            }

        }


        profileViewModel!!.observeUserProfile().observe(
            this
        ) { userProfileBaseResponse ->
            if (userProfileBaseResponse != null) {
                if (userProfileBaseResponse.data != null) {
                    try {

                        Log.i("aarati Woloo Coins ", "${userProfileBaseResponse.data!!.totalCoins!!.totalCoins}")
                        binding.showWolooCoins.text = "You have ${userProfileBaseResponse.data!!.totalCoins!!.totalCoins.toString()} Woloo Points to Redeem"
                        pendingCoins = userProfileBaseResponse.data!!.totalCoins!!.totalCoins.toString()
                        discountOfWolooCoins(binding.showWolooCoins.text.toString())

                    }catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } else {
                //                    displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }
        }


        binding.checkoutForPayment.setOnClickListener {

                try {
                    if(cartList == 0)
                    {
                        Toast.makeText(applicationContext , "Please add items to cart" , Toast.LENGTH_SHORT).show()

                    }
                    else {
                        /* val bottomSheetFragment = OrderSummaryBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)*/
                        progressView.show()
                        if (SharedPrefSettings.getPreferences.fetchSelectedAddressId()
                                .isNullOrEmpty() ||
                            SharedPrefSettings.getPreferences.fetchSelectedAddressId() == ""
                        ) {
                            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
                            bottomSheetFragment.show(
                                supportFragmentManager,
                                bottomSheetFragment.tag
                            )
                        } else {
                            if (findAddress?.postal_code != null) {
                               storeViewModel!!.getCheckCartInventory(SharedPrefSettings.getPreferences.fetchCartId().toString().toString())
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Please add complete address",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressView.hide()
                            }
                        }
                    }
                } catch (e: Exception) {
                    progressView.hide()
                    CommonUtils.printStackTrace(e)
                    Toast.makeText(applicationContext, "Please Select Address", Toast.LENGTH_SHORT)
                        .show()
                    val bottomSheetFragment = AddressBottomSheetFragment.newInstance("STORE")
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }
            }

        storeViewModel!!.mCheckInventorySuccess.observe(this, Observer { response ->
            response?.let {
                try {
                storeViewModel!!.getDeliveryCodesList(findAddress!!.postal_code.toString())
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)

                }
            }
        })

        storeViewModel!!.mCheckInventoryError.observe(this, Observer { errorResponse ->
            errorResponse?.let {
                progressView.hide()
                Logger.i("Aarati Store Inventory", "setLiveData ${it.errors}")
                val message = buildString {
                    append("Some items are out of stock:\n\n")
                    it.errors.forEach { error ->
                        append("• ${error.title} (Requested: ${error.requestedQuantity}, Available: ${error.availableQuantity})\n")
                    }
                }

                Logger.i("Aarati Store Inventory", "setLiveData ${message}")
               /* lateinit var variantsList : ArrayList<String>
                for ( i in it.errors.indices)
                {
                        variantsList.add(it.errors[i].variantId)
                }*/
                val variantsList = ArrayList(it.errors.map { error -> error.variantId })
                val filteredCartItems = ArrayList(cartListForInventory.filter { item ->
                    item.variant_id in variantsList
                })

                val bottomSheetFragment = InventoryBottomSheetFragment.newInstance(filteredCartItems)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        })

        storeViewModel!!.observeDeliveryCodesList().observe(this ,Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.deliveryCodes!!}")
                if(!it.deliveryCodes!!.isNullOrEmpty())
                {
                    if(!it.deliveryCodes[0].postalCode.center.isNullOrEmpty())
                    {
                        val shippingAddress = Address(

                            firstName = findAddress!!.first_name.toString(),
                            lastName = findAddress!!.last_name.toString(),
                            address1 = findAddress!!.address_1!!.replace("~", "").toString(),
                            city = findAddress!!.city.toString(),
                            countryCode = "in",
                            province = findAddress!!.province.toString(),
                            postalCode = findAddress!!.postal_code.toString(),
                            phone = findAddress!!.phone.toString()


                        )


                        val defaultId = SharedPrefSettings.getPreferences.fetchDefaultAddressId()
                        Logger.i("Aarati Default Address", "$defaultId Default Address try")

                        val defaultAddress = addressList?.find { it.id == defaultId }

                        val billingAddress = if (defaultAddress?.id.isNullOrEmpty()) {
                            findAddress?.let {
                                Address(
                                    firstName = it.first_name.orEmpty(),
                                    lastName = it.last_name.orEmpty(),
                                    address1 = it.address_1!!.replace("~", " ").orEmpty(),
                                    city = it.city.orEmpty(),
                                    countryCode = "in",
                                    province = it.province.orEmpty(),
                                    postalCode = it.postal_code.orEmpty(),
                                    phone = it.phone.orEmpty()
                                )
                            }
                        } else {
                            Address(
                                firstName = defaultAddress!!.first_name.orEmpty(),
                                lastName = defaultAddress.last_name.orEmpty(),
                                address1 = defaultAddress.address_1!!.replace("~", "").orEmpty(),
                                city = defaultAddress.city.orEmpty(),
                                countryCode = "in",
                                province = defaultAddress.province.orEmpty(),
                                postalCode = defaultAddress.postal_code.orEmpty(),
                                phone = defaultAddress.phone.orEmpty()
                            )
                        }

                        if (billingAddress != null) {
                            val request = ShippingBillingAddressRequest().apply {
                                this.shippingAddress = shippingAddress
                                this.billingAddress = billingAddress
                            }

                            storeViewModel?.getAddShippingBillingToCart(
                                SharedPrefSettings.getPreferences.fetchCartId().orEmpty(),
                                request
                            )
                        }
                        else{
                            val request = ShippingBillingAddressRequest().apply {
                                this.shippingAddress = shippingAddress
                                this.billingAddress = shippingAddress
                            }

                            storeViewModel?.getAddShippingBillingToCart(
                                SharedPrefSettings.getPreferences.fetchCartId().orEmpty(),
                                request
                            )
                        }

                    }

                }  else{
                    Toast.makeText(applicationContext , "Delivery Not Available at this pin code" , Toast.LENGTH_SHORT).show()
                }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        storeViewModel!!.observeAddShippingBillingToCart().observe(this, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    shippingAddress = it.cart!!.shipping_address!!.first_name.toString() + "\n" + it.cart!!.shipping_address!!.address_1.toString()

                storeViewModel!!.getShippingOptions( SharedPrefSettings.getPreferences.fetchCartId().toString())}catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        storeViewModel!!.observeShippingOptions().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.shipping_options!!}")
                    progressView.hide()
                    shippingAmount = it.shipping_options[0].amount.toString()
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
                  //  binding.totalAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
                     options = ArrayList(
                         it.shipping_options.map { shipping ->
                             ShippingMethodsRequestParam(
                                 optionId = shipping.id
                             )
                         }
                     )

                    var request = ShippingMethodsRequest()
                    request.options = options
                    storeViewModel!!.getShippingMethodsToCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(), request
                    )





                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

                /* try{
              Logger.i("Aarati Store", "setLiveData ${it.shipping_options!!}")
              if(!it.shipping_options[0].name.isNullOrEmpty()) {
                  var requestCal = CalculateShippingRequest()
                  requestCal.cartId = SharedPrefSettings.getPreferences.fetchCartId().toString()
                  storeViewModel!!.getCalculateShippingOptions(
                      it.shipping_options[0].id!!,
                      requestCal
                  )
              }
              else{
                  Toast.makeText(applicationContext , "product Available at given address" , Toast.LENGTH_SHORT).show()
              }
              }catch (e : Exception)
              {
                  CommonUtils.printStackTrace(e)
              }*/

            }
        })

       /* storeViewModel!!.observeCalculateShippingOptions().observe(this, Observer { response ->
            response?.let {
                try{
                Logger.i("Aarati Store", "setLiveData ${it.shippingOption!!}")
                progressView.hide()
                shippingAmount = it.shippingOption[0].amount.toString()
                    val options = ArrayList(
                        it.shippingOption.map { shipping ->
                            ShippingMethodsRequestParam(
                                optionId = shipping.id
                            )
                        }
                    )
                val bottomSheetFragment = CartBottomSheetFragment.newInstance(options ,promotionApplied , shippingAmount , coinsUsed , pendingCoins , shippingAddress , totalAmountFinal , discountedAmountFinal , productsAmountFinal)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })*/


        storeViewModel!!.observeShippingMethodsToCart().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                shippingAmount = it.cart!!.shipping_total.toString()
                val bottomSheetFragment = CartBottomSheetFragment.newInstance(options ,promotionApplied , shippingAmount , coinsUsed , pendingCoins , shippingAddress , totalAmountFinal , discountedAmountFinal , productsAmountFinal)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)


            }
        })




    }
    override fun onSupportNavigateUp(): Boolean {

        finish()
        return true
    }


    override fun onDestroy() {
        super.onDestroy()

    }




    fun discountOfWolooCoins(coins : String)
    {
        val numberOnly = coins.filter { it.isDigit() }
        val coinsA = numberOnly.toIntOrNull() ?: 0
        //var coinsA = coins.toInt()

        if(coinsA >= 10)
        {
           binding.applyWolooPromotion.isEnabled = true

        }
        else{
            binding.applyWolooPromotion.isEnabled = false
            binding.userCanUse.text = "Minimum 10 Woloo Coins Required."

        }
    }

    override fun onNotifyMeClicked() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun showLoginFailureDialog(msg: String?) {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
            val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
            tv_msg.text = msg
            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


}