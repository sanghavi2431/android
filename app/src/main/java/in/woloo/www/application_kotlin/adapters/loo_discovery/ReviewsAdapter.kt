package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.CircleImageView
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.ImageUtil.loadImageProfile
import `in`.woloo.www.utils.TimeAgoUtils
import `in`.woloo.www.utils.TimeAgoUtils.getTimeAgo
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewsAdapter(
    private val context: Context,
    private val reviewList: List<ReviewListResponse.Review>
) :
    RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    private val imageUtil: ImageUtil


    init {
        imageUtil = `in`.woloo.www.utils.ImageUtil
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.reviews_item, parent, false)
        val viewHolder: ViewHolder = ViewHolder(listItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.ivProfile!!.clipToOutline = true
        if (reviewList[position].userDetails!!.name != null) {
            if (!reviewList[position].userDetails!!.name.equals("", ignoreCase = true)) {
                holder.tvName!!.text = reviewList[position].userDetails!!.name
            } else {
                holder.tvName!!.text = "Guest"
            }
        } else {
            holder.tvName!!.text = "Guest"
        }
        Log.d("String", (reviewList[position].reviewDescription.toString()))
        holder.tvReview!!.text = reviewList[position].reviewDescription.toString()
        holder.starScoreCount!!.text = reviewList[position].ratingOption.toString()

        /*  try{
             if (reviewList.get(position).getReviewDescription().length() > 60) {
                 holder.tvReview.setLinkTextColor(context.getResources().getColor(R.color.text_color_five));
                 holder.tvReview.setText(String.format("%s%s",holder.tvReview.getText().subSequence(0, 60), context.getResources().getString(R.string.read_more)));
                 SpannableString spannableString = new SpannableString(holder.tvReview.getText().toString());
                 ClickableSpan clickableSpan = new ClickableSpan() {
                     @Override
                     public void onClick(View textView) {
                         context.startActivity(new Intent(context, ViewReviewActivity.class).putExtra(AppConstants.REVIEW,reviewList.get(position).getReviewDescription()));
                     }
                     @Override
                     public void updateDrawState(TextPaint ds) {
                         super.updateDrawState(ds);
                         ds.setUnderlineText(false);
                     }
                 };
                 spannableString.setSpan(clickableSpan,holder.tvReview.getText().toString().length()-11, holder.tvReview.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                 holder.tvReview.setText(spannableString);
                 holder.tvReview.setMovementMethod(LinkMovementMethod.getInstance());
                 holder.tvReview.setHighlightColor(Color.TRANSPARENT);
             }
         }catch (Exception ex) {
              CommonUtils.printStackTrace(ex);
         }*/
        holder.tvTimeAgo!!.text = TimeAgoUtils.getTimeAgo(
            CommonUtils.getTimeAgo(
                reviewList[position].updatedAt!!
            )
        )
        if (reviewList[position].userDetails!!.avatar == null || reviewList[position].userDetails!!.avatar!!.trim { it <= ' ' } == "users/default.png" || reviewList[position].userDetails!!.avatar!!.trim { it <= ' ' } == "default.png") {
            loadImageProfile(
                context,
                holder.ivProfile!!,
                BuildConfig.BASE_URL + "public/userProfile/default.png"
            )
        } else {
            loadImageProfile(
                context,
                holder.ivProfile!!,
                reviewList[position].userDetails!!.baseUrl + reviewList[position].userDetails!!.avatar
            )
        }
        var dateString = reviewList[position].userDetails!!.wolooMemberSince

        try {
            // Step 1: Define the input format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            // Step 2: Parse the input date string into a Date object
            val date = inputFormat.parse(dateString)

            // Step 3: Define the output format for "Nov 2024"
            val outputFormat = SimpleDateFormat("MMM yy", Locale.getDefault())

            dateString = outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.memberSince!!.text = "Woloo Member Since- $dateString"
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.ivProfile)
        var ivProfile: CircleImageView? = null

        @JvmField
        @BindView(R.id.tvReview)
        var tvReview: TextView? = null

        @JvmField
        @BindView(R.id.tvName)
        var tvName: TextView? = null

        @JvmField
        @BindView(R.id.tvTimeAgo)
        var tvTimeAgo: TextView? = null

        @JvmField
        @BindView(R.id.tvUserRating)
        var tvRating: TextView? = null


        @JvmField
        @BindView(R.id.star_score)
        var starScoreImage: ImageView? = null

        @JvmField
        @BindView(R.id.start_score_count)
        var starScoreCount: TextView? = null

        @JvmField
        @BindView(R.id.tvMemberSince)
        var memberSince: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
