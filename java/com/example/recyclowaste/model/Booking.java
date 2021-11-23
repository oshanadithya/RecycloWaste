package com.example.recyclowaste.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Booking implements Serializable {
    private String driver;
    private String type;
    private UserLocation location;
    private String date;
    private String time;
    private String includes;
    private double payment;

    public Booking(String driver, String type, UserLocation location, String date, String time, String includes, double payment) {
        this.driver = driver;
        this.type = type;
        this.location = location;
        this.date = date;
        this.time = time;
        this.includes = includes;
        this.payment = payment;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }
}
