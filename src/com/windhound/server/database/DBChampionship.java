package com.windhound.server.database;

import com.windhound.server.race.Championship;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBManager.executeSetQuery;
import static com.windhound.server.database.DBManager.getValueAt;
import static com.windhound.server.database.DBEvent.loadAdminsByEventTypeAndID;

public class DBChampionship {

    public static Championship loadChampionshipByID(Connection connection, Long championshipID) {
        JTable table = executeLoadQuery(connection, queryChampionshipByID + championshipID);

        Long   id   = ((BigDecimal)getValueAt(table, 0, "CHAMPIONSHIP_ID")).longValue();
        String name = (String)getValueAt(table, 0, "NAME");

        HashSet<Long> events = loadEventsByChampionshipID(connection, championshipID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Championship", championshipID);

        Championship championship = Championship.createChampionship(
                id,
                name,
                admins,
                events
        );

        championship.setStartDate((oracle.sql.TIMESTAMP) getValueAt(table, 0, "START_TIME"));
        championship.setEndDate((oracle.sql.TIMESTAMP) getValueAt(table, 0, "END_TIME"));

        return championship;
    }

    private static HashSet<Long> loadEventsByChampionshipID(Connection connection, Long championshipID) {
        JTable table = executeLoadQuery(connection, queryRelationEventsByChampionship + championshipID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal)getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    private static Long deleteChampionshipAdmins(Connection connection, Long championshipID) {
        Map<String, String> adminMap = new HashMap<>();

        adminMap.put("stage_id", championshipID.toString());
        StrSubstitutor adminSub = new StrSubstitutor(adminMap);
        String finalAdminQuery = adminSub.replace(queryDeleteChampionshipAdmins);

        Long state = DBManager.executeSetQuery(connection, finalAdminQuery);

        return state;
    }

    private static Long deleteChampionshipEvents(Connection connection, Long championshipID) {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("championship_id", championshipID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteChampionshipEventsRelation);

        Long state = DBManager.executeSetQuery(connection, finalEventQuery);

        return state;
    }

    public static Long saveOrUpdateChampionship (Connection connection, Championship championship) {
        Long championshipID = null;

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> adminMap = new HashMap<>();
        Map<String, String> eventMap = new HashMap<>();

        valuesMap.put("name"      , championship.getName());
        valuesMap.put("start_date", championship.getStartDate().toString());
        valuesMap.put("end_date"  , championship.getEndDate().toString());

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (championship.getID() != null) {
            valuesMap.put("id", championship.getID().toString());
            String finalQuery = sub.replace(queryUpdateChampionship);
            DBManager.executeSetQuery(connection, finalQuery);

            championshipID = championship.getID();

            deleteChampionshipAdmins(connection, championshipID);
            deleteChampionshipEvents(connection, championshipID);

        }
        //INSERT
        else {
            String finalQuery = sub.replace(queryInsertChampionship);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestChampionshipByName);
            JTable table   = executeLoadQuery(connection, idQuery);
            championshipID = Long.valueOf(table.getValueAt(0,0).toString());

        }

        //Save admins
        List<Long> admins = championship.getAdmins();
        adminMap.put("stage_id", championshipID.toString());

        for (Long admin : admins) {
            adminMap.put("user_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertChampionshipAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }

        //Save events
        List<Long> events = championship.getSubordinates();
        eventMap.put("championship_id", championshipID.toString());

        for (Long event : events) {
            eventMap.put("event_id", event.toString());
            StrSubstitutor eventSub = new StrSubstitutor(eventMap);
            String finalEventQuery = eventSub.replace(queryInsertChampionshipEventsRelation);

            DBManager.executeSetQuery(connection, finalEventQuery);
        }



        return championshipID;
    }

    public static Long deleteChampionship (Connection connection, Long championshipID) {
        //Delete CHAMPIONSHIP entry
        String query = queryDeleteChampionshipByID + championshipID.toString();
        Long state = DBManager.executeSetQuery(connection, query);

        //Delete relations
        deleteChampionshipEvents(connection, championshipID);
        deleteChampionshipAdmins(connection, championshipID);

        return state;
    }



    private static String queryChampionshipByID =
            "select * from CHAMPIONSHIP where CHAMPIONSHIP_ID=";
    private static String queryRelationEventsByChampionship =
            "select * from REL_CHAMPIONSHIP_EVENT where CHAMPIONSHIP_ID=";
    private static String queryInsertChampionship =
            "INSERT INTO CHAMPIONSHIP (NAME, START_TIME, END_TIME) VALUES (" +
                    "'${name}'                                                    ," +
                    "TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')    ," +
                    "TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    )";
    private static String queryUpdateChampionship =
            "UPDATE CHAMPIONSHIP SET                                               " +
                    "NAME='${name}',                                                       " +
                    "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF'), " +
                    "END_TIME=TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    " +
                    "WHERE CHAMPIONSHIP_ID=${id}                                           ";
    private static String queryInsertChampionshipAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                    "'${user_id}'                                              ," +
                    "'Championship'                                            ," +
                    "'${stage_id}'                                             )";
    private static String queryDeleteChampionshipAdmins =
            "DELETE FROM ADMINS WHERE STAGE_ID=${stage_id} AND STAGE_TYPE='Championship'";
    private static String queryDeleteChampionshipEventsRelation =
            "DELETE FROM REL_CHAMPIONSHIP_EVENT WHERE CHAMPIONSHIP_ID=${championship_id}";
    private static String queryLatestChampionshipByName =
            "SELECT (CHAMPIONSHIP_ID) FROM CHAMPIONSHIP WHERE NAME='${name}' ORDER BY CHAMPIONSHIP_ID DESC";
    private static String queryInsertChampionshipEventsRelation =
            "INSERT INTO REL_CHAMPIONSHIP_EVENT (CHAMPIONSHIP_ID, EVENT_ID) VALUES (" +
                    "'${championship_id}'                                                  ," +
                    "'${event_id}'                                                         )";
    private static String queryDeleteChampionshipByID =
            "DELETE FROM CHAMPIONSHIP WHERE CHAMPIONSHIP_ID=";
}
