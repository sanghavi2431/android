package in.woloo.www.invite_friend.fragments.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.invite_friend.fragments.model.Contacts;

public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contacts> subscriptionArrayList;
    private ArrayList<String> mobileNumbers = new ArrayList<String>();
    private ArrayList<Contacts> arraylist;

    public InviteFriendsAdapter(Context context, ArrayList<Contacts> subscriptionArrayList) {
        this.context = context;
        this.subscriptionArrayList = subscriptionArrayList;
        this.arraylist = new ArrayList();
        this.arraylist.addAll(subscriptionArrayList);
    }


    @NonNull
    @Override
    public InviteFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.invitecontacts_item, parent, false);
        InviteFriendsAdapter.ViewHolder viewHolder = new InviteFriendsAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(subscriptionArrayList.get(position).getFirst_name() + " " + subscriptionArrayList.get(position).getLast_name());
        holder.tv_mobilenumber.setText(subscriptionArrayList.get(position).getMobile_number());
        holder.rlParentLayout.setOnClickListener(v -> {
            try {
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        });
        if (subscriptionArrayList.get(position).isSelected() || mobileNumbers.contains(subscriptionArrayList.get(position).getMobile_number())) {
            holder.chkInvite.setChecked(true);
            subscriptionArrayList.get(position).setSelected(true);
        }else{
            holder.chkInvite.setChecked(false);
            subscriptionArrayList.get(position).setSelected(false);
        }

        holder.chkInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscriptionArrayList.get(position).isSelected()){
                    holder.chkInvite.setChecked(false);
                    subscriptionArrayList.get(position).setSelected(false);
                    mobileNumbers.remove(subscriptionArrayList.get(position).getMobile_number());
                }else{
                    holder.chkInvite.setChecked(true);
                    subscriptionArrayList.get(position).setSelected(true);
                    mobileNumbers.add(subscriptionArrayList.get(position).getMobile_number());
                }
            }
        });

        /*holder.chkInvite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                subscriptionArrayList.get(position).setSelected(true);
                mobileNumbers.add(subscriptionArrayList.get(position).getMobile_number());
            } else {
                subscriptionArrayList.get(position).setSelected(false);
                mobileNumbers.remove(subscriptionArrayList.get(position).getMobile_number());
            }
        });*/

    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        subscriptionArrayList.clear();
        if (charText.length() == 0) {
            subscriptionArrayList.addAll(arraylist);
        } else {
            for (Contacts wp : arraylist) {
                if (wp.getFirst_name().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getMobile_number().toLowerCase(Locale.getDefault()).contains(charText)) {
                    subscriptionArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    //    @Override
//    public void onBindViewHolder(@NonNull in.woloo.www.subscribe.adapter.SubscribeAdapter.ViewHolder holder, int position) {
//        holder.frequency.setText(subscriptionArrayList.get(position).getFrequency());
//        holder.tv_price.setText("\u20B9"+subscriptionArrayList.get(position).getPrice());
//        if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Monthly")) {
//            holder.tv_class.setText("CLASSIC");
//            holder.ll_subscription.setBackgroundResource(R.drawable.ic_monthly_subscription_bg);
//        } else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Quarterly")) {
//            holder.tv_class.setText("SILVER");
//            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
//        }
//        else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Half-yearly")) {
//            holder.tv_class.setText("GOLD");
//            holder.ll_subscription.setBackgroundResource(R.drawable.ic_half_yearly_subscription_bg);
//        }
//        else if (subscriptionArrayList.get(position).getFrequency().equalsIgnoreCase("Yearly")) {
//            holder.tv_class.setText("ELITE");
//            holder.ll_subscription.setBackgroundResource(R.drawable.ic_path_silver);
//        }else{
//            holder.tv_class.setText("WEEKLY");
//        }
//    }

    private void sendSMS(String number, String name) {
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {*/
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", number);
        smsIntent.putExtra("sms_body", "Hello  " + name);
        context.startActivity(smsIntent);
        /*   }*/
    }

    @Override
    public int getItemCount() {
        return subscriptionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        //
        @BindView(R.id.tv_mobilenumber)
        TextView tv_mobilenumber;

        @BindView(R.id.rlParentLayout)
        RelativeLayout rlParentLayout;

        @BindView(R.id.chkInvite)
        CheckBox chkInvite;

//
//        @BindView(R.id.tv_class)
//        TextView tv_class;
//
//
//        @BindView(R.id.ll_subscription)
//        LinearLayout ll_subscription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ArrayList<String> getSelectedNumbers() {
        try {
            return mobileNumbers;
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        return null;
    }


}
