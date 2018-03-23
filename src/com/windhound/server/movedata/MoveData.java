package com.windhound.server.movedata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MoveData
{
    private Long     competitorID;
    private Long     boatID;
    private Long     raceID;
    private Calendar timestamp;

    private GPSData          gpsData;
    private List<SensorData> sensorDataPoints;

    public MoveData(Long             a_competitorID,
                    Long             a_boatID,
                    Long             a_raceID,
                    Calendar         a_time,
                    GPSData          a_gpsData,
                    List<SensorData> a_sensorDataPoints)
    {
        competitorID     = a_competitorID;
        boatID           = a_boatID;
        raceID           = a_raceID;
        timestamp        = a_time;
        gpsData          = a_gpsData;
        sensorDataPoints = a_sensorDataPoints;
    }

    public MoveData(MoveDataDTO dto)
    {
        int size = dto.getX().size();
        if (size != dto.getY().size() || size != dto.getZ().size() ||
            size != dto.getAngle().size() || size != dto.getdX().size() ||
            size != dto.getdY().size() || size != dto.getdZ().size())
            throw new IllegalArgumentException("MoveDataDTO lists don't have the same size");

        competitorID = dto.getCompetitorID();
        boatID       = dto.getBoatID();
        raceID       = dto.getRaceID();
        timestamp    = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        timestamp.setTimeInMillis(dto.getTimeMilli());

        gpsData = new GPSData(dto.getLatitude(), dto.getLongitude());

        sensorDataPoints = new ArrayList<>();

        for (int i = 0; i < size; ++i)
        {
            AccelerometerData accelerometerData = new AccelerometerData(
                    dto.getX().get(i),
                    dto.getY().get(i),
                    dto.getZ().get(i));
            CompassData compassData = new CompassData(
                    dto.getAngle().get(i));
            GyroscopeData gyroscopeData = new GyroscopeData(
                    dto.getdX().get(i),
                    dto.getdY().get(i),
                    dto.getdZ().get(i));

            SensorData sensorData = new SensorData(
                    accelerometerData,
                    compassData,
                    gyroscopeData);

            sensorDataPoints.add(sensorData);
        }
    }

    public MoveDataDTO toDTO()
    {
        MoveDataDTO dto = new MoveDataDTO();

        dto.setCompetitorID(competitorID);
        dto.setBoatID(boatID);
        dto.setRaceID(raceID);
        dto.setTimeMilli(timestamp.getTimeInMillis());

        dto.setLatitude(gpsData.getLatitude());
        dto.setLongitude(gpsData.getLongitude());

        List<Float> x     =  new ArrayList<>();
        List<Float> y     =  new ArrayList<>();
        List<Float> z     =  new ArrayList<>();
        List<Float> angle =  new ArrayList<>();
        List<Float> dX    =  new ArrayList<>();
        List<Float> dY    =  new ArrayList<>();
        List<Float> dZ    =  new ArrayList<>();

        for (SensorData sensorData : sensorDataPoints)
        {
            x.add(sensorData.getAccelerometerData().getX());
            y.add(sensorData.getAccelerometerData().getY());
            z.add(sensorData.getAccelerometerData().getZ());
            angle.add(sensorData.getCompassData().getAngle());
            dX.add(sensorData.getGyroscopeData().getdX());
            dY.add(sensorData.getGyroscopeData().getdY());
            dZ.add(sensorData.getGyroscopeData().getdZ());
        }

        dto.setX(x);
        dto.setY(y);
        dto.setZ(z);
        dto.setAngle(angle);
        dto.setdX(dX);
        dto.setdY(dY);
        dto.setdZ(dZ);

        return dto;
    }

    public Long getCompetitorID()
    {
        return competitorID;
    }

    public void setCompetitorID(Long competitorID)
    {
        this.competitorID = competitorID;
    }

    public Long getBoatID()
    {
        return boatID;
    }

    public void setBoatID(Long boatID)
    {
        this.boatID = boatID;
    }

    public Long getRaceID()
    {
        return raceID;
    }

    public void setRaceID(Long raceID)
    {
        this.raceID = raceID;
    }

    public Calendar getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp)
    {
        this.timestamp = timestamp;
    }

    public GPSData getGpsData()
    {
        return gpsData;
    }

    public void setGpsData(GPSData gpsData)
    {
        this.gpsData = gpsData;
    }

    public List<SensorData> getSensorDataPoints()
    {
        return sensorDataPoints;
    }

    public void setSensorDataPoints(List<SensorData> sensorDataPoints)
    {
        this.sensorDataPoints = sensorDataPoints;
    }

}
