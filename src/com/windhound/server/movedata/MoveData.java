package com.windhound.server.movedata;

import java.util.ArrayList;

public class MoveData
{
    private final GPSData               gpsData;
    private final ArrayList<SensorData> sensorDatas;
    //TODO: need a list of sensordata objects?
    //Currently only the first item in the list is handled

    public MoveData(GPSData a_gpsData,  ArrayList<SensorData> a_sensorDatas)
    {
        gpsData     = a_gpsData;
        sensorDatas = a_sensorDatas;
    }

    public GPSData getGpsData() {
        return gpsData;
    }

    public ArrayList<SensorData> getSensorDatas() {
        return sensorDatas;
    }
}
