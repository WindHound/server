package com.windhound.server.race.course;

import com.windhound.server.movedata.GPSData;

public class Buoy
{
    GPSData location;

    public Buoy()
    {

    }

    public Buoy(GPSData a_location)
    {
        location = a_location;
    }

    public GPSData getLocation()
    {
        return location;
    }

    public void setLocation(GPSData location)
    {
        this.location = location;
    }
}
