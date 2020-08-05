package com.example.se1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.NetworkCalls.ImageDownloader;
import com.example.se1.R;
import com.example.se1.utils.CircleImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.Serializable;
import java.util.Calendar;

public class UserInfoActivity extends AppCompatActivity {

    private Auth auth;
    private TextView name, refresh, email, sign_out;
    private CircleImageView profile_image;
    private CardView transaction_image, qrCode, user_settings, timer;
    private ConnectivityManager connectivityManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private FrameLayout frameLayout;
    private ProgressDialog progressDialog;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        name = findViewById(R.id.name);
        progressDialog = new ProgressDialog(this);
        email = findViewById(R.id.email);
        refresh = findViewById(R.id.refresh);
        profile_image = findViewById(R.id.profile_image);
        sign_out = findViewById(R.id.signOut);
        transaction_image = findViewById(R.id.transaction);
        qrCode = findViewById(R.id.qrCode);
        user_settings = findViewById(R.id.user_settings);
        timer = findViewById(R.id.timer);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        auth = new Auth();
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();
                    init();
                }

            }
        });
        init();


    }

    private void init() {
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {

                progressDialog.cancel();
                loadBitmap("https://www.placecage.com/200/300", profile_image);
                name.setText(auth.user.getName());
                email.setText(auth.user.getUser_id());
                sign_out.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);
                sign_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        auth.signOut();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

                qrCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), QRcodeActivity.class);
                        startActivity(i);
                    }
                });

                timer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.i("timer", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        if (auth.user.getCurrent_time() <= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                                && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < auth.user.getCurrent_time() + auth.user.getCurrent_duration()) {
                            Intent i = new Intent(getApplicationContext(), TimerActivity.class);
                            i.putExtra("duration", auth.user.getCurrent_duration());
                            i.putExtra("time", auth.user.getCurrent_time());
                            startActivity(i);
                            finish();
                        }

                    }
                });

                transaction_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("TAG", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + auth.user.getTransaction_history().get(0));
                        Intent i = new Intent(UserInfoActivity.this, TransactionActivity.class);
                        i.putExtra("transactions", (Serializable) auth.user.getTransaction_history());
                        startActivity(i);
                    }
                });
                user_settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(UserInfoActivity.this, SettingActivity.class);
                        startActivity(i);
                    }
                });

                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = true;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle("Utilities");
                            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                            isShow = false;
                        }
                    }
                });
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {
                progressDialog.cancel();
                sign_out.setVisibility(View.INVISIBLE);
                refresh.setVisibility(View.VISIBLE);
                email.setText("Oop! Something went wrong");
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = true;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle("No Internet!");
                            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                            isShow = false;
                        }
                    }
                });
            }
        });
    }

    public void loadBitmap(String resId, CircleImageView imageView) {

        final Bitmap bitmap;

        imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        ImageDownloader task = new ImageDownloader(imageView, this, "-1");
        task.execute(resId);

    }

}
