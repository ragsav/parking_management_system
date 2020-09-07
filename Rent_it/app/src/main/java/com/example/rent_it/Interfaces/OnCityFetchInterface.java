package com.example.rent_it.Interfaces;

import com.example.rent_it.Models.City;

import java.util.List;

public interface OnCityFetchInterface {
    void onCityFetchSuccess(List<City> city_list_data);
    void onCityFetchFailure();
}
