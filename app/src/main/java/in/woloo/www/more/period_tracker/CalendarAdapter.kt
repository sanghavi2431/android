package `in`.woloo.www.more.period_tracker

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.graphics.toArgb
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R

/*class CalendarAdapter(private val days: List<CalendarDay>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val dotContainer: ImageView = itemView.findViewById(R.id.dotContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        if (day.day != null) {
            holder.dayText.text = day.day.toString()
            holder.dayText.visibility = View.VISIBLE
        } else {
            holder.dayText.visibility = View.INVISIBLE
        }

        // Populate dots

          *//*  val dot = View(holder.itemView.context).apply {
                layoutParams = LinearLayout.LayoutParams(10, 10).apply {
                    setMargins(4, 0, 4, 0)
                }
                if(MainActivity.dots.isNotEmpty()) {
                    setBackgroundColor(MainActivity.dots[position])
                }
            }*//*
     *//*   if( holder.dayText.visibility == View.VISIBLE) {
            var pos: Int = (holder.dayText.text.toString()).toInt()
            for(i in 0..days.size) {
                if (position == pos) {
                    holder.dotContainer.setBackgroundColor(MainActivity.dots[position])

                }
            }
        }*//*
    }

    override fun getItemCount(): Int = days.size
}*/
/*
class CalendarAdapter(private val days: List<CalendarDay>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val dotContainer: LinearLayout = itemView.findViewById(R.id.dotContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]

        if (day.day != null) {
            holder.dayText.text = day.day.toString()
            holder.dayText.visibility = View.VISIBLE
        } else {
            holder.dayText.visibility = View.INVISIBLE
        }

        holder.dotContainer.removeAllViews() // Clear previous dots
        day.dots.forEach { colorRes ->
            val dot = View(holder.dotContainer.context).apply {
                layoutParams = LinearLayout.LayoutParams(16, 16).apply {
                    setMargins(4, 0, 4, 0)
                }
                setBackgroundResource(colorRes)
            }
            holder.dotContainer.addView(dot)
        }
    }

    override fun getItemCount(): Int = days.size
}*/

class CalendarAdapter(private val days: List<CalendarDay>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val dotContainer: LinearLayout = itemView.findViewById(R.id.dotContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        if (day.day != null) {
            holder.dayText.text = day.day.toString()
            holder.dayText.visibility = View.VISIBLE

            // Clear previous dots (in case any dots were added from previous bindings)
            holder.dotContainer.removeAllViews()

            // Add dots based on the colors list
            for (colorResId in day.dots) {
                val dotView = View(holder.dotContainer.context).apply {
                    layoutParams = LinearLayout.LayoutParams(12, 12).apply {
                        gravity = Gravity.CENTER
                        setMargins(2, 0, 2, 0)
                    }
                    setBackgroundColor(holder.dotContainer.context.getColor(colorResId))
                }
                holder.dotContainer.addView(dotView)
            }
        } else {
            holder.dayText.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = days.size
}
