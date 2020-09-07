package com.example.rent_it.Registration;

import android.app.Activity;



public class RegistrationPresenter implements RegistrationContract.Presenter, RegistrationContract.onRegistrationListener {
    private RegistrationContract.View mRegisterView;
    private RegistrationInteractor mRegistrationInteractor;

    public RegistrationPresenter(RegistrationContract.View registerView) {
        this.mRegisterView = registerView;
        mRegistrationInteractor = new RegistrationInteractor(this);
    }

    @Override
    public void register(Activity activity,String name, String email, String password) {
        mRegistrationInteractor.performFirebaseRegistration(activity, name,email, password);
    }

    @Override
    public void onSuccess(String message) {
        mRegisterView.onRegistrationSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);

    }


}
