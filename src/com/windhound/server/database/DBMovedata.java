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

    public static Long saveMovedata(Connection connection, MoveData moveData, Long raceID, Long boatID) {
        Map<String, String> map = new HashMap<>();
        map.put("race_id", String.valueOf(raceID));
        map.put("boat_id", String.valueOf(boatID));

        map.put("longitude", String.valueOf(moveData.getGpsData().getLongitude()));
        map.put("latitude", String.valueOf(moveData.getGpsData().getLatitude()));

        //Currently selects first sensor data in list
        map.put("acc_x", String.valueOf(moveData.getSensorDatas().get(0).accelerometerData.getX()));
        map.put("acc_y", String.valueOf(moveData.getSensorDatas().get(0).accelerometerData.getY()));
        map.put("acc_z", String.valueOf(moveData.getSensorDatas().get(0).accelerometerData.getZ()));

        map.put("gyro_x", String.valueOf(moveData.getSensorDatas().get(0).gyroscopeData.getX()));
        map.put("gyro_y", String.valueOf(moveData.getSensorDatas().get(0).gyroscopeData.getY()));
        map.put("gyro_z", String.valueOf(moveData.getSensorDatas().get(0).gyroscopeData.getZ()));

        map.put("compass", String.valueOf(moveData.getSensorDatas().get(0).compassData.getAngle()));


        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryInsertMoveData);

        Long state = DBManager.executeSetQuery(connection, query);

        return state;
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
    private static String queryInsertMoveData =
            "INSERT INTO LOCATION (RACE_ID, BOAT_ID, LATITUDE, LONGITUDE, ACC_X, ACC_Y, ACC_Z, GYRO_X, GYRO_Y," +
                "GYRO_Z, COMPASS) VALUES (" +
                "${race_id}, " +
                "${boat_id}, " +
                "${longitude}, " +
                "${latitude}, " +
                "${acc_x}, " +
                "${acc_y}, " +
                "${acc_z}, " +
                "${gyro_x}, " +
                "${gyro_y}, " +
                "${gyro_z}, " +
                "${compass})";


}
