package com.example.se1.DataCacheUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.se1.Models.Mall;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MallImagesCacheManager {
    public Bitmap getImage(Context context, String mall_id) {
        String fileName = context.getCacheDir() + "/" + mall_id;
        File file = new File(fileName);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void putImage(Context context, String mall_id, Bitmap bitmap) {
        String fileName = context.getCacheDir() + "/" + mall_id;
        File file = new File(fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Bitmap> getMallImagesList() {
        List<Bitmap> bitmapList = new ArrayList<>();
//        for (int i=0;i<list.size();i++){
////            bitmapList.add(getImage(context,list.get(i).getId()));
//        }
        return bitmapList;
    }

    public void clearImageCache() {

    }
}
