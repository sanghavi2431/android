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
import in.woloo.www.shopping.fragments.ProductListFragment;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.CategoryModel;
import in.woloo.www.utils.Utility;


public class SubCategoryAdapterTop extends RecyclerView.Adapter<SubCategoryAdapterTop.MyViewHolder> {
    public ProgressDialog progressDialog;
    private List<CategoryModel> workList;

    public Context context1;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public Context context;
        public  ProgressDialog progressDialog;


        TextView imageTitle;
        ImageView image;
        View lineView;
        public LinearLayout mainLinearLayout;



        public final Handler handler = new Handler();

        public MyViewHolder(View view) {
            super(view);



            imageTitle = (TextView) view.findViewById(R.id.text);
             image = (ImageView) view.findViewById(R.id.image);
            lineView = (View) view.findViewById(R.id.lineView);
            mainLinearLayout=(LinearLayout)view.findViewById(R.id.mainLinearLayout);




            context=view.getContext();
            context1=view.getContext();



        }
    }





    public SubCategoryAdapterTop(List<CategoryModel> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_single_row_top, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryModel work = workList.get(position);


        holder.imageTitle.setText(work.name);
        Picasso.get().load(Config.hostname+"../images/"+ work.image).into(holder.image);




        holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();

                    try{

                        Bundle bundle = new Bundle();
                        bundle.putString("category_name", work.name);
                        bundle.putString("pincode", ShoppingFragment.pincode);
                        Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "category_icon_click");
                        
                        
                        
                        
                        ProductListFragment myFragment = new ProductListFragment();
                        Bundle b = new Bundle();
                        b.putString("catId", work.id);
                        b.putString("catName", work.name);
                        b.putString("cat_type","sub_category");
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











    }

    @Override
    public int getItemCount() {
        return workList.size();
    }





}