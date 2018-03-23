package com.windhound.server.movedata;

public class SensorData
{
    private AccelerometerData accelerometerData;
    private CompassData       compassData;
    private GyroscopeData     gyroscopeData;

    public SensorData()
    {

    }

    public SensorData(
            AccelerometerData a_acceleAccelerometerData,
            CompassData       a_compassData,
            GyroscopeData     a_gyroGyroscopeData)
    {
        accelerometerData = a_acceleAccelerometerData;
        compassData       = a_compassData;
        gyroscopeData     = a_gyroGyroscopeData;
    }

    public AccelerometerData getAccelerometerData()
    {
        return accelerometerData;
    }

    public void setAccelerometerData(AccelerometerData accelerometerData)
    {
        this.accelerometerData = accelerometerData;
    }

    public CompassData getCompassData()
    {
        return compassData;
    }

    public void setCompassData(CompassData compassData)
    {
        this.compassData = compassData;
    }

    public GyroscopeData getGyroscopeData()
    {
        return gyroscopeData;
    }

    public void setGyroscopeData(GyroscopeData gyroscopeData)
    {
        this.gyroscopeData = gyroscopeData;
    }
}
