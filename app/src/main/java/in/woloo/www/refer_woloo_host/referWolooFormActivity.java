package in.woloo.www.refer_woloo_host;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import in.woloo.www.app.WolooApplication;
import in.woloo.www.utils.Logger;

import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.FileUploadResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;
import in.woloo.www.refer_woloo_host.mvp.ReferredWolooPresenter;
import in.woloo.www.refer_woloo_host.mvp.ReferredWolooView;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.IOnCallWSCallBack;
import in.woloo.www.utils.ProfileAPIUtil;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.data.remote.MessageResponse;
import in.woloo.www.v2.geocode.ReverseGeocodeItem;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;
import in.woloo.www.woloo_host.model.GeoCodeResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class referWolooFormActivity extends AppCompatActivity implements ReferredWolooView, OnMapReadyCallback, NetworkAPIResponseCallback {
    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etOwnerName)
    EditText etOwnerName;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.etCity)
    EditText etCity;

    @BindView(R.id.etZipCode)
    EditText etZipCode;

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.marker_image_view)
    ImageView markerImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.image_left_Imv)
    ImageView imvLeft;

    @BindView(R.id.delete_left_Imv)
    ImageView imvLeftDelete;

    @BindView(R.id.plus_left_imv)
    ImageView imvLeftPlus;

    @BindView(R.id.image_right_Imv)
    ImageView imvRight;

    @BindView(R.id.delete_right_Imv)
    ImageView imvRightDelete;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.plus_right_imv)
    ImageView imvRightPlus;

    @BindView(R.id.transparent_image)
    ImageView transparent_image;

    @BindView(R.id.main_scrollview)
    NestedScrollView main_scrollview;

    @BindView(R.id.ivCurrentLocation)
    ImageView ivCurrentLocation;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final int REQUEST_CAMERA = 0, REQUEST_GALLERY = 1;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 80;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1999;
    private static Uri tmpFileUri;
    public static Bitmap bbmp;
    String filePath;
    Uri picUri;
    private FusedLocationProviderClient fusedLocationProviderClient;
//    private ReferredWolooPresenter referWolooHostPresenter;
    private WolooViewModel wolooViewModel;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private GoogleMap map;
    private double latitude;
    private double longitude;
    private boolean showLatLong = true;
    private float zoom = 20.0F;
    private boolean changeLocationTextFlag = false;
    private boolean changeTextFromMapFlag = false;
    JSONArray uploadedImages = new JSONArray();
    public static String TAG = referWolooFormActivity.class.getSimpleName();
    private boolean isGeoCodeFroLatLong=false;
    private boolean isGeoCodeFroAddress=false;

    File imgLeftFile;
    File imgRightFile;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 111;
    private static final int PICK_IMAGE_REQUEST = 112;

    private static final int REQUEST_CAMERA_PERMISSION = 113;
    private static final int REQUEST_IMAGE_CAPTURE = 114;

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 115;

    Uri selectedProfileImageUri = Uri.parse("");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_woloo_form);
        ButterKnife.bind(this);
        wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
        initViews();
        setLiveData();
        Logger.i(TAG, "onCreateView");
    }

    /*calling on initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        try {
            Logger.i(TAG, "initViews");
            tvTitle.setText("Refer a Woloo Host");
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            getDeviceLocation();
            tvSubmit.setOnClickListener(v -> {
                if (isValid()) {

//                    if(imvLeftDelete.getVisibility() == View.VISIBLE)
//                        uploadImages(((BitmapDrawable)imvLeft.getDrawable()).getBitmap(),iOnLeftCallWSCallBack);
                    new CommonUtils().showProgress(getContext());
                    if (imvLeftDelete.getVisibility() == View.VISIBLE){
                        MultipartBody.Builder requestBody = new MultipartBody.Builder();
                        requestBody.setType(MultipartBody.FORM);
                        requestBody.addFormDataPart("name", etName.getText().toString());
                        requestBody.addFormDataPart("city", etCity.getText().toString());
                        requestBody.addFormDataPart("address",etAddress.getText().toString()+" "
                                +  etCity.getText().toString() + " " + etZipCode.getText().toString() + " India");
                        requestBody.addFormDataPart("lat", String.valueOf(latitude));
                        requestBody.addFormDataPart("lng", String.valueOf(longitude));
                        requestBody.addFormDataPart("pincode", etZipCode.getText().toString() );
                        requestBody.addFormDataPart("recommended_mobile", etPhone.getText().toString() );
                        requestBody.addFormDataPart("image", imgLeftFile.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), imgLeftFile));

//                        requestBody.addFormDataPart("[]",imgLeftFile.getName(), RequestBody.create(imgLeftFile,
//                                        MediaType.parse("image/png")));
                        if(imgRightFile!=null){
                            requestBody.addFormDataPart("image", imgRightFile.getName(),
                                    RequestBody.create(MediaType.parse("application/octet-stream"), imgRightFile));

//                            requestBody.addFormDataPart("image[]",imgRightFile.getName(), RequestBody.create(imgRightFile,
//                                    MediaType.parse("image/png")));
                        }
                        wolooViewModel.recommendWoloo(requestBody.build());
//                        uploadImages(((BitmapDrawable) imvLeft.getDrawable()).getBitmap(), iOnLeftCallWSCallBack);
                    }
                }
            });
            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });

            imvLeft.setOnClickListener(view -> {
               /* if (checkAndRequestPermissions()) {
                    selectImage();
                }*/
                showImageUploadDialog();
            });

            imvRight.setOnClickListener(view -> {
               /* if (checkAndRequestPermissions()) {
                    selectImage();
                }*/
                showImageUploadDialog();
            });

            imvRightDelete.setOnClickListener(view -> {
                deleteImage(false);
            });

            imvLeftDelete.setOnClickListener(view -> {
                deleteImage(true);
            });


            etName.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    int len = s.length();
                    if (len >= 1) {
                        tvSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        tvSubmit.setBackgroundResource(R.drawable.yellow_rectangle_shape);
                    } else {
                        tvSubmit.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_five));
                        tvSubmit.setBackgroundResource(R.drawable.black_rectangle_shape);
                    }
                }
            });

            transparent_image.setOnTouchListener(new View.OnTouchListener() {
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
            });

            ivCurrentLocation.setOnClickListener(v -> {
                getCurrentLocation();
            });

            etCity.setOnClickListener(v -> {
                try {
                    if (!Places.isInitialized()) {
                        String key = CommonUtils.googlemapapikey(getActivity());
                        Places.initialize(getActivity(), key);
                    }
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .setTypeFilter(TypeFilter.CITIES)
                            .build(getActivity());
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }
            });

            etZipCode.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus){
                    if(!changeTextFromMapFlag) {
                        changeMapPosition();
                        changeLocationTextFlag = true;
                    }
                    else
                        changeTextFromMapFlag = false;
                }
            });

            etAddress.setOnFocusChangeListener((v, hasFocus) -> {
                if(!changeTextFromMapFlag) {
                    changeMapPosition();
                    changeLocationTextFlag = true;
                }
            });

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        wolooViewModel.observeReverseGeocoding().observe(this, new Observer<BaseResponse<ArrayList<ReverseGeocodeItem>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<ReverseGeocodeItem>> response) {
                if (response != null && response.getData() != null) {
                    if (isGeoCodeFroLatLong) {
                        isGeoCodeFroLatLong = false;
                        List<ReverseGeocodeItem> dataItems = response.getData();
                        for (int i = 0; i < dataItems.size(); i++) {
                            if (dataItems.get(i).getTypes().get(0).equals("postal_code")) {
                                latitude = dataItems.get(i).getGeometry().getLocation().getLat();
                                longitude = dataItems.get(i).getGeometry().getLocation().getLat();
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
                                break;
                            }
                        }
                    } else if (isGeoCodeFroAddress) {
                        isGeoCodeFroAddress = false;
                        List<ReverseGeocodeItem> dataItems = response.getData();
                        for (int i = 0; i < dataItems.size(); i++) {
                            if (dataItems.get(i).getTypes().get(0).equals("postal_code")) {
                                etAddress.setText(dataItems.get(i).getFormattedAddress());
                                List<ReverseGeocodeItem.AddressComponentsItem> addressComponentsItems = dataItems.get(i).getAddressComponents();
                                for (int j = 0; j < addressComponentsItems.size(); j++) {
                                    if (addressComponentsItems.get(i).getTypes().contains("locality"))
                                        etCity.setText(addressComponentsItems.get(i).getLongName());
                                    break;
                                }
                                for (int j = 0; j < addressComponentsItems.size(); j++) {
                                    if (addressComponentsItems.get(i).getTypes().contains("postal_code"))
                                        etZipCode.setText(addressComponentsItems.get(i).getLongName());
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }else{
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        wolooViewModel.observeRecommendWoloo().observe(this, new Observer<BaseResponse<MessageResponse>>() {
            @Override
            public void onChanged(BaseResponse<MessageResponse> response) {
                if(response!=null && response.getData()!=null){
                    showdialog(response.getData().getMessage());
                }
                new CommonUtils().hideProgress();
            }
        });
    }

    private Context getContext() {
        return referWolooFormActivity.this;
    }

    void uploadImages(Bitmap bitmap, IOnCallWSCallBack iOnCallWSCallBack){
        new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), bitmap , JetEncryptor.getInstance(), iOnCallWSCallBack, AppConstants.WOLOOS);
    }

    private Activity getActivity() {
        return referWolooFormActivity.this;
    }

    IOnCallWSCallBack iOnLeftCallWSCallBack = new IOnCallWSCallBack() {
        @Override
        public void onSuccessResponse(FileUploadResponse fileUploadResponse) {
            if (fileUploadResponse != null && fileUploadResponse.getStatus().equalsIgnoreCase(AppConstants.API_SUCCESS)) {
                /*JSONObject mJsObjParam = new JSONObject();
                try {
                    mJsObjParam.put(JSONTagConstant.FILE_NAMES, fileUploadResponse.getConvertedName());
                    mJsObjParam.put(JSONTagConstant.PATH, fileUploadResponse.getPath());
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }*/
                    uploadedImages.put("woloos/"+fileUploadResponse.getConvertedName());
//                    if(imvRightDelete.getVisibility() == View.VISIBLE && uploadedImages.length()<2)
//                        uploadImages(((BitmapDrawable)imvRight.getDrawable()).getBitmap(),iOnRightCallWSCallBack);
//                    else{
////                        referWolooHostPresenter.referredWoloo(etName.getText().toString(),
////                                etCity.getText().toString(), etAddress.getText().toString(), etPhone.getText().toString() ,etZipCode.getText().toString() , uploadedImages, latitude, longitude);
//                    }

                //morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        @Override
        public void onFailure(VolleyError volleyError) {

        }
    };

    IOnCallWSCallBack iOnRightCallWSCallBack = new IOnCallWSCallBack() {
        @Override
        public void onSuccessResponse(FileUploadResponse fileUploadResponse) {
            if (fileUploadResponse != null && fileUploadResponse.getStatus().equalsIgnoreCase(AppConstants.API_SUCCESS)) {
                    uploadedImages.put("woloos/"+fileUploadResponse.getConvertedName());
//                referWolooHostPresenter.referredWoloo(etName.getText().toString(),
//                            etCity.getText().toString(), etAddress.getText().toString(), etPhone.getText().toString() , etZipCode.getText().toString() , uploadedImages, latitude, longitude);

                //morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        @Override
        public void onFailure(VolleyError volleyError) {

        }
    };


    private void deleteImage(boolean isFromLeft) {
        if (isFromLeft) {
            if (imvRight.getDrawable() == null) {
                imvLeft.setImageResource(0);
                imvLeftPlus.setVisibility(View.VISIBLE);
                imvLeftDelete.setVisibility(View.GONE);
                imgLeftFile = null;
            } else {
                imvLeft.setImageResource(0);
                imvLeftPlus.setVisibility(View.GONE);
                imvLeftDelete.setVisibility(View.VISIBLE);
                imvLeft.setImageDrawable(imvRight.getDrawable());
                imvRight.setImageResource(0);
                imvRightPlus.setVisibility(View.VISIBLE);
                imvRightDelete.setVisibility(View.GONE);
                imgLeftFile = imgRightFile;
                imgRightFile = null;
            }
        } else {
            imvRight.setImageResource(0);
            imvRightPlus.setVisibility(View.VISIBLE);
            imvRightDelete.setVisibility(View.GONE);
            imgRightFile = null;
        }
    }

    /*calling on showdialog*/
    public void showdialog(String msg) {
        try {
            Logger.i(TAG, "showdialog");
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
                        getActivity().onBackPressed();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    /*calling on selectImage*/
    private void selectImage() {
        Logger.i(TAG, "selectImage");
        final CharSequence[] items = {"Gallery", "Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Attachment!");
        builder.setIcon(R.drawable.attachment_grey_ic);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Gallery")) {
                    try {
                        galleryIntent();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                } else if (items[item].equals("Take Photo")) {
                    try {
                        cameraIntent();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }/* else if (items[item].equals("File")) {
                    pickfiles();
                }*/ else/* if (items[item].equals("Cancel"))*/ {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                }
            }
        });
        builder.show();
    }

    private boolean checkAndRequestPermissions() {
        Logger.d(TAG, "checkAndRequestPermissions");
        int permissionReadStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Logger.d(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    checkAndRequestPermissions();
                }
            }
                break;
                case REQUEST_READ_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission granted, proceed with selecting an image
                        openGallery();
                    } else {
                        // Permission denied, show a message to the user
                        Toast.makeText(getActivity().getApplicationContext(), "Permission required to access gallery", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

                case REQUEST_CAMERA_PERMISSION: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission granted, proceed with selecting an image
                        takePictureIntent();
                        ;
                    } else {
                        // Permission denied, show a message to the user
                        Toast.makeText(getActivity().getApplicationContext(), "Permission required to access camera", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

    }

    /*calling on cameraIntent*/
    private void cameraIntent() {
        Logger.i(TAG, "cameraIntent");
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        tmpFileUri = getOutputMediaFile(1);
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri);
        pictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(pictureIntent,
                REQUEST_CAMERA);
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /*calling on getOutputMediaFile*/
    public Uri getOutputMediaFile(int type) {
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
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "IMG" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
              CommonUtils.printStackTrace(e);
        }

        // Save a file: path for use with ACTION_VIEW intents
        if (image != null) {
            return FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", image);
        }
        return null;
    }

    public static void createDirectory(String filePath) {
        Logger.i(TAG, "createDirectory");
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
    }

    /*calling on galleryIntent*/
    private void galleryIntent() {
        Logger.i(TAG, "galleryIntent");
        Intent i = new Intent(
                Intent.ACTION_PICK/*, MediaStore.Images.Media.EXTERNAL_CONTENT_UR*/);
        i.setType("image/*");
//        startActivityForResult(intent, SELECT_PICTURE);
//        Intent intent = new Intent();
//        i.setAction(Intent.ACTION_GET_CONTENT);//

//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

        startActivityForResult(i, REQUEST_GALLERY);
    }

    /*calling on getcamera*/
    private void getcamera(Intent data) {
        Logger.e("Inside camera", " intent");
        Logger.i(TAG, "getcamera");

        try {
            bbmp = (Bitmap) data.getExtras().get("data");
        } catch (Exception e) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 8;

                bbmp = BitmapFactory.decodeFile(tmpFileUri.getPath(),
                        options);

            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }

        try {
            if (tmpFileUri.getPath() != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    //  tmpFileUri = getImageUri(getApplicationContext(), bbmp);
                    filePath = tmpFileUri.getPath();
                    try {
                        galleryUpdatePic(tmpFileUri.getPath());
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                    try {
                        rotateCapturedImage(tmpFileUri.getPath(), getContext());
                    } catch (Exception e) {
                          CommonUtils.printStackTrace(e);
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }

//                            try {
//                                execMultipartPost(doccode, tv_uploadtexting);
//                            } catch (Exception e) {
//                                  CommonUtils.printStackTrace(e);
//                            }

                Logger.e("filePath", filePath);

                if(imvLeft.getDrawable() == null){
                    imvLeftPlus.setVisibility(View.GONE);
                    imvLeftDelete.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(getActivity())
                            .asBitmap().load(tmpFileUri)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imvLeft);
                }else {
                    imvRightPlus.setVisibility(View.GONE);
                    imvRightDelete.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(getActivity())
                            .asBitmap().load(tmpFileUri)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imvRight);
                }
            }
        } catch (Exception e) {
            System.out.println("We have eoor "  + e.toString());
             tmpFileUri = getImageUri(getActivity(), bbmp);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tmpFileUri));

            if (tmpFileUri != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    //  tmpFileUri = getImageUri(getApplicationContext(), bbmp);
                    filePath = getRealPathFromURI(tmpFileUri);
                    try {
                        galleryUpdatePic(filePath);
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                    try {
                        rotateCapturedImage(filePath, getContext());
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                } catch (Exception e3) {
                    CommonUtils.printStackTrace(e3);
                }

                       /*     try {
                                execMultipartPost(doccode, tv_uploadtexting);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
*/
                filePath = filePath.replaceAll(" ", "");
                Logger.e("filePath", filePath);

                if(imvLeft.getDrawable() == null){
                    imvLeftPlus.setVisibility(View.GONE);
                    imvLeftDelete.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(getActivity())
                            .asBitmap().load(bbmp)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imvLeft);
                }else {
                    imvRightPlus.setVisibility(View.GONE);
                    imvRightDelete.setVisibility(View.VISIBLE);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(getActivity())
                            .asBitmap().load(bbmp)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imvRight);
                }
            }
        }
    }
    /*calling on getgallery*/
    private void getgallery(Intent data) {
        Logger.i(TAG, "getgallery");
        Uri selectedImageUri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bmp = null;
        try {
            bmp = getBitmapFromUri(selectedImageUri);

        } catch (IOException e) {
            // TODO Auto-generated catch block
              CommonUtils.printStackTrace(e);
        }

        picUri = data.getData();
        filePath = getPath(picUri);
        Logger.e("picUri", picUri.toString());
        Logger.e("filePath", filePath);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(picturePath);
        } catch (IOException e) {
              CommonUtils.printStackTrace(e);
            Logger.e("ExifInterfaceException", "" + e.getMessage());
        }

        RequestOptions requestOptions = new RequestOptions();
//                        requestOptions.placeholder(R.drawable.event_place_holder);
//                        requestOptions.error(R.drawable.event_place_holder);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//                requestOptions.skipMemoryCache(true);
//                        imageView.setTag(R.id.etTag);

        bbmp = getResizedBitmap(bmp, 500);

        if(imvLeft.getDrawable() == null){
            imvLeftPlus.setVisibility(View.GONE);
            imvLeftDelete.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(bbmp)
                    .apply(requestOptions)
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            /*Logger.e("GLIDEIMAGE", "isFirstResource : "+isFirstResource);
                            Logger.e("GLIDEIMAGE", "Image downloaded Url : "+imageURL);*/
                            return false;
                        }
                    })
                    .into(imvLeft);
        }else {
            imvRightPlus.setVisibility(View.GONE);
            imvRightDelete.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(bbmp)
                    .apply(requestOptions)
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            /*Logger.e("GLIDEIMAGE", "isFirstResource : "+isFirstResource);
                            Logger.e("GLIDEIMAGE", "Image downloaded Url : "+imageURL);*/
                            return false;
                        }
                    })
                    .into(imvRight);
                   /* byteArrayOutputStream = new ByteArrayOutputStream();
                   rotatedBitmap = getResizedBitmap(bbmp, 500);
                   rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);*/
        }
    }


    private String getPath(Uri contentUri) {
        Logger.i(TAG, "getPath");
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    /*calling on getBitmapFromUri*/
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        Logger.i(TAG, "getBitmapFromUri");
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    /*calling on getResizedBitmap*/
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        Logger.i(TAG, "getResizedBitmap");
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /*calling on getRealPathFromURI*/
    public String getRealPathFromURI(Uri uri) {
        Logger.i(TAG, "getRealPathFromURI");
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    /*calling on getImageUri*/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Logger.i(TAG, "getImageUri");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*calling on rotateCapturedImage*/
    public void rotateCapturedImage(String imagePath, Context mContext) {
        Logger.i(TAG, "rotateCapturedImage");
        try {
            Bitmap sourceBitmap = BitmapFactory.decodeFile(imagePath);
            sourceBitmap = getScaledBitmap(sourceBitmap, getActivity());
            ExifInterface ei = new ExifInterface(imagePath);
            Bitmap bitmap = null;
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//			Toast.makeText(mContext,"orientation "+orientation,Toast.LENGTH_SHORT).show();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = RotateBitmap(sourceBitmap, 90);
                    if (bitmap != null) {
                        saveBitmap(bitmap, new File(imagePath));
                    }
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = RotateBitmap(sourceBitmap, 180);
                    if (bitmap != null) {
                        saveBitmap(bitmap, new File(imagePath));
                    }
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = RotateBitmap(sourceBitmap, 270);
                    if (bitmap != null) {
                        saveBitmap(bitmap, new File(imagePath));
                    }
                    break;
                default:
                    saveBitmap(sourceBitmap, new File(imagePath));
                    break;

            }
          /*  if (bitmap != null) {
                new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), bitmap, JetEncryptor.getInstance(), iOnCallWSCallBack);
            } else {
                new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), sourceBitmap, JetEncryptor.getInstance(), iOnCallWSCallBack);
            }*/


        } catch (IOException e) {
              CommonUtils.printStackTrace(e);
        } catch (NullPointerException e) {
            // null value
        } catch (OutOfMemoryError e) {
            // null value
        }
    }

    /*calling on saveBitmap*/
    public static void saveBitmap(Bitmap mBitmap, File destinationPath) {
        Logger.i(TAG, "saveBitmap");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(destinationPath);
            if (mBitmap.hasAlpha())
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            else
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (NullPointerException e) {
            //  CommonUtils.printStackTrace(e);
        } catch (Exception e) {
            //  CommonUtils.printStackTrace(e);
        } finally {
            try {
                out.close();
            } catch (Throwable ignore) {
            }
        }
    }

    /*calling on RotateBitmap*/
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Logger.i(TAG, "RotateBitmap");
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);

    }

    /*calling on getScaledBitmap*/
    private Bitmap getScaledBitmap(Bitmap bm, Context mContext) {
        Logger.i(TAG, "getScaledBitmap");
        //int maxWidth=180;
        //int maxHeight=180;
        int maxWidth;
        int maxHeight;
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (width > height) {
            maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, mContext.getResources().getDisplayMetrics());
            maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, mContext.getResources().getDisplayMetrics());
        } else if (width < height) {
            maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, mContext.getResources().getDisplayMetrics());
            maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 640, mContext.getResources().getDisplayMetrics());
        } else {
            maxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, mContext.getResources().getDisplayMetrics());
            maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, mContext.getResources().getDisplayMetrics());
        }

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }


        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    /*calling on galleryUpdatePic*/
    private void galleryUpdatePic(String mediaUrl) {
        Logger.i(TAG, "galleryUpdatePic");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mediaUrl);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, "onActivityResult");
        switch (requestCode) {


            case PICK_IMAGE_REQUEST:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    selectedProfileImageUri = data.getData();
                    Log.d("Aarati" , selectedProfileImageUri.toString());
                    // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    try {
                        Bitmap bitmap = null;
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedProfileImageUri);

                        if (imvLeft.getDrawable() == null) {
                            Glide.with(getActivity())
                                    .load(selectedProfileImageUri)
                                    .into(imvLeft);
                            imvLeftPlus.setVisibility(View.GONE);
                            imvLeftDelete.setVisibility(View.VISIBLE);
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            // String imageFileName = "JPEG_" + timeStamp + "_";
                            String imageFileName = "IMG" + timeStamp + ".jpg";
                            File savedFile = saveBitmapToFile(getActivity().getApplicationContext(), bitmap, imageFileName);
                            if (savedFile != null) {
                                // File saved successfully
                                imgLeftFile = savedFile;
                            } else {
                                // Handle the error
                            }

                        }
                        else {
                            Glide.with(getActivity())
                                    .load(selectedProfileImageUri)
                                    .into(imvRight);
                            imvRightPlus.setVisibility(View.GONE);
                            imvRightDelete.setVisibility(View.VISIBLE);
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            // String imageFileName = "JPEG_" + timeStamp + "_";
                            String imageFileName = "IMG" + timeStamp + ".jpg";
                            File savedFile = saveBitmapToFile(getActivity().getApplicationContext(), bitmap, imageFileName);
                            if (savedFile != null) {
                                // File saved successfully
                                imgRightFile = savedFile;
                            } else {
                                // Handle the error
                            }
                        }
                        Log.d("Aarati", "loaded by glide");
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Log.d("Aarati" , "not loaded");
                }
                break;
            case  REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                    // selectedProfileImageUri = data.getData();
                    Log.d("Aarati", selectedProfileImageUri.toString());
                    // if (data != null && data.getExtras() != null) {
                    //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (selectedProfileImageUri != null) {
                        if (imvLeft.getDrawable() == null)
                        {
                            Glide.with(getActivity())
                                    .load(selectedProfileImageUri)
                                    .into(imvLeft);
                            imvLeftPlus.setVisibility(View.GONE);
                            imvLeftDelete.setVisibility(View.VISIBLE);
                        }
                        else {
                            Glide.with(getActivity())
                                    .load(selectedProfileImageUri)
                                    .into(imvRight);
                            imvRightPlus.setVisibility(View.GONE);
                            imvRightDelete.setVisibility(View.VISIBLE);
                        }
                        Log.d("Aarati", "loaded by glide");
                    }
                   /* } else {
                        Log.d("Aarati", "not loaded");
                    }*/
                    Log.d("Aarati", "Done loaded");
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
                if (!changeTextFromMapFlag) {
                    changeMapPosition();
                    changeLocationTextFlag = true;
                }
                break;
            default: {
//                if (dialog.isShowing())
//                    dialog.dismiss();
            }
            break;
        }
        Log.d("Aarati" , "On Activity result completed");
    }

    String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File directory = new File(getActivity().getFilesDir().toString());

        // have the object build the directory structure, if needed.
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {

            String name =new SimpleDateFormat("'img'yyyyMMddhhmmss'.jpg'").format(new Date());
            Logger.d("heel", directory.toString());
            File f = new File(directory, name);
            if (f.exists())
                f.delete();
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(
                    getContext(),
                    new String[]{f.getAbsolutePath()},
                    new String[]{"image/jpeg"}, null
            );
            fo.close();
            Logger.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch ( IOException e1) {
            CommonUtils.printStackTrace(e1);
        }
        return "";
    }


    private void changeMapPosition() {
        String strAddress="";
        if(!TextUtils.isEmpty(etAddress.getText().toString()))
            strAddress = etAddress.getText().toString().trim() ;
        if(!TextUtils.isEmpty(etCity.getText().toString()))
            strAddress += " " + etCity.getText().toString().trim();
        if(!TextUtils.isEmpty(etZipCode.getText().toString()))
            strAddress += " " + etZipCode.getText().toString().trim();

        strAddress +=" India";
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address != null) {
                Address location = address.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
            }
        } catch (Exception e) {
            if(e.toString().contains("Service not Available"))
            {
                isGeoCodeFroLatLong = true;
//                referWolooHostPresenter.getLocation(String.valueOf(latitude), String.valueOf(longitude));
                wolooViewModel.reverseGeocoding(latitude,longitude);
                //CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
              CommonUtils.printStackTrace(e);
        }
    }


    /*calling on isValid*/
    private boolean isValid() {
        Logger.i(TAG, "isValid");
        if (TextUtils.isEmpty(etName.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.name_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.address_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etCity.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.city_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.phone_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (lastKnownLocation == null) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.location_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imvLeftDelete.getVisibility() == View.GONE || imvLeftDelete.getVisibility() == View.INVISIBLE) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.woloo_picture_validation), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void referredWolooListResponse(ReferredWolooListResponse referredWolooListResponse) {

    }

    /*calling on setProfileResponse*/
    @Override
    public void setProfileResponse(ViewProfileResponse viewProfileResponse) {
        Logger.i(TAG, "setProfileResponse");
        if (viewProfileResponse != null) {
          /*  etName.setText(viewProfileResponse.getUserData().getName());
            etAddress.setText(viewProfileResponse.getUserData().getAddress());
            etCity.setText(viewProfileResponse.getUserData().getCity());*/
        }
    }

    @Override
    public void referWolooHostSuccess(String message) {
        showdialog(message);
    }

    @Override
    public void geoCodeResponseSuccess(GeoCodeResponse geoCodeResponse) {
        if(isGeoCodeFroLatLong)
        {
            isGeoCodeFroLatLong=false;
            List<GeoCodeResponse.DataItem> dataItems = geoCodeResponse.getData();
            for(int i=0;i<dataItems.size();i++){
                if(dataItems.get(i).getTypes().get(0).equals("postal_code")){
                    latitude = dataItems.get(i).getGeometry().getLocation().getLat();
                    longitude = dataItems.get(i).getGeometry().getLocation().getLat();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
                    break;
                }
            }
        }
        else if(isGeoCodeFroAddress){
            isGeoCodeFroAddress = false;
            List<GeoCodeResponse.DataItem> dataItems = geoCodeResponse.getData();
            for(int i=0;i<dataItems.size();i++){
                if(dataItems.get(i).getTypes().get(0).equals("postal_code")){
                    etAddress.setText(dataItems.get(i).getFormattedAddress());
                    List<GeoCodeResponse.AddressComponentsItem> addressComponentsItems =  dataItems.get(i).getAddressComponents();
                    for(int j=0;j<addressComponentsItems.size();j++) {
                        if(addressComponentsItems.get(i).getTypes().contains("locality"))
                            etCity.setText(addressComponentsItems.get(i).getLongName());
                        break;
                    }
                    for(int j=0;j<addressComponentsItems.size();j++) {
                        if(addressComponentsItems.get(i).getTypes().contains("postal_code"))
                            etZipCode.setText(addressComponentsItems.get(i).getLongName());
                        break;
                    }
                    break;
                }
            }
        }
    }


    /*calling on getDeviceLocation*/
    private void getDeviceLocation() {
        Logger.i(TAG, "setProfileResponse");
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            locationPermissionGranted = isLocationPermissionGranted();
            if (locationPermissionGranted) {
                if (!Places.isInitialized()) {
                    String key = CommonUtils.googlemapapikey(getActivity());
                    Places.initialize(getActivity(), key);
                }
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
                getCurrentLocation();

            }
        } catch (SecurityException e) {
            if(e.toString().contains("Service not Available"))
            {
                CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
            Logger.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getCurrentLocation() {
        try {
            locationPermissionGranted = isLocationPermissionGranted();
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                latitude = lastKnownLocation.getLatitude();
                                longitude = lastKnownLocation.getLongitude();

                                if (map != null)
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
                            }
                        }
                    }
                });
            }
        }
        catch (Exception ex){

        }
    }

    /*calling on isLocationPermissionGranted*/
    public boolean isLocationPermissionGranted(){
        Logger.i(TAG, "isLocationPermissionGranted");
        if ( ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                /*if (markerImage.getTranslationY() == 0f) {
                    markerImage.animate()
                            .translationY(-75f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(250)
                            .start();
                }*/
            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (changeLocationTextFlag)
                    changeLocationTextFlag = false;
                else {
                    changeTextFromMapFlag = true;
                   /* markerImage.animate()
                            .translationY(0f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(250)
                            .start();*/

                    LatLng latLng = map.getCameraPosition().target;
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    getAddressForLocation();
                }
            }
        });

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    private void getAddressForLocation() {
        setAddress(latitude, longitude);
    }

    private void setAddress(double latitude, double longitude) {

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() != 0) {
                etAddress.setText(addresses.get(0).getAddressLine(0));
                //shortAddress = generateFinalAddress(fullAddress).trim();
                etCity.setText(addresses.get(0).getLocality());
                etZipCode.setText(addresses.get(0).getPostalCode());
                //mCountryCode = addresses.get(0).getCountryCode();
            } /*else {
                shortAddress = "";
                fullAddress = "";
            }*/
        } catch (Exception e) {
            /*shortAddress = "";
            fullAddress = "";
            addresses = null;*/
            if(e.toString().contains("Service not Available"))
            {
                isGeoCodeFroAddress=true;
//                referWolooHostPresenter.getLocation(String.valueOf(latitude),String.valueOf(longitude));
                wolooViewModel.reverseGeocoding(latitude,longitude);
                //CommonUtils.showCustomDialogBackClick(getActivity(),"Unable to find Location Service. Please start your location Service Or Reboot your device.");
            }
              CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {

    }

    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        Log.d("Aarati" , "in request PERMISSION");
       /* ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private void openGallery() {
        Log.d("Aarati" , "in open gallery");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        Log.d("Aarati" , "in request PERMISSION");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        }
    }


    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

                if (photoFile != null) {
                    if(imgLeftFile == null)
                        imgLeftFile = photoFile;
                    else
                        imgRightFile = photoFile;
                    selectedProfileImageUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedProfileImageUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                // Handle the error
            }

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // String imageFileName = "JPEG_" + timeStamp + "_";
        String imageFileName = "IMG" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    private void showImageUploadDialog() {
        Logger.i(TAG, "showImageUploadDialog");
        try {
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.dialog_profile_image, null);
            alertDialogBuilder.setView(child);
            alertDialogBuilder.setCancelable(true);
            androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvSelectGallery = child.findViewById(R.id.tvSelectGallery);
            TextView tvImageCapture = child.findViewById(R.id.tvImageCapture);
            TextView tv_image = child.findViewById(R.id.tv_image);
           /* if(profileImage == null)
            {
                tv_image.setText(getString(R.string.upload_image));
            }else {
                tv_image.setText(getString(R.string.change_image));
            }*/

            tv_image.setVisibility(View.GONE);

            tvSelectGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkStoragePermission())
                    {
                        Log.d("Aarati" , "PERMISSION GRANTED");
                        openGallery();
                        alertDialog.dismiss();
                    }
                    else
                    {
                        Log.d("Aarati" , "PERMISSION Already not GRANTED");
                        requestStoragePermission();
                        alertDialog.dismiss();
                    }

                }
            });

            tvImageCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(checkCameraPermission())
                    {
                        Log.d("Aarati" , "PERMISSION GRANTED");
                        takePictureIntent();
                        alertDialog.dismiss();
                    }
                    else
                    {
                        Log.d("Aarati" , "PERMISSION Already not GRANTED");
                        requestCameraPermission();
                        alertDialog.dismiss();
                    }

                }
            });

            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    public static File saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        // Get the directory where you want to save the file
        File directory = context.getExternalFilesDir(null); // or getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (directory == null) {
            // If the directory is null, return null or handle it as needed
            return null;
        }

        // Create a file object with the specified file name
        File file = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            // Create a FileOutputStream to write the bitmap to the file
            fos = new FileOutputStream(file);

            // Compress the bitmap and write it to the file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Use CompressFormat.JPEG for JPEG images

            // Flush and close the output stream
            fos.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close the FileOutputStream if it's not null
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}