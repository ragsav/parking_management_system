package com.example.se1.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;


public class RegistrationPresenter implements RegistrationContract.Presenter, RegistrationContract.onRegistrationListener {
    private RegistrationContract.View mRegisterView;
    private RegistrationInteractor mRegistrationInteractor;

    public RegistrationPresenter(RegistrationContract.View registerView) {
        this.mRegisterView = registerView;
        mRegistrationInteractor = new RegistrationInteractor(this);
    }

    @Override
    public void register(Activity activity, String email, String password, ProgressDialog progressDialog, TextView button) {
        mRegistrationInteractor.performFirebaseRegistration(activity, email, password, progressDialog, button);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onRegistrationSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);

    }
}
