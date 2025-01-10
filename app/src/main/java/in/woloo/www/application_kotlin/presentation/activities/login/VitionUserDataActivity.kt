package `in`.woloo.www.application_kotlin.presentation.activities.login

import AdvertisingIdCoroutine
import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.accessibility.AccessibilityManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.accessibility.AccessibilityManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.lists_models.ResultVtionSdkModel
import `in`.woloo.www.application_kotlin.presentation.fragments.login.DialogVitionEducation
import `in`.woloo.www.application_kotlin.presentation.fragments.login.DialogVitionOwnership
import `in`.woloo.www.application_kotlin.utilities.AgeCalculatorCoroutine
import `in`.woloo.www.application_kotlin.utilities.UserAccessPermissionResponseClass
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityVitionUserDataBinding
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import sdk.vtion.`in`.VtionSDK
import sdk.vtion.`in`.callback.VTIONSDKInitializationCallback
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale
import `in`.woloo.www.v2.WebActivity

class VitionUserDataActivity : AppCompatActivity(),
    DialogVitionOwnership.OnFragmentInteractionListener,
    DialogVitionEducation.OnFragmentInteractionListenerEdu, VTIONSDKInitializationCallback {
    private var binding: ActivityVitionUserDataBinding? = null
    private var profileViewModel: ProfileViewModel? = null
    private var gender = ""
    private var deviceId = ""
    var latitude = 0.0
    var longitude = 0.0
    var age = 0
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var settingsClient: SettingsClient? = null
    private var locationRequest: LocationRequest? = null
    private var mDatabase: DatabaseReference? = null
    private var mobileNumber: String? = null
    private var visionResult: Boolean? = null
    val myCalendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVitionUserDataBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val sps = SharedPrefSettings()
        sps.storeIsVTION(true)
        binding!!.txEdu.setOnKeyListener(null)
        binding!!.etOwnerShip.setOnKeyListener(null)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        settingsClient = LocationServices.getSettingsClient(this)
        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        checkGpsAndRequestLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {

            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {

            // Permission already granted, proceed to get location
            location
        }

        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_dropdown_item_1line
        )
        binding!!.etGender!!.setAdapter(genderAdapter)
        binding!!.etGender!!.setOnItemClickListener { parent, view, position, id ->
            gender = parent.getItemAtPosition(position).toString()
        }

        initView()
        setLiveData()

     AdvertisingIdCoroutine.fetchAdvertisingId(this) { advertisingId ->
           if (advertisingId != null) {
               Log.d("TAG", "Advertising ID: $advertisingId")
               deviceId = advertisingId
               Log.d("Data Is", deviceId)
               // Perform additional operations with the advertisingId here
           } else {
               Log.e("TAG", "Failed to retrieve advertising ID")
           }
       }

        gender = binding!!.etGender.text.toString()

        binding!!.etOwnerShip.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val newFragment = DialogVitionOwnership()
            newFragment.show(fragmentManager, "fullscreen_dialog")
        }
        binding!!.txEdu.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val newFragment = DialogVitionEducation()
            newFragment.show(fragmentManager, "fullscreen_dialog")
        }
      /*  binding!!.tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("privacy_policy","https://woloo.in/privacy-policy/") // Changed By Aarati
            //intent.putExtra("privacy_policy","https://api.woloo.in/WolooTermsofUse.html")
            startActivity(intent)
        }*/
    }

    private fun setLiveData() {
        profileViewModel?.observeEditProfile()?.observe(this, Observer<Any?> {
            if (visionResult!!) {
                showFreeTrialDialogVtion()
            } else {
                showFreeTrialDialogNormal()
            }

            /* startActivity(new Intent(VitionUserDataActivity.this, InterestedTopicsActivity.class));
                    finish();*/
        })
    }

    private fun initView() {
        profileViewModel = ViewModelProvider(this).get<ProfileViewModel>(
            ProfileViewModel::class.java
        )
        binding!!.etMobile.setText(CommonUtils().userInfo!!.mobile)
        binding!!.etCity.setOnClickListener { v: View? ->
            try {
                if (!Places.isInitialized()) {
                    val key = CommonUtils.googlemapapikey(this)
                    Places.initialize(this, key)
                }
                val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
                val intent: Intent =
                    Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(this)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
        val date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "dd MMM yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding!!.etDob.setText(sdf.format(myCalendar.time))
                val dobIs = binding!!.etDob.text.toString()
                val stringBuilder = StringBuilder(dobIs)
                // Iterate through the string and replace spaces with underscores
                for (i in 0 until stringBuilder.length) {
                    if (stringBuilder[i] == ' ') {
                        stringBuilder.setCharAt(i, '-')
                    }
                }
                val replacedString = stringBuilder.toString()
                Logger.d("AGE IS 1 ", replacedString)
                // new AgeCalculationTask().execute(replacedString);
                AgeCalculatorCoroutine.calculateAge(applicationContext, replacedString) { ageCalculated ->
                    if (ageCalculated != null) {
                        age = ageCalculated
                        Log.d("TAG", "Calculated age: $age")
                        // Update UI or perform further operations with the age
                    } else {
                        Log.e("TAG", "Age calculation failed")
                        age = 10
                        // Handle error
                    }
                }
            }
        }
        binding!!.etDob.setOnClickListener { v: View? ->
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis())
            datePickerDialog.show()
        }
        binding!!.btnNext.setOnClickListener { v: View? ->
            if (isValid) {
                try {
                    binding!!.btnNext.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
                    Log.d("Aarati Data Is", deviceId + "" + age)
                    mobileNumber = binding!!.etMobile.text.toString()
                    VtionSDK.initialize(
                        this@VitionUserDataActivity,
                        gender,
                        age.toString() + "",
                        deviceId,
                        binding!!.txEdu.text.toString(),
                        binding!!.etOwnerShip.text.toString(),
                        binding!!.etMobile.text.toString(),
                        "in.woloo.www",
                        latitude,
                        longitude,
                        this
                    )
                    Log.d(
                        "VITION", gender + " " + age + " " +
                                deviceId + " " + binding!!.txEdu.text.toString() + " " + binding!!.etOwnerShip.text.toString() + " " +
                                binding!!.etMobile.text.toString() + " " + "in.woloo.www" + " " + latitude + " " + longitude
                    )

                    /* Toast.makeText(this , " Details" +binding.txEdu.getText().toString() + "\n" +
                            binding.etOwnerShip.getText().toString() + "\n" + latitude + " and " + longitude + " and " + age , Toast.LENGTH_LONG).show();
                    Log.d("Data Is" , sdf.format(myCalendar.getTime()));*/
                    mDatabase!!.child("result").child(mobileNumber!!).get().addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.e("firebase", "Error getting data", task.exception)
                        } else {
                            val dataSnapshot = task.result
                            Log.d("firebase", dataSnapshot.toString())
                        }
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        }
    }

    private val isValid: Boolean
        private get() {
             if(binding!!.etGender.text.toString().isNotEmpty())
             {
                gender = binding!!.etGender.text.toString()
             }

            val mail = binding!!.etEmail.text.toString().replace("\\s+".toRegex(), "")
            if (!TextUtils.isEmpty(binding!!.etEmail.text.toString()) && !Patterns.EMAIL_ADDRESS.matcher(
                    mail
                ).matches()
            ) {
                // etCity.setError("please enter City");
                Toast.makeText(
                    applicationContext,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (binding!!.etCity.text.toString() == "") {
                Toast.makeText(applicationContext, "Please enter city ", Toast.LENGTH_SHORT).show()
                return false
            }
            if (binding!!.etPincode.text.toString() == "") {
                Toast.makeText(applicationContext, "Please enter pincode ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (binding!!.etPincode.text.toString().length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Please enter valid pincode ",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (binding!!.etAddress.text.toString() == "") {
                Toast.makeText(applicationContext, "Please enter address ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (binding!!.etDob.text.toString() == "") {
                Toast.makeText(applicationContext, "Please select Birth Date ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (binding!!.txEdu.text.toString() == "") {
                Toast.makeText(applicationContext, "Please select education ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (binding!!.etOwnerShip.text.toString() == "") {
                Toast.makeText(applicationContext, "Please select ownerships ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (gender == "") {
                Toast.makeText(applicationContext, "Please select gender ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            return true
        }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                binding!!.etCity.setText(place.name)
                binding!!.etPincode.requestFocus()
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                // User agreed to the settings change
                location
            } else {
                // User chose not to change the settings
                latitude = 0.0
                longitude = 0.0
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                location
            } else {
                // Permission denied
                // Handle this case if needed
                latitude = 0.0
                longitude = 0.0
            }
        }
    }

    private val location: Unit
        private get() {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                val locationResult: Task<Location> = fusedLocationClient!!.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val location = task.result
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                            Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                        } else if (location == null) {
                            latitude = 0.0
                            longitude = 0.0
                        }
                    }
                }
            }
        }

    override fun onRestart() {
        super.onRestart()
        val isAccessibilityEnabled1 = isAccessibilityServiceEnabled1(
            applicationContext,
            UserAccessPermissionResponseClass::class.java
        )
        if (isAccessibilityEnabled1) {
            Log.d("Accessibility", "Service is enabled")
            startActivity(Intent(applicationContext, VitionUserDataActivity::class.java))
            finish()
        } else {
            Log.d("Accessibility", "Service is not enabled")
            startActivity(Intent(applicationContext, SelectGenderActivity::class.java))
            finish()
        }
    }

    fun isAccessibilityServiceEnabled1(context: Context, serviceClass: Class<*>): Boolean {
        val am: AccessibilityManager =
            context.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices: List<AccessibilityServiceInfo> =
            AccessibilityManagerCompat.getEnabledAccessibilityServiceList(
                am,
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            )
        for (serviceInfo in enabledServices) {
            if (serviceInfo.getResolveInfo().serviceInfo.packageName == context.packageName && serviceInfo.getResolveInfo().serviceInfo.name == serviceClass.name) {
                return true
            }
        }
        return false
    }

    override fun onStringFragmentInteraction(data: String?) {
        Log.d("OwnerShip Data", data!!)
        binding!!.etOwnerShip.setText(data)
    }

    override fun onStringFragmentInteractionEdu(data: String?) {
        Log.d("Education Data", data!!)
        binding!!.txEdu.setText(data)
    }

    override fun onInitializationSuccess() {
        Log.d("VISION INITIALISED", "SUCCESS")
        visionResult = true
        val sps = SharedPrefSettings()
        sps.storeIsVTION(false)
        val data = ResultVtionSdkModel(mobileNumber, "Success", false)
        mDatabase!!.child("result").child(mobileNumber!!).setValue(data).addOnCompleteListener(
            OnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    println("Data written successfully")
                } else {
                    println("Error writing data: " + task.exception!!.message)
                }
            })
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
            .addFormDataPart("name", binding!!.etName.text.toString())
            .addFormDataPart("email", binding!!.etEmail.text.toString())
            .addFormDataPart("address", binding!!.etAddress.text.toString())
            .addFormDataPart("city", binding!!.etCity.text.toString())
            .addFormDataPart("pincode", binding!!.etPincode.text.toString())
            .addFormDataPart("dob", sdf.format(myCalendar.time))
            .addFormDataPart("gender", gender)
            .addFormDataPart("IsVtionUser", "1")
            .build()
        profileViewModel?.updateProfile(requestBody)
    }

    override fun onInitializationFailure(message : String) {
        Log.d("VISION INITIALISED", "FAILED")
        visionResult = false
        val sps = SharedPrefSettings()
        sps.storeIsVTION(false)
        val result = ResultVtionSdkModel(mobileNumber, "Failed", false)
        mDatabase!!.child("result").child(mobileNumber!!).setValue(result).addOnCompleteListener(
            OnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    println("Data written successfully")
                } else {
                    println("Error writing data: " + task.exception!!.message)
                }
            })
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
            .addFormDataPart("name", binding!!.etName.text.toString())
            .addFormDataPart("email", binding!!.etEmail.text.toString())
            .addFormDataPart("address", binding!!.etAddress.text.toString())
            .addFormDataPart("city", binding!!.etCity.text.toString())
            .addFormDataPart("pincode", binding!!.etPincode.text.toString())
            .addFormDataPart("dob", sdf.format(myCalendar.time))
            .addFormDataPart("gender", gender)
            .addFormDataPart("IsVtionUser", "0")
            .build()
        profileViewModel?.updateProfile(requestBody)
    }

    fun checkGpsAndRequestLocation() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val task: Task<LocationSettingsResponse> =
            settingsClient!!.checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response: LocationSettingsResponse? = task.getResult(
                    ApiException::class.java
                )
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvable: ResolvableApiException = e as ResolvableApiException
                        try {
                            resolvable.startResolutionForResult(
                                this@VitionUserDataActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sendIntentException: IntentSender.SendIntentException) {
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    interface AdvertisingIdCallback {
        fun onAdvertisingIdRetrieved(advertisingId: String?)
    }

    interface AgeCallback {
        fun onAgeRetrieved(age: Int?)
    }

    private fun decode(text: String): String {
        return text.replace("&amp;", "&")
    }

    private fun moveToDashboard() {
        val commonUtils = CommonUtils()
        val (_, _, _, _, _, _, mobile) = commonUtils.userInfo!!
        val netcoreUserDetails = NetcoreUserDetails(this)
        netcoreUserDetails.setNetcoreUserIdentityAndLogin(mobile ?: "")
        netcoreUserDetails.updateNetcoreUserProfile()
        Logger.e("data", "save to netcore")
        startActivity(Intent(this@VitionUserDataActivity, InterestedTopicsActivity::class.java))
        finish()
    }

    private fun showFreeTrialDialogNormal() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes = dialog.window!!.attributes
            dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
            val startFreeTrial: TextView = dialog.findViewById<TextView>(R.id.tv_startfreetrial)
           /* val daysTrials: TextView = dialog.findViewById<TextView>(R.id.tv_daysTrials)
            val typeOfVoucher: TextView = dialog.findViewById<TextView>(R.id.tv_typeOfVoucher)
            try {
                val authConfigResponse = CommonUtils.authconfig_response(this)
                daysTrials.setText(
                    """
    ${authConfigResponse.freeTrialPeriodDays}
    DAYS
    """.trimIndent()
                )
                typeOfVoucher.setText(authConfigResponse.freeTrialText)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }*/
            startFreeTrial.setText("Your" + AppConstants.FREE_TRAIL_NORMAL_DAYS + "Days\nFree Trial has been\nActivated.")
            val authConfigResponse = CommonUtils.authconfig_response(this)
            if (authConfigResponse != null) {
                var freeTrialDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()!!.freeTrialDialogText
                freeTrialDialogText = freeTrialDialogText!!.replace("\\\\n", "\n")
                freeTrialDialogText = freeTrialDialogText.replace("<", "")
                freeTrialDialogText = freeTrialDialogText.replace(">", "")
                freeTrialDialogText =
                    freeTrialDialogText.replace("Trial Days", AppConstants.FREE_TRAIL_NORMAL_DAYS)
                startFreeTrial.setText(decode(freeTrialDialogText))
            }
            val llStartFreeTrial: LinearLayout =
                dialog.findViewById<LinearLayout>(R.id.llStartFreeTrial)
            llStartFreeTrial.setOnClickListener(View.OnClickListener { v: View? -> dialog.dismiss() })
            dialog.show()
            dialog.setOnDismissListener(DialogInterface.OnDismissListener { dialog1: DialogInterface? -> moveToDashboard() })
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun showFreeTrialDialogVtion() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes = dialog.window!!.attributes
            dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
            val startFreeTrial: TextView = dialog.findViewById<TextView>(R.id.tv_startfreetrial)
          /*  val daysTrials: TextView = dialog.findViewById<TextView>(R.id.tv_daysTrials)
            val typeOfVoucher: TextView = dialog.findViewById<TextView>(R.id.tv_typeOfVoucher)
            val ivFreeTrial = dialog.findViewById<ImageView>(R.id.ivFreeTrial)*/
           /* try {
                val authConfigResponse = CommonUtils.authconfig_response(this)
                daysTrials.setText(
                    """
    ${AppConstants.FREE_TRAIL_VTION_DAYS}
    DAYS
    """.trimIndent()
                )
                typeOfVoucher.setText(authConfigResponse.freeTrialText)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }*/
            startFreeTrial.setText("Your" + AppConstants.FREE_TRAIL_VTION_DAYS + "Days\nFree Trial has been\nActivated.")
            val authConfigResponse = CommonUtils.authconfig_response(this)
            if (authConfigResponse != null) {
                var freeTrialDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()!!.freeTrialDialogText
                freeTrialDialogText = freeTrialDialogText!!.replace("\\\\n", "\n")
                freeTrialDialogText = freeTrialDialogText.replace("<", "")
                freeTrialDialogText = freeTrialDialogText.replace(">", "")
                freeTrialDialogText =
                    freeTrialDialogText.replace("Trial Days", AppConstants.FREE_TRAIL_VTION_DAYS)
                startFreeTrial.setText(decode(freeTrialDialogText))
            }
            val llStartFreeTrial: LinearLayout =
                dialog.findViewById<LinearLayout>(R.id.llStartFreeTrial)
            llStartFreeTrial.setOnClickListener(View.OnClickListener { v: View? -> dialog.dismiss() })
            dialog.show()
            dialog.setOnDismissListener(DialogInterface.OnDismissListener { dialog1: DialogInterface? -> moveToDashboard() })
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val REQUEST_CHECK_SETTINGS = 100
    }



}