package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.adapters.login.InterestedTopicAdapter
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.databinding.ActivityInterestedTopicsBinding
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import org.json.JSONArray
import org.json.JSONObject


class InterestedTopicsActivity : AppCompatActivity(), InterestedTopicAdapter.OnItemCheckListener {

    private var interestedTopicAdapter: InterestedTopicAdapter? = null

    var blogViewModel: BlogViewModel? = null
    private var categories: List<CategoriesResponse.Category>? = null

    lateinit var binding: ActivityInterestedTopicsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestedTopicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Logger.i(TAG, "onCreate")
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(BlogViewModel::class.java)
        setLiveData()
        blogViewModel!!.getCategories()

        binding.interestedScreenNext.setOnClickListener {
            val categoryList = ArrayList<Int>()
            if (categories != null && categories!!.isEmpty()) {
                Toast.makeText(this, "Select at least one category to proceed", Toast.LENGTH_SHORT)
                    .show()
            } else if (categories != null && categories!!.size > 0) {
                for (category in categories!!) {
                    if (category.isSelected) {
                        Logger.i("TAG", category.id.toString() + "")
                        categoryList.add(category.id)
                    }
                }
                blogViewModel?.saveUserCategory(categoryList)
            } else {
                onSaveUserCategories()
            }


        }

    }

    private fun setLiveData() {
        blogViewModel?.observeGetCategories()
            ?.observe(this, Observer<BaseResponse<ArrayList<CategoriesResponse.Category>>> { response ->
                if (response != null && response.data != null && response.data!!.isNotEmpty()) {
                    categories = response.data
                    Log.d(TAG, "onChanged: $categories")
                    interestedTopicAdapter =
                        categories?.let { InterestedTopicAdapter(applicationContext, it) }
                    val gridLayoutManager = GridLayoutManager(applicationContext, 3, GridLayoutManager.VERTICAL, false)
                    binding.interestedTopicRecyclerview?.layoutManager = gridLayoutManager
                    binding.interestedTopicRecyclerview?.adapter = interestedTopicAdapter
                } else {
                    WolooApplication.errorMessage = ""
                }
            })

        blogViewModel?.observeSaveUserCategory()
            ?.observe(this, Observer<BaseResponse<JSONObject>> { response ->
                if (response != null && response.data != null) {
                    onSaveUserCategories()
                } else {
                    WolooApplication.errorMessage = ""
                }
            })
    }


    fun onSaveUserCategories() {
        Logger.i("LOG", "saved")
        startActivity(Intent(this, WolooDashboard::class.java))
        finish()
    }

    override fun onItemClick(position: Int) {
        Logger.i("LOG", categories!![position].categoryName!!)
        val category = categories!![position]
        category.isSelected = !category.isSelected
        interestedTopicAdapter!!.notifyItemChanged(position)

    }

    private val selectedCategories: JSONArray?
        private get() {
            val selectedCategories = JSONArray()
            return if (categories != null && categories!!.size > 0) {
                for (category in categories!!) {
                    if (category.isSelected) {
                        Logger.i("TAG", category.id.toString() + "")
                        selectedCategories.put(category.id)
                    }
                }
                selectedCategories
            } else {
                null
            }
        }

    companion object {
        private val TAG = InterestedTopicsActivity::class.java.simpleName
    }
}