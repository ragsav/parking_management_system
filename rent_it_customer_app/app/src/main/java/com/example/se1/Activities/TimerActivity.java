package com.example.se1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se1.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimerActivity extends AppCompatActivity {
    Window window;

    private static CountDownTimer countDownTimer;
    private static TextView countdownTimerText;
    private int duration, user_time;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Intent i = getIntent();
        duration = i.getIntExtra("duration", 0);
        user_time = i.getIntExtra("time", 0);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        countdownTimerText = findViewById(R.id.countdownText);
        button = findViewById(R.id.background_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), UserInfoActivity.class);
//                startActivity(i);
                ;
                countDownTimer.cancel();
                Intent i = new Intent(TimerActivity.this, UserInfoActivity.class);
                startActivity(i);
                onBackPressed();
            }
        });
        int current_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int current_minute = Calendar.getInstance().get(Calendar.MINUTE);


        user_time = current_hour - user_time;
        duration -= user_time;
        duration *= 60;
        duration -= current_minute;
        duration *= 60;
        duration *= 1000;

        startTimer(duration);


    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            @Override
            public void onTick(long l) {
                long millis = l;
                String hms = String.format("%02d:%02d:%02d"
                        , TimeUnit.MILLISECONDS.toHours(millis)
                        , TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
                        , TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdownTimerText.setText(hms);//set text
            }

            @Override
            public void onFinish() {
                countdownTimerText.setText("TIME'S UP!!"); //On finish change timer text
                Intent i = new Intent(TimerActivity.this, UserInfoActivity.class);
                startActivity(i);
                finish();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        Intent i = new Intent(TimerActivity.this, UserInfoActivity.class);
        startActivity(i);
        super.onBackPressed();

    }
}
