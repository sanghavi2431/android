package in.woloo.www.vtion.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.utils.Logger;
import in.woloo.www.vtion.adapter.VitionEducationAdapter;
import in.woloo.www.vtion.model.VtionModel;
import in.woloo.www.vtion.utilities.MessageList;

public class DialogVitionEducation extends DialogFragment implements VitionEducationAdapter.OnItemCheckListener {

    private VitionEducationAdapter vitionEducationAdapter;
    RecyclerView educationRecyclerview;
    private List<VtionModel> educationArray = new ArrayList<VtionModel>();;

    private OnFragmentInteractionListenerEdu mListenerEdu;

    VitionEducationAdapter.GlobalsVtionEducation GVE = VitionEducationAdapter.GlobalsVtionEducation.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_vition_ownership, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        educationRecyclerview = view.findViewById(R.id.ownership_recyclerview);
        TextView headerText = view.findViewById(R.id.interestedScreenTitle);
        headerText.setText("Select Education");
        VtionModel educationModel1 = new VtionModel();
        educationModel1.setOwnershipName(MessageList.EDU_ONE);
        educationArray.add(educationModel1);
        VtionModel educationModel2 = new VtionModel();
        educationModel2.setOwnershipName(MessageList.EDU_TWO);
        educationArray.add(educationModel2);
        VtionModel educationModel3 = new VtionModel();
        educationModel3.setOwnershipName(MessageList.EDU_THREE);
        educationArray.add(educationModel3);
        VtionModel educationModel4 = new VtionModel();
        educationModel4.setOwnershipName(MessageList.EDU_FOUR);
        educationArray.add(educationModel4);
        VtionModel educationModel5 = new VtionModel();
        educationModel5.setOwnershipName(MessageList.EDU_FIVE);
        educationArray.add(educationModel5);
        VtionModel educationModel6 = new VtionModel();
        educationModel6.setOwnershipName(MessageList.EDU_SIX);
        educationArray.add(educationModel6);
        VtionModel educationModel7 = new VtionModel();
        educationModel7.setOwnershipName(MessageList.EDU_SEVEN);
        educationArray.add(educationModel7);
        VtionModel educationModel8 = new VtionModel();
        educationModel8.setOwnershipName(MessageList.EDU_EIGHT);
        educationArray.add(educationModel8);
        VtionModel educationModel9 = new VtionModel();
        educationModel9.setOwnershipName(MessageList.EDU_NINE);
        educationArray.add(educationModel9);
        VtionModel educationModel10 = new VtionModel();
        educationModel10.setOwnershipName(MessageList.EDU_TEN);
        educationArray.add(educationModel10);




        vitionEducationAdapter = new VitionEducationAdapter(getApplicationContext(), educationArray);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        educationRecyclerview.setLayoutManager(gridLayoutManager);
        educationRecyclerview.setAdapter(vitionEducationAdapter);


        view.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
                String educationSelected = GVE.getSelectedEducation();
               // Logger.d("Education is " , educationSelected);
                sendDataToActivityEdu(educationSelected);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set dialog fragment dimensions to full screen
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Handle dialog dismissal here if needed
    }

    @Override
    public void onItemClick(int position) {
        Logger.i("LOG", educationArray.get(position).getOwnershipName());
        VtionModel ownershipData = educationArray.get(position);
        ownershipData.setSelected(!ownershipData.isSelected());
        vitionEducationAdapter.notifyItemChanged(position);

    }



    public interface OnFragmentInteractionListenerEdu {
        void onStringFragmentInteractionEdu(String data);
    }

    private void sendDataToActivityEdu(String data) {
        if (mListenerEdu != null) {
            mListenerEdu.onStringFragmentInteractionEdu(data);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerEdu) {
            mListenerEdu = (OnFragmentInteractionListenerEdu) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



}

