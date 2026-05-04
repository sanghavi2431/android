package `in`.woloo.www.store.screens

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivitySeeMoreItemsBinding
import `in`.woloo.www.databinding.FragmentStoreProductListBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.TopBrandsCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.collections_response.CollectionsListData
import `in`.woloo.www.utils.Logger

class SeeMoreItemsActivity : AppCompatActivity() {

    lateinit var binding : ActivitySeeMoreItemsBinding
    private var cartList: CartParams? = null
    private var storeViewModel: StoreViewModel? = null

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val refresh = result.data?.getBooleanExtra("refresh", false) ?: false
            if (refresh) {
                refreshFragment()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeMoreItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
        if (isShowBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        storeViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchCartId().toString())
        storeViewModel!!.getAddressesList()
        storeViewModel!!.getCollectionsList("id,title,metadata")

        binding.goToCartView.setOnClickListener {
            if(cartList!!.items!!.size == 0)
            {
                showCartEmptyDialog()
            }
            else {
                val intent = Intent(this, ShoppingCartActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                activityResultLauncher.launch(intent)
            }
        }

        binding.showAllAddress.setOnClickListener {
            val bottomSheetFragment = AddressBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.ivBack.setOnClickListener {
            onSupportNavigateUp()
        }

        storeViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {

                Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                cartList = it.cart!!

                binding.displayCartCount.setText(it.cart!!.items!!.size.toString())
                //  compareListFinal()

            }
        })

        storeViewModel!!.observeAddressesList().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}")
                /*for (i in it.addresses!!.indices)
                    Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                if(!it.data!!.addresses!!.isNullOrEmpty())
                {
                    var findAddress = it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }
                    if(findAddress != null)
                    {
                        binding.homeText.text = findAddress.address_name
                        binding.addressText.text = findAddress.address_1
                    }
                    else{
                        binding.homeText.text = "Select address"
                        binding.addressText.text = "Address not selected"

                    }
                }
                else{
                    binding.homeText.text = "Add address"
                    binding.addressText.text = "Address not added"
                }

            }

        })

        storeViewModel!!.observeCollectionsList().observe(this, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.collections!! + it.collections!!.size}")
                for (i in it.collections!!.indices)
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.collections!![i].id + it.collections!![i].title}"
                    )

                binding.collectionsRecycler.layoutManager = GridLayoutManager(this, 3)
                binding.collectionsRecycler.adapter =
                    TopBrandsCustomAdapter(this, it.collections!!)
            }
        })
    }

    fun refreshFragment() {

        val intent = intent
        finish()
        startActivity(intent)

    }

    private fun showCartEmptyDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.tv_msg)
            val llclose = dialog.findViewById<TextView>(R.id.btnCloseDialog)
            llStartFreeTrial.text = "Your cart is empty.Please add items to Cart"
            llclose.setOnClickListener{dialog.dismiss()}
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}