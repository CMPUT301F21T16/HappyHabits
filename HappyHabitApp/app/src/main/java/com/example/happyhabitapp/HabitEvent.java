package com.example.happyhabitapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Calendar;



public class HabitEvent implements Serializable {


    //Define constants for status
    private int STATUS_INCOMPLETE = 0;
    private int STATUS_COMPLETE = 1;
    private int STATUS_IN_PROGRESS = 2;



    private Calendar event_date;
    private String pic_path;
    private LatLng location;
    private String description;
    private String title;
    private int status;

    /* for fire base */
    public HabitEvent() {
    }

    /* counstructors */
    public HabitEvent(Calendar event_date, String title, int statusCode, String description) { // no location or image
        this.event_date = event_date;
        this.title = title;
        this.status = statusCode;
        this.description = description;
        this.location = null;
        this.pic_path = "";
    }

    public HabitEvent(Calendar event_date, String title, int statusCode, String description, String pic_path) { // only image
        this.event_date = event_date;
        this.title = title;
        this.pic_path = pic_path;
        this.location = null;
        this.description = description;
        this.status = statusCode;
    }
    public HabitEvent(Calendar event_date, String title, int statusCode, String description, LatLng location) { // only location
        this.event_date = event_date;
        this.title = title;
        this.pic_path = "";
        this.location = location;
        this.description = description;
        this.status = statusCode;
    }
    public HabitEvent(Calendar event_date, String title, int statusCode, String description, String pic_path, LatLng location) { // image and location
        this.event_date = event_date;
        this.title = title;
        this.pic_path = pic_path;
        this.location = location;
        this.description = description;
        this.status = statusCode;
    }

    /* setters */


    public void setTitle(String title) {
        this.title = title;
    }

    public void setEvent_date(Calendar event_date) {
        this.event_date = event_date;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(int statusCode){
        this.status = statusCode;
    }

    /* getters */

    public Calendar getEvent_date() {
        return event_date;
    }

    public String getPic_path() {
        return pic_path;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

}
