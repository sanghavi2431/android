package `in`.woloo.www.more.giftcard

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.base_old.BaseFragment
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsResponse
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.more.giftcard.giftcard.viewmodel.CoinsViewModel
import `in`.woloo.www.more.giftcard.mvp.GiftCardPresenter
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Logger.w
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 * Use the [GiftCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftCardFragment : BaseFragment(), Serializable {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.tvHundred)
    var tvHundred: TextView? = null

    @JvmField
    @BindView(R.id.tvFiveHundred)
    var tvFiveHundred: TextView? = null

    @JvmField
    @BindView(R.id.tvThousand)
    var tvThousand: TextView? = null

    @JvmField
    @BindView(R.id.etAmount)
    var etAmount: EditText? = null

    @JvmField
    @BindView(R.id.etMobileNumber)
    var etMobileNumber: EditText? = null

    @JvmField
    @BindView(R.id.etMessage)
    var etMessage: EditText? = null

    @JvmField
    @BindView(R.id.tvSubmit)
    var llsendButton: TextView? = null

    @JvmField
    @BindView(R.id.giftAmountLayout)
    var giftAmountLayout: LinearLayout? = null

    private var commonUtils: CommonUtils? = null

    private var lastSelectedAmount: TextView? = null
    var Email: String? = null
    var isEmail: Boolean = false
    var mobile: String? = null

    // TODO: Rename and change types of parameters
    private val giftCardPresenter: GiftCardPresenter? = null
    private var coinsViewModel: CoinsViewModel? = null
    private var userCoinsResponse: UserCoins? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            Email = requireArguments().getString(ARG_PARAM1)
            mobile = requireArguments().getString(ARG_PARAM2)
            isEmail = requireArguments().getBoolean(ARG_PARAM3)
        }
        i(TAG, "onCreate")
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_gift_card, container, false)
        ButterKnife.bind(this, root)
        i(TAG, "onCreateView")
        initViews()
        return root
    }

    /*calling initViews*/
    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        try {
            i(TAG, "initViews")
            coinsViewModel = ViewModelProvider(this).get<CoinsViewModel>(
                CoinsViewModel::class.java
            )
            coinsViewModel!!.getUserCoins()
            setLiveData()
            setNetworkDetector()
            commonUtils = CommonUtils()
            ivBack!!.setOnClickListener { v: View? ->
                // getActivity().onBackPressed();
                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            }

            etMessage!!.setOnTouchListener(View.OnTouchListener { v, event ->
                if (etMessage!!.hasFocus()) {
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

            etMobileNumber!!.setOnTouchListener(View.OnTouchListener { v, event ->
                if (etMessage!!.hasFocus()) {
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

            tvHundred!!.setOnClickListener { v: View? ->
                w(TAG, "selected 100")
                selectTheAmount(100)
            }

            tvFiveHundred!!.setOnClickListener { v: View? ->
                selectTheAmount(500)
            }

            tvThousand!!.setOnClickListener { v: View? ->
                selectTheAmount(1000)
            }

            llsendButton!!.setOnClickListener { v: View? ->
                /*if(isValidate()){
                    giftCardPresenter.sendGiftCard(etAmount.getText().toString().trim(),etMobileNumber.getText().toString().trim(),etMessage.getText().toString().trim());
                }*/
                if (isValidate) {
                    val bundle = Bundle()
                    bundle.putString(
                        AppConstants.GIFT_CARD_AMOUNT,
                        etAmount!!.text.toString().trim { it <= ' ' })
                    logFirebaseEvent(
                        activity,
                        bundle,
                        AppConstants.GIFT_AMOUNT_SELECTED
                    )

                    val payload = HashMap<String, Any>()
                    payload[AppConstants.GIFT_CARD_AMOUNT] =
                        etAmount!!.text.toString().trim { it <= ' ' }
                    logNetcoreEvent(
                        requireActivity(),
                        payload,
                        AppConstants.GIFT_AMOUNT_SELECTED
                    )

                    //                     giftCardPresenter.RequestPoints(etAmount.getText().toString().trim(), etMobileNumber.getText().toString().trim(), etMessage.getText().toString().trim());
                    val request: AddCoinsRequest = AddCoinsRequest()
                    try {
                        request.coins = etAmount!!.text.toString().trim { it <= ' ' }.toInt()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Please enter valid amount",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    request.mobile = etMobileNumber!!.text.toString().trim { it <= ' ' }
                    request.message = etMessage!!.text.toString().trim { it <= ' ' }
                    coinsViewModel!!.addCoins(request)
                    //etAmount.getText().toString().trim(), etMobileNumber.getText().toString().trim(), etMessage.getText().toString().trim());
                }
            }

            etAmount!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (lastSelectedAmount != null) {
                        lastSelectedAmount!!.background = ContextCompat.getDrawable(
                            context!!, R.drawable.yello_rectangle_shape
                        )
                    }
                    when (etAmount!!.text.toString()) {
                        "100" -> {
                            tvHundred!!.background = ContextCompat.getDrawable(
                                context!!, R.drawable.new_button_onclick_background
                            )
                            lastSelectedAmount = tvHundred
                        }

                        "500" -> {
                            tvFiveHundred!!.background = ContextCompat.getDrawable(
                                context!!, R.drawable.new_button_onclick_background
                            )
                            lastSelectedAmount = tvFiveHundred
                        }

                        "1000" -> {
                            tvThousand!!.background = ContextCompat.getDrawable(
                                context!!, R.drawable.new_button_onclick_background
                            )
                            lastSelectedAmount = tvThousand
                        }
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun setLiveData() {
        coinsViewModel!!.observeAddCoins().observe(viewLifecycleOwner,
            Observer<BaseResponse<AddCoinsResponse>> { addCoinsResponseBaseResponse ->
                if (addCoinsResponseBaseResponse.data != null) {
                    val orderId: String = addCoinsResponseBaseResponse.data!!.orderId
                    if (!TextUtils.isEmpty(orderId)) {
                        //                    CommonUtils.showCustomDialog(getActivity(),"We got some response\n\n"+orderId);
                        CommonUtils.navigateToRazorPayFlow(
                            requireContext(),
                            "",
                            orderId,
                            Email,
                            isEmail,
                            mobile,
                            false,
                            null,
                            false,
                            false
                        )
                        /*selectTheAmount(0);
                            etAmount.setText("");
                            etMobileNumber.setText("");
                            etMessage.setText("");*/
                    } else {
                        //                        CommonUtils.showCustomDialog(getActivity(),);
                        CommonUtils.showCustomDialog(requireActivity(), "Some error occured")
                    }
                } else {
                    //                        displayToast(WolooApplication.getErrorMessage());
                    CommonUtils.showCustomDialog(requireActivity(), WolooApplication.errorMessage)
                    WolooApplication.errorMessage = ""
                }
            })

        coinsViewModel!!.observeUserCoins().observe(
            viewLifecycleOwner,
            Observer<BaseResponse<UserCoins>> { userCoinsResponse: BaseResponse<UserCoins>? ->
                if (userCoinsResponse?.data != null) {
                    this.userCoinsResponse = userCoinsResponse.data
                } else {
                    //                        displayToast(WolooApplication.getErrorMessage());
                    CommonUtils.showCustomDialog(requireActivity(), WolooApplication.errorMessage)
                    WolooApplication.errorMessage = ""
                }
            })
    }

    private val isValidate: Boolean
        /*calling isValidate*/
        get() {
            try {
                i(TAG, "isValidate")
                val mobileNumber =
                    etMobileNumber!!.text.toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(
                        etAmount!!.text.toString().trim { it <= ' ' })
                ) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Please enter points",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Please enter mobile number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (TextUtils.isEmpty(
                        etMessage!!.text.toString().trim { it <= ' ' })
                ) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Please enter message",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                if (!CommonUtils.isValidMobileNumber(mobileNumber)) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Please enter valid mobile number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                //LoginResponse userInfo = new CommonUtils().getUserInfo(getActivity());
                val userInfo =
                    CommonUtils().userInfo
                if (mobileNumber == userInfo!!.mobile) {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "You can not send Gift to YourSelf",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                /*if(userCoinsResponse != null && userCoinsResponse.getData() != null && userCoinsResponse.getData().getTotalCoins() != null && userCoinsResponse.getData().getTotalCoins() < Integer.parseInt(etAmount.getText().toString().trim())){
             //Toast.makeText(getActivity().getApplicationContext(),"You don't have enough points",Toast.LENGTH_SHORT).show();
             return false;
         }
*/
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            return true
        }

    /*calling selectTheAmount*/
    private fun selectTheAmount(rupees: Int) {
        try {
            i(TAG, "selectTheAmount")
            if (lastSelectedAmount != null) {
                lastSelectedAmount!!.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.yello_rectangle_shape
                )
            }
            if (rupees == 100) {
                etAmount!!.setText("100")
                tvHundred!!.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.new_button_onclick_background
                )
                lastSelectedAmount = tvHundred
            } else if (rupees == 500) {
                etAmount!!.setText("500")
                tvFiveHundred!!.background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.new_button_onclick_background
                )
                lastSelectedAmount = tvFiveHundred
            } else if (rupees == 1000) {
                etAmount!!.setText("1000")
                tvThousand!!.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.new_button_onclick_background
                )
                lastSelectedAmount = tvThousand
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    override fun onResume() {
        super.onResume()
        selectTheAmount(0)
        etAmount!!.setText("")
        etMobileNumber!!.setText("")
        etMessage!!.setText("")
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"

        var TAG: String = GiftCardFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param Email Parameter 1.
         * @param isEmail
         * @param mobile Parameter 2.
         * @return A new instance of fragment GiftCardFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(Email: String?, isEmail: Boolean, mobile: String?): GiftCardFragment {
            val fragment = GiftCardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, Email)
            args.putBoolean(ARG_PARAM3, isEmail)
            args.putString(ARG_PARAM2, mobile)
            fragment.arguments = args
            return fragment
        }
    }
}
