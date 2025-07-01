package in.woloo.www.my_account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.more.models.UserCoinHistoryModel;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.ImageUtil;

public class GiftCardDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView imvBack;

    @BindView(R.id.gift_card_amount_Tv)
    TextView tvCardAmount;

    @BindView(R.id.sentOReceived_Tv)
    TextView tvSentOReceived;

    @BindView(R.id.seen_date_time_Tv)
    TextView tvSeenDateTime;

    @BindView(R.id.user_name_Tv)
    TextView tvUserName;

    @BindView(R.id.user_mobile_no_Tv)
    TextView tvUserMobileNo;

    @BindView(R.id.description_Tv)
    TextView tvDescription;

    @BindView(R.id.gift_card_id_Tv)
    TextView tvCardId;

    @BindView(R.id.sentTO_BY_TV)
    TextView tvSent_TO_BY;

    @BindView(R.id.expiry_date_Tv)
    TextView tvCardExpireDate;

    @BindView(R.id.user_image_Imv)
    CircleImageView imvUserImage;

    UserCoinHistoryModel.Data.HistoryItem userCoinHistoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        tvTitle.setText("Woloo Gift-Card Detail");
        imvBack.setOnClickListener(view -> {
            finish();
        });

        if (getIntent().hasExtra("data")) {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            userCoinHistoryModel = (UserCoinHistoryModel.Data.HistoryItem) bundle.getSerializable("data");
            if(getIntent().hasExtra("giftMemberSent")){
                tvTitle.setText("Woloo Gift-Membership Detail");
                tvSentOReceived.setText("Gift Membership Sent");
                tvSent_TO_BY.setText("Gifted To");
            }
            else if(getIntent().hasExtra("giftMemberReceived")){
                tvTitle.setText("Woloo Gift-Membership Detail");
                tvSentOReceived.setText("Gift Membership Received");
                tvSent_TO_BY.setText("Gift from");
            }
            else if (userCoinHistoryModel.getTransactionType().equals("CR")) {
                tvSentOReceived.setText("Gift Card Received");
                tvSent_TO_BY.setText("Sent By");
            } else {
                if (userCoinHistoryModel.getSender().getName() == null || userCoinHistoryModel.getSender().getName().equals("")) {
                    tvSentOReceived.setText("Gift Sent to "+userCoinHistoryModel.getSender().getMobile());
                }else{
                    tvSentOReceived.setText("Gift Sent to "+userCoinHistoryModel.getSender().getName());
                }
                tvSent_TO_BY.setText("Sent To");
            }
            tvCardAmount.setText(userCoinHistoryModel.getValue());
            tvSeenDateTime.setText(CommonUtils.geCreditHistoryDateAndTime(userCoinHistoryModel.getCreatedAt()));
            if (userCoinHistoryModel.getSender().getName() == null || userCoinHistoryModel.getSender().getName().equals("")) {
                tvUserName.setText("");
            } else {
                tvUserName.setText(userCoinHistoryModel.getSender().getName());
            }
            tvUserMobileNo.setText(userCoinHistoryModel.getSender().getMobile());
            tvDescription.setText(userCoinHistoryModel.getMessage());
            if (userCoinHistoryModel.getSender().getAvatar().trim().equals("users/default.png") || userCoinHistoryModel.getSender().getAvatar().trim().equals("default.png")) {
                ImageUtil.loadImageProfile(this, imvUserImage, BuildConfig.BASE_URL + "public/userProfile/default.png");
            } else {
                ImageUtil.loadImageProfile(this, imvUserImage, BuildConfig.BASE_URL + "public/userProfile/" + userCoinHistoryModel.getSender().getAvatar());
            }
            imvUserImage.setBackgroundResource(R.color.transparent_background);
        }
    }
}