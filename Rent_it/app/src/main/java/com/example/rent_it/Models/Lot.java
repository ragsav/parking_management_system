package com.example.rent_it.Models;

public class Lot {
    public String getName() {
        return name;
    }


    public String getCity_id() {
        return city_id;
    }


    public String getArea_id() {
        return area_id;
    }


    public String getLot_id() {
        return lot_id;
    }


    public Integer getOpen_time() {
        return open_time;
    }


    public Integer getClose_time() {
        return close_time;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getArea_name() {
        return area_name;
    }
    public boolean isStatus() {
        return status;
    }
    public void setClose_time(Integer close_time) {
        this.close_time = close_time;
    }

    String name,cover_image;
    String city_id,city_name;
    String area_id,area_name;
    String lot_id;
    Integer open_time,close_time;



    boolean status;

    public Lot(String name, String city_id, String area_id, String lot_id
            , Integer open_time, Integer close_time,String cover_image,String city_name,String area_name,boolean status) {
        this.name = name;
        this.city_id = city_id;
        this.area_id = area_id;
        this.lot_id = lot_id;
        this.open_time = open_time;
        this.close_time = close_time;
        this.cover_image=cover_image;
        this.city_name = city_name;
        this.area_name = area_name;
        this.status = status;
    }


}
