//package com.example.se1.unused;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Vibrator;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//
//import com.example.se1.Activities.TimerActivity;
//import com.example.se1.Auth.Auth;
//import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
//import com.example.se1.R;
//import com.example.se1.utils.SorryDialogBox;
//
//import java.util.Calendar;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    public static String NOTIFICATION_ID = "notification-id" ;
//    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
//    private final static String default_notification_channel_id = "default" ;
//    private Auth auth = new Auth();
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
//        Notification notification = getNotification("Your plot time is started",context);
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//            int importance = NotificationManager. IMPORTANCE_HIGH ;
//            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(notificationChannel) ;
//        }
//        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
//        assert notificationManager != null;
//        notificationManager.notify(id , notification) ;
//        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        final Intent timerStartsIntent = new Intent(context, TimerActivity.class);
//
//        final Calendar calender = Calendar.getInstance();
//        auth.getUser(new AuthOnCompleteRetreiveInterface() {
//            @Override
//            public void onFireBaseUserRetrieveSuccess() {
//                startService(context,auth.user.getCurrent_duration());
////                timerStartsIntent.putExtra("user_time",auth.user.getCurrent_time());
//                timerStartsIntent.putExtra("duration",auth.user.getCurrent_duration());
//                timerStartsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(timerStartsIntent);
//
//                vibrator.vibrate(1000);
//            }
//
//            @Override
//            public void onFireBaseUserRetrieveFailure() {
//                new SorryDialogBox(context,"Unable to Fetch data !");
//            }
//        });
//
//    }
//    private Notification getNotification (String content,Context context) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder( context, default_notification_channel_id ) ;
//        builder.setContentTitle( "Rent it!" ) ;
//        builder.setContentText(content) ;
//        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
//        builder.setAutoCancel( true ) ;
//        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//        return builder.build() ;
//    }
//    public void startService(Context context , int duration) {
//        Intent serviceIntent = new Intent(context, NotificationService.class);
//        //serviceIntent.putExtra("time",time);
//        serviceIntent.putExtra("duration",duration);
//        ContextCompat.startForegroundService(context, serviceIntent);
//    }
//}
