package `in`.woloo.www.more.my_account

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.UserCoinHistoryModel.Data.HistoryItem
import `in`.woloo.www.utils.CircleImageView
import `in`.woloo.www.utils.ImageUtil

class GiftCardDetailActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var imvBack: ImageView? = null

    @JvmField
    @BindView(R.id.gift_card_amount_Tv)
    var tvCardAmount: TextView? = null

    @JvmField
    @BindView(R.id.sentOReceived_Tv)
    var tvSentOReceived: TextView? = null

    @JvmField
    @BindView(R.id.seen_date_time_Tv)
    var tvSeenDateTime: TextView? = null

    @JvmField
    @BindView(R.id.user_name_Tv)
    var tvUserName: TextView? = null

    @JvmField
    @BindView(R.id.user_mobile_no_Tv)
    var tvUserMobileNo: TextView? = null

    @JvmField
    @BindView(R.id.description_Tv)
    var tvDescription: TextView? = null

    @JvmField
    @BindView(R.id.gift_card_id_Tv)
    var tvCardId: TextView? = null

    @JvmField
    @BindView(R.id.sentTO_BY_TV)
    var tvSent_TO_BY: TextView? = null

    @JvmField
    @BindView(R.id.expiry_date_Tv)
    var tvCardExpireDate: TextView? = null

    @JvmField
    @BindView(R.id.user_image_Imv)
    var imvUserImage: CircleImageView? = null
    var userCoinHistoryModel: HistoryItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gift_card_detail)
        ButterKnife.bind(this)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        tvTitle!!.text = "Woloo Gift-Card Detail"
        imvBack!!.setOnClickListener { finish() }
        if (intent.hasExtra("data")) {
            val intent = this.intent
            val bundle = intent.extras
            userCoinHistoryModel = bundle!!.getSerializable("data") as HistoryItem?
            if (getIntent().hasExtra("giftMemberSent")) {
                tvTitle!!.text = "Woloo Gift-Membership Detail"
                tvSentOReceived!!.text = "Gift Membership Sent"
                tvSent_TO_BY!!.text = "Gifted To"
            } else if (getIntent().hasExtra("giftMemberReceived")) {
                tvTitle!!.text = "Woloo Gift-Membership Detail"
                tvSentOReceived!!.text = "Gift Membership Received"
                tvSent_TO_BY!!.text = "Gift from"
            } else if (userCoinHistoryModel!!.transactionType == "CR") {
                tvSentOReceived!!.text = "Gift Card Received"
                tvSent_TO_BY!!.text = "Sent By"
            } else {
                if (userCoinHistoryModel!!.sender
                        ?.name == null || userCoinHistoryModel!!.sender?.name == ""
                ) {
                    tvSentOReceived!!.text =
                        "Gift Sent to " + userCoinHistoryModel!!.sender?.mobile
                } else {
                    tvSentOReceived!!.text =
                        "Gift Sent to " + userCoinHistoryModel!!.sender?.name
                }
                tvSent_TO_BY!!.text = "Sent To"
            }
            tvCardAmount!!.text = userCoinHistoryModel!!.value
            tvSeenDateTime!!.text =
                CommonUtils.geCreditHistoryDateAndTime(userCoinHistoryModel!!.createdAt!!)
            if (userCoinHistoryModel!!.sender?.name == ""
            ) {
                tvUserName!!.text = ""
            } else {
                tvUserName!!.text = userCoinHistoryModel!!.sender?.name
            }
            tvUserMobileNo!!.text = userCoinHistoryModel!!.sender?.mobile
            tvDescription!!.text = userCoinHistoryModel!!.message
            if (userCoinHistoryModel!!.sender?.avatar
                    ?.trim { it <= ' ' } == "users/default.png" || userCoinHistoryModel!!.sender
                    !!.avatar?.trim { it <= ' ' } == "default.png"
            ) {
                ImageUtil.loadImageProfile(
                    this,
                    imvUserImage!!,
                    BuildConfig.BASE_URL + "public/userProfile/default.png"
                )
            } else {
                ImageUtil.loadImageProfile(
                    this,
                    imvUserImage!!,
                    BuildConfig.BASE_URL + "public/userProfile/" + userCoinHistoryModel!!.sender
                        !!.avatar
                )
            }
            imvUserImage!!.setBackgroundResource(R.color.transparent_background)
        }
    }
}