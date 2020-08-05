package com.example.se1.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.se1.Activities.MainActivity;
import com.example.se1.Activities.MallListActivity;
import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServce";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final Auth auth = new Auth();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: >>>>>>>>>>>>>>>>>>>>>>>>>>>" + remoteMessage.getData().get("data"));
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + remoteMessage.getNotification().getBody());
        }


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = getNotification(remoteMessage.getData().get("data"), this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(Integer.parseInt(NOTIFICATION_CHANNEL_ID), notification);

        //createNotification(remoteMessage.getNotification().getBody(),"hello");

    }


    private Notification getNotification(String content, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id);
        builder.setContentTitle("Rent it!");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }


    public void createNotification(String title, String message) {
        Log.i("inside create notification", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> title=" + title + "  body=" + message);
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(this, MallListActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }


    @Override
    public void onNewToken(@NonNull final String s) {
        super.onNewToken(s);


        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                firebaseFirestore.collection("User").document(auth.user.getUser_id())
                                        .update("mb_id", token);
                                Log.i("firebase service ", ">>>>>>>>>>>>>>>>>>>>>>>>>" + token);

                            }
                        });

                firebaseFirestore.collection("User").document(auth.user.getUser_id())
                        .update("mb_id", s);
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {

            }
        });

    }
}
