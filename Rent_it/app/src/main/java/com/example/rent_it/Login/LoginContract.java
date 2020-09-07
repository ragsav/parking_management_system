package com.example.rent_it.Login;

import android.app.Activity;

import org.json.JSONObject;

public interface LoginContract {
    interface View {
        void onLoginSuccess(String token,JSONObject user);

        void onLoginFailure(String message);
    }

    interface Presenter {
        void login(Activity activity, String email, String password);
    }

    interface Intractor {
        void performLogin(Activity activity, String email, String password);
    }

    interface onLoginListener {
        void onSuccess(String token, JSONObject user);

        void onFailure(String message);
    }
}
