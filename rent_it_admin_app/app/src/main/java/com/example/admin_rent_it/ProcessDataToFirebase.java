package com.example.admin_rent_it;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.admin_rent_it.Models.Plot;
import com.example.admin_rent_it.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


import java.util.Calendar;
import java.util.List;

class ProcessDataToFirebase {
    private String user_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ProcessDataToFirebase(String user_id, TextView t1,TextView t2,TextView t3,boolean entry) {
        Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> inside constructor");
        this.user_id = user_id;
        if(entry==true){
            init_entry(t1,t2,t3);
        }else{
            init_exit(t1,t2,t3);
        }

    }

    private void init_exit(final TextView t1,final TextView t2,final TextView t3){
        db.collection("User").document(this.user_id).get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> task success");
                            User user = task.getResult().toObject(User.class);

                            int current_time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            if(user.getCurrent_end_time()<current_time&&user.isHas_taken()&&user.isEntry()) {


                                t1.setText(user.getName());
                                t2.setText(user.getCurrent_plot_id());
                                Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> user entry granted");


                                db.collection("User").document(user_id)
                                        .update("entry",false
                                        ,"current_time",0
                                        ,"current_end_time",0
                                        ,"current_mall_id",""
                                        ,"current_plot_id",""
                                        ,"current_duration",0
                                        ,"has_taken",false);

                            }

                            else {
                                t1.setText(user.getName());
                                t2.setText(user.getCurrent_plot_id());
                                t3.setText((current_time-user.getCurrent_end_time()+1)+" hr Extended");
                                db.collection("User").document(user_id)
                                        .update("entry",false
                                                ,"current_time",0
                                                ,"current_end_time",0
                                                ,"current_mall_id",""
                                                ,"current_plot_id",""
                                                ,"current_duration",0
                                                ,"has_taken",false
                                                ,"extended_time",current_time-user.getCurrent_end_time()+1);

                                Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> entry denied");
                            }
                        }
                    }
                });
    }
    private void init_entry(final TextView t1, final TextView t2,final TextView t3) {
        db.collection("User").document(this.user_id).get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> task completed");
                        if(task.isSuccessful()){
                            Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> task success");
                            User user = task.getResult().toObject(User.class);
                            t1.setText(user.getName());

                            int current_time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            if(user.getCurrent_time()==current_time&&user.isHas_taken()){

                                if(user.getExtended_time()!=0){
                                    t3.setText(user.getExtended_time()+" hr due");
                                }
                                Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> user entry granted");
                                t2.setText(user.getCurrent_plot_id());

                                db.collection("User").document(user_id)
                                        .update("entry",true);

                            }
                            else {
                                t2.setText("Invalid Entry");
                                Log.i("Process data",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> entry denied");
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public void Update_plot(final String mall_id, final String plot_id, final int time, final int duration)
    {

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(mall_id).document(plot_id).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task!=null){
                    List<String> l1 = task.getResult().toObject(Plot.class).isOccupied();
                    List<String> l2 = task.getResult().toObject(Plot.class).getOccupied_user();
                    String s = time+"-"+duration;
                    for(int i=0;i<l1.size();i++)
                    {
                        if(l1.get(i).equals(s)){
                        l1.remove(i);
                        l2.remove(i);
                        break;
                        }
                    }
                    Log.i("process data",":::::::::::::"+l1+"  "+l2+"  "+s);
                    firebaseFirestore.collection(mall_id).document(plot_id).update("occupied",l1);
                    firebaseFirestore.collection(mall_id).document(plot_id).update("occupied_user",l2);

                }
            }
        });
    }
}
