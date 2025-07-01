package in.woloo.www.shopping.adapter;


import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.woloo.www.R;
import in.woloo.www.shopping.config.Config;

public class BannerAdapter extends PagerAdapter {


    public  ArrayList<String> IMAGES;
    public LayoutInflater inflater;
    public Context context;
  public String who;

    public BannerAdapter(Context context,ArrayList<String> IMAGES,String who) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
        this.who=who;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.banner, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
      //  myImage.setImageResource(IMAGES.get(position));


        if(who.equalsIgnoreCase("product_details")) {

        String imageUri = Config.hostname+"../images/"+IMAGES.get(position);

            if(IMAGES.get(position).contains("http"))
            {
                imageUri = IMAGES.get(position);
            }

            Picasso.get().load(imageUri).into(myImage);


           /* myImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, FullImageScreenActivity.class);
                    i.putExtra("images",IMAGES);

                    context.startActivity(i);


                }
            });
*/



        }

        else {

            String imageUri = Config.hostname+"../images/"+IMAGES.get(position);

            if(IMAGES.get(position).contains("http"))
            {
                imageUri = IMAGES.get(position);
            }

            Picasso.get().load(imageUri).into(myImage);


        }



       // Toast.makeText(context,"IMAGES======="+IMAGES.get(position),Toast.LENGTH_LONG).show();
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}