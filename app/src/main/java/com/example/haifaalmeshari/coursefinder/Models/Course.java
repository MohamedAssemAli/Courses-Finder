package com.example.haifaalmeshari.coursefinder.Models;

import java.io.Serializable;

public class Course implements Serializable {

    private String uid, title, desc, date, time, price, lat, lon;

    public Course() {
    }

    public Course(String title, String desc, String date, String time, String price, String lat, String lon) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.price = price;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
