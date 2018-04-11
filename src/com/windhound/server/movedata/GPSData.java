package com.windhound.server.movedata;

public class GPSData
{
    private Float latitude;
    private Float longitude;

    public GPSData()
    {

    }

    public GPSData(Float a_latitude, Float a_longitude)
    {
        latitude  = a_latitude;
        longitude = a_longitude;
    }

    public Float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Float latitude)
    {
        this.latitude = latitude;
    }

    public Float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Float longitude)
    {
        this.longitude = longitude;
    }
}
