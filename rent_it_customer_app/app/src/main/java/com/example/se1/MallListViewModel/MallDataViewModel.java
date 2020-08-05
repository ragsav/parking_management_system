package com.example.se1.MallListViewModel;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.se1.DataCacheUtils.MallImagesCacheManager;
import com.example.se1.MallListRecyclerView.MallListRecyclerViewAdapter;
import com.example.se1.Models.Mall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MallDataViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Mall>> liveData;
    private ArrayList<Mall> mallDataArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private MallImagesCacheManager mallImagesCacheManager;
    private Application application = new Application();

    public MallDataViewModel(final ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.mallImagesCacheManager = new MallImagesCacheManager();
        liveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        getMallData();
    }

    private void getMallData() {
        Log.i("ViewModel", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> inside getMallData");
        db.collection("Notebook").orderBy("priority").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    final List<Mall> list = queryDocumentSnapshots.toObjects(Mall.class);
                    mallDataArrayList.clear();
                    mallDataArrayList.addAll(list);

                    liveData.setValue(mallDataArrayList);
//                    db.collection("trigger").document("mall_data_changed").get(Source.SERVER)
//                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        int changed = task.getResult().toObject(int.class);
//                                        if(mallImagesCacheManager.getMallImagesList().size()==changed){
//
//                                            Log.i("ViewModel",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> getting data from cache");
//                                            List<Bitmap> imageList = mallImagesCacheManager.getMallImagesList();
//                                            for(int i=0;i<mallDataArrayList.size();i++){
//                                                mallDataArrayList.get(i).setImageBitmap(imageList.get(i));
//                                            }
//                                            liveData.setValue(mallDataArrayList);
//                                            progressBar.setVisibility(View.GONE);
//                                        }
//                                        else
//                                            {
//                                            Log.i("ViewModel",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> getting data from server");
//                                            for(int i=0;i<mallDataArrayList.size();i++){
//                                                new ImageDownloader(i,mallDataArrayList.size())
//                                                        .execute(mallDataArrayList.get(i).getUrl());
//                                            }
//                                        }
//                                    }
//                                }
//                            });


                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }


    public MutableLiveData<ArrayList<Mall>> getMallMutableLiveData() {
        return liveData;
    }


    private class ImageDownloader extends AsyncTask<String, String, Bitmap> {

        int index;
        int size;

        public ImageDownloader(int i, int size) {
            this.index = i;
            this.size = size;
        }

        @Override
        protected Bitmap doInBackground(String[] strings) {
            String url = strings[0];
            Bitmap b = getBitmapFromURL(url);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            if (b != null) {
                mallDataArrayList.get(index).setImageBitmap(b);
                mallImagesCacheManager.clearImageCache();
                mallImagesCacheManager.putImage(application.getApplicationContext(), mallDataArrayList.get(index).getId(), b);
            }
            if (index + 1 == size) {
                liveData.setValue(mallDataArrayList);
            }
        }

        private Bitmap getBitmapFromURL(String src) {
            try {

                InputStream is = new java.net.URL(src).openStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(is);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ImageDownloader", ">>>>>>>>>>>>>>>>>>>>>>>>>> failed for url = " + src);
                return null;
            }
        }

    }

}
