package com.example.rent_it.Interfaces;

import com.example.rent_it.Models.Area;

import org.json.JSONObject;

import java.util.List;

public interface OnAreaListFetchedInterface {
    void onAreaListFetchSuccess(List<Area> area_list);
    void onAreaListFetchFailure();
}
