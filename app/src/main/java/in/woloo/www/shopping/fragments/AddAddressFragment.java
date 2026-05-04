package in.woloo.www.shopping.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAddressFragment extends Fragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.imgCart)
    public ImageView imgCart;

    @BindView(R.id.cart_count_textview)
    public TextView cart_count_textview;


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.pincode)
    EditText pincode;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.state)
    EditText state;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.flat_building)
    EditText flat_building;
    @BindView(R.id.landmark)
    EditText landmark;
    @BindView(R.id.save_address)
    Button save_address;





    public String selected_address_id="0" , selected_address="";


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String addressUrl = Config.hostname+"save_address_api.php";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String catId;
    public String catName;

    public AddAddressFragment() {
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
    public static AddAddressFragment newInstance(String param1, String param2) {
        AddAddressFragment fragment = new AddAddressFragment();
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
        View root = inflater.inflate(R.layout.fragment_add_address, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }

    private void initViews() {
        try{
            tvTitle.setText("Address");
            ivBack.setOnClickListener(v -> {
                // getActivity().onBackPressed();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
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

            // Save Address  List


            save_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name_value = name.getText().toString();
                    String pincode_value = pincode.getText().toString();
                    String city_value = city.getText().toString();
                    String state_value = state.getText().toString();
                    String area_value = area.getText().toString();
                    String flat_building_value = flat_building.getText().toString();
                    String landmark_value = landmark.getText().toString();


                    if(name_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
                    }
                    else if(pincode_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter Pincode",Toast.LENGTH_SHORT).show();
                    }
                    else if(city_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter City",Toast.LENGTH_SHORT).show();
                    }
                    else if(state_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter State",Toast.LENGTH_SHORT).show();
                    }
                    else if(area_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter Area",Toast.LENGTH_SHORT).show();
                    }
                    else if(flat_building_value.trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity().getApplicationContext(),"Please Enter Flat No and Bulding Name",Toast.LENGTH_SHORT).show();
                    }

                    else {

                        Bundle bundle = new Bundle();
                        bundle.putString("name",name_value);
                        bundle.putString("city",city_value);
                        bundle.putString("state",state_value);
                        bundle.putString("area",area_value);
                        bundle.putString("flat_building",flat_building_value);
                        bundle.putString("landmark",landmark_value);
                        bundle.putString("pincode", pincode_value);
                        Utility.logFirebaseEvent(((WolooDashboard)getActivity()), bundle, "shopping_add_address");



                        saveAddressApi(ShoppingFragment.user_id, name_value,ShoppingFragment.user_phone, pincode_value, city_value, state_value, area_value, flat_building_value, landmark_value);

                    }

                }
            });







        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }





    private void saveAddressApi(String user_id,String name, String phone,String pincode_value, String city_value, String state_value, String area_value, String flat_builing_value, String landmark_value) {


        String postUrl = addressUrl;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", user_id);
            postData.put("name", name);
            postData.put("phone", phone);
            postData.put("pincode", pincode_value);
            postData.put("city", city_value);
            postData.put("state", state_value);
            postData.put("area", area_value);
            postData.put("flat_builing", flat_builing_value);
            postData.put("landmark", landmark_value);

        } catch (JSONException e) {
              CommonUtils.printStackTrace(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);

                Toast.makeText(getActivity().getApplicationContext(),"Address Added Successfully",Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_contant, new SelectAddressFragment(),"");
                fragmentTransaction.commit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_contant, new SelectAddressFragment(),"");
                fragmentTransaction.commit();

                // Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();

                CommonUtils.printStackTrace(error);



            }
        });

        requestQueue.add(jsonObjectRequest);
    }









}