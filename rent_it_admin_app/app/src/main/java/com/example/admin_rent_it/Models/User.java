package com.example.admin_rent_it.Models;

import java.util.List;

public class User {
        public User() {
    }



    private int current_end_time;
    private String name;
    private String user_id;
    private String current_mall_id;
    private String current_plot_id;
    private int current_duration;
    private int current_time;
    private boolean has_taken;
    private int extended_time;
    private boolean entry;
    private List<String> transaction_history;

    public String getUser_id() {
        return user_id;
    }

    public String getCurrent_mall_id() {
        return current_mall_id;
    }

    public String getCurrent_plot_id() {
        return current_plot_id;
    }

    public int getCurrent_duration() {
        return current_duration;
    }

    public int getCurrent_time() {
        return current_time;
    }

    public boolean isHas_taken() {
        return has_taken;
    }

    public List<String> getTransaction_history() {
        return transaction_history;
    }

    public int getExtended_time() {
        return extended_time;
    }

    public void setExtended_time(int extended_time) {
        this.extended_time = extended_time;
    }

    public int getCurrent_end_time() {
        return current_end_time;
    }
    public String getName() {
        return name;
    }


    public void setCurrent_end_time(int current_end_time) {
        this.current_end_time = current_end_time;
    }

    public User(String user_id,boolean entry,int current_end_time, String current_mall_id,
                String current_plot_id, int current_duration,
                int current_time, boolean has_taken, String name,
                List<String> transaction_history,int extended_time) {
        this.user_id = user_id;
        this.name = name;
        this.entry = entry;
        this.extended_time =extended_time;
        this.current_end_time = current_end_time;
        this.current_mall_id = current_mall_id;
        this.current_plot_id = current_plot_id;
        this.current_duration = current_duration;
        this.current_time = current_time;
        this.has_taken = has_taken;
        this.transaction_history = transaction_history;
    }


    public boolean isEntry() {
        return entry;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }
}
