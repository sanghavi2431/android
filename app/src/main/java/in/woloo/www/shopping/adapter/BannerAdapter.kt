package `in`.woloo.www.shopping.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import `in`.woloo.www.R
import `in`.woloo.www.shopping.config.Config


class BannerAdapter(var context: Context, var IMAGES: ArrayList<String>, var who: String) :
    PagerAdapter() {
    var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout = inflater.inflate(R.layout.banner, view, false)
        val myImage = myImageLayout
            .findViewById<View>(R.id.image) as ImageView


        //  myImage.setImageResource(IMAGES.get(position));
        if (who.equals("product_details", ignoreCase = true)) {
            var imageUri = Config.hostname + "../images/" + IMAGES[position]

            if (IMAGES[position].contains("http")) {
                imageUri = IMAGES[position]
            }

            Picasso.get().load(imageUri).into(myImage)


            /* myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, FullImageScreenActivity.class);
                    i.putExtra("images",IMAGES);

                    context.startActivity(i);


                }
            });
*/
        } else {
            var imageUri = Config.hostname + "../images/" + IMAGES[position]

            if (IMAGES[position].contains("http")) {
                imageUri = IMAGES[position]
            }

            Picasso.get().load(imageUri).into(myImage)
        }


        // Toast.makeText(context,"IMAGES======="+IMAGES.get(position),Toast.LENGTH_LONG).show();
        view.addView(myImageLayout, 0)
        return myImageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun saveState(): Parcelable? {
        return null
    }
}