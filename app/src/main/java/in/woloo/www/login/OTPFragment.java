package in.woloo.www.login;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.SelectGender.SelectGenderActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.login.models.OTPResponse;
import in.woloo.www.login.models.UpdateTokenResponse;
import in.woloo.www.login.receiver.OTP_Receiver;
import in.woloo.www.login.receiver.OtpReceivedInterface;
import in.woloo.www.login.receiver.SmsBroadcastReceiver;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.splash.PendingReviewStatusResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends Fragment implements TextWatcher, LoginView, GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.editTextOne)
    EditText editTextOne;
    @BindView(R.id.editTextTwo)
    EditText editTextTwo;
    @BindView(R.id.editTextThree)
    EditText editTextThree;
    @BindView(R.id.editTextFour)
    EditText editTextFour;
    @BindView(R.id.txtProceed)
    TextView txtProceed;

    @BindView(R.id.tv_message)
    TextView tv_message;

    @BindView(R.id.timer)
    TextView timer;

    @BindView(R.id.tv_notyou)
    TextView tv_notyou;

    @BindView(R.id.rlParentLayout)
    RelativeLayout rlParentLayout;

    @BindView(R.id.tv_resendcode)
    TextView tv_resendcode;

    private CommonUtils mCommonUtils;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mobileNumber;
    private String otp;
    private LoginPresenter mLoginPresenter;
    private SharedPreference mSharedPreference;
    private Dialog mGetStartDialog;
    private InputMethodManager imgr;

    private SmsBroadcastReceiver mSmsBroadcastReceiver;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "OTPFragment";
    int resendCount = 0;

    public OTPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OTPFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPFragment newInstance(String param1, String param2) {
        OTPFragment fragment = new OTPFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*Calling on create*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_PARAM1);
            otp = getArguments().getString(ARG_PARAM2);
        }
    }

    /*Calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_otp, container, false);
        ButterKnife.bind(this, root);
        initViews();
        requestsmspermission();
        return root;
    }

    /*requestsmspermission for receiving sms*/
    private void requestsmspermission() {
        Logger.i(TAG, "requestsmspermission");
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(getActivity(), smspermission);
        //check if read SMS permission is granted or not
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = smspermission;
            ActivityCompat.requestPermissions(getActivity(), permission_list, 1);
        }
    }

    /*enableSMSReceiver*/
    public void enableSMSReceiver(Context context) {
        Logger.i(TAG, "enableSMSReceiver");
        ComponentName component = new ComponentName(context, OTP_Receiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                component,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /*disableSMSReceiver*/
    public static void disableSMSReceiver(Context context) {
        Logger.i(TAG, "disableSMSReceiver");
        ComponentName component = new ComponentName(context, OTP_Receiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                component,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.i(TAG, "onStop");
    }

    public void loadFragment(Fragment fragment) {
        Logger.i(TAG, "loadFragment");
        try {
            FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment);
            fragmentTransaction.commit();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling initview*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            countDownTimer.start();
            try {
                imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                editTextOne.requestFocus();
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            mLoginPresenter = new LoginPresenter(getContext(), OTPFragment.this);
            mSharedPreference = new SharedPreference(getContext());
            tv_resendcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resendCount++;
                    if (resendCount > 2) {
                        showResendOTPDialog();
                    }
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        editTextOne.setText("");
                        editTextTwo.setText("");
                        editTextThree.setText("");
                        editTextFour.setText("");
                        String referral_code = mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.REFERRAL_CODE.getPreferenceKey());
                        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(getContext());
                        ArrayList<String> appSignature = appSignatureHelper.getAppSignatures();
                        mLoginPresenter.sendOtpAPI(getActivity(), mobileNumber, referral_code, appSignature.get(0));
                    }
                }
            });

            tv_message.setText(getResources().getString(R.string.otp_message) + " " + mobileNumber);

            editTextOne.addTextChangedListener(this);
            editTextTwo.addTextChangedListener(this);
            editTextThree.addTextChangedListener(this);
            editTextFour.addTextChangedListener(this);

            txtProceed.setOnClickListener(v -> {
                Utility.hideKeyboard(getActivity());
                try {
                    callLoginAPI();
                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }
            });
            editTextOne.setOnEditorActionListener((v, actionId, event) -> {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    callLoginAPI();
                }
                return false;
            });
            editTextTwo.setOnEditorActionListener((v, actionId, event) -> {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    callLoginAPI();
                }
                return false;
            });
            editTextThree.setOnEditorActionListener((v, actionId, event) -> {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    callLoginAPI();
                }
                return false;
            });
            editTextFour.setOnEditorActionListener((v, actionId, event) -> {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    callLoginAPI();
                }
                return false;
            });

            editTextFour.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 67 && TextUtils.isEmpty(editTextFour.getText())) {
                        editTextThree.requestFocus();
                    }
                    return false;
                }
            });

            editTextThree.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 67 && TextUtils.isEmpty(editTextThree.getText())) {
                        editTextTwo.requestFocus();
                    }
                    return false;
                }
            });

            editTextTwo.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 67 && TextUtils.isEmpty(editTextTwo.getText())) {
                        editTextOne.requestFocus();
                    }
                    return false;
                }
            });

            tv_notyou.setOnClickListener(v -> {
                try {
                    try {
                        imgr.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                    ((LoginActivity) getActivity()).loadFragment(new LoginFragment().newInstance("", ""));
                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }
            });


            mSmsBroadcastReceiver = new SmsBroadcastReceiver();

            //set google api client for hint request
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(getActivity(), this)
                   // .addApi(Auth.CREDENTIALS_API)
                    .build();

            mSmsBroadcastReceiver.setOnOtpListeners(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            getContext().registerReceiver(mSmsBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);

            startSMSListener();

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void showResendOTPDialog() {
        Logger.i(TAG, "showFreeTrialDialog");
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_otp_not_received);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView llStartFreeTrial = dialog.findViewById(R.id.btnCloseDialog);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    public void callLoginAPI() {
        Logger.i(TAG, "callLoginAPI");
        try {
            String password = editTextOne.getText().toString() + editTextTwo.getText().toString() + editTextThree.getText().toString() + editTextFour.getText().toString();
            if (isEnteredOTP()) {
                //if (password.trim().equals(otp)) {
                    mLoginPresenter.loginAPI(mobileNumber, password.trim());
                /*} else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter received otp.", Toast.LENGTH_SHORT).show();
                }*/
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please enter valid otp.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*to check if otp is entered or not in edit text */
    private boolean isEnteredOTP() {
        Logger.i(TAG, "isEnteredOTP");
        boolean isOTPEntered = false;
        try {
            if (!TextUtils.isEmpty(editTextOne.getText().toString()) && !TextUtils.isEmpty(editTextTwo.getText().toString()) && !TextUtils.isEmpty(editTextThree.getText().toString()) && !TextUtils.isEmpty(editTextFour.getText().toString())) {
                isOTPEntered = true;
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        return isOTPEntered;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.toString().length()>=4){
//            txtProceed.setBackground(getResources().getDrawable(R.drawable.yellow_rectangle_shape));
//            txtProceed.setTextColor(getResources().getColor(R.color.black));
//        }else{
//            txtProceed.setBackground(getResources().getDrawable(R.drawable.otp_edit_box));
//            txtProceed.setTextColor(getResources().getColor(R.color.text_color));
//        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            if (editable.length() == 1) {
                if (editTextOne.length() == 1) {
                    editTextTwo.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
                if (editTextTwo.length() == 1) {
                    editTextThree.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
                if (editTextThree.length() == 1) {
                    editTextFour.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }

                if (editTextFour.length() == 1) {
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
            } else if (editable.length() == 0) {
                if (editTextFour.length() == 0) {
                    editTextThree.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
                if (editTextThree.length() == 0) {
                    editTextTwo.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
                if (editTextTwo.length() == 0) {
                    editTextOne.requestFocus();
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }

                if (editTextOne.length() == 0) {
                    if (isEnteredOTP()) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void enableSubmitButton() {
        try {
            txtProceed.setBackground(getResources().getDrawable(R.drawable.yellow_rectangle_shape));
            txtProceed.setTextColor(getResources().getColor(R.color.black));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    public void disableSubmitButton() {
        try {
            txtProceed.setBackground(getResources().getDrawable(R.drawable.otp_edit_box));
            txtProceed.setTextColor(getResources().getColor(R.color.text_color));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    @Override
    public void sendOtpSuccessFlow(OTPResponse otpResponse) {
        Logger.i(TAG, "sendOtpSuccessFlow");
        countDownTimer.start();
        if (!TextUtils.isEmpty(mobileNumber) && isValidEmail(mobileNumber)) {
            Toast.makeText(getActivity().getApplicationContext(), "OTP is successfully sent to registered email address.", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(mobileNumber) && isValidMobile(mobileNumber)) {
            Toast.makeText(getActivity().getApplicationContext(), "Otp has been sent on your registered mobile number!", Toast.LENGTH_SHORT).show();
        }
        otp = String.valueOf(otpResponse.getUserData());
        tv_resendcode.setVisibility(View.GONE);
    }

    private boolean isValidMobile(String phone) {
        Logger.i(TAG, "isValidMobile");
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        Logger.i(TAG, "isValidMobile");
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*to check for the login success*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void loginSuccessFlow(LoginResponse loginResponse) {
        Logger.i(TAG, "loginSuccessFlow");
        try {
            try {
                if (loginResponse != null && loginResponse.getData() != null && !TextUtils.isEmpty(loginResponse.getData().getMessage())) {
                    Toast.makeText(getActivity().getApplicationContext(), loginResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }

            if (loginResponse != null && loginResponse.getData() != null && loginResponse.getData().getStatus().equalsIgnoreCase(AppConstants.API_SUCCESS)) {
                mSharedPreference.setStoredBooleanPreference(getContext(), SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey(), true);
                String userInfo = new Gson().toJson(loginResponse);
                mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.USER_INFO.getPreferenceKey(), userInfo);
                if (loginResponse.getData().getUser() != null && loginResponse.getData().getUser().getIs_first_session() != null && loginResponse.getData().getUser().getIs_first_session() == 1) {
                    showFreeTrialDialog(loginResponse);
                } else {
                    sendDeviceToken(getContext());
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*to check for the token is updated successfully or not*/
    @Override
    public void tokenUpdateSuccess(UpdateTokenResponse updateTokenResponse) {
        Logger.i(TAG, "tokenUpdateSuccess");
        mLoginPresenter.getReviewPendingStatus();
    }

    /*to check for the authConfigSuccess*/
    @Override
    public void authConfigSuccess(AuthConfigResponse authConfigResponse) {
        Logger.i(TAG, "authConfigSuccess");
    }

    /*to check for the pendingReviewStatusResponse*/
    @Override
    public void pendingReviewStatusResponse(PendingReviewStatusResponse pendingReviewStatusResponse) {
        Logger.i(TAG, "pendingReviewStatusResponse");
        try {
            if (pendingReviewStatusResponse != null) {
                if (pendingReviewStatusResponse.getCode() != null && pendingReviewStatusResponse.getCode().equals(200)) {
                    moveToAddReview(pendingReviewStatusResponse.getData().getWolooId());
                } else {
                    moveToDashboard();
                }
            } else {
                moveToDashboard();
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onInvalidOTP() {
        Toast.makeText(getActivity().getApplicationContext(), "Please enter received OTP.", Toast.LENGTH_SHORT).show();
    }

    /*to check for the sending DeviceToken */
    private void sendDeviceToken(Context mContext) {
        Logger.i(TAG, "sendDeviceToken");
        try {
            mLoginPresenter.updateDeviceTokenAPI(new CommonUtils().getDeviceId(mContext), new CommonUtils().getDeviceToken(mContext));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*to check for the moveToAddReview */
    public void moveToAddReview(int wolooId) {
        Logger.i(TAG, "moveToAddReview");
        Intent intent = new Intent(getContext(), AddReviewActivity.class);
        intent.putExtra(AppConstants.WOLOO_ID, wolooId);
        startActivity(intent);
        getActivity().finish();
    }

    /*to check for the moveToDashboard */
    public void moveToDashboard() {
        Logger.i(TAG, "moveToDashboard");
        LoginResponse userInfo = new CommonUtils().getUserInfo(getContext());

        if (TextUtils.isEmpty(userInfo.getData().getUser().getGender())) {
            requireActivity().startActivity(new Intent(requireActivity(), SelectGenderActivity.class));
            requireActivity().finish();
        } else {
            try {
                getActivity().startActivity(new Intent(getActivity(), WolooDashboard.class).putExtra("valuepassed", "showdialog"));
                getActivity().finish();
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }
    }


    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText(millisecondsToTime(millisUntilFinished) + "Sec");
            tv_resendcode.setVisibility(View.GONE);
//
        }

        @Override
        public void onFinish() {
            timer.setText("");
            tv_resendcode.setVisibility(View.VISIBLE);

//            otpTextView.setOTP("");
//
//            tv_resend.setEnabled(true);
//            tv_resend.setClickable(true);
//            tv_resend.setFocusable(true);
//            tv_resend.setFocusableInTouchMode(true);
//            tv_resend.setTextColor(Color.parseColor("#003399"));
        }
    };

    /*calling millisecondsToTime*/
    private String millisecondsToTime(long milliseconds) {
        Logger.i(TAG, "millisecondsToTime");
        return "" + String.format("%d ",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    /*calling onDestroyView*/
    @Override
    public void onDestroyView() {
        Logger.i(TAG, "onDestroyView");
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
        super.onDestroy();
        super.onDestroyView();
    }

    /*calling showFreeTrialDialog*/
    public void showFreeTrialDialog(LoginResponse loginResponse) {
        Logger.i(TAG, "showFreeTrialDialog");
        try {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_start_free_trial);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tv_startfreetrial = (TextView) dialog.findViewById(R.id.tv_startfreetrial);
            ImageView ivFreeTrial = (ImageView) dialog.findViewById(R.id.ivFreeTrial);
            try {
                AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
                ImageUtil.loadImage(getContext(), ivFreeTrial, authConfigResponse.getuRLS().getFree_trial_image_url());
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE().getFreeTrialDialogText();
                freeTrialDialogText = freeTrialDialogText.replaceAll("\\\\n", "\n");
                tv_startfreetrial.setText(decode(freeTrialDialogText));
            }

            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llStartFreeTrial);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sendDeviceToken(getContext());
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    private String decode(String text) {
        return text.replace("&amp;", "&");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*calling onOtpReceived*/
    @Override
    public void onOtpReceived(String otp) {
        Logger.i(TAG, "onOtpReceived");
        try {
            editTextOne.setText(otp.substring(0, 1));
            editTextTwo.setText(otp.substring(1, 2));
            editTextThree.setText(otp.substring(2, 3));
            editTextFour.setText(otp.substring(3, 4));

            txtProceed.performClick();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling onOtpTimeout*/
    @Override
    public void onOtpTimeout() {
        try {
            Logger.i(TAG, "onOtpTimeout");
            Toast.makeText(requireContext().getApplicationContext(), "Time out, please resend", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }

    /*calling startSMSListener*/
    public void startSMSListener() {
        Logger.i(TAG, "startSMSListener");
        SmsRetrieverClient mClient = SmsRetriever.getClient(getContext());
        Task<Void> mTask = mClient.startSmsRetriever();
        // Task<Void> mTask =  SmsRetriever.getClient(getContext()).startSmsUserConsent(mobileNumber /* or null */);
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

}