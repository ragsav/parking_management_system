package com.example.admin_rent_it.Models;

import android.media.Image;

public class Mall {

    public boolean isIs_blocked() {
        return is_blocked;
    }

    private boolean is_blocked;
    private String id;
    private String title;
    private String description;
    private String priority;


    private Image mall_image;
    public String getImage_url() {
        return mall_image_url;
    }

    private String mall_image_url;
//    private String opening_time;
//    private String closing_time;

    public Mall() {
        //empty constructor needed
    }

    public Mall(String description, String id, String priority, String title,boolean is_blocked,String mall_image_url) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.id = id;
        this.is_blocked= is_blocked;
        this.mall_image_url = mall_image_url;
//        this.opening_time=opening_time;
//        this.closing_time = closing_time;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public Image getMall_image() {
        return mall_image;
    }

    public void setMall_image(Image mall_image) {
        this.mall_image = mall_image;
    }

//    public String getOpening_time() { return opening_time; }
//
//    public String getClosing_time() { return closing_time; }
}
