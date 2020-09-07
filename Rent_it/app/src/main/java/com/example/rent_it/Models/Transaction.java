package com.example.rent_it.Models;

public class Transaction {
    private Integer duration, time;
    private String transaction_id,createdAt,lot_id,plot_id;
    private boolean cancelled, completed;
    public Integer getDuration() {
        return duration;
    }

    public Integer getTime() {
        return time;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isCompleted() {
        return completed;
    }



    public Transaction() {
        duration = 0;
        time = 0;
        transaction_id = "";
        cancelled = false;
        completed = false;
    }

    public Transaction(Integer duration, Integer time, String transaction_id, boolean cancelled, boolean completed) {
        this.duration = duration;
        this.time = time;
        this.transaction_id = transaction_id;
        this.cancelled = cancelled;
        this.completed = completed;
    }
}
