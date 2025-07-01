package in.woloo.www.SelectGender;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.editprofile.EditProfileActivity;
import in.woloo.www.editprofile.EditProfileFragment;
import in.woloo.www.editprofile.mvp.EditProfilePresenter;
import in.woloo.www.editprofile.mvp.EditProfileView;
import in.woloo.www.interestedtopic.InterestedTopicsActivity;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.period_tracker.ui.PeriodTrackerActivity;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.profile.model.EditProfileResponse;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.splash.UserDetails;
import in.woloo.www.v2.util.NetcoreUserDetails;
import in.woloo.www.vtion.activities.VitionUserDataActivity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SelectGenderActivity extends AppCompatActivity {
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.btnNext)
    TextView btnNext;

    @BindView(R.id.rgGender)
    RadioGroup rgGender;

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

    @BindView(R.id.llMobile)
    LinearLayout llMobile;

    @BindView(R.id.llEmail)
    LinearLayout llEmail;

    private EditProfilePresenter editProfilePresenter;
    private ProfileViewModel profileViewModel;
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);
        ButterKnife.bind(this);
        initView();
        setLiveData();
    }

    private void setLiveData(){
        profileViewModel.observeEditProfile().observe(this, new Observer<BaseResponse<EditProfileResponse>>() {
            @Override
            public void onChanged(BaseResponse<EditProfileResponse> editProfileResponseBaseResponse) {
              //  startActivity(new Intent(SelectGenderActivity.this, InterestedTopicsActivity.class));

                    showFreeTrialDialog();


            }
        });
    }
    private void initView() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        rgGender.clearCheck();
        etMobile.setText(new CommonUtils().getUserInfo().getMobile());
        etCity.setOnClickListener(v -> {
            try {
                if (!Places.isInitialized()) {
                    String key = CommonUtils.googlemapapikey(this);
                    Places.initialize(this,key);
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

        final Calendar myCalendar = Calendar.getInstance();
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
            DatePickerDialog datePickerDialog =  new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        btnNext.setOnClickListener(v -> {

            if (isValid()) {
//                JSONObject mJsObjParam = new JSONObject();
                try {
//                    mJsObjParam.put(JSONTagConstant.NAME, etName.getText().toString());
//                    mJsObjParam.put(JSONTagConstant.EMAIL, etEmail.getText().toString());
//                    mJsObjParam.put(JSONTagConstant.CITY, etCity.getText().toString());
//                    mJsObjParam.put(JSONTagConstant.PINCODE, etPincode.getText().toString());
//                    mJsObjParam.put(JSONTagConstant.ADDRESS, etAddress.getText().toString());
//                    mJsObjParam.put(JSONTagConstant.DOB, etDob.getText().toString());
//
//                    int checkedGenderId = rgGender.getCheckedRadioButtonId();
//                    RadioButton rbGender = findViewById(checkedGenderId);
//                    gender = rbGender.getText().toString();
//                    try {
//                        mJsObjParam.put(JSONTagConstant.GENDER, gender);
//                    } catch (Exception e) {
//                          CommonUtils.printStackTrace(e)
//                    }
                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                            .addFormDataPart("name", etName.getText().toString())
                            .addFormDataPart("email", etEmail.getText().toString())
                            .addFormDataPart("address", etAddress.getText().toString())
                            .addFormDataPart("city",etCity.getText().toString())
                            .addFormDataPart("pincode", etPincode.getText().toString())
                            .addFormDataPart("dob",sdf.format(myCalendar.getTime()))
                            .addFormDataPart("gender",gender)
                            .addFormDataPart("IsVtionUser" , "0")
                            .build();
                    profileViewModel.updateProfile(requestBody);
//                    editProfilePresenter.editProfile(SelectGenderActivity.this, mJsObjParam);
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
//                editProfilePresenter.editProfile(this, mJsObjParam);
                }

        });
    }

    private boolean isValid() {
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
        String mail = etEmail.getText().toString().replaceAll("\\s+", "");
        if (!TextUtils.isEmpty(etEmail.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            // etCity.setError("please enter City");
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etCity.getText().toString().equals("")) {
            // etCity.setError("please enter City");
            Toast.makeText(getApplicationContext(), "Please enter city ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPincode.getText().toString().equals("")) {
            // etPincode.setError("please enter pincode");
            Toast.makeText(getApplicationContext(), "Please enter pincode ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPincode.getText().toString().length() < 6) {
            //etPincode.setError("please enter valid pincode");
            Toast.makeText(getApplicationContext(), "Please enter valid pincode ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etAddress.getText().toString().equals("")) {
            //etAddress.setError("please enter address");
            Toast.makeText(getApplicationContext(), "Please enter address ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etCity.setText(place.getName());
                etPincode.requestFocus();
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

    private void moveToDashboard() {

        CommonUtils commonUtils = new CommonUtils();
        UserDetails userInfo = commonUtils.getUserInfo();

        NetcoreUserDetails netcoreUserDetails = new NetcoreUserDetails(this);
        netcoreUserDetails.setNetcoreUserIdentityAndLogin(userInfo.getMobile() != null ? userInfo.getMobile() : "");
        netcoreUserDetails.updateNetcoreUserProfile();

        Logger.e("data", "save to netcore");


            startActivity(new Intent(SelectGenderActivity.this, InterestedTopicsActivity.class));
            finish();
    }

    private void showFreeTrialDialog() {
        try {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_start_free_trial);
            dialog.getWindow().setAttributes(dialog.getWindow().getAttributes());
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

            TextView startFreeTrial = dialog.findViewById(R.id.tv_startfreetrial);
            TextView daysTrials = dialog.findViewById(R.id.tv_daysTrials);
            TextView typeOfVoucher = dialog.findViewById(R.id.tv_typeOfVoucher);
            ImageView ivFreeTrial = dialog.findViewById(R.id.ivFreeTrial);

            try {
                AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(this);
                daysTrials.setText(authConfigResponse.getFreeTrialPeriodDays() + "\nDAYS");
                typeOfVoucher.setText(authConfigResponse.getFreeTrialText());
                // ImageUtil.loadImage(
                //     this,
                //     ivFreeTrial,
                //     authConfigResponse.getuRLS().getFreeTrialImageUrl()
                // );
                // tv_daysTrials.setText(authConfigResponse.getFreeTrialPeriodDays() + "\nDays");
                // ivFreeTrial.setImageResource(R.drawable.free_trial_image);
            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(this);
            if (authConfigResponse != null) {
                String freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE().getFreeTrialDialogText();
                freeTrialDialogText = freeTrialDialogText.replace("\\\\n", "\n");

                freeTrialDialogText = freeTrialDialogText.replace("<" , "");
                freeTrialDialogText = freeTrialDialogText.replace(">" , "");
                freeTrialDialogText = freeTrialDialogText.replace("Trial Days", AppConstants.FREE_TRAIL_NORMAL_DAYS);


                startFreeTrial.setText(decode(freeTrialDialogText));
            }

            LinearLayout llStartFreeTrial = dialog.findViewById(R.id.llStartFreeTrial);
            llStartFreeTrial.setOnClickListener(v -> dialog.dismiss());

            dialog.show();

            dialog.setOnDismissListener(dialog1 -> {
                moveToDashboard();
                dialog.dismiss();

            });
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    private String decode(String text) {
        return text.replace("&amp;", "&");
    }


}