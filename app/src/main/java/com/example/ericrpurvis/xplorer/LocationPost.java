package com.example.ericrpurvis.xplorer;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ericrpurvis on 2/21/17.
 */

public class LocationPost {

    public String name;
    public String desc;
    public String actType;
    public LatLng coord;

    public LocationPost(){

    }

    public LocationPost(String name, String desc, String actType, LatLng coord){
        this.name = name;
        this.desc = desc;
        this.actType = actType;
        this.coord = coord;
    }
}
