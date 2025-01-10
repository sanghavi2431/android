package `in`.woloo.www.more.editprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.mvp.EditProfilePresenter
import `in`.woloo.www.more.editprofile.mvp.EditProfileView
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.subscribe.adapter.SubscribeAdapter
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity(), EditProfileView, TextWatcher,
    View.OnTouchListener {
    @JvmField
    @BindView(R.id.tvSubmit)
    var tvSubmit: TextView? = null

    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.etName)
    var etName: EditText? = null


    @JvmField
    @BindView(R.id.etEmail)
    var etEmail: EditText? = null

    @JvmField
    @BindView(R.id.etMobile)
    var etMobile: EditText? = null

    @JvmField
    @BindView(R.id.etCity)
    var etCity: EditText? = null

    @JvmField
    @BindView(R.id.etPincode)
    var etPincode: EditText? = null

    @JvmField
    @BindView(R.id.etAddress)
    var etAddress: EditText? = null

    @JvmField
    @BindView(R.id.etDob)
    var etDob: EditText? = null

    @JvmField
    @BindView(R.id.etGender)
    var etGender: AutoCompleteTextView? = null


    private var gender = ""

    private var editProfilePresenter: EditProfilePresenter? = null
    private var profileViewModel: ProfileViewModel? = null

    private var from_subscription = false
    private var planId: String? = ""
    private var id = 0
    private var msg: String? = null
    private var isEmail = false
    private var mobile: String? = null
    var myCalendar: Calendar? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        ButterKnife.bind(this)
        myCalendar = Calendar.getInstance()
        val genderAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_dropdown_item_1line
        )
        etGender!!.setAdapter(genderAdapter)
        etGender!!.threshold = 1
        etGender!!.setOnClickListener {
            gender =
                etGender!!.text.toString()
        }

        initViews()
        setLiveData()
    }

    /*calling initViews*/
    @SuppressLint("SimpleDateFormat")
    private fun initViews() {
        try {
            Logger.i(TAG, "initViews")
            editProfilePresenter = EditProfilePresenter(this, this@EditProfileActivity)
            profileViewModel = ViewModelProvider(this).get(
                ProfileViewModel::class.java
            )
            val viewProfileResponse: UserProfile =
                WolooApplication.instance!!.profileResponse!!
            from_subscription = intent.getBooleanExtra(AppConstants.FROM_SUBSCRIPTION, false)
            planId = intent.getStringExtra(AppConstants.PLAN_ID)
            id = intent.getIntExtra(AppConstants.ID, 0)
            msg = intent.getStringExtra(AppConstants.MSG)
            isEmail = intent.getBooleanExtra(AppConstants.IS_EMAIL, false)
            mobile = intent.getStringExtra(AppConstants.MOBILE)
            try {
                etName!!.setText(viewProfileResponse.profile!!.name)
                //if(!TextUtils.isEmpty(viewProfileResponse.getProfile().getEmail())){
                etEmail!!.setText(viewProfileResponse.profile!!.email)
                //llEmail.setVisibility(View.VISIBLE);
                /*}else{
                    llEmail.setVisibility(View.GONE);
                }*/
                if (!TextUtils.isEmpty(viewProfileResponse.profile!!.mobile)) {
                    etMobile!!.setText(viewProfileResponse.profile!!.mobile)
                } else {
                }
                etCity!!.setText(viewProfileResponse.profile!!.city)
                etPincode!!.setText(viewProfileResponse.profile!!.pincode)
                etAddress!!.setText(viewProfileResponse.profile!!.address)
                if (!TextUtils.isEmpty(viewProfileResponse.profile!!.dob)) {
                    val inputPattern = "yyyy-MM-dd"
                    val outputPattern = "dd MMM yyyy"
                    val date = SimpleDateFormat(inputPattern).parse(
                        viewProfileResponse.profile!!.dob
                    )
                    etDob!!.setText(SimpleDateFormat(outputPattern).format(date))
                }
                tvTitle!!.text = getString(R.string.edit_profile)
                etGender!!.setText(viewProfileResponse.profile!!.gender.toString())
                // setGenderSelection(viewProfileResponse.getProfile().getGender());
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            try {
                ivBack!!.setOnClickListener { v: View? ->
                    onBackPressed()
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
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
            tvSubmit!!.setOnClickListener { v: View? ->
                if (isValid) {
                    try {
                        val myFormat = "yyyy-MM-dd" //In which you need put here
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        val dob = ""
                        /*  if (!TextUtils.isEmpty(etDob.getText())) {
          String inputPattern = "yyyy-MM-dd";
          String outputPattern = "yyyy-MM-dd";
          Date date = null;
          date = new SimpleDateFormat(outputPattern).parse(etDob.getText().toString());
          dob = new SimpleDateFormat(outputPattern).format(date);
      }*/
                        val requestBody: RequestBody = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart(
                                "id",
                                CommonUtils().userInfo!!.id.toString()
                            )
                            .addFormDataPart("name", etName!!.text.toString())
                            .addFormDataPart("email", etEmail!!.text.toString())
                            .addFormDataPart("address", etAddress!!.text.toString())
                            .addFormDataPart("city", etCity!!.text.toString())
                            .addFormDataPart("pincode", etPincode!!.text.toString())
                            .addFormDataPart(
                                "dob",
                                sdf.format(myCalendar!!.time)
                            ) //                                .addFormDataPart("dob",etDob.getText().toString())
                            .addFormDataPart("gender", gender)
                            .build()
                        profileViewModel!!.updateProfile(requestBody)
                    } catch (e: Exception) {
                        throw RuntimeException(e)
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
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }

        etName!!.addTextChangedListener(this)
        etAddress!!.addTextChangedListener(this)
        //etCity.addTextChangedListener(this);
        etPincode!!.addTextChangedListener(this)


        etCity!!.setOnClickListener { v: View? ->
            try {
                if (!Places.isInitialized()) {
                    val key = CommonUtils.googlemapapikey(this)
                    Places.initialize(
                        this,
                        key
                    )
                }
                val fields =
                    Arrays.asList(
                        Place.Field.ID,
                        Place.Field.NAME
                    )
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .setTypeFilter(TypeFilter.CITIES)
                    .build(this)
                startActivityForResult(
                    intent,
                    AUTOCOMPLETE_REQUEST_CODE
                )
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }


        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar!![Calendar.YEAR] = year
                myCalendar!![Calendar.MONTH] = monthOfYear
                myCalendar!![Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "dd MMM yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                etDob!!.setText(sdf.format(myCalendar!!.time))
            }

        etDob!!.setOnClickListener { v: View? ->
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar!!
                    .get(Calendar.YEAR),
                myCalendar!![Calendar.MONTH],
                myCalendar!![Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    fun setLiveData() {
        profileViewModel!!.observeEditProfile().observe(
            this
        ) { response ->
            if (response != null) {
                editProfileSuccess()
            } else {
                Toast.makeText(
                    applicationContext,
                    WolooApplication.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                WolooApplication.errorMessage = ""
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data)
                etCity!!.setText(place.name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val isValid: Boolean
        /*calling isValid*/
        get() {
            Logger.i(TAG, "isValid")
            if (!etGender!!.text.toString().isEmpty()) {
                gender = etGender!!.text.toString()
            }


            if (!TextUtils.isEmpty(
                    etEmail!!.text.toString()
                ) && !Patterns.EMAIL_ADDRESS.matcher(etEmail!!.text.toString())
                    .matches()
            ) {
                // etCity.setError("please enter City");
                Toast.makeText(
                    applicationContext,
                    "Please enter a valid email address",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (etCity!!.text.toString() == "") {
                // etCity.setError("please enter City");
                Toast.makeText(
                    applicationContext,
                    "Please enter city ",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (etPincode!!.text.toString() == "") {
                // etPincode.setError("please enter pincode");
                Toast.makeText(
                    applicationContext,
                    "Please enter pincode ",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (etPincode!!.text.toString().length < 6) {
                //etPincode.setError("please enter valid pincode");
                Toast.makeText(
                    applicationContext,
                    "Please enter valid pincode ",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (etAddress!!.text.toString() == "") {
                //etAddress.setError("please enter address");
                Toast.makeText(
                    applicationContext,
                    "Please enter address ",
                    Toast.LENGTH_SHORT
                ).show()
            } else return !TextUtils.isEmpty(gender)
            return false
        }

    /*calling editProfileSuccess*/
    override fun editProfileSuccess() {
        Logger.i(TAG, "editProfileSuccess")
        try {
            if (from_subscription) {
//                TODO initSubscription
                editProfilePresenter!!.initSubscription(
                    id,
                    planId,
                    object : SubscribeAdapter.InitSubscriptionCallback {
                        override fun initSubscriptionSuccess(initSubscriptionResponse: InitSubscriptionResponse?) {
                            try {
                                if (initSubscriptionResponse?.data != null) {
                                    val subscriptionId =
                                        initSubscriptionResponse.data!!.subscriptionId
                                    if (!TextUtils.isEmpty(subscriptionId)) {
                                        CommonUtils.navigateToRazorPayFlow(
                                            this@EditProfileActivity,
                                            planId,
                                            subscriptionId,
                                            msg,
                                            isEmail,
                                            mobile,
                                            true,
                                            null,
                                            false,
                                            false
                                        )
                                        this@EditProfileActivity.finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            this@EditProfileActivity.resources.getString(R.string.subscription_validation_profile),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        this@EditProfileActivity.resources.getString(R.string.subscription_validation_profile),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (ex: Exception) {
                                CommonUtils.printStackTrace(ex)
                            }
                        }
                    })
            } else {
                val user = CommonUtils().userInfo
                user!!.name = etName!!.text.toString().trim { it <= ' ' }
                user.email = etEmail!!.text.toString().trim { it <= ' ' }
                getPreferences.storeUserDetails(user)

                //                LoginResponse loginResponse = new CommonUtils().getUserInfo(EditProfileActivity.this);
//                SharedPreference mSharedPreference = new SharedPreference(this);
//                loginResponse.getData().getUser().setName(etName.getText().toString().trim());
//                loginResponse.getData().getUser().setEmail(etEmail.getText().toString().trim());
//                String userInfo = new Gson().toJson(loginResponse);
//                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.USER_INFO.getPreferenceKey(), userInfo);
                Toast.makeText(applicationContext, "Profile updated..", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling beforeTextChanged*/
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        Logger.i(TAG, "beforeTextChanged")
        showSubmitButtonActive()
    }

    /*calling onTextChanged*/
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Logger.i(TAG, "onTextChanged")
        showSubmitButtonActive()
    }


    override fun afterTextChanged(s: Editable) {
        showSubmitButtonActive()
    }

    /*calling showSubmitButtonActive*/
    private fun showSubmitButtonActive() {
        Logger.i(TAG, "showSubmitButtonActive")
        try {
            tvSubmit!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            tvSubmit!!.background =
                resources.getDrawable(R.drawable.yellow_rectangle_shape)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling onTouch*/
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        Logger.i(TAG, "onTouch")
        showSubmitButtonActive()
        return false
    }


    private fun setGenderSelection(gender: String) {
        var radioButtonId = -1
        when (gender) {
            "Male" -> radioButtonId = R.id.rbMale
            "Female" -> radioButtonId = R.id.rbFemale
            "Other" -> radioButtonId = R.id.rbOther
        }

        if (radioButtonId != -1) {
            val radioButton = findViewById<RadioButton>(radioButtonId)
            radioButton.isChecked = true
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
        var TAG: String = EditProfileActivity::class.java.simpleName
    }
}