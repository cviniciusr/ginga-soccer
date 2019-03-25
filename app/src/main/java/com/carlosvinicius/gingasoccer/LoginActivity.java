package com.carlosvinicius.gingasoccer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    @BindView(R.id.email_edt)
    TextView emailTextView;

    @BindView(R.id.password_edt)
    TextView passwordTextView;

    @BindView(R.id.login_btn)
    Button loginButton;

    @BindView(R.id.register_btn)
    Button registerButton;

    @BindView(R.id.forgot_password_btn)
    Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.login_btn)
    public void login(View view) {
        if (!validateForm()) {
            return;
        }

        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
            .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
//                    }
            });
    }

    @OnClick(R.id.register_btn)
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.forgot_password_btn)
    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailTextView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailTextView.setError("Required.");
            valid = false;
        } else {
            emailTextView.setError(null);
        }

        String password = passwordTextView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextView.setError("Required.");
            valid = false;
        } else {
            passwordTextView.setError(null);
        }

        return valid;
    }
}
