package in.woloo.www.trendingblog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.utils.Logger;

public class BlogDetailsActivity extends AppCompatActivity {
    private static final String TAG = BlogDetailsActivity.class.getSimpleName();


    @BindView(R.id.topImageBlogDtl)
    ImageView blogImageDtlPage;

    @BindView(R.id.textTitleBlogDtl)
    TextView textTitleBlog;

    @BindView(R.id.titleBlogDtlRel)
    RelativeLayout coinPointRel;

    @BindView(R.id.viaBlogText)
    TextView viaBlogText;

    @BindView(R.id.textDtlArea)
    TextView paragraphText;

    int imgDrawableUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);
        Intent getIntentBlogRcyClick=getIntent();
        if (getIntentBlogRcyClick!=null)
        {
         imgDrawableUri=getIntentBlogRcyClick.getIntExtra("Clicked_blogImage_Position",0);
        }
        System.out.println("Clicked ps url"+imgDrawableUri);
        blogImageDtlPage.setImageResource(imgDrawableUri);
    }
}