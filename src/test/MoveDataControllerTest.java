package test;

import com.windhound.server.database.DBManager;
import com.windhound.server.movedata.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveDataControllerTest
{
    @Test
    public static void addGetMoveData()
    {
        Long compID = 2l;
        Long boatID = 3l;
        Long raceID = 4l;

        Instant timestamp = Instant.ofEpochMilli(1525232682000l);

        GPSData gpsData = new GPSData(45.7664343f, 120.556f);

        List<SensorData> sensorDataPoints = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            AccelerometerData accelerometerData = new AccelerometerData(0f + i, 1f + i, 2f + i);
            CompassData compassData = new CompassData(46.33f + i);
            GyroscopeData gyroscopeData = new GyroscopeData(23f + i, 54f + i, 100f + i);

            sensorDataPoints.add(new SensorData(
                    accelerometerData,
                    compassData,
                    gyroscopeData
            ));
        }

        MoveData moveData = new MoveData(
                compID,
                boatID,
                raceID,
                timestamp,
                gpsData,
                sensorDataPoints
        );

        DBManager.saveMoveData(moveData);

        MoveData[] moveDataPoints = DBManager.loadMoveData(raceID, boatID);

        //assertEquals(moveDataPoints.length, 1);
        MoveData dbMoveData = moveDataPoints[0];

        assertEquals(moveData.getCompetitorID(), dbMoveData.getCompetitorID());
        assertEquals(moveData.getBoatID(),       dbMoveData.getBoatID());
        assertEquals(moveData.getRaceID(),       dbMoveData.getRaceID());
        assertEquals(moveData.getTimestamp().toEpochMilli(), dbMoveData.getTimestamp().toEpochMilli());

        // Currently DBManager swaps lat with long
        assertEquals(moveData.getGpsData().getLatitude(),  dbMoveData.getGpsData().getLongitude());
        assertEquals(moveData.getGpsData().getLongitude(), dbMoveData.getGpsData().getLatitude());
        //

        //List<SensorData> sensorDataPoints = moveData.getSensorDataPoints();
        List<SensorData> dbSensortDataPoints = dbMoveData.getSensorDataPoints();
        assertEquals(sensorDataPoints.size(), dbSensortDataPoints.size());

        for (int i = 0; i < sensorDataPoints.size(); ++i)
        {
            assertEquals(sensorDataPoints.get(i).getAccelerometerData().getX(), dbSensortDataPoints.get(i).getAccelerometerData().getX());
            assertEquals(sensorDataPoints.get(i).getAccelerometerData().getY(), dbSensortDataPoints.get(i).getAccelerometerData().getY());
            assertEquals(sensorDataPoints.get(i).getAccelerometerData().getZ(), dbSensortDataPoints.get(i).getAccelerometerData().getZ());
            assertEquals(sensorDataPoints.get(i).getCompassData().getAngle(), dbSensortDataPoints.get(i).getCompassData().getAngle());
            assertEquals(sensorDataPoints.get(i).getGyroscopeData().getdX(), dbSensortDataPoints.get(i).getGyroscopeData().getdX());
            assertEquals(sensorDataPoints.get(i).getGyroscopeData().getdY(), dbSensortDataPoints.get(i).getGyroscopeData().getdY());
            assertEquals(sensorDataPoints.get(i).getGyroscopeData().getdZ(), dbSensortDataPoints.get(i).getGyroscopeData().getdZ());
        }
    }
}