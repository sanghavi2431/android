package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentDateTimeBottomSheetBinding
import `in`.woloo.www.databinding.StoreAddressesPopupBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.TimeSlotClass
import `in`.woloo.www.services.adapter.ServiceDateAdapter
import `in`.woloo.www.services.adapter.ServicesCartItemCustomAdapter
import `in`.woloo.www.services.adapter.TimeSlotAdapterClass
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartAddRequestHygiene
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartUpdateRequestHygiene
import `in`.woloo.www.store.cart_request_response.MetaDataLineItemsRequest
import `in`.woloo.www.store.cart_request_response.ServicesData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.screens.AddEditAddressBottomSheetFragment
import `in`.woloo.www.store.screens.ShoppingCartActivity
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class DateTimeBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentDateTimeBottomSheetBinding
   // private lateinit var storeViewModel: StoreViewModel
    private lateinit var servicesViewModel: ServiceViewModel
    private lateinit var product: ProductListData
    private lateinit var cartItem: CartLineItems
    var variantPosition : Int = 0
    var fromScreen : String = ""
    private var isAlreadyInCart : Boolean = false
    private  var cartList : ArrayList<CartLineItems> = ArrayList()
    private var selectedDate : String = ""
    private var selectedTime : String = ""
    private var selectedSlotStartTime : String = ""
    private var selectedSlotEndTime : String = ""

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDateTimeBottomSheetBinding.inflate(inflater, container, false)
       /* storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )*/
        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )
        val utcNow = ZonedDateTime.now(ZoneId.of("UTC"))
        val localDate = utcNow.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate()
        getLocalDateRangeFromUtcString(utcNow.toString())
        generateTimeSlots("07:30 AM" , "08:45 PM" , 60 , localDate)
        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())

        binding.backButton.setOnClickListener{
            dismiss()
        }

        fromScreen = arguments?.getString("FROM_SCREEN").toString()
        Logger.i("Aarati Product details", fromScreen.toString())

    if(fromScreen.equals("PRODUCT_DETAILS")) {
        val productJson = arguments?.getString("PRODUCT_DETAILS")
        Logger.i("Aarati Product details", productJson.toString())
        product = Gson().fromJson(productJson, ProductListData::class.java)
        Logger.i("Aarati Product details", product.id.toString())
    }

        if(fromScreen.equals("CART_SCREEN")) {
            val cartJson = arguments?.getString("CART_ITEM_DETAILS")
            Logger.i("Aarati Product details", cartJson.toString())

            if (!cartJson.isNullOrEmpty()) {
                cartItem = Gson().fromJson(cartJson, CartLineItems::class.java)
                Logger.i("Aarati Cart details", cartItem.variant_id.toString())
            } else {
                Logger.w("Aarati Cart details", "CART_ITEM_DETAILS is null or empty")
                // Optionally assign a default object or handle gracefully
            }
        }



        initviews()
        return binding.root
           }

    fun initviews()
    {





        binding.proceedButton.setOnClickListener {
            Log.d("Aarati SelectedSlot", "Start: $selectedDate, Time: $selectedTime")
            Log.i("Aarati Store Cart DateTime" , SharedPrefSettings.getPreferences.fetchServiceCartId().toString())
            try{
            if(fromScreen.equals("CART_SCREEN_ADD"))
            {
                if(selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                    val cartJson = arguments?.getString("CART_ITEM_DETAILS")
                    val matchedItem = Gson().fromJson(cartJson, CartLineItems::class.java)

                    if (matchedItem != null) {
                        // Variant exists in cart
                        isAlreadyInCart = true


                        // Step 2: Retrieve existing services list from matchedItem
                        val existingServices =
                            matchedItem.metadata?.servieceDataResponse ?: arrayListOf()

                        val request2 = CartUpdateRequestHygiene()
                        request2.quantity = (matchedItem.quantity!!.toInt()) + 1
                        val serviceDataNew = ServicesData(
                            serviceDate = selectedDate,
                            serviceTime = selectedTime,
                            serviceArea = "1150sqfeet"
                        )

                        val updatedServicesList = ArrayList(existingServices)
                        updatedServicesList.add(serviceDataNew)


                        val meta = MetaDataLineItemsRequest(
                            services_data = updatedServicesList
                        )

                        request2.metadata = meta
                        servicesViewModel.getUpdateToCart(
                            SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                            matchedItem.line_id.toString(),
                            request2
                        )
                    }

                    /*  val request2 = CartUpdateRequestHygiene()
                request2.quantity = cartItem.quantity
                val serviceData = ServicesData(
                    serviceDate =selectedDate ,
                    serviceTime = selectedTime,
                    serviceArea = "1150sqfeet"
                )
                val servicesList = arrayListOf(serviceData)

                val meta = MetaDataLineItemsRequest(
                    services_data = servicesList
                )

                request2.metadata = meta
                servicesViewModel.getUpdateToCart(
                    SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                    cartItem.line_id.toString(),
                    request2
                )*/
                }
                else{
                    showSelectDateTimeDialog()
                }
            }
            else if(fromScreen.equals("CART_SCREEN_EDIT"))
            {
                val oldDate = arguments?.getString("OLD_DATE").toString()
                val oldTime = arguments?.getString("OLD_TIME").toString()
                val cartJson = arguments?.getString("CART_ITEM_DETAILS")
                val matchedItem = Gson().fromJson(cartJson, CartLineItems::class.java)

                if(selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {

                if (matchedItem != null) {
                    isAlreadyInCart = true

                    val existingServices = matchedItem.metadata?.servieceDataResponse ?: arrayListOf()

                    val serviceDataNew = ServicesData(
                        serviceDate = selectedDate,
                        serviceTime = selectedTime,
                        serviceArea = "1150sqfeet"
                    )

                    val updatedServicesList = ArrayList(existingServices)

                    val indexToUpdate = updatedServicesList.indexOfFirst {
                        it.serviceDate == oldDate && it.serviceTime == oldTime && it.serviceArea == "1150sqfeet"
                    }

                    if (indexToUpdate != -1) {
                        updatedServicesList[indexToUpdate] = serviceDataNew
                    } else {
                        Log.w("CartUpdate", "No matching service entry found to update.")
                    }

                    val request2 = CartUpdateRequestHygiene().apply {
                        quantity = matchedItem.quantity!!.toInt()
                        metadata = MetaDataLineItemsRequest(
                            services_data = updatedServicesList
                        )
                    }

                    servicesViewModel.getUpdateToCart(
                        SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                        matchedItem.line_id.toString(),
                        request2
                    )
                } }
                else{
                    showSelectDateTimeDialog()
                }

            }
            else if(fromScreen.equals("PRODUCT_DETAILS"))
                {

                    val matchedItem = cartList.find { it.variant_id == product.variants!![variantPosition].id }
                    if(selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                        if (matchedItem != null) {
                            // Variant exists in cart
                            isAlreadyInCart = true


                            // Step 2: Retrieve existing services list from matchedItem
                            val existingServices =
                                matchedItem.metadata?.servieceDataResponse ?: arrayListOf()

                            val request2 = CartUpdateRequestHygiene()
                            request2.quantity = (matchedItem.quantity!!.toInt()) + 1
                            val serviceDataNew = ServicesData(
                                serviceDate = selectedDate,
                                serviceTime = selectedTime,
                                serviceArea = "1150sqfeet"
                            )

                            val updatedServicesList = ArrayList(existingServices)
                            updatedServicesList.add(serviceDataNew)


                            val meta = MetaDataLineItemsRequest(
                                services_data = updatedServicesList
                            )

                            request2.metadata = meta
                            servicesViewModel.getUpdateToCart(
                                SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                                matchedItem.line_id.toString(),
                                request2
                            )

                        } else {
                            // Not found
                            isAlreadyInCart = false

                            val request = CartAddRequestHygiene()
                            request.variant_id = arguments?.getString("VARIANT_ID").toString()
                            request.quantity = 1
                            val serviceData = ServicesData(
                                serviceDate = selectedDate,
                                serviceTime = selectedTime,
                                serviceArea = "1150sqfeet"
                            )
                            val servicesList = arrayListOf(serviceData)

                            val meta = MetaDataLineItemsRequest(
                                services_data = servicesList
                            )

                            request.metadata = meta
                            servicesViewModel!!.getAddToCartHygiene(
                                SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                                request
                            )

                        }
                    }else{
                        showSelectDateTimeDialog()
                    }


            }
            }catch (e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }
        }

        servicesViewModel!!.observeCartList().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    cartList = it.cart!!.items!!

                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        servicesViewModel!!.observeUpdateToCart().observe(this, Observer { response ->
            response?.let {
                try{
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(0, 0)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)

                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        servicesViewModel.observeAddToCart().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.cart!!}")
                    val matchFound =
                        it.cart!!.items!!.find { item -> item.variant_id == product.variants!![variantPosition].id }


                    val intent = Intent(requireContext() , ServicingCartActivity::class.java)
                    intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
                    intent.putExtra("DATEFORSERVICE" , selectedDate)
                    intent.putExtra("TIMEFORSERVICE" , selectedTime)
                    startActivity(intent)


                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }


            }
        })
    }



    fun getLocalDateRangeFromUtcString(utcDateString: String) {
        val utcZonedDateTime = ZonedDateTime.parse(utcDateString)

        val localDate = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate()

        val next7Days = (0..6).map { localDate.plusDays(it.toLong()) }.toMutableList()


        val adapter = ServiceDateAdapter(next7Days) { selectedDateLocalDate ->
            val selectedDateString = selectedDateLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            Log.d("SelectedDate", "User selected: $selectedDateString")

            // Pass to Activity
            (requireActivity())?.let {
                selectedDate = selectedDateString
                Log.d("Fragment", "Selected date set in Activity: ${selectedDate}")
            }

            selectedDate = selectedDateString

            // ✅ Now call the time slot generator
            val slots = generateTimeSlots(
                startTimeString = "07:30 AM",
                endTimeString = "06:45 PM",
                durationInMinutes = 60,
                selectedDate = selectedDateLocalDate
            )

            Log.d("SlotCount", "Total slots: ${slots.size}")


    }



        val spanCount = 4
        val layoutManager = GridLayoutManager(requireContext(), spanCount)


        binding.dateRecyclerView.layoutManager = layoutManager
        binding.dateRecyclerView.adapter = adapter
    }

    fun generateTimeSlots(
        startTimeString: String,
        endTimeString: String,
        durationInMinutes: Int,
        selectedDate: LocalDate
    ): List<TimeSlotClass> {
        val formatterInput = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        val formatterOutput = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

        Log.d("Aarati Time", "Generating slots from $startTimeString to $endTimeString on $selectedDate")


       /* val today = LocalDate.now()
        val now = LocalTime.now()*/

        val systemZone = ZoneId.systemDefault()
        val nowZoned = ZonedDateTime.now(systemZone)
        val today = nowZoned.toLocalDate()
        val now = nowZoned.toLocalTime()

        val startTime = LocalTime.parse(startTimeString, formatterInput)
        val endTime = LocalTime.parse(endTimeString, formatterInput)

        val adjustedStartTime = if (selectedDate.isEqual(today) && now.isAfter(startTime)) {
            // Round current time up to next full duration slot
            val minutesPastSlot = now.minute % durationInMinutes
            val minutesToAdd = if (minutesPastSlot == 0) 0 else (durationInMinutes - minutesPastSlot)
            now.plusMinutes(minutesToAdd.toLong()).withSecond(0).withNano(0)
        } else {
            startTime
        }

        Log.d("Aarati Time", "Now: $now, AdjustedStartTime: $adjustedStartTime, EndTime: $endTime")

        if (adjustedStartTime >= endTime) {
            Log.d("Aarati Time", "Start time is after or equal to end time, no slots generated.")
            return emptyList()
        }


        val slots = mutableListOf<TimeSlotClass>()
        var slotStart = adjustedStartTime

        while (slotStart.plusMinutes(durationInMinutes.toLong()) <= endTime) {
            val slotEnd = slotStart.plusMinutes(durationInMinutes.toLong())

            slots.add(
                TimeSlotClass(
                    slotStartTime = slotStart.format(formatterOutput),
                    slotEndTime = slotEnd.format(formatterOutput)
                )
            )

            slotStart = slotEnd
        }

        for(i in slots.indices)
        {
            Logger.i("Aarati Time Slots" , "${selectedDate}  ${slots.get(i).slotStartTime} and ${slots.get(i).slotEndTime}")
        }



        val adapter = TimeSlotAdapterClass(slots) { selectedSlot ->

            val selectedStartTime = selectedSlot.slotStartTime
            val selectedEndTime = selectedSlot.slotEndTime


            Log.d("SelectedSlot", "Start: $selectedStartTime, End: $selectedEndTime")

            selectedSlotStartTime = selectedStartTime
            selectedSlotEndTime = selectedEndTime
            selectedTime = selectedStartTime + "-" + selectedEndTime
        }

        binding.timeSlotSelecter.layoutManager = GridLayoutManager(requireContext() , 2)
        binding.timeSlotSelecter.adapter = adapter

        Log.d("Aarati SelectedSlot", "Start: $selectedDate, Time: $selectedTime")

        return slots
    }

    @SuppressLint("SetTextI18n")
    private fun showSelectDateTimeDialog() {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val llStartFreeTrial = dialog.findViewById<TextView>(R.id.tv_msg)
            val llclose = dialog.findViewById<TextView>(R.id.btnCloseDialog)
            llStartFreeTrial.text = "Select proper date and time"
            llclose.setOnClickListener{dialog.dismiss()}
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(variantId : String ,product: ProductListData, fromScreen : String): DateTimeBottomSheetFragment {
            val fragment = DateTimeBottomSheetFragment()
            val args = Bundle()
            val productJson = Gson().toJson(product)
            args.putString("VARIANT_ID", variantId)
            args.putString("PRODUCT_DETAILS", productJson)
            args.putString("FROM_SCREEN", fromScreen)
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(cartItem: CartLineItems , fromScreen : String): DateTimeBottomSheetFragment {
            val fragment = DateTimeBottomSheetFragment()
            val args = Bundle()
            val cartJson = Gson().toJson(cartItem)
            args.putString("CART_ITEM_DETAILS", cartJson)
            args.putString("FROM_SCREEN", fromScreen)
            fragment.arguments = args
            return fragment
        }

        @JvmStatic
        fun newInstance(cartItem: CartLineItems , fromScreen : String , oldDate: String , oldTime : String): DateTimeBottomSheetFragment {
            val fragment = DateTimeBottomSheetFragment()
            val args = Bundle()
            val cartJson = Gson().toJson(cartItem)
            args.putString("CART_ITEM_DETAILS", cartJson)
            args.putString("FROM_SCREEN", fromScreen)
            args.putString("OLD_DATE", oldDate)
            args.putString("OLD_TIME", oldTime)
            fragment.arguments = args
            return fragment
        }
    }
}