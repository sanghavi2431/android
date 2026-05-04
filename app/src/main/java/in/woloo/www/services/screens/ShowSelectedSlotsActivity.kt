package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityShowSelectedSlotsBinding
import `in`.woloo.www.databinding.FragmentDateTimeBottomSheetBinding
import `in`.woloo.www.services.SelectedDateTimeClass
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.TimeSlotClass
import `in`.woloo.www.services.adapter.SelectedServiceDateTimeAdapter
import `in`.woloo.www.services.adapter.ServiceDateAdapter
import `in`.woloo.www.services.adapter.TimeSlotAdapterClass
import `in`.woloo.www.store.cart_request_response.CartAddRequestHygiene
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartUpdateRequestHygiene
import `in`.woloo.www.store.cart_request_response.MetaDataLineItemsRequest
import `in`.woloo.www.store.cart_request_response.ServicesData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.utils.Logger
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ShowSelectedSlotsActivity : BottomSheetDialogFragment() {

    private lateinit var binding : ActivityShowSelectedSlotsBinding
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

        binding = ActivityShowSelectedSlotsBinding.inflate(inflater, container, false)

        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )


       // generateTimeSlots("07:30 AM" , "08:45 PM" , 60 , localDate)
        servicesViewModel!!.getCartList(SharedPrefSettings.getPreferences.fetchServiceCartId().toString())

        fromScreen = arguments?.getString("FROM_SCREEN").toString()
        Logger.i("Aarati Product details", fromScreen.toString())


        if(fromScreen.equals("CART_SCREEN_REMOVE")) {
            val cartJson = arguments?.getString("CART_ITEM_DETAILS")
            Logger.i("Aarati Product details", cartJson.toString())

            if (!cartJson.isNullOrEmpty()) {
                cartItem = Gson().fromJson(cartJson, CartLineItems::class.java)
                Logger.i("Aarati Cart details", cartItem.variant_id.toString())
                val datesArray: ArrayList<String> = ArrayList()
                val timesArray: ArrayList<String> = ArrayList()
                for(i in cartItem.metadata!!.servieceDataResponse!!.indices)
                {
                    datesArray.add(cartItem.metadata!!.servieceDataResponse!![i].serviceDate.toString())
                    timesArray.add(cartItem.metadata!!.servieceDataResponse!![i].serviceTime.toString())
                    Logger.w("Aarati Selected Cart details", datesArray[i].toString() + timesArray[i].toString())
                }
                getLocalDateRangeFromUtcString(datesArray , timesArray)
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

           if(selectedDate.isNotEmpty() &&  selectedTime.isNotEmpty()) {

               // ❷ Current list of booked slots for this line
               val existingServices = cartItem.metadata?.servieceDataResponse ?: arrayListOf()


               val updatedServicesList = ArrayList(
                   existingServices.filterNot { service ->
                       service.serviceDate == selectedDate &&
                               service.serviceTime == selectedTime &&
                               service.serviceArea == "1150sqfeet"
                   }
               )


               val newQuantity = (cartItem.quantity!!.toInt() - 1).coerceAtLeast(0)


               val request2 = CartUpdateRequestHygiene().apply {
                   quantity = newQuantity
                   metadata = MetaDataLineItemsRequest(services_data = updatedServicesList)
               }

               // ❻ Push to server
               servicesViewModel.getUpdateToCart(
                   SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                   cartItem.line_id.toString(),
                   request2
               )

           }
                else{
               showSelectDateTimeDialog()
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




    private fun getLocalDateRangeFromUtcString(
        datesArray: List<String>,
        timesArray: List<String>
    ) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Build the data set expected by SelectedServiceDateTimeAdapter
        val dateTimeList: List<SelectedDateTimeClass> = datesArray.indices.map { idx ->

            val localDate = LocalDate.parse(datesArray[idx], dateFormatter)

            val rawTime = timesArray.getOrNull(idx).orEmpty()
            val (start, end) = rawTime
                .split("-", limit = 2)                     // "10:00" , "12:00"
                .map { it.trim() }
                .let { parts ->
                    when (parts.size) {
                        2    -> parts[0] to parts[1]        // start–end
                        1    -> parts[0] to ""              // single time given
                        else -> ""      to ""               // malformed / empty
                    }
                }

            SelectedDateTimeClass(
                selectedDate      = localDate,
                selectedTimeSlots = TimeSlotClass(start, end)
            )
        }



        val adapter = SelectedServiceDateTimeAdapter(dateTimeList) { selected ->
            val selectedDateStr = selected.selectedDate.format(dateFormatter)
            Log.d("SelectedDate", "User selected: $selectedDateStr  ${selected.selectedTimeSlots}")

            // Pass back to activity (if needed)
            selectedDate = selectedDateStr
            selectedTime = selected.selectedTimeSlots.slotStartTime + "-" + selected.selectedTimeSlots.slotEndTime
        }

        binding.dateRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
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
        fun newInstance(cartItem: CartLineItems, fromScreen : String): ShowSelectedSlotsActivity {
            val fragment = ShowSelectedSlotsActivity()
            val args = Bundle()
            val cartJson = Gson().toJson(cartItem)
            args.putString("CART_ITEM_DETAILS", cartJson)
            args.putString("FROM_SCREEN", fromScreen)
            fragment.arguments = args
            return fragment
        }
    }
}