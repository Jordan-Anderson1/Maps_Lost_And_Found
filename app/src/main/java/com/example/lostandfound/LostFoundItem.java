package com.example.lostandfound;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.UUID;

public class LostFoundItem implements Serializable {


    private String id;

    private String name;
    private String phone;
    private String description;
    private String date;

    private String postType;

    private LatLng latLng;

    public LostFoundItem(String name, String phone, String description, String date, String postType, LatLng latLng){
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.postType = postType;
        this.id = UUID.randomUUID().toString();
        this.latLng = latLng;
    }

    public LostFoundItem(String name, String phone, String description, String date, String postType, String UUID, LatLng latLng){
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.postType = postType;
        this.id = UUID;
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPostType() {
        return postType;
    }

    public LatLng getLatLng() {return latLng;}

}
