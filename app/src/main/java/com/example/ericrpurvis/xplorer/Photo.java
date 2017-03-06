package com.example.ericrpurvis.xplorer;

/**
 * Created by patrickmuller on 2/27/17.
 */

public class Photo {
    public String fileLocation;
    public LocationPost place;

    public Photo(){

    }

    public Photo(String fileLocation, LocationPost place){
        this.fileLocation = fileLocation;
        this.place = place;
    }
}



