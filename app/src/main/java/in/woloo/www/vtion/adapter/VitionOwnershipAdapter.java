package in.woloo.www.vtion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.utils.Logger;
import in.woloo.www.vtion.model.VtionModel;

public class VitionOwnershipAdapter extends RecyclerView.Adapter<VitionOwnershipAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<VtionModel> ownershipList;
    GlobalsVtionOwnership GVO = GlobalsVtionOwnership.getInstance();

    Context context;
    String ownrshipSelected = "";

    private ArrayList<String> selectedOwnerships = new ArrayList<>();

    public VitionOwnershipAdapter(Context context, List<VtionModel> ownership ) {
        this.inflater = LayoutInflater.from(context);
        this.ownershipList = ownership;
        this.context = context;
        if (GVO.getSelectedOwnershipArray() != null) {
            selectedOwnerships.addAll(GVO.getSelectedOwnershipArray());
        }

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vtion_education_grid_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        VtionModel ownership = ownershipList.get(position);
      /*  Glide.with(context)
                .load(context.getDrawable(R.drawable.ic__01_hormones))
                .error(context.getDrawable(R.drawable.ic__01_hormones))
                .into(holder.topicImage);*/
        holder.textTitle.setText(ownership.getOwnershipName());
     //   holder.imageRelative.setBackgroundResource(R.drawable.circular_background);

        if (selectedOwnerships.contains(ownership.getOwnershipName())) {
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
        } else {
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
            holder.textTitle.setTextColor(context.getColor(R.color.application_background));
        }



        holder.parentView.setOnClickListener(view -> {
            Logger.d("POS" , position + ownership.getOwnershipName());

            String currentOwnershipName = ownership.getOwnershipName();


            if (selectedOwnerships.contains(currentOwnershipName)) {
                // Deselect the item
                selectedOwnerships.remove(currentOwnershipName);
                holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
                holder.textTitle.setTextColor(context.getColor(R.color.application_background));
            } else {
                // Select the item
                selectedOwnerships.add(currentOwnershipName);
                holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
            }

            StringBuilder selectedOwnershipsString = new StringBuilder();
            for (String ownershipName : selectedOwnerships) {
                selectedOwnershipsString.append(ownershipName).append(",");
            }
            if (selectedOwnershipsString.length() > 0) {
                selectedOwnershipsString.setLength(selectedOwnershipsString.length() - 1); // Remove trailing comma
            }
            GVO.setSelectedOwnership(selectedOwnershipsString.toString());
            GVO.setSelectedOwnershipArray(new ArrayList<>(selectedOwnerships));


        });

    }

    @Override
    public int getItemCount() {
        return ownershipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
     //   ImageView topicImage;
        TextView textTitle;
     //   RelativeLayout imageRelative;
        View parentView;
        CardView cardView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            parentView = itemView;
            textTitle = itemView.findViewById(R.id.itemNameText);
            cardView = itemView.findViewById(R.id.card_view_edu);
       //     topicImage = itemView.findViewById(R.id.checkboxItem);
       //     imageRelative = itemView.findViewById(R.id.gridDesignRel1);
        }
    }


    public static class GlobalsVtionOwnership{
        private static GlobalsVtionOwnership instance;

        // Global variable
        private String selectedOwnership;
        private ArrayList<String> selectedOwnershipArray;

        // Restrict the constructor from being instantiated
        private GlobalsVtionOwnership(){}

        public String getSelectedOwnership() {
            return selectedOwnership;
        }

        public void setSelectedOwnership(String selectedOwnership) {
            this.selectedOwnership = selectedOwnership;
        }

        public ArrayList<String> getSelectedOwnershipArray() {
            return selectedOwnershipArray;
        }

        public void setSelectedOwnershipArray(ArrayList<String> selectedOwnershipArray) {
            this.selectedOwnershipArray = selectedOwnershipArray;
        }


        public static synchronized GlobalsVtionOwnership getInstance(){
            if(instance==null){
                instance=new GlobalsVtionOwnership();
            }
            return instance;
        }
    }

}