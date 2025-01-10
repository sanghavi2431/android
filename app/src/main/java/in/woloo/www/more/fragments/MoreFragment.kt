package `in`.woloo.www.more.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jetsynthesys.encryptor.JetEncryptor
import dev.b3nedikt.restring.Restring.locale
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.base.BaseFragment
import `in`.woloo.www.application_kotlin.model.lists_models.ResultVtionSdkModel
import `in`.woloo.www.application_kotlin.presentation.activities.login.LoginActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.AddReviewsFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment.Companion.newInstance
import `in`.woloo.www.application_kotlin.utilities.EmailSenderClass
import `in`.woloo.www.application_kotlin.utilities.MessageList
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.adapter.MoreMenuRecyclerViewAdapter
import `in`.woloo.www.more.callbacks.MenuClickCallback
import `in`.woloo.www.more.editprofile.EditProfileActivity
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.giftcard.GiftCardFragment
import `in`.woloo.www.more.models.FileUploadResponse
import `in`.woloo.www.more.models.SubscriptionStatusResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.models.VoucherDetailsResponse
import `in`.woloo.www.more.mvp.MorePresenter
import `in`.woloo.www.more.mvp.MoreView
import `in`.woloo.www.more.my_account.MyAccountFragment
import `in`.woloo.www.more.my_history.MyHistoryFragment
import `in`.woloo.www.more.refer_woloo_host.ReferredWolooHostListing
import `in`.woloo.www.more.subscribe.MySubscribtionActivity
import `in`.woloo.www.more.subscribe.fragments.SubscribeFragment
import `in`.woloo.www.more.woloo_host.BecomeWolooHostFragment
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CircleImageView
import `in`.woloo.www.utils.IOnCallWSCallBack
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ProfileAPIUtil
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoreFragment  //  private EmailSenderViewModel emailSenderViewModel; // Added by Aarati;
    : BaseFragment(), MoreView, NetworkAPIResponseCallback {
    @JvmField
    @BindView(R.id.rvMenus)
    var rvMenus: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvName)
    var tvName: TextView? = null

    @JvmField
    @BindView(R.id.iv_edit)
    var iv_edit: ImageView? = null

    @JvmField
    @BindView(R.id.tvCity)
    var tvCity: TextView? = null

    @JvmField
    @BindView(R.id.tvMobileNumber)
    var tvMobileNumber: TextView? = null

    @JvmField
    @BindView(R.id.tvWolooPoints)
    var tvWolooPoints: TextView? = null

    @JvmField
    @BindView(R.id.tvWolooPremium)
    var tvWolooPremium: TextView? = null


    @JvmField
    @BindView(R.id.civProfileImage)
    var civProfileImage: CircleImageView? = null


    @JvmField
    @BindView(R.id.tvGender)
    var tvGender: TextView? = null

    @JvmField
    @BindView(R.id.tvWolooPremiumTitle)
    var tvWolooPremiumTitle: TextView? = null

    @JvmField
    @BindView(R.id.ll_wolooPremium)
    var ll_wolooPremium: LinearLayout? = null

    @JvmField
    @BindView(R.id.credit_history_layout)
    var creditHistoryLayout: RelativeLayout? = null


    var menuList: MutableList<String> = ArrayList()

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var morePreseter: MorePresenter? = null
    private var root: View? = null
    private val REQUEST_CAMERA = 0 // REQUEST_GALLERY = 1;
    private val EDIT_PROFILE = 11
    var filePath: String? = null
    var picUri: Uri? = null
    private val PERMISSION_STORAGE = 84
    private val PERMISSION_CAMERA = 85
    private var userProfileResponse: UserProfile? = null
    private var profileViewModel: ProfileViewModel? = null

    var avatarFile: File? = null

    var mobileNumber: String? = "" // Added by Aarati

    var profileImage: String? = "" // Added by Aarati

    private var mDatabase: DatabaseReference? = null // Added By Aarati

    var selectedProfileImageUri: Uri? = Uri.parse("")

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i(Companion.TAG, "onCreate")
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Logger.i(Companion.TAG, "onCreateView")
        // emailSenderViewModel = new ViewModelProvider(this).get(EmailSenderViewModel.class);
        mDatabase = FirebaseDatabase.getInstance().reference
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_more, container, false)
            ButterKnife.bind(this, root!!)
            initViews()
            setLiveData()
        }
        creditHistoryLayout!!.setOnClickListener {
            (activity as WolooDashboard).loadMenuFragment(
                MyAccountFragment.newInstance(true),
                "MyAccountFragment"
            )
        }
        return root
    }

    /*calling on onResume*/
    override fun onResume() {
        super.onResume()

        try {
            profileViewModel!!.getUserProfile()
            (activity as WolooDashboard).hideToolbar()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(Companion.TAG, "initViews")
        try {
            morePreseter = MorePresenter(requireContext(), this@MoreFragment)
            profileViewModel = ViewModelProvider(this).get(
                ProfileViewModel::class.java
            )
            //morePreseter.getUserOffers();
            //morePreseter.getProfile();
            //morePreseter.getSubscriptionDetails();
            setMenuLists()

            /*   tvInviteNow.setOnClickListener(v -> {
                navigateToInviteFriendScreen();
            });*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }


        civProfileImage!!.setImageResource(R.drawable.ic_profile_placeholder)
        civProfileImage!!.setOnClickListener { //Following block is commented by Aarati @woloo on 18 july 2024
            showImageUploadDialog()
            /* if (checkAndRequestPermissions()) {
                        selectImage();
                    }
                    else
                    {
                        Log.d("Aarati Test" , "Permission issue");
                    }*/
        }
    }

    private fun setLiveData() {
//        Toast.makeText(getContext(), "Inside setLiveData", Toast.LENGTH_SHORT).show();
        profileViewModel!!.observeUserProfile().observe(
             requireActivity()
        ) { userProfileBaseResponse ->
            //                Toast.makeText(getContext(), "Inside observeUserProfile onChanged", Toast.LENGTH_SHORT).show();
            Logger.i(Companion.TAG, "setSubscriptionResponse")
            if (userProfileBaseResponse != null) {
                if (userProfileBaseResponse.data != null) {
                    try {
                        userProfileResponse = userProfileBaseResponse.data
                        setMenuLists()
                        mobileNumber = userProfileResponse!!.profile!!.mobile
                        profileImage = userProfileResponse!!.planData!!.image
                        if (userProfileResponse != null && userProfileResponse!!.planData != null && !TextUtils.isEmpty(
                                userProfileResponse!!.planData!!.name
                            )
                        ) {
                            if (userProfileResponse!!.profile != null && !TextUtils.isEmpty(
                                    userProfileResponse!!.profile!!.expiryDate
                                ) && CommonUtils.isSubscriptionExpired(
                                    userProfileResponse!!.profile!!.expiryDate
                                )
                            ) {
                                tvWolooPremiumTitle!!.text =
                                    resources.getString(R.string.expired)
                            } else {
                                tvWolooPremiumTitle!!.text =
                                    resources.getString(R.string.woloo_premium)
                            }
                            Logger.i(
                                Companion.TAG,
                                "PLAN: " + userProfileResponse!!.planData!!.name
                            )
                            //                                Toast.makeText(getContext(),"PLAN: "+ userProfileResponse.getPlanData().getName() , Toast.LENGTH_SHORT).show();
                            tvWolooPremium!!.text = userProfileResponse!!.planData!!.name
                            tvWolooPremium!!.background =
                                ContextCompat.getDrawable(
                                     requireActivity().applicationContext,
                                    R.drawable.woloo_host_limit_bg
                                )
                        } else if (userProfileResponse!!.planData == null) {
                            tvWolooPremiumTitle!!.text =
                                resources.getString(R.string.woloo_premium)
                            tvWolooPremium!!.text =
                                resources.getString(R.string.expired)
                            tvWolooPremium!!.background =
                                ContextCompat.getDrawable(
                                     requireActivity().applicationContext,
                                    R.drawable.woloo_host_membership_expired
                                )
                        } else {
                            tvWolooPremium!!.text = AppConstants.FREE_TRAIL
                            tvWolooPremium!!.background =
                                ContextCompat.getDrawable(
                                     requireActivity().applicationContext,
                                    R.drawable.woloo_host_free_trial
                                )
                        }

                        ll_wolooPremium!!.setOnClickListener { v: View? ->
                            val intent = Intent(
                                activity,
                                MySubscribtionActivity::class.java
                            )
                            //Bundle bundle = new Bundle();
                            intent.putExtra("plan", userProfileResponse)
                            //intent.putExtras(bundle);
                            startActivity(intent)
                        }


                        if (userProfileResponse!!.totalCoins != null && userProfileResponse!!.totalCoins != null) {
                            tvWolooPoints!!.text =
                                "" + userProfileResponse!!.totalCoins!!.totalCoins + " Woloo Points"
                        }
                        Logger.i(
                            Companion.TAG,
                            "setProfileResponse"
                        )


                        //profileResponse = viewProfileResponse;
                        if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.name)) {
                            tvName!!.text = userProfileResponse!!.profile!!.name
                        } else {
                            //                if (commonUtils.isLoggedIn(getContext())) {
                            //                    tvName.setText("");
                            //                } else {
                            tvName!!.text = getString(R.string.guest)
                            //                }
                        }
                        if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.city)) {
                            tvCity!!.text = userProfileResponse!!.profile!!.city
                        } else {
                            //                tvCity.setText("--");
                        }
                        if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.mobile)) {
                            tvMobileNumber!!.text = userProfileResponse!!.profile!!.mobile
                        } else {
                            //                tvMobileNumber.setText("--");
                        }
                        if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.gender)) {
                            tvGender!!.text = userProfileResponse!!.profile!!.gender
                        } else {
                            tvGender!!.text = ""
                        }

                        //                            if (TextUtils.isEmpty(userProfileResponse.getProfile().getAvatar())) {
                        //                                civProfileImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle));
                        //                            } else {
                        if (userProfileResponse == null || userProfileResponse!!.profile == null || userProfileResponse!!.profile!!.avatar == null) {
                            civProfileImage!!.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(), R.drawable.ic_account_circle
                                )
                            )
                        } else {
                            if (TextUtils.isEmpty(userProfileResponse!!.profile!!.avatar) || userProfileResponse!!.profile!!.avatar!!.trim { it <= ' ' } == "users/default.png" || userProfileResponse!!.profile!!.avatar!!.trim { it <= ' ' } == "default.png") {
                                ImageUtil.loadImageProfile(
                                    requireContext(),
                                    civProfileImage!!,
                                    BuildConfig.BASE_URL + "public/userProfile/default.png"
                                )
                            } else {
                                ImageUtil.loadImageProfile(
                                    requireContext(),
                                    civProfileImage!!,
                                    userProfileResponse!!.profile!!.baseUrl + userProfileResponse!!.profile!!.avatar
                                )
                            }
                        }

                        //                            }
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } else {
                //                    displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }
        }

        profileViewModel!!.observeEditProfile().observe(
            viewLifecycleOwner
        ) { editProfileResponseBaseResponse ->
            if (editProfileResponseBaseResponse != null) {
                profileViewModel!!.getUserProfile()
            }
        }
    }

    /*calling on selectImage*/
    private fun selectImage() {
        Logger.i(Companion.TAG, "selectImage")
        val items = arrayOf<CharSequence>("Gallery", "Take Photo", "Cancel")
        val builder = AlertDialog.Builder(
             requireActivity()
        )
        //        builder.setTitle("Add Attachment!");
        builder.setIcon(R.drawable.attachment_grey_ic)
        builder.setItems(items) { dialog, item -> /*  if (items[item].equals("Gallery")) {
                        try {
                            galleryIntent();
                        } catch (Exception e) {
                            CommonUtils.printStackTrace(e);
                        }
                    } else*/
            if (items[item] == "Take Photo") {
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

    private fun changeLanguage() {
        val items = arrayOf<CharSequence>("English", "Hindi", "Cancel")
        val builder = AlertDialog.Builder(
             requireActivity()
        )
        //        builder.setTitle("Add Attachment!");
        builder.setIcon(R.drawable.attachment_grey_ic)
        builder.setItems(items) { dialog, item ->
            if (items[item] == "English") {
                locale = Locale.ENGLISH
            } else if (items[item] == "Hindi") {
                locale = Locale("HI")
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

    /*calling on cameraIntent*/
    private fun cameraIntent() {
        Logger.i(Companion.TAG, "cameraIntent")
        val pictureIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )

        tmpFileUri = getOutputMediaFile(1)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileUri)
        pictureIntent.setFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        startActivityForResult(
            pictureIntent,
            REQUEST_CAMERA
        )
         requireActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    /*calling on getOutputMediaFile*/
    fun getOutputMediaFile(type: Int): Uri? {
        Logger.i(Companion.TAG, "getOutputMediaFile")
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
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val imageFileName = "IMG$timeStamp"
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        if (image != null) {
            return FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                image
            )
        }
        return null
    }

    /*calling on galleryIntent*/
    private fun galleryIntent() {
        Logger.i(Companion.TAG, "galleryIntent")
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

        //   startActivityForResult(i, REQUEST_GALLERY);
    }

    /*calling on navigateToInviteFriendScreen*/
    private fun navigateToInviteFriendScreen() {
        Logger.i(Companion.TAG, "navigateToInviteFriendScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                InviteFriendFragment.newInstance(true),
                "InviteFriendFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on setMenuLists*/
    private fun setMenuLists() {
        Logger.i(Companion.TAG, "setMenuLists")
        try {
            if (menuList != null) {
                menuList.clear()
            }

            //menuList.add("Notification");
            menuList.add("Buy Pee’rs Club Membership")
            menuList.add("My History")
            //menuList.add("Invite A Friend");
            //menuList.add("My Account");
            menuList.add("Offer Cart")
            menuList.add("Woloo Gift-Card")
            menuList.add("Refer Friend")
            menuList.add("Become A Woloo Host")
            menuList.add("Refer a Woloo Host")
            menuList.add("About")
            menuList.add("Terms of Use")
            // menuList.add("Contact Us");
            /*  if (userProfileResponse != null) {
                if(userProfileResponse.getProfile().getVoucherId()!=null || userProfileResponse.getProfile().getSubscriptionId()!=null || userProfileResponse.getProfile().getGiftSubscriptionId()==null)
                    menuList.add("Discontinue Pee’rs Club Membership");
            } else
                menuList.add("Discontinue Pee’rs Club Membership");*/
            //menuList.add("App language");
            menuList.add("Logout")

            // following line added by Aarati @Woloo on 17th Jul 2024.
            //  menuList.add(MessageList.DELETEACC);
            val moreMenuRecyclerViewAdapter: MoreMenuRecyclerViewAdapter =
                MoreMenuRecyclerViewAdapter(
                    requireContext(), menuList, menuClickCallback
                )
            rvMenus!!.layoutManager = LinearLayoutManager(context)
            rvMenus!!.adapter = moreMenuRecyclerViewAdapter

            //            tvName.setOnClickListener(v -> {
//                if(profileResponse != null){
//                    WolooApplication.getInstance().setProfileResponse(profileResponse);
//                }
//                startActivity(new Intent(getContext(), EditProfileActivity.class));
//            });
            iv_edit!!.setOnClickListener {
                if (userProfileResponse != null) {
                    WolooApplication.instance!!.profileResponse = userProfileResponse
                }
                startActivityForResult(
                    Intent(context, EditProfileActivity::class.java),
                    EDIT_PROFILE
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on navigateTonotificationScreen*/ /*   private void navigateTonotificationScreen() {
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
    }*/
    var menuClickCallback: MenuClickCallback = object : MenuClickCallback {
        override fun menuItemClick(position: Int) {
            val menuName = menuList[position]
            val bundle = Bundle()
            val payload = HashMap<String, Any>()

            when (menuName) {
                "Notification" -> {}
                "My Cart" -> {}
                "Buy Pee’rs Club Membership" -> {
                    try {
                        bundle.putString(
                            AppConstants.CURRENT_MEMBERSHIP_ID,
                            userProfileResponse!!.planData!!.planId
                        )
                        logFirebaseEvent(activity, bundle, AppConstants.UPGRADE_CLICK)

                        payload[AppConstants.CURRENT_MEMBERSHIP_ID] =
                            userProfileResponse!!.planData!!.planId.toString()
                        logNetcoreEvent( requireActivity(), payload, AppConstants.BECOME_HOST_CLICK)
                    } catch (e: Exception) {
                    }
                    navigateToSubscriptionScreen(true)
                }

                "My History" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.MY_HISTORY_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.MY_HISTORY_CLICK)
                    navigateToMyHistoryScreen(false)
                }

                "Invite A Friend" -> navigateToInviteFriendScreen()
                "My Account" -> navigateToMyAccountScreen()
                "Offer Cart" -> navigateToMyHistoryScreen(true)
                "Woloo Gift-Card" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.WOLOO_GIFT_CARD_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.WOLOO_GIFT_CARD_CLICK)
                    navigateToGiftCardScreen()
                }

                "Add Review" -> navigateToReviewScreen()
                "Refer Friend" -> navigateToReferScreen()
                "Become A Woloo Host" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.BECOME_HOST_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.BECOME_HOST_CLICK)
                    navigateToWolooHostScreen()
                }

                "Refer a Woloo Host" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.REFER_HOST_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.REFER_HOST_CLICK)
                    navigateToReferWolooHostScreen()
                }

                "About" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.ABOUT_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.ABOUT_CLICK)
                    navigateToAboutScreen()
                }

                "Terms of Use" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.TERMS_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.TERMS_CLICK)
                    navigateToTermsOfUseScreen()
                }

                "Contact Us" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.CONTACT_US_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.CONTACT_US_CLICK)
                    navigateToContactUsScreen()
                }

                "Discontinue Pee’rs Club Membership" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.DISCONTINUE_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.DISCONTINUE_CLICK)
                    if (userProfileResponse!!.planData == null) {
                        showdialog("You don't have an active membership")
                    } else if (userProfileResponse!!.profile != null && !TextUtils.isEmpty(
                            userProfileResponse!!.profile!!.expiryDate
                        ) && CommonUtils.isSubscriptionExpired(
                            userProfileResponse!!.profile!!.expiryDate
                        )
                    ) {
                        showdialog("You don't have an active membership")
                    } else if (userProfileResponse!!.planData!!.isIs_cancel) {
                        showdialog("You have already Unsubscribed the Membership")
                    } else if (userProfileResponse!!.planData!!.name == null || userProfileResponse!!.planData!!.name == "FREE TRIAL") {
                        showdialog("You don't have an active membership")
                    } else if (userProfileResponse!!.purchase_by != null) {
                        if (userProfileResponse!!.purchase_by == AppConstants.PURCHASE_BY_APPLE) {
                            showdialog(AppConstants.PURCHASE_BY_APPLE_MSG)
                        }
                    } else navigateToSubscriptionScreen(false)
                }

                "App language" -> changeLanguage()
                "Logout" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.LOGOUT_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.LOGOUT_CLICK)
                    showLogoutDialog()
                }

                "Delete Account" -> {
                    logFirebaseEvent(activity, bundle, AppConstants.DELETE_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.DELETE_CLICK)
                    if (mobileNumber == null) {
                        Log.d("Delete User", "Mobile Number does not exists")
                        Toast.makeText(
                             requireActivity().applicationContext,
                            MessageList.TRYLATER,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        showDeleteUserDialog()
                    }
                }

                else -> {}
            }
        }
    }

    private fun openMyOffer() {
        val intent = Intent(context, MyHistoryFragment::class.java)
        intent.putExtra("isFromOffer", true)
        startActivity(intent)
    }

    private fun navigateToReferWolooHostScreen() {
        startActivity(Intent(context, ReferredWolooHostListing::class.java))
    }

    /*calling on navigateToTermsOfUseScreen*/
    private fun navigateToTermsOfUseScreen() {
        Logger.i(Companion.TAG, "navigateToTermsOfUseScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            val aboutURL = CommonUtils.getTermsUrl(context)
            (activity as WolooDashboard).loadMenuFragment(
                newInstance(
                    "Terms of use",
                    aboutURL,
                    Companion.TAG
                ), "TermsOfUseFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun navigateToContactUsScreen() {
        Logger.i(Companion.TAG, "navigateToContactUsScreen")
        try {
            showContactUsDialog()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun navigateToReferScreen() {
        (activity as WolooDashboard).loadMenuFragment(
            InviteFriendFragment.newInstance(false),
            "MyHistoryFragment"
        )
    }

    /*calling on navigateToMyHistoryScreen*/
    private fun navigateToMyHistoryScreen(isFromOffer: Boolean) {
        Logger.i(Companion.TAG, "navigateToMyHistoryScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            if (isFromOffer) {
                (activity as WolooDashboard).loadMenuFragment(
                    MyHistoryFragment.newInstance(
                        "",
                        "",
                        true
                    ), "MyHistoryFragment"
                )
            } else {
                (activity as WolooDashboard).loadMenuFragment(
                    MyHistoryFragment.newInstance(
                        "",
                        "",
                        false
                    ), "MyHistoryFragment"
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on navigateToAboutScreen*/
    private fun navigateToAboutScreen() {
        Logger.i(Companion.TAG, "navigateToAboutScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            val aboutURL = CommonUtils.getAboutUrl(context)
            (activity as WolooDashboard).loadMenuFragment(
                newInstance(
                    "About",
                    aboutURL,
                    Companion.TAG
                ), "AboutFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun showdialog(msg: String?) {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_background)
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView

            val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
            tv_msg.text = msg

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling on showLogoutDialog*/
    private fun showLogoutDialog() {
        Logger.i(Companion.TAG, "showLogoutDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                 requireActivity()
            )
            val child = layoutInflater.inflate(R.layout.logout_dialog, null)
            alertDialogBuilder.setView(child)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent_background)
            val tvCancel = child.findViewById<TextView>(R.id.tvCancel)
            val tvLogout = child.findViewById<TextView>(R.id.tvLogout)
            val tv_logout = child.findViewById<TextView>(R.id.tv_logout)
            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                val logoutDialog = authConfigResponse.getcUSTOMMESSAGE()!!.logoutDialog
                tv_logout.text = logoutDialog!!.replace("\\\\n".toRegex(), "\n")
            }

            tvCancel.setOnClickListener { v: View? ->
                alertDialog.dismiss()
            }
            tvLogout.setOnClickListener { v: View? ->
                this@MoreFragment.context?.let {
                    CommonUtils()
                        .clearApplicationData(it)
                }
                startActivity(
                    Intent(
                        this@MoreFragment.activity as WolooDashboard?,
                        LoginActivity::class.java
                    )
                )
                (this@MoreFragment.activity as WolooDashboard).finish()
                alertDialog.dismiss()
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
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
               */
    /* emailSenderViewModel.getEmailStatus().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String status) {
                        // Update the UI with the email status
                        emailSenderViewModel.sendEmailResonse(recipients , "Woloo: Delete Account", "Delete " + mobileNumber + " this user");
                    }
                });*/
    /*
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
    private fun navigateToMyAccountScreen() {
        Logger.i(Companion.TAG, "navigateToMyAccountScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                MyAccountFragment.newInstance(true),
                "MyAccountFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on navigateToReviewScreen*/
    private fun navigateToReviewScreen() {
        Logger.i(Companion.TAG, "navigateToReviewScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                AddReviewsFragment.newInstance(0, ""),
                "AddReviews"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on getImageUri*/
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        Logger.i(Companion.TAG, "getImageUri")
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    /*calling on navigateToSubscriptionScreen*/
    private fun navigateToSubscriptionScreen(isForSubscription: Boolean) {
        Logger.i(Companion.TAG, "navigateToSubscriptionScreen")
        try {
            var isEmail = false
            var email: String? = ""
            var mobile: String? = ""
            if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.mobile)) {
                mobile = userProfileResponse!!.profile!!.mobile
            } else if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.email)) {
                isEmail = true
                email = userProfileResponse!!.profile!!.email
            }
            var subscriptionPlanName: String? = ""
            if (userProfileResponse!!.planData != null) if (!CommonUtils.isSubscriptionExpired(
                    userProfileResponse!!.profile!!.expiryDate
                ) && userProfileResponse!!.planData!!.planId != null
            ) {
                subscriptionPlanName = userProfileResponse!!.planData!!.planId
            }
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                SubscribeFragment.newInstance(
                    subscriptionPlanName,
                    email,
                    isEmail,
                    mobile,
                    isForSubscription,
                    false,
                    userProfileResponse!!.profile!!.expiryDate
                ), "InviteFriendFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on navigateToWolooHostScreen*/
    private fun navigateToWolooHostScreen() {
        Logger.i(Companion.TAG, "navigateToWolooHostScreen")
        try {
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                BecomeWolooHostFragment.newInstance(
                    "",
                    ""
                ), "BecomeWolooHost"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on navigateToGiftCardScreen*/
    private fun navigateToGiftCardScreen() {
        Logger.i(Companion.TAG, "navigateToGiftCardScreen")
        try {
            var isEmail = false
            var email: String? = ""
            var mobile: String? = ""
            if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.mobile)) {
                mobile = userProfileResponse!!.profile!!.mobile
            } else if (!TextUtils.isEmpty(userProfileResponse!!.profile!!.email)) {
                isEmail = true
                email = userProfileResponse!!.profile!!.email
            }
            (activity as WolooDashboard).hideToolbar()
            (activity as WolooDashboard).loadMenuFragment(
                GiftCardFragment.newInstance(
                    email,
                    isEmail,
                    mobile
                ), "GiftCardFragment"
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on setProfileResponse*/
    override fun setProfileResponse(viewProfileResponse: ViewProfileResponse?) {
    }

    /*calling on editProfileSuccess*/
    override fun editProfileSuccess() {
        Logger.i(Companion.TAG, "editProfileSuccess")
        profileViewModel!!.getUserProfile()
        //morePreseter.getProfile();
    }

    /*calling on userCoinsResponseSuccess*/
    @SuppressLint("SetTextI18n")
    override fun userCoinsResponseSuccess(userCoinsResponse: UserCoinsResponse?) {
        Logger.i(Companion.TAG, "userCoinsResponseSuccess")
        try {
            if (userCoinsResponse?.data != null && userCoinsResponse.data!!
                    .totalCoins != null
            ) {
                tvWolooPoints!!.text =
                    "" + userCoinsResponse.data!!.totalCoins + " Woloo Points"
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on setSubscriptionResponse*/
    override fun setSubscriptionResponse(subscriptionStatusResponse: SubscriptionStatusResponse?) {
        Logger.i(Companion.TAG, "setSubscriptionResponse")
        try {
            //this.subscriptionStatusResponse = subscriptionStatusResponse;
            if (subscriptionStatusResponse!!.data != null && subscriptionStatusResponse.data
                    !!.planData != null && !TextUtils.isEmpty(
                    subscriptionStatusResponse.data!!.planData!!.name
                )
            ) {
                if (subscriptionStatusResponse.data!!
                        .userData != null && !TextUtils.isEmpty(
                        subscriptionStatusResponse.data!!.userData!!.expiryDate
                    ) && CommonUtils.isSubscriptionExpired(
                        subscriptionStatusResponse.data!!.userData!!.expiryDate
                    )
                ) {
                    tvWolooPremiumTitle!!.text =
                        resources.getString(R.string.expired)
                } else {
                    tvWolooPremiumTitle!!.text =
                        resources.getString(R.string.woloo_premium)
                }
                tvWolooPremium!!.setText(subscriptionStatusResponse.data!!.planData!!.name)
                tvWolooPremium!!.background = ContextCompat.getDrawable(
                     requireActivity().applicationContext, R.drawable.woloo_host_limit_bg
                )
            } else {
                tvWolooPremium!!.text = AppConstants.FREE_TRAIL
                tvWolooPremium!!.background = ContextCompat.getDrawable(
                     requireActivity().applicationContext, R.drawable.woloo_host_free_trial
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?) {
    }

    override fun setVoucherResponse(voucherDetailsResponse: VoucherDetailsResponse?) {
    }

    /*calling on galleryUpdatePic*/
    private fun galleryUpdatePic(mediaUrl: String) {
        Logger.i(Companion.TAG, "galleryUpdatePic")
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mediaUrl)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
         requireActivity().sendBroadcast(mediaScanIntent)
    }

    /*calling on getScaledBitmap*/
    private fun getScaledBitmap(bm: Bitmap, mContext: Context): Bitmap {
        var bm = bm
        Logger.i(Companion.TAG, "getScaledBitmap")
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

    /*calling on rotateCapturedImage*/
    fun rotateCapturedImage(imagePath: String, mContext: Context?) {
        Logger.i(Companion.TAG, "rotateCapturedImage")
        try {
            var sourceBitmap = BitmapFactory.decodeFile(imagePath)
            sourceBitmap = getScaledBitmap(sourceBitmap,  requireActivity())
            val ei = ExifInterface(imagePath)
            var bitmap: Bitmap? = null
            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            //			Toast.makeText(mContext,"orientation "+orientation,Toast.LENGTH_SHORT).show();
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

    /*calling on getRealPathFromURI*/
    fun getRealPathFromURI(uri: Uri): String {
        Logger.i(Companion.TAG, "getRealPathFromURI")
        var path = ""
        if ( requireActivity().contentResolver != null) {
            val cursor =  requireActivity().contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    /*calling on getPath*/
    private fun getPath(contentUri: Uri): String {
        Logger.i(Companion.TAG, "getPath")
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(
             requireActivity(), contentUri, proj, null, null, null
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
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        Logger.i(Companion.TAG, "getBitmapFromUri")
        val parcelFileDescriptor =  requireActivity().contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    /*calling on getResizedBitmap*/
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        Logger.i(Companion.TAG, "getResizedBitmap")
        var width = image.width
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

    /*calling on onActivityResult*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.i(Companion.TAG, "onActivityResult")
        when (requestCode) {
            EDIT_PROFILE -> if (resultCode == Activity.RESULT_OK) {
                profileViewModel!!.getUserProfile()
                //morePreseter.getProfile();
            }

            REQUEST_CAMERA -> if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) {
                val builder1 = AlertDialog.Builder(
                    requireContext()
                )
                builder1.setMessage("Do you want to upload the profile picture?")
                builder1.setCancelable(false)
                builder1.setPositiveButton(
                    "YES"
                ) { dialog, id ->
                    dialog.cancel()
                    //                                    getcamera(data);
                    if (data != null) {
                        //                        try {
                        val bitmap = data.extras!!["data"] as Bitmap?

                        avatarFile = File(saveImage(bitmap!!))
                        updateAvatar()
                        civProfileImage!!.setImageBitmap(bitmap)

                        //                        } catch (Exception e) {
                        //                            CommonUtils.printStackTrace(e);
                        //                            Toast.makeText(getContext(), "Image saving failed", Toast.LENGTH_SHORT).show();
                        //                        }
                    }
                }
                builder1.setNegativeButton(
                    "NO"
                ) { dialog, id -> dialog.cancel() }
                val alertDialog = builder1.create()
                alertDialog.show()
            }

            PICK_IMAGE_REQUEST -> if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                selectedProfileImageUri = data.data
                Log.d("Aarati", selectedProfileImageUri.toString())
                // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(
                         requireActivity().contentResolver,
                        selectedProfileImageUri
                    )
                    avatarFile = File(saveImage(bitmap))
                    Log.d(
                        "Aarati",
                        avatarFile!!.name + " " + avatarFile.toString() + "avtar in onActivityResult"
                    )
                    updateAvatar()
                    Glide.with( requireActivity())
                        .load(selectedProfileImageUri)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Hide the ProgressBar on failure
                                // progressBar.setVisibility(View.GONE);
                                return false // Return false to allow further handling
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable?>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Hide the ProgressBar on success
                                //   progressBar.setVisibility(View.GONE);
                                return false // Return false to allow Glide to handle the resource
                            }
                        })
                        .into(civProfileImage!!)
                    Log.d("Aarati", "loaded by glide")
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            } else {
                Log.d("Aarati", "not loaded")
            }

            REQUEST_IMAGE_CAPTURE -> if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                // selectedProfileImageUri = data.getData();
                Log.d("Aarati", selectedProfileImageUri.toString())

                try {
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(
                         requireActivity().contentResolver,
                        selectedProfileImageUri
                    )
                    avatarFile = File(saveImage(bitmap))
                    Log.d(
                        "Aarati",
                        avatarFile!!.name + " " + avatarFile.toString() + "avtar in onActivityResult"
                    )
                    updateAvatar()
                    Glide.with( requireActivity())
                        .load(selectedProfileImageUri)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Hide the ProgressBar on failure
                                // progressBar.setVisibility(View.GONE);
                                return false // Return false to allow further handling
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable?>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                // Hide the ProgressBar on success
                                // progressBar.setVisibility(View.GONE);
                                return false // Return false to allow Glide to handle the resource
                            }
                        })
                        .into(civProfileImage!!)
                    Log.d("Aarati", "loaded by glide")
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

                /* if (selectedProfileImageUri != null) {
                        Glide.with(getActivity())
                                .load(selectedProfileImageUri)
                                .into(civProfileImage);
                        Log.d("Aarati", "loaded by glide");

                    }*/
            } else {
                Log.d("Aarati", "Done loaded")
            }

            else -> {}
        }
        Log.d("Aarati", "On Activity result completed")
    }

    fun updateAvatar() {
        Log.d("Aarati", avatarFile!!.name + " " + avatarFile.toString() + "avtar in update")
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
            .addFormDataPart(
                "avatar", avatarFile!!.name,
                RequestBody.create("application/octet-stream".toMediaTypeOrNull(), avatarFile!!)
            )
            .build()
        Log.d(
            "Aarati img",
            avatarFile!!.name + " " + avatarFile.toString() + "avtar in update" + requestBody
        )
        profileViewModel!!.updateProfile(requestBody)
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val directory = File( requireActivity().filesDir.toString())

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
                context,
                arrayOf(f.absolutePath),
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


    /*calling on getcamera*/
    private fun getcamera(data: Intent) {
        Logger.e("Inside camera", " intent")
        Logger.i(Companion.TAG, "getcamera")

        try {
            bbmp = data.extras!!["data"] as Bitmap?
        } catch (e: Exception) {
            try {
                bbmp = getBitmapFromUri(tmpFileUri!!)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

        try {
            if (tmpFileUri!!.path != null) {
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

                val requestOptions = RequestOptions()
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                Glide.with( requireActivity())
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
                            target: Target<Bitmap?>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            ProfileAPIUtil(requireContext(), this@MoreFragment).updateUserProfile(
                                requireActivity(),
                                resource,
                                JetEncryptor.getInstance(),
                                iOnCallWSCallBack,
                                AppConstants.USER_PROFILE
                            )
                            return true
                        }
                    })
                    .into(civProfileImage!!)

                /*new ProfileAPIUtil(getContext(), this).updateUserProfile(getActivity(),
                        ((BitmapDrawable) civProfileImage.getDrawable()).getBitmap(), JetEncryptor.getInstance(), iOnCallWSCallBack ,AppConstants.USER_PROFILE);*/
            }
        } catch (e: Exception) {
            tmpFileUri = getImageUri(
                 requireActivity(),
                bbmp!!
            )

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            val finalFile = File(getRealPathFromURI(tmpFileUri!!))

            if (tmpFileUri != null) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    //  tmpFileUri = getImageUri(getApplicationContext(), bbmp);
                    filePath = getRealPathFromURI(tmpFileUri!!)
                    try {
                        galleryUpdatePic(filePath!!)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                    try {
                        rotateCapturedImage(filePath!!, context)
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
*/
                filePath = filePath!!.replace(" ".toRegex(), "")
                Logger.e("filePath", filePath.toString())
                val requestOptions = RequestOptions()
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                Glide.with( requireActivity())
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
                            target: Target<Bitmap?>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(civProfileImage!!)
            }
        }
    }

    /*calling on getgallery*/
    private fun getgallery(data: Intent) {
        Logger.i(Companion.TAG, "getgallery")
        val selectedImageUri = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor =  requireActivity().contentResolver.query(
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
        filePath = getPath(picUri!!)
        Logger.e("picUri", picUri.toString())
        Logger.e("filePath", filePath.toString())

        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(picturePath)
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
            Logger.e("ExifInterfaceException", "" + e.message)
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
        val requestOptions = RequestOptions()
        //                        requestOptions.placeholder(R.drawable.event_place_holder);
//                        requestOptions.error(R.drawable.event_place_holder);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        //                requestOptions.skipMemoryCache(true);
//                        imageView.setTag(R.id.etTag);
        bbmp = getResizedBitmap(bmp!!, 500)

        //        File temp = new File(filePath);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
//                .addFormDataPart("profile_url",temp.getName(),
//                        RequestBody.create( MediaType.get("image/png"), temp))
//                .build();
//        profileViewModel.updateProfile(requestBody);
        ProfileAPIUtil(requireContext(), this).updateUserProfile(
            requireActivity(),
            bbmp,
            JetEncryptor.getInstance(),
            iOnCallWSCallBack,
            AppConstants.USER_PROFILE
        )

        Glide.with( requireActivity())
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
                    target: Target<Drawable?>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    /*Logger.e("GLIDEIMAGE", "isFirstResource : "+isFirstResource);
                            Logger.e("GLIDEIMAGE", "Image downloaded Url : "+imageURL);*/
                    return false
                }
            })
            .into(civProfileImage!!)
        //                    byteArrayOutputStream = new ByteArrayOutputStream();
//                    rotatedBitmap = getResizedBitmap(bbmp, 500);
//                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    var iOnCallWSCallBack: IOnCallWSCallBack = object : IOnCallWSCallBack {
        override fun onSuccessResponse(fileUploadResponse: FileUploadResponse?) {
            if (fileUploadResponse != null && fileUploadResponse.status
                    .equals(AppConstants.API_SUCCESS, ignoreCase = true)
            ) {
                val mJsObjParam = JSONObject()
                try {
                    mJsObjParam.put(
                        JSONTagConstant.FILE_NAMES,
                        fileUploadResponse.convertedName
                    )
                    mJsObjParam.put(JSONTagConstant.PATH, fileUploadResponse.path)
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }


                //                morePreseter.editProfile(getContext(), mJsObjParam);
            }
        }

        override fun onFailure(volleyError: VolleyError?) {
        }
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {
    }

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

    /*calling on checkAndRequestPermissions*/
    private fun checkAndRequestPermissions(): Boolean {
        Logger.d(Companion.TAG, "checkAndRequestPermissions")
        val permissionReadStorage = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val permissionWriteStorage = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // int permissionMediaStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES);  // Added By Aarati to check with old code but not working.
        val camera = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)


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
                 requireActivity(),
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    /*calling on onRequestPermissionsResult*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Logger.d(Companion.TAG, "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CAMERA
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
                         requireActivity().applicationContext,
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
                         requireActivity().applicationContext,
                        "Permission required to access camera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun showImageUploadDialog() {
        Logger.i(Companion.TAG, "showImageUploadDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                 requireActivity()
            )
            val child = layoutInflater.inflate(R.layout.dialog_profile_image, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(true)
            val alertDialog = alertDialogBuilder.create()
            val tvSelectGallery = child.findViewById<TextView>(R.id.tvSelectGallery)
            val tvImageCapture = child.findViewById<TextView>(R.id.tvImageCapture)
            val tv_image = child.findViewById<TextView>(R.id.tv_image)
            if (profileImage == null) {
                tv_image.text = getString(R.string.upload_image)
            } else {
                tv_image.text = getString(R.string.change_image)
            }

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

    private fun checkStoragePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
             requireActivity().applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestStoragePermission() {
        Log.d("Aarati", "in request PERMISSION")
        /* ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
            requireActivity().applicationContext,
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
        if (takePictureIntent.resolveActivity( requireActivity().packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                if (photoFile != null) {
                    selectedProfileImageUri = FileProvider.getUriForFile(
                        requireContext(),
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
        val storageDir =  requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }


    @SuppressLint("SetTextI18n")
    private fun showDeleteUserDialog() {
        Logger.i(Companion.TAG, "showDeleteUserDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                 requireActivity()
            )
            val child = layoutInflater.inflate(R.layout.logout_dialog, null)
            alertDialogBuilder.setView(child)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent_background)
            val tvCancel = child.findViewById<TextView>(R.id.tvCancel)
            val tvLogout = child.findViewById<TextView>(R.id.tvLogout)
            val tv_logout = child.findViewById<TextView>(R.id.tv_logout)
            tv_logout.text =
                "Are you sure you want to delete your account?\nThis will permanently erase your account. It will take around 48 hrs to delete your data"
            tvLogout.text = "Delete"


            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                val logoutDialog = authConfigResponse.getcUSTOMMESSAGE()!!.logoutDialog
                // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener { v: View? ->
                alertDialog.dismiss()
            }
            tvLogout.setOnClickListener { v: View? ->
                Log.d("Mobile", mobileNumber!!)
                mDatabase!!.child("result").child(mobileNumber!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val result: ResultVtionSdkModel? =
                                    dataSnapshot.getValue<ResultVtionSdkModel>(
                                        ResultVtionSdkModel::class.java
                                    )
                                if (result != null) {
                                    val status: String = result.result.toString()
                                    Log.d(
                                        "FirebaseCheck",
                                        "Mobile number exists: $mobileNumber $status"
                                    )
                                    val subject: String =
                                        MessageList.EMAILSUBJECT + mobileNumber
                                    val message: String =
                                        MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO

                                    if (status.equals(
                                            "SUCCESS",
                                            ignoreCase = true
                                        ) || status.matches("SUCCESS".toRegex())
                                    ) {
                                        // The mobile number exists
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //    EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.VTIONEMAIL,
                                                subject,
                                                message
                                            ) // calling for vtionuser
                                            //    EmailSenderClass.Companion.sendEmail("aaratigujar@gmail.com", subject, message);
                                        }
                                    } else if (status.equals(
                                            "FAILED",
                                            ignoreCase = true
                                        ) || status.matches("FAILED".toRegex())
                                    ) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                    }
                                }

                                // Delete user details from firebase.
                                val mobileNumberRef = mDatabase!!.child("result").child(
                                    mobileNumber!!
                                )


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
                                mobileNumberRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Check if the data exists
                                        if (dataSnapshot.exists()) {
                                            // Get the current data as a ResultVtionSdkModel object
                                            val existingData: ResultVtionSdkModel? =
                                                dataSnapshot.getValue<ResultVtionSdkModel>(
                                                    ResultVtionSdkModel::class.java
                                                )

                                            if (existingData != null) {
                                                // Update the status field
                                                existingData.deleteRequest = true

                                                // Save the updated data back to the database
                                                mobileNumberRef.setValue(existingData)
                                                    .addOnCompleteListener { task: Task<Void?> ->
                                                        if (task.isSuccessful) {
                                                            Log.d(
                                                                "Firebase",
                                                                "Status updated successfully"
                                                            )
                                                        } else {
                                                            Log.e(
                                                                "Firebase",
                                                                "Failed to update status",
                                                                task.exception
                                                            )
                                                        }
                                                    }
                                            }
                                        } else {
                                            Log.d(
                                                "Firebase",
                                                "No data found for mobile number: $mobileNumber"
                                            )
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e(
                                            "Firebase",
                                            "Database error: " + databaseError.message
                                        )
                                    }
                                })


                                this@MoreFragment.context?.let {
                                    CommonUtils()
                                        .clearApplicationData(it)
                                }
                                startActivity(
                                    Intent(
                                        this@MoreFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@MoreFragment.activity as WolooDashboard).finish()
                                alertDialog.dismiss()
                            } else {
                                // The mobile number does not exist
                                Log.d(
                                    "FirebaseCheck",
                                    "Mobile number does not exist: $mobileNumber"
                                )
                                val subject: String = MessageList.EMAILSUBJECT + mobileNumber
                                val message: String =
                                    MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    EmailSenderClass.Companion.sendEmail(
                                        MessageList.WOLOOEMAIL,
                                        subject,
                                        message
                                    )

                                    // EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                }
                                this@MoreFragment.context?.let {
                                    CommonUtils()
                                        .clearApplicationData(it)
                                }
                                startActivity(
                                    Intent(
                                        this@MoreFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@MoreFragment.activity as WolooDashboard).finish()
                                alertDialog.dismiss()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle possible errors
                            Log.e(
                                "FirebaseCheck",
                                "Database error: " + databaseError.message
                            )
                            Toast.makeText(
                                 requireActivity().applicationContext,
                                MessageList.TRYLATER,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showContactUsDialog() {
        Logger.i(Companion.TAG, "showDeleteUserDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                 requireActivity()
            )
            val child = layoutInflater.inflate(R.layout.logout_dialog, null)
            alertDialogBuilder.setView(child)
            val alertDialog = alertDialogBuilder.create()
            val tvCancel = child.findViewById<TextView>(R.id.tvCancel)
            val tvLogout = child.findViewById<TextView>(R.id.tvLogout)
            val tv_logout = child.findViewById<TextView>(R.id.tv_logout)
            tv_logout.text =
                "Are you sure you want to delete your account? This will permanently erase your account. It will take around 48 hrs to delete your data"
            tvLogout.text = "Delete Account"


            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                val logoutDialog = authConfigResponse.getcUSTOMMESSAGE()!!.logoutDialog
                // tv_logout.setText(logoutDialog.replaceAll("\\\\n", "\n"));
            }

            tvCancel.setOnClickListener { v: View? ->
                alertDialog.dismiss()
            }
            tvLogout.setOnClickListener { v: View? ->
                Log.d("Mobile", mobileNumber!!)
                mDatabase!!.child("result").child(mobileNumber!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val result: ResultVtionSdkModel? =
                                    dataSnapshot.getValue<ResultVtionSdkModel>(
                                        ResultVtionSdkModel::class.java
                                    )
                                if (result != null) {
                                    val status: String = result.result.toString()
                                    Log.d(
                                        "FirebaseCheck",
                                        "Mobile number exists: $mobileNumber $status"
                                    )
                                    val subject: String =
                                        MessageList.EMAILSUBJECT + mobileNumber
                                    val message: String =
                                        MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO

                                    if (status.equals(
                                            "SUCCESS",
                                            ignoreCase = true
                                        ) || status.matches("SUCCESS".toRegex())
                                    ) {
                                        // The mobile number exists
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                        }
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.VTIONEMAIL,
                                                subject,
                                                message
                                            )
                                        }
                                    } else if (status.equals(
                                            "FAILED",
                                            ignoreCase = true
                                        ) || status.matches("FAILED".toRegex())
                                    ) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                        }
                                    } else {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            EmailSenderClass.Companion.sendEmail(
                                                MessageList.WOLOOEMAIL,
                                                subject,
                                                message
                                            )
                                            //  EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                        }
                                    }
                                }

                                // Delete user details from firebase.
                                val mobileNumberRef = mDatabase!!.child("result").child(
                                    mobileNumber!!
                                )


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
                                mobileNumberRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Check if the data exists
                                        if (dataSnapshot.exists()) {
                                            // Get the current data as a ResultVtionSdkModel object
                                            val existingData: ResultVtionSdkModel? =
                                                dataSnapshot.getValue<ResultVtionSdkModel>(
                                                    ResultVtionSdkModel::class.java
                                                )

                                            if (existingData != null) {
                                                // Update the status field
                                                existingData.deleteRequest = true

                                                // Save the updated data back to the database
                                                mobileNumberRef.setValue(existingData)
                                                    .addOnCompleteListener { task: Task<Void?> ->
                                                        if (task.isSuccessful) {
                                                            Log.d(
                                                                "Firebase",
                                                                "Status updated successfully"
                                                            )
                                                        } else {
                                                            Log.e(
                                                                "Firebase",
                                                                "Failed to update status",
                                                                task.exception
                                                            )
                                                        }
                                                    }
                                            }
                                        } else {
                                            Log.d(
                                                "Firebase",
                                                "No data found for mobile number: $mobileNumber"
                                            )
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e(
                                            "Firebase",
                                            "Database error: " + databaseError.message
                                        )
                                    }
                                })


                                this@MoreFragment.context?.let {
                                    CommonUtils()
                                        .clearApplicationData(it)
                                }
                                startActivity(
                                    Intent(
                                        this@MoreFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@MoreFragment.activity as WolooDashboard).finish()
                                alertDialog.dismiss()
                            } else {
                                // The mobile number does not exist
                                Log.d(
                                    "FirebaseCheck",
                                    "Mobile number does not exist: $mobileNumber"
                                )
                                val subject: String = MessageList.EMAILSUBJECT + mobileNumber
                                val message: String =
                                    MessageList.EMAILMESSAGEPARTONE + mobileNumber + MessageList.EMAILMESSAGEPARTTWO
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    EmailSenderClass.Companion.sendEmail(
                                        MessageList.WOLOOEMAIL,
                                        subject,
                                        message
                                    )

                                    // EmailSenderClass.Companion.sendEmail("abhijeetmalkapure11@gmail.com", subject, message);
                                }
                                this@MoreFragment.context?.let {
                                    CommonUtils()
                                        .clearApplicationData(it)
                                }
                                startActivity(
                                    Intent(
                                        this@MoreFragment.activity as WolooDashboard?,
                                        LoginActivity::class.java
                                    )
                                )
                                (this@MoreFragment.activity as WolooDashboard).finish()
                                alertDialog.dismiss()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle possible errors
                            Log.e(
                                "FirebaseCheck",
                                "Database error: " + databaseError.message
                            )
                            Toast.makeText(
                                 requireActivity().applicationContext,
                                MessageList.TRYLATER,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 80

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        private var tmpFileUri: Uri? = null
        var bbmp: Bitmap? = null
        private const val PERMISSION_REQUEST_CODE = 701

        var isNeedToUpdateProfile: Boolean = false
        private const val REQUEST_READ_EXTERNAL_STORAGE = 111
        private const val PICK_IMAGE_REQUEST = 112

        private const val REQUEST_CAMERA_PERMISSION = 113
        private const val REQUEST_IMAGE_CAPTURE = 114

        private const val REQUEST_WRITE_STORAGE_PERMISSION = 115

        var TAG: String = MoreFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): MoreFragment {
            val fragment = MoreFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        /*calling on createDirectory*/
        fun createDirectory(filePath: String) {
            Logger.i(TAG, "createDirectory")
            if (!File(filePath).exists()) {
                File(filePath).mkdirs()
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

        /*calling on saveBitmap*/
        fun saveBitmap(mBitmap: Bitmap, destinationPath: File?) {
            Logger.i(TAG, "saveBitmap")
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(destinationPath)
                if (mBitmap.hasAlpha()) mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                else mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: NullPointerException) {
                // CommonUtils.printStackTrace(e);
            } catch (e: Exception) {
                // CommonUtils.printStackTrace(e);
            } finally {
                try {
                    out!!.close()
                } catch (ignore: Throwable) {
                }
            }
        }
    }
}