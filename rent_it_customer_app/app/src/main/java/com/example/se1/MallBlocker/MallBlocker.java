package com.example.se1.MallBlocker;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.se1.Auth.Auth;
import com.example.se1.Models.Mall;
import com.example.se1.PlotProcessor.PlotProcessor;
import com.example.se1.utils.SorryDialogBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


public class MallBlocker {

    private int entry_direction;
    private String mall_id;
    private int start_time;
    private int duration;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private AlertDialog alertDialog;
    private Auth auth;

    public MallBlocker(int entry_direction
            , String mall_id
            , int start_time
            , int duration
            , AlertDialog alertDialog
            , Context context
            , Auth auth) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.start_time = start_time;
        this.duration = duration;
        this.entry_direction = entry_direction;
        this.mall_id = mall_id;
        this.alertDialog = alertDialog;
        this.context = context;
        this.auth = auth;
        init();
    }

    private void init() {


        if (entry_direction == 1) {

            firebaseFirestore.collection("Notebook").document(mall_id)
                    .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        if (task.getResult().toObject(Mall.class).isIs_blocked() == true) {
                            alertDialog.dismiss();
                            new SorryDialogBox(context, "Mall is busy,please try again!");
                            return;
                        } else {
                            blockMall();
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    alertDialog.dismiss();
                    new SorryDialogBox(context, "Oop!Something went Wrong");
                    return;
                }
            });
        }

    }

    private void blockMall() {
        firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", true);
        new PlotProcessor(alertDialog, mall_id, start_time, duration, context, auth);
    }


}
