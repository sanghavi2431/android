package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.shopping.config.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FailureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FailureFragment extends Fragment {


    @BindView(R.id.go_to_shop)
    TextView go_to_shop;




    private String saveOrderUrl = Config.hostname+"save_order_api.php";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String amount;
    public String address;
    public String gift_card_used_value;

    public FailureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FailureFragment newInstance(String param1, String param2, String param3) {
        FailureFragment fragment = new FailureFragment();
        Bundle args = new Bundle();
        args.putString("amount", param1);
        args.putString("address", param2);
        args.putString("gift_card_used_value", param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amount = getArguments().getString("amount");
            address = getArguments().getString("address");
            gift_card_used_value = getArguments().getString("gift_card_used_value");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_failure, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }


    private void initViews() {
        try{

            go_to_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, new ShoppingFragment(),"");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });





        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }




}

