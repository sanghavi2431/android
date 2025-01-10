package `in`.woloo.www.more.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.fragments.adapter.InviteFriendsAdapter
import `in`.woloo.www.more.fragments.model.Contacts
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.invite.viewmodel.InviteViewModel
import jagerfield.mobilecontactslibrary.Contact.Contact
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [SubscribeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InviteContactsFragments : Fragment() {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null

    @JvmField
    @BindView(R.id.tvInvite)
    var tvInvite: TextView? = null

    @JvmField
    @BindView(R.id.edit_search)
    var edit_search: EditText? = null

    @JvmField
    @BindView(R.id.recyclerView_invitecontacts)
    var recyclerView_invitecontacts: RecyclerView? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private val mParam2: String? = null

    //    ArrayList<SubscriptionListResponse.Subscription> subscriptionArrayList;
    private val fetchFriendsPresenter: FetchFriendsPresenter? = null
    private var g: Gson? = null
    var commonUtils: CommonUtils? = null
    private var arrayList: ArrayList<Contacts>? = null
    private var arrayList2: ArrayList<Contacts>? = null
    private var adapter: InviteFriendsAdapter? = null
    private var refcode: String? = ""
    private val chars: String? = null
    private var isGiftSub = false
    var inviteViewModel: InviteViewModel? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //mParam1 = getArguments().getString("ARRAYLIST");
            refcode = requireArguments().getString(AppConstants.REFCODE)
            isGiftSub = requireArguments().getBoolean("isGiftSub")
        }
        //Logger.e("mparam1",mParam1);
        Logger.i(TAG, "onCreate")
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_invitecontacts, container, false)
        ButterKnife.bind(this, root)
        commonUtils = CommonUtils()
        inviteViewModel = ViewModelProvider(this).get<InviteViewModel>(
            InviteViewModel::class.java
        )
        tvTitle!!.text = resources.getString(R.string.inv_contacts)
        ivBack!!.setOnClickListener { v: View? ->
            requireActivity().onBackPressed()
        }
        ContactsLogs().execute()
        Logger.i(TAG, "onCreateView")
        setLiveData()
        return root
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        //        subscriptionArrayList=new ArrayList<SubscriptionListResponse.Subscription>();
        try {
            if (isGiftSub) CommonUtils().hideProgress()

            //            inviteFriendsPresenter = new InviteFriendsPresenter(getContext(), InviteContactsFragments.this,subscriptionArrayList,recyclerView_invitecontacts);
//            inviteFriendsPresenter.getSubscriptionList();
            g = Gson()
            //            mobileContactsData = g.fromJson(mParam1, MobileContactsData.class);
//                        String str = g.toJson(s);
            arrayList = g!!.fromJson(mParam1, object : TypeToken<List<Contacts?>?>() {}.type)
            arrayList2 = ArrayList()

            for (i in arrayList!!.indices) {
                if (arrayList!!.get(i).first_name != "" && arrayList!!.get(i).mobile_number != ""
                ) {
                    arrayList2!!.add(arrayList!!.get(i))
                }
            }

            setSearchResults(arrayList2!!)



            tvInvite!!.setOnClickListener { v: View? ->
                if (adapter!!.selectedNumbers!!.size != 0) {
                    if (isGiftSub) {
                        val returnIntent = Intent()
                        returnIntent.putExtra(
                            "mobilenumber", TextUtils.join(
                                ",", validateNumber(
                                    adapter!!.selectedNumbers
                                )
                            )
                        )
                        returnIntent.putExtra(
                            "totalNumbers",
                            adapter!!.selectedNumbers!!.size.toString()
                        )
                        requireActivity().setResult(Activity.RESULT_OK, returnIntent)
                        requireActivity().finish()
                    } else {
//                       Intent i = new Intent(getContext(), EnterMessage.class);
//                       i.putExtra("mobilenumber", TextUtils.join(",", adapter.getSelectedNumbers()));
//                       i.putExtra(AppConstants.REFCODE, refcode);
//                       startActivity(i);

                        inviteViewModel!!.invite(adapter!!.selectedNumbers!!)
                    }
                } else {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Please select contact.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            edit_search!!.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val text = s.toString().lowercase(Locale.getDefault())
                    adapter!!.filter(text)
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun setLiveData() {
        inviteViewModel!!.observeInvite().observe(viewLifecycleOwner,
            Observer<BaseResponse<JSONObject>> { response ->
                if (response.data != null) {
                    try {
                        Logger.i(
                            TAG,
                            "inviteFriendSuccess"
                        )
                        Toast.makeText(
                            requireActivity().applicationContext,
                            "Invitation sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed({ requireActivity().onBackPressed() }, 2000)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            })
    }

    private fun validateNumber(selectedNumbers: ArrayList<String>?): ArrayList<String> {
        for (i in selectedNumbers!!.indices) {
            selectedNumbers[i] = selectedNumbers[i].replace("-", "")
            selectedNumbers[i] = selectedNumbers[i].substring(selectedNumbers[i].length - 10)
        }
        return selectedNumbers
    }

    /*calling on setSearchResults*/
    private fun setSearchResults(arrayList: ArrayList<Contacts>) {
        Logger.i(TAG, "setSearchResults")
        try {
            adapter = InviteFriendsAdapter(requireContext(), arrayList)
            recyclerView_invitecontacts!!.setHasFixedSize(true)
            recyclerView_invitecontacts!!.layoutManager = LinearLayoutManager(context)
            recyclerView_invitecontacts!!.adapter = adapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class ContactsLogs : AsyncTask<Void?, Void?, Void?>() {
        var pdLoading: ProgressDialog = ProgressDialog(activity)

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            commonUtils!!.showProgress(requireActivity())

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading Contacts...");
//            pdLoading.show();
        }

        @Deprecated("Deprecated in Java")
        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: Void?): Void? {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                ImportContactsAsync(activity!!, object : ImportContactsAsync.ICallback {
                    override fun mobileContacts(contactList: ArrayList<Contact>) {
                        var listItem = contactList

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
                            try {
                                mParam1 = jsonArray.toString()
                                initViews()
                                commonUtils!!.hideProgress()
                            } catch (ex: Exception) {
                                CommonUtils.printStackTrace(ex)
                            }
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

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        val CONTACTS_SUMMARY_PROJECTION: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.TIMES_CONTACTED,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
        )

        var TAG: String = InviteContactsFragments::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param isGiftSub
         * @return A new instance of fragment SubscribeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(refcode: String?, isGiftSub: Boolean): InviteContactsFragments {
            val fragment = InviteContactsFragments()
            val args = Bundle()
            //args.putString("ARRAYLIST", param1);
            args.putString(AppConstants.REFCODE, refcode)
            args.putBoolean("isGiftSub", isGiftSub)
            fragment.arguments = args
            return fragment
        }
    }
}