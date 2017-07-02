package app.saloonuser.craftystudio.saloonuser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import utils.FireBaseHandler;
import utils.User;

public class UserProfileActivity extends AppCompatActivity {

    TextView mUserName, mUserAge, mUserGender, mUserMobileNumber;
    ImageView mUserProfileImage;

    FireBaseHandler fireBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //views
        mUserName = (TextView) findViewById(R.id.userProfile_name_textview);
        mUserAge = (TextView) findViewById(R.id.userProfile_age_textview);
        mUserGender = (TextView) findViewById(R.id.userProfile_gender_textview);
        mUserMobileNumber = (TextView) findViewById(R.id.userProfile_number_textview);

        mUserProfileImage = (ImageView) findViewById(R.id.userProfile_image_imageview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        fireBaseHandler.downloadUser(LoginActivity.USER.getUserUID(), new FireBaseHandler.OnUserlistener() {
            @Override
            public void onUserDownLoad(User user, boolean isSuccessful) {
                mUserName.setText(user.getUserName());
                mUserAge.setText(user.getUserAge()+"");
                mUserGender.setText(user.getUserGender());

                if (user.getUserGender().equalsIgnoreCase("Male")) {
                    mUserProfileImage.setImageResource(R.drawable.malefinal);
                } else {
                    mUserProfileImage.setImageResource(R.drawable.female_copy);
                }

                mUserMobileNumber.setText(user.getUserPhoneNumber());
            }

            @Override
            public void onUserUpload(boolean isSuccessful) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
