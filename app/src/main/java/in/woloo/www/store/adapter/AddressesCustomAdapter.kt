package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.databinding.StoreAddressListItemBinding
import `in`.woloo.www.databinding.StoreCircularSizeListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.SizeItemListAdapter.ViewHolder
import `in`.woloo.www.store.collections_response.CollectionsListData
import `in`.woloo.www.store.screens.AddEditAddressBottomSheetFragment
import `in`.woloo.www.store.screens.StoreAddressMapActivity
import `in`.woloo.www.store.user_details.AddressList

class AddressesCustomAdapter (private val context: FragmentActivity,
                              private val storeViewModel: StoreViewModel,
                              private var addressList: ArrayList<AddressList>,
                              private var onItemSelected: (AddressList) -> Unit
) : RecyclerView.Adapter<AddressesCustomAdapter.ViewHolder>() {

    lateinit var binding: StoreAddressListItemBinding
    private var selectedPosition = -1
    private var selectedItem: AddressList? = null
    var addressText : String? = null


    class ViewHolder(val binding:StoreAddressListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StoreAddressListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        val address = addressList[position]

try {
    if(address != null) {
        if (!address!!.address_1.isNullOrEmpty())
            addressText = address.address_1

        if (!address!!.address_2.isNullOrEmpty())
            addressText = addressText + ", " + address.address_2

        if (!address!!.city.isNullOrEmpty())
            addressText = addressText + ", " + address.city

        if (!address!!.province.isNullOrEmpty())
            addressText = addressText + ", " + address.province

        if (!address!!.postal_code.isNullOrEmpty())
            addressText = addressText + ", " + address.postal_code

        if (!address!!.country_code.isNullOrEmpty())
            addressText = addressText + ", " + address.country_code

        holder.binding.addressText.text = addressText!!.replace("~", "")

        if (!address!!.address_name.isNullOrEmpty())
            holder.binding.addressName.text = address.address_name


        holder.binding.editAddressButton.setOnClickListener {

        }




        if (selectedPosition == position) {
            holder.binding.bottomHostImage.setImageResource(R.drawable.select_address_filled) // Selected
        } else {
            holder.binding.bottomHostImage.setImageResource(R.drawable.select_address_empty) // Unselected
        }

        holder.binding.bottomHostImage.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedItem = address
            onItemSelected(address)
            notifyDataSetChanged()
            selectedPosition = holder.position
            //  notifyDataSetChanged()
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        holder.binding.deleteAddressButton.setOnClickListener {
            storeViewModel!!.getDeleteAddress(address.id.toString())
        }

        /* val currentItemId = address.id.toString()
        if(!SharedPrefSettings.getPreferences.fetchSelectedAddressId().isNullOrEmpty()
            &&
            SharedPrefSettings.getPreferences.fetchSelectedAddressId() == currentItemId)
        {
            holder.binding.bottomHostImage.setImageResource(R.drawable.select_address_filled) // Selected
        }
        else{
            holder.binding.bottomHostImage.setImageResource(R.drawable.select_address_empty)
        }*/


        holder.binding.editAddressButton.setOnClickListener {
            /*val bottomSheetFragment = AddEditAddressBottomSheetFragment.newInstance(address)
            bottomSheetFragment.show(context.supportFragmentManager, bottomSheetFragment.tag)*/
            val addressJson = Gson().toJson(address)
            val intent = Intent(context, StoreAddressMapActivity::class.java)
            intent.putExtra("ADDRESS_ID" , addressJson.toString())
            context.startActivity(intent)
        }
    }
}catch (e : Exception)
{
    e.printStackTrace()
}

    }

    override fun getItemCount(): Int = addressList.size


}