package com.example.rent_it.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rent_it.R;
import com.example.rent_it.Registration.RegistrationContract;
import com.example.rent_it.Registration.RegistrationPresenter;


public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {
    Button btnRegistration, btnLogin;
    EditText name,email, password1, password2;
    final String TAG = "GS";


    private RegistrationPresenter mRegisterPresenter;
    ProgressDialog mPrgressDialog;

    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        initViews();
    }

    private void initViews() {
        btnRegistration = findViewById(R.id.button_register);
        btnLogin = findViewById(R.id.button_login);
        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        password1 = findViewById(R.id.password_register1);
        password2 = findViewById(R.id.password_register2);
        mRegisterPresenter = new RegistrationPresenter(this);
        mPrgressDialog = new ProgressDialog(this);
        mPrgressDialog.setMessage("Please wait, Adding profile to database...");


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegistrationDetails();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginActivity();
            }
        });

    }


    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    ///validation
    private void checkRegistrationDetails() {
        if (!TextUtils.isEmpty(email.getText().toString())
                && !TextUtils.isEmpty(password1.getText().toString())
                && !TextUtils.isEmpty(password2.getText().toString())
                && !TextUtils.isEmpty(name.getText().toString())) {
            initLogin(name.getText().toString(),email.getText().toString(), password1.getText().toString());


        } else {
            if (TextUtils.isEmpty(email.getText().toString())) {
                email.setError("Please enter a valid email");
            }
            if (TextUtils.isEmpty(password1.getText().toString())) {
                password1.setError("Please enter password");
            }
            if (TextUtils.isEmpty(password2.getText().toString())) {
                password2.setError("Please enter password");
            }
        }
    }

    private void initLogin(String name,String email, String password) {
        mPrgressDialog.show();
        mRegisterPresenter.register(this, name,email, password);
    }

    @Override
    public void onRegistrationSuccess(String message) {
        mPrgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        mPrgressDialog.dismiss();
        email.setError(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}