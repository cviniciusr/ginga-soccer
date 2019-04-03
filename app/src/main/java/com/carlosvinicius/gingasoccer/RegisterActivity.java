package com.carlosvinicius.gingasoccer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    @BindView(R.id.fullname_edt)
    EditText fullnameEditText;

    @BindView(R.id.nickname_edt)
    EditText nicknameEditText;

    @BindView(R.id.register_email_edt)
    EditText emailEditText;

    @BindView(R.id.birth_date_edt)
    EditText birthDateEditText;

    @BindView(R.id.register_password_edt)
    EditText passwordEditText;

    @BindView(R.id.confirm_password_edt)
    EditText confirmPasswordEditText;

    @BindView(R.id.sign_in_btn)
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference("users");

        birthDateEditText.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;

            datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedMonthText;
                    String selectedDayText;

                    selectedMonth = selectedMonth + 1;

                    if (selectedMonth < 10) {
                        selectedMonthText = "0" + selectedMonth;
                    } else {
                        selectedMonthText = "" + selectedMonth;
                    }

                    if (selectedDay < 10) {
                        selectedDayText = "0" + selectedDay;
                    } else {
                        selectedDayText = "" + selectedDay;
                    }

                    birthDateEditText.setText(selectedDayText + "/" + selectedMonthText + "/" + selectedYear);
                },
                year,
                month,
                day);

            datePickerDialog.setTitle("Select Date");
            datePickerDialog.show();
        });
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn(View view) {
        if (!validateForm()) {
            return;
        }

        String fullname = fullnameEditText.getText().toString();
        String nickname = nicknameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, (task) -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");

                    User user = new User(fullname, nickname, email, birthDate, password);

                    String key = usersDatabaseReference.push().getKey();
                    usersDatabaseReference.child(key).setValue(user.toMap());

                    Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
                    intent.putExtra(getResources().getString(R.string.user), user);
                    intent.putExtra(getResources().getString(R.string.user_key), key);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String fullname = fullnameEditText.getText().toString();
        if (TextUtils.isEmpty(fullname)) {
            fullnameEditText.setError("Required.");
            valid = false;
        } else {
            fullnameEditText.setError(null);
        }

        String nickname = nicknameEditText.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            nicknameEditText.setError("Required.");
            valid = false;
        } else {
            nicknameEditText.setError(null);
        }

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String birthDate = birthDateEditText.getText().toString();
        if (TextUtils.isEmpty(birthDate)) {
            birthDateEditText.setError("Required.");
            valid = false;
        } else {
            birthDateEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Password should be at least 6 characters.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Required.");
            valid = false;
        } else {
            confirmPasswordEditText.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm Password must be equal to password");
            valid = false;
        }

        return valid;
    }
}
