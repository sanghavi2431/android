package `in`.woloo.www.store.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentInventoryBottomSheetBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.AddressesCustomAdapter
import `in`.woloo.www.store.adapter.CartItemsCustomAdapter
import `in`.woloo.www.store.adapter.StocknotavailableAdapter
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.utils.Logger

class InventoryBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentInventoryBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var storeViewModel: StoreViewModel? = null
    private var selectedItems: ArrayList<CartLineItems>? = null
    private var cartItemsCustomAdapter : StocknotavailableAdapter? = null
    private var listener: InventoryActionListener? = null




    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentInventoryBottomSheetBinding.inflate(inflater, container, false)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        binding.root.setOnClickListener {
           // if (savedInstanceState == null) {
                listener?.onNotifyMeClicked()
           // }
        }


        val cartJson = arguments?.getString("INVENTORY_ITEMS")
        if (!cartJson.isNullOrEmpty()) {
            val type = object : TypeToken<ArrayList<CartLineItems>>() {}.type
            selectedItems = Gson().fromJson(cartJson, type)  // ✅ Convert JSON to AddressList
            Log.d("AddressId", selectedItems.toString())
            cartItemsCustomAdapter =
                StocknotavailableAdapter(requireActivity(), selectedItems!!, storeViewModel!!)
            binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(requireActivity())
            binding.deliveryDetailsRecycler.adapter = cartItemsCustomAdapter
        }

        storeViewModel!!.observeNotifyUserForProduct().observe(viewLifecycleOwner , Observer{response ->
            response?.let {
                try{
                    Logger.i("Aarati Store Notify", "setLiveData ${it.toString()}")
                   // listener?.onNotifyMeClicked()
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }

            }
        })

        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InventoryActionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement InventoryActionListener")
        }

    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener?.onNotifyMeClicked()
    }

    interface InventoryActionListener {
        fun onNotifyMeClicked()
    }

companion object {

    fun newInstance(inventoryStockList: ArrayList<CartLineItems>): InventoryBottomSheetFragment {
        val fragment = InventoryBottomSheetFragment()
        val args = Bundle()
        val cartJson = Gson().toJson(inventoryStockList)
        args.putString("INVENTORY_ITEMS", cartJson)
        fragment.arguments = args
        return fragment
    }

    }
}