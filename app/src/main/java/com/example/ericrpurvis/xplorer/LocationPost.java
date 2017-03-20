package com.example.ericrpurvis.xplorer;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ericrpurvis on 2/21/17.
 */

public class LocationPost {

    public String name;
    public String desc;
    public String actType;
    public FirebaseUser user;
    public LatLng coord;

    public LocationPost(){

    }

    public LocationPost(String name, String desc, String actType,double lat,double lng, FirebaseUser user){
        this.name = name;
        this.desc = desc;
        this.actType = actType;
        this.user = user;
        this.coord = new LatLng(lat, lng);
    }
}
