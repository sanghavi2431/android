package `in`.woloo.www.shopping.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import `in`.woloo.www.R
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.fragments.CartFragment
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import `in`.woloo.www.shopping.model.CartModel
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.floor

class CartAdapter(workList: MutableList<CartModel>) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    var progressDialog: ProgressDialog? = null
    private val workList: MutableList<CartModel> = workList

    var mCallback: UpdateTotalSummaryInfo? = null

    private var mRequestQueue: RequestQueue? = null
    private var mStringRequest: StringRequest? = null
    private val productsUrl = Config.hostname + "get_product_details_api.php"

    var context1: Context? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context = view.context
        var progressDialog: ProgressDialog? = null


        var pro_name: TextView =
            view.findViewById<View>(R.id.pro_name) as TextView
        var pro_price: TextView =
            view.findViewById<View>(R.id.pro_price) as TextView
        var total_amount: TextView =
            view.findViewById<View>(R.id.total_amount) as TextView
        var point_used: TextView =
            view.findViewById<View>(R.id.point_used) as TextView
        var coupon_text: TextView =
            view.findViewById<View>(R.id.coupon_text) as TextView
        var no_pincode_delivery: TextView =
            view.findViewById<View>(R.id.no_pincode_delivery) as TextView
        var decrementButton: TextView =
            view.findViewById<View>(R.id.decrementButton) as TextView
        var incrementButton: TextView =
            view.findViewById<View>(R.id.incrementButton) as TextView
        var quantity_textview: EditText =
            view.findViewById<View>(R.id.quantity_textview) as EditText
        var pro_image: ImageView =
            view.findViewById<View>(R.id.pro_image) as ImageView
        var delete_icon: ImageView =
            view.findViewById<View>(R.id.delete_icon) as ImageView


        val handler: Handler = Handler()

        init {
            context1 = view.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_list_row, parent, false)

        return MyViewHolder(itemView)
    }


    fun setOnUpdateListener(mCallback: UpdateTotalSummaryInfo?) {
        this.mCallback = mCallback
    }

    interface UpdateTotalSummaryInfo {
        fun updateTotalSummaryInfo()
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val work: CartModel = workList[position]


        holder.pro_name.setText(work.pro_name)
        holder.pro_price.text = "Rs." + work.price
        holder.total_amount.text = "To be paid Rs. " + work.total_amount
        holder.point_used.text = "Point Used: " + work.point_used
        holder.quantity_textview.setText(work.qty)

        holder.quantity_textview.isEnabled = false

        if (!work.coupon_value.equals("", ignoreCase = true)) {
            holder.coupon_text.visibility = View.VISIBLE
            if (work.coupon_value_unit.equals("per", ignoreCase = true)) {
                var c_dicount_on_product = 0f
                c_dicount_on_product = work.total_amount!!.toInt() * work.coupon_value!!.toFloat() / 100


                holder.coupon_text.text =
                    "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + work.coupon_value + "% Coupon Discount"
            } else {
                var c_dicount_on_product = 0f
                try {
                    c_dicount_on_product = work.qty!!.toInt() * work.coupon_value!!.toFloat()
                } catch (e: Exception) {
                }
                holder.coupon_text.text =
                    "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + "Rs." + work.coupon_value + " flat Coupon Discount on a product"
            }
        } else {
            holder.coupon_text.visibility = View.GONE
        }


        var imageUri = Config.hostname + "../images/" + work.image
        if (work.image!!.contains("http")) {
            imageUri = work.image!!
        }


        Picasso.get().load(imageUri).transform(RoundedTransformation(10, 0)).into(holder.pro_image)



        holder.decrementButton.setOnClickListener(View.OnClickListener {
            var value = 0
            try {
                value = holder.quantity_textview.text.toString().trim { it <= ' ' }.toInt()
            } catch (e: Exception) {
                return@OnClickListener
            }
            if (value > 1) {
                value -= 1
                holder.quantity_textview.setText(value.toString())


                var limit_crossed: Int =
                    ShoppingFragment.all_cart_list.get(position).get(10).toInt()

                if (limit_crossed > 0) {
                    limit_crossed -= 1

                    ShoppingFragment.all_cart_list.get(position).set(10, limit_crossed.toString())

                    try {
                        holder.total_amount.text =
                            "To be paid Rs. " + (work.total_amount!!.toInt() - (work.price!!.toInt())).toString()
                        ShoppingFragment.all_cart_list.get(position)
                            .set(7, (work.total_amount!!.toInt() - (work.price!!.toInt())).toString())
                        work.total_amount =
                            (work.total_amount!!.toInt() - (work.price!!.toInt())).toString()
                    } catch (e: Exception) {
                    }
                } else if (work.point_used!!.toInt() > 0) {
                    holder.point_used.text =
                        "Point Used:" + (work.point_used!!.toInt() - work.customer_margin!!.toInt()).toString()
                    holder.total_amount.text =
                        "To be paid Rs. " + (work.total_amount!!.toInt() - (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()
                    ShoppingFragment.userTotalPoints += work.customer_margin!!.toInt()



                    ShoppingFragment.all_cart_list.get(position)
                        .set(6, (work.point_used!!.toInt() - work.customer_margin!!.toInt()).toString())
                    ShoppingFragment.all_cart_list.get(position).set(
                        7,
                        (work.total_amount!!.toInt() - (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()
                    )

                    work.point_used =
                        (work.point_used!!.toInt() - work.customer_margin!!.toInt()).toString()
                    work.total_amount =
                        (work.total_amount!!.toInt() - (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()
                } else {
                    holder.total_amount.text =
                        "To be paid Rs. " + (work.total_amount!!.toInt() - (work.price!!.toInt())).toString()
                    ShoppingFragment.all_cart_list.get(position)
                        .set(7, (work.total_amount!!.toInt() - (work.price!!.toInt())).toString())
                    work.total_amount =
                        (work.total_amount!!.toInt() - (work.price!!.toInt())).toString()
                }

                work.qty = value.toString()


                ShoppingFragment.all_cart_list.get(position).set(4, value.toString())

                mCallback!!.updateTotalSummaryInfo()







                if (!work.coupon_value.equals("", ignoreCase = true)) {
                    holder.coupon_text.visibility = View.VISIBLE
                    if (work.coupon_value_unit.equals("per", ignoreCase = true)) {
                        var c_dicount_on_product = 0f
                        c_dicount_on_product =
                            work.total_amount!!.toInt() * work.coupon_value!!.toFloat() / 100


                        holder.coupon_text.text =
                            "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + work.coupon_value + "% Coupon Discount"
                    } else {
                        var c_dicount_on_product = 0f
                        c_dicount_on_product = work.qty!!.toInt() * work.coupon_value!!.toFloat()


                        holder.coupon_text.text =
                            "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + "Rs." + work.coupon_value + " flat Coupon Discount on a product"
                    }
                } else {
                    holder.coupon_text.visibility = View.GONE
                }
            }
        })

        holder.incrementButton.setOnClickListener {
            var value = holder.quantity_textview.text.toString().trim { it <= ' ' }.toInt()
            value = value + 1



            if (value <= ShoppingFragment.all_cart_list.get(position).get(9).toInt()) {
                if (work.point_used!!.toInt() > 0) {
                    if (work.customer_margin!!.toInt() <= ShoppingFragment.userTotalPoints) {
                        holder.point_used.text =
                            "Point Used: " + (work.point_used!!.toInt() + work.customer_margin!!.toInt()).toString()
                        holder.total_amount.text =
                            "To be paid Rs. " + (work.total_amount!!.toInt() + (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()
                        ShoppingFragment.userTotalPoints -= work.customer_margin!!.toInt()


                        ShoppingFragment.all_cart_list.get(position).set(
                            6,
                            (work.point_used!!.toInt() + work.customer_margin!!.toInt()).toString()
                        )
                        ShoppingFragment.all_cart_list.get(position).set(
                            7,
                            (work.total_amount!!.toInt() + (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()
                        )


                        ShoppingFragment.all_cart_list.get(position).set(4, value.toString())
                        work.point_used =
                            (work.point_used!!.toInt() + work.customer_margin!!.toInt()).toString()
                        work.total_amount =
                            (work.total_amount!!.toInt() + (work.price!!.toInt() - work.customer_margin!!.toInt())).toString()

                        work.qty = value.toString()
                        holder.quantity_textview.setText(value.toString())


                        mCallback!!.updateTotalSummaryInfo()
                    } else {
                        // New Logic 15-jul-21 for point limit crossed


                        //limit crosssed increase value


                        var limit_crossed: Int =
                            ShoppingFragment.all_cart_list.get(position).get(10).toInt()

                        limit_crossed = limit_crossed + 1

                        ShoppingFragment.all_cart_list.get(position)
                            .set(10, limit_crossed.toString())






                        holder.point_used.text =
                            "Point Used: " + work.point_used!!.toInt().toString()
                        holder.total_amount.text =
                            "To be paid Rs. " + (work.total_amount!!.toInt() + (work.price!!.toInt())).toString()


                        ShoppingFragment.all_cart_list.get(position)
                            .set(6, work.point_used!!.toInt().toString())
                        ShoppingFragment.all_cart_list.get(position)
                            .set(7, (work.total_amount!!.toInt() + (work.price!!.toInt())).toString())


                        ShoppingFragment.all_cart_list.get(position).set(4, value.toString())
                        work.point_used = work.point_used!!.toInt().toString()
                        work.total_amount =
                            (work.total_amount!!.toInt() + (work.price!!.toInt())).toString()

                        work.qty = value.toString()
                        holder.quantity_textview.setText(value.toString())


                        // ShoppingFragment.userTotalPoints = ShoppingFragment.userTotalPoints - ShoppingFragment.userTotalPoints;
                        mCallback!!.updateTotalSummaryInfo()


                        //  Toast.makeText(context1, "Sorry insufficient Points", Toast.LENGTH_LONG).show();
                    }
                } else {
                    holder.total_amount.text =
                        "To be paid Rs. " + (work.total_amount!!.toInt() + (work.price!!.toInt())).toString()


                    ShoppingFragment.all_cart_list.get(position)
                        .set(7, (work.total_amount!!.toInt() + (work.price!!.toInt())).toString())

                    ShoppingFragment.all_cart_list.get(position).set(4, value.toString())

                    work.total_amount =
                        (work.total_amount!!.toInt() + (work.price!!.toInt())).toString()

                    work.qty = value.toString()

                    holder.quantity_textview.setText(value.toString())


                    mCallback!!.updateTotalSummaryInfo()
                }
            } else {
            }
            if (!work.coupon_value.equals("", ignoreCase = true)) {
                holder.coupon_text.visibility = View.VISIBLE
                if (work.coupon_value_unit.equals("per", ignoreCase = true)) {
                    var c_dicount_on_product = 0f
                    c_dicount_on_product =
                        work.total_amount!!.toInt() * work.coupon_value!!.toFloat() / 100


                    holder.coupon_text.text =
                        "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + work.coupon_value + "% Coupon Discount"
                } else {
                    var c_dicount_on_product = 0f
                    c_dicount_on_product = work.qty!!.toInt() * work.coupon_value!!.toFloat()


                    holder.coupon_text.text =
                        "(Saved Rs." + floor(c_dicount_on_product.toDouble()) + ") " + "Rs." + work.coupon_value + " flat Coupon Discount on a product"
                }
            } else {
                holder.coupon_text.visibility = View.GONE
            }
        }



        holder.delete_icon.setOnClickListener {
            val builder = AlertDialog.Builder(context1)
            builder.setMessage("Are you sure?")
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(
                    "Yes"
                ) { dialog, id ->
                    Toast.makeText(
                        context1,
                        "Removed",
                        Toast.LENGTH_SHORT
                    ).show()
                    ShoppingFragment.all_cart_list.removeAt(position)
                    ShoppingFragment.userTotalPoints =
                        ShoppingFragment.userTotalPoints + work.point_used!!.toInt()

                    workList.removeAt(position)


                    notifyDataSetChanged()


                    // Update Summary Total
                    var bag_total_value = 0
                    val shopping_charges_value = 0
                    var bag_sub_total_value = 0
                    var total_point_used_value = 0
                    var total_payable_value = 0

                    for (i in ShoppingFragment.all_cart_list.indices) {
                        val pro_price: String =
                            ShoppingFragment.all_cart_list.get(i).get(3)
                        val qty: String = ShoppingFragment.all_cart_list.get(i).get(4)
                        val customer_margin_per: String =
                            ShoppingFragment.all_cart_list.get(i).get(5)
                        val point_used: String =
                            ShoppingFragment.all_cart_list.get(i).get(6)
                        val total_amount: String =
                            ShoppingFragment.all_cart_list.get(i).get(7)


                        bag_total_value = bag_total_value + (qty.toInt() * pro_price.toInt())
                        bag_sub_total_value = bag_total_value

                        total_point_used_value = total_point_used_value + point_used.toInt()

                        total_payable_value = total_payable_value + total_amount.toInt()
                    }

                    //  CartFragment.setTotalSummary(bag_total_value,bag_sub_total_value,total_point_used_value,total_payable_value);
                    mCallback!!.updateTotalSummaryInfo()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }


        // check pincode available
        checkPincodeAvail(work.pro_id!!, ShoppingFragment.pincode!!, holder)
    }

    override fun getItemCount(): Int {
        return workList.size
    }


    private fun checkPincodeAvail(pro_id: String, pincode: String, holder: MyViewHolder) {
        //RequestQueue initialized

        mRequestQueue = Volley.newRequestQueue(holder.context)


        //String Request initialized
        mStringRequest = object : StringRequest(
            Method.POST,
            productsUrl + "?product_id=" + pro_id + "&user_type=" + ShoppingFragment.user_type + "&pincode=" + pincode,
            Response.Listener<String?> { response ->
                try {
                    val obj = JSONArray(response)
                    val proData = obj[0] as JSONObject

                    val is_pincode_available = proData.getString("is_pincode_available")

                    if (is_pincode_available.equals("0", ignoreCase = true)) {
                        holder.no_pincode_delivery.visibility = View.VISIBLE
                        holder.no_pincode_delivery.text = "Not deliverable on pincode $pincode"

                        CartFragment.invalid_pincode = 1
                    } else {
                        holder.no_pincode_delivery.visibility = View.GONE
                    }
                } catch (e: Exception) {
                }
            },
            Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: Map<String, String> = HashMap()

                return params
            }
        }

        mRequestQueue!!.cache.clear()
        mStringRequest!!.setShouldCache(false)
        mRequestQueue!!.add(mStringRequest)
    }
}