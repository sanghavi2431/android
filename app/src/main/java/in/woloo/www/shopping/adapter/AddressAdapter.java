package in.woloo.www.shopping.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.woloo.www.R;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.AddressModel;

public  class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    public ProgressDialog progressDialog;
    private List<AddressModel> workList;

    SelectAddress mCallback;

    public Context context1;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String deleteUrl = Config.hostname+"delete_address_api.php";


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Context context;
        public  ProgressDialog progressDialog;


        TextView label,name,address,phone,delete ;
        RadioButton select_radio;


        public final Handler handler = new Handler();

        public MyViewHolder(View view) {
            super(view);



            label = (TextView) view.findViewById(R.id.label);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            delete = (TextView) view.findViewById(R.id.delete);

            select_radio = (RadioButton) view.findViewById(R.id.select_radio);


            context=view.getContext();
            context1=view.getContext();


        }
    }





    public AddressAdapter(List<AddressModel> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    public void setOnUpdateListener(SelectAddress mCallback) {
        this.mCallback = mCallback;
    }

    public interface SelectAddress {
        public void selectAddress(String id, String address);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AddressModel work = workList.get(position);


        if(position !=0)
        {
            holder.label.setVisibility(View.GONE);
        }

         holder.name.setText(work.getName());
         holder.phone.setText("Phone: "+work.getPhone());


         holder.address.setText(work.getFlat_building()+" , "+work.getLandmark()+" , "+ work.getArea()+" , "+work.getCity()+"-"+work.getPincode()+" "+work.getState());


         holder.select_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                 ShoppingFragment.pincode = work.getPincode();
                 ShoppingFragment.current_pincode = work.getPincode();

                 mCallback.selectAddress(work.getId(),work.getFlat_building()+" , "+work.getLandmark()+" , "+ work.getArea()+" , "+work.getCity()+"-"+work.getPincode()+" "+work.getState());

             }
         });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(context1);
                builder.setMessage("Are you sure?")
                        .setTitle("")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {



                                Toast.makeText(context1,"Deleted",Toast.LENGTH_SHORT).show();


                                workList.remove(position);

                                // Call Delete API Here




                                notifyDataSetChanged();

                                deleteAddress(work.getId());



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();






            }
        });













    }

    @Override
    public int getItemCount() {
        return workList.size();
    }





    private void deleteAddress(String id) {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context1);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, deleteUrl+"?delete_id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {



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













































}