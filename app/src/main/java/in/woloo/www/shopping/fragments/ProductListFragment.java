package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.home_shop.ContentCommerceFragment;
import in.woloo.www.shopping.adapter.ProductAdapter;
import in.woloo.www.shopping.adapter.SubCategoryAdapterTop;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.CategoryModel;
import in.woloo.www.shopping.model.ProductModel;
import in.woloo.www.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {

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



    @BindView(R.id.noDataFoundTextView)
    public TextView noDataFoundTextView;

    @BindView(R.id.noDataLayout)
    public LinearLayout noDataLayout;


    @BindView(R.id.sub_category_layout)
    public LinearLayout sub_category_layout;

    @BindView(R.id.recycler_view_cat)
    public RecyclerView recycler_view_cat;



    @BindView(R.id.grid_view)
    public GridView categoryGridView;

    public ProductAdapter productAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    public static List<List<String>> all_product_images = new ArrayList<List<String>>();

    public SubCategoryAdapterTop adapterViewAndroidtop;
    public static List<CategoryModel> categoryList = new ArrayList<>();





    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String productsUrl = Config.hostname+"get_product_list_api.php";
    private String productsUrlCoupon = Config.hostname+"get_product_list_coupon_code_vise_api.php";
    private String subcategoryUrl = Config.hostname+"get_sub_category_api.php";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;
    public String cat_type="category";
    public String parentFragmentName;

    public ProductListFragment() {
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
    public static ProductListFragment newInstance(String param1, String param2 , String param3, String parentFragment) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString("catID", param1);
        args.putString("catName", param2);
        args.putString("cat_type", param3);
        args.putString("parent" , parentFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getString("catId");
            catName = getArguments().getString("catName");
            cat_type = getArguments().getString("cat_type");
            parentFragmentName = getArguments().getString("parent");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{


            ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).showFooter();

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
            tvTitle.setText(catName);
            ivBack.setOnClickListener(v -> {
                if(parentFragmentName!= null && parentFragmentName.matches(ContentCommerceFragment.TAG))
                {
                    WolooDashboard dashboardActivity = (WolooDashboard) requireActivity();

                    dashboardActivity.loadFragment(new ContentCommerceFragment(), ContentCommerceFragment.TAG);

                    dashboardActivity.changeIcon(
                            dashboardActivity.nav_view.getMenu().findItem(R.id.navigation_dash_home)
                    );
                }
                else {
                    getActivity().onBackPressed();
                }
            });

            // Go to cart


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














            // Get Products from categgory

            //Toast.makeText(getActivity().getApplicationContext(),"Ycat_type"+cat_type,Toast.LENGTH_LONG).show();

            if(cat_type.equalsIgnoreCase("category")) {
                getTopCategory(catId);
            } else {  sub_category_layout.setVisibility(View.GONE);  }


            if(cat_type.equalsIgnoreCase("coupon"))
            {
                sub_category_layout.setVisibility(View.GONE);

                getProductListCoupon(catId, cat_type);
            }
            else {


                getProductList(catId, cat_type);

            }



        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }




    private void getTopCategory(String category_id) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, subcategoryUrl+"?category_id="+category_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                //  Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();
                categoryList.clear();



                try {
                    JSONArray obj = new JSONArray(response);

                    adapterViewAndroidtop = new SubCategoryAdapterTop(categoryList);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recycler_view_cat.setLayoutManager(layoutManager);
                    recycler_view_cat.setItemAnimator(new DefaultItemAnimator());
                    recycler_view_cat.setAdapter(adapterViewAndroidtop);
                    recycler_view_cat.setNestedScrollingEnabled(false);

                    CategoryModel yourwork;

                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject catObj = (JSONObject) obj.get(i);

                        yourwork = new CategoryModel(catObj.getString("image"), catObj.getString("name"), catObj.getString("id"), "dashboard","","","");


                        categoryList.add(yourwork);


                    }



                    adapterViewAndroidtop.notifyDataSetChanged();

                    if(categoryList.size() == 0)
                    {
                        sub_category_layout.setVisibility(View.GONE);
                    }












                }
                catch (Exception e) {

                    Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

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




    public void getProductList(String category_id, String cat_type) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, productsUrl+"?cat_id="+category_id+"&user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode+"&cat_type="+cat_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {

                    productList.clear();


                    JSONArray obj = new JSONArray(response);
                    JSONArray product = obj.getJSONArray(0);
                    JSONArray product_images = obj.getJSONArray(1);







                    final   ArrayList<ProductModel> productItems = new ArrayList<>();




                    int c=0;
                    ProductModel yourwork;
                    for (int i = 0; i < product.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product.get(i);



                        yourwork= new ProductModel(yourworkObj.getString("id"),yourworkObj.getString("name"),yourworkObj.getString("price"),yourworkObj.getString("desc"),yourworkObj.getString("date_time"),yourworkObj.getString("image"));


                        productItems.add(yourwork);




                    }

                    all_product_images.clear();
                    for (int i = 0; i < product_images.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product_images.get(i);



                        ArrayList<String>single_product=new ArrayList<String>();

                        single_product.add(yourworkObj.getString("product_id"));
                        single_product.add(yourworkObj.getString("img"));


                        all_product_images.add(single_product);

                    }




                    if(productItems.size()==0)
                    {
                        noDataLayout.setVisibility(View.VISIBLE);
                        categoryGridView.setVisibility(View.GONE);
                    }



                    productAdapter = new ProductAdapter(getActivity(), R.layout.product_single_row, productItems);

                    categoryGridView.setNumColumns(2);

                    categoryGridView.setAdapter(productAdapter);














                }
                catch (Exception e) {


                    noDataLayout.setVisibility(View.VISIBLE);
                    categoryGridView.setVisibility(View.GONE);

                    // Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

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





    private void getProductListCoupon(String category_id, String cat_type) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, productsUrlCoupon+"?coupon_code="+category_id+"&user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode+"&cat_type="+cat_type+"&user_id="+ShoppingFragment.user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {

                    productList.clear();


                    JSONArray obj = new JSONArray(response);
                    JSONArray product = obj.getJSONArray(0);
                    JSONArray product_images = obj.getJSONArray(1);







                    final   ArrayList<ProductModel> productItems = new ArrayList<>();




                    int c=0;
                    ProductModel yourwork;
                    for (int i = 0; i < product.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product.get(i);



                        yourwork= new ProductModel(yourworkObj.getString("id"),yourworkObj.getString("name"),yourworkObj.getString("price"),yourworkObj.getString("desc"),yourworkObj.getString("date_time"),yourworkObj.getString("image"));


                        productItems.add(yourwork);




                    }

                    all_product_images.clear();
                    for (int i = 0; i < product_images.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product_images.get(i);



                        ArrayList<String>single_product=new ArrayList<String>();

                        single_product.add(yourworkObj.getString("product_id"));
                        single_product.add(yourworkObj.getString("img"));


                        all_product_images.add(single_product);

                    }




                    if(productItems.size()==0)
                    {
                        noDataLayout.setVisibility(View.VISIBLE);
                        categoryGridView.setVisibility(View.GONE);
                    }



                    productAdapter = new ProductAdapter(getActivity(), R.layout.product_single_row, productItems);

                    categoryGridView.setNumColumns(2);

                    categoryGridView.setAdapter(productAdapter);














                }
                catch (Exception e) {


                    noDataLayout.setVisibility(View.VISIBLE);
                    categoryGridView.setVisibility(View.GONE);

                    // Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();

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








}