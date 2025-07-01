package in.woloo.www.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.home_details.adapters.NearByWolooImageAdapter;
import in.woloo.www.mapdirection.GetDistance;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EqualSpacingItemDecoration;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.enroute.EnrouteDirectionActivity;
import in.woloo.www.v2.home.model.WolooEngagementRequest;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.splash.UserDetails;

public class WolooStoreAdapter extends RecyclerView.Adapter<WolooStoreAdapter.ViewHolder> {

    String TAG = "WolooStoreAdapter";
    private Context context;
    private List<NearByStoreResponse.Data> dataList;

    private int buttonClick = 0;

    private HomeViewModel homeViewModel;
    private String distanceroad;
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private GetDistance getdistance;
    private String duration;
//    GpsTracker gpsTracker ;
    private int wolooSelectedIndex = -1;
    WolooEngagementRequest wolooEngagementRequest = new WolooEngagementRequest();


    public WolooStoreAdapter(Context context, List<NearByStoreResponse.Data> dataList, HomeViewModel homeViewModel) {
        this.context = context;
        this.dataList = dataList;
        this.homeViewModel = homeViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.woloo_store_images_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Logger.i("onBindViewHolder", "" + position);
        holder.setStoreData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (dataList != null && dataList.size() > 0) {
            return dataList.size();
        }
        return 0;
    }

    public int getWolooSelectedIndex(){
        return wolooSelectedIndex;
    }

    public WolooEngagementRequest getWolooEngagementRequest(){
        return wolooEngagementRequest;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.txt_address)
        TextView txt_address;

        @BindView(R.id.tv_direction)
        TextView tv_direction;

        @BindView(R.id.tv_start)
        TextView tv_start;


        @BindView(R.id.tv_distance)
        TextView tv_distance;


        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_like)
        TextView tv_like;

        @BindView(R.id.tvShare)
        TextView tvShare;

        @BindView(R.id.ivTransportMode)
        ImageView ivTransportMode;

        @BindView(R.id.rv_store_image)
        RecyclerView rv_store_image;

        @BindView(R.id.ll_bottom)
        LinearLayout ll_bottom;

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

        @BindView(R.id.ivSegregatedWashroom)
        ImageView ivSegregatedWashroom;

        @BindView(R.id.cibil_image)
        ImageView cibilImage;

        @BindView(R.id.cibil_layout)
        View cibilLayout;
/*
        @BindViews({R.id.iv_covid_free, R.id.ivCleanHygiene, R.id.iv_mom_feeding_baby, R.id.iv_hand_sanitizer, R.id.iv_coffee, R.id.iv_makeup, R.id.iv_diaper})
        List<ImageView> icon_views;
*/


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /*calling setLike*/
        public void setLike(TextView tv_like){
            Logger.i(TAG, "setLike");
            try{
                tv_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.details_layer_list_liked, 0, 0, 0);
                tv_like.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_rectangle) );
                tv_like.setTextColor(ContextCompat.getColor(context, R.color.text_color_five));
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }
        /*calling setDislike*/
        public void setDislike(TextView tv_like){
            Logger.i(TAG, "setDislike");
            try{
                tv_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.details_like_layer_list, 0, 0, 0);
                tv_like.setBackground(ContextCompat.getDrawable(context, R.drawable.transparent_rectangle) );
                tv_like.setTextColor(ContextCompat.getColor(context, R.color.white));
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }

        private void setStoreData(NearByStoreResponse.Data data, int position) {
            try {
                tv_name.setText(data.getName());
                txt_address.setText(data.getAddress());
                if(data.getCibilScoreImage().isEmpty()){
                    cibilLayout.setVisibility(View.GONE);
                }else {
                    cibilLayout.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(data.getCibilScoreImage())
                            .into(cibilImage);
                }
                if(data.getIsLiked() == 0){
                    setDislike(tv_like);
                }else{
                    setLike(tv_like);
                }
                String image = "";
                List<String> stringList = new ArrayList<>();
                if (data.getOffer() != null) {
                    image = data.getOffer().getImage();
                    if (!TextUtils.isEmpty(image)) {
                        String wolooOfferImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES  + image;
                        stringList.add(wolooOfferImage);
                    } else if (data.getImage().size() > 0) {
                        String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + data.getImage().get(0);
                        stringList.add(wolooImage);
                    } else {
                        String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
                        stringList.add(imgUrl);
                    }
                    WolooStoreImagesAdapter adapter = new WolooStoreImagesAdapter(context, stringList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                    rv_store_image.setLayoutManager(layoutManager);
                    rv_store_image.setAdapter(adapter);
                } else {/*if (data.getImage().size()>0) {
                    String wolooImage = BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+data.getImage().get(0);
                    stringList.add(wolooImage);
                }else{
                    String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
                    stringList.add(imgUrl);
                }*/
                    setImageData(data.getImage(), data.getBaseUrl());
                }

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

                tv_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(data.getDistance().equals("-"))
                        {
                            CommonUtils.showCustomDialog(context,"No route found for the transport mode. Please change mode and try again");
                        }
                        else {
                            Bundle params = new Bundle();
                            params.putString(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                            Utility.logFirebaseEvent(context, params, AppConstants.DIRECTION_WOLOO_EVENT);

                            HashMap<String,Object> payload = new HashMap<>();
                            payload.put(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                            Utility.logNetcoreEvent(context,payload,AppConstants.DIRECTION_WOLOO_EVENT);

                            Intent i = new Intent(context, EnrouteDirectionActivity.class);
                            i.putExtra("destlat", data.getLat());
                            i.putExtra("destlong", data.getLng());
                            i.putExtra("wolooId", data.getId());
                            i.putExtra("tag", "direction");
                            i.putExtra("wolooName", data.getName());
                            i.putExtra("wolooAddress", data.getAddress());
                            context.startActivity(i);
//                            String lat = data.getLat(),lng = data.getLng(), mode = "";
//
//
//                            String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
//                            switch (transport_mode) {
//                                case "0":
//                                    mode = "d";
//                                    break;
//                                case "1":
//                                    mode = "w";
//                                    break;
//                                case "2":
//                                    mode = "l"; //b for bycycler & l for two wheeler
//                                    break;
//                            }
//                            // Create a Uri from an intent string. Use the result to create an Intent.
//                            String request = "google.navigation:q="+lat+","+lng+"&mode="+mode;
//                            Uri mapIntentUri = Uri.parse(request);
//// Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
//                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                            mapIntent.setPackage("com.google.android.apps.maps");
//// Attempt to start an activity that can handle the Intent
//                            context.startActivity(mapIntent);
                        }
                    }
                });


//                ll_bottom.setVisibility(View.INVISIBLE);

                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, EnrouteDirectionActivity.class);
                        i.putExtra("destlat", data.getLat());
                        i.putExtra("destlong", data.getLng());
                        i.putExtra("wolooId", data.getId());
                        i.putExtra("tag", "direction");
                        i.putExtra("wolooName", data.getName());
                        i.putExtra("wolooAddress", data.getAddress());
                        context.startActivity(i);
//                        String lat = data.getLat(),lng = data.getLng(), mode = "";
//
//
//                        String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
//                        switch (transport_mode) {
//                            case "0":
//                                mode = "d";
//                                break;
//                            case "1":
//                                mode = "w";
//                                break;
//                            case "2":
//                                mode = "l"; //b for bycycler & l for two wheeler
//                                break;
//                        }
//                        // Create a Uri from an intent string. Use the result to create an Intent.
//                        String request = "google.navigation:q="+lat+","+lng+"&mode="+mode;
//                        Uri mapIntentUri = Uri.parse(request);
//// Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
//                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                        mapIntent.setPackage("com.google.android.apps.maps");
//// Attempt to start an activity that can handle the Intent
//                        context.startActivity(mapIntent);
                    }
                });


                tv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle params = new Bundle();
                        params.putString(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                        Utility.logFirebaseEvent(context,params,AppConstants.LIKE_WOLOO_EVENT);

                        HashMap<String,Object> payload = new HashMap<>();
                        payload.put(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                        Utility.logNetcoreEvent(context,payload,AppConstants.LIKE_WOLOO_EVENT);

                        wolooEngagementRequest.setWolooId(dataList.get(position).getId().toString());
                        UserDetails userInfo = new CommonUtils().getUserInfo();
                        wolooEngagementRequest.setUserId(userInfo.getId().toString());

                        if (buttonClick == 0) {
                            buttonClick = 2;
                            try {
                                wolooSelectedIndex = position;
                                wolooEngagementRequest.setLike(1);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        } else if (buttonClick == 2) {
                            buttonClick = 0;
                            try {
                                wolooSelectedIndex = position;
                                wolooEngagementRequest.setLike(0);
                                homeViewModel.wolooEngagement(wolooEngagementRequest);
//                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOUNLIKE, tv_like);
                            } catch (Exception ex) {
                                 CommonUtils.printStackTrace(ex);
                            }
                        }


                    }
                });
//                gpsTracker=new GpsTracker(context);
                tv_distance.setText(data.getDistance());
                tv_time.setText(data.getDuration());
//                if (gpsTracker.canGetLocation()){
//                    try {
//                        new getdistanceVal(gpsTracker.getLatitude(), gpsTracker.getLongitude(), Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()),tv_distance,tv_time,ll_bottom).execute();
//                    } catch (Exception e) {
//                         CommonUtils.printStackTrace(e);
//                    }
//                }


//                ImageView iv_toilet,iv_physically_disable,iv_mom_feeding_baby,iv_hand_sanitizer,iv_coffee,iv_makeup,iv_diaper;
                /*if (data.getIsSafeSpace().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
                if (data.getIsCovidFree().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
                if (data.getIsCleanAndHygiene().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
*/

                if (data.getIsWashroom() != null && data.getIsWashroom() == 1) {
                    ivToilet.setVisibility(View.VISIBLE);
                    ivToilet.setImageResource(R.drawable.ic_toilet);
                } else {
                    ivToilet.setVisibility(View.VISIBLE);
                    ivToilet.setImageResource(R.drawable.ic_indian_toilet);
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

                if (data.getSegregated() != null && data.getSegregated().equalsIgnoreCase("YES")) {
                    ivSegregatedWashroom.setVisibility(View.VISIBLE);
                    ivSegregatedWashroom.setImageResource(R.drawable.ic_unisex_toilet);
                } else {
                    ivSegregatedWashroom.setVisibility(View.VISIBLE);
                    ivSegregatedWashroom.setImageResource(R.drawable.ic_separate_toilet);
                }

                tvShare.setOnClickListener(v -> {
                    Bundle params = new Bundle();
                    params.putString(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                    Utility.logFirebaseEvent(context,params,AppConstants.SHARE_WOLOO_EVENT);

                    HashMap<String,Object> payload = new HashMap<>();
                    payload.put(AppConstants.WOLOO_NAME, String.valueOf(data.getId()));
                    Utility.logNetcoreEvent(context,payload,AppConstants.SHARE_WOLOO_EVENT);

                    shareMessage(data);
                });


            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
        }

        private void setImageData(List<String> image, String baseUrl) {
            if(image == null || image.size() == 0){
                image = new ArrayList<>();
                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
            } else {
                for (int i = 0; i < image.size(); i++) {
                    if(!TextUtils.isEmpty(baseUrl)){
                        if (!image.get(i).contains(baseUrl)) {
                            image.set(i, baseUrl + image.get(i));
                        }
                    } else {
                        if (!image.get(i).contains(BuildConfig.BASE_URL)) {
                            image.set(i, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image.get(i));
                        }
                    }
                }
            }
//            if (image == null)
//                image = new ArrayList<>();
//            else
//                for (int i = 0; i < image.size(); i++) {
//                    if(!TextUtils.isEmpty(baseUrl)){
//                        if (!image.get(i).contains(baseUrl)) {
//                            image.set(i, baseUrl + image.get(i));
//                        }
//                    } else {
//                        if (!image.get(i).contains(BuildConfig.BASE_URL)) {
//                            image.set(i, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image.get(i));
//                        }
//                    }
//                }
//            for (int i = image.size(); i < 5; i++) {
//                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
//            }
            rv_store_image.setVisibility(View.VISIBLE);
            NearByWolooImageAdapter nearByWolooImageAdapter = new NearByWolooImageAdapter((Activity) context, image);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rv_store_image.setLayoutManager(mLayoutManager);
            rv_store_image.setAdapter(nearByWolooImageAdapter);
            rv_store_image.setNestedScrollingEnabled(true);
            ViewCompat.setNestedScrollingEnabled(rv_store_image, true);
            rv_store_image.setHasFixedSize(true);
            rv_store_image.setItemViewCacheSize(20);
            rv_store_image.setDrawingCacheEnabled(true);
            rv_store_image.addItemDecoration(new EqualSpacingItemDecoration(dpToPx(2), EqualSpacingItemDecoration.HORIZONTAL)); // 16px. In practice, you'll want to use getDimensionPixelSize
            rv_store_image.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rv_store_image.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            rv.getParent().requestDisallowInterceptTouchEvent(true);

                            break;

                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }

        public int dpToPx(int dp) {
            Resources r = context.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
    }


    public void shareMessage(NearByStoreResponse.Data data) {
        try {
            String message = data.getName() + "\n" + data.getAddress() + "\n" + CommonUtils.authconfig_response(context).getuRLS().getApp_share_url();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(share, "Woloo Share"));

//            Dialog dialog=new Dialog(context);
//            CommonUtils.calldeeplink(context,dialog,"","",CommonUtils.authconfig_response(context).getData().getuRLS().getApp_share_url(),"https://woloo.page.link/share");

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

//    private class getdistanceVal extends AsyncTask<Void, Void, Void> {
//        double sourcelat, sourcelong, destlat, destlong;
//        TextView tv_distance;
//        TextView tv_time;
//        LinearLayout ll_bottom;
//
//        private getdistanceVal(double curlat, double curlon, double latitude, double longitude, TextView tv_distance, TextView tv_time, LinearLayout ll_bottom) {
//            this.sourcelat = curlat;
//            this.sourcelong = curlon;
//            this.destlat = latitude;
//            this.destlong = longitude;
//            this.tv_distance=tv_distance;
//            this.tv_time=tv_time;
//            this.ll_bottom=ll_bottom;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//                distanceroad = getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            try {
//                tv_distance.setText("" + distanceroad);
//                tv_time.setText("" +duration);
//                ll_bottom.setVisibility(View.VISIBLE);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//        }
//
//
//    }
//
//    private String getDistanceOnRoad(double latitude, double longitude,
//                                     double prelatitute, double prelongitude) {
//        String result_in_kms = "";
//        String key = "key=" + context.getResources().getString(R.string.google_maps_key);
//        String url = "https://maps.google.com/maps/api/directions/json?origin="
//                + latitude + "," + longitude + "&destination=" + prelatitute
//                + "," + prelongitude + "&sensor=false&units=metric" + "&" + key;
//        String tag[] = {"text"};
//        HttpResponse response = null;
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpPost httpPost = new HttpPost(url);
//            response = httpClient.execute(httpPost, localContext);
//            InputStream is = response.getEntity().getContent();
//            String result = convertInputStreamToString(is);
//            Logger.e("resultIS", result);
//            Gson gson = new Gson();
//            getdistance = gson.fromJson(result.toString(), GetDistance.class);
//            duration = getdistance.getRoutes().get(0).getLegs().get(0).getDuration().getText();
//            result_in_kms = getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getText();
//
//        } catch (Exception e) {
//              CommonUtils.printStackTrace(e);
//        }
//        return result_in_kms;
//    }


    private static String convertInputStreamToString(InputStream is) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        // Java 1.1
        return result.toString(StandardCharsets.UTF_8.name());

        // Java 10
        // return result.toString(StandardCharsets.UTF_8);

    }
}
