package in.woloo.www.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;

public class WolooStoreImagesAdapter extends RecyclerView.Adapter<WolooStoreImagesAdapter.ViewHolder> {

    private Context context;
    private List<String> dataList;

    public WolooStoreImagesAdapter(Context context,List<String> dataList) {
        this.context = context;
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_store_info_marker, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Logger.i("onBindViewHolder",""+position);
            holder.setStoreData(dataList.get(position));
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public int getItemCount() {
       /* if (dataList!=null && dataList.size()>0){
            return dataList.size();
        }*/
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_store_offer)
        ImageView image_store_offer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

      private void  setStoreData(String image){
          try {
              //ImageUtil.loadImage(context,image_store_offer,image);
              Glide.with(context)
                      .load(image)
                      .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                      .placeholder(R.drawable.banner_logo)
                      .into(image_store_offer);
          } catch (Exception e) {
               CommonUtils.printStackTrace(e);
          }
      }
    }
}
