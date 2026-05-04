package `in`.woloo.www.store.screens

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.databinding.StoreHelpOrderPopupBinding

class HelpSupportBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: StoreHelpOrderPopupBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreHelpOrderPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitHelpButton.setOnClickListener{
            dismiss()
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open
                val focusedView = dialog?.currentFocus
                focusedView?.let {
                    binding.scrollBottomSheet.smoothScrollTo(0, it.bottom)
                }
            }
        }



    }


    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
