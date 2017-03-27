package com.example.ericrpurvis.xplorer;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by patrickmuller on 2/27/17.
 */

public class Event {
    public String eventName;
    public String description;
    public Date eventDateTime;
    public String locationId;
    //public LocationPost eventLocation;

    public Event() {

    }

    public Event(String eventName, String description, Date eventDateTime, String locationId){
        this.eventName = eventName;
        this.description = description;
        this.eventDateTime = eventDateTime;
        //this.eventLocation = eventLocation;
        this.locationId = locationId;
    }

    @Override
    public String toString(){
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        String dateStr = format.format(this.eventDateTime);
        return "Name: " + this.eventName + "\n" +
                "Description: " + this.description + "\n" +
                "Date: " + dateStr;
    }
}

