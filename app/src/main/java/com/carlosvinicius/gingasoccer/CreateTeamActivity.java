package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.carlosvinicius.gingasoccer.models.Team;
import com.carlosvinicius.gingasoccer.models.UsersTeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTeamActivity extends AppCompatActivity {

    @BindView(R.id.team_name_edt)
    EditText mTeamNameEditText;

    @BindView(R.id.weekday_spinner)
    Spinner mWeekdaySpinner;

    @BindView(R.id.start_time_edt)
    EditText mStartTimeEditText;

    @BindView(R.id.end_time_edt)
    EditText mEndTimeEditText;

    @BindView(R.id.address_edt)
    EditText mAddressEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weekdays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mWeekdaySpinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(getResources().getString(R.string.user_key))) {
                userKey = intent.getStringExtra(getResources().getString(R.string.user_key));
            }
        }
    }

    @OnClick(R.id.create_team_btn)
    public void createTeam(View view) {
        if (!validateForm()) {
            return;
        }

        String teamName = mTeamNameEditText.getText().toString();
        String weekday = mWeekdaySpinner.getSelectedItem().toString();
        String startTime = mStartTimeEditText.getText().toString();
        String endTime = mEndTimeEditText.getText().toString();
        String address = mAddressEditText.getText().toString();

        Team team = new Team(teamName, weekday, startTime, endTime, address);

        String key = mDatabaseReference.push().getKey();

        UsersTeam usersTeam = new UsersTeam(key, Arrays.asList(userKey));

        mDatabaseReference.child("team").child(key).setValue(team);
        mDatabaseReference.child("usersTeam").child(key).setValue(usersTeam);
    }

    private boolean validateForm() {
        boolean valid = true;

        String teamName = mTeamNameEditText.getText().toString();
        if (TextUtils.isEmpty(teamName)) {
            mTeamNameEditText.setError("Required.");
            valid = false;
        } else {
            mTeamNameEditText.setError(null);
        }

//        String weekday = mWeekdaySpinner.getSelectedItem().toString();
//        if (TextUtils.isEmpty(weekday)) {
//            ((TextView) mWeekdaySpinner.getSelectedItem()).setError("Required.");
//            valid = false;
//        } else {
//            ((TextView) mWeekdaySpinner.getSelectedItem()).setError(null);
//        }

        String startTime = mStartTimeEditText.getText().toString();
        if (TextUtils.isEmpty(startTime)) {
            mStartTimeEditText.setError("Required.");
            valid = false;
        } else {
            mStartTimeEditText.setError(null);
        }

        String endTime = mEndTimeEditText.getText().toString();
        if (TextUtils.isEmpty(endTime)) {
            mEndTimeEditText.setError("Required.");
            valid = false;
        } else {
            mEndTimeEditText.setError(null);
        }

        String address = mAddressEditText.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddressEditText.setError("Required.");
            valid = false;
        } else {
            mAddressEditText.setError(null);
        }

        return valid;
    }
}