package com.example.se1.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.se1.Auth.Auth;
import com.example.se1.MallBlocker.MallBlocker;
import com.example.se1.R;

public class DateTimeManager {
    private Context context;
    private View dialogView;
    private Integer opening_time;
    private Integer closing_time;
    private NumberPicker numberPicker;
    private AlertDialog alertDialog;
    private String mall_id;
    private Auth auth;

    public DateTimeManager(final Context context, Integer opening_time, Integer closing_time, final String mall_id, final Auth auth) {
        this.closing_time = closing_time;
        this.opening_time = opening_time;
        this.context = context;
        this.mall_id = mall_id;
        this.dialogView = View.inflate(context, R.layout.date_time_picker, null);
        this.numberPicker = dialogView.findViewById(R.id.numberPicker);
        this.auth = auth;
        numberPicker.setMaxValue(3);
        numberPicker.setMinValue(1);
        alertDialog = new AlertDialog.Builder(context).create();
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("floating button pressed", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                Calendar minTimeStamp = Calendar.getInstance();
                Calendar maxTimeStamp = Calendar.getInstance();
                ScrollView scrollView = dialogView.findViewById(R.id.scroll_view);
                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                //DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                timePicker.is24HourView();
                //minTimeStamp.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                //maxTimeStamp.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                //datePicker.setMaxDate(maxTimeStamp.getTimeInMillis());
                //datePicker.setMinDate(minTimeStamp.getTimeInMillis());
                timePicker.is24HourView();
                timePicker.setMinute(0);
//                Calendar finalTimeStamp = Calendar.getInstance();
//                finalTimeStamp.set(datePicker.getYear()
//                        ,datePicker.getMonth()
//                        ,datePicker.getDayOfMonth()
//                        ,timePicker.getHour()
//                        ,timePicker.getMinute());

                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                new MallBlocker(1, mall_id, timePicker.getHour(), numberPicker.getValue(), alertDialog, context, auth);
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }


}
