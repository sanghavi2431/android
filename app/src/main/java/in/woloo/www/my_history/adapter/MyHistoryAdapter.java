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
import in.woloo.www.my_history.model.MyHistoryResponse;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.review.AddReviewActivity;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.ViewHolder> {

    private Context context;
    private List<MyHistoryResponse.History> historyList;

    public MyHistoryAdapter(Context context, List<MyHistoryResponse.History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.my_history_item, parent, false);//woloo_search_item
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
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
            ButterKnife.bind(this,itemView);
        }

        public void setData(MyHistoryResponse.History history) {
            try{
                if(history != null){
                    Logger.d("Aarati" , "history is " + history.getId());
                    if(history.getWolooDetails().getIsPremium() == null && history.getWolooDetails().getIsPremium() == 1){
                        tvPremium.setVisibility(View.VISIBLE);
                    }else{
                        tvPremium.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getImage()!=null && !history.getWolooDetails().getImage().isEmpty()){
                      //  ImageUtil.loadImageHistory(context,ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+history.getWolooDetails().getImage().get(0));
                    }else{
                        String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                       // ImageUtil.loadImageHistory(context,ivWolooStore,imgUrl);
                    }
                    /*String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                    ImageUtil.loadImage(context,ivWolooStore,imgUrl);*/

                    if(!TextUtils.isEmpty(history.getWolooDetails().getName())){
                        tvWolooStoreName.setText(history.getWolooDetails().getName());
                    }

                    if(!TextUtils.isEmpty(history.getWolooDetails().getAddress())){
                        tvAddress.setText(history.getWolooDetails().getAddress());
                    }

                    if(!TextUtils.isEmpty(history.getWolooDetails().getUserRating())){
                        tvRating.setText(history.getWolooDetails().getUserRating());
                    }

                   /*
                   if(history.getWolooDetails().getDistance() != null){
                        tvDistance.setText(""+ CommonUtils.getDistace(data.getDistance()));
                    }

                    if(data.getDuration() != null){
                        tvRequiredTime.setText(""+CommonUtils.getTimeForWolooStoreInfo(data.getDuration()));
                    }

                    tvRating.setText(""+data.getUserRating());*/

                    if (history.getWolooDetails().getIsWashroom() != null && history.getWolooDetails().getIsWashroom() == 1) {
                        ivToilet.setVisibility(View.VISIBLE);
                    } else {
                        ivToilet.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsWheelchairAccessible() != null && history.getWolooDetails().getIsWheelchairAccessible() == 1){
                        ivWheelChair.setVisibility(View.VISIBLE);
                    }else{
                        ivWheelChair.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsFeedingRoom() != null &&  history.getWolooDetails().getIsFeedingRoom() == 1){
                        ivFeedingRoom.setVisibility(View.VISIBLE);
                    }else{
                        ivFeedingRoom.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSanitizerAvailable() != null && history.getWolooDetails().getIsSanitizerAvailable() == 1){
                        ivSanitizer.setVisibility(View.VISIBLE);
                    }else{
                        ivSanitizer.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCoffeeAvailable() != null && history.getWolooDetails().getIsCoffeeAvailable() == 1){
                        ivCoffee.setVisibility(View.VISIBLE);
                    }else{
                        ivCoffee.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsMakeupRoomAvailable() != null && history.getWolooDetails().getIsMakeupRoomAvailable() == 1){
                        ivMakeupRoom.setVisibility(View.VISIBLE);
                    }else{
                        ivMakeupRoom.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSanitaryPadsAvailable() != null && history.getWolooDetails().getIsSanitaryPadsAvailable() == 1){
                        ivSanitaryPads.setVisibility(View.VISIBLE);
                    }else{
                        ivSanitaryPads.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCovidFree() != null && history.getWolooDetails().getIsCovidFree() == 1){
                        ivCovidFree.setVisibility(View.VISIBLE);
                    }else{
                        ivCovidFree.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSafeSpace() != null && history.getWolooDetails().getIsSafeSpace() == 1){
                        ivSafeSpace.setVisibility(View.VISIBLE);
                    }else{
                        ivSafeSpace.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCleanAndHygiene() != null && history.getWolooDetails().getIsCleanAndHygiene() == 1){
                        ivCleanHygiene.setVisibility(View.VISIBLE);
                    }else{
                        ivCleanHygiene.setVisibility(View.GONE);
                    }

                    if(!TextUtils.isEmpty(history.getAmount())){
                        tvEarnedPoints.setVisibility(View.VISIBLE);
                        tvEarnedPoints.setText("You have earned "+history.getAmount()+" points");
                    }else{
                        tvEarnedPoints.setVisibility(View.GONE);
                    }

                    if(history.getIsReviewPending()==1)
                        tvRateThisPlace.setVisibility(View.VISIBLE);
                    else
                        tvRateThisPlace.setVisibility(View.GONE);
                    tvRateThisPlace.setOnClickListener(v -> {
                        try{
                            Intent intent = new Intent(context, AddReviewActivity.class);
                            intent.putExtra(AppConstants.WOLOO_ID,history.getWolooId());
                            context.startActivity(intent);
                        }catch (Exception ex){
                             CommonUtils.printStackTrace(ex);
                        }
                    });

                }
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }
    }
}
