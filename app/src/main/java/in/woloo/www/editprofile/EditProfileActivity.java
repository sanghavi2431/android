package in.woloo.www.editprofile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.editprofile.mvp.EditProfilePresenter;
import in.woloo.www.editprofile.mvp.EditProfileView;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.subscribe.adapter.SubscribeAdapter;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.profile.model.EditProfileResponse;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.splash.UserDetails;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements EditProfileView, TextWatcher, View.OnTouchListener {

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.etCity)
    EditText etCity;

    @BindView(R.id.etPincode)
    EditText etPincode;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.etDob)
    EditText etDob;

    @BindView(R.id.rbMale)
    RadioButton rbMale;

    @BindView(R.id.rbFemale)
    RadioButton rbFemale;

    @BindView(R.id.rbOther)
    RadioButton rbOther;

    @BindView(R.id.llMobile)
    LinearLayout llMobile;

    @BindView(R.id.llEmail)
    LinearLayout llEmail;

    @BindView(R.id.rgGender)
    RadioGroup rgGender;

    private String gender = "";

    private EditProfilePresenter editProfilePresenter;
    private ProfileViewModel profileViewModel;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private boolean from_subscription = false;
    private String planId = "";
    private Integer id = 0;
    private String msg;
    private boolean isEmail;
    private String mobile;
    public static String TAG = EditProfileActivity.class.getSimpleName();
    Calendar myCalendar;

    /*calling onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        myCalendar = Calendar.getInstance();
        initViews();
        setLiveData();
    }

    /*calling initViews*/
    @SuppressLint("SimpleDateFormat")
    private void initViews() {
        try {
            Logger.i(TAG, "initViews");
            editProfilePresenter = new EditProfilePresenter(this, EditProfileActivity.this);
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            UserProfile viewProfileResponse = WolooApplication.getInstance().getProfileResponse();
            from_subscription = getIntent().getBooleanExtra(AppConstants.FROM_SUBSCRIPTION, false);
            planId = getIntent().getStringExtra(AppConstants.PLAN_ID);
            id = getIntent().getIntExtra(AppConstants.ID, 0);
            msg = getIntent().getStringExtra(AppConstants.MSG);
            isEmail = getIntent().getBooleanExtra(AppConstants.IS_EMAIL, false);
            mobile = getIntent().getStringExtra(AppConstants.MOBILE);
            try {
                etName.setText(viewProfileResponse.getProfile().getName());
                //if(!TextUtils.isEmpty(viewProfileResponse.getProfile().getEmail())){
                etEmail.setText(viewProfileResponse.getProfile().getEmail());
                //llEmail.setVisibility(View.VISIBLE);
                /*}else{
                    llEmail.setVisibility(View.GONE);
                }*/
                if (!TextUtils.isEmpty(viewProfileResponse.getProfile().getMobile())) {
                    etMobile.setText(viewProfileResponse.getProfile().getMobile());
                    llMobile.setVisibility(View.VISIBLE);
                } else {
                    llMobile.setVisibility(View.GONE);
                }
                etCity.setText(viewProfileResponse.getProfile().getCity());
                etPincode.setText(viewProfileResponse.getProfile().getPincode());
                etAddress.setText(viewProfileResponse.getProfile().getAddress());
                if (!TextUtils.isEmpty(viewProfileResponse.getProfile().getDob())) {
                    String inputPattern = "yyyy-MM-dd";
                    String outputPattern = "dd MMM yyyy";
                    Date date = new SimpleDateFormat(inputPattern).parse(viewProfileResponse.getProfile().getDob());
                    etDob.setText(new SimpleDateFormat(outputPattern).format(date));
                }
                tvTitle.setText(getString(R.string.edit_profile));
                setGenderSelection(viewProfileResponse.getProfile().getGender());
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
            try {
                ivBack.setOnClickListener(v -> {
                    onBackPressed();
                });
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }

           /* try {
                if (viewProfileResponse.getProfile().getGender().equalsIgnoreCase("Male")) {
                    rbMale.setChecked(true);
                    for (int i = 0; i < rgGender.getChildCount(); i++) {
                        ((RadioButton) rgGender.getChildAt(i)).setEnabled(false);
                    }
                }
                if (viewProfileResponse.getProfile().getGender().equalsIgnoreCase("Female")) {
                    rbFemale.setChecked(true);
                    for (int i = 0; i < rgGender.getChildCount(); i++) {
                        ((RadioButton) rgGender.getChildAt(i)).setEnabled(false);
                    }
                }
                if (viewProfileResponse.getProfile().getGender().equalsIgnoreCase("Other")) {
                    rbOther.setChecked(true);
                    for (int i = 0; i < rgGender.getChildCount(); i++) {
                        ((RadioButton) rgGender.getChildAt(i)).setEnabled(false);
                    }
                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }*/
            tvSubmit.setOnClickListener(v -> {
                if (isValid()) {

                    try {
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        String dob = "";
                      /*  if (!TextUtils.isEmpty(etDob.getText())) {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "yyyy-MM-dd";
                            Date date = null;
                            date = new SimpleDateFormat(outputPattern).parse(etDob.getText().toString());
                            dob = new SimpleDateFormat(outputPattern).format(date);
                        }*/
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                                .addFormDataPart("name", etName.getText().toString())
                                .addFormDataPart("email", etEmail.getText().toString())
                                .addFormDataPart("address", etAddress.getText().toString())
                                .addFormDataPart("city",etCity.getText().toString())
                                .addFormDataPart("pincode", etPincode.getText().toString())
                                .addFormDataPart("dob",sdf.format(myCalendar.getTime()))
//                                .addFormDataPart("dob",etDob.getText().toString())
                                .addFormDataPart("gender",gender)
                                .build();
                        profileViewModel.updateProfile(requestBody);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                    JSONObject mJsObjParam = new JSONObject();
//                    try {
//                        mJsObjParam.put(JSONTagConstant.NAME, etName.getText().toString());
//                        mJsObjParam.put(JSONTagConstant.EMAIL, etEmail.getText().toString());
//                        mJsObjParam.put(JSONTagConstant.CITY, etCity.getText().toString());
//                        mJsObjParam.put(JSONTagConstant.PINCODE, etPincode.getText().toString());
//                        mJsObjParam.put(JSONTagConstant.ADDRESS, etAddress.getText().toString());
//                        mJsObjParam.put(JSONTagConstant.GENDER, gender);
//                        mJsObjParam.put(JSONTagConstant.DOB, etDob.getText().toString());
//                    } catch (Exception e) {
//                          CommonUtils.printStackTrace(e)
//                    }
//                    editProfilePresenter.editProfile(EditProfileActivity.this, mJsObjParam);
                }
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }

        etName.addTextChangedListener(this);
        etAddress.addTextChangedListener(this);
        //etCity.addTextChangedListener(this);
        etPincode.addTextChangedListener(this);
        rbFemale.setOnTouchListener(this::onTouch);
        rbMale.setOnTouchListener(this::onTouch);

        etCity.setOnClickListener(v -> {
            try {
                if (!Places.isInitialized()) {
                    String key = CommonUtils.googlemapapikey(this);
                    Places.initialize(this, key);
                }
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etDob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        etDob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

    }

    void setLiveData(){
        profileViewModel.observeEditProfile().observe(this, new Observer<BaseResponse<EditProfileResponse>>() {
            @Override
            public void onChanged(BaseResponse<EditProfileResponse> response) {
                if(response != null){
                    editProfileSuccess();
                }else{
                    Toast.makeText(getApplicationContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etCity.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*calling isValid*/
    private boolean isValid() {
        Logger.i(TAG, "isValid");
        gender = "";
        if (rbMale.isChecked()) {
            gender = rbMale.getText().toString();
        }
        if (rbFemale.isChecked()) {
            gender = rbFemale.getText().toString();
        }
        if (rbOther.isChecked()) {
            gender = rbOther.getText().toString();
        }

        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedText = selectedRadioButton.getText().toString();
            Log.d("SelectedRadioButton", "Selected Text: " + selectedText);
            gender = selectedText;
        } else {
            Log.d("SelectedRadioButton", "No RadioButton is selected");
            gender = "";
        }

        if (!TextUtils.isEmpty(etEmail.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            // etCity.setError("please enter City");
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etCity.getText().toString().equals("")) {
            // etCity.setError("please enter City");
            Toast.makeText(getApplicationContext(), "Please enter city ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPincode.getText().toString().equals("")) {
            // etPincode.setError("please enter pincode");
            Toast.makeText(getApplicationContext(), "Please enter pincode ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPincode.getText().toString().length() < 6) {
            //etPincode.setError("please enter valid pincode");
            Toast.makeText(getApplicationContext(), "Please enter valid pincode ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAddress.getText().toString().equals("")) {
            //etAddress.setError("please enter address");
            Toast.makeText(getApplicationContext(), "Please enter address ", Toast.LENGTH_SHORT).show();
        } else return !TextUtils.isEmpty(gender);
        return false;
    }

    /*calling editProfileSuccess*/
    @Override
    public void editProfileSuccess() {
        Logger.i(TAG, "editProfileSuccess");
        try {
            if (from_subscription) {
//                TODO initSubscription
                editProfilePresenter.initSubscription(id, planId, new SubscribeAdapter.InitSubscriptionCallback() {
                    @Override
                    public void initSubscriptionSuccess(InitSubscriptionResponse initSubscriptionResponse) {
                        try {
                            if (initSubscriptionResponse != null && initSubscriptionResponse.getData() != null) {
                                String subscriptionId = initSubscriptionResponse.getData().getSubscriptionId();
                                if (!TextUtils.isEmpty(subscriptionId)) {
                                    CommonUtils.navigateToRazorPayFlow(EditProfileActivity.this, planId, subscriptionId, msg, isEmail, mobile, true, null, false, false);
                                    EditProfileActivity.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.subscription_validation_profile), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), EditProfileActivity.this.getResources().getString(R.string.subscription_validation_profile), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                             CommonUtils.printStackTrace(ex);
                        }
                    }
                });
            } else {
                UserDetails user = new CommonUtils().getUserInfo();
                user.setName(etName.getText().toString().trim());
                user.setEmail(etEmail.getText().toString().trim());
                SharedPrefSettings.Companion.getGetPreferences().storeUserDetails(user);

//                LoginResponse loginResponse = new CommonUtils().getUserInfo(EditProfileActivity.this);
//                SharedPreference mSharedPreference = new SharedPreference(this);
//                loginResponse.getData().getUser().setName(etName.getText().toString().trim());
//                loginResponse.getData().getUser().setEmail(etEmail.getText().toString().trim());
//                String userInfo = new Gson().toJson(loginResponse);
//                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.USER_INFO.getPreferenceKey(), userInfo);

                Toast.makeText(getApplicationContext(), "Profile updated..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling beforeTextChanged*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Logger.i(TAG, "beforeTextChanged");
        showSubmitButtonActive();
    }

    /*calling onTextChanged*/
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.i(TAG, "onTextChanged");
        showSubmitButtonActive();
    }


    @Override
    public void afterTextChanged(Editable s) {
        showSubmitButtonActive();
    }

    /*calling showSubmitButtonActive*/
    private void showSubmitButtonActive() {
        Logger.i(TAG, "showSubmitButtonActive");
        try {
            tvSubmit.setTextColor(ContextCompat.getColor(this, R.color.black));
            tvSubmit.setBackground(getResources().getDrawable(R.drawable.yellow_rectangle_shape));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling onTouch*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Logger.i(TAG, "onTouch");
        showSubmitButtonActive();
        return false;
    }


    private void setGenderSelection(String gender) {
        int radioButtonId = -1;
        switch (gender) {
            case "Male":
                radioButtonId = R.id.rbMale;
                break;
            case "Female":
                radioButtonId = R.id.rbFemale;
                break;
            case "Other":
                radioButtonId = R.id.rbOther;
                break;
        }

        if (radioButtonId != -1) {
            RadioButton radioButton = findViewById(radioButtonId);
            radioButton.setChecked(true);
        }
    }

}