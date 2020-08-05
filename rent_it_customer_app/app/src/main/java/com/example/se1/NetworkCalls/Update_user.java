package com.example.se1.NetworkCalls;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteUpdateInterface;
import com.example.se1.Models.User;
import com.example.se1.R;

import com.example.se1.utils.SorryDialogBox;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class Update_user {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private String mall_id;
    private String plot_id;
    private Auth auth;
    private boolean has_taken;
    private int time;
    private int duration;
    private FirebaseFirestore firebaseFirestore;
    private AlertDialog alertDialog;
    private List<String> modified_occupied_array;
    private List<String> modified_occupied_user_array;
    private Context context;

    public Update_user(String mall_id
            , String plot_id
            , int duration
            , int time
            , boolean has_taken
            , AlertDialog alertDialog
            , List<String> modified_occupied_array
            , List<String> modified_occupied_user_array
            , Context context
            , Auth auth
    ) {
        this.mall_id = mall_id;
        this.plot_id = plot_id;
        this.has_taken = has_taken;
        this.time = time;
        this.duration = duration;
        this.auth = auth;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.alertDialog = alertDialog;
        this.modified_occupied_array = modified_occupied_array;
        this.modified_occupied_user_array = modified_occupied_user_array;
        this.context = context;
        init_book();

    }

//    public Update_user(Auth auth) {
//
//        User user_new = new User(auth.user.getUser_id(),"","",0,0,false,auth.user.getName()
//                ,auth.user.getTransaction_history());
//        final int current_user_time = auth.user.getCurrent_time();
//        String mall_plot_id = auth.user.getTransaction_history().get(auth.user.getTransaction_history().size()-1);
//        final String mall_id = mall_plot_id.split(",",2)[0];
//        final String plot_id = mall_plot_id.split(",",2)[1];
//        final int time  = auth.user.getCurrent_time();
//        final int duration = auth.user.getCurrent_duration();
//        auth.setUser(new AuthOnCompleteUpdateInterface() {
//            @Override
//            public void onFireBaseUserUpdateSuccess() {
//                new Update_plot(mall_id,plot_id,current_user_time,duration+time);
//            }
//
//            @Override
//            public void onFireBaseUserUpdateFailure() {
//
//            }
//        },user_new);
//    }


    private void init_book() {

        List<String> l = auth.user.getTransaction_history();
        l.add(mall_id + "," + plot_id);
        User user_new = new User(auth.user.getUser_id(), time + duration, mall_id, plot_id, duration, time, has_taken, auth.user.getName(), l);

        auth.setUser(new AuthOnCompleteUpdateInterface() {
            @Override
            public void onFireBaseUserUpdateSuccess() {

                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
                new Update_plot(alertDialog, plot_id, mall_id, modified_occupied_array, modified_occupied_user_array);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = getNotification("You have successfully registered a plot", context);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                notificationManager.notify(Integer.parseInt(NOTIFICATION_CHANNEL_ID), notification);

            }

            @Override
            public void onFireBaseUserUpdateFailure() {

                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
                alertDialog.dismiss();
                new SorryDialogBox(context, "Something went Wrong");
                return;
            }
        }, user_new);
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

}
