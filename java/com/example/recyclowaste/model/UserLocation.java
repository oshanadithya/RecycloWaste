package com.example.recyclowaste.model;

import java.io.Serializable;

public class UserLocation implements Serializable {
    private String locality;
    private double latitude;
    private double longitude;

    public UserLocation(String locality, double latitude, double longitude) {
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
