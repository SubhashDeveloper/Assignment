package com.example.subhash.gmap;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("lat")
   private String lat;
    @SerializedName("lng")
   private  String lng;



    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat=lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
