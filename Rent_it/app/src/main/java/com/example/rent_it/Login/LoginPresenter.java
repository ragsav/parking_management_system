package com.example.rent_it.Login;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Ashish on 27-09-2017.
 */

public class LoginPresenter implements LoginContract.Presenter, LoginContract.onLoginListener {
    private LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;


    public LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password) {
        Log.i("TAG", email + " " + password);
        mLoginInteractor.performLogin(activity, email, password);

    }

    @Override
    public void onSuccess(String token, JSONObject user) {
        mLoginView.onLoginSuccess(token,user);

    }

    @Override
    public void onFailure(String message) {
        mLoginView.onLoginFailure(message);

    }
}
