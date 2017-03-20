package com.example.ericrpurvis.xplorer;

/**
 * Created by patrickmuller on 3/20/17.
 */

public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng(){ }

    public LatLng(Double lat, Double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }
}
