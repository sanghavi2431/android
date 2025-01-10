package in.woloo.www.shopping.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
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
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.home_shop.ContentCommerceFragment;
import in.woloo.www.shopping.adapter.BannerAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.utils.Utility;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {

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


    @BindView(R.id.pager)
    public ViewPager mPager;

    @BindView(R.id.indicator)
    public CircleIndicator indicator;


    @BindView(R.id.titleTextView)
    public TextView titleTextView;

    @BindView(R.id.priceTextView)
    public TextView priceTextView;

    @BindView(R.id.descTextView)
    public WebView descTextView;

    @BindView(R.id.radioGroup)
    public RadioGroup radioGroup;

    @BindView(R.id.radioPointUsed)
    public RadioButton radioPointUsed;

    @BindView(R.id.radioFullAmount)
    public RadioButton radioFullAmount;

    @BindView(R.id.decrementButton)
    public TextView decrementButton;

    @BindView(R.id.incrementButton)
    public TextView incrementButton;

    @BindView(R.id.quantity_textview)
    public EditText quantity_textview;


    @BindView(R.id.proceedButton)
    public LinearLayout proceedButton;





    public ArrayList<String> ImagesArray = new ArrayList<String>();
    public ArrayList<String> ProductImagesArray = new ArrayList<String>();





    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String productsUrl = Config.hostname+"get_product_details_api.php";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String proId;
    public String proName;

    public String parentFragmentName;
    String title,qty,price,desc,from="", product_of="", is_pincode_available="";
    public int total_stock = 0;

    public String user_total_points, customer_margin_per="0";
    public boolean point_can_be_use=false;

    public ProductDetailsFragment() {
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
    public static ProductDetailsFragment newInstance(String param1, String param2,String param3,String param4,String param5 , String parentFragment) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString("proId", param1);
        args.putString("proName", param2);
        args.putString("proPrice", param3);
        args.putString("proDesc", param4);
        args.putString("from", param5);
        args.putString("parent" , parentFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proId = getArguments().getString("proId");
            proName = getArguments().getString("proName");
            price = getArguments().getString("proPrice");
            desc = getArguments().getString("proDesc");
            from = getArguments().getString("from");
            parentFragmentName = getArguments().getString("parent");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{



            ((WolooDashboard)getActivity()).hideFooter();


            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }





        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    private void initViews() {
        try{
            tvTitle.setText(proName);
            ivBack.setOnClickListener(v -> {
                if(parentFragmentName!= null && parentFragmentName.matches(ContentCommerceFragment.TAG))
                {
                   /* WolooDashboard dashboardActivity = (WolooDashboard) requireActivity();

                    dashboardActivity.loadFragment(new ContentCommerceFragment(), ContentCommerceFragment.TAG);

                    dashboardActivity.changeIcon(
                            dashboardActivity.nav_view.getMenu().findItem(R.id.navigation_dash_home)
                    );*/
                    Fragment currentFragment =new  ContentCommerceFragment();
                    WolooDashboard dashboardActivity = (WolooDashboard) requireActivity();
                    try {
                        FragmentManager fragmentManager = dashboardActivity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frm_contant, currentFragment, ContentCommerceFragment.TAG);
                        fragmentTransaction.disallowAddToBackStack();
                        fragmentTransaction.commit();
                    } catch (Exception ex) {
                        CommonUtils.printStackTrace(ex);
                    }
                    dashboardActivity.showToolbar();
                }
                else {
                    getActivity().onBackPressed();
                }
            });

            // Go to cart

            quantity_textview.setEnabled(false);


            if(price.contains(".")) {
                // price = price.split(".")[0];

                int doti  = price.indexOf(".");

                price = price.substring(0,doti);






            }



            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }




            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(ShoppingFragment.all_cart_list.size() != 0 ) {


                        Bundle bundle = new Bundle();
                        bundle.putString("pincode", ShoppingFragment.pincode);
                        Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "shopping_cart_icon_click");


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
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Your Cart is empty",Toast.LENGTH_LONG).show();
                    }
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


            // Get Product Images

            try {


                ProductImagesArray.clear();



                if(from.equalsIgnoreCase("dashboard"))
                {
                    for (int i = 0; i < ShoppingFragment.all_product_images.size(); i++) {


                        String pro_id = ShoppingFragment.all_product_images.get(i).get(0);


                        if (pro_id.equalsIgnoreCase(proId)) {


                            ProductImagesArray.add(ShoppingFragment.all_product_images.get(i).get(1));


                        }


                    }
                }
                else if(from.equalsIgnoreCase("search_adapter")) {


                    for (int i = 0; i < SearchFragment.all_product_images.size(); i++) {


                        String pro_id = SearchFragment.all_product_images.get(i).get(0);


                        if (pro_id.equalsIgnoreCase(proId)) {


                            ProductImagesArray.add(SearchFragment.all_product_images.get(i).get(1));


                        }


                    }

                }

                else {

                    for (int i = 0; i < ProductListFragment.all_product_images.size(); i++) {


                        String pro_id = ProductListFragment.all_product_images.get(i).get(0);


                        if (pro_id.equalsIgnoreCase(proId)) {


                            ProductImagesArray.add(ProductListFragment.all_product_images.get(i).get(1));


                        }


                    }

                }


                mPager.setAdapter(new BannerAdapter(getActivity(), ProductImagesArray, "product_details"));
                indicator.setViewPager(mPager);


              /*  DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                mPager.getLayoutParams().height = height - ( height * 23/100  );
                mPager.requestLayout();
                */



                titleTextView.setText(proName);
                priceTextView.setText("Rs."+price);
                descTextView.loadDataWithBaseURL("",desc,"text/html","UTF-8","");




                // customer_margin_per = "10";



                radioFullAmount.setText("Get this for Rs."+price);


                radioPointUsed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b)
                        {
                            if(!point_can_be_use)
                            {

                                radioFullAmount.setChecked(true);

                                // Toast.makeText(getActivity().getApplicationContext(),"Opps Not enough points",Toast.LENGTH_LONG).show();


                                try {
                                    final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
                                    dialog.setContentView(R.layout.popup_error_point);
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);
                                    tvOkay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            radioFullAmount.setChecked(true);

                                            dialog.dismiss();
                                        }
                                    });


                                    dialog.show();
                                } catch (Exception e) {
                                      CommonUtils.printStackTrace(e);
                                }



                            }
                        }
                    }
                });



                decrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int value = Integer.parseInt(quantity_textview.getText().toString().trim());
                        if(value > 1) {
                            value = value - 1;
                            quantity_textview.setText(String.valueOf(value));
                        }


                    }
                });

                incrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int value = Integer.parseInt(quantity_textview.getText().toString().trim());

                        value = value + 1;

                        if(value <= total_stock) {

                            quantity_textview.setText(String.valueOf(value));

                        }
                        else {
                            Toast.makeText(getActivity().getApplicationContext(),"Only "+total_stock+" stock available",Toast.LENGTH_SHORT).show();
                        }



                    }
                });





                // Add to Cart functionality
                proceedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Add to card here
                        boolean can_add = true;

                        int point_used = 0;
                        int total_amount = 0 ;


                        if(is_pincode_available.equalsIgnoreCase("0")) {

                            can_add = false;


                            try {
                                final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
                                dialog.setContentView(R.layout.popup_error_pincode);
                                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkayPincode);
                                tvOkay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        dialog.dismiss();
                                    }
                                });


                                dialog.show();
                            } catch (Exception e) {
                                  CommonUtils.printStackTrace(e);
                            }


                        }

                        else {


                            if ((!radioPointUsed.isChecked()) && (!radioFullAmount.isChecked())) {
                                can_add = false;

                                Toast.makeText(getActivity().getApplicationContext(), "Please select one option", Toast.LENGTH_LONG).show();


                            }
                            if (total_stock == 0) {
                                can_add = false;

                                Toast.makeText(getActivity().getApplicationContext(), "Out Of Stock", Toast.LENGTH_LONG).show();


                            }


                            if (radioPointUsed.isChecked()) {


                                point_used = Integer.parseInt(quantity_textview.getText().toString().trim()) * Integer.parseInt(customer_margin_per);

                                if (point_used <= ShoppingFragment.userTotalPoints) {

                                    total_amount = Integer.parseInt(quantity_textview.getText().toString().trim()) * Integer.parseInt(price) - point_used;

                                    ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints - point_used;
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Sorry insufficient Points", Toast.LENGTH_LONG).show();
                                    can_add = false;
                                }

                            } else {
                                total_amount = Integer.parseInt(quantity_textview.getText().toString().trim()) * Integer.parseInt(price);
                            }


                            if (can_add) {



                                // check first if pro id added into cart
                                boolean is_aready_added = false;
                                int cart_index = 0;

                                for(int k=0; k<ShoppingFragment.all_cart_list.size(); k++)
                                {
                                    String added_pro_id=ShoppingFragment.all_cart_list.get(k).get(0);

                                    if(added_pro_id.equalsIgnoreCase(proId))
                                    {
                                        is_aready_added = true;

                                        String pro_qty=ShoppingFragment.all_cart_list.get(k).get(4);
                                        String pro_total_amount=ShoppingFragment.all_cart_list.get(k).get(7);
                                        String pro_point_used=ShoppingFragment.all_cart_list.get(k).get(6);

                                        ShoppingFragment.all_cart_list.get(k).set(6, String.valueOf(Integer.parseInt(pro_point_used) + point_used));
                                        ShoppingFragment.all_cart_list.get(k).set(7, String.valueOf(Integer.parseInt(pro_total_amount) + total_amount));


                                        ShoppingFragment.all_cart_list.get(k).set(4, String.valueOf( Integer.parseInt(pro_qty) + Integer.parseInt(quantity_textview.getText().toString().trim()) ));





                                    }


                                }


                                if(!is_aready_added) {


                                    ArrayList<String> all_data = new ArrayList<String>();
                                    all_data.add(proId);
                                    all_data.add(proName);
                                    all_data.add(ProductImagesArray.get(0));
                                    all_data.add(price);
                                    all_data.add(quantity_textview.getText().toString().trim());
                                    all_data.add(customer_margin_per);


                                    all_data.add(String.valueOf(point_used));
                                    all_data.add(String.valueOf(total_amount));

                                    all_data.add(product_of);

                                    all_data.add(String.valueOf(total_stock));

                                    all_data.add("0");


                                    ShoppingFragment.all_cart_list.add(all_data);

                                }







                                Toast.makeText(getActivity().getApplicationContext(), "Product added into cart", Toast.LENGTH_LONG).show();


                                // Redirect to cart page

                                Bundle bundle = new Bundle();

                                bundle.putString("product_name", proName);
                                bundle.putString("qty", quantity_textview.getText().toString().trim());
                                bundle.putString("pincode", ShoppingFragment.pincode);
                                Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "product_add_into_cart");










                                CartFragment myFragment = new CartFragment();
                                Bundle b = new Bundle();
                                b.putString("selected_address_id", "0");
                                b.putString("selected_address", "");
                                myFragment.setArguments(b);


                                FragmentManager fragmentManager = ((WolooDashboard) getActivity()).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frm_contant, myFragment, "");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }


                        }






                    }
                });





            }
            catch (Exception e)
            {
                // Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }

            // Get Products from categgory

            getProductData(proId);



        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }







    private void getProductData(String product_id) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, productsUrl+"?product_id="+product_id+"&user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {
                    // customer_margin_per = 0

                    JSONArray obj = new JSONArray(response);
                    JSONObject proData = (JSONObject) obj.get(0);
                    String customer_margin_per_value = proData.getString("customer_margin_per");

                    if(customer_margin_per_value.trim().equalsIgnoreCase(""))
                    {
                        customer_margin_per_value = "0";
                    }


                    product_of = proData.getString("product_of");

                    try {
                        total_stock = Integer.parseInt(proData.getString("stock"));
                    } catch(Exception e) { total_stock = 0; }








                    int use_point_val = Integer.parseInt(price) *  Integer.parseInt(customer_margin_per_value) / 100 ;

                    customer_margin_per = String.valueOf(use_point_val);

                    // Toast.makeText(getActivity(),"Respnse"+response+"customer_margin_per_value:"+customer_margin_per_value+" price"+price,Toast.LENGTH_LONG).show();


                    if(ShoppingFragment.userTotalPoints >= Integer.parseInt(customer_margin_per) ) {

                        int point_used_total = Integer.parseInt(price) - Integer.parseInt(customer_margin_per);


                        radioPointUsed.setText("Get this for Rs." + point_used_total + " By using " + customer_margin_per + " points");

                        point_can_be_use = true;
                    }
                    else {

                        // Hide points used
                        // radioPointUsed.setVisibility(View.GONE);
                        // radioPointUsed.setChecked(false);
                        // radioFullAmount.setChecked(true);

                        int point_used_total = Integer.parseInt(price) - Integer.parseInt(customer_margin_per);


                        radioPointUsed.setText("Get this for Rs." + point_used_total + " By using " + customer_margin_per + " points");

                        point_can_be_use = false;



                    }



                    is_pincode_available = proData.getString("is_pincode_available");




                }
                catch (Exception e) {

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


    private String convert(String decimalString){
        String converted = "";
        if (decimalString.indexOf(".") >= 0){
            String firstPart = decimalString.substring(0,
                    decimalString.indexOf("."));
            String lastPart =
                    decimalString.substring(decimalString.indexOf("."));
            if (lastPart.length() > 0) lastPart =
                    lastPart.substring(1);
            converted = firstPart + lastPart;
        }
        return converted;
    }


}