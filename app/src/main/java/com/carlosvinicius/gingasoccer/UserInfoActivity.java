package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.carlosvinicius.gingasoccer.models.User;

public class UserInfoActivity extends AppCompatActivity {

    private static final String TAG = UserInfoActivity.class.getSimpleName();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.user))) {
                user = (User) intent.getSerializableExtra(getResources().getString(R.string.user));

                Log.d(TAG, "User: " + user.getFullname());
            }
        }
    }
}
