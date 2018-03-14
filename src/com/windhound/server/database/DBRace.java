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
import static com.windhound.server.database.DBAdmin.loadAdminsByEventTypeAndID;
import static com.windhound.server.database.DBAdmin.addRaceAdmins;
import static com.windhound.server.database.DBAdmin.deleteAllRaceAdmins;
import static com.windhound.server.database.DBRelation.addRaceEventRelations;
import static com.windhound.server.database.DBRelation.deleteAllRaceEventRelations;
import static com.windhound.server.database.DBRelation.addRaceBoatRelations;
import static com.windhound.server.database.DBRelation.deleteAllRaceBoatRelations;

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
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Race", raceID);

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

        DateFormat dateFormat  = new SimpleDateFormat("YYYY-MM-DD kk:mm:ss.SSSSSS");
        String startDateString = dateFormat.format(race.getStartDate().getTime());
        String endDateString   = dateFormat.format(race.getEndDate().getTime());

        valuesMap.put("name", race.getName());
        valuesMap.put("start_time", startDateString);
        valuesMap.put("end_time", endDateString);

        // TODO move "sub" after valuesMap.put(id)
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (race.getID() != null)
        {
            valuesMap.put("id", race.getID().toString());
            String finalQuery = sub.replace(queryUpdateRace);
            DBManager.executeSetQuery(connection, finalQuery);

            raceID = race.getID();

            deleteAllRaceAdmins(connection, raceID);
            deleteAllRaceEventRelations(connection, raceID);
            deleteAllRaceBoatRelations(connection, raceID);
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
        Long[] adminIDs = race.getAdmins().toArray(new Long[0]);
        addRaceAdmins(connection, raceID, adminIDs);

        //Save events
        Long[] eventIDs = race.getManagers().toArray(new Long[0]);
        addRaceEventRelations(connection, raceID, eventIDs);

        //Save boats
        Long[] boatIDs = race.getSubordinates().toArray(new Long[0]);
        addRaceBoatRelations(connection, raceID, boatIDs);

        return raceID;
    }

    public static void deleteRace(Connection connection, Long raceID)
    {
        String query = queryDeleteRaceByID + raceID.toString();
        DBManager.executeSetQuery(connection, query);

        deleteAllRaceAdmins(connection, raceID);
        deleteAllRaceEventRelations(connection, raceID);
        deleteAllRaceBoatRelations(connection, raceID);
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
                    "END_TIME=TO_TIMESTAMP('${end_time}', 'YYYY-MM-DD HH24:MI:SS.FF')     " +
                    "WHERE Race_ID=${id}                                                  ";
    private static String queryLatestRaceByName =
            "SELECT (RACE_ID) FROM RACE WHERE NAME='${name}' ORDER BY RACE_ID DESC";
    private static String queryDeleteRaceByID =
            "DELETE FROM RACE WHERE RACE_ID=";
}
