package `in`.woloo.www.services.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.ServicesDescriptionListItemBinding

class DescriptionAdapter(private val context: Activity,private val items: ArrayList<String>) :
    RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder>() {

    inner class DescriptionViewHolder(val binding: ServicesDescriptionListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesDescriptionListItemBinding.inflate(inflater, parent, false)
        return DescriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val textOfdescription = items[position]

            holder.binding.productDetailsDescription.text = textOfdescription
        val cardView = holder.binding.cardForDescription

        val colorRes = if (position % 2 == 0) {
            R.color.start_theme_color   // even index
        } else {
            R.color.search_background   // odd index
        }

        // Set proper background color (not tint)
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))

    }

    override fun getItemCount(): Int = items.size
}

