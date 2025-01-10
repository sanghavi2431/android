package in.woloo.www.shopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.shopping.config.Config;
import in.woloo.www.shopping.fragments.ProductDetailsFragment;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.shopping.model.ProductModel;
import in.woloo.www.utils.Utility;

public class SearchAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public Context mycontext;
    public LinearLayout mainLinearLayout;

    public SearchAdapter(Context context, int layoutResourceId, ArrayList data) {
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
             holder.image = (ImageView) row.findViewById(R.id.image);
            holder.mainLinearLayout=(LinearLayout)row.findViewById(R.id.mainLinearLayout);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        final ProductModel item = (ProductModel) data.get(position);
        holder.imageTitle.setText(item.title);
        holder.priceTextView.setText("Rs. "+ item.price);

        //   holder.image.setImageBitmap(item.getImage());

        String imageUri = Config.hostname + "../images/" + item.image;
        if(item.image.contains("http"))
        {
            imageUri = item.image;
        }



        Picasso.get().load(imageUri).into(holder.image);


              holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      try{

                          Bundle bundle = new Bundle();
                          bundle.putString("product_name", item.title);
                          bundle.putString("pincode", ShoppingFragment.pincode);
                          Utility.logFirebaseEvent(((WolooDashboard)context), bundle, "shopping_search_product");


                          ProductDetailsFragment myFragment = new ProductDetailsFragment();
                          Bundle b = new Bundle();
                          b.putString("proId", item.id);
                          b.putString("proName", item.title);
                          b.putString("proPrice", item.price);
                          b.putString("proDesc", item.desc);
                          b.putString("from","search_adapter");
                          myFragment.setArguments(b);


                          FragmentManager fragmentManager = ((WolooDashboard)context).getSupportFragmentManager();
                          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                          fragmentTransaction.replace(R.id.frm_contant, myFragment,"");
                          fragmentTransaction.addToBackStack(null);
                          fragmentTransaction.commit();
                      }catch (Exception ex){
                           CommonUtils.printStackTrace(ex);
                      }


                      /*


                      Intent i=new Intent(context,ProductDetailsActivity.class);
                      i.putExtra("title",item.getTitle());
                      i.putExtra("price",item.getPrice());
                      i.putExtra("qty",item.getQty());

                      i.putExtra("sub_cat_id",item.getId());
                      i.putExtra("from","product_adapter");

                      context.startActivity(i);


                       */


                  }
              });


     /*   DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        float dpWidth = pxWidth / displayMetrics.density;
        int pxHeight = displayMetrics.heightPixels;
        float dpHeight = pxHeight / displayMetrics.density;


         holder.image.getLayoutParams().height = (pxHeight * 35/100);
*/






        return row;
    }

    static class ViewHolder {
        TextView imageTitle , priceTextView;
        ImageView image;
        public LinearLayout mainLinearLayout;
    }
}