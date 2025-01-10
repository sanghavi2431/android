package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.woloo.www.shopping.adapter.MyOrderAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.OrderModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {




    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.imgCart)
    public ImageView imgCart;

    @BindView(R.id.cart_count_textview)
    public TextView cart_count_textview;

    @BindView(R.id.grid_view)
    public GridView categoryGridView;


    @BindView(R.id.noDataFoundTextView)
    public TextView noDataFoundTextView;

    @BindView(R.id.noDataLayout)
    public LinearLayout noDataLayout;

    public MyOrderAdapter myOrderAdapter;
    private List<OrderModel> orderList = new ArrayList<>();
    public static List<List<String>> all_product_images = new ArrayList<List<String>>();





    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String orderUrl = Config.hostname+"get_my_orders_api.php";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;

    public MyOrdersFragment() {
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
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View root = inflater.inflate(R.layout.fragment_my_orders_list, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{

            tvTitle.setText("Orders");

            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }



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

            cart_count_textview.setText(String.valueOf(ShoppingFragment.all_cart_list.size()));
            if(ShoppingFragment.all_cart_list.size() == 0) {
                cart_count_textview.setVisibility(View.GONE);
            } else { cart_count_textview.setVisibility(View.VISIBLE); }




            // Get Products from categgory

            getOrderList(ShoppingFragment.user_id);



        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }





    private void getOrderList(String user_id) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, orderUrl+"?user_id="+user_id+"&user_type="+ShoppingFragment.user_type+"&pincode="+ShoppingFragment.pincode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {

                    orderList.clear();

                    noDataLayout.setVisibility(View.GONE);
                    categoryGridView.setVisibility(View.VISIBLE);


                    JSONArray obj = new JSONArray(response);
                    JSONArray product = obj.getJSONArray(0);
                    JSONArray product_images = obj.getJSONArray(1);







                    final   ArrayList<OrderModel> productItems = new ArrayList<>();




                    int c=0;
                    OrderModel yourwork;
                    for (int i = 0; i < product.length(); i++) {
                        c++;
                        JSONObject yourworkObj = (JSONObject) product.get(i);



                        yourwork= new OrderModel(yourworkObj.getString("id"), yourworkObj.getString("pro_id"), yourworkObj.getString("name"),yourworkObj.getString("price"),yourworkObj.getString("desc"),yourworkObj.getString("qty"),yourworkObj.getString("image"),yourworkObj.getString("date_time"),yourworkObj.getString("order_id"),yourworkObj.getString("status"),yourworkObj.getString("can_return"),yourworkObj.getString("can_cancel"),yourworkObj.getString("amount"));


                        productItems.add(yourwork);




                    }

                    if(productItems.size()==0)
                    {
                        noDataLayout.setVisibility(View.VISIBLE);
                        categoryGridView.setVisibility(View.GONE);
                    }



                    myOrderAdapter = new MyOrderAdapter(getActivity(), R.layout.my_order_row, productItems);

                    categoryGridView.setNumColumns(1);

                    categoryGridView.setAdapter(myOrderAdapter);














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