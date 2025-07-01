package in.woloo.www.invite_friend.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import in.woloo.www.app.WolooApplication;
import in.woloo.www.utils.Logger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.invite_friend.fragments.contacts.InviteContactsActivity;
import in.woloo.www.invite_friend.fragments.model.Contacts;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsPresenter;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsView;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ProgressBarUtils;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.webview.WebViewFragment;
import jagerfield.mobilecontactslibrary.Contact.Contact;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFriendFragment extends Fragment{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 85;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.termsAndConditionTv)
    TextView termsAndConditionTv;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tv_contacts)
    TextView tv_contacts;

    @BindView(R.id.ivShare)
    ImageView ivShare;

    @BindView(R.id.ivWhatsApp)
    ImageView ivWhatsApp;

    @BindView(R.id.tvRefferalCode)
    TextView tvRefferalCode;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvReferalMsg)
    TextView tvReferalMsg;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean isShowBackButton;
    private boolean mParam2;
    ArrayList<Contacts> personNames;
    ProfileViewModel profileViewModel;
    String refcode = "";
    private String final_message="";
    public static String TAG=InviteFriendFragment.class.getSimpleName();
    public InviteFriendFragment() {
        // Required empty public constructor
    }

    public static InviteFriendFragment newInstance(boolean isShowBackbutton) {
        InviteFriendFragment fragment = new InviteFriendFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isShowBackbutton);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isShowBackButton = getArguments().getBoolean(ARG_PARAM1);
        }
        Logger.i(TAG, "onCreate");
    }
    /*calling on onResume*/
    @Override
    public void onResume() {
        Logger.i(TAG, "onResume");
        super.onResume();
        ((WolooDashboard) getActivity()).hideToolbar();
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        ButterKnife.bind(this, rootView);
        profileViewModel =  new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getUserProfile();
        initView();
        setLiveData();
        if (isShowBackButton) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.GONE);
        }
        termsAndConditionTv.setOnClickListener(v -> {
            Logger.i(TAG, "navigateToTermsOfUseScreen");
            try {
                ((WolooDashboard) getActivity()).hideToolbar();
                String aboutURL = CommonUtils.getTermsUrl(getContext());
                ((WolooDashboard) getActivity()).loadMenuFragment(WebViewFragment.newInstance("Terms of use", aboutURL , InviteFriendFragment.TAG), "TermsOfUseFragment");
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        });
        return rootView;
    }
    /*calling on initView*/
    private void initView() {
        Logger.i(TAG, "initView");
        try {
            tvTitle.setText(getText(R.string.invite_friend));

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                if (authConfigResponse != null && authConfigResponse.getcUSTOMMESSAGE() != null && !TextUtils.isEmpty(authConfigResponse.getcUSTOMMESSAGE().getReferralRewardMessage())) {
                    tvReferalMsg.setText(authConfigResponse.getcUSTOMMESSAGE().getReferralRewardMessage());
                }
            }


            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });

//            ivFacebook.setOnClickListener(v -> {
//                sharemessage_onfb();
//            });

//            ivWhatsApp.setOnClickListener(v -> {
//                shareMessageOnWhatsapp();
//            });

            PackageManager pm = getActivity().getPackageManager();
            if (CommonUtils.isPackageInstalled("com.whatsapp",pm)) {
                ivWhatsApp.setVisibility(View.VISIBLE);
            } else {
                ivWhatsApp.setVisibility(View.GONE);
            }

            ivShare.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Utility.logFirebaseEvent(getContext(),bundle,AppConstants.SHARE_CLICK);

                HashMap<String,Object> payload = new HashMap<>();
                Utility.logNetcoreEvent(getContext(),payload,AppConstants.SHARE_CLICK);

                shareMessage(false);
            });

            ivWhatsApp.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Utility.logFirebaseEvent(getContext(),bundle,AppConstants.SHARE_CLICK);

                HashMap<String,Object> payload = new HashMap<>();
                Utility.logNetcoreEvent(getContext(),payload,AppConstants.SHARE_CLICK);

                shareMessage(true);
            });


        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        personNames = new ArrayList<Contacts>();
        tv_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle = new Bundle();
                    Utility.logFirebaseEvent(getContext(),bundle,AppConstants.INVITE_CONTACT_CLICK);

                    HashMap<String,Object> payload = new HashMap<>();
                    Utility.logNetcoreEvent(getContext(),payload,AppConstants.INVITE_CONTACT_CLICK);

                    if (checkAndRequestPermissions()) {
                        //new ContactsLogs().execute();
                        contactsLogs();
                    }
                } catch (Exception e) {
                      CommonUtils.printStackTrace(e);
                }

            }
        });

    }

    private  void setLiveData(){
        profileViewModel.observeUserProfile().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserProfile>>() {
            @Override
            public void onChanged(BaseResponse<UserProfile> userProfileResponse) {
                if (userProfileResponse != null && userProfileResponse.getData() != null) {
                    refcode = userProfileResponse.getData().getProfile().getRefCode();
                    try {
                        tvRefferalCode.setText(refcode);
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                }else{
                    Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    /*calling on sharemessage_onfb*/
    private void sharemessage_onfb() {
        Logger.i(TAG, "sharemessage_onfb");
        try {
            String message = "";
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                message = authConfigResponse.getuRLS().getApp_share_url();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            share.setPackage("com.facebook.katana"); //Facebook App package
            startActivity(Intent.createChooser(share, "Woloo Share"));
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }
    /*calling on ContactsLogs*/
    private class ContactsLogs extends AsyncTask<Void, Void, Void> {

        ProgressDialog pdLoading = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading Contacts...");
//            pdLoading.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Logger.i(TAG, "ContactsLogs");
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                new ImportContactsAsync((Activity) getContext(), new ImportContactsAsync.ICallback() {
                    @Override
                    public void mobileContacts(ArrayList<Contact> contactList) {
                        ArrayList<Contact> listItem = contactList;

                        if (listItem == null) {
                            listItem = new ArrayList<Contact>();
                            Logger.i("C.TAG_LIB", "Error in retrieving contacts");
                        }

                        if (listItem.isEmpty()) {
//                            Toast.makeText(DashboardActivity.this, "No contacts found", Toast.LENGTH_LONG).show();
                        }
                        try {
                            JSONObject obj;
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < listItem.size(); i++) {
                                obj = new JSONObject();
                                //                    Logger.e("listitems",listItem.get(i).getFirstName()+" "+listItem.get(i).getLastName()+","+listItem.get(i).getNumbers().get(0).elementValue());
                                obj.put("first_name", listItem.get(i).getFirstName());
                                obj.put("last_name", listItem.get(i).getLastName());
                                try {
                                    obj.put("mobile_number", listItem.get(i).getNumbers().get(0).elementValue().replaceAll("\\s+", ""));
                                } catch (Exception e) {
                                    obj.put("mobile_number", "");
                                }

                                try {
                                    obj.put("type", listItem.get(i).getNumbers().get(0).getNumType());
                                } catch (Exception e) {
                                    obj.put("type", "");
                                }


//                                obj.put("addresses", listItem.get(i).getAddresses());
//                                obj.put("displaydname", listItem.get(i).getDisplaydName());
//                                obj.put("id", listItem.get(i).getId());
//                                obj.put("emails", listItem.get(i).getEmails());
//                                obj.put("events", listItem.get(i).getEvents());
//                                obj.put("nicknames", listItem.get(i).getNickNames());
                                jsonArray.put(obj);
                            }
                            Logger.e("contacts", jsonArray.toString());



                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                              CommonUtils.printStackTrace(e);
                        }

                    }
                }).execute();
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread


//            pdLoading.dismiss();
        }

    }

    /*calling on shareMessage*/
    public void shareMessage(Boolean isWhatsapp) {
        Logger.i(TAG, "shareMessage");
        try {
//            String message = CommonUtils.authconfig_response(getContext()).getData().getuRLS().getApp_share_url();
//            AuthConfigResponse authConfigResponse = CommonUtils.authconfig_response(getContext());
//            if (authConfigResponse != null) {
//                message = authConfigResponse.getData().getuRLS().getApp_share_url();
//            }

            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            String message = authConfigResponse.getcUSTOMMESSAGE().getInviteFriendText();
            if(new CommonUtils().getUserInfo().getName()!=null)
                message= message.replace("{name}",new CommonUtils().getUserInfo().getName()).replace("{number}","("+new CommonUtils().getUserInfo().getMobile()+")");
            else
                message= message.replace("{name}","").replace("{number}",new CommonUtils().getUserInfo().getMobile());

            message= message.replace("{refcode}",refcode);
//            message= message.replace("{link}","");
            message = message.replace("\\n\\n "," \n\n");
            message = message.replace("\\n\\n"," \n\n");
            message = message.replace("\\n "," \n");

            String shareUrl = CommonUtils.authconfig_response(getContext()).getuRLS().getApp_share_url();

            message = message;//+"\n\n"+shareUrl;

            final_message = message;

            try{
                Dialog mProgressBar;
                mProgressBar = ProgressBarUtils.initProgressDialog(getContext());
                String longUrl = shareUrl+AppConstants.SHARE_CONTENT_URL_KEY+new CommonUtils().getBase64Encoded(refcode);
                CommonUtils.calldeeplink(getContext(),mProgressBar,"",final_message,longUrl,isWhatsapp);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }

           /* Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, final_message);
            startActivity(Intent.createChooser(share, "Woloo Share"));*/
        }
        catch (Exception ex)
        {
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling on shareMessageOnWhatsapp*/
    public void shareMessageOnWhatsapp() {
        try {
            Logger.i(TAG, "shareMessageOnWhatsapp");
            String message = "";
            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                message = authConfigResponse.getuRLS().getApp_share_url();
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(intent);
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
            Toast.makeText(getActivity().getApplicationContext(), "Please Install Whats App!", Toast.LENGTH_SHORT).show();
        }
    }




    /*calling on checkAndRequestPermissions*/
    private boolean checkAndRequestPermissions() {
        Logger.i(TAG, "checkAndRequestPermissions");
        int permissionReadContact = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        int permissionWriteContact = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionWriteContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
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
        Logger.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    contactsLogs();
                } else {
                    checkAndRequestPermissions();
                }
            }
        }
    }

    private void contactsLogs() {
        Intent i = new Intent(getContext(), InviteContactsActivity.class);
        //i.putExtra("ARRAYLIST", jsonArray.toString());
        i.putExtra(AppConstants.REFCODE, refcode);
        startActivity(i);
    }


}