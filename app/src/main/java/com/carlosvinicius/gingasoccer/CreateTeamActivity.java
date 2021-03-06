package com.carlosvinicius.gingasoccer;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.carlosvinicius.gingasoccer.appwidget.GingaSoccerWidgetProvider;
import com.carlosvinicius.gingasoccer.models.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTeamActivity extends AppCompatActivity {

    @BindView(R.id.team_name_edt)
    TextInputEditText mTeamNameEditText;

    @BindView(R.id.weekday_spinner)
    Spinner mWeekdaySpinner;

    @BindView(R.id.start_time_edt)
    EditText mStartTimeEditText;

    @BindView(R.id.end_time_edt)
    EditText mEndTimeEditText;

    @BindView(R.id.address_edt)
    TextInputEditText mAddressEditText;

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

        mStartTimeEditText.setOnClickListener(view -> setTime(mStartTimeEditText));
        mEndTimeEditText.setOnClickListener(view -> setTime(mEndTimeEditText));

//        mStartTimeEditText.setOnTouchListener(view -> {
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(CreateTeamActivity.this,
//                    (timePicker, selectedHour, selectedMinute) -> {
//                        mStartTimeEditText.setText( selectedHour + ":" + selectedMinute);
//                    },
//                    hour,
//                    minute,
//                    true);
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();
//
//            return true;
//        });
    }

    private void setTime(EditText editText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateTeamActivity.this,
            (timePicker, selectedHour, selectedMinute) -> {
                String selectedMinuteText;

                if (selectedMinute < 10) {
                    selectedMinuteText = "0" + selectedMinute;
                } else {
                    selectedMinuteText = "" + selectedMinute;
                }

                editText.setText(selectedHour + ":" + selectedMinuteText);
            },
            hour,
            minute,
            true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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

        String key = mDatabaseReference.push().getKey();

//        UsersTeam usersTeam = new UsersTeam(key);

        Team team = new Team(teamName, weekday, startTime, endTime, address);

        mDatabaseReference.child("team").child(key).setValue(team.toMap());

        Map<String, Object> addUsers = new HashMap<>();

        String teamPath = "team/" + key + "/usersTeam/" + userKey;
        addUsers.put(teamPath, true);
        mDatabaseReference.updateChildren(addUsers);

        Intent intent = new Intent(this, TeamInfoActivity.class);
        intent.putExtra(getResources().getString(R.string.team), team);
        intent.putExtra(getResources().getString(R.string.user_key), userKey);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

//    @OnTouch(R.id.start_time_edt)
//    public boolean setTime(View view) {
//        EditText editText = (EditText) view;
//
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(CreateTeamActivity.this,
//                (timePicker, selectedHour, selectedMinute) -> {
//                    editText.setText( selectedHour + ":" + selectedMinute);
//                },
//                hour,
//                minute,
//                true);
//        mTimePicker.setTitle("Select Time");
//        mTimePicker.show();
//
//        return false;
//    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                mAuth.signOut();

                SharedPreferences sharedPref = getSharedPreferences(
                        getResources().getString(R.string.ginga_soccer_preferences), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getResources().getString(R.string.user_key), "");
                editor.commit();

                GingaSoccerWidgetProvider.sendRefreshBroadcast(this);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
