package in.woloo.www.home_details.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;

public class SearchedPhotosAdapter extends RecyclerView.Adapter<SearchedPhotosAdapter.ViewHolder> {

    private Context context;
    private List<SearchWolooResponse.Data.Offer> offerList;

    public SearchedPhotosAdapter(Context context, List<SearchWolooResponse.Data.Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.photos_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           holder.ivPhoto.setClipToOutline(true);
           try{
               if(!TextUtils.isEmpty(offerList.get(position).getImage())){
                   ImageUtil.loadImage(context,holder.ivPhoto, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+offerList.get(position).getImage());
               }
           }catch (Exception ex){
                CommonUtils.printStackTrace(ex);
           }

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
