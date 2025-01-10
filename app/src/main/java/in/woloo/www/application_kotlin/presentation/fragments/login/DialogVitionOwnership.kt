package `in`.woloo.www.application_kotlin.presentation.fragments.login

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.login.VitionOwnershipAdapter
import `in`.woloo.www.application_kotlin.adapters.login.VitionOwnershipAdapter.GlobalsVtionOwnership
import `in`.woloo.www.application_kotlin.adapters.login.VitionOwnershipAdapter.GlobalsVtionOwnership.Companion.instance
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.VtionModel
import `in`.woloo.www.application_kotlin.utilities.MessageList

class DialogVitionOwnership : DialogFragment() {
    private var vitionOwnershipAdapter: VitionOwnershipAdapter? = null
    private var ownershipRecyclerview: RecyclerView? = null
    private val ownershipArray: MutableList<VtionModel> = ArrayList<VtionModel>()
    private var ownershipListString: String? = ""
    private var mListener: OnFragmentInteractionListener? = null
    var selected: ArrayList<String>? = null
    private var GVO: GlobalsVtionOwnership? = instance
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_vition_ownership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ownershipRecyclerview = view.findViewById<RecyclerView>(R.id.ownership_recyclerview)
        val headerText = view.findViewById<TextView>(R.id.screen_header)
        val headerDes = view.findViewById<TextView>(R.id.screen_description)
        val imgBack = view.findViewById<LinearLayout>(R.id.ivBack)
        headerText.text = "Select Ownerships"
        headerDes.text = "You can select all which you own."
        imgBack.setOnClickListener {
            dismiss()
        }
        val ownershipModel1 = VtionModel()
        ownershipModel1.ownershipIcon = "abc"
        ownershipModel1.ownershipName = MessageList.OWNERSHIP_ONE
        ownershipArray.add(ownershipModel1)
        val ownershipModel2 = VtionModel()
        ownershipModel2.ownershipIcon = "abc"
        ownershipModel2.ownershipName = MessageList.OWNERSHIP_TWO
        ownershipArray.add(ownershipModel2)
        val ownershipModel3 = VtionModel()
        ownershipModel3.ownershipIcon = "abc"
        ownershipModel3.ownershipName = MessageList.OWNERSHIP_THREE
        ownershipArray.add(ownershipModel3)
        val ownershipModel4 = VtionModel()
        ownershipModel4.ownershipIcon = "abc"
        ownershipModel4.ownershipName = MessageList.OWNERSHIP_FOUR
        ownershipArray.add(ownershipModel4)
        val ownershipModel5 = VtionModel()
        ownershipModel5.ownershipIcon = "abc"
        ownershipModel5.ownershipName = MessageList.OWNERSHIP_FIVE
        ownershipArray.add(ownershipModel5)
        val ownershipModel6 = VtionModel()
        ownershipModel6.ownershipIcon = "abc"
        ownershipModel6.ownershipName = MessageList.OWNERSHIP_SIX
        ownershipArray.add(ownershipModel6)
        val ownershipModel7 = VtionModel()
        ownershipModel7.ownershipIcon = "abc"
        ownershipModel7.ownershipName = MessageList.OWNERSHIP_SEVEN
        ownershipArray.add(ownershipModel7)
        val ownershipModel8 = VtionModel()
        ownershipModel8.ownershipIcon = "abc"
        ownershipModel8.ownershipName = MessageList.OWNERSHIP_EIGHT
        ownershipArray.add(ownershipModel8)
        val ownershipModel9 = VtionModel()
        ownershipModel9.ownershipIcon = "abc"
        ownershipModel9.ownershipName = MessageList.OWNERSHIP_NINE
        ownershipArray.add(ownershipModel9)
        val ownershipModel10 = VtionModel()
        ownershipModel10.ownershipIcon = "abc"
        ownershipModel10.ownershipName = MessageList.OWNERSHIP_TEN
        ownershipArray.add(ownershipModel10)
        val ownershipModel11 = VtionModel()
        ownershipModel11.ownershipIcon = "abc"
        ownershipModel11.ownershipName = MessageList.OWNERSHIP_ELEVEN
        ownershipArray.add(ownershipModel11)
        val ownershipModel12 = VtionModel()
        ownershipModel12.ownershipIcon = "abc"
        ownershipModel12.ownershipName = MessageList.OWNERSHIP_TWELVE
        ownershipArray.add(ownershipModel12)
        vitionOwnershipAdapter = VitionOwnershipAdapter(requireActivity().applicationContext, ownershipArray)
        val gridLayoutManager =
            GridLayoutManager(requireActivity().applicationContext, 3, GridLayoutManager.VERTICAL, false)
        ownershipRecyclerview!!.layoutManager = gridLayoutManager
        ownershipRecyclerview!!.adapter = vitionOwnershipAdapter
        view.findViewById<View>(R.id.button_done).setOnClickListener {
            dismiss() // Close the dialog
            ownershipListString = GVO?.selectedOwnership
            Logger.d("String ", ownershipListString.toString())
            if (ownershipListString != null && ownershipListString!!.isEmpty()) {
                Toast.makeText(activity, MessageList.SELECTOWNERSHIP, Toast.LENGTH_SHORT).show()
            } else {
                ownershipListString = "[$ownershipListString"
                //  Logger.d("String " , ownershipListString);
                // ownershipListString.substring(0, ownershipListString.length() - 1);
                val sb = StringBuilder(ownershipListString!!)
                //  sb.deleteCharAt(sb.length() - 1);
                ownershipListString = sb.toString()
                ownershipListString = "$ownershipListString]"
                //   Logger.d("String " , ownershipListString);
                sendDataToActivity(ownershipListString!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Set dialog fragment dimensions to full screen
        if (dialog != null) {
            dialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Handle dialog dismissal here if needed
    }

    interface OnFragmentInteractionListener {
        fun onStringFragmentInteraction(data: String?)
    }

    private fun sendDataToActivity(data: String) {
        if (mListener != null) {
            mListener!!.onStringFragmentInteraction(data)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }
}
