package com.windhound.server.database;

import com.windhound.server.race.Boat;
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

    public static Long saveOrUpdateBoat (Connection connection, Boat boat) {
        Long boatID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> competitorsMap = new HashMap<>();
        Map<String, String> racesMap = new HashMap<>();


        valuesMap.put("name", boat.getName());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //TODO: work out how to generate update & insert queries including BoatInfo object

        //UPDATE
        if (boat.getID() != null) {
            valuesMap.put("id", boat.getID().toString());
            String finalQuery = sub.replace(queryUpdateBoat);
            DBManager.executeSetQuery(connection, finalQuery);

            boatID = boat.getID();

            deleteBoatAdmins(connection, boatID);
            deleteBoatCompetitors(connection, boatID);
            deleteBoatRaces(connection, boatID);
        }
        //INSERT
        else {
            String finalQuery = sub.replace(queryInsertBoat);
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


    //TODO: design queryUpdateBoat, queryInsertBoat
    private static String queryUpdateBoat = null;

    private static String queryInsertBoat = null;
}
