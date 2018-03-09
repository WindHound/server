package com.windhound.server.database;

import org.apache.commons.lang.text.StrSubstitutor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DBRelation
{
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
    // Query string
    //
    private static String queryInsertChampionshipEventsRelation =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (CHAMPIONSHIP_ID, EVENT_ID) VALUES (        " +
                    "'${championship_id}'                                                  ," +
                    "'${event_id}'                                                         )";
    private static String queryDeleteAllChampionshipEventsRelations =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE CHAMPIONSHIP_ID=${championship_id}";
}
