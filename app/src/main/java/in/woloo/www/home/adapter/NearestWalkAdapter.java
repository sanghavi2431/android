package in.woloo.www.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.base.BaseActivity;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.home_details.HomeDetailsActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.enroute.EnrouteDirectionActivity;

public class NearestWalkAdapter extends RecyclerView.Adapter<NearestWalkAdapter.ViewHolder> {

    private Context context;
    private List<NearByStoreResponse.Data> nearByStoreResponseList;

    public static int FIRST_ITEM = 100, NORMAL_ITEM = 200;

    public NearestWalkAdapter(Context context, List<NearByStoreResponse.Data> nearByStoreResponseList) {
        this.context = context;
        this.nearByStoreResponseList = nearByStoreResponseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.nearest_walk_new_items, parent, false);//nearest_walk
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NearByStoreResponse.Data nearByStore = nearByStoreResponseList.get(position);
        holder.setData(nearByStore, context);
    }


    @Override
    public int getItemCount() {
        return nearByStoreResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvRequiredTime)
        TextView tvRequiredTime;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvAddress)
        TextView tvAddress;

        @BindView(R.id.tvDistance)
        TextView tvDistance;

        @BindView(R.id.tv_direction)
        TextView tv_direction;

        @BindView(R.id.ivToilet)
        ImageView ivToilet;

        @BindView(R.id.ivCovidFree)
        ImageView ivCovidFree;

        @BindView(R.id.ivCleanHygiene)
        ImageView ivCleanHygiene;

        @BindView(R.id.ivSafeSpace)
        ImageView ivSafeSpace;

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

        @BindView(R.id.rlParentLayout)
        RelativeLayout rlParentLayout;

        @BindView(R.id.ivTransportMode)
        ImageView ivTransportMode;

        @BindView(R.id.tvCibilScore)
        TextView tvCibilScore;

        @BindView(R.id.tvCibilTitle)
        TextView tvCibilTitle;

        @BindView(R.id.tvCibilScoreCV)
        CardView tvCibilScoreCV;

        protected SharedPreference mSharedPreference;
        private String selectedTravelMode = "car";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(NearByStoreResponse.Data nearByStore, Context context) {
            try {
                tvName.setText(nearByStore.getName());
                tvAddress.setText(nearByStore.getAddress());
                tvDistance.setText(nearByStore.getDistance());
                tvRequiredTime.setText(nearByStore.getDuration());
                if(nearByStore.getCibilScore().equals("0") || nearByStore.getCibilScoreColour().isEmpty()){
                    tvCibilScoreCV.setVisibility(View.GONE);
                    tvCibilTitle.setVisibility(View.GONE);
                }else {
                    tvCibilScoreCV.setVisibility(View.VISIBLE);
                    tvCibilTitle.setVisibility(View.VISIBLE);
                    tvCibilScore.setText(nearByStore.getCibilScore());
                    tvCibilScoreCV.setCardBackgroundColor(Color.parseColor(nearByStore.getCibilScoreColour()));
                }
                tv_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nearByStore.getDistance().equals("-"))
                        {
                            CommonUtils.showCustomDialog(context,"No route found for the transport mode. Please change mode and try again");
                        }
                        else {
                            Bundle params = new Bundle();
                            params.putString(AppConstants.WOLOO_NAME, String.valueOf(nearByStore.getId()));
                            Utility.logFirebaseEvent(context, params, AppConstants.DIRECTION_WOLOO_EVENT);

                            HashMap<String,Object> payload = new HashMap<>();
                            payload.put(AppConstants.WOLOO_NAME, String.valueOf(nearByStore.getId()));
                            Utility.logNetcoreEvent(context,payload,AppConstants.DIRECTION_WOLOO_EVENT);

                            Intent i = new Intent(context, EnrouteDirectionActivity.class);
                            i.putExtra("destlat", nearByStore.getLat());
                            i.putExtra("destlong", nearByStore.getLng());
                            i.putExtra("wolooId", nearByStore.getId());
                            i.putExtra("wolooName", nearByStore.getName());
                            i.putExtra("wolooAddress", nearByStore.getAddress());
                            i.putExtra("tag", "direction");
                            context.startActivity(i);
                        }
                    }
                });
                if (mSharedPreference == null) {
                    mSharedPreference = new SharedPreference(context);
                }
                String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
                switch (transport_mode) {
                    case "0":
                        ivTransportMode.setImageResource(R.drawable.ic_car);
                        selectedTravelMode = "car";
                        break;
                    case "1":
                        ivTransportMode.setImageResource(R.drawable.ic_walking_transport_mode);
                        selectedTravelMode = "walking";
                        break;
                    case "2":
                        ivTransportMode.setImageResource(R.drawable.ic_bicycle_transport_mode);
                        selectedTravelMode = "bicycle";
                        break;
                }

                if (nearByStore.getIsWashroom() == 1) {
                    ivToilet.setVisibility(View.VISIBLE);
                } else {
                    ivToilet.setVisibility(View.GONE);
                }

                if (nearByStore.getIsWheelchairAccessible() == 1) {
                    ivWheelChair.setVisibility(View.VISIBLE);
                } else {
                    ivWheelChair.setVisibility(View.GONE);
                }

                if (nearByStore.getIsFeedingRoom() == 1) {
                    ivFeedingRoom.setVisibility(View.VISIBLE);
                } else {
                    ivFeedingRoom.setVisibility(View.GONE);
                }

                if (nearByStore.getIsSanitizerAvailable() == 1) {
                    ivSanitizer.setVisibility(View.VISIBLE);
                } else {
                    ivSanitizer.setVisibility(View.GONE);
                }

                if (nearByStore.getIsCoffeeAvailable() == 1) {
                    ivCoffee.setVisibility(View.VISIBLE);
                } else {
                    ivCoffee.setVisibility(View.GONE);
                }

                if (nearByStore.getIsMakeupRoomAvailable() == 1) {
                    ivMakeupRoom.setVisibility(View.VISIBLE);
                } else {
                    ivMakeupRoom.setVisibility(View.GONE);
                }

                if (nearByStore.getIsSanitaryPadsAvailable() == 1) {
                    ivSanitaryPads.setVisibility(View.VISIBLE);
                } else {
                    ivSanitaryPads.setVisibility(View.GONE);
                }

                if (nearByStore.getIsCovidFree() == 1) {
                    ivCovidFree.setVisibility(View.VISIBLE);
                } else {
                    ivCovidFree.setVisibility(View.GONE);
                }

                if (nearByStore.getIsSafeSpace() == 1) {
                    ivSafeSpace.setVisibility(View.VISIBLE);
                } else {
                    ivSafeSpace.setVisibility(View.GONE);
                }

                if (nearByStore.getIsCleanAndHygiene() == 1) {
                    ivCleanHygiene.setVisibility(View.VISIBLE);
                } else {
                    ivCleanHygiene.setVisibility(View.GONE);
                }

                rlParentLayout.setOnClickListener(v -> {
                    try {
                        HashMap<String,Object> payload = new HashMap<>();
                        payload.put(AppConstants.LOCATION, SharedPrefSettings.Companion.getGetPreferences().fetchLocationForNetcore());
                        payload.put(AppConstants.TRAVEL_MODE, selectedTravelMode);
                        payload.put(AppConstants.HOST_CLICKED_ID,String.valueOf(nearByStore.getId()));
                        payload.put(AppConstants.HOST_CLICKED_LOCATION, "(" + nearByStore.getLat() + "," + nearByStore.getLng() + ")");
                        Utility.logNetcoreEvent(context,payload,AppConstants.WOLOO_DETAIL_CLICK);

                        WolooApplication.getInstance().setNearByWoloo(nearByStore);
                        context.startActivity(new Intent(context, HomeDetailsActivity.class));
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                });

            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }
    }
}
