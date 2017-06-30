package app.saloonuser.craftystudio.saloonuser;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import utils.FireBaseHandler;
import utils.User;

public class UserDetailActivity extends AppCompatActivity {

    public static User USER;

    EditText mUserNameEdittext , mUserAgeEdittext , mUserGenderEdittext ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        mUserNameEdittext =(EditText)findViewById(R.id.userDetail_userName_edittext);
        mUserGenderEdittext =(EditText)findViewById(R.id.userDetail_userGender_edittext);
        mUserAgeEdittext =(EditText)findViewById(R.id.userDetail_userAge_edittext);





    }


    public void uploadUserDetailButtonClick(View view) {

        creteUser();

        new FireBaseHandler().uploadUser(USER, new FireBaseHandler.OnUserlistener() {
            @Override
            public void onUserDownLoad(User user, boolean isSuccessful) {


            }

            @Override
            public void onUserUpload(boolean isSuccessful) {

                if (isSuccessful){
                    Toast.makeText(UserDetailActivity.this, "User uploaded", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserDetailActivity.this, "Failed to upload User", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void creteUser() {
        USER = new User();
        USER.setUserName(mUserNameEdittext.getText().toString().trim());
        USER.setUserGender(mUserGenderEdittext.getText().toString().trim());
        USER.setUserAge(Integer.valueOf(mUserAgeEdittext.getText().toString().trim()));
        USER.setUserUID(LoginActivity.USERUID);

    }
}
