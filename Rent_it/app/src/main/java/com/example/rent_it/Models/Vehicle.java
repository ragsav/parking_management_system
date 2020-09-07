package com.example.rent_it.Models;

public class Vehicle {
    public String getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    String image,number,vehicle_id;

    public Vehicle(String image, String number, String vehicle_id) {
        this.image = image;
        this.number = number;
        this.vehicle_id = vehicle_id;
    }
}
