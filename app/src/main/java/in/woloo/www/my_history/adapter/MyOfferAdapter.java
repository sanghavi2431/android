package in.woloo.www.my_history.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.my_history.model.MyHistoryResponse;
import in.woloo.www.my_history.model.MyOffersResponse;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;

public class MyOfferAdapter extends RecyclerView.Adapter<MyOfferAdapter.ViewHolder> {

    private Context context;
    private List<NearByStoreResponse.Data> offersList;

    public MyOfferAdapter(Context context, List<NearByStoreResponse.Data> offerList) {
        this.context = context;
        this.offersList = offerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.my_history_item, parent, false);//woloo_search_item
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(offersList.get(position));
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPremium)
        TextView tvPremium;

        @BindView(R.id.ivWolooStore)
        ImageView ivWolooStore;

        @BindView(R.id.tvWolooStoreName)
        TextView tvWolooStoreName;

        @BindView(R.id.tvRequiredTime)
        TextView tvRequiredTime;

        @BindView(R.id.tvRating)
        TextView tvRating;

        @BindView(R.id.ivToilet)
        ImageView ivToilet;

        @BindView(R.id.ivWheelChair)
        ImageView ivWheelChair;

        @BindView(R.id.ivFeedingRoom)
        ImageView ivFeedingRoom;

        @BindView(R.id.ivSanitizer)
        ImageView ivSanitizer;

        @BindView(R.id.ivCoffee)
        ImageView ivCoffee;

        @BindView(R.id.ivMakeupRoom)
        ImageView ivMakeupRoom;

        @BindView(R.id.ivSanitaryPads)
        ImageView ivSanitaryPads;

        @BindView(R.id.ivCovidFree)
        ImageView ivCovidFree;

        @BindView(R.id.ivCleanHygiene)
        ImageView ivCleanHygiene;

        @BindView(R.id.ivSafeSpace)
        ImageView ivSafeSpace;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tvEarnedPoints)
        TextView tvEarnedPoints;

        @BindView(R.id.tvRateThisPlace)
        TextView tvRateThisPlace;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(NearByStoreResponse.Data history) {
            try {
                if (history != null) {
                    if (history.getIsPremium() != null && history.getIsPremium() == 1) {
                        tvPremium.setVisibility(View.VISIBLE);
                    } else {
                        tvPremium.setVisibility(View.GONE);
                    }

                    if(history.getImage()!=null && !history.getImage().isEmpty()){
                        ImageUtil.loadImageHistory(context,ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+history.getImage().get(0));
                    }else{
                        String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                        ImageUtil.loadImageHistory(context,ivWolooStore,imgUrl);
                    }
                    /*
                    String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                    ImageUtil.loadImage(context,ivWolooStore,imgUrl);
                    */

//                    if (history.getOffer() != null && !TextUtils.isEmpty(history.getOffer().getImage())) {
//                        ImageUtil.loadImageHistory(context, ivWolooStore, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + history.getOffer().getImage());
//                    } else if (history.getImage() != null && !history.getImage().isEmpty()) {
//                        ImageUtil.loadImageHistory(context, ivWolooStore, history.getBaseUrl() + history.getImage().get(0));
//                    } else {
//                        String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
//                        ImageUtil.loadImageHistory(context, ivWolooStore, imgUrl);
//                    }

                    if (!TextUtils.isEmpty(history.getName())) {
                        tvWolooStoreName.setText(history.getName());
                    }

                    if (!TextUtils.isEmpty(history.getAddress())) {
                        tvAddress.setText(history.getAddress());
                    }

                    if (!TextUtils.isEmpty(history.getUserRating())) {
                        tvRating.setText(history.getUserRating());
                    }


                    if (history.getDistance() != null) {
                        tvDistance.setText("" + CommonUtils.getDistace(history.getDistance()));
                    }

                    if (history.getDuration() != null) {
                        tvRequiredTime.setText("" + CommonUtils.getTimeForWolooStoreInfo(history.getDuration()));
                    }

                    tvRating.setText("" + history.getUserRating());

                    if (history.getIsWashroom() != null && history.getIsWashroom() == 1) {
                        ivToilet.setVisibility(View.VISIBLE);
                    } else {
                        ivToilet.setVisibility(View.GONE);
                    }

                    if (history.getIsWheelchairAccessible() != null && history.getIsWheelchairAccessible() == 1) {
                        ivWheelChair.setVisibility(View.VISIBLE);
                    } else {
                        ivWheelChair.setVisibility(View.GONE);
                    }

                    if (history.getIsFeedingRoom() != null && history.getIsFeedingRoom() == 1) {
                        ivFeedingRoom.setVisibility(View.VISIBLE);
                    } else {
                        ivFeedingRoom.setVisibility(View.GONE);
                    }

                    if (history.getIsSanitizerAvailable() != null && history.getIsSanitizerAvailable() == 1) {
                        ivSanitizer.setVisibility(View.VISIBLE);
                    } else {
                        ivSanitizer.setVisibility(View.GONE);
                    }

                    if (history.getIsCoffeeAvailable() != null && history.getIsCoffeeAvailable() == 1) {
                        ivCoffee.setVisibility(View.VISIBLE);
                    } else {
                        ivCoffee.setVisibility(View.GONE);
                    }

                    if (history.getIsMakeupRoomAvailable() != null && history.getIsMakeupRoomAvailable() == 1) {
                        ivMakeupRoom.setVisibility(View.VISIBLE);
                    } else {
                        ivMakeupRoom.setVisibility(View.GONE);
                    }

                    if (history.getIsSanitaryPadsAvailable() != null && history.getIsSanitaryPadsAvailable() == 1) {
                        ivSanitaryPads.setVisibility(View.VISIBLE);
                    } else {
                        ivSanitaryPads.setVisibility(View.GONE);
                    }

                    if (history.getIsCovidFree() != null && history.getIsCovidFree() == 1) {
                        ivCovidFree.setVisibility(View.VISIBLE);
                    } else {
                        ivCovidFree.setVisibility(View.GONE);
                    }

                    if (history.getIsSafeSpace() != null && history.getIsSafeSpace() == 1) {
                        ivSafeSpace.setVisibility(View.VISIBLE);
                    } else {
                        ivSafeSpace.setVisibility(View.GONE);
                    }

                    if (history.getIsCleanAndHygiene() != null && history.getIsCleanAndHygiene() == 1) {
                        ivCleanHygiene.setVisibility(View.VISIBLE);
                    } else {
                        ivCleanHygiene.setVisibility(View.GONE);
                    }

                    /*if(!TextUtils.isEmpty(history.getAmount())){
                        tvEarnedPoints.setVisibility(View.VISIBLE);
                        tvEarnedPoints.setText("You have earned "+history.getAmount()+" points");
                    }else{
                        tvEarnedPoints.setVisibility(View.GONE);
                    }*/

                    /*if(history.getIsReviewPending()==1)
                        tvRateThisPlace.setVisibility(View.VISIBLE);
                    else*/
                    tvRateThisPlace.setVisibility(View.GONE);
                    /*tvRateThisPlace.setOnClickListener(v -> {
                        try{
                            Intent intent = new Intent(context, AddReviewActivity.class);
                            intent.putExtra(AppConstants.WOLOO_ID,history.getWolooId());
                            context.startActivity(intent);
                        }catch (Exception ex){
                             CommonUtils.printStackTrace(ex);
                        }
                    });*/

                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }
    }
}
