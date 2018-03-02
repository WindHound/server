package com.windhound.server.database;

import com.windhound.server.movedata.*;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBMovedata {

    public static Long saveMovedata(Connection connection, MoveData moveData) {
        //TODO: implement saveMoveData

        return new Long(-1);
    }

    public static List<MoveData> loadMoveDatas (Connection connection, Long raceID, Long boatID) {

        Map<String, String> map = new HashMap<>();
        map.put("race_id", String.valueOf(raceID));
        map.put("boat_id", String.valueOf(boatID));

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryLoadMoveData);
        JTable table = DBManager.executeLoadQuery(connection, query);

        ArrayList<MoveData> list = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); i++) {
            float latitude = ((BigDecimal) DBManager.getValueAt(table, i, "LATITUDE")).floatValue();
            float longitude = ((BigDecimal) DBManager.getValueAt(table, i, "LONGITUDE")).floatValue();
            GPSData gpsData = new GPSData(latitude, longitude);

            float accX = ((BigDecimal) DBManager.getValueAt(table, i, "ACC_X")).floatValue();
            float accY = ((BigDecimal) DBManager.getValueAt(table, i, "ACC_Y")).floatValue();
            float accZ = ((BigDecimal) DBManager.getValueAt(table, i, "ACC_Z")).floatValue();
            AccelerometerData accelerometerData = new AccelerometerData(accX, accY, accZ);

            float gyroX = ((BigDecimal) DBManager.getValueAt(table, i, "GYRO_X")).floatValue();
            float gyroY = ((BigDecimal) DBManager.getValueAt(table, i, "GYRO_Y")).floatValue();
            float gyroZ = ((BigDecimal) DBManager.getValueAt(table, i, "GYRO_Z")).floatValue();
            GyroscopeData gyroscopeData = new GyroscopeData(gyroX, gyroY, gyroZ);

            float compass = ((BigDecimal) DBManager.getValueAt(table, i, "COMPASS")).floatValue();
            CompassData compassData = new CompassData(compass);

            SensorData sensorData = new SensorData(accelerometerData, gyroscopeData, compassData);
            ArrayList<SensorData> sensorList = new ArrayList<>();
            sensorList.add(sensorData);

            MoveData moveData = new MoveData(gpsData, sensorList);
            list.add(moveData);
        }

        return list;
    }



    private static String queryLoadMoveData =
            "SELECT * FROM LOCATION WHERE RACE_ID=${race_id} AND BOAT_ID=${boat_id} ORDER BY LOCATION_ID DESC";
}
