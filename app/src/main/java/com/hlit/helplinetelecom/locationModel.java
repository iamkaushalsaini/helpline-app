package com.hlit.helplinetelecom;

public class locationModel {

    private String id;
    private String user_id;
    private String date;
    private String time;
    private String month;
    private String month_year;
    private String year;
    private String time_stamp;
    private String location;
    private String latitude;
    private String longitude;
    private String day;

    public locationModel(String id, String user_id, String date, String time, String month, String month_year, String year, String time_stamp, String location, String latitude, String longitude, String day) {
        this.id = id;
        this.user_id = user_id;
        this.date = date;
        this.time = time;
        this.month = month;
        this.month_year = month_year;
        this.year = year;
        this.time_stamp = time_stamp;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
