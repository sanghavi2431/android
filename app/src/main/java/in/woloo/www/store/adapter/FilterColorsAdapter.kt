package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.StorCirclularImageListItemBinding
import android.graphics.Color
import android.util.Log
import `in`.woloo.www.databinding.OptionsParentAdapterBinding
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.ValuesProductListData
import `in`.woloo.www.store.product_response.VariantsProductListData

class FilterColorsAdapter(private val context : Activity,
                          private var valuesList: ArrayList<String>,
                          private val listener: OnItemSelectedListener
) : RecyclerView.Adapter<FilterColorsAdapter.ViewHolder>() {

    private var selectedSize = -1
    class ViewHolder(val binding:StorCirclularImageListItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding = StorCirclularImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val value = valuesList!![position]

        holder.binding.sizeFrame.visibility = View.VISIBLE
        holder.binding.colorFrame.visibility = View.GONE

        holder.binding.sizeText.text = value

        if (position == selectedSize) {
            holder.binding.sizeText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.yellow_button_background_square)
        } else {
            holder.binding.sizeText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.new_button_background_smallest)
        }



        holder.binding.sizeFrame.setOnClickListener{
            selectedSize = position
            listener.onSizeSelected(value)
            notifyDataSetChanged()

        }

    }

    interface OnItemSelectedListener {
        fun onSizeSelected(selectedValue: String)
    }

    override fun getItemCount(): Int = valuesList?.size ?: 0
}