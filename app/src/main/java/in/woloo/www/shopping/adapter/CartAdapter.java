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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.woloo.www.R;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.CartFragment;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.CartModel;

public  class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    public ProgressDialog progressDialog;
    private List<CartModel> workList;

    UpdateTotalSummaryInfo mCallback;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String productsUrl = Config.hostname+"get_product_details_api.php";

    public Context context1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Context context;
        public  ProgressDialog progressDialog;




        TextView pro_name,pro_price,total_amount,point_used , coupon_text, no_pincode_delivery, decrementButton , incrementButton;
        EditText quantity_textview;
        ImageView pro_image , delete_icon;



        public final Handler handler = new Handler();

        public MyViewHolder(View view) {
            super(view);



            pro_name = (TextView) view.findViewById(R.id.pro_name);
            pro_price = (TextView) view.findViewById(R.id.pro_price);
            total_amount = (TextView) view.findViewById(R.id.total_amount);
            point_used = (TextView) view.findViewById(R.id.point_used);
            coupon_text = (TextView) view.findViewById(R.id.coupon_text);
            no_pincode_delivery = (TextView) view.findViewById(R.id.no_pincode_delivery);
            decrementButton = (TextView) view.findViewById(R.id.decrementButton);
            incrementButton = (TextView) view.findViewById(R.id.incrementButton);
            pro_image = (ImageView) view.findViewById(R.id.pro_image);
            delete_icon = (ImageView) view.findViewById(R.id.delete_icon);
            quantity_textview = (EditText) view.findViewById(R.id.quantity_textview);


            context=view.getContext();
            context1=view.getContext();


        }
    }





    public CartAdapter(List<CartModel> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    public void setOnUpdateListener(UpdateTotalSummaryInfo mCallback) {
        this.mCallback = mCallback;
    }

    public interface UpdateTotalSummaryInfo {
        public void updateTotalSummaryInfo();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CartModel work = workList.get(position);


         holder.pro_name.setText(work.getPro_name());
         holder.pro_price.setText("Rs."+work.getPrice());
         holder.total_amount.setText("To be paid Rs. "+work.getTotal_amount());
         holder.point_used.setText("Point Used: "+work.getPoint_used());
         holder.quantity_textview.setText(work.getQty());

        holder.quantity_textview.setEnabled(false);

        if(!work.getCoupon_value().equalsIgnoreCase(""))
        {
            holder.coupon_text.setVisibility(View.VISIBLE);
            if(work.getCoupon_value_unit().equalsIgnoreCase("per"))
            {
                float c_dicount_on_product = 0;
                c_dicount_on_product = Integer.parseInt(work.getTotal_amount()) * Float.parseFloat(work.getCoupon_value()) / 100;


                holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " +work.getCoupon_value()+"% Coupon Discount");
            }
            else {

                float c_dicount_on_product = 0;
                try {
                    c_dicount_on_product = Integer.parseInt(work.getQty()) * Float.parseFloat(work.getCoupon_value());

                }catch(Exception e){}
                holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " + "Rs."+work.getCoupon_value()+" flat Coupon Discount on a product");

            }
        }
        else
        {
            holder.coupon_text.setVisibility(View.GONE);
        }





        String imageUri = Config.hostname + "../images/" + work.getImage();
        if(work.getImage().contains("http"))
        {
            imageUri = work.getImage();
        }


        Picasso.get().load(imageUri).transform(new RoundedTransformation(10, 0)).into(holder.pro_image);



        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 0;
                try {
                    value = Integer.parseInt(holder.quantity_textview.getText().toString().trim());
                }catch (Exception e){
                    return;
                }
                if(value > 1) {
                    value = value - 1;
                    holder.quantity_textview.setText(String.valueOf(value));




                    int limit_crossed =  Integer.parseInt(ShoppingFragment.all_cart_list.get(position).get(10));

                    if(limit_crossed > 0) {
                        limit_crossed = limit_crossed - 1;

                        ShoppingFragment.all_cart_list.get(position).set(10, String.valueOf(limit_crossed));

                        try {
                            holder.total_amount.setText("To be paid Rs. " + String.valueOf((Integer.parseInt(work.getTotal_amount()) - (Integer.parseInt(work.getPrice())))));
                            ShoppingFragment.all_cart_list.get(position).set(7, String.valueOf((Integer.parseInt(work.getTotal_amount()) - (Integer.parseInt(work.getPrice())))));
                            work.setTotal_amount(String.valueOf((Integer.parseInt(work.getTotal_amount()) - (Integer.parseInt(work.getPrice())))));
                        }catch (Exception e){

                        }

                    }





                   else if(Integer.parseInt(work.getPoint_used()) > 0) {



                        holder.point_used.setText("Point Used:"+String.valueOf(Integer.parseInt(work.getPoint_used()) - Integer.parseInt(work.getCustomer_margin())));
                        holder.total_amount.setText("To be paid Rs. "+String.valueOf(Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice()) -  Integer.parseInt(work.getCustomer_margin()))));
                        ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints + Integer.parseInt(work.getCustomer_margin());



                        ShoppingFragment.all_cart_list.get(position).set(6,String.valueOf(Integer.parseInt(work.getPoint_used()) - Integer.parseInt(work.getCustomer_margin())));
                        ShoppingFragment.all_cart_list.get(position).set(7,String.valueOf(Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice()) -  Integer.parseInt(work.getCustomer_margin()))));

                        work.setPoint_used(String.valueOf(Integer.parseInt(work.getPoint_used()) - Integer.parseInt(work.getCustomer_margin())));
                        work.setTotal_amount(String.valueOf(Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice()) -  Integer.parseInt(work.getCustomer_margin()))));


                    }
                    else
                    {
                        holder.total_amount.setText("To be paid Rs. "+String.valueOf((Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice())))));
                         ShoppingFragment.all_cart_list.get(position).set(7,String.valueOf((Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice())))));
                        work.setTotal_amount(String.valueOf((Integer.parseInt(work.getTotal_amount()) - ( Integer.parseInt(work.getPrice())))));


                    }

                    work.setQty(String.valueOf(value));


                    ShoppingFragment.all_cart_list.get(position).set(4,String.valueOf(value));

                    mCallback.updateTotalSummaryInfo();







                    if(!work.getCoupon_value().equalsIgnoreCase(""))
                    {
                        holder.coupon_text.setVisibility(View.VISIBLE);
                        if(work.getCoupon_value_unit().equalsIgnoreCase("per"))
                        {
                            float c_dicount_on_product = 0;
                            c_dicount_on_product = Integer.parseInt(work.getTotal_amount()) * Float.parseFloat(work.getCoupon_value()) / 100;


                            holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " +work.getCoupon_value()+"% Coupon Discount");
                        }
                        else {

                            float c_dicount_on_product = 0;
                            c_dicount_on_product = Integer.parseInt(work.getQty()) * Float.parseFloat(work.getCoupon_value());


                            holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " + "Rs."+work.getCoupon_value()+" flat Coupon Discount on a product");

                        }
                    }
                    else
                    {
                        holder.coupon_text.setVisibility(View.GONE);
                    }

                }




            }
        });

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int value = Integer.parseInt(holder.quantity_textview.getText().toString().trim());

                value = value + 1;



                if(value <= Integer.parseInt(ShoppingFragment.all_cart_list.get(position).get(9))) {


                    if (Integer.parseInt(work.getPoint_used()) > 0) {


                        if (Integer.parseInt(work.getCustomer_margin()) <= ShoppingFragment.userTotalPoints) {

                            holder.point_used.setText("Point Used: " + String.valueOf(Integer.parseInt(work.getPoint_used()) + Integer.parseInt(work.getCustomer_margin())));
                            holder.total_amount.setText("To be paid Rs. " + String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()) - Integer.parseInt(work.getCustomer_margin()))));
                            ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints - Integer.parseInt(work.getCustomer_margin());


                            ShoppingFragment.all_cart_list.get(position).set(6, String.valueOf(Integer.parseInt(work.getPoint_used()) + Integer.parseInt(work.getCustomer_margin())));
                            ShoppingFragment.all_cart_list.get(position).set(7, String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()) - Integer.parseInt(work.getCustomer_margin()))));


                            ShoppingFragment.all_cart_list.get(position).set(4, String.valueOf(value));
                            work.setPoint_used(String.valueOf(Integer.parseInt(work.getPoint_used()) + Integer.parseInt(work.getCustomer_margin())));
                            work.setTotal_amount(String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()) - Integer.parseInt(work.getCustomer_margin()))));

                            work.setQty(String.valueOf(value));
                            holder.quantity_textview.setText(String.valueOf(value));


                            mCallback.updateTotalSummaryInfo();

                        } else {

                            // New Logic 15-jul-21 for point limit crossed


                            //limit crosssed increase value
                            int limit_crossed =  Integer.parseInt(ShoppingFragment.all_cart_list.get(position).get(10));

                            limit_crossed = limit_crossed + 1;

                            ShoppingFragment.all_cart_list.get(position).set(10, String.valueOf(limit_crossed));






                            holder.point_used.setText("Point Used: " + String.valueOf(Integer.parseInt(work.getPoint_used()) ));
                            holder.total_amount.setText("To be paid Rs. " + String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));


                            ShoppingFragment.all_cart_list.get(position).set(6, String.valueOf(Integer.parseInt(work.getPoint_used())  ));
                            ShoppingFragment.all_cart_list.get(position).set(7, String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));


                            ShoppingFragment.all_cart_list.get(position).set(4, String.valueOf(value));
                            work.setPoint_used(String.valueOf(Integer.parseInt(work.getPoint_used()) ));
                            work.setTotal_amount(String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));

                            work.setQty(String.valueOf(value));
                            holder.quantity_textview.setText(String.valueOf(value));

                           // ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints - ShoppingFragment.userTotalPoints;


                            mCallback.updateTotalSummaryInfo();





                          //  Toast.makeText(context1, "Sorry insufficient Points", Toast.LENGTH_LONG).show();

                        }


                    } else {
                        holder.total_amount.setText("To be paid Rs. " + String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));


                        ShoppingFragment.all_cart_list.get(position).set(7, String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));

                        ShoppingFragment.all_cart_list.get(position).set(4, String.valueOf(value));

                        work.setTotal_amount(String.valueOf(Integer.parseInt(work.getTotal_amount()) + (Integer.parseInt(work.getPrice()))));

                        work.setQty(String.valueOf(value));

                        holder.quantity_textview.setText(String.valueOf(value));


                        mCallback.updateTotalSummaryInfo();


                    }


                }

                else {


                }




                if(!work.getCoupon_value().equalsIgnoreCase(""))
                {
                    holder.coupon_text.setVisibility(View.VISIBLE);
                    if(work.getCoupon_value_unit().equalsIgnoreCase("per"))
                    {
                        float c_dicount_on_product = 0;
                        c_dicount_on_product = Integer.parseInt(work.getTotal_amount()) * Float.parseFloat(work.getCoupon_value()) / 100;


                        holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " +work.getCoupon_value()+"% Coupon Discount");
                    }
                    else {

                        float c_dicount_on_product = 0;
                        c_dicount_on_product = Integer.parseInt(work.getQty()) * Float.parseFloat(work.getCoupon_value());


                        holder.coupon_text.setText("(Saved Rs."+ Math.floor(c_dicount_on_product)+") " + "Rs."+work.getCoupon_value()+" flat Coupon Discount on a product");

                    }
                }
                else
                {
                    holder.coupon_text.setVisibility(View.GONE);
                }





            }
        });



        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(context1);
                builder.setMessage("Are you sure?")
                        .setTitle("")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {



                                Toast.makeText(context1,"Removed",Toast.LENGTH_SHORT).show();

                                ShoppingFragment.all_cart_list.remove(position);
                                ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints + Integer.parseInt(work.getPoint_used());

                                workList.remove(position);


                                notifyDataSetChanged();


                             // Update Summary Total
                                int bag_total_value=0;
                                int shopping_charges_value=0;
                                int bag_sub_total_value=0;
                                int total_point_used_value=0;
                                int total_payable_value=0;

                                for (int i=0;i< ShoppingFragment.all_cart_list.size();i++) {



                                    String pro_price=ShoppingFragment.all_cart_list.get(i).get(3);
                                    String qty=ShoppingFragment.all_cart_list.get(i).get(4);
                                    String customer_margin_per=ShoppingFragment.all_cart_list.get(i).get(5);
                                    String point_used=ShoppingFragment.all_cart_list.get(i).get(6);
                                    String total_amount=ShoppingFragment.all_cart_list.get(i).get(7);


                                    bag_total_value = bag_total_value + ( Integer.parseInt(qty) * Integer.parseInt(pro_price));
                                    bag_sub_total_value  = bag_total_value;

                                    total_point_used_value = total_point_used_value + Integer.parseInt(point_used);

                                    total_payable_value  =  total_payable_value + Integer.parseInt(total_amount);



                                }

                              //  CartFragment.setTotalSummary(bag_total_value,bag_sub_total_value,total_point_used_value,total_payable_value);

                                mCallback.updateTotalSummaryInfo();





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













        // check pincode available


        checkPincodeAvail(work.getPro_id(),ShoppingFragment.pincode,holder);














    }

    @Override
    public int getItemCount() {
        return workList.size();
    }


























    private  void checkPincodeAvail(String pro_id, String pincode, MyViewHolder holder)
    {

        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(holder.context);


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, productsUrl+"?product_id="+pro_id+"&user_type="+ShoppingFragment.user_type+"&pincode="+pincode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray obj = new JSONArray(response);
                    JSONObject proData = (JSONObject) obj.get(0);

                   String is_pincode_available = proData.getString("is_pincode_available");

                    if(is_pincode_available.equalsIgnoreCase("0")) {

                        holder.no_pincode_delivery.setVisibility(View.VISIBLE);
                        holder.no_pincode_delivery.setText("Not deliverable on pincode "+pincode);

                        CartFragment.invalid_pincode = 1;

                    }
                    else
                    {
                        holder.no_pincode_delivery.setVisibility(View.GONE);

                    }





                    }
                catch (Exception e) {



                }




            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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