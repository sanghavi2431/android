package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.ServiceTimeListItemBinding
import `in`.woloo.www.services.TimeSlotClass


class TimeSlotAdapterClass(
    private val slotList: List<TimeSlotClass>,
    private val onSlotSelected: (TimeSlotClass) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapterClass.TimeSlotViewHolder>() {

    private var selectedPosition = -1


    inner class TimeSlotViewHolder(val binding: ServiceTimeListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServiceTimeListItemBinding.inflate(inflater, parent, false)
        return TimeSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val slot = slotList[position]
        holder.binding.selectServiceTime.text = slot.slotStartTime + "-" + slot.slotEndTime

        val context = holder.itemView.context
        if (position == selectedPosition) {
            holder.binding.root.setBackgroundResource(R.drawable.yello_rectangle_shape)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.new_button_background)
        }

        // Handle click
        holder.binding.root.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)

            onSlotSelected(slot)
        }

    }

    override fun getItemCount(): Int = slotList.size
}
