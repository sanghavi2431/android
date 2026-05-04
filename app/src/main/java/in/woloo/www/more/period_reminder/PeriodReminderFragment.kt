package `in`.woloo.www.more.period_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger.i

class PeriodReminderFragment : Fragment() {
    private var periodReminderViewModel: PeriodReminderViewModel? = null

    /*Calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        i(TAG, "onCreateView")
        periodReminderViewModel = ViewModelProvider(this).get(
            PeriodReminderViewModel::class.java
        )
        val root = inflater.inflate(R.layout.fragment_period_reminder, container, false)
        periodReminderViewModel!!.text.observe(
            viewLifecycleOwner
        ) { }

        return root
    }

    companion object {
        private val TAG: String = PeriodReminderFragment::class.java.simpleName
    }
}