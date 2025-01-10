package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.BookmarkListAdapter
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.databinding.ActivityShowBookmarkListBinding
import `in`.woloo.www.utils.Logger


class BookmarkListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowBookmarkListBinding
    private var bookmarkedWolooList = ArrayList<NearByStoreResponse.Data>()
    private var myHistoryAdapter: BookmarkListAdapter? = null
    private var homeViewModel: HomeViewModel? = null
    private var lastKnownLattitude:Double = 0.0
    private var lastKnownLongitude:Double = 0.0
    lateinit var customeIntent: Intent
    private lateinit var nearByStoreResponseListFromApi: ArrayList<NearByStoreResponse.Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBookmarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        customeIntent = intent
        lastKnownLattitude = customeIntent.getDoubleExtra("LAT" , 0.0);
        lastKnownLongitude = customeIntent.getDoubleExtra("LNG" , 0.0);
        initView()
        binding.ivBack.setOnClickListener {
            finish()
        }

    }


    private fun initView()
    {
        Logger.i("Bookmark", "initView")
        getNearByWoloos(
            lastKnownLattitude,
            lastKnownLongitude,
            SharedPreference(applicationContext).getStoredPreference(
                applicationContext,
                SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
                "0"
            )!!.toInt(),

            )
        setLiveData()

    }
    private fun  setLiveData()
    {
        Logger.i("Bookmark", "setLivedata")

        homeViewModel!!.observeNearByWoloo().observe(this) { arrayListBaseResponse ->
            // Safely handle null and initialize the list
            for (i in arrayListBaseResponse.data?.indices!!) {
                Log.d("Bookmark 1", arrayListBaseResponse.data!![i].name.toString())
            }
            nearByStoreResponseListFromApi = arrayListBaseResponse.data!!

            for (i in nearByStoreResponseListFromApi.indices) {
                Log.d("Bookmark 2", nearByStoreResponseListFromApi.get(i).name.toString() + " " + nearByStoreResponseListFromApi.get(i).isLiked.toString() )
            }

            // Filter stores that are liked
            nearByStoreResponseListFromApi.filter { it.isLiked == 1 }
                .forEach { bookmarkedWolooList.add(it) }

            for (i in bookmarkedWolooList.indices) {
                Log.d("Bookmark 3", bookmarkedWolooList.get(i).name.toString())
            }

            setAdapterList()
        }

    }

    private  fun setAdapterList()
    {
        myHistoryAdapter = BookmarkListAdapter(applicationContext, bookmarkedWolooList)
        binding.bookmarkRecyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        binding.bookmarkRecyclerView.layoutManager = linearLayoutManager
        binding.bookmarkRecyclerView.adapter = myHistoryAdapter
    }
    private fun getNearByWoloos(
        lat: Double,
        lng: Double,
        mode: Int,
    ) {
        val request = NearbyWolooRequest()
        request.lat = lat
        request.lng = lng
        request.mode = mode
        request.range = 2
        request.isOffer = 0
        request.showAll = 1
        request.packageName = "in.woloo.app"
        request.isSearch = 0
        homeViewModel!!.getNearbyWoloos(request)

    }

}