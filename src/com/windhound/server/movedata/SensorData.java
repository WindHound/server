package com.windhound.server.movedata;

public class SensorData
{
    public final AccelerometerData accelerometerData;
    public final GyroscopeData gyroscopeData;
    public final CompassData       compassData;

    public SensorData(
            AccelerometerData a_acceleAccelerometerData,
            GyroscopeData     a_gyroGyroscopeData,
            CompassData       a_compassData)
    {
        accelerometerData = a_acceleAccelerometerData;
        gyroscopeData     = a_gyroGyroscopeData;
        compassData       = a_compassData;
    }
}
