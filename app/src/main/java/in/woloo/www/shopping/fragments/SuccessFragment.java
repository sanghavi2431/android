package in.woloo.www.shopping.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.UserGiftCardUpdateResponse;
import in.woloo.www.shopping.mvp.UserGiftCardPresenter;
import in.woloo.www.shopping.mvp.UserGiftCardUpdateView;
import in.woloo.www.shopping.mvp.UserGiftCardView;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.blog.model.EcomCoinUpdateResponse;
import in.woloo.www.v2.blog.viewmodel.BlogViewModel;
import in.woloo.www.v2.data.remote.BaseResponse;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccessFragment extends Fragment  implements NetworkAPIResponseCallback, UserGiftCardUpdateView {


    @BindView(R.id.go_to_shop)
    TextView go_to_shop;


    private String saveOrderUrl = Config.hostname+"save_order_api.php";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String amount;
    public String address;
    public int total_point_used = 0;
    public String gift_card_used_value;
    public String orderid="";

    private UserGiftCardPresenter userGiftCardPresenter;
    BlogViewModel blogViewModel;

    public SuccessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuccessFragment newInstance(String param1, String param2, String param3) {
        SuccessFragment fragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putString("amount", param1);
        args.putString("address", param2);
        args.putString("gift_card_used_value", param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amount = getArguments().getString("amount");
             address = getArguments().getString("address");
            gift_card_used_value = getArguments().getString("gift_card_used_value");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_success, container, false);
        ButterKnife.bind(this,root);

        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
        userGiftCardPresenter = new UserGiftCardPresenter(getActivity(),SuccessFragment.this);

        initViews();
        setLiveData();
        return root;
    }


    private void initViews() {
        try{


            go_to_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, new ShoppingFragment(),"");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });


            // Get All order  and product order
            JSONArray jsonArrayorder = new JSONArray();
            JSONArray jsonArrayorderproduct = new JSONArray();



            // Putting in json
            try {

                JSONObject JSONrow = new JSONObject();
                JSONrow.put("user_id", ShoppingFragment.user_id);
                JSONrow.put("name", String.valueOf(ShoppingFragment.user_name));
                JSONrow.put("total_amount", String.valueOf(amount));
                JSONrow.put("address", String.valueOf(address));
                JSONrow.put("email", String.valueOf(ShoppingFragment.user_email));
                JSONrow.put("mobile", String.valueOf(ShoppingFragment.user_phone));
                JSONrow.put("shipping_charges", String.valueOf(CartFragment.total_shipping_charges_current));
                JSONrow.put("user_type", ShoppingFragment.user_type);
                JSONrow.put("gift_card_used_value", gift_card_used_value);
                JSONrow.put("coupon_code", ShoppingFragment.coupon_code);
                JSONrow.put("coupon_discount", ShoppingFragment.totalCouponDiscount);



                jsonArrayorder.put(JSONrow);

            } catch (Exception e) {  }




            for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {



                String pro_id=ShoppingFragment.all_cart_list.get(i).get(0);
                String pro_name=ShoppingFragment.all_cart_list.get(i).get(1);
                String pro_image=ShoppingFragment.all_cart_list.get(i).get(2);
                String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
                String qty=ShoppingFragment.all_cart_list.get(i).get(4);
                String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
                String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
                String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);


                total_point_used = total_point_used + Integer.valueOf(point_used);





                // Putting in json
                try {
                    JSONObject JSONrow = new JSONObject();
                    JSONrow.put("pro_id", pro_id);
                    JSONrow.put("pro_name", pro_name);
                    JSONrow.put("qty", qty);
                    JSONrow.put("price", pro_price);
                    JSONrow.put("customer_margin_per", customer_margin_per);
                    JSONrow.put("point_used", point_used);
                    JSONrow.put(amount, total_amount);

                    jsonArrayorderproduct.put(JSONrow);

                } catch (Exception e) {  }




            }

              JSONObject EverythingJSON = new JSONObject();

            EverythingJSON.put("order", jsonArrayorder);
            EverythingJSON.put("order_product", jsonArrayorderproduct);

           saveOrder(EverythingJSON);





        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    void setLiveData(){
        blogViewModel.observeEcomCoinUpdate().observe(getViewLifecycleOwner(), new Observer<BaseResponse<EcomCoinUpdateResponse>>() {
            @Override
            public void onChanged(BaseResponse<EcomCoinUpdateResponse> response) {
                if(!response.getSuccess()) {
//            userGiftCardPresenter.updateGiftCardFail(giftCardModelUpdateResponse.getTransaction_id());
//                    blogViewModel.ecomCoinFail(response.getTransaction_id()); TODO
                }
            }
        });
    }

    private void saveOrder(JSONObject postData) {


        String postUrl = saveOrderUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);



                try {
                    orderid = response.getString("orderid");
                } catch (JSONException e) {
                      CommonUtils.printStackTrace(e);
                }

                try {

                    if (total_point_used > 0) {
                        userGiftCardPresenter.updateGiftCard("points", total_point_used,orderid);
                        blogViewModel.ecomCoinUpdate("points", total_point_used,orderid);

                    }


                    if (Integer.valueOf(gift_card_used_value) > 0) {
                        userGiftCardPresenter.updateGiftCard("gift", Integer.valueOf(gift_card_used_value),orderid);
                        blogViewModel.ecomCoinUpdate("gift", Integer.valueOf(gift_card_used_value),orderid);
                    }

                } catch(Exception e) {   }




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




                ShoppingFragment.coupon_code = "";
                ShoppingFragment.coupon_value = "";
                ShoppingFragment.coupon_value_unit = "";
                ShoppingFragment.totalCouponDiscount = 0;
                ShoppingFragment.couponProductList = new ArrayList<>();


                ShoppingFragment.all_cart_list.clear();

               // Toast.makeText(getActivity().getApplicationContext(),""+String.valueOf(response).toString(),Toast.LENGTH_SHORT).show();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.printStackTrace(error);
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {

       // Toast.makeText(getContext(),"REsponse="+response,Toast.LENGTH_LONG).show();

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

    @Override
    public void setUserCardResponseUpdate(UserGiftCardUpdateResponse giftCardModelUpdateResponse) {

       // Toast.makeText(getActivity(),"Messagee:"+giftCardModelUpdateResponse.getMessage()+" Tran ID:"+giftCardModelUpdateResponse.getTransaction_id(),Toast.LENGTH_LONG).show();
        if(!giftCardModelUpdateResponse.isSuccess()) {
            userGiftCardPresenter.updateGiftCardFail(giftCardModelUpdateResponse.getTransaction_id());
            blogViewModel.ecomCoinFail(giftCardModelUpdateResponse.getTransaction_id());
        }

    }


}

