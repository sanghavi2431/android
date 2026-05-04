package `in`.woloo.www.store.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.databinding.OptionsParentAdapterBinding
import `in`.woloo.www.store.product_response.ImagesProductListData
import `in`.woloo.www.store.product_response.OptionsProductListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.ValuesProductListData
import `in`.woloo.www.store.product_response.VariantsProductListData
import `in`.woloo.www.utils.Logger


class OptionsAdapter(
    private val context : Activity,
    private val optionsList: List<OptionsProductListData>,
    private val allVariants: List<VariantsProductListData>,
    private val product : ProductListData ,
    var  productImageRecylcerAdapter :ProductImageRecylcerAdapter,
    private val selectedOptionsMap: MutableMap<String, String>,
    private val onOptionsChanged: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

   // lateinit var binding: OptionsParentAdapterBinding
    private val optionAdaptersMap = mutableMapOf<String, ColorItemListAdapter>()
    private var hasInitializedDefaultPrices = false


    class ViewHolder(val binding:OptionsParentAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OptionsParentAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val options = optionsList[position]
        holder.binding.optionsTitle.text = options.title.toString()

        val normalizedTitle = options.title?.lowercase().orEmpty()

        val colorsoptionsAdapter = ColorItemListAdapter(context ,options.title!!, options.values ,
            allVariants , product ,
            onColorSelected = { matchingImageUrls, selectedPosition ->
                val updatedImageData = ArrayList<ImagesProductListData>()
                matchingImageUrls?.forEach { url ->
                    val imageData = ImagesProductListData()
                    imageData.url = url
                    imageData.metadata = "" // Set properly if you have color metadata
                    updatedImageData.add(imageData)
                }

                productImageRecylcerAdapter.updateImageList(
                    newProductData = updatedImageData,
                    newThumbnail = updatedImageData.firstOrNull()?.url ?: "",
                    newSelectedColorPosition = selectedPosition
                )
            },
            onMatchingVariantsCalculated = { updatedOptionsMap ->
                updateOtherOptionAdapters(updatedOptionsMap , options.title?.lowercase() ?: "")
            },
            onOptionValueSelected = { optionTitle, selectedValue ->
                // ✅ Update main selectedOptionsMap here
                selectedOptionsMap[optionTitle.lowercase()] = selectedValue.lowercase()
                Logger.i("Aarati Selected Options Map", selectedOptionsMap.toString())
                onOptionsChanged(selectedOptionsMap)
            }
        )

        optionAdaptersMap[normalizedTitle] = colorsoptionsAdapter

        // Only run once — after all adapters are mapped
        if (!hasInitializedDefaultPrices && optionAdaptersMap.size == optionsList.size) {
            hasInitializedDefaultPrices = true

            val defaultVariant = product.variants?.firstOrNull()
            val defaultOptions = defaultVariant?.options ?: emptyList()

            val selectedMap = mutableMapOf<String, String>()
            for (opt in defaultOptions) {
                val title = product.options?.find { it.id == opt.option_id }?.title
                if (title != null) {
                    selectedMap[title.lowercase()] = opt.value.orEmpty()
                }
            }

            val availableOptionsWithPrices = mutableMapOf<String, MutableList<Pair<String, Int>>>()

            for (option in product.options.orEmpty()) {
                val title = option.title ?: continue
                val values = option.values.orEmpty()

                val pricesList = mutableListOf<Pair<String, Int>>()

                for (value in values) {
                    val matchingVariant = product.variants!!.find { variant ->
                        // Replace only this option with the value, keep others same as default
                        val variantMap = variant.options!!.associateBy(
                            { opt -> product.options?.find { it.id == opt.option_id }?.title?.lowercase().orEmpty() },
                            { it.value.orEmpty() }
                        )

                        // Replace current option with this value
                        val modifiedMap = selectedMap.toMutableMap()
                        modifiedMap[title.lowercase()] = value.value.orEmpty()

                        variantMap == modifiedMap
                    }

                    if (matchingVariant != null) {
                        pricesList.add(value.value.orEmpty() to (matchingVariant.calculated_price?.originalAmount ?: 0))
                    } else {
                        // Optional: fallback to 0 or mark as unavailable
                        pricesList.add(value.value.orEmpty() to 0)
                    }
                }

                availableOptionsWithPrices[title] = pricesList
            }
            Logger.i("Aarati price is " , availableOptionsWithPrices.toString())
            updateOtherOptionAdapters(availableOptionsWithPrices, "")
        }


        holder.binding.colorsRecycler.layoutManager = LinearLayoutManager(context ,  LinearLayoutManager.HORIZONTAL, false)
        holder.binding.colorsRecycler.adapter = colorsoptionsAdapter

    }

    override fun getItemCount(): Int = optionsList.size

    private fun updateOtherOptionAdapters(
        updatedOptionsMap: Map<String, List<Pair<String, Int>>>,
        currentOptionTitle: String
    ) {
        for ((optionTitle, valuePriceList) in updatedOptionsMap) {
            val normalizedTitle = optionTitle.lowercase()

            if (normalizedTitle == currentOptionTitle.lowercase()) continue

            val adapterToUpdate = optionAdaptersMap[normalizedTitle]
            if (adapterToUpdate != null) {
                Logger.i("Aarati 1 Options price is " , valuePriceList.toString())
                adapterToUpdate.updateValues(valuePriceList)
            }
        }
    }


}
