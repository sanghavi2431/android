package in.woloo.www.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.installations.FirebaseInstallations;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.login.models.OTPResponse;
import in.woloo.www.login.models.UpdateTokenResponse;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.splash.PendingReviewStatusResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import studios.codelight.smartloginlibrary.LoginType;
import studios.codelight.smartloginlibrary.SmartLogin;
import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
import studios.codelight.smartloginlibrary.SmartLoginConfig;
import studios.codelight.smartloginlibrary.SmartLoginFactory;
import studios.codelight.smartloginlibrary.UserSessionManager;
import studios.codelight.smartloginlibrary.users.SmartFacebookUser;
import studios.codelight.smartloginlibrary.users.SmartGoogleUser;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoginView, SmartLoginCallbacks {

    @BindView(R.id.txtSkip)
    TextView txtSkip;

    @BindView(R.id.etEmailMobile)
    EditText etEmailMobile;

    @BindView(R.id.txtSendOtp)
    TextView txtSendOtp;

    @BindView(R.id.logout_button)
    Button logout_button;

    @BindView(R.id.ll_facebooklogin)
    LinearLayout ll_facebooklogin;

    @BindView(R.id.ll_gmaillogin)
    LinearLayout ll_gmaillogin;

    @BindView(R.id.ll_sociallogin)
    LinearLayout ll_sociallogin;

    @BindView(R.id.tv_loginwith)
    TextView tv_loginwith;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoginPresenter mLoginPresenter;
    private CommonUtils mCommonUtils;

    SmartUser currentUser;
    //GoogleApiClient mGoogleApiClient;
    SmartLoginConfig config;
    SmartLogin smartLogin;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private String Name;
    private String FEmail;
    private String id;
    private String token;
    private SharedPreference mSharedPreference;
    private static final String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Logger.i(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
       Logger.i(TAG, "onCreateView");
        ButterKnife.bind(this, root);
        initView();

        config = new SmartLoginConfig(getActivity(), this);
        config.setFacebookAppId(getString(R.string.facebook_app_id));
        config.setFacebookPermissions(null);
        config.setGoogleApiClient(null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                keyhash();
            }
        }, 5000);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) root.findViewById(R.id.loginButton);
//        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Logger.v("LoginActivityUserCoinHistoryModel", response.toString());

                                try {
                                    Name = object.getString("name");

                                    FEmail = object.getString("email");
//                                    id = object.getString("id");
                                    Logger.v("Email = ", " " + FEmail);
                                    Toast.makeText(getActivity().getApplicationContext(), "Name " + Name, Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                      CommonUtils.printStackTrace(e);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                goMainScreen();
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        ll_sociallogin.setVisibility(View.GONE);
        tv_loginwith.setVisibility(View.GONE);

        try {
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String is_socialloginenabled = authConfigResponse.getcUSTOMMESSAGE().getIsSocialLoginEnable().replaceAll("\\\\n", "\n");
                Logger.e("is_socialloginenabled", is_socialloginenabled.toString());
                if (is_socialloginenabled.equalsIgnoreCase("true")) {
                    ll_sociallogin.setVisibility(View.VISIBLE);
                    tv_loginwith.setVisibility(View.VISIBLE);
                } else {
                    ll_sociallogin.setVisibility(View.GONE);
                    tv_loginwith.setVisibility(View.GONE);
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
            return null;
        }


//        currentUser = UserSessionManager.getCurrentUser(getContext());
//        refreshLayout();
        return root;
    }

    /*Getting key Hash */
    private void keyhash() {
       Logger.i(TAG, "keyhash");
        PackageInfo info;
        try {
            info = getContext().getPackageManager().getPackageInfo("in.woloo.www", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Logger.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Logger.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Logger.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Logger.e("exception", e.toString());
        }
    }
/*To Refresh the layout*/
    private void refreshLayout() {
       Logger.i(TAG,"refreshLayout");
        currentUser = UserSessionManager.getCurrentUser(getContext());
        if (currentUser != null) {
            Logger.d("Smart Login", "Logged in user: " + currentUser.toString());
            logout_button.setVisibility(View.VISIBLE);
        } else {
            logout_button.setVisibility(View.GONE);
        }
    }


    /*for Calling back the instance of on Activity Result*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       Logger.i(TAG, "onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

//        if (smartLogin != null) {
//            smartLogin.onActivityResult(requestCode, resultCode, data, config);
//        }
    }

    /*To go on the main Screen*/
    private void goMainScreen() {
       Logger.i(TAG, "MainScreen");
//        Intent intent = new Intent(this, MainActivity.class);
////        intent.putExtra("id",id);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }


/*to check if a mobile number is valid or not */
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /*to check if email is valid or not */
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    /*for initialization of views*/
    private void initView() {
       Logger.i(TAG, "initView");
        try {
            mSharedPreference = new SharedPreference(getContext());
            mLoginPresenter = new LoginPresenter(getContext(), this);
            mLoginPresenter.getAuthConfig();
            mCommonUtils = new CommonUtils();
            txtSkip.setVisibility(View.GONE);
            txtSkip.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), WolooDashboard.class));
                getActivity().finish();
            });

            txtSendOtp.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(etEmailMobile.getText().toString())) {
                    if (TextUtils.isDigitsOnly(etEmailMobile.getText().toString())) {
                        if (mCommonUtils.isValidMobileNumber(etEmailMobile.getText().toString())) {
                            Bundle bundle = new Bundle();
                            Utility.logFirebaseMobileEvent(getActivity(),bundle, AppConstants.MOBILE_OTP,etEmailMobile.getText().toString());
                            mCommonUtils.hideKeyboard(((LoginActivity) getActivity()));
                            String referral_code = mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.REFERRAL_CODE.getPreferenceKey());
                            AppSignatureHelper appSignatureHelper = new AppSignatureHelper(getContext());
                            ArrayList<String> appSignature = appSignatureHelper.getAppSignatures();
                            mLoginPresenter.sendOtpAPI(LoginFragment.this.getContext(), etEmailMobile.getText().toString().trim(), referral_code, appSignature.get(0));
//                            getInstallReferrer(getContext());
                        } else {
                            showLoginFailureDialog("Please enter mobile number !");
                            //Toast.makeText(getActivity(), "Please enter mobile number !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showLoginFailureDialog("Please enter mobile number !");
                        //Toast.makeText(getActivity(), "Please enter mobile number !", Toast.LENGTH_SHORT).show();
                    }
                    /*else {
                        if (isValidEmail(etEmailMobile.getText().toString())) {
                            mCommonUtils.hideKeyboard(((LoginActivity) getActivity()));
                            mLoginPresenter.sendOtpAPI(LoginFragment.this.getContext(), etEmailMobile.getText().toString().trim());
                        } else {
                            Toast.makeText(getActivity(), "Invalid Email Address Entered !", Toast.LENGTH_SHORT).show();
                        }
                    }*/
                }


            });

            ll_facebooklogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                    smartLogin.login(config);

                }
            });

            ll_gmaillogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smartLogin = SmartLoginFactory.build(LoginType.Google);
                    smartLogin.login(config);
                }
            });
            logout_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser != null) {
                        if (currentUser instanceof SmartFacebookUser) {
                            smartLogin = SmartLoginFactory.build(LoginType.Facebook);
                        } else if (currentUser instanceof SmartGoogleUser) {
                            smartLogin = SmartLoginFactory.build(LoginType.Google);
                        } else {
                            smartLogin = SmartLoginFactory.build(LoginType.CustomLogin);
                        }
                        boolean result = smartLogin.logout(getContext());
                        if (result) {
                            refreshLayout();
                            Toast.makeText(getActivity().getApplicationContext(), "User logged out successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


            etEmailMobile.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                    if (cs.length() >= 1) {
                        etEmailMobile.setBackgroundResource(R.drawable.rounded_corner_button);
                    } else {
                        etEmailMobile.setBackgroundResource(R.drawable.rounded_white);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().length() >= 1) {
                        enableSubmitButton();
                    } else {
                        disableSubmitButton();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

            });

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
       /* FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                    Logger.e("FCM TOKEN Failed", task.getException() + "");
                } else {
                    token = task.getResult().getToken();
                    Logger.e("FCM TOKEN", token);
                }
            }
        });*/
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                token = task.getException().getMessage();
                Logger.e("FCM TOKEN Failed", task.getException() + "");
            } else {
                token = task.getResult().getToken();
                Logger.e("FCM TOKEN", token);
            }
        });
    }


    /*to anable submit button*/
    public void enableSubmitButton() {
       Logger.i(TAG, "enableSubmitButton");
        try {
            txtSendOtp.setBackground(getResources().getDrawable(R.drawable.yellow_rectangle_shape));
            txtSendOtp.setTextColor(getResources().getColor(R.color.black));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }
/*to disable submit button*/
    public void disableSubmitButton() {
       Logger.i(TAG, "disableSubmitButton");
        try {
            txtSendOtp.setBackground(getResources().getDrawable(R.drawable.rounded_corner_button));
            txtSendOtp.setTextColor(getResources().getColor(R.color.text_color_five));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*otp sucess*/
    @Override
    public void sendOtpSuccessFlow(OTPResponse otpResponse) {
       Logger.i(TAG, "sendOtpSuccessFlow");
        mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.REFERRAL_CODE.getPreferenceKey(), "");
        ((LoginActivity) getActivity()).loadFragment(new OTPFragment().newInstance(etEmailMobile.getText().toString(), String.valueOf(otpResponse.getUserData())));
    }


    /*login success flow*/
    @Override
    public void loginSuccessFlow(LoginResponse loginResponse) {
       Logger.i(TAG, "loginSuccessFlow");
    }

    /*token update success*/
    @Override
    public void tokenUpdateSuccess(UpdateTokenResponse updateTokenResponse) {
       Logger.i(TAG, "tokenUpdateSuccess");
    }


    /*auth config success*/
    @Override
    public void authConfigSuccess(AuthConfigResponse authConfigResponse) {
       Logger.i(TAG, "authConfigSuccess");
        try {
            SharedPreference mSharedPreference = new SharedPreference(getContext());
            String authConfigInfo = new Gson().toJson(authConfigResponse);
            mSharedPreference.setStoredPreference(getContext(), SharedPreferencesEnum.AUTH_CONFIG.getPreferenceKey(), authConfigInfo);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*pendingReviewStatusResponse*/
    @Override
    public void pendingReviewStatusResponse(PendingReviewStatusResponse pendingReviewStatusResponse) {
       Logger.i(TAG, "pendingReviewStatusResponse");
    }

    @Override
    public void onInvalidOTP() {

    }

    /*onLoginSuccess*/
    @Override
    public void onLoginSuccess(SmartUser user) {
       Logger.i(TAG, "onLoginSuccess");
        refreshLayout();
    }

    /*onLoginFailure*/
    @Override
    public void onLoginFailure(SmartLoginException e) {
       Logger.i(TAG, "onLoginFailure");
        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /*doCustomLogin*/
    @Override
    public SmartUser doCustomLogin() {
       Logger.i(TAG, "doCustomLogin");
        SmartUser user = new SmartUser();
        user.setEmail("");
        return user;
    }

    /*doCustomSignup*/
    @Override
    public SmartUser doCustomSignup() {
       Logger.i(TAG, "doCustomSignup");
        SmartUser user = new SmartUser();
        user.setEmail("");
        return user;
    }

    /*showLoginFailureDialog*/
    public void showLoginFailureDialog(String msg) {
       Logger.i(TAG, "showLoginFailureDialog");
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


}