package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.DateViewListBinding
import java.time.LocalDate

class ServiceDateAdapter(
    private val dateList: List<LocalDate>,
    private val onDateSelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<ServiceDateAdapter.DateViewHolder>() {

    private var selectedPosition = -1

    inner class DateViewHolder(val binding: DateViewListBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DateViewListBinding.inflate(layoutInflater, parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val localDate = dateList[position]

        val day = localDate.dayOfWeek.toString().take(3).lowercase().replaceFirstChar { it.uppercase() } // e.g. Mon
        val date = localDate.dayOfMonth.toString() // e.g. 25

        holder.binding.selectServiceDay.text = day
        holder.binding.selectServiceDate.text = date

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
