package in.woloo.www.vtion.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.utils.Logger;
import in.woloo.www.vtion.adapter.VitionOwnershipAdapter;
import in.woloo.www.vtion.model.VtionModel;
import in.woloo.www.vtion.utilities.MessageList;

public class DialogVitionOwnership extends DialogFragment {

    private VitionOwnershipAdapter vitionOwnershipAdapter;
    RecyclerView ownershipRecyclerview;
    private List<VtionModel> ownershipArray = new ArrayList<VtionModel>();

    public String  ownershipListString = "";

    private OnFragmentInteractionListener mListener;

    ArrayList <String> selected;

    private VtionModel viewModel;

    VitionOwnershipAdapter.GlobalsVtionOwnership GVO = VitionOwnershipAdapter.GlobalsVtionOwnership.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_vition_ownership, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ownershipRecyclerview = view.findViewById(R.id.ownership_recyclerview);
        VtionModel ownershipModel1 = new VtionModel();
        ownershipModel1.setOwnershipIcon("abc");
        ownershipModel1.setOwnershipName(MessageList.OWNERSHIP_ONE);
        ownershipArray.add(ownershipModel1);
        VtionModel ownershipModel2 = new VtionModel();
        ownershipModel2.setOwnershipIcon("abc");
        ownershipModel2.setOwnershipName(MessageList.OWNERSHIP_TWO);
        ownershipArray.add(ownershipModel2);
        VtionModel ownershipModel3 = new VtionModel();
        ownershipModel3.setOwnershipIcon("abc");
        ownershipModel3.setOwnershipName(MessageList.OWNERSHIP_THREE);
        ownershipArray.add(ownershipModel3);
        VtionModel ownershipModel4 = new VtionModel();
        ownershipModel4.setOwnershipIcon("abc");
        ownershipModel4.setOwnershipName(MessageList.OWNERSHIP_FOUR);
        ownershipArray.add(ownershipModel4);
        VtionModel ownershipModel5 = new VtionModel();
        ownershipModel5.setOwnershipIcon("abc");
        ownershipModel5.setOwnershipName(MessageList.OWNERSHIP_FIVE);
        ownershipArray.add(ownershipModel5);
        VtionModel ownershipModel6 = new VtionModel();
        ownershipModel6.setOwnershipIcon("abc");
        ownershipModel6.setOwnershipName(MessageList.OWNERSHIP_SIX);
        ownershipArray.add(ownershipModel6);
        VtionModel ownershipModel7 = new VtionModel();
        ownershipModel7.setOwnershipIcon("abc");
        ownershipModel7.setOwnershipName(MessageList.OWNERSHIP_SEVEN);
        ownershipArray.add(ownershipModel7);
        VtionModel ownershipModel8 = new VtionModel();
        ownershipModel8.setOwnershipIcon("abc");
        ownershipModel8.setOwnershipName(MessageList.OWNERSHIP_EIGHT);
        ownershipArray.add(ownershipModel8);
        VtionModel ownershipModel9 = new VtionModel();
        ownershipModel9.setOwnershipIcon("abc");
        ownershipModel9.setOwnershipName(MessageList.OWNERSHIP_NINE);
        ownershipArray.add(ownershipModel9);
        VtionModel ownershipModel10 = new VtionModel();
        ownershipModel10.setOwnershipIcon("abc");
        ownershipModel10.setOwnershipName(MessageList.OWNERSHIP_TEN);
        ownershipArray.add(ownershipModel10);
        VtionModel ownershipModel11 = new VtionModel();
        ownershipModel11.setOwnershipIcon("abc");
        ownershipModel11.setOwnershipName(MessageList.OWNERSHIP_ELEVEN);
        ownershipArray.add(ownershipModel11);
        VtionModel ownershipModel12 = new VtionModel();
        ownershipModel12.setOwnershipIcon("abc");
        ownershipModel12.setOwnershipName(MessageList.OWNERSHIP_TWELVE);
        ownershipArray.add(ownershipModel12);

        vitionOwnershipAdapter = new VitionOwnershipAdapter(getApplicationContext(), ownershipArray );
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
        ownershipRecyclerview.setLayoutManager(gridLayoutManager);
        ownershipRecyclerview.setAdapter(vitionOwnershipAdapter);


        view.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
                ownershipListString = GVO.getSelectedOwnership();
                Logger.d("String " , ownershipListString);
                if (ownershipListString != null && ownershipListString.isEmpty()) {
                    Toast.makeText(getActivity(), MessageList.SELECTOWNERSHIP, Toast.LENGTH_SHORT).show();
                } else
                {
                    ownershipListString = "[" + ownershipListString;
                  //  Logger.d("String " , ownershipListString);
                   // ownershipListString.substring(0, ownershipListString.length() - 1);
                    StringBuilder sb = new StringBuilder(ownershipListString);
                  //  sb.deleteCharAt(sb.length() - 1);
                    ownershipListString = sb.toString();
                    ownershipListString = ownershipListString + "]";
                 //   Logger.d("String " , ownershipListString);


                    sendDataToActivity(ownershipListString);
                }
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







    public interface OnFragmentInteractionListener {
        void onStringFragmentInteraction(String data);
    }

    private void sendDataToActivity(String data) {
        if (mListener != null) {
            mListener.onStringFragmentInteraction(data);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



}
