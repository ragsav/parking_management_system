package com.example.se1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class QRcodeActivity extends AppCompatActivity {

    ImageView qr_code;
    TextView simple_text, refresh;
    Auth auth;
    Window window;
    ProgressDialog progressDialog;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new Auth();
        setContentView(R.layout.activity_qrcode);
        qr_code = findViewById(R.id.qrCodeImageView);
        simple_text = findViewById(R.id.simple_text);
        progressDialog = new ProgressDialog(this);
        refresh = findViewById(R.id.refresh);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog.setMessage("Fetching QR-code...");
        progressDialog.show();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    progressDialog.setMessage("Fetching QR-code...");
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
                try {

                    final QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix bitMatrix = writer.encode(auth.user.getUser_id(), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    refresh.setVisibility(View.GONE);
                    qr_code.setImageBitmap(bmp);

                } catch (WriterException e) {
                    refresh.setVisibility(View.VISIBLE);
                    simple_text.setText("Oop! something went wrong");
                    e.printStackTrace();
                } finally {
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {

                progressDialog.cancel();
                refresh.setVisibility(View.VISIBLE);
                simple_text.setText("Oop! something went wrong");
            }
        });
    }
}
