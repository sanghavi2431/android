package in.woloo.www.shopping.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.application_kotlin.api_classes.BaseResponse;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.giftcard.giftcard.model.UserCoins;
import in.woloo.www.more.giftcard.giftcard.viewmodel.CoinsViewModel;
import in.woloo.www.shopping.adapter.CartAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.CartModel;
import in.woloo.www.shopping.model.UserGiftCardResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements CartAdapter.UpdateTotalSummaryInfo , PaymentResultListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.imgCart)
    public ImageView imgCart;

    @BindView(R.id.imgSearch)
    public  ImageView imgSearch;


    @BindView(R.id.cart_count_textview)
    public TextView cart_count_textview;

    @BindView(R.id.radioGroup)
    public RadioGroup radioGroup;

    @BindView(R.id.radioAddress)
    public RadioButton radioAddress;

    @BindView(R.id.radioPickup)
    public RadioButton radioPickup;



    @BindView(R.id.pickupNote)
    public  TextView pickupNote;


    @BindView(R.id.recycler_view)
    public RecyclerView recycler_view;

    @BindView(R.id.totalGiftCardValue)
    public  TextView totalGiftCardValue;

    @BindView(R.id.total_gift_card_used)
    public  TextView total_gift_card_used;

    @BindView(R.id.useGiftCardCheckbox)
    public CheckBox useGiftCardCheckbox;

    @BindView(R.id.giftCardSection)
    public LinearLayout giftCardSection;


    @BindView(R.id.bag_total)
    public  TextView bag_total;

    @BindView(R.id.shipping_charges)
    public  TextView shipping_charges;
    @BindView(R.id.bag_subtotal)
    public  TextView bag_subtotal;
    @BindView(R.id.total_point_used)
    public  TextView total_point_used;
    @BindView(R.id.coupon_discount_textview)
    public  TextView coupon_discount_textview;
    @BindView(R.id.total_point_left)
    public  TextView total_point_left;
    @BindView(R.id.total_payable)
    public  TextView total_payable;
    @BindView(R.id.final_amount)
    public  TextView final_amount;

    @BindView(R.id.checkout)
    public TextView checkout1;


    @BindView(R.id.couponLayout)
    public LinearLayout couponLayout;


    @BindView(R.id.enterCouponLayout)
    public LinearLayout enterCouponLayout;

    @BindView(R.id.appliedCouponLayout)
    public LinearLayout appliedCouponLayout;

    @BindView(R.id.coupon_code_edittext)
    public EditText coupon_code_edittext;

    @BindView(R.id.applyCouponCode)
    public TextView applyCouponCode;

    @BindView(R.id.applied_coupon_textview)
    public TextView applied_coupon_textview;

    @BindView(R.id.removeCouponCode)
    public ImageView removeCouponCode;




    public List<CartModel> cartList = new ArrayList<>();

    public static CartAdapter cartAdapter;

    public String selected_address_id="0" , selected_address="";
    public static int  total_shipping_charges = 0 , total_shipping_charges_current =0 , free_shipping_above_value = 500000 ,total_cart_value , total_gift_coins = 0 , gift_card_used_value = 0 , invalid_pincode = 0;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String shippingUrl = Config.hostname+"get_shipping_charges_api.php";
    private String couponUrl = Config.hostname+"apply_coupon_api.php";
//    private UserGiftCardPresenter userGiftCardPresenter;
    private CoinsViewModel coinsViewModel;

    public boolean is_any_vendor_product = false, is_any_franchisee_product= false, is_any_admin_product=false;

    public String total_payment_amount = "0";

    private static final String TAG = WolooDashboard.class.getSimpleName();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;

    public CartFragment() {
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
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString("selected_address_id", param1);
        args.putString("selected_address", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_address_id = getArguments().getString("selected_address_id");
            selected_address = getArguments().getString("selected_address");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this,root);

//        userGiftCardPresenter = new UserGiftCardPresenter(getActivity(),CartFragment.this);
        coinsViewModel = new ViewModelProvider(this).get(CoinsViewModel.class);
        // userGiftCardPresenter.getUserGiftCard();






        initViews();
        setLiveData();
        return root;
    }

    void setLiveData(){
        coinsViewModel.observeUserCoins().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserCoins>>() {
            @Override
            public void onChanged(BaseResponse<UserCoins> response) {
                if(response != null && response.getData() !=null){
                    ShoppingFragment.userGiftCardValue = response.getData().getGiftCoins();

                    total_gift_coins = response.getData().getGiftCoins();

                    //Toast.makeText(getActivity(),"Point:"+giftCardModelResponse.getGiftCoins(),Toast.LENGTH_LONG).show();


                    if(ShoppingFragment.userGiftCardValue==0) {

                        giftCardSection.setVisibility(View.GONE);

                    }
                    else
                    {
                        totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));
                    }



        /*

        if(useGiftCardCheckbox.isChecked())
        {
            if(ShoppingFragment.userGiftCardValue  >= Integer.parseInt(total_payment_amount)) {

                gift_card_used_value = Integer.parseInt(total_payment_amount);

            }
            else
            {
                gift_card_used_value = ShoppingFragment.userGiftCardValue;
            }

            ShoppingFragment.userGiftCardValue =  total_gift_coins - gift_card_used_value;
            totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));


        }
        */

                    getShippingCharges();

                    // setTotalSummary();

                }else{
                    WolooApplication.errorMessage = "";
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try{

            selected_address = ShoppingFragment.user_address;
            if(!selected_address.equalsIgnoreCase(""))
            {
                radioAddress.setText("Ship To: "+selected_address);
            }
            else
            {
                radioAddress.setText("Ship to:"+ShoppingFragment.pincode+" (Select Address)");
            }

            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }

            initViews();




            ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).hideFooter();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void initViews() {
        try{

            gift_card_used_value = 0;
            total_gift_coins = 0;


//            userGiftCardPresenter.getUserGiftCard();
            coinsViewModel.getUserCoins();

            tvTitle.setText("Cart");
            ivBack.setOnClickListener(v -> {

                // getActivity().getBa

                getActivity().onBackPressed();
            });


            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }



            // Go to cart

            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CartFragment myFragment = new CartFragment();
                    Bundle b = new Bundle();
                    b.putString("selected_address_id",ShoppingFragment.selected_address_id);
                    b.putString("selected_address",ShoppingFragment.user_address);
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard)getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment,"");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


            imgSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    try {

                        SearchFragment myFragment = new SearchFragment();
                        Bundle b = new Bundle();
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                    catch(Exception e) {


                    }

                }
            });



            // Select Addess


            // Display Cart List

            CartFragment.invalid_pincode = 0;


            cartList.clear();
            cartAdapter = new CartAdapter(cartList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());

            cartAdapter.setOnUpdateListener(this);
            recycler_view.setAdapter(cartAdapter);


            int bag_total_value=0;
            int shopping_charges_value=0;
            int bag_sub_total_value=0;
            int total_point_used_value=0;
            int total_payable_value=0;


            CartModel yourwork;


            for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {


                String pro_id=ShoppingFragment.all_cart_list.get(i).get(0);
                String pro_name=ShoppingFragment.all_cart_list.get(i).get(1);
                String pro_image=ShoppingFragment.all_cart_list.get(i).get(2);
                String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
                String qty=ShoppingFragment.all_cart_list.get(i).get(4);
                String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
                String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
                String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);

                String product_of = ShoppingFragment.all_cart_list.get(i).get(8);

                if(product_of.equalsIgnoreCase("vendor"))
                {
                    is_any_vendor_product = true;
                }
                if(product_of.equalsIgnoreCase("franchisee"))
                {
                    is_any_franchisee_product = true;
                }

                if(product_of.equalsIgnoreCase("admin"))
                {
                    is_any_admin_product = true;
                }




                yourwork = new CartModel(pro_id,pro_name,pro_image,pro_price,qty,customer_margin_per,point_used,total_amount,"","");


                cartList.add(yourwork);

                // total summary calculation



                bag_total_value = bag_total_value + ( Integer.parseInt(qty) * Integer.parseInt(pro_price));
                bag_sub_total_value  = bag_total_value;
                total_cart_value = bag_total_value;

                total_point_used_value = total_point_used_value + Integer.parseInt(point_used);

                total_payable_value  =  total_payable_value + Integer.parseInt(total_amount);



            }




            cartAdapter.notifyDataSetChanged();

            // setTotalSummary();
            //getShippingCharges





            // pickup condition
            if(is_any_vendor_product && !is_any_admin_product && !is_any_franchisee_product)
            {
                radioPickup.setVisibility(View.GONE);
                pickupNote.setVisibility(View.GONE);
            }

            if(!is_any_vendor_product && (is_any_admin_product || is_any_franchisee_product ))
            {
                // radioPickup.setVisibility(View.VISIBLE);
                pickupNote.setVisibility(View.GONE);
            }

            if(is_any_vendor_product && (is_any_admin_product || is_any_franchisee_product ))
            {
                //  radioPickup.setVisibility(View.VISIBLE);
                radioPickup.setEnabled(false);
                pickupNote.setVisibility(View.VISIBLE);
            }












            // Select Address

            radioAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, new SelectAddressFragment(),"");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });

            radioPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(radioPickup.isChecked())
                    {
                        total_shipping_charges_current = 0;
                        setTotalSummary();
                    }
                }
            });

            radioAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(radioAddress.isChecked())
                    {
                        total_shipping_charges_current = total_shipping_charges;
                        setTotalSummary();
                    }
                }
            });





            applyCouponCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String enter_copon_code = coupon_code_edittext.getText().toString();


                    applyCoupon(enter_copon_code);




                }
            });


            removeCouponCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        ShoppingFragment.coupon_code = "";
                        ShoppingFragment.coupon_value = "";
                        ShoppingFragment.coupon_value_unit = "";
                        ShoppingFragment.totalCouponDiscount = 0;
                        ShoppingFragment.couponProductList = new ArrayList<>();

                        CartFragment.invalid_pincode = 0;


                        appliedCouponLayout.setVisibility(View.GONE);
                        enterCouponLayout.setVisibility(View.VISIBLE);
                        coupon_code_edittext.setText("");




                        CartModel yourwork;

                        cartList.clear();


                        for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {


                            String pro_id=ShoppingFragment.all_cart_list.get(i).get(0);
                            String pro_name=ShoppingFragment.all_cart_list.get(i).get(1);
                            String pro_image=ShoppingFragment.all_cart_list.get(i).get(2);
                            String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
                            String qty=ShoppingFragment.all_cart_list.get(i).get(4);
                            String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
                            String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
                            String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);

                            String product_of = ShoppingFragment.all_cart_list.get(i).get(8);




                            yourwork = new CartModel(pro_id,pro_name,pro_image,pro_price,qty,customer_margin_per,point_used,total_amount,"","");


                            cartList.add(yourwork);



                        }




                        cartAdapter.notifyDataSetChanged();

                        setTotalSummary();

                    } catch(Exception e) {
                        // Toast.makeText(getActivity().getApplicationContext(),"Error+"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });











            useGiftCardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                  /*  if(useGiftCardCheckbox.isChecked())
                    {
                        if(ShoppingFragment.userGiftCardValue  >= Integer.parseInt(total_payment_amount)) {

                            gift_card_used_value = Integer.parseInt(total_payment_amount);

                        }
                        else
                        {
                            gift_card_used_value = ShoppingFragment.userGiftCardValue;
                        }

                        ShoppingFragment.userGiftCardValue =  total_gift_coins - gift_card_used_value;
                        totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));


                    }
                    else
                    {
                        ShoppingFragment.userGiftCardValue =  total_gift_coins;
                        totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));
                        gift_card_used_value = 0;



                    }

                    */


                    setTotalSummary();

                }
            });






            // load preload checkout form payment gateway
            Checkout.preload(getContext());





            // Load PAyment Gateay

            checkout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                   /* SuccessFragment myFragment = new SuccessFragment();
                    Bundle b = new Bundle();
                    b.putString("amount",total_payment_amount);
                    b.putString("gift_card_used_value",String.valueOf(gift_card_used_value));
                    b.putString("address",selected_address);
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                    fragmentTransaction.commit();

                   */

                    if(radioPickup.isChecked())
                    {
                        selected_address =  "Pickup from store";
                    }



                    if(String.valueOf(selected_address).trim().equalsIgnoreCase("") || String.valueOf(selected_address).trim().equalsIgnoreCase("null") )
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Please select Address",Toast.LENGTH_SHORT).show();

                    }
                    else if(ShoppingFragment.all_cart_list.size() == 0)
                    {
                        Toast.makeText(getActivity().getApplicationContext().getApplicationContext(),"Your cart is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(CartFragment.invalid_pincode == 1)
                    {
                        Toast.makeText(getActivity().getApplicationContext().getApplicationContext(),"Please Remove not deliverable products on your pincode",Toast.LENGTH_SHORT).show();

                    }




                    else if(total_payment_amount.equalsIgnoreCase("0") && gift_card_used_value > 0 )
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("total_amount", total_payment_amount);
                        bundle.putString("gift_card_used_value",String.valueOf(gift_card_used_value));
                        bundle.putString("address",String.valueOf(gift_card_used_value));
                        bundle.putString("pincode", selected_address);
                        Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "checkout_click");





                        SuccessFragment myFragment = new SuccessFragment();
                        Bundle b = new Bundle();
                        b.putString("amount",total_payment_amount);
                        b.putString("gift_card_used_value",String.valueOf(gift_card_used_value));
                        b.putString("address",selected_address);
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                        fragmentTransaction.commit();

                    }
                    else
                    {
                        // startPayment();


                        Bundle bundle = new Bundle();
                        bundle.putString("total_amount", total_payment_amount);
                        bundle.putString("gift_card_used_value",String.valueOf(gift_card_used_value));
                        bundle.putString("address",String.valueOf(gift_card_used_value));
                        bundle.putString("pincode", selected_address);
                        Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "checkout_click");






                        PaymentWebviewFragement myFragment = new PaymentWebviewFragement();
                        Bundle b = new Bundle();
                        b.putString("amount",total_payment_amount);
                        b.putString("gift_card_used_value",String.valueOf(gift_card_used_value));
                        b.putString("address",selected_address);
                        myFragment.setArguments(b);


                        FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, myFragment,"");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }




                }
            });

















            if(!selected_address.equalsIgnoreCase(""))
            {
                radioAddress.setText("Ship To: "+selected_address);
            }



            if(!ShoppingFragment.coupon_code.equalsIgnoreCase(""))
            {

                coupon_code_edittext.setText(ShoppingFragment.coupon_code);
                applyCoupon(ShoppingFragment.coupon_code);

            }







        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    public void setTotalSummary()
    {





        // Update Summary Total
        int bag_total_value=0;
        int shipping_charges_value=total_shipping_charges_current;



        int bag_sub_total_value=0;
        int total_point_used_value=0;
        int total_payable_value=0;
        int coupon_discount_value= 0;

        for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {



            String pro_id=ShoppingFragment.all_cart_list.get(i).get(0);
            String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
            String qty=ShoppingFragment.all_cart_list.get(i).get(4);
            String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
            String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
            String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);


            String product_of = ShoppingFragment.all_cart_list.get(i).get(8);

            if(product_of.equalsIgnoreCase("vendor"))
            {
                is_any_vendor_product = true;
            }
            if(product_of.equalsIgnoreCase("franchisee"))
            {
                is_any_franchisee_product = true;
            }

            if(product_of.equalsIgnoreCase("admin"))
            {
                is_any_admin_product = true;
            }







            bag_total_value = bag_total_value + ( Integer.parseInt(qty) * Integer.parseInt(pro_price));
            bag_sub_total_value  = bag_total_value ;

            total_point_used_value = total_point_used_value + Integer.parseInt(point_used);

            total_payable_value  =  total_payable_value + Integer.parseInt(total_amount);


            if(ShoppingFragment.couponProductList.contains(pro_id))
            {
                float c_dicount_on_product = 0;
                if(ShoppingFragment.coupon_value_unit.equalsIgnoreCase("per"))
                {
                    c_dicount_on_product = Integer.parseInt(total_amount) * Float.parseFloat(ShoppingFragment.coupon_value) / 100;

                }
                else

                {
                    c_dicount_on_product =  Float.parseFloat(ShoppingFragment.coupon_value)  * Integer.parseInt(qty);
                }

                coupon_discount_value = (int) (coupon_discount_value + Math.floor(c_dicount_on_product));
            }



        }

        bag_total.setText("Rs. "+String.valueOf(bag_total_value));


        if(bag_total_value >=  free_shipping_above_value ) {


            shipping_charges_value = 0;

        }





        if(shipping_charges_value == 0) {
            shipping_charges.setText("FREE");
        }
        else
        {
            shipping_charges.setText("Rs. "+String.valueOf(shipping_charges_value));
        }


        if(ShoppingFragment.all_cart_list.size() == 0)
        {
            shipping_charges.setText("0");
            shipping_charges_value = 0;


            ShoppingFragment.coupon_code = "";
            ShoppingFragment.coupon_value = "";
            ShoppingFragment.coupon_value_unit = "";
            ShoppingFragment.totalCouponDiscount = 0;
            ShoppingFragment.couponProductList = new ArrayList<>();


            appliedCouponLayout.setVisibility(View.GONE);
            enterCouponLayout.setVisibility(View.VISIBLE);
            coupon_code_edittext.setText("");

        }



        bag_sub_total_value = bag_sub_total_value + shipping_charges_value;
        total_payable_value = total_payable_value + shipping_charges_value;

        ShoppingFragment.totalCouponDiscount = coupon_discount_value;

        total_payable_value = total_payable_value - ShoppingFragment.totalCouponDiscount;





        if(ShoppingFragment.all_cart_list.size() == 0)
        {
            if(gift_card_used_value > 0)
            {
                ShoppingFragment.userGiftCardValue =  ShoppingFragment.userGiftCardValue + gift_card_used_value;
                totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));

            }
            gift_card_used_value = 0;


        }
        else {


            if (useGiftCardCheckbox.isChecked()) {
                if (ShoppingFragment.userGiftCardValue >= total_payable_value) {

                    gift_card_used_value = total_payable_value;

                } else {
                    gift_card_used_value = ShoppingFragment.userGiftCardValue;
                }

                ShoppingFragment.userGiftCardValue = total_gift_coins - gift_card_used_value;
                totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));


            }
            else
            {
                ShoppingFragment.userGiftCardValue = total_gift_coins;

                totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));
                gift_card_used_value = 0;
            }


        }



        total_payable_value = total_payable_value - gift_card_used_value;













        bag_subtotal.setText("Rs. "+String.valueOf(bag_sub_total_value));
        total_point_used.setText(String.valueOf(total_point_used_value)+" Pts.");
        total_gift_card_used.setText(String.valueOf(gift_card_used_value)+" Pts.");
        coupon_discount_textview.setText(String.valueOf(ShoppingFragment.totalCouponDiscount));
        total_payable.setText("Rs. "+String.valueOf(total_payable_value));
        final_amount.setText("Rs. "+String.valueOf(total_payable_value));



        int total_point_left_value = ShoppingFragment.userTotalPointsBackup - total_point_used_value;

        ShoppingFragment.userTotalPoints = total_point_left_value;



        total_point_left.setText(String.valueOf(ShoppingFragment.userTotalPoints)+" Pts.");


        total_payment_amount  = String.valueOf(total_payable_value);


        // pickup condition
        if(is_any_vendor_product && !is_any_admin_product && !is_any_franchisee_product)
        {
            radioPickup.setVisibility(View.GONE);
            pickupNote.setVisibility(View.GONE);
        }

        if(!is_any_vendor_product && (is_any_admin_product || is_any_franchisee_product ))
        {
            //radioPickup.setVisibility(View.VISIBLE);
            pickupNote.setVisibility(View.GONE);
        }

        if(is_any_vendor_product && (is_any_admin_product || is_any_franchisee_product ))
        {
            //radioPickup.setVisibility(View.VISIBLE);
            radioPickup.setEnabled(false);
            pickupNote.setVisibility(View.VISIBLE);
        }







        cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
        if(ShoppingFragment.all_cart_list.size() == 0) {
            cart_count_textview.setVisibility(View.GONE);
        } else { cart_count_textview.setVisibility(View.VISIBLE); }







    }


    @Override
    public void updateTotalSummaryInfo() {
        setTotalSummary();

    }




    private void getShippingCharges() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, shippingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray obj = new JSONArray(response);
                    JSONObject catObj = (JSONObject) obj.get(0);
                    String shipping_charges_val = catObj.getString("shipping_charges");
                    String free_shipping_above = catObj.getString("free_shipping_above");


                    total_shipping_charges = Integer.parseInt(shipping_charges_val);
                    total_shipping_charges_current = Integer.parseInt(shipping_charges_val);
                    free_shipping_above_value = Integer.parseInt(free_shipping_above);







                    setTotalSummary();











                }
                catch (Exception e) {

                    setTotalSummary();

                    //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }





    private void applyCoupon(String coupon_code) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, couponUrl+"?user_id="+ShoppingFragment.user_id+"&coupon_code="+coupon_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray obj = new JSONArray(response);
                    JSONObject catObj = (JSONObject) obj.get(0);
                    String result = catObj.getString("result");

                    if(result.equalsIgnoreCase("success")) {

                        String coupon_code = catObj.getString("coupon_code");
                        String title = catObj.getString("title");
                        String coupon_value = catObj.getString("value");
                        String coupon_value_unit = catObj.getString("value_unit");
                        String product_ids = catObj.getString("product_ids");



                        List<String> couponProductList = Arrays.asList(product_ids.split("\\s*,\\s*"));



                        cartList.clear();

                        int bag_total_value=0;
                        int shopping_charges_value=0;
                        int bag_sub_total_value=0;
                        int total_point_used_value=0;
                        int total_payable_value=0;


                        int is_product_added_coupon = 0;


                        CartModel yourwork;



                        for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {


                            String pro_id=ShoppingFragment.all_cart_list.get(i).get(0);
                            String pro_name=ShoppingFragment.all_cart_list.get(i).get(1);
                            String pro_image=ShoppingFragment.all_cart_list.get(i).get(2);
                            String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
                            String qty=ShoppingFragment.all_cart_list.get(i).get(4);
                            String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
                            String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
                            String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);

                            String product_of = ShoppingFragment.all_cart_list.get(i).get(8);

                            if(product_of.equalsIgnoreCase("vendor"))
                            {
                                is_any_vendor_product = true;
                            }
                            if(product_of.equalsIgnoreCase("franchisee"))
                            {
                                is_any_franchisee_product = true;
                            }

                            if(product_of.equalsIgnoreCase("admin"))
                            {
                                is_any_admin_product = true;
                            }




                            if(couponProductList.contains(pro_id))
                            {

                                is_product_added_coupon = 1;

                                yourwork = new CartModel(pro_id, pro_name, pro_image, pro_price, qty, customer_margin_per, point_used, total_amount, coupon_value, coupon_value_unit);

                            }
                            else {

                                yourwork = new CartModel(pro_id, pro_name, pro_image, pro_price, qty, customer_margin_per, point_used, total_amount, "", "");
                            }



                            cartList.add(yourwork);



                        }




                        cartAdapter.notifyDataSetChanged();






                        if(is_product_added_coupon == 1) {

                            ShoppingFragment.coupon_code = coupon_code;
                            ShoppingFragment.coupon_value = coupon_value;
                            ShoppingFragment.coupon_value_unit = coupon_value_unit;
                            ShoppingFragment.couponProductList = couponProductList;




                            enterCouponLayout.setVisibility(View.GONE);
                            appliedCouponLayout.setVisibility(View.VISIBLE);
                            if(coupon_value_unit.equalsIgnoreCase("per")) {
                                applied_coupon_textview.setText(coupon_code + " Coupon Applied  \n "+coupon_value+"% discount on a product ");
                            }
                            else
                            {
                                applied_coupon_textview.setText(coupon_code + " Coupon Applied  \n Rs. "+coupon_value+" flat discount on a product ");


                            }

                        }
                        else
                        {

                            final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
                            dialog.setContentView(R.layout.popup_coupon);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkayPincode);
                            TextView message_text = (TextView) dialog.findViewById(R.id.message_text);

                            message_text.setText("Invalid Coupon Code");

                            tvOkay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    dialog.dismiss();
                                }
                            });


                            dialog.show();

                        }






                        setTotalSummary();

                    }

                    else {






                        final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
                        dialog.setContentView(R.layout.popup_coupon);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkayPincode);
                        TextView message_text = (TextView) dialog.findViewById(R.id.message_text);

                        message_text.setText(catObj.getString("message"));

                        tvOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                dialog.dismiss();
                            }
                        });


                        dialog.show();


                    }













                }
                catch (Exception e) {

                    setTotalSummary();

                    //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }





















    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_live_GCIwrUoJIqrzYt");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            checkout.open(activity, options);
        } catch(Exception e) {
            Logger.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(getContext(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Logger.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getContext(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Logger.e(TAG, "Exception in onPaymentError", e);
        }
    }

//    @Override
    public void setUserCardResponse(UserGiftCardResponse giftCardModelResponse) {

        ShoppingFragment.userGiftCardValue = giftCardModelResponse.giftCoins;

        total_gift_coins = giftCardModelResponse.giftCoins;

        //Toast.makeText(getActivity(),"Point:"+giftCardModelResponse.getGiftCoins(),Toast.LENGTH_LONG).show();


        if(ShoppingFragment.userGiftCardValue==0) {

            giftCardSection.setVisibility(View.GONE);

        }
        else
        {
            totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));
        }



        /*

        if(useGiftCardCheckbox.isChecked())
        {
            if(ShoppingFragment.userGiftCardValue  >= Integer.parseInt(total_payment_amount)) {

                gift_card_used_value = Integer.parseInt(total_payment_amount);

            }
            else
            {
                gift_card_used_value = ShoppingFragment.userGiftCardValue;
            }

            ShoppingFragment.userGiftCardValue =  total_gift_coins - gift_card_used_value;
            totalGiftCardValue.setText(String.valueOf(ShoppingFragment.userGiftCardValue));


        }
        */

        getShippingCharges();

        // setTotalSummary();



    }
}