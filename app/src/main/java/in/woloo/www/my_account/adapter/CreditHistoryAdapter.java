package in.woloo.www.my_account.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.UserCoinHistoryModel;
import in.woloo.www.my_account.GiftCardDetailActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Utility;


public class CreditHistoryAdapter extends RecyclerView.Adapter<CreditHistoryAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    ArrayList<UserCoinHistoryModel.Data.HistoryItem> coin_History;
    String previous_History_Date = "";

    public CreditHistoryAdapter(Context context, ArrayList<UserCoinHistoryModel.Data.HistoryItem> coin_History) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.coin_History = coin_History;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.credit_history_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        setData(viewHolder, position);

    }

    private void setData(ViewHolder viewHolder, int position) {
        try {

            if (coin_History.get(position).getType().equals(AppConstants.NAVIGATION_REWARDS)) {
                if(coin_History.get(position).getWolooDetails() !=null){
                    viewHolder.tvCreditMsg.setText("Woloo Navigation used to reach Woloo Host " + coin_History.get(position).getWolooDetails().getName());
                }else {
                    viewHolder.tvCreditMsg.setText("Woloo Navigation used to reach Woloo Host ");
                }
                Glide.with(context)
                        .load(R.drawable.woloo_navigation_reward_credit_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.REGISTRATION_POINTS)) {
                viewHolder.tvCreditMsg.setText("completing your registration process");
                Glide.with(context)
                        .load(R.drawable.registration_point_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.GIFT_RECEIVED)) {
                Glide.with(context)
                        .load(R.drawable.gift_received_icon)
                        .into(viewHolder.ivWolooStore);
                if(coin_History.get(position).getSender() !=null){
                    viewHolder.tvCreditMsg.setText("Received Gift from " + coin_History.get(position).getSender().getName() + " - " + coin_History.get(position).getSender().getMobile());
                }else {
                    viewHolder.tvCreditMsg.setText("Gift Received ");
                }
            } else if (coin_History.get(position).getType().equals(AppConstants.GIFT_SENT)) {
                if(coin_History.get(position).getSender() != null){
                    viewHolder.tvCreditMsg.setText("Purchase of Woloo Gift Card for " + coin_History.get(position).getSender().getName() + "-" + coin_History.get(position).getSender().getMobile());
                }else {
                    viewHolder.tvCreditMsg.setText("Woloo Gift Card Sent");
                }
                Glide.with(context)
                        .load(R.drawable.gift_sent_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.REFERAL_POINT)) {
                if (coin_History.get(position).getSender() != null) {
                    viewHolder.tvCreditMsg.setText("Successful Referral made " + coin_History.get(position).getSender().getMobile());
                } else {
                    viewHolder.tvCreditMsg.setText("Successful Referral made");
                }
                Glide.with(context)
                        .load(R.drawable.referral_point_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.ADD_COINS)) {
                viewHolder.tvCreditMsg.setText("Purchase of Coins " + coin_History.get(position).getValue());
                Glide.with(context)
                        .load(R.drawable.add_coins_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.NO_WOLOO_FOUND_REWARD)) {
                if (coin_History.get(position).getWolooDetails() != null)
                    viewHolder.tvCreditMsg.setText("Enjoy Woloo Points till we get Woloo at your searched location " + coin_History.get(position).getWolooDetails().getAddress());
                else
                    viewHolder.tvCreditMsg.setText("Enjoy Woloo Points till we get Woloo at your searched location");
                Glide.with(context)
                        .load(R.drawable.no_woloo_found_reward)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.RECOMMEND_WOLOO_CREDITS)) {
                if(coin_History.get(position).getWolooDetails() !=null){
                    viewHolder.tvCreditMsg.setText("Recommended " + coin_History.get(position).getWolooDetails().getName() + " to be included as a Woloo Host");
                }else {
                    viewHolder.tvCreditMsg.setText("Recommended  to be included as a Woloo Host");
                }
                Glide.with(context)
                        .load(R.drawable.woloo_credit_icon)
                        .into(viewHolder.ivWolooStore);
            } else if (coin_History.get(position).getType().equals(AppConstants.APPROVED_RECOMMEND_WOLOO_CREDITS)) {
                if(coin_History.get(position).getWolooDetails() !=null){
                    viewHolder.tvCreditMsg.setText("Recommended Woloo Host " + coin_History.get(position).getWolooDetails().getName() + "approved by Woloo");
                }else {
                    viewHolder.tvCreditMsg.setText("Recommended Woloo Host approved by Woloo");
                }
                Glide.with(context)
                        .load(R.drawable.approve_recommend_woloo_credit_icon)
                        .into(viewHolder.ivWolooStore);
            }else if (coin_History.get(position).getType().equals(AppConstants.USING_WOLOO_SERVICE_AT_PARTICULAR_HOST)) {
                viewHolder.tvCreditMsg.setText("Woloo Service used at " + coin_History.get(position).getWolooDetails().getName() +" - "+coin_History.get(position).getWolooDetails().getAddress());
                Glide.with(context)
                        .load(R.drawable.using_woloo_at_particular_host_icon)
                        .into(viewHolder.ivWolooStore);
            } else if(coin_History.get(position).getType().equals(AppConstants.ECOM_GIFT_DEBIT)) {
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
                Glide.with(context)
                        .load(R.drawable.gift_sent_icon)
                        .into(viewHolder.ivWolooStore);
            }else if(coin_History.get(position).getType().equals(AppConstants.ECOM_POINTS_DEBIT)) {
                Glide.with(context)
                        .load(R.drawable.add_coins_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }
            else if(coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_SENT)) {
                Glide.with(context)
                        .load(R.drawable.gift_sent_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }else if(coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_RECEIVED)) {
                Glide.with(context)
                        .load(R.drawable.gift_received_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }
            else if(coin_History.get(position).getType().equals(AppConstants.PURCHASE_MEMBER)) {
                Glide.with(context)
                        .load(R.drawable.add_coins_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }
            else if(coin_History.get(position).getType().equals(AppConstants.BLOG_READ_POINT)) {
                Glide.with(context)
                        .load(R.drawable.blog_read_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }else if(coin_History.get(position).getType().equals(AppConstants.GIFT_POINTS_DEDUCTED)){
                Glide.with(context)
                        .load(R.drawable.gift_points_deducted)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }
            else if(coin_History.get(position).getType().equals(AppConstants.WAH_CERTIFICATE_POINT)){
                Glide.with(context)
                        .load(R.drawable.add_coins_icon)
                        .into(viewHolder.ivWolooStore);
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getRemarks());
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            }
            else{
                viewHolder.tvCreditMsg.setText(coin_History.get(position).getType());
                String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                ImageUtil.loadImageProfileAccount(context, viewHolder.ivWolooStore, imgUrl);
            }

            if (!TextUtils.isEmpty(coin_History.get(position).getValue())) {
                if (coin_History.get(position).getIsGift() == 1)
                    viewHolder.tvPoints.setText("\u20B9 "+coin_History.get(position).getValue());
                else
                    viewHolder.tvPoints.setText(coin_History.get(position).getValue() + "\nPoints");
            }

            String updated_at = coin_History.get(position).getUpdatedAt();
            String createdAt = coin_History.get(position).getCreatedAt();

            if (updated_at != null) {
                viewHolder.tv_updated_at.setText(CommonUtils.geCreditHistoryDate(updated_at, false));
            }

            if (createdAt != null) {
                if (previous_History_Date.equals(CommonUtils.geCreditHistoryDate(createdAt, true)))
                    viewHolder.tv_created_at.setVisibility(View.GONE);
                else {
                    previous_History_Date = CommonUtils.geCreditHistoryDate(createdAt, true);
                    viewHolder.tv_created_at.setVisibility(View.VISIBLE);
                }
                viewHolder.tv_created_at.setText(CommonUtils.geCreditHistoryDate(createdAt, true));
            }

            /*if(coin_History.get(position).getWolooDetails() != null && !TextUtils.isEmpty(coin_History.get(position).getWolooDetails().getImage())){
                ImageUtil.loadImageProfileAccount(context,viewHolder.ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+coin_History.get(position).getWolooDetails().getImage());
            }else{
                String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE;
                ImageUtil.loadImageProfileAccount(context,viewHolder.ivWolooStore,imgUrl);
            }*/


            if (!(coin_History.get(position).getSender()==null) && (coin_History.get(position).getType().equals(AppConstants.GIFT_RECEIVED) || coin_History.get(position).getType().equals(AppConstants.GIFT_SENT)
                    || coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_SENT) || coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_RECEIVED))) {
                viewHolder.tvCreditMsg.setOnClickListener(view -> {
                    Bundle firebaseBundle = new Bundle();
                    firebaseBundle.putString(AppConstants.POINTS_ID, String.valueOf(coin_History.get(position).getId()));
                    Utility.logFirebaseEvent(context,firebaseBundle,AppConstants.POINT_DETAILS_CLICK);

                    HashMap<String,Object> payload = new HashMap<>();
                    payload.put(AppConstants.POINTS_ID, String.valueOf(coin_History.get(position).getId()));
                    Utility.logNetcoreEvent(context,payload,AppConstants.POINT_DETAILS_CLICK);

                    Intent intent = new Intent(context, GiftCardDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", coin_History.get(position));
                    if(coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_SENT))
                        intent.putExtra("giftMemberSent","");
                    if(coin_History.get(position).getType().equals(AppConstants.GIFT_SUB_RECEIVED))
                        intent.putExtra("giftMemberReceived","");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                });
            }

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public int getItemCount() {
        return coin_History.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_remarks)
        TextView tv_remarks;

        @BindView(R.id.tv_updated_at)
        TextView tv_updated_at;

        @BindView(R.id.tv_created_at)
        TextView tv_created_at;

        @BindView(R.id.tvCreditMsg)
        TextView tvCreditMsg;

        @BindView(R.id.tvPoints)
        TextView tvPoints;

        @BindView(R.id.ivWolooStore)
        ImageView ivWolooStore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
