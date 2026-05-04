package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreCartPopupBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.store.ApiEventBus
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CartItemsCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.cart_request_response.PaymentCollectionRequest
import `in`.woloo.www.store.cart_request_response.PaymentSessionRequest
import `in`.woloo.www.store.cart_request_response.RawAmount
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequestParam
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import org.json.JSONObject

class CartBottomSheetFragment : BottomSheetDialogFragment()  {

    private lateinit var binding: StoreCartPopupBinding

    private var storeViewModel: StoreViewModel? = null
    lateinit var providerId : String
    private lateinit var orderId : String

    private lateinit var amount : String
    private var profileViewModel: ProfileViewModel? = null
    private var useCoins : Boolean = false

    private var shippingOptionId: ArrayList<ShippingMethodsRequestParam>? = null
    private var promotionApplied: String? = null
    private var shippingAmount: String? = null
    private var coinsUsed: Boolean? = null
    private var pendingCoins: String? = null
    private var shippingAddress: String? = null
    private  var totalAmountFinal : String? = null
    private  var discountedAmountFinal : String? = null
    private  var productsAmountFinal : String? = null
    private lateinit var progressView: CustomProgressView

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StoreCartPopupBinding.inflate(inflater, container, false)

        storeViewModel = ViewModelProvider(requireActivity()).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )

        profileViewModel!!.getUserProfile()
        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
        progressView = CustomProgressView(requireActivity())
        shippingOptionId = arguments?.getParcelableArrayList(ARG_SHIPPING_OPTION_ID)
         promotionApplied = arguments?.getString(ARG_PROMOTION_APPLIED)
         shippingAmount = arguments?.getString(ARG_SHIPPING_AMOUNT)
         coinsUsed = arguments?.getBoolean(ARG_COINS_USED)
         pendingCoins = arguments?.getString(ARG_PENDING_COINS)
        shippingAddress = arguments?.getString(ARG_SHIPPING_ADDRESS)
        totalAmountFinal = arguments?.getString(ARG_TOTAL_AMOUNT)
        discountedAmountFinal = arguments?.getString(ARG_DISCOUNT_AMOUNT)
        productsAmountFinal = arguments?.getString(ARG_PRODUCT_AMOUNT)
        if(promotionApplied.isNullOrEmpty())
        {
            binding.promotionLayout.visibility = View.GONE
        }
        else {
            binding.promotionAppliedName.text = promotionApplied
        }
        binding.shippingAmount.text = "₹ $shippingAmount/-"
      //  binding.shippingAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
        binding.productsTotal.text = "₹ $productsAmountFinal/-"
        binding.discountTotal.text = "₹ $discountedAmountFinal/-"
        binding.finalTotal.text = "₹ $totalAmountFinal/-"
        if(coinsUsed == true) {
            binding.coinsUsed.text = "Used :10"
            binding.coinsPending.text = "Coins Available in your account :{$pendingCoins}"
        }
        else{
            binding.coinsUsed.visibility = View.GONE
            binding.coinsLayout.visibility = View.GONE
        }

        binding.addressSelected.text = shippingAddress

        storeViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}/-")
                binding.shippingAmount.text = "₹ ${it.cart!!.shipping_total}/-"
                binding.shippingAmount.text = "₹ ${it.cart!!.original_shipping_total.toString()}/-"
                binding.productsTotal.text = "₹ ${it.cart!!.original_item_total}/-"
                binding.discountTotal.text = "₹ ${it.cart!!.discount_total}/-"
                binding.finalTotal.text = "₹ ${it.cart!!.total}/-"
            }
        })




        storeViewModel!!.observePaymentProvidersList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.paymentProviders!!}")
                providerId = it.paymentProviders!![0].id.toString()
                val request = PaymentCollectionRequest()
                request.cartId = SharedPrefSettings.getPreferences.fetchCartId().toString()
                storeViewModel!!.getPaymentCollection(request)
            }
        })


        storeViewModel!!.observePaymentCollection().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.paymentCollection!!}")
                val request = PaymentSessionRequest()
                request.providerId = providerId
                storeViewModel!!.getPaymentSession(it.paymentCollection!!.id.toString(), request)
            }

        })


        profileViewModel!!.observeUserProfile().observe(
            viewLifecycleOwner
        ) { userProfileBaseResponse ->
            if (userProfileBaseResponse != null) {
                if (userProfileBaseResponse.data != null) {
                    try {
pendingCoins = userProfileBaseResponse.data!!.totalCoins!!.totalCoins.toString()
                    }catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } else {
                //                    displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }
        }



        storeViewModel!!.observePaymentSession().observe(viewLifecycleOwner, Observer { response ->
            response?.let {

                val responseCode = it.responseCode
                Log.d("Response Code", responseCode.toString())

                if(responseCode != 200)
                {
                    Toast.makeText(requireContext(), "Something went wrong , try again later", Toast.LENGTH_SHORT).show()
                    dismiss()
                    requireActivity().recreate()
                }

                    if (it.data!!.paymentCollection != null) {
                        Logger.i("Aarati Store", "setLiveData ${it.data!!.paymentCollection!!}")


                        orderId =
                            it.data!!.paymentCollection!!.paymentSessions!!.get(0).data!!.id.toString()
                        amount =
                            it.data!!.paymentCollection!!.paymentSessions!!.get(0).data!!.amount.toString()

                        val intent = Intent(requireContext(), ShopOrderDetailsActivity::class.java)
                        intent.putExtra("ORDERIDBEFOREPAYMENT", orderId)
                        intent.putExtra("ORDERAMOUNT", amount)
                        intent.putExtra(
                            "ORDERSESSIONID",
                            it.data!!.paymentCollection!!.paymentSessions!!.get(0).id.toString()
                        )
                        startActivity(intent)
                    }

            }

        })

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.checkoutButton.setOnClickListener{
            progressView.show()

            Handler(Looper.getMainLooper()).postDelayed({
                progressView.hide()
            }, 2000)


            storeViewModel!!.getPaymentProvidersList(
                SharedPrefSettings.getPreferences.fetchRegionId().toString()
            )





        }

        binding.keepShoppingButton.setOnClickListener{

            val intent = Intent(requireActivity(), WolooDashboard::class.java)
            intent.putExtra("StoreListingFragment", "StoreListingFragment")
            startActivity(intent)
            dismiss()
            requireActivity().finish()

        }


    }

    private fun setupRecyclerView() {
        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())

        storeViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {

                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = CartItemsCustomAdapter(requireActivity() , it.cart!!.items!! , storeViewModel!! , true)
                    totalAmountFinal = it.cart!!.total
                binding.finalTotal.text = "₹ $totalAmountFinal/-"
                binding.shippingAmount.text = "₹ ${it.cart!!.shipping_total}/-"

                val promotionCodes = it.cart?.promotions.orEmpty()
                    .mapNotNull { it.code }

                val isWolooCoinsApplied = promotionCodes.contains("WOLOO_COINS")

// Remove "WOLOO_COINS" from the final displayed list
                val promotionApplied = promotionCodes
                    .filterNot { it == "WOLOO_COINS" }
                    .toSet()
                    .joinToString(",")

// Show/hide layout based on the result
                if (promotionApplied.isEmpty()) {
                    binding.promotionLayout.visibility = View.GONE
                } else {
                    binding.promotionLayout.visibility = View.VISIBLE
                    binding.promotionAppliedName.text = promotionApplied
                }

                if(isWolooCoinsApplied == true)
                {
                    binding.coinsUsed.visibility = View.VISIBLE
                    binding.coinsLayout.visibility = View.VISIBLE
                    binding.coinsUsed.text = "Used :10"
                    binding.coinsPending.text = "Coins Available in your account :{$pendingCoins}"
                }
                else
                {
                    binding.coinsUsed.visibility = View.GONE
                    binding.coinsLayout.visibility = View.GONE
                }


            }
        })

        storeViewModel!!.observeDeleteFromCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                // binding.displayCartCount.setText(it.cart!!.items.size.toString())
            }
        })

        storeViewModel!!.observeAddToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = CartItemsCustomAdapter(requireActivity() , it.cart!!.items!! , storeViewModel!! , true)

                /* cartList = it.cart
                 adapter = ProductsCollectionsCustomeAdapter(requireActivity() , productList , cartList!! , storeViewModel!!)
                 adapter.notifyDataSetChanged()*/

            }
        })

        storeViewModel!!.observeUpdateToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = CartItemsCustomAdapter(requireActivity() , it.cart!!.items!! , storeViewModel!! , true)

            }
        })





    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
    }


    override fun onDestroyView() {
        super.onDestroyView()
        progressView.hide()
        //binding = null
    }




    companion object {
        private const val ARG_SHIPPING_OPTION_ID = "shipping_option_id"
        private const val ARG_PROMOTION_APPLIED = "promotion_applied"
        private const val ARG_SHIPPING_AMOUNT = "shipping_amount"
        private const val ARG_COINS_USED = "coins_used"
        private const val ARG_PENDING_COINS = "pending_coins"
        private const val ARG_SHIPPING_ADDRESS = "shipping_address"
        private const val ARG_TOTAL_AMOUNT = "total_amount"
        private const val ARG_DISCOUNT_AMOUNT = "discount_amount"
        private const val ARG_PRODUCT_AMOUNT = "product_amount"

        fun newInstance(
            shipingOptionId: ArrayList<ShippingMethodsRequestParam>?,
            promotionApplied: String,
            shippingAmount: String,
            coinsUsed: Boolean,
            pendingCoins: String,
            shippingAddress : String,
            totalAmount : String,
            discountAmount : String,
            productAmount: String
        ): CartBottomSheetFragment {
            val fragment = CartBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_SHIPPING_OPTION_ID, shipingOptionId)
            bundle.putString(ARG_PROMOTION_APPLIED, promotionApplied)
            bundle.putString(ARG_SHIPPING_AMOUNT, shippingAmount)
            bundle.putBoolean(ARG_COINS_USED, coinsUsed)
            bundle.putString(ARG_PENDING_COINS, pendingCoins)
            bundle.putString(ARG_SHIPPING_ADDRESS, shippingAddress)
            bundle.putString(ARG_TOTAL_AMOUNT, totalAmount)
            bundle.putString(ARG_DISCOUNT_AMOUNT, discountAmount)
            bundle.putString(ARG_PRODUCT_AMOUNT, productAmount)

            fragment.arguments = bundle
            return fragment
        }
    }



}

