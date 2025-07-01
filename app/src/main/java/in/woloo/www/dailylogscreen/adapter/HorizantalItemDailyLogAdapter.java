package in.woloo.www.dailylogscreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.dailylogscreen.models.DailyLogSubTitle;
import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.period_tracker.model.Log;


public class HorizantalItemDailyLogAdapter extends RecyclerView.Adapter<HorizantalItemDailyLogAdapter.GroupItemsViewHolder> {

    private Context context;
    private List<DailyLogSubTitle> dailyLogSubTitleArrayList;
    private LayoutInflater inflater;
    private SharedPreference mSharedPreference;
    private int titlePosition;

    public HorizantalItemDailyLogAdapter(Context context, List<DailyLogSubTitle> dailyLogSubTitleArrayList, int titlePosition) {
        this.dailyLogSubTitleArrayList = dailyLogSubTitleArrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.titlePosition = titlePosition;
    }

    @NonNull
    @NotNull
    @Override
    public GroupItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.horizontal_rcy_design, parent, false);
        return new GroupItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupItemsViewHolder holder, int position) {
        holder.bind(dailyLogSubTitleArrayList.get(position), position);
        mSharedPreference = new SharedPreference(context);
        //  dailyLogSymptoms = new DailyLogSymptoms();
        final DailyLogSubTitle dailyLogSubTitleobj = dailyLogSubTitleArrayList.get(position);
        holder.imageText.setText(dailyLogSubTitleobj.subTitleName);
        holder.imageView.setImageResource(dailyLogSubTitleobj.imageUrl);

    }

    @Override
    public int getItemCount() {
        return dailyLogSubTitleArrayList.size();
    }


    public class GroupItemsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout imageContainCard;
        ImageView imageView;
        TextView imageText;
        ImageView chekedImage;

        public GroupItemsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageContainCard = itemView.findViewById(R.id.rel);
            imageView = itemView.findViewById(R.id.groupItemImage);
            imageText = itemView.findViewById(R.id.imgNameText);
            chekedImage = itemView.findViewById(R.id.chekedImage);

            //itemView.setOnClickListener(this);
        }

        void bind(final DailyLogSubTitle dailyLogSubTitle, int position) {

            //  imageContainCard.setVisibility(dailyLogSubTitle.isChecked() ? View.VISIBLE :View.GONE);

            if (dailyLogSubTitle.isChecked()) {
                imageContainCard.setBackgroundResource(R.drawable.circular_background);
                chekedImage.setVisibility(View.VISIBLE);
            } else {
                imageContainCard.setBackgroundResource(R.drawable.circular_background);
                chekedImage.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dailyLogSubTitle.setChecked(!dailyLogSubTitle.isChecked());
                    Gson gson = new Gson();
                    if (dailyLogSubTitle.isChecked()) {
                        imageContainCard.setBackgroundResource(R.drawable.circular_background);
                        chekedImage.setVisibility(View.VISIBLE);
                    } else {
                        imageContainCard.setBackgroundResource(R.drawable.circular_background);
                        chekedImage.setVisibility(View.GONE);
                    }
                }
            });

            /*if (pos==-1)
            { imageContainCard.setBackgroundResource(R.drawable.circular_background);
                chekedImage.setVisibility(View.GONE); }
            else { if (pos==getAdapterPosition()) {
                    imageContainCard.setBackgroundResource(R.drawable.circular_background);
                    chekedImage.setVisibility(View.VISIBLE); }
                else { imageContainCard.setBackgroundResource(R.drawable.circular_background);
                    chekedImage.setVisibility(View.GONE); } }*/
        }

    }


   /* public interface itemClickListner
    {
        void onItemClick(int position);
    }*/
}
