package `in`.woloo.www.shopping.adapter

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.fragments.ProductListFragment
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import `in`.woloo.www.shopping.model.CategoryModel
import `in`.woloo.www.utils.Utility.logFirebaseEvent

class DashboardCategoryAdapterTop(private val workList: List<CategoryModel>) :
    RecyclerView.Adapter<DashboardCategoryAdapterTop.MyViewHolder>() {
    var progressDialog: ProgressDialog? = null

    var context1: Context? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context = view.context
        var progressDialog: ProgressDialog? = null


        var imageTitle: TextView =
            view.findViewById<View>(R.id.text) as TextView
        var image: ImageView =
            view.findViewById<View>(R.id.image) as ImageView
        var lineView: View =
            view.findViewById(R.id.lineView) as View
        var mainLinearLayout: LinearLayout =
            view.findViewById<View>(R.id.mainLinearLayout) as LinearLayout


        val handler: Handler = Handler()

        init {
            context1 = view.context
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_single_row_top, parent, false)

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val work = workList[position]


        holder.imageTitle.text = work.name
        Picasso.get().load(Config.hostname + "../images/" + work.image).into(holder.image)




        holder.image.setOnClickListener { //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            try {
                val bundle = Bundle()
                bundle.putString("category_name", work.name)
                bundle.putString("pincode", ShoppingFragment.pincode)
                logFirebaseEvent(
                    (context1 as WolooDashboard?),
                    bundle,
                    "category_icon_click"
                )


                val myFragment: ProductListFragment = ProductListFragment()
                val b = Bundle()
                b.putString("catId", work.id)
                b.putString("catName", work.name)
                b.putString("cat_type", "category")
                myFragment.setArguments(b)


                val fragmentManager: FragmentManager =
                    (context1 as WolooDashboard).getSupportFragmentManager()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
        }
    }

    override fun getItemCount(): Int {
        return workList.size
    }
}