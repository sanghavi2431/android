package `in`.woloo.www.invite_friend.fragments.contacts

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.invite_friend.fragments.InviteContactsFragments
import `in`.woloo.www.invite_friend.fragments.InviteContactsFragments.Companion.newInstance
import `in`.woloo.www.utils.AppConstants

class InviteContactsActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null
    private val contactlist = ""
    var refcode: String? = ""
    var isGiftSub: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invitecontactslayout)
        ButterKnife.bind(this)
        initViews()
    }

    private fun initViews() {
        //contactlist= getIntent().getStringExtra("ARRAYLIST");
        refcode = intent.getStringExtra(AppConstants.REFCODE)
        isGiftSub = intent.hasExtra("isGiftSub")
        loadFragment(InviteContactsFragments.newInstance(refcode, isGiftSub))
    }

    fun loadFragment(fragment: Fragment) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }
}