package com.windhound.server.database;

import com.windhound.server.race.Boat;
import com.windhound.server.race.BoatInfo;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBAdmin.addBoatAdmins;
import static com.windhound.server.database.DBAdmin.deleteAllBoatAdmins;
import static com.windhound.server.database.DBRelation.addBoatRaceRelations;
import static com.windhound.server.database.DBRelation.deleteAllBoatRaceRelations;
import static com.windhound.server.database.DBRelation.addBoatCompetitorRelations;
import static com.windhound.server.database.DBRelation.deleteAllBoatCompetitorRelations;

public class DBBoat
{
    public static Boat loadBoatByID(Connection connection, Long boatID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryBoatByID + boatID);

        Long id = ((BigDecimal) DBManager.getValueAt(table, 0, "BOAT_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> competitors = loadCompetitorsByBoatID(connection, boatID);
        HashSet<Long> admins = loadAdminsByBoatID(connection, boatID);
        HashSet<Long> races = loadRacesByBoatID(connection, boatID);

        Boat boat = new Boat(
                id,
                name,
                admins,
                competitors,
                races
        );

        return boat;
    }

    private static String generateInsertBoatQuery(String boatName, BoatInfo boatInfo)
    {
        String query = "INSERT INTO BOATS (NAME, SKIPPER, SAILNO, LENGTH, BEAM, DISPLACEMENT, DRAFT, CLASS, TYPE," +
                " GPH, OFFSHORE_TOD, OFFSHORE_TOT, OFFSHORE_TRIPLE_L, OFFSHORE_TRIPLE_M, OFFSHORE_TRIPLE_H," +
                "INSHORE_TOT, INSHORE_TOD, INSHORE_TRIPLE_L, INSHORE_TRIPLE_M, INSHORE_TRIPLE_H) VALUES ('" +
                boatName + "', '" +
                boatInfo.getSkipper() + "', " +
                boatInfo.getSailNo() + ", " +
                boatInfo.getLength() + ", " +
                boatInfo.getBeam() + ", " +
                boatInfo.getDisplacement() + ", " +
                boatInfo.getDraft() + ", '" +
                boatInfo.getBoatClass() + "', '" +
                boatInfo.getType() + "', " +
                boatInfo.getGph() + ", " +
                boatInfo.getOffshoreToD() + ", " +
                boatInfo.getOffshoreToT() + ", " +
                boatInfo.getOffshoreTnoL() + ", " +
                boatInfo.getOffshoreTnoM() + ", " +
                boatInfo.getOffshoreTnoH() + ", " +
                boatInfo.getInshoreToD() + ", " +
                boatInfo.getInshoreToT() + ", " +
                boatInfo.getInshoreTnoL() + ", " +
                boatInfo.getInshoreTnoM() + ", " +
                boatInfo.getInshoreTnoH() + ")";

        return query;
    }

    private static String generateUpdateBoatQuery(Long boatID, String boatName, BoatInfo boatInfo)
    {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id"          , String.valueOf(boatID));
        map.put("name"             , boatName);
        map.put("skipper"          , boatInfo.getSkipper());
        map.put("sailno"           , String.valueOf(boatInfo.getSailNo()));
        map.put("length"           , String.valueOf(boatInfo.getLength()));
        map.put("beam"             , String.valueOf(boatInfo.getBeam()));
        map.put("displacement"     , String.valueOf(boatInfo.getSailNo()));
        map.put("draft"            , String.valueOf(boatInfo.getDraft()));
        map.put("class"            , boatInfo.getBoatClass());
        map.put("type"             , boatInfo.getType());
        map.put("gph"              , String.valueOf(boatInfo.getGph()));
        map.put("offshore_tod"     , String.valueOf(boatInfo.getOffshoreToD()));
        map.put("offshore_tot"     , String.valueOf(boatInfo.getOffshoreToT()));
        map.put("offshore_triple_l", String.valueOf(boatInfo.getOffshoreTnoL()));
        map.put("offshore_triple_m", String.valueOf(boatInfo.getOffshoreTnoM()));
        map.put("offshore_triple_h", String.valueOf(boatInfo.getOffshoreTnoH()));
        map.put("inshore_tod"      , String.valueOf(boatInfo.getInshoreToD()));
        map.put("inshore_tot"      , String.valueOf(boatInfo.getInshoreToT()));
        map.put("inshore_triple_l" , String.valueOf(boatInfo.getInshoreTnoL()));
        map.put("inshore_triple_m" , String.valueOf(boatInfo.getInshoreTnoM()));
        map.put("inshore_triple_h" , String.valueOf(boatInfo.getInshoreTnoH()));

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryUpdateBoat);

        return query;
    }

    public static Long saveOrUpdateBoat(Connection connection, Boat boat)
    {
        Long boatID = null;

        Map<String, String> valuesMap = new HashMap<>();

        valuesMap.put("name", boat.getName());

        // TODO move "sub" after valuesMap.put(id)
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (boat.getID() != null)
        {
            valuesMap.put("id", boat.getID().toString());

            //String finalQuery = generateUpdateBoatQuery(boat.getID(), boat.getName(), boat.getBoatInfo());
            String finalQuery = sub.replace(queryUpdateBoat);
            DBManager.executeSetQuery(connection, finalQuery);

            boatID = boat.getID();

            deleteAllBoatAdmins(connection, boatID);
            deleteAllBoatRaceRelations(connection, boatID);
            deleteAllBoatCompetitorRelations(connection, boatID);
        }
        //INSERT
        else
        {
            //String finalQuery = generateInsertBoatQuery(boat.getName(), boat.getBoatInfo());
            String finalQuery = sub.replace(queryInsertBoat);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestBoatByName);
            JTable table = executeLoadQuery(connection, idQuery);
            boatID = Long.valueOf(table.getValueAt(0, 0).toString());
        }

        //Save admins
        Long[] adminIDs = boat.getAdmins().toArray(new Long[0]);
        addBoatAdmins(connection, boatID, adminIDs);

        //Save races
        Long[] raceIDs = boat.getManagers().toArray(new Long[0]);
        addBoatRaceRelations(connection, boatID, raceIDs);

        //Save competitors
        Long[] competitorIDs = boat.getSubordinates().toArray(new Long[0]);
        addBoatCompetitorRelations(connection, boatID, competitorIDs);

        return boatID;
    }

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }

    public static HashSet<Long> loadAdminsByBoatID(Connection connection, Long boatID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryAdminsByBoat + boatID);

        HashSet<Long> admins = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            admins.add(id);
        }

        return admins;
    }

    public static HashSet<Long> loadRacesByBoatID(Connection connection, Long boatID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationRacesByBoat + boatID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    public static void deleteBoat(Connection connection, Long boatID)
    {
        String query = queryDeleteBoatByID + boatID.toString();
        DBManager.executeSetQuery(connection, query);

        deleteAllBoatAdmins(connection, boatID);
        deleteAllBoatRaceRelations(connection, boatID);
        deleteAllBoatCompetitorRelations(connection, boatID);
    }

    //
    // Query strings
    //
    private static String queryBoatByID =
            "select * from BOAT where BOAT_ID=";
    private static String queryRelationCompetitorsByBoat =
            "select * from REL_BOAT_USER where BOAT_ID=";
    private static String queryRelationRacesByBoat =
            "SELECT * FROM REL_RACE_BOAT WHERE BOAT_ID=";
    private static String queryAdminsByBoat =
            "SELECT * FROM ADMINS WHERE STAGE_TYPE='Boat' AND STAGE_ID=";
    private static String queryLatestBoatByName =
            "SELECT (BOAT_ID) FROM BOATS WHERE NAME='${name}' ORDER BY BOAT_ID DESC";
    private static String queryDeleteBoatByID =
            "DELETE FROM BOATS WHERE BOAT_ID=";
    private static String queryInsertBoat =
            "INSERT INTO BOATS (NAME) VALUES (" +
                    "'${name}'               )";
    private static String queryUpdateBoat =
            "UPDATE BOATS SET            " +
                    "NAME='${name}'      " +
                    "WHERE Boat_ID=${id} ";
    /*private static String queryUpdateBoat =
            "UPDATE BOATS SET " +
                    "NAME='${name}', " +
                    "SKIPPER='${skipper}', " +
                    "SAILNO=${sailno}, " +
                    "LENGTH=${length}, " +
                    "BEAM=${beam}, " +
                    "DISPLACEMENT=${displacement}, " +
                    "DRAFT=${draft}, " +
                    "CLASS='${class}', " +
                    "TYPE='${type}', " +
                    "GPH=${gph}, " +
                    "OFFSHORE_TOD=${offshore_tod}, " +
                    "OFFSHORE_TOT=${offshore_tot}, " +
                    "OFFSHORE_TRIPLE_L=${offshore_triple_l}, " +
                    "OFFSHORE_TRIPLE_M=${offshore_triple_m}, " +
                    "OFFSHORE_TRIPLE_H=${offshore_triple_h}, " +
                    "INSHORE_TOD=${inshore_tod}, " +
                    "INSHORE_TOT=${inshore_tot}, " +
                    "INSHORE_TRIPLE_L=${inshore_triple_l}, " +
                    "INSHORE_TRIPLE_M=${inshore_triple_m}, " +
                    "INSHORE_TRIPLE_H=${inshore_triple_h} " +
                    "WHERE BOAT_ID=${boat_id}";*/

}
