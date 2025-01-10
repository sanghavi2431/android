package `in`.woloo.www.shopping.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.model.UserGiftCardUpdateResponse
import `in`.woloo.www.shopping.mvp.UserGiftCardPresenter
import `in`.woloo.www.shopping.mvp.UserGiftCardUpdateView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SuccessFragment : Fragment(), NetworkAPIResponseCallback,
    UserGiftCardUpdateView {
    @JvmField
    @BindView(R.id.go_to_shop)
    var go_to_shop: TextView? = null


    private val saveOrderUrl = Config.hostname + "save_order_api.php"


    // TODO: Rename and change types of parameters
    var amount: String? = null
    var address: String? = null
    private var total_point_used: Int = 0
    private var gift_card_used_value: String? = null
    var orderid: String = ""

    private var userGiftCardPresenter: UserGiftCardPresenter? = null
    private var blogViewModel: BlogViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            amount = requireArguments().getString("amount")
            address = requireArguments().getString("address")
            gift_card_used_value = requireArguments().getString("gift_card_used_value")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_success, container, false)
        ButterKnife.bind(this, root)

        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(
            BlogViewModel::class.java
        )
        userGiftCardPresenter = UserGiftCardPresenter(requireActivity(), this@SuccessFragment)

        initViews()
        setLiveData()
        return root
    }


    private fun initViews() {
        try {
            go_to_shop!!.setOnClickListener {
                val fragmentManager: FragmentManager =
                    (context as WolooDashboard).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frm_contant, ShoppingFragment(), "")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }


            // Get All order  and product order
            val jsonArrayorder = JSONArray()
            val jsonArrayorderproduct = JSONArray()


            // Putting in json
            try {
                val JSONrow = JSONObject()
                JSONrow.put("user_id", ShoppingFragment.user_id)
                JSONrow.put("name", ShoppingFragment.user_name.toString())
                JSONrow.put("total_amount", amount.toString())
                JSONrow.put("address", address.toString())
                JSONrow.put("email", ShoppingFragment.user_email.toString())
                JSONrow.put("mobile", ShoppingFragment.user_phone.toString())
                JSONrow.put(
                    "shipping_charges",
                    CartFragment.total_shipping_charges_current.toString()
                )
                JSONrow.put("user_type", ShoppingFragment.user_type)
                JSONrow.put("gift_card_used_value", gift_card_used_value)
                JSONrow.put("coupon_code", ShoppingFragment.coupon_code)
                JSONrow.put("coupon_discount", ShoppingFragment.totalCouponDiscount)



                jsonArrayorder.put(JSONrow)
            } catch (e: Exception) {
            }




            for (i in ShoppingFragment.all_cart_list.indices) {
                val pro_id = ShoppingFragment.all_cart_list[i][0]
                val pro_name = ShoppingFragment.all_cart_list[i][1]
                val pro_image = ShoppingFragment.all_cart_list[i][2]
                val pro_price = ShoppingFragment.all_cart_list[i][3]
                val qty = ShoppingFragment.all_cart_list[i][4]
                val customer_margin_per = ShoppingFragment.all_cart_list[i][5]
                val point_used = ShoppingFragment.all_cart_list[i][6]
                val total_amount = ShoppingFragment.all_cart_list[i][7]


                total_point_used = total_point_used + point_used.toInt()


                // Putting in json
                try {
                    val JSONrow = JSONObject()
                    JSONrow.put("pro_id", pro_id)
                    JSONrow.put("pro_name", pro_name)
                    JSONrow.put("qty", qty)
                    JSONrow.put("price", pro_price)
                    JSONrow.put("customer_margin_per", customer_margin_per)
                    JSONrow.put("point_used", point_used)
                    JSONrow.put(amount!!, total_amount)

                    jsonArrayorderproduct.put(JSONrow)
                } catch (_: Exception) {
                }
            }

            val EverythingJSON = JSONObject()

            EverythingJSON.put("order", jsonArrayorder)
            EverythingJSON.put("order_product", jsonArrayorderproduct)

            saveOrder(EverythingJSON)
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    fun setLiveData() {
        blogViewModel!!.observeEcomCoinUpdate().observe(viewLifecycleOwner,
            Observer<BaseResponse<EcomCoinUpdateResponse>> { response ->
                if (!response.success) {

                }
            })
    }

    private fun saveOrder(postData: JSONObject) {
        val postUrl = saveOrderUrl
        val requestQueue = Volley.newRequestQueue(context)


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, postUrl, postData,
            { response ->
                //System.out.println(response);
                try {
                    orderid = response.getString("orderid")
                } catch (e: JSONException) {
                    printStackTrace(e)
                }

                try {
                    if (total_point_used > 0) {
                        userGiftCardPresenter!!.updateGiftCard("points", total_point_used, orderid)
                        blogViewModel!!.ecomCoinUpdate("points", total_point_used, orderid)
                    }


                    if (gift_card_used_value!!.toInt() > 0) {
                        userGiftCardPresenter!!.updateGiftCard(
                            "gift",
                            gift_card_used_value!!.toInt(),
                            orderid
                        )
                        blogViewModel!!.ecomCoinUpdate(
                            "gift",
                            gift_card_used_value!!.toInt(),
                            orderid
                        )
                    }
                } catch (_: Exception) {
                }


                // CAll API HERE For debit coins

                /*  NetworkAPICall mNetworkAPICall = new NetworkAPICall();
                        JetEncryptor mJetEncryptor = JetEncryptor.getInstance();
        
                        try {
                            JSONObject mJsObjParam = new JSONObject();
                            mJsObjParam.put("type", "points");
                            mJsObjParam.put("coins", total_point_used);
        
        
                            Type parserType = new TypeToken<ViewProfileResponse>() {
                            }.getType();
        
                            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel("api/v1/ecomCoinUpdate", AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
                             mNetworkAPICall.callApplicationWS((Activity) getContext(), networkAPICallModel, SuccessFragment.this);
                        } catch (Exception e) {
        
                           // Log.e(TAG, e.getMessage());
                        }
        
        
        
                         // API CAll here for debit gift card
        
                        NetworkAPICall mNetworkAPICall1 = new NetworkAPICall();
                        JetEncryptor mJetEncryptor1 = JetEncryptor.getInstance();
        
                        try {
                            JSONObject mJsObjParam1 = new JSONObject();
                            mJsObjParam1.put("type", "gift");
                            mJsObjParam1.put("coins", Integer.valueOf(gift_card_used_value));
        
        
                            Type parserType = new TypeToken<ViewProfileResponse>() {
                            }.getType();
        
                            NetworkAPICallModel networkAPICallModel1 = new NetworkAPICallModel("api/v1/ecomCoinUpdate", AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam1, mJetEncryptor1);
                            mNetworkAPICall.callApplicationWS((Activity) getContext(), networkAPICallModel1, SuccessFragment.this);
                        } catch (Exception e) {
        
                            // Log.e(TAG, e.getMessage());
                        }
        
                                */
                ShoppingFragment.coupon_code = ""
                ShoppingFragment.coupon_value = ""
                ShoppingFragment.coupon_value_unit = ""
                ShoppingFragment.totalCouponDiscount = 0
                ShoppingFragment.couponProductList = ArrayList()


                ShoppingFragment.all_cart_list.clear()
                // Toast.makeText(getActivity().getApplicationContext(),""+String.valueOf(response).toString(),Toast.LENGTH_SHORT).show();
            },
            { error ->
                printStackTrace(
                    error
                )
            })

        requestQueue.add(jsonObjectRequest)
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        // Toast.makeText(getContext(),"REsponse="+response,Toast.LENGTH_LONG).show();
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

    override fun setUserCardResponseUpdate(giftCardModelUpdateResponse: UserGiftCardUpdateResponse?) {
        // Toast.makeText(getActivity(),"Messagee:"+giftCardModelUpdateResponse.getMessage()+" Tran ID:"+giftCardModelUpdateResponse.getTransaction_id(),Toast.LENGTH_LONG).show();

        if (!giftCardModelUpdateResponse!!.isSuccess) {
            userGiftCardPresenter!!.updateGiftCardFail(giftCardModelUpdateResponse.transaction_id)
            blogViewModel!!.ecomCoinFail(giftCardModelUpdateResponse.transaction_id)
        }
    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubscribeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?, param3: String?): SuccessFragment {
            val fragment = SuccessFragment()
            val args = Bundle()
            args.putString("amount", param1)
            args.putString("address", param2)
            args.putString("gift_card_used_value", param3)
            fragment.arguments = args
            return fragment
        }
    }
}

