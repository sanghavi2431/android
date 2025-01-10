package `in`.woloo.www.more.refer_woloo_host

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.VolleyError
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.model.lists_models.ReverseGeocodeItem
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.FileUploadResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse
import `in`.woloo.www.more.refer_woloo_host.mvp.ReferredWolooView
import `in`.woloo.www.more.woloo_host.model.GeoCodeResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.IOnCallWSCallBack
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ProfileAPIUtil
import `in`.woloo.www.v2.woloo.viewmodel.WolooViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale

class referWolooFormActivity : AppCompatActivity(), ReferredWolooView, OnMapReadyCallback,
    NetworkAPIResponseCallback {
    @JvmField
    @BindView(R.id.etName)
    var etName: EditText? = null

    @JvmField
    @BindView(R.id.etOwnerName)
    var etOwnerName: EditText? = null

    @JvmField
    @BindView(R.id.etAddress)
    var etAddress: EditText? = null

    @JvmField
    @BindView(R.id.etCity)
    var etCity: EditText? = null

    @JvmField
    @BindView(R.id.etZipCode)
    var etZipCode: EditText? = null

    @JvmField
    @BindView(R.id.tvSubmit)
    var tvSubmit: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    /* @BindView(R.id.marker_image_view)
    ImageView markerImage;*/
    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.image_left_Imv)
    var imvLeft: ImageView? = null

    @JvmField
    @BindView(R.id.delete_left_Imv)
    var imvLeftDelete: ImageView? = null

    @JvmField
    @BindView(R.id.plus_left_imv)
    var imvLeftPlus: ImageView? = null

    @JvmField
    @BindView(R.id.image_right_Imv)
    var imvRight: ImageView? = null

    @JvmField
    @BindView(R.id.delete_right_Imv)
    var imvRightDelete: ImageView? = null

    @JvmField
    @BindView(R.id.etPhone)
    var etPhone: EditText? = null

    @JvmField
    @BindView(R.id.plus_right_imv)
    var imvRightPlus: ImageView? = null

    /* @BindView(R.id.transparent_image)
    ImageView transparent_image;*/
    @JvmField
    @BindView(R.id.ivCurrentLocation)
    var ivCurrentLocation: ImageView? = null

    // TODO: Rename and change types of parameters
    private val mParam1: String? = null
    private val mParam2: String? = null
    private val REQUEST_CAMERA = 0
    private val REQUEST_GALLERY = 1
    var filePath: String? = null
    var picUri: Uri? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    //    private ReferredWolooPresenter referWolooHostPresenter;
    private var wolooViewModel: WolooViewModel? = null
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private var map: GoogleMap? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private val showLatLong = true
    private val zoom = 20.0f
    private var changeLocationTextFlag = false
    private var changeTextFromMapFlag = false
    var uploadedImages = JSONArray()
    private var isGeoCodeFroLatLong = false
    private var isGeoCodeFroAddress = false
    var imgLeftFile: File? = null
    var imgRightFile: File? = null
    var selectedProfileImageUri = Uri.parse("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_woloo_form)
        ButterKnife.bind(this)
        wolooViewModel = ViewModelProvider(this).get(
            WolooViewModel::class.java
        )
        initViews()
        setLiveData()
        Logger.i(TAG, "onCreateView")
    }

    /*calling on initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        try {
            Logger.i(TAG, "initViews")
            tvTitle!!.text = "Refer a Woloo Host"
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
            deviceLocation
            tvSubmit!!.setOnClickListener { v: View? ->
                if (isValid) {

//                    if(imvLeftDelete.getVisibility() == View.VISIBLE)
//                        uploadImages(((BitmapDrawable)imvLeft.getDrawable()).getBitmap(),iOnLeftCallWSCallBack);
                    CommonUtils().showProgress(context)
                    if (imvLeftDelete!!.visibility == View.VISIBLE) {
                        val requestBody = MultipartBody.Builder()
                        requestBody.setType(MultipartBody.FORM)
                        requestBody.addFormDataPart("name", etName!!.text.toString())
                        requestBody.addFormDataPart("city", etCity!!.text.toString())
                        requestBody.addFormDataPart(
                            "address", etAddress!!.text.toString() + " "
                                    + etCity!!.text.toString() + " " + etZipCode!!.text.toString() + " India"
                        )
                        requestBody.addFormDataPart("lat", latitude.toString())
                        requestBody.addFormDataPart("lng", longitude.toString())
                        requestBody.addFormDataPart("pincode", etZipCode!!.text.toString())
                        requestBody.addFormDataPart("recommended_mobile", etPhone!!.text.toString())
                        requestBody.addFormDataPart(
                            "image", imgLeftFile!!.name,
                            RequestBody.create(
                                "application/octet-stream".toMediaTypeOrNull(),
                                imgLeftFile!!
                            )
                        )

//                        requestBody.addFormDataPart("[]",imgLeftFile.getName(), RequestBody.create(imgLeftFile,
//                                        MediaType.parse("image/png")));
                        if (imgRightFile != null) {
                            requestBody.addFormDataPart(
                                "image", imgRightFile!!.name,
                                RequestBody.create(
                                    "application/octet-stream".toMediaTypeOrNull(),
                                    imgRightFile!!
                                )
                            )

//                            requestBody.addFormDataPart("image[]",imgRightFile.getName(), RequestBody.create(imgRightFile,
//                                    MediaType.parse("image/png")));
                        }
                        wolooViewModel!!.recommendWoloo(requestBody.build())
                        //                        uploadImages(((BitmapDrawable) imvLeft.getDrawable()).getBitmap(), iOnLeftCallWSCallBack);
                    }
                }
            }
            ivBack!!.setOnClickListener { v: View? -> activity.onBackPressed() }
            imvLeft!!.setOnClickListener { view: View? ->
                /* if (checkAndRequestPermissions()) {
                    selectImage();
                }*/showImageUploadDialog()
            }
            imvRight!!.setOnClickListener { view: View? ->
                /* if (checkAndRequestPermissions()) {
                    selectImage();
                }*/showImageUploadDialog()
            }
            imvRightDelete!!.setOnClickListener { view: View? -> deleteImage(false) }
            imvLeftDelete!!.setOnClickListener { view: View? -> deleteImage(true) }
            etName!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    val len = s.length
                    if (len >= 1) {
                        tvSubmit!!.setTextColor(ContextCompat.getColor(context, R.color.black))
                        tvSubmit!!.setBackgroundResource(R.drawable.yellow_rectangle_shape)
                    } else {
                        tvSubmit!!.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.text_color_five
                            )
                        )
                        tvSubmit!!.setBackgroundResource(R.drawable.black_rectangle_shape)
                    }
                }
            })

            /*  transparent_image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                        case MotionEvent.ACTION_MOVE:
                            // Disallow ScrollView to intercept touch events.
                            main_scrollview.requestDisallowInterceptTouchEvent(true);
                            // Disable touch on transparent view
                            return false;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            main_scrollview.requestDisallowInterceptTouchEvent(false);
                            return true;

                        default:
                            return true;
                    }
                }
            });*/ivCurrentLocation!!.setOnClickListener { v: View? -> currentLocation }
            etCity!!.setOnClickListener { v: View? ->
                try {
                    if (!Places.isInitialized()) {
                        val key = CommonUtils.googlemapapikey(activity)
                        Places.initialize(activity, key)
                    }
                    val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
                    val intent =
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .setTypeFilter(TypeFilter.CITIES)
                            .build(activity)
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
            etZipCode!!.onFocusChangeListener =
                OnFocusChangeListener { v: View?, hasFocus: Boolean ->
                    if (!hasFocus) {
                        if (!changeTextFromMapFlag) {
                            changeMapPosition()
                            changeLocationTextFlag = true
                        } else changeTextFromMapFlag = false
                    }
                }
            etAddress!!.onFocusChangeListener =
                OnFocusChangeListener { v: View?, hasFocus: Boolean ->
                    if (!changeTextFromMapFlag) {
                        changeMapPosition()
                        changeLocationTextFlag = true
                    }
                }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun setLiveData() {
        wolooViewModel!!.observeReverseGeocoding().observe(this) { response ->
            if (response != null && response.data != null) {
                if (isGeoCodeFroLatLong) {
                    isGeoCodeFroLatLong = false
                    val dataItems: List<ReverseGeocodeItem> = response.data!!
                    for (i in dataItems.indices) {
                        if (dataItems[i].types!![0] == "postal_code") {
                            latitude = dataItems[i].geometry!!.location!!.lat
                            longitude = dataItems[i].geometry!!.location!!.lat
                            map!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitude,
                                        longitude
                                    ), zoom
                                )
                            )
                            break
                        }
                    }
                } else if (isGeoCodeFroAddress) {
                    isGeoCodeFroAddress = false
                    val dataItems: List<ReverseGeocodeItem> = response.data!!
                    for (i in dataItems.indices) {
                        if (dataItems[i].types!![0] == "postal_code") {
                            etAddress!!.setText(dataItems[i].formattedAddress)
                            val addressComponentsItems = dataItems[i].addressComponents
                            for (j in addressComponentsItems!!.indices) {
                                if (addressComponentsItems[i].types!!.contains("locality")) etCity!!.setText(
                                    addressComponentsItems[i].longName
                                )
                                break
                            }
                            for (j in addressComponentsItems.indices) {
                                if (addressComponentsItems[i].types!!.contains("postal_code")) etZipCode!!.setText(
                                    addressComponentsItems[i].longName
                                )
                                break
                            }
                            break
                        }
                    }
                }
            } else {
                WolooApplication.errorMessage = ""
            }
        }
        wolooViewModel!!.observeRecommendWoloo().observe(this) { response ->
            if (response != null && response.data != null) {
                showdialog(response.data!!.message)
            }
            CommonUtils().hideProgress()
        }
    }

    private val context: Context
        private get() = this@referWolooFormActivity

    fun uploadImages(bitmap: Bitmap?, iOnCallWSCallBack: IOnCallWSCallBack?) {
        ProfileAPIUtil(context, this).updateUserProfile(
            activity,
            bitmap,
            JetEncryptor.getInstance(),
            iOnCallWSCallBack!!,
            AppConstants.WOLOOS
        )
    }

    private val activity: Activity
        private get() = this@referWolooFormActivity
    var iOnLeftCallWSCallBack: IOnCallWSCallBack = object : IOnCallWSCallBack {
        override fun onSuccessResponse(fileUploadResponse: FileUploadResponse?) {
            if (fileUploadResponse != null && fileUploadResponse.status
                    .equals(AppConstants.API_SUCCESS, ignoreCase = true)
            ) {
                /*JSONObject mJsObjParam = new JSONObject();
                try {
                    mJsObjParam.put(JSONTagConstant.FILE_NAMES, fileUploadResponse.getConvertedName());
                    mJsObjParam.put(JSONTagConstant.PATH, fileUploadResponse.getPath());
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }*/
                uploadedImages.put("woloos/" + fileUploadResponse.convertedName)
                //                    if(imvRightDelete.getVisibility() == View.VISIBLE && uploadedImages.length()<2)
//                        uploadImages(((BitmapDrawable)imvRight.getDrawable()).getBitmap(),iOnRightCallWSCallBack);
//                    else{
////                        referWolooHostPresenter.referredWoloo(etName.getText().toString(),
////                                etCity.getText().toString(), etAddress.getText().toString(), etPhone.getText().toString() ,etZipCode.getText().toString() , uploadedImages, latitude, longitude);
//                    }

                //morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        override fun onFailure(volleyError: VolleyError?) {}
    }
    var iOnRightCallWSCallBack: IOnCallWSCallBack = object : IOnCallWSCallBack {
        override fun onSuccessResponse(fileUploadResponse: FileUploadResponse?) {
            if (fileUploadResponse != null && fileUploadResponse.status
                    .equals(AppConstants.API_SUCCESS, ignoreCase = true)
            ) {
                uploadedImages.put("woloos/" + fileUploadResponse.convertedName)
                //                referWolooHostPresenter.referredWoloo(etName.getText().toString(),
//                            etCity.getText().toString(), etAddress.getText().toString(), etPhone.getText().toString() , etZipCode.getText().toString() , uploadedImages, latitude, longitude);

                //morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        override fun onFailure(volleyError: VolleyError?) {}
    }

    private fun deleteImage(isFromLeft: Boolean) {
        if (isFromLeft) {
            if (imvRight!!.drawable == null) {
                imvLeft!!.setImageResource(0)
                imvLeftPlus!!.visibility = View.VISIBLE
                imvLeftDelete!!.visibility = View.GONE
                imgLeftFile = null
            } else {
                imvLeft!!.setImageResource(0)
                imvLeftPlus!!.visibility = View.GONE
                imvLeftDelete!!.visibility = View.VISIBLE
                imvLeft!!.setImageDrawable(imvRight!!.drawable)
                imvRight!!.setImageResource(0)
                imvRightPlus!!.visibility = View.VISIBLE
                imvRightDelete!!.visibility = View.GONE
                imgLeftFile = imgRightFile
                imgRightFile = null
            }
        } else {
            imvRight!!.setImageResource(0)
            imvRightPlus!!.visibility = View.VISIBLE
            imvRightDelete!!.visibility = View.GONE
            imgRightFile = null
        }
    }

    /*calling on showdialog*/
    fun showdialog(msg: String?) {
        try {
            Logger.i(TAG, "showdialog")
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
            val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
            tv_msg.text = msg
            btnCloseDialog.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        activity.onBackPressed()
                    }
                }
            })
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling on selectImage*/
    private fun selectImage() {
        Logger.i(TAG, "selectImage")
        val items = arrayOf<CharSequence>("Gallery", "Take Photo", "Cancel")
        val builder = AlertDialog.Builder(activity)
        //        builder.setTitle("Add Attachment!");
        builder.setIcon(R.drawable.attachment_grey_ic)
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Gallery") {
                try {
                    galleryIntent()
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            } else if (items[item] == "Take Photo") {
                try {
                    cameraIntent()
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            } /* else if (items[item].equals("File")) {
                    pickfiles();
                }*/ else  /* if (items[item].equals("Cancel"))*/ {
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        }
        builder.show()
    }

    private fun checkAndRequestPermissions(): Boolean {
        Logger.d(TAG, "checkAndRequestPermissions")
        val permissionReadStorage = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val permissionWriteStorage = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val camera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Logger.d(TAG, "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    selectImage()
                } else {
                    checkAndRequestPermissions()
                }
            }

            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    openGallery()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(
                        activity.applicationContext,
                        "Permission required to access gallery",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    takePictureIntent()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(
                        activity.applicationContext,
                        "Permission required to access camera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /*calling on cameraIntent*/
    private fun cameraIntent() {
        Logger.i(TAG, "cameraIntent")
        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        tmpFileUri = getOutputMediaFile(1)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri)
        pictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(
            pictureIntent,
            REQUEST_CAMERA
        )
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    /*calling on getOutputMediaFile*/
    fun getOutputMediaFile(type: Int): Uri? {
        /*Logger.i(TAG, "getOutputMediaFile");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            String imageStoragePath = mediaStorageDir + "/Images";
            createDirectory(imageStoragePath);
            mediaFile = new File(imageStoragePath, "IMG" + timeStamp + ".jpg");
        } else if (type == 2) {
            String videoStoragePath = mediaStorageDir + "/Videos";
            createDirectory(videoStoragePath);
            mediaFile = new File(videoStoragePath, "VID" + timeStamp + ".MP4");
        } else {
            return null;
        }


        tmpFileUri = Uri.fromFile(mediaFile);
//		return Uri.fromFile(mediaFile) ;
        Uri photoURI = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                mediaFile);
        return photoURI;*/
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val imageFileName = "IMG$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
        }

        // Save a file: path for use with ACTION_VIEW intents
        return if (image != null) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", image)
        } else null
    }

    /*calling on galleryIntent*/
    private fun galleryIntent() {
        Logger.i(TAG, "galleryIntent")
        val i = Intent(
            Intent.ACTION_PICK /*, MediaStore.Images.Media.EXTERNAL_CONTENT_UR*/
        )
        i.setType("image/*")
        //        startActivityForResult(intent, SELECT_PICTURE);
//        Intent intent = new Intent();
//        i.setAction(Intent.ACTION_GET_CONTENT);//

//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        startActivityForResult(i, REQUEST_GALLERY)
    }

    /*calling on getcamera*/
    @SuppressLint("CheckResult")
    private fun getcamera(data: Intent) {
        Logger.e("Inside camera", " intent")
        Logger.i(TAG, "getcamera")
        try {
            bbmp = data.extras!!["data"] as Bitmap?
        } catch (e: Exception) {
            try {
                val options = BitmapFactory.Options()
                options.inSampleSize = 8
                bbmp = BitmapFactory.decodeFile(
                    tmpFileUri!!.path,
                    options
                )
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
        try {
            if (tmpFileUri!!.path != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    //  tmpFileUri = getImageUri(getApplicationContext(), bbmp);
                    filePath = tmpFileUri!!.path
                    try {
                        galleryUpdatePic(tmpFileUri!!.path)
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                    try {
                        rotateCapturedImage(tmpFileUri!!.path, context)
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }

//                            try {
//                                execMultipartPost(doccode, tv_uploadtexting);
//                            } catch (Exception e) {
//                                  CommonUtils.printStackTrace(e);
//                            }
                Logger.e("filePath", filePath.toString())
                if (imvLeft!!.drawable == null) {
                    imvLeftPlus!!.visibility = View.GONE
                    imvLeftDelete!!.visibility = View.VISIBLE
                    val requestOptions = RequestOptions()
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(activity)
                        .asBitmap().load(tmpFileUri)
                        .apply(requestOptions)
                        .listener(object : RequestListener<Bitmap?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                model: Any,
                                target: Target<Bitmap?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(imvLeft!!)
                } else {
                    imvRightPlus!!.visibility = View.GONE
                    imvRightDelete!!.visibility = View.VISIBLE
                    val requestOptions = RequestOptions()
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(activity)
                        .asBitmap().load(tmpFileUri)
                        .apply(requestOptions)
                        .listener(object : RequestListener<Bitmap?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                model: Any,
                                target: Target<Bitmap?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(imvRight!!)
                }
            }
        } catch (e: Exception) {
            println("We have eoor $e")
            tmpFileUri = getImageUri(activity, bbmp)

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            val finalFile = File(getRealPathFromURI(tmpFileUri))
            if (tmpFileUri != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    //  tmpFileUri = getImageUri(getApplicationContext(), bbmp);
                    filePath = getRealPathFromURI(tmpFileUri)
                    try {
                        galleryUpdatePic(filePath)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                    try {
                        rotateCapturedImage(filePath, context)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                } catch (e3: Exception) {
                    CommonUtils.printStackTrace(e3)
                }

                /*     try {
                                execMultipartPost(doccode, tv_uploadtexting);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
*/filePath = filePath!!.replace(" ".toRegex(), "")
                Logger.e("filePath", filePath.toString())
                if (imvLeft!!.drawable == null) {
                    imvLeftPlus!!.visibility = View.GONE
                    imvLeftDelete!!.visibility = View.VISIBLE
                    val requestOptions = RequestOptions()
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(activity)
                        .asBitmap().load(bbmp)
                        .apply(requestOptions)
                        .listener(object : RequestListener<Bitmap?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                model: Any,
                                target: Target<Bitmap?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(imvLeft!!)
                } else {
                    imvRightPlus!!.visibility = View.GONE
                    imvRightDelete!!.visibility = View.VISIBLE
                    val requestOptions = RequestOptions()
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(activity)
                        .asBitmap().load(bbmp)
                        .apply(requestOptions)
                        .listener(object : RequestListener<Bitmap?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap,
                                model: Any,
                                target: Target<Bitmap?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(imvRight!!)
                }
            }
        }
    }

    /*calling on getgallery*/
    private fun getgallery(data: Intent) {
        Logger.i(TAG, "getgallery")
        val selectedImageUri = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.contentResolver.query(
            selectedImageUri!!,
            filePathColumn, null, null, null
        )
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        var bmp: Bitmap? = null
        try {
            bmp = getBitmapFromUri(selectedImageUri)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            CommonUtils.printStackTrace(e)
        }
        picUri = data.data
        filePath = getPath(picUri)
        Logger.e("picUri", picUri.toString())
        Logger.e("filePath", filePath.toString())
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(picturePath)
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
            Logger.e("ExifInterfaceException", "" + e.message)
        }
        val requestOptions = RequestOptions()
        //                        requestOptions.placeholder(R.drawable.event_place_holder);
//                        requestOptions.error(R.drawable.event_place_holder);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        //                requestOptions.skipMemoryCache(true);
//                        imageView.setTag(R.id.etTag);
        bbmp = getResizedBitmap(bmp, 500)
        if (imvLeft!!.drawable == null) {
            imvLeftPlus!!.visibility = View.GONE
            imvLeftDelete!!.visibility = View.VISIBLE
            Glide.with(activity)
                .load(bbmp)
                .apply(requestOptions)
                .dontAnimate()
                .thumbnail(0.1f)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        /*Logger.e("GLIDEIMAGE", "isFirstResource : "+isFirstResource);
                            Logger.e("GLIDEIMAGE", "Image downloaded Url : "+imageURL);*/
                        return false
                    }
                })
                .into(imvLeft!!)
        } else {
            imvRightPlus!!.visibility = View.GONE
            imvRightDelete!!.visibility = View.VISIBLE
            Glide.with(activity)
                .load(bbmp)
                .apply(requestOptions)
                .dontAnimate()
                .thumbnail(0.1f)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        /*Logger.e("GLIDEIMAGE", "isFirstResource : "+isFirstResource);
                            Logger.e("GLIDEIMAGE", "Image downloaded Url : "+imageURL);*/
                        return false
                    }
                })
                .into(imvRight!!)
            /* byteArrayOutputStream = new ByteArrayOutputStream();
                   rotatedBitmap = getResizedBitmap(bbmp, 500);
                   rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);*/
        }
    }

    private fun getPath(contentUri: Uri?): String {
        Logger.i(TAG, "getPath")
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(
            activity, contentUri!!, proj, null, null, null
        )
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    /*calling on getBitmapFromUri*/
    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        Logger.i(TAG, "getBitmapFromUri")
        val parcelFileDescriptor = activity.contentResolver.openFileDescriptor(
            uri!!, "r"
        )
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    /*calling on getResizedBitmap*/
    fun getResizedBitmap(image: Bitmap?, maxSize: Int): Bitmap {
        Logger.i(TAG, "getResizedBitmap")
        var width = image!!.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    /*calling on getRealPathFromURI*/
    fun getRealPathFromURI(uri: Uri?): String {
        Logger.i(TAG, "getRealPathFromURI")
        var path = ""
        if (activity.contentResolver != null) {
            val cursor = activity.contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    /*calling on getImageUri*/
    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        Logger.i(TAG, "getImageUri")
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    /*calling on rotateCapturedImage*/
    fun rotateCapturedImage(imagePath: String?, mContext: Context?) {
        Logger.i(TAG, "rotateCapturedImage")
        try {
            var sourceBitmap = BitmapFactory.decodeFile(imagePath)
            sourceBitmap = getScaledBitmap(sourceBitmap, activity)
            val ei = ExifInterface(imagePath!!)
            var bitmap: Bitmap? = null
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    bitmap = RotateBitmap(sourceBitmap, 90f)
                    if (bitmap != null) {
                        saveBitmap(bitmap, File(imagePath))
                    }
                }

                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    bitmap = RotateBitmap(sourceBitmap, 180f)
                    if (bitmap != null) {
                        saveBitmap(bitmap, File(imagePath))
                    }
                }

                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    bitmap = RotateBitmap(sourceBitmap, 270f)
                    if (bitmap != null) {
                        saveBitmap(bitmap, File(imagePath))
                    }
                }

                else -> saveBitmap(sourceBitmap, File(imagePath))
            }
            /*  if (bitmap != null) {
                new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), bitmap, JetEncryptor.getInstance(), iOnCallWSCallBack);
            } else {
                new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), sourceBitmap, JetEncryptor.getInstance(), iOnCallWSCallBack);
            }*/
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
        } catch (e: NullPointerException) {
            // null value
        } catch (e: OutOfMemoryError) {
            // null value
        }
    }

    /*calling on getScaledBitmap*/
    private fun getScaledBitmap(bm: Bitmap, mContext: Context): Bitmap {
        var bm = bm
        Logger.i(TAG, "getScaledBitmap")
        //int maxWidth=180;
        //int maxHeight=180;
        val maxWidth: Int
        val maxHeight: Int
        var width = bm.width
        var height = bm.height
        if (width > height) {
            maxWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                640f,
                mContext.resources.displayMetrics
            ).toInt()
            maxHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                320f,
                mContext.resources.displayMetrics
            ).toInt()
        } else if (width < height) {
            maxWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                320f,
                mContext.resources.displayMetrics
            ).toInt()
            maxHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                640f,
                mContext.resources.displayMetrics
            ).toInt()
        } else {
            maxWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                320f,
                mContext.resources.displayMetrics
            ).toInt()
            maxHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                320f,
                mContext.resources.displayMetrics
            ).toInt()
        }
        if (width > height) {
            // landscape
            val ratio = width.toFloat() / maxWidth
            width = maxWidth
            height = (height / ratio).toInt()
        } else if (height > width) {
            // portrait
            val ratio = height.toFloat() / maxHeight
            height = maxHeight
            width = (width / ratio).toInt()
        } else {
            // square
            height = maxHeight
            width = maxWidth
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true)
        return bm
    }

    /*calling on galleryUpdatePic*/
    private fun galleryUpdatePic(mediaUrl: String?) {
        Logger.i(TAG, "galleryUpdatePic")
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mediaUrl)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
        activity.sendBroadcast(mediaScanIntent)
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, "onActivityResult");
        switch (requestCode) {
            case REQUEST_CAMERA:
                main_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        main_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                });
                if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
//                    getcamera(data);
                    if (data != null) {
//                        try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                        if (imvLeft.getDrawable() == null) {
                            imvLeft.setImageBitmap(bitmap);
                            imvLeftPlus.setVisibility(View.GONE);
                            imvLeftDelete.setVisibility(View.VISIBLE);
                            imgLeftFile = new File(saveImage(bitmap));
                        }else {
                            imvRight.setImageBitmap(bitmap);
                            imvRightPlus.setVisibility(View.GONE);
                            imvRightDelete.setVisibility(View.VISIBLE);
                            imgRightFile = new File(saveImage(bitmap));
                        }
//                        } catch (Exception e) {
//                            CommonUtils.printStackTrace(e);
//                            Toast.makeText(getContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
                break;
            case REQUEST_GALLERY:
                main_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        main_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                });
                if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
//                    getgallery(data);
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            Bitmap bitmap =
                                    MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                            if (imvLeft.getDrawable() == null) {
                                imvLeft.setImageBitmap(bitmap);
                                imvLeftPlus.setVisibility(View.GONE);
                                imvLeftDelete.setVisibility(View.VISIBLE);
                                imgLeftFile = new File(saveImage(bitmap));
                            }else {
                                imvRight.setImageBitmap(bitmap);
                                imvRightPlus.setVisibility(View.GONE);
                                imvRightDelete.setVisibility(View.VISIBLE);
                                imgRightFile = new File(saveImage(bitmap));
                            }
                        } catch (IOException e) {
                            CommonUtils.printStackTrace(e);
                            Toast.makeText(getContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    etCity.setText(place.getName());
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                if(!changeTextFromMapFlag) {
                    changeMapPosition();
                    changeLocationTextFlag = true;
                }
                break;


            default: {
//                if (dialog.isShowing())
//                    dialog.dismiss();
            }

        }
    }*/
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.i(TAG, "onActivityResult")
        when (requestCode) {
            PICK_IMAGE_REQUEST -> if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
                selectedProfileImageUri = data.data
                Log.d("Aarati", selectedProfileImageUri.toString())
                // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(
                        activity.contentResolver,
                        selectedProfileImageUri
                    )
                    if (imvLeft!!.drawable == null) {
                        Glide.with(activity)
                            .load(selectedProfileImageUri)
                            .into(imvLeft!!)
                        imvLeftPlus!!.visibility = View.GONE
                        imvLeftDelete!!.visibility = View.VISIBLE
                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                        // String imageFileName = "JPEG_" + timeStamp + "_";
                        val imageFileName = "IMG$timeStamp.jpg"
                        val savedFile = saveBitmapToFile(
                            activity.applicationContext, bitmap, imageFileName
                        )
                        if (savedFile != null) {
                            // File saved successfully
                            imgLeftFile = savedFile
                        } else {
                            // Handle the error
                        }
                    } else {
                        Glide.with(activity)
                            .load(selectedProfileImageUri)
                            .into(imvRight!!)
                        imvRightPlus!!.visibility = View.GONE
                        imvRightDelete!!.visibility = View.VISIBLE
                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                        // String imageFileName = "JPEG_" + timeStamp + "_";
                        val imageFileName = "IMG$timeStamp.jpg"
                        val savedFile = saveBitmapToFile(
                            activity.applicationContext, bitmap, imageFileName
                        )
                        if (savedFile != null) {
                            // File saved successfully
                            imgRightFile = savedFile
                        } else {
                            // Handle the error
                        }
                    }
                    Log.d("Aarati", "loaded by glide")
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            } else {
                Log.d("Aarati", "not loaded")
            }

            REQUEST_IMAGE_CAPTURE -> if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                // selectedProfileImageUri = data.getData();
                Log.d("Aarati", selectedProfileImageUri.toString())
                // if (data != null && data.getExtras() != null) {
                //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (selectedProfileImageUri != null) {
                    if (imvLeft!!.drawable == null) {
                        Glide.with(activity)
                            .load(selectedProfileImageUri)
                            .into(imvLeft!!)
                        imvLeftPlus!!.visibility = View.GONE
                        imvLeftDelete!!.visibility = View.VISIBLE
                    } else {
                        Glide.with(activity)
                            .load(selectedProfileImageUri)
                            .into(imvRight!!)
                        imvRightPlus!!.visibility = View.GONE
                        imvRightDelete!!.visibility = View.VISIBLE
                    }
                    Log.d("Aarati", "loaded by glide")
                }
                /* } else {
                        Log.d("Aarati", "not loaded");
                    }*/Log.d("Aarati", "Done loaded")
            }

            AUTOCOMPLETE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(
                        data!!
                    )
                    etCity!!.setText(place.name)
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    val status = Autocomplete.getStatusFromIntent(
                        data!!
                    )
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                if (!changeTextFromMapFlag) {
                    changeMapPosition()
                    changeLocationTextFlag = true
                }
            }

            else -> {}
        }
        Log.d("Aarati", "On Activity result completed")
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val directory = File(activity.filesDir.toString())

        // have the object build the directory structure, if needed.
        if (!directory.exists()) {
            directory.mkdirs()
        }
        try {
            val name = SimpleDateFormat("'img'yyyyMMddhhmmss'.jpg'").format(Date())
            Logger.d("heel", directory.toString())
            val f = File(directory, name)
            if (f.exists()) f.delete()
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                context, arrayOf(f.absolutePath), arrayOf("image/jpeg"), null
            )
            fo.close()
            Logger.d("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            CommonUtils.printStackTrace(e1)
        }
        return ""
    }

    private fun changeMapPosition() {
        var strAddress = ""
        if (!TextUtils.isEmpty(etAddress!!.text.toString())) strAddress =
            etAddress!!.text.toString().trim { it <= ' ' }
        if (!TextUtils.isEmpty(etCity!!.text.toString())) strAddress += " " + etCity!!.text.toString()
            .trim { it <= ' ' }
        if (!TextUtils.isEmpty(etZipCode!!.text.toString())) strAddress += " " + etZipCode!!.text.toString()
            .trim { it <= ' ' }
        strAddress += " India"
        val coder = Geocoder(activity)
        val address: List<Address>?
        try {
            address = coder.getFromLocationName(strAddress, 1)
            if (address != null) {
                val location = address[0]
                latitude = location.latitude
                longitude = location.longitude
                map!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(latitude, longitude),
                        zoom
                    )
                )
            }
        } catch (e: Exception) {
            if (e.toString().contains("Service not Available")) {
                isGeoCodeFroLatLong = true
                //                referWolooHostPresenter.getLocation(String.valueOf(latitude), String.valueOf(longitude));
                wolooViewModel!!.reverseGeocoding(latitude, longitude)
                //CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
            CommonUtils.printStackTrace(e)
        }
    }

    private val isValid: Boolean
        /*calling on isValid*/private get() {
            Logger.i(TAG, "isValid")
            if (TextUtils.isEmpty(etName!!.text.toString())) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.name_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (TextUtils.isEmpty(etAddress!!.text.toString())) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.address_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (TextUtils.isEmpty(etCity!!.text.toString())) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.city_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (TextUtils.isEmpty(etPhone!!.text.toString())) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.phone_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (lastKnownLocation == null) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.location_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (imvLeftDelete!!.visibility == View.GONE || imvLeftDelete!!.visibility == View.INVISIBLE) {
                Toast.makeText(
                    activity.applicationContext,
                    getString(R.string.woloo_picture_validation),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            return true
        }

    override fun referredWolooListResponse(referredWolooListResponse: ReferredWolooListResponse?) {}

    /*calling on setProfileResponse*/
    override fun setProfileResponse(viewProfileResponse: ViewProfileResponse?) {
        Logger.i(TAG, "setProfileResponse")
        if (viewProfileResponse != null) {
            /*  etName.setText(viewProfileResponse.getUserData().getName());
            etAddress.setText(viewProfileResponse.getUserData().getAddress());
            etCity.setText(viewProfileResponse.getUserData().getCity());*/
        }
    }

    override fun referWolooHostSuccess(message: String?) {
        showdialog(message)
    }

    override fun geoCodeResponseSuccess(geoCodeResponse: GeoCodeResponse?) {
        if (isGeoCodeFroLatLong) {
            isGeoCodeFroLatLong = false
            val dataItems = geoCodeResponse!!.data
            for (i in dataItems!!.indices) {
                if (dataItems[i].types!![0] == "postal_code") {
                    latitude = dataItems[i].geometry!!.location!!.lat
                    longitude = dataItems[i].geometry!!.location!!.lat
                    map!!.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(latitude, longitude),
                            zoom
                        )
                    )
                    break
                }
            }
        } else if (isGeoCodeFroAddress) {
            isGeoCodeFroAddress = false
            val dataItems = geoCodeResponse!!.data
            for (i in dataItems!!.indices) {
                if (dataItems[i].types!![0] == "postal_code") {
                    etAddress!!.setText(dataItems[i].formattedAddress)
                    val addressComponentsItems = dataItems[i].addressComponents
                    for (j in addressComponentsItems!!.indices) {
                        if (addressComponentsItems[i].types!!.contains("locality")) etCity!!.setText(
                            addressComponentsItems[i].longName
                        )
                        break
                    }
                    for (j in addressComponentsItems.indices) {
                        if (addressComponentsItems[i].types!!.contains("postal_code")) etZipCode!!.setText(
                            addressComponentsItems[i].longName
                        )
                        break
                    }
                    break
                }
            }
        }
    }

    private val deviceLocation: Unit
        /*calling on getDeviceLocation*/private get() {
            Logger.i(TAG, "setProfileResponse")
            /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */try {
                locationPermissionGranted = isLocationPermissionGranted()
                if (locationPermissionGranted) {
                    if (!Places.isInitialized()) {
                        val key = CommonUtils.googlemapapikey(activity)
                        Places.initialize(activity, key)
                    }
                    val mapFragment =
                        supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment?.getMapAsync(this)
                    currentLocation
                }
            } catch (e: SecurityException) {
                if (e.toString().contains("Service not Available")) {
                    CommonUtils.showCustomDialogBackClick(
                        activity,
                        "Unable to find Location Service. Please start your location Service Or Reboot your device."
                    )
                }
                Logger.e("Exception: %s", e.message, e)
            }
        }
    private val currentLocation: Unit
        private get() {
            try {
                locationPermissionGranted = isLocationPermissionGranted()
                if (locationPermissionGranted) {
                    @SuppressLint("MissingPermission") val locationResult =
                        fusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.result
                            if (lastKnownLocation != null) {
                                latitude = lastKnownLocation!!.latitude
                                longitude = lastKnownLocation!!.longitude
                                if (map != null) map!!.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(latitude, longitude), zoom
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
            }
        }

    /*calling on isLocationPermissionGranted*/
    fun isLocationPermissionGranted(): Boolean {
        Logger.i(TAG, "isLocationPermissionGranted")
        return if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom))
        map!!.setOnCameraMoveStartedListener { /*if (markerImage.getTranslationY() == 0f) {
                    markerImage.animate()
                            .translationY(-75f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(250)
                            .start();
                }*/
        }
        map!!.setOnCameraIdleListener(object : OnCameraIdleListener {
            override fun onCameraIdle() {
                if (changeLocationTextFlag) changeLocationTextFlag = false else {
                    changeTextFromMapFlag = true
                    /* markerImage.animate()
                            .translationY(0f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(250)
                            .start();*/
                    val latLng = map!!.cameraPosition.target
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    addressForLocation
                }
            }
        })
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom))
    }

    private val addressForLocation: Unit
        private get() {
            setAddress(latitude, longitude)
        }

    private fun setAddress(latitude: Double, longitude: Double) {
        val geoCoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses = geoCoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.size != 0) {
                etAddress!!.setText(addresses[0].getAddressLine(0))
                //shortAddress = generateFinalAddress(fullAddress).trim();
                etCity!!.setText(addresses[0].locality)
                etZipCode!!.setText(addresses[0].postalCode)
                //mCountryCode = addresses.get(0).getCountryCode();
            } /*else {
                shortAddress = "";
                fullAddress = "";
            }*/
        } catch (e: Exception) {
            /*shortAddress = "";
            fullAddress = "";
            addresses = null;*/
            if (e.toString().contains("Service not Available")) {
                isGeoCodeFroAddress = true
                //                referWolooHostPresenter.getLocation(String.valueOf(latitude),String.valueOf(longitude));
                wolooViewModel!!.reverseGeocoding(latitude, longitude)
                //CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
            CommonUtils.printStackTrace(e)
        }
    }

    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {}
    override fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    override fun onTimeOutConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    private fun checkStoragePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermission() {
        Log.d("Aarati", "in request PERMISSION")
        /* ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);*/if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun openGallery() {
        Log.d("Aarati", "in open gallery")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun checkCameraPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.CAMERA
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestCameraPermission() {
        Log.d("Aarati", "in request PERMISSION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE_PERMISSION
            )
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE_PERMISSION
            )
        }
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                if (photoFile != null) {
                    if (imgLeftFile == null) imgLeftFile = photoFile else imgRightFile = photoFile
                    selectedProfileImageUri = FileProvider.getUriForFile(
                        activity.applicationContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedProfileImageUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } catch (ex: IOException) {
                // Handle the error
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // String imageFileName = "JPEG_" + timeStamp + "_";
        val imageFileName = "IMG$timeStamp"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }

    private fun showImageUploadDialog() {
        Logger.i(TAG, "showImageUploadDialog")
        try {
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(
                activity
            )
            val child: View = layoutInflater.inflate(R.layout.dialog_profile_image, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(true)
            val alertDialog = alertDialogBuilder.create()
            val tvSelectGallery = child.findViewById<TextView>(R.id.tvSelectGallery)
            val tvImageCapture = child.findViewById<TextView>(R.id.tvImageCapture)
            val tv_image = child.findViewById<TextView>(R.id.tv_image)
            /* if(profileImage == null)
            {
                tv_image.setText(getString(R.string.upload_image));
            }else {
                tv_image.setText(getString(R.string.change_image));
            }*/tv_image.visibility = View.GONE
            tvSelectGallery.setOnClickListener {
                if (checkStoragePermission()) {
                    Log.d("Aarati", "PERMISSION GRANTED")
                    openGallery()
                    alertDialog.dismiss()
                } else {
                    Log.d("Aarati", "PERMISSION Already not GRANTED")
                    requestStoragePermission()
                    alertDialog.dismiss()
                }
            }
            tvImageCapture.setOnClickListener {
                if (checkCameraPermission()) {
                    Log.d("Aarati", "PERMISSION GRANTED")
                    takePictureIntent()
                    alertDialog.dismiss()
                } else {
                    Log.d("Aarati", "PERMISSION Already not GRANTED")
                    requestCameraPermission()
                    alertDialog.dismiss()
                }
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 80
        private const val AUTOCOMPLETE_REQUEST_CODE = 1999
        private var tmpFileUri: Uri? = null
        var bbmp: Bitmap? = null
        var TAG = referWolooFormActivity::class.java.simpleName
        private const val REQUEST_READ_EXTERNAL_STORAGE = 111
        private const val PICK_IMAGE_REQUEST = 112
        private const val REQUEST_CAMERA_PERMISSION = 113
        private const val REQUEST_IMAGE_CAPTURE = 114
        private const val REQUEST_WRITE_STORAGE_PERMISSION = 115
        fun createDirectory(filePath: String?) {
            Logger.i(TAG, "createDirectory")
            if (!File(filePath).exists()) {
                File(filePath).mkdirs()
            }
        }

        /*calling on saveBitmap*/
        fun saveBitmap(mBitmap: Bitmap, destinationPath: File?) {
            Logger.i(TAG, "saveBitmap")
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(destinationPath)
                if (mBitmap.hasAlpha()) mBitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                ) else mBitmap.compress(
                    Bitmap.CompressFormat.JPEG, 100, out
                )
            } catch (e: NullPointerException) {
                //  CommonUtils.printStackTrace(e);
            } catch (e: Exception) {
                //  CommonUtils.printStackTrace(e);
            } finally {
                try {
                    out!!.close()
                } catch (ignore: Throwable) {
                }
            }
        }

        /*calling on RotateBitmap*/
        fun RotateBitmap(source: Bitmap, angle: Float): Bitmap {
            Logger.i(TAG, "RotateBitmap")
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.width,
                source.height, matrix, true
            )
        }

        fun saveBitmapToFile(context: Context, bitmap: Bitmap?, fileName: String?): File? {
            // Get the directory where you want to save the file
            val directory = context.getExternalFilesDir(null)
                ?: // If the directory is null, return null or handle it as needed
                return null // or getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            // Create a file object with the specified file name
            val file = File(directory, fileName)
            var fos: FileOutputStream? = null
            return try {
                // Create a FileOutputStream to write the bitmap to the file
                fos = FileOutputStream(file)

                // Compress the bitmap and write it to the file
                bitmap!!.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    fos
                ) // Use CompressFormat.JPEG for JPEG images

                // Flush and close the output stream
                fos.flush()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                // Close the FileOutputStream if it's not null
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}