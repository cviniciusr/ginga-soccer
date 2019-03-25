package com.carlosvinicius.gingasoccer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    @BindView(R.id.fullname_edt)
    TextView fullnameTextView;

    @BindView(R.id.nickname_edt)
    TextView nicknameTextView;

    @BindView(R.id.register_email_edt)
    TextView emailTextView;

    @BindView(R.id.birth_date_edt)
    TextView birthDateTextView;

    @BindView(R.id.register_password_edt)
    TextView passwordTextView;

    @BindView(R.id.confirm_password_edt)
    TextView confirmPasswordTextView;

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
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn(View view) {
        if (!validateForm()) {
            return;
        }

        String fullname = fullnameTextView.getText().toString();
        String nickname = nicknameTextView.getText().toString();
        String email = emailTextView.getText().toString();
        String birthDate = birthDateTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
            .addOnCompleteListener(this, (task) -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    User user = new User(fullname, nickname, email, birthDate, password);

//                    String key = usersDatabaseReference.push().getKey();
//                    usersDatabaseReference.child(key).setValue(user);
//                        updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String fullname = fullnameTextView.getText().toString();
        if (TextUtils.isEmpty(fullname)) {
            fullnameTextView.setError("Required.");
            valid = false;
        } else {
            fullnameTextView.setError(null);
        }

        String nickname = nicknameTextView.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            nicknameTextView.setError("Required.");
            valid = false;
        } else {
            nicknameTextView.setError(null);
        }

        String email = emailTextView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailTextView.setError("Required.");
            valid = false;
        } else {
            emailTextView.setError(null);
        }

        String birthDate = birthDateTextView.getText().toString();
        if (TextUtils.isEmpty(birthDate)) {
            birthDateTextView.setError("Required.");
            valid = false;
        } else {
            birthDateTextView.setError(null);
        }

        String password = passwordTextView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextView.setError("Required.");
            valid = false;
        } else {
            passwordTextView.setError(null);
        }

        String confirmPassword = confirmPasswordTextView.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordTextView.setError("Required.");
            valid = false;
        } else {
            confirmPasswordTextView.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordTextView.setError("Confirm Password must be equal to password");
            valid = false;
        }

        return valid;
    }
}
