package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.StorCirclularImageListItemBinding
import `in`.woloo.www.store.ColorNameUtils
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.ValuesProductListData
import `in`.woloo.www.store.product_response.VariantsProductListData
import `in`.woloo.www.utils.Logger

class ColorItemListAdapter(private val context : Activity,
                           private var optionsName : String,
                           private var valuesList: ArrayList<ValuesProductListData>?,
                           private val allVariants: List<VariantsProductListData>,
                           private val product : ProductListData,
                           private val onColorSelected: (List<String?>?, Int) -> Unit,
                           private val onMatchingVariantsCalculated: (Map<String, List<Pair<String, Int>>>) -> Unit,
                           private val onOptionValueSelected: (String, String) -> Unit
) : RecyclerView.Adapter<ColorItemListAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var selectedSize = -1
    private var valuePriceMap = emptyMap<String, Int>()
        var selectedOptionName = ""
        var selectedOptionValiue = ""

    class ViewHolder(val binding:StorCirclularImageListItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val  binding = StorCirclularImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val value = valuesList!![position]

        val price = valuePriceMap[value.value] ?: 0



       // Log.d("Aarati Store Values" , value.value.toString())
        holder.binding.colorSelectedImage.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE
        if (position == selectedSize) {
            holder.binding.sizeText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.yellow_button_background_square)
        } else {
            holder.binding.sizeText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.new_button_background_smallest)
        }

if(optionsName.equals("Colors",ignoreCase = true)
    || optionsName.equals("Color",ignoreCase = true)
    || optionsName.contains("colour",ignoreCase = true)) {
    val colorStr = value.value ?: ""



   // holder.binding.productPrice.text = allVariants.get(position).calculated_price!!.original_amount.toString()

    if (colorStr.matches(Regex("^[0-9a-fA-F]{6,8}$"))) {
        // It's a hex code (without #) like "ffffff", "60ff11"
        holder.binding.colorImage.setColorFilter(Color.parseColor("#$colorStr"))
      //  val colorName = ColorNameUtils.getClosestColorName("#$colorStr")
   //     holder.binding.productVariantName.text = value.value.toString()
        holder.binding.sizeFrame.visibility = View.GONE
        holder.binding.colorFrame.visibility = View.VISIBLE
    } else {
        try {
            holder.binding.sizeFrame.visibility = View.GONE
            holder.binding.colorFrame.visibility = View.VISIBLE
            holder.binding.colorImage.setColorFilter(Color.parseColor(value.value ?: "#000000"))
          //  holder.binding.productVariantName.text = value.value.toString()
        } catch (e: Exception) {
            holder.binding.colorImage.setColorFilter(Color.GRAY)
        }
    }

}else
{
    holder.binding.colorFrame.visibility = View.GONE
    holder.binding.sizeFrame.visibility = View.VISIBLE
    holder.binding.sizeText.text = value.value.toString()
}



        holder.binding.colorFrame.setOnClickListener {
            // Update selection
            val previousSelected = selectedPosition
            selectedPosition = position

            // Refresh items
            notifyItemChanged(previousSelected) // Deselect previous
            notifyItemChanged(selectedPosition) // Select new



            val selectedColor = value.value?.lowercase() ?: ""

            val matchingImages = product.images?.filter {
                it.url?.contains(selectedColor, ignoreCase = true) == true
            }?.map { it.url }

            onColorSelected.invoke(matchingImages , position)

            selectedOptionName = optionsName
            selectedOptionValiue = value.value.toString()
            onOptionValueSelected.invoke(optionsName, selectedOptionValiue)

            val selectedOptionId = product.options!!.find { it.title.equals(selectedOptionName, ignoreCase = true) }?.id

            val matchingVariants = if (selectedOptionId != null) {
                product.variants!!.filter { variant ->
                    variant.options!!.any {
                        it.option_id == selectedOptionId && it.value.equals(selectedOptionValiue, ignoreCase = true)
                    }
                }
            } else emptyList()

            for (i in matchingVariants.indices)
            {
                Log.d("Aarati Store Variants" , matchingVariants.get(i).title.toString() + " " + matchingVariants.get(i).calculated_price!!.originalAmount.toString() )
            }


            // After getting matchingVariants
            val remainingOptions = product.options!!.filterNot { it.id == selectedOptionId }

            val availableOptionsWithPrices = mutableMapOf<String, MutableList<Pair<String, Int>>>()

            for (option in remainingOptions) {
                val valuesWithPrices = mutableSetOf<Pair<String, Int>>()

                for (variant in matchingVariants) {
                    val value = variant.options?.find { it.option_id == option.id }?.value
                    val price = variant.calculated_price?.originalAmount

                    if (value != null && price != null) {
                        valuesWithPrices.add(value to price)
                    }
                }

                option.title?.let { title ->
                    availableOptionsWithPrices[title] = valuesWithPrices.toMutableList()
                }
            }

// Send this data to the activity/fragment to update other adapters
            onMatchingVariantsCalculated.invoke(availableOptionsWithPrices)



        }

        holder.binding.sizeFrame.setOnClickListener {
            val previousSelected = selectedSize
            selectedSize = position

            // Refresh items
            notifyItemChanged(previousSelected) // Deselect previous
            notifyItemChanged(selectedSize)


            val selectedColor = value.value?.lowercase() ?: ""

            val matchingImages = product.images?.filter {
                it.url?.contains(selectedColor, ignoreCase = true) == true
            }?.map { it.url }

         //   onColorSelected.invoke(matchingImages , position)

            selectedOptionName = optionsName
            selectedOptionValiue = value.value.toString()
            onOptionValueSelected.invoke(optionsName, selectedOptionValiue)

            val selectedOptionId = product.options!!.find { it.title.equals(selectedOptionName, ignoreCase = true) }?.id

            val matchingVariants = if (selectedOptionId != null) {
                product.variants!!.filter { variant ->
                    variant.options!!.any {
                        it.option_id == selectedOptionId && it.value.equals(selectedOptionValiue, ignoreCase = true)
                    }
                }
            } else emptyList()

            for (i in matchingVariants.indices)
            {
                Log.d("Aarati Store Variants" , matchingVariants.get(i).title.toString() + " " + matchingVariants.get(i).calculated_price!!.originalAmount.toString() )
            }




            // After getting matchingVariants
            val remainingOptions = product.options!!.filterNot { it.id == selectedOptionId }

            val availableOptionsWithPrices = mutableMapOf<String, MutableList<Pair<String, Int>>>()

            for (option in remainingOptions) {
                val valuesWithPrices = mutableSetOf<Pair<String, Int>>()

                for (variant in matchingVariants) {
                    val value = variant.options?.find { it.option_id == option.id }?.value
                    val price = variant.calculated_price?.originalAmount

                    if (value != null && price != null) {
                        valuesWithPrices.add(value to price)
                    }
                }

                option.title?.let { title ->
                    availableOptionsWithPrices[title] = valuesWithPrices.toMutableList()
                }
            }

// Send this data to the activity/fragment to update other adapters
            onMatchingVariantsCalculated.invoke(availableOptionsWithPrices)




        }

    }
    fun updateValues(newValues: List<Pair<String, Int>>) {
        valuePriceMap = newValues.toMap()

        valuesList!!.clear()
        valuesList!!.addAll(newValues.map { (value, _) ->
            ValuesProductListData().apply {
                this.value = value
            }
        })
        Logger.i("Aarati 1 Colors price is " , valuePriceMap.toString())
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int = valuesList!!.size

}