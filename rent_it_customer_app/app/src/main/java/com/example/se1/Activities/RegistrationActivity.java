package com.example.se1.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.se1.Auth.Auth;
import com.example.se1.R;
import com.example.se1.Registration.RegistrationContract;
import com.example.se1.Registration.RegistrationPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, RegistrationContract.View {
    TextView tvLogin;
    Button btnRegistration;
    EditText edtEmail, edtPassword;
    final String TAG = "GS";
//    private static final int RC_SIGN_IN = 1001;

    private RegistrationPresenter mRegisterPresenter;
    ProgressDialog mPrgressDialog;
    private TextView email_verified;
    private Window window;
    private ImageView gSignIn;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    Auth auth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initViews();
    }

    private void initViews() {
        btnRegistration = (Button) findViewById(R.id.btn_register);
        gSignIn = findViewById(R.id.g_sign_in_button);
        email_verified = findViewById(R.id.email_verified);
//        email_verified.setVisibility(View.INVISIBLE);
        btnRegistration.setOnClickListener(this);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(this);
        edtEmail = (EditText) findViewById(R.id.email_register);
        edtPassword = (EditText) findViewById(R.id.password_register);
        mRegisterPresenter = new RegistrationPresenter(this);

        mPrgressDialog = new ProgressDialog(this);
        mPrgressDialog.setMessage("Please wait, Adding profile to database.");
        gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrgressDialog.setMessage("Authenticating user...");
                mPrgressDialog.show();
                signIn();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                checkRegistrationDetails();
                break;
            case R.id.tv_login:
                moveToLoginActivity();
                break;
            case R.id.g_sign_in_button:
                break;

        }
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkRegistrationDetails() {
        if (!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {
            initLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
        } else {
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                edtEmail.setError("Please enter a valid email");
            }
            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                edtPassword.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        mPrgressDialog.show();
        mRegisterPresenter.register(this, email, password, mPrgressDialog, email_verified);
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mPrgressDialog.dismiss();
//        btnRegistration.setText("Email verified ?");
        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrationActivity.this, MallListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        mPrgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken(), this);
            } catch (ApiException e) {

                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "There was a problem signing in through Google", Toast.LENGTH_LONG);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, final Context context) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore.getInstance().collection("User").document(user.getUid())
                                    .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            mPrgressDialog.cancel();
                                            startActivity(new Intent(RegistrationActivity.this, MallListActivity.class));
                                            finish();
                                        } else {
                                            addNewUser(user);
                                            mPrgressDialog.cancel();
                                            startActivity(new Intent(RegistrationActivity.this, MallListActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            });


                        } else {
                            mPrgressDialog.cancel();


                            Toast.makeText(context, "There was a problem signing in through Google", Toast.LENGTH_LONG);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    private void addNewUser(FirebaseUser user) {
        List<String> l = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> docData = new HashMap<>();
        docData.put("user_id", user.getUid());
        docData.put("current_time", 0);
        docData.put("current_duration", 0);
        docData.put("current_mall_id", "");
        docData.put("current_plot_id", "");
        docData.put("has_taken", false);
        docData.put("transaction_history", l);
        docData.put("name", "");
        docData.put("extended_time", 0);
        docData.put("entry", false);
        docData.put("mb_id", "");

        firebaseFirestore.collection("User").document(user.getUid()).set(docData);
    }
}