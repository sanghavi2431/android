package `in`.woloo.www.more.fragments.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.fragments.model.Contacts
import java.util.Locale

class InviteFriendsAdapter(
    private val context: Context,
    private val subscriptionArrayList: ArrayList<Contacts>
) :
    RecyclerView.Adapter<InviteFriendsAdapter.ViewHolder>() {
    private val mobileNumbers = ArrayList<String>()
    private val arraylist: ArrayList<Contacts>

    init {
        this.arraylist = ArrayList<Contacts>()
        arraylist.addAll(subscriptionArrayList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.invitecontacts_item, parent, false)
        val viewHolder: ViewHolder = ViewHolder(listItem)
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name!!.text =
            subscriptionArrayList[position].first_name + " " + subscriptionArrayList[position].last_name
        holder.tv_mobilenumber!!.text = subscriptionArrayList[position].mobile_number
        holder.rlParentLayout!!.setOnClickListener { v: View? ->
            try {
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
        if (subscriptionArrayList[position].isSelected || mobileNumbers.contains(
                subscriptionArrayList[position].mobile_number
            )
        ) {
            holder.chkInvite!!.isChecked = true
            subscriptionArrayList[position].isSelected = true
        } else {
            holder.chkInvite!!.isChecked = false
            subscriptionArrayList[position].isSelected = false
        }

        holder.chkInvite!!.setOnClickListener {
            if (subscriptionArrayList[position].isSelected) {
                holder.chkInvite!!.isChecked = false
                subscriptionArrayList[position].isSelected = false
                mobileNumbers.remove(subscriptionArrayList[position].mobile_number)
            } else {
                holder.chkInvite!!.isChecked = true
                subscriptionArrayList[position].isSelected = true
                mobileNumbers.add(subscriptionArrayList[position].mobile_number!!)
            }
        }

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
    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        subscriptionArrayList.clear()
        if (charText.length == 0) {
            subscriptionArrayList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.first_name!!.lowercase(Locale.getDefault())
                        .contains(charText) || wp.mobile_number!!.lowercase(
                        Locale.getDefault()
                    ).contains(charText)
                ) {
                    subscriptionArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
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
    @SuppressLint("UnsafeImplicitIntentLaunch")
    private fun sendSMS(number: String, name: String) {
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
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.setType("vnd.android-dir/mms-sms")
        smsIntent.putExtra("address", number)
        smsIntent.putExtra("sms_body", "Hello  $name")
        context.startActivity(smsIntent)
        /*   }*/
    }

    override fun getItemCount(): Int {
        return subscriptionArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.tv_name)
        var tv_name: TextView? = null

        //
        @JvmField
        @BindView(R.id.tv_mobilenumber)
        var tv_mobilenumber: TextView? = null

        @JvmField
        @BindView(R.id.rlParentLayout)
        var rlParentLayout: RelativeLayout? = null

        @JvmField
        @BindView(R.id.chkInvite)
        var chkInvite: CheckBox? = null

        //
        //        @BindView(R.id.tv_class)
        //        TextView tv_class;
        //
        //
        //        @BindView(R.id.ll_subscription)
        //        LinearLayout ll_subscription;
        init {
            ButterKnife.bind(this, itemView)
        }
    }

    val selectedNumbers: ArrayList<String>?
        get() {
            try {
                return mobileNumbers
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            return null
        }
}
