package `in`.woloo.www.store.screens

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentShopOrderDetailsBinding
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.OrderDetailsCustomAdapter
import `in`.woloo.www.store.adapter.OrderListCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.cart_request_response.DeletePaymentSessionRequest
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONObject


class ShopOrderDetailsActivity  : AppCompatActivity() , PaymentResultWithDataListener
{

    lateinit var binding : FragmentShopOrderDetailsBinding
    private lateinit var orderId : String
    private var storeViewModel: StoreViewModel? = null
    private var blogViewModel: BlogViewModel? = null
    private lateinit var orderIdAfterPayment : String
    private lateinit var orderIdOfRazorPay : String
    private lateinit var orderAmountPay : String
    private lateinit var orderPaymentSession : String
    private lateinit var productId : String
    private lateinit var productName : String
    private  var isPaymentDone :Boolean = false
    private var backCallback: OnBackInvokedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShopOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reviewButton.isEnabled = false

        if( intent!= null) {
            orderIdOfRazorPay = intent.getStringExtra("ORDERIDBEFOREPAYMENT").toString()
            orderAmountPay = intent.getStringExtra("ORDERAMOUNT").toString()
            orderPaymentSession = intent.getStringExtra("ORDERSESSIONID").toString()
        }
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(
            BlogViewModel::class.java
        )
        try {
            if(orderIdOfRazorPay.isNotEmpty()) {
                Checkout.preload(this)
                startPayment()
            }
        }
        catch (e : Exception)
        {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            backCallback = OnBackInvokedCallback {
                handleBack()
            }
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                backCallback!!
            )
        }

        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {

            val intent = Intent(this, WolooDashboard::class.java)
            intent.putExtra("StoreListingFragment", "StoreListingFragment")
            startActivity(intent)
            finish()
        }





        storeViewModel!!.observeOrderDetails().observe(this) { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.order!!.items}")
                binding.productPrice.text = "₹ ${it.order!!.itemTotal.toString()}/-"
                binding.discountPrice.text = "₹ ${it.order!!.discountTotal.toString()}/-"
                binding.totalAmount.text = "₹ ${it.order!!.shippingTotal.toString()}/-"
                binding.grandTotal.text = "₹ ${it.order!!.total.toString()}/-"
                binding.orderDetailsRecycler.layoutManager = LinearLayoutManager(this)
                binding.orderDetailsRecycler.adapter =
                    OrderDetailsCustomAdapter(this, it.order!!.items!!)
                    productId = it.order!!.items!![0].productId.toString()
                    productName = it.order!!.items!![0].variantId.toString()
                binding.reviewButton.isEnabled = true
            }

        }

        binding.ivBack.setOnClickListener{

            SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
                val intent = Intent(this, WolooDashboard::class.java)
                intent.putExtra("StoreListingFragment", "StoreListingFragment")
                startActivity(intent)
                finish()

        }

        binding.helpButton.setOnClickListener{
            val bottomSheetFragment = HelpSupportBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.reviewButton.setOnClickListener{
            if(productId != null) {
                val bottomSheetFragment =
                    ReviewBottomSheetFragment.newInstance(productId, productName, 3)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        }

        storeViewModel!!.observeCompleteCart().observe(this, Observer { response ->
            response?.let {
              //  Logger.i("Aarati Store", "setLiveData ${it.order!!.parent_order.status}")
                SharedPrefSettings.getPreferences.storeCartId("");
                val request = CartRequest()
                request.region_id = SharedPrefSettings.getPreferences.fetchRegionId()
                storeViewModel!!.getCartCreate(request)
                orderIdAfterPayment = it.orderSet!!.id.toString()
                if(SharedPrefSettings.getPreferences.fetchCoinsUsed())
                {
                    blogViewModel!!.ecomCoinUpdate("points", 10, it.orderSet.id.toString())
                    SharedPrefSettings.getPreferences.storeCoinsUsed(false)
                }

              //  storeViewModel!!.getOrderDetails(orderIdAfterPayment)

                binding.orderDetailsRecycler.layoutManager = LinearLayoutManager(this)
                binding.orderDetailsRecycler.adapter =
                    OrderListCustomAdapter(this, it.orderSet.orders!! , supportFragmentManager)
               /* productId =  it.orderSet.orders!!.items!![0].productId.toString()
                productName = it.orderSet.orders!!order!!.items!![0].variantId.toString()*/

                binding.reviewButton.isEnabled = true

            }
        })

        storeViewModel!!.observeCartCreate().observe(this , Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    SharedPrefSettings.getPreferences.storeCartId(it.cart!!.id)


                }catch(e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        blogViewModel!!.observeEcomCoinUpdate().observe(this,
            Observer<BaseResponse<EcomCoinUpdateResponse>> { response ->
                try {
                    if (!response.success) {
                      //  pendingCoins = response.data!!.remaining_coins.toString()
                    }
                }catch (e :Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            })


    }



    override fun onSupportNavigateUp(): Boolean {
       // finish()

        val intent = Intent(this, WolooDashboard::class.java)
        intent.putExtra("StoreListingFragment", "StoreListingFragment")
        startActivity(intent)
        finish()
        return true
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment Successful: $p0", Toast.LENGTH_LONG).show()
        Log.i("Aarati Store", "onPaymentSuccess: $p0 ${p1!!.paymentId}")
        isPaymentDone = true

        storeViewModel!!.getCompleteCart(SharedPrefSettings.getPreferences.fetchCartId().toString())
        // Handle success logic (e.g., update backend, show confirmation screen)
        showPaymentSuccessDialog()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed: $p1", Toast.LENGTH_LONG).show()
        Log.i("Aarati Store", "onPaymentSuccess: $p0 $p1  $p2")
        var request = DeletePaymentSessionRequest()
        request.ids?.set(0, orderPaymentSession)
        storeViewModel!!.getDeletePaymentSession(request)
        showPaymentFailedDialog()
    }

    fun startPayment() {


        val co = Checkout()
        co.setKeyID("rzp_test_ZIlhyKgx2C38vT")

        try {
            val options = JSONObject()
            options.put("order_id", orderIdOfRazorPay)
            options.put("name", "Woloo Store")
            options.put("currency", "INR")
            options.put("description", "Store ID")

            options.put("currency", "INR")
            options.put("prefill.email", SharedPrefSettings.getPreferences.fetchUserDetails()?.email.toString())
            options.put("prefill.contact", SharedPrefSettings.getPreferences.fetchUserDetails()?.mobile.toString())
            // Order ID from backend
            options.put("theme.color", "#3399cc")
            options.put("otp.auto_read", false)
            options.put("theme", JSONObject().apply { put("color", "#3399cc") })




            co.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_LONG).show()
        }


    }

    private fun showPaymentSuccessDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_shop_purchase_success)
            dialog.window!!.attributes = dialog.window!!.attributes

            val checkOrderStatus: TextView =
                dialog.findViewById<TextView>(R.id.check_order_details)
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

         //   val gifImageVieworder = dialog.findViewById<View>(R.id.success_order) as ImageView

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)
/*

            Glide.with(this)
                .load(R.drawable.successful_order) // your gif in res/drawable
                .into(gifImageVieworder)
*/

            checkOrderStatus.setOnClickListener(View.OnClickListener { v: View? -> dialog.dismiss() })
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun showPaymentFailedDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_payment_failed)
            dialog.window!!.attributes = dialog.window!!.attributes

            val checkOrderStatus: TextView =
                dialog.findViewById<TextView>(R.id.check_order_details)

            val keepShopping: TextView =
                dialog.findViewById<TextView>(R.id.keep_shopping)


            checkOrderStatus.setOnClickListener(View.OnClickListener { v: View? ->
                dialog.dismiss()
                val intent = Intent(this, ShoppingCartActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                startActivity(intent)
                finish()
            })

            keepShopping.setOnClickListener(View.OnClickListener { v: View? ->
                dialog.dismiss()
                val intent = Intent(this, WolooDashboard::class.java)
                intent.putExtra("StoreListingFragment", "StoreListingFragment")
                startActivity(intent)
                finish()
            })

            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }



    private fun handleBack() {
        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {
            val intent = Intent(this, WolooDashboard::class.java)
            intent.putExtra("StoreListingFragment", "StoreListingFragment")
            startActivity(intent)
            finish()
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
        handleBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            backCallback?.let {
                onBackInvokedDispatcher.unregisterOnBackInvokedCallback(it)
            }
        }
    }


}