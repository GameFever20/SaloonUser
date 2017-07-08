package app.saloonuser.craftystudio.saloonuser;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.GridLayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

    public RelativeLayout mSaloonDetailRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getting window component
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        saloon = (Saloon) bundle.getSerializable("Saloon_Class");


        //animation

        /*
        Explode explode = new Explode();
        explode.setDuration(200);
        explode.setMode(Visibility.MODE_IN);
        getWindow().setEnterTransition(explode);
        */

        /*
        Slide slide = new Slide();
        slide.setDuration(250);
        slide.setSlideEdge(Gravity.TOP);
        getWindow().setEnterTransition(slide);
        */


        SaloonUID = saloon.getSaloonUID();

        //views
        mSaloonDetailRelativeLayout = (RelativeLayout) findViewById(R.id.content_saloon_detail);

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

        //animate shake in Saloon Name
        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .repeat(1)
                .playOn(mSaloonDetailNameTv);


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
        Intent intent = new Intent(SaloonDetailActivity.this, ServiceTypeActivity.class);
        intent.putExtra("Saloon", saloon);
        startActivity(intent);
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
                        .override(1000, 500)
                        .crossFade(100)
                        .fitCenter()
                        .into(mSaloonDetailProfileIv);
            }

            //Display 1 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_1");

            if (saloon.getSaloonImageList().containsKey("image_1")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(300, 300)
                        .crossFade(100)
                        .fitCenter()
                        .into(mSaloonDetailShow1Iv);
            }

            //Display 2 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_2");

            if (saloon.getSaloonImageList().containsKey("image_2")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(300, 300)
                        .crossFade(100)
                        .fitCenter()
                        .into(mSaloonDetailShow2Iv);
            }

            //Display 3 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_3");

            if (saloon.getSaloonImageList().containsKey("image_3")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(300, 300)
                        .crossFade(100)
                        .fitCenter()

                        .into(mSaloonDetailShow3Iv);
            }

            //Display 4 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_4");

            if (saloon.getSaloonImageList().containsKey("image_4")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(300, 300)
                        .crossFade(100)
                        .fitCenter()

                        .into(mSaloonDetailShow4Iv);
            }


            //Display 5 showcase image
            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_5");

            if (saloon.getSaloonImageList().containsKey("image_5")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(300, 300)
                        .crossFade(100)
                        .fitCenter()

                        .into(mSaloonDetailShow5Iv);
            }


        }


    }

    public void openImageDisplayActivity(View view) {

        //get screen size
        DisplayMetrics metrics = SaloonDetailActivity.this.getResources().getDisplayMetrics();
        int fullWidth = metrics.widthPixels;
        int fullHeight = metrics.heightPixels;

        int centerX = (view.getLeft() + view.getRight()) / 2;
        int centerY = (view.getTop() + view.getBottom()) / 2;

        float radius = Math.max(view.getWidth(), view.getHeight()) * 2.0f;

        Toast.makeText(this, " width " + view.getWidth() + "  height " + view.getHeight(), Toast.LENGTH_SHORT).show();

        float maxradius = Math.max(fullWidth, fullHeight) * 2.0f;
        Toast.makeText(this, "full width " + fullWidth + " full height " + fullHeight, Toast.LENGTH_SHORT).show();

        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, maxradius);
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SaloonDetailActivity.this);
                Intent intent = new Intent(SaloonDetailActivity.this, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Saloon", saloon);
                intent.putExtras(bundle);
                startActivity(intent, options.toBundle());


            }

            @Override
            public void onAnimationEnd(Animator animator) {


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        reveal.start();

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        // onBackPressed();
        return true;
    }

}
