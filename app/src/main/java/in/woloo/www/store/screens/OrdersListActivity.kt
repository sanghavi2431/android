package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.woloo.www.databinding.ActivityOrdersListBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.OrderListCustomAdapter
import `in`.woloo.www.store.adapter.OrderSetAdapter
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.orders_response.ItemsListData
import `in`.woloo.www.store.orders_response.OrdersListData
import `in`.woloo.www.utils.Logger

class OrdersListActivity : AppCompatActivity() {

    lateinit var binding : ActivityOrdersListBinding
    private var storeViewModel: StoreViewModel? = null
    private var orderListCustomAdapter: OrderListCustomAdapter? = null

            @SuppressLint("NotifyDataSetChanged")
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                binding = ActivityOrdersListBinding.inflate(layoutInflater)
                setContentView(binding.root)
                storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
                    StoreViewModel::class.java
                )



                val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
                if (isShowBackButton) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }

                storeViewModel!!.getOrders()

                binding.ivBack.setOnClickListener {
                    onSupportNavigateUp()
                }

                binding.swipeRefreshLayout.setOnRefreshListener {
                    binding.swipeRefreshLayout.isRefreshing = true
                    storeViewModel!!.getOrders()
                }


                storeViewModel!!.observeOrderlist().observe(this) { response ->
                    response?.let {

                        try {
                            Logger.i("Aarati Store", "setLiveData ${it.orderSets!!.toString() + it.orderSets!![0].orders!!.size}")

                            val orderSets = it.orderSets ?: emptyList()

                            if (orderSets.isNotEmpty()) {
                                val adapter = OrderSetAdapter(this, ArrayList(orderSets) ,  supportFragmentManager)
                                binding.orderDetailsRecycler.layoutManager =
                                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                binding.orderDetailsRecycler.adapter = adapter
                            }

                        }catch (e : Exception)
                        {
                            Logger.i("Aarati Store", "setLiveData ${e.message}")
                        }
                    finally {
                        // ✅ Stop refreshing here after the UI is updated or fails
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    }
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}