package `in`.woloo.www.application_kotlin.presentation.fragments.login

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.application_kotlin.adapters.login.VitionEducationAdapter.GlobalsVtionEducation
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.login.VitionEducationAdapter
import `in`.woloo.www.application_kotlin.adapters.login.VitionEducationAdapter.GlobalsVtionEducation.Companion.instance
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.VtionModel
import `in`.woloo.www.application_kotlin.utilities.MessageList

class DialogVitionEducation : DialogFragment(), VitionEducationAdapter.OnItemCheckListener {
    private var vitionEducationAdapter: VitionEducationAdapter? = null
    var educationRecyclerview: RecyclerView? = null
    private val educationArray: MutableList<VtionModel> = ArrayList<VtionModel>()
    private var mListenerEdu: OnFragmentInteractionListenerEdu? = null
    var GVE: GlobalsVtionEducation? = instance
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_vition_ownership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        educationRecyclerview = view.findViewById<RecyclerView>(R.id.ownership_recyclerview)
        val headerText = view.findViewById<TextView>(R.id.screen_header)
        val headerDes = view.findViewById<TextView>(R.id.screen_description)
        val imgBack = view.findViewById<LinearLayout>(R.id.ivBack)
        headerText.text = "Select Education"
        headerDes.text = "Let us know your education."
        imgBack.setOnClickListener {
                dismiss()
        }
        val educationModel1 = VtionModel()
        educationModel1.ownershipName = MessageList.EDU_ONE
        educationArray.add(educationModel1)
        val educationModel2 = VtionModel()
        educationModel2.ownershipName = MessageList.EDU_TWO
        educationArray.add(educationModel2)
        val educationModel3 = VtionModel()
        educationModel3.ownershipName = MessageList.EDU_THREE
        educationArray.add(educationModel3)
        val educationModel4 = VtionModel()
        educationModel4.ownershipName = MessageList.EDU_FOUR
        educationArray.add(educationModel4)
        val educationModel5 = VtionModel()
        educationModel5.ownershipName = MessageList.EDU_FIVE
        educationArray.add(educationModel5)
        val educationModel6 = VtionModel()
        educationModel6.ownershipName = MessageList.EDU_SIX
        educationArray.add(educationModel6)
        val educationModel7 = VtionModel()
        educationModel7.ownershipName = MessageList.EDU_SEVEN
        educationArray.add(educationModel7)
        val educationModel8 = VtionModel()
        educationModel8.ownershipName = MessageList.EDU_EIGHT
        educationArray.add(educationModel8)
        val educationModel9 = VtionModel()
        educationModel9.ownershipName = MessageList.EDU_NINE
        educationArray.add(educationModel9)
        val educationModel10 = VtionModel()
        educationModel10.ownershipName = MessageList.EDU_TEN
        educationArray.add(educationModel10)
        vitionEducationAdapter = VitionEducationAdapter(requireActivity().applicationContext, educationArray)
        val gridLayoutManager =
            GridLayoutManager(activity?.applicationContext, 2, GridLayoutManager.VERTICAL, false)
        educationRecyclerview?.layoutManager = gridLayoutManager
        educationRecyclerview?.adapter = vitionEducationAdapter
        view.findViewById<View>(R.id.button_done).setOnClickListener {
            dismiss() // Close the dialog
            val educationSelected: String = GVE?.selectedEducation.toString()
            // Logger.d("Education is " , educationSelected);
            sendDataToActivityEdu(educationSelected)
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

    override fun onItemClick(position: Int) {
        Logger.i("LOG", educationArray[position].ownershipName!!)
        val ownershipData: VtionModel = educationArray[position]
        ownershipData.isSelected = !ownershipData.isSelected
        vitionEducationAdapter?.notifyItemChanged(position)
    }

    interface OnFragmentInteractionListenerEdu {
        fun onStringFragmentInteractionEdu(data: String?)
    }

    private fun sendDataToActivityEdu(data: String) {
        if (mListenerEdu != null) {
            mListenerEdu!!.onStringFragmentInteractionEdu(data)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListenerEdu = if (context is OnFragmentInteractionListenerEdu) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }
}
