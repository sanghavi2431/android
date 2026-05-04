package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
import in.woloo.www.shopping.adapter.SearchAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.ProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {



    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.etSearchText)
    public EditText etSearchText;

    @BindView(R.id.noDataFoundTextView)
    public TextView noDataFoundTextView;

    @BindView(R.id.noDataLayout)
    public LinearLayout noDataLayout;

    @BindView(R.id.grid_view)
    public GridView categoryGridView;

    public SearchAdapter productAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    public static List<List<String>> all_product_images = new ArrayList<List<String>>();





    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String productsUrl = Config.hostname+"get_search_list_api.php";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("catID", param1);
        args.putString("catName", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getString("catId");
            catName = getArguments().getString("catName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_list, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{

            String keyword = etSearchText.getText().toString();
            if(!keyword.trim().equalsIgnoreCase("")) {

                getProductList(keyword);
            }



            ((WolooDashboard)getActivity()).hideToolbar();
            ((WolooDashboard)getActivity()).showFooter();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void initViews() {
        try{

            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });


            etSearchText.addTextChangedListener(filterTextWatcher);


            etSearchText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {

                   String keyword = etSearchText.getText().toString();


                    if(!keyword.trim().equalsIgnoreCase("")) {

                        getProductList(keyword);
                    }



                    return false;
                }
            });



            // Get Products from categgory

          //  getProductList(catId);



        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {

            String keyword = etSearchText.getText().toString();

            if(!keyword.trim().equalsIgnoreCase("")) {

                getProductList(keyword);
            }



        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) {




        }
    };




    private void getProductList(String keyword) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, productsUrl+"?keyword="+keyword+"&user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {

                    productList.clear();

                    noDataLayout.setVisibility(View.GONE);
                    categoryGridView.setVisibility(View.VISIBLE);


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



                    productAdapter = new SearchAdapter(getActivity(), R.layout.product_search_row, productItems);

                    categoryGridView.setNumColumns(1);

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