package com.example.se1.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ashish on 27-09-2017.
 */

public interface RegistrationContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String email, String password, ProgressDialog progressDialog, TextView button);
    }

    interface Intractor {
        void performFirebaseRegistration(Activity activity, String email, String password, ProgressDialog progressDialog, TextView button);
    }

    interface onRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}
