package `in`.woloo.www.dashboard.ui.invite_friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger.i

class InviteFriendFragment : Fragment() {
    private var inviteFriendViewModel: InviteFriendViewModel? = null

    /*Calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        i(TAG, "onCreateView")
        inviteFriendViewModel =
            ViewModelProvider(this).get(
                InviteFriendViewModel::class.java
            )
        val root = inflater.inflate(R.layout.fragment_invite_friend, container, false)
        inviteFriendViewModel!!.text.observe(
            viewLifecycleOwner
        ) { }

        return root
    }

    companion object {
        private val TAG: String = InviteFriendFragment::class.java.simpleName
    }
}