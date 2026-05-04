package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import in.woloo.www.shopping.adapter.AddressAdapter;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.model.AddressModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectAddressFragment extends Fragment implements AddressAdapter.SelectAddress {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.imgCart)
    public ImageView imgCart;

    @BindView(R.id.cart_count_textview)
    public TextView cart_count_textview;

    @BindView(R.id.add_new_address)
    TextView add_new_address;

    @BindView(R.id.recycler_view)
    public RecyclerView recycler_view;




    public List<AddressModel> addressList = new ArrayList<>();

    public static AddressAdapter addressAdapter;

    public String selected_address_id="0" , selected_address="";


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String addressUrl = Config.hostname+"get_address_list_api.php";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;

    public SelectAddressFragment() {
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
    public static SelectAddressFragment newInstance(String param1, String param2) {
        SelectAddressFragment fragment = new SelectAddressFragment();
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
        View root = inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    private void initViews() {
        try{
            tvTitle.setText("Address");
            ivBack.setOnClickListener(v -> {
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

           // Get Address  List

            getAddressList(ShoppingFragment.user_id);

            add_new_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, new AddAddressFragment(),"");
                    fragmentTransaction.commit();


                }
            });





        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    public void selectAddressValue(String id,String address)
    {
        this.selected_address_id = id;
        this.selected_address = address;

        ShoppingFragment.user_address = address;

       /* CartFragment myFragment = new CartFragment();
        Bundle b = new Bundle();
        b.putString("selected_address_id",id);
        b.putString("selected_address",address);
        myFragment.setArguments(b);


        FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

        fragmentTransaction.commit();
        */

        getActivity().onBackPressed();




    }


    @Override
    public void selectAddress(String id,String address) {
         selectAddressValue(id,address);

    }



    private void getAddressList(String user_id) {

        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(getContext());
        mRequestQueue.getCache().remove(addressUrl+"?user_id="+user_id);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, addressUrl+"?user_id="+user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getActivity(),"Respnse"+response,Toast.LENGTH_LONG).show();



                try {

                    addressList.clear();

                    JSONArray obj = new JSONArray(response);


                    addressAdapter = new AddressAdapter(addressList);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recycler_view.setLayoutManager(layoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());

                    addressAdapter.setOnUpdateListener(SelectAddressFragment.this);

                    recycler_view.setAdapter(addressAdapter);
                    recycler_view.setNestedScrollingEnabled(false);

                    AddressModel yourwork;


                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject catObj = (JSONObject) obj.get(i);

                        yourwork = new AddressModel(catObj.getString("id"), catObj.getString("name"), catObj.getString("phone"),catObj.getString("pincode"),catObj.getString("city"),catObj.getString("state"),catObj.getString("area"),catObj.getString("flat_building"),catObj.getString("landmark"));


                        addressList.add(yourwork);


                    }

                    addressAdapter.notifyDataSetChanged();















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

        mRequestQueue.getCache().clear();
        mStringRequest.setShouldCache(false);
        mRequestQueue.add(mStringRequest);
    }









}