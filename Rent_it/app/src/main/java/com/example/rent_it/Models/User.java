package com.example.rent_it.Models;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String user_id;
    private List<String> transaction_history;
    private List<String> schedule_dates;
    private List<String> vehicle_ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getTransaction_history() {
        return transaction_history;
    }

    public void setTransaction_history(List<String> transaction_history) {
        this.transaction_history = transaction_history;
    }

    public List<String> getSchedule_dates() {
        return schedule_dates;
    }

    public void setSchedule_dates(List<String> schedule_dates) {
        this.schedule_dates = schedule_dates;
    }

    public User() {
    }

    public User(String user_id, String name,String email) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;

    }

    public User(String user_id, String name, String email
            ,List<String> transaction_history,List<String> schedule_dates,List<String> vehicle_ids) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.transaction_history = transaction_history;
        this.schedule_dates = schedule_dates;
    }







//    public boolean add_transaction_item(String symbol) {
//        if (this.watch_list_symbols != null) {
//            if(this.watch_list_symbols.size()==20)return false;
//            if (!this.watch_list_symbols.contains(symbol)) {
//                watch_list_symbols.add(symbol);
//                return true;
//            }
//
//            return false;
//        }
//        return false;
//    }
//
//    public boolean delete_watch_list_item(String symbol) {
//        if (this.watch_list_symbols != null) {
//            if (this.watch_list_symbols.contains(symbol)) {
//                this.watch_list_symbols.remove(watch_list_symbols.indexOf(symbol));
//                return true;
//            }
//            return false;
//        }
//        return false;
//    }





}
