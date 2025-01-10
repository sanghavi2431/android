package in.woloo.www.shopping.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.ProductDetailsFragment;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.HomeProductModel;
import in.woloo.www.utils.Utility;


class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.MyViewHolder> {
    public ProgressDialog progressDialog;
    private List<HomeProductModel> workList;

    public Context context1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView counttext,priceTextView , datetimeTextview , commissionTextview , statusTextview;
        public LinearLayout fullLinearLayout;

        public Context context;
        public  ProgressDialog progressDialog;

        public ImageView img,img1,banner_img;


        TextView imageTitle;
        ImageView image;
        View view1;
        public LinearLayout mainLinearLayout;



        public final Handler handler = new Handler();

        public MyViewHolder(View view) {
            super(view);



            imageTitle = (TextView) view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);
            mainLinearLayout=(LinearLayout)view.findViewById(R.id.mainLinearLayout);




            context=view.getContext();
            context1=view.getContext();



        }
    }





    public HomeProductAdapter(List<HomeProductModel> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HomeProductModel work = workList.get(position);


        holder.imageTitle.setText(work.name);

        String imageUri = Config.hostname + "../images/" + work.image;


        if(work.image.contains("http"))
        {
            imageUri = work.image;
        }



        Picasso.get().load(imageUri).transform(new RoundedTransformation(10, 0)).into(holder.image);








        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{

                    Bundle bundle = new Bundle();
                    bundle.putString("product_name", work.name);
                    bundle.putString("pincode", ShoppingFragment.pincode);
                    Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "shopping_view_product");



                    ProductDetailsFragment myFragment = new ProductDetailsFragment();
                    Bundle b = new Bundle();
                    b.putString("proId", work.id);
                    b.putString("proName", work.name);
                    b.putString("proPrice", work.price);
                    b.putString("proDesc", work.desc);
                    b.putString("from","dashboard");
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard)context1).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment,"");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }catch (Exception ex){
                     CommonUtils.printStackTrace(ex);
                }






            }
        });


       /* String colorcode= work.getColor();
        colorcode = colorcode.replace("#","");

        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(8, Color.parseColor("#59"+colorcode));
        border.setCornerRadius(5);


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            holder.mainLinearLayout.setBackgroundDrawable(border);

        } else {
            holder.mainLinearLayout.setBackground(border);

        }
        holder.view1.setBackgroundColor(Color.parseColor(work.getColor()));
*/









    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

















































}