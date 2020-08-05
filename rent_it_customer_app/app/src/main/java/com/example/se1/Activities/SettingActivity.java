package com.example.se1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.NetworkCalls.ImageDownloader;
import com.example.se1.R;
import com.example.se1.utils.CircleImageView;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingActivity extends AppCompatActivity {

    private Window window;
    private Auth auth;
    private Button submit;
    private EditText name;
    private EditText vehicle;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        auth = new Auth();
        circleImageView = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.button);
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {

                name.setText(auth.user.getName());
                loadBitmap("https://www.placecage.com/200/300", circleImageView);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("User").document(auth.user.getUser_id())
                                .update("name", name.getText().toString());
                        finish();
                    }
                });

            }

            @Override
            public void onFireBaseUserRetrieveFailure() {

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
