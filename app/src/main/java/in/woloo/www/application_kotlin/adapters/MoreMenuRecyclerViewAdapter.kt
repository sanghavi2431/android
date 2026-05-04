package `in`.woloo.www.application_kotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.callbacks.MenuClickCallback

class MoreMenuRecyclerViewAdapter(
    private val context: Context,
    private val menuList: List<String>,
    menuClickCallback: MenuClickCallback
) : RecyclerView.Adapter<MoreMenuRecyclerViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    private val menuClickCallback: MenuClickCallback

    init {
        mInflater = LayoutInflater.from(context)
        this.menuClickCallback = menuClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.recycler_gridlayout_design, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val animal = menuList[position]
        viewHolder.tvMenu.text = animal
        viewHolder.llContentLayout.setOnClickListener { v: View? ->
            menuClickCallback.menuItemClick(
                position
            )
        }
        if (position < 3) {
            viewHolder.llContentLayout.background = context.getDrawable(R.drawable.yellow_rectangle_shape)
        }
        setMenuIcon(viewHolder, position)
    }

    private fun setMenuIcon(viewHolder: ViewHolder, position: Int) {
        val menu = menuList[position]
        when (menu) {
            "Notification" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_notification)
            "My Cart" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_my_cart)
            "Pee’rs Club \nMembership" -> viewHolder.ivMenu.setImageResource(R.drawable.buy_peers_club_membership)
            "My History" -> viewHolder.ivMenu.setImageResource(R.drawable.my_history_icon)
            "Invite A Friend" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_invite)
            "My Account" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_account)
            "Woloo Gift-\nCard" -> viewHolder.ivMenu.setImageResource(R.drawable.gift_cart_icon)
            "Become A Woloo Host", "Become a Woloo Host" -> viewHolder.ivMenu.setImageResource(R.drawable.become_host_icon)
            "Period Reminder" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_gift_card)
            "Add Review" -> viewHolder.ivMenu.setImageResource(R.drawable.ic_gift_card)
            "About" -> viewHolder.ivMenu.setImageResource(R.drawable.about_us_icon)
            "Terms of Use" -> viewHolder.ivMenu.setImageResource(R.drawable.terms_of_use_icon)
            "Refer a Woloo \nHost" -> viewHolder.ivMenu.setImageResource(R.drawable.refere_woloo_host_icon)
            "Discontinue Pee’rs Club Membership" -> viewHolder.ivMenu.setImageResource(R.drawable.unsubsribe_icon)
            "Delete Account" -> viewHolder.ivMenu.setImageResource(R.drawable.unsubsribe_icon)
            "Offer Cart" -> viewHolder.ivMenu.setImageResource(R.drawable.offer_cart_icon)
            "Contact Us" -> viewHolder.ivMenu.setImageResource(R.drawable.contact_us)
            "Refer Friend" -> viewHolder.ivMenu.setImageResource(R.drawable.refer_icon)
            "Say it with \nWoloo" -> viewHolder.ivMenu.setImageResource(R.drawable.say_it_woloo_icon)
            "Period \nManagement" -> viewHolder.ivMenu.setImageResource(R.drawable.period_management_icon)
            "Thirst \nReminder" -> viewHolder.ivMenu.setImageResource(R.drawable.thrist_reminder_bottom_nav)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMenu: TextView
        var ivMenu: ImageView
        var llContentLayout: LinearLayout

        init {
            tvMenu = itemView.findViewById<TextView>(R.id.itemNameText)
            ivMenu = itemView.findViewById<ImageView>(R.id.checkboxItem)
            llContentLayout = itemView.findViewById<LinearLayout>(R.id.relative_card)
        }
    }
}
