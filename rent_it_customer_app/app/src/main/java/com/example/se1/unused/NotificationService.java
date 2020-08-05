//package com.example.se1.unused;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.MainThread;
//import androidx.annotation.Nullable;
//import androidx.annotation.UiThread;
//import androidx.core.app.NotificationCompat;
//import com.example.se1.Activities.MallListActivity;
//import com.example.se1.Auth.Auth;
//import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
//import com.example.se1.Interfaces.AuthOnCompleteUpdateInterface;
//import com.example.se1.Models.User;
//import com.example.se1.PlotListGetterNetworkCalls.Update_plot;
//import com.example.se1.PlotListGetterNetworkCalls.Update_user;
//import com.example.se1.R;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Calendar;
//import java.util.Date;
//
//public class NotificationService extends Service {
//    long sec_in_minute = 1000;
//    public static final String CHANNEL_ID = "ForegroundServiceChannel";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    public void StopService(){
//        stopSelf();
//    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        final Auth auth =new Auth();
//        Log.i("sevice ",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+Thread.currentThread().getId());
//        //int input = intent.getIntExtra("time",0);
//        int duration = intent.getIntExtra("duration",0);
//        duration*=60;
//        HandlerThread mHandlerThread = new HandlerThread("LocalServiceThread");
//        mHandlerThread.start();
//
//        final Handler mHandler = new Handler(mHandlerThread.getLooper());
//        createNotificationChannel();
//        final Context c = this.getBaseContext();
//        Intent notificationIntent = new Intent(c, MallListActivity.class);
//        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//
//        final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Rent it!")
//                .setContentText("Your slot starts in :")
//                .setOnlyAlertOnce(true)
//                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1, notification);
//
//        Calendar calendar = Calendar.getInstance();
//        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY) + 1;
//        int minutesOfDay = calendar.get(Calendar.MINUTE);
////        time[0]-=hourOfDay;
////        time[0]*=60;
////        time[0]+=60-minutesOfDay;
//        final int[] finalDuration = {duration};
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("simple thread",">>>>>>>>>>>>>>>>>>>>>"+Thread.currentThread().getId());
//                while (finalDuration[0] > 0){
//
//
//                        Notification notification1 = new NotificationCompat.Builder(c, CHANNEL_ID)
//                                .setContentTitle("Rent it!")
//                                .setContentText("Your slot time remaining is :"+ --finalDuration[0])
//                                .setOnlyAlertOnce(true)
//                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                                .setContentIntent(pendingIntent)
//                                .build();
//                        startForeground(1,notification1);
//
//                    try {
//
//                        Thread.sleep(sec_in_minute);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                auth.getUser(new AuthOnCompleteRetreiveInterface() {
//                    @Override
//                    public void onFireBaseUserRetrieveSuccess() {
//                        new Update_user(auth);
//                        stopSelf();
//
//                    }
//                    @Override
//                    public void onFireBaseUserRetrieveFailure() {
//
//                    }
//                });
//
//
//            }
//        }).start();
//
//
//
//        return START_STICKY;    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Foreground Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//        }
//    }
//
//}