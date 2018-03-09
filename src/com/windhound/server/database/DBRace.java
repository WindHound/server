package com.windhound.server.database;

import com.windhound.server.race.Race;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBManager.getValueAt;

public class DBRace
{
    public static Race loadRaceByID(Connection connection, Long raceID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRaceByID + raceID);

        Long id = ((BigDecimal) DBManager.getValueAt(table, 0, "RACE_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");
        String startDateString = getValueAt(table, 0, "START_TIME").toString();
        String endDateString   = getValueAt(table, 0, "END_TIME").toString();

        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar endDate   = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        try
        {
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
            startDate.setTime(dateFormat.parse(startDateString));
            endDate.setTime(dateFormat.parse(endDateString));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        HashSet<Long> boats = loadBoatsByRaceID(connection, raceID);
        HashSet<Long> events = loadEventsByRaceID(connection, raceID);
        HashSet<Long> admins = DBEvent.loadAdminsByEventTypeAndID(connection, "Race", raceID);

        Race race = new Race(
                id,
                name,
                startDate,
                endDate,
                admins,
                boats,
                events
        );

        return race;
    }

    public static Long saveOrUpdateRace(Connection connection, Race race)
    {
        Long raceID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> boatMap = new HashMap<>();
        Map<String, String> eventMap = new HashMap<>();

        DateFormat dateFormat  = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
        String startDateString = dateFormat.format(race.getStartDate().getTime());
        String endDateString   = dateFormat.format(race.getEndDate().getTime());

        valuesMap.put("name", race.getName());
        valuesMap.put("start_time", startDateString);
        valuesMap.put("end_time", endDateString);

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (race.getID() != null)
        {
            valuesMap.put("id", race.getID().toString());
            String finalQuery = sub.replace(queryUpdateRace);
            DBManager.executeSetQuery(connection, finalQuery);

            raceID = race.getID();

            deleteRaceAdmins(connection, raceID);
            deleteRaceBoats(connection, raceID);
            deleteRaceEvents(connection, raceID);
        }
        //INSERT
        else
        {
            String finalQuery = sub.replace(queryInsertRace);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestRaceByName);
            JTable table = executeLoadQuery(connection, idQuery);
            raceID = Long.valueOf(table.getValueAt(0, 0).toString());
        }

        //Save admins
        List<Long> admins = race.getAdmins();
        adminMap.put("stage_id", raceID.toString());

        for (Long admin : admins)
        {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertRaceAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save boats
        List<Long> boats = race.getSubordinates();
        boatMap.put("race_id", raceID.toString());

        for (Long boat : boats)
        {
            boatMap.put("boat_id", boat.toString());
            StrSubstitutor boatSub = new StrSubstitutor(boatMap);
            String finalAdminQuery = boatSub.replace(queryInsertRaceBoats);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save events
        List<Long> events = race.getAdmins();
        eventMap.put("race_id", raceID.toString());

        for (Long event : events)
        {
            eventMap.put("event_id", event.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalAdminQuery = eventSub.replace(queryInsertRaceEvents);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }


        return raceID;
    }

    public static void deleteRace(Connection connection, Long raceID)
    {
        String query = queryDeleteRaceByID + raceID.toString();
        DBManager.executeSetQuery(connection, query);


        deleteRaceAdmins(connection, raceID);
        deleteRaceBoats(connection, raceID);
        deleteRaceEvents(connection, raceID);
    }

    private static HashSet<Long> loadBoatsByRaceID(Connection connection, Long raceID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationBoatsByRace + raceID);

        HashSet<Long> boats = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "BOAT_ID")).longValue();
            boats.add(id);
        }

        return boats;
    }

    private static HashSet<Long> loadEventsByRaceID(Connection connection, Long raceID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationEventsByRace + raceID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    private static void deleteRaceEvents(Connection connection, Long raceID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceEventRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    private static void deleteRaceBoats(Connection connection, Long raceID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceBoatRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    private static void deleteRaceAdmins(Connection connection, Long raceID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceAdminRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Query strings
    //
    private static String queryRaceByID =
            "select * from RACE where RACE_ID=";
    private static String queryRelationBoatsByRace =
            "select * from REL_RACE_BOAT where RACE_ID=";
    private static String queryRelationEventsByRace =
            "select * from REL_EVENT_RACE where RACE_ID=";

    private static String queryInsertRace =
            "INSERT INTO RACE (NAME, START_TIME, END_TIME) VALUES             (" +
                    "'${name}'                                                ," +
                    "TO_TIMESTAMP('${start_time}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
                    "TO_TIMESTAMP('${end_time}', 'YYYY-MM-DD HH24:MI:SS.FF'))  ";
    private static String queryUpdateRace =
            "UPDATE RACE SET                                                              " +
                    "NAME='${name}'                                                      ," +
                    "START_TIME=TO_TIMESTAMP('${start_time}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
                    "END_TIME=TO_TIMESTAMP('${end_time}', 'YYYY-M;M-DD HH24:MI:SS.FF'))   ";
    private static String queryLatestRaceByName =
            "SELECT (RACE_ID) FROM RACE WHERE NAME='${name}' ORDER BY RACE_ID DESC";
    private static String queryInsertRaceAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                    "'${user_id}'                                      ," +
                    "'Race'                                            ," +
                    "'${stage_id}'                                     )";
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
