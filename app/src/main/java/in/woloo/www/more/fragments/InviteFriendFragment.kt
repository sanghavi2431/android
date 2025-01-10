package `in`.woloo.www.more.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment.Companion.newInstance
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.fragments.contacts.InviteContactsActivity
import `in`.woloo.www.more.fragments.model.Contacts
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ProgressBarUtils
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import jagerfield.mobilecontactslibrary.Contact.Contact
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [InviteFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InviteFriendFragment : Fragment() {
    /* @BindView(R.id.tvTitle)
       TextView tvTitle;*/
    @JvmField
    @BindView(R.id.termsAndConditionTv)
    var termsAndConditionTv: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.tv_contacts)
    var tv_contacts: TextView? = null

    @JvmField
    @BindView(R.id.ivShare)
    var ivShare: ImageView? = null

    @JvmField
    @BindView(R.id.ivWhatsApp)
    var ivWhatsApp: ImageView? = null

    @JvmField
    @BindView(R.id.tvRefferalCode)
    var tvRefferalCode: TextView? = null

    /*   @BindView(R.id.toolbar)
    Toolbar toolbar;*/
    @JvmField
    @BindView(R.id.tvReferalMsg)
    var tvReferalMsg: TextView? = null


    // TODO: Rename and change types of parameters
    private var isShowBackButton = false
    private val mParam2 = false
    var personNames: ArrayList<Contacts>? = null
    var profileViewModel: ProfileViewModel? = null
    var refcode: String? = ""
    private var final_message = ""

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isShowBackButton = requireArguments().getBoolean(ARG_PARAM1)
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling on onResume*/
    override fun onResume() {
        Logger.i(TAG, "onResume")
        super.onResume()
        (activity as WolooDashboard).hideToolbar()
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.i(TAG, "onCreateView")
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_invite_friend, container, false)
        ButterKnife.bind(this, rootView)
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        profileViewModel!!.getUserProfile()
        initView()
        setLiveData()
        /*   if (isShowBackButton) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.GONE);
        }*/
        termsAndConditionTv!!.setOnClickListener { v: View? ->
            Logger.i(
                TAG,
                "navigateToTermsOfUseScreen"
            )
            try {
                (activity as WolooDashboard).hideToolbar()
                val aboutURL = CommonUtils.getTermsUrl(context)
                (activity as WolooDashboard).loadMenuFragment(
                    newInstance(
                        "Terms of use",
                        aboutURL,
                        TAG
                    ), "TermsOfUseFragment"
                )
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
        return rootView
    }

    /*calling on initView*/
    private fun initView() {
        Logger.i(TAG, "initView")
        try {
            //  tvTitle.setText(getText(R.string.invite_friend));

            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                if (authConfigResponse?.getcUSTOMMESSAGE() != null && !TextUtils.isEmpty(
                        authConfigResponse.getcUSTOMMESSAGE()!!.referralRewardMessage
                    )
                ) {
                    tvReferalMsg!!.text =
                        authConfigResponse.getcUSTOMMESSAGE()!!.referralRewardMessage
                }
            }


            ivBack!!.setOnClickListener { v: View? ->
                try {
                    //getActivity().onBackPressed();
                    val fm = requireActivity().supportFragmentManager
                    if (fm.backStackEntryCount > 0) {
                        fm.popBackStack()
                    } else {
                        (requireActivity() as WolooDashboard).loadFragment(
                            ContentCommerceFragment(),
                            ContentCommerceFragment.TAG
                        )
                        (requireActivity() as WolooDashboard).changeIcon(
                            (requireActivity() as WolooDashboard).nav_view!!.getMenu()
                                .findItem(R.id.navigation_dash_home)
                        )
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }

            //            ivFacebook.setOnClickListener(v -> {
//                sharemessage_onfb();
//            });

//            ivWhatsApp.setOnClickListener(v -> {
//                shareMessageOnWhatsapp();
//            });
            val pm = requireActivity().packageManager
            if (CommonUtils.isPackageInstalled("com.whatsapp", pm)) {
                ivWhatsApp!!.visibility = View.VISIBLE
            } else {
                ivWhatsApp!!.visibility = View.GONE
            }

            ivShare!!.setOnClickListener { v: View? ->
                val bundle = Bundle()
                logFirebaseEvent(
                    context,
                    bundle,
                    AppConstants.SHARE_CLICK
                )

                val payload = HashMap<String, Any>()
                logNetcoreEvent(
                    requireContext(),
                    payload,
                    AppConstants.SHARE_CLICK
                )
                shareMessage(false)
            }

            ivWhatsApp!!.setOnClickListener { v: View? ->
                val bundle = Bundle()
                logFirebaseEvent(
                    context,
                    bundle,
                    AppConstants.SHARE_CLICK
                )

                val payload = HashMap<String, Any>()
                logNetcoreEvent(
                    requireContext(),
                    payload,
                    AppConstants.SHARE_CLICK
                )
                shareMessage(true)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
        personNames = ArrayList()
        tv_contacts!!.setOnClickListener {
            try {
                val bundle = Bundle()
                logFirebaseEvent(
                    context,
                    bundle,
                    AppConstants.INVITE_CONTACT_CLICK
                )

                val payload = HashMap<String, Any>()
                logNetcoreEvent(
                    requireContext(),
                    payload,
                    AppConstants.INVITE_CONTACT_CLICK
                )

                if (checkAndRequestPermissions()) {
                    //new ContactsLogs().execute();
                    contactsLogs()
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
    }

    private fun setLiveData() {
        profileViewModel!!.observeUserProfile().observe(
            viewLifecycleOwner
        ) { userProfileResponse ->
            if (userProfileResponse?.data != null) {
                refcode = userProfileResponse.data!!.profile!!.refCode
                try {
                    tvRefferalCode!!.text = refcode
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            } else {
                Toast.makeText(
                    context,
                    WolooApplication.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                WolooApplication.errorMessage = ""
            }
        }
    }

    /*calling on sharemessage_onfb*/
    private fun sharemessage_onfb() {
        Logger.i(TAG, "sharemessage_onfb")
        try {
            var message = ""
            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                message = authConfigResponse.getuRLS()!!.app_share_url.toString()
            }

            val share = Intent(Intent.ACTION_SEND)
            share.setType("text/plain")
            share.putExtra(Intent.EXTRA_TEXT, message)
            share.setPackage("com.facebook.katana") //Facebook App package
            startActivity(Intent.createChooser(share, "Woloo Share"))
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling on ContactsLogs*/
    private inner class ContactsLogs : AsyncTask<Void?, Void?, Void?>() {
        var pdLoading: ProgressDialog = ProgressDialog(context)

        override fun onPreExecute() {
            super.onPreExecute()

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading Contacts...");
//            pdLoading.show();
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: Void?): Void? {
            Logger.i(TAG, "ContactsLogs")
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                ImportContactsAsync(
                    (context as Activity?)!!,
                    object : ImportContactsAsync.ICallback {
                        override fun mobileContacts(contactList: ArrayList<Contact>) {
                            var listItem = contactList

                            if (listItem == null) {
                                listItem = ArrayList()
                                Logger.i("C.TAG_LIB", "Error in retrieving contacts")
                            }

                            if (listItem.isEmpty()) {
//                            Toast.makeText(DashboardActivity.this, "No contacts found", Toast.LENGTH_LONG).show();
                            }
                            try {
                                var obj: JSONObject
                                val jsonArray = JSONArray()
                                for (i in listItem.indices) {
                                    obj = JSONObject()
                                    //                    Logger.e("listitems",listItem.get(i).getFirstName()+" "+listItem.get(i).getLastName()+","+listItem.get(i).getNumbers().get(0).elementValue());
                                    obj.put("first_name", listItem[i].firstName)
                                    obj.put("last_name", listItem[i].lastName)
                                    try {
                                        obj.put(
                                            "mobile_number",
                                            listItem[i].numbers[0].elementValue()
                                                .replace("\\s+".toRegex(), "")
                                        )
                                    } catch (e: Exception) {
                                        obj.put("mobile_number", "")
                                    }

                                    try {
                                        obj.put("type", listItem[i].numbers[0].numType)
                                    } catch (e: Exception) {
                                        obj.put("type", "")
                                    }


                                    //                                obj.put("addresses", listItem.get(i).getAddresses());
//                                obj.put("displaydname", listItem.get(i).getDisplaydName());
//                                obj.put("id", listItem.get(i).getId());
//                                obj.put("emails", listItem.get(i).getEmails());
//                                obj.put("events", listItem.get(i).getEvents());
//                                obj.put("nicknames", listItem.get(i).getNickNames());
                                    jsonArray.put(obj)
                                }
                                Logger.e("contacts", jsonArray.toString())
                            } catch (e: JSONException) {
                                // TODO Auto-generated catch block
                                CommonUtils.printStackTrace(e)
                            }
                        }
                    }).execute()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }


            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)


            //this method will be running on UI thread


//            pdLoading.dismiss();
        }
    }

    /*calling on shareMessage*/
    fun shareMessage(isWhatsapp: Boolean?) {
        Logger.i(TAG, "shareMessage")
        try {
//            String message = CommonUtils.authconfig_response(getContext()).getData().getuRLS().getApp_share_url();
//            AuthConfigResponse authConfigResponse = CommonUtils.authconfig_response(getContext());
//            if (authConfigResponse != null) {
//                message = authConfigResponse.getData().getuRLS().getApp_share_url();
//            }

            val authConfigResponse = getPreferences.fetchAuthConfig()
            var message = authConfigResponse!!.getcUSTOMMESSAGE()!!.inviteFriendText
            message =
                if (CommonUtils().userInfo!!.name != null) message!!.replace(
                    "{name}",
                    CommonUtils().userInfo!!.name!!
                ).replace(
                    "{number}",
                    "(" + CommonUtils().userInfo!!.mobile + ")"
                )
                else message!!.replace("{name}", "").replace(
                    "{number}",
                    CommonUtils().userInfo!!.mobile!!
                )

            message = message.replace("{refcode}", refcode!!)
            //            message= message.replace("{link}","");
            message = message.replace("\\n\\n ", " \n\n")
            message = message.replace("\\n\\n", " \n\n")
            message = message.replace("\\n ", " \n")

            val shareUrl = CommonUtils.authconfig_response(
                context
            )!!.getuRLS()!!.app_share_url

            message = message //+"\n\n"+shareUrl;

            final_message = message

            try {
                val mProgressBar: Dialog? = ProgressBarUtils.initProgressDialog(requireContext())
                val longUrl =
                    shareUrl + AppConstants.SHARE_CONTENT_URL_KEY + CommonUtils().getBase64Encoded(
                        refcode!!
                    )
                CommonUtils.calldeeplink(
                    requireContext(),
                    mProgressBar,
                    "",
                    final_message,
                    longUrl,
                    isWhatsapp!!
                )
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            /* Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, final_message);
            startActivity(Intent.createChooser(share, "Woloo Share"));*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on shareMessageOnWhatsapp*/
    fun shareMessageOnWhatsapp() {
        try {
            Logger.i(TAG, "shareMessageOnWhatsapp")
            var message = ""
            val authConfigResponse = CommonUtils.authconfig_response(
                context
            )
            if (authConfigResponse != null) {
                message = authConfigResponse.getuRLS()!!.app_share_url.toString()
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.setPackage("com.whatsapp")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
            Toast.makeText(
                requireActivity().applicationContext,
                "Please Install Whats App!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /*calling on checkAndRequestPermissions*/
    private fun checkAndRequestPermissions(): Boolean {
        Logger.i(TAG, "checkAndRequestPermissions")
        val permissionReadContact = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS
        )
        val permissionWriteContact = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_CONTACTS
        )

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionReadContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (permissionWriteContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)
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
        Logger.i(TAG, "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    contactsLogs()
                } else {
                    checkAndRequestPermissions()
                }
            }
        }
    }

    private fun contactsLogs() {
        val i = Intent(context, InviteContactsActivity::class.java)
        //i.putExtra("ARRAYLIST", jsonArray.toString());
        i.putExtra(AppConstants.REFCODE, refcode)
        startActivity(i)
    }


    companion object {
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 85

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        var TAG: String = InviteFriendFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(isShowBackbutton: Boolean): InviteFriendFragment {
            val fragment = InviteFriendFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM1, isShowBackbutton)
            fragment.arguments = args
            return fragment
        }
    }
}