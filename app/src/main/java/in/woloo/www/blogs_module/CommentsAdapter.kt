package `in`.woloo.www.blogs_module

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.databinding.CommentsListItemBinding
import `in`.woloo.www.databinding.StoreAddressListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.AddressesCustomAdapter
import `in`.woloo.www.store.screens.AddEditAddressBottomSheetFragment
import `in`.woloo.www.store.user_details.AddressList

class CommentsAdapter(private val context: FragmentActivity,
                      private val comment: ArrayList<CommentResponse.Data>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    lateinit var binding: CommentsListItemBinding


    class ViewHolder(val binding: CommentsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            CommentsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = comment!![position]
        try {

            holder.binding.userName.text = commentItem.userName

            holder.binding.productPrice.text = commentItem.commentText

            Glide.with(context)
                .load(commentItem.userProfilePicture)
                .error(R.color.search_background) // optional
                .into(holder.binding.bottomHostImage)

        }catch (e : Exception)
        {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int = comment.size ?: 0


}