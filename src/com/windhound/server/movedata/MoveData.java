package com.windhound.server.movedata;

import java.util.ArrayList;

public class MoveData
{
    private final GPSData               gpsData;
    private final ArrayList<SensorData> sensorDatas;

    public MoveData(GPSData a_gpsData,  ArrayList<SensorData> a_sensorDatas)
    {
        gpsData     = a_gpsData;
        sensorDatas = a_sensorDatas;
    }
}
