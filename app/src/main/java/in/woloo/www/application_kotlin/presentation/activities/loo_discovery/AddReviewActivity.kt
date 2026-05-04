package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WahCertificateActivity.Companion
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.AddReviewsFragment
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants

class AddReviewActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null
    var hasReachedAtDestination = false
    private var wolooId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        ButterKnife.bind(this)
        handleDeepLink(intent)
        if (mSharedPreference == null) {
           mSharedPreference = SharedPreference(this)
        }
        val wah_certificate: String = mSharedPreference?.getStoredPreference(
            this,
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        Log.i("Aarati Dynamic link 2" , wah_certificate)
        if(wah_certificate.isNotEmpty()) {
            wolooId = wah_certificate.toInt()
            Log.i("Aarati Dynamic link 2" , wolooId.toString())

            loadFragment(AddReviewsFragment.newInstance(wolooId,"", "" , ""))
            return
        }
        else
        {
            val pwdcode: String = mSharedPreference?.getStoredPreference(
                this,
                SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
                ""
            ).toString()

            Log.i("Aarati Dynamic link 2" , pwdcode)
            if(pwdcode.isNotEmpty()) {
                wolooId = pwdcode.toInt()
                Log.i("Aarati Dynamic link 2" , wolooId.toString())

                loadFragment(AddReviewsFragment.newInstance(wolooId,"", "" , ""))
                return
            }

        }

        initViews()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
        Log.d("DeepLinkTest", "onNewIntent triggered")
    }

    private fun initViews() {
        try {
            hasReachedAtDestination = false
            val isDeepLinkProcessed = handleDeepLink(intent)
            if (!isDeepLinkProcessed) {
                wolooId = intent.getIntExtra(AppConstants.WOLOO_ID, 0)
            }
            if(intent.getStringExtra("RANGE").toString().isNotEmpty())
            {
                val range = intent.getStringExtra("RANGE").toString()
                val rating = intent.getStringExtra("RATINGS").toString()
                loadFragment(AddReviewsFragment.newInstance(wolooId,"", range , rating))
            }
            else {
                loadFragment(AddReviewsFragment.newInstance(wolooId, "","" , ""))
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun loadFragment(fragment: Fragment?) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun handleDeepLink(intent: Intent?): Boolean {
        if (intent != null && intent.data != null) {
            val deepLink = intent.data
            if (("https" == deepLink!!.scheme || "http" == deepLink.scheme) && "app.woloo.in" == deepLink.host || "/woloo_feedback" == deepLink.path) {
                val wolooIdString = deepLink.getQueryParameter("wolooId")
                if (wolooIdString != null) {
                    try {
                        wolooId = wolooIdString.toInt()
                        if(intent.getStringExtra("RANGE").toString().isNotEmpty())
                        {
                            val range = intent.getStringExtra("RANGE").toString()
                            val ratings = intent.getStringExtra("RATINGS").toString()
                            loadFragment(AddReviewsFragment.newInstance(wolooId,"", range , ratings))
                        }
                        else {
                            loadFragment(AddReviewsFragment.newInstance(wolooId, "" , "" , ""))
                        }
                        // Successfully processed the deep link
                        return true
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return false
    }

    companion object {
        protected var mSharedPreference: SharedPreference? = null
    }
}