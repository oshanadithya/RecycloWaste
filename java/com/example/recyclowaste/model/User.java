package com.example.recyclowaste.model;

import java.io.Serializable;

public class User implements Serializable {
    private String fname;
    private String username;
    private String email;
    private String telno;
    private String password;



    public User(String fname, String username, String email, String telno,String password) {
        this.fname = fname;
        this.username = username;
        this.email = email;
        this.telno = telno;
        this.password = password;
    }

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }
}
