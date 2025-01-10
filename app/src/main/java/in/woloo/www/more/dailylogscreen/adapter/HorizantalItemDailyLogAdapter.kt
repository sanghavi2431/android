package `in`.woloo.www.more.dailylogscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSubTitle

class HorizantalItemDailyLogAdapter(
    private val context: Context, private val dailyLogSubTitleArrayList: List<DailyLogSubTitle>,
    private val titlePosition: Int
) :
    RecyclerView.Adapter<HorizantalItemDailyLogAdapter.GroupItemsViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var mSharedPreference: SharedPreference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemsViewHolder {
        val view = inflater.inflate(R.layout.horizontal_rcy_design, parent, false)
        return GroupItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupItemsViewHolder, position: Int) {
        holder.bind(dailyLogSubTitleArrayList[position], position)
        mSharedPreference = SharedPreference(context)
        //  dailyLogSymptoms = new DailyLogSymptoms();
        val dailyLogSubTitleobj: DailyLogSubTitle = dailyLogSubTitleArrayList[position]
        holder.imageText.setText(dailyLogSubTitleobj.subTitleName)
        holder.imageView.setImageResource(dailyLogSubTitleobj.imageUrl)
    }

    override fun getItemCount(): Int {
        return dailyLogSubTitleArrayList.size
    }


    inner class GroupItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageContainCard: RelativeLayout =
            itemView.findViewById(R.id.rel)
        var imageView: ImageView =
            itemView.findViewById(R.id.groupItemImage)
        var imageText: TextView =
            itemView.findViewById(R.id.imgNameText)
        var chekedImage: ImageView =
            itemView.findViewById(R.id.chekedImage)

        init {
            //itemView.setOnClickListener(this);
        }

        fun bind(dailyLogSubTitle: DailyLogSubTitle, position: Int) {
            //  imageContainCard.setVisibility(dailyLogSubTitle.isChecked() ? View.VISIBLE :View.GONE);

            if (dailyLogSubTitle.isChecked) {
                imageContainCard.setBackgroundResource(R.drawable.circular_background)
                chekedImage.visibility = View.VISIBLE
            } else {
                imageContainCard.setBackgroundResource(R.drawable.circular_background)
                chekedImage.visibility = View.GONE
            }

            itemView.setOnClickListener {
                dailyLogSubTitle.isChecked = !dailyLogSubTitle.isChecked
                val gson = Gson()
                if (dailyLogSubTitle.isChecked) {
                    imageContainCard.setBackgroundResource(R.drawable.circular_background)
                    chekedImage.visibility = View.VISIBLE
                } else {
                    imageContainCard.setBackgroundResource(R.drawable.circular_background)
                    chekedImage.visibility = View.GONE
                }
            }

            /*if (pos==-1)
    { imageContainCard.setBackgroundResource(R.drawable.circular_background);
        chekedImage.setVisibility(View.GONE); }
    else { if (pos==getAdapterPosition()) {
            imageContainCard.setBackgroundResource(R.drawable.circular_background);
            chekedImage.setVisibility(View.VISIBLE); }
        else { imageContainCard.setBackgroundResource(R.drawable.circular_background);
            chekedImage.setVisibility(View.GONE); } }*/
        }
    } /* public interface itemClickListner
    {
        void onItemClick(int position);
    }*/
}
