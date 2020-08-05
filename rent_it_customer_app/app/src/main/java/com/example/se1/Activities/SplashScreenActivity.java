package com.example.se1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.R;

import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Animation top, bottom;
    Auth auth;
    int current_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.i("Splash", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>before auth");
        auth = new Auth();
        Calendar calendar = Calendar.getInstance();
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        Log.i("Splash", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + current_time);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageView.setAnimation(top);
        textView.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auth.getUser(new AuthOnCompleteRetreiveInterface() {
                    @Override
                    public void onFireBaseUserRetrieveSuccess() {
                        if (auth.user == null) {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
//                        else if(auth.user!=null&& auth.user.isHas_taken()&& auth.user.getCurrent_time()>=hourOfDay
//                                &&auth.user.getCurrent_time()+auth.user.getCurrent_duration()>=hourOfDay)
//                        {
//                            Intent intent = new Intent(SplashScreenActivity.this,TimerActivity.class);
//                            intent.putExtra("duration",auth.user.getCurrent_duration());
//                            startActivity(intent);
//                            finish();
//                        }

                        else {
                            Intent intent = new Intent(SplashScreenActivity.this, MallListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFireBaseUserRetrieveFailure() {
                        Log.i("reached here", ">>>>>>>>>>>>>>>>>>>>>>>");
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
