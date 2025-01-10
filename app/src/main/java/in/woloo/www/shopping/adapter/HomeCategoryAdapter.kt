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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.fragments.ProductListFragment
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import `in`.woloo.www.shopping.model.CategoryModel
import `in`.woloo.www.shopping.model.HomeProductModel
import `in`.woloo.www.utils.Utility.logFirebaseEvent

class HomeCategoryAdapter(private val workList: List<CategoryModel>) :
    RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder>() {
    var progressDialog: ProgressDialog? = null

    var context1: Context? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context = view.context
        var progressDialog: ProgressDialog? = null

        var img: ImageView? = null
        var img1: ImageView? = null
        var banner_img: ImageView? = null


        var imageTitle: TextView =
            view.findViewById<View>(R.id.text) as TextView
        var view_all: TextView =
            view.findViewById<View>(R.id.view_all) as TextView
        var image: ImageView? = null
        var banner1: ImageView =
            view.findViewById<View>(R.id.banner1) as ImageView
        var banner2: ImageView =
            view.findViewById<View>(R.id.banner2) as ImageView
        var banner3: ImageView =
            view.findViewById<View>(R.id.banner3) as ImageView
        var mainLinearLayout: LinearLayout? = null
        var recycler_view: RecyclerView =
            view.findViewById<View>(R.id.recycler_view) as RecyclerView
        var recycler_view_sub_cat: RecyclerView =
            view.findViewById<View>(R.id.recycler_view_sub_cat) as RecyclerView

        var homeProductList: MutableList<HomeProductModel> = ArrayList<HomeProductModel>()
        internal var homeProductAdapter: HomeProductAdapter? = null

        var homeSubCategoryList: MutableList<CategoryModel> = ArrayList()
        var homeSubCategoryAdapter: HomeSubCategoryAdapter? = null


        val handler: Handler = Handler()

        init {
            context1 = view.context
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_category_list_row, parent, false)

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val work = workList[position]


        holder.imageTitle.text = work.name
        Picasso.get().load(Config.hostname + "../images/" + work.banner1)
            .transform(RoundedTransformation(10, 0)).into(holder.banner1)
        Picasso.get().load(Config.hostname + "../images/" + work.banner2)
            .transform(RoundedTransformation(10, 0)).into(holder.banner2)
        Picasso.get().load(Config.hostname + "../images/" + work.banner3)
            .transform(RoundedTransformation(10, 0)).into(holder.banner3)






        holder.homeProductList.clear()
        holder.homeProductAdapter = HomeProductAdapter(holder.homeProductList)

        val layoutManager = LinearLayoutManager(context1, LinearLayoutManager.HORIZONTAL, false)

        holder.recycler_view.layoutManager = layoutManager

        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                */
        holder.recycler_view.itemAnimator = DefaultItemAnimator()
        holder.recycler_view.adapter = holder.homeProductAdapter


        // recyclerView.setNestedScrollingEnabled(false);
        //  recyclerView.setHasFixedSize(false);
        for (i in ShoppingFragment.homeProductList.indices) {
            if (ShoppingFragment.homeProductList.get(i).count.equals(work.id, ignoreCase = true)) {
                holder.homeProductList.add(ShoppingFragment.homeProductList.get(i))
            }
        }

        //Toast.makeText(context1,"size"+holder.homeProductList.size(),Toast.LENGTH_LONG).show();
        holder.homeProductAdapter!!.notifyDataSetChanged()








        holder.homeSubCategoryList.clear()
        holder.homeSubCategoryAdapter = HomeSubCategoryAdapter(holder.homeSubCategoryList)

        val layoutManager2 = LinearLayoutManager(context1, LinearLayoutManager.HORIZONTAL, false)

        holder.recycler_view_sub_cat.layoutManager = layoutManager2

        /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                */
        holder.recycler_view_sub_cat.itemAnimator = DefaultItemAnimator()
        holder.recycler_view_sub_cat.adapter = holder.homeSubCategoryAdapter


        // recyclerView.setNestedScrollingEnabled(false);
        //  recyclerView.setHasFixedSize(false);
        var get_count_sub = 0

        for (i in ShoppingFragment.homeSubCategoryList.indices) {
            if (ShoppingFragment.homeSubCategoryList.get(i).from.equals(
                    work.id,
                    ignoreCase = true
                )
            ) {
                get_count_sub++

                holder.homeSubCategoryList.add(ShoppingFragment.homeSubCategoryList.get(i))
            }
        }

        //Toast.makeText(context1,"size"+holder.homeProductList.size(),Toast.LENGTH_LONG).show();
        holder.homeSubCategoryAdapter!!.notifyDataSetChanged()

        if (get_count_sub == 0) {
            holder.recycler_view_sub_cat.visibility = View.GONE
        }




















        holder.view_all.setOnClickListener { //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            try {
                val bundle = Bundle()
                bundle.putString("category_name", work.name)
                bundle.putString("pincode", ShoppingFragment.pincode)
                logFirebaseEvent(
                    (context1 as WolooDashboard?),
                    bundle,
                    "category_view_all_click"
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

        holder.banner1.setOnClickListener { //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            try {
                val bundle = Bundle()
                bundle.putString("category_name", work.name)
                bundle.putString("pincode", ShoppingFragment.pincode)
                logFirebaseEvent(
                    (context1 as WolooDashboard?),
                    bundle,
                    "category_banner1_click"
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


        holder.banner2.setOnClickListener { //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            try {
                val bundle = Bundle()
                bundle.putString("category_name", work.name)
                bundle.putString("pincode", ShoppingFragment.pincode)
                logFirebaseEvent(
                    (context1 as WolooDashboard?),
                    bundle,
                    "category_banner2_click"
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


        holder.banner3.setOnClickListener { //  Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            try {
                val bundle = Bundle()
                bundle.putString("category_name", work.name)
                bundle.putString("pincode", ShoppingFragment.pincode)
                logFirebaseEvent(
                    (context1 as WolooDashboard?),
                    bundle,
                    "category_banner3_click"
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