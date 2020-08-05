package com.example.se1.PlotProcessor;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.se1.Auth.Auth;
import com.example.se1.MallBlocker.MallBlocker;
import com.example.se1.Models.Plot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.List;

public class PlotProcessor {
    private AlertDialog alertDialog;
    private String mall_id;
    private FirebaseFirestore firebaseFirestore;
    private int start_time;
    private int duration;
    private Context context;
    private Auth auth;

    public PlotProcessor(AlertDialog alertDialog, String mall_id, int start_time, int duration, Context context, Auth auth) {
        this.alertDialog = alertDialog;
        this.mall_id = mall_id;
        this.start_time = start_time;
        this.duration = duration;
        this.context = context;
        this.auth = auth;
        firebaseFirestore = FirebaseFirestore.getInstance();
        init();
    }

    private void init() {
        Log.i("PlotProcessor", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + mall_id);
        firebaseFirestore.collection(mall_id).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Plot> plotList = task.getResult().toObjects(Plot.class);
                    if (!plotList.isEmpty()) {
                        Log.i("plotList", ">>>>>>>>>>>>>>>>>>>>>>>>>>>." + plotList.toString());

                        new PlotSelectorAsyncTask(start_time, plotList, duration, mall_id, alertDialog, context, auth).execute(0);
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
                alertDialog.dismiss();
            }
        });
    }
}
