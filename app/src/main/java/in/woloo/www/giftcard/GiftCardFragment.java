package in.woloo.www.giftcard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.giftcard.model.RequestPointsResponse;
import in.woloo.www.giftcard.mvp.GiftCardPresenter;
import in.woloo.www.giftcard.mvp.GiftCardView;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.base.BaseFragment;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.giftcard.model.AddCoinsRequest;
import in.woloo.www.v2.giftcard.model.AddCoinsResponse;
import in.woloo.www.v2.giftcard.model.UserCoins;
import in.woloo.www.v2.giftcard.viewmodel.CoinsViewModel;
import in.woloo.www.v2.splash.UserDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftCardFragment extends BaseFragment implements Serializable {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvHundred)
    TextView tvHundred;
    @BindView(R.id.tvFiveHundred)
    TextView tvFiveHundred;
    @BindView(R.id.tvThousand)
    TextView tvThousand;

    @BindView(R.id.etAmount)
    EditText etAmount;

    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.etMessage)
    EditText etMessage;

    @BindView(R.id.llsendButton)
    LinearLayout llsendButton;

    @BindView(R.id.giftAmountLayout)
    LinearLayout giftAmountLayout;

    private CommonUtils commonUtils;

    private TextView lastSelectedAmount = null;
    String Email; boolean isEmail;
    String mobile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters

    private GiftCardPresenter giftCardPresenter;
    private CoinsViewModel coinsViewModel;
    private UserCoins userCoinsResponse;
    public static String TAG= GiftCardFragment.class.getSimpleName();

    public GiftCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Email Parameter 1.
     * @param isEmail
     * @param mobile Parameter 2.
     * @return A new instance of fragment GiftCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftCardFragment newInstance(String Email, boolean isEmail, String mobile) {
        GiftCardFragment fragment = new GiftCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Email);
        args.putBoolean(ARG_PARAM3, isEmail);
        args.putString(ARG_PARAM2, mobile);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Email = getArguments().getString(ARG_PARAM1);
            mobile = getArguments().getString(ARG_PARAM2);
            isEmail = getArguments().getBoolean(ARG_PARAM3);
        }
        Logger.i(TAG, "onCreate");
    }

    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gift_card, container, false);
        ButterKnife.bind(this,root);
        Logger.i(TAG, "onCreateView");
        initViews();
        return root;
    }
    /*calling initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        try{
            Logger.i(TAG, "initViews");
            coinsViewModel = new ViewModelProvider(this).get(CoinsViewModel.class);
            coinsViewModel.getUserCoins();
            setLiveData();
            setNetworkDetector();
            tvTitle.setText(getString(R.string.woloo_gift_card));
            commonUtils = new CommonUtils();
            ivBack.setOnClickListener(v -> {
                // getActivity().onBackPressed();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
            });

            etMessage.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (etMessage.hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                    return false;
                }
            });

            etMobileNumber.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (etMessage.hasFocus()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_SCROLL:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                return true;
                        }
                    }
                    return false;
                }
            });

            tvHundred.setOnClickListener(v -> {
                Logger.w(TAG,"selected 100");
                selectTheAmount(100);
            });

            tvFiveHundred.setOnClickListener(v -> {
                selectTheAmount(500);
            });

            tvThousand.setOnClickListener(v -> {
                selectTheAmount(1000);
            });

            llsendButton.setOnClickListener(v -> {
                         /*if(isValidate()){
                             giftCardPresenter.sendGiftCard(etAmount.getText().toString().trim(),etMobileNumber.getText().toString().trim(),etMessage.getText().toString().trim());
                         }*/
                if(isValidate()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.GIFT_CARD_AMOUNT, etAmount.getText().toString().trim());
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.GIFT_AMOUNT_SELECTED);

                    HashMap<String,Object> payload = new HashMap<>();
                    payload.put(AppConstants.GIFT_CARD_AMOUNT, etAmount.getText().toString().trim());
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.GIFT_AMOUNT_SELECTED);

                    //                     giftCardPresenter.RequestPoints(etAmount.getText().toString().trim(), etMobileNumber.getText().toString().trim(), etMessage.getText().toString().trim());
                    AddCoinsRequest request = new AddCoinsRequest();
                    try {
                        request.setCoins(Integer.parseInt(etAmount.getText().toString().trim()));
                    }catch(Exception e){
                        Toast.makeText(getContext(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                    }
                    request.setMobile(etMobileNumber.getText().toString().trim());
                    request.setMessage(etMessage.getText().toString().trim());
                    coinsViewModel.addCoins(request);
                    //etAmount.getText().toString().trim(), etMobileNumber.getText().toString().trim(), etMessage.getText().toString().trim());
                }
            });

            etAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(lastSelectedAmount != null){
                        lastSelectedAmount.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_not_selected));
                    }
                    switch (etAmount.getText().toString()) {
                        case "100":
                            tvHundred.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                            lastSelectedAmount = tvHundred;
                            break;
                        case "500":
                            tvFiveHundred.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                            lastSelectedAmount = tvFiveHundred;
                            break;
                        case "1000":
                            tvThousand.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                            lastSelectedAmount = tvThousand;
                            break;
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public void setLiveData(){
        coinsViewModel.observeAddCoins().observe(getViewLifecycleOwner(), new Observer<BaseResponse<AddCoinsResponse>>() {
            @Override
            public void onChanged(BaseResponse<AddCoinsResponse> addCoinsResponseBaseResponse) {
                if (addCoinsResponseBaseResponse != null && addCoinsResponseBaseResponse.getData() != null) {
                    String orderId = addCoinsResponseBaseResponse.getData().getOrderId();
                    if (!TextUtils.isEmpty(orderId)) {
                        //                    CommonUtils.showCustomDialog(getActivity(),"We got some response\n\n"+orderId);
                        CommonUtils.navigateToRazorPayFlow(getContext(), "", orderId, Email, isEmail, mobile, false, null, false, false);
                    /*selectTheAmount(0);
                    etAmount.setText("");
                    etMobileNumber.setText("");
                    etMessage.setText("");*/
                    } else {
                        //                        CommonUtils.showCustomDialog(getActivity(),);
                        CommonUtils.showCustomDialog(getActivity(), "Some error occured");
                    }
                }
                else {
                    //                        displayToast(WolooApplication.getErrorMessage());
                    CommonUtils.showCustomDialog(getActivity(),WolooApplication.getErrorMessage());
                    WolooApplication.setErrorMessage("");
                }

            }
        });

        coinsViewModel.observeUserCoins().observe(getViewLifecycleOwner(), userCoinsResponse -> {
            if (userCoinsResponse != null && userCoinsResponse.getData() != null) {
                this.userCoinsResponse = userCoinsResponse.getData();
            }
            else {
                //                        displayToast(WolooApplication.getErrorMessage());
                CommonUtils.showCustomDialog(getActivity(),WolooApplication.getErrorMessage());
                WolooApplication.setErrorMessage("");
            }
        });
    }

    /*calling isValidate*/
    private boolean isValidate() {
        try{
            Logger.i(TAG, "isValidate");
            String mobileNumber = etMobileNumber.getText().toString().trim();
            if(TextUtils.isEmpty(etAmount.getText().toString().trim())){
                Toast.makeText(getActivity().getApplicationContext(),"Please enter points",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(TextUtils.isEmpty(mobileNumber)){
                Toast.makeText(getActivity().getApplicationContext(),"Please enter mobile number",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(TextUtils.isEmpty(etMessage.getText().toString().trim())){
                Toast.makeText(getActivity().getApplicationContext(),"Please enter message",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!commonUtils.isValidMobileNumber(mobileNumber)){
                Toast.makeText(getActivity().getApplicationContext(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                return false;
            }
            //LoginResponse userInfo = new CommonUtils().getUserInfo(getActivity());
            UserDetails userInfo = new CommonUtils().getUserInfo();
            if(mobileNumber.equals(userInfo.getMobile())){
                Toast.makeText(getActivity().getApplicationContext(),"You can not send Gift to YourSelf",Toast.LENGTH_SHORT).show();
                return false;
            }
                    /*if(userCoinsResponse != null && userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null && userCoinsResponse.getData().getTotalCoins() < Integer.parseInt(etAmount.getText().toString().trim())){
                        //Toast.makeText(getActivity().getApplicationContext(),"You don't have enough points",Toast.LENGTH_SHORT).show();
                        return false;
                    }
        */
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return true;
    }

    /*calling selectTheAmount*/
    private void selectTheAmount(int rupees) {
        try{
            Logger.i(TAG, "selectTheAmount");
            if(lastSelectedAmount != null){
                lastSelectedAmount.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_not_selected));
            }
            if(rupees == 100){
                etAmount.setText("100");
                tvHundred.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                lastSelectedAmount = tvHundred;
            }else if(rupees == 500){
                etAmount.setText("500");
                tvFiveHundred.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                lastSelectedAmount = tvFiveHundred;
            }else if(rupees == 1000) {
                etAmount.setText("1000");
                tvThousand.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_rupees_selected));
                lastSelectedAmount = tvThousand;
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        selectTheAmount(0);
        etAmount.setText("");
        etMobileNumber.setText("");
        etMessage.setText("");
    }

}
