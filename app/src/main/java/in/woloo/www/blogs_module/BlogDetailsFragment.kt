package `in`.woloo.www.blogs_module

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.gson.Gson
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.BlogDetailsPopupBinding
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.MediaItemBlog
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.utils.Logger
import org.json.JSONObject
import kotlin.properties.Delegates


class BlogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: BlogDetailsPopupBinding
    private lateinit var blogViewModel: BlogViewModel
    private var blogId: String? = null
    private var isPointsGivenNow : String = ""
    private lateinit var blog : Blog
    private var isFavouriteBlog by Delegates.notNull<Boolean>()
    private var storeViewModel: StoreViewModel? = null
    /*  private var imageUrl: ArrayList<String>? = null
      private var videoUrl: ArrayList<String>? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BlogDetailsPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blogViewModel = ViewModelProvider(this)[BlogViewModel::class.java]
        storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]

      //  onReadABlog()

        binding.ivBack.setOnClickListener {
          finish()
        }


        blogId = intent.getStringExtra(BLOG_ID)
        isPointsGivenNow = intent.getStringExtra(BLOG_READ_POINTS)!!
     /*   imageUrl = intent.getStringArrayListExtra(IMAGEURL)
        videoUrl = intent.getStringArrayListExtra(VIDEOURL)*/

        if(isPointsGivenNow.equals("yes" , ignoreCase = true))
        {
           // showSuccessDialog()
            blogViewModel!!.blogReadPoint(blogId.toString())
        }

        val request = BlockBlogRequest().apply {
            blog_id = blogId ?: ""
        }
        blogViewModel.getBlogDetails(request)

        binding.shareimg.setOnClickListener {
            onClickBlogShare(blog)
        }

        binding.commentimg.setOnClickListener {
            val bottomSheetFragment = CommentsPopup.newInstance(blogId!!)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

       /* binding.closeImage.setOnClickListener {
            binding.imageFrame.visibility = View.GONE
            binding.descriptionLayout.visibility = View.VISIBLE
        }*/

        binding.heartimg.setOnClickListener {
            if (isFavouriteBlog)
            {
                isFavouriteBlog = !isFavouriteBlog
                R.drawable.favorite_blogs_icon
            } else {
                isFavouriteBlog = isFavouriteBlog
                R.drawable.like_blog
            }
            onClickBlogFavourite(blog)

        }

        binding.shopNow.setOnClickListener{
            storeViewModel!!.getProductDetailsWithPrice( blog.isMapToShop!! , "*variants.calculated_price,+variants.inventory_quantity,*categories" , SharedPrefSettings.getPreferences.fetchRegionId().toString())
        }

        binding.productDetailsImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var previousPosition = -1

            override fun onPageSelected(position: Int) {
                val recyclerView = binding.productDetailsImage.getChildAt(0) as? RecyclerView

                // Release previous video player if any
                val previousViewHolder = recyclerView?.findViewHolderForAdapterPosition(previousPosition)
                if (previousViewHolder is MediaPagerAdapter.VideoViewHolder) {
                    previousViewHolder.releasePlayer()
                }

                previousPosition = position
            }
        })

        blogViewModel.observeBlogReadPoint().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.message}")

                        blogViewModel.ctaBlogRead(blogId.toString())

                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        blogViewModel!!.observeCtaBlogRead().observe(this,
            Observer<BaseResponse<JSONObject>?> { response ->
                if (response != null && response.data != null) {
                    showSuccessDialog("10")
                }
            })


        storeViewModel!!.observeProductDetailsWithPrice()
            .observe(this, Observer { response ->
                response?.let {
                    try{
                        Logger.i("Aarati Store", "setLiveData ${it.products!!.id!!}")

                        val intent = Intent(this, StoreProductDetailsActivity::class.java)
                        intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                        intent.putExtra("PRODUCT_DETAILS", Gson().toJson(it.products))
                        intent.putExtra("CALLINGACTIVITY", "CONTENTCOMMERCE")
                        startActivity(intent)

                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }


                }

            })

        blogViewModel.observeCtaFavourite().observe(this ,Observer<BaseResponse<FavouriteResult>?> { response ->


                try {
                    if (response != null && response.data != null) {
                        val isFavouriteAPI = response?.data?.favourite
                      /*  val jsonObject = response.data
                        Logger.i("Aarati Content Store", "Raw JSONObject: ${response.data!!} ${jsonObject.toString()} ")
                        val resultsObj = jsonObject?.getJSONObject("results")
                        val isFavouriteAPI = resultsObj?.getBoolean("favourite")*/
                        Logger.i("Aarati Content Store", "Favourite: $isFavouriteAPI")

                        if (isFavouriteAPI == true) {
                            binding.heartimg.setImageResource(R.drawable.favorite_blogs_icon)
                            binding.heartimg.tag = R.drawable.favorite_blogs_icon
                        } else {
                            binding.heartimg.setImageResource(R.drawable.like_blog)
                            binding.heartimg.tag = R.drawable.like_blog
                        }
                    }
                } catch (e: Exception) {
                    Logger.e("Aarati Content Store", "Exception in parsing JSONObject", e)
                }

             /*   if (isFavourite) R.drawable.favorite_blogs_icon else R.drawable.like_blog
                isFavourite = !isFavourite*/


        })




        blogViewModel.observeBlogDetails().observe(this) { response ->
            response?.data?.let { data ->
                try {
                    blog = data[0]
                    val mediaList = mutableListOf<MediaItemBlog>()
                    blog.mainImage?.forEach {
                        mediaList.add(MediaItemBlog.Image(BuildConfig.NODE_API_URL + it))
                    }
                    blog.videoUrl?.forEach {
                        mediaList.add(MediaItemBlog.Video(BuildConfig.NODE_API_URL + it))
                    }
/*if(imageUrl !=null) {
    imageUrl?.forEach {
        mediaList.add(MediaItemBlog.Image(BuildConfig.NODE_API_URL + it))
    }
}
                    if(videoUrl != null) {
                        videoUrl?.forEach {
                            mediaList.add(MediaItemBlog.Video(BuildConfig.NODE_API_URL + it))
                        }
                    }*/
                    val adapter = MediaPagerAdapter(this, mediaList)
                    binding.productDetailsImage.adapter = adapter

                    binding.blogText.makeExpandableHtml(blog.contentData ?: "")

                    if(blog.isFavourite == 1)
                    {
                        isFavouriteBlog = true
                        binding.heartimg.setImageResource(R.drawable.favorite_blogs_icon)
                    }
                    else{
                        isFavouriteBlog = false
                        binding.heartimg.setImageResource(R.drawable.like_blog)
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun TextView.makeExpandableHtml(
        htmlText: String,
        maxLines: Int = 2,
        expandText: String = " See more...",
        collapseText: String = " See less..."
    ) {
        val spannedHtml = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        val originalText = SpannableStringBuilder(spannedHtml)

        this.text = originalText
        this.post {
            if (lineCount <= maxLines) {
                text = originalText
                return@post
            }

            val layout = layout ?: return@post
            val endIndex = layout.getLineEnd(maxLines - 1)
            val trimmedText = SpannableStringBuilder(originalText.subSequence(0, endIndex).trim())
            trimmedText.append("...")

            // Append "See more..." with custom ClickableSpan
            trimmedText.append(expandText)
            trimmedText.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val expandedText = SpannableStringBuilder(originalText)
                    expandedText.append(collapseText)

                    // Add collapse clickable span
                    expandedText.setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            makeExpandableHtml(htmlText, maxLines, expandText, collapseText)
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = Color.BLACK
                            ds.isUnderlineText = false
                        }
                    }, expandedText.length - collapseText.length, expandedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    text = expandedText
                    movementMethod = LinkMovementMethod.getInstance()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.BLACK
                    ds.isUnderlineText = false
                }
            }, trimmedText.length - expandText.length, trimmedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            text = trimmedText
            movementMethod = LinkMovementMethod.getInstance()
        }
    }


    fun onClickBlogShare(blog: Blog) {
        if (TextUtils.isEmpty(blog.title) || TextUtils.isEmpty(blog.shortLink)) {
            return
        }
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT, """
     ${blog.title}
     ${blog.shortLink}
     """.trimIndent()
        )
        startActivity(Intent.createChooser(share, null))
    }

    fun onClickBlogFavourite(blog: Blog) {
        blog.isFavourite = if (blog.isFavourite == 0) 1 else 0
        blogViewModel.ctaFavourite(blog.id.toString())

    }




    fun showSuccessDialog(coins : String)
    {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById(R.id.tv_logout) as TextView
            btnSuccessTextDialog.text = "Woohoo! You Earned ${coins} Woloo Points!"

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            btnShopDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }

                this.finish()
                val intent = Intent(this, WolooDashboard::class.java)
                intent.putExtra("StoreListingFragment", "StoreListingFragment")
                startActivity(intent)


            }

            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        const val BLOG_ID = "blog_id"
        const val BLOG_READ_POINTS = "blog_read_points"
     /*   const val IMAGEURL = "image"
        const val VIDEOURL = "video"
*/

        fun start(context: Context, blogId: String  , blogreadpoints : String//, imageUrl : ArrayList<String> , videoUrl : ArrayList<String>
             ) {
            val intent = Intent(context, BlogDetailsActivity::class.java)
            intent.putExtra(BLOG_ID, blogId)
            intent.putExtra(BLOG_READ_POINTS , blogreadpoints)
           /* intent.putExtra(IMAGEURL, imageUrl)
            intent.putExtra(VIDEOURL, videoUrl)*/
            context.startActivity(intent)
        }
    }
}
