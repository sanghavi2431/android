package `in`.woloo.www.store.screens

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentSearchFromStoreBinding
import `in`.woloo.www.databinding.RecentlySearchedListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class SearchFromStoreActivity  : AppCompatActivity() {

    lateinit var binding : FragmentSearchFromStoreBinding
    private var storeViewModel: StoreViewModel? = null
    private val searchQuery = MutableStateFlow("")
    private var itemList = ArrayList<String>()
    private val maxItems = 5
    private var removalJob: Job? = null
    private var cartList: CartParams? = null
    private  lateinit var productJson : String
    private var productsCollectionsCustomeAdapter: ProductsCollectionsCustomeAdapter? = null
    private  var latestProductsNamesList : ArrayList<String> = ArrayList()


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchFromStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
            if(!SharedPrefSettings.getPreferences.fetchRecentSearchedArray().isNullOrEmpty())
            {
        itemList = SharedPrefSettings.getPreferences.fetchRecentSearchedArray()!!
        loadListFromPreferences()
            }


        val firstItemTime = getSavedTimestamp()
        if (firstItemTime > 0) {
            val currentTime = System.currentTimeMillis()
            val timeElapsed = currentTime - firstItemTime

            if (timeElapsed >= 24 * 60 * 60 * 1000) {
                removeAllItems() // Remove all if 24 hours have passed
            } else {
                scheduleRemovalAfter24Hours(firstItemTime) // Schedule remaining time
            }
        }

        binding.clearList.setOnClickListener{
            itemList.clear()
            SharedPrefSettings.getPreferences.clearArrayList()
            updateChipGroup()
        }

        if(itemList.isNotEmpty()) {
            storeViewModel?.getProductWithPriceListWithQuery("*variants.calculated_price,+variants.inventory_quantity,*categories", SharedPrefSettings.getPreferences.fetchRegionId().toString(),itemList[0])
        }


        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())


        if(itemList.size > 0)
        {
            var searchProduct = ""
for (i in itemList.indices)
{
    Logger.i("Aarati Store", "setLiveData ${itemList[i] + itemList[i]}")
}
             }


        // Handle back button visibility (if needed)
        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        latestProductsNamesList = intent.getStringArrayListExtra("LATESTPRODUCTS")!!

        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, latestProductsNamesList)

        binding.searchProductAutoComplete.setAdapter(adapter)

        binding.searchProductAutoComplete.threshold= 1

        binding.searchProductAutoComplete.setOnClickListener{
            binding.searchProductAutoComplete.showDropDown()
        }

        if (isShowBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.ivBack.setOnClickListener {
            onSupportNavigateUp()
        }

        binding.viewAllProductsText.setOnClickListener{
           // val position = (itemList.size) - 1

                val intent = Intent(this, StoreProductListActivity::class.java)
                intent.putExtra("FROMSCREEN", AppConstants.FROM_SEARCH_PRODUCT)
                intent.putExtra("QUERY", itemList.get(0))
                intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                startActivity(intent)
                finish()

        }

        binding.searchProductAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Before text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when user types in AutoCompleteTextView

            }

            override fun afterTextChanged(s: Editable?) {
                // Called after text has been changed
               // storeViewModel!!.getSearchedProductList(binding.searchProductAutoComplete.text.toString())
                Log.d("AutoComplete", "Text changed: $s")
                searchQuery.value = s.toString()
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            searchQuery
                .debounce(1000) // Wait 500ms after user stops typing
                .filter { it.length > 5 } // Ensure at least 3 characters are entered
                .collect { query ->
                    storeViewModel?.getProductWithPriceListWithQuery("*variants.calculated_price,+variants.inventory_quantity,*categories" , SharedPrefSettings.getPreferences.fetchRegionId().toString(),query)

                  //  storeViewModel?.getSearchedProductList(query)
                    addItem(query)
                }
        }

      /*  storeViewModel!!.observeSearchedProductList().observe(this , Observer { response ->
            response?.let {
                try {
                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )
                        productJson = Gson().toJson(it.products)
                        productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionRecycler.layoutManager =
                            GridLayoutManager(this, 3 , GridLayoutManager.HORIZONTAL , false)
                        binding.collectionRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }

                        binding.viewAllProductsText.visibility = ViewGroup.VISIBLE
                    }
                    else{
                        Toast.makeText(this , "No Product Found" , Toast.LENGTH_SHORT).show()
                    }
                }catch (e :Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })*/

        storeViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {

                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                cartList = it.cart!!
                cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
            }
        })



        storeViewModel!!.observeProductWithPriceListWithQuery().observe(this , Observer { response ->
            response?.let {
                if (!it.products.isNullOrEmpty()) {
                    Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")

                    if (!it.products.isNullOrEmpty()) {
                        Logger.i(
                            "Aarati Store",
                            "setLiveData ${it.products!! + it.products!!.size}"
                        )

                        productJson = Gson().toJson(it.products)
                        productsCollectionsCustomeAdapter = ProductsCollectionsCustomeAdapter(
                            this,
                            it.products!!,
                            storeViewModel!!
                        )
                        binding.collectionRecycler.layoutManager =
                            LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL , false)
                        binding.collectionRecycler.adapter = productsCollectionsCustomeAdapter
                        cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }
                        if(it.products!= null)
                        binding.viewAllProductsText.visibility = ViewGroup.VISIBLE
                    }
                    else{
                        Toast.makeText(this , "No Product Found" , Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })


        storeViewModel!!.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                /* cartList = it.cart
                 adapter = ProductsCollectionsCustomeAdapter(requireActivity() , productList , cartList!! , storeViewModel!!)
                 adapter.notifyDataSetChanged()*/
                cartList = it.cart
                cartList?.let { productsCollectionsCustomeAdapter?.updateCart(it) }

            }
        })





    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun addItem(item: String) {
        // Maintain max 5 items
        if (itemList.isEmpty()) {
            // Store timestamp when the first item is added
            val currentTime = System.currentTimeMillis()
            saveTimestamp(currentTime)

            scheduleRemovalAfter24Hours(currentTime)
        }

        // Maintain a max of 5 items
        if (itemList.size >= 5) {
            itemList.removeAt(0) // Remove oldest item
        }

        itemList.add(item)
        SharedPrefSettings.getPreferences.clearArrayList()
        SharedPrefSettings.getPreferences.storeRecentSearchedArray(itemList)

        updateChipGroup()
    }

    private fun updateChipGroup() {
        binding.recentItemsRecycler.removeAllViews() // Clear previous views

        for (item in itemList) {
            // Inflate the custom layout with Data Binding (NO parent attached)
            val bindingChip = RecentlySearchedListItemBinding.inflate(
                LayoutInflater.from(binding.recentItemsRecycler.context), null, false
            )

            // Bind data to layout
            bindingChip.recentTextItem.text = item

            // Handle item removal
            bindingChip.ivClose.setOnClickListener {
                itemList.remove(item)
                updateChipGroup()
            }

            bindingChip.root.setOnClickListener{
                binding.searchProductAutoComplete.setText(item)
            }

            // Ensure the view is not attached anywhere else before adding
            if (bindingChip.root.parent != null) {
                (bindingChip.root.parent as ViewGroup).removeView(bindingChip.root)
            }

            // Add the bound view to the ChipGroup
            binding.recentItemsRecycler.addView(bindingChip.root)
        }
    }

    private fun saveTimestamp(time: Long) {
        val sharedPreferences = getSharedPreferences("RecentSearchPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("firstItemTimestamp", time).commit()
    }


    private fun scheduleRemovalAfter24Hours(firstItemTime: Long) {
        val currentTime = System.currentTimeMillis()
        val timeRemaining = (24 * 60 * 60 * 1000) - (currentTime - firstItemTime) // Time left until 24 hours

        // Cancel any existing job before scheduling a new one
        removalJob?.cancel()

        removalJob = CoroutineScope(Dispatchers.IO).launch {
            delay(timeRemaining) // Wait exactly until 24 hours has passed
            removeAllItems()
        }
    }

    private fun removeAllItems() {
        runOnUiThread {
            itemList.clear() // Clear all items
            saveTimestamp(0L) // Reset timestamp
            updateChipGroup()
        }
    }

    private fun getSavedTimestamp(): Long {
        val sharedPreferences = getSharedPreferences("RecentSearchPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("firstItemTimestamp", 0L)
    }

    private fun loadListFromPreferences() {

        updateChipGroup() // Refresh UI
    }



}