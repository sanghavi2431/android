package `in`.woloo.www.more.trendingblog

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.utils.Logger

class BlogDetailsActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.topImageBlogDtl)
    var blogImageDtlPage: ImageView? = null

    @JvmField
    @BindView(R.id.textTitleBlogDtl)
    var textTitleBlog: TextView? = null

    @JvmField
    @BindView(R.id.titleBlogDtlRel)
    var coinPointRel: RelativeLayout? = null

    @JvmField
    @BindView(R.id.viaBlogText)
    var viaBlogText: TextView? = null

    @JvmField
    @BindView(R.id.textDtlArea)
    var paragraphText: TextView? = null
    var imgDrawableUri = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_details)
        Logger.i(TAG, "onCreate")
        ButterKnife.bind(this)
        val getIntentBlogRcyClick = intent
        if (getIntentBlogRcyClick != null) {
            imgDrawableUri = getIntentBlogRcyClick.getIntExtra("Clicked_blogImage_Position", 0)
        }
        println("Clicked ps url$imgDrawableUri")
        blogImageDtlPage!!.setImageResource(imgDrawableUri)
    }

    companion object {
        private val TAG = BlogDetailsActivity::class.java.simpleName
    }
}