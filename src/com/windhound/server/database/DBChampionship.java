package com.windhound.server.database;

import com.windhound.server.race.Championship;
import org.apache.commons.lang.text.StrSubstitutor;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.windhound.server.database.DBManager.executeLoadQuery;
import static com.windhound.server.database.DBManager.getValueAt;
import static com.windhound.server.database.DBAdmin.loadAdminsByEventTypeAndID;
import static com.windhound.server.database.DBRelation.addChampionshipEventRelations;
import static com.windhound.server.database.DBRelation.deleteAllChampionshipEventRelations;
import static com.windhound.server.database.DBAdmin.addChampionshipAdmins;
import static com.windhound.server.database.DBAdmin.deleteAllChampionshipAdmins;

public class DBChampionship
{
    public static Championship loadChampionshipByID(Connection connection, Long championshipID)
    {
        JTable table = executeLoadQuery(connection, queryChampionshipByID + championshipID);

        Long   id   = ((BigDecimal) getValueAt(table, 0, "CHAMPIONSHIP_ID")).longValue();
        String name = (String) getValueAt(table, 0, "NAME");
        String startDateString = getValueAt(table, 0, "START_TIME").toString();
        String endDateString   = getValueAt(table, 0, "END_TIME").toString();

        Instant startDate  = null;
        Instant endDate    = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try
        {
            startDate = Instant.ofEpochMilli(dateFormat.parse(startDateString).getTime());
            endDate   = Instant.ofEpochMilli(dateFormat.parse(endDateString).getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        HashSet<Long> events = loadEventsByChampionshipID(connection, championshipID);
        HashSet<Long> admins = loadAdminsByEventTypeAndID(connection, "Championship", championshipID);

        Championship championship = new Championship(
                id,
                name,
                startDate,
                endDate,
                admins,
                events
        );
        return championship;
    }

    public static Long saveOrUpdateChampionship(Connection connection, Championship championship)
    {
        Long championshipID = null;

        Map<String, String> valuesMap = new HashMap<>();

        LocalDateTime startDatetime = LocalDateTime.ofInstant(championship.getStartDate(), ZoneOffset.UTC);
        LocalDateTime endDatetime   = LocalDateTime.ofInstant(championship.getEndDate(), ZoneOffset.UTC);
        String startDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS").format(startDatetime);
        String endDateString   = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS").format(endDatetime);

        valuesMap.put("name", championship.getName());
        valuesMap.put("start_date", startDateString);
        valuesMap.put("end_date", endDateString);

        // TODO move "sub" after valuesMap.put(id)
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        //UPDATE
        if (championship.getID() != null)
        {
            valuesMap.put("id", championship.getID().toString());
            String finalQuery = sub.replace(queryUpdateChampionship);
            DBManager.executeSetQuery(connection, finalQuery);

            championshipID = championship.getID();

            deleteAllChampionshipAdmins(connection, championshipID);
            deleteAllChampionshipEventRelations(connection, championshipID);
        }
        //INSERT
        else
        {
            String finalQuery = sub.replace(queryInsertChampionship);
            DBManager.executeSetQuery(connection, finalQuery);

            String idQuery = sub.replace(queryLatestChampionshipByName);
            JTable table = executeLoadQuery(connection, idQuery);
            championshipID = Long.valueOf(table.getValueAt(0, 0).toString());

        }

        //Save admins
        Long[] adminIDs = championship.getAdmins().toArray(new Long[0]);
        addChampionshipAdmins(connection, championshipID, adminIDs);

        //Save events
        Long[] eventIDs = championship.getSubordinates().toArray(new Long[0]);
        addChampionshipEventRelations(connection, championshipID, eventIDs);

        return championshipID;
    }

    public static void deleteChampionship(Connection connection, Long championshipID)
    {
        //Delete CHAMPIONSHIP entry
        String query = queryDeleteChampionshipByID + championshipID.toString();
        DBManager.executeSetQuery(connection, query);

        //Delete relations
        deleteAllChampionshipEventRelations(connection, championshipID);
        deleteAllChampionshipAdmins(connection, championshipID);
    }

    private static HashSet<Long> loadEventsByChampionshipID(Connection connection, Long championshipID)
    {
        JTable table = executeLoadQuery(connection, queryRelationEventsByChampionship + championshipID);

        HashSet<Long> events = new HashSet<>();

        for (int i = 0; i < table.getRowCount(); ++i)
        {
            Long id = ((BigDecimal) getValueAt(table, i, "EVENT_ID")).longValue();
            events.add(id);
        }

        return events;
    }

    //
    // Query strings
    //
    private static String queryChampionshipByID =
            "select * from CHAMPIONSHIP where CHAMPIONSHIP_ID=";
    private static String queryRelationEventsByChampionship =
            "select * from REL_CHAMPIONSHIP_EVENT where CHAMPIONSHIP_ID=";
    private static String queryInsertChampionship =
            "INSERT INTO CHAMPIONSHIP (NAME, START_TIME, END_TIME) VALUES (        " +
                    "'${name}'                                                    ," +
                    "TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF')    ," +
                    "TO_TIMESTAMP('${end_date}'  , 'YYYY-MM-DD HH24:MI:SS.FF')    )";
    private static String queryUpdateChampionship =
            "UPDATE CHAMPIONSHIP SET                                                       " +
                    "NAME='${name}',                                                       " +
                    "START_TIME=TO_TIMESTAMP('${start_date}', 'YYYY-MM-DD HH24:MI:SS.FF'), " +
                    "END_TIME=TO_TIMESTAMP('${end_date}', 'YYYY-MM-DD HH24:MI:SS.FF')      " +
                    "WHERE CHAMPIONSHIP_ID=${id}                                           ";
    private static String queryLatestChampionshipByName =
            "SELECT (CHAMPIONSHIP_ID) FROM CHAMPIONSHIP WHERE NAME='${name}' ORDER BY CHAMPIONSHIP_ID DESC";
    private static String queryDeleteChampionshipByID =
            "DELETE FROM CHAMPIONSHIP WHERE CHAMPIONSHIP_ID=";
}
