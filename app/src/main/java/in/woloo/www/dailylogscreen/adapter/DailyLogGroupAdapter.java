package in.woloo.www.dailylogscreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.dailylogscreen.models.DailyLogGroupTitle;
import in.woloo.www.dailylogscreen.models.DailyLogSubTitle;
import in.woloo.www.interestedtopic.adapter.InterestedTopicAdapter;

public class DailyLogGroupAdapter extends RecyclerView.Adapter<DailyLogGroupAdapter.GroupViewHolder> {
    ArrayList<DailyLogGroupTitle> titlesofEveryGropup;
    Context context;
    LayoutInflater inflater;

    public DailyLogGroupAdapter(Context context, ArrayList<DailyLogGroupTitle> titlesofEveryGropup)
    {
        this.titlesofEveryGropup=titlesofEveryGropup;
        this.inflater=LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.dailylog_rcy_group_design,parent,false);
        return new DailyLogGroupAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GroupViewHolder holder, int position) {
        holder.itemNameGroups.setText(titlesofEveryGropup.get(position).groupName);
        holder.horizontalGroupItemRcy.setAdapter(new HorizantalItemDailyLogAdapter(context, titlesofEveryGropup.get(position).subTitle, position));
        holder.horizontalGroupItemRcy.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.horizontalGroupItemRcy.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return titlesofEveryGropup.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemNameGroups;
        RecyclerView horizontalGroupItemRcy;

        public GroupViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            itemNameGroups=itemView.findViewById(R.id.itemNameGroup);
            horizontalGroupItemRcy=itemView.findViewById(R.id.groupItemRecyHorizontal);
        }
    }
}
