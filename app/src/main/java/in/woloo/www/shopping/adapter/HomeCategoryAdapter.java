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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.ProductListFragment;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.CategoryModel;
import in.woloo.www.shopping.model.HomeProductModel;
import in.woloo.www.utils.Utility;


public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder> {
    public ProgressDialog progressDialog;
    private List<CategoryModel> workList;

    public Context context1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Context context;
        public  ProgressDialog progressDialog;

        public ImageView img,img1,banner_img;


        TextView imageTitle , view_all;
        ImageView image , banner1 , banner2 , banner3;
        public LinearLayout mainLinearLayout;
        public RecyclerView recycler_view, recycler_view_sub_cat;

        public List<HomeProductModel> homeProductList = new ArrayList<>();
        public HomeProductAdapter homeProductAdapter;

        public List<CategoryModel> homeSubCategoryList = new ArrayList<>();
        public HomeSubCategoryAdapter homeSubCategoryAdapter;





        public final Handler handler = new Handler();

        public MyViewHolder(View view) {
            super(view);



            banner1 = (ImageView) view.findViewById(R.id.banner1);
            banner2 = (ImageView) view.findViewById(R.id.banner2);
            banner3 = (ImageView) view.findViewById(R.id.banner3);
            imageTitle = (TextView) view.findViewById(R.id.text);
            view_all = (TextView) view.findViewById(R.id.view_all);

              recycler_view=(RecyclerView) view.findViewById(R.id.recycler_view);
            recycler_view_sub_cat=(RecyclerView) view.findViewById(R.id.recycler_view_sub_cat);




            context=view.getContext();
            context1=view.getContext();



        }
    }






    public HomeCategoryAdapter(List<CategoryModel> workList) {
        this.workList = workList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_category_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryModel work = workList.get(position);


        holder.imageTitle.setText(work.getName());
        Picasso.get().load(Config.hostname+"../images/"+work.getBanner1()).transform(new RoundedTransformation(10, 0)).into(holder.banner1);
        Picasso.get().load(Config.hostname+"../images/"+work.getBanner2()).transform(new RoundedTransformation(10, 0)).into(holder.banner2);
        Picasso.get().load(Config.hostname+"../images/"+work.getBanner3()).transform(new RoundedTransformation(10, 0)).into(holder.banner3);






        holder.homeProductList.clear();
        holder.homeProductAdapter  = new HomeProductAdapter(holder.homeProductList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context1, LinearLayoutManager.HORIZONTAL, false);

        holder.recycler_view.setLayoutManager(layoutManager);

              /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                */

        holder.recycler_view.setItemAnimator(new DefaultItemAnimator());
        holder.recycler_view.setAdapter(holder.homeProductAdapter);
        // recyclerView.setNestedScrollingEnabled(false);
        //  recyclerView.setHasFixedSize(false);


         for (int i=0;i<ShoppingFragment.homeProductList.size();i++)
        {


            if(ShoppingFragment.homeProductList.get(i).getCount().equalsIgnoreCase(work.getId())) {

                holder.homeProductList.add(ShoppingFragment.homeProductList.get(i));
            }

        }

       //Toast.makeText(context1,"size"+holder.homeProductList.size(),Toast.LENGTH_LONG).show();

          holder.homeProductAdapter.notifyDataSetChanged();








        holder.homeSubCategoryList.clear();
        holder.homeSubCategoryAdapter  = new HomeSubCategoryAdapter(holder.homeSubCategoryList);

        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(context1, LinearLayoutManager.HORIZONTAL, false);

        holder.recycler_view_sub_cat.setLayoutManager(layoutManager2);

              /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                */

        holder.recycler_view_sub_cat.setItemAnimator(new DefaultItemAnimator());
        holder.recycler_view_sub_cat.setAdapter(holder.homeSubCategoryAdapter);
        // recyclerView.setNestedScrollingEnabled(false);
        //  recyclerView.setHasFixedSize(false);




        int get_count_sub = 0;

        for (int i=0;i<ShoppingFragment.homeSubCategoryList.size();i++)
        {


            if(ShoppingFragment.homeSubCategoryList.get(i).getFrom().equalsIgnoreCase(work.getId())) {

                get_count_sub++;

                holder.homeSubCategoryList.add(ShoppingFragment.homeSubCategoryList.get(i));
            }

        }

        //Toast.makeText(context1,"size"+holder.homeProductList.size(),Toast.LENGTH_LONG).show();

        holder.homeSubCategoryAdapter.notifyDataSetChanged();

        if(get_count_sub == 0)
        {

            holder.recycler_view_sub_cat.setVisibility(View.GONE);

        }




















        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();

                try{

                    Bundle bundle = new Bundle();
                    bundle.putString("category_name", work.getName());
                    bundle.putString("pincode", ShoppingFragment.pincode);
                    Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "category_view_all_click");


                    ProductListFragment myFragment = new ProductListFragment();
                    Bundle b = new Bundle();
                    b.putString("catId",work.getId());
                    b.putString("catName",work.getName());
                    b.putString("cat_type","category");
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

        holder.banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();

                try{

                    Bundle bundle = new Bundle();
                    bundle.putString("category_name", work.getName());
                    bundle.putString("pincode", ShoppingFragment.pincode);
                    Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "category_banner1_click");


                    ProductListFragment myFragment = new ProductListFragment();
                    Bundle b = new Bundle();
                    b.putString("catId",work.getId());
                    b.putString("catName",work.getName());
                    b.putString("cat_type","category");
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


        holder.banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();

                try{

                    Bundle bundle = new Bundle();
                    bundle.putString("category_name", work.getName());
                    bundle.putString("pincode", ShoppingFragment.pincode);
                    Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "category_banner2_click");



                    ProductListFragment myFragment = new ProductListFragment();
                    Bundle b = new Bundle();
                    b.putString("catId",work.getId());
                    b.putString("catName",work.getName());
                    b.putString("cat_type","category");
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


        holder.banner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();

                try{

                    Bundle bundle = new Bundle();
                    bundle.putString("category_name", work.getName());
                    bundle.putString("pincode", ShoppingFragment.pincode);
                    Utility.logFirebaseEvent(((WolooDashboard)context1), bundle, "category_banner3_click");



                    ProductListFragment myFragment = new ProductListFragment();
                    Bundle b = new Bundle();
                    b.putString("catId",work.getId());
                    b.putString("catName",work.getName());
                    b.putString("cat_type","category");
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