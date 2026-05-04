package `in`.woloo.www.services.screens

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.databinding.StoreCartPopupBinding
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CartItemsCustomAdapter
import `in`.woloo.www.store.cart_request_response.PaymentCollectionRequest
import `in`.woloo.www.store.cart_request_response.PaymentSessionRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequestParam
import `in`.woloo.www.store.screens.ShopOrderDetailsActivity
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import androidx.lifecycle.Observer
import `in`.woloo.www.services.adapter.ServicesCartItemCustomAdapter

class ServicesCartBottomSheetFragment : BottomSheetDialogFragment()  {

    private lateinit var binding: StoreCartPopupBinding

    private var storeViewModel: StoreViewModel? = null
    private var servicesViewModel: ServiceViewModel? = null
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
        servicesViewModel = ViewModelProvider(requireActivity()).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )
        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
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
        binding.productsTotal.text = "₹ $productsAmountFinal/-"
        binding.discountTotal.text = "₹ $discountedAmountFinal/-"
        binding.finalTotal.text = "₹ $totalAmountFinal/-"
        if(coinsUsed == true) {
            binding.coinsUsed.text = "Coins Used :10"
            binding.coinsPending.text = "Coins Available in your account :{$pendingCoins}"
        }
        else{
            binding.coinsUsed.visibility = View.GONE
            binding.coinsLayout.visibility = View.GONE
        }

        binding.addressSelected.text = shippingAddress

        servicesViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.shippingAmount.text = "₹ ${it.cart!!.shipping_total}/-"
                binding.productsTotal.text = "₹ ${it.cart!!.original_item_total}/-"
                binding.discountTotal.text = "₹ ${it.cart!!.discount_total}/-"
                binding.finalTotal.text = "₹ ${it.cart!!.total}/-"
            }
        })


        servicesViewModel!!.observeShippingMethodsToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                servicesViewModel!!.getPaymentProvidersList(
                    SharedPrefSettings.getPreferences.fetchRegionId().toString()
                )

            }
        })

        servicesViewModel!!.observePaymentProvidersList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.paymentProviders!!}")
                providerId = it.paymentProviders!![0].id.toString()
                val request = PaymentCollectionRequest()
                request.cartId = SharedPrefSettings.getPreferences.fetchServiceCartId().toString()
                servicesViewModel!!.getPaymentCollection(request)
            }
        })


        servicesViewModel!!.observePaymentCollection().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.paymentCollection!!}")
                val request = PaymentSessionRequest()
                request.providerId = providerId
                servicesViewModel!!.getPaymentSession(it.paymentCollection!!.id.toString(), request)
            }

        })

        servicesViewModel!!.observePaymentSession().observe(viewLifecycleOwner, Observer { response ->
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

                    val intent = Intent(requireContext(), ServiceOrderDetailsActivity::class.java)
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

            var request = ShippingMethodsRequest()
            request.options = shippingOptionId
            servicesViewModel!!.getShippingMethodsToCart(
                SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request
            )






        }

        binding.keepShoppingButton.setOnClickListener{

            val intent = Intent(requireActivity(), WolooDashboard::class.java)
            intent.putExtra("ServicesListFragment", "ServicesListFragment")
            startActivity(intent)
            dismiss()
            requireActivity().finish()

        }


    }

    private fun setupRecyclerView() {
        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())

        servicesViewModel!!.observeCartList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {

                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = ServicesCartItemCustomAdapter(requireActivity() , it.cart!!.items!! , servicesViewModel!! , true)
                totalAmountFinal = it.cart!!.total
                binding.finalTotal.text = "₹ $totalAmountFinal"
            }
        })

        servicesViewModel!!.observeDeleteFromCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                //  Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                // binding.displayCartCount.setText(it.cart!!.items.size.toString())
            }
        })

        servicesViewModel!!.observeAddToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = ServicesCartItemCustomAdapter(requireActivity() , it.cart!!.items!! , servicesViewModel!! , true)

                /* cartList = it.cart
                 adapter = ProductsCollectionsCustomeAdapter(requireActivity() , productList , cartList!! , servicesViewModel!!)
                 adapter.notifyDataSetChanged()*/

            }
        })

        servicesViewModel!!.observeUpdateToCart().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
                binding.deliveryDetailsRecycler.adapter = ServicesCartItemCustomAdapter(requireActivity() , it.cart!!.items!! , servicesViewModel!! , true)

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
        ): ServicesCartBottomSheetFragment {
            val fragment = ServicesCartBottomSheetFragment()
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