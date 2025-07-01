package `in`.woloo.www.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import `in`.woloo.www.databinding.FragmentContactUsDialogBinding

class ContactUsDialogFragment : DialogFragment() {

    private var binding: FragmentContactUsDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactUsDialogBinding.inflate(layoutInflater)
        return binding?.root
    }
}
