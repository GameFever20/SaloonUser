package app.saloonuser.craftystudio.saloonuser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import utils.Saloon;

public class SaloonDetailActivity extends AppCompatActivity {

    TextView mSaloonDetailNameTv, mSaloonDetailAddressTv, mSaloonDetailRatingTv, mSaloonDetailOpeningTimeTv,
            mSaloonDetailClosingTimeTv;

    ImageView mSaloonDetailProfileIv, mSaloonDetailShow1Iv, mSaloonDetailShow2Iv, mSaloonDetailShow3Iv,
            mSaloonDetailShow4Iv, mSaloonDetailShow5Iv;

    Saloon saloon;

    public static String SaloonUID;

    public double lattitude, longitute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        saloon = (Saloon) bundle.getSerializable("Saloon_Class");

        SaloonUID = saloon.getSaloonUID();

        //views
        mSaloonDetailNameTv = (TextView) findViewById(R.id.saloon_detail_name_textview);
        mSaloonDetailAddressTv = (TextView) findViewById(R.id.saloon_detail_address_textview);
        mSaloonDetailRatingTv = (TextView) findViewById(R.id.saloon_detail_rating_textview);
        mSaloonDetailOpeningTimeTv = (TextView) findViewById(R.id.saloon_detail_openingtime_textview);
        mSaloonDetailClosingTimeTv = (TextView) findViewById(R.id.saloon_detail_closingtime_textview);

        mSaloonDetailProfileIv = (ImageView) findViewById(R.id.saloon_detail_profileimage_imageview);
        mSaloonDetailShow1Iv = (ImageView) findViewById(R.id.saloon_detail_showcase1_imageview);
        mSaloonDetailShow2Iv = (ImageView) findViewById(R.id.saloon_detail_showcase2_imageview);
        mSaloonDetailShow3Iv = (ImageView) findViewById(R.id.saloon_detail_showcase3_imageview);
        mSaloonDetailShow4Iv = (ImageView) findViewById(R.id.saloon_detail_showcase4_imageview);
        mSaloonDetailShow5Iv = (ImageView) findViewById(R.id.saloon_detail_showcase5_imageview);

        //setting all data in views
        setAllValues();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "http://maps.google.com/maps?daddr=" + lattitude + "," + longitute + " (" + "Parlour Location" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(SaloonDetailActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });
    }

    private void setAllValues() {
        String m, p = "";
        mSaloonDetailNameTv.setText(saloon.getSaloonName());
        mSaloonDetailAddressTv.setText(saloon.getSaloonAddress());
        int a = saloon.getOpeningTimeHour();
        if (a < 12 && a >= 0) {
            m = "AM";
        } else {
            m = "PM";
        }
        int b = saloon.getClosingTimeHour();
        if (b < 12 && b >= 0) {
            p = "AM";
        } else {
            p = "PM";
        }
        mSaloonDetailOpeningTimeTv.setText(mSaloonDetailOpeningTimeTv.getText() + " " + saloon.getOpeningTimeHour() + m);
        mSaloonDetailClosingTimeTv.setText(mSaloonDetailClosingTimeTv.getText() + " " + saloon.getClosingTimeHour() + p);
        mSaloonDetailRatingTv.setText(saloon.getSaloonRating() + "");
        lattitude = saloon.getSaloonLocationLatitude();
        longitute = saloon.getSaloonLocationLongitude();
        setSaloonImages();
    }

    public void seeSaloonService(View view) {

    }

    public void setSaloonImages() {
        if (saloon.getSaloonImageList() != null) {

            //firebase storage connection
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "profile_image");

            //Display Profile image
            if (saloon.getSaloonImageList().containsKey("profile_image")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailProfileIv);
            }

            //Display 1 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_1");

            if (saloon.getSaloonImageList().containsKey("image_1")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailShow1Iv);
            }

            //Display 2 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_2");

            if (saloon.getSaloonImageList().containsKey("image_2")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailShow2Iv);
            }

            //Display 3 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_3");

            if (saloon.getSaloonImageList().containsKey("image_3")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailShow3Iv);
            }

            //Display 4 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_4");

            if (saloon.getSaloonImageList().containsKey("image_4")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailShow4Iv);
            }


            //Display 5 showcase image
            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_5");

            if (saloon.getSaloonImageList().containsKey("image_5")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mSaloonDetailShow5Iv);
            }


        }


    }

}
