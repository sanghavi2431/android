package in.woloo.www.more.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jetsynthesys.encryptor.JetEncryptor;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.b3nedikt.restring.Restring;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.base.BaseFragment;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.editprofile.EditProfileActivity;
import in.woloo.www.giftcard.GiftCardFragment;
import in.woloo.www.invite_friend.fragments.InviteFriendFragment;
import in.woloo.www.more.adapter.MoreMenuRecyclerViewAdapter;
import in.woloo.www.more.callbacks.MenuClickCallback;
import in.woloo.www.more.models.FileUploadResponse;
import in.woloo.www.more.models.SubscriptionStatusResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.models.VoucherDetailsResponse;
import in.woloo.www.more.mvp.MorePresenter;
import in.woloo.www.more.mvp.MoreView;
import in.woloo.www.my_account.MyAccountFragment;
import in.woloo.www.my_history.MyHistoryFragment;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.notification.NotificationFragment;
import in.woloo.www.refer_woloo_host.ReferredWolooHostListing;
import in.woloo.www.review.fragments.AddReviewsFragment;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.MySubscribtionActivity;
import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.EmailSender;
import in.woloo.www.utils.EmailSenderViewModel;
import in.woloo.www.utils.IOnCallWSCallBack;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ProfileAPIUtil;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.login.activity.LoginActivity;
import in.woloo.www.v2.login.viewmodel.LoginViewModel;
import in.woloo.www.v2.profile.model.EditProfileResponse;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.vtion.model.ResultVtionSdkModel;
import in.woloo.www.vtion.utilities.EmailSenderClass;
import in.woloo.www.vtion.utilities.MessageList;
import in.woloo.www.webview.WebViewFragment;
import in.woloo.www.woloo_host.BecomeWolooHostFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends BaseFragment implements MoreView, NetworkAPIResponseCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 80;

    @BindView(R.id.rvMenus)
    RecyclerView rvMenus;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.iv_edit)
    ImageView iv_edit;

    @BindView(R.id.tvCity)
    TextView tvCity;

    @BindView(R.id.tvMobileNumber)
    TextView tvMobileNumber;

    @BindView(R.id.tvWolooPoints)
    TextView tvWolooPoints;

    @BindView(R.id.tvWolooPremium)
    TextView tvWolooPremium;

    @BindView(R.id.tvInviteNow)
    TextView tvInviteNow;

    @BindView(R.id.civProfileImage)
    CircleImageView civProfileImage;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.iv_pencilicon)
    ImageView iv_pencilicon;

    @BindView(R.id.tvGender)
    TextView tvGender;

    @BindView(R.id.tvWolooPremiumTitle)
    TextView tvWolooPremiumTitle;

    @BindView(R.id.ll_wolooPremium)
    LinearLayout ll_wolooPremium;


    List<String> menuList = new ArrayList<String>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MorePresenter morePreseter;
    private View root;
    private final int REQUEST_CAMERA = 0;// REQUEST_GALLERY = 1;
    private final int EDIT_PROFILE = 11;
    private static Uri tmpFileUri;
    String filePath;
    Uri picUri;
    public static Bitmap bbmp;
    private static final int PERMISSION_REQUEST_CODE = 701;

    private final int PERMISSION_STORAGE = 84;
    private final int PERMISSION_CAMERA = 85;
    private UserProfile userProfileResponse;
    private ProfileViewModel profileViewModel;

    private LoginViewModel loginViewModel;
    public static boolean isNeedToUpdateProfile = false;
    File avatarFile;

    String mobileNumber = ""; // Added by Aarati

    String profileImage = ""; // Added by Aarati

    private DatabaseReference mDatabase; // Added By Aarati

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 111;
    private static final int PICK_IMAGE_REQUEST = 112;

    private static final int REQUEST_CAMERA_PERMISSION = 113;
    private static final int REQUEST_IMAGE_CAPTURE = 114;

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 115;

    Uri selectedProfileImageUri = Uri.parse("");

    //  private EmailSenderViewModel emailSenderViewModel; // Added by Aarati;

    public MoreFragment() {
        // Required empty public constructor
    }
    public static String TAG= MoreFragment.class.getSimpleName();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView");
       // emailSenderViewModel = new ViewModelProvider(this).get(EmailSenderViewModel.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_more, container, false);
            ButterKnife.bind(this, root);
            initViews();
            setLiveData();
        }
        return root;
    }

    /*calling on onResume*/
    @Override
    public void onResume() {
        super.onResume();

       try {
            profileViewModel.getUserProfile();
            ((WolooDashboard) getActivity()).hideToolbar();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }

    }
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            morePreseter = new MorePresenter(getContext(), MoreFragment.this);
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
            //morePreseter.getUserOffers();
            //morePreseter.getProfile();
            //morePreseter.getSubscriptionDetails();
            setMenuLists();
            tvInviteNow.setOnClickListener(v -> {
                navigateToInviteFriendScreen();
            });

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }

        iv_pencilicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                civProfileImage.performClick();
            }
        });

        civProfileImage.setImageResource(R.drawable.ic_profile_placeholder);
        civProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Following block is commented by Aarati @woloo on 18 july 2024
                showImageUploadDialog();
               /* if (checkAndRequestPermissions()) {
                    selectImage();
                }
                else
                {
                    Log.d("Aarati Test" , "Permission issue");
                }*/
            }
        });
    }

    private void setLiveData(){
//        Toast.makeText(getContext(), "Inside setLiveData", Toast.LENGTH_SHORT).show();
        profileViewModel.observeUserProfile().observe(getActivity(), new Observer<BaseResponse<UserProfile>>() {
            @Override
            public void onChanged(BaseResponse<UserProfile> userProfileBaseResponse) {
//                Toast.makeText(getContext(), "Inside observeUserProfile onChanged", Toast.LENGTH_SHORT).show();

                Logger.i(TAG, "setSubscriptionResponse");
                if (userProfileBaseResponse != null) {
                    if (userProfileBaseResponse.getData() != null) {
                        try {
                            userProfileResponse = userProfileBaseResponse.getData();
                            setMenuLists();
                            mobileNumber = userProfileResponse.getProfile().getMobile();
                            profileImage = userProfileResponse.getPlanData().getImage();
                            if (userProfileResponse != null && userProfileResponse.getPlanData() != null && !TextUtils.isEmpty(userProfileResponse.getPlanData().getName())) {
                                if (userProfileResponse.getProfile() != null && !TextUtils.isEmpty(userProfileResponse.getProfile().getExpiryDate()) && CommonUtils.isSubscriptionExpired(userProfileResponse.getProfile().getExpiryDate())) {
                                    tvWolooPremiumTitle.setText(getResources().getString(R.string.expired));
                                } else {
                                    tvWolooPremiumTitle.setText(getResources().getString(R.string.woloo_premium));
                                }
                                Logger.i(TAG, "PLAN: "+ userProfileResponse.getPlanData().getName());
//                                Toast.makeText(getContext(),"PLAN: "+ userProfileResponse.getPlanData().getName() , Toast.LENGTH_SHORT).show();
                                tvWolooPremium.setText(userProfileResponse.getPlanData().getName());
                            } else if (userProfileResponse.getPlanData() == null) {
                                tvWolooPremiumTitle.setText(getResources().getString(R.string.woloo_premium));
                                tvWolooPremium.setText(getResources().getString(R.string.expired));
                            } else {
                                tvWolooPremium.setText(AppConstants.FREE_TRAIL);
                            }

                            ll_wolooPremium.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), MySubscribtionActivity.class);
                                //Bundle bundle = new Bundle();
                                intent.putExtra("plan", userProfileResponse);
                                //intent.putExtras(bundle);
                                startActivity(intent);
                            });


                            if (userProfileResponse.getTotalCoins() != null && userProfileResponse.getTotalCoins() != null) {
                                tvWolooPoints.setText("" + userProfileResponse.getTotalCoins().getTotalCoins());
                            }
                            Logger.i(TAG, "setProfileResponse");



                            //profileResponse = viewProfileResponse;
                            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getName())) {
                                tvName.setText(userProfileResponse.getProfile().getName());
                            } else {
//                if (commonUtils.isLoggedIn(getContext())) {
//                    tvName.setText("");
//                } else {
                                tvName.setText(getString(R.string.guest));
//                }
                            }
                            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getCity())) {
                                tvCity.setText(userProfileResponse.getProfile().getCity());
                            } else {
//                tvCity.setText("--");
                            }
                            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                                tvMobileNumber.setText(userProfileResponse.getProfile().getMobile());
                            } else {
//                tvMobileNumber.setText("--");
                            }
                            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getGender())) {
                                tvGender.setText(userProfileResponse.getProfile().getGender());
                            } else {
                                tvGender.setText("");
                            }
//                            if (TextUtils.isEmpty(userProfileResponse.getProfile().getAvatar())) {
//                                civProfileImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle));
//                            } else {

                            if (userProfileResponse == null || userProfileResponse.getProfile() == null || userProfileResponse.getProfile().getAvatar() == null) {
                                civProfileImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle));
                            } else {
                                if (TextUtils.isEmpty(userProfileResponse.getProfile().getAvatar()) || userProfileResponse.getProfile().getAvatar().trim().equals("users/default.png") || userProfileResponse.getProfile().getAvatar().trim().equals("default.png")) {
                                    ImageUtil.loadImageProfile(getContext(), civProfileImage, BuildConfig.BASE_URL + "public/userProfile/default.png");
                                } else {
                                    ImageUtil.loadImageProfile(getContext(), civProfileImage, userProfileResponse.getProfile().getBaseUrl() + userProfileResponse.getProfile().getAvatar());
                                }
                            }

//                            }
                        } catch (Exception ex) {
                            CommonUtils.printStackTrace(ex);
                        }
                    }
                }else {
//                    displayToast(WolooApplication.getErrorMessage())
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        profileViewModel.observeEditProfile().observe(getViewLifecycleOwner(), new Observer<BaseResponse<EditProfileResponse>>() {
            @Override
            public void onChanged(BaseResponse<EditProfileResponse> editProfileResponseBaseResponse) {
                if(editProfileResponseBaseResponse!= null){
                    profileViewModel.getUserProfile();
                }
            }
        });
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

              /*  if (items[item].equals("Gallery")) {
                    try {
                        galleryIntent();
                    } catch (Exception e) {
                        CommonUtils.printStackTrace(e);
                    }
                } else*/ if (items[item].equals("Take Photo")) {
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

    private void changeLanguage() {
        final CharSequence[] items = {"English", "Hindi", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Attachment!");
        builder.setIcon(R.drawable.attachment_grey_ic);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("English")) {
                    Restring.setLocale(Locale.ENGLISH);
                } else if (items[item].equals("Hindi")) {
                    Restring.setLocale(new Locale("HI"));
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

    /*calling on cameraIntent*/
    private void cameraIntent() {
        Logger.i(TAG, "cameraIntent");
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        tmpFileUri = getOutputMediaFile(1);
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri);
        pictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(pictureIntent,
                REQUEST_CAMERA);
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


    }

    /*calling on getOutputMediaFile*/
    public Uri getOutputMediaFile(int type) {
        Logger.i(TAG, "getOutputMediaFile");
        /*File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
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
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "IMG" + timeStamp;
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
            return FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", image);
        }
        return null;
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

     //   startActivityForResult(i, REQUEST_GALLERY);
    }

    /*calling on navigateToInviteFriendScreen*/
    private void navigateToInviteFriendScreen() {
        Logger.i(TAG, "navigateToInviteFriendScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(InviteFriendFragment.newInstance(true), "InviteFriendFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on setMenuLists*/
    private void setMenuLists() {
        Logger.i(TAG, "setMenuLists");
        try {
            if (menuList != null) {
                menuList.clear();
            }

            //menuList.add("Notification");
            menuList.add("Buy Pee’rs Club Membership");
            menuList.add("My History");
            //menuList.add("Invite A Friend");
            //menuList.add("My Account");
            menuList.add("Offer Cart");
            menuList.add("Woloo Gift-Card");
            menuList.add("Become A Woloo Host");
            menuList.add("Refer a Woloo Host");
            menuList.add("About");
            menuList.add("Terms of Use");
            menuList.add("Contact Us");
            if (userProfileResponse != null) {
                if(userProfileResponse.getProfile().getVoucherId()!=null || userProfileResponse.getProfile().getSubscriptionId()!=null || userProfileResponse.getProfile().getGiftSubscriptionId()==null)
                    menuList.add("Discontinue Pee’rs Club Membership");
            } else
                menuList.add("Discontinue Pee’rs Club Membership");
            //menuList.add("App language");
            menuList.add("Logout");
            // following line added by Aarati @Woloo on 17th Jul 2024.
            menuList.add(MessageList.DELETEACC);

            MoreMenuRecyclerViewAdapter moreMenuRecyclerViewAdapter = new MoreMenuRecyclerViewAdapter(getContext(), menuList, menuClickCallback);
            rvMenus.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMenus.setAdapter(moreMenuRecyclerViewAdapter);

//            tvName.setOnClickListener(v -> {
//                if(profileResponse != null){
//                    WolooApplication.getInstance().setProfileResponse(profileResponse);
//                }
//                startActivity(new Intent(getContext(), EditProfileActivity.class));
//            });

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userProfileResponse != null) {
                        WolooApplication.getInstance().setProfileResponse(userProfileResponse);
                    }
                    startActivityForResult(new Intent(getContext(), EditProfileActivity.class),
                            EDIT_PROFILE);
                }
            });

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on navigateTonotificationScreen*/
    private void navigateTonotificationScreen() {
        Logger.i(TAG, "navigateTonotificationScreen");
        try {
            boolean isEmail = false;
            String msg = "";
            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                msg = userProfileResponse.getProfile().getMobile();
            } else if (!TextUtils.isEmpty(userProfileResponse.getProfile().getEmail())) {
                isEmail = true;
                msg = userProfileResponse.getProfile().getEmail();
            }
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(NotificationFragment.newInstance(userProfileResponse.getPlanData().getName(), msg, isEmail), "NotificationFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    MenuClickCallback menuClickCallback = new MenuClickCallback() {
        @Override
        public void menuItemClick(int position) {
            String menuName = menuList.get(position);
            Bundle bundle = new Bundle();
            HashMap<String,Object> payload = new HashMap<>();

            switch (menuName) {
                case "Notification":
                    navigateTonotificationScreen();
                    break;
                case "My Cart":
                    break;
                case "Buy Pee’rs Club Membership":
                    try {
                        bundle.putString(AppConstants.CURRENT_MEMBERSHIP_ID, userProfileResponse.getPlanData().getPlanId());
                        Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.UPGRADE_CLICK);

                        payload.put(AppConstants.CURRENT_MEMBERSHIP_ID, userProfileResponse.getPlanData().getPlanId());
                        Utility.logNetcoreEvent(getActivity(),payload,AppConstants.BECOME_HOST_CLICK);
                    }
                    catch (Exception e){

                    }
                    navigateToSubscriptionScreen(true);
                    break;
                case "My History":
                    Utility.logFirebaseEvent(getActivity(),bundle,AppConstants.MY_HISTORY_CLICK);
                    Utility.logNetcoreEvent(getActivity(),payload,AppConstants.MY_HISTORY_CLICK);
                    navigateToMyHistoryScreen(false);
                    break;
                case "Invite A Friend":
                    navigateToInviteFriendScreen();
                    break;
                case "My Account":
                    navigateToMyAccountScreen();
                    break;
                case "Offer Cart":
                    navigateToMyHistoryScreen(true);
                    break;
                case "Woloo Gift-Card":
                    Utility.logFirebaseEvent(getActivity(),bundle,AppConstants.WOLOO_GIFT_CARD_CLICK);
                    Utility.logNetcoreEvent(getActivity(),payload,AppConstants.WOLOO_GIFT_CARD_CLICK);
                    navigateToGiftCardScreen();
                    break;
                case "Add Review":
                    navigateToReviewScreen();
                    break;
                case "Become A Woloo Host":
                    Utility.logFirebaseEvent(getActivity(),bundle,AppConstants.BECOME_HOST_CLICK);
                    Utility.logNetcoreEvent(getActivity(),payload,AppConstants.BECOME_HOST_CLICK);
                    navigateToWolooHostScreen();
                    break;
                case "Refer a Woloo Host":
                    Utility.logFirebaseEvent(getActivity(),bundle,AppConstants.REFER_HOST_CLICK);
                    Utility.logNetcoreEvent(getActivity(),payload,AppConstants.REFER_HOST_CLICK);
                    navigateToReferWolooHostScreen();
                    break;
                case "About":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.ABOUT_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.ABOUT_CLICK);
                    navigateToAboutScreen();
                    break;
                case "Terms of Use":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.TERMS_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.TERMS_CLICK);
                    navigateToTermsOfUseScreen();
                    break;
                case "Contact Us":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.CONTACT_US_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.CONTACT_US_CLICK);
                    navigateToContactUsScreen();
                    break;

                case "Discontinue Pee’rs Club Membership":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.DISCONTINUE_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.DISCONTINUE_CLICK);
                    if (userProfileResponse.getPlanData() == null) {
                        showdialog("You don't have an active membership");
                    }
                    else if (userProfileResponse.getProfile() != null && !TextUtils.isEmpty(userProfileResponse.getProfile().getExpiryDate()) && CommonUtils.isSubscriptionExpired(userProfileResponse.getProfile().getExpiryDate())) {
                        showdialog("You don't have an active membership");
                    }else if (userProfileResponse.getPlanData().isIs_cancel()) {
                        showdialog("You have already Unsubscribed the Membership");
                    }
                    // Following null check is added by Aarati @Woloo, on 13 Jul 24
                    else if (userProfileResponse.getPlanData().getName() == null || userProfileResponse.getPlanData().getName().equals("FREE TRIAL"))
                         {
                        showdialog("You don't have an active membership");
                    }else if (userProfileResponse.getPurchase_by() != null) {
                        if (userProfileResponse.getPurchase_by().equals(AppConstants.PURCHASE_BY_APPLE)) {
                            showdialog(AppConstants.PURCHASE_BY_APPLE_MSG);
                        }
                    }else
                        navigateToSubscriptionScreen(false);
                    break;
                case "App language":
                    changeLanguage();
                    break;
                case "Logout":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.LOGOUT_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.LOGOUT_CLICK);
                    showLogoutDialog();
                    break;
                    // following case added by Aarati @Woloo on 17th Jul 2024.
                case "Delete Account":
                    Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.DELETE_CLICK);
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.DELETE_CLICK);
                    if(mobileNumber == null)
                    {
                        Log.d("Delete User" , "Mobile Number does not exists");
                        Toast.makeText(getActivity().getApplicationContext() , MessageList.TRYLATER , Toast.LENGTH_SHORT).show();

                    }else {
                        showDeleteUserDialog();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private void openMyOffer() {
        Intent intent = new Intent(getContext(),MyHistoryFragment.class);
        intent.putExtra("isFromOffer",true);
        startActivity(intent);
    }

    private void navigateToReferWolooHostScreen() {
        startActivity(new Intent(getContext(), ReferredWolooHostListing.class));
    }

    /*calling on navigateToTermsOfUseScreen*/
    private void navigateToTermsOfUseScreen() {
        Logger.i(TAG, "navigateToTermsOfUseScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            String aboutURL = CommonUtils.getTermsUrl(getContext());
            ((WolooDashboard) getActivity()).loadMenuFragment(WebViewFragment.newInstance("Terms of use", aboutURL , MoreFragment.TAG), "TermsOfUseFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private void navigateToContactUsScreen()
    {
        Logger.i(TAG, "navigateToContactUsScreen");
        try {
            showContactUsDialog();
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on navigateToMyHistoryScreen*/
    private void navigateToMyHistoryScreen(boolean isFromOffer) {
        Logger.i(TAG, "navigateToMyHistoryScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            if (isFromOffer){
                ((WolooDashboard) getActivity()).loadMenuFragment(MyHistoryFragment.newInstance("", "",true), "MyHistoryFragment");
            }else {
                ((WolooDashboard) getActivity()).loadMenuFragment(MyHistoryFragment.newInstance("", "",false), "MyHistoryFragment");
            }
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on navigateToAboutScreen*/
    private void navigateToAboutScreen() {
        Logger.i(TAG, "navigateToAboutScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            String aboutURL = CommonUtils.getAboutUrl(getContext());
            ((WolooDashboard) getActivity()).loadMenuFragment(WebViewFragment.newInstance("About", aboutURL , MoreFragment.TAG), "AboutFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on createDirectory*/
    public static void createDirectory(String filePath) {
        Logger.i(TAG, "createDirectory");
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
    }

    public void showdialog(String msg) {
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

    /*calling on showLogoutDialog*/
    private void showLogoutDialog() {
        Logger.i(TAG, "showLogoutDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.logout_dialog, null);
            alertDialogBuilder.setView(child);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvCancel = child.findViewById(R.id.tvCancel);
            TextView tvLogout = child.findViewById(R.id.tvLogout);
            TextView tv_logout = child.findViewById(R.id.tv_logout);
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String logoutDialog = authConfigResponse.getcUSTOMMESSAGE().getLogoutDialog();
                tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                alertDialog.dismiss();
            });
            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    // following block added by Aarati @Woloo on 17th Jul 2024.
  /*  @SuppressLint("SetTextI18n")
    private void showDeleteUserDialog() {
        Logger.i(TAG, "showDeleteUserDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.logout_dialog, null);
            alertDialogBuilder.setView(child);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvCancel = child.findViewById(R.id.tvCancel);
            TextView tvLogout = child.findViewById(R.id.tvLogout);
            TextView tv_logout = child.findViewById(R.id.tv_logout);
            tv_logout.setText("Are you sure you want to delete your account? This will permanently erase your account. It will take around 48 hrs to delete your data");
            tvLogout.setText("Delete Account");
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String logoutDialog = authConfigResponse.getcUSTOMMESSAGE().getLogoutDialog();
               // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                Log.d("Mobile" , mobileNumber);
                List<String>  recipients = Arrays.asList("supreet@woloo.in", "woloo.in@gmail.com" , "aaratigujar@gmail.com");
               *//* emailSenderViewModel.getEmailStatus().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String status) {
                        // Update the UI with the email status
                        emailSenderViewModel.sendEmailResonse(recipients , "Woloo: Delete Account", "Delete " + mobileNumber + " this user");
                    }
                });*//*
              //  EmailSender.Companion.sendEmail(recipients , "Woloo: Delete Account", "Delete " + mobileNumber + " this user");
                String subject = "User Deletion Request - " + mobileNumber;
                String message = "Hi Woloo Support Team,\n\n\tUser " +  mobileNumber  +" has requested for deletion of account, kindly do the needful.\n\n\nThanks & Regards,\nWoloo Team ";
                EmailSender.Companion.sendEmail("aaratigujar@gmail.com", subject, message);
                new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                alertDialog.dismiss();
            });
            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }*/





    /*calling on navigateToMyAccountScreen*/
    private void navigateToMyAccountScreen() {
        Logger.i(TAG, "navigateToMyAccountScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(MyAccountFragment.newInstance(true), "MyAccountFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on navigateToReviewScreen*/
    private void navigateToReviewScreen() {
        Logger.i(TAG, "navigateToReviewScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(AddReviewsFragment.newInstance(0, ""), "AddReviews");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on getImageUri*/
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Logger.i(TAG, "getImageUri");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /*calling on navigateToSubscriptionScreen*/
    private void navigateToSubscriptionScreen(boolean isForSubscription) {
        Logger.i(TAG, "navigateToSubscriptionScreen");
        try {
            boolean isEmail = false;
            String email = "";
            String mobile = "";
            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                mobile = userProfileResponse.getProfile().getMobile();
            } else if (!TextUtils.isEmpty(userProfileResponse.getProfile().getEmail())) {
                isEmail = true;
                email = userProfileResponse.getProfile().getEmail();
            }
            String subscriptionPlanName = "";
            if (userProfileResponse.getPlanData() != null)
                if (!CommonUtils.isSubscriptionExpired(userProfileResponse.getProfile().getExpiryDate()) && userProfileResponse.getPlanData().getPlanId() != null) {
                    subscriptionPlanName = userProfileResponse.getPlanData().getPlanId();
                }
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(SubscribeFragment.newInstance(subscriptionPlanName, email, isEmail, mobile, isForSubscription, false, userProfileResponse.getProfile().getExpiryDate()), "InviteFriendFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on navigateToWolooHostScreen*/
    private void navigateToWolooHostScreen() {
        Logger.i(TAG, "navigateToWolooHostScreen");
        try {
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(BecomeWolooHostFragment.newInstance("", ""), "BecomeWolooHost");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on navigateToGiftCardScreen*/
    private void navigateToGiftCardScreen() {
        Logger.i(TAG, "navigateToGiftCardScreen");
        try {
            boolean isEmail = false;
            String email = "";
            String mobile = "";
            if (!TextUtils.isEmpty(userProfileResponse.getProfile().getMobile())) {
                mobile = userProfileResponse.getProfile().getMobile();
            } else if (!TextUtils.isEmpty(userProfileResponse.getProfile().getEmail())) {
                isEmail = true;
                email = userProfileResponse.getProfile().getEmail();
            }
            ((WolooDashboard) getActivity()).hideToolbar();
            ((WolooDashboard) getActivity()).loadMenuFragment(GiftCardFragment.newInstance( email, isEmail, mobile), "GiftCardFragment");
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on setProfileResponse*/
    @Override
    public void setProfileResponse(ViewProfileResponse viewProfileResponse) {
    }

    /*calling on editProfileSuccess*/
    @Override
    public void editProfileSuccess() {
        Logger.i(TAG, "editProfileSuccess");
        profileViewModel.getUserProfile();
        //morePreseter.getProfile();
    }
    /*calling on userCoinsResponseSuccess*/
    @Override
    public void userCoinsResponseSuccess(UserCoinsResponse userCoinsResponse) {
        Logger.i(TAG, "userCoinsResponseSuccess");
        try {
            if (userCoinsResponse != null && userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null) {
                tvWolooPoints.setText("" + userCoinsResponse.getData().getTotalCoins());
            }
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on setSubscriptionResponse*/
    @Override
    public void setSubscriptionResponse(SubscriptionStatusResponse subscriptionStatusResponse) {
        Logger.i(TAG, "setSubscriptionResponse");
        try {
            //this.subscriptionStatusResponse = subscriptionStatusResponse;
            if (subscriptionStatusResponse.getData() != null && subscriptionStatusResponse.getData().getPlanData() != null && !TextUtils.isEmpty(subscriptionStatusResponse.getData().getPlanData().getName())) {
                if (subscriptionStatusResponse.getData().getUserData() != null && !TextUtils.isEmpty(subscriptionStatusResponse.getData().getUserData().getExpiryDate()) && CommonUtils.isSubscriptionExpired(subscriptionStatusResponse.getData().getUserData().getExpiryDate())) {
                    tvWolooPremiumTitle.setText(getResources().getString(R.string.expired));
                } else {
                    tvWolooPremiumTitle.setText(getResources().getString(R.string.woloo_premium));
                }
                tvWolooPremium.setText(subscriptionStatusResponse.getData().getPlanData().getName());
            }
            else {
                tvWolooPremium.setText(AppConstants.FREE_TRAIL);
            }
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse) {
    }

    @Override
    public void setVoucherResponse(VoucherDetailsResponse voucherDetailsResponse) {

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
    /*calling on RotateBitmap*/
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Logger.i(TAG, "RotateBitmap");
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);

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
            // CommonUtils.printStackTrace(e);
        } catch (Exception e) {
            // CommonUtils.printStackTrace(e);
        } finally {
            try {
                out.close();
            } catch (Throwable ignore) {
            }
        }
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
    /*calling on getPath*/
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
    /*calling on onActivityResult*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, "onActivityResult");
        switch (requestCode) {
            case EDIT_PROFILE:
                if (resultCode == RESULT_OK) {
                    profileViewModel.getUserProfile();
                    //morePreseter.getProfile();
                }
                break;

            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Do you want to upload the profile picture?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
//                                    getcamera(data);
                                    if (data != null) {
//                        try {
                                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                                        avatarFile = new File(saveImage(bitmap));
                                        updateAvatar();
                                        civProfileImage.setImageBitmap(bitmap);

//                        } catch (Exception e) {
//                            CommonUtils.printStackTrace(e);
//                            Toast.makeText(getContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
//                        }
                                    }
                                }
                            });
                    builder1.setNegativeButton(
                            "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                }
                break;

            case PICK_IMAGE_REQUEST:
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                 selectedProfileImageUri = data.getData();
                Log.d("Aarati" , selectedProfileImageUri.toString());
               // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    Bitmap bitmap = null;
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedProfileImageUri);
                    avatarFile = new File(saveImage(bitmap));
                    Log.d("Aarati" , avatarFile.getName() +" " + avatarFile.toString() + "avtar in onActivityResult");
                    updateAvatar();
                    Glide.with(getActivity())
                            .load(selectedProfileImageUri)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // Hide the ProgressBar on failure
                                    progressBar.setVisibility(View.GONE);
                                    return false; // Return false to allow further handling
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Hide the ProgressBar on success
                                    progressBar.setVisibility(View.GONE);
                                    return false; // Return false to allow Glide to handle the resource
                                }
                            })
                            .into(civProfileImage);
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

                    try {
                        Bitmap bitmap = null;
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedProfileImageUri);
                        avatarFile = new File(saveImage(bitmap));
                        Log.d("Aarati" , avatarFile.getName() +" " + avatarFile.toString() + "avtar in onActivityResult");
                        updateAvatar();
                        Glide.with(getActivity())
                                .load(selectedProfileImageUri)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // Hide the ProgressBar on failure
                                        progressBar.setVisibility(View.GONE);
                                        return false; // Return false to allow further handling
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        // Hide the ProgressBar on success
                                        progressBar.setVisibility(View.GONE);
                                        return false; // Return false to allow Glide to handle the resource
                                    }
                                })
                                .into(civProfileImage);
                        Log.d("Aarati", "loaded by glide");
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                   /* if (selectedProfileImageUri != null) {
                        Glide.with(getActivity())
                                .load(selectedProfileImageUri)
                                .into(civProfileImage);
                        Log.d("Aarati", "loaded by glide");

                    }*/

                }
                else {
                    Log.d("Aarati" , "Done loaded");
                }
                break;
          /*  case REQUEST_GALLERY:
                if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && null != data) {
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Do you want to upload the profile picture?");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(
                                "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
//                                        getgallery(data);
                                        if (data != null) {
                                            Uri contentURI = data.getData();
                                            try {
                                                Bitmap bitmap =
                                                        MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                                                avatarFile = new File(saveImage(bitmap));
                                                updateAvatar();
                                                civProfileImage.setImageBitmap(bitmap);


                                            } catch (IOException e) {
                                                CommonUtils.printStackTrace(e);
                                                Toast.makeText(getContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                });
                        builder1.setNegativeButton(
                                "NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();
                    }


                }
                break;*/




            default: {
//                if (dialog.isShowing())
//                    dialog.dismiss();
            }
            break;
        }
        Log.d("Aarati" , "On Activity result completed");
    }

    void updateAvatar(){
        Log.d("Aarati" , avatarFile.getName() +" " + avatarFile.toString() + "avtar in update");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                .addFormDataPart("avatar", avatarFile.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), avatarFile))
                .build();
        Log.d("Aarati img" , avatarFile.getName() +" " + avatarFile.toString() + "avtar in update" + requestBody);
        profileViewModel.updateProfile(requestBody);
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


    /*calling on getcamera*/
    private void getcamera(Intent data) {
        Logger.e("Inside camera", " intent");
        Logger.i(TAG, "getcamera");

        try {
            bbmp = (Bitmap) data.getExtras().get("data");
        } catch (Exception e) {
            try {
                bbmp = getBitmapFromUri(tmpFileUri);
            } catch (Exception ex) {
                CommonUtils.printStackTrace(ex);
            }
        }

        try {
            if (tmpFileUri.getPath() != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
/*
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
//                                 CommonUtils.printStackTrace(e);
//                            }

                Logger.e("filePath", filePath);
*/

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
                                new ProfileAPIUtil(getContext(), MoreFragment.this).updateUserProfile(getActivity(),
                                        resource, JetEncryptor.getInstance(), iOnCallWSCallBack ,AppConstants.USER_PROFILE);
                                return true;
                            }
                        })
                        .into(civProfileImage);

                /*new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(),
                        ((BitmapDrawable) civProfileImage.getDrawable()).getBitmap(), JetEncryptor.getInstance(), iOnCallWSCallBack ,AppConstants.USER_PROFILE);*/

            }
        } catch (Exception e) {

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
                        .into(civProfileImage);


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
//                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_UNDEFINED);
//
//                    Bitmap rotatedBitmap = null;
//                    switch (orientation) {
//
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            rotatedBitmap = rotateImage(bmp, 90);
//                            break;
//
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            rotatedBitmap = rotateImage(bmp, 180);
//                            break;
//
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            rotatedBitmap = rotateImage(bmp, 270);
//                            break;
//
//                        case ExifInterface.ORIENTATION_NORMAL:
//                        default:
//                            rotatedBitmap = bmp;
//                    }

//                    onCaptureImageResult(getResizedBitmap(rotatedBitmap, 500));

        RequestOptions requestOptions = new RequestOptions();
//                        requestOptions.placeholder(R.drawable.event_place_holder);
//                        requestOptions.error(R.drawable.event_place_holder);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//                requestOptions.skipMemoryCache(true);
//                        imageView.setTag(R.id.etTag);
        bbmp = getResizedBitmap(bmp, 500);

//        File temp = new File(filePath);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
//                .addFormDataPart("profile_url",temp.getName(),
//                        RequestBody.create( MediaType.get("image/png"), temp))
//                .build();
//        profileViewModel.updateProfile(requestBody);

        new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(), bbmp, JetEncryptor.getInstance(), iOnCallWSCallBack, AppConstants.USER_PROFILE);

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
                .into(civProfileImage);
//                    byteArrayOutputStream = new ByteArrayOutputStream();
//                    rotatedBitmap = getResizedBitmap(bbmp, 500);
//                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    IOnCallWSCallBack iOnCallWSCallBack = new IOnCallWSCallBack() {
        @Override
        public void onSuccessResponse(FileUploadResponse fileUploadResponse) {
            if (fileUploadResponse != null && fileUploadResponse.getStatus().equalsIgnoreCase(AppConstants.API_SUCCESS)) {
                JSONObject mJsObjParam = new JSONObject();
                try {
                    mJsObjParam.put(JSONTagConstant.FILE_NAMES, fileUploadResponse.getConvertedName());
                    mJsObjParam.put(JSONTagConstant.PATH, fileUploadResponse.getPath());
                } catch (Exception e) {
                    CommonUtils.printStackTrace(e);
                }


//                morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        @Override
        public void onFailure(VolleyError volleyError) {

        }
    };


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
    /*calling on checkAndRequestPermissions*/
    private boolean checkAndRequestPermissions() {
        Logger.d(TAG, "checkAndRequestPermissions");
        int permissionReadStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
       // int permissionMediaStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES);  // Added By Aarati to check with old code but not working.
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
    /*calling on onRequestPermissionsResult*/
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


    private void showImageUploadDialog() {
        Logger.i(TAG, "showImageUploadDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.dialog_profile_image, null);
            alertDialogBuilder.setView(child);
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvSelectGallery = child.findViewById(R.id.tvSelectGallery);
            TextView tvImageCapture = child.findViewById(R.id.tvImageCapture);
            TextView tv_image = child.findViewById(R.id.tv_image);
            if(profileImage == null)
            {
                tv_image.setText(getString(R.string.upload_image));
            }else {
                tv_image.setText(getString(R.string.change_image));
            }

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
                    selectedProfileImageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
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



    @SuppressLint("SetTextI18n")
    private void showDeleteUserDialog() {
        Logger.i(TAG, "showDeleteUserDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.logout_dialog, null);
            alertDialogBuilder.setView(child);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvCancel = child.findViewById(R.id.tvCancel);
            TextView tvLogout = child.findViewById(R.id.tvLogout);
            TextView tv_logout = child.findViewById(R.id.tv_logout);
            tv_logout.setText("Are you sure you want to delete your account? This will permanently erase your account. It will take around 48 hrs to delete your data");
            tvLogout.setText("Delete Account");



            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String logoutDialog = authConfigResponse.getcUSTOMMESSAGE().getLogoutDialog();
                // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                Log.d("Mobile" , mobileNumber);

                loginViewModel.deleteWolooUser(userProfileResponse.getProfile().getId());

                mDatabase.child("result").child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            ResultVtionSdkModel result = dataSnapshot.getValue(ResultVtionSdkModel.class);
                            if (result != null) {
                                String status = result.getResult();
                                Log.d("FirebaseCheck", "Mobile number exists: " + mobileNumber + " " + status);
                                String subject = MessageList.EMAILSUBJECT + mobileNumber;
                                String message = MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO;

                                if (status.equalsIgnoreCase("SUCCESS")||status.matches("SUCCESS")) {
                                    // The mobile number exists
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                     //    EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.VTIONEMAIL, subject, message); // calling for vtionuser
                                     //    EmailSenderClass.Companion.sendEmail("aaratigujar@gmail.com", subject, message);
                                    }
                                }
                                else if (status.equalsIgnoreCase("FAILED")||status.matches("FAILED")) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                       //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }

                                }
                                else  {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                          EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                      //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }

                                }
                            }

                            // Delete user details from firebase.
                            DatabaseReference mobileNumberRef = mDatabase.child("result").child(mobileNumber);
                           /* mobileNumberRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Deletion successful
                                    Log.d("FirebaseDelete", "Mobile number entry deleted successfully: " + mobileNumber);

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

                                } else {
                                    // Handle possible errors
                                    Log.e("FirebaseDelete", "Failed to delete mobile number entry: " + task.getException().getMessage());
                                    mDatabase.child("result").child(mobileNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase F", "Error getting data", task.getException());
                                            }
                                            else {
                                                DataSnapshot dataSnapshot = task.getResult();
                                                Log.d("firebase F", String.valueOf(dataSnapshot));
                                            }
                                        }
                                    });
                                }
                            });*/


// Retrieve the data
                            mobileNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Check if the data exists
                                    if (dataSnapshot.exists()) {
                                        // Get the current data as a ResultVtionSdkModel object
                                        ResultVtionSdkModel existingData = dataSnapshot.getValue(ResultVtionSdkModel.class);

                                        if (existingData != null) {
                                            // Update the status field
                                            existingData.setDeleteRequest(true);

                                            // Save the updated data back to the database
                                            mobileNumberRef.setValue(existingData).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("Firebase", "Status updated successfully");
                                                } else {
                                                    Log.e("Firebase", "Failed to update status", task.getException());
                                                }
                                            });
                                        }
                                    } else {
                                        Log.d("Firebase", "No data found for mobile number: " + mobileNumber);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("Firebase", "Database error: " + databaseError.getMessage());
                                }
                            });


                            new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                            startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                            ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                            alertDialog.dismiss();
                        } else {
                            // The mobile number does not exist
                            Log.d("FirebaseCheck", "Mobile number does not exist: " + mobileNumber);
                            String subject = MessageList.EMAILSUBJECT + mobileNumber;
                            String message = MessageList.EMAILMESSAGEPARTONE +  mobileNumber  + MessageList.EMAILMESSAGEPARTTWO;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                  EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                               // EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);

                            }
                            new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                            startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                            ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("FirebaseCheck", "Database error: " + databaseError.getMessage());
                        Toast.makeText(getActivity().getApplicationContext() , MessageList.TRYLATER , Toast.LENGTH_SHORT).show();
                    }
                });


            });
            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }



    @SuppressLint("SetTextI18n")
    private void showContactUsDialog() {
        Logger.i(TAG, "showDeleteUserDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.logout_dialog, null);
            alertDialogBuilder.setView(child);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvCancel = child.findViewById(R.id.tvCancel);
            TextView tvLogout = child.findViewById(R.id.tvLogout);
            TextView tv_logout = child.findViewById(R.id.tv_logout);
            tv_logout.setText("Are you sure you want to delete your account? This will permanently erase your account. It will take around 48 hrs to delete your data");
            tvLogout.setText("Delete Account");



            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String logoutDialog = authConfigResponse.getcUSTOMMESSAGE().getLogoutDialog();
                // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                Log.d("Mobile" , mobileNumber);



                mDatabase.child("result").child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            ResultVtionSdkModel result = dataSnapshot.getValue(ResultVtionSdkModel.class);
                            if (result != null) {
                                String status = result.getResult();
                                Log.d("FirebaseCheck", "Mobile number exists: " + mobileNumber + " " + status);
                                String subject = MessageList.EMAILSUBJECT + mobileNumber;
                                String message = MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO;

                                if (status.equalsIgnoreCase("SUCCESS")||status.matches("SUCCESS")) {
                                    // The mobile number exists
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                        //    EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.VTIONEMAIL, subject, message); // calling for vtionuser
                                        //    EmailSenderClass.Companion.sendEmail("aaratigujar@gmail.com", subject, message);
                                    }
                                }
                                else if (status.equalsIgnoreCase("FAILED")||status.matches("FAILED")) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                        //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }

                                }
                                else  {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                        //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                    }

                                }
                            }

                            // Delete user details from firebase.
                            DatabaseReference mobileNumberRef = mDatabase.child("result").child(mobileNumber);
                           /* mobileNumberRef.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Deletion successful
                                    Log.d("FirebaseDelete", "Mobile number entry deleted successfully: " + mobileNumber);

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

                                } else {
                                    // Handle possible errors
                                    Log.e("FirebaseDelete", "Failed to delete mobile number entry: " + task.getException().getMessage());
                                    mDatabase.child("result").child(mobileNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase F", "Error getting data", task.getException());
                                            }
                                            else {
                                                DataSnapshot dataSnapshot = task.getResult();
                                                Log.d("firebase F", String.valueOf(dataSnapshot));
                                            }
                                        }
                                    });
                                }
                            });*/


// Retrieve the data
                            mobileNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Check if the data exists
                                    if (dataSnapshot.exists()) {
                                        // Get the current data as a ResultVtionSdkModel object
                                        ResultVtionSdkModel existingData = dataSnapshot.getValue(ResultVtionSdkModel.class);

                                        if (existingData != null) {
                                            // Update the status field
                                            existingData.setDeleteRequest(true);

                                            // Save the updated data back to the database
                                            mobileNumberRef.setValue(existingData).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("Firebase", "Status updated successfully");
                                                } else {
                                                    Log.e("Firebase", "Failed to update status", task.getException());
                                                }
                                            });
                                        }
                                    } else {
                                        Log.d("Firebase", "No data found for mobile number: " + mobileNumber);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("Firebase", "Database error: " + databaseError.getMessage());
                                }
                            });


                            new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                            startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                            ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                            alertDialog.dismiss();
                        } else {
                            // The mobile number does not exist
                            Log.d("FirebaseCheck", "Mobile number does not exist: " + mobileNumber);
                            String subject = MessageList.EMAILSUBJECT + mobileNumber;
                            String message = MessageList.EMAILMESSAGEPARTONE +  mobileNumber  + MessageList.EMAILMESSAGEPARTTWO;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                EmailSenderClass.Companion.sendEmail(MessageList.WOLOOEMAIL, subject, message);
                                // EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);

                            }
                            new CommonUtils().clearApplicationData(MoreFragment.this.getContext());
                            startActivity(new Intent((WolooDashboard) MoreFragment.this.getActivity(), LoginActivity.class));
                            ((WolooDashboard) MoreFragment.this.getActivity()).finish();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("FirebaseCheck", "Database error: " + databaseError.getMessage());
                        Toast.makeText(getActivity().getApplicationContext() , MessageList.TRYLATER , Toast.LENGTH_SHORT).show();
                    }
                });


            });
            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

}