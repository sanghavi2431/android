package `in`.woloo.www.services.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.utilities.DateTimeUtils
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreOrderSummeryPopupBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.ServicesOrderListCustomAdapter
import `in`.woloo.www.store.orders_response.OrderSetResponse

import `in`.woloo.www.utils.Logger
import java.text.DecimalFormat

class ServicesOrderSummaryBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: StoreOrderSummeryPopupBinding? = null
    private val binding get() = _binding!!
    private lateinit var servicesViewModel: ServiceViewModel

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreOrderSummeryPopupBinding.inflate(inflater, container, false)

        servicesViewModel = ViewModelProvider(this).get<ServiceViewModel>(
            ServiceViewModel::class.java
        )

        try {
            val orderJson = arguments?.getString("ORDERSET")
            val orderSet = Gson().fromJson(orderJson, OrderSetResponse::class.java)

            Logger.i(
                "Aaratim Order set",
                "orderSet : ${orderSet.id} , ${orderSet.status} , ${orderSet.updatedAt.toString()}"
            )

            binding.orderSetId.text = orderSet.id.toString()
            binding.orderPlacedAt.text =
                DateTimeUtils.convertUtcToIst(orderSet.createdAt.toString())
            if (orderSet.delivery_date.toString().isEmpty() || orderSet.delivery_date == null) {
                binding.orderDeliveredLable.visibility = View.GONE
                binding.orderDeliveredAt.visibility = View.GONE
                binding.statusOfOrder.text = orderSet.status.toString()
            } else {
                binding.orderDeliveredAt.text =
                    DateTimeUtils.convertUtcToIst(orderSet.delivery_date.toString())
                binding.statusOfOrder.text = orderSet.fulfillmentStatus.toString()
            }


            val discountsApplied = orderSet.orders.orEmpty()
                .flatMap { it.items.orEmpty() }
                .flatMap { it.adjustments.orEmpty() }
                .mapNotNull { it.code }
                .toSet()
                .joinToString(",")
            if (discountsApplied.isEmpty())
                binding.offersAppliedNames.visibility = View.GONE
            else
                binding.offersAppliedNames.visibility = View.VISIBLE
            binding.orderDiscount.text = discountsApplied
            var decimalFormat = DecimalFormat("0.00")
            binding.productPrice.text = "₹ ${decimalFormat.format(orderSet.originalItemTotal)}/-"
            binding.discountPrice.text = "₹ ${decimalFormat.format(orderSet.discountTotal)}/-"
            binding.shippingAmount.text = "₹ ${decimalFormat.format(orderSet.shippingTotal)}/-"
            binding.totalAmount.text = "₹ ${decimalFormat.format(orderSet.total)}/-"
            binding.orderSavedAmount.text = "Saved: "
            binding.addressName.text =
                "${orderSet.cart!!.shippingAddress!!.firstName} ${orderSet.cart!!.shippingAddress!!.lastName}"
            binding.addressDetails.text = "${orderSet.cart!!.shippingAddress!!.address1}"




            binding.ordersRecycler.layoutManager = LinearLayoutManager(requireActivity())
            binding.ordersRecycler.adapter =
                ServicesOrderListCustomAdapter(
                    requireActivity(),
                    ArrayList(orderSet.orders!!),
                    requireActivity().supportFragmentManager
                )

        }catch (e : Exception)
        {

        }
        return binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    fun showSuccessDialog()
    {
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                requireActivity()
            )
            val child = layoutInflater.inflate(R.layout.store_payment_success_popup, null)
            alertDialogBuilder.setView(child)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
            alertDialog.setCancelable(false)
            val orderStatus = child.findViewById<TextView>(R.id.check_order_details)

            orderStatus.setOnClickListener { v: View? ->

                startActivity(
                    Intent(
                        this@ServicesOrderSummaryBottomSheetFragment.activity as ServicingCartActivity?,
                        ServiceOrderDetailsActivity::class.java
                    )
                )
                (this@ServicesOrderSummaryBottomSheetFragment.activity as ServicingCartActivity).finish()
                alertDialog.dismiss()
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
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
    }

    companion object {
        fun newInstance(orderset: OrderSetResponse): ServicesOrderSummaryBottomSheetFragment {
            val fragment = ServicesOrderSummaryBottomSheetFragment()
            val args = Bundle()
            val ordersetJson = Gson().toJson(orderset)
            args.putString("ORDERSET", ordersetJson)
            fragment.arguments = args
            return fragment
        }
    }
}

