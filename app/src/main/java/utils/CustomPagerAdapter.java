package utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import app.saloonuser.craftystudio.saloonuser.R;

import static app.saloonuser.craftystudio.saloonuser.SaloonDetailActivity.SaloonUID;

/**
 * Created by Aisha on 7/1/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    int[] mResources = {
            R.drawable.malecolor,
            R.drawable.female_copy,
            R.drawable.malecolor,
            R.drawable.femalecolor,

    };

    Context mContext;
    LayoutInflater mLayoutInflater;
    Saloon saloon;
    static String SaloonUID;

    public CustomPagerAdapter(Context context, Saloon mSaloon) {
        mContext = context;
        saloon = mSaloon;
        SaloonUID = saloon.getSaloonUID();
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return saloon.getSaloonImageList().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.slideimageView);

        if (saloon.getSaloonImageList() != null) {

            //firebase storage connection
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "profile_image");

            if (position == 0) {

                //Display Profile image
                if (saloon.getSaloonImageList().containsKey("profile_image")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } else if (position == 1) {

                //Display 1 showcase image

                storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_1");

                if (saloon.getSaloonImageList().containsKey("image_1")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } else if (position == 2) {

                //Display 2 showcase image

                storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_2");

                if (saloon.getSaloonImageList().containsKey("image_2")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } else if (position == 3) {

                //Display 3 showcase image

                storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_3");

                if (saloon.getSaloonImageList().containsKey("image_3")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } else if (position == 4) {

                //Display 4 showcase image

                storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_4");

                if (saloon.getSaloonImageList().containsKey("image_4")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } else if (position == 5) {

                //Display 5 showcase image
                storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_5");

                if (saloon.getSaloonImageList().containsKey("image_5")) {

                    Glide.with(mContext)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .thumbnail(0.5f)
                            .override(900, 400)
                            .crossFade(100)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }

            }
        }


        // imageView.setImageResource(mResources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
