package `in`.woloo.www.more.my_account.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.UserCoinHistoryModel.Data.HistoryItem
import `in`.woloo.www.more.my_account.GiftCardDetailActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent

class CreditHistoryAdapter(private val context: Context, coin_History: ArrayList<HistoryItem>) :
    RecyclerView.Adapter<CreditHistoryAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    var coin_History: ArrayList<HistoryItem>
    var previous_History_Date = ""

    init {
        mInflater = LayoutInflater.from(context)
        this.coin_History = coin_History
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.credit_history_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        setData(viewHolder, position)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(viewHolder: ViewHolder, position: Int) {
        try {
            if (coin_History[position].type == AppConstants.NAVIGATION_REWARDS) {
                if (coin_History[position].wolooDetails != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Woloo Navigation used to reach Woloo Host " + coin_History[position].wolooDetails
                            ?.name
                } else {
                    viewHolder.tvCreditMsg!!.text = "Woloo Navigation used to reach Woloo Host "
                }
                Glide.with(context)
                    .load(R.drawable.woloo_navigation_reward_credit_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.REGISTRATION_POINTS) {
                viewHolder.tvCreditMsg!!.text = "completing your registration process"
                Glide.with(context)
                    .load(R.drawable.registration_point_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.GIFT_RECEIVED) {
                Glide.with(context)
                    .load(R.drawable.gift_received_icon)
                    .into(viewHolder.ivWolooStore!!)
                if (coin_History[position].sender != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Received Gift from " + coin_History[position].sender
                            ?.name + " - " + coin_History[position].sender?.mobile
                } else {
                    viewHolder.tvCreditMsg!!.text = "Gift Received "
                }
            } else if (coin_History[position].type == AppConstants.GIFT_SENT) {
                if (coin_History[position].sender != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Purchase of Woloo Gift Card for " + coin_History[position].sender
                            ?.name + "-" + coin_History[position].sender?.mobile
                } else {
                    viewHolder.tvCreditMsg!!.text = "Woloo Gift Card Sent"
                }
                Glide.with(context)
                    .load(R.drawable.gift_sent_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.REFERAL_POINT) {
                if (coin_History[position].sender != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Successful Referral made " + coin_History[position].sender?.mobile
                } else {
                    viewHolder.tvCreditMsg!!.text = "Successful Referral made"
                }
                Glide.with(context)
                    .load(R.drawable.referral_point_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.ADD_COINS) {
                viewHolder.tvCreditMsg!!.text =
                    "Purchase of Coins " + coin_History[position].value
                Glide.with(context)
                    .load(R.drawable.add_coins_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.NO_WOLOO_FOUND_REWARD) {
                if (coin_History[position].wolooDetails != null) viewHolder.tvCreditMsg!!.text =
                    "Enjoy Woloo Points till we get Woloo at your searched location " + coin_History[position].wolooDetails
                        ?.address else viewHolder.tvCreditMsg!!.text =
                    "Enjoy Woloo Points till we get Woloo at your searched location"
                Glide.with(context)
                    .load(R.drawable.no_woloo_found_reward)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.RECOMMEND_WOLOO_CREDITS) {
                if (coin_History[position].wolooDetails != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Recommended " + coin_History[position].wolooDetails
                            ?.name + " to be included as a Woloo Host"
                } else {
                    viewHolder.tvCreditMsg!!.text = "Recommended  to be included as a Woloo Host"
                }
                Glide.with(context)
                    .load(R.drawable.woloo_credit_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.APPROVED_RECOMMEND_WOLOO_CREDITS) {
                if (coin_History[position].wolooDetails != null) {
                    viewHolder.tvCreditMsg!!.text =
                        "Recommended Woloo Host " + (coin_History[position].wolooDetails
                            ?.name ) + "approved by Woloo"
                } else {
                    viewHolder.tvCreditMsg!!.text = "Recommended Woloo Host approved by Woloo"
                }
                Glide.with(context)
                    .load(R.drawable.approve_recommend_woloo_credit_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.USING_WOLOO_SERVICE_AT_PARTICULAR_HOST) {
                viewHolder.tvCreditMsg!!.text =
                    "Woloo Service used at " + coin_History[position].wolooDetails
                        ?.name + " - " + coin_History[position].wolooDetails!!.address
                Glide.with(context)
                    .load(R.drawable.using_woloo_at_particular_host_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.ECOM_GIFT_DEBIT) {
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
                Glide.with(context)
                    .load(R.drawable.gift_sent_icon)
                    .into(viewHolder.ivWolooStore!!)
            } else if (coin_History[position].type == AppConstants.ECOM_POINTS_DEBIT) {
                Glide.with(context)
                    .load(R.drawable.add_coins_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.GIFT_SUB_SENT) {
                Glide.with(context)
                    .load(R.drawable.gift_sent_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.GIFT_SUB_RECEIVED) {
                Glide.with(context)
                    .load(R.drawable.gift_received_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.PURCHASE_MEMBER) {
                Glide.with(context)
                    .load(R.drawable.add_coins_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.BLOG_READ_POINT) {
                Glide.with(context)
                    .load(R.drawable.blog_read_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.GIFT_POINTS_DEDUCTED) {
                Glide.with(context)
                    .load(R.drawable.gift_points_deducted)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else if (coin_History[position].type == AppConstants.WAH_CERTIFICATE_POINT) {
                Glide.with(context)
                    .load(R.drawable.add_coins_icon)
                    .into(viewHolder.ivWolooStore!!)
                viewHolder.tvCreditMsg!!.setText(coin_History[position].remarks)
                //viewHolder.tvCreditMsg.setText(coin_History.get(position).getType() + " " +coin_History.get(position).getRemarks());
            } else {
                viewHolder.tvCreditMsg!!.setText(coin_History[position].type)
                val imgUrl =
                    BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE
                ImageUtil.loadImageProfileAccount(context, viewHolder.ivWolooStore!!, imgUrl)
            }
            if (!TextUtils.isEmpty(coin_History[position].value)) {
                if (coin_History[position].isGift == 1) viewHolder.tvPoints!!.text =
                    "\u20B9 " + coin_History[position].value else viewHolder.tvPoints!!.setText(
                    coin_History[position].value + "\nPoints"
                )
            }
            val updated_at: String = coin_History[position].updatedAt.toString()
            val createdAt: String = coin_History[position].createdAt.toString()
            if (updated_at != null) {
                viewHolder.tv_updated_at!!.text = CommonUtils.geCreditHistoryDate(updated_at, false)
            }
            if (createdAt != null) {
                if (previous_History_Date == CommonUtils.geCreditHistoryDate(
                        createdAt,
                        true
                    )
                ) viewHolder.tv_created_at!!.visibility = View.GONE else {
                    previous_History_Date = CommonUtils.geCreditHistoryDate(createdAt, true)
                    viewHolder.tv_created_at!!.visibility = View.VISIBLE
                }
                viewHolder.tv_created_at!!.text = CommonUtils.geCreditHistoryDate(createdAt, true)
            }

            /*if(coin_History.get(position).getWolooDetails() != null && !TextUtils.isEmpty(coin_History.get(position).getWolooDetails().getImage())){
                ImageUtil.loadImageProfileAccount(context,viewHolder.ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+coin_History.get(position).getWolooDetails().getImage());
            }else{
                String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE;
                ImageUtil.loadImageProfileAccount(context,viewHolder.ivWolooStore,imgUrl);
            }*/if (coin_History[position].sender != null && (coin_History[position].type == AppConstants.GIFT_RECEIVED || coin_History[position].type == AppConstants.GIFT_SENT || coin_History[position].type == AppConstants.GIFT_SUB_SENT || coin_History[position].type == AppConstants.GIFT_SUB_RECEIVED)) {
                viewHolder.tvCreditMsg!!.setOnClickListener { view: View? ->
                    val firebaseBundle = Bundle()
                    firebaseBundle.putString(
                        AppConstants.POINTS_ID,
                        coin_History[position].id.toString()
                    )
                    logFirebaseEvent(context, firebaseBundle, AppConstants.POINT_DETAILS_CLICK)
                    val payload = HashMap<String, Any>()
                    payload[AppConstants.POINTS_ID] = coin_History[position].id.toString()
                    logNetcoreEvent(context, payload, AppConstants.POINT_DETAILS_CLICK)
                    val intent = Intent(context, GiftCardDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("data", coin_History[position])
                    if (coin_History[position].type == AppConstants.GIFT_SUB_SENT) intent.putExtra(
                        "giftMemberSent",
                        ""
                    )
                    if (coin_History[position].type == AppConstants.GIFT_SUB_RECEIVED) intent.putExtra(
                        "giftMemberReceived",
                        ""
                    )
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun getItemCount(): Int {
        return coin_History.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.tv_remarks)
        var tv_remarks: TextView? = null

        @JvmField
        @BindView(R.id.tv_updated_at)
        var tv_updated_at: TextView? = null

        @JvmField
        @BindView(R.id.tv_created_at)
        var tv_created_at: TextView? = null

        @JvmField
        @BindView(R.id.tvCreditMsg)
        var tvCreditMsg: TextView? = null

        @JvmField
        @BindView(R.id.tvPoints)
        var tvPoints: TextView? = null

        @JvmField
        @BindView(R.id.ivWolooStore)
        var ivWolooStore: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
