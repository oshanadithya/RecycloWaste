package com.example.recyclowaste.model;

public class Review {
    private static int tip;

    public Review(String s, int tip) {
        this.tip = tip;
    }

    public static int getTip() { return tip;}

    public void setTip(int tip) { this.tip = tip;}
}
