package com.carlosvinicius.gingasoccer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    @BindView(R.id.recovery_email_edt)
    EditText recoveryEmailEditText;

    @BindView(R.id.recovery_send_btn)
    Button recoverySendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.recovery_send_btn)
    public void recoverySend(View view) {
        if (!validateForm()) {
            return;
        }

        String email = recoveryEmailEditText.getText().toString();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Email sent.");
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = recoveryEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            recoveryEmailEditText.setError("Required.");
            valid = false;
        } else {
            recoveryEmailEditText.setError(null);
        }

        return valid;
    }
}
