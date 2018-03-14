package com.windhound.server.database;

import com.windhound.server.race.Event;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBManager.getValueAt;
import static com.windhound.server.database.DBAdmin.loadAdminsByEventTypeAndID;
import static com.windhound.server.database.DBAdmin.addEventAdmins;
import static com.windhound.server.database.DBAdmin.deleteAllEventAdmins;
import static com.windhound.server.database.DBRelation.addEventRaceRelations;
import static com.windhound.server.database.DBRelation.deleteAllEventRaceRelations;
import static com.windhound.server.database.DBRelation.addEventChampionshipRelations;
import static com.windhound.server.database.DBRelation.deleteAllEventChampionshipRelations;

public class DBEvent
{
    public static Event loadEventByID(Connection connection, Long eventID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryEventByID + eventID);

        Long id = ((BigDecimal) DBManager.getValueAt(table, 0, "EVENT_ID")).longValue();
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

        HashSet<Long> races = loadRacesByEventID(connection, eventID);
        HashSet<Long> championships = loadChampionshipsByEventID(connection, eventID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Event", eventID);

        Event event = new Event(
                id,
                name,
                startDate,
                endDate,
                admins,
                races,
                championships
        );
        return event;
    }

    public static Long saveOrUpdateEvent(Connection connection, Event event)
    {
        Long eventID = null;

        Map<String, String> valuesMap = new HashMap<>();

        DateFormat dateFormat  = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
        String startDateString = dateFormat.format(event.getStartDate().getTime());
        String endDateString   = dateFormat.format(event.getEndDate().getTime());

        valuesMap.put("name", event.getName());
        valuesMap.put("start_date", startDateString);
        valuesMap.put("end_date", endDateString);

        // TODO move "sub" after valuesMap.put(id)
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (event.getID() != null)
        {
            valuesMap.put("id", event.getID().toString());
            String finalQuery = sub.replace(queryUpdateEvent);
            DBManager.executeSetQuery(connection, finalQuery);

            eventID = event.getID();

            deleteAllEventAdmins(connection, eventID);
            deleteAllEventChampionshipRelations(connection, eventID);
            deleteAllEventRaceRelations(connection, eventID);
        }
        //INSERT
        else
        {
            String finalQuery = sub.replace(queryInsertEvent);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestEventByName);
            JTable table = executeLoadQuery(connection, idQuery);
            eventID = Long.valueOf(table.getValueAt(0, 0).toString());
        }

        //Save admins
        Long[] adminIDs = event.getAdmins().toArray(new Long[0]);
        addEventAdmins(connection, eventID, adminIDs);

        //Save championships
        Long[] championshipIDs = event.getManagers().toArray(new Long[0]);
        addEventChampionshipRelations(connection, eventID, championshipIDs);

        //Save races
        Long[] raceIDs = event.getSubordinates().toArray(new Long[0]);
        addEventRaceRelations(connection, eventID, raceIDs);

        return eventID;
    }

    public static void deleteEvent(Connection connection, Long eventID)
    {
        String query = queryDeleteEventByID + eventID.toString();
        DBManager.executeSetQuery(connection, query);

        deleteAllEventAdmins(connection, eventID);
        deleteAllEventChampionshipRelations(connection, eventID);
        deleteAllEventRaceRelations(connection, eventID);
    }

    private static HashSet<Long> loadChampionshipsByEventID(Connection connection, Long eventID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationChampionshipsByEvent + eventID);

        HashSet<Long> championships = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "CHAMPIONSHIP_ID")).longValue();
            championships.add(id);
        }

        return championships;
    }

    private static HashSet<Long> loadRacesByEventID(Connection connection, Long eventID)
    {
        JTable table = DBManager.executeLoadQuery(connection, queryRelationRacesByEvent + eventID);

        HashSet<Long> races = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) DBManager.getValueAt(table, i, "RACE_ID")).longValue();
            races.add(id);
        }

        return races;
    }

    //
    // Query strings
    //
    private static String queryEventByID =
            "select * from EVENT where EVENT_ID=";
    private static String queryRelationRacesByEvent =
            "select * from REL_EVENT_RACE where EVENT_ID=";
    private static String queryRelationChampionshipsByEvent =
            "select * from REL_CHAMPIONSHIP_EVENT where EVENT_ID=";
    private static String queryInsertEvent =
            "INSERT INTO EVENT (NAME, START_TIME, END_TIME) VALUES            (" +
                    "'${name}'                                                ," +
                    "TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
                    "TO_TIMESTAMP('${end_date}', 'YYYY-MM-DD HH24:MI:SS.FF'))  ";
    private static String queryUpdateEvent =
            "UPDATE EVENT SET                                               " +
                    "NAME='${name}',                                                      " +
                    "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')," +
                    "END_TIME=TO_TIMESTAMP('${end_date}', 'YYYY-MM-DD HH24:MI:SS.FF')     " +
                    "WHERE EVENT_ID=${id}                                                 ";
    private static String queryLatestEventByName =
            "SELECT (EVENT_ID) FROM EVENT WHERE NAME='${name}' ORDER BY EVENT_ID DESC";
    private static String queryDeleteEventByID =
            "DELETE FROM EVENT WHERE EVENT_ID=";
}
