package in.woloo.www.invite_friend.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import in.woloo.www.utils.Logger;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.invite_friend.fragments.model.Contacts;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsPresenter;
import in.woloo.www.invite_friend.fragments.mvp.InviteFriendsView;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ProgressBarUtils;
import in.woloo.www.v2.data.local.SharedPrefSettings;

/**
 * create an instance of this fragment.
 */
public class EnterMessageFragment extends Fragment implements InviteFriendsView {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

//    @BindView(R.id.tv_msgpartener)
//    TextView tv_msgpartener;


    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.edt_message)
    EditText edt_message;

    @BindView(R.id.tv_submitpartner)
    TextView tv_submitpartner;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mobilenumber;
    private String name;
    //    ArrayList<SubscriptionListResponse.Subscription> subscriptionArrayList;
    private InviteFriendsPresenter inviteFriendsPresenter;
    Gson g;
    private ArrayList<Contacts> arrayList;
    private ArrayList<Contacts> arrayList2;
    private String refcode;
    CommonUtils mcoCommonUtils;
    public static String TAG= EnterMessageFragment.class.getSimpleName();
    public EnterMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterMessageFragment newInstance(String mobilenumber,String name,String refcode) {
        EnterMessageFragment fragment = new EnterMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mobilenumber);
        args.putString(ARG_PARAM2, name);
        args.putString(AppConstants.REFCODE, refcode);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        if (getArguments() != null) {
            mobilenumber = getArguments().getString(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            refcode = getArguments().getString(AppConstants.REFCODE);
        }
        Logger.e("mobilenumber",mobilenumber);
        Logger.e("name",name);
        Logger.e("refcode",refcode);
    }
    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_entermessage, container, false);
        ButterKnife.bind(this,root);
        initViews();
        Logger.i(TAG, "onCreateView");
        return root;
    }
    /*calling initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        Logger.i(TAG, "initViews");
        inviteFriendsPresenter = new InviteFriendsPresenter(getContext(),this);
        mcoCommonUtils = new CommonUtils();
        edt_message.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (edt_message.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
        String message = authConfigResponse.getcUSTOMMESSAGE().getInviteFriendText();
        if(new CommonUtils().getUserInfo().getName()!=null)
            message= message.replace("{name}",new CommonUtils().getUserInfo().getName()).replace("{number}","("+new CommonUtils().getUserInfo().getMobile()+")");
        else
            message= message.replace("{name}","").replace("{number}",new CommonUtils().getUserInfo().getMobile());

        message = message.replace("{refcode}", refcode);
        message = message.replace("{link}", "");
        message = message.replace("\\n\\n ", " \n\n");
        message = message.replace("\\n\\n", " \n\n");
        message = message.replace("\\n ", " \n");

        Dialog mProgressBar = ProgressBarUtils.initProgressDialog(getContext());
        mProgressBar.show();
        String shareUrl = CommonUtils.authconfig_response(getContext()).getuRLS().getApp_share_url();
        String longUrl = shareUrl + AppConstants.SHARE_CONTENT_URL_KEY + new CommonUtils().getBase64Encoded(refcode);
        String finalMessage = message;
        final String[] deepLinkFinal = {""};
        CommonUtils.getDeeplink(getContext(), mProgressBar, "", "", longUrl, new DeepLinkCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void getDeepLink(String deepLink) {
                deepLinkFinal[0] = deepLink;
                edt_message.setText(finalMessage + deepLink +" - LOOM & WEAVER RETAIL");
                mProgressBar.dismiss();
            }
        });
//        subscriptionArrayList=new ArrayList<SubscriptionListResponse.Subscription>();
        try {
//            inviteFriendsPresenter = new InviteFriendsPresenter(getContext(), InviteContactsFragments.this,subscriptionArrayList,recyclerView_invitecontacts);
//            inviteFriendsPresenter.getSubscriptionList();
//            mobileContactsData = g.fromJson(mParam1, MobileContactsData.class);
//                        String str = g.toJson(s);

//            setSearchResults();

            tvTitle.setText(getResources().getString(R.string.entermessage));
            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });

            tv_submitpartner.setOnClickListener(v -> {
                try{
                            inviteFriendsPresenter.inviteContacts(mobilenumber,edt_message.getText().toString(),deepLinkFinal[0]);
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }
            });

//            edt_message.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start,
//                                              int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start,
//                                          int before, int count) {
//
//                    int len = s.length();
//                    tv_msgpartener.setText(len + "/150");
//                    if (150 - len < 0) {
//                        tv_msgpartener.setText("limit Exceeded!");
//                        tv_msgpartener.setTextColor(Color.WHITE);
//                    } else
//                        tv_msgpartener.setTextColor(Color.WHITE);
//                }
//            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling inviteFriendSuccess*/
    @Override
    public void inviteFriendSuccess(String msg) {
        try{
            Logger.i(TAG, "inviteFriendSuccess");
            Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling showRefferalCode*/
    @Override
    public void showRefferalCode(String refCode, String expiryDate) {
        Logger.i(TAG, "showRefferalCode");
    }
//    private void setSearchResults() {
//        try{
//            InviteFriendsAdapter adapter = new InviteFriendsAdapter(getContext(),arrayList);
//            recyclerView_invitecontacts.setHasFixedSize(true);
//            recyclerView_invitecontacts.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView_invitecontacts.setAdapter(adapter);
//        }catch (Exception ex){
//             CommonUtils.printStackTrace(ex);
//        }
//    }

    public interface DeepLinkCallback{

        public void getDeepLink(String deepLink);
    }


}