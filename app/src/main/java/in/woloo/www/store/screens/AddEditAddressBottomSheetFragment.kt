package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreAddEditAddressPopupBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.auth_request_response.City
import `in`.woloo.www.store.auth_request_response.State
import `in`.woloo.www.store.auth_request_response.UpdateCustomerRequest
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.store.user_details.CreateUpdateAddressRequest
import `in`.woloo.www.utils.CustomProgressView


class AddEditAddressBottomSheetFragment  : BottomSheetDialogFragment() {

    private var _binding: StoreAddEditAddressPopupBinding? = null
    private val binding get() = _binding!!
    private var storeViewModel: StoreViewModel? = null
    private var selectedItem: AddressList? = null
    var newCity : String = ""
    var newZipCode : String = ""
    var newState : String = ""
    var newArea : String = ""
    var newFlat : String = ""
    var newAddress : String = ""
    private var selectedPosition: Boolean = false
    private lateinit var progressView: CustomProgressView
    var fromFragment : String = ""


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreAddEditAddressPopupBinding.inflate(inflater, container, false)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        progressView = CustomProgressView(requireActivity())

        SharedPrefSettings.getPreferences.storeIsReturningFromBottomSheet(true)

        val addressJson = arguments?.getString("ADDRESS_ID")  // ✅ Get JSON String
        fromFragment = arguments?.getString("FROM_FRAGMENT").toString()

        if(addressJson.isNullOrEmpty())
        {
            newAddress = arguments?.getString("NEW_ADDRESS").toString()
             newCity = arguments?.getString("CITY_NEW").toString()
             newZipCode = arguments?.getString("PIN_CODE").toString()
             newState = arguments?.getString("STATE_NEW").toString()
             newArea = arguments?.getString("AREA").toString()
             newFlat =arguments?.getString("BUILDING").toString()
            binding.apartmentRoadArea.setText(newAddress.toString())
            binding.cityAddresser.setText(newCity.toString())
            binding.pincodeAddress.setText(newZipCode.toString())
            binding.stateAddresser.setText(newState.toString())
            binding.flatNum.setText(newFlat.toString())
            binding.locality.setText(newArea.toString())
        }



        try {


            storeViewModel!!.observeAddAddress().observe(viewLifecycleOwner, Observer {
                    response ->
                response?.let {

                    Log.d("SELECTED POSITION"," $selectedPosition ")

                    if (selectedPosition) {
                        SharedPrefSettings.getPreferences.storeSelectedAddressId(it.data!!.customer!!.addresses!!.lastOrNull()!!.id!!)
                        SharedPrefSettings.getPreferences.storeDefaultAddressId(it.data!!.customer!!.addresses!!.lastOrNull()!!.id!!)
                        Log.d("Aarati defaault address SELECTED POSITION"," $selectedPosition  ${SharedPrefSettings.getPreferences.fetchDefaultAddressId()}")
                        val findDefaultAddress = it.data!!.customer!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchDefaultAddressId() }!!
                        val request = UpdateCustomerRequest()
                        request.firstName = findDefaultAddress.first_name.toString()
                        request.lastName = findDefaultAddress.last_name.toString()
                        storeViewModel!!.getUpdateCustomer(SharedPrefSettings.getPreferences.fetchStoreCustomerId()!! , request)
                    }
                    else{
                        SharedPrefSettings.getPreferences.storeDefaultAddressId("")
                        val request = UpdateCustomerRequest()
                        request.firstName = ""
                        request.lastName = ""
                        storeViewModel!!.getUpdateCustomer(SharedPrefSettings.getPreferences.fetchStoreCustomerId()!! , request)
                    }
                    if(requireActivity() is WolooDashboard) {
                        (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.frm_contant, StoreListingFragment())
                            .commit()
                    }
                    else{
                        if(fromFragment.equals("SERVICE" ,ignoreCase = true)) {
                            requireActivity().finish()
                            val intent = Intent(requireActivity(), WolooDashboard::class.java)
                            intent.putExtra("ServicesListFragment", "ServicesListFragment")
                            startActivity(intent)
                        }
                        else{
                            requireActivity().finish()
                            val intent = Intent(requireActivity(), WolooDashboard::class.java)
                            intent.putExtra("StoreListingFragment", "StoreListingFragment")
                            startActivity(intent)
                        }
                      /*  requireActivity().overridePendingTransition(0, 0)
                        startActivity(intent)
                        requireActivity().overridePendingTransition(0, 0)*/
                    }
                }
                dismiss()
              //  requireActivity().finish()
            })

            storeViewModel!!.observeUpdateAddress().observe(viewLifecycleOwner , Observer{
                    response ->
                response?.let {
                    Toast.makeText(requireContext(), "Address Updated Successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                    if (selectedPosition) {
                        SharedPrefSettings.getPreferences.storeSelectedAddressId(it.data!!.customer!!.addresses!!.lastOrNull()!!.id!!)
                        SharedPrefSettings.getPreferences.storeDefaultAddressId(it.data!!.customer!!.addresses!!.lastOrNull()!!.id!!)
                        Log.d("Aarati defaault address SELECTED POSITION"," $selectedPosition  ${SharedPrefSettings.getPreferences.fetchDefaultAddressId()}")
                        val findDefaultAddress = it.data!!.customer!!.addresses!!.find { it.id == SharedPrefSettings.getPreferences.fetchDefaultAddressId() }!!
                        val request = UpdateCustomerRequest()
                        request.firstName = findDefaultAddress.first_name.toString()
                        request.lastName = findDefaultAddress.last_name.toString()
                        storeViewModel!!.getUpdateCustomer(SharedPrefSettings.getPreferences.fetchStoreCustomerId()!! , request)
                    }
                    else{
                        SharedPrefSettings.getPreferences.storeDefaultAddressId("")
                        val request = UpdateCustomerRequest()
                        request.firstName = ""
                        request.lastName = ""
                        storeViewModel!!.getUpdateCustomer(SharedPrefSettings.getPreferences.fetchStoreCustomerId()!! , request)
                    }
                    if(requireActivity() is WolooDashboard) {
                        (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.frm_contant, StoreListingFragment())
                            .commit()
                    }
                    else{
                        if(fromFragment.equals("SERVICE" ,ignoreCase = true)) {
                            requireActivity().finish()
                            val intent = Intent(requireActivity(), WolooDashboard::class.java)
                            intent.putExtra("ServicesListFragment", "ServicesListFragment")
                            startActivity(intent)
                        }
                        else{
                            requireActivity().finish()
                            val intent = Intent(requireActivity(), WolooDashboard::class.java)
                            intent.putExtra("StoreListingFragment", "StoreListingFragment")
                            startActivity(intent)
                        }
                       /* requireActivity().overridePendingTransition(0, 0)
                        startActivity(intent)
                        requireActivity().overridePendingTransition(0, 0)*/
                    }

                   // activity?.finish()

                }
            })



            if (!addressJson.isNullOrEmpty()) {
                val type = object : TypeToken<AddressList>() {}.type
                selectedItem = Gson().fromJson(addressJson, type)  // ✅ Convert JSON to AddressList
                Log.d("AddressId", selectedItem.toString())

                if (selectedItem != null) {

                    binding.cartHeaderText.setText("Edit Address")
                    if(!selectedItem!!.first_name.isNullOrEmpty())
                    binding.firstNameAddresser.setText(selectedItem!!.first_name)
                    if(!selectedItem!!.last_name.isNullOrEmpty())
                    binding.lastNameAddresser.setText(selectedItem!!.last_name)
                    if(!selectedItem!!.address_1.isNullOrEmpty()) {
                        val parts = selectedItem!!.address_1!!.split("~")
                        binding.flatNum.setText(parts[0].trimStart())
                        binding.locality.setText(parts[1].trimStart())
                        binding.apartmentRoadArea.setText(parts[2].trimStart())
                    }
                    if(!selectedItem!!.address_2.isNullOrEmpty())
                    binding.apartmentRoadAreaTwo.setText(selectedItem!!.address_2!!.trimStart())
                    if(!selectedItem!!.postal_code.isNullOrEmpty())
                    binding.pincodeAddress.setText(selectedItem!!.postal_code)
                    if(!selectedItem!!.city.isNullOrEmpty())
                    binding.cityAddresser.setText(selectedItem!!.city)
                    if(!selectedItem!!.province.isNullOrEmpty())
                    binding.stateAddresser.setText(selectedItem!!.province)
                    if(!selectedItem!!.phone.isNullOrEmpty())
                    binding.phoneAddresser.setText(selectedItem!!.phone)
                    if(!selectedItem!!.address_name.isNullOrEmpty())
                    binding.addressName.setText(selectedItem!!.address_name)
                    binding.addAddressButton.setText("Update Address")
                }
            }

        }catch (e : Exception)
        {
            e.printStackTrace()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            dismiss()
        }

        binding.defaultAddressImage.setOnClickListener {
            if (!selectedPosition) {
                binding.defaultAddressImage.setImageResource(R.drawable.select_address_filled)
                selectedPosition = true
            } else {
                binding.defaultAddressImage.setImageResource(R.drawable.select_address_empty)
                selectedPosition = false
            }
        }
           /* binding.apartmentRoadArea.setOnClickListener{
              //  Log.d("Aarati Address" , "On Click")

            }*/


            binding.addAddressButton.setOnClickListener{
           binding.addAddressButton.isClickable =false
           progressView.show()
           validate()
       }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                val focusedView = dialog?.currentFocus
                focusedView?.let {
                    binding.scrollBottomSheet.smoothScrollTo(0, it.bottom)
                }
            }
        }



    }



    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SuspiciousIndentation")
    fun validate()
    {
        if(!binding.firstNameAddresser.text.toString().trimStart().isNullOrEmpty())
        {
            if(!binding.lastNameAddresser.text.toString().trimStart().isNullOrEmpty())
            {
                if(!binding.flatNum.text.toString().trimStart().isNullOrEmpty()) {
                    if (!binding.locality.text.toString().trimStart().isNullOrEmpty()) {

                        if (!binding.apartmentRoadArea.text.toString().trimStart()
                                .isNullOrEmpty()
                        ) {
                            if (!binding.stateAddresser.text.toString().trimStart()
                                    .isNullOrEmpty()
                            ) {
                                /* val selectedStateName = binding.stateAddresser.text.toString()
                        val selectedCityName = binding.cityAddresser.text.toString()

                        if (!stateNameToState.containsKey(selectedStateName)) {
                            binding.stateAddresser.hint = "select valid state from list"
                            binding.stateAddresser.setText("")
                            return
                        }

                        val selectedState = stateNameToState[selectedStateName]!!*/

                                if (!binding.cityAddresser.text.toString().trimStart()
                                        .isNullOrEmpty()
                                ) {
                                    /* val cities = getCitiesForState(requireContext(), selectedState.id)
                            val cityNames = cities.map { it.name }

                            if (!cityNames.contains(selectedCityName)) {
                                binding.cityAddresser.hint = "select valid city from list"
                                binding.cityAddresser.setText("")
                                return
                            }*/
                                    if (!binding.pincodeAddress.text.toString().trimStart()
                                            .isNullOrEmpty()
                                    ) {

                                        if (binding.pincodeAddress.text!!.length == 6) {

                                            if (!binding.phoneAddresser.text.toString().trimStart()
                                                    .isNullOrEmpty()
                                            ) {

                                                if (binding.phoneAddresser.text!!.length == 10) {

                                                    /*  if (binding.firstNameAddresser.text!!.startsWith(" "))
                                                binding.firstNameAddresser.setText(binding.firstNameAddresser.text!!.trimStart())
                                                if (binding.lastNameAddresser.text!!.startsWith(" "))
                                                    binding.lastNameAddresser.setText(binding.lastNameAddresser.text!!.trimStart())*/

                                                    if (selectedItem != null) {
                                                        val request = CreateUpdateAddressRequest()
                                                        request.first_name =
                                                            binding.firstNameAddresser.text.toString()
                                                                .trimStart()
                                                        request.last_name =
                                                            binding.lastNameAddresser.text.toString()
                                                                .trimStart()
                                                        request.address_1 =
                                                            "${binding.flatNum.text.toString().trimStart()} ~ ${binding.locality.text.toString().trimStart()} ~ ${binding.apartmentRoadArea.text.toString().trimStart()}"
                                                        if (binding.apartmentRoadAreaTwo.text != null)
                                                            request.address_2 =
                                                                binding.apartmentRoadAreaTwo.text.toString()
                                                                    .trimStart()
                                                        request.city =
                                                            binding.cityAddresser.text.toString()
                                                        request.province =
                                                            binding.stateAddresser.text.toString()
                                                        request.phone =
                                                            binding.phoneAddresser.text.toString()
                                                        request.address_name =
                                                            binding.addressName.text.toString()
                                                        request.postal_code =
                                                            binding.pincodeAddress.text.toString()
                                                        storeViewModel!!.getUpdateAddress(
                                                            selectedItem!!.id.toString(),
                                                            request
                                                        )
                                                        binding.addAddressButton.isEnabled = true
                                                        progressView.hide()
                                                    } else {
                                                        val request = CreateUpdateAddressRequest()
                                                        request.first_name =
                                                            binding.firstNameAddresser.text.toString()
                                                        request.last_name =
                                                            binding.lastNameAddresser.text.toString()
                                                        request.address_1 =
                                                            (binding.flatNum.text.toString().trimStart() + "~" + binding.locality.text.toString().trimStart() + "~" + binding.apartmentRoadArea.text.toString()).trimStart()
                                                        if (binding.apartmentRoadAreaTwo.text != null)
                                                            request.address_2 =
                                                                binding.apartmentRoadAreaTwo.text.toString()
                                                                    .trimStart()
                                                        request.city =
                                                            binding.cityAddresser.text.toString()
                                                        request.province =
                                                            binding.stateAddresser.text.toString()
                                                        request.phone =
                                                            binding.phoneAddresser.text.toString()
                                                        request.address_name =
                                                            binding.addressName.text.toString()
                                                        request.postal_code =
                                                            binding.pincodeAddress.text.toString()
                                                        storeViewModel!!.getAddAddress(request)
                                                        binding.addAddressButton.isEnabled = true
                                                        progressView.hide()
                                                    }
                                                } else {
                                                    binding.phoneAddresser.setHint("Enter valid Phone Number")
                                                    binding.phoneAddresser.setText("")
                                                }
                                            } else {
                                                binding.phoneAddresser.setHint("Enter Phone Number")
                                            }
                                        } else {
                                            binding.pincodeAddress.setHint("Enter Valid Pincode")
                                            binding.pincodeAddress.setText("")
                                        }
                                    } else {
                                        binding.pincodeAddress.setText("")
                                        binding.pincodeAddress.setHint("Enter Pincode")
                                    }
                                } else {
                                    binding.cityAddresser.setText("")
                                    binding.cityAddresser.setHint("Select City")
                                }
                            } else {
                                binding.stateAddresser.setText("")
                                binding.stateAddresser.setHint("Select State")
                            }
                        } else {
                            binding.apartmentRoadArea.setText("")
                            binding.apartmentRoadArea.setHint("Enter Apartment Name/Road/Area")
                        }
                    } else {
                        binding.locality.setText("")
                        binding.locality.setHint("Locality")
                    }
                }
                else{
                binding.flatNum.setText("")
                binding.flatNum.setHint("Flat/Building")
            }

            }
            else{
                binding.lastNameAddresser.setText("")
                binding.lastNameAddresser.setHint("Enter Last Name")
        }
        }
        else{
            binding.firstNameAddresser.setText("")
            binding.firstNameAddresser.setHint("Enter First Name")
        }
    }

    companion object {
        fun newInstance(address: AddressList , fromFragment: String): AddEditAddressBottomSheetFragment {
            val fragment = AddEditAddressBottomSheetFragment()
            val args = Bundle()
            val addressJson = Gson().toJson(address)
            args.putString("ADDRESS_ID", addressJson)
            args.putString("FROM_FRAGMENT", fromFragment)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(fromFragment:String , cityNew : String , pinCodeNew :String ,
                        stateNew:String ,area:String, building:String , newAddress : String): AddEditAddressBottomSheetFragment {
            val fragment = AddEditAddressBottomSheetFragment()
            val args = Bundle()
            args.putString("FROM_FRAGMENT", fromFragment)
            args.putString("CITY_NEW", cityNew)
            args.putString("PIN_CODE", pinCodeNew)
            args.putString("STATE_NEW", stateNew)
            args.putString("AREA", area)
            args.putString("BUILDING", building)
            args.putString("NEW_ADDRESS" , newAddress)
            fragment.arguments = args
            return fragment
        }
    }



}
