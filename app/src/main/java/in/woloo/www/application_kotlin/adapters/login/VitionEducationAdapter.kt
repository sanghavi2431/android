package `in`.woloo.www.application_kotlin.adapters.login

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.VtionModel

class VitionEducationAdapter(context: Context, education: List<VtionModel>) :
    RecyclerView.Adapter<VitionEducationAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val educationList: List<VtionModel>
    var context: Context
    private var selectedEducation: String? = ""
    private val selectedItem = -1

    //String selectedItemToHighlight;
    private var previouslySelectedOwnershipName: String? = null
    var GVE = GlobalsVtionEducation.instance

    init {
        inflater = LayoutInflater.from(context)
        educationList = education
        this.context = context
        if (context is OnItemCheckListener) {
            itemCheckListener = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            inflater.inflate(R.layout.vtion_education_grid_adapter_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val education: VtionModel = educationList[position]
        holder.textTitle.setText(education.ownershipName)
        if (selectedEducation != null && selectedEducation == education.ownershipName) {
            // Item is selected
            holder.cardView.background =
                context.getDrawable(R.drawable.vtion_education_card_selected)
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
        } else {
            // Item is not selected
            holder.cardView.background = context.getDrawable(R.drawable.vtion_education_card_view)
            holder.textTitle.setTextColor(context.getColor(R.color.application_background))
        }
        if (education.ownershipName == previouslySelectedOwnershipName) {
            // Apply the previously selected item style
            holder.cardView.background =
                context.getDrawable(R.drawable.vtion_education_card_selected)
            holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
        } else {
            // Apply default style
            holder.cardView.background = context.getDrawable(R.drawable.vtion_education_card_view)
            holder.textTitle.setTextColor(context.getColor(R.color.application_background))
        }
        if (GVE!!.selectedEducation == null) {
        } else {
            if (GVE!!.selectedEducation == education.ownershipName) {
                Log.d(
                    "ITTTEM",
                    GVE!!.selectedEducation + " " + education.ownershipName + " " + position
                )
                holder.cardView.background =
                    context.getDrawable(R.drawable.vtion_education_card_selected)
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
            }
        }
        holder.parentView.setOnClickListener { view: View? ->
            Logger.d("POS", position.toString() + education.ownershipName)
            val currentOwnershipName: String = education.ownershipName.toString()
            if (previouslySelectedOwnershipName != null && previouslySelectedOwnershipName != currentOwnershipName) {
                // Find the position of the previously selected item
                val previousPosition = findPositionByOwnershipName(
                    previouslySelectedOwnershipName!!
                )
                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition) // Reset the previous item background
                }
            }

            // Update the currently selected item
            previouslySelectedOwnershipName = currentOwnershipName
            notifyItemChanged(position)
            Logger.d("POS OnCreate", position.toString() + education.ownershipName)
            if (selectedEducation != null && selectedEducation == education.ownershipName) {
                // Already selected, so deselect
                selectedEducation = ""
                holder.cardView.background =
                    context.getDrawable(R.drawable.vtion_education_card_view)
                holder.textTitle.setTextColor(context.getColor(R.color.application_background))
                notifyDataSetChanged()
            } else {
                // Not selected, so select it
                selectedEducation = education.ownershipName
                holder.cardView.background =
                    context.getDrawable(R.drawable.vtion_education_card_selected)
                holder.textTitle.setTextColor(context.getColor(R.color.dark_yellow))
                notifyDataSetChanged()
            }

            // Update the selected item in your data source or wherever it's stored
            GVE!!.selectedEducation = selectedEducation


            // Notify adapter of the change
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return educationList.size
    }

    class ViewHolder(var parentView: View) : RecyclerView.ViewHolder(
        parentView
    ) {
        var textTitle: TextView
        var cardView: CardView

        init {
            textTitle = itemView.findViewById<TextView>(R.id.itemNameText)
            cardView = itemView.findViewById<CardView>(R.id.card_view_edu)
        }
    }

    interface OnItemCheckListener {
        fun onItemClick(position: Int)
    }

    class GlobalsVtionEducation  // Restrict the constructor from being instantiated
    private constructor() {
        // Global variable
        @JvmField
        var selectedEducation: String? = null

        companion object {
            @JvmStatic
            @get:Synchronized
            var instance: GlobalsVtionEducation? = null
                get() {
                    if (field == null) {
                        field = GlobalsVtionEducation()
                    }
                    return field
                }
                private set
        }
    }

    private fun findPositionByOwnershipName(ownershipName: String): Int {
        for (i in educationList.indices) {
            if (educationList[i].ownershipName == ownershipName) {
                return i
            }
        }
        return -1 // Return -1 if not found
    }

    companion object {
        private var itemCheckListener: OnItemCheckListener? = null
    }
}