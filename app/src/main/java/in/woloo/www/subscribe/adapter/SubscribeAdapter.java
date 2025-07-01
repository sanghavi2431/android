package in.woloo.www.subscribe.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.editprofile.EditProfileActivity;
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.subscribe.models.InitSubscriptionResponse;
import in.woloo.www.subscribe.models.PlanResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.subscription.model.InitSubscriptionRequest;
import in.woloo.www.v2.subscription.viewmodel.SubscriptionViewModel;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {

    private static final String TAG = SubscribeAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<PlanResponse.Data> subscriptionArrayList;
    private ArrayList<PlanResponse.Data> selectedSubscriptionArrayList;
//    private SubscribePresenter subscribePresenter;
    private SubscriptionViewModel viewModel;
    private UserProfile viewProfileResponse;
    private String currentSubscriptionPlan;
    private String futureSubscriptionPlan;
    private String msg;
    private boolean isEmail;
    private String mobile;
    private int selectedPosition = 0;
    private SubscribeFragment subscribeFragment;
    private String planId;
    private Integer id;
    private boolean expanded = false;
    private SimpleDateFormat dateFormat;
    boolean isFutureSubscription = false;
    private String purchasedBy = "";
    public static final int REQUEST_CODE = 100;


    public SubscribeAdapter(Context context, ArrayList<PlanResponse.Data> subscriptionArrayList, SubscriptionViewModel viewModel, UserProfile viewProfileResponse, String currentSubscriptionPlan, String futureSubscriptionPlan, String msg, boolean isEmail, String mobile, String purchasedBy) {
        this.context = context;
        this.subscriptionArrayList = subscriptionArrayList;
        this.viewModel = viewModel;
        this.viewProfileResponse = viewProfileResponse;
        this.currentSubscriptionPlan = currentSubscriptionPlan;
        this.futureSubscriptionPlan = futureSubscriptionPlan;
        this.msg = msg;
        this.isEmail = isEmail;
        this.purchasedBy = purchasedBy;
        this.mobile = mobile;
    }

    @NonNull
    @Override
    public SubscribeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //View listItem = layoutInflater.inflate(R.layout.subscribe_item, parent, false);
        View listItem;
        if (viewType == 0) {
            listItem = layoutInflater.inflate(R.layout.item_gold_membership, parent, false);
        } else {
            listItem = layoutInflater.inflate(R.layout.item_silver_membership, parent, false);
        }
        SubscribeAdapter.ViewHolder viewHolder = new SubscribeAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        PlanResponse.Data data = subscriptionArrayList.get(position);
//        if (data.getDays() >= 365) {
        if (data.getFrequency() != null && data.getFrequency().equalsIgnoreCase("annual")) {
            return 0;
        } else {
            return 1;
        }
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull SubscribeAdapter.ViewHolder holder, int position) {
        /*if (subscriptionArrayList.get(position).getFrequency() != null)
            holder.frequency.setText(subscriptionArrayList.get(position).getFrequency());*/
        /*else
            holder.frequency.setText(subscriptionArrayList.get(position).getFrequency() + " Days");*/
        String currencySymbol = "";
        if (!TextUtils.isEmpty(subscriptionArrayList.get(position).getCurrency())) {
            if (subscriptionArrayList.get(position).getCurrency().equalsIgnoreCase("INR")) {
                currencySymbol = "\u20B9";
            } else {
                currencySymbol = subscriptionArrayList.get(position).getCurrency();
            }
        }
        holder.tv_price.setText(currencySymbol + subscriptionArrayList.get(position).getPrice());
        if(subscriptionArrayList.get(position).getFrequency()!=null){
        holder.tvPlan.setText(subscriptionArrayList.get(position).getFrequency().toUpperCase() + holder.tvPlan.getText());
        }
        //holder.tv_class.setText(subscriptionArrayList.get(position).getName());
        /*holder.description_Wb.setBackgroundColor(Color.TRANSPARENT);
        holder.description_Wb.getSettings().setDomStorageEnabled(true);
        holder.description_Wb.getSettings().setAppCacheEnabled(true);
        holder.description_Wb.getSettings().setLoadsImagesAutomatically(true);
        holder.description_Wb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        holder.description_Wb.getSettings().setJavaScriptEnabled(true);
        holder.description_Wb.loadDataWithBaseURL(null, subscriptionArrayList.get(position).getDescription(), "text/html", "UTF-8", null);*/
        try {
            if (!TextUtils.isEmpty(currentSubscriptionPlan) && currentSubscriptionPlan.equals(subscriptionArrayList.get(position).getPlanId())) {
                holder.ivSubscriptionMark.setVisibility(View.VISIBLE);
                holder.tvSubscriptionTitle.setText("Active");
                //holder.tv_upgrade.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(futureSubscriptionPlan) && futureSubscriptionPlan.equals(subscriptionArrayList.get(position).getPlanId())) {
                holder.ivSubscriptionMark.setVisibility(View.VISIBLE);
                holder.tvSubscriptionTitle.setText("Future");
                //holder.tv_upgrade.setVisibility(View.GONE);
            } else {
                holder.ivSubscriptionMark.setVisibility(View.GONE);
                //holder.tv_upgrade.setVisibility(View.VISIBLE);
            }

            if (viewProfileResponse != null) {
                if (!TextUtils.isEmpty(viewProfileResponse.getProfile().getName())) {
                    holder.tvUserName.setText(viewProfileResponse.getProfile().getName());
                } else {
                    holder.tvUserName.setText("Guest");
                }
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        //holder.tvSubscriptionDetails.setText("Buy Now " + subscriptionArrayList.get(position).getFrequency() + " Membership");
        //holder.ll_subscription.setBackgroundColor(subscriptionArrayList.get(position).getCo);
        /*if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Monthly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Quarterly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Half-yearly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Yearly")) {
            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
        }*/
        /*try {
            if (subscriptionArrayList.get(position).getShield_color() != null && subscriptionArrayList.get(position).getShield_color().contains("#")) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.ic_path_silver);
                drawable.setTint(Color.parseColor(subscriptionArrayList.get(position).getShield_color()));
                holder.ll_subscription.setBackground(drawable);
                //holder.ll_subscription.getBackground().setTint(Color.parseColor(subscriptionArrayList.get(position).getShield_color()));
            }
            if (subscriptionArrayList.get(position).getBackgroud_color() != null && subscriptionArrayList.get(position).getShield_color().contains("#")) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.rectangle_shape);
                drawable.setTint(Color.parseColor(subscriptionArrayList.get(position).getBackgroud_color()));
                holder.background_layout_ll.setBackground(drawable);
                //holder.background_layout_ll.getBackground().setTint(Color.parseColor(subscriptionArrayList.get(position).getBackgroud_color()));
            }
        } catch (Exception e) {

        }*/
        String startDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(c.getTime());
        c.add(Calendar.DATE, subscriptionArrayList.get(position).getDays());
        Date expDate = c.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String endDate = formatter.format(expDate);


        holder.tv_startDate.setText("START DATE : " + startDate);
        holder.tv_endDate.setText("END DATE : " + endDate);

        holder.frequency.setText(subscriptionArrayList.get(position).getFrequency());
//        holder.tvSubscriptionDetails.setText(subscriptionArrayList.get(position).getDescription());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvSubscriptionDetails.setText(Html.fromHtml(subscriptionArrayList.get(position).getDescription(),
                    Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvSubscriptionDetails.setText(Html.fromHtml(subscriptionArrayList.get(position).getDescription()));
        }
        try {
            String priceStr = subscriptionArrayList.get(position).getPrice();
            Integer beforeDiscountPrice = subscriptionArrayList.get(position).getBeforeDiscountPrice();
            if (!TextUtils.isEmpty(priceStr) && beforeDiscountPrice != null) {
                int price = Integer.parseInt(priceStr);
                if (price < beforeDiscountPrice) {
                    holder.tvBeforeDiscount.setText(currencySymbol + String.valueOf(beforeDiscountPrice));
                    holder.tvBeforeDiscount.setVisibility(View.VISIBLE);
                    holder.tvBeforeDiscount.setPaintFlags(holder.tvBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tvBeforeDiscount.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.tvBeforeDiscount.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            holder.tvBeforeDiscount.setVisibility(View.INVISIBLE);
        }

        /*if (selectedPosition == position) {
            holder.ll_expanded.setVisibility(View.VISIBLE);
        } else {
            holder.ll_expanded.setVisibility(View.GONE);
        }*/


        /*holder.llParentLayout.setOnClickListener(v -> {
            String planId = "";
            if (viewProfileResponse.getData().getPlanData() != null)
                planId = viewProfileResponse.getData().getPlanData().getPlanId();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.CURRENT_MEMBERSHIP_ID, planId);
            Utility.logFirebaseEvent(context, bundle, AppConstants.UPGRADE_PACKAGE_CLICK);
            holder.ll_expanded.setVisibility(View.VISIBLE);
            selectedPosition = position;
            notifyDataSetChanged();
        });*/


        holder.llParentLayout.setOnClickListener(v -> {
            onClickBuy(position);
        });

        holder.tv_upgrade.setOnClickListener(v -> {
            onClickBuy(position);
        });

    }

    private void onClickBuy(int position) {
        try {

            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.MEMBERSHIP_ID, planId);
            bundle.putString(AppConstants.CURRENT_MEMBERSHIP_ID, planId);
            Utility.logFirebaseEvent(context, bundle, AppConstants.MEMBERSHIP_UPGRADE);

            HashMap<String,Object> payload = new HashMap<>();
            payload.put(AppConstants.MEMBERSHIP_ID, planId);
            payload.put(AppConstants.CURRENT_MEMBERSHIP_ID, planId);
            Utility.logNetcoreEvent(context, payload, AppConstants.MEMBERSHIP_UPGRADE);

//            Date stringtodate = null;
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                stringtodate = format.parse(viewProfileResponse.getData().getProfile().getExpiryDate());
//            } catch (ParseException e) {
//                  CommonUtils.printStackTrace(e)
//            }
            if (!TextUtils.isEmpty(currentSubscriptionPlan) && currentSubscriptionPlan.equals(subscriptionArrayList.get(position).getName())) {
                Toast.makeText(context, context.getString(R.string.subscription_not_expired_validation), Toast.LENGTH_SHORT).show();
            } else if (viewProfileResponse.isFutureSubcriptionExist().equals("true")) {
                showdialog("You already have an Active Future Membership, You can buy new Membership only after the future Membership is active");
            } else if (!CommonUtils.isSubscriptionExpired(viewProfileResponse.getProfile().getExpiryDate())) {
                if (purchasedBy.equals(AppConstants.PURCHASE_BY_APPLE)) {
                    showdialog(AppConstants.PURCHASE_BY_APPLE_MSG);
                } else
                    showAlreadySubscriptionDialog(position);
            } else {
                initSubscriptionSelected(position,false);
//                subscribePresenter.initSubscription(subscriptionArrayList.get(position).getId(), subscriptionArrayList.get(position).getPlanId(), initSubscriptionCallback, isFutureSubscription);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void initSubscriptionSelected(int position, boolean isFutureSubscription) {
        planId = subscriptionArrayList.get(position).getPlanId();
        id = subscriptionArrayList.get(position).getId();
        InitSubscriptionRequest request = new InitSubscriptionRequest();
        request.setPlanId(planId);
        request.setId(id);
        request.setFuture(isFutureSubscription);
        viewModel.setInitSubscriptionRequest(request);
        viewModel.setSelectedSubscription(subscriptionArrayList.get(position));
    }

    private void showAlreadySubscriptionDialog(int position) {
        Logger.i(TAG, "AlreadySubscriptionDialog");
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.error_already_subscription);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            RadioButton futureSubscriptionBtn = dialog.findViewById(R.id.futureSubscriptionBtn);
            if (Objects.equals(viewProfileResponse.getLifetime_free(), "1")) {
                futureSubscriptionBtn.setVisibility(View.GONE);
            }
            LinearLayout llStartFreeTrial = (LinearLayout) dialog.findViewById(R.id.llOkayBtn);
            llStartFreeTrial.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    initSubscriptionSelected(position,futureSubscriptionBtn.isChecked());
//                    subscribePresenter.initSubscription(subscriptionArrayList.get(position).getId(), subscriptionArrayList.get(position).getPlanId(), initSubscriptionCallback, isFutureSubscription);
                    dialog.dismiss();
                }
            });
            LinearLayout llCancelBtn = (LinearLayout) dialog.findViewById(R.id.llCancelBtn);
            llCancelBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    public void showdialog(String msg) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_login_failure);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(msg);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    private void showErrorDialog() {
        try {
            final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_subscription_validation);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOkay);
            tvOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        WolooApplication.getInstance().setProfileResponse(viewProfileResponse);
                        try {
                            MoreFragment.isNeedToUpdateProfile = true;
                        } catch (Exception ex) {
                             CommonUtils.printStackTrace(ex);
                        }
                        Intent intent = new Intent(context, EditProfileActivity.class);
                        intent.putExtra(AppConstants.ID, id);
                        intent.putExtra(AppConstants.FROM_SUBSCRIPTION, true);
                        intent.putExtra(AppConstants.PLAN_ID, planId);
                        intent.putExtra(AppConstants.MSG, msg);
                        intent.putExtra(AppConstants.IS_EMAIL, isEmail);
                        intent.putExtra(AppConstants.MOBILE, mobile);
                        context.startActivity(intent);
                    } catch (Exception ex) {
                         CommonUtils.printStackTrace(ex);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionArrayList.size();
    }

    public void setViewProfile(UserProfile viewProfileResponse) {
        this.viewProfileResponse = viewProfileResponse;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.frequency)
        TextView frequency;

        @BindView(R.id.tv_price)
        TextView tv_price;

        /*@BindView(R.id.tv_class)
        TextView tv_class;*/

        /*@BindView(R.id.ll_subscription)
        LinearLayout ll_subscription;*/

        /*@BindView(R.id.background_layout_ll)
        LinearLayout background_layout_ll;*/

        @BindView(R.id.imageView2)
        View llParentLayout;

        @BindView(R.id.tvSubscriptionDetails)
        TextView tvSubscriptionDetails;

        @BindView(R.id.tvSubscriptionTitle)
        TextView tvSubscriptionTitle;

        @BindView(R.id.ivSubscriptionMark)
        LinearLayout ivSubscriptionMark;

        /*@BindView(R.id.tv_price_info)
        TextView tv_price_info;*/

       /* @BindView(R.id.ll_expanded)
        LinearLayout ll_expanded;*/

        @BindView(R.id.tv_upgrade)
        TextView tv_upgrade;

        @BindView(R.id.tvStartDate)
        TextView tv_startDate;

        /*@BindView(R.id.description_Wb)
        WebView description_Wb;*/

        @BindView(R.id.tvEndDate)
        TextView tv_endDate;

        @BindView(R.id.tvName)
        TextView tvUserName;

        @BindView(R.id.tvBeforeDiscount)
        TextView tvBeforeDiscount;

        @BindView(R.id.tvPlan)
        TextView tvPlan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    InitSubscriptionCallback initSubscriptionCallback = new InitSubscriptionCallback() {
        @Override
        public void initSubscriptionSuccess(InitSubscriptionResponse initSubscriptionResponse) {
            try {
                if (initSubscriptionResponse != null && initSubscriptionResponse.getData() != null) {
                    String subscriptionId = initSubscriptionResponse.getData().getSubscriptionId();
                    if (!TextUtils.isEmpty(subscriptionId)) {
                        CommonUtils.navigateToRazorPayFlow(context, planId, subscriptionId, msg, isEmail, mobile, true, null, isFutureSubscription, false);
                    } else {
                        //showErrorDialog();
                        showdialog(initSubscriptionResponse.getMessage());
                    }
                } else {
                    showdialog(initSubscriptionResponse != null ? initSubscriptionResponse.getMessage() : null);
                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }
    };

    public interface InitSubscriptionCallback {
        void initSubscriptionSuccess(InitSubscriptionResponse initSubscriptionResponse);
    }
}
