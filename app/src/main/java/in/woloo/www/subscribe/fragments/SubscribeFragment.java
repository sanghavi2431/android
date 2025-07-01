package in.woloo.www.subscribe.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.cancelsubscription.model.CancelSubscriptionResponse;
import in.woloo.www.cancelsubscription.mvp.CancelSubscriptionPresenter;
import in.woloo.www.cancelsubscription.mvp.CancelSubscriptionView;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.adapter.SubscribeAdapter;
import in.woloo.www.subscribe.models.GetSubscriptionDetailsResponse;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.subscribe.models.PlanResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.subscription.model.SubmitSubscriptionPurchaseRequest;
import in.woloo.www.v2.subscription.viewmodel.SubscriptionViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscribeFragment extends Fragment implements CancelSubscriptionView {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.recyclerView_subscribe)
    RecyclerView recyclerView_subscribe;

    @BindView(R.id.tv_gift_points)
    TextView tvGiftPoints;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";

    // TODO: Rename and change types of parameters
    private String currentSubscriptionPlan = "", purchasedBy = "";
    private String futureSubscriptionPlan = "";
    private String msg;
    ArrayList<PlanResponse.Data> subscriptionArrayList;
    SubscriptionViewModel subscriptionViewModel;
    ProfileViewModel profileViewModel;
    private boolean isEmail = false;
    private String mobile, expireDate;
    boolean isForSubscription;
    CancelSubscriptionPresenter cancelSubscriptionPresenter;
    boolean isCancelSubscription;
    ArrayList<PlanResponse.Data> selectedSubscriptionArrayList = new ArrayList<>();
    private SubscribeAdapter adapter;
    private UserProfile viewProfileResponse;
    String subscriptionId="";

    public SubscribeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param isEmail
     * @param mobile
     * @param isForSubscription
     * @param end_at
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscribeFragment newInstance(String param1, String param2, boolean isEmail, String mobile, boolean isForSubscription, boolean isCancelSubscription, String end_at) {
        SubscribeFragment fragment = new SubscribeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM7, end_at);
        args.putBoolean(ARG_PARAM3, isEmail);
        args.putString(ARG_PARAM4, mobile);
        args.putBoolean(ARG_PARAM5, isForSubscription);
        args.putBoolean(ARG_PARAM6, isCancelSubscription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentSubscriptionPlan = getArguments().getString(ARG_PARAM1);
            msg = getArguments().getString(ARG_PARAM2);
            isEmail = getArguments().getBoolean(ARG_PARAM3);
            isForSubscription = getArguments().getBoolean(ARG_PARAM5);
            isCancelSubscription = getArguments().getBoolean(ARG_PARAM6);
            mobile =  getArguments().getString(ARG_PARAM4);
            expireDate  =  getArguments().getString(ARG_PARAM7);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        subscriptionViewModel.getMySubscription();
//        subscribePresenter.getMySubscriptionList();
        //initViews();
    }

    private void setLiveData() {
        subscriptionViewModel.observeMySubscription().observe(getViewLifecycleOwner(), new Observer<BaseResponse<GetSubscriptionDetailsResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<GetSubscriptionDetailsResponse.Data> it) {
                if (it != null) {
                    if (Objects.requireNonNull(it.getData()).getActiveSubscription() != null) {
                        if (!CommonUtils.isSubscriptionExpired(expireDate))
                            currentSubscriptionPlan = it.getData().getActiveSubscription().getPlanId();
                        if (it.getData().getPurchase_by() != null)
                            purchasedBy = it.getData().getPurchase_by();
                    }
                    else {
                        currentSubscriptionPlan = "";
                        purchasedBy = "";
                    }
                    if (it.getData().getFutureSubscription() != null && !it.getData().getFutureSubscription().isEmpty()) {
                        futureSubscriptionPlan = it.getData().getFutureSubscription().get(0).getPlanId();
                    }
                    else {
                        futureSubscriptionPlan = "";
                    }
                    initViews();
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.setErrorMessage("");
                }
                initViews();

            }
        });
        subscriptionViewModel.observeSubscriptionPlans().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ArrayList<PlanResponse.Data>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<PlanResponse.Data>> it) {
                if (it != null) {
                    initSubscriptionPlans(it.getData());
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        subscriptionViewModel.observeSubscriptionSelected().observe(getViewLifecycleOwner(), new Observer<PlanResponse.Data>() {
            @Override
            public void onChanged(PlanResponse.Data plan) {
                if (plan != null) {
                    try {
                        if (Integer.parseInt(plan.getPrice()) <= viewProfileResponse.getTotalCoins().getGiftCoins()) {
                            showBottomSheetDialog();
                        } else {
                            subscriptionViewModel.initSubscriptionByOrder(subscriptionViewModel.getInitSubscriptionRequest());
                        }
//                        Toast.makeText(getContext(), "Price " + plan.getPrice() + "\nPlan Name "+plan.getName(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                         CommonUtils.printStackTrace(e);
                    }
                }
            }
        });

        subscriptionViewModel.observeInitSubscriptionByOrder().observe(getViewLifecycleOwner(), new Observer<BaseResponse<InitSubscriptionResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<InitSubscriptionResponse.Data> initSubscriptionResponse) {
                if (initSubscriptionResponse != null) {
                    try {
                        if (initSubscriptionResponse.getData() != null) {
                            subscriptionId = initSubscriptionResponse.getData().getSubscriptionId();
                            if (!TextUtils.isEmpty(subscriptionId)) {
                                CommonUtils.navigateToRazorPayFlow(getContext(), subscriptionViewModel.getInitSubscriptionRequest().getPlanId(), subscriptionId, msg, isEmail, mobile, true, null,subscriptionViewModel.getInitSubscriptionRequest().getFuture() , false);
                            } else {
                                //showErrorDialog();
                                showdialog(initSubscriptionResponse.getMessage());
                                Logger.e("REponse if else", initSubscriptionResponse.getData().toString());
                            }
                        } else {
                            showdialog(initSubscriptionResponse != null ? initSubscriptionResponse.getMessage() : null);
                            Logger.e("REponse else", initSubscriptionResponse.getData().toString());

                        }
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                } else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        subscriptionViewModel.observeSubmitSubscriptionPurchaseResponse().observe(getViewLifecycleOwner(), new Observer<BaseResponse<InitSubscriptionResponse>>() {
            @Override
            public void onChanged(BaseResponse<InitSubscriptionResponse> it) {
                try{
                    Logger.i("SubscibeFragment", "observeSubmitSubscriptionPurchaseResponse");
                    if (it != null && it.getSuccess()) {
                        profileViewModel.getUserProfile();
                        showPaymentSuccessDialog();
                    }else{
                        if(!WolooApplication.getErrorMessage().isEmpty())
                            Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        WolooApplication.setErrorMessage("");
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            }
        });

        profileViewModel.observeUserProfile().observe(getActivity(), new Observer<BaseResponse<UserProfile>>() {
            @Override
            public void onChanged(BaseResponse<UserProfile> it) {

                try{
                    Logger.i("SubscibeFragment", "observeUserProfile");
                    if (it != null && it.getSuccess()) {
                        viewProfileResponse = it.getData();
                        if(adapter != null){
                            adapter.setViewProfile(viewProfileResponse);
                            setPlanResults();
                        }
                        tvGiftPoints.setText("Gift Voucher : "+viewProfileResponse.getTotalCoins().getGiftCoins());
                    }else{
                        if(!WolooApplication.getErrorMessage().isEmpty())
                            Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        WolooApplication.setErrorMessage("");
                    }
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            }
        });
    }

    public void showPaymentSuccessDialog(){
        try {

            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_payment_success);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_paymentsuccess = (TextView) dialog.findViewById(R.id.tv_paymentsuccess);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String paymentSuccessDialogText = authConfigResponse.getcUSTOMMESSAGE().getPaymentSuccessDialogText();
                tv_paymentsuccess.setText(paymentSuccessDialogText.replaceAll("\\\\n","\n"));
            }

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
//                    FragmentManager fm = requireActivity().getSupportFragmentManager();
//                    fm.popBackStack();
                }
            });
            dialog.show();
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    void initSubscriptionPlans(List<PlanResponse.Data> planList){
        try{

                selectedSubscriptionArrayList = new ArrayList<>();
                subscriptionArrayList = new ArrayList<>();
                selectedSubscriptionArrayList.addAll(planList);
                if (currentSubscriptionPlan != null) {
                    for (int i = 0; i < selectedSubscriptionArrayList.size(); i++) {
                        if (currentSubscriptionPlan.equals(selectedSubscriptionArrayList.get(i).getPlanId())) {
                            subscriptionArrayList.add(0, selectedSubscriptionArrayList.get(i));
                            selectedSubscriptionArrayList.remove(i);
                        }
                    }
                }
                if (futureSubscriptionPlan != null) {
                    for (int i = 0; i < selectedSubscriptionArrayList.size(); i++) {
                        if (futureSubscriptionPlan.equals(selectedSubscriptionArrayList.get(i).getPlanId())) {
                            if (subscriptionArrayList.size() < 1)
                                subscriptionArrayList.add(0, selectedSubscriptionArrayList.get(i));
                            else
                                subscriptionArrayList.add(1, selectedSubscriptionArrayList.get(i));
                            selectedSubscriptionArrayList.remove(i);
                        }
                    }
                }
                subscriptionArrayList.addAll(selectedSubscriptionArrayList);
                setPlanResults();

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_subscribe, container, false);
        ButterKnife.bind(this, root);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        subscriptionViewModel.getMySubscription();
        profileViewModel.getUserProfile();
        setLiveData();
        //initViews();
        return root;
    }

    private void initViews() {
        subscriptionArrayList = new ArrayList<PlanResponse.Data>();
        try {
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            subscriptionViewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
            cancelSubscriptionPresenter = new CancelSubscriptionPresenter(getContext(), SubscribeFragment.this);
//            subscribePresenter.getSubscriptionPlans();
            subscriptionViewModel.getSubscriptionPlans();
//            setSearchResults();

            tvTitle.setText(getResources().getString(R.string.woloo_premium));
            ivBack.setOnClickListener(v -> {
                //getActivity().onBackPressed();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
            });
            if (!isForSubscription) {
                if (isCancelSubscription) {
                    showdialog("You have already Unsubscribed the Membership");
                } else {
                    showCancelSubscriptionDialog();
                }
                isForSubscription = true;
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void setPlanResults() {
        try{
            adapter = new SubscribeAdapter(getContext(),subscriptionArrayList,subscriptionViewModel,viewProfileResponse,currentSubscriptionPlan,futureSubscriptionPlan,msg,isEmail,mobile,purchasedBy);
            recyclerView_subscribe.setHasFixedSize(true);
            recyclerView_subscribe.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_subscribe.setAdapter(adapter);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void cancelSubscriptionResponse(CancelSubscriptionResponse cancelSubscriptionResponse) {
        showdialog("Membership Cancelled Successfully");
        //initViews();
    }

    public void showCancelSubscriptionDialog() {
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_discontinue_subscription);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView discontinueTextTv = dialog.findViewById(R.id.discontinueTextTv);
            discontinueTextTv.setText("Even if you choose to cancel your current membership,you can still use the app benefits until the end of current billing period.");
            TextView llCancelBtn = dialog.findViewById(R.id.llCancelBtn);
            llCancelBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });
            TextView llStartFreeTrial = dialog.findViewById(R.id.llOkayBtn);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getReasonPopup();
                }
            });
            dialog.show();

        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getReasonPopup() {
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_cancel_subscription_reason);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            EditText etRemark = dialog.findViewById(R.id.etRemark);
            etRemark.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (etRemark.hasFocus()) {
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
            Spinner spinner = (Spinner) dialog.findViewById(R.id.cancel_reason_spinner);
            SharedPreference mSharedPref = new SharedPreference(getContext());
            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, authConfigResponse.getcUSTOMMESSAGE().getCancelSubscriptionReasons().split(","));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            TextView llCancelBtn = dialog.findViewById(R.id.llCancelBtn);
            llCancelBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });
            TextView llOkayBtn = dialog.findViewById(R.id.llOkayBtn);
            llOkayBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(etRemark.getText().toString().trim().equals(""))
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Add Comment", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        cancelSubscriptionPresenter.cancelSubscription(authConfigResponse.getcUSTOMMESSAGE().getCancelSubscriptionReasons().split(",")[spinner.getSelectedItemPosition()],
                                etRemark.getText().toString().trim(),
                                authConfigResponse.getSupportEmail());
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling on showdialog*/
    public void showdialog(String msg) {
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_login_failure);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(msg);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottomsheet_payment_options);
        RadioButton giftcard = bottomSheetDialog.findViewById(R.id.radio_gift);
        RadioButton razorpay = bottomSheetDialog.findViewById(R.id.radio_razorpay);
        TextView tvGiftPoints = bottomSheetDialog.findViewById(R.id.tv_gift_points);
        View giftLayout = bottomSheetDialog.findViewById(R.id.gift_layout);
        View razorpayLayout = bottomSheetDialog.findViewById(R.id.razorpay_layout);
        TextView pay = bottomSheetDialog.findViewById(R.id.btn_pay);

        giftcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!giftcard.isChecked()){
                    giftcard.setChecked(true);
                }
                razorpay.setChecked(false);
            }
        });
        giftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!giftcard.isChecked()){
                    giftcard.setChecked(true);
                }
                razorpay.setChecked(false);
            }
        });

        razorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!razorpay.isChecked()){
                    razorpay.setChecked(true);
                }
                giftcard.setChecked(false);
            }
        });
        razorpayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!razorpay.isChecked()){
                    razorpay.setChecked(true);
                }
                giftcard.setChecked(false);
            }
        });

       tvGiftPoints.setText("\u20B9" + viewProfileResponse.getTotalCoins().getGiftCoins());

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!giftcard.isChecked() && !razorpay.isChecked()) {
                    Toast.makeText(getContext(), "Please select any one payment options", Toast.LENGTH_SHORT).show();
                } else if(giftcard.isChecked()) {
                    //giftcard
                    SubmitSubscriptionPurchaseRequest request = new SubmitSubscriptionPurchaseRequest();
                    request.setPlanId(subscriptionViewModel.getInitSubscriptionRequest().getPlanId());
                    request.setUserGiftPoints(true);
                    subscriptionViewModel.submitSubscriptionPurchase(request);
                } else if(razorpay.isChecked()) {
                    subscriptionViewModel.initSubscriptionByOrder(subscriptionViewModel.getInitSubscriptionRequest());
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();
    }
}