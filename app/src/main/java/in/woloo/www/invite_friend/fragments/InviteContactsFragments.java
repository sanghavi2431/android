package in.woloo.www.invite_friend.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.invite_friend.fragments.adapter.InviteFriendsAdapter;
import in.woloo.www.invite_friend.fragments.model.Contacts;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.invite.viewmodel.InviteViewModel;
import jagerfield.mobilecontactslibrary.Contact.Contact;


import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link in.woloo.www.subscribe.fragments.SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteContactsFragments extends Fragment  {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvInvite)
    TextView tvInvite;

    @BindView(R.id.edit_search)
    EditText edit_search;

    @BindView(R.id.recyclerView_invitecontacts)
    RecyclerView recyclerView_invitecontacts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    ArrayList<SubscriptionListResponse.Subscription> subscriptionArrayList;
    private FetchFriendsPresenter fetchFriendsPresenter;
    private Gson g;
    CommonUtils commonUtils;
    private ArrayList<Contacts> arrayList;
    private ArrayList<Contacts> arrayList2;
    private InviteFriendsAdapter adapter;
    private String refcode="";
    private String chars;
    private boolean isGiftSub=false;
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.TIMES_CONTACTED,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
    };

    InviteViewModel inviteViewModel;

    public InviteContactsFragments() {
        // Required empty public constructor
    }

    public static String TAG= InviteContactsFragments.class.getSimpleName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isGiftSub
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InviteContactsFragments newInstance(String refcode, boolean isGiftSub) {
        InviteContactsFragments fragment = new InviteContactsFragments();
        Bundle args = new Bundle();
        //args.putString("ARRAYLIST", param1);
        args.putString(AppConstants.REFCODE, refcode);
        args.putBoolean("isGiftSub", isGiftSub);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString("ARRAYLIST");
            refcode = getArguments().getString(AppConstants.REFCODE);
            isGiftSub = getArguments().getBoolean("isGiftSub");
        }
        //Logger.e("mparam1",mParam1);
        Logger.i(TAG, "onCreate");
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_invitecontacts, container, false);
        ButterKnife.bind(this,root);
        commonUtils = new CommonUtils();
        inviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);
        tvTitle.setText(getResources().getString(R.string.inv_contacts));
        ivBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        new ContactsLogs().execute();
        Logger.i(TAG, "onCreateView");
        setLiveData();
        return root;
    }

    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
//        subscriptionArrayList=new ArrayList<SubscriptionListResponse.Subscription>();
        try{
            if (isGiftSub)
                new CommonUtils().hideProgress();

//            inviteFriendsPresenter = new InviteFriendsPresenter(getContext(), InviteContactsFragments.this,subscriptionArrayList,recyclerView_invitecontacts);
//            inviteFriendsPresenter.getSubscriptionList();
            g = new Gson();
//            mobileContactsData = g.fromJson(mParam1, MobileContactsData.class);
//                        String str = g.toJson(s);
            arrayList=g.fromJson(mParam1, new TypeToken<List<Contacts>>()
            {}.getType());
            arrayList2=new ArrayList<Contacts>();

            for (int i=0;i<arrayList.size();i++) {
                if (!arrayList.get(i).getFirst_name().equals("") && !arrayList.get(i).getMobile_number().equals("")) {
                    arrayList2.add(arrayList.get(i));
                }
            }

            setSearchResults(arrayList2);



            tvInvite.setOnClickListener(v -> {
                if(adapter.getSelectedNumbers().size() != 0){
                   if(isGiftSub)
                   {
                       Intent returnIntent = new Intent();
                       returnIntent.putExtra("mobilenumber",TextUtils.join(",",  validateNumber(adapter.getSelectedNumbers())));
                       returnIntent.putExtra("totalNumbers",String.valueOf(adapter.getSelectedNumbers().size()));
                       getActivity().setResult(Activity.RESULT_OK,returnIntent);
                       getActivity().finish();
                   }
                   else {
//                       Intent i = new Intent(getContext(), EnterMessage.class);
//                       i.putExtra("mobilenumber", TextUtils.join(",", adapter.getSelectedNumbers()));
//                       i.putExtra(AppConstants.REFCODE, refcode);
//                       startActivity(i);

                       inviteViewModel.invite(adapter.getSelectedNumbers());
                   }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please select contact.",Toast.LENGTH_SHORT).show();
                }
            });
            edit_search.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String text = s.toString().toLowerCase();
                    adapter.filter(text);

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {


                }

                public void afterTextChanged(Editable s) {
                }
            });
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        inviteViewModel.observeInvite().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response!=null && response.getData()!=null){
                    try{
                        Logger.i(TAG, "inviteFriendSuccess");
                        Toast.makeText(getActivity().getApplicationContext(),"Invitation sent successfully",Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> getActivity().onBackPressed(), 2000);
                    }catch (Exception ex){
                        CommonUtils.printStackTrace(ex);
                    }
                }
            }
        });
    }

    private ArrayList<String> validateNumber(ArrayList<String> selectedNumbers) {
        for(int i=0;i<selectedNumbers.size();i++)
        {
            selectedNumbers.set(i,selectedNumbers.get(i).replace("-",""));
            selectedNumbers.set(i,selectedNumbers.get(i).substring(selectedNumbers.get(i).length() - 10));
        }
        return selectedNumbers;
    }

    /*calling on setSearchResults*/
    private void setSearchResults(ArrayList<Contacts> arrayList) {
        Logger.i(TAG,"setSearchResults");
        try{
            adapter = new InviteFriendsAdapter(getContext(), arrayList);
            recyclerView_invitecontacts.setHasFixedSize(true);
            recyclerView_invitecontacts.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_invitecontacts.setAdapter(adapter);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private class ContactsLogs extends AsyncTask<Void, Void, Void> {

        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            commonUtils.showProgress(getActivity());

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading Contacts...");
//            pdLoading.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                new ImportContactsAsync(getActivity(), new ImportContactsAsync.ICallback() {
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
                            try{
                                mParam1 = jsonArray.toString();
                                initViews();
                                commonUtils.hideProgress();
                            }catch (Exception ex){
                                 CommonUtils.printStackTrace(ex);
                            }
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

}