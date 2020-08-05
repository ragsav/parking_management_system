package com.example.se1.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;


public class RegistrationInteractor implements RegistrationContract.Intractor {

    private static final String TAG = RegistrationInteractor.class.getSimpleName();
    private RegistrationContract.onRegistrationListener mOnRegistrationListener;

    public RegistrationInteractor(RegistrationContract.onRegistrationListener onRegistrationListener) {
        this.mOnRegistrationListener = onRegistrationListener;
    }

    @Override
    public void performFirebaseRegistration(final Activity activity,
                                            String email,
                                            String password,
                                            final ProgressDialog progressDialog, final TextView button) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            mOnRegistrationListener.onFailure(task.getException().getMessage());
                        } else {
                            final FirebaseUser user = task.getResult().getUser();
                            addNewUser(user);
                            progressDialog.setMessage("Sending Verification email");
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser().reload();

                                        progressDialog.cancel();

                                        button.setText("Email verified?");
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                FirebaseAuth.getInstance().getCurrentUser()
                                                        .reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.i("verification", "hello????????????????????????????????????????"
                                                                    + FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
                                                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                                                mOnRegistrationListener.onSuccess(user);

                                                            } else {
                                                                Toast.makeText(activity, "Please verify email first!", Toast.LENGTH_LONG);
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        });
                                    }
                                }
                            });

//                                    .update("user_id",user.getUid(),
//                                    "current_time",0,
//                                    "current_duration",0,
//                                    "current_mall_id","",
//                                    "current_plt_id","",
//                                    "has_taken",false,
//                                    "transaction_history",l,
//                                    "name","",
//                                    "extended_time",0,
//                                            "entry",false,
//                                            "mb_id","");
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
