package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.StoreCircularSizeListItemBinding

class SizeItemListAdapter (private val itemCount: Int) : RecyclerView.Adapter<SizeItemListAdapter.ViewHolder>() {

    private var selectedPosition = -1
    lateinit var binding: StoreCircularSizeListItemBinding
    class ViewHolder(val binding:StoreCircularSizeListItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StoreCircularSizeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {


        if (position == selectedPosition) {
            binding.sizeImage.setImageResource(R.drawable.imagecircle_yello) // Selected state
        } else {
            binding.sizeImage.setImageResource(R.drawable.imagecircle) // Default state
        }

        binding.root.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position

            // Refresh items
            notifyItemChanged(previousSelected) // Deselect previous
            notifyItemChanged(selectedPosition) // Select new
        }

        binding.sizeText.setOnClickListener{
            val previousSelected = selectedPosition
            selectedPosition = position

            // Refresh items
            notifyItemChanged(previousSelected) // Deselect previous
            notifyItemChanged(selectedPosition) // Select new
        }

        binding.sizeImage.setOnClickListener{
            val previousSelected = selectedPosition
            selectedPosition = position

            // Refresh items
            notifyItemChanged(previousSelected) // Deselect previous
            notifyItemChanged(selectedPosition) // Select new
        }

    }

    override fun getItemCount(): Int = itemCount
}