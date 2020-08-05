package com.example.admin_rent_it.Models;

import java.util.List;

public class Plot {
    private String plotNo;
    private boolean is_reserved;


    private String id;


    private List<String> occupied;

    public List<String> getOccupied_user() {
        return occupied_user;
    }

    private List<String> occupied_user;


    private int priority;

    public Plot() {
        //empty constructor needed
    }

    public Plot(List<String> occupied_user, List<String> occupied, String plotNo, int priority, String id, boolean is_reserved) {
        this.occupied=occupied;
        this.plotNo = plotNo;
        this.id=id;
        this.occupied_user = occupied_user;
        this.is_reserved = is_reserved;
        this.priority = priority;
    }

    public String getPlotNo() {
        return plotNo;
    }
    public List<String> isOccupied() {
        return occupied;
    }
    public String getId() {
        return id;
    }
    public int getPriority() {
        return priority;
    }
    public boolean isIs_reserved(){return  is_reserved;}
}
