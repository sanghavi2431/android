package `in`.woloo.www.v2.profile.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.netcore.android.Smartech
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityProfileBinding
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.utils.Logger

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    private val GALLERY = 1
    private val CAMERA = 2

    private val CAMERA_PERMISSION_REQUEST_CODE = 200
    private val GALLERY_PERMISSION_REQUEST_CODE = 300

    private var mediaListIndex: Int = 0

    private var adharImage: File? = null
    private var panImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        setContentView(binding.root)
        setProgressBar()
        initViews()
        setNetworkDetector()
        setLiveData()
        setClickables()
    }

    private fun initViews() {
        if(CommonUtils().userInfo?.mobile != null) {
            binding.phone.setText(CommonUtils().userInfo!!.mobile.toString())
            binding.phone.isEnabled = false
            binding.phone.alpha = 0.6f
        }
        if(CommonUtils().userInfo?.email != null)
            binding.email.setText(CommonUtils().userInfo!!.email.toString())
        if(CommonUtils().userInfo?.name != null)
            binding.firstName.setText(CommonUtils().userInfo!!.name.toString())
    }

    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                takePhotoFromCamera()
            }
            GALLERY_PERMISSION_REQUEST_CODE -> {
                choosePhotoFromGallery()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            GALLERY_PERMISSION_REQUEST_CODE
        )
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        if (!checkCameraPermission()) {
            requestCameraPermission()
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }
    }

    private fun setLiveData() {
        profileViewModel.observeEditProfile().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                it.data?.wolooGuest?.response?.let { it1 ->
                    displayToast(it1)
                    //Saving User Profile  Data to Netcore
                    val payload: HashMap<String, Any> = HashMap()
                    payload["NAME"] =  binding.firstName.text.trim().toString() + " " + binding.lastName.text.trim().toString()
                    payload["EMAIL"] = binding.email.text.trim().toString()
                    Smartech.getInstance(WeakReference(this)).updateUserProfile(payload)

                }
                finish()
            }
        })
    }

    private fun setClickables() {
        binding.uploadAdhar.setOnClickListener {
            mediaListIndex = 1
            showPictureDialog()
        }
        binding.uploadPan.setOnClickListener {
            mediaListIndex = 2
            showPictureDialog()
        }
        binding.btnRedeem.setOnClickListener {
            validateFormData()
        }
        binding.dob.transformIntoDatePicker(this, "yyyy/MM/dd", Date())
    }

    private fun validateFormData() {
        if (binding.firstName.text.trim().toString().isEmpty()){
            displayToast("Please enter your first name")
            return
        }
        if (binding.lastName.text.trim().toString().isEmpty()){
            displayToast("Please enter your last name")
            return
        }
        if (binding.email.text.trim().toString().isEmpty()){
            displayToast("Please enter your email")
            return
        }
        if (!isValidEmail(binding.email.text.trim().toString())){
            displayToast("Please enter a valid email")
            return
        }
        if (binding.phone.text.trim().toString().isEmpty()){
            displayToast("Please enter your mobile number")
            return
        }
        if(!isValidMobile(binding.phone.text.trim().toString())){
            displayToast("Please enter a valid mobile number")
            return
        }
        if (binding.streetAddress.text.trim().toString().isEmpty()){
            displayToast("Please enter your address")
            return
        }
        if (binding.city.text.trim().toString().isEmpty()){
            displayToast("Please enter your city")
            return
        }
        if (binding.state.text.trim().toString().isEmpty()){
            displayToast("Please enter your state")
            return
        }
        if (binding.zip.text.trim().toString().isEmpty()){
            displayToast("Please enter your pin code")
            return
        }
        if (binding.dob.text.trim().toString().isEmpty()){
            displayToast("Please select your date of birth")
            return
        }
        if (adharImage == null){
            displayToast("Please upload your Aadhaar card")
            return
        }
        if (panImage == null){
            displayToast("Please upload your Pan card")
            return
        }
        createRequest()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun createRequest() {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
            .addFormDataPart("name", binding.firstName.text.trim().toString() + " " + binding.lastName.text.trim().toString())
            .addFormDataPart("email",  binding.email.text.trim().toString())
            .addFormDataPart("mobile", CommonUtils().userInfo!!.mobile.toString())
            .addFormDataPart("address", binding.streetAddress.text.trim().toString())
            .addFormDataPart("city", binding.city.text.trim().toString())
            .addFormDataPart("state", binding.state.text.trim().toString())
            .addFormDataPart("pincode", binding.zip.text.trim().toString())
            .addFormDataPart("dob", binding.dob.text.trim().toString())
            .addFormDataPart("aadhar_url", adharImage?.name, RequestBody.create(
                "image/png".toMediaTypeOrNull(),
                adharImage!!
            ))
            .addFormDataPart("pan_url", adharImage?.name, RequestBody.create(
                "image/png".toMediaTypeOrNull(),
                panImage!!
            ))
            .build()
        profileViewModel.updateProfile(requestBody)
    }

    companion object {
        private val ADHAR_DIRECTORY = "/adhar_documents"
        private val PAN_DIRECTORY = "/pan_documents"
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        var directoryName = when (mediaListIndex) {
            1 -> ADHAR_DIRECTORY
            2 -> PAN_DIRECTORY
            else -> "data"
        }

        val directory = File(
            filesDir.toString() + directoryName
        )
        // have the object build the directory structure, if needed.
        if (!directory.exists()) {
            directory.mkdirs()
        }
        try {

            val name = when (mediaListIndex) {
                1 -> "ADHAR"
                2 -> "PAN"
                else -> {
                    ""
                }
            } + ".jpg"
            Logger.d("heel", directory.toString())
            val f = File(
                directory, name
            )
            if (f.exists())
                f.delete()
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Logger.d("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            CommonUtils.printStackTrace(e1)
        }
        return ""
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        displayToast("Please submit all the information before exploring the app")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        when (mediaListIndex) {
                            1 -> {
                                binding.adharImage.setImageBitmap(bitmap)
                                adharImage = File(saveImage(bitmap))
                            }
                            2 -> {
                                binding.panImage.setImageBitmap(bitmap)
                                panImage = File(saveImage(bitmap))
                            }
                        }
                    } catch (e: IOException) {
                        CommonUtils.printStackTrace(e)
                        displayToast("Image saving failed")
                    }
                }
            } else if (requestCode == CAMERA) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                when (mediaListIndex) {
                    1 -> {
                        binding.adharImage.setImageBitmap(thumbnail)
                        adharImage = File(saveImage(thumbnail))
                    }
                    2 -> {
                        binding.panImage.setImageBitmap(thumbnail)
                        panImage = File(saveImage(thumbnail))
                    }
                }
            }
        }
    }

    private fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}