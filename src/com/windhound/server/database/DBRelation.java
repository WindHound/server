package com.windhound.server.database;

import org.apache.commons.lang.text.StrSubstitutor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBRelation
{
    //
    // Championship - Event
    //
    public static void addChampionshipEventRelations(Connection connection, Long championshipID, Long[] eventIDs)
    {
        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("championship_id", championshipID.toString());

        for (Long eventID : eventIDs)
        {
            eventMap.put("event_id", eventID.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalEventQuery = eventSub.replace(queryInsertChampionshipEventsRelation);

            DBManager.executeSetQuery(connection, finalEventQuery);
        }
    }

    public static void deleteAllChampionshipEventRelations(Connection connection, Long championshipID)
    {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("championship_id", championshipID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteAllChampionshipEventsRelations);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Event - Championship
    //
    public static void addEventChampionshipRelations(Connection connection, Long eventID, Long[] championshipIDs)
    {
        Map<String, String> championshipMap = new HashMap<>();
        championshipMap.put("event_id", eventID.toString());

        for (Long championshipID : championshipIDs)
        {
            championshipMap.put("championship_id", championshipID.toString());
            StrSubstitutor adminSub = new StrSubstitutor(championshipMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventChampionships);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllEventChampionshipRelations(Connection connection, Long eventID)
    {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventChampionshipRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Event - Race
    //
    public static void addEventRaceRelations(Connection connection, Long eventID, Long[] raceIDs)
    {
        Map<String, String> raceMap = new HashMap<>();
        raceMap.put("event_id", eventID.toString());

        for (Long raceID : raceIDs)
        {
            raceMap.put("race_id", raceID.toString());
            StrSubstitutor adminSub = new StrSubstitutor(raceMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventRaces);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllEventRaceRelations(Connection connection, Long eventID)
    {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventRaceRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Race - Event
    //
    public static void addRaceEventRelations(Connection connection, Long raceID, Long[] eventIDs)
    {
        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("race_id", raceID.toString());

        for (Long eventID : eventIDs)
        {
            eventMap.put("event_id", eventID.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalAdminQuery = eventSub.replace(queryInsertRaceEvents);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllRaceEventRelations(Connection connection, Long raceID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceEventRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Race - Boat
    //
    public static void addRaceBoatRelations(Connection connection, Long raceID, Long[] boatIDs)
    {
        Map<String, String> boatMap = new HashMap<>();
        boatMap.put("race_id", raceID.toString());

        for (Long boatID : boatIDs)
        {
            boatMap.put("boat_id", boatID.toString());
            StrSubstitutor boatSub = new StrSubstitutor(boatMap);
            String finalAdminQuery = boatSub.replace(queryInsertRaceBoats);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllRaceBoatRelations(Connection connection, Long raceID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteRaceBoatRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Boat - Race
    //
    public static void addBoatRaceRelations(Connection connection, Long boatID, Long[] raceIDs)
    {
        Map<String, String> racesMap = new HashMap<>();
        racesMap.put("boat_id", boatID.toString());

        for (Long raceID : raceIDs)
        {
            racesMap.put("race_id", raceID.toString());
            StrSubstitutor raceSub = new StrSubstitutor(racesMap);
            String finalAdminQuery = raceSub.replace(queryInsertBoatRaces);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllBoatRaceRelations(Connection connection, Long boatID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatRaceRelations);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Boat - Competitor
    //
    public static void addBoatCompetitorRelations(Connection connection, Long boatID, Long[] competitorIDs)
    {
        Map<String, String> competitorsMap = new HashMap<>();
        competitorsMap.put("boat_id", boatID.toString());

        for (Long competitorID : competitorIDs)
        {
            competitorsMap.put("user_id", competitorID.toString());
            StrSubstitutor compSub = new StrSubstitutor(competitorsMap);
            String finalAdminQuery = compSub.replace(queryInsertBoatCompetitors);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllBoatCompetitorRelations(Connection connection, Long boatID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatCompetitorRelations);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Query string
    //
    private static String queryInsertChampionshipEventsRelation =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (CHAMPIONSHIP_ID, EVENT_ID) VALUES (        " +
                    "'${championship_id}'                                                  ," +
                    "'${event_id}'                                                         )";
    private static String queryDeleteAllChampionshipEventsRelations =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE CHAMPIONSHIP_ID=${championship_id}";
    private static String queryInsertEventChampionships =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (EVENT_ID, CHAMPIONSHIP_ID) VALUES (" +
                    "'${event_id}'                                                 ," +
                    "'${championship_id}'                                          )";
    private static String queryDeleteEventChampionshipRelation =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE EVENT_ID=${event_id}";
    private static String queryInsertEventRaces =
            "INSERT INTO REL_EVENT_RACE (EVENT_ID, RACE_ID) VALUES (" +
                    "'${event_id}'                                 ," +
                    "'${race_id}'                                  )";
    private static String queryDeleteEventRaceRelation =
            "DELETE FROM REL_EVENT_RACE WHERE EVENT_ID=${event_id}";
    private static String queryInsertRaceEvents =
            "INSERT INTO REL_EVENT_RACE (EVENT_ID, RACE_ID) VALUES (" +
                    "${event_id}                                   ," +
                    "${race_id})                                    ";
    private static String queryDeleteRaceEventRelation =
            "DELETE FROM REL_EVENT_RACE WHERE RACE_ID=${race_id}";
    private static String queryInsertRaceBoats =
            "INSERT INTO REL_RACE_BOAT (RACE_ID, BOAT_ID) VALUES (" +
                    "${race_id}                                  ," +
                    "${boat_id})                                  ";
    private static String queryDeleteRaceBoatRelation =
            "DELETE FROM REL_RACE_BOAT WHERE RACE_ID=${race_id}";
    private static String queryInsertBoatRaces =
            "INSERT INTO REL_RACE_BOAT (RACE_ID, BOAT_ID) VALUES (" +
                    "'${race_id}'                                ," +
                    "'${boat_id}'                                )";
    private static String queryDeleteBoatRaceRelations =
            "DELETE FROM REL_RACE_BOAT WHERE BOAT_ID='${boat_id}'";
    private static String queryInsertBoatCompetitors =
            "INSERT INTO REL_BOAT_USER (BOAT_ID, USER_ID) VALUES (" +
                    "'${boat_id}'                                ," +
                    "'${user_id}'                                )";
    private static String queryDeleteBoatCompetitorRelations =
            "DELETE FROM REL_BOAT_USER WHERE BOAT_ID='${boat_id}'";
}
