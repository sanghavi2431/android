package in.woloo.www.refer_woloo_host.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;

public class ReferredWolooHostListingAdapter extends RecyclerView.Adapter<ReferredWolooHostListingAdapter.ReferredwolooHostHolder> {

    private Context context;
    private List<ReferredWolooListResponse.DataItem> dataItems;

    public ReferredWolooHostListingAdapter(Context context, List<ReferredWolooListResponse.DataItem> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NotNull
    @Override
    public ReferredwolooHostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.referred_woloo_host_listing_details, parent, false);
        return new ReferredwolooHostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReferredwolooHostHolder holder, int position) {
        switch (dataItems.get(position).getStatus()) {
            case 0:
                holder.status_Tv.setText("Under Review");
                break;
            case 1:
                holder.status_Tv.setText("Approved");
                break;
            case 2:
                holder.status_Tv.setText("Rejected");
                break;
            default:
                break;
        }
        holder.hostTitle_Tv.setText(dataItems.get(position).getName());
        holder.hostAddress_Tv.setText(dataItems.get(position).getAddress());
        if(dataItems.get(position).getImage().size()>0){
            ImageUtil.loadImage(context,holder.host_image, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+dataItems.get(position).getImage().get(0));
        }else{
            String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
            ImageUtil.loadImage(context,holder.host_image,imgUrl);
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ReferredwolooHostHolder extends RecyclerView.ViewHolder{
        ImageView host_image;
        TextView status_Tv, hostTitle_Tv, hostAddress_Tv;
        public ReferredwolooHostHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            status_Tv = (TextView) itemView.findViewById(R.id.status_Tv);
            hostTitle_Tv = (TextView) itemView.findViewById(R.id.host_title_Tv);
            hostAddress_Tv = (TextView) itemView.findViewById(R.id.host_address_Tv);
            host_image = (ImageView) itemView.findViewById(R.id.host_imv);
        }
    }
}
