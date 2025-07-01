package in.woloo.www.vtion.activities;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.woloo.www.R;
import in.woloo.www.SelectGender.SelectGenderActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.databinding.ActivityVitionUserDataBinding;
import in.woloo.www.interestedtopic.InterestedTopicsActivity;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.profile.model.EditProfileResponse;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.splash.UserDetails;
import in.woloo.www.v2.util.NetcoreUserDetails;
import in.woloo.www.vtion.fragments.DialogVisionPrivacyPolicy;
import in.woloo.www.vtion.fragments.DialogVitionEducation;
import in.woloo.www.vtion.fragments.DialogVitionOwnership;
import in.woloo.www.vtion.model.ResultVtionSdkModel;
import in.woloo.www.vtion.utilities.AgeCalculatorFromDOB;
import in.woloo.www.vtion.utilities.UserAccessPermissionResponseClass;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sdk.vtion.in.VtionSDK;
import sdk.vtion.in.callback.VTIONSDKInitializationCallback;

public class VitionUserDataActivity extends AppCompatActivity implements DialogVitionOwnership.OnFragmentInteractionListener  , DialogVitionEducation.OnFragmentInteractionListenerEdu , VTIONSDKInitializationCallback {


    private ActivityVitionUserDataBinding binding;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private ProfileViewModel profileViewModel;
    private String gender = "";
    private  String deviceId = "";
    Double latitude = 0.0 , longitude = 0.0;

    int age;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    private SettingsClient settingsClient;
    private LocationRequest locationRequest;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private DatabaseReference mDatabase;
    private String mobileNumber;

    private  Boolean visionResult;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVitionUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPrefSettings sps = new SharedPrefSettings();
        sps.storeIsVTION(true);
        binding.txEdu.setOnKeyListener(null);
        binding.etOwnerShip.setOnKeyListener(null);
        binding.rgGender.clearCheck();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        settingsClient = LocationServices.getSettingsClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        checkGpsAndRequestLocation();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {

            // Permission not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

            // Permission already granted, proceed to get location

            getLocation();
        }

        initView();
        setLiveData();

        new AdvertisingIdTask(this, new AdvertisingIdCallback() {
            @Override
            public void onAdvertisingIdRetrieved(String advertisingId) {
                if (advertisingId != null) {
                    Log.d("TAG", "Advertising ID: " + advertisingId);
                    deviceId = advertisingId;
                    Log.d("Data Is", deviceId);
                    // Perform additional operations with the advertisingId here
                } else {
                    Log.e("TAG", "Failed to retrieve advertising ID");
                }
            }
        }).execute();

        int selectedId = binding.rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedText = selectedRadioButton.getText().toString();
            Log.d("SelectedRadioButton", "Selected Text: " + selectedText);
            gender = selectedText;
        } else {
            Log.d("SelectedRadioButton", "No RadioButton is selected");
            gender = "";
        }


        binding.etOwnerShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogVitionOwnership newFragment = new DialogVitionOwnership();
                newFragment.show(fragmentManager, "fullscreen_dialog");
            }
        });

        binding.txEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogVitionEducation newFragment = new DialogVitionEducation();
                newFragment.show(fragmentManager, "fullscreen_dialog");

            }
        });

        binding.tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogVisionPrivacyPolicy newFragment = new DialogVisionPrivacyPolicy();
                newFragment.show(fragmentManager, "fullscreen_dialog");
            }
        });

    }

    private void setLiveData(){
        profileViewModel.observeEditProfile().observe(this, new Observer<BaseResponse<EditProfileResponse>>() {
            @Override
            public void onChanged(BaseResponse<EditProfileResponse> editProfileResponseBaseResponse) {
                if(visionResult)
                {
                    showFreeTrialDialogVtion();
                }
                else {
                    showFreeTrialDialogNormal();
                }

               /* startActivity(new Intent(VitionUserDataActivity.this, InterestedTopicsActivity.class));
                finish();*/
            }
        });
    }
    private void initView() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.etMobile.setText(new CommonUtils().getUserInfo().getMobile());
        binding.etCity.setOnClickListener(v -> {
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


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.etDob.setText(sdf.format(myCalendar.getTime()));
                String dobIs = binding.etDob.getText().toString();
                StringBuilder stringBuilder = new StringBuilder(dobIs);
                // Iterate through the string and replace spaces with underscores
                for (int i = 0; i < stringBuilder.length(); i++) {
                    if (stringBuilder.charAt(i) == ' ') {
                        stringBuilder.setCharAt(i, '-');
                    }
                }
                String replacedString = stringBuilder.toString();
                Logger.d("AGE IS 1 ", String.valueOf(replacedString));
                // new AgeCalculationTask().execute(replacedString);
                new AgeCalculationTask(VitionUserDataActivity.this, new AgeCallback() {
                    @Override
                    public void onAgeRetrieved(Integer age1) {
                        if (age1 != null) {
                            Log.d("TAG", "Age: " + age1);
                            age = age1;
                            Log.d("Data Is", age + "");
                        } else {
                            Log.e("TAG", "Failed to retrieve age");
                        }
                    }
                }).execute(replacedString);
            }
        };

        binding.etDob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog =  new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


        binding.btnNext.setOnClickListener(v -> {

            if (isValid()) {

                try {


                    Log.d("Aarati Data Is", deviceId + "" + age);

                    mobileNumber =  binding.etMobile.getText().toString();
                    VtionSDK.initialize(VitionUserDataActivity.this , gender , age + "" ,
                            deviceId , binding.txEdu.getText().toString() , binding.etOwnerShip.getText().toString(),
                            binding.etMobile.getText().toString() , "in.woloo.www", latitude , longitude , this);
                    Log.d("VITION" , gender +" "+ age +" "+
                            deviceId +" "+ binding.txEdu.getText().toString() +" "+ binding.etOwnerShip.getText().toString() +" "+
                            binding.etMobile.getText().toString() +" "+ "in.woloo.www"+" "+ latitude +" "+ longitude);

                   /* Toast.makeText(this , " Details" +binding.txEdu.getText().toString() + "\n" +
                            binding.etOwnerShip.getText().toString() + "\n" + latitude + " and " + longitude + " and " + age , Toast.LENGTH_LONG).show();
                    Log.d("Data Is" , sdf.format(myCalendar.getTime()));*/

                    mDatabase.child("result").child(mobileNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                DataSnapshot dataSnapshot = task.getResult();
                                Log.d("firebase", String.valueOf(dataSnapshot));
                            }
                        }
                    });


                } catch (Exception e) {
                    CommonUtils.printStackTrace(e);
                }
            }

        });
    }

    private boolean isValid() {
        int selectedId = binding.rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedText = selectedRadioButton.getText().toString();
            Log.d("SelectedRadioButton", "Selected Text: " + selectedText);
            gender = selectedText;
        } else {
            Log.d("SelectedRadioButton", "No RadioButton is selected");
            gender = "";
        }
        String mail = binding.etEmail.getText().toString().replaceAll("\\s+", "");
        if (!TextUtils.isEmpty(binding.etEmail.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            // etCity.setError("please enter City");
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etCity.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter city ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etPincode.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter pincode ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etPincode.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter valid pincode ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etAddress.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter address ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etDob.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select Birth Date ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.txEdu.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select education ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.etOwnerShip.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please select ownerships ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gender.equals("")) {
            Toast.makeText(getApplicationContext(), "Please select gender ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                binding.etCity.setText(place.getName());
                binding.etPincode.requestFocus();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // User agreed to the settings change
                getLocation();
            } else {
                // User chose not to change the settings
                latitude = 0.0;
                longitude = 0.0;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                getLocation();
            } else {
                // Permission denied
                // Handle this case if needed
                latitude = 0.0;
                longitude = 0.0;
            }
        }
    }

    private void getLocation() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
                &&   (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            Task<Location> locationResult  = fusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            // Use latitude and longitude
                            // Example: Log the latitude and longitude
                            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                        } else if (location == null) {
                            latitude = 0.0 ;
                            longitude = 0.0;
                        }
                    }
                }

            });

        }
    }



       /* public  String getAdvertisingId() {
            try {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(VitionUserDataActivity.this);
                return info.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (GooglePlayServicesRepairableException e) {
                throw new RuntimeException(e);
            } catch (GooglePlayServicesNotAvailableException e) {
                throw new RuntimeException(e);
            }
        }*/

/*    @Override
    protected void onResume() {
        super.onResume();
        boolean isAccessibilityEnabled1 = isAccessibilityServiceEnabled1(getApplicationContext() , UserClass.class);
        if (isAccessibilityEnabled1) {
            // Accessibility service is enabled
            Log.d("Accessibility", "Service is enabled");
            startActivity(new Intent(getApplicationContext(), VitionUserDataActivity.class));
            finish();
        } else {
            // Accessibility service is not enabled
            Log.d("Accessibility", "Service is not enabled");
            startActivity(new Intent(getApplicationContext(), SelectGenderActivity.class));
           finish();
        }
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isAccessibilityEnabled1 = isAccessibilityServiceEnabled1(getApplicationContext() , UserAccessPermissionResponseClass.class);
        if (isAccessibilityEnabled1) {
            // Accessibility service is enabled
            Log.d("Accessibility", "Service is enabled");
            startActivity(new Intent(getApplicationContext(), VitionUserDataActivity.class));
            finish();
        } else {
            // Accessibility service is not enabled
            Log.d("Accessibility", "Service is not enabled");
            startActivity(new Intent(getApplicationContext(), SelectGenderActivity.class));
            finish();
        }
    }

    public boolean isAccessibilityServiceEnabled1(Context context, Class<?> serviceClass) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = AccessibilityManagerCompat.getEnabledAccessibilityServiceList(am, AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo serviceInfo : enabledServices){
            if (serviceInfo.getResolveInfo().serviceInfo.packageName.equals(context.getPackageName())
                    && serviceInfo.getResolveInfo().serviceInfo.name.equals(serviceClass.getName())) {


                return true;
            }

        }
        return false;
    }

    @Override
    public void onStringFragmentInteraction(String data) {
        Log.d("OwnerShip Data", data);
        binding.etOwnerShip.setText(data);
    }


    @Override
    public void onStringFragmentInteractionEdu(String data) {
        Log.d("Education Data", data);
        binding.txEdu.setText(data);
    }

    @Override
    public void onInitializationSuccess() {
        Log.d("VISION INITIALISED" , "SUCCESS");
        visionResult = true;
        SharedPrefSettings sps = new SharedPrefSettings();
        sps.storeIsVTION(false);
        ResultVtionSdkModel data = new ResultVtionSdkModel(mobileNumber, "Success" , false);
        mDatabase.child("result").child(mobileNumber).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Data written successfully");
            } else {
                System.out.println("Error writing data: " + task.getException().getMessage());

            }
        });
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                .addFormDataPart("name", binding.etName.getText().toString())
                .addFormDataPart("email", binding.etEmail.getText().toString())
                .addFormDataPart("address", binding.etAddress.getText().toString())
                .addFormDataPart("city",binding.etCity.getText().toString())
                .addFormDataPart("pincode", binding.etPincode.getText().toString())
                .addFormDataPart("dob",sdf.format(myCalendar.getTime()))
                .addFormDataPart("gender",gender)
                .addFormDataPart("IsVtionUser" , "1")
                .build();
        profileViewModel.updateProfile(requestBody);

    }

    @Override
    public void onInitializationFailure() {
        Log.d("VISION INITIALISED" , "FAILED");
        visionResult = false;
        SharedPrefSettings sps = new SharedPrefSettings();
        sps.storeIsVTION(false);
        ResultVtionSdkModel result = new ResultVtionSdkModel(mobileNumber, "Failed" , false);
        mDatabase.child("result").child(mobileNumber).setValue(result).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Data written successfully");
            } else {
                System.out.println("Error writing data: " + task.getException().getMessage());
            }
        });
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                .addFormDataPart("name", binding.etName.getText().toString())
                .addFormDataPart("email", binding.etEmail.getText().toString())
                .addFormDataPart("address", binding.etAddress.getText().toString())
                .addFormDataPart("city",binding.etCity.getText().toString())
                .addFormDataPart("pincode", binding.etPincode.getText().toString())
                .addFormDataPart("dob",sdf.format(myCalendar.getTime()))
                .addFormDataPart("gender",gender)
                .addFormDataPart("IsVtionUser" , "0")
                .build();
        profileViewModel.updateProfile(requestBody);
    }


    private class AgeCalculationTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private AgeCallback acallback;

        public AgeCalculationTask(Context context, AgeCallback acallback) {
            this.context = context.getApplicationContext(); // Use application context to avoid leaks
            this.acallback = acallback;
        }



        @Override
        protected Integer doInBackground(String... strings) {
            // Background task: Parse date and calculate age
            if (strings.length == 0) return null;
            String dateString = strings[0];
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            try {
                Date dateOfBirth = sdf.parse(dateString);
                AgeCalculatorFromDOB ageCalculator = new AgeCalculatorFromDOB();
                age = ageCalculator.calculateAge(dateOfBirth);
                Logger.d("AGE IS 1 ", String.valueOf(age));
                return age;
            }catch (ParseException e){
                Log.e("TAG", "Error parsing date", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Integer age) {
           /* // UI thread: Update UI with the calculated age
            if (age != null) {
                Log.d("TAG", "Calculated age: " + age);
                // Update UI or do other operations with the age
            } else {
                Log.e("TAG", "Age calculation failed");
                // Handle error
            }*/
            if (acallback != null) {
                acallback.onAgeRetrieved(age);
            }
        }
    }


    private class AdvertisingIdTask extends AsyncTask<Void, Void, String> {


        private Context context;
        private AdvertisingIdCallback callback;

        public AdvertisingIdTask(Context context, AdvertisingIdCallback callback) {
            this.context = context.getApplicationContext(); // Use application context to avoid leaks
            this.callback = callback;
        }


        @Override
        protected String doInBackground(Void... voids) {
            try {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(VitionUserDataActivity.this);
                deviceId = info.getId();
                return info.getId();
            } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                Log.e("TAG", "Failed to retrieve advertising ID", e);
                return null;
            }
        }


        @Override
        protected void onPostExecute(String advertisingId) {
            // Use the retrieved advertisingId
         /*   if (advertisingId != null) {
                Log.d("TAG", "Advertising ID: " + advertisingId);
                deviceId = advertisingId;
                // Further processing with the advertising ID
            } else {
                Log.e("TAG", "Failed to retrieve advertising ID");
                // Handle error or retry mechanism
            }*/
            if (callback != null) {
                callback.onAdvertisingIdRetrieved(advertisingId);
            }
        }
    }

    public void checkGpsAndRequestLocation() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. You can use the location settings.
                    return;
                } catch (ApiException e) {
                    // Location settings are not satisfied, show user a dialog to change settings
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                resolvable.startResolutionForResult(VitionUserDataActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied, and cannot be fixed here.
                            //  Toast.makeText(VitionUserDataActivity.this, "Location settings are not available", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    public interface AdvertisingIdCallback {
        void onAdvertisingIdRetrieved(String advertisingId);
    }

    public interface AgeCallback {
        void onAgeRetrieved(Integer age);
    }

   /* private void showFreeTrialDialog() {
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
                if (freeTrialDialogText.contains("7")) {
                    freeTrialDialogText = freeTrialDialogText.replace("7", authConfigResponse.getFreeTrialPeriodDays());
                }

                startFreeTrial.setText(decode(freeTrialDialogText));
            }

            LinearLayout llStartFreeTrial = dialog.findViewById(R.id.llStartFreeTrial);
            llStartFreeTrial.setOnClickListener(v -> dialog.dismiss());

            dialog.show();

            dialog.setOnDismissListener(dialog1 -> {
                moveToDashboard();
            });
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }*/

    private String decode(String text) {
        return text.replace("&amp;", "&");
    }

    private void moveToDashboard() {

        CommonUtils commonUtils = new CommonUtils();
        UserDetails userInfo = commonUtils.getUserInfo();

        NetcoreUserDetails netcoreUserDetails = new NetcoreUserDetails(this);
        netcoreUserDetails.setNetcoreUserIdentityAndLogin(userInfo.getMobile() != null ? userInfo.getMobile() : "");
        netcoreUserDetails.updateNetcoreUserProfile();

        Logger.e("data", "save to netcore");

            startActivity(new Intent(VitionUserDataActivity.this, InterestedTopicsActivity.class));
            finish();


    }


    private void showFreeTrialDialogNormal() {
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

            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(this);
            if (authConfigResponse != null) {
                String freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE().getFreeTrialDialogText();
                freeTrialDialogText = freeTrialDialogText.replace("\\\\n", "\n");
              //  if (freeTrialDialogText.contains("<Trial Days>")) {
                freeTrialDialogText = freeTrialDialogText.replace("<" , "");
                freeTrialDialogText = freeTrialDialogText.replace(">" , "");
                    freeTrialDialogText = freeTrialDialogText.replace("Trial Days", AppConstants.FREE_TRAIL_NORMAL_DAYS);
              //  }

                startFreeTrial.setText(decode(freeTrialDialogText));
            }

            LinearLayout llStartFreeTrial = dialog.findViewById(R.id.llStartFreeTrial);
            llStartFreeTrial.setOnClickListener(v -> dialog.dismiss());

            dialog.show();

            dialog.setOnDismissListener(dialog1 -> {
                moveToDashboard();
            });
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }

    private void showFreeTrialDialogVtion() {
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
                daysTrials.setText( AppConstants.FREE_TRAIL_VTION_DAYS + "\nDAYS");
                typeOfVoucher.setText(authConfigResponse.getFreeTrialText());

            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(this);
            if (authConfigResponse != null) {
                String freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE().getFreeTrialDialogText();
                freeTrialDialogText = freeTrialDialogText.replace("\\\\n", "\n");
                freeTrialDialogText = freeTrialDialogText.replace("<" , "");
                freeTrialDialogText = freeTrialDialogText.replace(">" , "");
                freeTrialDialogText = freeTrialDialogText.replace("Trial Days", AppConstants.FREE_TRAIL_VTION_DAYS);


                startFreeTrial.setText(decode(freeTrialDialogText));
            }

            LinearLayout llStartFreeTrial = dialog.findViewById(R.id.llStartFreeTrial);
            llStartFreeTrial.setOnClickListener(v -> dialog.dismiss());

            dialog.show();

            dialog.setOnDismissListener(dialog1 -> {
                moveToDashboard();
            });
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
        }
    }


}