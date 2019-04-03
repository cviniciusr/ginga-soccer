package com.carlosvinicius.gingasoccer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosvinicius.gingasoccer.appwidget.GingaSoccerWidgetProvider;
import com.carlosvinicius.gingasoccer.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;
    private ChildEventListener childEventListener;

    @BindView(R.id.email_edt)
    EditText emailEditText;

    @BindView(R.id.password_edt)
    EditText passwordEditText;

    @BindView(R.id.login_btn)
    Button loginButton;

    @BindView(R.id.register_btn)
    TextView registerButton;

    @BindView(R.id.forgot_password_btn)
    TextView forgotPasswordButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference("users");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
                intent.putExtra(getResources().getString(R.string.user), user);
                intent.putExtra(getResources().getString(R.string.user_key), dataSnapshot.getKey());

                SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                        getResources().getString(R.string.ginga_soccer_preferences), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getResources().getString(R.string.user_key), dataSnapshot.getKey());
                editor.commit();

                GingaSoccerWidgetProvider.sendRefreshBroadcast(LoginActivity.this);

                progressDialog.dismiss();

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(LoginActivity.this
                        , "Friend changed: " + dataSnapshot.getKey()
                                + "  name: " + user.getFullname()
                                + "  e-mail: " + user.getEmail()
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Toast.makeText(LoginActivity.this
                        , "Friend removed: " + dataSnapshot.getKey()
                                + "  name: " + user.getFullname()
                                + "  e-mail: " + user.getEmail()
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "childEventListener, childEventListener()");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "childEventListener, onCancelled()");
            }
        };

        if (mAuth.getCurrentUser() != null) {
            Query query = usersDatabaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
            query.addChildEventListener(childEventListener);
        }
    }

    @OnClick(R.id.login_btn)
    public void login(View view) {
        if (!validateForm()) {
            return;
        }

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");

                        Query query = usersDatabaseReference.orderByChild("email").equalTo(email);
                        query.addChildEventListener(childEventListener);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
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

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}
