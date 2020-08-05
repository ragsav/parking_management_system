package com.example.se1.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.MallListRecyclerView.MallListRecyclerViewAdapter;
import com.example.se1.MallListViewModel.MallDataViewModel;
import com.example.se1.MallListViewModel.MallViewModelFactory;
import com.example.se1.Models.Mall;
import com.example.se1.NetworkCalls.ImageDownloader;
import com.example.se1.R;
import com.example.se1.utils.CircleImageView;
import com.example.se1.utils.SorryDialogBox;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.okhttp.internal.DiskLruCache;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static android.os.Environment.isExternalStorageRemovable;

public class MallListActivity extends AppCompatActivity {


    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    private RecyclerView recyclerView;
    private MallDataViewModel mallDataViewModel;
    ProgressBar progressBar;
    private MallListRecyclerViewAdapter mallListRecyclerViewAdapter;
    private Window window;
    private TextView name_text_view;
    private CircleImageView profile_image;
    private Button user_info_button;
    LruCache<String, Bitmap> memoryCache;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        Log.i("mall list", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + Thread.currentThread().getId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_list);
        name_text_view = findViewById(R.id.textView2);
        profile_image = findViewById(R.id.profile_image);
        user_info_button = findViewById(R.id.user_info);


        auth = new Auth();
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        user_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(i);
            }
        });
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                FirebaseFirestore.getInstance().collection("User").document(auth.user.getUser_id())
                                        .update("mb_id", token);
                                Log.i("firebase service ", ">>>>>>>>>>>>>>>>>>>>>>>>>" + token);

                            }
                        });
                loadBitmap("https://www.placecage.com/200/300", profile_image);
                name_text_view.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                name_text_view.setText(auth.user.getName());
                Log.i("text view", ">>>>>>>>>>>>>>>>>>>>>>>>" + auth.user.getName());
                Toast.makeText(getApplicationContext(), auth.user.getName(), Toast.LENGTH_LONG);
                setUpMallRecyclerView(auth.user.isHas_taken(), auth);
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {
                setUpMallRecyclerView(true, null);
                new SorryDialogBox(getApplicationContext(), "Something went Wrong in getting user");
            }
        });

    }


    private void setUpMallRecyclerView(final boolean b, final Auth auth1) {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mallDataViewModel = ViewModelProviders.of(this, new MallViewModelFactory(progressBar)).get(MallDataViewModel.class);

        mallDataViewModel.getMallMutableLiveData().observe(this, new Observer<ArrayList<Mall>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Mall> mallData) {
                mallListRecyclerViewAdapter = new MallListRecyclerViewAdapter(
                        mallData, MallListActivity.this);
                recyclerView.setAdapter(mallListRecyclerViewAdapter);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    public void loadBitmap(String resId, CircleImageView imageView) {

        final Bitmap bitmap;

        imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        ImageDownloader task = new ImageDownloader(imageView, this, "-1");
        task.execute(resId);

    }
}
