package `in`.woloo.www.application_kotlin.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.databinding.RateToiletSearchItemAdapterBinding


class NearByRateSearchAdapter(
    context: Context,
    private val storeList: List<NearByStoreResponse.DataSearch>
) : ArrayAdapter<NearByStoreResponse.DataSearch>(context, 0, storeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createBindingView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createBindingView(position, convertView, parent)
    }

    private fun createBindingView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: RateToiletSearchItemAdapterBinding
        val view: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = RateToiletSearchItemAdapterBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as RateToiletSearchItemAdapterBinding
        }

        val store = getItem(position)
        store?.let {
            binding.tvWolooStoreName.text = it.name
            binding.tvAddress.text = it.address
            binding.bottomHostLimit.text = it.cibilScore
            binding.bottomHostLimit.backgroundTintList =   ColorStateList.valueOf(Color.parseColor(it.cibilScoreColour))
            val rawImage = if (!it.image.isNullOrEmpty()) {
                it.image[0].orEmpty()
            } else {
                ""
            }
            val cleanedImage = rawImage
                .replace("[", "")
                .replace("]", "")
                .replace("'", "")
                .trim()
            var img = BuildConfig.NODE_API_URL + cleanedImage
            Glide.with(context)
                .load(img)
                .into(binding.ivWolooStore)
            // Example: Glide/Picasso for image
            // Glide.with(binding.ivWolooStore).load(it.imageUrl).into(binding.ivWolooStore)
        }

        return view
    }
}
