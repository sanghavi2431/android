package `in`.woloo.www.store.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.databinding.StoreCheckoutPopupBinding

class CheckoutBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: StoreCheckoutPopupBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreCheckoutPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reviewOrderButton.setOnClickListener {
            dismiss()
            val bottomSheetFragment = OrderSummaryBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
        binding.bottomHostImage.setOnClickListener{
            val drawable = binding.bottomHostImage.drawable
            val drawableOne = binding.bottomHostImageOne.drawable
            val drawableTwo = binding.bottomHostImageTwo.drawable


            if (drawable == null) {
                if(drawableOne == null) {
                    if(drawableTwo == null) {
                        binding.bottomHostImage.setImageResource(R.drawable.select_address_filled)
                    }
                    else{
                        binding.bottomHostImageTwo.setImageDrawable(null)
                    }
                }
                else{
                    binding.bottomHostImageOne.setImageDrawable(null)
                }
            } else {
                binding.bottomHostImage.setImageDrawable(null)
            }
        }
        binding.bottomHostImageOne.setOnClickListener{
            val drawableOne = binding.bottomHostImageOne.drawable
            val drawable = binding.bottomHostImage.drawable
            val drawableTwo = binding.bottomHostImageTwo.drawable
            if (drawableOne == null) {
                if(drawableTwo == null) {
                    if(drawable == null) {
                        binding.bottomHostImageOne.setImageResource(R.drawable.select_address_filled)
                    }
                    else{
                        binding.bottomHostImage.setImageDrawable(null)
                    }
                }
                else{
                    binding.bottomHostImageTwo.setImageDrawable(null)
                }
            } else {
                binding.bottomHostImageOne.setImageDrawable(null)
            }

        }

        binding.bottomHostImageTwo.setOnClickListener{
            val drawableTwo = binding.bottomHostImageTwo.drawable
            val drawableOne = binding.bottomHostImageOne.drawable
            val drawable = binding.bottomHostImage.drawable
            if (drawableTwo == null) {
                if(drawableOne == null) {
                    if(drawable == null) {
                        binding.bottomHostImageTwo.setImageResource(R.drawable.select_address_filled)
                    }
                    else{
                        binding.bottomHostImage.setImageDrawable(null)
                    }
                }
                else{
                    binding.bottomHostImageOne.setImageDrawable(null)
                }
            } else {
                binding.bottomHostImageTwo.setImageDrawable(null)
            }

        }


    }


    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
