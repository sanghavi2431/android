package in.woloo.www.shopping.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.woloo.www.R;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.OrderModel;

public class MyOrderAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public Context mycontext;
    public LinearLayout mainLinearLayout;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String orderReturnUrl = Config.hostname+"return_order_api.php";

    public MyOrderAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.priceTextView = (TextView) row.findViewById(R.id.price);
            holder.order_id = (TextView) row.findViewById(R.id.order_id);
            holder.amount = (TextView) row.findViewById(R.id.amount);
            holder.return_order = (TextView) row.findViewById(R.id.return_order);
            holder.date_time = (TextView) row.findViewById(R.id.date_time);
            holder.status = (TextView) row.findViewById(R.id.status);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.mainLinearLayout=(LinearLayout)row.findViewById(R.id.mainLinearLayout);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        final OrderModel item = (OrderModel) data.get(position);
        holder.imageTitle.setText(item.title);
        holder.priceTextView.setText("Rs. "+ item.price +" X "+ item.qty);
        holder.order_id.setText("Order Id: "+ item.order_id);
        holder.status.setText("Order Status: "+ item.status);
        holder.amount.setText("Amount: "+ item.amount);
        holder.date_time.setText(item.date_time);

        if(item.status.equalsIgnoreCase("Return"))
        {
            holder.status.setText("Status: Return Requested");
        }

        //   holder.image.setImageBitmap(item.getImage());

        String imageUri = Config.hostname + "../images/" + item.image;
        if(item.image.contains("http"))
        {
            imageUri = item.image;
        }



        Picasso.get().load(imageUri).into(holder.image);


        if(item.can_return.equalsIgnoreCase("1")) {

            holder.return_order.setVisibility(View.VISIBLE);

        }


        holder.return_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent_background)));
                dialog.setContentView(R.layout.popup_return_confirmation);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                TextView cancel_button = (TextView) dialog.findViewById(R.id.cancel_button);
                TextView confirm_button = (TextView) dialog.findViewById(R.id.confirm_button);
                CheckBox confirm_checkbox = (CheckBox) dialog.findViewById(R.id.confirm_checkbox);


                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });



                confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(confirm_checkbox.isChecked()) {

                            view.setVisibility(View.GONE);

                            Toast.makeText(context.getApplicationContext(), "Order Return Requested!", Toast.LENGTH_LONG).show();

                            returnProduct(item.id, item.pro_id);

                            item.status = "Return";
                            item.can_return = "0";

                            notifyDataSetChanged();


                            dialog.dismiss();

                        }
                        else
                        {

                            Toast.makeText(context.getApplicationContext(), "Please check on confirmation!", Toast.LENGTH_LONG).show();


                        }
                    }
                });


                dialog.show();







            }
        });









        return row;
    }

    private  void returnProduct(String id, String pro_id)
    {

        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(getContext());


        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, orderReturnUrl+"?user_id="+ShoppingFragment.user_id+"&order_id="+id+"&pro_id="+pro_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {



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










    static class ViewHolder {
        TextView imageTitle , priceTextView , order_id, amount , return_order, status, date_time;
        ImageView image;
        public LinearLayout mainLinearLayout;
    }
}