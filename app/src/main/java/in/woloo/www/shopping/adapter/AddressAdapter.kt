package `in`.woloo.www.shopping.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import `in`.woloo.www.R
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import `in`.woloo.www.shopping.model.AddressModel

class AddressAdapter(workList: MutableList<AddressModel>) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
    var progressDialog: ProgressDialog? = null
    private val workList: MutableList<AddressModel> = workList

    var mCallback: SelectAddress? = null

    var context1: Context? = null


    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val deleteUrl = Config.hostname + "delete_address_api.php"


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context = view.context
        var progressDialog: ProgressDialog? = null


        var label: TextView =
            view.findViewById<View>(R.id.label) as TextView
        var name: TextView =
            view.findViewById<View>(R.id.name) as TextView
        var address: TextView =
            view.findViewById<View>(R.id.address) as TextView
        var phone: TextView =
            view.findViewById<View>(R.id.phone) as TextView
        var delete: TextView =
            view.findViewById<View>(R.id.delete) as TextView
        var select_radio: RadioButton = view.findViewById<View>(R.id.select_radio) as RadioButton


        val handler: Handler = Handler()

        init {
            context1 = view.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list_row, parent, false)

        return MyViewHolder(itemView)
    }


    fun setOnUpdateListener(mCallback: SelectAddress?) {
        this.mCallback = mCallback
    }

    interface SelectAddress {
        fun selectAddress(id: String?, address: String?)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val work: AddressModel = workList[position]


        if (position != 0) {
            holder.label.visibility = View.GONE
        }

        holder.name.setText(work.name)
        holder.phone.text = "Phone: " + work.phone


        holder.address.setText(work.flat_building + " , " + work.landmark + " , " + work.area + " , " + work.city + "-" + work.pincode + " " + work.state)


        holder.select_radio.setOnCheckedChangeListener { compoundButton, b ->
            ShoppingFragment.pincode = work.pincode
            ShoppingFragment.current_pincode = work.pincode
            mCallback!!.selectAddress(
                work.id,
                work.flat_building + " , " + work.landmark + " , " + work.area + " , " + work.city + "-" + work.pincode + " " + work.state
            )
        }


        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(context1)
            builder.setMessage("Are you sure?")
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { dialog, id ->
                    Toast.makeText(
                        context1,
                        "Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    workList.removeAt(position)


                    // Call Delete API Here
                    notifyDataSetChanged()
                    deleteAddress(work.id!!)
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun getItemCount(): Int {
        return workList.size
    }


    private fun deleteAddress(id: String) {
        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(context1)

        //String Request initialized
        mStringRequest = object : StringRequest(
            Method.GET,
            "$deleteUrl?delete_id=$id", Response.Listener {
                try {
                } catch (e: Exception) {
                    //  Toast.makeText(getActivity(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            },
            Response.ErrorListener {
                //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: Map<String, String> = HashMap()

                return params
            }
        }

        mRequestQueue!!.add(mStringRequest)
    }
}