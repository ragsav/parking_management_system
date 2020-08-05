package com.example.se1.PlotProcessor;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.se1.Auth.Auth;
import com.example.se1.Models.Plot;
import com.example.se1.NetworkCalls.Update_user;
import com.example.se1.utils.SorryDialogBox;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlotSelectorAsyncTask extends AsyncTask<Integer, String, Pair<String, List<String>>> {

    private int time;
    private int duration;
    private AlertDialog alertDialog;
    private String final_id;
    private String mall_id;
    private Context context;
    private List<String> modified_occupied_array = new ArrayList<>();
    private List<String> modified_occupied_user_array = new ArrayList<>();
    private List<Plot> plotList;
    private Auth auth;
    private int flag = 0;


    final String TAG1 = "PlotSelectorAsyncTask";
    final String TAG2 = "PlotSelectorAsyncTask - Do in Background";
    final String TAG3 = "PlotSelectorAsyncTask - OnPostExecution";
    final String TAG4 = "PlotSelectorAsyncTask - Do in Background - process_list";

    public PlotSelectorAsyncTask(int time, List<Plot> plotList, int duration, String mall_id, AlertDialog alertDialog, Context context, Auth auth) {
        this.time = time;
        this.alertDialog = alertDialog;
        this.plotList = plotList;
        this.duration = duration;
        this.mall_id = mall_id;
        this.context = context;
        this.auth = auth;

    }

    @Override
    protected Pair<String, List<String>> doInBackground(Integer... integers) {
        process_list(plotList, time, duration);
        return new Pair<>(final_id, modified_occupied_array);
    }

    private void process_list(List<Plot> plotList, int time1, int duration) {

        int time = time1;
        int end_time = time1 + duration;
        int startTime, endTime;
        int previous_occurance_end_time = 9;
        Log.i("inside process list", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List<String> occupied_array = new ArrayList<>();
        List<String> occupied_user_array = new ArrayList<>();


        for (int i = 0; i < plotList.size() && flag == 0; i++) {
            occupied_array = plotList.get(i).getOccupied_time();
            occupied_user_array = plotList.get(i).getOccupied_user();
            if (occupied_array.size() == 0) {
                modified_occupied_array.add(time + "-" + end_time);
                modified_occupied_user_array.add(auth.user.getUser_id());
                final_id = plotList.get(i).getPlot_id();
                flag = 1;
                break;
            } else {
                for (int j = 0; j < occupied_array.size() && flag == 0; j++) {
                    String[] start = occupied_array.get(j).split("-", 2);
                    startTime = Integer.parseInt(start[0]);
                    endTime = Integer.parseInt(start[1]);
                    // parse starting and ending time of each occupied slot


                    if (j == 0) {
                        if (end_time <= startTime) {
                            flag = 1;
                            modified_occupied_array.add(time + "-" + end_time);
                            modified_occupied_user_array.add(auth.user.getUser_id());
                            for (int w = 0; w < occupied_array.size(); w++) {
                                modified_occupied_array.add(occupied_array.get(w));
                                modified_occupied_user_array.add(occupied_user_array.get(w));
                            }
                            final_id = plotList.get(i).getPlot_id();

                            break;
                        } else {
                            previous_occurance_end_time = endTime;
                        }
                    }
                    // if we find the slot in the starting only


                    else if (j == occupied_array.size() - 1 && endTime <= time && end_time <= 24) {
                        for (int w = 0; w < occupied_array.size(); w++) {
                            modified_occupied_array.add(occupied_array.get(w));
                            modified_occupied_user_array.add(occupied_user_array.get(w));
                        }
                        modified_occupied_array.add(time + "-" + end_time);
                        modified_occupied_user_array.add(auth.user.getUser_id());
                        flag = 1;
                        final_id = plotList.get(i).getPlot_id();

                        break;
                    }
                    // if we find the slot in the ending before the
                    // ending time of the parking slot


                    else {
                        if (previous_occurance_end_time <= time && end_time <= startTime) {
                            flag = 1;
                            for (int w = 0; w < j; w++) {
                                modified_occupied_array.add(occupied_array.get(w));
                                modified_occupied_user_array.add(occupied_user_array.get(w));
                            }
                            modified_occupied_array.add(time + "-" + end_time);
                            modified_occupied_user_array.add(auth.user.getUser_id());
                            for (int w = j + 1; w < occupied_array.size() + 1; w++) {
                                modified_occupied_array.add(occupied_array.get(w - 1));
                                modified_occupied_user_array.add(occupied_user_array.get(w - 1));
                            }

                            final_id = plotList.get(i).getPlot_id();

                            break;
                        } else {
                            previous_occurance_end_time = endTime;
                        }
                    }
                    // if we find find slot in between somewhere
                }

            }

        }


    }

    @Override
    protected void onPostExecute(Pair<String, List<String>> stringListPair) {
        super.onPostExecute(stringListPair);

        if (flag == 1) {
            new Update_user(mall_id, final_id, duration, time, true, alertDialog, modified_occupied_array
                    , modified_occupied_user_array, context, auth);
            flag = 0;
//                auth.getUser(new AuthOnCompleteRetreiveInterface() {
//                    @Override
//                    public void onFireBaseUserRetrieveSuccess() {
//                        List<String> l = auth.user.getTransaction_history();
//                        l.add(mall_id + "," + final_id);
//                        User user_new = new User(auth.user.getUser_id(), mall_id, final_id, duration, time, true, l);
//                        auth.setUser(new AuthOnCompleteUpdateInterface() {
//                            @Override
//                            public void onFireBaseUserUpdateSuccess() {
//
//                                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
//                                new Update_plot(alertDialog, final_id, mall_id, modified_occupied_array, modified_occupied_user_array);
//                                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
//                                //startService(time);
//                                time = time- Calendar.getInstance().get(Calendar.HOUR);
//                                time *=60;
//                                time-=Calendar.getInstance().get(Calendar.MINUTE);
//                                time*=60;
//                                time = 10;
//                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                                        + (time * 1000), pendingIntent);
//                                Log.i("tag",">>>>>>>>>>>>>>>>>>>>>>alarm fired set at "+System.currentTimeMillis()
//                                        + (time * 1000));
//                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
//                                Notification notification = getNotification("You have successfully registered a plot",context);
//                                if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//                                    int importance = NotificationManager. IMPORTANCE_HIGH ;
//                                    NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
//                                    assert notificationManager != null;
//                                    notificationManager.createNotificationChannel(notificationChannel) ;
//                                }
//                                notificationManager.notify(Integer.parseInt(NOTIFICATION_CHANNEL_ID), notification) ;
//
//                            }
//
//                            @Override
//                            public void onFireBaseUserUpdateFailure() {
//
//                                firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
//                                alertDialog.dismiss();
//                                new SorryDialogBox(context,"Something went Wrong");
//                                return;
//                            }
//                        }, user_new);
//
//                    }
//
//                    @Override
//                    public void onFireBaseUserRetrieveFailure() {
//                        alertDialog.dismiss();
//                        firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
//                        new SorryDialogBox(context,"Something went Wrong");
//                        return;
//                    }
//                });
        } else {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Notebook").document(mall_id).update("is_blocked", false);
            Toast.makeText(context, "No slot for selected time available", Toast.LENGTH_LONG);
            alertDialog.dismiss();
            new SorryDialogBox(context, "No slot for selected time available");
            return;
        }


    }


}
