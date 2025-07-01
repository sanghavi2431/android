package in.woloo.www.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.home_details.HomeDetailsActivity;
import in.woloo.www.home_details.adapters.NearByWolooImageAdapter;
import in.woloo.www.home_details.adapters.PhotosAdapter;
import in.woloo.www.home_details.adapters.SearchedPhotosAdapter;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EqualSpacingItemDecoration;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Utility;

public class WolooSearchAdapter extends RecyclerView.Adapter<WolooSearchAdapter.ViewHolder> {

    private Context context;
    private List<NearByStoreResponse.Data> nearByStoreResponseList;
    public Location lastKnownLocation;
    public String keyword = "";

    public WolooSearchAdapter(Context context, List<NearByStoreResponse.Data> nearByStoreResponseList, Location lastKnownLocation) {
        this.context = context;
        this.nearByStoreResponseList = nearByStoreResponseList;
        this.lastKnownLocation = lastKnownLocation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.woloo_search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(nearByStoreResponseList.get(position));
    }

    @Override
    public int getItemCount() {
        return nearByStoreResponseList.size();
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

        @BindView(R.id.ivTransportMode)
        ImageView ivTransportMode;

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

        @BindView(R.id.ivSafeSpace)
        ImageView ivSafeSpace;

        @BindView(R.id.ivCleanHygiene)
        ImageView ivCleanHygiene;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.llParentLayout)
        LinearLayout llParentLayout;

        @BindView(R.id.iv_banner_recycle)
        RecyclerView iv_banner_recycle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(NearByStoreResponse.Data data) {
            try {
                if (data != null) {

                    SharedPreference mSharedPreference = new SharedPreference(context);
                    String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
                    switch (transport_mode) {
                        case "0":
                            ivTransportMode.setImageResource(R.drawable.ic_car);
                            break;
                        case "1":
                            ivTransportMode.setImageResource(R.drawable.ic_walking_transport_mode);
                            break;
                        case "2":
                            ivTransportMode.setImageResource(R.drawable.ic_bicycle_transport_mode);
                            break;
                    }

                    if (data.getIsPremium() != null && data.getIsPremium() == 1) {
                        tvPremium.setVisibility(View.VISIBLE);
                    } else {
                        tvPremium.setVisibility(View.GONE);
                    }

                    /*if(data.getImage().size()>0){
                        ImageUtil.loadImage(context,ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+data.getImage().get(0));
                    }else{
                        String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
                        ImageUtil.loadImage(context,ivWolooStore,imgUrl);
                    }*/
                    List<String> images = new ArrayList<>();
                    if (data.getOffer() != null) {
                        //setOffers(data);
                        images.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + data.getOffer().getImage());
                    }
                    if (data.getImage() != null && !data.getImage().isEmpty()) {
                        for (String imageName : data.getImage()) {
                            images.add(data.getBaseUrl() + imageName);
                        }
                    }
                    setImageData(images);

                    if (!TextUtils.isEmpty(data.getName())) {
                        tvWolooStoreName.setText(data.getName());
                    }

                    if (!TextUtils.isEmpty(data.getAddress())) {
                        tvAddress.setText(data.getAddress());
                    }

                    if (data.getDistance() != null) {
                        tvDistance.setText(data.getDistance());
                    }

                    if (data.getDuration() != null) {
                        tvRequiredTime.setText(data.getDuration());
                    }
                    tvRating.setText("" + data.getUserRating());

                    if (data.getIsWashroom() != null && data.getIsWashroom() == 1) {
                        ivToilet.setVisibility(View.VISIBLE);
                    } else {
                        ivToilet.setVisibility(View.GONE);
                    }


                    if (data.getIsWheelchairAccessible() != null && data.getIsWheelchairAccessible() == 1) {
                        ivWheelChair.setVisibility(View.VISIBLE);
                    } else {
                        ivWheelChair.setVisibility(View.GONE);
                    }

                    if (data.getIsFeedingRoom() != null && data.getIsFeedingRoom() == 1) {
                        ivFeedingRoom.setVisibility(View.VISIBLE);
                    } else {
                        ivFeedingRoom.setVisibility(View.GONE);
                    }

                    if (data.getIsSanitizerAvailable() != null && data.getIsSanitizerAvailable() == 1) {
                        ivSanitizer.setVisibility(View.VISIBLE);
                    } else {
                        ivSanitizer.setVisibility(View.GONE);
                    }

                    if (data.getIsCoffeeAvailable() != null && data.getIsCoffeeAvailable() == 1) {
                        ivCoffee.setVisibility(View.VISIBLE);
                    } else {
                        ivCoffee.setVisibility(View.GONE);
                    }

                    if (data.getIsMakeupRoomAvailable() != null && data.getIsMakeupRoomAvailable() == 1) {
                        ivMakeupRoom.setVisibility(View.VISIBLE);
                    } else {
                        ivMakeupRoom.setVisibility(View.GONE);
                    }

                    if (data.getIsSanitaryPadsAvailable() != null && data.getIsSanitaryPadsAvailable() == 1) {
                        ivSanitaryPads.setVisibility(View.VISIBLE);
                    } else {
                        ivSanitaryPads.setVisibility(View.GONE);
                    }

                    if (data.getIsCovidFree() != null && data.getIsCovidFree() == 1) {
                        ivCovidFree.setVisibility(View.VISIBLE);
                    } else {
                        ivCovidFree.setVisibility(View.GONE);
                    }

                    if (data.getIsSafeSpace() != null && data.getIsSafeSpace() == 1) {
                        ivSafeSpace.setVisibility(View.VISIBLE);
                    } else {
                        ivSafeSpace.setVisibility(View.GONE);
                    }

                    if (data.getIsCleanAndHygiene() != null && data.getIsCleanAndHygiene() == 1) {
                        ivCleanHygiene.setVisibility(View.VISIBLE);
                    } else {
                        ivCleanHygiene.setVisibility(View.GONE);
                    }

                    llParentLayout.setOnClickListener(v -> {
                        try {
                            WolooApplication.getInstance().setNearByWoloo(data);
                            Intent intent = new Intent(context, HomeDetailsActivity.class);
                            intent.putExtra(AppConstants.FROM_SEARCH, false);
                            context.startActivity(intent);
                            Bundle bundle = new Bundle();
                            HashMap<String,Object> payload = new HashMap<>();
                            try {
                                if (lastKnownLocation != null) {
                                    bundle.putString(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                                    payload.put(AppConstants.LOCATION, "(" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
                                }
                            } catch (Exception ex) {

                            }
                            bundle.putString(AppConstants.SEARCH_KEYWORD, keyword);
                            bundle.putString(AppConstants.HOST_CLICKED_ID, String.valueOf(data.getId()));
                            bundle.putString(AppConstants.HOST_CLICKED_LOCATION, "(" + data.getLat() + "," + data.getLng() + ")");
                            Utility.logFirebaseEvent(context, bundle, AppConstants.SEARCHED_WOLOO_CLICK);


                            payload.put(AppConstants.SEARCH_KEYWORD, keyword);
                            payload.put(AppConstants.HOST_CLICKED_ID, String.valueOf(data.getId()));
                            payload.put(AppConstants.HOST_CLICKED_LOCATION, "(" + data.getLat() + "," + data.getLng() + ")");
                            Utility.logNetcoreEvent(context,payload,AppConstants.SEARCHED_WOLOO_CLICK);
                        } catch (Exception ex) {
                             CommonUtils.printStackTrace(ex);
                        }
                    });

                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }

        private void setOffers(NearByStoreResponse.Data nearByWoloo) {
            List<NearByStoreResponse.Data.Offer> offerList = new ArrayList<NearByStoreResponse.Data.Offer>();
            offerList.add(nearByWoloo.getOffer());
            PhotosAdapter adapter = new PhotosAdapter(context, offerList);

        }

        /*rvPhotos.setHasFixedSize(true);
                    rvPhotos.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvPhotos.setAdapter(adapter);*/
        private void setImageData(List<String> image) {
            /*if (image == null)
                image = new ArrayList<>();
            else
                for (int i = 0; i < image.size(); i++) {
                    if (!image.get(i).contains(BuildConfig.BASE_URL)) {
                        image.set(i, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image.get(i));
                    }
                }
            for (int i = image.size(); i < 5; i++) {
                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
            }*/
            if (image != null && image.isEmpty()) {
                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
            }
            iv_banner_recycle.setVisibility(View.VISIBLE);
            NearByWolooImageAdapter nearByWolooImageAdapter = new NearByWolooImageAdapter((Activity) context, image);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            iv_banner_recycle.setLayoutManager(mLayoutManager);
            iv_banner_recycle.setAdapter(nearByWolooImageAdapter);
            iv_banner_recycle.setNestedScrollingEnabled(true);
            ViewCompat.setNestedScrollingEnabled(iv_banner_recycle, true);
            iv_banner_recycle.setHasFixedSize(true);
            iv_banner_recycle.setItemViewCacheSize(20);
            iv_banner_recycle.setDrawingCacheEnabled(true);
            iv_banner_recycle.addItemDecoration(new EqualSpacingItemDecoration(dpToPx(2), EqualSpacingItemDecoration.HORIZONTAL)); // 16px. In practice, you'll want to use getDimensionPixelSize
            iv_banner_recycle.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        }

        public int dpToPx(int dp) {
            Resources r = context.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
    }


}
