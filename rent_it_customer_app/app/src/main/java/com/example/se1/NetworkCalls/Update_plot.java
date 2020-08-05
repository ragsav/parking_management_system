package com.example.se1.NetworkCalls;

import android.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Update_plot {
    private AlertDialog alertDialog;
    private String final_id;
    private String mall_id;
    List<String> modified_occupied_array = new ArrayList<>();
    List<String> modified_occupied_user_array = new ArrayList<>();

    public Update_plot(AlertDialog alertDialog, String final_id, String mall_id, List<String> modified_occupied_array, List<String> modified_occupied_user_array) {
        this.alertDialog = alertDialog;
        this.final_id = final_id;
        this.mall_id = mall_id;
        this.modified_occupied_array = modified_occupied_array;
        this.modified_occupied_user_array = modified_occupied_user_array;
        init();
    }

    private void init() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(mall_id).document(final_id).update("occupied_time", modified_occupied_array);
        firebaseFirestore.collection(mall_id).document(final_id).update("occupied_user", modified_occupied_user_array);
        alertDialog.dismiss();
    }
}
