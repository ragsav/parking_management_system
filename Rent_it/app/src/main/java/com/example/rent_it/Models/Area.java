package com.example.rent_it.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Area {
    String area_id;
    String name;
    JSONArray lot_list;
    String city_id;
    String image_binary;
    String image_url;
    Integer distance;

    public Area(String area_id,String name, JSONArray lot_list, String city_id,String image_binary,String image_url,Integer distance) {
        this.name = name;
        this.area_id = area_id;
        this.lot_list = lot_list;
        this.city_id = city_id;
        this.image_binary = image_binary;
        this.image_url = image_url;
        this.distance = distance;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getName() {
        return name;
    }

    public JSONArray getLot_list() {
        return lot_list;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getImage_binary() {
        return image_binary;
    }

    public String getImage_url() {
        return image_url;
    }

    public Integer getDistance() {
        return distance;
    }
}
