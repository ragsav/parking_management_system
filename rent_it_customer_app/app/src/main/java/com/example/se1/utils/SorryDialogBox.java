package com.example.se1.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.se1.Login.LoginContract;
import com.example.se1.R;

public class SorryDialogBox {
    private AlertDialog alertDialog;
    private Context context;
    private View dialogView;
    private TextView textView;

    public SorryDialogBox(Context context, String text) {
        this.context = context;
        this.dialogView = View.inflate(context, R.layout.sorry_dialog, null);
        alertDialog = new AlertDialog.Builder(context).create();
        textView = dialogView.findViewById(R.id.text_view_sorry);
        textView.setText(text);
        dialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
