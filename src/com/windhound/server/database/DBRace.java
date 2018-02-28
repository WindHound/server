package com.windhound.server.database;

import com.windhound.server.race.Race;
import org.apache.commons.lang.text.StrSubstitutor;
import sun.util.resources.ga.LocaleNames_ga;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.windhound.server.database.DBManager.executeLoadQuery;

public class DBRace {

    public static Race loadRaceByID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRaceByID + raceID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "RACE_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> boats = loadBoatsByRaceID(connection, raceID);
        HashSet<Long> events = loadEventsByRaceID(connection, raceID);
        HashSet<Long> admins = DBEvent.loadAdminsByEventTypeAndID(connection, "Race", raceID);

        Race race = Race.createRace(
                id,
                name,
                admins,
                boats,
                events
        );

        return race;

    }

    private static HashSet<Long> loadBoatsByRaceID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationBoatsByRace + raceID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    private static HashSet<Long> loadEventsByRaceID(Connection connection, Long raceID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationEventsByRace + raceID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    public static Long saveOrUpdateRace (Connection connection, Race race) {
        Long raceID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> boatMap = new HashMap<>();
        Map<String, String> eventMap = new HashMap<>();


        valuesMap.put("name"      , race.getName());
        valuesMap.put("start_time", race.getStartDate().toString());
        valuesMap.put("end_time"  , race.getEndDate().toString());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (race.getID() != null) {
            valuesMap.put("id", race.getID().toString());
            String finalQuery = sub.replace(queryUpdateRace);
            DBManager.executeSetQuery(connection, finalQuery);

            raceID = race.getID();

            deleteRaceAdmins(connection, raceID);
            deleteRaceBoats(connection, raceID);
            deleteRaceEvents(connection, raceID);
        }
        //INSERT
        else {
            String finalQuery = sub.replace(queryInsertRace);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestRaceByName);
            JTable table   = executeLoadQuery(connection, idQuery);
            raceID = Long.valueOf(table.getValueAt(0,0).toString());
        }

        //Save admins
        List<Long> admins = race.getAdmins();
        adminMap.put("race_id", raceID.toString());

        for (Long admin : admins) {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertRaceAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save boats
        List<Long> boats = race.getAdmins();
        adminMap.put("event_id", raceID.toString());

        for (Long boat : boats) {
            adminMap.put("boat_id", boat.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertRaceBoats);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save events
        List<Long> events = race.getAdmins();
        adminMap.put("race_id", raceID.toString());

        for (Long event : events) {
            adminMap.put("event_id", event.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertRaceEvents);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }


        return raceID;
    }

    public static Long deleteRace (Connection connection, Long raceID) {
        String query = queryDeleteRaceByID + raceID.toString();
        Long state = DBManager.executeSetQuery(connection, query);


        deleteRaceAdmins(connection, raceID);
        deleteRaceBoats(connection, raceID);
        deleteRaceEvents(connection, raceID);

        return new Long(-1);
    }

    private static Long deleteRaceEvents(Connection connection, Long raceID) {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceEventRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteRaceBoats(Connection connection, Long raceID) {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceBoatRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteRaceAdmins(Connection connection, Long raceID) {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceAdminRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }


    private static String queryRaceByID =
            "select * from RACE where RACE_ID=";
    private static String queryRelationBoatsByRace =
            "select * from REL_RACE_BOAT where RACE_ID=";
    private static String queryRelationEventsByRace =
            "select * from REL_EVENT_RACE where RACE_ID=";

    private static String queryInsertRace =
            "INSERT INTO RACE (NAME, START_TIME, END_TIME) VALUES )" +
            "'${name}'," +
            "TO_TIMESTAMP('${start_time}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
            "TO_TIMESTAMP('${end_time}', 'YYYY-M;M-DD HH24:MI:SS.FF'))";
    private static String queryUpdateRace =
            "UPDATE RACE SET " +
                "NAME='${name}'," +
                "START_TIME=TO_TIMESTAMP('${start_time}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
                "END_TIME=TO_TIMESTAMP('${end_time}', 'YYYY-M;M-DD HH24:MI:SS.FF'))";
    private static String queryLatestRaceByName =
            "SELECT (RACE_ID) FROM RACE WHERE NAME='${name}' ORDER BY RACE_ID DESC";
    private static String queryInsertRaceAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                "'${user_id}'                                              ," +
                "'Race'                                            ," +
                "'${stage_id}'                                             )";
    private static String queryInsertRaceBoats =
            "INSERT INTO REL_RACE_BOAT (RACE_ID, BOAT_ID) VALUES (" +
            "${race_id}," +
            "${boat_id})";
    private static String queryInsertRaceEvents =
            "INSERT INTO REL_EVENT_RACE (EVENT_ID, RACE_ID) VALUES (" +
                    "${event_id}," +
                    "${race_id})";
    private static String queryDeleteRaceByID =
            "DELETE FROM RACE WHERE RACE_ID=";
    private static String queryDeleteRaceEventRelation =
            "DELETE FROM REL_EVENT_RACE WHERE RACE_ID=${race_id}";
    private static String queryDeleteRaceBoatRelation =
            "DELETE FROM REL_RACE_BOAT WHERE RACE_ID=${race_id}";
    private static String queryDeleteRaceAdminRelation =
            "DELETE FROM ADMINS WHERE STAGE_ID=${race_id} AND STAGE_TYPE='Race'";

}
