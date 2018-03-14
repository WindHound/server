package com.windhound.server.database;

import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DBAdmin
{
    public static HashSet<Long> loadAdminsByEventTypeAndID(Connection connection, String stageType, Long stageID)
    {
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

    //
    // Championship
    //
    public static void addChampionshipAdmins(Connection connection, Long championshipID, Long[] adminIDs)
    {
        Map<String, String> adminMap = new HashMap<>();
        adminMap.put("championship_id", championshipID.toString());

        for (Long admin : adminIDs)
        {
            adminMap.put("admin_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertChampionshipAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllChampionshipAdmins(Connection connection, Long championshipID)
    {
        Map<String, String> adminMap = new HashMap<>();

        adminMap.put("championship_id", championshipID.toString());
        StrSubstitutor adminSub = new StrSubstitutor(adminMap);
        String finalAdminQuery = adminSub.replace(queryDeleteChampionshipAdmins);

        DBManager.executeSetQuery(connection, finalAdminQuery);
    }

    //
    // Event
    //
    public static void addEventAdmins(Connection connection, Long eventID, Long[] adminIDs)
    {
        Map<String, String> adminMap = new HashMap<>();
        adminMap.put("event_id", eventID.toString());

        for (Long admin : adminIDs)
        {
            adminMap.put("admin_id", admin.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertEventAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllEventAdmins(Connection connection, Long eventID)
    {
        Map<String, String> eventMap = new HashMap<>();

        eventMap.put("event_id", eventID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(eventMap);
        String finalEventQuery = eventSub.replace(queryDeleteEventAdminRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Race
    //
    public static void addRaceAdmins(Connection connection, Long raceID, Long[] adminIDs)
    {
        Map<String, String> adminMap = new HashMap<>();
        adminMap.put("race_id", raceID.toString());

        for (Long adminID : adminIDs)
        {
            adminMap.put("admin_id", adminID.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertRaceAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllRaceAdmins(Connection connection, Long raceID)
    {
        Map<String, String> raceMap = new HashMap<>();

        raceMap.put("race_id", raceID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(raceMap);
        String finalEventQuery = eventSub.replace(queryDeleteRaceAdminRelation);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Boat
    //
    public static void addBoatAdmins(Connection connection, Long boatID, Long[] adminIDs)
    {
        Map<String, String> adminMap = new HashMap<>();
        adminMap.put("boat_id", boatID.toString());

        for (Long adminID : adminIDs)
        {
            adminMap.put("admin_id", adminID.toString());
            StrSubstitutor adminSub = new StrSubstitutor(adminMap);
            String finalAdminQuery = adminSub.replace(queryInsertBoatAdmins);

            DBManager.executeSetQuery(connection, finalAdminQuery);
        }
    }

    public static void deleteAllBoatAdmins(Connection connection, Long boatID)
    {
        Map<String, String> map = new HashMap<>();

        map.put("boat_id", boatID.toString());
        StrSubstitutor eventSub = new StrSubstitutor(map);
        String finalEventQuery = eventSub.replace(queryDeleteBoatAdminRelations);

        DBManager.executeSetQuery(connection, finalEventQuery);
    }

    //
    // Query strings
    //
    private static String queryAdminsByStageType =
            "select * from ADMINS where STAGE_TYPE=";
    private static String queryInsertChampionshipAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (        " +
                    "'${admin_id}'                                             ," +
                    "'Championship'                                            ," +
                    "'${championship_id}'                                      )";
    private static String queryDeleteChampionshipAdmins =
            "DELETE FROM ADMINS WHERE STAGE_ID=${championship_id} AND STAGE_TYPE='Championship'";
    private static String queryInsertEventAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                    "'${admin_id}'                                     ," +
                    "'Event'                                           ," +
                    "'${event_id}'                                     )";
    private static String queryDeleteEventAdminRelation =
            "DELETE FROM ADMINS WHERE STAGE_ID=${event_id} AND STAGE_TYPE='Event'";
    private static String queryInsertRaceAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_TYPE, STAGE_ID) VALUES (" +
                    "'${admin_id}'                                     ," +
                    "'Race'                                            ," +
                    "'${race_id}'                                      )";
    private static String queryDeleteRaceAdminRelation =
            "DELETE FROM ADMINS WHERE STAGE_ID=${race_id} AND STAGE_TYPE='Race'";
    private static String queryInsertBoatAdmins =
            "INSERT INTO ADMINS (USER_ID, STAGE_ID, STAGE_TYPE) VALUES (" +
                    "'${admin_id}'                                     ," +
                    "'${boat_id}'                                      ," +
                    "'Boat')                                            ";
    private static String queryDeleteBoatAdminRelations =
            "DELETE FROM ADMINS WHERE STAGE_ID='${boat_id}' AND STAGE_TYPE='Boat'";
}
