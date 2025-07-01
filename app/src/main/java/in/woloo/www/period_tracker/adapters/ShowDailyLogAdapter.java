package in.woloo.www.period_tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.my_history.model.MoodsModel;
import in.woloo.www.period_tracker.model.DailyLogWithTitle;
import in.woloo.www.utils.ImageUtil;

public class ShowDailyLogAdapter extends RecyclerView.Adapter<ShowDailyLogAdapter.ViewHolder> {

    public Context context;
    private List<DailyLogWithTitle> moodsModelsList;

    public ShowDailyLogAdapter(Context context) {
        this.context = context;
    }

    public void addMoods(List<DailyLogWithTitle> moodsModelsList) {
        this.moodsModelsList = moodsModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.show_daily_log_data_layout, parent, false);//woloo_search_item
        ShowDailyLogAdapter.ViewHolder viewHolder = new ShowDailyLogAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyLogWithTitle dailyLogWithTitle = moodsModelsList.get(position);
        holder.tvMoods.setText(dailyLogWithTitle.getTitleName());
        int symptomsSize = dailyLogWithTitle.getDailyLogSymptoms().size();
        if (symptomsSize == 1) {
            holder.imvMultipleMoods.setVisibility(View.GONE);
        } else {
            holder.imvMultipleMoods.setVisibility(View.VISIBLE);
            holder.imvMultipleMoods.setText("+" + (symptomsSize - 1));
        }
        holder.imvMoods.setImageResource(dailyLogWithTitle.getDailyLogSymptoms().get(0).imageURL);
    }

    @Override
    public int getItemCount() {
        return moodsModelsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.moods_Tv)
        TextView tvMoods;

        @BindView(R.id.moods_Imv)
        ImageView imvMoods;

        @BindView(R.id.multipleMoods_Imv)
        TextView imvMultipleMoods;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
