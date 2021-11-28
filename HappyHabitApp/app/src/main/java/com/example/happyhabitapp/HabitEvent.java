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
    private String encodedImage;
    private transient LatLng location;
    private String description;
    private String title;
    private int status;

    /* constructors */

    /* for fire base */
    public HabitEvent() {
    }

    /**
     * Even though we have multiple optional parameters, Since they will have the same type, ie String,
     *  overloading does not work for us. We decided to create only one constructor, and pass the correct
     *  values in when creating the HabitEvent class
     * @param event_date
     * @param title
     * @param statusCode
     * @param description
     * @param encodedImage
     * @param location
     */
    public HabitEvent(Calendar event_date, String title, int statusCode, String description, String encodedImage, LatLng location) { // image and location
        if(encodedImage == null) {
            encodedImage = "";
        }

        this.event_date = event_date;
        this.title = title;
        this.encodedImage = encodedImage;
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

    public void setPic_path(String encodedImage) {
        this.encodedImage = encodedImage;
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

    public String getImage() {
        return encodedImage;
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
