package in.woloo.www.vtion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.utils.Logger;
import in.woloo.www.vtion.model.VtionModel;

public class VitionEducationAdapter extends RecyclerView.Adapter<VitionEducationAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<VtionModel> educationList;
    private static OnItemCheckListener itemCheckListener;
    Context context;
    private String selectedEducation = "";

    private int selectedItem = -1;

    //String selectedItemToHighlight;

    private String previouslySelectedOwnershipName = null;

    GlobalsVtionEducation GVE = GlobalsVtionEducation.getInstance();

    public VitionEducationAdapter(Context context, List<VtionModel> education) {
        this.inflater = LayoutInflater.from(context);
        this.educationList = education;
        this.context = context;
        if (context instanceof OnItemCheckListener) {
            itemCheckListener = (OnItemCheckListener) context;
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
        VtionModel education = educationList.get(position);


        holder.textTitle.setText(education.getOwnershipName());

        if (selectedEducation != null && selectedEducation.equals(education.getOwnershipName())) {
            // Item is selected
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
        } else {
            // Item is not selected
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
            holder.textTitle.setTextColor(context.getColor(R.color.application_background));
        }



        if (education.getOwnershipName().equals(previouslySelectedOwnershipName)) {
            // Apply the previously selected item style
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
        } else {
            // Apply default style
            holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
            holder.textTitle.setTextColor(context.getColor(R.color.application_background));
        }

        if(GVE.getSelectedEducation() == null) {
        }
        else{
            if (GVE.getSelectedEducation().equals(education.getOwnershipName())) {
                Log.d("ITTTEM", GVE.getSelectedEducation() + " " + education.getOwnershipName() + " " + position);
                holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
            }
        }



        holder.parentView.setOnClickListener(view -> {

            Logger.d("POS" , position + education.getOwnershipName());

            String currentOwnershipName = education.getOwnershipName();

            if (previouslySelectedOwnershipName != null && !previouslySelectedOwnershipName.equals(currentOwnershipName)) {
                // Find the position of the previously selected item
                int previousPosition = findPositionByOwnershipName(previouslySelectedOwnershipName);
                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition); // Reset the previous item background
                }
            }

            // Update the currently selected item
            previouslySelectedOwnershipName = currentOwnershipName;
            notifyItemChanged(position);

            Logger.d("POS OnCreate" , position + education.getOwnershipName());

                if (selectedEducation != null && selectedEducation.equals(education.getOwnershipName())) {
                    // Already selected, so deselect
                    selectedEducation = "";
                    holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
                    holder.textTitle.setTextColor(context.getColor(R.color.application_background));
                    notifyDataSetChanged();
                } else {
                    // Not selected, so select it
                    selectedEducation = education.getOwnershipName();
                    holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
                    holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
                    notifyDataSetChanged();
                }

            // Update the selected item in your data source or wherever it's stored
            GVE.setSelectedEducation(selectedEducation);


            // Notify adapter of the change
            notifyDataSetChanged();



         /*   if (selectedEducation.matches(education.getOwnershipName()))
            {

                selectedEducation = "";
                holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_view));
                holder.textTitle.setTextColor(context.getColor(R.color.application_background));


            }
            else
            {

                selectedEducation = education.getOwnershipName();
                holder.cardView.setBackground(context.getDrawable(R.drawable.vtion_education_card_selected));
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow));
            }*/





          //  GVE.setSelectedEducation(selectedEducation);


        });
    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        CardView cardView;

        View parentView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            parentView = itemView;
            textTitle = itemView.findViewById(R.id.itemNameText);
            cardView = itemView.findViewById(R.id.card_view_edu);

        }
    }

    public interface OnItemCheckListener {
        void onItemClick(int position);
    }

    public static class GlobalsVtionEducation{
        private static GlobalsVtionEducation instance;

        // Global variable
        private String selectedEducation;


        // Restrict the constructor from being instantiated
        private GlobalsVtionEducation(){}

        public String getSelectedEducation() {
            return selectedEducation;
        }

        public void setSelectedEducation(String selectedEducation) {
            this.selectedEducation = selectedEducation;
        }

        public static synchronized GlobalsVtionEducation getInstance(){
            if(instance==null){
                instance=new GlobalsVtionEducation();
            }
            return instance;
        }
    }

    private int findPositionByOwnershipName(String ownershipName) {
        for (int i = 0; i < educationList.size(); i++) {
            if (educationList.get(i).getOwnershipName().equals(ownershipName)) {
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

}