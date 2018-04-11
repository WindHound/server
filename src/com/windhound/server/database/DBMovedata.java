package com.windhound.server.database;

import com.windhound.server.movedata.*;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBManager.getValueAt;

public class DBMoveData
{
    public static MoveData[] loadMoveData(Connection connection, Long raceID, Long boatID)
    {
        Map<String, String> map = new HashMap<>();
        map.put("race_id", String.valueOf(raceID));
        map.put("boat_id", String.valueOf(boatID));

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryLoadMoveData);
        JTable table = DBManager.executeLoadQuery(connection, query);

        List<MoveData> moveDataPoints = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); i++)
        {
            Long moveDataID   = ((BigDecimal)getValueAt(table, i, "MOVEDATA_ID")).longValue();
            Long competitorID = ((BigDecimal)getValueAt(table, i, "COMPETITOR_ID")).longValue();
            //Long boatID       = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();
            //Long raceID       = ((BigDecimal)getValueAt(table, i, "BOAT_ID")).longValue();

            String timestampString   = getValueAt(table, i, "TIMESTAMP").toString();
            Calendar timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
            try
            {
                timestamp.setTime(dateFormat.parse(timestampString));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }

            float latitude  = ((BigDecimal)getValueAt(table, i, "LATITUDE")).floatValue();
            float longitude = ((BigDecimal)getValueAt(table, i, "LONGITUDE")).floatValue();
            GPSData gpsData = new GPSData(latitude, longitude);

            List<SensorData> sensorDataPoints = loadSensorData(connection, moveDataID);

            MoveData moveData = new MoveData(competitorID, boatID, raceID, timestamp, gpsData, sensorDataPoints);
            moveDataPoints.add(moveData);
        }

        return moveDataPoints.toArray(new MoveData[moveDataPoints.size()]);
    }

    public static Long saveMoveData(Connection connection, MoveData moveData)
    {
        Map<String, String> map = new HashMap<>();
        map.put("competitor_id", moveData.getCompetitorID().toString());
        map.put("boat_id",       moveData.getBoatID().toString());
        map.put("race_id",       moveData.getRaceID().toString());
        map.put("latitude",      moveData.getGpsData().getLatitude().toString());
        map.put("longitude",     moveData.getGpsData().getLongitude().toString());

        DateFormat dateFormat  = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
        String timestamp = dateFormat.format(moveData.getTimestamp().getTime());
        map.put("timestamp", timestamp);

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryInsertMoveData);
        DBManager.executeSetQuery(connection, query);

        String idQuery = sub.replace(queryLatestMoveDataByName);
        JTable table = executeLoadQuery(connection, idQuery);
        Long moveDataID = Long.valueOf(table.getValueAt(0, 0).toString());

        for(SensorData sensorData : moveData.getSensorDataPoints())
            saveSensorData(connection, moveDataID, sensorData);

        return moveDataID;
    }

    private static List<SensorData> loadSensorData(Connection connection, Long moveDataID)
    {
        Map<String, String> map = new HashMap<>();
        map.put("movedata_id", moveDataID.toString());

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryLoadSensorData);
        JTable table = DBManager.executeLoadQuery(connection, query);

        List<SensorData> sensorDataPoints = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); i++)
        {
            float x = ((BigDecimal)getValueAt(table, i, "X")).floatValue();
            float y = ((BigDecimal)getValueAt(table, i, "Y")).floatValue();
            float z = ((BigDecimal)getValueAt(table, i, "Z")).floatValue();
            AccelerometerData accelerometerData = new AccelerometerData(x, y, z);

            float angle = ((BigDecimal)getValueAt(table, i, "ANGLE")).floatValue();
            CompassData compassData = new CompassData(angle);

            float dX = ((BigDecimal)getValueAt(table, i, "DX")).floatValue();
            float dY = ((BigDecimal)getValueAt(table, i, "DY")).floatValue();
            float dZ = ((BigDecimal)getValueAt(table, i, "DZ")).floatValue();
            GyroscopeData gyroscopeData = new GyroscopeData(dX, dY, dZ);

            SensorData sensorData = new SensorData(accelerometerData, compassData, gyroscopeData);
            sensorDataPoints.add(sensorData);
        }

        return sensorDataPoints;
    }

    private static void saveSensorData(Connection connection, Long moveDataID, SensorData sensorData)
    {
        Map<String, String> map = new HashMap<>();
        map.put("movedata_id", moveDataID.toString());
        map.put("x",           sensorData.getAccelerometerData().getX().toString());
        map.put("y",           sensorData.getAccelerometerData().getY().toString());
        map.put("z",           sensorData.getAccelerometerData().getZ().toString());
        map.put("angle",       sensorData.getCompassData().getAngle().toString());
        map.put("dx",          sensorData.getGyroscopeData().getdX().toString());
        map.put("dy",          sensorData.getGyroscopeData().getdY().toString());
        map.put("dz",          sensorData.getGyroscopeData().getdZ().toString());
        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryInsertSensorData);
        DBManager.executeSetQuery(connection, query);
    }

    private static String queryLoadMoveData =
            "SELECT * FROM MOVEDATA WHERE RACE_ID='${race_id}' AND BOAT_ID='${boat_id}' ORDER BY MOVEDATA_ID";
    private static String queryInsertMoveData =
            "INSERT INTO MOVEDATA (COMPETITOR_ID, BOAT_ID, RACE_ID, LONGITUDE, LATITUDE, TIMESTAMP) VALUES (" +
                    "${competitor_id}, " +
                    "${boat_id}, " +
                    "${race_id}, " +
                    "${latitude}, " +
                    "${longitude}, " +
                    "TO_TIMESTAMP('${timestamp}', 'YYYY-MM-DD HH24:MI:SS.FF'))";
    private static String queryLatestMoveDataByName =
            "SELECT (MOVEDATA_ID) FROM MOVEDATA WHERE COMPETITOR_ID='${competitor_id}' AND BOAT_ID='${boat_id}' AND RACE_ID='${race_id}' ORDER BY MOVEDATA_ID DESC";
    private static String queryInsertSensorData =
            "INSERT INTO SENSORDATA (MOVEDATA_ID, X, Y, Z, ANGLE, DX, DY, DZ) VALUES (" +
                    "${movedata_id}, " +
                    "${x}, " +
                    "${y}, " +
                    "${z}, " +
                    "${angle}, " +
                    "${dx}, " +
                    "${dy}, " +
                    "${dz})";
    private static String queryLoadSensorData =
            "SELECT * FROM SENSORDATA WHERE MOVEDATA_ID='${movedata_id}' ORDER BY SENSORDATA_ID";
}