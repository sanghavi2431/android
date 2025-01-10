package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import `in`.woloo.www.databinding.ActivityDashboardBinding
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel

class DashboardActivity : BaseActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityDashboardBinding

    private val viewProfileResponse: ViewProfileResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel =  ViewModelProvider(this)[HomeViewModel::class.java]
        setProgressBar()
        setNetworkDetector()
        setLiveData()
        setClickables()

        FirebaseMessaging.getInstance().token.addOnSuccessListener { s ->
            Logger.i(
                WolooDashboard.TAG,
                "onCreate $s"
            )
        }

        val request = NearbyWolooRequest()
        request.lat = 21.1993259
        request.lng = 72.8232821
        request.mode = 0
        request.range = 6
        request.packageName = "in.woloo.app"
        request.isSearch = 0
        homeViewModel.getNearbyWoloos(request)
    }

    private fun setClickables() {
        binding.imgScanQR.setOnClickListener {
            val intent: Intent = Intent(
                this,
                QRcodeScannerActivity::class.java
            )
            if (viewProfileResponse != null) {
                val viewProfileInString = Gson().toJson(viewProfileResponse)
                intent.putExtra(AppConstants.VIEW_PROFILE_STRING, viewProfileInString)
            }
            startActivity(intent)
        }
    }

    private fun setLiveData() {
        homeViewModel.observeNearByWoloo().observe(this, Observer {
            displayToast(""+Gson().toJson(it.data))
        })
    }

    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this)[HomeViewModel::class.java]
    }

}