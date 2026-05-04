package `in`.woloo.www.more.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.fragments.model.Contacts
import `in`.woloo.www.more.fragments.mvp.InviteFriendsPresenter
import `in`.woloo.www.more.fragments.mvp.InviteFriendsView
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ProgressBarUtils

/**
 * create an instance of this fragment.
 */
class EnterMessageFragment : Fragment(), InviteFriendsView {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null


    //    @BindView(R.id.tv_msgpartener)
    //    TextView tv_msgpartener;
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null

    @JvmField
    @BindView(R.id.edt_message)
    var edt_message: EditText? = null

    @JvmField
    @BindView(R.id.tv_submitpartner)
    var tv_submitpartner: TextView? = null


    // TODO: Rename and change types of parameters
    private var mobilenumber: String? = null
    private var name: String? = null

    //    ArrayList<SubscriptionListResponse.Subscription> subscriptionArrayList;
    private var inviteFriendsPresenter: InviteFriendsPresenter? = null
    var g: Gson? = null
    private val arrayList: ArrayList<Contacts>? = null
    private val arrayList2: ArrayList<Contacts>? = null
    private var refcode: String? = null
    var mcoCommonUtils: CommonUtils? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i(TAG, "onCreate")
        if (arguments != null) {
            mobilenumber = requireArguments().getString(ARG_PARAM1)
            name = requireArguments().getString(ARG_PARAM2)
            refcode = requireArguments().getString(AppConstants.REFCODE)
        }
        Logger.e("mobilenumber", mobilenumber.toString())
        Logger.e("name", name.toString())
        Logger.e("refcode", refcode.toString())
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_entermessage, container, false)
        ButterKnife.bind(this, root)
        initViews()
        Logger.i(TAG, "onCreateView")
        return root
    }

    /*calling initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        Logger.i(TAG, "initViews")
        inviteFriendsPresenter = InviteFriendsPresenter(
            requireContext(),
            this
        )
        mcoCommonUtils = CommonUtils()
        edt_message!!.setOnTouchListener(View.OnTouchListener { v, event ->
            if (edt_message!!.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })
        val authConfigResponse = getPreferences.fetchAuthConfig()
        var message = authConfigResponse!!.getcUSTOMMESSAGE()!!.inviteFriendText
        message = if (CommonUtils().userInfo!!.name != null) message!!.replace(
            "{name}",
            CommonUtils().userInfo!!.name!!
        ).replace("{number}", "(" + CommonUtils().userInfo!!.mobile + ")")
        else message!!.replace("{name}", "").replace(
            "{number}",
            CommonUtils().userInfo!!.mobile!!
        )

        message = message.replace("{refcode}", refcode!!)
        message = message.replace("{link}", "")
        message = message.replace("\\n\\n ", " \n\n")
        message = message.replace("\\n\\n", " \n\n")
        message = message.replace("\\n ", " \n")

        val mProgressBar: Dialog? = ProgressBarUtils.initProgressDialog(requireContext())
        mProgressBar!!.show()
        val shareUrl = CommonUtils.authconfig_response(context)!!.getuRLS()!!.app_share_url
        val longUrl =
            shareUrl + AppConstants.SHARE_CONTENT_URL_KEY + CommonUtils().getBase64Encoded(refcode!!)
        val finalMessage: String = message
        val deepLinkFinal = arrayOf("")
        CommonUtils.getDeeplink(requireContext(), mProgressBar, "", "", longUrl, object : DeepLinkCallback {
            @SuppressLint("SetTextI18n")
            override fun getDeepLink(deepLink: String) {
                deepLinkFinal[0] = deepLink
                edt_message!!.setText("$finalMessage$deepLink - LOOM & WEAVER RETAIL")
                mProgressBar.dismiss()
            }
        })
        //        subscriptionArrayList=new ArrayList<SubscriptionListResponse.Subscription>();
        try {
//            inviteFriendsPresenter = new InviteFriendsPresenter(getContext(), InviteContactsFragments.this,subscriptionArrayList,recyclerView_invitecontacts);
//            inviteFriendsPresenter.getSubscriptionList();
//            mobileContactsData = g.fromJson(mParam1, MobileContactsData.class);
//                        String str = g.toJson(s);

//            setSearchResults();

            tvTitle!!.text = resources.getString(R.string.entermessage)
            ivBack!!.setOnClickListener { v: View? ->
                requireActivity().onBackPressed()
            }

            tv_submitpartner!!.setOnClickListener { v: View? ->
                try {
                    inviteFriendsPresenter!!.inviteContacts(
                        mobilenumber, edt_message!!.text.toString(),
                        deepLinkFinal[0]
                    )
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }

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
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling inviteFriendSuccess*/
    override fun inviteFriendSuccess(msg: String?) {
        try {
            Logger.i(TAG, "inviteFriendSuccess")
            Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling showRefferalCode*/
    override fun showRefferalCode(refCode: String?, expiryDate: String?) {
        Logger.i(TAG, "showRefferalCode")
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
    interface DeepLinkCallback {
        fun getDeepLink(deepLink: String)
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        var TAG: String = EnterMessageFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SubscribeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            mobilenumber: String?,
            name: String?,
            refcode: String?
        ): EnterMessageFragment {
            val fragment = EnterMessageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, mobilenumber)
            args.putString(ARG_PARAM2, name)
            args.putString(AppConstants.REFCODE, refcode)
            fragment.arguments = args
            return fragment
        }
    }
}