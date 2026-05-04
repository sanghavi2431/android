package `in`.woloo.www.store.screens

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.databinding.FragmentShopOrderDetailsBinding
import `in`.woloo.www.store.adapter.OrderDetailsCustomAdapter
import `in`.woloo.www.store.orders_response.OrdersListData
import `in`.woloo.www.store.product_response.ProductListData

class DisplayOrderDetails : AppCompatActivity() {

    lateinit var binding : FragmentShopOrderDetailsBinding
    private lateinit var order: OrdersListData
    private lateinit var productIdFromIntent : String
    private lateinit var productTitle : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShopOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderJson = intent.getStringExtra("ORDER_JSON")
        Log.i("Aarati Order is " , orderJson.toString())
        order = Gson().fromJson(orderJson, OrdersListData::class.java)
        try {


            productIdFromIntent = intent.getStringExtra("PRODUCT_ID").toString()
            productTitle = intent.getStringExtra("PRODUCT_TITLE").toString()

          /*  val matchedProductList = order?.items
                ?.filter { it.productId == productIdFromIntent }
                ?.toCollection(ArrayList())*/

            binding.orderDetailsRecycler.layoutManager = LinearLayoutManager(this)
            binding.orderDetailsRecycler.adapter =
                OrderDetailsCustomAdapter(this, order.items!!)
            productIdFromIntent =  order!!.items!![0].productId.toString()
            productTitle = order!!.items!![0].variantId.toString()
            binding.reviewButton.isEnabled = true

            binding.productPrice.text = "₹ ${order!!.itemTotal.toString()}/-"
            binding.discountPrice.text = "₹ ${order!!.discountTotal.toString()}/-"
            binding.totalAmount.text = "₹ ${order!!.shippingTotal.toString()}/-"
            binding.grandTotal.text = "₹ ${order!!.total.toString()}/-"

        }catch (e : Exception)
        {
            e.printStackTrace()
        }


        binding.helpButton.setOnClickListener{
            val bottomSheetFragment = HelpSupportBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.reviewButton.setOnClickListener{
            val bottomSheetFragment = ReviewBottomSheetFragment.newInstance(productIdFromIntent,productTitle , 3)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


        if(order.paymentStatus.equals("captured" , ignoreCase = true) && order.fulfillmentStatus.equals("not_fulfilled" , ignoreCase = true))
        {
            binding.imageOrderAccepted.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))
            binding.imageOrderPlaceLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
        }
        else if(order.paymentStatus.equals("captured" , ignoreCase = true) && order.fulfillmentStatus.equals("shipped" , ignoreCase = true))
        {
            binding.imageOrderAccepted.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))
            binding.imageOrderPlaceLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
            binding.imageOrderShipped.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))
            binding.imageOrderAcceptedLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
        }
        else if(order.paymentStatus.equals("captured" , ignoreCase = true) && order.fulfillmentStatus.equals("delivered" , ignoreCase = true))
        {
            binding.imageOrderAccepted.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))
            binding.imageOrderPlaceLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
            binding.imageOrderShipped.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))
            binding.imageOrderAcceptedLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
            binding.imageOrderShippedLine.setBackgroundColor(ResourcesCompat.getColor(resources , R.color.green , theme))
            binding.imageOrderDelivered.setImageDrawable(ResourcesCompat.getDrawable(resources , R.drawable.green_circle , theme))

        }

        binding.ivBack.setOnClickListener{
            SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
            finish()

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
