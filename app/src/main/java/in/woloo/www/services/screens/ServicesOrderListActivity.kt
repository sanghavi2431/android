package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.woloo.www.databinding.ActivityOrdersListBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.ServicesOrderSetAdapter
import `in`.woloo.www.utils.Logger

class ServicesOrderListActivity : AppCompatActivity() {

    lateinit var binding : ActivityOrdersListBinding
    private var servicesViewModel: ServiceViewModel? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )



        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        servicesViewModel!!.getOrders()

        binding.ivBack.setOnClickListener {
            onSupportNavigateUp()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            servicesViewModel!!.getOrders()
        }


        servicesViewModel!!.observeOrderlist().observe(this) { response ->
            response?.let {

                try {
                    Logger.i("Aarati service", "setLiveData ${it.orderSets!!.toString() + it.orderSets!![0].orders!!.size}")

                    val orderSets = it.orderSets ?: emptyList()

                    if (orderSets.isNotEmpty()) {
                        val adapter = ServicesOrderSetAdapter(this, ArrayList(orderSets) ,  supportFragmentManager)
                        binding.orderDetailsRecycler.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        binding.orderDetailsRecycler.adapter = adapter
                    }

                }catch (e : Exception)
                {
                    Logger.i("Aarati service", "setLiveData ${e.message}")
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