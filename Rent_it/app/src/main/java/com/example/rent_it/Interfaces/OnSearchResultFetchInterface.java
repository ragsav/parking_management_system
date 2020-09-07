package com.example.rent_it.Interfaces;

import com.example.rent_it.Models.Lot;

import java.util.List;

public interface OnSearchResultFetchInterface {
    void onSearchResultFetchSuccess(List<Lot> lot_list_search_result);
    void onSearchResultFetchFailure();
}
