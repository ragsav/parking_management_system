package com.example.rent_it.Registration;

import android.app.Activity;


public interface RegistrationContract {
    interface View {
        void onRegistrationSuccess(String message);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String name,String email, String password);
    }

    interface Intractor {
        void performFirebaseRegistration(Activity activity, String name,String email, String password);
    }

    interface onRegistrationListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
