package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.DateTimeViewRecyclerItemBinding
import `in`.woloo.www.services.SelectedDateTimeClass
import java.time.LocalDate

class SelectedServiceDateTimeAdapter (private val dateList: List<SelectedDateTimeClass>,
                                      private val onDateSelected: (SelectedDateTimeClass) -> Unit
) : RecyclerView.Adapter<SelectedServiceDateTimeAdapter.DateViewHolder>() {

    private var selectedPosition = -1

    inner class DateViewHolder(val binding: DateTimeViewRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DateTimeViewRecyclerItemBinding.inflate(layoutInflater, parent, false)
        return DateViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DateViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val localDate = dateList[position]

        val day = localDate.selectedDate.dayOfWeek.toString().take(3).lowercase().replaceFirstChar { it.uppercase() } // e.g. Mon
        val date = localDate.selectedDate.dayOfMonth.toString() // e.g. 25

        holder.binding.selectServiceDay.text = day
        holder.binding.selectServiceDate.text = date
        holder.binding.selectServiceTime.text = localDate.selectedTimeSlots.slotStartTime + "-" + localDate.selectedTimeSlots.slotEndTime

        if (position == selectedPosition) {
            holder.binding.root.setBackgroundResource(R.drawable.yello_rectangle_shape)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.new_button_background)
        }

        holder.binding.root.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)

            onDateSelected(localDate)
        }

    }

    override fun getItemCount(): Int = dateList.size



}
