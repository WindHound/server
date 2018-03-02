package com.windhound.server.database;

import com.windhound.server.race.Boat;
import com.windhound.server.race.BoatInfo;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.windhound.server.database.DBManager.executeLoadQuery;

public class DBBoat {

    public static Boat loadBoatByID(Connection connection, Long boatID) {
        JTable table = DBManager.executeLoadQuery(connection, queryBoatByID + boatID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "BOAT_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> competitors = loadCompetitorsByBoatID(connection, boatID);

        Boat boat = Boat.createBoat(
                id,
                name,
                new HashSet<>(),
                competitors,
                new HashSet<>()
        );

        return boat;
    }

    public static HashSet<Long> loadCompetitorsByBoatID(Connection connection, Long boatID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationCompetitorsByBoat + boatID);

        HashSet<Long> competitors = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            competitors.add(id);
        }

        return competitors;
    }

    private static String generateInsertBoatQuery(String boatName, BoatInfo boatInfo) {
        String query = "INSERT INTO BOATS (NAME, SKIPPER, SAILNO, LENGTH, BEAM, DISPLACEMENT, DRAFT, CLASS, TYPE," +
                " GPH, OFFSHORE_TOD, OFFSHORE_TOT, OFFSHORE_TRIPLE_L, OFFSHORE_TRIPLE_M, OFFSHORE_TRIPLE_H," +
                "INSHORE_TOT, INSHORE_TOD, INSHORE_TRIPLE_L, INSHORE_TRIPLE_M, INSHORE_TRIPLE_H) VALUES ('" +
                boatName                    + "', '" +
                boatInfo.getSkipper()       + "', " +
                boatInfo.getSailNo()        + ", " +
                boatInfo.getLength()        + ", " +
                boatInfo.getBeam()          + ", " +
                boatInfo.getDisplacement()  + ", " +
                boatInfo.getDraft()         + ", '" +
                boatInfo.getBoatClass()     + "', '" +
                boatInfo.getType()          + "', " +
                boatInfo.getGph()           + ", " +
                boatInfo.getOffshoreToD()   + ", " +
                boatInfo.getOffshoreToT()   + ", " +
                boatInfo.getOffshoreTnoL()  + ", " +
                boatInfo.getOffshoreTnoM()  + ", " +
                boatInfo.getOffshoreTnoH()  + ", " +
                boatInfo.getInshoreToD()    + ", " +
                boatInfo.getInshoreToT()    + ", " +
                boatInfo.getInshoreTnoL()   + ", " +
                boatInfo.getInshoreTnoM()   + ", " +
                boatInfo.getInshoreTnoH()   + ")";

        return query;
    }

    public static String generateUpdateBoatQuery(Long boatID, String boatName, BoatInfo boatInfo) {

        Map<String, String> map = new HashMap<>();

        map.put("boat_id", String.valueOf(boatID));
        map.put("name", boatName);
        map.put("skipper", boatInfo.getSkipper());
        map.put("sailno", String.valueOf(boatInfo.getSailNo()));
        map.put("length", String.valueOf(boatInfo.getLength()));
        map.put("beam", String.valueOf(boatInfo.getBeam()));
        map.put("displacement", String.valueOf(boatInfo.getSailNo()));
        map.put("draft", String.valueOf(boatInfo.getDraft()));
        map.put("class", boatInfo.getBoatClass());
        map.put("type", boatInfo.getType());
        map.put("gph", String.valueOf(boatInfo.getGph()));
        map.put("offshore_tod", String.valueOf(boatInfo.getOffshoreToD()));
        map.put("offshore_tot", String.valueOf(boatInfo.getOffshoreToT()));
        map.put("offshore_triple_l", String.valueOf(boatInfo.getOffshoreTnoL()));
        map.put("offshore_triple_m", String.valueOf(boatInfo.getOffshoreTnoM()));
        map.put("offshore_triple_h", String.valueOf(boatInfo.getOffshoreTnoH()));
        map.put("inshore_tod", String.valueOf(boatInfo.getInshoreToD()));
        map.put("inshore_tot", String.valueOf(boatInfo.getInshoreToT()));
        map.put("inshore_triple_l", String.valueOf(boatInfo.getInshoreTnoL()));
        map.put("inshore_triple_m", String.valueOf(boatInfo.getInshoreTnoM()));
        map.put("inshore_triple_h", String.valueOf(boatInfo.getInshoreTnoH()));

        StrSubstitutor sub = new StrSubstitutor(map);
        String query = sub.replace(queryUpdateBoat);

        return query;
    }

    public static Long saveOrUpdateBoat (Connection connection, Boat boat) {
        Long boatID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> competitorsMap = new HashMap<>();
        Map<String, String> racesMap = new HashMap<>();


        valuesMap.put("name", boat.getName());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (boat.getID() != null) {
            //valuesMap.put("id", boat.getID().toString());
            String finalQuery = generateUpdateBoatQuery(boat.getID(), boat.getName(), boat.getBoatInfo());
            DBManager.executeSetQuery(connection, finalQuery);

            boatID = boat.getID();

            deleteBoatAdmins(connection, boatID);
            deleteBoatCompetitors(connection, boatID);
            deleteBoatRaces(connection, boatID);
        }
        //INSERT
        else {
            String finalQuery = generateInsertBoatQuery(boat.getName(), boat.getBoatInfo());
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestBoatByName);
            JTable table   = executeLoadQuery(connection, idQuery);
            boatID = Long.valueOf(table.getValueAt(0,0).toString());
        }

        //Save admins
        List<Long> admins = boat.getAdmins();
        adminMap.put("boat_id", boatID.toString());

        for (Long admin : admins) {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertBoatAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save competitors
        List<Long> competitors = boat.getAdmins();
        competitorsMap.put("boat_id", boatID.toString());

        for (Long competitor : competitors) {
            competitorsMap.put("user_id", competitor.toString());
            StrSubstitutor compSub = new StrSubstitutor(competitorsMap);
            String finalAdminQuery = compSub.replace(queryInsertBoatCompetitors);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save races
        List<Long> races = boat.getAdmins();
        racesMap.put("boat_id", boatID.toString());

        for (Long race : races) {
            racesMap.put("race_id", race.toString());
            StrSubstitutor raceSub = new StrSubstitutor(racesMap);
            String finalAdminQuery = raceSub.replace(queryInsertBoatRaces);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }


        return boatID;
    }

    private static Long deleteBoatAdmins(Connection connection, Long boatID) {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatAdminRelations);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteBoatCompetitors(Connection connection, Long boatID) {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatCompetitorRelations);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteBoatRaces(Connection connection, Long boatID) {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatRaceRelations);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    public static Long deleteBoat (Connection connection, Long boatID) {
        String query = queryDeleteBoatByID + boatID.toString();
        Long state = DBManager.executeSetQuery(connection, query);

        deleteBoatAdmins(connection, boatID);
        deleteBoatCompetitors(connection, boatID);
        deleteBoatRaces(connection, boatID);

        return state;
    }


    private static String queryBoatByID =
            "select * from BOAT where BOAT_ID=";
    private static String queryRelationCompetitorsByBoat =
            "select * from REL_BOAT_USER where USER_ID=";

    private static String queryInsertBoatAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_ID, STAGE_TYPE) VALUES (" +
                "'${user_id}'," +
                "'${boat_id}'," +
                "'Boat')";
    private static String queryInsertBoatCompetitors =
            "INSERT INTO REL_BOAT_USER (BOAT_ID, USER_ID) VALUES (" +
                "'${boat_id}'," +
                "'${user_id}')";
    private static String queryInsertBoatRaces =
            "INSERT INTO REL_RACE_BOAT (RACE_ID, BOAT_ID) VALUES (" +
                "'${race_id}'," +
                "'${boat_id}')";
    private static String queryLatestBoatByName =
            "SELECT (BOAT_ID) FROM BOATS WHERE NAME='${name}' ORDER BY BOAT_ID DESC";
    private static String queryDeleteBoatAdminRelations =
            "DELETE FROM ADMINS WHERE STAGE_ID='${boat_id}' AND STAGE_TYPE='Boat'";
    private static String queryDeleteBoatRaceRelations =
            "DELETE FROM REL_RACE_BOAT WHERE BOAT_ID='${boat_id}'";
    private static String queryDeleteBoatCompetitorRelations =
            "DELETE FROM REL_BOAT_USER WHERE BOAT_ID='${boat_id}'";
    private static String queryDeleteBoatByID =
            "DELETE FROM BOAT WHERE BOAT_ID=";

    private static String queryUpdateBoat =
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
            "WHERE BOAT_ID=${boat_id}";

}
