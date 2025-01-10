package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.mvp.EditProfilePresenter
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class SelectGenderActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.btnNext)
    var btnNext: TextView? = null

    @JvmField
    @BindView(R.id.etName)
    var etName: EditText? = null

    @JvmField
    @BindView(R.id.etGender)
    var etGender: AutoCompleteTextView? = null


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

   /* @JvmField
    @BindView(R.id.llMobile)
    var llMobile: LinearLayout? = null

    @JvmField
    @BindView(R.id.llEmail)
    var llEmail: LinearLayout? = null*/
    private val editProfilePresenter: EditProfilePresenter? = null
    private var profileViewModel: ProfileViewModel? = null
    private var gender = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_gender)
        ButterKnife.bind(this)
        initView()

        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_dropdown_item_1line
        )
        etGender!!.setAdapter(genderAdapter)
        etGender!!.threshold = 0
        etGender!!.setOnItemClickListener { parent, view, position, id ->
            gender = parent.getItemAtPosition(position).toString()
        }

        setLiveData()
    }

    private fun setLiveData() {
        profileViewModel?.observeEditProfile()?.observe(
            this,
            Observer<Any?> { //  startActivity(new Intent(SelectGenderActivity.this, InterestedTopicsActivity.class));
                showFreeTrialDialog()
            })
    }

    private fun initView() {
        profileViewModel = ViewModelProvider(this).get<ProfileViewModel>(
            ProfileViewModel::class.java
        )
        etMobile!!.setText(CommonUtils().userInfo!!.mobile)
        etCity!!.setOnClickListener { v: View? ->
            try {
                if (!Places.isInitialized()) {
                    val key = CommonUtils.googlemapapikey(this)
                    Places.initialize(this, key)
                }
                val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .setTypeFilter(TypeFilter.CITIES)
                    .build(this)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
        val myCalendar = Calendar.getInstance()
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            val myFormat = "dd MMM yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            etDob!!.setText(sdf.format(myCalendar.time))
        }
        etDob!!.setOnClickListener { v: View? ->
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
        btnNext!!.setOnClickListener { v: View? ->
            if (isValid) {
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
                    btnNext!!.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
                    val myFormat = "yyyy-MM-dd" //In which you need put here
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    val requestBody: RequestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
                        .addFormDataPart("name", etName!!.text.toString())
                        .addFormDataPart("email", etEmail!!.text.toString())
                        .addFormDataPart("address", etAddress!!.text.toString())
                        .addFormDataPart("city", etCity!!.text.toString())
                        .addFormDataPart("pincode", etPincode!!.text.toString())
                        .addFormDataPart("dob", sdf.format(myCalendar.time))
                        .addFormDataPart("gender", gender)
                        .addFormDataPart("IsVtionUser", "0")
                        .build()
                    profileViewModel!!.updateProfile(requestBody)
                    //                    editProfilePresenter.editProfile(SelectGenderActivity.this, mJsObjParam);
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
                //                editProfilePresenter.editProfile(this, mJsObjParam);
            }
        }
    }

    private val isValid: Boolean
        private get() {
            if(etGender!!.text.toString().isNotEmpty())
            {
                gender = etGender!!.text.toString()
            }
            val mail = etEmail!!.text.toString().replace("\\s+".toRegex(), "")
            if (!TextUtils.isEmpty(etEmail!!.text.toString()) && !Patterns.EMAIL_ADDRESS.matcher(
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
            if (etCity!!.text.toString() == "") {
                // etCity.setError("please enter City");
                Toast.makeText(applicationContext, "Please enter city ", Toast.LENGTH_SHORT).show()
                return false
            }
            if (etPincode!!.text.toString() == "") {
                // etPincode.setError("please enter pincode");
                Toast.makeText(applicationContext, "Please enter pincode ", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (etPincode!!.text.toString().length < 6) {
                //etPincode.setError("please enter valid pincode");
                Toast.makeText(
                    applicationContext,
                    "Please enter valid pincode ",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (etAddress!!.text.toString() == "") {
                //etAddress.setError("please enter address");
                Toast.makeText(applicationContext, "Please enter address ", Toast.LENGTH_SHORT)
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
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(
                    data!!
                )
                etCity!!.setText(place.name)
                etPincode!!.requestFocus()
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(
                    data!!
                )
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun moveToDashboard() {
        val commonUtils = CommonUtils()
        val (_, _, _, _, _, _, mobile) = commonUtils.userInfo!!
        val netcoreUserDetails = NetcoreUserDetails(this)
        netcoreUserDetails.setNetcoreUserIdentityAndLogin(mobile ?: "")
        netcoreUserDetails.updateNetcoreUserProfile()
        Logger.e("data", "save to netcore")
        startActivity(Intent(this@SelectGenderActivity, InterestedTopicsActivity::class.java))
        finish()
    }

    private fun showFreeTrialDialog() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_start_free_trial)
            dialog.window!!.attributes = dialog.window!!.attributes
            dialog.window!!.setWindowAnimations(R.style.DialogAnimation)
            val startFreeTrial = dialog.findViewById<TextView>(R.id.tv_startfreetrial)
           /* val daysTrials = dialog.findViewById<TextView>(R.id.tv_daysTrials)
            val typeOfVoucher = dialog.findViewById<TextView>(R.id.tv_typeOfVoucher)
            val ivFreeTrial = dialog.findViewById<ImageView>(R.id.ivFreeTrial)
            try {
                val authConfigResponse = CommonUtils.authconfig_response(this)
                daysTrials.text = """
                    ${authConfigResponse.freeTrialPeriodDays}
                    DAYS
                    """.trimIndent()
                typeOfVoucher.text = authConfigResponse.freeTrialText
                // ImageUtil.loadImage(
                //     this,
                //     ivFreeTrial,
                //     authConfigResponse.getuRLS().getFreeTrialImageUrl()
                // );
                // tv_daysTrials.setText(authConfigResponse.getFreeTrialPeriodDays() + "\nDays");
                // ivFreeTrial.setImageResource(R.drawable.free_trial_image);
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }*/

            startFreeTrial.setText("Your" + AppConstants.FREE_TRAIL_NORMAL_DAYS + "Days\nFree Trial has been\nActivated.")
            val authConfigResponse = CommonUtils.authconfig_response(this)
            if (authConfigResponse != null) {
                var freeTrialDialogText = authConfigResponse.getcUSTOMMESSAGE()?.freeTrialDialogText
                freeTrialDialogText = freeTrialDialogText?.replace("\\\\n", "\n")
                freeTrialDialogText = freeTrialDialogText?.replace("<", "")
                freeTrialDialogText = freeTrialDialogText?.replace(">", "")
                freeTrialDialogText =
                    freeTrialDialogText?.replace("Trial Days", AppConstants.FREE_TRAIL_NORMAL_DAYS)
                startFreeTrial.text = decode(freeTrialDialogText.toString())
            }
            val llStartFreeTrial = dialog.findViewById<LinearLayout>(R.id.llStartFreeTrial)
            llStartFreeTrial.setOnClickListener { v: View? -> dialog.dismiss() }
            dialog.show()
            dialog.setOnDismissListener { dialog1: DialogInterface? ->
                moveToDashboard()
                dialog.dismiss()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun decode(text: String): String {
        return text.replace("&amp;", "&")
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}