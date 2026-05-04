package `in`.woloo.www.store.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreAddressesPopupBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.AddressesCustomAdapter
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.store.user_details.AddressListResponse
import `in`.woloo.www.utils.Logger

class AddressBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: StoreAddressesPopupBinding? = null
    private val binding get() = _binding!!
    private var storeViewModel: StoreViewModel? = null
    private var selectedItem: AddressList? = null
    private lateinit var adapter: AddressesCustomAdapter
    var id: String? = null
    var addressFull: String? = null
    var addressName: String? = null
    var fromFragment: String? = null


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreAddressesPopupBinding.inflate(inflater, container, false)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeViewModel!!.getAddressesList()

        fromFragment = arguments?.getString("FROM_FRAGMENT")

        storeViewModel!!.observeDeleteAddress().observe(viewLifecycleOwner, Observer {
                response ->
            response?.let {
                SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
                Toast.makeText(requireContext(), "Address Deleted Successfully", Toast.LENGTH_SHORT).show()
                dismiss()
                SharedPrefSettings.getPreferences.storeSelectedAddressId("")
                if(requireActivity() is WolooDashboard) {
                    (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.frm_contant, StoreListingFragment())
                        .commit()
                }
                else{
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(0, 0)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                }
              //  storeViewModel!!.getAddressesList()
            }

        })

        storeViewModel!!.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let {
                // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                Logger.i("Aarati Store" ,"Registration Error: $it")
                val  mobileNumber= SharedPrefSettings.getPreferences.fetchUserDetails()!!.mobile
                val decryptedData =  SharedPrefSettings.getPreferences.fetchDecryptedPassword().toString()
                val request = RegisterOnMedusaRequest()
                request.email = "$mobileNumber@gmail.com"
                request.password = decryptedData
                storeViewModel!!.getCustomerAuthPassOnMedusa(request)
            }
        })

        storeViewModel!!.observeCustomerAuthPassOnMedusa().observe(this , Observer
        {
            try{
                it?.let {
                    Logger.d("aarati token 4", it.token)
                    SharedPrefSettings.getPreferences.storeShopLoginToken(it.token)
                    Logger.d("aarati token 1", it.token)
                }
                storeViewModel!!.getAddressesList()
                storeViewModel!!.observeCustomerAuthPassOnMedusa().removeObservers(this)
            }catch(e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        })


        storeViewModel!!.observeAddressesList().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
//                Logger.i("Aarati Store", "setLiveData ${it.data!!.addresses!! + it.data!!.addresses!!.size}")
                /*for (i in it.addresses!!.indices)
                    Logger.i("Aarati Store", "setLiveData ${it.addresses!![i].id + it.addresses!![i].title}")*/
                val addresses = it.data?.addresses

                if(addresses.isNullOrEmpty())
                {
                    binding.addressRecycler.visibility = View.GONE
                    binding.noText.visibility = View.VISIBLE
                    binding.selectAddressFromList.visibility = View.GONE

                }
                else {

                    setupRecyclerView(it.data!!.addresses!!)
                    binding.addressRecycler.visibility = View.VISIBLE
                    binding.noText.visibility = View.GONE
                    binding.selectAddressFromList.visibility = View.VISIBLE
                }

            }

        })

        binding.addNewAddress.setOnClickListener {
          /*  val bottomSheetFragment = AddEditAddressBottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)*/

            val intent = Intent(requireActivity(), StoreAddressMapActivity::class.java)
            intent.putExtra("FROM_FRAGMENT" , fromFragment)
            startActivity(intent)

        }



        binding.selectAddressFromList.setOnClickListener {
            selectedItem?.let {
                    id = it.id
                addressFull = it.address_1
                addressName = it.address_name
                Log.d("ADDRESS ID SELECTED" , "$id")
                SharedPrefSettings.getPreferences.storeSelectedAddressId(id!!)
                SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)
                if(requireActivity() is WolooDashboard) {
                    (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.frm_contant, StoreListingFragment())
                        .commit()
                }
                else{
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(0, 0)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                }
            } ?: Toast.makeText(requireContext(), "Please Select Address", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }



    private fun setupRecyclerView(addressList : ArrayList<AddressList>) {
        adapter = AddressesCustomAdapter(requireActivity(),storeViewModel!! , addressList) { item ->
            selectedItem = item  // Update selected item
        }

        binding.addressRecycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.addressRecycler.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance(fromFragment:  String): AddressBottomSheetFragment {
            val fragment = AddressBottomSheetFragment()
            val args = Bundle()
            args.putString("FROM_FRAGMENT", fromFragment)
            fragment.arguments = args
            return fragment
        }


    }

}
