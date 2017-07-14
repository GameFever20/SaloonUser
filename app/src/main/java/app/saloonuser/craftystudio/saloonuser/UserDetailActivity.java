package app.saloonuser.craftystudio.saloonuser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import utils.FireBaseHandler;
import utils.User;

public class UserDetailActivity extends AppCompatActivity {

    public  User USER;

    EditText mUserNameEdittext, mUserAgeEdittext, mUserGenderEdittext;

    ImageView mMaleImage, mFemaleImage, mUserprofile;

    TextView mUserGenderTextview;




     String GENDER;

    LinearLayout mUserGenderDetailLinearLayout;
    LinearLayout mUserDetailLinearlayout;

    Button mUploadBtn, mNextUploadButton;
    private ProgressDialog progressDialog;
    private String mCitySelected;
    private int mCitySelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);


        progressDialog = new ProgressDialog(this);

        //detail filling layout
        mUserNameEdittext = (EditText) findViewById(R.id.userDetail_userName_edittext);
        //mUserGenderEdittext = (EditText) findViewById(R.id.userDetail_userGender_edittext);
        mUserAgeEdittext = (EditText) findViewById(R.id.userDetail_userAge_edittext);
        mUserGenderTextview = (TextView) findViewById(R.id.userDetail_userGender_textview);
        mUserprofile = (ImageView) findViewById(R.id.userdetail_userprofile_imageview);


        //male female selection layout
        mUserGenderDetailLinearLayout = (LinearLayout) findViewById(R.id.user_genderdetail_linearlayout);
        mMaleImage = (ImageView) findViewById(R.id.user_detail_male_imageview);
        mFemaleImage = (ImageView) findViewById(R.id.user_detail_female_imageview);
        mUploadBtn = (Button) findViewById(R.id.user_detail_upload_button);

        mNextUploadButton = (Button) findViewById(R.id.nextUserDetailButton);

        mUserDetailLinearlayout = (LinearLayout)findViewById(R.id.userDetail_userDetail_linearLayout);


    }


    public void uploadUserDetailButtonClick(View view) {

        creteUser();

        showProgressDialog("Uploading");

        new FireBaseHandler().uploadUser(USER, new FireBaseHandler.OnUserlistener() {
            @Override
            public void onUserDownLoad(User user, boolean isSuccessful) {


            }

            @Override
            public void onUserUpload(boolean isSuccessful) {
                closeProgressDialog();
                if (isSuccessful) {
                    Toast.makeText(UserDetailActivity.this, "User uploaded", Toast.LENGTH_SHORT).show();
LoginActivity.USER =USER;
                    openMainActivity();

                } else {
                    Toast.makeText(UserDetailActivity.this, "Failed to upload User", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openMainActivity() {
        Intent intent = new Intent(UserDetailActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void creteUser() {
        USER = LoginActivity.USER;
        USER.setUserName(mUserNameEdittext.getText().toString().trim());
        //  USER.setUserGender(mUserGenderEdittext.getText().toString().trim());
        USER.setUserGender(GENDER);
        USER.setUserAge(Integer.valueOf(mUserAgeEdittext.getText().toString().trim()));

        USER.setUserCity(mCitySelected);
        USER.setUserCityIndex(mCitySelectedIndex);

    }

    public void femaleSelection(View view) {
        mFemaleImage.setImageResource(R.drawable.femalecolor);
        mMaleImage.setImageResource(R.drawable.malefinal);
        GENDER = "Female";
        Toast.makeText(this, "Hey! Beautiful", Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            //animate Next Button
            YoYo.with(Techniques.FlipInX)
                    .duration(1000)
                    .repeat(1)
                    .playOn(mNextUploadButton);

        } else {
            // do something for phones running an SDK before lollipop
        }

    }

    public void maleSelection(View view) {
        mFemaleImage.setImageResource(R.drawable.female_copy);
        mMaleImage.setImageResource(R.drawable.malecolor);
        GENDER = "Male";
        Toast.makeText(this, "Hey! HandSome", Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            //animate Next Button
            YoYo.with(Techniques.FlipInX)
                    .duration(1000)
                    .repeat(1)
                    .playOn(mNextUploadButton);

        } else {
            // do something for phones running an SDK before lollipop
        }


    }


    public void userDetailNext(View view) {

        mUserGenderDetailLinearLayout.setVisibility(View.GONE);

        /*mUserNameEdittext.setVisibility(View.VISIBLE);
        mUserAgeEdittext.setVisibility(View.VISIBLE);
        mUploadBtn.setVisibility(View.VISIBLE);
        //mUserGenderTextview.setText(GENDER);
        //  mUserGenderTextview.setVisibility(View.VISIBLE);
        mUserprofile.setVisibility(View.VISIBLE);*/

        mUserDetailLinearlayout.setVisibility(View.VISIBLE);

        if (GENDER.equalsIgnoreCase("Male")) {
            mUserprofile.setImageResource(R.drawable.malefinal);
        } else {
            mUserprofile.setImageResource(R.drawable.female_copy);
        }
    }

    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        progressDialog.dismiss();
    }


    public void selectCity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your city")
                .setItems(R.array.city, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item


                        mCitySelected  = getResources().getStringArray(R.array.city)[which];
                        mCitySelectedIndex =which;



                        TextView textView = (TextView) findViewById(R.id.userDetail_userCity_textView);
                        textView.setText(mCitySelected);


                    }
                });
        builder.create();
        builder.show();
    }
}
