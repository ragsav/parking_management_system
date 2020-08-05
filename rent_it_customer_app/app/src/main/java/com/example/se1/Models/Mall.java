package com.example.se1.Models;


import android.graphics.Bitmap;

import java.io.Serializable;

public class Mall implements Serializable {

    private boolean is_blocked;
    private String id;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    private float lat;
    private float lng;
    private String title;
    private String description;
    private String priority;
    private Bitmap imageBitmap;
    private String url;


    public Mall() {
        //empty constructor needed
    }

    public Mall(String description, String id, String priority, String title, boolean is_blocked, String url, float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.id = id;
        this.is_blocked = is_blocked;
        this.url = url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }


    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

}
