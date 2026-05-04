package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.databinding.CollectionsListItemBinding
import `in`.woloo.www.databinding.ReviewsItemBinding
import `in`.woloo.www.databinding.StorCirclularImageListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.ProductsCollectionsCustomeAdapter.ViewHolder
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.reviews.ReviewListData
import `in`.woloo.www.store.reviews.ReviewsResponse

class StoreReviewAdapter (private val context: Activity,
                          private var reviewListItem: ArrayList<ReviewListData>,
                          private val storeViewModel: StoreViewModel
) : RecyclerView.Adapter<StoreReviewAdapter.ViewHolder>() {

    lateinit var binding: ReviewsItemBinding
    class ViewHolder(val binding:ReviewsItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ReviewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewItem = reviewListItem[position]
try{


    binding.tvReview.setText(reviewItem.comment)
    binding.startScoreCount.setText(reviewItem.rating.toString())
    binding.tvMemberSince.setText("Posted on- ${reviewItem.created_at}")
    binding.tvName.text = reviewItem.customer?.firstName + " " + reviewItem.customer?.lastName

}catch (e : Exception)
{

}


    }

    override fun getItemCount(): Int = reviewListItem.size
}