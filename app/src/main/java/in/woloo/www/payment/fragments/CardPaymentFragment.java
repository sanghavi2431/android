package in.woloo.www.payment.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.payment.PaymentActivity;
import in.woloo.www.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardPaymentFragment extends Fragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvChooseAnotherPayment)
    TextView tvChooseAnotherPayment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String TAG= CardPaymentFragment.class.getSimpleName();
    public CardPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardPaymentFragment newInstance(String param1, String param2) {
        CardPaymentFragment fragment = new CardPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Logger.i(TAG, "onCreate");
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_card_payment, container, false);
        ButterKnife.bind(this,root);
        initViews();
        Logger.i(TAG, "onCreateView");
        return root;
    }
    /*calling on initViews*/
    private void initViews() {
        try{
            Logger.i(TAG, "initViews");
            tvTitle.setText(getResources().getString(R.string.choose_payment));

            tvChooseAnotherPayment.setOnClickListener(v -> {
                ((PaymentActivity)getActivity()).loadFragment(new PaymentFragment().newInstance("",""));
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}