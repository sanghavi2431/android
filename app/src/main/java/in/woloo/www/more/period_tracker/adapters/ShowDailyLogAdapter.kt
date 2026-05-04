package `in`.woloo.www.more.period_tracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.more.period_tracker.model.DailyLogWithTitle

class ShowDailyLogAdapter(var context: Context) :
    RecyclerView.Adapter<ShowDailyLogAdapter.ViewHolder>() {
    private var moodsModelsList: List<DailyLogWithTitle>? = null

    fun addMoods(moodsModelsList: List<DailyLogWithTitle>?) {
        this.moodsModelsList = moodsModelsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(
            R.layout.show_daily_log_data_layout,
            parent,
            false
        ) //woloo_search_item
        val viewHolder = ViewHolder(listItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyLogWithTitle: DailyLogWithTitle = moodsModelsList!![position]
        holder.tvMoods!!.setText(dailyLogWithTitle.titleName)
        val symptomsSize: Int = dailyLogWithTitle.getDailyLogSymptoms().size
        if (symptomsSize == 1) {
            holder.imvMultipleMoods!!.visibility = View.GONE
        } else {
            holder.imvMultipleMoods!!.visibility = View.VISIBLE
            holder.imvMultipleMoods!!.text = "+" + (symptomsSize - 1)
        }
        holder.imvMoods!!.setImageResource(dailyLogWithTitle.getDailyLogSymptoms()[0]!!.imageURL)
    }

    override fun getItemCount(): Int {
        return moodsModelsList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.moods_Tv)
        var tvMoods: TextView? = null

        @JvmField
        @BindView(R.id.moods_Imv)
        var imvMoods: ImageView? = null

        @JvmField
        @BindView(R.id.multipleMoods_Imv)
        var imvMultipleMoods: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
