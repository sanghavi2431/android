package `in`.woloo.www.more.period_tracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.more.period_tracker.ui.PeriodTrackerFragment
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


class CalendarPagerAdapter(
private val months: List<Pair<Int, Int>> ,
private val lastPeriodStart: LocalDate
): RecyclerView.Adapter<CalendarPagerAdapter.CalendarViewHolder>() {

    private val calendar = Calendar.getInstance()

    inner class CalendarViewHolder(private val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            // Get month/year from the months list
            val (month, year) = months[position]

            // Disable vertical scroll for grid
            recyclerView.layoutManager = object : GridLayoutManager(recyclerView.context, 7) {
                override fun canScrollVertically(): Boolean = true
            }

            // Generate calendar days for this month/year
            val days = PeriodTrackerFragment.generateCalendarData(month, year , lastPeriodStart)

            recyclerView.adapter = CalendarAdapter(days)
        }





    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val recyclerView = RecyclerView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )



        }
        return CalendarViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = months.size // 6 months before, current month, 6 months after
}
