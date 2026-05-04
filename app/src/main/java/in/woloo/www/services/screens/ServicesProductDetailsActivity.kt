package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityServicesProductDetailsBinding
import `in`.woloo.www.databinding.FragmentStoreProductDetailsBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.DescriptionAdapter
import `in`.woloo.www.services.adapter.ExpressServiceAdapter
import `in`.woloo.www.services.adapter.ServicesCartItemCustomAdapter
import `in`.woloo.www.services.adapter.ServicesProductImageRecylcerAdapter
import `in`.woloo.www.services.adapter.ServicesProductListAdapter
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.OptionsAdapter
import `in`.woloo.www.store.adapter.ServiceOptionsAdapter
import `in`.woloo.www.store.adapter.StoreReviewAdapter
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.screens.AddressBottomSheetFragment
import `in`.woloo.www.store.screens.ShoppingCartActivity
import `in`.woloo.www.store.screens.StoreReviewsPopUpFragment
import `in`.woloo.www.utils.Logger
import java.text.DecimalFormat

class ServicesProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServicesProductDetailsBinding
    private var servicesViewModel: ServiceViewModel? = null
    private var storeViewModel: StoreViewModel? = null
    private lateinit var product: ProductListData
    lateinit var categoryId :String
    var variantPosition : Int = 0
    lateinit var  productImageRecylcerAdapter :ServicesProductImageRecylcerAdapter
    private  var wishlistItemId : String = ""
    private var isWishListed : Boolean = false
    private val selectedOptionsMap = mutableMapOf<String, String>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)

        val productJson = intent.getStringExtra("PRODUCT_DETAILS")
        product = Gson().fromJson(productJson, ProductListData::class.java)

        storeViewModel!!.getAddressesList()

        isWishListed = product.variants?.any { it.isWishlisted == true } == true
        wishlistItemId = product.variants
            ?.firstOrNull { it.isWishlisted == true }
            ?.wishlist_item_id.toString()




        var address = SharedPrefSettings.getPreferences.fetchSelectedAddressId()
        if(address != null)
        {
            Logger.d("Address object" , address )
        }

        binding.changeAddress.setOnClickListener{
            val bottomSheetFragment = AddressBottomSheetFragment.newInstance("SERVICE")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

    initviews()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initviews()
    {


        try {

            binding.productDetailsText.text = product.title
            binding.productDetailsVenderName.text = product.collection!!.title
        //    binding.productDetailsDescription.text = product.description

            val paragraphs =  ArrayList(product.description!!.split(Regex("\\n\\s*\\n")))

           /* val htmlContent = StringBuilder()
            for (paragraph in paragraphs) {
                htmlContent.append("<p>$paragraph</p>")
            }*/

            val adapter = DescriptionAdapter(this ,paragraphs)
            binding.descriptionRecycler.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.descriptionRecycler.adapter = adapter


            servicesViewModel!!.getReviewsListForProduct(product.id!!)

        }
        catch (e : Exception)
        {

        }

        try {
            categoryId = product.categories!![0].id.toString()
        }catch (e:Exception)
        { Log.i("Aaarati Product Data 2" , "After Adapter")
            categoryId = "pcat_01JVYHKPD7TNXQMPVFSMBMFKH5"
        }

        servicesViewModel?.getCategoryWiseProductList(categoryId)

        binding.ivBack.setOnClickListener {

            val isShowBackButton = intent.getBooleanExtra("IS_SHOW_BACK_BUTTON", false)
            if (isShowBackButton) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            finish()

        }
        try {
            binding.productPrice.text = "₹" + product.variants!!.get(variantPosition).calculated_price!!.originalAmount.toString()+ "/-"

        }catch (e : Exception)
        {
            CommonUtils.printStackTrace(e)
        }

        productImageRecylcerAdapter = ServicesProductImageRecylcerAdapter(
            this,
            product.images!!,
            product.thumbnail!!,
            product.options!![0].values!!,
            0,
            product.variants!![variantPosition].id.toString(),
            product.variants!![variantPosition].isWishlisted!!,
            product.variants!![variantPosition].wishlist_item_id.toString(),
            servicesViewModel!!
        )
        binding.productImageRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.productImageRecycler.adapter = productImageRecylcerAdapter

        productImageRecylcerAdapter.notifyDataSetChanged()


        binding.addCartView.setOnClickListener{

            var variantId = getMatchingVariantId()

            if(variantId.isNullOrEmpty())
            {
                variantId = product.variants!![0].id.toString()
            }

            val bottomSheetFragment = DateTimeBottomSheetFragment.newInstance(variantId.toString() , product , "PRODUCT_DETAILS")
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        }

        binding.viewAllProductsReviews.setOnClickListener {

            val bottomSheetFragment = StoreReviewsPopUpFragment.newInstance(product.id.toString() , product.title.toString())
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        }


        val optionsAdapter = ServiceOptionsAdapter(this ,product.options ?: arrayListOf()  , product.variants!! , product , productImageRecylcerAdapter , selectedOptionsMap)
        { updatedMap ->

            val displayText = updatedMap.entries.joinToString(", ") { (key, value) ->
                "${key.toString()}: ${value.toString()}"
            }
            val sizeValue = updatedMap.entries
                .find { it.key.equals("size", ignoreCase = true) }
                ?.value

            if (sizeValue != null) {
                binding.sizeText.text = "${sizeValue.capitalize()}"
            }
            updatePriceUI(updatedMap)

        }

        binding.optionsParentList.layoutManager = LinearLayoutManager(this)
        binding.optionsParentList.adapter = optionsAdapter



        servicesViewModel!!.observeCategoryWiseProductList().observe(this, Observer { response ->
            response?.let {

                try {
                    Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                    if (it.products!!.isNotEmpty()) {
                        for (i in it.products!!.indices)
                            Logger.i(
                                "Aarati Store",
                                "setLiveData ${it.products!![i].id + it.products!![i].title}"
                            )
                        binding.productRecycler.layoutManager =    LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL , false)
                        binding.productRecycler.adapter = ServicesProductListAdapter(
                            this,
                            it.products!!,
                            supportFragmentManager,
                            servicesViewModel!!
                        )
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })


        storeViewModel!!.observeAddressesList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}"
                    )
                    /*for (i in it.addresses!!.indices)
                    Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                    if (!it.data!!.addresses!!.isNullOrEmpty()) {
                        SharedPrefSettings.getPreferences.storeStoreCustomerId(it.data!!.addresses!![0].customer_id.toString())
                        var findAddress =
                            it.data!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchSelectedAddressId() }
                        if (findAddress != null) {
                            binding.addressName.text = findAddress.address_name
                            binding.fullAddress.text = findAddress.address_1
                        } else {
                            binding.addressName.text = "Select address"
                            binding.fullAddress.text = "Address not selected"

                        }
                    } else {
                        binding.addressName.text = "Add address"
                        binding.fullAddress.text = "Address not added"
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }

        })


        servicesViewModel!!.observeAddWishListItem().observe(this , Observer{response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.wishlist!!}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })


        servicesViewModel!!.observeDeleteWishListItem().observe(this , Observer{response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.wishlist!!}")
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })


        servicesViewModel!!.observeGetReviewListForProduct().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store REVIEWs",
                        "setLiveData ${it.data!!.reviews!! + it.data!!.reviews!!.size}"
                    )

                    binding.reviewCountText.setText("(${it.data!!.reviews!!.size})")

                    /*  if(it.data!!.reviews!!.size >= 2) {
                          val limitedList: ArrayList<ReviewListData> =
                              ArrayList(it.data!!.reviews!!.subList(0, minOf(2, it.data!!.reviews!!.size)))

                          binding.reviewRecycler.layoutManager =
                              LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                          binding.reviewRecycler.adapter =
                              StoreReviewAdapter(this,limitedList, storeViewModel!!)

                      }
                      else{*/
                    binding.reviewRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.reviewRecycler.adapter =
                        StoreReviewAdapter(this, it.data!!.reviews!!, storeViewModel!!)
                    //  }


                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })



    }

    private fun getMatchingVariantId(): String? {
        for (variant in product.variants.orEmpty()) {
            val variantMap = variant.options?.associateBy(
                { opt -> product.options?.find { it.id == opt.option_id }?.title?.lowercase().orEmpty() },
                { it.value.orEmpty().lowercase() }
            )

            if (variantMap == selectedOptionsMap) {
                return variant.id
            }
        }
        return null
    }


    private fun updatePriceUI(selectedMap: Map<String, String>) {
        val selectedVariant = product.variants?.find { variant ->
            val variantOptions = variant.options?.associateBy(
                { option -> product.options?.find { it.id == option.option_id }?.title?.lowercase() ?: "" },
                { it.value?.lowercase() ?: "" }
            )

            variantOptions == selectedMap.mapValues { it.value.lowercase() }
        }

        if (selectedVariant != null) {
            val price = selectedVariant.calculated_price?.originalAmount ?: 0
            val decimalFormat = DecimalFormat("0.00")
            binding.productPrice.text = "₹" + decimalFormat.format(price).toString() + "/-"  // Convert paise to rupees if needed
        }
    }

}