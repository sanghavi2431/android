package `in`.woloo.www.application_kotlin.adapters.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.VtionModel

class VitionOwnershipAdapter(context: Context, ownership: List<VtionModel>) :
    RecyclerView.Adapter<VitionOwnershipAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val ownershipList: List<VtionModel>
    var GVO = GlobalsVtionOwnership.instance
    var context: Context
    var ownrshipSelected = ""
    private val selectedOwnerships = ArrayList<String>()

    init {
        inflater = LayoutInflater.from(context)
        ownershipList = ownership
        this.context = context
        if (GVO!!.selectedOwnershipArray != null) {
            selectedOwnerships.addAll(GVO!!.selectedOwnershipArray!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            inflater.inflate(R.layout.vtion_education_grid_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ownership: VtionModel = ownershipList[position]
        /*  Glide.with(context)
                .load(context.getDrawable(R.drawable.ic__01_hormones))
                .error(context.getDrawable(R.drawable.ic__01_hormones))
                .into(holder.topicImage);*/holder.textTitle.setText(ownership.ownershipName)
        //   holder.imageRelative.setBackgroundResource(R.drawable.circular_background);
        if (selectedOwnerships.contains(ownership.ownershipName)) {
            holder.cardView.background =
                context.getDrawable(R.drawable.vtion_education_card_selected)
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
        } else {
            holder.cardView.background = context.getDrawable(R.drawable.vtion_education_card_view)
            holder.textTitle.setTextColor(context.getColor(R.color.application_background))
        }
        holder.parentView.setOnClickListener { view: View? ->
            Logger.d("POS", position.toString() + ownership.ownershipName)
            val currentOwnershipName: String = ownership.ownershipName.toString()
            if (selectedOwnerships.contains(currentOwnershipName)) {
                // Deselect the item
                selectedOwnerships.remove(currentOwnershipName)
                holder.cardView.background =
                    context.getDrawable(R.drawable.vtion_education_card_view)
                holder.textTitle.setTextColor(context.getColor(R.color.application_background))
            } else {
                // Select the item
                selectedOwnerships.add(currentOwnershipName)
                holder.cardView.background =
                    context.getDrawable(R.drawable.vtion_education_card_selected)
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
            }
            val selectedOwnershipsString = StringBuilder()
            for (ownershipName in selectedOwnerships) {
                selectedOwnershipsString.append(ownershipName).append(",")
            }
            if (selectedOwnershipsString.length > 0) {
                selectedOwnershipsString.setLength(selectedOwnershipsString.length - 1) // Remove trailing comma
            }
            GVO!!.selectedOwnership = selectedOwnershipsString.toString()
            GVO!!.selectedOwnershipArray = ArrayList(selectedOwnerships)
        }
    }

    override fun getItemCount(): Int {
        return ownershipList.size
    }

    class ViewHolder(//   RelativeLayout imageRelative;
        var parentView: View
    ) : RecyclerView.ViewHolder(
        parentView
    ) {
        //   ImageView topicImage;
        var textTitle: TextView
        var cardView: CardView

        init {
            textTitle = itemView.findViewById<TextView>(R.id.itemNameText)
            cardView = itemView.findViewById<CardView>(R.id.card_view_edu)
            //     topicImage = itemView.findViewById(R.id.checkboxItem);
            //     imageRelative = itemView.findViewById(R.id.gridDesignRel1);
        }
    }

    class GlobalsVtionOwnership  // Restrict the constructor from being instantiated
    private constructor() {
        // Global variable
        @JvmField
        var selectedOwnership: String? = null
        var selectedOwnershipArray: ArrayList<String>? = null

        companion object {
            @JvmStatic
            @get:Synchronized
            var instance: GlobalsVtionOwnership? = null
                get() {
                    if (field == null) {
                        field = GlobalsVtionOwnership()
                    }
                    return field
                }
                private set
        }
    }
}