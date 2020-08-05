package com.example.se1.MallListRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1.Activities.MapsActivity;
import com.example.se1.Auth.Auth;
import com.example.se1.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.se1.Models.Mall;
import com.example.se1.NetworkCalls.ImageDownloader;
import com.example.se1.R;
import com.example.se1.utils.DateTimeManager;
import com.example.se1.utils.SorryDialogBox;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MallListRecyclerViewAdapter extends RecyclerView.Adapter<MallListRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Mall> mallDataArrayList;
    private Context context;
    private Auth auth;


    public MallListRecyclerViewAdapter(ArrayList<Mall> mallDataArrayList, Context context) {
        this.mallDataArrayList = mallDataArrayList;
        this.context = context;
        this.auth = new Auth();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mall_item_2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d("TAG", "changed.....................................");
        viewHolder.textViewTitle.setText(mallDataArrayList.get(i).getTitle());
        viewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("lat", mallDataArrayList.get(i).getLat());
                intent.putExtra("lng", mallDataArrayList.get(i).getLng());
                context.startActivity(intent);
            }
        });
        loadBitmap(mallDataArrayList.get(i).getUrl(), viewHolder.imageView, mallDataArrayList.get(i).getId());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                     auth.getUser(new AuthOnCompleteRetreiveInterface() {
                                                         @Override
                                                         public void onFireBaseUserRetrieveSuccess() {
                                                             if (!auth.user.isHas_taken()) {
                                                                 new DateTimeManager(context, 9, 20, mallDataArrayList.get(i).getId(), auth);
                                                                 notifyDataSetChanged();
                                                             }
                                                         }

                                                         @Override
                                                         public void onFireBaseUserRetrieveFailure() {
                                                             new SorryDialogBox(context, "Something went Wrong! Please check your internet");
                                                         }
                                                     });

                                                 }
                                             }
        );
    }

    @Override
    public int getItemCount() {
        return mallDataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewTitle;
        ImageView imageView;
        ImageView location;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            textViewTitle = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.book);
        }
    }

    public void loadBitmap(String resId, ImageView imageView, String mall_id) {

        final Bitmap bitmap = getBitmapFromMemCache(mall_id);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
            ImageDownloader task = new ImageDownloader(imageView, context, mall_id);
            task.execute(resId);
        }
    }

    private Bitmap getBitmapFromMemCache(String mall_id) {
        String filename = context.getCacheDir() + "/" + mall_id;
        File file = new File(filename);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
