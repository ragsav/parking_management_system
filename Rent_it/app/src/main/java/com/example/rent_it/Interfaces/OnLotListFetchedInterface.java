package com.example.rent_it.Interfaces;

import com.example.rent_it.Models.Area;
import com.example.rent_it.Models.Lot;

import java.util.List;

public interface OnLotListFetchedInterface {
    void onLotListFetchSuccess(List<Lot> lot_list);
    void onLotListFetchFailure();
}
