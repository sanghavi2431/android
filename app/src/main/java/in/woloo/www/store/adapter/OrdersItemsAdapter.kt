package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.OrderDetailsItemsListItemBinding
import `in`.woloo.www.databinding.OrderDetailsListItemBinding
import `in`.woloo.www.store.orders_response.ItemsListData
import `in`.woloo.www.store.orders_response.ItemsListDataOrderSet
import `in`.woloo.www.store.orders_response.OrdersListData
import `in`.woloo.www.store.screens.DisplayOrderDetails
import `in`.woloo.www.store.screens.ReviewBottomSheetFragment
import java.text.DecimalFormat

class OrdersItemsAdapter (private val items: List<ItemsListDataOrderSet> ,
    private val order : OrdersListData , private val activity : Activity ,
                          private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<OrdersItemsAdapter.ViewHolder>() {

    lateinit var binding: OrderDetailsItemsListItemBinding
    lateinit var productId : String
    lateinit var productName : String
    var ratingsGiven : Int = 3

    class ViewHolder(val binding: OrderDetailsItemsListItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun setRatingIcon(rating: Int) {
                try {

                    when (rating) {
                        1 -> {
                            binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivBad.setImageResource(R.drawable.empty_star_new)
                            binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                            binding.ivGood.setImageResource(R.drawable.empty_star_new)
                            binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                        }

                        2 -> {
                            binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                            binding.ivGood.setImageResource(R.drawable.empty_star_new)
                            binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)

                        }

                        3 -> {
                            binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                            binding.ivGood.setImageResource(R.drawable.empty_star_new)
                            binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                        }

                        4 -> {

                            binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                            binding.ivGood.setImageResource(R.drawable.filled_star_new)
                            binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                        }

                        5 -> {

                            binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivBad.setImageResource(R.drawable.filled_star_new)
                            binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                            binding.ivGood.setImageResource(R.drawable.filled_star_new)
                            binding.ivLovedIt.setImageResource(R.drawable.filled_star_new)
                        }

                        else -> {}
                    }

                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderDetailsItemsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val decimalFormat = DecimalFormat("0.00")
            val item = items[position]
            productId = item.productId.toString()
            productName = item.productTitle.toString()
            holder.binding.productName.text = item.productTitle
            holder.binding.variantName.text = item.variantTitle
            holder.binding.productPrice.text = "₹" + decimalFormat.format(item.originalTotal).toString()+"/-"
           /* holder.binding.quantityUnit.text = "Quantity :" + item.quantity.toString()
            holder.binding.unitPrice.text = "Item :₹" + item.unitPrice.toString()
            holder.binding.discountOnItem.text = "Discount :₹" + item.discount_total.toString()*/
            holder.binding.orderDate.text = order.createdAt

            holder.binding.ivVeryBad.setOnClickListener {
                ratingsGiven = 1
                holder.setRatingIcon(1)
           }
            holder.binding.ivBad.setOnClickListener {
                ratingsGiven = 2
                holder.setRatingIcon(2) }
            holder.binding.ivAverage.setOnClickListener {
                ratingsGiven = 3
                holder.setRatingIcon(3) }
            holder.binding.ivGood.setOnClickListener {
                ratingsGiven = 4
                holder.setRatingIcon(4) }
            holder.binding.ivLovedIt.setOnClickListener {
                ratingsGiven = 5
                holder.setRatingIcon(5) }

            holder.binding.rateThisProduct.setOnClickListener{
                val bottomSheetFragment = ReviewBottomSheetFragment.newInstance(productId, productName , ratingsGiven)
                bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            }

            holder.binding.checkStatusLayout.setOnClickListener {

                val intent = Intent(activity, DisplayOrderDetails::class.java)
                intent.putExtra("ORDER_JSON", Gson().toJson(order))
                intent.putExtra("PRODUCT_ID" , productId)
                intent.putExtra("PRODUCT_TITLE" , productName)
                activity.startActivity(intent)
            }

            if (item.thumbnail != null) {
                Log.i("Aarati Store cart image 1" , item.thumbnail.toString())
                Glide.with(holder.itemView.context)
                    .load(item.thumbnail) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .into(holder.binding.bottomHostImage)
            }

        }catch (e : Exception)
        {
            e.printStackTrace()
        }
    }



    override fun getItemCount() = items.size
}