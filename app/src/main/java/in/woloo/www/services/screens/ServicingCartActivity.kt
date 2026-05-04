package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentShoppingCartBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.ServicesCartItemCustomAdapter
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.cart_request_response.AddPromotionsRequest
import `in`.woloo.www.store.cart_request_response.Address
import `in`.woloo.www.store.cart_request_response.ShippingBillingAddressRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequestParam
import `in`.woloo.www.store.screens.AddressBottomSheetFragment
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger

class ServicingCartActivity  : AppCompatActivity(){

    lateinit var binding : FragmentShoppingCartBinding
    private var storeViewModel: StoreViewModel? = null
    private var servicesViewModel: ServiceViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    private var shippingAddress : String = ""
    private  var totalAmountFinal : String = ""
    private  var discountedAmountFinal : String = ""
    private  var productsAmountFinal : String = ""
    private  var promotionApplied : String = ""
    private  var shippingAmount : String = "0"
    private var coinsUsed : Boolean = false
    private  var pendingCoins : String= ""
    var findAddress : AddressList? = null
    var findDefaultAddress : AddressList? = null
    var cartItemsCustomAdapter : ServicesCartItemCustomAdapter? = null
    private lateinit var progressView: CustomProgressView
    private var cartList : Int = 0
    private var deletedPromotion : String = ""
    private var deletedWolooPromotion : String = ""

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
        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )




        storeViewModel!!.getAddressesList()
        profileViewModel!!.getUserProfile()

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


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
            if(binding.applyPromotion.text.toString().equals("Apply" , ignoreCase = true)) {
                if (!binding.userPromocode.text.isNullOrEmpty()) {


                    var request = AddPromotionsRequest()
                    request.promo_codes = ArrayList<String>(
                        listOf(
                            binding.userPromocode.text.toString()
                        )
                    )

                    servicesViewModel!!.getAddPromotionToCart(
                        SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request
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
                        servicesViewModel!!.getDeletePromotionFromCart(
                            SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request
                        )
                        deletedPromotion = binding.userPromocode.text.toString()
                    }
                }catch (e : Exception)
                {

                }
            }
        }


        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())

        servicesViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    cartList = it.cart!!.items!!.size
                    cartItemsCustomAdapter =
                        ServicesCartItemCustomAdapter(this, it.cart!!.items!!, servicesViewModel!!, false)
                    binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                    binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
                    binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                    binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
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

        servicesViewModel!!.observeAddPromotionToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")


                    binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                    binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
                    totalAmountFinal = it.cart!!.total.toString()
                    discountedAmountFinal = it.cart!!.discount_total.toString()
                    productsAmountFinal = it.cart!!.original_item_total.toString()


                    val promoList = it.cart?.promotions ?: emptyList()

                    val userCode = binding.userPromocode.text.toString().trim()

// --- Check if WOLOO_COINS applied ---
                    val isWolooApplied = promoList.any { promo -> promo.code.equals("WOLOO_COINS", ignoreCase = true) }
                    binding.applyWolooPromotion.text = if (isWolooApplied) "Remove" else "Apply"
                    SharedPrefSettings.getPreferences.storeCoinsUsed(isWolooApplied)
                    coinsUsed = isWolooApplied

// --- Check if USER promo code applied ---
                    val isUserPromoApplied = promoList.any { promo -> promo.code.equals(userCode, ignoreCase = true) }
                    binding.applyPromotion.text = if (isUserPromoApplied) "Remove" else "Apply"
                    promotionApplied = if (isUserPromoApplied) userCode else ""


                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        servicesViewModel!!.observeDeletePromotionFromCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")

                    binding.productPrice.text = "₹ ${it.cart!!.original_item_total.toString()}/-"
                    binding.discountPrice.text = "₹ ${it.cart!!.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
                    binding.grandTotal.text = "₹ ${it.cart!!.total.toString()}/-"
                    totalAmountFinal = it.cart!!.total.toString()
                    discountedAmountFinal = it.cart!!.discount_total.toString()
                    productsAmountFinal = it.cart!!.original_item_total.toString()

                    val promoList = it.cart?.promotions ?: emptyList()

                    val userCode = deletedPromotion
                    val isUserPromoStillApplied = promoList.any { promo -> promo.code.equals(userCode, ignoreCase = true) }
                    binding.applyPromotion.text = if (isUserPromoStillApplied) "Remove" else "Apply"
                    deletedPromotion = ""

                    val isWolooStillApplied = promoList.any { promo -> promo.code.equals("WOLOO_COINS", ignoreCase = true) }
                    binding.applyWolooPromotion.text = if (isWolooStillApplied) "Remove" else "Apply"
                    deletedWolooPromotion = ""

                    SharedPrefSettings.getPreferences.storeCoinsUsed(isWolooStillApplied)
                    coinsUsed = isWolooStillApplied
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        servicesViewModel!!.observeDeleteFromCart().observe(this, Observer { response ->
            response?.let {
                try{
                    //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    // binding.displayCartCount.setText(it.cart!!.items.size.toString())\\

                    if(it.parent!!.items!!.size == 0)
                    {
                        cartList = 0
                    }
                    cartItemsCustomAdapter =
                        ServicesCartItemCustomAdapter(this, it.parent.items!!, servicesViewModel!!, false)
                    binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                    binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
                    binding.productPrice.text =
                        "₹ ${it.parent.original_item_total.toString()}/-"
                    binding.discountPrice.text = "₹ ${it.parent.discount_total.toString()}/-"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "₹ ${shippingAmount}/-"
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

        servicesViewModel!!.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    cartItemsCustomAdapter =   ServicesCartItemCustomAdapter(this, it.cart!!.items!!, servicesViewModel!! , false)
                    binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                    binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
                    binding.productPrice.text = "Rs. ${it.cart!!.original_item_total.toString()}"
                    binding.discountPrice.text = "Rs. ${it.cart!!.discount_total.toString()}"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "Rs. ${shippingAmount}"
                    binding.grandTotal.text = "Rs. ${it.cart!!.total.toString()}"
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

        servicesViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(this)
                    binding.deliveryDetailsRecycler.adapter =
                        ServicesCartItemCustomAdapter(this, it.cart!!.items!!, servicesViewModel!! , false)
                    binding.productPrice.text = "Rs. ${it.cart!!.original_item_total.toString()}"
                    binding.discountPrice.text = "Rs. ${it.cart!!.discount_total.toString()}"
                    if(shippingAmount == null)
                        shippingAmount = "0"
                    binding.totalAmount.text = "Rs. ${shippingAmount}"
                    binding.grandTotal.text = "Rs. ${it.cart!!.total.toString()}"
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
                    servicesViewModel!!.getAddPromotionToCart(
                        SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request
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
                    servicesViewModel!!.getDeletePromotionFromCart(
                        SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request
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
                        val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
                        bottomSheetFragment.show(
                            supportFragmentManager,
                            bottomSheetFragment.tag
                        )
                    } else {
                        if (findAddress?.postal_code != null) {
                            servicesViewModel!!.getDeliveryCodesList(findAddress!!.postal_code.toString())
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
                val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        }

        servicesViewModel!!.observeDeliveryCodesList().observe(this ,Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.deliveryCodes!!}")
                    if(!it.deliveryCodes!!.isNullOrEmpty())
                    {
                        if(!it.deliveryCodes[0].postalCode.center.isNullOrEmpty())
                        {
                            val shippingAddress = Address(
                                /* firstName = "John",
                                 lastName = "Doe",
                                 address1 = "123 Main St",
                                 city = "Mumbai",
                                 countryCode = "in",
                                 postalCode = "415002",
                                 phone = "1234567890"*/
                                firstName = findAddress!!.first_name.toString(),
                                lastName = findAddress!!.last_name.toString(),
                                address1 = findAddress!!.address_1.toString(),
                                city = findAddress!!.city.toString(),
                                countryCode = "in",
                                province = findAddress!!.province.toString(),
                                postalCode = findAddress!!.postal_code.toString(),
                                phone = findAddress!!.phone.toString()


                            )

                            val billingAddress = Address(
                                firstName = findAddress!!.first_name.toString(),
                                lastName = findAddress!!.last_name.toString(),
                                address1 = findAddress!!.address_1.toString(),
                                city = findAddress!!.city.toString(),
                                countryCode = "in",
                                province = findAddress!!.province.toString(),
                                postalCode = findAddress!!.postal_code.toString(),
                                phone = findAddress!!.phone.toString()
                            )
                            var request = ShippingBillingAddressRequest()
                            request.shippingAddress = shippingAddress
                            request.billingAddress = billingAddress
                            servicesViewModel!!.getAddShippingBillingToCart(
                                SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                                request
                            )
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

        servicesViewModel!!.observeAddShippingBillingToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    shippingAddress = it.cart!!.shipping_address!!.first_name.toString() + "\n" + it.cart!!.shipping_address!!.address_1.toString()

                    servicesViewModel!!.getShippingOptions( SharedPrefSettings.getPreferences.fetchServiceCartId().toString())}catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        servicesViewModel!!.observeShippingOptions().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.shipping_options!!}")
                    progressView.hide()
                    shippingAmount = it.shipping_options[0].amount.toString()
                    binding.totalAmount.text = "Rs. ${shippingAmount}"
                    val options = ArrayList(
                        it.shipping_options.map { shipping ->
                            ShippingMethodsRequestParam(
                                optionId = shipping.id
                            )
                        }
                    )
                    val bottomSheetFragment = ServicesCartBottomSheetFragment.newInstance(options ,promotionApplied , shippingAmount , coinsUsed , pendingCoins , shippingAddress , totalAmountFinal , discountedAmountFinal , productsAmountFinal)
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }


            }
        })

        servicesViewModel!!.observeCalculateShippingOptions().observe(this, Observer { response ->
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
                    val bottomSheetFragment = ServicesCartBottomSheetFragment.newInstance(options ,promotionApplied , shippingAmount , coinsUsed , pendingCoins , shippingAddress , totalAmountFinal , discountedAmountFinal , productsAmountFinal)
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
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


}