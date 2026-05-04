package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.CategoryChipListItemBinding
import `in`.woloo.www.databinding.RecentlySearchedListItemBinding
import `in`.woloo.www.databinding.StoreFilterPopupBinding
import `in`.woloo.www.databinding.StoreHelpOrderPopupBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CategoriesCustomeAdapter
import `in`.woloo.www.store.adapter.ColorItemListAdapter
import `in`.woloo.www.store.adapter.FilterColorsAdapter
import `in`.woloo.www.store.adapter.NewInStoreCustomAdapter
import `in`.woloo.www.store.adapter.PeriodEssentialsCustomeAdapter
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.product_response.ImagesProductListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

class FilterProductsFragment : BottomSheetDialogFragment() ,FilterColorsAdapter.OnItemSelectedListener {

    private var _binding: StoreFilterPopupBinding? = null
    private val binding get() = _binding!!
    private var storeViewModel: StoreViewModel? = null
    val categoriesList: ArrayList<CategoriesListData> = ArrayList()
    var selectedCategory:String = ""
   // var selectedColor:String = ""
   // var selectedSize:String = ""
    var filterListener: OnFilteredListener? = null
    var optionsValues  = ArrayList<String>()
    var slectedSizeForFilter : String = ""

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreFilterPopupBinding.inflate(inflater, container, false)

        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        storeViewModel!!.getCategoriesList()

        binding.applyFilters.setOnClickListener{

            filterListener?.onFilterApplied(selectedCategory , slectedSizeForFilter , AppConstants.FROM_FILTER_CATEGORIES)
            dismiss()
        }

        binding.reset.setOnClickListener{
            selectedCategory = ""
           // selectedColor = ""
            slectedSizeForFilter = ""

        }


        storeViewModel!!.getProductWithPriceList(
            "*variants.calculated_price,+variants.inventory_quantity,*categories",
            SharedPrefSettings.getPreferences.fetchRegionId().toString()
        )


        view.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                val focusedView = dialog?.currentFocus
                focusedView?.let {
                    binding.scrollBottomSheet.smoothScrollTo(0, it.bottom)
                }
            }
        }


        storeViewModel!!.observeCategoriesList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i(
                    "Aarati Store",
                    "setLiveData ${it.productCategories!! + it.productCategories!!.size}"
                )
                for (i in it.productCategories!!.indices) {
                    Logger.i(
                        "Aarati Store",
                        "setLiveData ${it.productCategories!![i].id + it.productCategories!![i].name}"
                    )
                    /* Logger.i(
                         "Aarati Store",
                         "metadata ${it.productCategories!![i].metadata!!.image + " " + it.productCategories!![i].metadata!!.background_color}"
                     )*/
                }


                for (category in it.productCategories!!) {
                    categoriesList.add(category)
                }

                updateChipGroup()

            }
        })

        storeViewModel!!.observeProductWithPriceList()
            .observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    try{
                      //  Logger.i("Aarati Store", "setLiveData ${it.products!! + it.products!!.size}")
                      /*  for (i in it.products!!.indices) {
                                for(j in it.products!![i].options!!.indices)
                                {
                                    if(it.products!![i].options!![j].title.equals("size" , ignoreCase = true) ||
                                        it.products!![i].options!![j].title.equals("sizes" , ignoreCase = true))
                                        for(k in it.products!![i].options!![j].values!!.indices) {
                                            optionsValues!!.add(it.products!![i].options!![j].values!![k].value!!)
                                            Logger.i(
                                                "Aarati options",
                                                "setLiveData ${it.products!![i].options!![j].values!![k].value!!}")
                                        }
                                }

                        }*/
                        it.products?.forEach { product ->
                            product.options?.forEach { option ->
                                if (option.title.equals("size", ignoreCase = true) || option.title.equals("sizes", ignoreCase = true)) {
                                    option.values?.forEach { value ->
                                        value.value?.let { safeValue ->
                                            optionsValues?.add(safeValue)
                                            Logger.i("Aarati options", "setLiveData $safeValue")
                                        }
                                    }
                                }
                            }
                        }
                        val uniqueOptions = optionsValues?.distinctBy { it.lowercase() } ?: emptyList()
                        Logger.i("Aarati options", "unique value ${uniqueOptions.size} + ${optionsValues.size}")
                        for(i in uniqueOptions.indices)
                        {
                            Logger.i("Aarati options", "unique value ${uniqueOptions[i]}")
                        }

                        val optionsAdapter = FilterColorsAdapter(requireActivity() , ArrayList(uniqueOptions) , this
                        )

                       binding.sizeRecycler.layoutManager = LinearLayoutManager(context ,  LinearLayoutManager.HORIZONTAL, false)
                       binding.sizeRecycler.adapter = optionsAdapter


                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }


                }

            })



    }

   /* private fun updateChipGroup() {
        binding.categoriesChipgroup.removeAllViews() // Clear previous views

        for (item in categoriesList) {
            // Inflate the custom layout with Data Binding (NO parent attached)
            val bindingChip = CategoryChipListItemBinding.inflate(
                LayoutInflater.from(binding.categoriesChipgroup.context), null, false
            )

            // Bind data to layout
            bindingChip.recentTextItem.text = item.name

            bindingChip.root.setOnClickListener{
                selectedCategory =  item.id.toString()


            }

            // Ensure the view is not attached anywhere else before adding
            if (bindingChip.root.parent != null) {
                (bindingChip.root.parent as ViewGroup).removeView(bindingChip.root)
            }

            // Add the bound view to the ChipGroup
            binding.categoriesChipgroup.addView(bindingChip.root)
        }
    }*/



    private fun updateChipGroup() {
        binding.categoriesChipgroup.removeAllViews()

        for (item in categoriesList) {
            val bindingChip = CategoryChipListItemBinding.inflate(
                LayoutInflater.from(binding.categoriesChipgroup.context), binding.categoriesChipgroup, false
            )

            bindingChip.recentTextItem.text = item.name

            // ✅ Check and apply background based on selectedCategory
            if (item.id == selectedCategory) {
                bindingChip.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.yellow_button_background_square)
            } else {
                bindingChip.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.new_button_background_smallest)
            }

            // ✅ Set click listener
            bindingChip.root.setOnClickListener {
                if (selectedCategory != item.id) {
                    selectedCategory = item.id.toString()
                    if (item.id == selectedCategory) {
                        bindingChip.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.yellow_button_background_square)
                    } else {
                        bindingChip.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.new_button_background_smallest)
                    }
                    updateChipGroup() // Refresh all views
                }
            }

            binding.categoriesChipgroup.addView(bindingChip.root)
        }
    }



    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    interface OnFilteredListener {
        fun onFilterApplied(selectedCategory : String ,  selectedSize : String , FROMSCREEN : String)
    }

    override fun onSizeSelected(selectedValue: String) {

        slectedSizeForFilter = selectedValue

    }





}
