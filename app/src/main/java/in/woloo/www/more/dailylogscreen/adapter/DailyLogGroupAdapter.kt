package `in`.woloo.www.more.dailylogscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.more.dailylogscreen.models.DailyLogGroupTitle

class DailyLogGroupAdapter(
    var context: Context,
    titlesofEveryGropup: ArrayList<DailyLogGroupTitle>
) :
    RecyclerView.Adapter<DailyLogGroupAdapter.GroupViewHolder>() {
    var titlesofEveryGropup: ArrayList<DailyLogGroupTitle> = titlesofEveryGropup
    var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = inflater.inflate(R.layout.dailylog_rcy_group_design, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.itemNameGroups.setText(titlesofEveryGropup[position].groupName)
        holder.horizontalGroupItemRcy.adapter =
            HorizantalItemDailyLogAdapter(
                context,
                titlesofEveryGropup[position].subTitle!!,
                position
            )
        holder.horizontalGroupItemRcy.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.horizontalGroupItemRcy.setHasFixedSize(true)
    }

    override fun getItemCount(): Int {
        return titlesofEveryGropup.size
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemNameGroups: TextView =
            itemView.findViewById(R.id.itemNameGroup)
        var horizontalGroupItemRcy: RecyclerView =
            itemView.findViewById(R.id.groupItemRecyHorizontal)
    }
}
