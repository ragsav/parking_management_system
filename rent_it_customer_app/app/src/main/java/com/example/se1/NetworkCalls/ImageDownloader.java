package com.example.se1.NetworkCalls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageDownloader extends AsyncTask<String, String, Bitmap> {

    ImageView imageView;
    Context context;
    String mall_id;

    private void addBitmapToMemoryCache(String mall_id, Bitmap bitmap) {
        String filename = context.getCacheDir() + "/" + mall_id;
        File file = new File(filename);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public ImageDownloader(ImageView imageView, Context context, String mall_id) {
        this.imageView = imageView;
        this.context = context;
        this.mall_id = mall_id;
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
            imageView.setImageBitmap(b);
            if (mall_id != "-1")
                addBitmapToMemoryCache(mall_id, b);
        }

    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
