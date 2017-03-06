package com.example.ericrpurvis.xplorer;

import java.util.Date;

/**
 * Created by patrickmuller on 2/27/17.
 */

public class Event {
    public String eventName;
    public String description;
    public Date eventDate;
    public LocationPost eventLocation;

    public Event() {

    }

    public Event(String eventName, String description, Date eventDate, LocationPost eventLocation){
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
    }
}

