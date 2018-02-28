package com.windhound.server.database;

import com.windhound.server.race.Event;
import oracle.sql.TIMESTAMP;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.windhound.server.database.DBManager.executeLoadQuery;

public class DBEvent {

    public static HashSet<Long> loadAdminsByEventTypeAndID(Connection connection, String stageType, Long stageID) {
        JTable table = DBManager.executeLoadQuery(connection, queryAdminsByStageType + "'" + stageType + "'"
                + " AND STAGE_ID=" + stageID);


        HashSet<Long> admins = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "USER_ID")).longValue();
            admins.add(id);
        }

        return admins;
    }

    private static HashSet<Long> loadChampionshipsByEventID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationChampionshipsByEvent + eventID);

        HashSet<Long> championships = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships;
    }

    private static HashSet<Long> loadRacesByEventID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationRacesByEvent + eventID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    public static Event loadEventByID(Connection connection, Long eventID) {
        JTable table = DBManager.executeLoadQuery(connection, queryEventByID + eventID);

        Long   id   = ((BigDecimal) DBManager.getValueAt(table, 0, "EVENT_ID")).longValue();
        String name = (String) DBManager.getValueAt(table, 0, "NAME");

        HashSet<Long> races = loadRacesByEventID(connection, eventID);
        HashSet<Long> championships = loadChampionshipsByEventID(connection, eventID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Event", eventID);

        Event event = Event.createEvent(
                id,
                name,
                admins,
                races,
                championships
        );

        event.setStartDate((TIMESTAMP) DBManager.getValueAt(table, 0, "START_TIME"));
        event.setEndDate((TIMESTAMP) DBManager.getValueAt(table, 0, "END_TIME"));

        return event;
    }


    private static Long deleteEventAdmins(Connection connection, Long eventID) {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventAdminRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteEventRaces(Connection connection, Long eventID) {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventRaceRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    private static Long deleteEventChampionships(Connection connection, Long eventID) {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventChampionshipRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    public static Long saveOrUpdateEvent (Connection connection, Event event) {
        Long eventID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> raceMap = new HashMap<>();
        Map<String, String> championshipMap = new HashMap<>();


        valuesMap.put("name"      , event.getName());
        valuesMap.put("start_date", event.getStartDate().toString());
        valuesMap.put("end_date"  , event.getEndDate().toString());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (event.getID() != null) {
            valuesMap.put("id", event.getID().toString());
            String finalQuery = sub.replace(queryUpdateEvent);
            DBManager.executeSetQuery(connection, finalQuery);

            eventID = event.getID();

            deleteEventAdmins(connection, eventID);
            deleteEventRaces(connection, eventID);
            deleteEventChampionships(connection, eventID);
        }
        //INSERT
        else {
            String finalQuery = sub.replace(queryInsertEvent);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestEventByName);
            JTable table   = executeLoadQuery(connection, idQuery);
            eventID = Long.valueOf(table.getValueAt(0,0).toString());
        }

        //Save admins
        List<Long> admins = event.getAdmins();
        adminMap.put("stage_id", eventID.toString());

        for (Long admin : admins) {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save races
        List<Long> races = event.getAdmins();
        adminMap.put("event_id", eventID.toString());

        for (Long race : races) {
            adminMap.put("race_id", race.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventRaces);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save championships
        List<Long> champs = event.getAdmins();
        adminMap.put("event_id", eventID.toString());

        for (Long champ : champs) {
            adminMap.put("championship_id", champ.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventChampionships);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }


        return eventID;
    }

    public static Long deleteEvent (Connection connection, Long eventID) {
        String query = queryDeleteEventByID + eventID.toString();
        Long state = DBManager.executeSetQuery(connection, query);

        deleteEventAdmins(connection, eventID);
        deleteEventChampionships(connection, eventID);
        deleteEventRaces(connection, eventID);

        return new Long(-1);
    }


    private static String queryEventByID =
            "select * from EVENT where EVENT_ID=";
    private static String queryAdminsByStageType =
            "select * from ADMINS where STAGE_TYPE=";
    private static String queryRelationRacesByEvent =
            "select * from REL_EVENT_RACE where EVENT_ID=";
    private static String queryRelationChampionshipsByEvent =
            "select * from REL_CHAMPIONSHIP_EVENT where EVENT_ID=";

    private static String queryInsertEvent =
            "INSERT INTO EVENT (NAME, START_TIME, END_TIME) VALUES (" +
            "'${name}'," +
            "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
            "END_TIME=TO_TIMESTAMP('${end_date}', 'YYYY-MM-DD HH24:MI:SS.FF'))";
    private static String queryUpdateEvent =
            "UPDATE EVENT SET                                               " +
                "NAME='${name}',                                                       " +
                "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF'), " +
                "END_TIME=TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    " +
                "WHERE EVENT_ID=${id}";
    private static String queryLatestEventByName =
            "SELECT (EVENT_ID) FROM EVENT WHERE NAME='${name}' ORDER BY EVENT_ID DESC";
    private static String queryInsertEventAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                    "'${user_id}'                                              ," +
                    "'Event'                                            ," +
                    "'${stage_id}'                                             )";
    private static String queryInsertEventRaces =
            "INSERT INTO REL_EVENT_RACE (EVENT_ID, RACE_ID) VALUES (" +
                    "'${event_id}'," +
                    "'${race_id}')";
    private static String queryInsertEventChampionships =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (EVENT_ID, CHAMPIONSHIP_ID) VALUES (" +
                    "'${event_id}'," +
                    "'${championship_id}')";
    private static String queryDeleteEventAdminRelation =
            "DELETE FROM ADMINS WHERE STAGE_ID={event_id} AND STAGE_TYPE='Event'";
    private static String queryDeleteEventRaceRelation =
            "DELETE FROM REL_EVENT_RACE WHERE EVENT_ID=${event_id}";
    private static String queryDeleteEventChampionshipRelation =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE EVENT_ID=${event_id}";
    private static String queryDeleteEventByID =
            "DELETE FROM EVENT WHERE EVENT_ID=";
}
